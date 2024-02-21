package com.luzatuvida.lolatvadminandroid.vista;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.GenerarIdAlfanumerico;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapsMarcadoresContratos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsMarcadoresContratos extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<String> contratosACrearMarcadores;

    Fragment fragmento;
    Global global;
    ObtenerRol obtenerRol;
    String rol = "";
    Internet internet;
    Runnable runnable;

    int bandera = 0;

    String idUsuario;

    Llaves llaves;

    baseDeDatos conexion;

    GenerarIdAlfanumerico generarIdAlfanumerico;

    public MapsMarcadoresContratos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsMarcadoresContratos.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsMarcadoresContratos newInstance(String param1, String param2) {
        MapsMarcadoresContratos fragment = new MapsMarcadoresContratos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Throwable identificador error 3
        */

        fragmento = MapsMarcadoresContratos.this;
        global = new Global(fragmento);
        internet = new Internet(fragmento.getActivity());
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
        generarIdAlfanumerico = new GenerarIdAlfanumerico(fragmento);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                global.escribirErrorClasePrincipalThrowable(throwable, 3);
                System.exit(2);
            }
        });

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps_marcadores_contratos, container, false);

        contratosACrearMarcadores = new ArrayList<String>();

        Bundle bundle = getArguments();
        if(bundle != null){
            contratosACrearMarcadores = bundle.getStringArrayList("contratosACrearMarcadores");
            bandera = bundle.getInt("bandera");
        }

        obtenerRol = new ObtenerRol(fragmento);
        rol = obtenerRol.obtenerAtributoUsuarioLogeado("ROL");
        idUsuario = obtenerRol.obtenerAtributoUsuarioLogeado("ID");

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(MapsMarcadoresContratos.this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        for(int i = 0; i < contratosACrearMarcadores.size(); i++){

            try {

                String[] camposMarcadores = contratosACrearMarcadores.get(i).split(",");
                int zoom = 0;

                LatLng marcador = new LatLng(Float.parseFloat(camposMarcadores[0]), Float.parseFloat(camposMarcadores[1]));
                String idContrato = camposMarcadores[2];

                String textoAMostrar = idContrato;
                if (bandera == 0) {
                    //Bandera es igual a 0 (Contratos)
                    zoom = 7;
                    int diasAtrasados = 0;
                    int estadoContrato = Integer.parseInt(global.obtenerAtributoContrato(idContrato, "ESTATUS_ESTADOCONTRATO"));
                    if (estadoContrato == 4) {
                        diasAtrasados = global.obtenerDiasAtrasados(global.obtenerAtributoContrato(idContrato, "FECHAATRASO"));
                    }
                    int colorPersonalizado = Color.parseColor(global.obtenerColorHexadecimalEstatus(rol, estadoContrato, diasAtrasados)); // Color verde en formato hexadecimal
                    googleMap.addMarker(new MarkerOptions()
                            .position(marcador)
                            .title(textoAMostrar)
                            .icon(getMarkerIcon(R.drawable.ic_location_azul, colorPersonalizado))); //Establecer el icono personalizado (Color en formato decimal)
                }else {
                    //Bandera es igual a 1 (Abonos)
                    zoom = 1;
                    googleMap.addMarker(new MarkerOptions()
                            .position(marcador)
                            .title(textoAMostrar));
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(marcador));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

            }catch(Exception e){
                global.escribirError(e,261);
                continue;
            }

        }

        googleMap.setOnMarkerClickListener(this::onMarkerClick);
    }

    private BitmapDescriptor getMarkerIcon(@DrawableRes int id, int color) {
        Drawable vectorDrawable = ContextCompat.getDrawable(getContext(), id);
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public boolean onMarkerClick(final Marker marker) {

        if (bandera == 0) {
            //Bandera es igual a 0 (Contratos)

            String descripcionContrato = "";

            descripcionContrato = "CLIENTE: " + global.obtenerAtributoContrato(marker.getTitle(), "NOMBRE").toUpperCase() + ".\n\n";
            descripcionContrato += "DIRECCIÓN: " + global.obtenerAtributoContrato(marker.getTitle(), "CALLEENTREGA").toUpperCase() + " " +
                    global.obtenerAtributoContrato(marker.getTitle(), "NUMEROENTREGA").toUpperCase() + ", COL. " +
                    global.obtenerAtributoContrato(marker.getTitle(), "COLONIAENTREGA").toUpperCase() + ", " +
                    global.obtenerAtributoContrato(marker.getTitle(), "LOCALIDADENTREGA").toUpperCase() + ".\n\n";
            descripcionContrato += "TEL: " + global.obtenerAtributoContrato(marker.getTitle(), "TELEFONO").replace("-", "") + ".";

            final Spinner spOpcionesAlertDialogMapaContratos = new Spinner(fragmento.getActivity());
            //Llenar campos spOpcionesAlertDialogMapaContratos
            String[] idsAlertDialogOpcionesMapaContratos = new String[] {"", "0", "1", "2"};
            String[] opcionesAlertDialogOpcionesMapaContratos = new String[] {"Seleccionar", "Trazar ruta contrato", "Mandar a ruta", "Abrir contrato"};
            spOpcionesAlertDialogMapaContratos.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, opcionesAlertDialogOpcionesMapaContratos));

            final Spinner spRutaAlertDialogMapaContratos = new Spinner(fragmento.getActivity());
            //Llenar campos spRutaAlertDialogMapaContratos
            String[] idsAlertDialogRutaMapaContratos = new String[] {"0", "1", "2", "3", "4", "5", "6", "7"};
            String[] opcionesAlertDialogRutaMapaContratos = new String[] {"Ruta General", "Ruta Lunes", "Ruta Martes", "Ruta Miercoles", "Ruta Jueves", "Ruta Viernes", "Ruta Sabado", "Ruta Domingo"};
            spRutaAlertDialogMapaContratos.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, opcionesAlertDialogRutaMapaContratos));

            // Crear un LinearLayout vertical para contener los Spinners
            LinearLayout linearLayout = new LinearLayout(fragmento.getActivity());
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            // Agregar los Spinners al LinearLayout
            linearLayout.addView(spOpcionesAlertDialogMapaContratos);
            linearLayout.addView(spRutaAlertDialogMapaContratos);

            spOpcionesAlertDialogMapaContratos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    if (idsAlertDialogOpcionesMapaContratos[spOpcionesAlertDialogMapaContratos.getSelectedItemPosition()].equals("1")) {
                        //Se selecciono opcion Mandar a ruta
                        //Mostrar spRutaAlertDialogMapaContratos
                        spRutaAlertDialogMapaContratos.setVisibility(View.VISIBLE);
                    }else {
                        //Se selecciono otra opcion
                        //Ocultar spRutaAlertDialogMapaContratos
                        spRutaAlertDialogMapaContratos.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Acción a realizar si no se ha seleccionado nada en el Spinner
                }
            });

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getContext());
            alerta.setTitle("CONTRATO: " + marker.getTitle()).setView(linearLayout).setMessage(descripcionContrato)
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String opcionSeleccionada = idsAlertDialogOpcionesMapaContratos[spOpcionesAlertDialogMapaContratos.getSelectedItemPosition()];
                            if (opcionSeleccionada.equals("")) {
                                //No se selecciono ninguna opcion
                                Toast.makeText(fragmento.getActivity(), "Debes seleccionar una opción", Toast.LENGTH_LONG).show();
                            } else {
                                //Se selecciono una de las opciones

                                String opcionRuta = idsAlertDialogRutaMapaContratos[spRutaAlertDialogMapaContratos.getSelectedItemPosition()];
                                if (opcionSeleccionada.equals("1") && !global.obtenerUnResultadoQuery("SELECT ID_CONTRATO FROM RUTA WHERE ID_CONTRATO = '" + marker.getTitle() + "' AND ESTADO = '1' AND DIA = '" + opcionRuta + "'").equals("")) {
                                    //Opcion seleccionada es igual a Mandar a ruta y el idcontrato ya existe en la seccion de ruta
                                    Toast.makeText(fragmento.getActivity(), "El contrato ya se encuentra en ruta", Toast.LENGTH_LONG).show();
                                } else {
                                    //No esta en la seccion de ruta

                                    try {

                                        boolean ocultarAlertDialog = true;

                                        switch (opcionSeleccionada) {
                                            case "0": //Trazar ruta contrato
                                                ocultarAlertDialog = global.trazarRutaContrato(marker.getTitle());
                                                break;
                                            case "1": //Mandar a ruta
                                                int contadorOPosicion = Integer.parseInt(global.obtenerUnResultadoQuery("SELECT COUNT(ID_CONTRATO) FROM RUTA WHERE DIA = '" + opcionRuta + "'"));
                                                if (contadorOPosicion > 0) {
                                                    //Hay mas de un registro en la tabla
                                                    contadorOPosicion = Integer.parseInt(global.obtenerUnResultadoQuery("SELECT POSICION FROM RUTA WHERE DIA = '" + opcionRuta + "' ORDER BY CAST(POSICION AS INTEGER) DESC LIMIT 1")) + 1;
                                                }

                                                String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("RUTA", 20);
                                                SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
                                                ContentValues valores = new ContentValues();
                                                valores.put("ID", idAlfanumerico);
                                                valores.put("DIA", opcionRuta);
                                                valores.put("ID_CONTRATO", marker.getTitle());
                                                valores.put("ID_USUARIO", idUsuario);
                                                valores.put("POSICION", String.valueOf(contadorOPosicion));
                                                valores.put("ESTADO", "1");
                                                valores.put("ENVIADOPAGINA", "0");
                                                sqLiteDB2.insert("RUTA", null, valores);
                                                sqLiteDB2.close();

                                                global.llamadaSincronizacion();
                                                Toast.makeText(fragmento.getActivity(), "Se agrego correctamente el contrato a la ruta", Toast.LENGTH_LONG).show();
                                                break;
                                            case "2": //Abrir contrato
                                                global.agregarAUltimoContratoVisto(marker.getTitle());

                                                Fragment verificadorFragment = new verContrato();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("ultimoIdContratoCreado", marker.getTitle());
                                                verificadorFragment.setArguments(bundle);
                                                FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                transaction.replace(R.id.content, verificadorFragment);
                                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                transaction.addToBackStack(null);
                                                transaction.commit();
                                                break;
                                        }

                                        if (ocultarAlertDialog) {
                                            //Ocultar alertDialog
                                            dialogInterface.dismiss();
                                        }

                                    } catch (Exception e) {
                                        global.escribirError(e, 325);
                                        e.printStackTrace();
                                    }

                                }

                            }

                        }
                    }).setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).setNeutralButton("LLAMAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", global.obtenerAtributoContrato(marker.getTitle(), "TELEFONO").replace("-", ""), null));
                            startActivity(intent);
                        }
                    }).show();

        }else {
            //Bandera es igual a 1 (Abonos)

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getContext());
            alerta.setTitle("CONTRATO: " + marker.getTitle())
                    .setPositiveButton("ABRIR CONTRATO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            global.agregarAUltimoContratoVisto(marker.getTitle());

                            Fragment verificadorFragment = new verContrato();
                            Bundle bundle = new Bundle();
                            bundle.putString("ultimoIdContratoCreado", marker.getTitle());
                            verificadorFragment.setArguments(bundle);
                            FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.content, verificadorFragment);
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }).setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
        }

        return false;
    }

}