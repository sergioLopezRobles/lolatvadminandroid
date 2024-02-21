package com.luzatuvida.lolatvadminandroid.inicioSesion;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.luzatuvida.lolatvadminandroid.BuildConfig;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Seguridad;
import com.luzatuvida.lolatvadminandroid.global.Sincronizacion;
import com.luzatuvida.lolatvadminandroid.global.Teclado;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;
import com.luzatuvida.lolatvadminandroid.vista.principal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class Controlador {

    /*
    Identificador error del 111 al 117
     */

    Fragment fragmento;

    //Declaración de clases
    private Internet conexionInternet;
    private baseDeDatos conexion;
    Teclado teclado;
    ObtenerRol obtenerRol;

    EditText etCorreo;
    EditText etContrasena;
    LinearLayout llProgress;
    TextView tvPorcentajeProgreso, tvDescargarAplicacion, tvCerrarUltimaSesion;
    Button btnIniciar;

    Llaves llaves;
    Global global;
    Sincronizacion sincronizacion;
    SQLiteDatabase sqLiteDB;
    Seguridad seguridad;
    String urlDescargarApp = "";

    public Controlador(Object[] objetos) {
        this.fragmento = (Fragment)objetos[0];
        etCorreo = (EditText)objetos[1];
        etContrasena = (EditText)objetos[2];
        llProgress = (LinearLayout)objetos[3];
        tvPorcentajeProgreso = (TextView)objetos[4];
        btnIniciar = (Button) objetos[5];
        tvDescargarAplicacion = (TextView) objetos[6];
        tvCerrarUltimaSesion = (TextView) objetos[7];

        llaves = new Llaves();
        conexionInternet = new Internet(fragmento.getActivity());
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
        teclado = new Teclado(fragmento);
        obtenerRol = new ObtenerRol(fragmento);
        global = new Global(fragmento);
        seguridad = new Seguridad(fragmento);
        sincronizacion = new Sincronizacion(fragmento,false);
        global.actualizarAtributoTabla("GLOBAL", "SUBIRIMAGENESFTP", "0", "ID", "1");
    }

    /*Metodo/Funcion: llenarCampoUsuario
     Descripcion: Obtener correo del usuario que actualmente se logeo y mostrarlo
   */
    public void llenarCampoUsuario() {

        //Mostrar mensaje para que cambie de idioma el telefono
        if (!String.valueOf(Locale.getDefault()).equals("es_MX") && !String.valueOf(Locale.getDefault()).equals("es_US")) {
            Toast.makeText(fragmento.getActivity(), "Se recomienda cambiar el idioma del dispositivo a español de México o Estados Unidos", Toast.LENGTH_LONG).show();
        }
        etCorreo.setText(obtenerRol.obtenerAtributoUsuarioLogeado("CORREO"));
    }

    /*Metodo/Funcion: validarInicioSesion
     Descripcion: Validar si correo o contraseña estan vacios o no, si hay internet o no para realizar peticion
   */
    public void validarInicioSesion() {

        //Mostrar ProgressBar
        llProgress.setVisibility(View.VISIBLE);
        tvPorcentajeProgreso.setText("Iniciando Sesión");

        if(etCorreo.getText().toString().equals("") || etContrasena.getText().toString().equals("")) {
            //Correo o contraseña vacios
            habilitarDeshabilitarLogin();
            Toast.makeText(fragmento.getActivity(), "Uno o mas campos vacíos", Toast.LENGTH_LONG).show();
        }else {
            //Correo o contraseña no vacios

            //Mostrar mensaje para que cambie de idioma el telefono
            if (!String.valueOf(Locale.getDefault()).equals("es_MX") && !String.valueOf(Locale.getDefault()).equals("es_US")) {
                //No se tiene configurado en idioma español
                Toast.makeText(fragmento.getActivity(), "Se recomienda cambiar el idioma del dispositivo a español de México o Estados Unidos", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.settings", "com.android.settings.LanguageSettings");
                fragmento.startActivity(intent);
                habilitarDeshabilitarLogin();
            }else {
                //Se tiene configurado en idioma español

                if (obtenerRol.obtenerAtributoUsuarioLogeado("USUARIOLOGEADO").equals("1")) {
                    //El usuario ya habia sido logeado por lo menos una vez
                    if (etCorreo.getText().toString().toUpperCase().equals(obtenerRol.obtenerAtributoUsuarioLogeado("CORREO"))
                            && etContrasena.getText().toString().equals(obtenerRol.obtenerAtributoUsuarioLogeado("CONTRASENA"))) {
                        //Usuario y contraseña correctos
                        if (global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1").contains("4")) {
                            sincronizacion.actualizarBanderaDescargaCompleta("0");
                        }
                        irAContratos();
                    } else if (!etCorreo.getText().toString().equals(obtenerRol.obtenerAtributoUsuarioLogeado("CORREO"))) {
                        //No es el mismo correo
                        tvCerrarUltimaSesion.setVisibility(View.VISIBLE);
                        Toast.makeText(fragmento.getActivity(), "Necesitas cerrar sesión con la cuenta anterior (" + obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO") + ")", Toast.LENGTH_LONG).show();
                    } else {
                        //Usuario o contraseña incorrectos
                        Toast.makeText(fragmento.getActivity(), "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    }
                    habilitarDeshabilitarLogin();
                } else {

                    if (conexionInternet.verificarConexionInternet()) {
                        //Hay internet
                        iniciarSesionPeticion();
                    } else {
                        //No hay internet
                        Toast.makeText(fragmento.getActivity(), "No se pudo conectar al servidor - Se necesita internet", Toast.LENGTH_LONG).show();
                        habilitarDeshabilitarLogin();
                    }

                }

            }


        }

    }

    /*Metodo/Funcion: iniciarSesionPeticion
     Descripcion: Se realiza la peticion a la pagina mandando correo, contraseña, dispositivo, etc
   */
    public void iniciarSesionPeticion() {

        RequestQueue requestQueue = Volley.newRequestQueue(fragmento.getContext());
        JSONObject parametrosJSON = new JSONObject();

        try {

            parametrosJSON.put("correo", etCorreo.getText().toString());
            parametrosJSON.put("contrasena", etContrasena.getText().toString());
            parametrosJSON.put("dispositivo", llaves.dispositivo);
            parametrosJSON.put("idunico", Settings.Secure.getString(fragmento.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
            parametrosJSON.put("version", Build.VERSION.RELEASE);
            parametrosJSON.put("modelo", Build.MODEL);
            parametrosJSON.put("versiongradle", BuildConfig.VERSION_NAME);
            parametrosJSON.put("lenguajetelefono", Locale.getDefault().getDisplayName());

            JSONObject jsonArrayAbonosArchivo = new JSONObject();
            File archivoMovimientos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/luzatuvida/.nomedia/movimientos.txt");
            if (archivoMovimientos.exists()) {
                //Existe archivo movimientos
                jsonArrayAbonosArchivo = global.obtenerAbonosArchivoMovimientosTxt(50);
            }
            parametrosJSON.put("datosarchivo", "[" + jsonArrayAbonosArchivo + "]");

            String rol = obtenerRol.obtenerAtributoUsuarioLogeado("ROL");
            //Es Opto/Asistente?
            if(!rol.equals("4")){
                //Obtener historiales clinicos del archivo txt
                JSONObject jsonArrayHistorialesClinicosArchivo = new JSONObject();
                File archivoHistorialesClinicos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/luzatuvida/.nomedia/historialesClinicos.txt");
                if (archivoHistorialesClinicos.exists()) {
                    //Existe archivo historiales clinicos
                    jsonArrayHistorialesClinicosArchivo = global.obtenerRegistrosArchivoHistorialesClinicosTxt(5);
                }

                parametrosJSON.put("datosarchivohistorialesclinicos", "[" + jsonArrayHistorialesClinicosArchivo + "]");
            }

            JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST, llaves.url_inicio_sesion, parametrosJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        switch (sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "codigo")) {
                            case "5Gn6oZ7QUFxT4uDULhAB": //Usuario o contraseña incorrectos
                                global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos");
                                habilitarDeshabilitarLogin();
                                break;
                            case "Ppts8qWkkqosQQqRKlMz": //Solicitud pendiente por confirmar
                                global.imprimirMensajeEnHilo("Solicita a sucursal autorizacion para acceder");
                                habilitarDeshabilitarLogin();
                                break;
                            case "4vdw3EAq7xfyeKVg0NN7": //Login Correcto!!
                                sincronizacion.actualizarBanderaDescargaCompleta("0");
                                habilitarDeshabilitarLogin();
                                eliminarRegistroTablaMovil();
                                limpiarValoresHamburguesa();
                                ((MainActivity) fragmento.getActivity()).onPrepareOptionsMenu(((MainActivity) fragmento.getActivity()).menuHamburguesa);
                                Object[] objetos = {sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "token"), etCorreo.getText().toString(),
                                                    etContrasena.getText().toString()};
                                guardarCredenciales(sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "id"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "usuario"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "correo"),
                                        etContrasena.getText().toString(),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "token"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "rol"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "id_zona"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "fechaactual"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "sucursal"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "telefonoatencionclientessucursal"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "id_franquicia"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "ruta_ftp"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "usuario_ftp"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "contrasena_ftp"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "whatsapp"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "abonominimosemanal"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "preciodolar"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "supervisorcobranza"),
                                        sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "ultimaconexion"));
                                sincronizacion.sincronizarMetodo(2, objetos, 0);
                                irAContratos();
                                break;
                            case "ozpw6GLnvbCMpL2QjIQl": //Debe mandarse el mensaje que es necesario actualizar la aplicacion
                                global.imprimirMensajeEnHilo("Es necesario actualizar la aplicación");
                                urlDescargarApp = sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "appactual");
                                tvDescargarAplicacion.setVisibility(View.VISIBLE);
                                habilitarDeshabilitarLogin();
                                break;
                            /*case "SINCRONIZAR1": //Aplicacion no valida
                                habilitarDeshabilitarLogin();
                                Object[] objetos2 = {obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN"), etCorreo.getText().toString(), etContrasena.getText().toString()};
                                sincronizacion.sincronizarMetodo(1, objetos2, 0);
                                break;*/
                            case "yqZKQB8et5w3N7dK3ZZC": //El usuario esta logeado en la pagina
                                global.imprimirMensajeEnHilo("Ya tienes una sesión iniciada en la pagina web");
                                habilitarDeshabilitarLogin();
                                break;
                            case "swYbf6Diq6DiRS67lXaA": //Usuario diferente a Opto/Asist/Cobranza
                                global.imprimirMensajeEnHilo("Usuario no admitido (Por favor contacta a administración)");
                                habilitarDeshabilitarLogin();
                                break;
                            case "eQiNoYUY0qV4dSlaHMMf": //No existe el usuario en la tabla usuariosfranquicia en el webservice
                                global.imprimirMensajeEnHilo("Usuario inexistente o sin permisos para acceder");
                                habilitarDeshabilitarLogin();
                                break;
                            case "CSrOppxysdlvvd1JhEsF": //Usuario de ventas sin registro de asistencia
                                global.imprimirMensajeEnHilo("Es necesario que registres tu asistencia (Contacta a administración o asiste a sucursal).");
                                habilitarDeshabilitarLogin();
                                break;
                            case "Q7YuAnPM6ykW51SWmdmu": //Usuario suspendido por algun motivo
                                global.imprimirMensajeEnHilo("Has sido suspendido, comunicate con tu jefe inmediato.");
                                habilitarDeshabilitarLogin();
                                break;
                            default:
                                global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos.");
                                habilitarDeshabilitarLogin();
                        }

                    } catch (JSONException e) {
                        global.escribirError(e, 111);
                        Log.i("MENSAJE", e.getMessage());
                        habilitarDeshabilitarLogin();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error instanceof TimeoutError) {
                            //Time out error
                            global.imprimirMensajeEnHilo("Error al intentar conectar al servidor, se acabo el tiempo de espera (Sin datos disponibles/sin señal/sin conexion a internet por wifi)");
                        }else if(error instanceof NoConnectionError){
                            //net work error
                            global.imprimirMensajeEnHilo("No se pudo conectar(Sin señal/servicio)");
                        } else if (error instanceof AuthFailureError) {
                            //error
                            global.imprimirMensajeEnHilo("Error de autenticacion");
                        } else if (error instanceof ServerError) {
                            //Erroor
                            global.imprimirMensajeEnHilo("No se pudo conectar al servidor");
                        } else if (error instanceof NetworkError) {
                            //Error
                            global.imprimirMensajeEnHilo("Error de red/señal");
                        } else if (error instanceof ParseError) {
                            //Error
                            global.imprimirMensajeEnHilo("Ocurrio un error al intentar conectar");
                        }else{
                            global.imprimirMensajeEnHilo("Error al intentar conectar");
                        }
                        habilitarDeshabilitarLogin();
                        //End
                    } catch (Exception e) {
                        global.escribirError(e, 112);
                        global.imprimirMensajeEnHilo("Error: "+e.toString());
                        habilitarDeshabilitarLogin();
                    }
                }
            });

            peticion.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            requestQueue.add(peticion);

        }catch (JSONException e) {
            global.escribirError(e, 113);
            habilitarDeshabilitarLogin();
            Log.i("ERRORJSON", "" + e);
        }

    }

    private void limpiarValoresHamburguesa() {
        try {
            seguridad.guardarSharedPreferences("valorHamburguesaTodos", "");
            seguridad.guardarSharedPreferences("valorHamburguesaLunes", "");
            seguridad.guardarSharedPreferences("valorHamburguesaMartes", "");
            seguridad.guardarSharedPreferences("valorHamburguesaMiercoles", "");
            seguridad.guardarSharedPreferences("valorHamburguesaJueves", "");
            seguridad.guardarSharedPreferences("valorHamburguesaViernes", "");
            seguridad.guardarSharedPreferences("valorHamburguesaSabado", "");
            seguridad.guardarSharedPreferences("valorHamburguesaDomingo", "");
            seguridad.guardarSharedPreferences("valorHamburguesaContado", "");
            seguridad.guardarSharedPreferences("valorHamburguesaSemanal", "");
            seguridad.guardarSharedPreferences("valorHamburguesaQuincenal", "");
            seguridad.guardarSharedPreferences("valorHamburguesaMensual", "");
            seguridad.guardarSharedPreferences("valorHamburguesaRuta", "");
        } catch (Exception e) {
            global.escribirError(e, 114);
            e.printStackTrace();
        }
    }

    public void habilitarDeshabilitarLogin(){
        llProgress.setVisibility(View.GONE);
        btnIniciar.setVisibility(View.VISIBLE);
        etCorreo.setFocusableInTouchMode(true);
        etContrasena.setFocusableInTouchMode(true);
    }

    /*Metodo/Funcion: guardarCredenciales
     Descripcion: Se guardan los datos obtenidos de la respuesta de la pagina en la tabla MOVIL
   */
    public void guardarCredenciales(String id, String usuario, String correo, String contrasena, String token, String rol, String id_zona, String fechaactual,
                                    String sucursal, String telefonoatencionclientessucursal, String id_franquicia, String ruta_ftp, String usuario_ftp,
                                    String contrasena_ftp, String whatsapp, String abonominimosemanal, String preciodolar, String supervisorcobranza, String ultimaconexion) {

        try{

            Log.i("MENSAJE", "ENTRO A GUARDAR LAS CREDENCIALES");
            sqLiteDB = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID", seguridad.cifrar((id).getBytes("UTF-16LE")));
            valores.put("USUARIO", seguridad.cifrar((usuario).getBytes("UTF-16LE")));
            valores.put("CORREO", seguridad.cifrar((correo).getBytes("UTF-16LE")));
            valores.put("CONTRASENA", seguridad.cifrar((contrasena).getBytes("UTF-16LE")));
            valores.put("TOKEN", seguridad.cifrar((token).getBytes("UTF-16LE")));
            valores.put("ROL", seguridad.cifrar((rol).getBytes("UTF-16LE")));
            valores.put("ID_ZONA", seguridad.cifrar((id_zona).getBytes("UTF-16LE")));
            valores.put("FECHAACTUAL", seguridad.cifrar((fechaactual).getBytes("UTF-16LE")));
            valores.put("USUARIOLOGEADO", seguridad.cifrar(("1").getBytes("UTF-16LE")));
            valores.put("SUCURSAL", seguridad.cifrar((sucursal).getBytes("UTF-16LE")));
            valores.put("ULTIMOCONTRATO", seguridad.cifrar(("").getBytes("UTF-16LE")));
            valores.put("TELEFONOATENCIONCLIENTESSUCURSAL", seguridad.cifrar((telefonoatencionclientessucursal).getBytes("UTF-16LE")));
            valores.put("ID_FRANQUICIA", seguridad.cifrar((id_franquicia).getBytes("UTF-16LE")));
            valores.put("RUTA_FTP", seguridad.cifrar((ruta_ftp).getBytes("UTF-16LE")));
            valores.put("USUARIO_FTP", seguridad.cifrar((usuario_ftp).getBytes("UTF-16LE")));
            valores.put("CONTRASENA_FTP", seguridad.cifrar((contrasena_ftp).getBytes("UTF-16LE")));
            valores.put("WHATSAPP", seguridad.cifrar((whatsapp).getBytes("UTF-16LE")));
            valores.put("ABONOMINIMOSEMANAL", seguridad.cifrar((abonominimosemanal).getBytes("UTF-16LE")));
            valores.put("PRECIODOLAR", seguridad.cifrar((preciodolar).getBytes("UTF-16LE")));
            valores.put("SUPERVISORCOBRANZA", seguridad.cifrar((supervisorcobranza).getBytes("UTF-16LE")));
            valores.put("ULTIMACONEXION", seguridad.cifrar((ultimaconexion).getBytes("UTF-16LE")));
            sqLiteDB.insert("MOVIL",null, valores);
            sqLiteDB.close();

        }catch (Exception e){
            global.escribirError(e, 115);
            Log.i("ERROR", e.getMessage() + "");
        }

    }

    private void eliminarRegistroTablaMovil() {
        try{

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            sqLiteDB2.delete("MOVIL",null,null);
            sqLiteDB2.close();

        }catch (SQLiteException e){
            global.escribirError(e, 116);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }
    }

    /*Metodo/Funcion: irAContratos
     Descripcion: Redireccionar a vista principal de contratos
   */
    public void irAContratos() {

        Log.i("MENSAJE", "IR A CONTRATOS");

        String nombreUsuarioLogeado = obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO");

        try {

            //Ocultar teclado
            teclado.hideKeyboard();

            //Obtener hora actual
            Calendar fecha = Calendar.getInstance();
            int hora = fecha.get(Calendar.HOUR_OF_DAY);

            //4:00 am - 11:59 am
            if(hora >= 4 && hora < 12){
                Toast.makeText(fragmento.getActivity(), "Buenos días " + nombreUsuarioLogeado, Toast.LENGTH_LONG).show();
            }
            //12:00pm - 5:59 pm
            if(hora >= 12 && hora <= 17){
                Toast.makeText(fragmento.getActivity(), "Buenas tardes " + nombreUsuarioLogeado, Toast.LENGTH_LONG).show();
            }
            //6:00pm - 3:59 am
            if(hora >= 18 || hora <= 3){
                Toast.makeText(fragmento.getActivity(), "Buenas noches " + nombreUsuarioLogeado, Toast.LENGTH_LONG).show();
            }

            (fragmento.getActivity()).setTitle(nombreUsuarioLogeado);
            Fragment verificadorFragment;
            FragmentTransaction transaction;
            verificadorFragment = new principal(true);
            transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, verificadorFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack(null);
            transaction.commit();

        }catch (Exception e) {
            global.escribirError(e, 117);
            Log.i("MENSAJE", "Ocurrio algo mal " + e.getMessage());
        }

    }

    public void descargarAplicacion() {
        global.iniciarDescargaAppURL(urlDescargarApp);
    }

    public void cerrarUltimaSesion() {
        sincronizacion.token = obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN");
        sincronizacion.cierresesion();
    }
}
