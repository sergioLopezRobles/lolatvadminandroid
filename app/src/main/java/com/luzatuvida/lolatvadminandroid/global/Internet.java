package com.luzatuvida.lolatvadminandroid.global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.fragment.app.FragmentActivity;

public class Internet {

    FragmentActivity activity;

    public Internet(FragmentActivity activity) {
        this.activity = activity;
    }

    /*Metodo/Funcion: verificarConexionInternet
      Descripcion: Verifica si el movil cuenta o no con acceso a internet
    */
    public boolean verificarConexionInternet() {

        ConnectivityManager cm;
        cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
