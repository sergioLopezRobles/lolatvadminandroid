package com.luzatuvida.lolatvadminandroid.global;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistorialMovimientosContrato {

    /*
    Identificador error del 73
     */

    Fragment fragmento;
    baseDeDatos conexion;
    SQLiteDatabase sqLiteDB;
    ObtenerRol obtenerRol;
    GenerarIdAlfanumerico generarIdAlfanumerico;
    Llaves llaves;
    Global global;

    public HistorialMovimientosContrato(Fragment fragmento) {
        global = new Global(fragmento);
        this.fragmento = fragmento;
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
        obtenerRol = new ObtenerRol(fragmento);
        generarIdAlfanumerico = new GenerarIdAlfanumerico(fragmento);
    }

    /*Metodo/Funcion: guardarHistorialMovimientosContrato
      Descripcion: Guardar historial de movimientos que se realizan en el contrato
    */
    public void guardarHistorialMovimientosContrato(String idContrato, String mensajeCambios, String tipomensaje) {

        String time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        //Obtener un idAlfanumerico para la tabla HISTORIALCONTRATO
        String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("HISTORIALCONTRATO", 5);

        //Obtener fechaactual y idusuario
        String fechaActual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");
        String idUsuarioC = obtenerRol.obtenerAtributoUsuarioLogeado("ID");

        try {

            sqLiteDB = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID", idAlfanumerico);
            valores.put("ID_CONTRATO", idContrato);
            valores.put("ID_USUARIOC", idUsuarioC);
            valores.put("CAMBIOS", "M - " + mensajeCambios);
            valores.put("TIPOMENSAJE", tipomensaje);
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT", fechaActual + time);
            valores.put("UPDATED_AT", fechaActual + time);
            sqLiteDB.insert("HISTORIALCONTRATO",null, valores);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 73);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    public void eliminarMovimientoHistorialContrato(String idContrato, String atributo, String idAtributo) {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            sqLiteDB2.execSQL("DELETE FROM HISTORIALCONTRATO WHERE ID_CONTRATO = '" + idContrato + "' AND " + atributo + " = '" + idAtributo + "'");
            sqLiteDB2.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 276);
            Log.i("ERRORBD", "ERROR: ERROR AL ELIMINAR REGISTRO: " + e.getMessage());
        }
    }

    public void guardarHistorialMovimientosFotoContrato(String idContrato, String rutaFoto, String observaciones, String tipomensaje) {

        String time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        //Obtener un idAlfanumerico para la tabla HISTORIALCONTRATO
        String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("HISTORIALFOTOSCONTRATO", 5);

        //Obtener fechaactual y idusuario
        String fechaActual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");
        String idUsuarioC = obtenerRol.obtenerAtributoUsuarioLogeado("ID");

        //Observaciones son diferentes de vacio?
        if(observaciones.length() > 0){
            //Concatenar "M" para indicar que es un movimiento desde movil
            observaciones = "M - " + observaciones;
        }

        try {

            sqLiteDB = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID", idAlfanumerico);
            valores.put("ID_CONTRATO", idContrato);
            valores.put("ID_USUARIOC", idUsuarioC);
            valores.put("FOTO", rutaFoto);
            valores.put("OBSERVACIONES", observaciones );
            valores.put("TIPOMENSAJE", tipomensaje);
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT", fechaActual + time);
            valores.put("UPDATED_AT", fechaActual + time);
            sqLiteDB.insert("HISTORIALFOTOSCONTRATO",null, valores);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 277);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }
}
