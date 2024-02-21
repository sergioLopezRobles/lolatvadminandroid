package com.luzatuvida.lolatvadminandroid.global;

import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
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
import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.vista.inicioSesion;
import com.luzatuvida.lolatvadminandroid.vista.supervisionVehicular;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sincronizacion {

    /*
    Identificador error del 77 al 102
     */

    Fragment fragmento;
    baseDeDatos conexion;
    SQLiteDatabase sqLiteDB;
    static Global global;
    static Llaves llaves;
    public String token;
    String correo;
    String contrasena;
    String file_path;
    File direccionNoMedia;
    int num;
    Internet internet;
    Toolbar toolbar;
    TextView tvPorcentajeProgreso;
    LinearLayout llProgress, llBuscador, llBuscadorIcono;
    ObtenerRol obtenerRol;
    int cierresesion;
    boolean sincronizar;
    Seguridad seguridad;

    public Sincronizacion(Fragment fragmento,boolean sincronizar) {
        this.fragmento = fragmento;
        global = new Global(fragmento);
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
        file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        direccionNoMedia = new File(file_path + "/luzatuvida/.nomedia");
        internet = new Internet(fragmento.getActivity());
        toolbar = fragmento.getActivity().findViewById(R.id.toolbar);
        tvPorcentajeProgreso = toolbar.findViewById(R.id.tvPorcentajeProgreso);
        llProgress = toolbar.findViewById(R.id.llProgress);
        llBuscador = toolbar.findViewById(R.id.llBuscador);
        llBuscadorIcono = toolbar.findViewById(R.id.llBuscadorIcono);
        obtenerRol = new ObtenerRol(fragmento);
        llaves.ruta_ftp = obtenerRol.obtenerAtributoUsuarioLogeado("RUTA_FTP");
        llaves.usuario_ftp = obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO_FTP");
        llaves.contrasena_ftp = obtenerRol.obtenerAtributoUsuarioLogeado("CONTRASENA_FTP");
        llaves.preciodolar = obtenerRol.obtenerAtributoUsuarioLogeado("PRECIODOLAR");
        if (llaves.preciodolar.equals("")) {
            llaves.preciodolar = "18"; //Valor por default en caso de que no se tuviera nada en ese atributo
        }
        seguridad = new Seguridad(fragmento);
        this.sincronizar = sincronizar;
    }

    /*Metodo/Funcion: sincronizarMetodo
      Descripcion: Verifica que tenga acceso a intenet y manda a llamar los metodos para sincronizar
    */
    public void sincronizarMetodo(int opcion, Object[] objetos, int cierresesion) {

        //opcion
        //0 -> Sincronizar datos (servicio web) (contrato actual y contratos anteriores) y subir imagenes de los contratos terminados (sin correo y contrasena)
        //1 -> Sincronizar datos y debe mandarse el mensaje que es necesario actualizar la aplicacion (con correo y contrasena)
        //2 -> Sincronizar datos (servicio web), eliminar registros de las tablas y cargar datos WebService a tablas (con correo y contrasena)

        if(internet.verificarConexionInternet()) {
            //Acceso a internet

            this.token = (String) objetos[0];
            this.correo = (String) objetos[1];
            this.contrasena = (String) objetos[2];
            this.num = opcion;
            this.cierresesion = cierresesion;

            //Se muestra el ProgressBar
            llProgress.setVisibility(View.VISIBLE);
            tvPorcentajeProgreso.setText("Sincronizando...");

            sincronizar0();

        }

    }

    /*Metodo/Funcion: sincronizar0
      Descripcion: Se obtiene el JSON con los datos a enviar a la pagina y se hace a peticion correspondiente
    */
    private void sincronizar0() {

        JSONObject jsonGeneral = obtenerJSONGeneral();

        if(jsonGeneral.toString().length() > 2) {
            //Si jsonGeneral trae datos

            //PETICION AL SERVIDOR WEB (SINCRONIZAR0)
            RequestQueue requestQueue = Volley.newRequestQueue(fragmento.getContext());
            JSONObject parametrosJSON = new JSONObject();

            try {

                parametrosJSON.put("token", token);
                parametrosJSON.put("dispositivo", llaves.dispositivo);
                parametrosJSON.put("idunico", Settings.Secure.getString(fragmento.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                parametrosJSON.put("version", Build.VERSION.RELEASE);
                parametrosJSON.put("modelo", Build.MODEL);
                parametrosJSON.put("datos", "[" + jsonGeneral + "]");

                JSONObject jsonArrayAbonosArchivo = new JSONObject();
                File archivoMovimientos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/luzatuvida/.nomedia/movimientos.txt");
                if (archivoMovimientos.exists()) {
                    //Existe archivo movimientos
                    jsonArrayAbonosArchivo = global.obtenerAbonosArchivoMovimientosTxt(5);
                }
                parametrosJSON.put("datosarchivo", "[" + jsonArrayAbonosArchivo + "]");

                //Sincronizar historiales
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

                JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST, llaves.url_sincronizar_0, parametrosJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Fragment verificadorFragment;
                            FragmentTransaction transaction;
                            switch (obtenerAtributoJSONDecodificado(response.get("datos").toString(), "codigo")) { // Nos llega un codigo de respuesta
                                case "Ppts8qWkkqosQQqRKlMz":
                                    global.imprimirMensajeEnHilo("No se pudo sincronizar con el servidor necesitas permisos adicionales");
                                    llProgress.setVisibility(View.GONE);
                                    break;
                                case "JGvcYgZn8PD4KpIm4uIN": //Se sincronizo informacion correctamente
                                    actualizarAtributoEnviadoPaginaTablas();
                                    llProgress.setVisibility(View.GONE);
                                    if(num == 0) {
                                        if (cierresesion == 1) {
                                            cierresesion();
                                        }
                                    }
                                    if(num == 2) {
                                        llProgress.setVisibility(View.VISIBLE);
                                        sincronizar2();
                                    }
                                    break;
                                case "AUeYOgyZnyTAJXJMA59u": //Aplicacion ya no existe pero el token es valido y se debera subir la informacion
                                    actualizarAtributoEnviadoPaginaTablas();
                                    borrarRegistroTablaMovil(obtenerRol.obtenerAtributoUsuarioLogeado("ID")); //Borrar registro en la tabla MOVIL excepto CORREO

                                    //Redireccionar a Login
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
                                    global.imprimirMensajeEnHilo("Es necesario actualizar aplicacion - se subio correctamente la información");
                                    break;
                                case "P1zv7ZFD8dOvOTQVUTys": //Token no valido redireccionar al Login. (Borrar datos de login)
                                    borrarRegistroTablaMovil(obtenerRol.obtenerAtributoUsuarioLogeado("ID")); //Borrar registro en la tabla MOVIL excepto CORREO

                                    //Redireccionar a Login
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
                                    global.imprimirMensajeEnHilo("Sesión caducada - favor de iniciar sesión de nuevo");
                                    break;
                                default:
                                    global.imprimirMensajeEnHilo("No se pudo sincronizar");
                            }

                            Log.i("MENSAJE", "SINCRONIZAR0");

                        } catch (Exception e) {
                            global.escribirError(e, 77);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error instanceof TimeoutError) {
                                imprimirMensaje("Error al intentar conectar al servidor, se acabo el tiempo de espera (Sin datos disponibles/sin señal/sin conexion a internet por wifi) Error:0901");
                            }else if(error instanceof NoConnectionError){
                                imprimirMensaje("No se pudo conectar(Sin señal/servicio)");
                            } else if (error instanceof AuthFailureError) {
                                imprimirMensaje("Error de autenticacion");
                            } else if (error instanceof ServerError) {
                                imprimirMensaje("No se pudo conectar al servidor");
                            } else if (error instanceof NetworkError) {
                                imprimirMensaje("Error de red/señal");
                            } else if (error instanceof ParseError) {
                                imprimirMensaje("Ocurrio un error al intentar conectar");
                            }else{
                                imprimirMensaje("Error al intentar conectar");
                            }
                            llProgress.setVisibility(View.GONE);
                            //End
                        } catch (Exception e) {
                            global.escribirError(e, 78);
                            imprimirMensaje("Error: "+e.toString());
                            llProgress.setVisibility(View.GONE);
                        }
                    }
                });

                peticion.setRetryPolicy(new DefaultRetryPolicy(
                        60000,
                        10,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(peticion);

            } catch (Exception e) {
                global.escribirError(e, 79);
                Log.i("ERRORJSON", "" + e);
            }

        }else {
            if(num == 0) {
                llProgress.setVisibility(View.GONE);
                if (cierresesion == 1) {
                    cierresesion();
                }
            }else if(num == 2) {
                sincronizar2();
            }
        }
    }

    /*Metodo/Funcion: obtenerJSONGeneral
      Descripcion: Obtener el JSON con los datos a enviar a la pagina
    */
    private JSONObject obtenerJSONGeneral() {

        JSONArray jsonArrayContratos = sincronizarTabla(1);
        JSONArray jsonArrayHistorialesClinicos = sincronizarTabla(2);
        JSONArray jsonArrayAbonos = sincronizarTabla(3);
        JSONArray jsonArrayContratoProducto = sincronizarTabla(4);
        JSONArray jsonArrayAbonosEliminados = sincronizarTabla(5);
        JSONArray jsonArrayProductosEliminados = sincronizarTabla(6);
        JSONArray jsonArrayHistorialContratos = sincronizarTabla(7);
        JSONArray jsonArrayPromocionContratos = sincronizarTabla(8);
        JSONArray jsonArrayPayments = sincronizarTabla(9);
        JSONArray jsonArrayPromocionesEliminadas = sincronizarTabla(10);
        JSONArray jsonArrayDatosStripe = sincronizarTabla(11);
        JSONArray jsonArrayGarantias = sincronizarTabla(12);
        JSONArray jsonArrayRuta = sincronizarTabla(13);
        JSONArray jsonArrayHistorialSinConversion = sincronizarTabla(14);
        JSONArray jsonArrayBuzon = sincronizarTabla(15);
        JSONArray jsonArrayAutorizacionesArmazon = sincronizarTabla(16);
        JSONArray jsonArrayHistorialFotosContrato = sincronizarTabla(17);
        JSONArray jsonArrayContratosListaNegra = sincronizarTabla(18);
        JSONArray jsonArrayNotasCobranza = sincronizarTabla(19);
        JSONArray jsonArrayAsistencia = sincronizarTabla(20);

        //Se guarda en el JSON general
        JSONObject jsonGeneral = new JSONObject();

        //Se encripta el JSON en base64
        try {

            if(jsonArrayContratos.toString().length() > 2) {
                jsonGeneral.put("contratos", new String(Base64.encode(jsonArrayContratos.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayHistorialesClinicos.toString().length() > 2) {
                jsonGeneral.put("historialesclinicos", new String(Base64.encode(jsonArrayHistorialesClinicos.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayAbonos.toString().length() > 2) {
                jsonGeneral.put("abonos", new String(Base64.encode(jsonArrayAbonos.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayContratoProducto.toString().length() > 2) {
                jsonGeneral.put("contratosproductos", new String(Base64.encode(jsonArrayContratoProducto.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayAbonosEliminados.toString().length() > 2) {
                jsonGeneral.put("abonoseliminados", new String(Base64.encode(jsonArrayAbonosEliminados.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayProductosEliminados.toString().length() > 2) {
                jsonGeneral.put("productoseliminados", new String(Base64.encode(jsonArrayProductosEliminados.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayHistorialContratos.toString().length() > 2) {
                jsonGeneral.put("historialcontratos", new String(Base64.encode(jsonArrayHistorialContratos.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayPromocionContratos.toString().length() > 2) {
                jsonGeneral.put("promocioncontratos", new String(Base64.encode(jsonArrayPromocionContratos.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayPayments.toString().length() > 2) {
                jsonGeneral.put("payments", new String(Base64.encode(jsonArrayPayments.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayPromocionesEliminadas.toString().length() > 2) {
                jsonGeneral.put("promocioneseliminadas", new String(Base64.encode(jsonArrayPromocionesEliminadas.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayDatosStripe.toString().length() > 2) {
                jsonGeneral.put("datosstripe", new String(Base64.encode(jsonArrayDatosStripe.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayGarantias.toString().length() > 2) {
                jsonGeneral.put("garantias", new String(Base64.encode(jsonArrayGarantias.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayRuta.toString().length() > 2) {
                jsonGeneral.put("ruta", new String(Base64.encode(jsonArrayRuta.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayHistorialSinConversion.toString().length() > 2) {
                jsonGeneral.put("historialessinconversion", new String(Base64.encode(jsonArrayHistorialSinConversion.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayBuzon.toString().length() > 2) {
                jsonGeneral.put("buzon", new String(Base64.encode(jsonArrayBuzon.toString().getBytes(), Base64.DEFAULT)));
            }
            if(jsonArrayAutorizacionesArmazon.toString().length() > 2) {
                jsonGeneral.put("autorizacionesarmazon", new String(Base64.encode(jsonArrayAutorizacionesArmazon.toString().getBytes(), Base64.DEFAULT)));
            }if(jsonArrayHistorialFotosContrato.toString().length() > 2) {
                jsonGeneral.put("historialfotoscontratos", new String(Base64.encode(jsonArrayHistorialFotosContrato.toString().getBytes(), Base64.DEFAULT)));
            }if(jsonArrayContratosListaNegra.toString().length() > 2) {
                jsonGeneral.put("contratoslistanegra", new String(Base64.encode(jsonArrayContratosListaNegra.toString().getBytes(), Base64.DEFAULT)));
            }if(jsonArrayNotasCobranza.toString().length() > 2) {
                jsonGeneral.put("notascobranza", new String(Base64.encode(jsonArrayNotasCobranza.toString().getBytes(), Base64.DEFAULT)));
            }if(jsonArrayAsistencia.toString().length() > 2) {
                jsonGeneral.put("asistencia", new String(Base64.encode(jsonArrayAsistencia.toString().getBytes(), Base64.DEFAULT)));
            }

        } catch (JSONException e) {
            global.escribirError(e, 80);
            e.printStackTrace();
        }

        return jsonGeneral;
    }

    /*Metodo/Funcion: borrarRegistroTablaMovil
      Descripcion: Actualiza la mayoria de los campos de la tabla movil en vacio
    */
    public void borrarRegistroTablaMovil(String id) {
        try{

            sqLiteDB = conexion.getWritableDatabase();
            String consulta = "UPDATE MOVIL SET ID = '', USUARIO = '', CONTRASENA = '', TOKEN = '', ROL = '', ID_ZONA = '', FECHAACTUAL = '', USUARIOLOGEADO = ''" +
                    " WHERE ID='" + seguridad.cifrar((id).getBytes("UTF-16LE")) + "'";
            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (Exception e){
            global.escribirError(e, 81);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }
    }

    /*Metodo/Funcion: sincronizarTabla
      Descripcion: Se obtiene un JSONArray de las tablas correspondientes con los datos que se mandaran a la pagina
    */
    private JSONArray sincronizarTabla(int numTabla) {

        //1 -> Contratos
        //2 -> Historial Clinico
        //3 -> Abonos
        //4 -> ContratoProducto
        //5 -> Eliminacion de abonos
        //6 -> Eliminacion de productos
        //7 -> HistorialContrato
        //8 -> PromocionContrato
        //9 -> Payments
        //10 -> PromocionesEliminadas
        //11 -> DatosStripe
        //12 -> Garantias
        //13 -> Ruta
        //14 -> Historiales sin conversion
        //15 -> Buzon quejas/sugerencias
        //16 -> Autorizaciones
        //17 -> HistorialFotosContrato
        //18 -> LISTANEGRA
        //19 -> NOTASCOBRANZA
        //20 -> ASISTENCIA

        JSONArray jsonArray = new JSONArray();

        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "";

            if(numTabla == 1) {
                SQL = "SELECT * FROM CONTRATOS WHERE DATOS = '1' AND ENVIADOPAGINA = '0' AND ESTATUS_ESTADOCONTRATO != '13'";
            }else if(numTabla == 2) {
                SQL = "SELECT * FROM HISTORIALCLINICO H INNER JOIN CONTRATOS C ON H.ID_CONTRATO = C.ID_CONTRATO WHERE H.ENVIADOPAGINA = '0' AND C.ESTATUS_ESTADOCONTRATO != '13'";
            }else if(numTabla == 3) {
                SQL = "SELECT * FROM ABONOS WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 4) {
                SQL = "SELECT * FROM CONTRATOPRODUCTO WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 5) {
                SQL = "SELECT * FROM ABONOSELIMINADOS WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 6) {
                SQL = "SELECT * FROM PRODUCTOSELIMINADOS WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 7) {
                SQL = "SELECT * FROM HISTORIALCONTRATO WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 8) {
                SQL = "SELECT * FROM PROMOCIONCONTRATO WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 9) {
                SQL = "SELECT * FROM PAYMENTS WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 10) {
                SQL = "SELECT * FROM PROMOCIONESELIMINADAS WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 11) {
                SQL = "SELECT * FROM DATOSSTRIPE WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 12) {
                SQL = "SELECT * FROM GARANTIAS WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 13) {
                SQL = "SELECT * FROM RUTA WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 14) {
                SQL = "SELECT * FROM HISTORIALSINCONVERSION HS INNER JOIN CONTRATOS C ON HS.ID_CONTRATO = C.ID_CONTRATO WHERE HS.ENVIADOPAGINA = '0' AND C.ESTATUS_ESTADOCONTRATO != '13'";
            }else if(numTabla == 15) {
                SQL = "SELECT * FROM BUZON WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 16) {
                SQL = "SELECT * FROM AUTORIZACIONESARMAZON WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 17) {
                SQL = "SELECT * FROM HISTORIALFOTOSCONTRATO WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 18) {
                SQL = "SELECT * FROM CONTRATOSLISTANEGRA WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 19) {
                SQL = "SELECT * FROM NOTASCOBRANZA WHERE ENVIADOPAGINA = '0'";
            }else if(numTabla == 20) {
                SQL = "SELECT * FROM ASISTENCIA WHERE ENVIADOPAGINA = '0'";
            }
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0) {
                Log.i("MENSAJE", "No hay registros en tabla");
            } else {

                try {

                    while (datos.moveToNext()) {

                        JSONObject jsonObject = new JSONObject();

                        if(numTabla == 1) {
                            //CONTRATOS
                            jsonObject.put("ID_CONTRATO", datos.getString(0));
                            jsonObject.put("DATOS", datos.getString(1));
                            jsonObject.put("ID_USUARIOCREACION", datos.getString(2));
                            jsonObject.put("NOMBRE_USUARIOCREACION", datos.getString(3));
                            jsonObject.put("ID_ZONA", datos.getString(4));
                            jsonObject.put("NOMBRE", datos.getString(5));
                            jsonObject.put("CALLE", datos.getString(6));
                            jsonObject.put("NUMERO", datos.getString(7));
                            jsonObject.put("DEPTO", datos.getString(8));
                            jsonObject.put("ALLADODE", datos.getString(9));
                            jsonObject.put("FRENTEA", datos.getString(10));
                            jsonObject.put("ENTRECALLES", datos.getString(11));
                            jsonObject.put("COLONIA", datos.getString(12));
                            jsonObject.put("LOCALIDAD", datos.getString(13));
                            jsonObject.put("TELEFONO", datos.getString(14));
                            jsonObject.put("TELEFONOREFERENCIA", datos.getString(15));
                            jsonObject.put("NOMBREREFERENCIA", datos.getString(16));
                            jsonObject.put("CASATIPO", datos.getString(17));
                            jsonObject.put("CASACOLOR", datos.getString(18));
                            jsonObject.put("FOTOINEFRENTE", datos.getString(19));
                            jsonObject.put("FOTOINEATRAS", datos.getString(20));
                            jsonObject.put("FOTOCASA", datos.getString(21));
                            jsonObject.put("COMPROBANTEDOMICILIO", datos.getString(22));
                            jsonObject.put("ID_OPTOMETRISTA", datos.getString(23));
                            jsonObject.put("TARJETAFRENTE", datos.getString(24));
                            jsonObject.put("TARJETAATRAS", datos.getString(25));
                            jsonObject.put("PAGO", datos.getString(26));
                            jsonObject.put("ID_PROMOCION", datos.getString(27));
                            jsonObject.put("TOTAL", datos.getString(28));
                            jsonObject.put("IDCONTRATORELACION", datos.getString(29));
                            jsonObject.put("CONTADOR", datos.getString(30));
                            jsonObject.put("TOTALHISTORIAL", datos.getString(31));
                            jsonObject.put("TOTALPROMOCION", datos.getString(32));
                            jsonObject.put("TOTALPRODUCTO", datos.getString(33));
                            jsonObject.put("TOTALABONO", datos.getString(34));
                            jsonObject.put("ULTIMOABONO", datos.getString(35));
                            jsonObject.put("ESTATUS_ESTADOCONTRATO", datos.getString(36));
                            jsonObject.put("DIAPAGO", datos.getString(37));
                            jsonObject.put("FECHACOBROINI", datos.getString(38));
                            jsonObject.put("FECHACOBROFIN", datos.getString(39));
                            jsonObject.put("FECHAATRASO", datos.getString(40));
                            jsonObject.put("COSTOATRASO", datos.getString(41));
                            jsonObject.put("DIASELECCIONADO", datos.getString(42));
                            jsonObject.put("FECHAENTREGA", datos.getString(43));
                            jsonObject.put("PAGOSADELANTAR", datos.getString(44));
                            jsonObject.put("ENGANCHE", datos.getString(45));
                            jsonObject.put("ENTREGAPRODUCTO", datos.getString(46));
                            jsonObject.put("CORREO", datos.getString(47));
                            jsonObject.put("ESTATUS", datos.getString(48));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(49));
                            jsonObject.put("PAGARE", datos.getString(50));
                            jsonObject.put("FOTOOTROS", datos.getString(51));
                            jsonObject.put("PROMOCIONTERMINADA", datos.getString(52));
                            jsonObject.put("SUBSCRIPCION", datos.getString(53));
                            jsonObject.put("FECHASUBSCRIPCION", datos.getString(54));
                            jsonObject.put("NOTA", datos.getString(55));
                            jsonObject.put("TOTALREAL", datos.getString(56));
                            jsonObject.put("DIATEMPORAL", datos.getString(57));
                            jsonObject.put("COORDENADAS", datos.getString(58));
                            jsonObject.put("CALLEENTREGA", datos.getString(59));
                            jsonObject.put("NUMEROENTREGA", datos.getString(60));
                            jsonObject.put("DEPTOENTREGA", datos.getString(61));
                            jsonObject.put("ALLADODEENTREGA", datos.getString(62));
                            jsonObject.put("FRENTEAENTREGA", datos.getString(63));
                            jsonObject.put("ENTRECALLESENTREGA", datos.getString(64));
                            jsonObject.put("COLONIAENTREGA", datos.getString(65));
                            jsonObject.put("LOCALIDADENTREGA", datos.getString(66));
                            jsonObject.put("CASATIPOENTREGA", datos.getString(67));
                            jsonObject.put("CASACOLORENTREGA", datos.getString(68));
                            jsonObject.put("ALIAS", datos.getString(69));
                            String referencia = "";
                            if(datos.getString(70) != null){
                                referencia = datos.getString(70);
                            }
                            jsonObject.put("REFERENCIA", referencia);
                            jsonObject.put("OPCIONLUGARENTREGA", datos.getString(71));
                            jsonObject.put("OBSERVACIONFOTOINE", datos.getString(72));
                            jsonObject.put("OBSERVACIONFOTOINEATRAS", datos.getString(73));
                            jsonObject.put("OBSERVACIONFOTOCASA", datos.getString(74));
                            jsonObject.put("OBSERVACIONCOMPROBANTEDOMICILIO", datos.getString(75));
                            jsonObject.put("OBSERVACIONPAGARE", datos.getString(76));
                            jsonObject.put("OBSERVACIONFOTOOTROS", datos.getString(77));
                            jsonObject.put("CREATED_AT", datos.getString(78));
                            jsonObject.put("UPDATED_AT", datos.getString(79));
                        }else if(numTabla == 2) {
                            //HISTORIALCLINICO
                            jsonObject.put("ID", datos.getString(0));
                            jsonObject.put("ID_CONTRATO", datos.getString(1));
                            jsonObject.put("EDAD", datos.getString(2));
                            jsonObject.put("FECHAENTREGA", datos.getString(3));
                            jsonObject.put("DIAGNOSTICO", datos.getString(4));
                            jsonObject.put("OCUPACION", datos.getString(5));
                            jsonObject.put("DIABETES", datos.getString(6));
                            jsonObject.put("HIPERTENSION", datos.getString(7));
                            jsonObject.put("DOLOR", datos.getString(8));
                            jsonObject.put("ARDOR", datos.getString(9));
                            jsonObject.put("GOLPEOJOS", datos.getString(10));
                            jsonObject.put("OTROM", datos.getString(11));
                            jsonObject.put("MOLESTIAOTRO", datos.getString(12));
                            jsonObject.put("ULTIMOEXAMEN", datos.getString(13));
                            jsonObject.put("ESFERICODER", datos.getString(14));
                            jsonObject.put("CILINDRODER", datos.getString(15));
                            jsonObject.put("EJEDER", datos.getString(16));
                            jsonObject.put("ADDDER", datos.getString(17));
                            jsonObject.put("ALTDER", datos.getString(18));
                            jsonObject.put("ESFERICOIZQ", datos.getString(19));
                            jsonObject.put("CILINDROIZQ", datos.getString(20));
                            jsonObject.put("EJEIZQ", datos.getString(21));
                            jsonObject.put("ADDIZQ", datos.getString(22));
                            jsonObject.put("ALTIZQ", datos.getString(23));
                            jsonObject.put("ID_PRODUCTO", datos.getString(24));
                            jsonObject.put("ID_PAQUETE", datos.getString(25));
                            jsonObject.put("MATERIAL", datos.getString(26));
                            jsonObject.put("MATERIALOTRO", datos.getString(27));
                            jsonObject.put("COSTOMATERIAL", datos.getString(28));
                            jsonObject.put("BIFOCAL", datos.getString(29));
                            jsonObject.put("FOTOCROMATICO", datos.getString(30));
                            jsonObject.put("AR", datos.getString(31));
                            jsonObject.put("TINTE", datos.getString(32));
                            jsonObject.put("BLUERAY", datos.getString(33));
                            jsonObject.put("OTROT", datos.getString(34));
                            jsonObject.put("TRATAMIENTOOTRO", datos.getString(35));
                            jsonObject.put("COSTOTRATAMIENTO", datos.getString(36));
                            jsonObject.put("OBSERVACIONES", datos.getString(37));
                            jsonObject.put("OBSERVACIONESINTERNO", datos.getString(38));
                            jsonObject.put("TIPO", datos.getString(39));
                            jsonObject.put("BIFOCALOTRO", datos.getString(40));
                            jsonObject.put("COSTOBIFOCAL", datos.getString(41));
                            jsonObject.put("EMBARAZADA", datos.getString(42));
                            jsonObject.put("DURMIOSEISOCHOHORAS", datos.getString(43));
                            jsonObject.put("ACTIVIDADDIA", datos.getString(44));
                            jsonObject.put("PROBLEMASOJOS", datos.getString(45));
                            jsonObject.put("POLICARBONATOTIPO", datos.getString(46));
                            jsonObject.put("ID_TRATAMIENTOCOLORTINTE", datos.getString(47));
                            jsonObject.put("ESTILOTINTE", datos.getString(48));
                            jsonObject.put("POLARIZADO", datos.getString(49));
                            jsonObject.put("ID_TRATAMIENTOCOLORPOLARIZADO", datos.getString(50));
                            jsonObject.put("ESPEJO", datos.getString(51));
                            jsonObject.put("ID_TRATAMIENTOCOLORESPEJO", datos.getString(52));
                            jsonObject.put("FOTOARMAZON", datos.getString(53));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(54));
                            jsonObject.put("CREATED_AT", datos.getString(55));
                            jsonObject.put("UPDATED_AT", datos.getString(56));
                        }else if(numTabla == 3) {
                            //ABONOS
                            jsonObject.put("ID", datos.getString(0));
                            jsonObject.put("ID_CONTRATO", datos.getString(1));
                            jsonObject.put("ABONO", datos.getString(2));
                            jsonObject.put("FOLIO", datos.getString(3));
                            jsonObject.put("TIPOABONO", datos.getString(4));
                            jsonObject.put("ATRASO", datos.getString(5));
                            jsonObject.put("ADELANTOS", datos.getString(6));
                            jsonObject.put("ID_USUARIO", datos.getString(7));
                            jsonObject.put("METODOPAGO", datos.getString(8));
                            jsonObject.put("CORTE", datos.getString(9));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(10));
                            jsonObject.put("ID_CONTRATOPRODUCTO", datos.getString(11));
                            jsonObject.put("COORDENADAS", datos.getString(12));
                            jsonObject.put("CREATED_AT", datos.getString(13));
                            jsonObject.put("UPDATED_AT", datos.getString(14));
                        }else if(numTabla == 4) {
                            //CONTRATOPRODUCTO
                            jsonObject.put("ID", datos.getString(0));
                            jsonObject.put("ID_CONTRATO", datos.getString(1));
                            jsonObject.put("ID_PRODUCTO", datos.getString(2));
                            jsonObject.put("PIEZAS", datos.getString(3));
                            jsonObject.put("TOTAL", datos.getString(4));
                            jsonObject.put("ID_USUARIO", datos.getString(5));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(6));
                            jsonObject.put("CREATED_AT", datos.getString(7));
                            jsonObject.put("UPDATED_AT", datos.getString(8));
                        }else if(numTabla == 5) {
                            //ABONOSELIMINADOS
                            jsonObject.put("ID_CONTRATO", datos.getString(0));
                            jsonObject.put("ID_ABONO", datos.getString(1));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(2));
                            jsonObject.put("CREATED_AT", datos.getString(3));
                        }else if(numTabla == 6) {
                            //PRODUCTOSELIMINADOS
                            jsonObject.put("ID_CONTRATO", datos.getString(0));
                            jsonObject.put("ID_CONTRATOPRODUCTO", datos.getString(1));
                            jsonObject.put("ID_PRODUCTO", datos.getString(2));
                            jsonObject.put("PIEZAS", datos.getString(3));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(4));
                            jsonObject.put("CREATED_AT", datos.getString(5));
                        }else if(numTabla == 7) {
                            //HISTORIALCONTRATO
                            jsonObject.put("ID", datos.getString(0));
                            jsonObject.put("ID_CONTRATO", datos.getString(1));
                            jsonObject.put("ID_USUARIOC", datos.getString(2));
                            jsonObject.put("CAMBIOS", datos.getString(3));
                            jsonObject.put("TIPOMENSAJE", datos.getString(4));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(5));
                            jsonObject.put("CREATED_AT", datos.getString(6));
                            jsonObject.put("UPDATED_AT", datos.getString(7));
                        }else if(numTabla == 8) {
                            //PROMOCIONCONTRATO
                            jsonObject.put("ID", datos.getString(0));
                            jsonObject.put("ID_CONTRATO", datos.getString(1));
                            jsonObject.put("ID_PROMOCION", datos.getString(2));
                            jsonObject.put("ESTADO", datos.getString(3));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(4));
                            jsonObject.put("CREATED_AT", datos.getString(5));
                            jsonObject.put("UPDATED_AT", datos.getString(6));
                        }else if(numTabla == 9) {
                            //PAYMENTS
                            jsonObject.put("PAYMENT_ID", datos.getString(0));
                            jsonObject.put("PAYER_EMAIL", datos.getString(1));
                            jsonObject.put("ID_ABONO", datos.getString(2));
                            jsonObject.put("AMOUNT", datos.getString(3));
                            jsonObject.put("CURRENCY", datos.getString(4));
                            jsonObject.put("PAYMENT_STATUS", datos.getString(5));
                            jsonObject.put("TIPOORIGEN", datos.getString(6));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(7));
                            jsonObject.put("CREATED_AT", datos.getString(8));
                            jsonObject.put("UPDATED_AT", datos.getString(9));
                        }else if(numTabla == 10) {
                            //PROMOCIONESELIMINADAS
                            jsonObject.put("ID_CONTRATO", datos.getString(0));
                            jsonObject.put("ID_PROMOCIONCONTRATO", datos.getString(1));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(2));
                            jsonObject.put("CREATED_AT", datos.getString(3));
                        }else if(numTabla == 11) {
                            //DATOSSTRIPE
                            jsonObject.put("ID_CONTRATO", datos.getString(0));
                            jsonObject.put("ID_PAYMENTINTENT", datos.getString(1));
                            jsonObject.put("ID_PAYMENTMETHOD", datos.getString(2));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(3));
                            jsonObject.put("CREATED_AT", datos.getString(4));
                            jsonObject.put("UPDATED_AT", datos.getString(5));
                        }else if(numTabla == 12) {
                            //GARANTIAS
                            jsonObject.put("ID", datos.getString(0));
                            jsonObject.put("ID_CONTRATO", datos.getString(1));
                            jsonObject.put("ID_HISTORIAL", datos.getString(2));
                            jsonObject.put("ID_HISTORIALGARANTIA", datos.getString(3));
                            jsonObject.put("ID_OPTOMETRISTA", datos.getString(4));
                            jsonObject.put("ESTADOGARANTIA", datos.getString(5));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(6));
                            jsonObject.put("CREATED_AT", datos.getString(7));
                            jsonObject.put("UPDATED_AT", datos.getString(8));
                        }else if(numTabla == 13) {
                            //RUTA
                            jsonObject.put("ID", datos.getString(0));
                            jsonObject.put("DIA", datos.getString(1));
                            jsonObject.put("ID_CONTRATO", datos.getString(2));
                            jsonObject.put("ID_USUARIO", datos.getString(3));
                            jsonObject.put("POSICION", datos.getString(4));
                            jsonObject.put("ESTADO", datos.getString(5));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(6));
                        }else if(numTabla == 14) {
                            //HISTORIALSINCONVERSION
                            jsonObject.put("ID_CONTRATO", datos.getString(0));
                            jsonObject.put("ID_HISTORIAL", datos.getString(1));
                            jsonObject.put("ESFERICODER", datos.getString(2));
                            jsonObject.put("CILINDRODER", datos.getString(3));
                            jsonObject.put("EJEDER", datos.getString(4));
                            jsonObject.put("ADDDER", datos.getString(5));
                            jsonObject.put("ESFERICOIZQ", datos.getString(6));
                            jsonObject.put("CILINDROIZQ", datos.getString(7));
                            jsonObject.put("EJEIZQ", datos.getString(8));
                            jsonObject.put("ADDIZQ", datos.getString(9));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(10));
                            jsonObject.put("CREATED_AT", datos.getString(11));
                        }else if(numTabla == 15) {
                            //BUZON
                            jsonObject.put("ID_USUARIO", datos.getString(1));
                            jsonObject.put("ID_FRANQUICIA", datos.getString(2));
                            jsonObject.put("MENSAJE", datos.getString(3));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(4));
                            jsonObject.put("CREATED_AT", datos.getString(5));
                        }else if(numTabla == 16) {
                            //AUTORIZACIONESARMAZON
                            jsonObject.put("ID_CONTRATO", datos.getString(0));
                            jsonObject.put("ID_PRODUCTO", datos.getString(1));
                            jsonObject.put("FOLIOPOLIZA", datos.getString(2));
                            jsonObject.put("ID_USUARIOC", datos.getString(3));
                            jsonObject.put("TIPO", datos.getString(4));
                            jsonObject.put("PIEZAS", datos.getString(5));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(6));
                            jsonObject.put("CREATED_AT", datos.getString(7));
                        }else if(numTabla == 17) {
                            //HISTORIALCONTRATO
                            jsonObject.put("ID", datos.getString(0));
                            jsonObject.put("ID_CONTRATO", datos.getString(1));
                            jsonObject.put("ID_USUARIOC", datos.getString(2));
                            jsonObject.put("FOTO", datos.getString(3));
                            jsonObject.put("OBSERVACIONES", datos.getString(4));
                            jsonObject.put("TIPOMENSAJE", datos.getString(5));
                            jsonObject.put("ENVIADOPAGINA", datos.getString(6));
                            jsonObject.put("CREATED_AT", datos.getString(7));
                            jsonObject.put("UPDATED_AT", datos.getString(8));
                        }else if(numTabla == 18) {
                            //CONTRATOSLISTANEGRA
                            jsonObject.put("ID_CONTRATO", datos.getString(0));
                            jsonObject.put("DESCRIPCION", datos.getString(1));
                            jsonObject.put("ESTADO", datos.getString(2));
                        }else if(numTabla == 19) {
                            //NOTASCOBRANZA
                            jsonObject.put("ID", datos.getString(0));
                            jsonObject.put("NOTA", datos.getString(1));
                            jsonObject.put("CREATED_AT", datos.getString(2));
                            jsonObject.put("BANDERAELIMINADO", datos.getString(4));
                        }else if(numTabla == 20) {
                            //ASISTENCIA
                            jsonObject.put("ID_USUARIO", datos.getString(0));
                            jsonObject.put("NOMBRE", datos.getString(1));
                            jsonObject.put("ASISTENCIA", datos.getString(2));
                            jsonObject.put("REGISTROSALIDA", datos.getString(3));
                        }
                        jsonArray.put(jsonObject);

                    }


                } catch (JSONException e) {
                    global.escribirError(e, 82);
                    e.printStackTrace();
                }

            }

            sqLiteDB.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 83);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return jsonArray;
    }

    public void validacionImagenesFTP(String idContrato, String fotoinefrente, String fotoineatras, String fotocasa, String comprobantedomicilio, String tarjetafrente, String tarjetaatras,
                                      String pagare, String fotootros, String fotomovimiento, String fotoarmazon1,  String fotoarmazon2) {

        String[] tablavalidacion = new String[] {
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "FOTOINE"),
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "FOTOINEATRAS"),
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "FOTOCASA"),
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "COMPROBANTEDOMICILIO"),
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "TARJETAPENSION"),
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "TARJETAPENSIONATRAS"),
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "PAGARE"),
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "FOTOOTROS"),
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "FOTOMOVIMIENTO"),
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "FOTOARMAZON1"),
                global.obtenerAtributoTablaImagenesCargadasContrato(idContrato, "FOTOARMAZON2")};

        String[] imagenes = new String[] {fotoinefrente, fotoineatras,
                fotocasa, comprobantedomicilio, tarjetafrente,
                tarjetaatras, pagare, fotootros, fotomovimiento, fotoarmazon1, fotoarmazon2};

        String[] carpetas = new String[] {"fotoine", "fotoineatras", "fotocasa", "comprobantedomicilio", "tarjetapension", "tarjetapensionatras", "pagare", "fotootros", "fotomovimiento", "fotoarmazon1", "fotoarmazon2"};

        List<String> imagenesPrecargadas = new ArrayList<>();
        List<String> carpetasPrecargadas = new ArrayList<>();

        for (int i = 0; i < imagenes.length; i++) {
            if (imagenes[i].length() != 0) {
                //Imagenes es diferente de ""

                if(tablavalidacion[i].equals("0")) {
                    File direccionAntesDeCargar = new File(direccionNoMedia + "/" + imagenes[i]);
                    Sincronizacion.subirArchivoFtp cargar = new Sincronizacion.subirArchivoFtp(direccionAntesDeCargar.getAbsolutePath(), imagenes[i], carpetas[i], idContrato);
                    cargar.execute();
                }else if(tablavalidacion[i].equals("1")) {
                    imagenesPrecargadas.add(imagenes[i]);
                    carpetasPrecargadas.add(carpetas[i]);
                }

            }
        }

        if(imagenesPrecargadas.size() != 0) {
            Sincronizacion.verificarSiExisteArchivoFTP cargar = new Sincronizacion.verificarSiExisteArchivoFTP(imagenesPrecargadas, carpetasPrecargadas, idContrato);
            cargar.execute();
        }

    }

    /*Metodo/Funcion: sincronizarTabla
      Descripcion: Se obtiene un JSONArray de las tablas correspondientes con los datos que se mandaran a la pagina
    */
    private void actualizarAtributoEnviadoPaginaTablas() {

        String[] tablas = new String[] {"CONTRATOS", "HISTORIALCLINICO", "ABONOS", "CONTRATOPRODUCTO", "ABONOSELIMINADOS", "PRODUCTOSELIMINADOS",
                "HISTORIALCONTRATO", "PROMOCIONCONTRATO", "PAYMENTS", "PROMOCIONESELIMINADAS", "DATOSSTRIPE", "GARANTIAS", "RUTA", "HISTORIALSINCONVERSION",
                "BUZON", "AUTORIZACIONESARMAZON", "HISTORIALFOTOSCONTRATO", "NOTASCOBRANZA", "ASISTENCIA"};

        try {

            sqLiteDB = conexion.getWritableDatabase();

            for(int i = 0; i < tablas.length; i++) {
                ContentValues valor = new ContentValues();
                valor.put("ENVIADOPAGINA", "1");
                sqLiteDB.update(tablas[i], valor, "ENVIADOPAGINA = '0'", null);
            }
            sqLiteDB.close();

            actualizarAtributoContratoEnviadoContratos();

        }catch (SQLiteException e) {
            global.escribirError(e, 84);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    private void actualizarAtributoContratoEnviadoContratos() {
        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            String consulta = "UPDATE CONTRATOS SET CONTRATOENVIADO = '1' WHERE ESTATUS_ESTADOCONTRATO IN (1,9)";
            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 262);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    /*Metodo/Funcion: sincronizar2
      Descripcion: Mandar token a pagina, eliminar los registros de todas las tablas y agregar los nuevos
    */
    private void sincronizar2() {

        //Mandar token peticion a tal url POST LOLATV3 -> Lanzar DELETE A TODO y Llegaran contratos, abonos, promociones TODO
        RequestQueue requestQueue = Volley.newRequestQueue(fragmento.getContext());
        JSONObject parametrosJSON = new JSONObject();

        //Ocultar buscador para cobranza
        llBuscador.setVisibility(View.GONE);
        llBuscadorIcono.setVisibility(View.GONE);
        ((MainActivity) fragmento.getActivity()).hideMenuHamburguesa();
        actualizarBanderaDescargaCompleta("4");
        toolbar.setBackgroundColor(Color.parseColor("#FFACA6"));

        try {

            parametrosJSON.put("token", token);

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

                //Obtener bandera para sincronizar colonias
                String banderaColonias = global.obtenerUnResultadoQuery("SELECT BANDERA FROM COLONIAS LIMIT 1");
                parametrosJSON.put("banderacolonias", banderaColonias);
            }

            JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST, llaves.url_sincronizar_2, parametrosJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        switch (obtenerAtributoJSONDecodificado(response.get("datos").toString(), "codigo")) {
                            case "5Gn6oZ7QUFxT4uDULhAB":
                                global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos");
                                break;
                            case "Ppts8qWkkqosQQqRKlMz":
                                global.imprimirMensajeEnHilo("Solicitud pendiente por confirmar");
                                break;
                            case "4vdw3EAq7xfyeKVg0NN7":
                                eliminarTodoBD();
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "mensajes"), 1);//*?
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "usuarios"), 2);//*?
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "contratos"), 4);//*?
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "historialesclinicos"), 6);//*?
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "productos"), 7);//*?
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "promocioncontratos"), 10);//*?
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "contratosproductos"), 11);//*?
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "abonos"), 12);//*?
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "garantias"), 14);//*?
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "llaves"), 19);//*?
                                decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "configuracionmovil"), 21);//*?
                                if (!rol.equals("4")) {
                                    //Asistente o Optometrista
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "zonas"), 3);//*
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "promociones"), 5);//*
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "paquetes"), 8);//*
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "tratamientos"), 9);//*
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "contratosnuevos"), 13);//*
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "contratosliosfugas"), 17);//*
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "historialessinconversion"), 18);//*
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "ventas"), 20);//*
                                    String jsonColonias = obtenerAtributoJSONDecodificado(response.get("datos").toString(), "colonias");//*
                                    if(!jsonColonias.equals("null")){
                                        //jsoncolonias contiene valores - Limpiar tabla COLONIAS
                                        SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
                                        sqLiteDB2.execSQL("DELETE FROM COLONIAS");
                                        sqLiteDB2.close();
                                        //Insertar nueva lista de colonias
                                        decodificarJsonEInsertarDatosTabla(jsonColonias, 22);
                                    }
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "tratamientoscolores"), 23);//*
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "imagenescontratoservidor"), 24);//*
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "asistencia"), 27);//*
                                }else {
                                    //Cobrador
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "ruta"), 15);//?
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "totalcontratosconabonos"), 16);//?
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "contratoslistanegra"), 25);
                                    decodificarJsonEInsertarDatosTabla(obtenerAtributoJSONDecodificado(response.get("datos").toString(), "notascobranza"), 26);
                                    global.actualizarAtributoTabla("MOVIL", "JSONABONOSGENERAL", seguridad.cifrar(obtenerAtributoJSONDecodificado(response.get("datos").toString(),
                                                    "jsonabonosgeneral").getBytes("UTF-16LE")), "ID",
                                            seguridad.cifrar(obtenerRol.obtenerAtributoUsuarioLogeado("ID").getBytes("UTF-16LE")));//?
                                    //Actualizar registro de ultima conexion
                                    global.actualizarAtributoTabla("MOVIL", "ULTIMACONEXION", seguridad.cifrar(obtenerAtributoJSONDecodificado(response.get("datos").toString(),
                                            "ultimaconexion").getBytes("UTF-16LE")), "ID", seguridad.cifrar(obtenerRol.obtenerAtributoUsuarioLogeado("ID").getBytes("UTF-16LE")));
                                }
                                actualizarBanderaDescargaCompleta("1");
                                llProgress.setVisibility(View.GONE);
                                break;
                            case "ozpw6GLnvbCMpL2QjIQl":
                                global.imprimirMensajeEnHilo("No fue posible conectar");
                                break;
                            case "JGvcYgZn8PD4KpIm4uIN"://El token no es valido, se debera cerrar sesión
                                cierresesion();
                                llProgress.setVisibility(View.GONE);
                                break;
                            default:
                                global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos 2");
                        }

                    } catch (Exception e) {
                        global.escribirError(e, 85);
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error instanceof TimeoutError) {
                            imprimirMensaje("Error al intentar conectar al servidor, se acabo el tiempo de espera (Sin datos disponibles/sin señal/sin conexion a internet por wifi)Error:0902");
                        }else if(error instanceof NoConnectionError){
                            imprimirMensaje("No se pudo conectar(Sin señal/servicio)");
                        } else if (error instanceof AuthFailureError) {
                            imprimirMensaje("Error de autenticacion");
                        } else if (error instanceof ServerError) {
                            imprimirMensaje("No se pudo conectar al servidor");
                        } else if (error instanceof NetworkError) {
                            imprimirMensaje("Error de red/señal");
                        } else if (error instanceof ParseError) {
                            imprimirMensaje("Ocurrio un error al intentar conectar");
                        }else{
                            imprimirMensaje("Error al intentar conectar");
                        }
                        llProgress.setVisibility(View.GONE);
                        actualizarBanderaDescargaCompleta("2");
                        //End
                    } catch (Exception e) {
                        global.escribirError(e, 86);
                        imprimirMensaje("Error: "+e.toString());
                        llProgress.setVisibility(View.GONE);
                        actualizarBanderaDescargaCompleta("2");
                    }
                }
            });

            peticion.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    10,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(peticion);

        }catch (JSONException e) {
            global.escribirError(e, 87);
            Log.i("ERRORJSON", "" + e);
        }

    }

    private void imprimirMensaje(String mensajeError) {
        if(sincronizar){
            global.imprimirMensajeEnHilo(mensajeError);
        }else{
            Log.i("MENSAJE","Error: "+mensajeError);
        }
    }

    public void cierresesion() {

        RequestQueue requestQueue = Volley.newRequestQueue(fragmento.getContext());
        JSONObject parametrosJSON = new JSONObject();

        try {

            parametrosJSON.put("token", token);

            JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST, llaves.url_cerrarsesion, parametrosJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        Fragment verificadorFragment;
                        FragmentTransaction transaction;

                        switch (obtenerAtributoJSONDecodificado(response.get("datos").toString(), "codigo")) {
                            case "P1zv7ZFD8dOvOTQVUTys": //Token no valido
                                borrarRegistroTablaMovil(obtenerRol.obtenerAtributoUsuarioLogeado("ID")); //Borrar registro en la tabla MOVIL excepto CORREO
                                //Redireccionar a Login
                                fragmento.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                verificadorFragment = new inicioSesion();
                                transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.content, verificadorFragment);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                actualizarBanderaDescargaCompleta("3");
                                ((MainActivity) fragmento.getActivity()).setTitle("");
                                ((MainActivity) fragmento.getActivity()).lockDrawer();
                                ((MainActivity) fragmento.getActivity()).hideMenuHamburguesa();
                                llBuscador.setVisibility(View.GONE);
                                llBuscadorIcono.setVisibility(View.GONE);
                                global.imprimirMensajeEnHilo("Sesión caducada - favor de iniciar sesión de nuevo");
                                break;
                            case "NXQRwc2HsxVyIS43QWqO": //Token valido y se cierra sesion
                                borrarRegistroTablaMovil(obtenerRol.obtenerAtributoUsuarioLogeado("ID")); //Borrar registro en la tabla MOVIL excepto CORREO
                                //Redireccionar a Login
                                fragmento.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                verificadorFragment = new inicioSesion();
                                transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.content, verificadorFragment);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                actualizarBanderaDescargaCompleta("3");
                                ((MainActivity) fragmento.getActivity()).setTitle("");
                                ((MainActivity) fragmento.getActivity()).lockDrawer();
                                ((MainActivity) fragmento.getActivity()).hideMenuHamburguesa();
                                llBuscador.setVisibility(View.GONE);
                                llBuscadorIcono.setVisibility(View.GONE);
                                global.imprimirMensajeEnHilo("Sesión cerrada correctamente");
                                break;
                            default:
                                global.imprimirMensajeEnHilo("Tuvimos un problema al cerrar sesión");
                        }

                    } catch (Exception e) {
                        global.escribirError(e, 88);
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error instanceof TimeoutError) {
                            imprimirMensaje("Error al intentar conectar al servidor, se acabo el tiempo de espera (Sin datos disponibles/sin señal/sin conexion a internet por wifi)Error:0903");
                        }else if(error instanceof NoConnectionError){
                            imprimirMensaje("No se pudo conectar(Sin señal/servicio)");
                        } else if (error instanceof AuthFailureError) {
                            imprimirMensaje("Error de autenticacion");
                        } else if (error instanceof ServerError) {
                            imprimirMensaje("No se pudo conectar al servidor");
                        } else if (error instanceof NetworkError) {
                            imprimirMensaje("Error de red/señal");
                        } else if (error instanceof ParseError) {
                            imprimirMensaje("Ocurrio un error al intentar conectar");
                        }else{
                            imprimirMensaje("Error al intentar conectar");
                        }
                        llProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        global.escribirError(e, 89);
                        imprimirMensaje("Error: "+e.toString());
                        llProgress.setVisibility(View.GONE);
                    }
                }
            });

            peticion.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    10,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(peticion);

        }catch (Exception e) {
            global.escribirError(e, 90);
            Log.i("ERRORJSON", "" + e);
        }

    }

    /*Metodo/Funcion: sesioncaducada
    Descripcion: Redirecciona al inicio de sesion si usuario
    tiene mas de 12 horas sin sincronizar/realizar su corte
    */
    public void sesioncaducada(){

        Fragment verificadorFragment;
        FragmentTransaction transaction;

        borrarRegistroTablaMovil(obtenerRol.obtenerAtributoUsuarioLogeado("ID")); //Borrar registro en la tabla MOVIL excepto CORREO
        //Redireccionar a Login
        fragmento.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        verificadorFragment = new inicioSesion();
        transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
        actualizarBanderaDescargaCompleta("3");
        ((MainActivity) fragmento.getActivity()).setTitle("");
        ((MainActivity) fragmento.getActivity()).lockDrawer();
        ((MainActivity) fragmento.getActivity()).hideMenuHamburguesa();
        llBuscador.setVisibility(View.GONE);
        llBuscadorIcono.setVisibility(View.GONE);
        global.imprimirMensajeEnHilo("Sesión caducada - favor de iniciar sesión de nuevo");

    }

    /*Metodo/Funcion: eliminarTodoBD
      Descripcion: Borrar todos los registros de las tablas de la base de datos
    */
    public void eliminarTodoBD() {

        Log.i("MENSAJE", "ENTRO A ELIMINAR TODO");

        String rol = obtenerRol.obtenerAtributoUsuarioLogeado("ROL");

        String[] tablas = new String[]{"MENSAJES", "USERS", "ZONAS", "HISTORIALCLINICO", "PROMOCION", "PRODUCTO",
                    "PAQUETES", "TRATAMIENTOS", "PROMOCIONCONTRATO", "CONTRATOPRODUCTO", "ABONOS", "HISTORIALCONTRATO",
                    "ABONOSELIMINADOS", "PRODUCTOSELIMINADOS", "PROMOCIONESELIMINADAS", "PAYMENTS", "DATOSSTRIPE",
                    "GARANTIAS", "CONTRATOS", "RUTA", "CONTRATOSLIOSFUGAS", "HISTORIALSINCONVERSION", "LLAVES", "VENTAS", "CONFIGURACIONMOVIL",
                    "BUZON", "AUTORIZACIONESARMAZON", "TRATAMIENTOSCOLORES", "IMAGENESSERVIDORCONTRATOS", "CONTRATOSLISTANEGRA", "NOTASCOBRANZA", "ASISTENCIA"};

        try{

            sqLiteDB = conexion.getWritableDatabase();
            for (int i = 0; i < tablas.length; i++) {
                String tabla = tablas[i];
                if(tabla.equals("CONTRATOS")) {
                    sqLiteDB.execSQL("DELETE FROM '" + tabla + "' WHERE ESTATUS_ESTADOCONTRATO != '13'");
                }else if(tabla.equals("HISTORIALCLINICO")) {
                    if(rol.equals("4")) {
                        sqLiteDB.execSQL("DELETE FROM '" + tabla + "'");
                    }else {
                        sqLiteDB.execSQL("DELETE FROM '" + tabla + "' WHERE ID_CONTRATO IN (SELECT ID_CONTRATO FROM CONTRATOS WHERE ESTATUS_ESTADOCONTRATO != '13')");
                    }
                }else if(tabla.equals("HISTORIALSINCONVERSION")) {
                    if(rol.equals("4")) {
                        sqLiteDB.execSQL("DELETE FROM '" + tabla + "'");
                    }else {
                        sqLiteDB.execSQL("DELETE FROM '" + tabla + "' WHERE ID_CONTRATO IN (SELECT ID_CONTRATO FROM CONTRATOS WHERE ESTATUS_ESTADOCONTRATO != '13')");
                    }
                }else {
                    sqLiteDB.execSQL("DELETE FROM '" + tabla + "'");
                }
            }
            sqLiteDB.close();

        }catch (SQLiteException e){
            global.escribirError(e, 91);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }
    }

    /*Metodo/Funcion: decodificarJsonEInsertarDatosTabla
      Descripcion: Insertar los nuevos datos obtenidos de la respuesta de la pagina en la base de datos
    */
    private void decodificarJsonEInsertarDatosTabla(String json, int numTabla) {

        Log.i("MENSAJE", "InsertarDatosTabla");

        JSONArray jsonArray = null;
        try {
            if(!json.equals("null")) {
                jsonArray = new JSONArray(json);
            }
        } catch (JSONException e) {
            global.escribirError(e, 92);
            e.printStackTrace();
        }

        //numTabla
        //1 - MENSAJES
        //2 - USERS
        //3 - ZONAS
        //4 - CONTRATOS
        //5 - PROMOCION
        //6 - HISTORIALCLINICO
        //7 - PRODUCTO
        //8 - PAQUETES
        //9 - TRATAMIENTOS
        //10 - PROMOCIONCONTRATO
        //11 - CONTRATOPRODUCTO
        //12 - ABONOS
        //13 - CONTRATOSNUEVOS
        //14 - GARANTIAS
        //15 - RUTA
        //16 - TOTALCONTRATOSCONABONO
        //17 - CONTRATOSLIOSFUGAS
        //18 - HISTORIALSINCONVERSION
        //19 - LLAVES
        //20 - VENTAS
        //21 - CONFIGURACIONMOVIL
        //22 - COLONIAS
        //23 - TRATAMIENTOSCOLORES
        //24 - IMAGENESSERVIDORCONTRATOS
        //25 - CONTRATOSLISTANEGRA
        //26 - NOTASCOBRANZA
        //27 - ASISTENCIA

        if(jsonArray != null) {

            try {

                try {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String rol = obtenerRol.obtenerAtributoUsuarioLogeado("ROL");

                        ContentValues valores = new ContentValues();
                        boolean existeContrato = false, existeHistorial = false;

                        if (numTabla == 4 || numTabla == 13) {
                            existeContrato = existeRegistro("CONTRATOS", "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                            Log.i("MENSAJE", "ID contrato: " + ((JSONObject) jsonArray.get(i)).optString("id") + " Valor bolean: " + existeContrato);
                        }else if(numTabla == 6) {
                            existeHistorial = existeRegistro("HISTORIALCLINICO", "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            Log.i("MENSAJE", "ID historial: " + ((JSONObject) jsonArray.get(i)).optString("id") + " Valor bolean: " + existeHistorial);
                        }else if(numTabla == 18) {
                            existeHistorial = existeRegistro("HISTORIALSINCONVERSION", "ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                            Log.i("MENSAJE", "ID historial sin conversion: " + ((JSONObject) jsonArray.get(i)).optString("id_historial") + " Valor bolean: " + existeHistorial);
                        }

                        sqLiteDB = conexion.getWritableDatabase();

                        if (numTabla == 1) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("DESCRIPCION", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("descripcion")));
                            valores.put("FECHALIMITE", ((JSONObject) jsonArray.get(i)).optString("fechalimite"));
                            valores.put("NUMINTENTOS", "0");
                            valores.put("MAXINTENTOS", ((JSONObject) jsonArray.get(i)).optString("intentos"));
                            sqLiteDB.insert("MENSAJES", null, valores);
                        } else if (numTabla == 2) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("ROL_ID", ((JSONObject) jsonArray.get(i)).optString("rol_id"));
                            valores.put("NAME", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("name")));
                            sqLiteDB.insert("USERS", null, valores);
                        } else if (numTabla == 3) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("ZONA", ((JSONObject) jsonArray.get(i)).optString("zona"));
                            sqLiteDB.insert("ZONAS", null, valores);
                        } else if (numTabla == 4) {
                            if(!existeContrato) {
                                //No existe contrato
                                valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                valores.put("DATOS", ((JSONObject) jsonArray.get(i)).optString("datos"));
                                valores.put("ID_USUARIOCREACION", ((JSONObject) jsonArray.get(i)).optString("id_usuariocreacion"));
                                valores.put("NOMBRE_USUARIOCREACION", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("nombre_usuariocreacion")));
                                valores.put("ID_ZONA", ((JSONObject) jsonArray.get(i)).optString("id_zona"));
                                valores.put("NOMBRE", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("nombre")));
                                valores.put("CALLE", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("calle")));
                                valores.put("NUMERO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("numero")));
                                valores.put("DEPTO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("depto")));
                                valores.put("ALLADODE", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("alladode")));
                                valores.put("FRENTEA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("frentea")));
                                valores.put("ENTRECALLES", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("entrecalles")));
                                valores.put("COLONIA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("colonia")));
                                valores.put("LOCALIDAD", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("localidad")));
                                valores.put("TELEFONO", ((JSONObject) jsonArray.get(i)).optString("telefono"));
                                valores.put("TELEFONOREFERENCIA", ((JSONObject) jsonArray.get(i)).optString("telefonoreferencia"));
                                valores.put("NOMBREREFERENCIA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("nombrereferencia")));
                                valores.put("CASATIPO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casatipo")));
                                valores.put("CASACOLOR", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casacolor")));
                                valores.put("FOTOINEFRENTE", ((JSONObject) jsonArray.get(i)).optString("fotoine"));
                                valores.put("FOTOINEATRAS", ((JSONObject) jsonArray.get(i)).optString("fotoineatras"));
                                valores.put("FOTOCASA", ((JSONObject) jsonArray.get(i)).optString("fotocasa"));
                                valores.put("COMPROBANTEDOMICILIO", ((JSONObject) jsonArray.get(i)).optString("comprobantedomicilio"));
                                valores.put("ID_OPTOMETRISTA", ((JSONObject) jsonArray.get(i)).optString("id_optometrista"));
                                valores.put("TARJETAFRENTE", ((JSONObject) jsonArray.get(i)).optString("tarjeta"));
                                valores.put("TARJETAATRAS", ((JSONObject) jsonArray.get(i)).optString("tarjetapensionatras"));
                                valores.put("PAGO", ((JSONObject) jsonArray.get(i)).optString("pago"));
                                valores.put("ID_PROMOCION", ((JSONObject) jsonArray.get(i)).optString("id_promocion"));
                                valores.put("TOTAL", ((JSONObject) jsonArray.get(i)).optString("total"));
                                valores.put("IDCONTRATORELACION", ((JSONObject) jsonArray.get(i)).optString("idcontratorelacion"));
                                valores.put("CONTADOR", ((JSONObject) jsonArray.get(i)).optString("contador"));
                                valores.put("TOTALHISTORIAL", ((JSONObject) jsonArray.get(i)).optString("totalhistorial"));
                                valores.put("TOTALPROMOCION", ((JSONObject) jsonArray.get(i)).optString("totalpromocion"));
                                valores.put("TOTALPRODUCTO", ((JSONObject) jsonArray.get(i)).optString("totalproducto"));
                                valores.put("TOTALABONO", ((JSONObject) jsonArray.get(i)).optString("totalabono"));
                                valores.put("ULTIMOABONO", ((JSONObject) jsonArray.get(i)).optString("ultimoabono"));
                                if(((JSONObject) jsonArray.get(i)).optString("estatus_estadocontrato").equals("9") && !rol.equals("4")) {
                                    valores.put("ESTATUS_ESTADOCONTRATO", "1");
                                }else {
                                    valores.put("ESTATUS_ESTADOCONTRATO", ((JSONObject) jsonArray.get(i)).optString("estatus_estadocontrato"));
                                }
                                valores.put("DIAPAGO", ((JSONObject) jsonArray.get(i)).optString("diapago"));
                                valores.put("FECHACOBROINI", ((JSONObject) jsonArray.get(i)).optString("fechacobroini"));
                                valores.put("FECHACOBROFIN", ((JSONObject) jsonArray.get(i)).optString("fechacobrofin"));
                                valores.put("FECHAATRASO", ((JSONObject) jsonArray.get(i)).optString("fechaatraso"));
                                valores.put("COSTOATRASO", ((JSONObject) jsonArray.get(i)).optString("costoatraso"));
                                valores.put("DIASELECCIONADO", ((JSONObject) jsonArray.get(i)).optString("diaseleccionado"));
                                valores.put("FECHAENTREGA", ((JSONObject) jsonArray.get(i)).optString("fechaentrega"));
                                valores.put("PAGOSADELANTAR", ((JSONObject) jsonArray.get(i)).optString("pagosadelantar"));
                                valores.put("ENGANCHE", ((JSONObject) jsonArray.get(i)).optString("enganche"));
                                valores.put("ENTREGAPRODUCTO", ((JSONObject) jsonArray.get(i)).optString("entregaproducto"));
                                valores.put("CORREO", ((JSONObject) jsonArray.get(i)).optString("correo"));
                                valores.put("ESTATUS", ((JSONObject) jsonArray.get(i)).optString("estatus"));
                                valores.put("ENVIADOPAGINA", "1");
                                valores.put("PAGARE", ((JSONObject) jsonArray.get(i)).optString("pagare"));
                                valores.put("FOTOOTROS", ((JSONObject) jsonArray.get(i)).optString("fotootros"));
                                valores.put("PROMOCIONTERMINADA", ((JSONObject) jsonArray.get(i)).optString("promocionterminada"));
                                valores.put("SUBSCRIPCION", ((JSONObject) jsonArray.get(i)).optString("subscripcion"));
                                valores.put("FECHASUBSCRIPCION", ((JSONObject) jsonArray.get(i)).optString("fechasubscripcion"));
                                valores.put("NOTA", ((JSONObject) jsonArray.get(i)).optString("nota"));
                                valores.put("TOTALREAL", ((JSONObject) jsonArray.get(i)).optString("totalreal"));
                                valores.put("DIATEMPORAL", ((JSONObject) jsonArray.get(i)).optString("diatemporal"));
                                valores.put("COORDENADAS", ((JSONObject) jsonArray.get(i)).optString("coordenadas"));
                                valores.put("CALLEENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("calleentrega")));
                                valores.put("NUMEROENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("numeroentrega")));
                                valores.put("DEPTOENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("deptoentrega")));
                                valores.put("ALLADODEENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("alladodeentrega")));
                                valores.put("FRENTEAENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("frenteaentrega")));
                                valores.put("ENTRECALLESENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("entrecallesentrega")));
                                valores.put("COLONIAENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("coloniaentrega")));
                                valores.put("LOCALIDADENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("localidadentrega")));
                                valores.put("CASATIPOENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casatipoentrega")));
                                valores.put("CASACOLORENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casacolorentrega")));
                                valores.put("ALIAS", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("alias")));
                                valores.put("AUTORIZACION", ((JSONObject) jsonArray.get(i)).optString("autorizacion"));
                                valores.put("OPCIONLUGARENTREGA", ((JSONObject) jsonArray.get(i)).optString("opcionlugarentrega"));
                                valores.put("OBSERVACIONFOTOINE", ((JSONObject) jsonArray.get(i)).optString("observacionfotoine"));
                                valores.put("OBSERVACIONFOTOINEATRAS", ((JSONObject) jsonArray.get(i)).optString("observacionfotoineatras"));
                                valores.put("OBSERVACIONFOTOCASA", ((JSONObject) jsonArray.get(i)).optString("observacionfotocasa"));
                                valores.put("OBSERVACIONCOMPROBANTEDOMICILIO", ((JSONObject) jsonArray.get(i)).optString("observacioncomprobantedomicilio"));
                                valores.put("OBSERVACIONPAGARE", ((JSONObject) jsonArray.get(i)).optString("observacionpagare"));
                                valores.put("OBSERVACIONFOTOOTROS", ((JSONObject) jsonArray.get(i)).optString("observacionfotootros"));
                                valores.put("CREATED_AT", ((JSONObject) jsonArray.get(i)).optString("created_at"));
                                valores.put("UPDATED_AT", ((JSONObject) jsonArray.get(i)).optString("updated_at"));
                                valores.put("NOMBREPAQUETE", ((JSONObject) jsonArray.get(i)).optString("nombrepaquete"));
                                valores.put("TITULOPROMOCION", ((JSONObject) jsonArray.get(i)).optString("titulopromocion"));
                                valores.put("CONTRATOENVIADO", "1");
                                valores.put("ABONOMINIMO", ((JSONObject) jsonArray.get(i)).optString("abonominimo"));
                                sqLiteDB.insert("CONTRATOS", null, valores);
                            }else {
                                //Existe contrato
                                if(((JSONObject) jsonArray.get(i)).optString("datos").equals("1")) {
                                    //DATOS = 1
                                    global.actualizarAtributoTabla("CONTRATOS", "DATOS", ((JSONObject) jsonArray.get(i)).optString("datos"), "ID_CONTRATO",
                                            ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ID_USUARIOCREACION",
                                            ((JSONObject) jsonArray.get(i)).optString("id_usuariocreacion"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "NOMBRE_USUARIOCREACION",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("nombre_usuariocreacion")), "ID_CONTRATO",
                                            ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ID_ZONA",
                                            ((JSONObject) jsonArray.get(i)).optString("id_zona"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "NOMBRE",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("nombre")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "CALLE",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("calle")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "NUMERO",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("numero")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "DEPTO",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("depto")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ALLADODE",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("alladode")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FRENTEA",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("frentea")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ENTRECALLES",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("entrecalles")), "ID_CONTRATO",
                                            ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "COLONIA",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("colonia")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "LOCALIDAD",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("localidad")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TELEFONO",
                                            ((JSONObject) jsonArray.get(i)).optString("telefono"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TELEFONOREFERENCIA",
                                            ((JSONObject) jsonArray.get(i)).optString("telefonoreferencia"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "NOMBREREFERENCIA",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("nombrereferencia")), "ID_CONTRATO",
                                            ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "CASATIPO",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casatipo")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "CASACOLOR",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casacolor")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FOTOINEFRENTE",
                                            ((JSONObject) jsonArray.get(i)).optString("fotoine"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FOTOINEATRAS",
                                            ((JSONObject) jsonArray.get(i)).optString("fotoineatras"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FOTOCASA",
                                            ((JSONObject) jsonArray.get(i)).optString("fotocasa"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "COMPROBANTEDOMICILIO",
                                            ((JSONObject) jsonArray.get(i)).optString("comprobantedomicilio"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ID_OPTOMETRISTA",
                                            ((JSONObject) jsonArray.get(i)).optString("id_optometrista"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TARJETAFRENTE",
                                            ((JSONObject) jsonArray.get(i)).optString("tarjeta"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TARJETAATRAS",
                                            ((JSONObject) jsonArray.get(i)).optString("tarjetapensionatras"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "PAGO",
                                            ((JSONObject) jsonArray.get(i)).optString("pago"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ID_PROMOCION",
                                            ((JSONObject) jsonArray.get(i)).optString("id_promocion"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTAL",
                                            ((JSONObject) jsonArray.get(i)).optString("total"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "IDCONTRATORELACION",
                                            ((JSONObject) jsonArray.get(i)).optString("idcontratorelacion"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "CONTADOR",
                                            ((JSONObject) jsonArray.get(i)).optString("contador"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTALHISTORIAL",
                                            ((JSONObject) jsonArray.get(i)).optString("totalhistorial"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTALPROMOCION",
                                            ((JSONObject) jsonArray.get(i)).optString("totalpromocion"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTALPRODUCTO",
                                            ((JSONObject) jsonArray.get(i)).optString("totalproducto"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTALABONO",
                                            ((JSONObject) jsonArray.get(i)).optString("totalabono"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ULTIMOABONO",
                                            ((JSONObject) jsonArray.get(i)).optString("ultimoabono"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    if(((JSONObject) jsonArray.get(i)).optString("estatus_estadocontrato").equals("9") && !rol.equals("4")) {
                                        global.actualizarAtributoTabla("CONTRATOS", "ESTATUS_ESTADOCONTRATO", "1", "ID_CONTRATO",
                                                ((JSONObject) jsonArray.get(i)).optString("id"));
                                    }else {
                                        global.actualizarAtributoTabla("CONTRATOS", "ESTATUS_ESTADOCONTRATO",
                                                ((JSONObject) jsonArray.get(i)).optString("estatus_estadocontrato"), "ID_CONTRATO",
                                                ((JSONObject) jsonArray.get(i)).optString("id"));
                                    }
                                    global.actualizarAtributoTabla("CONTRATOS", "DIAPAGO",
                                            ((JSONObject) jsonArray.get(i)).optString("diapago"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FECHACOBROINI",
                                            ((JSONObject) jsonArray.get(i)).optString("fechacobroini"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FECHACOBROFIN",
                                            ((JSONObject) jsonArray.get(i)).optString("fechacobrofin"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FECHAATRASO",
                                            ((JSONObject) jsonArray.get(i)).optString("fechaatraso"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "COSTOATRASO",
                                            ((JSONObject) jsonArray.get(i)).optString("costoatraso"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "DIASELECCIONADO",
                                            ((JSONObject) jsonArray.get(i)).optString("diaseleccionado"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FECHAENTREGA",
                                            ((JSONObject) jsonArray.get(i)).optString("fechaentrega"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "PAGOSADELANTAR",
                                            ((JSONObject) jsonArray.get(i)).optString("pagosadelantar"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ENGANCHE",
                                            ((JSONObject) jsonArray.get(i)).optString("enganche"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ENTREGAPRODUCTO",
                                            ((JSONObject) jsonArray.get(i)).optString("entregaproducto"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "CORREO",
                                            ((JSONObject) jsonArray.get(i)).optString("correo"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ESTATUS",
                                            ((JSONObject) jsonArray.get(i)).optString("estatus"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ENVIADOPAGINA", "1", "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "PAGARE",
                                            ((JSONObject) jsonArray.get(i)).optString("pagare"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FOTOOTROS",
                                            ((JSONObject) jsonArray.get(i)).optString("fotootros"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "PROMOCIONTERMINADA",
                                            ((JSONObject) jsonArray.get(i)).optString("promocionterminada"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "SUBSCRIPCION",
                                            ((JSONObject) jsonArray.get(i)).optString("subscripcion"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FECHASUBSCRIPCION",
                                            ((JSONObject) jsonArray.get(i)).optString("fechasubscripcion"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "NOTA",
                                            ((JSONObject) jsonArray.get(i)).optString("nota"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL",
                                            ((JSONObject) jsonArray.get(i)).optString("totalreal"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "DIATEMPORAL",
                                            ((JSONObject) jsonArray.get(i)).optString("diatemporal"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "COORDENADAS",
                                            ((JSONObject) jsonArray.get(i)).optString("coordenadas"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "CALLEENTREGA",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("calleentrega")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "NUMEROENTREGA",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("numeroentrega")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "DEPTOENTREGA",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("deptoentrega")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ALLADODEENTREGA",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("alladodeentrega")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "FRENTEAENTREGA",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("frenteaentrega")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ENTRECALLESENTREGA",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("entrecallesentrega")), "ID_CONTRATO",
                                            ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "COLONIAENTREGA",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("coloniaentrega")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "LOCALIDADENTREGA",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("localidadentrega")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "CASATIPOENTREGA",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casatipoentrega")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "CASACOLORENTREGA",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casacolorentrega")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ALIAS",
                                            global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("alias")), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "AUTORIZACION",
                                            ((JSONObject) jsonArray.get(i)).optString("autorizacion"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "OPCIONLUGARENTREGA",
                                            ((JSONObject) jsonArray.get(i)).optString("opcionlugarentrega"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "OBSERVACIONFOTOINE",
                                            ((JSONObject) jsonArray.get(i)).optString("observacionfotoine"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "OBSERVACIONFOTOINEATRAS",
                                            ((JSONObject) jsonArray.get(i)).optString("observacionfotoineatras"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "OBSERVACIONFOTOCASA",
                                            ((JSONObject) jsonArray.get(i)).optString("observacionfotocasa"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "OBSERVACIONCOMPROBANTEDOMICILIO",
                                            ((JSONObject) jsonArray.get(i)).optString("observacioncomprobantedomicilio"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "OBSERVACIONPAGARE",
                                            ((JSONObject) jsonArray.get(i)).optString("observacionpagare"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "OBSERVACIONFOTOOTROS",
                                            ((JSONObject) jsonArray.get(i)).optString("observacionfotootros"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "CREATED_AT",
                                            ((JSONObject) jsonArray.get(i)).optString("created_at"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "UPDATED_AT",
                                            ((JSONObject) jsonArray.get(i)).optString("updated_at"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "NOMBREPAQUETE",
                                            ((JSONObject) jsonArray.get(i)).optString("nombrepaquete"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TITULOPROMOCION",
                                            ((JSONObject) jsonArray.get(i)).optString("titulopromocion"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "CONTRATOENVIADO", "1", "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("CONTRATOS", "ABONOMINIMO",
                                            ((JSONObject) jsonArray.get(i)).optString("abonominimo"), "ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                }
                            }
                        } else if (numTabla == 5) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("TITULO", ((JSONObject) jsonArray.get(i)).optString("titulo"));
                            valores.put("PRECIOP", ((JSONObject) jsonArray.get(i)).optString("precioP"));
                            valores.put("INICIO", ((JSONObject) jsonArray.get(i)).optString("inicio"));
                            valores.put("FIN", ((JSONObject) jsonArray.get(i)).optString("fin"));
                            valores.put("STATUS", ((JSONObject) jsonArray.get(i)).optString("status"));
                            valores.put("ASIGNADO", ((JSONObject) jsonArray.get(i)).optString("asignado"));
                            valores.put("ID_TIPOPROMOCIONUSUARIO", ((JSONObject) jsonArray.get(i)).optString("id_tipopromocionusuario"));
                            valores.put("CONTADO", ((JSONObject) jsonArray.get(i)).optString("contado"));
                            valores.put("ARMAZONES", ((JSONObject) jsonArray.get(i)).optString("armazones"));
                            valores.put("TIPOPROMOCION", ((JSONObject) jsonArray.get(i)).optString("tipopromocion"));
                            valores.put("PRECIOUNO", ((JSONObject) jsonArray.get(i)).optString("preciouno"));
                            sqLiteDB.insert("PROMOCION", null, valores);
                        } else if (numTabla == 6) {
                            if(!existeHistorial) {
                                //No existe historial
                                valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id_contrato"));
                                valores.put("EDAD", ((JSONObject) jsonArray.get(i)).optString("edad"));
                                valores.put("FECHAENTREGA", ((JSONObject) jsonArray.get(i)).optString("fechaentrega"));
                                valores.put("DIAGNOSTICO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("diagnostico")));
                                valores.put("OCUPACION", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("ocupacion")));
                                valores.put("DIABETES", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("diabetes")));
                                valores.put("HIPERTENSION", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("hipertension")));
                                valores.put("DOLOR", ((JSONObject) jsonArray.get(i)).optString("dolor"));
                                valores.put("ARDOR", ((JSONObject) jsonArray.get(i)).optString("ardor"));
                                valores.put("GOLPEOJOS", ((JSONObject) jsonArray.get(i)).optString("golpeojos"));
                                valores.put("OTROM", ((JSONObject) jsonArray.get(i)).optString("otroM"));
                                valores.put("MOLESTIAOTRO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("molestiaotro")));
                                valores.put("ULTIMOEXAMEN", ((JSONObject) jsonArray.get(i)).optString("ultimoexamen"));
                                valores.put("ESFERICODER", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("esfericoder")));
                                valores.put("CILINDRODER", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("cilindroder")));
                                valores.put("EJEDER", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("ejeder")));
                                valores.put("ADDDER", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("addder")));
                                valores.put("ALTDER", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("altder")));
                                valores.put("ESFERICOIZQ", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("esfericoizq")));
                                valores.put("CILINDROIZQ", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("cilindroizq")));
                                valores.put("EJEIZQ", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("ejeizq")));
                                valores.put("ADDIZQ", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("addizq")));
                                valores.put("ALTIZQ", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("altizq")));
                                valores.put("ID_PRODUCTO", ((JSONObject) jsonArray.get(i)).optString("id_producto"));
                                valores.put("ID_PAQUETE", ((JSONObject) jsonArray.get(i)).optString("id_paquete"));
                                valores.put("MATERIAL", ((JSONObject) jsonArray.get(i)).optString("material"));
                                valores.put("MATERIALOTRO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("materialotro")));
                                valores.put("COSTOMATERIAL", ((JSONObject) jsonArray.get(i)).optString("costomaterial"));
                                valores.put("BIFOCAL", ((JSONObject) jsonArray.get(i)).optString("bifocal"));
                                valores.put("FOTOCROMATICO", ((JSONObject) jsonArray.get(i)).optString("fotocromatico"));
                                valores.put("AR", ((JSONObject) jsonArray.get(i)).optString("ar"));
                                valores.put("TINTE", ((JSONObject) jsonArray.get(i)).optString("tinte"));
                                valores.put("BLUERAY", ((JSONObject) jsonArray.get(i)).optString("blueray"));
                                valores.put("OTROT", ((JSONObject) jsonArray.get(i)).optString("otroT"));
                                valores.put("TRATAMIENTOOTRO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("tratamientootro")));
                                valores.put("COSTOTRATAMIENTO", ((JSONObject) jsonArray.get(i)).optString("costotratamiento"));
                                valores.put("OBSERVACIONES", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("observaciones")));
                                valores.put("OBSERVACIONESINTERNO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("observacionesinterno")));
                                valores.put("TIPO", ((JSONObject) jsonArray.get(i)).optString("tipo"));
                                valores.put("BIFOCALOTRO", ((JSONObject) jsonArray.get(i)).optString("bifocalotro"));
                                valores.put("COSTOBIFOCAL", ((JSONObject) jsonArray.get(i)).optString("costobifocal"));
                                valores.put("EMBARAZADA", ((JSONObject) jsonArray.get(i)).optString("embarazada"));
                                valores.put("DURMIOSEISOCHOHORAS", ((JSONObject) jsonArray.get(i)).optString("durmioseisochohoras"));
                                valores.put("ACTIVIDADDIA", ((JSONObject) jsonArray.get(i)).optString("actividaddia"));
                                valores.put("PROBLEMASOJOS", ((JSONObject) jsonArray.get(i)).optString("problemasojos"));
                                valores.put("POLICARBONATOTIPO", ((JSONObject) jsonArray.get(i)).optString("policarbonatotipo"));
                                valores.put("ID_TRATAMIENTOCOLORTINTE", ((JSONObject) jsonArray.get(i)).optString("id_tratamientocolortinte"));
                                valores.put("ESTILOTINTE", ((JSONObject) jsonArray.get(i)).optString("estilotinte"));
                                valores.put("POLARIZADO", ((JSONObject) jsonArray.get(i)).optString("polarizado"));
                                valores.put("ID_TRATAMIENTOCOLORPOLARIZADO", ((JSONObject) jsonArray.get(i)).optString("id_tratamientocolorpolarizado"));
                                valores.put("ESPEJO", ((JSONObject) jsonArray.get(i)).optString("espejo"));
                                valores.put("ID_TRATAMIENTOCOLORESPEJO", ((JSONObject) jsonArray.get(i)).optString("id_tratamientocolorespejo"));
                                valores.put("FOTOARMAZON", ((JSONObject) jsonArray.get(i)).optString("fotoarmazon"));
                                valores.put("ENVIADOPAGINA", "1");
                                valores.put("CREATED_AT", ((JSONObject) jsonArray.get(i)).optString("created_at"));
                                valores.put("UPDATED_AT", ((JSONObject) jsonArray.get(i)).optString("updated_at"));
                                sqLiteDB.insert("HISTORIALCLINICO", null, valores);
                            }else {
                                //Existe historial
                                if(!global.esHistorialClinicoPorCrear(((JSONObject) jsonArray.get(i)).optString("id"))) {
                                    //No es historial por crear
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ID_CONTRATO",
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "EDAD",
                                            ((JSONObject) jsonArray.get(i)).optString("edad"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "FECHAENTREGA",
                                            ((JSONObject) jsonArray.get(i)).optString("fechaentrega"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "DIAGNOSTICO",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("diagnostico")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "OCUPACION",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("ocupacion")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "DIABETES",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("diabetes")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "HIPERTENSION",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("hipertension")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "DOLOR",
                                            ((JSONObject) jsonArray.get(i)).optString("dolor"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ARDOR",
                                            ((JSONObject) jsonArray.get(i)).optString("ardor"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "GOLPEOJOS",
                                            ((JSONObject) jsonArray.get(i)).optString("golpeojos"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "OTROM",
                                            ((JSONObject) jsonArray.get(i)).optString("otroM"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "MOLESTIAOTRO",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("molestiaotro")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ULTIMOEXAMEN",
                                            ((JSONObject) jsonArray.get(i)).optString("ultimoexamen"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ESFERICODER",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("esfericoder")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "CILINDRODER",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("cilindroder")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "EJEDER",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("ejeder")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ADDDER",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("addder")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ALTDER",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("altder")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ESFERICOIZQ",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("esfericoizq")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "CILINDROIZQ",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("cilindroizq")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "EJEIZQ",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("ejeizq")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ADDIZQ",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("addizq")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ALTIZQ",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("altizq")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ID_PRODUCTO",
                                            ((JSONObject) jsonArray.get(i)).optString("id_producto"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ID_PAQUETE",
                                            ((JSONObject) jsonArray.get(i)).optString("id_paquete"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "MATERIAL",
                                            ((JSONObject) jsonArray.get(i)).optString("material"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "MATERIALOTRO",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("materialotro")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "COSTOMATERIAL",
                                            ((JSONObject) jsonArray.get(i)).optString("costomaterial"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "BIFOCAL",
                                            ((JSONObject) jsonArray.get(i)).optString("bifocal"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "FOTOCROMATICO",
                                            ((JSONObject) jsonArray.get(i)).optString("fotocromatico"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "AR",
                                            ((JSONObject) jsonArray.get(i)).optString("ar"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "TINTE",
                                            ((JSONObject) jsonArray.get(i)).optString("tinte"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "BLUERAY",
                                            ((JSONObject) jsonArray.get(i)).optString("blueray"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "OTROT",
                                            ((JSONObject) jsonArray.get(i)).optString("otroT"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "TRATAMIENTOOTRO",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("tratamientootro")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "COSTOTRATAMIENTO",
                                            ((JSONObject) jsonArray.get(i)).optString("costotratamiento"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "OBSERVACIONES",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("observaciones")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "OBSERVACIONESINTERNO",
                                    global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("observacionesinterno")), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "TIPO",
                                            ((JSONObject) jsonArray.get(i)).optString("tipo"), "ID",
                                            ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "BIFOCALOTRO",
                                            ((JSONObject) jsonArray.get(i)).optString("bifocalotro"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "COSTOBIFOCAL",
                                            ((JSONObject) jsonArray.get(i)).optString("costobifocal"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "EMBARAZADA",
                                            ((JSONObject) jsonArray.get(i)).optString("embarazada"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "DURMIOSEISOCHOHORAS",
                                            ((JSONObject) jsonArray.get(i)).optString("durmioseisochohoras"), "ID",
                                            ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ACTIVIDADDIA",
                                            ((JSONObject) jsonArray.get(i)).optString("actividaddia"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "PROBLEMASOJOS",
                                            ((JSONObject) jsonArray.get(i)).optString("problemasojos"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "POLICARBONATOTIPO",
                                            ((JSONObject) jsonArray.get(i)).optString("policarbonatotipo"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ID_TRATAMIENTOCOLORTINTE",
                                            ((JSONObject) jsonArray.get(i)).optString("id_tratamientocolortinte"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ESTILOTINTE",
                                            ((JSONObject) jsonArray.get(i)).optString("estilotinte"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "POLARIZADO",
                                            ((JSONObject) jsonArray.get(i)).optString("polarizado"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ID_TRATAMIENTOCOLORPOLARIZADO",
                                            ((JSONObject) jsonArray.get(i)).optString("id_tratamientocolorpolarizado"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ESPEJO",
                                            ((JSONObject) jsonArray.get(i)).optString("espejo"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ID_TRATAMIENTOCOLORESPEJO",
                                            ((JSONObject) jsonArray.get(i)).optString("id_tratamientocolorespejo"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "FOTOARMAZON",
                                            ((JSONObject) jsonArray.get(i)).optString("fotoarmazon"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ENVIADOPAGINA", "1", "ID",
                                            ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "CREATED_AT",
                                            ((JSONObject) jsonArray.get(i)).optString("created_at"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                    global.actualizarAtributoTabla("HISTORIALCLINICO", "UPDATED_AT",
                                            ((JSONObject) jsonArray.get(i)).optString("updated_at"), "ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                                }
                            }
                        } else if (numTabla == 7) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("ID_TIPOPRODUCTO", ((JSONObject) jsonArray.get(i)).optString("id_tipoproducto"));
                            valores.put("NOMBRE", ((JSONObject) jsonArray.get(i)).optString("nombre"));
                            valores.put("PIEZAS", ((JSONObject) jsonArray.get(i)).optString("piezas"));
                            valores.put("PRECIO", ((JSONObject) jsonArray.get(i)).optString("precio"));
                            valores.put("FOTO", "");
                            valores.put("COLOR",((JSONObject) jsonArray.get(i)).optString("color").toUpperCase());
                            valores.put("ESTADO", "");
                            valores.put("INICIOP", "");
                            valores.put("FINP", "");
                            valores.put("PRECIOP", ((JSONObject) jsonArray.get(i)).optString("preciop"));
                            valores.put("ACTIVO", ((JSONObject) jsonArray.get(i)).optString("activo"));
                            valores.put("PREMIUM", ((JSONObject) jsonArray.get(i)).optString("premium"));
                            valores.put("CREATED_AT", "");
                            valores.put("UPDATED_AT", "");
                            sqLiteDB.insert("PRODUCTO", null, valores);
                        } else if (numTabla == 8) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("NOMBRE", ((JSONObject) jsonArray.get(i)).optString("nombre"));
                            valores.put("PRECIO", ((JSONObject) jsonArray.get(i)).optString("precio"));
                            sqLiteDB.insert("PAQUETES", null, valores);
                        } else if (numTabla == 9) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("NOMBRE", ((JSONObject) jsonArray.get(i)).optString("nombre"));
                            valores.put("PRECIO", ((JSONObject) jsonArray.get(i)).optString("precio"));
                            sqLiteDB.insert("TRATAMIENTOS", null, valores);
                        } else if (numTabla == 10) {
                            String datos = String.valueOf(((JSONObject) jsonArray.get(i)));
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id_contrato"));
                            valores.put("ID_PROMOCION", ((JSONObject) jsonArray.get(i)).optString("id_promocion"));
                            valores.put("ESTADO", ((JSONObject) jsonArray.get(i)).optString("estado"));
                            valores.put("ENVIADOPAGINA", "1");
                            valores.put("CREATED_AT", ((JSONObject) jsonArray.get(i)).optString("created_at"));
                            valores.put("UPDATED_AT", ((JSONObject) jsonArray.get(i)).optString("updated_at"));
                            sqLiteDB.insert("PROMOCIONCONTRATO", null, valores);
                        } else if (numTabla == 11) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id_contrato"));
                            valores.put("ID_PRODUCTO", ((JSONObject) jsonArray.get(i)).optString("id_producto"));
                            valores.put("PIEZAS", ((JSONObject) jsonArray.get(i)).optString("piezas"));
                            valores.put("TOTAL", ((JSONObject) jsonArray.get(i)).optString("total"));
                            valores.put("ID_USUARIO", ((JSONObject) jsonArray.get(i)).optString("id_usuario"));
                            valores.put("ENVIADOPAGINA", "1");
                            valores.put("CREATED_AT", ((JSONObject) jsonArray.get(i)).optString("created_at"));
                            valores.put("UPDATED_AT", ((JSONObject) jsonArray.get(i)).optString("updated_at"));
                            sqLiteDB.insert("CONTRATOPRODUCTO", null, valores);
                        } else if (numTabla == 12) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id_contrato"));
                            valores.put("ABONO", ((JSONObject) jsonArray.get(i)).optString("abono"));
                            valores.put("FOLIO", ((JSONObject) jsonArray.get(i)).optString("folio"));
                            valores.put("TIPOABONO", ((JSONObject) jsonArray.get(i)).optString("tipoabono"));
                            valores.put("ATRASO", ((JSONObject) jsonArray.get(i)).optString("atraso"));
                            valores.put("ADELANTOS", ((JSONObject) jsonArray.get(i)).optString("adelantos"));
                            valores.put("ID_USUARIO", ((JSONObject) jsonArray.get(i)).optString("id_usuario"));
                            valores.put("METODOPAGO", ((JSONObject) jsonArray.get(i)).optString("metodopago"));
                            valores.put("CORTE", ((JSONObject) jsonArray.get(i)).optString("corte"));
                            valores.put("ENVIADOPAGINA", "1");
                            valores.put("ID_CONTRATOPRODUCTO", ((JSONObject) jsonArray.get(i)).optString("id_contratoproducto"));
                            valores.put("COORDENADAS", ((JSONObject) jsonArray.get(i)).optString("coordenadas"));
                            valores.put("CREATED_AT", ((JSONObject) jsonArray.get(i)).optString("created_at"));
                            valores.put("UPDATED_AT", ((JSONObject) jsonArray.get(i)).optString("updated_at"));
                            valores.put("ENVIADO", "1");
                            valores.put("POLIZA", ((JSONObject) jsonArray.get(i)).optString("poliza"));
                            sqLiteDB.insert("ABONOS", null, valores);
                        } else if (numTabla == 13) {
                            if(!existeContrato) {
                                valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                                valores.put("DATOS", ((JSONObject) jsonArray.get(i)).optString("datos"));
                                valores.put("ID_USUARIOCREACION", ((JSONObject) jsonArray.get(i)).optString("id_usuariocreacion"));
                                valores.put("NOMBRE_USUARIOCREACION", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("nombre_usuariocreacion")));
                                valores.put("ID_ZONA", ((JSONObject) jsonArray.get(i)).optString("id_zona"));
                                valores.put("NOMBRE", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("nombre")));
                                valores.put("CALLE", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("calle")));
                                valores.put("NUMERO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("numero")));
                                valores.put("DEPTO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("depto")));
                                valores.put("ALLADODE", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("alladode")));
                                valores.put("FRENTEA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("frentea")));
                                valores.put("ENTRECALLES", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("entrecalles")));
                                valores.put("COLONIA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("colonia")));
                                valores.put("LOCALIDAD", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("localidad")));
                                valores.put("TELEFONO", ((JSONObject) jsonArray.get(i)).optString("telefono"));
                                valores.put("TELEFONOREFERENCIA", ((JSONObject) jsonArray.get(i)).optString("telefonoreferencia"));
                                valores.put("NOMBREREFERENCIA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("nombrereferencia")));
                                valores.put("CASATIPO", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casatipo")));
                                valores.put("CASACOLOR", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casacolor")));
                                valores.put("FOTOINEFRENTE", ((JSONObject) jsonArray.get(i)).optString("fotoine"));
                                valores.put("FOTOINEATRAS", ((JSONObject) jsonArray.get(i)).optString("fotoineatras"));
                                valores.put("FOTOCASA", ((JSONObject) jsonArray.get(i)).optString("fotocasa"));
                                valores.put("COMPROBANTEDOMICILIO", ((JSONObject) jsonArray.get(i)).optString("comprobantedomicilio"));
                                valores.put("ID_OPTOMETRISTA", ((JSONObject) jsonArray.get(i)).optString("id_optometrista"));
                                valores.put("TARJETAFRENTE", ((JSONObject) jsonArray.get(i)).optString("tarjeta"));
                                valores.put("TARJETAATRAS", ((JSONObject) jsonArray.get(i)).optString("tarjetapensionatras"));
                                valores.put("PAGO", ((JSONObject) jsonArray.get(i)).optString("pago"));
                                valores.put("ID_PROMOCION", ((JSONObject) jsonArray.get(i)).optString("id_promocion"));
                                valores.put("TOTAL", ((JSONObject) jsonArray.get(i)).optString("total"));
                                valores.put("IDCONTRATORELACION", ((JSONObject) jsonArray.get(i)).optString("idcontratorelacion"));
                                valores.put("CONTADOR", ((JSONObject) jsonArray.get(i)).optString("contador"));
                                valores.put("TOTALHISTORIAL", ((JSONObject) jsonArray.get(i)).optString("totalhistorial"));
                                valores.put("TOTALPROMOCION", ((JSONObject) jsonArray.get(i)).optString("totalpromocion"));
                                valores.put("TOTALPRODUCTO", ((JSONObject) jsonArray.get(i)).optString("totalproducto"));
                                valores.put("TOTALABONO", ((JSONObject) jsonArray.get(i)).optString("totalabono"));
                                valores.put("ULTIMOABONO", ((JSONObject) jsonArray.get(i)).optString("ultimoabono"));
                                valores.put("ESTATUS_ESTADOCONTRATO", ((JSONObject) jsonArray.get(i)).optString("estatus_estadocontrato"));
                                valores.put("DIAPAGO", ((JSONObject) jsonArray.get(i)).optString("diapago"));
                                valores.put("FECHACOBROINI", ((JSONObject) jsonArray.get(i)).optString("fechacobroini"));
                                valores.put("FECHACOBROFIN", ((JSONObject) jsonArray.get(i)).optString("fechacobrofin"));
                                valores.put("FECHAATRASO", ((JSONObject) jsonArray.get(i)).optString("fechaatraso"));
                                valores.put("COSTOATRASO", ((JSONObject) jsonArray.get(i)).optString("costoatraso"));
                                valores.put("DIASELECCIONADO", ((JSONObject) jsonArray.get(i)).optString("diaseleccionado"));
                                valores.put("FECHAENTREGA", ((JSONObject) jsonArray.get(i)).optString("fechaentrega"));
                                valores.put("PAGOSADELANTAR", ((JSONObject) jsonArray.get(i)).optString("pagosadelantar"));
                                valores.put("ENGANCHE", ((JSONObject) jsonArray.get(i)).optString("enganche"));
                                valores.put("ENTREGAPRODUCTO", ((JSONObject) jsonArray.get(i)).optString("entregaproducto"));
                                valores.put("CORREO", ((JSONObject) jsonArray.get(i)).optString("correo"));
                                valores.put("ESTATUS", ((JSONObject) jsonArray.get(i)).optString("estatus"));
                                valores.put("ENVIADOPAGINA", "1");
                                valores.put("PAGARE", ((JSONObject) jsonArray.get(i)).optString("pagare"));
                                valores.put("FOTOOTROS", ((JSONObject) jsonArray.get(i)).optString("fotootros"));
                                valores.put("PROMOCIONTERMINADA", ((JSONObject) jsonArray.get(i)).optString("promocionterminada"));
                                valores.put("SUBSCRIPCION", ((JSONObject) jsonArray.get(i)).optString("subscripcion"));
                                valores.put("FECHASUBSCRIPCION", ((JSONObject) jsonArray.get(i)).optString("fechasubscripcion"));
                                valores.put("NOTA", ((JSONObject) jsonArray.get(i)).optString("nota"));
                                valores.put("TOTALREAL", ((JSONObject) jsonArray.get(i)).optString("totalreal"));
                                valores.put("DIATEMPORAL", ((JSONObject) jsonArray.get(i)).optString("diatemporal"));
                                valores.put("COORDENADAS", ((JSONObject) jsonArray.get(i)).optString("coordenadas"));
                                valores.put("CALLEENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("calleentrega")));
                                valores.put("NUMEROENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("numeroentrega")));
                                valores.put("DEPTOENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("deptoentrega")));
                                valores.put("ALLADODEENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("alladodeentrega")));
                                valores.put("FRENTEAENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("frenteaentrega")));
                                valores.put("ENTRECALLESENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("entrecallesentrega")));
                                valores.put("COLONIAENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("coloniaentrega")));
                                valores.put("LOCALIDADENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("localidadentrega")));
                                valores.put("CASATIPOENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casatipoentrega")));
                                valores.put("CASACOLORENTREGA", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("casacolorentrega")));
                                valores.put("ALIAS", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("alias")));
                                valores.put("AUTORIZACION", "0");
                                valores.put("OPCIONLUGARENTREGA", ((JSONObject) jsonArray.get(i)).optString("opcionlugarentrega"));
                                valores.put("OBSERVACIONFOTOINE", ((JSONObject) jsonArray.get(i)).optString("observacionfotoine"));
                                valores.put("OBSERVACIONFOTOINEATRAS", ((JSONObject) jsonArray.get(i)).optString("observacionfotoineatras"));
                                valores.put("OBSERVACIONFOTOCASA", ((JSONObject) jsonArray.get(i)).optString("observacionfotocasa"));
                                valores.put("OBSERVACIONCOMPROBANTEDOMICILIO", ((JSONObject) jsonArray.get(i)).optString("observacioncomprobantedomicilio"));
                                valores.put("OBSERVACIONPAGARE", ((JSONObject) jsonArray.get(i)).optString("observacionpagare"));
                                valores.put("OBSERVACIONFOTOOTROS", ((JSONObject) jsonArray.get(i)).optString("observacionfotootros"));
                                valores.put("CREATED_AT", ((JSONObject) jsonArray.get(i)).optString("created_at"));
                                valores.put("UPDATED_AT", ((JSONObject) jsonArray.get(i)).optString("updated_at"));
                                valores.put("NOMBREPAQUETE", ((JSONObject) jsonArray.get(i)).optString("nombrepaquete"));
                                valores.put("TITULOPROMOCION", ((JSONObject) jsonArray.get(i)).optString("titulopromocion"));
                                valores.put("ABONOMINIMO", ((JSONObject) jsonArray.get(i)).optString("abonominimo"));
                                sqLiteDB.insert("CONTRATOS", null, valores);
                            }
                        } else if (numTabla == 14) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id_contrato"));
                            valores.put("ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                            valores.put("ID_HISTORIALGARANTIA", ((JSONObject) jsonArray.get(i)).optString("id_historialgarantia"));
                            valores.put("ID_OPTOMETRISTA", ((JSONObject) jsonArray.get(i)).optString("id_optometrista"));
                            valores.put("ESTADOGARANTIA", ((JSONObject) jsonArray.get(i)).optString("estadogarantia"));
                            valores.put("ENVIADOPAGINA", "1");
                            valores.put("CREATED_AT", ((JSONObject) jsonArray.get(i)).optString("created_at"));
                            valores.put("UPDATED_AT", ((JSONObject) jsonArray.get(i)).optString("updated_at"));
                            sqLiteDB.insert("GARANTIAS", null, valores);
                        } else if (numTabla == 15) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("DIA", ((JSONObject) jsonArray.get(i)).optString("dia"));
                            valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id_contrato"));
                            valores.put("ID_USUARIO", ((JSONObject) jsonArray.get(i)).optString("id_usuario"));
                            valores.put("POSICION", ((JSONObject) jsonArray.get(i)).optString("posicion"));
                            valores.put("ESTADO", ((JSONObject) jsonArray.get(i)).optString("estado"));
                            valores.put("ENVIADOPAGINA", "1");
                            sqLiteDB.insert("RUTA", null, valores);
                        } else if (numTabla == 16) {
                            global.actualizarAtributoTabla("MOVIL", "TOTALCONTRATOSCONABONO",
                                    seguridad.cifrar(((((JSONObject) jsonArray.get(i)).optString("totalcontratosconabonos"))).getBytes("UTF-16LE")),
                                    "ID", seguridad.cifrar(obtenerRol.obtenerAtributoUsuarioLogeado("ID").getBytes("UTF-16LE")));
                        } else if (numTabla == 17) {
                            valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("NOMBRE", ((JSONObject) jsonArray.get(i)).optString("nombre"));
                            valores.put("COLONIA", ((JSONObject) jsonArray.get(i)).optString("colonia"));
                            valores.put("CALLE", ((JSONObject) jsonArray.get(i)).optString("calle"));
                            valores.put("NUMERO", ((JSONObject) jsonArray.get(i)).optString("numero"));
                            valores.put("TELEFONO", ((JSONObject) jsonArray.get(i)).optString("telefono"));
                            valores.put("CAMBIOS", ((JSONObject) jsonArray.get(i)).optString("cambios"));
                            sqLiteDB.insert("CONTRATOSLIOSFUGAS", null, valores);
                        } else if (numTabla == 18) {
                            if(!existeHistorial) {
                                //No existe historialsinconversion
                                valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id_contrato"));
                                valores.put("ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                valores.put("ESFERICODER", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("esfericoder")));
                                valores.put("CILINDRODER", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("cilindroder")));
                                valores.put("EJEDER", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("ejeder")));
                                valores.put("ADDDER", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("addder")));
                                valores.put("ESFERICOIZQ", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("esfericoizq")));
                                valores.put("CILINDROIZQ", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("cilindroizq")));
                                valores.put("EJEIZQ", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("ejeizq")));
                                valores.put("ADDIZQ", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("addizq")));
                                valores.put("ENVIADOPAGINA", "1");
                                valores.put("CREATED_AT", ((JSONObject) jsonArray.get(i)).optString("created_at"));
                                sqLiteDB.insert("HISTORIALSINCONVERSION", null, valores);
                            }else {
                                //Existe historialsinconversion
                                if(!global.esHistorialClinicoPorCrear(((JSONObject) jsonArray.get(i)).optString("id_historial"))) {
                                    //No es historialsinconversion por crear
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "ID_CONTRATO",
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "ID_HISTORIAL",
                                            ((JSONObject) jsonArray.get(i)).optString("id_historial"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "ESFERICODER",
                                            ((JSONObject) jsonArray.get(i)).optString("esfericoder"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "CILINDRODER",
                                            ((JSONObject) jsonArray.get(i)).optString("cilindroder"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "EJEDER",
                                            ((JSONObject) jsonArray.get(i)).optString("ejeder"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "ADDDER",
                                            ((JSONObject) jsonArray.get(i)).optString("addder"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "ESFERICOIZQ",
                                            ((JSONObject) jsonArray.get(i)).optString("esfericoizq"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "CILINDROIZQ",
                                            ((JSONObject) jsonArray.get(i)).optString("cilindroizq"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "EJEIZQ",
                                            ((JSONObject) jsonArray.get(i)).optString("ejeizq"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "ADDIZQ",
                                            ((JSONObject) jsonArray.get(i)).optString("addizq"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "ENVIADOPAGINA", "1", "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                    global.actualizarAtributoTabla("HISTORIALSINCONVERSION", "CREATED_AT",
                                            ((JSONObject) jsonArray.get(i)).optString("created_at"), "ID_CONTRATO ='" +
                                            ((JSONObject) jsonArray.get(i)).optString("id_contrato") + "' AND ID_HISTORIAL", ((JSONObject) jsonArray.get(i)).optString("id_historial"));
                                }
                            }
                        } else if (numTabla == 19) {
                            valores.put("LLAVE", seguridad.cifrar((((JSONObject) jsonArray.get(i)).optString("llave")).getBytes("UTF-16LE")));
                            valores.put("TIPO", seguridad.cifrar((((JSONObject) jsonArray.get(i)).optString("tipo")).getBytes("UTF-16LE")));
                            sqLiteDB.insert("LLAVES", null, valores);
                        }else if (numTabla == 20) {
                            valores.put("NOMBRE", ((JSONObject) jsonArray.get(i)).optString("nombre"));
                            valores.put("ID_USUARIO", ((JSONObject) jsonArray.get(i)).optString("id_usuario"));
                            valores.put("ROL", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("rol")));
                            valores.put("NUMEROVENTAS", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("numeroventas")));
                            valores.put("SUCURSAL", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("sucursal")));
                            sqLiteDB.insert("VENTAS", null, valores);
                        }else if (numTabla == 21) {
                            valores.put("INDICE", ((JSONObject) jsonArray.get(i)).optString("indice"));
                            valores.put("FOTOLOGO", ((JSONObject) jsonArray.get(i)).optString("fotologo"));
                            valores.put("COLORICONOS", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("coloriconos")));
                            valores.put("COLORENCABEZADOS", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("colorencabezados")));
                            valores.put("COLORNAVBAR", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("colornavbar")));
                            valores.put("ESTADOCONFIGURACION", global.replaceCadena(((JSONObject) jsonArray.get(i)).optString("estadoconfiguracion")));
                            sqLiteDB.insert("CONFIGURACIONMOVIL", null, valores);
                        }else if (numTabla == 22) {
                            valores.put("INDICE", ((JSONObject) jsonArray.get(i)).optString("indice"));
                            valores.put("ID_ZONA", ((JSONObject) jsonArray.get(i)).optString("id_zona"));
                            valores.put("COLONIA", ((JSONObject) jsonArray.get(i)).optString("colonia"));
                            valores.put("LOCALIDAD", ((JSONObject) jsonArray.get(i)).optString("localidad"));
                            valores.put("BANDERA", ((JSONObject) jsonArray.get(i)).optString("bandera"));
                            sqLiteDB.insert("COLONIAS", null, valores);
                        }else if (numTabla == 23) {
                            valores.put("INDICE", ((JSONObject) jsonArray.get(i)).optString("indice"));
                            valores.put("ID_TRATAMIENTO", ((JSONObject) jsonArray.get(i)).optString("id_tratamiento"));
                            valores.put("COLOR", ((JSONObject) jsonArray.get(i)).optString("color"));
                            sqLiteDB.insert("TRATAMIENTOSCOLORES", null, valores);
                        }else if (numTabla == 24) {
                            valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("FOTOINEFRENTE", ((JSONObject) jsonArray.get(i)).optString("fotoine"));
                            valores.put("FOTOINEATRAS", ((JSONObject) jsonArray.get(i)).optString("fotoineatras"));
                            valores.put("FOTOCASA", ((JSONObject) jsonArray.get(i)).optString("fotocasa"));
                            valores.put("COMPROBANTEDOMICILIO", ((JSONObject) jsonArray.get(i)).optString("comprobantedomicilio"));
                            valores.put("PAGARE", ((JSONObject) jsonArray.get(i)).optString("pagare"));
                            valores.put("FOTOOTROS", ((JSONObject) jsonArray.get(i)).optString("fotootros"));
                            sqLiteDB.insert("IMAGENESSERVIDORCONTRATOS", null, valores);
                        }else if (numTabla == 25) {
                            valores.put("ID_CONTRATO", ((JSONObject) jsonArray.get(i)).optString("id_contrato"));
                            valores.put("DESCRIPCION", ((JSONObject) jsonArray.get(i)).optString("descripcion"));
                            valores.put("ESTADO", ((JSONObject) jsonArray.get(i)).optString("estado"));
                            valores.put("ENVIADOPAGINA", "1");
                            sqLiteDB.insert("CONTRATOSLISTANEGRA", null, valores);
                        }else if (numTabla == 26) {
                            valores.put("ID", ((JSONObject) jsonArray.get(i)).optString("id"));
                            valores.put("NOTA", ((JSONObject) jsonArray.get(i)).optString("nota"));
                            valores.put("CREATED_AT", ((JSONObject) jsonArray.get(i)).optString("created_at"));
                            valores.put("ENVIADOPAGINA", "1");
                            valores.put("BANDERAELIMINADO", "0");
                            sqLiteDB.insert("NOTASCOBRANZA", null, valores);
                        }else if (numTabla == 27) {
                            valores.put("ID_USUARIO", ((JSONObject) jsonArray.get(i)).optString("id_usuario"));
                            valores.put("NOMBRE", ((JSONObject) jsonArray.get(i)).optString("nombre"));
                            valores.put("ASISTENCIA", ((JSONObject) jsonArray.get(i)).optString("asistencia"));
                            valores.put("REGISTROSALIDA", ((JSONObject) jsonArray.get(i)).optString("registrosalida"));
                            valores.put("ENVIADOPAGINA", "1");
                            sqLiteDB.insert("ASISTENCIA", null, valores);
                        }
                    }

                    if(sqLiteDB.isOpen()){
                        sqLiteDB.close();
                    }


                } catch (JSONException e) {
                    global.escribirError(e, 93);
                    e.printStackTrace();
                }

            } catch (Exception e) {
                global.escribirError(e, 94);
                Log.i("ERRORBD", e.getMessage() + "");
            }

        }

    }

    private boolean existeRegistro(String tabla, String atributo, String id) {

        boolean existe = true;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();

            String SQL = "SELECT " + atributo + " FROM " + tabla + " WHERE " + atributo + " = '" + id + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                existe = false;
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 95);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return existe;
    }

    public void actualizarBanderaDescargaCompleta(String status) {

        Log.i("MENSAJE", "actualizarBanderaDescargaCompleta");

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

            String consulta = "UPDATE GLOBAL SET DESCARGACOMPLETA = '" + status + "' WHERE ID = '1'";

            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 96);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Clase: subirArchivoFtp
      Descripcion: Subir imagenes a servidor FTP
    */
    public static class subirArchivoFtp extends AsyncTask<String, Integer, Boolean> {

        String absolutePath;
        String nombreImagen;
        String carpeta;
        String idContrato;

        public subirArchivoFtp(String absolutePath, String nombreImagen, String carpetaFTP, String idContrato) {
            this.absolutePath = absolutePath;
            this.nombreImagen = nombreImagen;
            this.carpeta = carpetaFTP;
            this.idContrato = idContrato;
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            FTPClient clienteFtp = null;
            try{

                clienteFtp = new FTPClient();

                clienteFtp.connect(llaves.ruta_ftp,21);
                clienteFtp.setSoTimeout(10000);
                clienteFtp.enterLocalPassiveMode();
                if(clienteFtp.login(llaves.usuario_ftp, llaves.contrasena_ftp)){
                    clienteFtp.setFileType(FTP.BINARY_FILE_TYPE);
                    clienteFtp.setFileTransferMode(FTP.BINARY_FILE_TYPE);

                    InputStream input = new FileInputStream(absolutePath);
                    clienteFtp.storeFile("/" + llaves.carpeta_principal_ftp + "/" + carpeta + "/" + nombreImagen, input);
                    int reply = clienteFtp.getReplyCode();
                    if(!FTPReply.isPositiveCompletion(reply)){
                        clienteFtp.disconnect();
                        Log.i("MENSAJE","EL SERVIDOR NO PERMITIO CONECTARSE");
                    }
                    input.close();
                    clienteFtp.logout();
                    return true;
                }
                return false;

            }catch(Exception e){
                global.escribirError(e, 97);
                Log.i("MENSAJE","Error: "+e);
                return false;
            }finally {
                if(clienteFtp.isConnected()){
                    try{clienteFtp.disconnect();}
                    catch(Exception e){
                        global.escribirError(e, 98);
                        Log.i("MENSAJE","Error: "+e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean enviado) {
            if (enviado) {
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", carpeta.toUpperCase(), "1", "ID_CONTRATO", idContrato);
                Log.i("MENSAJE", "Cambio el valor a 1");
            }else {
                Log.i("MENSAJE", "No paso nada");
            }
        }
    }

    /*Clase: verificarSiExisteArchivoFTP
      Descripcion: Verificar si existe archivo en el FTP
    */
    public static class verificarSiExisteArchivoFTP extends AsyncTask<String, Integer, Boolean> {

        List<String> imagenesPrecargadas;
        List<String> carpetasPrecargadas;
        String idContrato;

        public verificarSiExisteArchivoFTP(List<String> imagenesPrecargadas, List<String> carpetasPrecargadas, String idContrato) {
            this.imagenesPrecargadas = new ArrayList<>();
            this.carpetasPrecargadas = new ArrayList<>();
            this.imagenesPrecargadas = imagenesPrecargadas;
            this.carpetasPrecargadas = carpetasPrecargadas;
            this.idContrato = idContrato;
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            FTPClient clienteFtp = null;
            try{

                clienteFtp = new FTPClient();

                clienteFtp.connect(llaves.ruta_ftp,21);
                clienteFtp.setSoTimeout(10000);
                clienteFtp.enterLocalPassiveMode();
                if(clienteFtp.login(llaves.usuario_ftp, llaves.contrasena_ftp)){

                    int reply = clienteFtp.getReplyCode();
                    if(!FTPReply.isPositiveCompletion(reply)){
                        clienteFtp.disconnect();
                        Log.i("MENSAJE","EL SERVIDOR NO PERMITIO CONECTARSE");
                    }

                    for(int i = 0; i < carpetasPrecargadas.size(); i++) {

                        String[] archivosFTP = clienteFtp.listNames("/" + llaves.carpeta_principal_ftp + "/" + carpetasPrecargadas.get(i));

                        List<String> stringList = new ArrayList<String>(Arrays.asList(archivosFTP));
                        if (stringList.contains("/" + llaves.carpeta_principal_ftp + "/" + carpetasPrecargadas.get(i) + "/" + imagenesPrecargadas.get(i))) {
                            global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", carpetasPrecargadas.get(i).toUpperCase(), "2", "ID_CONTRATO", idContrato);
                            File fotoAEliminar = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/luzatuvida/.nomedia/" + imagenesPrecargadas.get(i));
                            if(fotoAEliminar.exists()) {
                                fotoAEliminar.delete();
                            }
                            Log.i("MENSAJE","Cambio el valor a 2");
                        }else {
                            global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", carpetasPrecargadas.get(i).toUpperCase(), "0", "ID_CONTRATO", idContrato);
                            Log.i("MENSAJE","Cambio el valor a 0");
                        }
                    }

                    global.eliminarRegistroTablaImagenesCargadas();

                    clienteFtp.logout();
                    return true;
                }
                return false;

            }catch(Exception e){
                global.escribirError(e, 99);
                Log.i("MENSAJE","Error: "+e);
                return false;
            }finally {
                if(clienteFtp.isConnected()){
                    try{clienteFtp.disconnect();}
                    catch(Exception e){
                        global.escribirError(e, 100);
                        Log.i("MENSAJE","Error: "+e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean enviado) {
            if(enviado){
                Log.i("MENSAJE","El que sea");
            }else{
                Log.i("MENSAJE","No el que sea");
            }
        }
    }

    /*Metodo/Funcion: obtenerAtributoJSONDecodificado
      Descripcion: Obtener un atributo de un JSON ya decodificado
    */
    public String obtenerAtributoJSONDecodificado(String datos, String name) {

        String atributo = "";

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(new String(Base64.decode(datos, Base64.DEFAULT)));
        } catch (JSONException e) {
            global.escribirError(e, 101);
            e.printStackTrace();
        }

        try{
            atributo = ((JSONObject)jsonArray.get(0)).optString(name);
        } catch (JSONException e) {
            global.escribirError(e, 102);
            e.printStackTrace();
        }

        return atributo;
    }

    public static class subirArchivoSupervisionFtp extends AsyncTask<String, Integer, Boolean> {

        String absolutePath;
        String nombreImagen;
        String carpeta;
        String columnaTabla;
        String idReferencia;

        public subirArchivoSupervisionFtp(String absolutePath, String nombreImagen, String carpetaFTP, String columnaTabla, String idReferencia) {
            this.absolutePath = absolutePath;
            this.nombreImagen = nombreImagen;
            this.carpeta = carpetaFTP;
            this.columnaTabla = columnaTabla;
            this.idReferencia = idReferencia;
        }

        @Override
        protected Boolean doInBackground(String... strings) {

                FTPClient clienteFtp = null;
                try{
                    clienteFtp = new FTPClient();

                    clienteFtp.connect(llaves.ruta_ftp,21);
                    clienteFtp.setSoTimeout(10000);
                    clienteFtp.enterLocalPassiveMode();
                    if(clienteFtp.login(llaves.usuario_ftp, llaves.contrasena_ftp)){
                        clienteFtp.setFileType(FTP.BINARY_FILE_TYPE);
                        clienteFtp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
                        InputStream input = new FileInputStream(absolutePath);
                        clienteFtp.storeFile("/" + llaves.carpeta_vehiculos_ftp + "/" + carpeta + "/" + nombreImagen, input);
                        int reply = clienteFtp.getReplyCode();
                        if(!FTPReply.isPositiveCompletion(reply)){
                            clienteFtp.disconnect();
                            Log.i("MENSAJE","EL SERVIDOR NO PERMITIO CONECTARSE");
                        }
                        input.close();
                        clienteFtp.logout();
                        return true;
                    }
                    return false;

                }catch(Exception e){
                    global.escribirError(e, 293);
                    Log.i("MENSAJE","Error: "+e);
                    return false;
                }finally {
                    if(clienteFtp.isConnected()){
                        try{clienteFtp.disconnect();}
                        catch(Exception e){
                            global.escribirError(e, 294);
                            Log.i("MENSAJE","Error: "+e);
                        }
                    }
                }

        }

        @Override
        protected void onPostExecute(Boolean enviado) {
            if (enviado) {
                global.actualizarAtributoTabla("IMAGENESCARGADASSUPERVISION", columnaTabla.toUpperCase(), "1", "INDICE_SUPERVISION",idReferencia);
                Log.i("MENSAJE", "Cambio el valor a 1");
            }else {
                Log.i("MENSAJE", "No paso nada");
            }
        }
    }

    public static class verificarSiExisteArchivoSupervisionFTP extends AsyncTask<String, Integer, Boolean> {

        List<String> imagenesPrecargadas;
        List<String> carpetasPrecargadas;
        List<String> columnaTabla;
        String indiceSupervision;

        public verificarSiExisteArchivoSupervisionFTP(List<String> imagenesPrecargadas, List<String> carpetasPrecargadas, List<String> columnasTabla, String indiceSupervision) {
            this.imagenesPrecargadas = new ArrayList<>();
            this.carpetasPrecargadas = new ArrayList<>();
            this.columnaTabla = new ArrayList<>();
            this.imagenesPrecargadas = imagenesPrecargadas;
            this.carpetasPrecargadas = carpetasPrecargadas;
            this.columnaTabla = columnasTabla;
            this.indiceSupervision = indiceSupervision;
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            FTPClient clienteFtp = null;
            try{

                clienteFtp = new FTPClient();

                clienteFtp.connect(llaves.ruta_ftp,21);
                clienteFtp.setSoTimeout(10000);
                clienteFtp.enterLocalPassiveMode();
                if(clienteFtp.login(llaves.usuario_ftp, llaves.contrasena_ftp)){

                    int reply = clienteFtp.getReplyCode();
                    if(!FTPReply.isPositiveCompletion(reply)){
                        clienteFtp.disconnect();
                        Log.i("MENSAJE","EL SERVIDOR NO PERMITIO CONECTARSE");
                    }

                    for(int i = 0; i < carpetasPrecargadas.size(); i++) {

                        String[] archivosFTP = clienteFtp.listNames("/" + llaves.carpeta_vehiculos_ftp + "/" + carpetasPrecargadas.get(i));

                        List<String> stringList = new ArrayList<String>(Arrays.asList(archivosFTP));
                        if (stringList.contains("/" + llaves.carpeta_vehiculos_ftp + "/" + carpetasPrecargadas.get(i) + "/" + imagenesPrecargadas.get(i))) {
                            global.actualizarAtributoTabla("IMAGENESCARGADASSUPERVISION", columnaTabla.get(i).toUpperCase(), "2", "INDICE_SUPERVISION", indiceSupervision);
                            File fotoAEliminar = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/luzatuvida/.nomedia/" + imagenesPrecargadas.get(i));
                            if(fotoAEliminar.exists()) {
                                fotoAEliminar.delete();
                            }
                            Log.i("MENSAJE","Cambio el valor a 2");
                        }else {
                            global.actualizarAtributoTabla("IMAGENESCARGADASSUPERVISION", columnaTabla.get(i).toUpperCase(), "2", "INDICE_SUPERVISION", indiceSupervision);
                            Log.i("MENSAJE","Cambio el valor a 0");
                        }
                    }

                    global.eliminarRegistroTablaImagenesCargadasSupervision(0);

                    clienteFtp.logout();
                    return true;
                }
                return false;

            }catch(Exception e){
                global.escribirError(e, 295);
                Log.i("MENSAJE","Error: "+e);
                return false;
            }finally {
                if(clienteFtp.isConnected()){
                    try{clienteFtp.disconnect();}
                    catch(Exception e){
                        global.escribirError(e, 296);
                        Log.i("MENSAJE","Error: "+e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean enviado) {
            if(enviado){
                Log.i("MENSAJE","El que sea");
            }else{
                Log.i("MENSAJE","No el que sea");
            }
        }
    }

    /*Clase: subirArchivoPendienteFtp
    Descripcion: Subir imagenes a servidor FTP
    */
    public static class subirArchivoPendienteFtp extends AsyncTask<String, Integer, Boolean> {

        String absolutePath;
        String nombreImagen;
        String carpeta;

        public subirArchivoPendienteFtp(String absolutePath, String nombreImagen, String carpetaFTP) {
            this.absolutePath = absolutePath;
            this.nombreImagen = nombreImagen;
            this.carpeta = carpetaFTP;
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            FTPClient clienteFtp = null;
            try{

                clienteFtp = new FTPClient();

                clienteFtp.connect(llaves.ruta_ftp,21);
                clienteFtp.setSoTimeout(10000);
                clienteFtp.enterLocalPassiveMode();
                if(clienteFtp.login(llaves.usuario_ftp, llaves.contrasena_ftp)){
                    clienteFtp.setFileType(FTP.BINARY_FILE_TYPE);
                    clienteFtp.setFileTransferMode(FTP.BINARY_FILE_TYPE);

                    InputStream input = new FileInputStream(absolutePath);
                    clienteFtp.storeFile("/" + llaves.carpeta_principal_ftp + "/" + carpeta + "/" + nombreImagen, input);
                    int reply = clienteFtp.getReplyCode();
                    if(!FTPReply.isPositiveCompletion(reply)){
                        clienteFtp.disconnect();
                        Log.i("MENSAJE","EL SERVIDOR NO PERMITIO CONECTARSE");
                    }
                    input.close();
                    clienteFtp.logout();
                    return true;
                }
                return false;

            }catch(Exception e){
                global.escribirError(e, 315);
                Log.i("MENSAJE","Error: "+e);
                return false;
            }finally {
                if(clienteFtp.isConnected()){
                    try{clienteFtp.disconnect();}
                    catch(Exception e){
                        global.escribirError(e, 316);
                        Log.i("MENSAJE","Error: "+e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean enviado) {
            if (enviado) {
                global.actualizarAtributoTabla("IMAGENESPENDIENTESMOVIL","BANDERA","1","NOMBREFOTO",nombreImagen);
            }else {
                Log.i("MENSAJE", "No paso nada");
            }
        }
    }

    public static class verificarArchivoEnFTP extends AsyncTask<String, Integer, Boolean> {

        String imagenCargada;
        String carpetaImagen;

        public verificarArchivoEnFTP(String imagenCargada, String carpetaImagen) {
            this.imagenCargada = imagenCargada;
            this.carpetaImagen = carpetaImagen;
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            FTPClient clienteFtp = null;
            try{

                clienteFtp = new FTPClient();

                clienteFtp.connect(llaves.ruta_ftp,21);
                clienteFtp.setSoTimeout(10000);
                clienteFtp.enterLocalPassiveMode();
                if(clienteFtp.login(llaves.usuario_ftp, llaves.contrasena_ftp)){

                    int reply = clienteFtp.getReplyCode();
                    if(!FTPReply.isPositiveCompletion(reply)){
                        clienteFtp.disconnect();
                        Log.i("MENSAJE","EL SERVIDOR NO PERMITIO CONECTARSE");
                    }

                        String[] archivosFTP = clienteFtp.listNames("/" + llaves.carpeta_principal_ftp + "/" + carpetaImagen);

                        List<String> stringList = new ArrayList<String>(Arrays.asList(archivosFTP));
                        if (stringList.contains("/" + llaves.carpeta_principal_ftp + "/" + carpetaImagen + "/" + imagenCargada)) {
                            File fotoAEliminar = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/luzatuvida/.nomedia/" + imagenCargada);
                            if(fotoAEliminar.exists()) {
                                fotoAEliminar.delete();
                            }
                        }else {
                            Log.i("MENSAJE","No existe imagen " + imagenCargada + " en el servidor");
                        }

                    clienteFtp.logout();
                    return true;
                }
                return false;

            }catch(Exception e){
                global.escribirError(e, 317);
                Log.i("MENSAJE","Error: "+e);
                return false;
            }finally {
                if(clienteFtp.isConnected()){
                    try{clienteFtp.disconnect();}
                    catch(Exception e){
                        global.escribirError(e, 318);
                        Log.i("MENSAJE","Error: "+e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean enviado) {
            if(enviado){
                global.actualizarAtributoTabla("IMAGENESPENDIENTESMOVIL","BANDERA","2","NOMBREFOTO",imagenCargada);
                global.eliminarRegistroTablaImagenesPendientesMovil();
            }else{
                Log.i("MENSAJE","Tarea subir imagen pendientes 48 hrs ejecutada");
            }
        }
    }
}
