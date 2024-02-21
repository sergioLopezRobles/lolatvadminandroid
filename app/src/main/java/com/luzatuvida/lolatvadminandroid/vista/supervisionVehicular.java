package com.luzatuvida.lolatvadminandroid.vista;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Camara;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.supervision.Controlador;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisionVehicular#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisionVehicular extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout llKilometrajeManana, llKilometrajeTarde, llFotoLadoIzquierdo, llFotoLadoDerecho, llFotoFrente, llFotoAtras, llFotoExtra1, llFotoExtra2, llFotoExtra3,
                  llFotoExtra4, llFotoExtra5, llFotoExtra6;
    ImageView ivKilometrajeManana, ivKilometrajeTarde, ivMotoLadoIzquierdo, ivMotoLadoDerecho, ivMotoFrente, ivMotoAtras, ivMotoFotoExtra1, ivMotoFotoExtra2,
              ivMotoFotoExtra3, ivMotoFotoExtra4, ivMotoFotoExtra5, ivMotoFotoExtra6;

    TextView tvKilometrajeManana, tvKilometrajeTarde, tvMotoLadoIzquierdo, tvMotoLadoDerecho, tvMotoFrente, tvMotoAtras, tvFotosPendientesSupervision;
    Button btnCancelarSupervision, btnCrearSupervision;
    Camara camara;
    Global global;
    Fragment fragmento;
    private Internet conexionInternet;
    Controlador controlador;
    ObtenerRol obtenerRol;
    Runnable runnable, runnable2;
    Toolbar toolbar;
    LinearLayout llBuscador, llBuscadorIcono, llFormatoImagenCompartir;
    public supervisionVehicular() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmento_supervisionvehicular.
     */
    // TODO: Rename and change types and number of parameters
    public static supervisionVehicular newInstance(String param1, String param2) {
        supervisionVehicular fragment = new supervisionVehicular();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        fragmento = supervisionVehicular.this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_supervisionvehicular, container, false);

        obtenerRol = new ObtenerRol(fragmento);

        toolbar = getActivity().findViewById(R.id.toolbar);
        llBuscador = toolbar.findViewById(R.id.llBuscador);
        llBuscadorIcono = toolbar.findViewById(R.id.llBuscadorIcono);

        //Inicialización de Componentes

        //Inicializar clases
        camara = new Camara(fragmento);
        global = new Global(fragmento);
        conexionInternet = new Internet(fragmento.getActivity());

        //LinearLayout
        llKilometrajeManana = view.findViewById(R.id.llKilometrajeManana);
        llKilometrajeTarde = view.findViewById(R.id.llKilometrajeTarde);
        llFotoLadoIzquierdo = view.findViewById(R.id.llFotoLadoIzquierdo);
        llFotoLadoDerecho = view.findViewById(R.id.llFotoLadoDerecho);
        llFotoFrente = view.findViewById(R.id.llFotoFrente);
        llFotoAtras = view.findViewById(R.id.llFotoAtras);
        llFotoExtra1 = view.findViewById(R.id.llFotoExtra1);
        llFotoExtra2 = view.findViewById(R.id.llFotoExtra2);
        llFotoExtra3 = view.findViewById(R.id.llFotoExtra3);
        llFotoExtra4 = view.findViewById(R.id.llFotoExtra4);
        llFotoExtra5 = view.findViewById(R.id.llFotoExtra5);
        llFotoExtra6 = view.findViewById(R.id.llFotoExtra6);
        //ImageViews
        ivKilometrajeManana = view.findViewById(R.id.ivKilometrajeManana);
        ivKilometrajeTarde = view.findViewById(R.id.ivKilometrajeTarde);
        ivMotoLadoIzquierdo = view.findViewById(R.id.ivMotoLadoIzquierdo);
        ivMotoLadoDerecho = view.findViewById(R.id.ivMotoLadoDerecho);
        ivMotoFrente = view.findViewById(R.id.ivMotoFrente);
        ivMotoAtras = view.findViewById(R.id.ivMotoAtras);
        ivMotoFotoExtra1 = view.findViewById(R.id.ivMotoFotoExtra1);
        ivMotoFotoExtra2 = view.findViewById(R.id.ivMotoFotoExtra2);
        ivMotoFotoExtra3 = view.findViewById(R.id.ivMotoFotoExtra3);
        ivMotoFotoExtra4 = view.findViewById(R.id.ivMotoFotoExtra4);
        ivMotoFotoExtra5 = view.findViewById(R.id.ivMotoFotoExtra5);
        ivMotoFotoExtra6 = view.findViewById(R.id.ivMotoFotoExtra6);
        //TextViews
        tvKilometrajeManana = view.findViewById(R.id.tvKilometrajeManana);
        tvKilometrajeTarde = view.findViewById(R.id.tvKilometrajeTarde);
        tvMotoLadoIzquierdo = view.findViewById(R.id.tvMotoLadoIzquierdo);
        tvMotoLadoDerecho = view.findViewById(R.id.tvMotoLadoDerecho);
        tvMotoFrente = view.findViewById(R.id.tvMotoFrente);
        tvMotoAtras = view.findViewById(R.id.tvMotoAtras);
        tvFotosPendientesSupervision = view.findViewById(R.id.tvFotosPendientesSupervision);

        //Botones
        btnCancelarSupervision = view.findViewById(R.id.btnCancelarSupervision);
        btnCrearSupervision = view.findViewById(R.id.btnCrearSupervision);

        //Ocultar buscador y ocultar menuHamburguesa
        String nombreUsuarioLogeado = obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO");

        llBuscadorIcono.setVisibility(View.GONE);
        llBuscador.setVisibility(View.GONE);
        ((MainActivity)fragmento.getActivity()).setTitle(nombreUsuarioLogeado);
        ((MainActivity)fragmento.getActivity()).hideMenuHamburguesa();

        Object[] objetos = {fragmento, llKilometrajeManana, llKilometrajeTarde, llFotoLadoIzquierdo, llFotoLadoDerecho, llFotoFrente, llFotoAtras, llFotoExtra1, llFotoExtra2, llFotoExtra3,
                            llFotoExtra4, llFotoExtra5, llFotoExtra6, ivKilometrajeManana, ivKilometrajeTarde, ivMotoLadoIzquierdo, ivMotoLadoDerecho, ivMotoFrente, ivMotoAtras, ivMotoFotoExtra1,
                            ivMotoFotoExtra2, ivMotoFotoExtra3, ivMotoFotoExtra4, ivMotoFotoExtra5, ivMotoFotoExtra6,tvKilometrajeManana, tvKilometrajeTarde, tvMotoLadoIzquierdo,
                            tvMotoLadoDerecho, tvMotoFrente, tvMotoAtras, btnCrearSupervision};

        controlador = new Controlador(objetos);

        controlador.verificarsupervisionvehiculo();

        //Verificar si las imagenes ya se subieron del contrato (Cambiar tvTextoContrato)
        Handler handler3 = new Handler();
        runnable = new Runnable() {
            public void run() {
                tvFotosPendientesSupervision.setText("Imagenes pendientes (" + controlador.obtenerNumeroImagenesSupervisionNoSubidasFTP() + ")");
                handler3.postDelayed(this, 2000);
            }
        };
        runnable.run();

        if(global.obtenerAtributoTabla("GLOBAL", "SUBIRIMAGENESSUPERVISIONFTP", "ID", "1").equals("0")) {
            global.actualizarAtributoTabla("GLOBAL", "SUBIRIMAGENESSUPERVISIONFTP", "1", "ID", "1");
            Handler handler2 = new Handler();
            runnable2 = new Runnable() {
                public void run() {
                    if(global.obtenerAtributoTabla("GLOBAL", "SUBIRIMAGENESSUPERVISIONFTP", "ID", "1").equals("0")) {
                        Log.i("MENSAJE", "Se detuvo el runnable");
                        handler2.removeCallbacks(runnable2);
                    }else {
                        if (conexionInternet.verificarConexionInternet()) {
                            controlador.subirImagenesAFTPSupervisionVehicular();
                            Log.i("MENSAJE", "Entro a nuevo runnable");
                        }
                        handler2.postDelayed(this, 30000);
                    }
                }
            };
            runnable2.run();
        }

        //Evento clic para tomar fotografia
        ivKilometrajeManana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivKilometrajeManana, 8);
                        controlador.tomoFotoKilometraje1 = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivKilometrajeManana, 8);
                    controlador.tomoFotoKilometraje1 = "1";
                }
            }
        });

        ivKilometrajeTarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivKilometrajeTarde, 8);
                        controlador.tomoFotoKilometraje2 = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivKilometrajeTarde, 8);
                    controlador.tomoFotoKilometraje2 = "1";
                }
            }
        });

        ivMotoLadoIzquierdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivMotoLadoIzquierdo, 8);
                        controlador.tomoFotoLadoIzquierdo = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMotoLadoIzquierdo, 8);
                    controlador.tomoFotoLadoIzquierdo = "1";
                }
            }
        });

        ivMotoLadoDerecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivMotoLadoDerecho, 8);
                        controlador.tomoFotoLadoDerecho = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMotoLadoDerecho, 8);
                    controlador.tomoFotoLadoDerecho = "1";
                }
            }
        });

        ivMotoFrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivMotoFrente, 8);
                        controlador.tomoFotoFrente = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMotoFrente, 8);
                    controlador.tomoFotoFrente = "1";
                }
            }
        });

        ivMotoAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivMotoAtras, 8);
                        controlador.tomoFotoAtras = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMotoAtras, 8);
                    controlador.tomoFotoAtras = "1";
                }
            }
        });

        ivMotoFotoExtra1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivMotoFotoExtra1, 8);
                        controlador.tomoFotoExtra1 = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMotoFotoExtra1, 8);
                    controlador.tomoFotoExtra1 = "1";
                }
            }
        });

        ivMotoFotoExtra2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivMotoFotoExtra2, 8);
                        controlador.tomoFotoExtra2 = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMotoFotoExtra2, 8);
                    controlador.tomoFotoExtra2 = "1";
                }
            }
        });

        ivMotoFotoExtra3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivMotoFotoExtra3, 8);
                        controlador.tomoFotoExtra3 = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMotoFotoExtra3, 8);
                    controlador.tomoFotoExtra3 = "1";
                }
            }
        });

        ivMotoFotoExtra4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivMotoFotoExtra4, 8);
                        controlador.tomoFotoExtra4 = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMotoFotoExtra4, 8);
                    controlador.tomoFotoExtra4 = "1";
                }
            }
        });

        ivMotoFotoExtra5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivMotoFotoExtra5, 8);
                        controlador.tomoFotoExtra5 = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMotoFotoExtra5, 8);
                    controlador.tomoFotoExtra5 = "1";
                }
            }
        });

        ivMotoFotoExtra6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivMotoFotoExtra6, 8);
                        controlador.tomoFotoExtra6 = "1";
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMotoFotoExtra6, 8);
                    controlador.tomoFotoExtra6 = "1";
                }
            }
        });

        btnCrearSupervision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvFotosPendientesSupervision.getText().equals("Imagenes pendientes (0)")){
                    controlador.verificarFotosSupervision();
                    //controlador.crearActualizarSupervisionVehicular();
                }else{
                    Toast.makeText(fragmento.getActivity(), "No puedes generar esta acción hasta que existan 0 imagenes pendientes", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancelarSupervision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global = new Global(fragmento);
                String descargaCompleta = global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1");
                if(descargaCompleta.contains("1")) {
                    //Descarga de datos fue insertada correctamente
                    Fragment verificadorFragment = new principal(false);
                    FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, verificadorFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else if(descargaCompleta.contains("4")){
                    //EN ESPERA
                    Toast.makeText(fragmento.getActivity(), "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                }else {
                    //Descarga de datos incompleta o no alcanzo a responder el webservice
                    Toast.makeText(fragmento.getActivity(), "Para continuar, es necesario sincronizar...", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    //Metodo que se utiliza para escuchar la respuesta de las funciones de las fotos
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camara.onActivityResult(requestCode, resultCode, data);
    }

    //En caso de que el usuario acepte los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}