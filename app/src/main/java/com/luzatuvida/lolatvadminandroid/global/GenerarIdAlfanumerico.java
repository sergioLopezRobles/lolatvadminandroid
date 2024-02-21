package com.luzatuvida.lolatvadminandroid.global;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.security.SecureRandom;

public class GenerarIdAlfanumerico {

    /*
    Identificador error del 51 al 52
     */

    Fragment fragmento;
    baseDeDatos conexion;
    SQLiteDatabase sqLiteDB;
    Llaves llaves;
    Global global;

    public GenerarIdAlfanumerico(Fragment fragmento) {
        global = new Global(fragmento);
        this.fragmento = fragmento;
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
    }

    /*Metodo/Funcion: validarSiExisteYObtenerIdAlfanumerico
      Descripcion: Obtener IdAlfanumerico y verificar que este no exista en la tabla (en caso de existir, se generara otro IdAlfanumerico)
    */
    public String validarSiExisteYObtenerIdAlfanumerico(String tabla, int length) {

        boolean disponible = false;
        String idAlfanumerico = "";

        try{

            sqLiteDB = conexion.getReadableDatabase();

            do {

                //Obtencion de IdAlfanumerico
                idAlfanumerico = generarIdAlfanumerico(length);

                //Verificar que este no exista en la tabla (en caso de existir, se generara otro IdAlfanumerico)
                String SQL = "SELECT ID FROM " + tabla + " WHERE ID = '" + idAlfanumerico + "'";
                Cursor datos = sqLiteDB.rawQuery(SQL, null);

                if(datos.getCount() == 0){
                    disponible = true;
                }

                datos.close();

            } while (!disponible);

            sqLiteDB.close();

        }catch (SQLiteException e){
            global.escribirError(e, 51);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return idAlfanumerico;
    }

    /*Metodo/Funcion: generarIdAlfanumerico
      Descripcion: Creacion de IdAlfaumerico
    */
    private String generarIdAlfanumerico(int length) {

        String CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String NUMBER = "0123456789";

        String DATA_FOR_RANDOM_STRING = CHAR + NUMBER;
        SecureRandom random = new SecureRandom();

        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);
        }

        return sb.toString();
    }

    /*Metodo/Funcion: validarSiExisteFolioAlfanumericoEnAbonosContrato
      Descripcion: Obtener IdAlfanumerico y verificar que este no exista el folio en la tabla de abonos (en caso de existir, se generara otro IdAlfanumerico)
    */
    public String validarSiExisteFolioAlfanumericoEnAbonosContrato(String idContrato) {

        boolean disponible = false;
        String idAlfanumerico = "";

        try{

            sqLiteDB = conexion.getReadableDatabase();

            do {

                //Obtencion de IdAlfanumerico
                idAlfanumerico = generarIdAlfanumerico(5);

                //Verificar que este no exista el folio en la tabla de abonos (en caso de existir, se generara otro IdAlfanumerico)
                String SQL = "SELECT FOLIO FROM ABONOS WHERE FOLIO = '" + idAlfanumerico + "' AND ID_CONTRATO = '" + idContrato + "'";
                Cursor datos = sqLiteDB.rawQuery(SQL, null);

                if(datos.getCount() == 0){
                    disponible = true;
                }

                datos.close();

            } while (!disponible);

            sqLiteDB.close();

        }catch (SQLiteException e){
            global.escribirError(e, 52);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return idAlfanumerico;
    }

}
