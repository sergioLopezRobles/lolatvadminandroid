package com.luzatuvida.lolatvadminandroid.vista;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.configuracion.Controlador;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Seguridad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link configuracion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class configuracion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Toolbar toolbar;
    LinearLayout llBuscador, llBuscadorIcono, llFormatoImagenCompartir;
    ObtenerRol obtenerRol;
    Controlador controlador;
    TextView tvNombreImpresora, btnSeleccionarImpresora, btnImprimirCorte, tvDatosAImprimir, btnDescargarMapa, tvDatosCorte, tvFechaImagenCorte, btnCompartirCorte, tvNombreCobrador;
    Seguridad seguridad;
    Global global;

    Llaves llaves;

    Fragment fragmento;

    public configuracion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment configuracion.
     */
    // TODO: Rename and change types and number of parameters
    public static configuracion newInstance(String param1, String param2) {
        configuracion fragment = new configuracion();
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
        Identificador error del 235 al 236, 259

        Throwable identificador error 1
        */

        fragmento = configuracion.this;
        global = new Global(fragmento);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                global.escribirErrorClasePrincipalThrowable(throwable, 1);
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
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);

        toolbar = getActivity().findViewById(R.id.toolbar);
        llBuscador = toolbar.findViewById(R.id.llBuscador);
        llBuscadorIcono = toolbar.findViewById(R.id.llBuscadorIcono);

        tvNombreImpresora = view.findViewById(R.id.tvNombreImpresora);
        btnSeleccionarImpresora = view.findViewById(R.id.btnSeleccionarImpresora);
        btnImprimirCorte = view.findViewById(R.id.btnImprimirCorte);
        btnDescargarMapa = view.findViewById(R.id.btnDescargarMapa);
        tvDatosAImprimir = view.findViewById(R.id.tvDatosAImprimir);

        //Compartir corte
        btnCompartirCorte = view.findViewById(R.id.btnCompartirCorte);
        llFormatoImagenCompartir = view.findViewById(R.id.llFormatoImagenCompartir);
        tvDatosCorte = view.findViewById(R.id.tvDatosCorte);
        tvFechaImagenCorte = view.findViewById(R.id.tvFechaImagenCorte);
        tvNombreCobrador = view.findViewById(R.id.tvNombreCobrador);

        obtenerRol = new ObtenerRol(fragmento);
        seguridad = new Seguridad(fragmento);
        llaves = new Llaves();

        String rol = obtenerRol.obtenerAtributoUsuarioLogeado("ROL");
        String nombreUsuarioLogeado = obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO");

        //Ocultar buscador y ocultar menuHamburguesa
        llBuscadorIcono.setVisibility(View.GONE);
        llBuscador.setVisibility(View.GONE);
        ((MainActivity)fragmento.getActivity()).setTitle(nombreUsuarioLogeado);
        ((MainActivity)fragmento.getActivity()).hideMenuHamburguesa();

        Object[] objetos = {rol, nombreUsuarioLogeado, tvNombreImpresora, tvDatosAImprimir, llFormatoImagenCompartir, tvDatosCorte, tvFechaImagenCorte, tvNombreCobrador};
        controlador = new Controlador(fragmento, objetos);

        if(llaves.impresora_termica){ // Se esta utilizando la impresora???
            controlador.verificarSiBluetoothFunciona(); //En caso de no funcionar lo mandara a la lista de contratos
        }

        tvDatosAImprimir.setText(controlador.obtenerCadenaDatosAImprimir());
        tvDatosCorte.setText(controlador.prepararDatosCompartirCorte(tvDatosAImprimir.getText().toString())); //Cargamos los datos a imprimir al formato de imagen corte

        try {
            if(seguridad.leerSharedPreferences("valorImpresoraSeleccionada").length() > 0) {
                tvNombreImpresora.setText(seguridad.leerSharedPreferences("valorImpresoraSeleccionada"));
                tvNombreImpresora.setTextColor(Color.parseColor("#0AA09E"));
            }
        } catch (Exception e) {
            global.escribirError(e, 235);
            e.printStackTrace();
        }

        btnSeleccionarImpresora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llaves.impresora_termica){
                    if(controlador.tienePermisos()){
                        controlador.mostrarAlertDialogSeleccionarImpresora();
                    }
                }
            }
        });

        btnImprimirCorte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnImprimirCorte.setVisibility(View.INVISIBLE);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            global.escribirError(e, 236);
                            Log.i("ERROR", e.toString());
                        }

                        fragmento.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnImprimirCorte.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
                controlador.mandarAImprimirCorte();
            }
        });

        //Evento clic para Descargar Mapa Google Maps
        btnDescargarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.mandarADescargarMapa();
            }
        });

        //Evento clic para boton compartir corte
        btnCompartirCorte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.mostrarAlertaCompartirCorte();

            }
        });

        return view;
    }
}