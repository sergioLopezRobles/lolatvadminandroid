package com.luzatuvida.lolatvadminandroid.vista;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.luzatuvida.lolatvadminandroid.BuildConfig;
import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.global.Teclado;
import com.luzatuvida.lolatvadminandroid.inicioSesion.Controlador;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link inicioSesion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inicioSesion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters 2
    private String mParam1;
    private String mParam2;

    //Declaración de componentes
    Button btnIniciar;
    EditText etCorreo, etContrasena;
    TextView tvDescargarAplicacion, tvCerrarUltimaSesion;

    //Declaración de clases
    Controlador controlador;
    Teclado teclado;

    Fragment fragmento;
    Toolbar toolbar;
    TextView tvPorcentajeProgreso;
    LinearLayout llProgress;

    Global global;
    TextView tvVersion, tvFechaSesion;

    ImageView img_Logo;

    public inicioSesion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inicioSesion.
     */
    // TODO: Rename and change types and number of parameters
    public static inicioSesion newInstance(String param1, String param2) {
        inicioSesion fragment = new inicioSesion();
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
        Throwable identificador error 2
        */

        fragmento = inicioSesion.this;
        global = new Global(fragmento);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                global.escribirErrorClasePrincipalThrowable(throwable, 2);
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

        View view = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);

        //Inicialización de Componentes
        btnIniciar = view.findViewById(R.id.btnIniciar);
        etCorreo = view.findViewById(R.id.etCorreo);
        etContrasena = view.findViewById(R.id.etContrasena);
        tvDescargarAplicacion = view.findViewById(R.id.tvDescargarAplicacion);
        tvCerrarUltimaSesion = view.findViewById(R.id.tvCerrarUltimaSesion);
        img_Logo = view.findViewById(R.id.img_Logo);
        tvVersion = view.findViewById(R.id.tvVersion);
        tvFechaSesion = view.findViewById(R.id.tvFechaSesion);

        //Inicialización de Clases
        toolbar = getActivity().findViewById(R.id.toolbar);
        tvPorcentajeProgreso = toolbar.findViewById(R.id.tvPorcentajeProgreso);
        llProgress = toolbar.findViewById(R.id.llProgress);

        Object[] objetos = {fragmento, etCorreo, etContrasena, llProgress, tvPorcentajeProgreso, btnIniciar, tvDescargarAplicacion, tvCerrarUltimaSesion};
        controlador = new Controlador(objetos);
        teclado = new Teclado(fragmento);

        //Quitar menu
        ((MainActivity) fragmento.getActivity()).lockDrawer();

        //Llenar campo correo
        controlador.llenarCampoUsuario();

        //Asignar Version y Fecha
        tvVersion = view.findViewById(R.id.tvVersion);
        tvVersion.setText("V " + BuildConfig.VERSION_NAME);

        //Hacer uso del metodo obtenerFechaSesion y pintar fecha de sesion en el tv de inicio de sesion
        tvFechaSesion = view.findViewById(R.id.tvFechaSesion);
        tvFechaSesion.setText(global.obtenerFechaSesion());

        //Configuracion activa para logotipo
        global.verificarConfiguracionLogoIconos(img_Logo, 0);

        //Configuracion activa para Encabezados
        global.verificarConfiguracionEncabezados(tvVersion);
        global.verificarConfiguracionEncabezados(tvFechaSesion);

        //Configuracion activa para barra de herramientas
        global.verificarConfiguracionNavBar(toolbar);

            etCorreo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etContrasena.requestFocus();
                    return true;
                }

                return false;
            }
        });

        etContrasena.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnIniciar.setVisibility(View.INVISIBLE);
                etCorreo.setFocusable(false);
                etContrasena.setFocusable(false);
                controlador.validarInicioSesion();
            }
        });

        tvDescargarAplicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.descargarAplicacion();
            }
        });

        tvCerrarUltimaSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.cerrarUltimaSesion();
            }
        });

        return view;
    }

}