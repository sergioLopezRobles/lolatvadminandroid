package com.luzatuvida.lolatvadminandroid.global;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Seguridad {

    Fragment fragmento;
    private static final String llavecs = "IMtx1Vir1ZvrVqTlsAPOCc2dfyF9r3bY6IQ6buJhvmR=";

    public Seguridad(Fragment fragmento) {
        this.fragmento = fragmento;
    }

    public static String cifrar(byte[] texto) throws Exception{
        MessageDigest md = MessageDigest.getInstance( "md5" );
        byte[] digestOfPassword = md.digest(llavecs.getBytes( "UTF-16LE" ));
        SecretKeySpec sKeySpec = new SecretKeySpec( digestOfPassword , "AES" );
        Cipher cipher = Cipher.getInstance( "AES/ECB/PKCS7Padding" );
        cipher.init( Cipher.ENCRYPT_MODE , sKeySpec );
        byte[] encrypted = cipher.doFinal( texto );
        return Base64.encodeToString( encrypted , Base64.DEFAULT );
    }
    /*
     * Metodo: decifrar
     * Descripcion:Se ejecuta cada ves que se rerquiere leer un dato de la configuracion, recibe un texto lo decifra y lo retorna.
     * Entrada: llave,texto
     * Salida: string
     */
    public static String decifrar(byte[] texto) throws Exception{
        MessageDigest md = MessageDigest.getInstance( "md5" );
        byte[] digestOfPassword = md.digest(llavecs.getBytes( "UTF-16LE" ));
        SecretKeySpec skeySpec = new SecretKeySpec( digestOfPassword , "AES" );
        Cipher cipher = Cipher.getInstance( "AES/ECB/PKCS7Padding" );
        cipher.init( Cipher.DECRYPT_MODE , skeySpec );
        byte[] decrypted = cipher.doFinal( texto );
        return new String( decrypted , "UTF-16LE" );
    }

    public String leerSharedPreferences(String llave) throws Exception {
        SharedPreferences preferences = fragmento.getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        return decifrar(Base64.decode(preferences.getString(llave, "").getBytes("UTF-16LE"), Base64.DEFAULT));
    }

    public void guardarSharedPreferences(String llave, String valor) throws Exception {
        SharedPreferences preferencias = fragmento.getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor objEditor = preferencias.edit();
        objEditor.putString(llave, cifrar((valor).getBytes("UTF-16LE")));
        objEditor.commit();
    }

}
