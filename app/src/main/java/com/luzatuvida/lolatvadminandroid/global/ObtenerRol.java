package com.luzatuvida.lolatvadminandroid.global;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Base64;
import android.util.Log;

import androidx.fragment.app.Fragment;

public class ObtenerRol {

    /*
    Identificador error del 76
     */

    Fragment fragmento;
    baseDeDatos conexion;
    SQLiteDatabase sqLiteDB;
    Seguridad seguridad;
    Llaves llaves;
    Global global;

    public ObtenerRol(Fragment fragmento) {
        global = new Global(fragmento);
        this.fragmento = fragmento;
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
        seguridad = new Seguridad(fragmento);
    }

    /*Metodo/Funcion: obtenerAtributoUsuarioLogeado
      Descripcion: Obtener cualquier atributo del usuario logeado
    */
    public String obtenerAtributoUsuarioLogeado(String atributo) {

        String respuesta = "";

        try {

            sqLiteDB = conexion.getReadableDatabase();

            String consulta = "SELECT " + atributo + " FROM MOVIL DESC LIMIT 1";
            Cursor datos = sqLiteDB.rawQuery(consulta, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No se pudo encontrar el atributo");
            }

            if (datos.moveToFirst()) {

                respuesta = seguridad.decifrar(Base64.decode(datos.getString(0).getBytes("UTF-16LE"), Base64.DEFAULT));

            }

            sqLiteDB.close();
            datos.close();

        }catch (Exception e) {
            global.escribirError(e, 76);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return respuesta;

    }
}
