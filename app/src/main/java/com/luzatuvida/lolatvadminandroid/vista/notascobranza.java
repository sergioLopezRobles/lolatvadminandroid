package com.luzatuvida.lolatvadminandroid.vista;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.notascobranza.Controlador;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notascobranza#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notascobranza extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TableLayout tblNotasCobranza;
    Button btnCancelarNotasCobranza, btnNuevaNotaCobranza;
    Controlador controlador;
    Fragment fragmento;



    public notascobranza() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notascobranza.
     */
    // TODO: Rename and change types and number of parameters
    public static notascobranza newInstance(String param1, String param2) {
        notascobranza fragment = new notascobranza();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmento = notascobranza.this;

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notascobranza, container, false);

        tblNotasCobranza = view.findViewById(R.id.tblNotasCobranza);
        btnNuevaNotaCobranza = view.findViewById(R.id.btnNuevaNotaCobranza);
        btnCancelarNotasCobranza = view.findViewById(R.id.btnCancelarNotasCobranza);

        Object[] objetos = {tblNotasCobranza};

        controlador = new Controlador(fragmento, objetos);

        controlador.cargarListaNotasCobranza();

        btnNuevaNotaCobranza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText etNotasCobranza = new EditText(fragmento.getActivity());
                etNotasCobranza.setHint("DESCRIPCIÓN");
                etNotasCobranza.setHeight(200);
                LinearLayout.LayoutParams layoutReferencia = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                etNotasCobranza.setLayoutParams(layoutReferencia);

                android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(fragmento.getActivity());
                alerta.setTitle("Notas cobranza")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!etNotasCobranza.getText().toString().trim().equals("")){
                                    //Guardar nota en BD
                                    controlador.guardarNuevaNotaCobranza(etNotasCobranza.getText().toString());
                                    //Carga de nuevo lista de notas
                                    controlador.cargarListaNotasCobranza();
                                }else {
                                    Toast.makeText(fragmento.getContext(), "Ingresa una descripción valida para la nueva nota.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setView(etNotasCobranza);

                final android.app.AlertDialog dialog = alerta.create();
                dialog.show();
            }
        });

        btnCancelarNotasCobranza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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