package com.luzatuvida.lolatvadminandroid.vista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.buzon.Controlador;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link buzon#newInstance} factory method to
 * create an instance of this fragment.
 */
public class buzon extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Controlador controlador;
    EditText etNuevaQuejaSugerencia;
    TextView tvCaracteresDisponibles;
    Button btnCancelarNuevaQuejaSugerencia, btnGuardarNuevaQuejaSugerencia;

    Global global;
    Fragment fragmento;
    ObtenerRol obtenerRol;
    Toolbar toolbar;
    LinearLayout llBuscador, llBuscadorIcono;

    public buzon() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment buzon.
     */
    // TODO: Rename and change types and number of parameters
    public static buzon newInstance(String param1, String param2) {
        buzon fragment = new buzon();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Throwable identificador error 8
        */

        fragmento = buzon.this;
        global = new Global(fragmento);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                global.escribirErrorClasePrincipalThrowable(throwable, 8);
                System.exit(2);
            }
        });

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_buzon, container, false);

        toolbar = getActivity().findViewById(R.id.toolbar);
        llBuscador = toolbar.findViewById(R.id.llBuscador);
        llBuscadorIcono = toolbar.findViewById(R.id.llBuscadorIcono);

        obtenerRol = new ObtenerRol(fragmento);
        String nombreUsuarioLogeado = obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO");

        //Ocultar buscador y ocultar menuHamburguesa
        llBuscadorIcono.setVisibility(View.GONE);
        llBuscador.setVisibility(View.GONE);
        ((MainActivity)fragmento.getActivity()).setTitle(nombreUsuarioLogeado);
        ((MainActivity)fragmento.getActivity()).hideMenuHamburguesa();

        etNuevaQuejaSugerencia = view.findViewById(R.id.etNuevaQuejaSugerencia);
        tvCaracteresDisponibles = view.findViewById(R.id.tvCaracteresDisponibles);
        btnCancelarNuevaQuejaSugerencia = view.findViewById(R.id.btnCancelarNuevaQuejaSugerencia);
        btnGuardarNuevaQuejaSugerencia = view.findViewById(R.id.btnGuardarNuevaQuejaSugerencia);

        Object[] objetos = {etNuevaQuejaSugerencia, tvCaracteresDisponibles, btnGuardarNuevaQuejaSugerencia};
        controlador = new Controlador(fragmento, objetos);


        //Evento de teclado para contar caracteres disponibles
        etNuevaQuejaSugerencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                controlador.mostrarCaracteresDisponibles();
            }
        });

        btnGuardarNuevaQuejaSugerencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Almacenamos neuvo mensaje de queja/sugerencia
               if(controlador.guardarQuejaSugerenciaBD()){
                   //Si se almacena un mensaje correctamente

                   //Retornamos a ventana principal para sincronizacion en automatico
                   Fragment verificadorFragment;
                   FragmentTransaction transaction;
                   verificadorFragment = new principal(false);
                   transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                   transaction.replace(R.id.content, verificadorFragment);
                   transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                   transaction.addToBackStack(null);
                   transaction.commit();
               }
            }
        });

        btnCancelarNuevaQuejaSugerencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNuevaQuejaSugerencia.setText(""); // Limpiamos el campo de mensaje
                Toast.makeText(fragmento.getActivity(),"Mensaje cancelado",Toast.LENGTH_LONG).show();   //Imprimimos mensaje de cancelacion
                //Retornamos a ventana principal
                Fragment verificadorFragment;
                FragmentTransaction transaction;
                verificadorFragment = new principal(false);
                transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, verificadorFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;

    }


}