package com.luzatuvida.lolatvadminandroid.global;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Localizacion implements LocationListener {

    /*
    Identificador error del 74 al 75
     */

    Fragment fragmento;
    String latitud;
    String longitud;
    private final int MY_PERMISSION_LOCATION = 1000;
    String coordenadas = "";
    Global global;

    public Localizacion(Fragment fragmento) {
        this.fragmento = fragmento;
        global = new Global(fragmento);
    }

    public boolean tienePermisos(){
        //Verificar si la version del sistema operativo es menor a la 6, si es asi no ocupamos de pedir permisos
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        //Si los permisos estan aceptados
        if ((fragmento.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                && (fragmento.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        //Pedir los permisos al usuario mediante un mensaje extra
        if ((fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                || (fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getContext());
            alerta.setTitle("Permisos denegados").setMessage("Los permisos son necesarios para poder usar la aplicación").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fragmento.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_LOCATION);
                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        } else {
            fragmento.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_LOCATION);
        }

        return false;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitud = String.valueOf(location.getLatitude());
        longitud = String.valueOf(location.getLongitude());
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        //LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        //LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //LocationListener.super.onStatusChanged(provider, status, extras);
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSION_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permisos aceptados
                Toast.makeText(fragmento.getContext(), "Permisos aceptados", Toast.LENGTH_LONG).show();
            } else {
                //Permisos no aceptados
                showExplanation();
            }
        }
    }

    private void showExplanation() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getContext());
        alerta.setTitle("Permisos denegados")
                .setMessage("Por favor ve a los ajustes de la aplicación y autoriza todos los permisos.")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mandarAPantallaConfiguracionTelefono();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public void mandarAPantallaConfiguracionTelefono() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS); //Va a abrir la aplicacion de configuraciones junto con el detalle de la aplicacion para poder aceptar o habilitar los permisos
        Uri uri = Uri.fromParts("package", fragmento.getActivity().getPackageName(), null);
        intent.setData(uri);
        fragmento.startActivity(intent);
    }

    //Metodo generar ruta para un solo destino
    //Descripcion: este metodo recibe la ubicacion actual del cobrador y los datos del contrato a cobrar
    public void trazarRutaDestinos(String direccionOrigen, String direccionDestino) {
        try {
            //Generar ruta a partir de una direccion origen y destino
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + direccionOrigen + direccionDestino);

            //Abrir googlemaps en la app del movil con la ruta cargada
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            fragmento.startActivity(intent);

            //Si no se cuenta con la app googleMaps te dirige a descargarla
        } catch (ActivityNotFoundException e) {
            global.escribirError(e, 74);
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");

            //Abre play Store en el dispositivo para descargar la app
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            fragmento.startActivity(intent);
        }
    }

    public boolean actualizarUbicacion(Localizacion localizacion){

        //Declarar e implementar Location manager para hacer uso de servicios
        LocationManager locationManager = (LocationManager) fragmento.getActivity().getSystemService(Context.LOCATION_SERVICE);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            fragmento.getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            Toast.makeText(fragmento.getActivity(),"Favor de encender GPS",Toast.LENGTH_LONG).show();
            return false;
        }

        //Agregar checkSelfPermission para hacer uso de los request para ubicacion
        if (ActivityCompat.checkSelfPermission(fragmento.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(fragmento.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        //Generar peticiones en cuanto la ubicacion actual
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) localizacion);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) localizacion);

        return true;

    }

    //Metodo validar direccion
    //Descripcion: Valida si una direccion es posible para trazar en mapa

    public boolean validarDireccionContrato(String direccionAValidar){

        String direccionGoogle = "";
        String localidad = "";

        if (direccionAValidar != null) {
            //si obtenemos una direccion distinta a vacio

            //separamos la direccion recibida para optener localidad
            String [] direccionRecibida = direccionAValidar.split(",");
            localidad = direccionRecibida[2];

            try {

                Geocoder geocoder = new Geocoder(fragmento.getActivity(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocationName(
                        direccionAValidar, 1);

                if (!((List<?>) list).isEmpty()) {
                    //Si lista contiene resultado
                    Address location = list.get(0);
                    //Obtenemos las coordenadas
                    setCoordenadas(location.getLatitude() + ","+location.getLongitude());

                    Address address = list.get(0);
                    direccionGoogle = address.getAddressLine(0);

                    //Dividimos resultado obtenido para verificar una direccion completa
                    //nomenclatura Google: calle #, colonia, CP. localidad, Estado, Pais
                    String[] divideDireccionRecuperada = direccionGoogle.split(",");

                    if(divideDireccionRecuperada.length == 5) {
                        //Si nomenclatura es completa, es una direccion posible a trazar

                        //Separamos la localidad de su C.P para comparar localidad
                        String [] dividirLocalidad = divideDireccionRecuperada[2].split(" ");
                        if(dividirLocalidad.length == 3) {
                            if (localidad.equals(dividirLocalidad[2].toUpperCase())) {
                                //si localidad recibida es igual a la localidad proporcionada por google

                                if (divideDireccionRecuperada[4].equals(" México")) {
                                    //Si pais es igual a Mexico
                                    return true;
                                }
                            }
                        }
                    }
                }

            } catch (IOException e) {
                global.escribirError(e, 75);
                e.printStackTrace();
            }
        }

        return false;

    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

}
