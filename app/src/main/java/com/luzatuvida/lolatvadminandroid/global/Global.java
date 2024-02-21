package com.luzatuvida.lolatvadminandroid.global;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.luzatuvida.lolatvadminandroid.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Global {

    /*
    Identificador error del 53 al 72, 252
     */

    Fragment fragmento;
    baseDeDatos conexion;
    SQLiteDatabase sqLiteDB;
    Llaves llaves;
    static String file_path;

    Seguridad seguridad;

    ObtenerRol obtenerRol;

    public String toolBarBackground = "#0AA09E";
    Runnable runnable;
    int tiempoEspera;

    Sincronizacion sincronizacion;

    Localizacion localizacion;

    Internet internet;

    public Global(Fragment fragmento) {

        file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        this.fragmento = fragmento;
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
    }

    /*Metodo/Funcion: imprimirMensajeEnHilo
      Descripcion: Muestra mensaje en hilo (Segundo plano)
    */
    public void imprimirMensajeEnHilo(String mensaje) {

        if(fragmento.getActivity() == null) {
            return;
        }else {
            fragmento.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(fragmento.getActivity(), mensaje, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    /*Metodo/Funcion: actualizarAtributoTabla
      Descripcion: Actualizar atributo de cualquier tabla
    */
    public void actualizarAtributoTabla(String tabla, String atributo, String estado, String idAtributo, String id) {
        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE " + tabla + " SET " + atributo + " = '" + estado + "' WHERE " + idAtributo + "='" + id + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            escribirError(e, 53);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    /*Metodo/Funcion: obtenerAtributoContrato
      Descripcion: Se obtiene atributo de la tabla de contratos
    */
    public String obtenerAtributoContrato(String idContrato, String atributo) {

        String atributoContrato = "";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT " + atributo + " FROM CONTRATOS WHERE ID_CONTRATO='" + idContrato + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el atributo del contrato");
            }

            if (datos.moveToFirst()) {
                atributoContrato = datos.getString(0);
            }

            sqLiteDB2.close();
            datos.close();


        }catch (SQLiteException e){
            escribirError(e, 54);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return atributoContrato;

    }

    public String obtenerAtributoTablaImagenesCargadasContrato(String idContrato, String atributo) {

        String atributoContrato = "";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT " + atributo + " FROM IMAGENESCARGADASCONTRATOS WHERE ID_CONTRATO='" + idContrato + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el atributo en el contrato");
            }

            if (datos.moveToFirst()) {
                atributoContrato = datos.getString(0);
            }

            sqLiteDB2.close();
            datos.close();


        }catch (SQLiteException e){
            escribirError(e, 55);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return atributoContrato;

    }

    public void eliminarRegistroTablaImagenesCargadas() {

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            String SQL = "DELETE FROM IMAGENESCARGADASCONTRATOS WHERE FOTOINE = '2'" +
                    " AND FOTOINEATRAS = '2' AND FOTOCASA = '2' AND COMPROBANTEDOMICILIO = '2'" +
                    " AND PAGARE = '2' AND TARJETAPENSION = '2' AND TARJETAPENSIONATRAS = '2' AND FOTOOTROS = '2'" +
                    " AND FOTOMOVIMIENTO = '2' AND FOTOARMAZON1 = '2' AND FOTOARMAZON2 = '2'";
            sqLiteDB2.execSQL(SQL);
            sqLiteDB2.close();

        }catch (SQLiteException e){
            escribirError(e, 56);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

    }

    public boolean esHistorialClinicoPorCrear(String idHistorialClinico) {

        boolean respuesta = true;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT H.ID FROM HISTORIALCLINICO H INNER JOIN CONTRATOS C ON H.ID_CONTRATO = C.ID_CONTRATO" +
                    " WHERE H.ID='" + idHistorialClinico + "' AND C.ESTATUS_ESTADOCONTRATO = '13'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                respuesta = false;
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            escribirError(e, 57);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return respuesta;
    }

    public String obtenerAtributoTabla(String tabla, String atributo, String idAtributo, String id) {

        String  respuesta = "";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT " + atributo + " FROM " + tabla + " WHERE " + idAtributo + " = '" + id + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()){
                respuesta = datos.getString(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            escribirError(e, 58);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return respuesta;

    }

    /*Metodo/Funcion: iniciarDescargaAppURL
      Descripcion: Descargar app desde una url
    */
    public void iniciarDescargaAppURL(String url) {

        //Crear la solicitud de descarga
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        //Tipo de red
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("lolatv.apk");
        request.setDescription("Descargando archivo...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "lolatv.apk");

        //Obtener el servicio
        try {
            DownloadManager manager = (DownloadManager) fragmento.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            Toast.makeText(fragmento.getActivity(), "La descarga ha iniciado", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            escribirError(e, 59);
            Log.i("MENSAJE",e.toString());
        }


    }

    /*Metodo/Funcion: escribirMovimientoEnArchivoTxt
      Descripcion: Crear/Escribe los movimientos del cobrador
    */
    public void escribirMovimientoEnArchivoTxt(String movimiento) {

        try {
            File archivoMovimientos = new File(file_path + "/luzatuvida/.nomedia/movimientos.txt");

            if (!archivoMovimientos.exists()) {
                String mensaje = archivoMovimientos.createNewFile() ? "Archivo de movimientos creado correctamente" : "Archivo de movimientos no creado";
                Log.i("MENSAJE", "Carpeta LuzATuVida :  " + mensaje);
            }

            BufferedWriter buf = new BufferedWriter(new FileWriter(archivoMovimientos, true));
            buf.append(movimiento + "\n******************************************************");
            buf.newLine();
            buf.close();

        } catch (Exception e) {
            escribirError(e, 60);
            Log.i("MENSAJE",e.toString());
        }
    }

    public void removerLineasEnArchivoMovimientosTxt(String fechaActual) {

        //fechaActual = "2022-04-15";
        Scanner file;
        PrintWriter writer;

        try {

            File archivoMovimientos2 = new File(file_path + "/luzatuvida/.nomedia/movimientos2.txt");

            if (!archivoMovimientos2.exists()) {
                String mensaje = archivoMovimientos2.createNewFile() ? "Archivo de movimientos2 creado correctamente" : "Archivo de movimientos2 no creado";
                Log.i("MENSAJE", "Carpeta LuzATuVida :  " + mensaje);
            }

            file = new Scanner(new File(file_path + "/luzatuvida/.nomedia/movimientos.txt"));
            writer = new PrintWriter(file_path + "/luzatuvida/.nomedia/movimientos2.txt");

            boolean bandera = true;

            while (file.hasNext()) {

                String line = file.nextLine();

                if(!line.contains("*")) {
                    //No es separador, son los datos

                    String[] datos = line.split(",");

                    try {

                        //Validacion para saber si es el archivo antiguo
                        String[] fechaRegistro = datos[0].split(" ");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        sdf.parse(fechaRegistro[0]); //Validacion principal para que truene y no entre la linea antigua

                        //Si llega aqui es por que son lineas actuales
                        if (diasDiferenciaEntreFechas(fechaActual, datos[0]) > -15) {
                            writer.write(line);
                            writer.write("\n");
                            bandera = true;
                        }else {
                            bandera = false;
                        }

                    }catch (Exception e) {
                        //Si llega aqui es por que son lineas antiguas
                        bandera = false;
                        Log.i("ERROR", e.toString());
                    }

                }else {
                    //Es separador
                    if(bandera) {
                        writer.write(line);
                        writer.write("\n");
                    }
                }
            }

            file.close();
            writer.close();

            File file1 = new File(file_path + "/luzatuvida/.nomedia/movimientos.txt");
            File file2 = new File(file_path + "/luzatuvida/.nomedia/movimientos2.txt");

            file1.delete();
            file2.renameTo(file1);

        } catch (Exception e) {
            escribirError(e, 61);
            Log.i("ERROR", e.toString());
        }

    }

    /*Metodo/Funcion: escribirError
      Descripcion: Crear/Escribe los errores de la app
    */
    public void escribirError(Exception e, int identificador) {

        obtenerRol = new ObtenerRol(fragmento);

        String fechaactual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");

        String error = fechaactual + "," + identificador + "," + e.toString();

        try {
            File archivoErrores = new File(file_path + "/luzatuvida/.nomedia/reporte.txt");

            if (!archivoErrores.exists()) {
                String mensaje = archivoErrores.createNewFile() ? "Archivo de reporte creado correctamente" : "Archivo de reporte no creado";
                Log.i("MENSAJE", "Carpeta LuzATuVida :  " + mensaje);
            }

            BufferedWriter buf = new BufferedWriter(new FileWriter(archivoErrores, true));
            buf.append(error + "\n******************************************************");
            buf.newLine();
            buf.close();

            //removerLineasEnArchivoErroresTxt(fechaactual);

        } catch (Exception ex) {
            Log.i("MENSAJE",ex.toString());
        }
    }

    /*Metodo/Funcion: escribirErrorClasePrincipalThrowable
      Descripcion: Crear/Escribe los errores de la app
    */
    public void escribirErrorClasePrincipalThrowable(Throwable e, int identificador) {

        obtenerRol = new ObtenerRol(fragmento);

        String fechaactual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");

        String error = fechaactual + "," + identificador + "t," + e.toString();

        try {
            File archivoErrores = new File(file_path + "/luzatuvida/.nomedia/reporte.txt");

            if (!archivoErrores.exists()) {
                String mensaje = archivoErrores.createNewFile() ? "Archivo de reporte creado correctamente" : "Archivo de reporte no creado";
                Log.i("MENSAJE", "Carpeta LuzATuVida :  " + mensaje);
            }

            BufferedWriter buf = new BufferedWriter(new FileWriter(archivoErrores, true));
            buf.append(error + "\n******************************************************");
            buf.newLine();
            buf.close();

        } catch (Exception ex) {
            Log.i("MENSAJE",ex.toString());
        }
    }

    public void removerLineasEnArchivoErroresTxt(String fechaActual) {

        //fechaActual = "2022-04-15";
        Scanner file;
        PrintWriter writer;

        try {

            File archivoErrores2 = new File(file_path + "/luzatuvida/.nomedia/reporte2.txt");

            if (!archivoErrores2.exists()) {
                String mensaje = archivoErrores2.createNewFile() ? "Archivo de reporte2 creado correctamente" : "Archivo de reporte2 no creado";
                Log.i("MENSAJE", "Carpeta LuzATuVida :  " + mensaje);
            }

            file = new Scanner(new File(file_path + "/luzatuvida/.nomedia/reporte.txt"));
            writer = new PrintWriter(file_path + "/luzatuvida/.nomedia/reporte2.txt");

            boolean bandera = true;

            while (file.hasNext()) {

                String line = file.nextLine();

                if(!line.contains("*")) {
                    //No es separador, son los datos

                    String[] datos = line.split(",");

                    try {

                        //Validacion para saber si es el archivo antiguo
                        String[] fechaRegistro = datos[0].split(" ");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        sdf.parse(fechaRegistro[0]); //Validacion principal para que truene y no entre la linea antigua

                        //Si llega aqui es por que son lineas actuales
                        if (diasDiferenciaEntreFechas(fechaActual, datos[0]) > -15) {
                            writer.write(line);
                            writer.write("\n");
                            bandera = true;
                        }else {
                            bandera = false;
                        }

                    }catch (Exception e) {
                        //Si llega aqui es por que son lineas antiguas
                        bandera = false;
                        Log.i("ERROR", e.toString());
                    }

                }else {
                    //Es separador
                    if(bandera) {
                        writer.write(line);
                        writer.write("\n");
                    }
                }
            }

            file.close();
            writer.close();

            File file1 = new File(file_path + "/luzatuvida/.nomedia/reporte.txt");
            File file2 = new File(file_path + "/luzatuvida/.nomedia/reporte2.txt");

            file1.delete();
            file2.renameTo(file1);

        } catch (Exception e) {
            escribirError(e, 62);
            Log.i("ERROR", e.toString());
        }

    }

    private int diasDiferenciaEntreFechas(String fechaActual, String fechaLineaTxt) {

        int diasDiferencia = 0;

        try {

            String[] fechaRegistro = fechaLineaTxt.split(" ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateRegistro = sdf.parse(fechaRegistro[0]);
            Date dateActual = sdf.parse(fechaActual);

            long diff = dateRegistro.getTime() - dateActual.getTime();
            diasDiferencia = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        }catch (Exception e) {
            escribirError(e, 63);
            Log.i("ERROR", e.toString());
        }

        return diasDiferencia;
    }

    public void eliminarArchivo(String ruta){
        try {
            File file = new File(ruta);

            if (file.exists()) {
                file.delete();
            }

        } catch (Exception e) {
            escribirError(e, 64);
            Log.i("MENSAJE",e.toString());
        }
    }

    public String replaceCadena(String cadena) {
        String cadenanueva = cadena.replace("'", "");
        cadenanueva = cadenanueva.replace("\"", "");
        cadenanueva = cadenanueva.replace("á", "a");
        cadenanueva = cadenanueva.replace("é", "e");
        cadenanueva = cadenanueva.replace("í", "i");
        cadenanueva = cadenanueva.replace("ó", "o");
        cadenanueva = cadenanueva.replace("ú", "u");
        cadenanueva = cadenanueva.replace("Á", "A");
        cadenanueva = cadenanueva.replace("É", "E");
        cadenanueva = cadenanueva.replace("Í", "I");
        cadenanueva = cadenanueva.replace("Ó", "O");
        cadenanueva = cadenanueva.replace("Ú", "U");
        cadenanueva = cadenanueva.replace("ü", "u");
        cadenanueva = cadenanueva.replace("Ü", "U");
        cadenanueva = cadenanueva.replace("Ã‘", "N");
        cadenanueva = cadenanueva.replace("ā‘", "n");
        return cadenanueva;
    }

    public int contadorContratosOHistorialesTablaGarantias(String idContrato, String idHistorial, boolean historial) {

        int contador = 0;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "";
            if(!historial) {
                SQL = "SELECT COUNT(ID) FROM GARANTIAS" +
                        " WHERE ID_CONTRATO='" + idContrato + "'";
            }else {
                SQL = "SELECT COUNT(ID) FROM GARANTIAS" +
                        " WHERE ID_CONTRATO='" + idContrato + "' AND ID_HISTORIAL = '" + idHistorial + "' AND ESTADOGARANTIA = '1'";
            }
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()) {
                contador = datos.getInt(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            escribirError(e, 65);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return contador;

    }

    public int contadorHistorialesConGarantia(String idContrato, boolean cobranza) {

        int contador = 0;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "";
            if(cobranza) {
                SQL = "SELECT COUNT(ID) FROM HISTORIALCLINICO" +
                        " WHERE ID_CONTRATO='" + idContrato + "' AND TIPO = '1'";
            }else {
                SQL = "SELECT COUNT(HC.ID) FROM HISTORIALCLINICO HC INNER JOIN GARANTIAS G ON G.ID_HISTORIALGARANTIA = HC.ID" +
                        " WHERE HC.ID_CONTRATO='" + idContrato + "' AND HC.TIPO = '1'";
            }
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()) {
                contador = datos.getInt(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            escribirError(e, 66);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return contador;

    }

    public boolean existeHistorialEnTablaGarantias(String idHistorialGarantia) {

        boolean respuesta = true;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();

            String SQL = "SELECT ID FROM GARANTIAS WHERE ID_HISTORIALGARANTIA = '" + idHistorialGarantia + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                respuesta = false;
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            escribirError(e, 67);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return respuesta;
    }

    public void eliminarTablaORegistroTabla(String tabla, String idAtributo, String id) {

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            if(idAtributo.length() > 0) {
                //Eliminar un registro de la tabla
                sqLiteDB2.execSQL("DELETE FROM '" + tabla + "' WHERE " + idAtributo + "='" + id + "'");
            }else {
                //Eliminar tabla completa
                sqLiteDB2.execSQL("DELETE FROM '" + tabla + "'");
            }
            sqLiteDB2.close();

        }catch (SQLiteException e){
            escribirError(e, 68);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

    }

    public String obtenerUnResultadoQuery(String query) {

        String resultado = "";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            Cursor datos = sqLiteDB2.rawQuery(query, null);

            if(datos.moveToFirst()) {
                resultado = datos.getString(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            escribirError(e, 69);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return resultado;

    }

    public String obtenerFormaPagoLetra(int pago) {
        String formaPago = "";

        switch (pago) {
            case 0:
                formaPago = "CONTADO";
                break;
            case 1:
                formaPago = "SEMANAL";
                break;
            case 2:
                formaPago = "QUINCENAL";
                break;
            case 4:
                formaPago = "MENSUAL";
                break;
        }

        return formaPago;
    }

    public String obtenerMetodoPagoLetra(int metodoPago) {

        String metodoPagoLetra = "";

        switch (metodoPago) {
            case 0:
                metodoPagoLetra = "EFECTIVO";
                break;
            case 1:
                metodoPagoLetra = "TARJETA";
                break;
            case 2:
                metodoPagoLetra = "TRANSFERENCIA";
                break;
            case 3:
                metodoPagoLetra = "CANCELACIÓN";
                break;
        }

        return metodoPagoLetra;
    }


    public String obtenerFechaSesion(){

        String fechaSesion = "";

        seguridad = new Seguridad(fragmento);
        obtenerRol = new ObtenerRol(fragmento);
        String idusuario = obtenerRol.obtenerAtributoUsuarioLogeado("ID");

        if(idusuario.length()>0){
            try {
                idusuario = seguridad.cifrar((idusuario).getBytes("UTF-16LE"));
            } catch (Exception e) {
                escribirError(e, 70);
                e.printStackTrace();
            }
            try {
                fechaSesion =seguridad.decifrar(Base64.decode(obtenerAtributoTabla("MOVIL","FECHAACTUAL","ID",idusuario).getBytes("UTF-16LE"), Base64.DEFAULT));
            } catch (Exception e) {
                escribirError(e, 71);
                e.printStackTrace();
            }
        }

        return fechaSesion;
    }

    public void compartirArchivoErrores() {

        //Si los permisos estan aceptados
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU || ((fragmento.getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (fragmento.getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))) {
            //Han sido aceptados

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            try {
                File archivoErrores = new File(file_path + "/luzatuvida/.nomedia/reporte.txt");

                if (!archivoErrores.exists()) {
                    Toast.makeText(fragmento.getActivity(), "El archivo aun no existe", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+archivoErrores));
                fragmento.startActivity(Intent.createChooser(intent, "Compartiendo archivo..."));

            } catch (Exception e) {
                escribirError(e, 72);
                Log.i("MENSAJE",e.toString());
            }

        }else {
            //Aun no los han aceptado
            ActivityCompat.requestPermissions(fragmento.getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PackageManager.PERMISSION_GRANTED);
        }

    }

    public String obtenerTamanoArchivo(String ruta){

        String textoFileSize = "";

        File file = new File(ruta);

        if (file.exists()) {
            //Existe el archivo

            //Obtenemos el tamano del archivo en bytes
            long fileSize = file.length();
            textoFileSize = fileSize + "B";
            if (fileSize > 1024) {
                //Convertimos los bytes a Kilobytes (1 KB = 1024 Bytes)
                fileSize = fileSize / 1024;
                textoFileSize = fileSize + "KB";
                if(fileSize > 1024) {
                    //Convertimos los KB a MegaBytes (1 MB = 1024 KBytes)
                    fileSize = fileSize / 1024;
                    textoFileSize = fileSize + "MB";
                    if(fileSize > 1024) {
                        //Convertimos los MegaBytes a GigaBytes (1 GB = 1024 MegaBytes)
                        fileSize = fileSize / 1024;
                        textoFileSize = fileSize + "GB";
                    }
                }
            }

        }

        return textoFileSize;
    }

    /*Metodo/Funcion: obtenerAtributoTablaDescifrado
      Descripcion: Obtener cualquier atributo de una tabla la cual este cifrada
    */
    public String obtenerAtributoTablaDescifrado(String tabla, String atributo, String idAtributo, String id) {

        String respuesta = "";

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();

            String consulta = "SELECT " + atributo + " FROM " + tabla + " WHERE " + idAtributo + " = '" + seguridad.cifrar(id.getBytes("UTF-16LE")) + "' LIMIT 1";

            Cursor datos = sqLiteDB2.rawQuery(consulta, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No se pudo encontrar el atributo");
            }

            if (datos.moveToFirst()) {
                respuesta = seguridad.decifrar(Base64.decode(datos.getString(0).getBytes("UTF-16LE"), Base64.DEFAULT));
            }

            sqLiteDB2.close();
            datos.close();

        }catch (Exception e) {
            escribirError(e, 252);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return respuesta;

    }

    public  void verificarConfiguracionLogoIconos(ImageView imagen, int tipo) {
        //Tipos
        //0 -> Logotipo
        //1 -> Iconos

        switch (tipo){
            case 0:
                Internet internet;
                ImageView img_Logo = imagen;
                ImageView img_Servidor = new ImageView(fragmento.getActivity());

                //Convertimos la imagen actual a BitMap
                BitmapDrawable img_Actual = (BitmapDrawable) img_Logo.getDrawable();
                Bitmap bitmap_imgActual = img_Actual.getBitmap();

                internet = new Internet(fragmento.getActivity());

                String urlLogotipoConfiguracion = obtenerAtributoTabla("CONFIGURACIONMOVIL","FOTOLOGO","ESTADOCONFIGURACION","1"); //Obtenemos la imagen de configuracion
                String ruta = llaves.url_ruta_principal +"/" + urlLogotipoConfiguracion;
                     if(urlLogotipoConfiguracion.length() > 0){
                    //si tenemos una url de imagen en la configuracion

                    if(internet.verificarConexionInternet()){
                        //Hay conexion a internet

                        //Descargamos la imagen del servidor
                        Picasso.get()
                                .load(ruta)
                                .error(R.drawable.logo)//Si se presenta un error se carga logo por default
                                .into(img_Servidor);

                        //Limpiar ImageView del logo
                        img_Logo.setImageDrawable(null);

                        //Generar metodo en segundo plano para comparar imagenes
                        Handler handler = new Handler();

                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (img_Servidor.getDrawable() != null && tiempoEspera <10) {
                                    //Una vez que se descargo la imagem

                                    //Convertimos imagen de configuracion a mapa de bits
                                    BitmapDrawable img_servidorDrawable = (BitmapDrawable) img_Servidor.getDrawable();
                                    Bitmap bitmap_imgServidor = img_servidorDrawable.getBitmap();

                                    if(bitmap_imgActual != bitmap_imgServidor){
                                        //Si las imagenes son distintas
                                        img_Logo.setImageBitmap(bitmap_imgServidor); //Asignamos a ImageView logo nueva foto
                                    }else {
                                        //Son iguales
                                        img_Logo.setImageBitmap(bitmap_imgActual); //Asignamos a ImageView logo actual
                                    }

                                    handler.removeCallbacks(runnable);

                                } if((img_Servidor.getDrawable() == null && tiempoEspera <10)) {
                                    //Si es no termina la descarga y tiempoEspera es menor a 100 milisegundos
                                    handler.postDelayed(runnable, 100); //Genera un retardo mas
                                    tiempoEspera++;
                                } if(tiempoEspera >= 10){
                                    //Si tiempoEspera son 100milisegundos o mas
                                    handler.removeCallbacks(runnable); // Detenemos la funcion en segundo plano
                                    img_Logo.setImageBitmap(bitmap_imgActual);  //Asignamos la foto actual para evitar esperar mas
                                }
                            }
                        };
                        runnable.run();
                    }else {
                        //Si no tienes conexion a internet
                        img_Logo.setImageResource(R.drawable.logo);  //Asignamos imagen por default
                    }
                }else {
                    //Imagen configuracion es vacia
                    img_Logo.setImageResource(R.drawable.logo); //Asignamos imagen por default
                }

                break;
            case 1:
                ImageView iconos = imagen;
                String colorConfiguracion = obtenerAtributoTabla("CONFIGURACIONMOVIL","COLORICONOS","ESTADOCONFIGURACION","1");

                if(colorConfiguracion.length() > 0){
                    //Si color de configuracion es diferente de vacio
                    iconos.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorConfiguracion))); // Pintar icono de nuevo color
                }

                break;
        }
    }

    public void verificarConfiguracionEncabezados(TextView encabezado){
        TextView tvEncabezado = encabezado;

        String color = obtenerAtributoTabla("CONFIGURACIONMOVIL","COLORENCABEZADOS","ESTADOCONFIGURACION","1").toLowerCase(); //Color en tabla de configuracion
        String colorEncabezados = "#" + Integer.toHexString(encabezado.getCurrentTextColor()).substring(2); //Obtenemos color actual de etiquetas

        if(color.length() > 0){
            //Si color de configuracion es diferente de vacio
            if(color != colorEncabezados){
                //Si colores son distintos
                tvEncabezado.setTextColor(Color.parseColor(color)); // Cambia color por el de la configuracion
            }
        }else{
            //Si color de configuracion es vacio -> tomar color por defaul para encabezados
            tvEncabezado.setTextColor(Color.parseColor(colorEncabezados));
        }

    }

    public void verificarConfiguracionNavBar(Toolbar toolbar){
        Toolbar navBar = toolbar;

        String colorConfiguracion = obtenerAtributoTabla("CONFIGURACIONMOVIL","COLORNAVBAR","ESTADOCONFIGURACION","1"); //Color almacenado en configuracion
        String colornavBar = "#" + Integer.toHexString(((ColorDrawable)navBar.getBackground()).getColor()).substring(2); //Color por default del componente

        if(colorConfiguracion.length() > 0 ){
            //Si el color de la configuracion es distinta a vacio
            if(colorConfiguracion != colornavBar){
                //Si color actual y almacenado en configuracion sn distintos
                navBar.setBackgroundColor(Color.parseColor(colorConfiguracion));// Pintar toolbar de nuevo color
                toolBarBackground = colorConfiguracion;
            }

        }else {
            //Si color configuracion es vacio -> tomar colore por defaul
            navBar.setBackgroundColor(Color.parseColor(colornavBar));
        }

    }

    public ArrayList<String> obtenerTopVentas(String id_rol) {
        ArrayList<String> respuesta = new ArrayList<>();
        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT NOMBRE, ROL, NUMEROVENTAS, SUCURSAL FROM VENTAS WHERE ROL = '" + id_rol +"' LIMIT 5";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() > 0) {
                while (datos.moveToNext()) {
                    respuesta.add((datos.getString(0) + "-" +  datos.getString(2) + "-" + datos.getString(3)));
                }
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            escribirError(e, 255); // id de error escrito 09-08-2022
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return (respuesta);

    }

    public JSONObject obtenerAbonosArchivoMovimientosTxt(int limiteRegistros) {

        JSONObject jsonAbonosArchivo = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        String fechaActualizacion = "2022-11-12";

        try{

            List<String> values = new ArrayList<String>();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //Validacion por android estudio (Version oreo en adelante)
                values = Files.readAllLines(Paths.get(file_path + "/luzatuvida/.nomedia/movimientos.txt"));
            }else {
                //Versiones anteriores a oreo

                Scanner file;
                file = new Scanner(new File(file_path + "/luzatuvida/.nomedia/movimientos.txt"));

                while (file.hasNext()) {

                    String line = file.nextLine();

                    if(!line.contains("*")) {
                        //No es separador, son los datos

                        String[] datos = line.split(",");

                        try {

                            //Validacion para saber si es el archivo antiguo
                            String[] fechaRegistro = datos[0].split(" ");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sdf.parse(fechaRegistro[0]); //Validacion principal para que truene y no entre la linea antigua
                            values.add(line);

                        } catch (Exception e) {
                            Log.i("ERROR", e.toString());
                            continue;
                        }

                    }
                }
                file.close();
            }

            if(values.size() > 0) {
                //Se tienen valores

                Collections.reverse(values);
                int contador = 0;

                for (int i = 0; i < values.size(); i++) {

                    if (!values.get(i).contains("*")) {
                        //No es separador, son los datos

                        String[] datos = values.get(i).split(",");

                        try {

                            //Validacion para saber si es el archivo antiguo
                            String[] fechaRegistro = datos[0].split(" ");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sdf.parse(fechaRegistro[0]); //Validacion principal para que truene y no entre la linea antigua

                            String coordenadas = "";
                            if (datos.length > 14 && datos[14].length() > 0 && datos[15].length() > 0) {
                                //Tiene coordenadas el abono
                                coordenadas = datos[14] + "," + datos[15];
                            }

                            //Si llega aqui es por que son lineas actuales
                            if (diasDiferenciaEntreFechas(fechaActualizacion, datos[0]) >= 0) {
                                //Es abono que fue agregado y fecha del abono es mayor a la fecha de actualizacion
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("TITULO", datos[1]);
                                jsonObject.put("ID_ABONO", datos[8]);
                                jsonObject.put("FOLIO", datos[7]);
                                jsonObject.put("ID_CONTRATO", datos[3]);
                                jsonObject.put("ABONO", datos[5]);
                                jsonObject.put("METODOPAGO", datos[10]);
                                jsonObject.put("ADELANTOS", datos[11]);
                                jsonObject.put("TIPOABONO", datos[12]);
                                jsonObject.put("ATRASO", datos[13]);
                                jsonObject.put("COORDENADAS", coordenadas);
                                jsonObject.put("CREATED_AT", datos[0]);
                                jsonArray.put(jsonObject);
                                contador++;
                                if (contador == limiteRegistros) {
                                    //Ya fueron los limiteRegistros registros
                                    break; //Detener for
                                }
                            }

                        } catch (Exception e) {
                            Log.i("ERROR", e.toString());
                            continue;
                        }

                    }

                }

            }

        } catch (IOException e) {
            Log.i("ERROR", e.toString());
            e.printStackTrace();
        }

        //Se encripta el JSON en base64
        try {
            if(jsonArray.toString().length() > 2) {
                jsonAbonosArchivo.put("abonosarchivo", new String(Base64.encode(jsonArray.toString().getBytes(), Base64.DEFAULT)));
            }
        } catch (JSONException e) {
            escribirError(e, 234);
            e.printStackTrace();
        }

        return jsonAbonosArchivo;

    }

    public void borrarSiExisteImagenEnDirectorio(String ruta) {

        if(ruta.length() > 0) {
            File foto = new File(file_path + "/luzatuvida/.nomedia/" + ruta);
            if(foto.exists()) {
                foto.delete();
            }
        }

    }

    public void escribirNuevoContratoOHistorialClinicoEnArchivoTXT(String fechaActual, String idContrato, String idHistorial, int opcion){

        //Opciones
        // 1 -> Escribir sobre archivo de contratos.txt
        // 2 -> Escribir sobre archivo de historialclinico.txt

        try {
            switch (opcion) {
                case 1:
                    File archivoContratos = new File(file_path + "/luzatuvida/.nomedia/contratos.txt");

                    //Validacion de archivos, si no existen los crea
                    if (!archivoContratos.exists()) {
                        String mensaje = archivoContratos.createNewFile() ? "Archivo de contratos creado correctamente" : "Archivo de movimientos no creado";
                        Log.i("MENSAJE", "Carpeta LuzATuVida :  " + mensaje);
                    }

                    //Consultar datos contrato
                    String datosContrato = "";

                    try {

                        SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
                        String SQL = "SELECT * FROM CONTRATOS WHERE ID_CONTRATO='" + idContrato + "'";
                        Cursor datos = sqLiteDB2.rawQuery(SQL, null);

                        if (datos.getCount() == 0) {
                            Log.i("MENSAJE", "No se encontro el atributo del contrato");
                        }

                        if (datos.moveToFirst()) {
                            //Obtener fecha de creacion para almacebar como primer dato en el registro del archivo txt de contratos
                            datosContrato = datos.getString(78) + ",";
                            //Obtener los datos del contrato
                            datosContrato += "ID_CONTRATO:" + datos.getString(0) + ",DATOS:" + datos.getString(1) + ",ID_USUARIOCREACION:" + datos.getString(2) +
                                    ",NOMBRE_USUARIOCREACION:" + datos.getString(3) + ",ID_ZONA:" + datos.getString(4) + ",NOMBRE:" + datos.getString(5) +
                                    ",CALLE:" + datos.getString(6) + ",NUMERO:" + datos.getString(7) + ",DEPTO: " + datos.getString(8) + ",ALLADODE:" + datos.getString(9) +
                                    ",FRENTEA:" + datos.getString(10) + ",ENTRECALLES:" + datos.getString(11) + ",COLONIA:" + datos.getString(12) +
                                    ",LOCALIDAD:" + datos.getString(13) + ",TELEFONO:" + datos.getString(14) + ",TELEFONOREFERENCIA:" + datos.getString(15) +
                                    ",NOMBREREFERENCIA:" + datos.getString(16) + ",CASATIPO:" + datos.getString(17) + ",CASACOLOR:" + datos.getString(18) +
                                    ",FOTOINEFRENTE:" + datos.getString(19) + ",FOTOINEATRAS:" + datos.getString(20) + ",FOTOCASA:" + datos.getString(21) +
                                    ",COMPROBANTEDOMICILIO:" + datos.getString(22) + ",ID_OPTOMETRISTA:" + datos.getString(23) + ",TARJETAFRENTE:" + datos.getString(24) +
                                    ",TARJETAATRAS:" + datos.getString(25) + ",PAGO:" + datos.getString(26) + ",ID_PROMOCION:" + datos.getString(27) +
                                    ",TOTAL:" + datos.getString(28) + ",IDCONTRATORELACION:" + datos.getString(29) + ",CONTADOR:" + datos.getString(30) +
                                    ",TOTALHISTORIAL:" + datos.getString(31) + ",TOTALPROMOCION:" + datos.getString(32) + ",TOTALPRODUCTO:" + datos.getString(33) +
                                    ",TOTALABONO:" + datos.getString(34) + ",ULTIMOABONO:" + datos.getString(35) + ",ESTATUS_ESTADOCONTRATO:" + datos.getString(36) +
                                    ",DIAPAGO:" + datos.getString(37) + ",FECHACOBROINI:" + datos.getString(38) + ",FECHACOBROFIN:" + datos.getString(39) +
                                    ",FECHAATRASO:" + datos.getString(40) + ",COSTOATRASO:" + datos.getString(41) + ",DIASELECCIONADO:" + datos.getString(42) +
                                    ",FECHAENTREGA:" + datos.getString(43) + ",PAGOSADELANTAR:" + datos.getString(44) + ",ENGANCHE:" + datos.getString(45) +
                                    ",ENTREGAPRODUCTO:" + datos.getString(46) + ",CORREO:" + datos.getString(47) + ",ESTATUS:" + datos.getString(48) +
                                    ",ENVIADOPAGINA:" + datos.getString(49) + ",PAGARE:" + datos.getString(50) + ",FOTOOTROS:" + datos.getString(51) +
                                    ",PROMOCIONTERMINADA:" + datos.getString(52) + ",SUBSCRIPCION:" + datos.getString(53) + ",FECHASUBSCRIPCION:" + datos.getString(54) +
                                    ",NOTA:" + datos.getString(55) + ",TOTALREAL:" + datos.getString(56) + ",DIATEMPORAL:" + datos.getString(57) +
                                    ",COORDENADAS:" + datos.getString(58) + ",CALLEENTREGA:" + datos.getString(59) + ",NUMEROENTREGA:" + datos.getString(60) +
                                    ",DEPTOENTREGA:" + datos.getString(61) + ",ALLADODEENTREGA:" + datos.getString(62) + ",FRENTEAENTREGA:" + datos.getString(63) +
                                    ",ENTRECALLESENTREGA:" + datos.getString(64) + ",COLONIAENTREGA:" + datos.getString(65) + ",LOCALIDADENTREGA:" + datos.getString(66) +
                                    ",CASATIPOENTREGA:" + datos.getString(67) + ",CASACOLORENTREGA:" + datos.getString(68) + ",ALIAS:" + datos.getString(69) +
                                    ",OPCIONLUGARENTREGA:" + datos.getString(71) +",OBSERVACIONFOTOINE:" + datos.getString(72) +",OBSERVACIONFOTOINEATRAS:" + datos.getString(73) +
                                    ",OBSERVACIONFOTOCASA:" + datos.getString(74) +",OBSERVACIONCOMPROBANTEDOMICILIO:" + datos.getString(75) +",OBSERVACIONPAGARE:" + datos.getString(76) +
                                    ",OBSERVACIONFOTOOTROS:" + datos.getString(77) + ",CREATED_AT:" + datos.getString(78) + ",UPDATED_AT:" + datos.getString(79);
                        }

                        sqLiteDB2.close();
                        datos.close();


                    } catch (SQLiteException e) {
                        escribirError(e, 267);
                        Log.i("ERRORBD", e.getMessage() + "");
                    }

                    //Escribir sobre archivo txt de contratos
                    BufferedWriter bufContratos = new BufferedWriter(new FileWriter(archivoContratos, true));
                    bufContratos.append(datosContrato + "\n******************************************************");
                    bufContratos.newLine();
                    bufContratos.close();

                    //Remover registros creados despues de 15 dias de archivo contratos.txt
                    removerLineasEnArchivoContratosHistorialesTxt(fechaActual, "contratos.txt", "contratos2.txt");
                    break;

                case 2:
                    File archivoHistoriales = new File(file_path + "/luzatuvida/.nomedia/historialesClinicos.txt");

                    if (archivoHistoriales.exists()) {
                        String mensaje = archivoHistoriales.createNewFile() ? "Archivo de historiales creado correctamente" : "Archivo de movimientos no creado";
                        Log.i("MENSAJE", "Carpeta LuzATuVida :  " + mensaje);
                    }

                    //Consultar datos historial clinico contrato
                    String datosHistorial = "";

                    try {

                        SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
                        String SQL = "SELECT * FROM HISTORIALCLINICO WHERE ID= '" + idHistorial + "' AND ID_CONTRATO='" + idContrato + "'";
                        Cursor datos = sqLiteDB2.rawQuery(SQL, null);

                        if (datos.getCount() == 0) {
                            Log.i("MENSAJE", "No se encontro el atributo del contrato");
                        }

                        if (datos.moveToFirst()) {
                            //Fecha de creacion de registro
                            datosHistorial = datos.getString(55) + ",";

                            //Datos de historial clinico
                            datosHistorial += datos.getString(0) + "," + datos.getString(1) + "," + datos.getString(2) + "," + datos.getString(3) + "," + datos.getString(4) + "," +
                                    datos.getString(5) + "," + datos.getString(6) + "," + datos.getString(7) + "," + datos.getString(8) + "," + datos.getString(9) + "," +
                                    datos.getString(10) + "," + datos.getString(11) + "," + datos.getString(12) + "," + datos.getString(13) + "," + datos.getString(14) + "," +
                                    datos.getString(15) + "," + datos.getString(16) + "," + datos.getString(17) + "," + datos.getString(18) + "," + datos.getString(19) + "," +
                                    datos.getString(20) + "," + datos.getString(21) + "," + datos.getString(22) + "," + datos.getString(23) + "," + datos.getString(24) + "," +
                                    datos.getString(25) + "," + datos.getString(26) + "," + datos.getString(27) + "," + datos.getString(28) + "," + datos.getString(29) + "," +
                                    datos.getString(30) + "," + datos.getString(31) + "," + datos.getString(32) + "," + datos.getString(33) + "," + datos.getString(34) + "," +
                                    datos.getString(35) + "," + datos.getString(36) + "," + datos.getString(37).replace(",", "") + "," + datos.getString(38).replace(",", "") + "," + datos.getString(39) + "," +
                                    datos.getString(40) + "," + datos.getString(41) + "," + datos.getString(42) + "," + datos.getString(43) + "," + datos.getString(44) + "," +
                                    datos.getString(45) + "," + datos.getString(46) + "," + datos.getString(47) + "," + datos.getString(48) + "," + datos.getString(49) + "," +
                                    datos.getString(50) + "," + datos.getString(51) + "," + datos.getString(52) + "," + datos.getString(53) + "," + datos.getString(54) + "," +
                                    datos.getString(55) + "," + datos.getString(56);
                        }

                    } catch (SQLiteException e) {
                        escribirError(e, 268);
                        Log.i("ERRORBD", e.getMessage() + "");
                    }

                    //Escribir sobre archivo txt de historiales
                    BufferedWriter bufHistorial = new BufferedWriter(new FileWriter(archivoHistoriales, true));
                    bufHistorial.append(datosHistorial + "\n******************************************************");
                    bufHistorial.newLine();
                    bufHistorial.close();

                    //Remover registros creados despues de 15 dias de archivo historialesClinicos.txt
                    removerLineasEnArchivoContratosHistorialesTxt(fechaActual, "historialesClinicos.txt", "historialesClinicos2.txt");
                    break;
            }

        } catch(Exception e){
            escribirError(e, 269);
            Log.i("MENSAJE", e.toString());
        }

    }

    public void removerLineasEnArchivoContratosHistorialesTxt(String fechaActual, String nombreArchivoOriginal, String nombreArchivoTemporal) {

        Scanner file;
        PrintWriter writer;

        try {

            File archivoContratoHistorial = new File(file_path + "/luzatuvida/.nomedia/"+nombreArchivoTemporal);

            if (!archivoContratoHistorial.exists()) {
                String mensaje = archivoContratoHistorial.createNewFile() ? "Archivo creado correctamente" : "Archivo no creado";
                Log.i("MENSAJE", "Carpeta LuzATuVida :  " + mensaje);
            }

            file = new Scanner(new File(file_path + "/luzatuvida/.nomedia/"+nombreArchivoOriginal));
            writer = new PrintWriter(file_path + "/luzatuvida/.nomedia/"+nombreArchivoTemporal);

            boolean bandera = true;

            while (file.hasNext()) {

                String line = file.nextLine();

                if(!line.contains("*")) {
                    //No es separador, son los datos

                    String[] datos = line.split(",");

                    try {

                        //Validacion para saber si es el archivo antiguo
                        String[] fechaRegistro = datos[0].split(" ");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        sdf.parse(fechaRegistro[0]); //Validacion principal para que truene y no entre la linea antigua

                        //Si llega aqui es por que son lineas actuales
                        if (diasDiferenciaEntreFechas(fechaActual, datos[0]) > -15) {
                            writer.write(line);
                            writer.write("\n");
                            bandera = true;
                        }else {
                            bandera = false;
                        }

                    }catch (Exception e) {
                        //Si llega aqui es por que son lineas antiguas
                        bandera = false;
                        Log.i("ERROR", e.toString());
                    }

                }else {
                    //Es separador
                    if(bandera) {
                        writer.write(line);
                        writer.write("\n");
                    }
                }
            }

            file.close();
            writer.close();

            File file1 = new File(file_path + "/luzatuvida/.nomedia/"+nombreArchivoOriginal);
            File file2 = new File(file_path + "/luzatuvida/.nomedia/"+nombreArchivoTemporal);

            file1.delete();
            file2.renameTo(file1);

        } catch (Exception e) {
            escribirError(e, 270);
            Log.i("ERROR", e.toString());
        }

    }

    public JSONObject obtenerRegistrosArchivoHistorialesClinicosTxt(int limiteRegistros) {

        JSONObject jsonHistorialesClinicosArchivo = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String fechaActualizacion = "2022-02-25";

        try{

            List<String> values = new ArrayList<String>();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //Validacion por android estudio (Version oreo en adelante)
                values = Files.readAllLines(Paths.get(file_path + "/luzatuvida/.nomedia/historialesClinicos.txt"));
            }else {
                //Versiones anteriores a oreo

                Scanner file;
                file = new Scanner(new File(file_path + "/luzatuvida/.nomedia/historialesClinicos.txt"));

                while (file.hasNext()) {

                    String line = file.nextLine();

                    if(!line.contains("*")) {
                        //No es separador, son los datos

                        String[] datos = line.split(",");

                        try {

                            //Validacion para saber si es el archivo antiguo
                            String[] fechaRegistro = datos[0].split(" ");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sdf.parse(fechaRegistro[0]); //Validacion principal para que truene y no entre la linea antigua

                            //Si llega aqui es por que son lineas actuales
                            values.add(line);

                        } catch (Exception e) {
                            Log.i("ERROR", e.toString());
                            continue;
                        }

                    }
                }
                file.close();
            }

            if(values.size() > 0) {
                //Se tienen valores

                Collections.reverse(values);
                int contador = 0;

                for (int i = 0; i < values.size(); i++) {

                    if (!values.get(i).contains("*")) {
                        //No es separador, son los datos

                        String[] datos = values.get(i).split(",");

                        try {

                            //Validacion para saber si es el archivo antiguo
                            String[] fechaRegistro = datos[0].split(" ");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sdf.parse(fechaRegistro[0]); //Validacion principal para que truene y no entre la linea antigua

                            //Si llega aqui es por que son lineas actuales
                            if (diasDiferenciaEntreFechas(fechaActualizacion, datos[0]) >= 0) {
                                //Es abono que fue agregado y fecha del abono es mayor a la fecha de actualizacion
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("ID", datos[1]);
                                jsonObject.put("ID_CONTRATO", datos[2]);
                                jsonObject.put("EDAD", datos[3]);
                                jsonObject.put("FECHAENTREGA", datos[4]);
                                jsonObject.put("DIAGNOSTICO", datos[5]);
                                jsonObject.put("OCUPACION", datos[6]);
                                jsonObject.put("DIABETES", datos[7]);
                                jsonObject.put("HIPERTENSION", datos[8]);
                                jsonObject.put("DOLOR", datos[9]);
                                jsonObject.put("ARDOR", datos[10]);
                                jsonObject.put("GOLPEOJOS", datos[11]);
                                jsonObject.put("OTROM", datos[12]);
                                jsonObject.put("MOLESTIAOTRO", datos[13]);
                                jsonObject.put("ULTIMOEXAMEN", datos[14]);
                                jsonObject.put("ESFERICODER", datos[15]);
                                jsonObject.put("CILINDRODER", datos[16]);
                                jsonObject.put("EJEDER", datos[17]);
                                jsonObject.put("ADDDER", datos[18]);
                                jsonObject.put("ALTDER", datos[19]);
                                jsonObject.put("ESFERICOIZQ", datos[20]);
                                jsonObject.put("CILINDROIZQ", datos[21]);
                                jsonObject.put("EJEIZQ", datos[22]);
                                jsonObject.put("ADDIZQ", datos[23]);
                                jsonObject.put("ALTIZQ", datos[24]);
                                jsonObject.put("ID_PRODUCTO", datos[25]);
                                jsonObject.put("ID_PAQUETE", datos[26]);
                                jsonObject.put("MATERIAL", datos[27]);
                                jsonObject.put("MATERIALOTRO", datos[28]);
                                jsonObject.put("COSTOMATERIAL", datos[29]);
                                jsonObject.put("BIFOCAL", datos[30]);
                                jsonObject.put("FOTOCROMATICO", datos[31]);
                                jsonObject.put("AR", datos[32]);
                                jsonObject.put("TINTE", datos[33]);
                                jsonObject.put("BLUERAY", datos[34]);
                                jsonObject.put("OTROT", datos[35]);
                                jsonObject.put("TRATAMIENTOOTRO", datos[36]);
                                jsonObject.put("COSTOTRATAMIENTO", datos[37]);
                                jsonObject.put("OBSERVACIONES", datos[38]);
                                jsonObject.put("OBSERVACIONESINTERNO", datos[39]);
                                jsonObject.put("TIPO", datos[40]);
                                jsonObject.put("BIFOCALOTRO", datos[41]);
                                jsonObject.put("COSTOBIFOCAL", datos[42]);
                                jsonObject.put("EMBARAZADA", datos[43]);
                                jsonObject.put("DURMIOSEISOCHOHORAS", datos[44]);
                                jsonObject.put("ACTIVIDADDIA", datos[45]);
                                jsonObject.put("PROBLEMASOJOS", datos[46]);
                                jsonObject.put("POLICARBONATOTIPO", datos[47]);
                                jsonObject.put("ID_TRATAMIENTOCOLORTINTE", datos[48]);
                                jsonObject.put("ESTILOTINTE", datos[49]);
                                jsonObject.put("POLARIZADO", datos[50]);
                                jsonObject.put("ID_TRATAMIENTOCOLORPOLARIZADO", datos[51]);
                                jsonObject.put("ESPEJO", datos[52]);
                                jsonObject.put("ID_TRATAMIENTOCOLORESPEJO", datos[53]);
                                jsonObject.put("FOTOARMAZON", datos[54]);
                                jsonObject.put("ENVIADOPAGINA", datos[55]);
                                jsonObject.put("CREATED_AT", datos[56]);
                                jsonObject.put("UPDATED_AT", datos[57]);
                                jsonArray.put(jsonObject);
                                contador++;
                                if (contador == limiteRegistros) {
                                    //Ya fueron los limiteRegistros registros
                                    break; //Detener for
                                }

                            }

                        } catch (Exception e) {
                            Log.i("ERROR", e.toString());
                            continue;
                        }

                    }
                }

            }

        } catch (IOException e) {
            Log.i("ERROR", e.toString());
            e.printStackTrace();
        }

        //Se encripta el JSON en base64
        try {
            if(jsonArray.toString().length() > 2) {
                jsonHistorialesClinicosArchivo.put("historialesclinicosarchivo", new String(Base64.encode(jsonArray.toString().getBytes(), Base64.DEFAULT)));
            }
        } catch (JSONException e) {
            escribirError(e, 271);
            e.printStackTrace();
        }

        return jsonHistorialesClinicosArchivo;

    }

    /*Metodo/Funcion: limpiarAcentosCaracteresEspecialesCadena
      Descripcion: Se definio la lógica para quitar caracteres especiales de una cadena. Sabemos que el valor ASCII de los alfabetos de letras mayúsculas comienza de 65 a 90 (A-Z)
                   y el valor ASCII del alfabeto de letras pequeñas comienza de 97 a 122 (a-z) y la 209 y 241 (ñ-Ñ) y la 32 es el espacio ( ).
                   Cada carácter se compara con su valor ASCII correspondiente. Si ambas condiciones especificadas devuelven true, return true else return false.
                   El bucle for se ejecuta hasta la longitud de la cadena. Cuando la cadena alcanza su tamaño, termina la ejecución y obtenemos la cadena resultante.
    */
    public String limpiarCadenaCaracteresEspeciales(String cadena) {

        String cadenaLimpia = "";

        //Ejecución de bucle hasta la longitud de la cadena
        for (int i = 0; i < cadena.length(); i++)
        {
            //Comparar alfabetos con su valor ASCII correspondiente
            if ((cadena.charAt(i) >= 48 && cadena.charAt(i) <= 57)
                    ||(cadena.charAt(i) >= 65 && cadena.charAt(i) <= 90)
                    || (cadena.charAt(i) >= 97 && cadena.charAt(i) <= 122)
                    || cadena.charAt(i) == 209 || cadena.charAt(i) == 241 || cadena.charAt(i) == 32) //Devuelve verdadera si ambas condiciones devuelven verdadera
            {
                //Agregando caracteres en una cadena vacía
                cadenaLimpia= cadenaLimpia + cadena.charAt(i);
            }
        }

        return cadenaLimpia;

    }

    //Metodo: eliminarImagenCompartir
    //Descripcion: Elimina la imagen existente del corte o ticket abono que se comparte por whatsapp
    public void eliminarImagenCompartir(String nombreArchivo){

        try {
            File ruta = Environment.getExternalStorageDirectory();
            File dir = new File(ruta + "/Pictures/");
            File archivo = new File(ruta + "/Pictures/"+nombreArchivo+".jpg");
            if (dir.isDirectory()){
                //Si es un directorio
                if (archivo.exists()) {
                    //Si existe el archivo
                    archivo.delete();
                    Log.i("Imagen: ","Imagen eliminada correctamente");
                }else{
                    Log.i("Imagen: ", "No existe el archivo: "+nombreArchivo);
                }
            } else {
                Log.i("Imagen:","Directorio no encontrado");
            }
        } catch (Exception e){
            escribirError(e, 258); // Escrito 21-08-2022
            Log.i("Imagen","Error al eliminar archivo: \n"+e.getMessage());
        }
    }

    public void mostrarAlertDialogIngresarReferenciaCitaPrevia(String idContrato){

        final EditText etReferenciaCita = new EditText(fragmento.getActivity());
        etReferenciaCita.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        etReferenciaCita.setHint("# Referencia cita");
        etReferenciaCita.setHintTextColor(Color.parseColor("#756464"));
        LinearLayout.LayoutParams layoutReferencia = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etReferenciaCita.setLayoutParams(layoutReferencia);

        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Cita previa").setMessage((Html.fromHtml("¿El paciente agendo una cita para su examen?<br><br>" +
                        "<font color='#FFACA6'><b>En caso de haber agendado la cita ingresa el numero de referencia que se asigno en su acuse de cita.</b></font>")))
                .setPositiveButton("Registrar cita", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Debe ir vacio por que se esta obteniendo abajo
                    }
                }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setView(etReferenciaCita);

        final android.app.AlertDialog dialog = alerta.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etReferenciaCita.getText().toString().equals("")){
                    //Ingreso texto en etReferenciaCita
                    actualizarAtributoTabla("CONTRATOS","REFERENCIA",etReferenciaCita.getText().toString(),"ID_CONTRATO",idContrato);
                }else{
                    //etReferenciaCita vacio
                    Toast.makeText(fragmento.getContext(), "Ingresa # de referencia para notificar cita.", Toast.LENGTH_LONG).show();
                    mostrarAlertDialogIngresarReferenciaCitaPrevia(idContrato);
                }
                dialog.cancel();
            }
        });

    }

    public void eliminarRegistroTablaImagenesCargadasSupervision(int opcion) {
        //Opciones
        // 0 -> eliminar registro de imagenes pendientes por subir que haya sido completado
        // 1 -> limpiar tabla por completo

        try{
            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            String SQL = "";
            switch (opcion){
                case 0:
                    //Eliminar registros que coincidan
                    SQL = "DELETE FROM IMAGENESCARGADASSUPERVISION WHERE KILOMETRAJE1 = '2'" +
                            " AND KILOMETRAJE2 = '2' AND LADOIZQUIERDO = '2' AND LADODERECHO = '2'" +
                            " AND FRENTE = '2' AND ATRAS = '2' AND EXTRA1 = '2' AND EXTRA2 = '2'" +
                            " AND EXTRA3 = '2' AND EXTRA4 = '2' AND EXTRA5 = '2' AND EXTRA6 = '2'";
                    break;
                case 1:
                    //Eliminar todos los registros
                    SQL = "DELETE FROM IMAGENESCARGADASSUPERVISION";
                    break;
            }
            sqLiteDB2.execSQL(SQL);
            sqLiteDB2.close();

        }catch (SQLiteException e){
            escribirError(e, 297);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

    }

    public void eliminarRegistroTablaImagenesPendientesMovil() {

        try{
            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            String SQL = "DELETE FROM IMAGENESPENDIENTESMOVIL WHERE BANDERA = '2'";
            sqLiteDB2.execSQL(SQL);
            sqLiteDB2.close();

        }catch (SQLiteException e){
            escribirError(e, 297);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

    }

    public String obtenerColorHexadecimalEstatus(String rol, int estadoContrato, int diasAtrasados) {

        String colorHexadecimal = "#EEEEEE"; //Color gris del encabezado por default

        switch (estadoContrato) {
            case 0:
                //NO TERMINADO
                colorHexadecimal = "#6c757d";
                break;
            case 1:
                //TERMINADO
                if(rol.equals("4")) {
                    //Rol cobranza
                    colorHexadecimal = "#F48FB1";
                }else {
                    //Rol opto garantia
                    colorHexadecimal = "#5bc0de";
                }
                break;
            case 2:
                //ENTREGADO
                if(rol.equals("4")) {
                    //Rol cobranza
                    colorHexadecimal = "#0275d8";
                }else {
                    //Rol opto garantia
                    colorHexadecimal = "#F48FB1";
                }
                break;
            case 3:
                //PRE-CANCELADO
                colorHexadecimal = "#d9534f";
                break;
            case 4:
                //ABONO ATRASADO
                if(rol.equals("4")) {
                    //Rol cobranza
                    if (diasAtrasados <= 10) {
                        //1-10 dias
                        colorHexadecimal = "#fff2cc";
                    } else {
                        if (diasAtrasados <= 20) {
                            //11-20 dias
                            colorHexadecimal = "#fce5cd";
                        } else {
                            if (diasAtrasados > 20) {
                                //20 dias en delante
                                colorHexadecimal = "#f4cccc";
                            }
                        }
                    }
                }else {
                    //Rol opto garantia
                    colorHexadecimal = "#F48FB1";
                }
                break;
            case 5:
                //PAGADO
                if(rol.equals("4")) {
                    //Rol cobranza
                    colorHexadecimal = "#5cb85c";
                }else {
                    //Rol opto garantia
                    colorHexadecimal = "#F48FB1";
                }
                break;
            case 7:
            case 9:
            case 10:
            case 11: //APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                if(rol.equals("4")) {
                    //Rol cobranza
                    colorHexadecimal = "#F48FB1";
                }
                break;
            case 12:
                //ENVIADO
                if(rol.equals("4")) {
                    //Rol cobranza
                    colorHexadecimal = "#5bc0de";
                }else {
                    //Rol opto garantia
                    colorHexadecimal = "#F48FB1";
                }
                break;
            case 13:
                //POR CREAR
                colorHexadecimal = "#b900ff";
                break;
            case 6:
                //CENCELADO
                colorHexadecimal = "#ff0000";
                break;
        }

        return colorHexadecimal;
    }

    public int obtenerDiasAtrasados(String fechaatrasoBD) {

        int workDays = 0;
        obtenerRol = new ObtenerRol(fragmento);

        if(fechaatrasoBD.length() > 0) {
            //fechaatraso diferente de vacio

            Date fechaactual = new Date();
            Date fechaatraso = new Date();

            try {
                //Cambiar formato fechaatraso proviniente de la BD a yyyy-MM-dd
                SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                fechaactual = convetDateFormat.parse(obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL"));

                fechaatraso = convetDateFormat.parse(fechaatrasoBD);

            } catch (ParseException e) {
                escribirError(e, 323);
                Log.i("MENSAJE", "ERROR: " + e.toString());
            }


            Calendar startCal = Calendar.getInstance();
            startCal.setTime(fechaatraso);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(fechaactual);

            if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
                //fechaatraso es igual a la fechaactual
                return 0;
            }

            if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
                startCal.setTime(fechaactual);
                endCal.setTime(fechaatraso);
            }

            do {
                //Conteo de dias quitando los Domingos de cada semana
                startCal.add(Calendar.DAY_OF_MONTH, 1);
                if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    ++workDays;
                }

            } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

        }

        return workDays;
    }

    public void agregarAUltimoContratoVisto(String idContrato) {

        String idUsuario = "";
        String idUltimoContrato = idContrato;
        obtenerRol = new ObtenerRol(fragmento);
        seguridad = new Seguridad(fragmento);

        try {
            idUsuario = seguridad.cifrar((obtenerRol.obtenerAtributoUsuarioLogeado("ID")).getBytes("UTF-16LE"));
            idUltimoContrato = seguridad.cifrar((idUltimoContrato).getBytes("UTF-16LE"));
        } catch (Exception e) {
            escribirError(e, 326);
            e.printStackTrace();
        }

        actualizarAtributoTabla("MOVIL","ULTIMOCONTRATO",idUltimoContrato,"ID",idUsuario);

    }

    public void llamadaSincronizacion() {
        obtenerRol = new ObtenerRol(fragmento);
        sincronizacion = new Sincronizacion(fragmento,false);
        Object[] objetosWebService = {obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN"), "", ""};
        sincronizacion.sincronizarMetodo(0, objetosWebService, 0);
    }

    public boolean trazarRutaContrato(String idContratoAtrazar){

        boolean respuesta = false;
        obtenerRol = new ObtenerRol(fragmento);
        localizacion = new Localizacion(fragmento);
        internet = new Internet(fragmento.getActivity());
        if(obtenerRol.obtenerAtributoUsuarioLogeado("ROL").equals("4")){
            //Tienes rol de cobrador

            if(localizacion.tienePermisos()) {
                //Permisos para localizacion

                String idContratoSeleccionado = idContratoAtrazar;

                if (internet.verificarConexionInternet()){
                    //Si tienes conexion a internet

                    String coordenadasContrato = obtenerAtributoContrato(idContratoSeleccionado, "COORDENADAS");

                    if(coordenadasContrato.length() > 0) {
                        //Tiene coordenadas el contrato

                        //Actualizar ubicacion actual
                        if (localizacion.actualizarUbicacion(localizacion)) {
                            //Gps esta encendio y tiene permisos

                            //Generar metodo en segundo plano para obtener coordenadas
                            Handler handler = new Handler();
                            runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (localizacion.getLatitud() != null) {
                                        //si latitud es diferente a vacio mandamos llamar el metodo trazarRutaUnicoDestino y enviamos los datos
                                        localizacion.trazarRutaDestinos(localizacion.getLatitud() + "," + localizacion.getLongitud(), "/" + coordenadasContrato);
                                        handler.removeCallbacks(runnable);
                                    } else {
                                        handler.postDelayed(runnable, 2000);
                                    }
                                }
                            };
                            runnable.run();
                            respuesta = true;

                        }
                    }else {
                        //No tiene coordenadas el contrato
                        ClipboardManager clipboard2 = (ClipboardManager) fragmento.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip2 = ClipData.newPlainText(idContratoSeleccionado, idContratoSeleccionado);
                        clipboard2.setPrimaryClip(clip2);
                        Toast.makeText(fragmento.getActivity(), "El contrato no tiene ubicación", Toast.LENGTH_LONG).show();
                    }

                }else{
                    //No tiene internet
                    Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
                }

            }

        }

        return respuesta;

    }

    public long obtenerMinutosPasadosDesdeHoraDispositivoMovil(String horaAComparar) {

        long diferenciaMinutos = 0;

        try {

            // Obtener la hora actual del dispositivo
            Calendar horaActual = Calendar.getInstance();

            String[] horaACompararArreglo = horaAComparar.split(":");
            // Definir la hora específica con la que quieres comparar
            int horaEspecifica = Integer.parseInt(horaACompararArreglo[0]); // Hora
            int minutoEspecifico = Integer.parseInt(horaACompararArreglo[1]); // Minuto
            int segundoEspecifico = Integer.parseInt(horaACompararArreglo[2]); // Segundo

            // Crear un objeto Calendar para la hora específica
            Calendar horaEspecificaCalendar = Calendar.getInstance();
            horaEspecificaCalendar.set(Calendar.HOUR_OF_DAY, horaEspecifica);
            horaEspecificaCalendar.set(Calendar.MINUTE, minutoEspecifico);
            horaEspecificaCalendar.set(Calendar.SECOND, segundoEspecifico);

            // Calcular la diferencia de tiempo en milisegundos
            long diferenciaTiempoMillis = horaActual.getTimeInMillis() - horaEspecificaCalendar.getTimeInMillis();

            // Convertir la diferencia de tiempo de milisegundos a minutos
            diferenciaMinutos = diferenciaTiempoMillis / (60 * 1000);

        } catch (Exception e) {
            escribirError(e, 327);
            e.printStackTrace();
        }

        return diferenciaMinutos;
    }

    public void mostrarAlertDialogObservacionImagenesContrato(EditText etObservacionImagen, String mensajeAlerta){

        final EditText etObservaciones = new EditText(fragmento.getActivity());
        etObservaciones.setHint("OBSERVACIONES");
        etObservaciones.setHeight(200);
        LinearLayout.LayoutParams layoutReferencia = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etObservaciones.setLayoutParams(layoutReferencia);

        //Obtenemos texto actual del edit text de la vista y lo asignamos al nuevo edit texto del alert
        etObservaciones.setText(etObservacionImagen.getText().toString());

        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Observacion imagen " + mensajeAlerta).setMessage((Html.fromHtml("Ingresa las observacion")))
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etObservacionImagen.setText(etObservaciones.getText().toString());
                    }
                }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setView(etObservaciones);

        final android.app.AlertDialog dialog = alerta.create();
        dialog.show();
    }

}
