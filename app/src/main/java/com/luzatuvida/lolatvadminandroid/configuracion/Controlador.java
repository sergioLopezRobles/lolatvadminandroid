package com.luzatuvida.lolatvadminandroid.configuracion;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Controlador {

    /*
    Identificador error del 1 al 7, 257 al 258
     */

    Fragment fragmento;
    String rol = "";
    String nombreUsuarioLogeado = "";
    Seguridad seguridad;
    TextView tvNombreImpresora, tvDatosAImprimir;
    LayoutInflater inflater = null;
    View searchView;
    Spinner spDispositivosAlertDialog;
    List<String> nombresDispositivos;
    BluetoothAdapter bluetoothAdapter;
    baseDeDatos conexion;
    Llaves llaves;
    Global global;
    ObtenerRol obtenerRol;
    ImpresoraBluetooth impresoraBluetooth;
    Sincronizacion sincronizacion;
    Internet internet;

    String idUsuario = "";

    //Crear Imagen corte
    LinearLayout llFormatoImagenCompartir;
    TextView tvDatosCorte, tvFechaImagenCorte, tvNombreCobrador;
    String nombreImagen = "";

    private final int MY_PERMISSION_BLUETOOTH = 1000;

    public Controlador(Fragment fragmento, Object[] objetos) {
        this.fragmento = fragmento;
        rol = (String)objetos[0];
        nombreUsuarioLogeado = (String)objetos[1];
        tvNombreImpresora = (TextView) objetos[2];
        tvDatosAImprimir = (TextView) objetos[3];
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        seguridad = new Seguridad(fragmento);
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getContext(), "basedatos", null, llaves.versiondb);
        global = new Global(fragmento);
        obtenerRol = new ObtenerRol(fragmento);
        impresoraBluetooth = new ImpresoraBluetooth(fragmento);
        sincronizacion = new Sincronizacion(fragmento, false);
        internet = new Internet(fragmento.getActivity());
        idUsuario = obtenerRol.obtenerAtributoUsuarioLogeado("ID");

        llFormatoImagenCompartir = (LinearLayout) objetos[4];
        tvDatosCorte = (TextView) objetos[5];
        tvFechaImagenCorte = (TextView) objetos[6];
        tvNombreCobrador = (TextView) objetos[7];
    }

    public boolean tienePermisos(){
        //Verificar si la version del sistema operativo es menor a la 6, si es asi no ocupamos de pedir permisos
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || (Build.VERSION.SDK_INT < 31)) {
            return true;
        }

        //Si los permisos estan aceptados
        //Para versiones Android 11 -> Verificar permiso BLUETOOTH_ADMIN
        if(fragmento.getActivity().checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED){
            return true;
        }

        //Para versiones Android 12 o superior -> Verificar permisos BLUETOOTH_ADVERTISE, BLUETOOTH_CONNECT, BLUETOOTH_SCAN
        if ((fragmento.getActivity().checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED)
                    && (fragmento.getActivity().checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED)
                    && (fragmento.getActivity().checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        //Pedir los permisos al usuario mediante un mensaje extra
        if ((fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_ADVERTISE))
                || (fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_CONNECT))
                || (fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_SCAN))) {
            androidx.appcompat.app.AlertDialog.Builder alerta = new androidx.appcompat.app.AlertDialog.Builder(fragmento.getContext());
            alerta.setTitle("Permisos denegados").setMessage("Por favor ve a los ajustes de la aplicación y autoriza todos los permisos.").setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
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
        } else {
            fragmento.requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN}, MY_PERMISSION_BLUETOOTH);
        }
        return false;
    }

    public void mandarAPantallaConfiguracionTelefono() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS); //Va a abrir la aplicacion de configuraciones junto con el detalle de la aplicacion para poder aceptar o habilitar los permisos
        Uri uri = Uri.fromParts("package", fragmento.getActivity().getPackageName(), null);
        intent.setData(uri);
        fragmento.startActivity(intent);
    }

    public void mostrarAlertDialogSeleccionarImpresora() {

        if (!bluetoothAdapter.isEnabled()) {
            //No esta encendido el bluetooth
            Toast.makeText(fragmento.getContext(), "Favor de encender el Bluetooth", Toast.LENGTH_LONG).show();
        }else {
            //Esta encendido el bluetooth

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            inflater = (LayoutInflater) fragmento.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            searchView = inflater.inflate(R.layout.dialog_seleccionarimpresora, null);

            spDispositivosAlertDialog = searchView.findViewById(R.id.spDispositivosAlertDialog);

            llenarSpinnersDispositivosBluetooth();

            alerta.setTitle("Seleccionar impresora").setView(searchView)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Debe ir vacio por que se esta obteniendo abajo
                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            final AlertDialog dialog = alerta.create();
            dialog.show();

            spDispositivosAlertDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (nombresDispositivos.get(spDispositivosAlertDialog.getSelectedItemPosition()) == "Seleccionar") {
                                Toast.makeText(fragmento.getActivity(), "Debes seleccionar un dispositivo", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    seguridad.guardarSharedPreferences("valorImpresoraSeleccionada", nombresDispositivos.get(spDispositivosAlertDialog.getSelectedItemPosition()));
                                    tvNombreImpresora.setText(nombresDispositivos.get(spDispositivosAlertDialog.getSelectedItemPosition()));
                                    tvNombreImpresora.setTextColor(Color.parseColor("#0AA09E"));
                                    Toast.makeText(fragmento.getActivity(), "Se agrego correctamente el dispositivo", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    global.escribirError(e, 1);
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

    }

    private void llenarSpinnersDispositivosBluetooth() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        nombresDispositivos = new ArrayList<String>();

        nombresDispositivos.add("Seleccionar");
        for(BluetoothDevice bt : pairedDevices) {
            nombresDispositivos.add(bt.getName());
        }

        spDispositivosAlertDialog.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, nombresDispositivos));

    }

    public void verificarSiBluetoothFunciona() {
        if(bluetoothAdapter == null) {
            //No funciona el Bluetooth del dispositivo
            Toast.makeText(fragmento.getContext(), "Tu dispositivo no soporta Bluetooth", Toast.LENGTH_LONG).show();
            Fragment verificadorFragment = new principal(false);
            FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, verificadorFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void mandarAImprimirCorte() {

        if(llaves.impresora_termica) {
            //Se utilizara impresora termica

            JSONObject jsonObjectCompletado = obtenerJSONObjectCompletado(obtenerRol.obtenerAtributoUsuarioLogeado("JSONABONOSGENERAL"));

            List<String> keys = new ArrayList<String>();
            List<String> values = new ArrayList<String>();
            String totalAbonos = "";

            if (jsonObjectCompletado.toString().length() > 2) {

                //Log.i("MENSAJE", "ABONOS");
                Object[] valoresSeparados = obtenerValoresSeparadosJsonObjectCompleto(jsonObjectCompletado);
                keys = (List<String>) valoresSeparados[0];
                values = (List<String>) valoresSeparados[1];
                totalAbonos = (String) valoresSeparados[2];

            /*for (int i = 0; i < keys.size(); i++) {
                Log.i("MENSAJE", keys.get(i) + " -> " + values.get(i));
            }
            Log.i("MENSAJE", "TOTAL: " + totalAbonos);*/

            } else {
                //Log.i("MENSAJE", "SIN ABONOS");
                //Log.i("MENSAJE", "TOTAL: 0");
            }

            int numTotalContratos = obtenerNumTotalContratosCorte();
            String numTotalContratosSumado = String.valueOf(Integer.parseInt(obtenerRol.obtenerAtributoUsuarioLogeado("TOTALCONTRATOSCONABONO")) + numTotalContratos);
            int numTotalContratosQueNoSeHanSubidoAPagina = obtenerNumTotalContratosQueNoSeHanSubidoAPagina();

            if (impresoraBluetooth.findBluetoothDevice()) {
                impresoraBluetooth.verificarSiBluetoothPrinterEstaCerrado();
                Object[] objetos = {numTotalContratosSumado, String.valueOf(numTotalContratosQueNoSeHanSubidoAPagina), keys, values, totalAbonos};
                impresoraBluetooth.printData(objetos, 1);
                impresoraBluetooth.closedBluetoothPrinter();
            }

        }

    }

    private JSONObject obtenerJSONObjectCompletado(String jsonabonosgeneral) {

        JSONObject jsonObject = new JSONObject();

        try {

            List<String> abonos = obtenerAbonosCorteCero(); //Se obtienen los abonos con corte en 0

            if(jsonabonosgeneral.length() > 2) {
                //jsonabonosgeneral tiene algo

                jsonObject = new JSONObject(jsonabonosgeneral);

                for (int i = 0; i < abonos.size(); i++) {

                    if(jsonObject.has(abonos.get(i))) {
                        //Existe llave
                        jsonObject.put(abonos.get(i), Integer.parseInt(jsonObject.get(abonos.get(i)).toString()) + 1);
                    }else {
                        //No existe llave
                        jsonObject.put(abonos.get(i), "1");
                    }
                }

            }else {
                //jsonabonosgeneral esta vacio

                for (int i = 0; i < abonos.size(); i++) {

                    if(jsonObject.has(abonos.get(i))) {
                        //Existe llave
                        jsonObject.put(abonos.get(i), Integer.parseInt(jsonObject.get(abonos.get(i)).toString()) + 1);
                    }else {
                        //No existe llave
                        jsonObject.put(abonos.get(i), "1");
                    }
                }

            }

        } catch (JSONException e) {
            global.escribirError(e, 2);
            e.printStackTrace();
        }

        return jsonObject;
    }

    private List<String> obtenerAbonosCorteCero() {

        List<String> abonos = new ArrayList<>();

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ABONO FROM ABONOS WHERE CORTE = '0' AND ENVIADO = '0' AND ID_USUARIO = '" + idUsuario + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() > 0){
                while (datos.moveToNext()) {
                    if(datos.getString(0).indexOf(".") > 0) {
                        //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
                        abonos.add(datos.getString(0).substring(0, datos.getString(0).indexOf(".")));
                    }else {
                        abonos.add(datos.getString(0));
                    }
                }
            }

            sqLiteDB2.close();
            datos.close();

        }catch (Exception e) {
            global.escribirError(e, 3);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return abonos;
    }

    private Object[] obtenerValoresSeparadosJsonObjectCompleto(JSONObject jsonObjectCompletado) {

        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        int totalAbonos = 0;

        try {

            Iterator<String> iter = jsonObjectCompletado.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                Object value = jsonObjectCompletado.get(key);
                keys.add(key);
                values.add(String.valueOf(value));
                totalAbonos = totalAbonos + (Integer.parseInt(key) * Integer.parseInt(String.valueOf(value)));
            }

        } catch (JSONException e) {
            global.escribirError(e, 4);
            e.printStackTrace();
        }

        Object[] objects = {keys, values, String.valueOf(totalAbonos)};
        return objects;
    }

    private void mandarALlamarSincronizacion() {
        Object[] objetosWebService = new Object[]{obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN"), "", ""};
        sincronizacion.sincronizarMetodo(0, objetosWebService, 0);
    }

    private int obtenerNumTotalContratosCorte() {

        int numTotalContratos = 0;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT COUNT(C.ID_CONTRATO) FROM CONTRATOS C WHERE C.ID_CONTRATO IN (SELECT A.ID_CONTRATO FROM ABONOS A " +
                         "WHERE A.CORTE = '0' AND A.ENVIADO = '0' AND A.ID_USUARIO = '" + idUsuario + "' AND A.TIPOABONO NOT IN (7) GROUP BY A.ID_CONTRATO)";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el atributo del contrato");
            }

            if (datos.moveToFirst()) {
                numTotalContratos = datos.getInt(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 5);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return numTotalContratos;

    }

    private int obtenerNumTotalContratosQueNoSeHanSubidoAPagina() {

        int numTotalContratosQueNoSeHanSubidoAPagina = 0;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT COUNT(ID_CONTRATO) FROM CONTRATOS WHERE ENVIADOPAGINA = '0'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el atributo del contrato");
            }

            if (datos.moveToFirst()) {
                numTotalContratosQueNoSeHanSubidoAPagina = datos.getInt(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 6);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return numTotalContratosQueNoSeHanSubidoAPagina;

    }

    public String obtenerCadenaDatosAImprimir() {

        String mensajeCompleto = "";

        JSONObject jsonObjectCompletado = obtenerJSONObjectCompletado(obtenerRol.obtenerAtributoUsuarioLogeado("JSONABONOSGENERAL"));

        List<String> keys = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        String totalAbonos = "";
        int contador = 0;

        if(jsonObjectCompletado.toString().length() > 2) {

            mensajeCompleto = "ABONOS\n";
            Object[] valoresSeparados = obtenerValoresSeparadosJsonObjectCompleto(jsonObjectCompletado);
            keys = (List<String>) valoresSeparados[0];
            values = (List<String>) valoresSeparados[1];
            totalAbonos = (String) valoresSeparados[2];

            for (int i = 0; i < keys.size(); i++) {
                mensajeCompleto += keys.get(i) + " -> " + values.get(i) + "\n";
                contador += Integer.parseInt(values.get(i));
            }
            mensajeCompleto += "TOTAL ABONOS: " + contador + "\n";
            mensajeCompleto += "TOTAL: $" + totalAbonos + "\n\n";

        }else {
            mensajeCompleto += "SIN ABONOS\n";
            mensajeCompleto += "TOTAL ABONOS: 0\n";
            mensajeCompleto += "TOTAL: $0\n\n";
        }

        int numTotalContratos = obtenerNumTotalContratosCorte();
        String numTotalContratosSumado = String.valueOf(Integer.parseInt(obtenerRol.obtenerAtributoUsuarioLogeado("TOTALCONTRATOSCONABONO")) + numTotalContratos);
        int numTotalContratosQueNoSeHanSubidoAPagina = obtenerNumTotalContratosQueNoSeHanSubidoAPagina();

        mensajeCompleto += "    TOTAL CONTRATOS:             " + numTotalContratosSumado + "\n";
        mensajeCompleto += "    CONTRATOS PENDIENTES:  " + numTotalContratosQueNoSeHanSubidoAPagina;

        String nombreCobrador = obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO");
        mensajeCompleto += "\n\n" + "     COBRADOR: " + nombreCobrador;

        return mensajeCompleto;

    }

    public void mandarADescargarMapa() {

        if (internet.verificarConexionInternet()) {
            //Si tienes conexion puedes descargar el mapa
            try {
                Uri uri = Uri.parse("https://maps.google.co.in/maps?q=ok maps");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                fragmento.getActivity().startActivity(intent);

            } catch (ActivityNotFoundException e) {
                global.escribirError(e, 7);
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                fragmento.getActivity().startActivity(intent);
            }
        }else{
            //Si no tienes conexion
            Toast.makeText(fragmento.getActivity(), "Sin conexion a internet...", Toast.LENGTH_LONG).show();
        }
    }

    public void mostrarAlertaCompartirCorte(){

        llFormatoImagenCompartir.setVisibility(View.VISIBLE);

        final AlertDialog.Builder builder = new AlertDialog.Builder(fragmento.getActivity());
        builder
                .setTitle("Compartir Corte")
                .setMessage("Deseas compartir el corte hasta este momento?")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        llFormatoImagenCompartir.setVisibility(View.GONE);
                    }
                })
                .setPositiveButton("Compartir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        compartirCorte();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.setCancelable(false); // No se cancele con un clic fuera
    }

    //Metodo: compartirCorte
    //Descripcion: Recupera los datos correspondientes a los cortes del cobrador generando para convertirlo en imagen
    public void compartirCorte(){

        //eliminamos imagen del corte en caso de que exista una ya generada anteriormente
        global.eliminarImagenCompartir(nombreImagen);

        String fecha = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String hora = new SimpleDateFormat(" HH:mm:ss").format(new Date());
        String nombreCobrador = obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO");

        tvNombreCobrador.setText(nombreCobrador);
        tvFechaImagenCorte.setText("Fecha: "+fecha+"    Hora: "+hora); //Asignamos fecha y hora de creacion de iamgen corte
        llFormatoImagenCompartir.setDrawingCacheEnabled(true); //Se habilita la cache
        llFormatoImagenCompartir.buildDrawingCache(); //Carga los datos
        Bitmap bitmap = llFormatoImagenCompartir.getDrawingCache(); // Se crea e inicializa el mapa de bits

        try {
            opcionesCompartir(bitmap);
        } catch (Exception e) {
            global.escribirError(e, 257); // Escrito 21-08-2022
            Log.i("Imagen", "Error al generar archivo .JPG: \n" + e.getMessage());
        } finally {
            llFormatoImagenCompartir.destroyDrawingCache();
        }
    }

    //Metodo: opcionesCompartir
    //Descripcion: Despliega un intent con las opciones a compartir del movil
    public void opcionesCompartir(Bitmap bitmap){

        String fecha = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        String hora = new SimpleDateFormat("HH_mm_ss").format(new Date());

        Intent compartir = new Intent(Intent.ACTION_SEND);
        compartir.setType("image/jpg"); // Se asiga el tipo de archivo
//        compartir.setPackage("com.android.bluetooth");  // Solo compartir por bluetooth
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);//creamos la imagen en formato jpg, con la calidad 100
        nombreImagen = "IMG_CORTE_"+fecha+"_"+hora; //Asignamos un nombre a la imagen
        String path = MediaStore.Images.Media.insertImage(fragmento.getActivity().getContentResolver(), bitmap, nombreImagen,null); // Insertamos la imagen creada en vista previa a compartir
        Uri imagenUri = Uri.parse(path); // Optenemos la uri de la imagen
        compartir.putExtra(Intent.EXTRA_STREAM, imagenUri); //Adjuta el archivo
        fragmento.startActivity(Intent.createChooser(compartir,"Seleccionar")); // Despliega las opciones de compartir
        llFormatoImagenCompartir.setVisibility(View.GONE);
    }

    //Metodo: prepararDatosCompartirCorte
    /*Descripcion: Este metodo reside los datos del tvDatosImprimir y eliminaremos el texto del combre cobrador para tomar el formato del tvNombreCobrador
      asignado en el formato de la imagen de corte compartir */
    public String prepararDatosCompartirCorte(String datosCorte) {
        String cadenaResultado = "";

        String nombreCobrador = obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO");    //Recuperamos el nombre del cobrador
        cadenaResultado = datosCorte.replace("\n\n" + "     COBRADOR: " + nombreCobrador, "");  //Remplazamos la cadena por vacio


        return cadenaResultado; //Retornamos la cadena de datos pero sin el nombre del cobrador con el formato antiguo
    }

}