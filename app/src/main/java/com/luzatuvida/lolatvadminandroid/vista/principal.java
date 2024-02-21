package com.luzatuvida.lolatvadminandroid.vista;

import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.luzatuvida.lolatvadminandroid.BuildConfig;
import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.contratosPrincipal.Controlador;
import com.luzatuvida.lolatvadminandroid.contratosPrincipal.FilaContratos;
import com.luzatuvida.lolatvadminandroid.global.Camara;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Teclado;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link principal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class principal extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Declaración de componentes
    FloatingTextButton fab;
    ListView lvListaContratos;

    //Declaración de clases
    Controlador controlador;
    List<FilaContratos> filaContratos;

    ObtenerRol obtenerRol;

    Toolbar toolbar;
    EditText etBuscador;
    LinearLayout llBuscador, llBuscadorIcono, llAvisoImagenesFTP, llProgress, llConfiguracion, llTopVentas, llSupervision, llAlmacenamientoImagenesContrato, llNotasCobranza,
                 llTimerImagenesFTP, llRegistroSalida;
    TextView tvEstadoImagenesFTP, tvLocalidadClienteContratoPrincipal, tvColoniaClienteContratoPrincipal,
                tvCalleClienteContratoPrincipal, tvIdContratoClienteContratoPrincipal, tvNombreClienteContratoPrincipal, tvUltimoAbonoContratoPrincipal, tvObservacionesClienteContratoPrincipal,
                tvNumeroClientePrincipal, tvFormaPagoClienteContratoPrincipal, tvNotaClienteContratoPrincipal, tvDiasAtrasoContratoPrincipal, tvTimerImagenesFTP;
    ImageView ivQuitarBuscadorIcono;
    Teclado teclado;
    Runnable runnable, runnable2, runnable3, runnable4;
    boolean esLogin;
    Internet internet;
    Global global;
    static Global global2;

    NavigationView navigationView;

    Camara camara;

    //Nav view para colocar fechaSesion en menu
    NavigationView nav_view;
    TextView tvFechaSesionNavView;

    //Version App
    TextView tvVersion;

    //Top ventas
    TextView tvNombreOpto, tvNumVentas, tvSucursal, tvNombreAsistente;

    Fragment fragmento;

    //Iconos
    ImageView img_Logo, ic_Contrato, ic_Sincronizar, ic_Configuracion, ic_Compartir, ic_Cerrarsesion, ic_NotasCobranza;

    //Sucursal usuario logueado
    TextView tvSucursalUsuario;

    //Ruta carpeta imagenes
    String file_path;
    Llaves llaves;
    baseDeDatos conexion;
    //Temporizador para cargar imagenes a FTP
    CountDownTimer countDownTimer;

    public principal(boolean esLogin) {
        this.esLogin = esLogin;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment principal.
     */
    // TODO: Rename and change types and number of parameters
    public static principal newInstance(String param1, String param2) {
        principal fragment = new principal(false);
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
        Throwable identificador error 6
        */

        fragmento = principal.this;
        global = new Global(fragmento);
        global2 = new Global(fragmento);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                global.escribirErrorClasePrincipalThrowable(throwable, 6);
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
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        //Inicialización de Componentes
        fab = view.findViewById(R.id.fab);
        lvListaContratos = view.findViewById(R.id.lvListaContratos);

        //Inicialización de Clases
        filaContratos = new ArrayList<>();

        toolbar = getActivity().findViewById(R.id.toolbar);
        etBuscador = toolbar.findViewById(R.id.etBuscador);
        llBuscador = toolbar.findViewById(R.id.llBuscador);
        llBuscadorIcono = toolbar.findViewById(R.id.llBuscadorIcono);
        ivQuitarBuscadorIcono = toolbar.findViewById(R.id.ivQuitarBuscadorIcono);
        llProgress = toolbar.findViewById(R.id.llProgress);

        llAvisoImagenesFTP = getActivity().findViewById(R.id.llAvisoImagenesFTP);
        tvEstadoImagenesFTP = getActivity().findViewById(R.id.tvEstadoImagenesFTP);

        tvLocalidadClienteContratoPrincipal = view.findViewById(R.id.tvLocalidadClienteContratoPrincipal);
        tvColoniaClienteContratoPrincipal = view.findViewById(R.id.tvColoniaClienteContratoPrincipal);
        tvCalleClienteContratoPrincipal = view.findViewById(R.id.tvCalleClienteContratoPrincipal);
        tvIdContratoClienteContratoPrincipal = view.findViewById(R.id.tvIdContratoClienteContratoPrincipal);
        tvNombreClienteContratoPrincipal = view.findViewById(R.id.tvNombreClienteContratoPrincipal);
        tvUltimoAbonoContratoPrincipal = view.findViewById(R.id.tvUltimoAbonoContratoPrincipal);
        tvObservacionesClienteContratoPrincipal = view.findViewById(R.id.tvObservacionesClienteContratoPrincipal);
        tvNumeroClientePrincipal = view.findViewById(R.id.tvNumeroClientePrincipal);
        tvFormaPagoClienteContratoPrincipal = view.findViewById(R.id.tvFormaPagoClienteContratoPrincipal);
        tvNotaClienteContratoPrincipal = view.findViewById(R.id.tvNotaClienteContratoPrincipal);
        tvDiasAtrasoContratoPrincipal = view.findViewById(R.id.tvDiasAtrasoContratoPrincipal);


        navigationView = getActivity().findViewById(R.id.nav_view); //Para obtener el menu del navigationView

        nav_view = getActivity().findViewById(R.id.nav_view);  //Para obtener el menu del navigationView
        tvFechaSesionNavView = nav_view.findViewById(R.id.tvFechaSesionNavView); //Igualamos nuestra variable al componente dentro del Nav_View

        llConfiguracion = getActivity().findViewById(R.id.llConfiguracion);

        //Registro hora salida
        llRegistroSalida = getActivity().findViewById(R.id.llRegistroSalida);
        //Top ventas
        llTopVentas = getActivity().findViewById(R.id.llTopVentas);
        TableLayout tblTopVenta = getActivity().findViewById(R.id.tblTopVentas);

        //Seccion de supervision
        llSupervision = getActivity().findViewById(R.id.llSupervision);
        //Seccion de notas de cobranza
        llNotasCobranza = getActivity().findViewById(R.id.llNotasCobranza);

        //Seccion de temporizador imagenes pendinentes
        llTimerImagenesFTP = getActivity().findViewById(R.id.llTimerImagenesFTP);
        tvTimerImagenesFTP = getActivity().findViewById(R.id.tvTimerImagenesFTP);
        //Acceso a carpeta de app para ver imagenes contratos - Ventas
        llAlmacenamientoImagenesContrato = getActivity().findViewById(R.id.llAlmacenamientoImagenesContrato);
        //Imagen de logotipo
        img_Logo= getActivity().findViewById(R.id.img_Logo);

        //Iconos dentro del diseño
        ic_Contrato = getActivity().findViewById(R.id.ic_Contrato);
        ic_Sincronizar = getActivity().findViewById(R.id.ic_Sincronizar);
        ic_Configuracion = getActivity().findViewById(R.id.ic_Configuracion);
        ic_Compartir = getActivity().findViewById(R.id.ic_Compartir);
        ic_Cerrarsesion = getActivity().findViewById(R.id.ic_Cerrarsesion);

        //Pintamos Version de aplicacion en tv antes del icono cerrar sesion
        tvVersion = getActivity().findViewById(R.id.tvVersion);
        tvVersion.setText("V " + BuildConfig.VERSION_NAME);

        //Configuracion activa para Logotipo
        global.verificarConfiguracionLogoIconos(img_Logo, 0);

        //Configuracion activa para iconos
        global.verificarConfiguracionLogoIconos(ic_Contrato, 1);
        global.verificarConfiguracionLogoIconos(ic_Sincronizar, 1);
        global.verificarConfiguracionLogoIconos(ic_Configuracion, 1);
        global.verificarConfiguracionLogoIconos(ic_Compartir, 1);
        global.verificarConfiguracionLogoIconos(ic_Cerrarsesion, 1);

        //Configuracion activada para encabezados
        global.verificarConfiguracionEncabezados(tvEstadoImagenesFTP);
        global.verificarConfiguracionEncabezados(tvVersion);
        global.verificarConfiguracionEncabezados(tvFechaSesionNavView);

        //Configuracion activa para barra de herramientas
        global.verificarConfiguracionNavBar(toolbar);

        obtenerRol = new ObtenerRol(fragmento);
        String rol = obtenerRol.obtenerAtributoUsuarioLogeado("ROL");
        String nombreUsuarioLogeado = obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO");
        String sucursalUsuarioLogueado = obtenerRol.obtenerAtributoUsuarioLogeado("SUCURSAL");

        Object[] objetos = {lvListaContratos, filaContratos, rol, llBuscadorIcono, llBuscador, ivQuitarBuscadorIcono, nombreUsuarioLogeado,
                            tvIdContratoClienteContratoPrincipal, tvNombreClienteContratoPrincipal, tvColoniaClienteContratoPrincipal,
                            tvNotaClienteContratoPrincipal, etBuscador, llProgress, tblTopVenta, tvUltimoAbonoContratoPrincipal};
        controlador = new Controlador(fragmento, objetos);
        teclado = new Teclado(fragmento);
        internet = new Internet(fragmento.getActivity());
        camara = new Camara(fragmento);

        Handler handler = new Handler();
        runnable = new Runnable() {
            public void run() {

                String descargaCompleta = global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1");
                if(descargaCompleta.contains("1") || descargaCompleta.contains("3")) {
                    //Descarga de datos fue insertada correctamente o cierre de sesion

                    //Cambiar el color del toolbar a azul
                    toolbar.setBackgroundColor(Color.parseColor(global.toolBarBackground));

                    //Colocar nombre de la sucursal del usuario logueado
                    tvSucursalUsuario = getActivity().findViewById(R.id.tvSucursalUsuario);
                    tvSucursalUsuario.setText("SUC: " + sucursalUsuarioLogueado.toUpperCase());

                    if(descargaCompleta.contains("1")) {
                        //Descarga de datos fue insertada correctamente

                        //Para que se desbloquee el NavigationDrawer y se muestre
                        ((MainActivity) getActivity()).unlockDrawer();
                        tvFechaSesionNavView.setText(global.obtenerFechaSesion());  //Asignamos FechaSesion mandando llamar el metodo en clase global

                        if (rol.equals("4")) {
                            //Rol cobranza
//                            mostrarOcultarLocalidadNumeroHorizontalVertical();
                            llConfiguracion.setVisibility(View.VISIBLE);
                            tvIdContratoClienteContratoPrincipal.setEnabled(true);
                            tvNombreClienteContratoPrincipal.setEnabled(true);
                            tvUltimoAbonoContratoPrincipal.setEnabled(true);
                            tvColoniaClienteContratoPrincipal.setEnabled(true);
                            tvNotaClienteContratoPrincipal.setEnabled(true);
                            fab.setVisibility(View.GONE);
                            llTopVentas.setVisibility(View.GONE);
                            llSupervision.setVisibility(View.VISIBLE);
                            llNotasCobranza.setVisibility(View.VISIBLE);
                            llAlmacenamientoImagenesContrato.setVisibility(View.GONE);
                            llTimerImagenesFTP.setVisibility(View.GONE);
                            llRegistroSalida.setVisibility(View.GONE);

                            fragmento.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            ((MainActivity) getActivity()).showMenuHamburguesa();

                        } else {
                            //Aistente/Optometrista

                            //Columnas a ocultar lista contratos
                            tvFormaPagoClienteContratoPrincipal.setVisibility(View.GONE);
                            tvDiasAtrasoContratoPrincipal.setVisibility(View.GONE);
                            tvNotaClienteContratoPrincipal.setVisibility(View.GONE);
                            tvObservacionesClienteContratoPrincipal.setVisibility(View.GONE);
                            tvUltimoAbonoContratoPrincipal.setVisibility(View.GONE);
                            llAlmacenamientoImagenesContrato.setVisibility(View.VISIBLE);
                            llTimerImagenesFTP.setVisibility(View.VISIBLE);
                            if(rol.equals("12")){
                                llRegistroSalida.setVisibility(View.VISIBLE);
                            }else{
                                llRegistroSalida.setVisibility(View.GONE);
                            }
                            fragmento.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            fab.setVisibility(View.VISIBLE);

                            //Top Ventas
                            ArrayList<String> topOptometristas = global.obtenerTopVentas("12");
                            ArrayList<String> topAsistentes = global.obtenerTopVentas("13");
                            controlador.crearTablaTopVentas(topOptometristas,topAsistentes);

                        }

                        if (etBuscador.getText().toString().length() > 0) {
                            llProgress.setVisibility(View.GONE);
                            llBuscadorIcono.setVisibility(View.GONE);
                            llBuscador.setVisibility(View.VISIBLE);
                            ivQuitarBuscadorIcono.setVisibility(View.VISIBLE);
                            ((MainActivity) fragmento.getActivity()).setTitle("");
                            controlador.obtenerListaContratosBuscador(etBuscador.getText().toString());
                        } else {
                            if (rol.equals("4")) {
                                //Rol cobranza
                                llBuscadorIcono.setVisibility(View.VISIBLE);
                                camara.mayRequestStoragePermission();
                            }
                            controlador.obtenerListaContratos(true);
                        }
                        if (esLogin) {
                            controlador.consultarMensajes();
                        }

                    }

                    handler.removeCallbacks(runnable);

                }else{
                    //Descarga de datos incompleta o no alcanzo a responder el webservice

                    //Cambiar el color del toolbar a rojo
                    toolbar.setBackgroundColor(Color.parseColor("#FFACA6"));

                    //Para que se desbloquee el NavigationDrawer y se muestre
                    ((MainActivity) getActivity()).unlockDrawer();

                    //Log.i("MENSAJE", "Entro " + descargaCompleta);

                    handler.postDelayed(this, 300);
                }
            }
        };
        runnable.run();

        if(global.obtenerAtributoTabla("GLOBAL", "SUBIRIMAGENESFTP", "ID", "1").equals("0")) {
            global.actualizarAtributoTabla("GLOBAL", "SUBIRIMAGENESFTP", "1", "ID", "1");
            Handler handler2 = new Handler();
            runnable2 = new Runnable() {
                public void run() {
                    if(global.obtenerAtributoTabla("GLOBAL", "SUBIRIMAGENESFTP", "ID", "1").equals("0")) {
                        Log.i("MENSAJE", "Se detuvo el runnable");
                        tvTimerImagenesFTP.setText("");
                        handler2.removeCallbacks(runnable2);
                    }else {
                        if (internet.verificarConexionInternet()) {
                            controlador.subirImagenesAFTPContratosTerminados();
                            Log.i("MENSAJE", "Entro a nuevo runnable");
                        }

                        countDownTimer = new CountDownTimer(30000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long tiempo = millisUntilFinished / 1000;
                                if(tiempo > 10){
                                    tvTimerImagenesFTP.setText("Subiendo imágenes... por favor, espera.");
                                }else{
                                    tvTimerImagenesFTP.setText("Subiendo imagenes en: " + tiempo + " segundos");
                                }
                            }

                            @Override
                            public void onFinish() {
                                // Acciones a realizar cuando el temporizador llega a cero (30 segundos)
                                tvTimerImagenesFTP.setText("");
                            }
                        };

                        // Iniciar el temporizador
                        countDownTimer.start();
                    }
                        handler2.postDelayed(this, 30000);
                    }
            };
            runnable2.run();
        }

        //Tarea en segundo plano para subir imagenes existentes en el movil desde hace 48hrs.
        file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        conexion = new baseDeDatos(fragmento.getContext(), "basedatos", null, llaves.versiondb);
        File carpetaImagenes = new File(file_path + "/luzatuvida/.nomedia");
        llaves = new Llaves();

        if(carpetaImagenes.exists()){
            //Existe carpeta de imagenes

            Handler handler4 = new Handler();
            runnable4 = new Runnable() {
                public void run() {
                    File[] imagenesPendientes = carpetaImagenes.listFiles(new obtenerImagenesMovil());
                    if(imagenesPendientes != null) {
                        if (internet.verificarConexionInternet()) {
                            //Con conexion a internet
                            for (File archivo : imagenesPendientes) {
                                //Recorrer imagenes obtenidas del movil
                                if(global.obtenerAtributoTabla("IMAGENESPENDIENTESMOVIL","BANDERA","NOMBREFOTO", archivo.getName()).equals("")){
                                    //Si no existe registro en la tabla - insertarlo

                                    try {
                                        //Insertar nombre de la imagen en tabla con bandera en 0
                                        SQLiteDatabase sqLiteDB = conexion.getWritableDatabase();
                                        ContentValues valores = new ContentValues();
                                        valores.put("NOMBREFOTO", archivo.getName());
                                        valores.put("BANDERA", "0");
                                        sqLiteDB.insert("IMAGENESPENDIENTESMOVIL",null, valores);
                                        sqLiteDB.close();

                                    }catch (SQLiteException e) {
                                        global.escribirError(e, 313);
                                        Log.i("ERRORBD", e.getMessage() + "");
                                    }

                                }
                            }

                            //Verificar si existe una imagen por subir
                            try{

                                SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
                                String SQL = "SELECT * FROM IMAGENESPENDIENTESMOVIL";
                                Cursor datos = sqLiteDB2.rawQuery(SQL, null);

                                if (datos.getCount() == 0){
                                    Log.i("MENSAJE", "No hay contratos para subir fotos a FTP");
                                }

                                while(datos.moveToNext()){
                                    //Mandar imagen a subir (Nombre imagen y bandera)
                                    controlador.subirImagenesAFTUltimas48Horas(datos.getString(0), datos.getString(1));
                                }

                                sqLiteDB2.close();
                                datos.close();

                            }catch (SQLiteException e){
                                global.escribirError(e, 314);
                                Log.i("ERRORBD", e.getMessage() + "");
                            }

                        }
                        handler4.postDelayed(this, 30000);
                    }else{
                        Log.i("MENSAJE", "Sin imagenes pendientes por subir (48hrs)");
                        handler4.removeCallbacks(runnable4);
                    }
                }
            };
            runnable4.run();
        }

        llAvisoImagenesFTP.setVisibility(View.VISIBLE);
        //Verificar si las imagenes ya se subieron del contrato (Cambiar tvTextoContrato)
        Handler handler3 = new Handler();
        runnable3 = new Runnable() {
            public void run() {
                tvEstadoImagenesFTP.setText("Imagenes pendientes (" + controlador.obtenerNumeroImagenesNoSubidasFTP() + ")");
                handler3.postDelayed(this, 2000);
            }
        };
        runnable3.run();

        if(!rol.equals("4")) {
            llTopVentas.setVisibility(View.VISIBLE);
        }

        //Registro hora de salida
        llRegistroSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descargaCompleta = global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1");
                if(descargaCompleta.contains("1")) {
                    //Descarga de datos fue insertada correctamente
                    controlador.mostrarAlertDialogRegistrarHoraSalida();
                }else if(descargaCompleta.contains("4")){
                    //EN ESPERA
                    Toast.makeText(fragmento.getActivity(), "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                }else {
                    //Descarga de datos incompleta o no alcanzo a responder el webservice
                    Toast.makeText(fragmento.getActivity(), "Para continuar, es necesario sincronizar...", Toast.LENGTH_LONG).show();
                }
            }
        });

        //FloatingTextButton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!global.obtenerUnResultadoQuery("SELECT ID_CONTRATO FROM CONTRATOS WHERE DATOS = '0' AND ESTATUS_ESTADOCONTRATO = '' ORDER BY ID_CONTRATO ASC LIMIT 1").equals("")) {
                    //Hay contratos disponibles (Se sincronizo correctamente)
                    Fragment verificadorFragment;
                    FragmentTransaction transaction;
                    verificadorFragment = new nuevoContrato();
                    transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, verificadorFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else {
                    //No hay contratos disponibles (Fallo de sincronizacion)
                    Toast.makeText(fragmento.getActivity(), "No se han encontrado contratos disponibles (Favor de sincronizar nuevamente)", Toast.LENGTH_LONG).show();
                }
            }
        });

        //ListViews
        lvListaContratos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                controlador.abrirContrato(i);
            }
        });

        lvListaContratos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                controlador.mostrarAlertCancelarContrato(i);
                return true;
            }
        });

        llBuscadorIcono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llBuscador.setVisibility(View.VISIBLE);
                etBuscador.requestFocus();
                teclado.showKeyboard();
                llBuscadorIcono.setVisibility(View.GONE);
                ivQuitarBuscadorIcono.setVisibility(View.VISIBLE);
                ((MainActivity)fragmento.getActivity()).setTitle("");
            }
        });

        ivQuitarBuscadorIcono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teclado.hideKeyboard();
                llBuscador.setVisibility(View.GONE);
                llBuscadorIcono.setVisibility(View.VISIBLE);
                ivQuitarBuscadorIcono.setVisibility(View.GONE);
                etBuscador.setText("");
                ((MainActivity)fragmento.getActivity()).setTitle(nombreUsuarioLogeado);
                controlador.obtenerListaContratos(false);
            }
        });

        etBuscador.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    controlador.obtenerListaContratosBuscador(etBuscador.getText().toString());
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });

        tvIdContratoClienteContratoPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rol.equals("4")) {
//                    controlador.mostrarOcultarTvIdContratoNombre();
                    if (etBuscador.getText().toString().length() > 0) {
                        controlador.obtenerListaContratosBuscador(etBuscador.getText().toString());
                    } else {
                        controlador.obtenerListaContratos(false);
                    }
                }
            }
        });

        tvNombreClienteContratoPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rol.equals("4")) {
//                    controlador.mostrarOcultarTvIdContratoNombre();
                    if (etBuscador.getText().toString().length() > 0) {
                        controlador.obtenerListaContratosBuscador(etBuscador.getText().toString());
                    } else {
                        controlador.obtenerListaContratos(false);
                    }
                }
            }
        });

        tvColoniaClienteContratoPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rol.equals("4")) {
//                    controlador.mostrarOcultarTvColoniaNota();
                    if (etBuscador.getText().toString().length() > 0) {
                        controlador.obtenerListaContratosBuscador(etBuscador.getText().toString());
                    } else {
                        controlador.obtenerListaContratos(false);
                    }
                }
            }
        });

        tvNotaClienteContratoPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rol.equals("4")) {
//                    controlador.mostrarOcultarTvColoniaNota();
                    if (etBuscador.getText().toString().length() > 0) {
                        controlador.obtenerListaContratosBuscador(etBuscador.getText().toString());
                    } else {
                        controlador.obtenerListaContratos(false);
                    }
                }
            }
        });

        return view;
    }

    private void mostrarOcultarLocalidadNumeroHorizontalVertical() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            tvLocalidadClienteContratoPrincipal.setVisibility(View.VISIBLE);
            tvNumeroClientePrincipal.setVisibility(View.VISIBLE);
            tvFormaPagoClienteContratoPrincipal.setVisibility(View.VISIBLE);
        } else {
            // In portrait
            tvLocalidadClienteContratoPrincipal.setVisibility(View.GONE);
            tvNumeroClientePrincipal.setVisibility(View.GONE);
            tvFormaPagoClienteContratoPrincipal.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*
        mostrarOcultarLocalidadNumeroHorizontalVertical();
        */

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        camara.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static class obtenerImagenesMovil implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            // Verifica si el archivo tiene una extensión de imagen conocida
            if((name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif")) && !name.equals("temp")) {
                //Si es imagen - particionar nombre
                String[] arregloNombreImagen = name.split("-");

                //Tomar seccion de numero de contrato
                String idContrato = arregloNombreImagen[arregloNombreImagen.length - 2];

                if(global2.obtenerAtributoContrato(idContrato,"ID_CONTRATO").equals("")){
                    //Imagen pertenece a un contrato antiguo que no se encuentra en la BD del movil

                    //Tomar seccion de fechahora de registro
                    String fechaCreacionImagen = arregloNombreImagen[arregloNombreImagen.length - 1];
                    //Dar formato a cadena como si fuera una fecha dividida por guiones
                    String fechaHora = fechaCreacionImagen.substring(0, 8);
                    String fechaHoraFormato = fechaHora.substring(0, 4) + "-" + fechaHora.substring(4, 6) + "-" + fechaHora.substring(6, 8);

                    try {
                        // Formato de la cadena
                        String formatoFecha = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);

                        //Convertir cadena a tipo fecha
                        Date fechaCreacion = sdf.parse(fechaHoraFormato);

                        // Obtener la fecha actual
                        Date fechaActual = new Date();

                        // Verificar si la diferencia es menor a 48 horas (en milisegundos)
                        long diferenciaEnMilisegundos = fechaActual.getTime() - fechaCreacion.getTime();
                        long horasDiferencia = diferenciaEnMilisegundos / (60 * 60 * 1000);

                        if (horasDiferencia < 48) {
                            return true;
                        }else{
                            return false;
                        }

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                return false;
            }
            return false;
        }
    }
}