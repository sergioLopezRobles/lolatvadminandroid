package com.luzatuvida.lolatvadminandroid.buzon;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Seguridad;
import com.luzatuvida.lolatvadminandroid.global.Sincronizacion;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;
import com.luzatuvida.lolatvadminandroid.impresoraBluetooth.ImpresoraBluetooth;
import com.luzatuvida.lolatvadminandroid.vista.principal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Controlador {

     /*

     Identificador error 260

    */

    DrawerLayout drawerLayout;
    ObtenerRol obtenerRol;
    Llaves llaves;
    baseDeDatos conexion;
    Global global;
    Fragment fragmento;
    EditText etNuevaQuejaSugerencia;
    TextView tvCaracteresDisponibles;
    Button btnGuardarNuevaQuejaSugerencia;
    MainActivity principal;

    public Controlador(Fragment fragmento, Object[] objetos) {
        this.fragmento = fragmento;

        obtenerRol = new ObtenerRol(fragmento);
        etNuevaQuejaSugerencia = (EditText)objetos[0];
        tvCaracteresDisponibles = (TextView) objetos[1];
        btnGuardarNuevaQuejaSugerencia = (Button) objetos[2];
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getContext(), "basedatos", null, llaves.versiondb);

    }

    public void mostrarCaracteresDisponibles() {
        int caracteres = etNuevaQuejaSugerencia.getText().toString().length();  //obtenemos tamaño actual de cadena
        if(caracteres == 0){
            //Si esta en blanco el mensaje - texto del boton aceptar cera opaco
            btnGuardarNuevaQuejaSugerencia.setTextColor(Color.parseColor("#5C5C5C"));
        }if(caracteres > 0) {
            //Si hay al menos un caracter en el mensaje - texto del boton sera color blanco
            btnGuardarNuevaQuejaSugerencia.setTextColor(Color.parseColor("#ffffff"));
        }
        tvCaracteresDisponibles.setText("("+caracteres+"/1000)");   //Se asigna el total de caracteres escritos a la etiqueta

    }

    public boolean guardarQuejaSugerenciaBD(){
        String mensaje = etNuevaQuejaSugerencia.getText().toString();
        String time = new SimpleDateFormat(" HH:mm:ss").format(new Date());
        String fechaactual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");
        String idUsuario = obtenerRol.obtenerAtributoUsuarioLogeado("ID");
        String id_franquicia = obtenerRol.obtenerAtributoUsuarioLogeado("ID_FRANQUICIA");

        if(mensaje.length() > 0){
            //Si existen caracteres en el campo de mensaje
            try {

                //Registramos el nuevo registro en BD
                SQLiteDatabase sqLiteDB = conexion.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put("ID_USUARIO", idUsuario);
                valores.put("ID_FRANQUICIA", id_franquicia);
                valores.put("MENSAJE", mensaje);
                valores.put("ENVIADOPAGINA", "0");
                valores.put("CREATED_AT", fechaactual+" "+time);
                sqLiteDB.insert("BUZON",null, valores);
                sqLiteDB.close();

                etNuevaQuejaSugerencia.setText(""); // Limpiamos el campo de mensaje
                Toast.makeText(fragmento.getActivity(),"Tu mensaje fue enviado correctamente.",Toast.LENGTH_LONG).show();  //Desplegamos mensaje de mensaje creado

            }catch (SQLiteException e) {
                global.escribirError(e, 260);
                Log.i("ERRORBD", e.getMessage() + "");
            }

            return true;    //Retorna verdadero como todo correcto y regresar a ventana pricipal

        }else{
            //La caja de texto esta en blanco
            Toast.makeText(fragmento.getActivity(),"Campo mensaje vacio",Toast.LENGTH_LONG).show();

            //Retorna falso como bandera para continuar en la misma ventana
            return false;
        }
    }

    private Fragment obtenerFragment() {
        FragmentManager fragmentManager = fragmento.getActivity().getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
        }
        return null;
    }


}
