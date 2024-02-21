package com.luzatuvida.lolatvadminandroid.supervision;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
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
import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Camara;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Seguridad;
import com.luzatuvida.lolatvadminandroid.global.Sincronizacion;
import com.luzatuvida.lolatvadminandroid.global.Teclado;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;
import com.luzatuvida.lolatvadminandroid.vista.inicioSesion;
import com.luzatuvida.lolatvadminandroid.vista.principal;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Controlador {

    Fragment fragmento;
    Global global;
    Camara camara;
    ObtenerRol obtenerRol;
    Sincronizacion sincronizacion;
    Llaves llaves;
    private Internet conexionInternet;
    private baseDeDatos conexion;
    Teclado teclado;
    SQLiteDatabase sqLiteDB;
    Seguridad seguridad;
    String urlDescargarApp = "";
    Toolbar toolbar;
    LinearLayout llProgress, llBuscador, llBuscadorIcono;
    DrawerLayout drawerLayout;
    String idSupervisionVehicular = "";
    String numSerieVehiculo = "";
    LinearLayout llKilometrajeManana, llKilometrajeTarde, llFotoLadoIzquierdo, llFotoLadoDerecho, llFotoFrente, llFotoAtras, llFotoExtra1, llFotoExtra2, llFotoExtra3,
            llFotoExtra4, llFotoExtra5, llFotoExtra6;
    ImageView ivKilometrajeManana, ivKilometrajeTarde, ivMotoLadoIzquierdo, ivMotoLadoDerecho, ivMotoFrente, ivMotoAtras, ivMotoFotoExtra1, ivMotoFotoExtra2,
            ivMotoFotoExtra3, ivMotoFotoExtra4, ivMotoFotoExtra5, ivMotoFotoExtra6;
    TextView tvKilometrajeManana, tvKilometrajeTarde, tvMotoLadoIzquierdo, tvMotoLadoDerecho, tvMotoFrente, tvMotoAtras;
    Button btnCrearSupervision;

    String time = "";
    String fechaActual = "";
    boolean esActualizarSupervision;
    File direccionNoMedia;
    String file_path;

    //Banderas de imagenes actualizadas
    public String tomoFotoKilometraje1 = "0";
    public String tomoFotoKilometraje2 = "0";
    public String tomoFotoLadoIzquierdo = "0";
    public String tomoFotoLadoDerecho = "0";
    public String tomoFotoFrente = "0";
    public String tomoFotoAtras = "0";
    public String tomoFotoExtra1 = "0";
    public String tomoFotoExtra2 = "0";
    public String tomoFotoExtra3 = "0";
    public String tomoFotoExtra4 = "0";
    public String tomoFotoExtra5 = "0";
    public String tomoFotoExtra6 = "0";

    public Controlador(Object[] objetos) {
        this.fragmento = (Fragment)objetos[0];
        llKilometrajeManana = (LinearLayout) objetos[1];
        llKilometrajeTarde = (LinearLayout) objetos[2];
        llFotoLadoIzquierdo = (LinearLayout) objetos[3];
        llFotoLadoDerecho = (LinearLayout) objetos[4];
        llFotoFrente = (LinearLayout) objetos[5];
        llFotoAtras = (LinearLayout) objetos[6];
        llFotoExtra1 = (LinearLayout) objetos[7];
        llFotoExtra2 = (LinearLayout) objetos[8];
        llFotoExtra3 = (LinearLayout) objetos[9];
        llFotoExtra4 = (LinearLayout) objetos[10];
        llFotoExtra5 = (LinearLayout) objetos[11];
        llFotoExtra6 = (LinearLayout) objetos[12];
        ivKilometrajeManana = (ImageView) objetos[13];
        ivKilometrajeTarde = (ImageView) objetos[14];
        ivMotoLadoIzquierdo = (ImageView) objetos[15];
        ivMotoLadoDerecho = (ImageView) objetos[16];
        ivMotoFrente = (ImageView) objetos[17];
        ivMotoAtras = (ImageView) objetos[18];
        ivMotoFotoExtra1 = (ImageView) objetos[19];
        ivMotoFotoExtra2 = (ImageView) objetos[20];
        ivMotoFotoExtra3 = (ImageView) objetos[21];
        ivMotoFotoExtra4 = (ImageView) objetos[22];
        ivMotoFotoExtra5 = (ImageView) objetos[23];
        ivMotoFotoExtra6 = (ImageView) objetos[24];
        tvKilometrajeManana = (TextView) objetos[25];
        tvKilometrajeTarde = (TextView) objetos[26];
        tvMotoLadoIzquierdo = (TextView) objetos[27];
        tvMotoLadoDerecho = (TextView) objetos[28];
        tvMotoFrente = (TextView) objetos[29];
        tvMotoAtras = (TextView) objetos[30];
        btnCrearSupervision = (Button) objetos[31];

        llaves = new Llaves();
        conexionInternet = new Internet(fragmento.getActivity());
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
        teclado = new Teclado(fragmento);
        obtenerRol = new ObtenerRol(fragmento);
        camara = new Camara(fragmento);
        global = new Global(fragmento);
        seguridad = new Seguridad(fragmento);
        sincronizacion = new Sincronizacion(fragmento,false);
        file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        direccionNoMedia = new File(file_path + "/luzatuvida/.nomedia");
        global.actualizarAtributoTabla("GLOBAL", "SUBIRIMAGENESSUPERVISIONFTP", "0", "ID", "1");
        idSupervisionVehicular = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","INDICE","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
        toolbar = fragmento.getActivity().findViewById(R.id.toolbar);
        llProgress = toolbar.findViewById(R.id.llProgress);
        llBuscador = toolbar.findViewById(R.id.llBuscador);
        llBuscadorIcono = toolbar.findViewById(R.id.llBuscadorIcono);
        drawerLayout = fragmento.getActivity().findViewById(R.id.drawer_layout);
    }

    public void verificarsupervisionvehiculo() {
        if (conexionInternet.verificarConexionInternet()) {
            //Tiene internet
            RequestQueue requestQueue = Volley.newRequestQueue(fragmento.getContext());
            JSONObject parametrosJSON = new JSONObject();

            try {

                parametrosJSON.put("correo", obtenerRol.obtenerAtributoUsuarioLogeado("CORREO"));
                parametrosJSON.put("dispositivo", llaves.dispositivo);
                parametrosJSON.put("idunico", Settings.Secure.getString(fragmento.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                parametrosJSON.put("version", Build.VERSION.RELEASE);
                parametrosJSON.put("modelo", Build.MODEL);
                parametrosJSON.put("versiongradle", BuildConfig.VERSION_NAME);
                parametrosJSON.put("lenguajetelefono", Locale.getDefault().getDisplayName());

                JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST, llaves.url_supervision_entrar, parametrosJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "codigo")) {
                                case "5Gn6oZ7QUFxT4uDULhAB": //Usuario o contraseña incorrectos
                                    global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos");
                                    redireccionarLogin();
                                    break;
                                case "Ppts8qWkkqosQQqRKlMz": //Solicitud pendiente por confirmar
                                    global.imprimirMensajeEnHilo("Solicita a sucursal autorizacion para acceder");
                                    redireccionarLogin();
                                    break;
                                case "ozpw6GLnvbCMpL2QjIQl": //Debe mandarse el mensaje que es necesario actualizar la aplicacion
                                    global.imprimirMensajeEnHilo("Es necesario actualizar la aplicación");
                                    redireccionarLogin();
                                    break;
                                case "yqZKQB8et5w3N7dK3ZZC": //El usuario esta logeado en la pagina
                                    global.imprimirMensajeEnHilo("Ya tienes una sesión iniciada en la pagina web");
                                    redireccionarLogin();
                                    break;
                                case "swYbf6Diq6DiRS67lXaA": //Usuario diferente a Opto/Asist/Cobranza
                                    global.imprimirMensajeEnHilo("Usuario no admitido (Por favor contacta a administración)");
                                    redireccionarLogin();
                                    break;
                                case "eQiNoYUY0qV4dSlaHMMf": //No existe el usuario en la tabla usuariosfranquicia en el webservice
                                    global.imprimirMensajeEnHilo("Usuario inexistente o sin permisos para acceder");
                                    redireccionarLogin();
                                    break;
                                case "qefs3ZPOvqz2E2cZsPbp": //Cobrador sin vehiculo
                                    global.imprimirMensajeEnHilo("No cuentas con un vehiculo asignado (Por favor contacta a administracion)");
                                    mostrarOcultarCamposImagenesSupervision(false);
                                    break;
                                case "ZlbS4yVHLdebhF8F83sf": //Supervision pendiente por autorizar y con mas de 24 hrs de creacion
                                    global.imprimirMensajeEnHilo("Tienes una supervisión atrasada por autorizar (Por favor contacta a administracion)");
                                    mostrarOcultarCamposImagenesSupervision(false);
                                    break;
                                case "BCE3KTxrNUqff5opY5J9": //Crear por primera vez una supervision
                                    esActualizarSupervision = false;
                                    global.imprimirMensajeEnHilo("Toma las fotografias de tu vehículo y da clic en CREAR");
                                    numSerieVehiculo = sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "numserie");
                                    //Eliminar registro en tabla de supervision
                                    global.eliminarTablaORegistroTabla("VEHICULOSSUPERVISION","INDICE",idSupervisionVehicular);
                                    //Limpiar tabla de imagenes pendientes por subir
                                    global.eliminarRegistroTablaImagenesCargadasSupervision(1);
                                    mostrarOcultarCamposImagenesSupervision(true);
                                    break;
                                case "oZ6kPihlk0LwE9Je7S5g"://Supervision pendiente por autorizar - permitir accesar a vista para actualizar imagenes
                                    esActualizarSupervision = true;
                                    //Limpiar tabla de imagenes pendientes si no existe ninguna por subir - evitara multiples registros
                                    global.eliminarRegistroTablaImagenesCargadasSupervision(0);
                                    //Eliminar registro en tabla de supervision
                                    global.eliminarTablaORegistroTabla("VEHICULOSSUPERVISION","INDICE",sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "indice"));
                                    //Ingresar valores a tabla de supervision
                                    decodificarJsonEInsertarDatosSupervision(sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "supervision"));
                                    //Mostrar ImagenView
                                    mostrarOcultarCamposImagenesSupervision(true);
                                    //Cargar imagenes para mostrar fotos almacenadas en supervision
                                    descargarMostrarImagenSupervision();
                                    btnCrearSupervision.setText("ACTUALIZAR");
                                    break;
                                default:
                                    global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos.");
                                    redireccionarLogin();
                            }

                        } catch (JSONException e) {
                            global.escribirError(e, 279);
                            Log.i("MENSAJE", e.getMessage());
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

                        } catch (Exception e) {
                            global.escribirError(e, 280);
                            global.imprimirMensajeEnHilo("Error: "+e.toString());
                        }
                    }
                });

                peticion.setRetryPolicy(new DefaultRetryPolicy(
                        60000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));

                requestQueue.add(peticion);

            }catch (JSONException e) {
                global.escribirError(e, 281);
                Log.i("ERRORJSON", "" + e);
            }
        }else {
            //No se cuenta con internet
            Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
        }

    }

    public void registrarSupervisionVehicular(String idSupervision, String Kilometraje1, String Kilometraje2, String ladoizquierdo, String ladoderecho, String frente, String atras,
                                              String extra1, String extra2, String extra3, String extra4, String extra5, String extra6){

        RequestQueue requestQueue = Volley.newRequestQueue(fragmento.getContext());
        JSONObject parametrosJSON = new JSONObject();

        try {

            parametrosJSON.put("correo", obtenerRol.obtenerAtributoUsuarioLogeado("CORREO"));
            parametrosJSON.put("dispositivo", llaves.dispositivo);
            parametrosJSON.put("idunico", Settings.Secure.getString(fragmento.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
            parametrosJSON.put("version", Build.VERSION.RELEASE);
            parametrosJSON.put("modelo", Build.MODEL);
            parametrosJSON.put("versiongradle", BuildConfig.VERSION_NAME);
            parametrosJSON.put("lenguajetelefono", Locale.getDefault().getDisplayName());
            parametrosJSON.put("idSupervision", idSupervision);
            parametrosJSON.put("kilometraje1", Kilometraje1);
            parametrosJSON.put("Kilometraje2", Kilometraje2);
            parametrosJSON.put("ladoizquierdo", ladoizquierdo);
            parametrosJSON.put("ladoderecho", ladoderecho);
            parametrosJSON.put("frente", frente);
            parametrosJSON.put("atras", atras);
            parametrosJSON.put("extra1", extra1);
            parametrosJSON.put("extra2", extra2);
            parametrosJSON.put("extra3", extra3);
            parametrosJSON.put("extra4", extra4);
            parametrosJSON.put("extra5", extra5);
            parametrosJSON.put("extra6", extra6);

            JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST, llaves.url_supervision_registrar, parametrosJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        switch (sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "codigo")) {
                            case "5Gn6oZ7QUFxT4uDULhAB": //Usuario o contraseña incorrectos
                                global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos");
                                redireccionarLogin();
                                break;
                            case "Ppts8qWkkqosQQqRKlMz": //Solicitud pendiente por confirmar
                                global.imprimirMensajeEnHilo("Solicita a sucursal autorizacion para acceder");
                                redireccionarLogin();
                                break;
                            case "ozpw6GLnvbCMpL2QjIQl": //Debe mandarse el mensaje que es necesario actualizar la aplicacion
                                global.imprimirMensajeEnHilo("Es necesario actualizar la aplicación");
                                redireccionarLogin();
                                break;
                            case "yqZKQB8et5w3N7dK3ZZC": //El usuario esta logeado en la pagina
                                global.imprimirMensajeEnHilo("Ya tienes una sesión iniciada en la pagina web");
                                redireccionarLogin();
                                break;
                            case "swYbf6Diq6DiRS67lXaA": //Usuario diferente a Opto/Asist/Cobranza
                                global.imprimirMensajeEnHilo("Usuario no admitido (Por favor contacta a administración)");
                                redireccionarLogin();
                                break;
                            case "eQiNoYUY0qV4dSlaHMMf": //No existe el usuario en la tabla usuariosfranquicia en el webservice
                                global.imprimirMensajeEnHilo("Usuario inexistente o sin permisos para acceder");
                                redireccionarLogin();
                                break;
                            case "qefs3ZPOvqz2E2cZsPbp": //Cobrador sin vehiculo
                                global.imprimirMensajeEnHilo("No cuentas con un vehiculo asignado (Por favor contacta a administracion)");
                                mostrarOcultarCamposImagenesSupervision(false);
                                break;
                            case "e91pVlsRSJInQ5RegdPK": //Supervision no cumple con alguna validacion de registro
                                global.imprimirMensajeEnHilo(sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "mensaje"));
                                //Limpiar tabla de imagenes pendientes si no existe ninguna por subir - evitara multiples registros
                                global.eliminarRegistroTablaImagenesCargadasSupervision(0);
                                boolean validacion = validarImagenesSupervisionDiaSemana();
                                break;
                            case "lazeXsDzR8pptisupKhP": //Supervision registrada correctamente
                                global.imprimirMensajeEnHilo("Supervision registrada correctamente");
                                //Eliminar registro en tabla de supervision
                                global.eliminarTablaORegistroTabla("VEHICULOSSUPERVISION","INDICE",sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "indice"));
                                //Ingresar valores a tabla de supervision
                                decodificarJsonEInsertarDatosSupervision(sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "supervision"));
                                //Registrar imagenes a cargar a servidor ftp en su tabla de imagenes pendientes
                                insertarImagenesPendientesSupervision(Kilometraje1, Kilometraje2, ladoizquierdo, ladoderecho, frente, atras,
                                        extra1, extra2, extra3, extra4, extra5, extra6);
                                //Inicializar banderas de imagenes tomadas
                                reiniciarBanderasFotosTomadas();
                                //Actualizar texto del boton de crear
                                btnCrearSupervision.setText("ACTUALIZAR");
                                //Actualizar bandera
                                esActualizarSupervision = true;
                                //Limpiar tabla de imagenes pendientes si no existe ninguna por subir - evitara multiples registros
                                global.eliminarRegistroTablaImagenesCargadasSupervision(0);
                                break;
                            default:
                                global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos.");
                                redireccionarLogin();
                        }

                    } catch (JSONException e) {
                        global.escribirError(e, 287);
                        Log.i("MENSAJE", e.getMessage());
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

                    } catch (Exception e) {
                        global.escribirError(e, 288);
                        global.imprimirMensajeEnHilo("Error: "+e.toString());
                    }
                }
            });

            peticion.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            requestQueue.add(peticion);

        }catch (JSONException e) {
            global.escribirError(e, 289);
            Log.i("ERRORJSON", "" + e);
        }
    }

    public void verificarFotosSupervision(){
        RequestQueue requestQueue = Volley.newRequestQueue(fragmento.getContext());
        JSONObject parametrosJSON = new JSONObject();

        try {

            //Verificar que imagen se adjunto o se tomo
            parametrosJSON.put("id_usuario", obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
            parametrosJSON.put("id_franquicia", obtenerRol.obtenerAtributoUsuarioLogeado("ID_FRANQUICIA"));
            parametrosJSON.put("kilometraje1", tomoFotoKilometraje1);
            parametrosJSON.put("kilometraje2", tomoFotoKilometraje2);
            parametrosJSON.put("banderaActualizar", esActualizarSupervision);

            JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST, llaves.url_supervision_fotos, parametrosJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        switch (sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "codigo")) {

                            case "e91pVlsRSJInQ5RegdPK": //Supervision no cumple con alguna validacion de registro
                                global.imprimirMensajeEnHilo(sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "mensaje"));
                                vaciarImagenesTomadasRecientemente();
                                descargarMostrarImagenSupervision();
                                reiniciarBanderasFotosTomadas();
                                break;
                            case "lazeXsDzR8pptisupKhP": //Respuesta correcta
                                crearActualizarSupervisionVehicular();
                                break;
                            default:
                                global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos.");
                                redireccionarLogin();
                        }

                    } catch (JSONException e) {
                        global.escribirError(e, 297);
                        Log.i("MENSAJE", e.getMessage());
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

                    } catch (Exception e) {
                        global.escribirError(e, 298);
                        global.imprimirMensajeEnHilo("Error: "+e.toString());
                    }
                }
            });

            peticion.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            requestQueue.add(peticion);

        }catch (JSONException e) {
            global.escribirError(e, 299);
            Log.i("ERRORJSON", "" + e);
        }

    }
    public void crearActualizarSupervisionVehicular(){
        if(conexionInternet.verificarConexionInternet()) {
            //Se cuenta con internet

            String rutaFotoKilometrajeManana = "";
            String rutaFotoKilometrajeTarde = "";
            String rutaFotoLadoIzquierdo = "";
            String rutaFotoLadoDerecho = "";
            String rutaFotoFrente = "";
            String rutaFotoAtras = "";
            String rutaFotoExtra1 = "";
            String rutaFotoExtra2 = "";
            String rutaFotoExtra3 = "";
            String rutaFotoExtra4 = "";
            String rutaFotoExtra5 = "";
            String rutaFotoExtra6 = "";

            boolean almacenarImagenes = false;

            time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            fechaActual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");

            //Es actualizar supervision o registrara pro primera vez las imagenes?
            if(!esActualizarSupervision){
                //Creara la supervision - Validar campos
                if(validarImagenesSupervisionDiaSemana()){
                    //Respuesta es correcta
                    almacenarImagenes = true;

                }else{
                    //Falta alguna imagen por adjuntar
                    global.imprimirMensajeEnHilo("Faltan imagenes por adjuntar.");
                }
            }else{
                //Actualizara la supervision
                almacenarImagenes = true;
            }

            if(almacenarImagenes){
                //Cumplen las validaciones de imagenes a capturar - Validar fotografias tomadas

                if(ivKilometrajeManana.getDrawable() != null && tomoFotoKilometraje1.equals("1")){
                    rutaFotoKilometrajeManana = camara.guardarImagenGaleria(ivKilometrajeManana, "Kilometraje1-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivKilometrajeTarde.getDrawable() != null && tomoFotoKilometraje2.equals("1")){
                    rutaFotoKilometrajeTarde = camara.guardarImagenGaleria(ivKilometrajeTarde, "Kilometraje2-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivMotoLadoIzquierdo.getDrawable() != null && tomoFotoLadoIzquierdo.equals("1")){
                    rutaFotoLadoIzquierdo = camara.guardarImagenGaleria(ivMotoLadoIzquierdo, "LadoDerecho-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivMotoLadoDerecho.getDrawable() != null && tomoFotoLadoDerecho.equals("1")){
                    rutaFotoLadoDerecho = camara.guardarImagenGaleria(ivMotoLadoDerecho, "LadoDerecho-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivMotoFrente.getDrawable() != null && tomoFotoFrente.equals("1")){
                    rutaFotoFrente = camara.guardarImagenGaleria(ivMotoFrente, "Frente-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivMotoAtras.getDrawable() != null && tomoFotoAtras.equals("1")){
                    rutaFotoAtras = camara.guardarImagenGaleria(ivMotoAtras, "Atras-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivMotoFotoExtra1.getDrawable() != null && tomoFotoExtra1.equals("1")){
                    rutaFotoExtra1 = camara.guardarImagenGaleria(ivMotoFotoExtra1, "Extra1-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivMotoFotoExtra2.getDrawable() != null && tomoFotoExtra2.equals("1")){
                    rutaFotoExtra2 = camara.guardarImagenGaleria(ivMotoFotoExtra2, "Extra2-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivMotoFotoExtra3.getDrawable() != null && tomoFotoExtra3.equals("1")){
                    rutaFotoExtra3 = camara.guardarImagenGaleria(ivMotoFotoExtra3, "Extra3-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivMotoFotoExtra4.getDrawable() != null && tomoFotoExtra4.equals("1")){
                    rutaFotoExtra4 = camara.guardarImagenGaleria(ivMotoFotoExtra4, "Extra4-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivMotoFotoExtra5.getDrawable() != null && tomoFotoExtra5.equals("1")){
                    rutaFotoExtra5 = camara.guardarImagenGaleria(ivMotoFotoExtra5, "Extra5-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }if(ivMotoFotoExtra6.getDrawable() != null && tomoFotoExtra6.equals("1")){
                    rutaFotoExtra6 = camara.guardarImagenGaleria(ivMotoFotoExtra6, "Extra6-Vehiculos-Supervision" + numSerieVehiculo + "-" +
                            fechaActual.replace("-", "") + time.replace(":", ""));
                }

                registrarSupervisionVehicular(idSupervisionVehicular, rutaFotoKilometrajeManana, rutaFotoKilometrajeTarde, rutaFotoLadoIzquierdo, rutaFotoLadoDerecho,
                        rutaFotoFrente, rutaFotoAtras, rutaFotoExtra1, rutaFotoExtra2, rutaFotoExtra3, rutaFotoExtra4, rutaFotoExtra5, rutaFotoExtra6);
            }
        }else {
            //No se cuenta con internet
            Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
        }

    }

    private void decodificarJsonEInsertarDatosSupervision(String json) {

        JSONArray jsonArray = null;
        try {
            if (!json.equals("null")) {
                jsonArray = new JSONArray(json);
            }
        } catch (JSONException e) {
            global.escribirError(e, 283);
            e.printStackTrace();
        }

        if(jsonArray != null) {

            try {

                try {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ContentValues valores = new ContentValues();
                        sqLiteDB = conexion.getWritableDatabase();
                        idSupervisionVehicular = ((JSONObject) jsonArray.get(i)).optString("indice");
                        numSerieVehiculo = ((JSONObject) jsonArray.get(i)).optString("numserie");
                        valores.put("INDICE", idSupervisionVehicular);
                        valores.put("ID_FRANQUICIA", ((JSONObject) jsonArray.get(i)).optString("id_franquicia"));
                        valores.put("ID_USUARIO", ((JSONObject) jsonArray.get(i)).optString("id_usuario"));
                        valores.put("ID_VEHICULO", ((JSONObject) jsonArray.get(i)).optString("id_vehiculo"));
                        valores.put("NUMSERIE", numSerieVehiculo);
                        valores.put("ESTADO", ((JSONObject) jsonArray.get(i)).optString("estado"));
                        valores.put("KILOMETRAJE1", ((JSONObject) jsonArray.get(i)).optString("kilometraje1"));
                        valores.put("KILOMETRAJE2", ((JSONObject) jsonArray.get(i)).optString("kilometraje2"));
                        valores.put("LADOIZQUIERDO", ((JSONObject) jsonArray.get(i)).optString("ladoizquierdo"));
                        valores.put("LADODERECHO", ((JSONObject) jsonArray.get(i)).optString("ladoderecho"));
                        valores.put("FRENTE", ((JSONObject) jsonArray.get(i)).optString("frente"));
                        valores.put("ATRAS", ((JSONObject) jsonArray.get(i)).optString("atras"));
                        valores.put("EXTRA1", ((JSONObject) jsonArray.get(i)).optString("extra1"));
                        valores.put("EXTRA2", ((JSONObject) jsonArray.get(i)).optString("extra2"));
                        valores.put("EXTRA3", ((JSONObject) jsonArray.get(i)).optString("extra3"));
                        valores.put("EXTRA4", ((JSONObject) jsonArray.get(i)).optString("extra4"));
                        valores.put("EXTRA5", ((JSONObject) jsonArray.get(i)).optString("extra5"));
                        valores.put("EXTRA6", ((JSONObject) jsonArray.get(i)).optString("extra6"));
                        sqLiteDB.insert("VEHICULOSSUPERVISION", null, valores);
                    }

                    if(sqLiteDB.isOpen()){
                        sqLiteDB.close();
                    }


                } catch (JSONException e) {
                    global.escribirError(e, 284);
                    e.printStackTrace();
                }

            } catch (Exception e) {
                global.escribirError(e, 285);
                Log.i("ERRORBD", e.getMessage() + "");
            }

        }

    }

    public boolean validarImagenesSupervisionDiaSemana(){

        boolean banderaValidacion = true;
        //Validar dia de la semana
        String fechaSecciones[] = fechaActual.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(fechaSecciones[0]));
        calendar.set(Calendar.MONTH,Integer.parseInt(fechaSecciones[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(fechaSecciones[2]));

        // Obtener el día de la semana
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        //1 -> domingo
        //2 -> Lunes....
        //6 -> Viernes
        //7 -> sabado
        if(diaSemana == 6){

            //Es viernes - Obligatorias 6 primeras imagenes - Foto kilometraje es evaluada con peticion al servidor

            if(ivMotoLadoIzquierdo.getDrawable() == null){
                tvMotoLadoIzquierdo.setVisibility(View.VISIBLE);
                banderaValidacion = false;
            }else{
                tvMotoLadoIzquierdo.setVisibility(View.GONE);
            }

            if(ivMotoLadoDerecho.getDrawable() == null){
                tvMotoLadoDerecho.setVisibility(View.VISIBLE);
                banderaValidacion = false;
            }else{
                tvMotoLadoDerecho.setVisibility(View.GONE);
            }

            if(ivMotoFrente.getDrawable() == null){
                tvMotoFrente.setVisibility(View.VISIBLE);
                banderaValidacion = false;
            }else{
                tvMotoFrente.setVisibility(View.GONE);
            }

            if(ivMotoAtras.getDrawable() == null){
                tvMotoAtras.setVisibility(View.VISIBLE);
                banderaValidacion = false;
            }else{
                tvMotoAtras.setVisibility(View.GONE);
            }

        }

        return banderaValidacion;
    }

    public void descargarMostrarImagenSupervision() {
        if(conexionInternet.verificarConexionInternet()) {
            //Se cuenta con internet
            int numeroFotos = 12;
            String urlFotoSupervision = "";
            ImageView imagenSupervision = new ImageView(fragmento.getActivity());

            //Recorrer todas las imagenes para descargar cada una de las imagenes existentes
            for (int i = 0; i < numeroFotos; i++){

                switch (i) {
                    case 0:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","KILOMETRAJE1","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivKilometrajeManana;
                        break;
                    case 1:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","KILOMETRAJE2","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivKilometrajeTarde;
                        break;
                    case 2:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","LADOIZQUIERDO","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivMotoLadoIzquierdo;
                        break;
                    case 3:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","LADODERECHO","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivMotoLadoDerecho;
                        break;
                    case 4:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","FRENTE","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivMotoFrente;
                        break;
                    case 5:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","ATRAS","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivMotoAtras;
                        break;
                    case 6:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","EXTRA1","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivMotoFotoExtra1;
                        break;
                    case 7:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","EXTRA2","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivMotoFotoExtra2;
                        break;
                    case 8:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","EXTRA3","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivMotoFotoExtra3;
                        break;
                    case 9:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","EXTRA4","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivMotoFotoExtra4;
                        break;
                    case 10:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","EXTRA5","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivMotoFotoExtra5;
                        break;
                    case 11:
                        urlFotoSupervision = global.obtenerAtributoTabla("VEHICULOSSUPERVISION","EXTRA6","ID_USUARIO",obtenerRol.obtenerAtributoUsuarioLogeado("ID"));
                        imagenSupervision = ivMotoFotoExtra6;
                        break;
                }

                if (urlFotoSupervision.length() > 0) {

                    try {
                        //Limpiar imagen default para imageView
                        imagenSupervision.setBackground(null);
                        //Descargar imagen
                        Picasso.get()
                                .load("https://adminlabo.luzatuvida.com.mx/uploads/imagenes/" + llaves.carpeta_vehiculos_ftp + "/supervisionimagenes/" +urlFotoSupervision)
                                .into(imagenSupervision, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }
                                    @Override
                                    public void onError(Exception e) {
                                        // Ocurrió un error al cargar la imagen o no existe en el servidor
                                    }
                                });

                    } catch (Exception e) {
                        global.escribirError(e, 286);
                        Log.i("ERROR", e.toString());
                    }

                }
            }
        }else {
            //No se cuenta con internet
            Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
        }

    }

    public void insertarImagenesPendientesSupervision(String Kilometraje1, String Kilometraje2, String ladoizquierdo, String ladoderecho, String frente, String atras,
                                                      String extra1, String extra2, String extra3, String extra4, String extra5, String extra6){
        try {

            Kilometraje1 = (Kilometraje1.length() > 0)? "0" : "2";
            Kilometraje2 = (Kilometraje2.length() > 0)? "0" : "2";
            ladoizquierdo = (ladoizquierdo.length() > 0)? "0" : "2";
            ladoderecho = (ladoderecho.length() > 0)? "0" : "2";
            frente = (frente.length() > 0)? "0" : "2";
            atras = (atras.length() > 0)? "0" : "2";
            extra1 = (extra1.length() > 0)? "0" : "2";
            extra2 = (extra2.length() > 0)? "0" : "2";
            extra3 = (extra3.length() > 0)? "0" : "2";
            extra4 = (extra4.length() > 0)? "0" : "2";
            extra5 = (extra5.length() > 0)? "0" : "2";
            extra6 = (extra6.length() > 0)? "0" : "2";

            ContentValues valores = new ContentValues();
            sqLiteDB = conexion.getWritableDatabase();

            valores.put("INDICE_SUPERVISION", idSupervisionVehicular);
            valores.put("KILOMETRAJE1", Kilometraje1);
            valores.put("KILOMETRAJE2", Kilometraje2);
            valores.put("LADOIZQUIERDO", ladoizquierdo);
            valores.put("LADODERECHO", ladoderecho);
            valores.put("FRENTE", frente);
            valores.put("ATRAS", atras);
            valores.put("EXTRA1", extra1);
            valores.put("EXTRA2", extra2);
            valores.put("EXTRA3", extra3);
            valores.put("EXTRA4", extra4);
            valores.put("EXTRA5", extra5);
            valores.put("EXTRA6", extra6);
            sqLiteDB.insert("IMAGENESCARGADASSUPERVISION", null, valores);

            if(sqLiteDB.isOpen()){
                sqLiteDB.close();
            }

            global.actualizarAtributoTabla("GLOBAL", "SUBIRIMAGENESSUPERVISIONFTP", "1", "ID", "1");

        } catch (Exception e) {
            global.escribirError(e, 290);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    public int obtenerNumeroImagenesSupervisionNoSubidasFTP(){
        int numeroImagenes = 0;
        String condicion = " WHERE INDICE = '" + idSupervisionVehicular + "'";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT (SELECT COUNT(KILOMETRAJE1) FROM IMAGENESCARGADASSUPERVISION WHERE KILOMETRAJE1 IN (0,1)) +" +
                    " (SELECT COUNT(KILOMETRAJE2) FROM IMAGENESCARGADASSUPERVISION WHERE KILOMETRAJE2 IN (0,1)) +" +
                    " (SELECT COUNT(LADOIZQUIERDO) FROM IMAGENESCARGADASSUPERVISION WHERE LADOIZQUIERDO IN (0,1)) + " +
                    " (SELECT COUNT(LADODERECHO) FROM IMAGENESCARGADASSUPERVISION WHERE LADODERECHO IN (0,1)) + " +
                    " (SELECT COUNT(FRENTE) FROM IMAGENESCARGADASSUPERVISION WHERE FRENTE IN (0,1)) + " +
                    " (SELECT COUNT(ATRAS) FROM IMAGENESCARGADASSUPERVISION WHERE ATRAS IN (0,1)) +" +
                    " (SELECT COUNT(EXTRA1) FROM IMAGENESCARGADASSUPERVISION WHERE EXTRA1 IN (0,1)) +" +
                    " (SELECT COUNT(EXTRA2) FROM IMAGENESCARGADASSUPERVISION WHERE EXTRA2 IN (0,1)) +" +
                    " (SELECT COUNT(EXTRA3) FROM IMAGENESCARGADASSUPERVISION WHERE EXTRA3 IN (0,1)) +" +
                    " (SELECT COUNT(EXTRA4) FROM IMAGENESCARGADASSUPERVISION WHERE EXTRA4 IN (0,1)) +" +
                    " (SELECT COUNT(EXTRA5) FROM IMAGENESCARGADASSUPERVISION WHERE EXTRA5 IN (0,1)) +" +
                    " (SELECT COUNT(EXTRA6) FROM IMAGENESCARGADASSUPERVISION WHERE EXTRA6 IN (0,1)) " +
                    " FROM VEHICULOSSUPERVISION " + condicion;
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            while(datos.moveToNext()){
                numeroImagenes += datos.getInt(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 291);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return numeroImagenes;
    }

    public void subirImagenesAFTPSupervisionVehicular(){

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();

            String SQL = "SELECT IFNULL((SELECT V.KILOMETRAJE1 FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.KILOMETRAJE1 IN (0,1)),''), " +
                  "IFNULL((SELECT V.KILOMETRAJE2 FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.KILOMETRAJE2 IN (0,1)),''), " +
                  "IFNULL((SELECT V.LADOIZQUIERDO FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.LADOIZQUIERDO IN (0,1)),''), " +
                  "IFNULL((SELECT V.LADODERECHO FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.LADODERECHO IN (0,1)),''), " +
                  "IFNULL((SELECT V.FRENTE FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.FRENTE IN (0,1)),''), " +
                  "IFNULL((SELECT V.ATRAS FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.ATRAS IN (0,1)),''), " +
                  "IFNULL((SELECT V.EXTRA1 FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.EXTRA1 IN (0,1)),''), " +
                  "IFNULL((SELECT V.EXTRA2 FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.EXTRA2 IN (0,1)),''), " +
                  "IFNULL((SELECT V.EXTRA3 FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.EXTRA3 IN (0,1)),''), " +
                  "IFNULL((SELECT V.EXTRA4 FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.EXTRA4 IN (0,1)),''), " +
                  "IFNULL((SELECT V.EXTRA5 FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.EXTRA5 IN (0,1)),''), " +
                  "IFNULL((SELECT V.EXTRA6 FROM VEHICULOSSUPERVISION V WHERE V.INDICE = I.INDICE_SUPERVISION AND I.EXTRA6 IN (0,1)),'') " +
                  "FROM IMAGENESCARGADASSUPERVISION I WHERE I.INDICE_SUPERVISION = (SELECT INDICE FROM VEHICULOSSUPERVISION LIMIT 1) ORDER BY INDICE_SUPERVISION DESC LIMIT 1";

            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No hay supervision para subir fotos a FTP");
            }

            while(datos.moveToNext()){

                //Subir imagenes a FTP
                validacionImagenesFTP(datos.getString(0), datos.getString(1), datos.getString(2),
                        datos.getString(3), datos.getString(4), datos.getString(5),
                        datos.getString(6), datos.getString(7), datos.getString(8), datos.getString(9),
                        datos.getString(10), datos.getString(11));
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 292);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    public void validacionImagenesFTP(String Kilometraje1, String Kilometraje2, String ladoizquierdo, String ladoderecho, String frente, String atras,
                                      String extra1, String extra2, String extra3, String extra4, String extra5, String extra6) {

        String[] tablavalidacion = new String[] {
                global.obtenerUnResultadoQuery("SELECT KILOMETRAJE1 FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT KILOMETRAJE2 FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT LADOIZQUIERDO FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT LADODERECHO FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT FRENTE FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT ATRAS FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT EXTRA1 FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT EXTRA2 FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT EXTRA3 FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT EXTRA4 FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT EXTRA5 FROM IMAGENESCARGADASSUPERVISION"),
                global.obtenerUnResultadoQuery("SELECT EXTRA6 FROM IMAGENESCARGADASSUPERVISION")};

        String[] imagenes = new String[] {Kilometraje1, Kilometraje2,
                ladoizquierdo, ladoderecho, frente, atras,
                extra1, extra2, extra3, extra4, extra5, extra6};

        String[] fotos = new String[]{"Kilometraje1", "Kilometraje2",
                "ladoizquierdo", "ladoderecho", "frente", "atras",
                "extra1", "extra2", "extra3", "extra4", "extra5", "extra6"};

        String carpeta ="supervisionimagenes";

        List<String> imagenesPrecargadas = new ArrayList<>();
        List<String> carpetasPrecargadas = new ArrayList<>();
        List<String> columnaTabla = new ArrayList<>();

        for (int i = 0; i < imagenes.length; i++) {
            if (imagenes[i].length() != 0) {
                //Imagenes es diferente de ""

                if(tablavalidacion[i].equals("0")) {
                    File direccionAntesDeCargar = new File(direccionNoMedia + "/" + imagenes[i]);
                        Sincronizacion.subirArchivoSupervisionFtp cargar = new Sincronizacion.subirArchivoSupervisionFtp(direccionAntesDeCargar.getAbsolutePath(), imagenes[i], carpeta, fotos[i], idSupervisionVehicular);
                        cargar.execute();
                }else if(tablavalidacion[i].equals("1")) {
                    imagenesPrecargadas.add(imagenes[i]);
                    carpetasPrecargadas.add(carpeta);
                    columnaTabla.add(fotos[i]);
                }

            }
        }

        if(imagenesPrecargadas.size() != 0) {
            Sincronizacion.verificarSiExisteArchivoSupervisionFTP cargar = new Sincronizacion.verificarSiExisteArchivoSupervisionFTP(imagenesPrecargadas, carpetasPrecargadas, columnaTabla, idSupervisionVehicular);
            cargar.execute();
        }

    }

    private void mostrarOcultarCamposImagenesSupervision(boolean visible){
        if(visible){
            //Mostrar imagenView
            llKilometrajeManana.setVisibility(View.VISIBLE);
            llKilometrajeTarde.setVisibility(View.VISIBLE);
            llFotoLadoIzquierdo.setVisibility(View.VISIBLE);
            llFotoLadoDerecho.setVisibility(View.VISIBLE);
            llFotoFrente.setVisibility(View.VISIBLE);
            llFotoAtras.setVisibility(View.VISIBLE);
            llFotoExtra1.setVisibility(View.VISIBLE);
            llFotoExtra2.setVisibility(View.VISIBLE);
            llFotoExtra3.setVisibility(View.VISIBLE);
            llFotoExtra4.setVisibility(View.VISIBLE);
            llFotoExtra5.setVisibility(View.VISIBLE);
            llFotoExtra6.setVisibility(View.VISIBLE);
        }else{
            //Ocultar ImagenView
            llKilometrajeManana.setVisibility(View.GONE);
            llKilometrajeTarde.setVisibility(View.GONE);
            llFotoLadoIzquierdo.setVisibility(View.GONE);
            llFotoLadoDerecho.setVisibility(View.GONE);
            llFotoFrente.setVisibility(View.GONE);
            llFotoAtras.setVisibility(View.GONE);
            llFotoExtra1.setVisibility(View.GONE);
            llFotoExtra2.setVisibility(View.GONE);
            llFotoExtra3.setVisibility(View.GONE);
            llFotoExtra4.setVisibility(View.GONE);
            llFotoExtra5.setVisibility(View.GONE);
            llFotoExtra6.setVisibility(View.GONE);
        }

    }

    public void redireccionarLogin(){
        Fragment verificadorFragment;
        FragmentTransaction transaction;
        fragmento.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        verificadorFragment = new inicioSesion();
        transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
        ((MainActivity) fragmento.getActivity()).setTitle("");
        ((MainActivity) fragmento.getActivity()).lockDrawer();
        ((MainActivity) fragmento.getActivity()).hideMenuHamburguesa();
        llBuscador.setVisibility(View.GONE);
        llBuscadorIcono.setVisibility(View.GONE);
        llProgress.setVisibility(View.GONE);
    }

    public void reiniciarBanderasFotosTomadas(){
        tomoFotoKilometraje1 = "0";
        tomoFotoKilometraje2 = "0";
        tomoFotoLadoIzquierdo = "0";
        tomoFotoLadoDerecho = "0";
        tomoFotoFrente = "0";
        tomoFotoAtras = "0";
        tomoFotoExtra1 = "0";
        tomoFotoExtra2 = "0";
        tomoFotoExtra3 = "0";
        tomoFotoExtra4 = "0";
        tomoFotoExtra5 = "0";
    }

    public void vaciarImagenesTomadasRecientemente(){
        if(tomoFotoKilometraje1 == "1"){
            ivKilometrajeManana.setImageDrawable(null);
            ivKilometrajeManana.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.kilometraje));
        }
        if(tomoFotoKilometraje2 == "1"){
            ivKilometrajeTarde.setImageDrawable(null);
            ivKilometrajeTarde.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.kilometraje));
        }
        if(tomoFotoLadoIzquierdo == "1"){
            ivMotoLadoIzquierdo.setImageDrawable(null);
            ivMotoLadoIzquierdo.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.motoizq));
        }
        if(tomoFotoLadoDerecho == "1"){
            ivMotoLadoDerecho.setImageDrawable(null);
            ivMotoLadoDerecho.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.motoder));
        }
        if(tomoFotoFrente == "1"){
            ivMotoFrente.setImageDrawable(null);
            ivMotoFrente.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.motofrente));
        }
        if(tomoFotoAtras == "1"){
            ivMotoAtras.setImageDrawable(null);
            ivMotoAtras.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.motoatras));
        }
        if(tomoFotoExtra1 == "1"){
            ivMotoFotoExtra1.setImageDrawable(null);
            ivMotoFotoExtra1.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.extra));
        }
        if(tomoFotoExtra2 == "1"){
            ivMotoFotoExtra2.setImageDrawable(null);
            ivMotoFotoExtra2.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.extra));
        }
        if(tomoFotoExtra3 == "1"){
            ivMotoFotoExtra3.setImageDrawable(null);
            ivMotoFotoExtra3.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.extra));
        }
        if(tomoFotoExtra4 == "1"){
            ivMotoFotoExtra4.setImageDrawable(null);
            ivMotoFotoExtra4.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.extra));
        }
        if(tomoFotoExtra5 == "1"){
            ivMotoFotoExtra5.setImageDrawable(null);
            ivMotoFotoExtra5.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.extra));
        }
        if(tomoFotoExtra6 == "1"){
            ivMotoFotoExtra6.setImageDrawable(null);
            ivMotoFotoExtra6.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.extra));
        }
    }
}
