package com.luzatuvida.lolatvadminandroid.nuevoHistorialClinico;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.luzatuvida.lolatvadminandroid.global.Camara;
import com.luzatuvida.lolatvadminandroid.global.DatePickerFragment;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.GenerarIdAlfanumerico;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.HistorialMovimientosContrato;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Sincronizacion;
import com.luzatuvida.lolatvadminandroid.global.Teclado;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;
import com.luzatuvida.lolatvadminandroid.vista.verContrato;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Controlador {

    /*
    Identificador error del 140 al 156
     */

    Fragment fragmento;
    EditText etNombreCliente2;
    EditText etEdadNuevoHistorial2, etDiagnosticoNuevoHistorial2, etOcupacionNuevoHistorial2, etDiabetesNuevoHistorial2, etHipertensionNuevoHistorial2,
            etOtroMoletiaNuevoHistorial2, etUltimoExamenNuevoHistorial2, etFechaEntregaNuevoHistorial2, etOjoDerechoEsfericoNuevoHistorial2, etOjoDerechoCilindroNuevoHistorial2,
            etOjoDerechoEjeNuevoHistorial2, etOjoDerechoAddNuevoHistorial2, etOjoDerechoALTNuevoHistorial2, etOjoIzquierdoEsfericoNuevoHistorial2,
            etOjoIzquierdoCilindroNuevoHistorial2, etOjoIzquierdoEjeNuevoHistorial2, etOjoIzquierdoAddNuevoHistorial2, etOjoIzquierdoALTNuevoHistorial2,
            etOtroMaterialNuevoHistorial2, etPrecioMaterialNuevoHistorial2, etOtroTratamientoNuevoHistorial2, etPrecioTratamientoNuevoHistorial2, etObservacionesNuevoHistorial2,
            etObservacionesInternoNuevoHistorial2, etEmbarazadaNuevoHistorial2, etDurmioSeisOchoHorasNuevoHistorial2, etActividadDiaNuevoHistorial2, etProblemasOjosNuevoHistorial2,
            etOtroBifocalNuevoHistorial2, etPrecioBifocalNuevoHistorial2;
    EditText etOjoDerechoEsfericoSinConversionNuevoHistorial2, etOjoDerechoCilindroSinConversionNuevoHistorial2, etOjoDerechoEjeSinConversionNuevoHistorial2,
            etOjoDerechoAddSinConversionNuevoHistorial2, etOjoIzquierdoEsfericoSinConversionNuevoHistorial2, etOjoIzquierdoCilindroSinConversionNuevoHistorial2,
            etOjoIzquierdoEjeSinConversionNuevoHistorial2, etOjoIzquierdoAddSinConversionNuevoHistorial2;
    TextView tvProductoNuevoHistorial2, tvFechaEntregaNuevoHistorial2, tvOjoDerechoEsfericoNuevoHistorial2, tvOjoDerechoCilindroNuevoHistorial2,
            tvOjoDerechoEjeNuevoHistorial2, tvOjoDerechoAddNuevoHistorial2, tvOjoDerechoALTNuevoHistorial2, tvOjoIzquierdoEsfericoNuevoHistorial2,
            tvOjoIzquierdoCilindroNuevoHistorial2, tvOjoIzquierdoEjeNuevoHistorial2, tvOjoIzquierdoAddNuevoHistorial2, tvOjoIzquierdoALTNuevoHistorial2,
            tvOtroMaterialNuevoHistorial2, tvPrecioMaterialNuevoHistorial2, tvOtroTratamientoNuevoHistorial2, tvPrecioTratamientoNuevoHistorial2, tvTipoBifocalNuevoHistorial2,
            tvGeneralSinConversionNuevoHistorial2, tvOtroBifocalNuevoHistorial2, tvPrecioBifocalNuevoHistorial2, tvColorTratamiento2, tvEstiloTratamiento2,
            tvColorTratamientoPolarizado2, tvColorTratamientoEspejeado2, tvFotoArmazonPropioHistorial2;
    RadioGroup rgTipoBifocalNuevoHistorial2;
    RadioButton rbHiIndex2, rbCR2, rbPolicarbonato2, rbOtroMaterial2, rbFT2, rbBlend2, rbProgresivo2, rbNA2, rbOtroBifocal2;
    CheckBox cbDolorCabeza2, cbArdorOjos2, cbGolpeCabeza2, cbOtroMolestia2, cbTratamientoFotocromatico2, cbTratamientoAR2, cbTratamientoTinte2, cbTratamientoBlueray2,
             cbTratamientoOtro2, cbTratamientoPolarizado2, cbPolicarbonatoTipo2, cbTratamientoEspejeado2;
    Spinner spProductoNuevoHistorial2, spPaquetesNuevoHistorial2, spColorTratamiento2, spEstiloTratamiento2, spColorTratamientoPolarizado2,
            spColorTratamientoEspejeado2;
    Button btnGuardarHistorialNuevo2;
    LinearLayout llVisionCercana, llColorEstiloTinte2, llTratamientoPolarizado2, llTratamientoEspejeado2, llFotoArmazonPropioHistorial2;

    ImageView ivFotoArmazonPropioHistorial2;

    String[] idsPaquetes, nombresPaquetes, idsColorTratamientoTinte, colorTratamientoTinte, idsEstiloTratamientoTinte, estiloTratamientoTinte, idsColorTratamientoPolarizado,
            colorTratamientoPolarizado, idsColorTratamientoEspejo, colorTratamientoEspejo;
    baseDeDatos conexion;
    SQLiteDatabase sqLiteDB;
    HistorialMovimientosContrato historialMovimientosContrato;

    boolean[] arregloComponentes;
    boolean correcto = true;

    String ultimoIdContratoCreado = "";
    String ultimoIdHistorialClinicoCreado = "";
    boolean modificarHistorialClinico;

    List<String> idsProductos;
    List<String> nombresProductos;
    int[] coloresRBGProductos;

    boolean[] arregloTratamientos;
    String idContratoPadre;
    String time = "";
    String fechaActual = "";
    ObtenerRol obtenerRol;
    GenerarIdAlfanumerico generarIdAlfanumerico;
    Global global;
    Sincronizacion sincronizacion;
    Object[] objetosWebService;
    Llaves llaves;

    Teclado teclado;
    double precioTratamientoOtro = 0;
    double precioMaterialOtro = 0;
    double precioCilindroLectura = 0;
    String idPaqueteSeleccionado;
    double precioAumentoORestaDorado1OPlatino = 0;
    boolean esHistorialGarantia;
    boolean confirmacionAlertaCambioPaqueteDorado1PlatinooViserversa = false;
    double precioBifocalOtro = 0;

    String idProductoGarantia = "";
    String bifocalBD = "";
    String otroBifocalBD = "";
    String otroBifocalPrecioBD = "";
    double precioMaterial = 0;
    boolean policarbonatoBD = false;
    double precioPolicarbonato = 300;
    boolean esArmazonPropio = false;
    public String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
    Camara camara;

    public Controlador(Fragment fragmento, Object[] objetos) {
        this.fragmento = fragmento;
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
        arregloComponentes = new boolean[25];
        arregloTratamientos = new boolean[7];
        historialMovimientosContrato = new HistorialMovimientosContrato(fragmento);
        obtenerRol = new ObtenerRol(fragmento);
        camara = new Camara(fragmento);
        generarIdAlfanumerico = new GenerarIdAlfanumerico(fragmento);
        global = new Global(fragmento);
        sincronizacion = new Sincronizacion(fragmento,false);
        teclado = new Teclado(fragmento);

        etEdadNuevoHistorial2 = (EditText)objetos[0];
        etDiagnosticoNuevoHistorial2 = (EditText)objetos[1];
        etOcupacionNuevoHistorial2 = (EditText)objetos[2];
        etDiabetesNuevoHistorial2 = (EditText)objetos[3];
        etHipertensionNuevoHistorial2 = (EditText)objetos[4];
        cbDolorCabeza2 = (CheckBox) objetos[5];
        cbArdorOjos2 = (CheckBox)objetos[6];
        cbGolpeCabeza2 = (CheckBox)objetos[7];
        cbOtroMolestia2 = (CheckBox)objetos[8];
        etOtroMoletiaNuevoHistorial2 = (EditText)objetos[9];
        etUltimoExamenNuevoHistorial2 = (EditText)objetos[10];
        spProductoNuevoHistorial2 = (Spinner) objetos[11];
        spPaquetesNuevoHistorial2 = (Spinner) objetos[12];
        etFechaEntregaNuevoHistorial2 = (EditText)objetos[13];
        etOjoDerechoEsfericoNuevoHistorial2 = (EditText)objetos[14];
        etOjoDerechoCilindroNuevoHistorial2 = (EditText)objetos[15];
        etOjoDerechoEjeNuevoHistorial2 = (EditText)objetos[16];
        etOjoDerechoAddNuevoHistorial2 = (EditText)objetos[17];
        etOjoDerechoALTNuevoHistorial2 = (EditText)objetos[18];
        etOjoIzquierdoEsfericoNuevoHistorial2 = (EditText)objetos[19];
        etOjoIzquierdoCilindroNuevoHistorial2 = (EditText)objetos[20];
        etOjoIzquierdoEjeNuevoHistorial2 = (EditText)objetos[21];
        etOjoIzquierdoAddNuevoHistorial2 = (EditText)objetos[22];
        etOjoIzquierdoALTNuevoHistorial2 = (EditText)objetos[23];
        rbHiIndex2 = (RadioButton) objetos[24];
        rbCR2 = (RadioButton)objetos[25];
        rbPolicarbonato2 = (RadioButton)objetos[26];
        rbOtroMaterial2 = (RadioButton)objetos[27];
        etOtroMaterialNuevoHistorial2 = (EditText)objetos[28];
        etPrecioMaterialNuevoHistorial2 = (EditText)objetos[29];
        cbTratamientoFotocromatico2 = (CheckBox)objetos[30];
        cbTratamientoAR2 = (CheckBox) objetos[31];
        cbTratamientoTinte2 = (CheckBox)objetos[32];
        cbTratamientoBlueray2 = (CheckBox)objetos[33];
        cbTratamientoOtro2 = (CheckBox)objetos[34];
        etOtroTratamientoNuevoHistorial2 = (EditText)objetos[35];
        etPrecioTratamientoNuevoHistorial2 = (EditText)objetos[36];
        rbFT2 = (RadioButton) objetos[37];
        rbBlend2 = (RadioButton)objetos[38];
        rbProgresivo2 = (RadioButton)objetos[39];
        rbNA2 = (RadioButton)objetos[40];
        etObservacionesNuevoHistorial2 = (EditText)objetos[41];

        tvProductoNuevoHistorial2 = (TextView)objetos[42];
        tvFechaEntregaNuevoHistorial2 = (TextView)objetos[43];
        tvOjoDerechoEsfericoNuevoHistorial2 = (TextView)objetos[44];
        tvOjoDerechoCilindroNuevoHistorial2 = (TextView) objetos[45];
        tvOjoDerechoEjeNuevoHistorial2 = (TextView)objetos[46];
        tvOjoDerechoAddNuevoHistorial2 = (TextView)objetos[47];
        tvOjoDerechoALTNuevoHistorial2 = (TextView)objetos[48];
        tvOjoIzquierdoEsfericoNuevoHistorial2 = (TextView)objetos[49];
        tvOjoIzquierdoCilindroNuevoHistorial2 = (TextView)objetos[50];
        tvOjoIzquierdoEjeNuevoHistorial2 = (TextView)objetos[51];
        tvOjoIzquierdoAddNuevoHistorial2 = (TextView)objetos[52];
        tvOjoIzquierdoALTNuevoHistorial2 = (TextView)objetos[53];
        tvOtroMaterialNuevoHistorial2 = (TextView)objetos[54];
        tvPrecioMaterialNuevoHistorial2 = (TextView)objetos[55];
        tvOtroTratamientoNuevoHistorial2 = (TextView)objetos[56];
        tvPrecioTratamientoNuevoHistorial2 = (TextView)objetos[57];
        tvTipoBifocalNuevoHistorial2 = (TextView)objetos[58];

        ultimoIdContratoCreado = (String)objetos[59];
        ultimoIdHistorialClinicoCreado = (String)objetos[60];

        etNombreCliente2 = (EditText)objetos[61];

        modificarHistorialClinico = (boolean)objetos[62];
        btnGuardarHistorialNuevo2 = (Button)objetos[63];
        llVisionCercana = (LinearLayout) objetos[64];
        etObservacionesInternoNuevoHistorial2 = (EditText) objetos[65];
        esHistorialGarantia = (boolean) objetos[66];

        etOjoDerechoEsfericoSinConversionNuevoHistorial2 = (EditText) objetos[67];
        etOjoDerechoCilindroSinConversionNuevoHistorial2 = (EditText) objetos[68];
        etOjoDerechoEjeSinConversionNuevoHistorial2 = (EditText) objetos[69];
        etOjoDerechoAddSinConversionNuevoHistorial2 = (EditText) objetos[70];
        etOjoIzquierdoEsfericoSinConversionNuevoHistorial2 = (EditText) objetos[71];
        etOjoIzquierdoCilindroSinConversionNuevoHistorial2 = (EditText) objetos[72];
        etOjoIzquierdoEjeSinConversionNuevoHistorial2 = (EditText) objetos[73];
        etOjoIzquierdoAddSinConversionNuevoHistorial2 = (EditText) objetos[74];
        tvGeneralSinConversionNuevoHistorial2 = (TextView) objetos[75];

        etEmbarazadaNuevoHistorial2 = (EditText) objetos[76];
        etDurmioSeisOchoHorasNuevoHistorial2 = (EditText) objetos[77];
        etActividadDiaNuevoHistorial2 = (EditText) objetos[78];
        etProblemasOjosNuevoHistorial2 = (EditText) objetos[79];
        etOtroBifocalNuevoHistorial2 = (EditText) objetos[80];
        etPrecioBifocalNuevoHistorial2 = (EditText) objetos[81];
        rbOtroBifocal2 = (RadioButton) objetos[82];
        tvOtroBifocalNuevoHistorial2 = (TextView) objetos[83];
        tvPrecioBifocalNuevoHistorial2 = (TextView) objetos[84];
        llColorEstiloTinte2 = (LinearLayout) objetos[85];
        spColorTratamiento2 = (Spinner) objetos[86];
        spEstiloTratamiento2 = (Spinner) objetos[87];
        tvColorTratamiento2 = (TextView) objetos[88];
        tvEstiloTratamiento2 = (TextView) objetos[89];
        cbTratamientoPolarizado2 = (CheckBox) objetos[90];
        llTratamientoPolarizado2 = (LinearLayout) objetos[91];
        spColorTratamientoPolarizado2 = (Spinner) objetos[92];
        tvColorTratamientoPolarizado2 = (TextView) objetos[93];
        cbTratamientoEspejeado2 = (CheckBox) objetos[94];
        llTratamientoEspejeado2 = (LinearLayout) objetos[95];
        spColorTratamientoEspejeado2 = (Spinner) objetos[96];
        tvColorTratamientoEspejeado2 = (TextView) objetos[97];
        cbPolicarbonatoTipo2 = (CheckBox) objetos[98];
        rgTipoBifocalNuevoHistorial2 = (RadioGroup) objetos[99];
        llFotoArmazonPropioHistorial2 = (LinearLayout) objetos[100];
        ivFotoArmazonPropioHistorial2 = (ImageView) objetos[101];
        tvFotoArmazonPropioHistorial2 = (TextView) objetos[102];

        objetosWebService = new Object[]{obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN"), "", ""};

    }

    /*Metodo/Funcion: llenarCamposHistorialBD
     Descripcion: Obtener fecha actual, llenar (spineer paquetes, campos contrato, campos historial clinico) de la BD
   */
    public void llenarCamposHistorialBD() {

        //Obtener FechaActual
        fechaActual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");

        //Llenar spinner paquetes BD
        llenarSpinnerPaqueteBD();
        //Llenar spinner tratamiento
        llenarSpinnerColorEstiloTratamiento();
        //Llenar campos contrato BD
        llenarCamposContrato();
        //Llenar campos historial clinico BD
        llenarCamposHistorial();

        contratoHijo();

    }

    /*Metodo/Funcion: contratoHijo
     Descripcion: Obtener idContratoPadre para poder realizar calculo del total del contrato
   */
    private boolean contratoHijo() {

        idContratoPadre = "";
        boolean valor = false;
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT IDCONTRATORELACION FROM CONTRATOS" +
                    " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                //No hay datos
                Log.i("MENSAJE", "No se encontro el IDCONTRATORELACION el en contrato");
            }

            if (datos.moveToFirst()){
                //Si hay datos
                if(datos.getString(0).length() == 0){
                    //El contrato entrante es un contrato padre
                    idContratoPadre = ultimoIdContratoCreado;
                    valor = false;
                }else{
                    //El contrato entrante es un contrato hijo
                    idContratoPadre = datos.getString(0);
                    valor = true;
                }

            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 140);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return valor;
    }

    /*Metodo/Funcion: validacionPaquete
     Descripcion: Validacion para saber si sera la creacion del segundo historial clinico (Dorado 2) o si es para modificar un historial clinico
   */
    public void validacionPaquete() {

        if(modificarHistorialClinico) { //Modificar historial

            correcto = true;
            Arrays.fill(arregloComponentes, Boolean.FALSE);
            for (int i = 0; i < arregloComponentes.length; i++) {
                mostrarOcultarMensajesError(i, false);
            }

            verificarCheckBoxsRadioButtons();

            if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 1) { //LECTURA
                verificarComponentesDiferentesPaqueteUno();
            }

            if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 2) { //PROTECCION
                //validacionCamposPaqueteDos();
            }

            if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 3) { //ECO JR
                verificarComponentesDiferentesPaqueteUno();
            }

            if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 4) { //JR
                verificarComponentesDiferentesPaqueteUno();
            }

            if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 5) { //DORADO 1
                verificarComponentesDiferentesPaqueteTres();
            }

            if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 6) { //DORADO 2
                verificarComponentesDiferentesPaqueteUno();
            }

            if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 7) { //PLATINO
                verificarComponentesDiferentesPaqueteTres();
            }

            if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 8) { //PREMIUM
                verificarComponentesDiferentesPaquetePremium();
            }
            validacionCamposPaqueteModificar();

        }else { //Creacion de nuevo historial paquete Dorado 2

            correcto = true;
            Arrays.fill(arregloComponentes, Boolean.FALSE);
            for (int i = 0; i < arregloComponentes.length; i++) {
                mostrarOcultarMensajesError(i, false);
            }

            validacionCamposPaqueteDorado2();

        }

    }

    /*Metodo/Funcion: validacionCamposPaqueteModificar
     Descripcion: Validacion de que por lo menos un tratamiento haya sido seleccionado
   */
    private void validacionCamposPaqueteModificar() {

        for(int i=0; i < arregloComponentes.length; i++){
            if(arregloComponentes[i]){
                mostrarOcultarMensajesError(i,true);
            }

            boolean valido = validarArreglo(i);
            if(!valido){
                correcto = false;
            }

        }

        if(correcto){
            //Se selecciono al menos un tratamiento
            mostrarAlertDialogConfirmacion();
        }else{
            //No se selecciono ningun tratamiento
            Toast.makeText(fragmento.getContext(),"Uno o mas campos vacios", Toast.LENGTH_LONG).show();
        }

    }

    private void mostrarAlertDialogConfirmacion() {

        String mensaje = "";
        if(esHistorialGarantia) {
            if(!global.obtenerAtributoTabla("HISTORIALCLINICO", "TIPO", "ID", ultimoIdHistorialClinicoCreado).equals("1")) {
                mensaje = "<font color='#000000'>¿Crear garantía del historial clínico?</font>" +
                        "<font color='#FFACA6'><br><br><b>Recuerda que al ser garantia solo se deben enviar las micas.</b></font>";
            }else {
                mensaje = "<font color='#000000'>¿Actualizar garantía del historial clínico?</font>";
            }
        }else {
            mensaje = "<font color='#000000'>¿Actualizar historial clínico?</font>";
        }

        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Confirmación").setMessage(Html.fromHtml(mensaje))
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mostrarAlertDialogCambioPaqueteDorado1APlatinoOViseversa();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();

    }

    private void mostrarAlertDialogCambioPaqueteDorado1APlatinoOViseversa() {

        String mensaje = "";
        boolean mostrarAlerta = false;
        confirmacionAlertaCambioPaqueteDorado1PlatinooViserversa = false;

        String mensajeCambioPaquetesMovimiento = "";

        //Cambio de paquete de Dorado 1 a Platino
        if(idPaqueteSeleccionado.equals("5") && (Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial2.getText().toString()) > 3.25
                || Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString()) > 3.25
                || Double.parseDouble(etOjoDerechoAddNuevoHistorial2.getText().toString()) > 3.25
                || Double.parseDouble(etOjoIzquierdoAddNuevoHistorial2.getText().toString()) > 3.25
                || Double.parseDouble(etOjoDerechoCilindroNuevoHistorial2.getText().toString()) != 0
                || Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial2.getText().toString()) != 0)) {
            mensaje = "De acuerdo al historial se recomienda actualizar el paquete de Dorado 1 a Platino";
            mensajeCambioPaquetesMovimiento = "Dorado 1 a Platino";
            mostrarAlerta = true;
        }

        //Cambio de paquete de Platino a Dorado 1
        if(idPaqueteSeleccionado.equals("7") && (Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial2.getText().toString()) <= 3.25
                && Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString()) <= 3.25
                && Double.parseDouble(etOjoDerechoAddNuevoHistorial2.getText().toString()) <= 3.25
                && Double.parseDouble(etOjoIzquierdoAddNuevoHistorial2.getText().toString()) <= 3.25
                && Double.parseDouble(etOjoDerechoCilindroNuevoHistorial2.getText().toString()) == 0
                && Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial2.getText().toString()) == 0)) {
            mensaje = "De acuerdo al historial se recomienda actualizar el paquete de Platino a Dorado 1";
            mensajeCambioPaquetesMovimiento = "Platino a Dorado 1";
            mostrarAlerta = true;
        }

        if(mostrarAlerta) {
            //Se va a ser cambio de paquete (Dorado 1 a Platino/Platino a Dorado 1) por lo que se le pide autorizacion
            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            String finalMensajeCambioPaquetesMovimiento = mensajeCambioPaquetesMovimiento;
            alerta.setTitle("Confirmación").setMessage(Html.fromHtml(mensaje + "<br><br>" +
                            "<font color='#FFACA6'><b>¿Deseas hacer el cambio?</b></font>"))
                    .setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmacionAlertaCambioPaqueteDorado1PlatinooViserversa = true;
                            if(esHistorialGarantia) {
                                //Es garantia
                                if (!global.obtenerAtributoTabla("HISTORIALCLINICO", "TIPO", "ID", ultimoIdHistorialClinicoCreado).equals("1")) {
                                    //Al crear garantia
                                    historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                            "La sugerencia de cambio de paquete de " + finalMensajeCambioPaquetesMovimiento + " fue aceptada en la creación de garantía", "0");
                                }else {
                                    //Al editar garantia
                                    historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                            "La sugerencia de cambio de paquete de " + finalMensajeCambioPaquetesMovimiento + " fue aceptada en la edición de garantía", "0");
                                }
                            }else {
                                //No es garantia
                                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                        "La sugerencia de cambio de paquete de " + finalMensajeCambioPaquetesMovimiento + " fue aceptada en la edición", "0");
                            }
                            actualizarHistorialClinicoCampos();
                            validacionTratamientosPaquetes();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(esHistorialGarantia) {
                                //Es garantia
                                if (!global.obtenerAtributoTabla("HISTORIALCLINICO", "TIPO", "ID", ultimoIdHistorialClinicoCreado).equals("1")) {
                                    //Al crear garantia
                                    historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                            "La sugerencia de cambio de paquete de " + finalMensajeCambioPaquetesMovimiento + " fue rechazada en la creación de garantía", "0");
                                }else {
                                    //Al editar garantia
                                    historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                            "La sugerencia de cambio de paquete de " + finalMensajeCambioPaquetesMovimiento + " fue rechazada en la edición de garantía", "0");
                                }
                            }else {
                                //No es garantia
                                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                        "La sugerencia de cambio de paquete de " + finalMensajeCambioPaquetesMovimiento + " fue rechazada en la edición", "0");
                            }
                            actualizarHistorialClinicoCampos();
                            validacionTratamientosPaquetes();
                        }
                    }).show();

        }else {
            //No se hara ningun cambio de paquete (Dorado 1 a Platino/Platino a Dorado 1)
            actualizarHistorialClinicoCampos();
            validacionTratamientosPaquetes();
        }
    }

    private void actualizarHistorialClinicoCampos() {

        if(precioMaterialOtro > 0) {
            //Ya habia sido seleccionado otro material
            if(rbOtroMaterial2.isChecked()) {
                //Sigue seleccionado otro material
                if(precioMaterialOtro == Double.parseDouble(etPrecioMaterialNuevoHistorial2.getText().toString())) {
                    //No se suma ni resta nada
                    precioMaterialOtro = 0;
                }else if(Double.parseDouble(etPrecioMaterialNuevoHistorial2.getText().toString()) > precioMaterialOtro) {
                    //El precio que se puso actual es mayor a lo que estaba anteriormente (Se suma el restante de lo que se puso actual)
                    precioMaterialOtro = Double.parseDouble(etPrecioMaterialNuevoHistorial2.getText().toString()) - precioMaterialOtro;
                }else {
                    //El precio que se puso actual es menor a lo que estaba anteriormente (Se resta el restante de lo que estaba anteriormente)
                    precioMaterialOtro = precioMaterialOtro - Double.parseDouble(etPrecioMaterialNuevoHistorial2.getText().toString());
                    precioMaterialOtro = (precioMaterialOtro * -1);
                }
            }else {
                //Se deselecciona otro material (Se resta el precio)
                precioMaterialOtro = (precioMaterialOtro * -1);
            }
        }else {
            //No habia sido seleccionado otro material
            if(rbOtroMaterial2.isChecked()) {
                //Se selecciona otro material (Se suma el precio)
                precioMaterialOtro = Double.parseDouble(etPrecioMaterialNuevoHistorial2.getText().toString());
            }
        }

        //Validar material Policarbonato
        if (policarbonatoBD){
            //Ya se habia seleccionado policarbonato
            if(rbPolicarbonato2.isChecked()){
                //Esta aun seleccionado material policarbonato
                if(precioMaterial > 0){
                    //Es policarbonato para adulto
                    if(cbPolicarbonatoTipo2.isChecked()){
                        //Se selecciono policarbonato para niño - Descontar precio de policrabonato
                        precioMaterial = (precioPolicarbonato * -1);
                    }else{
                        precioMaterial = 0;
                    }
                }else{
                    //Es policarbonato para niño
                    if(!cbPolicarbonatoTipo2.isChecked()){
                        //Se quito check box de policarbonato para niño - Sumar precio de policarbonato
                        precioMaterial = precioPolicarbonato;
                    }
                }

            }else{
                //No se selecciono rbPolicarbonato
                if(precioMaterial > 0){
                    //Estaba seleccionado antes
                    precioMaterial = (precioPolicarbonato * -1);
                }else{
                    //No estaba seleccionado
                    precioMaterial = 0;
                }
            }
        }else{
            //No habia sido seleccionado
            if(rbPolicarbonato2.isChecked()){
                //Se selecciono policarbonato
                if(!cbPolicarbonatoTipo2.isChecked()){
                    //Es opcion de policarbonato adulto - Sumar precio de policarbonato
                    precioMaterial = precioPolicarbonato;
                }
            }
        }

        if(precioBifocalOtro > 0) {
            //Ya habia sido seleccionado otro bifocal
            if(rbOtroBifocal2.isChecked()) {
                //Sigue seleccionado otro bifocal
                if(precioBifocalOtro == Double.parseDouble(etPrecioBifocalNuevoHistorial2.getText().toString())) {
                    //No se suma ni resta nada
                    precioBifocalOtro = 0;
                }else if(Double.parseDouble(etPrecioBifocalNuevoHistorial2.getText().toString()) > precioBifocalOtro) {
                    //El precio que se puso actual es mayor a lo que estaba anteriormente (Se suma el restante de lo que se puso actual)
                    precioBifocalOtro = Double.parseDouble(etPrecioBifocalNuevoHistorial2.getText().toString()) - precioBifocalOtro;
                }else {
                    //El precio que se puso actual es menor a lo que estaba anteriormente (Se resta el restante de lo que estaba anteriormente)
                    precioBifocalOtro = precioBifocalOtro - Double.parseDouble(etPrecioBifocalNuevoHistorial2.getText().toString());
                    precioBifocalOtro = (precioBifocalOtro * -1);
                }
            }else {
                //Se deselecciona otro bifocal (Se resta el precio)
                precioBifocalOtro = (precioBifocalOtro * -1);
            }
        }else {
            //No habia sido seleccionado otro bifocal
            if(rbOtroBifocal2.isChecked()) {
                //Se selecciona otro bifocal (Se suma el precio)
                precioBifocalOtro = Double.parseDouble(etPrecioBifocalNuevoHistorial2.getText().toString());
            }
        }

        if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 1) { //LECTURA
            if(precioCilindroLectura > 0) {
                //Ya habia sido sumado el precio del cilindro
                if(Double.parseDouble(etOjoDerechoCilindroNuevoHistorial2.getText().toString()) != 0 || Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial2.getText().toString()) != 0
                    || Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial2.getText().toString()) > 5.25
                    || Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString()) > 5.25) {
                    //Sigue estando igual los valores en los cilindros (No se hace nada)
                    precioCilindroLectura = 0;
                }else {
                    //Cambian los valores de cilindros por 0 (Se resta el precio del cilindro)
                    precioCilindroLectura = (precioCilindroLectura * -1);
                }
            }else {
                //No habia sido sumado el precio del cilindro
                if(Double.parseDouble(etOjoDerechoCilindroNuevoHistorial2.getText().toString()) != 0 || Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial2.getText().toString()) != 0
                        || Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial2.getText().toString()) > 5.25
                        || Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString()) > 5.25) {
                    //Los valores en los cilindros son diferentes a 0 (Se suma el precio del cilindro)
                    precioCilindroLectura = 590;
                }
            }
        }

        if (confirmacionAlertaCambioPaqueteDorado1PlatinooViserversa) {

            //Cambio de paquete de Dorado 1 a Platino
            if (idPaqueteSeleccionado.equals("5") && (Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial2.getText().toString()) > 3.25
                    || Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString()) > 3.25
                    || Double.parseDouble(etOjoDerechoAddNuevoHistorial2.getText().toString()) > 3.25
                    || Double.parseDouble(etOjoIzquierdoAddNuevoHistorial2.getText().toString()) > 3.25
                    || Double.parseDouble(etOjoDerechoCilindroNuevoHistorial2.getText().toString()) != 0
                    || Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial2.getText().toString()) != 0)) {
                idPaqueteSeleccionado = "7";
                precioAumentoORestaDorado1OPlatino = 700;
            }

            //Cambio de paquete de Platino a Dorado 1
            if (idPaqueteSeleccionado.equals("7") && (Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial2.getText().toString()) <= 3.25
                    && Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString()) <= 3.25
                    && Double.parseDouble(etOjoDerechoAddNuevoHistorial2.getText().toString()) <= 3.25
                    && Double.parseDouble(etOjoIzquierdoAddNuevoHistorial2.getText().toString()) <= 3.25
                    && Double.parseDouble(etOjoDerechoCilindroNuevoHistorial2.getText().toString()) == 0
                    && Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial2.getText().toString()) == 0)) {
                idPaqueteSeleccionado = "5";
                precioAumentoORestaDorado1OPlatino = -700;
            }

        }

        int valorMaterial = 0;
        int valorBifocal = 0;
        int valorPolicatbonatoTipo = 0;

        //Validacion de RadioButton de Material
        if (rbHiIndex2.isChecked()) {
            valorMaterial = 0;
        } else
            if (rbCR2.isChecked()){
                valorMaterial = 1;
            }else
                if(rbPolicarbonato2.isChecked()) {
                    valorMaterial = 2;
                    if(cbPolicarbonatoTipo2.isChecked()){
                        valorPolicatbonatoTipo = 1;
                    }
                }else
                    if(rbOtroMaterial2.isChecked()) {
                        valorMaterial = 3;
                    }

        //Validacion de RadioButton de Bifocal
        if (rbFT2.isChecked()) {
            valorBifocal = 0;
        } else
            if (rbBlend2.isChecked()){
                valorBifocal = 1;
            }else
                if(rbProgresivo2.isChecked()) {
                    valorBifocal = 2;
                }else
                    if(rbNA2.isChecked()) {
                        valorBifocal = 3;
                    }else
                        if(rbOtroBifocal2.isChecked()) {
                            valorBifocal = 4;
                        }

        //Validacion de valores en ojo Derecho e Izquierdo
        String ojoDerechoEsferico2 = etOjoDerechoEsfericoNuevoHistorial2.getText().toString();
        String ojoDerechoCilindro2 = etOjoDerechoCilindroNuevoHistorial2.getText().toString();
        String ojoDerechoEje2 = etOjoDerechoEjeNuevoHistorial2.getText().toString();
        String ojoDerechoAdd2 = etOjoDerechoAddNuevoHistorial2.getText().toString();
        String ojoDerechoAlt2 = etOjoDerechoALTNuevoHistorial2.getText().toString();

        String ojoIzquierdoEsferico2 = etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString();
        String ojoIzquierdoCilindro2 = etOjoIzquierdoCilindroNuevoHistorial2.getText().toString();
        String ojoIzquierdoEje2 = etOjoIzquierdoEjeNuevoHistorial2.getText().toString();
        String ojoIzquierdoAdd2 = etOjoIzquierdoAddNuevoHistorial2.getText().toString();
        String ojoIzquierdoAlt2 = etOjoIzquierdoALTNuevoHistorial2.getText().toString();

        //OJO DERECHO
        //ojoDerechoEsferico
        if(ojoDerechoEsferico2.startsWith(".")) {
            ojoDerechoEsferico2 = "0" + ojoDerechoEsferico2;
        }

        if(ojoDerechoEsferico2.endsWith(".")) {
            ojoDerechoEsferico2 = ojoDerechoEsferico2 + "0";
        }

        if (ojoDerechoEsferico2.length() > 0) {
            if (Double.parseDouble(ojoDerechoEsferico2) > 0) {
                ojoDerechoEsferico2 = ojoDerechoEsferico2.replace("+", "");
                ojoDerechoEsferico2 = "+" + ojoDerechoEsferico2;
            }
        }

        //ojoDerechoCilindro
        if(ojoDerechoCilindro2.startsWith(".")) {
            ojoDerechoCilindro2 = "0" + ojoDerechoCilindro2;
        }

        if(ojoDerechoCilindro2.endsWith(".")) {
            ojoDerechoCilindro2 = ojoDerechoCilindro2 + "0";
        }

        if (ojoDerechoCilindro2.length() > 0) {
            if (Double.parseDouble(ojoDerechoCilindro2) > 0) {
                ojoDerechoCilindro2 = ojoDerechoCilindro2.replace("+", "");
                ojoDerechoCilindro2 = "-" + ojoDerechoCilindro2;
            }
        }

        //ojoDerechoEje
        if(ojoDerechoEje2.startsWith(".")) {
            ojoDerechoEje2 = "0" + ojoDerechoEje2;
        }

        if(ojoDerechoEje2.endsWith(".")) {
            ojoDerechoEje2 = ojoDerechoEje2 + "0";
        }

        //ojoDerechoAdd
        if(ojoDerechoAdd2.startsWith(".")) {
            ojoDerechoAdd2 = "0" + ojoDerechoAdd2;
        }

        if(ojoDerechoAdd2.endsWith(".")) {
            ojoDerechoAdd2 = ojoDerechoAdd2 + "0";
        }

        if (ojoDerechoAdd2.length() > 0) {
            if (Double.parseDouble(ojoDerechoAdd2) > 0) {
                ojoDerechoAdd2 = ojoDerechoAdd2.replace("+", "");
                ojoDerechoAdd2 = "+" + ojoDerechoAdd2;
            }
        }

        //ojoDerechoAlt
        if(ojoDerechoAlt2.startsWith(".")) {
            ojoDerechoAlt2 = "0" + ojoDerechoAlt2;
        }

        if(ojoDerechoAlt2.endsWith(".")) {
            ojoDerechoAlt2 = ojoDerechoAlt2 + "0";
        }

        //OJO IZQUIERDO
        //ojoIzquierdoEsferico
        if(ojoIzquierdoEsferico2.startsWith(".")) {
            ojoIzquierdoEsferico2 = "0" + ojoIzquierdoEsferico2;
        }

        if(ojoIzquierdoEsferico2.endsWith(".")) {
            ojoIzquierdoEsferico2 = ojoIzquierdoEsferico2 + "0";
        }

        if (ojoIzquierdoEsferico2.length() > 0) {
            if (Double.parseDouble(ojoIzquierdoEsferico2) > 0) {
                ojoIzquierdoEsferico2 = ojoIzquierdoEsferico2.replace("+", "");
                ojoIzquierdoEsferico2 = "+" + ojoIzquierdoEsferico2;
            }
        }

        //ojoIzquierdoCilindro
        if(ojoIzquierdoCilindro2.startsWith(".")) {
            ojoIzquierdoCilindro2 = "0" + ojoIzquierdoCilindro2;
        }

        if(ojoIzquierdoCilindro2.endsWith(".")) {
            ojoIzquierdoCilindro2 = ojoIzquierdoCilindro2 + "0";
        }

        if (ojoIzquierdoCilindro2.length() > 0) {
            if (Double.parseDouble(ojoIzquierdoCilindro2) > 0) {
                ojoIzquierdoCilindro2 = ojoIzquierdoCilindro2.replace("+", "");
                ojoIzquierdoCilindro2 = "-" + ojoIzquierdoCilindro2;
            }
        }

        //ojoIzquierdoEje
        if(ojoIzquierdoEje2.startsWith(".")) {
            ojoIzquierdoEje2 = "0" + ojoIzquierdoEje2;
        }

        if(ojoIzquierdoEje2.endsWith(".")) {
            ojoIzquierdoEje2 = ojoIzquierdoEje2 + "0";
        }

        //ojoIzquierdoAdd
        if(ojoIzquierdoAdd2.startsWith(".")) {
            ojoIzquierdoAdd2 = "0" + ojoIzquierdoAdd2;
        }

        if(ojoIzquierdoAdd2.endsWith(".")) {
            ojoIzquierdoAdd2 = ojoIzquierdoAdd2 + "0";
        }

        if (ojoIzquierdoAdd2.length() > 0) {
            if (Double.parseDouble(ojoIzquierdoAdd2) > 0) {
                ojoIzquierdoAdd2 = ojoIzquierdoAdd2.replace("+", "");
                ojoIzquierdoAdd2 = "+" + ojoIzquierdoAdd2;
            }
        }

        //ojoIzquierdoAlt
        if(ojoIzquierdoAlt2.startsWith(".")) {
            ojoIzquierdoAlt2 = "0" + ojoIzquierdoAlt2;
        }

        if(ojoIzquierdoAlt2.endsWith(".")) {
            ojoIzquierdoAlt2 = ojoIzquierdoAlt2 + "0";
        }

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try {

            if(esHistorialGarantia) {
                //Es historial con garantia

                if(global.obtenerAtributoTabla("HISTORIALCLINICO", "TIPO", "ID", ultimoIdHistorialClinicoCreado).equals("1")) {
                    //Es historial de garantia a modificar

                    SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

                    String consulta = "UPDATE HISTORIALCLINICO SET FECHAENTREGA = '" + etFechaEntregaNuevoHistorial2.getText().toString() + "', ESFERICODER='" +
                            global.replaceCadena(ojoDerechoEsferico2) + "', CILINDRODER='" + global.replaceCadena(ojoDerechoCilindro2)
                            + "', EJEDER='" + global.replaceCadena(ojoDerechoEje2) + "', ADDDER='" + global.replaceCadena(ojoDerechoAdd2)
                            + "', ALTDER='" + global.replaceCadena(ojoDerechoAlt2) + "', ESFERICOIZQ='" + global.replaceCadena(ojoIzquierdoEsferico2)
                            + "', CILINDROIZQ='" + global.replaceCadena(ojoIzquierdoCilindro2) + "', EJEIZQ='" + global.replaceCadena(ojoIzquierdoEje2)
                            + "', ADDIZQ='" + global.replaceCadena(ojoIzquierdoAdd2) + "', ALTIZQ='" + global.replaceCadena(ojoIzquierdoAlt2)
                            + "', MATERIAL='" + valorMaterial + "', MATERIALOTRO='" + global.replaceCadena(etOtroMaterialNuevoHistorial2.getText().toString())
                            + "', COSTOMATERIAL='" + global.replaceCadena(etPrecioMaterialNuevoHistorial2.getText().toString()) + "', BIFOCAL='" + valorBifocal
                            + "', OBSERVACIONES='" + global.replaceCadena(etObservacionesNuevoHistorial2.getText().toString()) + "', OBSERVACIONESINTERNO='" +
                            global.replaceCadena(etObservacionesInternoNuevoHistorial2.getText().toString())
                            + "', BIFOCALOTRO='" + global.replaceCadena(etOtroBifocalNuevoHistorial2.getText().toString()) + "', COSTOBIFOCAL='" +
                            global.replaceCadena(etPrecioBifocalNuevoHistorial2.getText().toString())
                            + "', ID_PAQUETE='" + idPaqueteSeleccionado + "', POLICARBONATOTIPO='" + valorPolicatbonatoTipo + "', UPDATED_AT='" + fechaActual + " " + time
                            + "' WHERE ID = '" + ultimoIdHistorialClinicoCreado + "' AND ID_CONTRATO='" + ultimoIdContratoCreado + "'";

                    sqLiteDB2.execSQL(consulta);
                    sqLiteDB2.close();

                    //Guardar en historial de movimientos
                    historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                            "Se actualizo el historial clinico", "0");

                    //Actualizar historialsinconversion
                    if(idPaqueteSeleccionado.equals("1") || idPaqueteSeleccionado.equals("6")) {
                        //LECTURA O DORADO2
                        actualizarHistorialesOHistorialSinConversion(ultimoIdContratoCreado, ultimoIdHistorialClinicoCreado);
                    }

                }else {
                    //Es historial de garantia a crear

                    //Obtener idAlfanumerico
                    String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("HISTORIALCLINICO", 5);
                    SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    valores.put("ID", idAlfanumerico);
                    valores.put("ID_CONTRATO", ultimoIdContratoCreado);
                    valores.put("EDAD", etEdadNuevoHistorial2.getText().toString());
                    valores.put("FECHAENTREGA", etFechaEntregaNuevoHistorial2.getText().toString());
                    valores.put("DIAGNOSTICO ", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDiagnosticoNuevoHistorial2.getText().toString())));
                    valores.put("OCUPACION", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etOcupacionNuevoHistorial2.getText().toString())));
                    valores.put("DIABETES", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDiabetesNuevoHistorial2.getText().toString())));
                    valores.put("HIPERTENSION", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etHipertensionNuevoHistorial2.getText().toString())));
                    valores.put("DOLOR", cbDolorCabeza2.isChecked() ? "1" : "0");
                    valores.put("ARDOR", cbArdorOjos2.isChecked() ? "1" : "0");
                    valores.put("GOLPEOJOS", cbGolpeCabeza2.isChecked() ? "1" : "0");
                    valores.put("OTROM", cbOtroMolestia2.isChecked() ? "1" : "0");
                    valores.put("MOLESTIAOTRO", global.replaceCadena(etOtroMoletiaNuevoHistorial2.getText().toString()));
                    valores.put("ULTIMOEXAMEN", etUltimoExamenNuevoHistorial2.getText().toString());
                    valores.put("ESFERICODER", global.replaceCadena(ojoDerechoEsferico2));
                    valores.put("CILINDRODER", global.replaceCadena(ojoDerechoCilindro2));
                    valores.put("EJEDER", global.replaceCadena(ojoDerechoEje2));
                    valores.put("ADDDER", global.replaceCadena(ojoDerechoAdd2));
                    valores.put("ALTDER", global.replaceCadena(ojoDerechoAlt2));
                    valores.put("ESFERICOIZQ", global.replaceCadena(ojoIzquierdoEsferico2));
                    valores.put("CILINDROIZQ", global.replaceCadena(ojoIzquierdoCilindro2));
                    valores.put("EJEIZQ", global.replaceCadena(ojoIzquierdoEje2));
                    valores.put("ADDIZQ", global.replaceCadena(ojoIzquierdoAdd2));
                    valores.put("ALTIZQ", global.replaceCadena(ojoIzquierdoAlt2));
                    valores.put("ID_PRODUCTO", idProductoGarantia);
                    valores.put("ID_PAQUETE", idPaqueteSeleccionado);
                    if (rbHiIndex2.isChecked()) {
                        valores.put("MATERIAL", rbHiIndex2.isChecked() ? "0" : "");
                    } else
                    if (rbCR2.isChecked()){
                        valores.put("MATERIAL", rbCR2.isChecked() ? "1" : "");
                    }else
                    if(rbPolicarbonato2.isChecked()) {
                        valores.put("MATERIAL", rbPolicarbonato2.isChecked() ? "2" : "");
                        valores.put("POLICARBONATOTIPO", cbPolicarbonatoTipo2.isChecked() ? "1" : "0");
                    }else
                    if(rbOtroMaterial2.isChecked()) {
                        valores.put("MATERIAL", rbOtroMaterial2.isChecked() ? "3" : "");
                    }
                    valores.put("MATERIALOTRO", global.replaceCadena(etOtroMaterialNuevoHistorial2.getText().toString()));
                    valores.put("COSTOMATERIAL", etPrecioMaterialNuevoHistorial2.getText().toString());
                    if (rbFT2.isChecked()) {
                        valores.put("BIFOCAL", rbFT2.isChecked() ? "0" : "");
                    } else
                    if (rbBlend2.isChecked()){
                        valores.put("BIFOCAL", rbBlend2.isChecked() ? "1" : "");
                    }else
                    if(rbProgresivo2.isChecked()) {
                        valores.put("BIFOCAL", rbProgresivo2.isChecked() ? "2" : "");
                    }else
                    if(rbNA2.isChecked()) {
                        valores.put("BIFOCAL", rbNA2.isChecked() ? "3" : "");
                    }else
                    if(rbOtroBifocal2.isChecked()) {
                        valores.put("BIFOCAL", rbOtroBifocal2.isChecked() ? "4" : "");
                    }
                    valores.put("FOTOCROMATICO", cbTratamientoFotocromatico2.isChecked() ? "1" : "0");
                    valores.put("AR", cbTratamientoAR2.isChecked() ? "1" : "0");
                    valores.put("TINTE", cbTratamientoTinte2.isChecked() ? "1" : "0");
                    valores.put("BLUERAY", cbTratamientoBlueray2.isChecked() ? "1" : "0");
                    valores.put("OTROT", cbTratamientoOtro2.isChecked() ? "1" : "0");
                    valores.put("TRATAMIENTOOTRO", global.replaceCadena(etOtroTratamientoNuevoHistorial2.getText().toString()));
                    valores.put("COSTOTRATAMIENTO", etPrecioTratamientoNuevoHistorial2.getText().toString());
                    valores.put("OBSERVACIONES", global.replaceCadena(etObservacionesNuevoHistorial2.getText().toString()));
                    valores.put("OBSERVACIONESINTERNO", global.replaceCadena(etObservacionesInternoNuevoHistorial2.getText().toString()));
                    valores.put("TIPO", "1");
                    valores.put("BIFOCALOTRO", global.replaceCadena(etOtroBifocalNuevoHistorial2.getText().toString()));
                    valores.put("COSTOBIFOCAL", etPrecioBifocalNuevoHistorial2.getText().toString());
                    valores.put("EMBARAZADA", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etEmbarazadaNuevoHistorial2.getText().toString())));
                    valores.put("DURMIOSEISOCHOHORAS", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDurmioSeisOchoHorasNuevoHistorial2.getText().toString())));
                    valores.put("ACTIVIDADDIA", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etActividadDiaNuevoHistorial2.getText().toString())));
                    valores.put("PROBLEMASOJOS", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etProblemasOjosNuevoHistorial2.getText().toString())));
                    valores.put("ID_TRATAMIENTOCOLORTINTE", cbTratamientoTinte2.isChecked() ? idsColorTratamientoTinte[spColorTratamiento2.getSelectedItemPosition()]: "");
                    valores.put("ESTILOTINTE", cbTratamientoTinte2.isChecked() ? idsEstiloTratamientoTinte[spEstiloTratamiento2.getSelectedItemPosition()]: "");
                    valores.put("POLICARBONATOTIPO", rbPolicarbonato2.isChecked() ? cbPolicarbonatoTipo2.isChecked() ? "1" : "0": "");
                    valores.put("POLARIZADO", cbTratamientoPolarizado2.isChecked() ? "1" : "0");
                    valores.put("ID_TRATAMIENTOCOLORPOLARIZADO", cbTratamientoPolarizado2.isChecked() ?  idsColorTratamientoPolarizado[spColorTratamientoPolarizado2.getSelectedItemPosition()] : "");
                    valores.put("ESPEJO", cbTratamientoEspejeado2.isChecked() ? "1" : "0");
                    valores.put("ID_TRATAMIENTOCOLORESPEJO", cbTratamientoEspejeado2.isChecked() ?  idsColorTratamientoEspejo[spColorTratamientoEspejeado2.getSelectedItemPosition()] : "");
                    valores.put("ENVIADOPAGINA", "0");
                    valores.put("CREATED_AT", fechaActual + time);
                    valores.put("UPDATED_AT", fechaActual + time);
                    sqLiteDB2.insert("HISTORIALCLINICO",null, valores);
                    sqLiteDB2.close();

                    Toast.makeText(fragmento.getContext(), "Se creo correctamente la garantía", Toast.LENGTH_LONG).show();

                    //Guardar en historial de movimientos
                    historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                            "Se creo una garantía al historial clinico " + ultimoIdHistorialClinicoCreado, "0");

                    //Actualizar garantia
                    actualizarHistorialTablaGarantias(idAlfanumerico);

                    //Actualizar el estado del contrato a TERMINADO y quitar la bandera de entregaproducto y fechaentrega
                    if(global.contadorContratosOHistorialesTablaGarantias(ultimoIdContratoCreado, "", false) == global.contadorHistorialesConGarantia(ultimoIdContratoCreado, false)) {
                        //Contador de garantias es igual a historiales con garantias creados
                        global.actualizarAtributoTabla("CONTRATOS", "UPDATED_AT", fechaActual + time, "ID_CONTRATO", ultimoIdContratoCreado);
                        global.actualizarAtributoTabla("CONTRATOS", "ESTATUS_ESTADOCONTRATO", "1", "ID_CONTRATO", ultimoIdContratoCreado);
                    }

                    //Guadar historialsinconversion
                    if(idPaqueteSeleccionado.equals("1") || idPaqueteSeleccionado.equals("6")) {
                        //LECTURA o DORADO 2
                        guardarHistorialSinConversion(ultimoIdContratoCreado, idAlfanumerico);
                    }

                    //Guardar nuevo historial clinico en archivo historialesclinicos.txt
                    global.escribirNuevoContratoOHistorialClinicoEnArchivoTXT(fechaActual, ultimoIdContratoCreado, idAlfanumerico, 2);

                }

                if(precioAumentoORestaDorado1OPlatino < 0) {
                    precioAumentoORestaDorado1OPlatino = 0;
                }
                if(precioMaterialOtro < 0) {
                    precioMaterialOtro = 0;
                }
                if(precioCilindroLectura < 0) {
                    precioCilindroLectura = 0;
                }
                if(precioBifocalOtro < 0) {
                    precioBifocalOtro = 0;
                }
                if(precioMaterial < 0){
                    precioMaterial = 0;
                }

                double totalhistorial = Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS", "TOTALHISTORIAL",
                        "ID_CONTRATO", ultimoIdContratoCreado)) + precioAumentoORestaDorado1OPlatino;
                actualizarPrecioTotalContrato("TOTALHISTORIAL", totalhistorial + (precioMaterialOtro + precioMaterial + precioCilindroLectura + precioBifocalOtro));
                if(global.obtenerAtributoTabla("CONTRATOS", "PROMOCIONTERMINADA",
                        "ID_CONTRATO", ultimoIdContratoCreado).equals("1")) {
                    double totalpromocion = Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS", "TOTALPROMOCION",
                            "ID_CONTRATO", ultimoIdContratoCreado)) + precioAumentoORestaDorado1OPlatino;
                    actualizarPrecioTotalContrato("TOTALPROMOCION", totalpromocion + (precioMaterialOtro + precioMaterial + precioCilindroLectura + precioBifocalOtro));
                }

                //Actualizar TOTALREAL
                double totalreal = Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS", "TOTALREAL",
                        "ID_CONTRATO", ultimoIdContratoCreado)) + precioAumentoORestaDorado1OPlatino;
                global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL",
                        String.valueOf(totalreal + (precioMaterialOtro + precioMaterial + precioCilindroLectura + precioBifocalOtro)), "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL

            }else {
                //Es historial sin garantia a modificar

                SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

                String consulta = "UPDATE HISTORIALCLINICO SET FECHAENTREGA = '" + etFechaEntregaNuevoHistorial2.getText().toString() + "', ESFERICODER='" +
                        global.replaceCadena(ojoDerechoEsferico2) + "', CILINDRODER='" + global.replaceCadena(ojoDerechoCilindro2)
                        + "', EJEDER='" + global.replaceCadena(ojoDerechoEje2) + "', ADDDER='" + global.replaceCadena(ojoDerechoAdd2)
                        + "', ALTDER='" + global.replaceCadena(ojoDerechoAlt2) + "', ESFERICOIZQ='" + global.replaceCadena(ojoIzquierdoEsferico2)
                        + "', CILINDROIZQ='" + global.replaceCadena(ojoIzquierdoCilindro2) + "', EJEIZQ='" + global.replaceCadena(ojoIzquierdoEje2)
                        + "', ADDIZQ='" + global.replaceCadena(ojoIzquierdoAdd2) + "', ALTIZQ='" + global.replaceCadena(ojoIzquierdoAlt2)
                        + "', MATERIAL='" + valorMaterial + "', MATERIALOTRO='" + global.replaceCadena(etOtroMaterialNuevoHistorial2.getText().toString())
                        + "', COSTOMATERIAL='" + global.replaceCadena(etPrecioMaterialNuevoHistorial2.getText().toString()) + "', BIFOCAL='" + valorBifocal
                        + "', OBSERVACIONES='" + global.replaceCadena(etObservacionesNuevoHistorial2.getText().toString()) + "', OBSERVACIONESINTERNO='" +
                        global.replaceCadena(etObservacionesInternoNuevoHistorial2.getText().toString())
                        + "', BIFOCALOTRO='" + global.replaceCadena(etOtroBifocalNuevoHistorial2.getText().toString()) + "', COSTOBIFOCAL='" +
                        global.replaceCadena(etPrecioBifocalNuevoHistorial2.getText().toString())
                        + "', ID_PAQUETE='" + idPaqueteSeleccionado + "', POLICARBONATOTIPO='" + valorPolicatbonatoTipo + "', UPDATED_AT='" + fechaActual + " " + time
                        + "' WHERE ID = '" + ultimoIdHistorialClinicoCreado + "' AND ID_CONTRATO='" + ultimoIdContratoCreado + "'";

                sqLiteDB2.execSQL(consulta);
                sqLiteDB2.close();

                double totalhistorial = Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS", "TOTALHISTORIAL",
                        "ID_CONTRATO", ultimoIdContratoCreado)) + precioAumentoORestaDorado1OPlatino;
                actualizarPrecioTotalContrato("TOTALHISTORIAL", totalhistorial + (precioMaterialOtro + precioMaterial + precioCilindroLectura + precioBifocalOtro));
                if(global.obtenerAtributoTabla("CONTRATOS", "PROMOCIONTERMINADA",
                        "ID_CONTRATO", ultimoIdContratoCreado).equals("1")) {
                    double totalpromocion = Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS", "TOTALPROMOCION",
                            "ID_CONTRATO", ultimoIdContratoCreado)) + precioAumentoORestaDorado1OPlatino;
                    actualizarPrecioTotalContrato("TOTALPROMOCION", totalpromocion + (precioMaterialOtro + precioMaterial + precioCilindroLectura + precioBifocalOtro));
                }

                //Actualizar TOTALREAL
                double totalreal = Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS", "TOTALREAL",
                        "ID_CONTRATO", ultimoIdContratoCreado)) + precioAumentoORestaDorado1OPlatino;
                global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(totalreal + (precioMaterialOtro + precioMaterial + precioCilindroLectura + precioBifocalOtro)),
                        "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL

                //Guardar en historial de movimientos
                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                        "Se actualizo el historial clinico", "0");

                //Actualizar historialsinconversion
                if(idPaqueteSeleccionado.equals("1") || idPaqueteSeleccionado.equals("6")) {
                    //LECTURA O DORADO 2
                    actualizarHistorialesOHistorialSinConversion(ultimoIdContratoCreado, ultimoIdHistorialClinicoCreado);
                }

            }

        }catch (SQLiteException e) {
            global.escribirError(e, 141);
            Log.i("ERRORBD", e.getMessage() + "");
        }



    }

    private void actualizarHistorialesOHistorialSinConversion(String idContrato, String idHistorialClinico) {

        if(idHistorialClinico.length() > 0) {
            //LECTURA O DORADO2
            global.actualizarAtributoTabla("HISTORIALSINCONVERSION",
                    "ESFERICODER ='" + etOjoDerechoEsfericoSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "CILINDRODER ='" + etOjoDerechoCilindroSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "EJEDER ='" + etOjoDerechoEjeSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "ADDDER ='" + etOjoDerechoAddSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "ESFERICOIZQ ='" + etOjoIzquierdoEsfericoSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "CILINDROIZQ ='" + etOjoIzquierdoCilindroSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "EJEIZQ ='" + etOjoIzquierdoEjeSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "ADDIZQ ='" + etOjoIzquierdoAddSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "ENVIADOPAGINA",
                    "0",
                    "ID_CONTRATO = '" + idContrato + "' AND ID_HISTORIAL",
                    idHistorialClinico);
        }else {
            //DORADO 2
            global.actualizarAtributoTabla("HISTORIALSINCONVERSION",
                    "ESFERICODER ='" + etOjoDerechoEsfericoSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "CILINDRODER ='" + etOjoDerechoCilindroSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "EJEDER ='" + etOjoDerechoEjeSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "ADDDER ='" + etOjoDerechoAddSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "ESFERICOIZQ ='" + etOjoIzquierdoEsfericoSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "CILINDROIZQ ='" + etOjoIzquierdoCilindroSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "EJEIZQ ='" + etOjoIzquierdoEjeSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "ADDIZQ ='" + etOjoIzquierdoAddSinConversionNuevoHistorial2.getText().toString() + "', " +
                            "ENVIADOPAGINA",
                    "0",
                    "ID_CONTRATO",
                    idContrato);
        }

    }

    private void actualizarHistorialTablaGarantias(String idHistorialNuevo) {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());
        SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

        String consulta = "UPDATE GARANTIAS SET ID_HISTORIALGARANTIA='" + idHistorialNuevo + "', ESTADOGARANTIA = '2', ENVIADOPAGINA = '0', UPDATED_AT = '" + fechaActual + time +
                "' WHERE ID_HISTORIAL = '" + ultimoIdHistorialClinicoCreado + "' AND ID_CONTRATO='" + ultimoIdContratoCreado + "' AND ESTADOGARANTIA = '1'";

        sqLiteDB2.execSQL(consulta);
        sqLiteDB2.close();

    }

    /*Metodo/Funcion: validacionTratamientosPaquetes
     Descripcion: Obtener el precio total del o los tratamientos modificados en el historial clinico
   */
    private void validacionTratamientosPaquetes() {


        if(esHistorialGarantia) {
            //Es historial con garantia a crear o modificar

            boolean historialTipoGarantia = global.obtenerAtributoTabla("HISTORIALCLINICO", "TIPO", "ID", ultimoIdHistorialClinicoCreado).equals("1");

            if (idPaqueteSeleccionado.equals("7")
                    && (Double.parseDouble(etOjoDerechoAddNuevoHistorial2.getText().toString()) > 3.25
                    || Double.parseDouble(etOjoIzquierdoAddNuevoHistorial2.getText().toString()) > 3.25)) {
                //Si paquete es igual a PLATINO y add derecho o izquierdo tienen mas de +3.25
                //Se cambian todos a 0

                //NO SE HACE NADA POR QUE NO SE LE DESCUENTA AL CONTRATO AL SER UNA GARANTIA
                /*if (arregloTratamientos[0]) { //Fotocromatico habia sido checkeado
                    //Se quita el fotocromatico y se resta el valor
                    //actualizarTratamientosHistorial("FOTOCROMATICO", "0");
                    actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - obtenerPrecioTratamiento("3"));
                }
                if (arregloTratamientos[1]) { //AR habia sido checkeado
                    //Se quita el AR y se resta el valor
                    //actualizarTratamientosHistorial("AR", "0");
                    actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - obtenerPrecioTratamiento("4"));
                }
                if (arregloTratamientos[2]) { //Tinte habia sido checkeado
                    //Se quita el AR y se resta el valor
                    //actualizarTratamientosHistorial("TINTE", "0");
                    actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - obtenerPrecioTratamiento("5"));
                }
                if (arregloTratamientos[3]) { //Blueray habia sido checkeado
                    //Se quita el AR y se resta el valor
                    //actualizarTratamientosHistorial("BLUERAY", "0");
                    actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - obtenerPrecioTratamiento("6"));
                }
                if (arregloTratamientos[4]) { //Otro habia sido checkeado
                    //Se quita el AR y se resta el valor
                    //actualizarTratamientosHistorial("OTROT", "0");
                    actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - precioTratamientoOtro);
                }*/

            } else {
                //Si paquete es diferente a PLATINO y add derecho o izquierdo tienen menos de +3.25

                String cadenaTratamientosAgregados = "";
                String cadenaTratamientosQuitados = "";
                //boolean descuentoProteccion = false;
                boolean aumentoProteccion = false;

                if (cbTratamientoFotocromatico2.isChecked() && arregloTratamientos[0]) { //Fotocromatico
                    //NO SE HACE NADA Fotocromatico BD
                } else {
                    if (cbTratamientoFotocromatico2.isChecked()) {
                        if (!arregloTratamientos[0]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Fotocromatico AL TOTALHISTORIAL
                            if(historialTipoGarantia) {
                                actualizarTratamientosHistorial("FOTOCROMATICO", cbTratamientoFotocromatico2.isChecked() ? "1" : "0");
                                cadenaTratamientosAgregados = cadenaTratamientosAgregados + "FOTOCROMATICO";
                            }
                            if (spPaquetesNuevoHistorial2.getSelectedItemPosition() == 2) {
                                //PAQUETE PROTECCION
                                if (arregloTratamientos[3] && cbTratamientoBlueray2.isChecked()) {
                                    //Tratamiento blueray ya estaba chequeado desde la creacion del contrato
                                    actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                            obtenerPrecioTratamiento("6"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("6")),
                                            "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                    modificarTotalPromocion(obtenerPrecioTratamiento("6"));
                                }else {
                                    if (cbTratamientoBlueray2.isChecked()) {
                                        if(!arregloTratamientos[3]) {
                                            //Tratamiento blueray no estaba chequeado
                                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL", 0) +
                                                    obtenerPrecioTratamiento("6"));
                                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("6")),
                                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                            modificarTotalPromocion(obtenerPrecioTratamiento("6"));
                                            aumentoProteccion = true;
                                        }
                                    }
                                }
                            } else {
                                //PAQUETE DIFERENTE PROTECCION
                                actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                        obtenerPrecioTratamiento("3"));
                                global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                        "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("3")),
                                        "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                modificarTotalPromocion(obtenerPrecioTratamiento("3"));
                            }
                        }
                    } else {
                        //NO SE HACE NADA POR QUE NO SE LE DESCUENTA AL CONTRATO AL SER UNA GARANTIA
                        if (arregloTratamientos[0]) {
                            //SE QUITA EL TRATAMIENTO FOTOCROMATICO BD
                            if(historialTipoGarantia) {
                                actualizarTratamientosHistorial("FOTOCROMATICO", cbTratamientoFotocromatico2.isChecked() ? "1" : "0");
                                cadenaTratamientosQuitados = cadenaTratamientosQuitados + "FOTOCROMATICO";
                            }
                            /*if (spPaquetesNuevoHistorial2.getSelectedItemPosition() == 2) {
                                //PAQUETE PROTECCION
                                if (arregloTratamientos[0] && arregloTratamientos[3]) {
                                    //Tratamiento fotocromatico y blueray ya estaban chequeado
                                    descuentoProteccion = true;
                                    actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - obtenerPrecioTratamiento("6"));
                                }
                            } else {
                                //PAQUETE DIFERENTE PROTECCION
                                actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - obtenerPrecioTratamiento("3"));
                            }*/
                        } else {
                            //NO SE HACE NADA
                        }
                    }
                }

                if (cbTratamientoAR2.isChecked() && arregloTratamientos[1]) { //AR
                    //NO SE HACE NADA AR BD
                } else {
                    if (cbTratamientoAR2.isChecked()) {
                        if (!arregloTratamientos[1]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO AR AL TOTALHISTORIAL
                            if(historialTipoGarantia) {
                                actualizarTratamientosHistorial("AR", cbTratamientoAR2.isChecked() ? "1" : "0");
                                cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/AR";
                            }
                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                    obtenerPrecioTratamiento("4"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("4")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("4"));
                        }
                    } else {
                        //NO SE HACE NADA POR QUE NO SE LE DESCUENTA AL CONTRATO AL SER UNA GARANTIA
                        if (arregloTratamientos[1]) {
                            //SE QUITA EL TRATAMIENTO AR BD
                            if(historialTipoGarantia) {
                                actualizarTratamientosHistorial("AR", cbTratamientoAR2.isChecked() ? "1" : "0");
                                cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/AR";
                            }
                            //actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - obtenerPrecioTratamiento("4"));
                        } else {
                            //NO SE HACE NADA
                        }
                    }
                }

                if (cbTratamientoTinte2.isChecked() && arregloTratamientos[2]) { //Tinte
                    //VERIFICAR COLOR Y ESTILO PARA CASO DE MODIFICACION
                    actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORTINTE", cbTratamientoTinte2.isChecked() ? idsColorTratamientoTinte[spColorTratamiento2.getSelectedItemPosition()]: "");
                    actualizarTratamientosHistorial("ESTILOTINTE", cbTratamientoTinte2.isChecked() ? idsEstiloTratamientoTinte[spEstiloTratamiento2.getSelectedItemPosition()]: "");
                } else {
                    if (cbTratamientoTinte2.isChecked()) {
                        if (!arregloTratamientos[2]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Tinte AL TOTALHISTORIAL
                            if(historialTipoGarantia) {
                                actualizarTratamientosHistorial("TINTE", cbTratamientoTinte2.isChecked() ? "1" : "0");
                                actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORTINTE", cbTratamientoTinte2.isChecked() ? idsColorTratamientoTinte[spColorTratamiento2.getSelectedItemPosition()]: "");
                                actualizarTratamientosHistorial("ESTILOTINTE", cbTratamientoTinte2.isChecked() ? idsEstiloTratamientoTinte[spEstiloTratamiento2.getSelectedItemPosition()]: "");
                                cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/TINTE";
                            }
                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                    obtenerPrecioTratamiento("5"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("5")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("5"));
                        }
                    } else {
                        //NO SE HACE NADA POR QUE NO SE LE DESCUENTA AL CONTRATO AL SER UNA GARANTIA
                        if (arregloTratamientos[2]) {
                            //SE QUITA EL TRATAMIENTO TINTE BD
                            if(historialTipoGarantia) {
                                actualizarTratamientosHistorial("TINTE", cbTratamientoTinte2.isChecked() ? "1" : "0");
                                actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORTINTE", cbTratamientoTinte2.isChecked() ? idsColorTratamientoTinte[spColorTratamiento2.getSelectedItemPosition()]: "");
                                actualizarTratamientosHistorial("ESTILOTINTE", cbTratamientoTinte2.isChecked() ? idsEstiloTratamientoTinte[spEstiloTratamiento2.getSelectedItemPosition()]: "");
                                cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/TINTE";
                            }
                            //actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - obtenerPrecioTratamiento("5"));
                        } else {
                            //NO SE HACE NADA
                        }
                    }
                }

                if (cbTratamientoBlueray2.isChecked() && arregloTratamientos[3]) { //Blueray
                    //NO SE HACE NADA Blueray BD
                } else {
                    if (cbTratamientoBlueray2.isChecked()) {
                        if (!arregloTratamientos[3]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Blueray AL TOTALHISTORIAL
                            if(historialTipoGarantia) {
                                actualizarTratamientosHistorial("BLUERAY", cbTratamientoBlueray2.isChecked() ? "1" : "0");
                                cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/BLUERAY";
                            }
                            if (spPaquetesNuevoHistorial2.getSelectedItemPosition() == 2) {
                                //PAQUETE PROTECCION
                                if (arregloTratamientos[0] && cbTratamientoFotocromatico2.isChecked()) {
                                    //Tratamiento fotocromatico ya estaba chequeado desde la creacion del contrato
                                    actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                            obtenerPrecioTratamiento("3"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("3")),
                                            "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                    modificarTotalPromocion(obtenerPrecioTratamiento("3"));
                                }else {
                                    if (cbTratamientoFotocromatico2.isChecked()) {
                                        if(!arregloTratamientos[0]) {
                                            //Tratamiento fotocromatico no estaba chequeado
                                            if(!aumentoProteccion) {
                                                actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                                        obtenerPrecioTratamiento("3"));
                                                global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                                        "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("3")),
                                                        "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                                modificarTotalPromocion(obtenerPrecioTratamiento("3"));
                                            }
                                        }
                                    }
                                }
                            } else {
                                //PAQUETE DIFERENTE PROTECCION
                                actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                        obtenerPrecioTratamiento("6"));
                                global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                        "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("6")),
                                        "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                modificarTotalPromocion(obtenerPrecioTratamiento("6"));
                            }
                        }
                    } else {
                        //NO SE HACE NADA POR QUE NO SE LE DESCUENTA AL CONTRATO AL SER UNA GARANTIA
                        if (arregloTratamientos[3]) {
                            //SE QUITA EL TRATAMIENTO BLUERAY BD
                            if(historialTipoGarantia) {
                                actualizarTratamientosHistorial("BLUERAY", cbTratamientoBlueray2.isChecked() ? "1" : "0");
                                cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/BLUERAY";
                            }
                            /*if (spPaquetesNuevoHistorial2.getSelectedItemPosition() == 2) {
                                //PAQUETE PROTECCION
                                if (arregloTratamientos[3] && arregloTratamientos[0]) {
                                    //Tratamiento blueray y fotocromatico ya estaban chequeado
                                    if (!descuentoProteccion) {
                                        actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - obtenerPrecioTratamiento("3"));
                                    }
                                }
                            } else {
                                //PAQUETE DIFERENTE PROTECCION
                                actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - obtenerPrecioTratamiento("6"));
                            }*/
                        } else {
                            //NO SE HACE NADA
                        }
                    }
                }

                if (cbTratamientoPolarizado2.isChecked() && arregloTratamientos[5]) { //Polarizado
                    //VERIFICAR COLOR Y ESTILO PARA CASO DE MODIFICACION
                    actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORPOLARIZADO", cbTratamientoPolarizado2.isChecked() ? idsColorTratamientoPolarizado[spColorTratamientoPolarizado2.getSelectedItemPosition()]: "");
                } else {
                    if (cbTratamientoTinte2.isChecked()) {
                        if (!arregloTratamientos[5]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Polarizado AL TOTALHISTORIAL
                            actualizarTratamientosHistorial("POLARIZADO", cbTratamientoPolarizado2.isChecked() ? "1" : "0");
                            actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORPOLARIZADO", cbTratamientoPolarizado2.isChecked() ? idsColorTratamientoPolarizado[spColorTratamientoPolarizado2.getSelectedItemPosition()]: "");

                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                    obtenerPrecioTratamiento("7"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("7")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("7"));

                            cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/POLARIZADO";
                        }
                    } else {
                        if (arregloTratamientos[5]) {
                            //SE QUITA EL TRATAMIENTO POLARIZADO BD - NO SE DESCUENTA
                            actualizarTratamientosHistorial("POLARIZADO", cbTratamientoPolarizado2.isChecked() ? "1" : "0");
                            actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORPOLARIZADO", cbTratamientoPolarizado2.isChecked() ? idsColorTratamientoPolarizado[spColorTratamientoPolarizado2.getSelectedItemPosition()]: "");

                            cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/POLARIZADO";

                        }
                    }
                }

                if (cbTratamientoEspejeado2.isChecked() && arregloTratamientos[6]) { //Espejo
                    //VERIFICAR COLOR Y ESTILO PARA CASO DE MODIFICACION
                    actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORESPEJO", cbTratamientoEspejeado2.isChecked() ? idsColorTratamientoEspejo[spColorTratamientoEspejeado2.getSelectedItemPosition()]: "");
                } else {
                    if (cbTratamientoTinte2.isChecked()) {
                        if (!arregloTratamientos[6]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Espejo AL TOTALHISTORIAL
                            actualizarTratamientosHistorial("ESPEJO", cbTratamientoEspejeado2.isChecked() ? "1" : "0");
                            actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORESPEJO", cbTratamientoEspejeado2.isChecked() ? idsColorTratamientoEspejo[spColorTratamientoEspejeado2.getSelectedItemPosition()]: "");

                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                    obtenerPrecioTratamiento("8"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("8")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("8"));

                            cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/ESPEJO";
                        }
                    } else {
                        if (arregloTratamientos[6]) {
                            //SE QUITA EL TRATAMIENTO ESPEJO BD - NO SE DESCUENTA
                            actualizarTratamientosHistorial("ESPEJO", cbTratamientoEspejeado2.isChecked() ? "1" : "0");
                            actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORESPEJO", cbTratamientoEspejeado2.isChecked() ? idsColorTratamientoEspejo[spColorTratamientoEspejeado2.getSelectedItemPosition()]: "");

                            cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/ESPEJO";

                        }
                    }
                }

                if (cbTratamientoOtro2.isChecked() && arregloTratamientos[4]) { //Otro
                    //NO SE HACE NADA Otro BD
                } else {
                    if (cbTratamientoOtro2.isChecked()) {
                        if (!arregloTratamientos[4]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Otro AL TOTALHISTORIAL
                            if(historialTipoGarantia) {
                                actualizarTratamientosHistorial("OTROT", cbTratamientoOtro2.isChecked() ? "1" : "0");
                                cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/OTROT";
                            }
                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                    Double.parseDouble(etPrecioTratamientoNuevoHistorial2.getText().toString()));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + Double.parseDouble(etPrecioTratamientoNuevoHistorial2.getText().toString())),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(Double.parseDouble(etPrecioTratamientoNuevoHistorial2.getText().toString()));
                        }
                    } else {
                        //NO SE HACE NADA POR QUE NO SE LE DESCUENTA AL CONTRATO AL SER UNA GARANTIA
                        if (arregloTratamientos[4]) {
                            //SE QUITA EL TRATAMIENTO OTROT BD
                            if(historialTipoGarantia) {
                                actualizarTratamientosHistorial("OTROT", cbTratamientoOtro2.isChecked() ? "1" : "0");
                                cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/OTROT";
                            }
                            //actualizarPrecioTotalContrato(obtenerPrecioTotalContratoActualizadoBD(0) - precioTratamientoOtro);
                        } else {
                            //NO SE HACE NADA
                        }
                    }
                }

                if(historialTipoGarantia) {
                    if (cadenaTratamientosAgregados.length() != 0) {
                        //Guardar en historial de movimientos los tratamientos agregados
                        historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                "Historial modificado, tratamientos agregados " + cadenaTratamientosAgregados, "0");
                    }

                    if (cadenaTratamientosQuitados.length() != 0) {
                        //Guardar en historial de movimientos los tratamientos quitados
                        historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                "Historial modificado, tratamientos quitados " + cadenaTratamientosQuitados, "0");
                    }

                    //Actualizar atributo ENVIADOPAGINA a 0 del historial clinico actual
                    global.actualizarAtributoTabla("HISTORIALCLINICO", "ENVIADOPAGINA", "0", "ID", ultimoIdHistorialClinicoCreado);
                }

            }

        }else {
            //Es historial sin garantia a modificar

            if (idPaqueteSeleccionado.equals("7")
                    && (Double.parseDouble(etOjoDerechoAddNuevoHistorial2.getText().toString()) > 3.25
                    || Double.parseDouble(etOjoIzquierdoAddNuevoHistorial2.getText().toString()) > 3.25)) {
                //Si paquete es igual a PLATINO y add derecho o izquierdo tienen mas de +3.25
                //Se cambian todos a 0

                if (arregloTratamientos[0]) { //Fotocromatico habia sido checkeado
                    //Se quita el fotocromatico y se resta el valor
                    actualizarTratamientosHistorial("FOTOCROMATICO", "0");
                    actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                            obtenerPrecioTratamiento("3"));
                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("3")),
                            "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                    modificarTotalPromocion(obtenerPrecioTratamiento("3") * -1);
                }
                if (arregloTratamientos[1]) { //AR habia sido checkeado
                    //Se quita el AR y se resta el valor
                    actualizarTratamientosHistorial("AR", "0");
                    actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                            obtenerPrecioTratamiento("4"));
                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("4")),
                            "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                    modificarTotalPromocion(obtenerPrecioTratamiento("4") * -1);
                }
                if (arregloTratamientos[2]) { //Tinte habia sido checkeado
                    //Se quita el AR y se resta el valor
                    actualizarTratamientosHistorial("TINTE", "0");
                    actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORTINTE", "");
                    actualizarTratamientosHistorial("ESTILOTINTE", "");
                    actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                            obtenerPrecioTratamiento("5"));
                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("5")),
                            "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                    modificarTotalPromocion(obtenerPrecioTratamiento("5") * -1);
                }
                if (arregloTratamientos[3]) { //Blueray habia sido checkeado
                    //Se quita el AR y se resta el valor
                    actualizarTratamientosHistorial("BLUERAY", "0");
                    actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                            obtenerPrecioTratamiento("6"));
                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("6")),
                            "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                    modificarTotalPromocion(obtenerPrecioTratamiento("6") * -1);
                }
                if (arregloTratamientos[4]) { //Otro habia sido checkeado
                    //Se quita el AR y se resta el valor
                    actualizarTratamientosHistorial("OTROT", "0");
                    actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) - precioTratamientoOtro);
                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS", "TOTALREAL",
                            "ID_CONTRATO", ultimoIdContratoCreado)) - precioTratamientoOtro), "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                    modificarTotalPromocion(precioTratamientoOtro * -1);
                }

            } else {
                //Si paquete es diferente a PLATINO y add derecho o izquierdo tienen menos de +3.25

                String cadenaTratamientosAgregados = "";
                String cadenaTratamientosQuitados = "";
                boolean descuentoProteccion = false;
                boolean aumentoProteccion = false;

                if (cbTratamientoFotocromatico2.isChecked() && arregloTratamientos[0]) { //Fotocromatico
                    //NO SE HACE NADA Fotocromatico BD
                } else {
                    if (cbTratamientoFotocromatico2.isChecked()) {
                        if (!arregloTratamientos[0]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Fotocromatico AL TOTALHISTORIAL
                            actualizarTratamientosHistorial("FOTOCROMATICO", cbTratamientoFotocromatico2.isChecked() ? "1" : "0");
                            if (spPaquetesNuevoHistorial2.getSelectedItemPosition() == 2) {
                                //PAQUETE PROTECCION
                                if (arregloTratamientos[3] && cbTratamientoBlueray2.isChecked()) {
                                    //Tratamiento blueray ya estaba chequeado desde la creacion del contrato
                                    actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                            obtenerPrecioTratamiento("6"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("6")),
                                            "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                    modificarTotalPromocion(obtenerPrecioTratamiento("6"));
                                }else {
                                    if (cbTratamientoBlueray2.isChecked()) {
                                        if(!arregloTratamientos[3]) {
                                            //Tratamiento blueray no estaba chequeado
                                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL", 0) +
                                                    obtenerPrecioTratamiento("6"));
                                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("6")),
                                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                            modificarTotalPromocion(obtenerPrecioTratamiento("6"));
                                            aumentoProteccion = true;
                                        }
                                    }
                                }
                            } else {
                                //PAQUETE DIFERENTE PROTECCION
                                actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                        obtenerPrecioTratamiento("3"));
                                global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                        "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("3")),
                                        "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                modificarTotalPromocion(obtenerPrecioTratamiento("3"));
                            }
                            cadenaTratamientosAgregados = cadenaTratamientosAgregados + "FOTOCROMATICO";
                        }
                    } else {
                        if (arregloTratamientos[0]) {
                            //SE QUITA EL TRATAMIENTO FOTOCROMATICO BD
                            actualizarTratamientosHistorial("FOTOCROMATICO", cbTratamientoFotocromatico2.isChecked() ? "1" : "0");
                            if (spPaquetesNuevoHistorial2.getSelectedItemPosition() == 2) {
                                //PAQUETE PROTECCION
                                if (arregloTratamientos[0] && arregloTratamientos[3]) {
                                    //Tratamiento fotocromatico y blueray ya estaban chequeado
                                    descuentoProteccion = true;
                                    actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                                            obtenerPrecioTratamiento("6"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("6")),
                                            "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                    modificarTotalPromocion(obtenerPrecioTratamiento("6") * -1);
                                }
                            } else {
                                //PAQUETE DIFERENTE PROTECCION
                                actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                                        obtenerPrecioTratamiento("3"));
                                global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                        "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("3")),
                                        "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                modificarTotalPromocion(obtenerPrecioTratamiento("3") * -1);
                            }
                            cadenaTratamientosQuitados = cadenaTratamientosQuitados + "FOTOCROMATICO";
                        } else {
                            //NO SE HACE NADA
                        }
                    }
                }

                if (cbTratamientoAR2.isChecked() && arregloTratamientos[1]) { //AR
                    //NO SE HACE NADA AR BD
                } else {
                    if (cbTratamientoAR2.isChecked()) {
                        if (!arregloTratamientos[1]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO AR AL TOTALHISTORIAL
                            actualizarTratamientosHistorial("AR", cbTratamientoAR2.isChecked() ? "1" : "0");
                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                    obtenerPrecioTratamiento("4"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("4")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("4"));
                            cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/AR";
                        }
                    } else {
                        if (arregloTratamientos[1]) {
                            //SE QUITA EL TRATAMIENTO AR BD
                            actualizarTratamientosHistorial("AR", cbTratamientoAR2.isChecked() ? "1" : "0");
                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                                    obtenerPrecioTratamiento("4"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("4")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("4") * -1);
                            cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/AR";
                        } else {
                            //NO SE HACE NADA
                        }
                    }
                }

                if (cbTratamientoTinte2.isChecked() && arregloTratamientos[2]) { //Tinte
                    //VERIFICAR COLOR Y ESTILO PARA CASO DE MODIFICACION
                } else {
                    if (cbTratamientoTinte2.isChecked()) {
                        if (!arregloTratamientos[2]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Tinte AL TOTALHISTORIAL
                            actualizarTratamientosHistorial("TINTE", cbTratamientoTinte2.isChecked() ? "1" : "0");
                            actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORTINTE", cbTratamientoTinte2.isChecked() ? idsColorTratamientoTinte[spColorTratamiento2.getSelectedItemPosition()]: "");
                            actualizarTratamientosHistorial("ESTILOTINTE", cbTratamientoTinte2.isChecked() ? idsEstiloTratamientoTinte[spEstiloTratamiento2.getSelectedItemPosition()]: "");
                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                    obtenerPrecioTratamiento("5"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("5")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("5"));
                            cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/TINTE";
                        }
                    } else {
                        if (arregloTratamientos[2]) {
                            //SE QUITA EL TRATAMIENTO TINTE BD
                            actualizarTratamientosHistorial("TINTE", cbTratamientoTinte2.isChecked() ? "1" : "0");
                            actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORTINTE", cbTratamientoTinte2.isChecked() ? idsColorTratamientoTinte[spColorTratamiento2.getSelectedItemPosition()]: "");
                            actualizarTratamientosHistorial("ESTILOTINTE", cbTratamientoTinte2.isChecked() ? idsEstiloTratamientoTinte[spEstiloTratamiento2.getSelectedItemPosition()]: "");
                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                                    obtenerPrecioTratamiento("5"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("5")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("5") * -1);
                            cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/TINTE";
                        } else {
                            //NO SE HACE NADA
                        }
                    }
                }

                if (cbTratamientoBlueray2.isChecked() && arregloTratamientos[3]) { //Blueray
                    //NO SE HACE NADA Blueray BD
                } else {
                    if (cbTratamientoBlueray2.isChecked()) {
                        if (!arregloTratamientos[3]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Blueray AL TOTALHISTORIAL
                            actualizarTratamientosHistorial("BLUERAY", cbTratamientoBlueray2.isChecked() ? "1" : "0");
                            if (spPaquetesNuevoHistorial2.getSelectedItemPosition() == 2) {
                                //PAQUETE PROTECCION
                                if (arregloTratamientos[0] && cbTratamientoFotocromatico2.isChecked()) {
                                    //Tratamiento fotocromatico ya estaba chequeado desde la creacion del contrato
                                    actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                            obtenerPrecioTratamiento("3"));
                                    global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("3")),
                                            "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                    modificarTotalPromocion(obtenerPrecioTratamiento("3"));
                                }else {
                                    if (cbTratamientoFotocromatico2.isChecked()) {
                                        if(!arregloTratamientos[0]) {
                                            //Tratamiento fotocromatico no estaba chequeado
                                            if(!aumentoProteccion) {
                                                actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                                        obtenerPrecioTratamiento("3"));
                                                global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                                        "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("3")),
                                                        "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                                modificarTotalPromocion(obtenerPrecioTratamiento("3"));
                                            }
                                        }
                                    }
                                }
                            } else {
                                //PAQUETE DIFERENTE PROTECCION
                                actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                        obtenerPrecioTratamiento("6"));
                                global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                        "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("6")),
                                        "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                modificarTotalPromocion(obtenerPrecioTratamiento("6"));
                            }
                            cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/BLUERAY";
                        }
                    } else {
                        if (arregloTratamientos[3]) {
                            //SE QUITA EL TRATAMIENTO BLUERAY BD
                            actualizarTratamientosHistorial("BLUERAY", cbTratamientoBlueray2.isChecked() ? "1" : "0");
                            if (spPaquetesNuevoHistorial2.getSelectedItemPosition() == 2) {
                                //PAQUETE PROTECCION
                                if (arregloTratamientos[3] && arregloTratamientos[0]) {
                                    //Tratamiento blueray y fotocromatico ya estaban chequeado
                                    if (!descuentoProteccion) {
                                        actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                                                obtenerPrecioTratamiento("3"));
                                        global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                                "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("3")),
                                                "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                        modificarTotalPromocion(obtenerPrecioTratamiento("3") * -1);
                                    }
                                }
                            } else {
                                //PAQUETE DIFERENTE PROTECCION
                                actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                                        obtenerPrecioTratamiento("6"));
                                global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                        "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("6")),
                                        "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                                modificarTotalPromocion(obtenerPrecioTratamiento("6") * -1);
                            }
                            cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/BLUERAY";
                        } else {
                            //NO SE HACE NADA
                        }
                    }
                }

                if (cbTratamientoPolarizado2.isChecked() && arregloTratamientos[5]) { //Polarizado
                    //VERIFICAR COLOR Y ESTILO PARA CASO DE MODIFICACION
                    actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORPOLARIZADO", cbTratamientoPolarizado2.isChecked() ? idsColorTratamientoPolarizado[spColorTratamientoPolarizado2.getSelectedItemPosition()]: "");
                } else {
                    if (cbTratamientoPolarizado2.isChecked()) {
                        if (!arregloTratamientos[5]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Polarizado AL TOTALHISTORIAL
                            actualizarTratamientosHistorial("POLARIZADO", cbTratamientoPolarizado2.isChecked() ? "1" : "0");
                            actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORPOLARIZADO", cbTratamientoPolarizado2.isChecked() ? idsColorTratamientoPolarizado[spColorTratamientoPolarizado2.getSelectedItemPosition()]: "");

                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                    obtenerPrecioTratamiento("7"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("7")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("7"));

                            cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/POLARIZADO";

                        }
                    } else {
                        if (arregloTratamientos[5]) {
                            //SE QUITA EL TRATAMIENTO POLARIZADO BD
                            actualizarTratamientosHistorial("POLARIZADO", cbTratamientoPolarizado2.isChecked() ? "1" : "0");
                            actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORPOLARIZADO", cbTratamientoPolarizado2.isChecked() ? idsColorTratamientoPolarizado[spColorTratamientoPolarizado2.getSelectedItemPosition()]: "");
                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                                    obtenerPrecioTratamiento("7"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("7")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("7") * -1);
                            cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/POLARIZADO";
                        }
                    }
                }

                if (cbTratamientoEspejeado2.isChecked() && arregloTratamientos[6]) { //Espejo
                    //VERIFICAR COLOR Y ESTILO PARA CASO DE MODIFICACION
                    actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORESPEJO", cbTratamientoEspejeado2.isChecked() ? idsColorTratamientoEspejo[spColorTratamientoEspejeado2.getSelectedItemPosition()]: "");
                } else {
                    if (cbTratamientoEspejeado2.isChecked()) {
                        if (!arregloTratamientos[6]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Espejo AL TOTALHISTORIAL
                            actualizarTratamientosHistorial("ESPEJO", cbTratamientoEspejeado2.isChecked() ? "1" : "0");
                            actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORESPEJO", cbTratamientoEspejeado2.isChecked() ? idsColorTratamientoEspejo[spColorTratamientoEspejeado2.getSelectedItemPosition()]: "");

                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                    obtenerPrecioTratamiento("8"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + obtenerPrecioTratamiento("8")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("8"));

                            cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/ESPEJO";

                        }
                    } else {
                        if (arregloTratamientos[6]) {
                            //SE QUITA EL TRATAMIENTO ESPEJO BD
                            actualizarTratamientosHistorial("ESPEJO", cbTratamientoEspejeado2.isChecked() ? "1" : "0");
                            actualizarTratamientosHistorial("ID_TRATAMIENTOCOLORESPEJO", cbTratamientoEspejeado2.isChecked() ? idsColorTratamientoEspejo[spColorTratamientoEspejeado2.getSelectedItemPosition()]: "");

                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                                    obtenerPrecioTratamiento("8"));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                            "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - obtenerPrecioTratamiento("8")),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(obtenerPrecioTratamiento("8") * -1);
                            cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/ESPEJO";
                        }
                    }
                }

                if (cbTratamientoOtro2.isChecked() && arregloTratamientos[4]) { //Otro
                    //NO SE HACE NADA Otro BD
                } else {
                    if (cbTratamientoOtro2.isChecked()) {
                        if (!arregloTratamientos[4]) {
                            //SE AUMENTA EL PRECIO DEL TRATAMIENTO Otro AL TOTALHISTORIAL
                            actualizarTratamientosHistorial("OTROT", cbTratamientoOtro2.isChecked() ? "1" : "0");
                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) +
                                    Double.parseDouble(etPrecioTratamientoNuevoHistorial2.getText().toString()));
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) + Double.parseDouble(etPrecioTratamientoNuevoHistorial2.getText().toString())),
                                    "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(Double.parseDouble(etPrecioTratamientoNuevoHistorial2.getText().toString()));
                            cadenaTratamientosAgregados = cadenaTratamientosAgregados + "/OTROT";
                        }
                    } else {
                        if (arregloTratamientos[4]) {
                            //SE QUITA EL TRATAMIENTO OTROT BD
                            actualizarTratamientosHistorial("OTROT", cbTratamientoOtro2.isChecked() ? "1" : "0");
                            actualizarPrecioTotalContrato("TOTALHISTORIAL", obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL",0) -
                                    precioTratamientoOtro);
                            global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(Double.parseDouble(global.obtenerAtributoTabla("CONTRATOS",
                                    "TOTALREAL", "ID_CONTRATO", ultimoIdContratoCreado)) - precioTratamientoOtro), "ID_CONTRATO", ultimoIdContratoCreado); //Actualizar TOTALREAL
                            modificarTotalPromocion(precioTratamientoOtro * -1);
                            cadenaTratamientosQuitados = cadenaTratamientosQuitados + "/OTROT";
                        } else {
                            //NO SE HACE NADA
                        }
                    }
                }

                if (cadenaTratamientosAgregados.length() != 0) {
                    //Guardar en historial de movimientos los tratamientos agregados
                    historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                            "Historial modificado, tratamientos agregados " + cadenaTratamientosAgregados, "0");
                }

                if (cadenaTratamientosQuitados.length() != 0) {
                    //Guardar en historial de movimientos los tratamientos quitados
                    historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                            "Historial modificado, tratamientos quitados " + cadenaTratamientosQuitados, "0");
                }

            }

            //Actualizar atributo ENVIADOPAGINA a 0 del historial clinico actual
            global.actualizarAtributoTabla("HISTORIALCLINICO", "ENVIADOPAGINA", "0", "ID", ultimoIdHistorialClinicoCreado);

        }

        llamadaSincronizacion();

        //Mandar a vista verContrato
        Fragment verificadorFragment = new verContrato();
        Bundle bundle = new Bundle();
        bundle.putString("ultimoIdContratoCreado", ultimoIdContratoCreado);
        verificadorFragment.setArguments(bundle);
        FragmentTransaction transaction = ((FragmentActivity)fragmento.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void modificarTotalPromocion(double precioTratamiento) {
        if(global.obtenerAtributoTabla("CONTRATOS", "PROMOCIONTERMINADA", "ID_CONTRATO", ultimoIdContratoCreado).equals("1")) {
            actualizarPrecioTotalContrato("TOTALPROMOCION", obtenerPrecioTotalContratoActualizadoBD("TOTALPROMOCION",0) + precioTratamiento);
        }
    }

    /*Metodo/Funcion: actualizarTratamientosHistorial
     Descripcion: Actualizar tratamientos por separados en caso de ser seleccionado al momento de modificar el historial clinico
   */
    private void actualizarTratamientosHistorial(String atributo, String valorCheckBox) {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE HISTORIALCLINICO SET " + atributo + "='" + valorCheckBox
                    + "', TRATAMIENTOOTRO='" + global.replaceCadena(etOtroTratamientoNuevoHistorial2.getText().toString())
                    + "', COSTOTRATAMIENTO='" + etPrecioTratamientoNuevoHistorial2.getText().toString()
                    + "', UPDATED_AT='" + fechaActual + time
                    + "' WHERE ID='" + ultimoIdHistorialClinicoCreado + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 142);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: obtenerPrecioTratamiento
     Descripcion: Obtener precio del tratamiento por separado en caso de ser seleccionado al momento de modificar el historial clinico
   */
    private double obtenerPrecioTratamiento(String idTratamiento) {

        double precioTratamiento = 0;

        try {

            sqLiteDB = conexion.getReadableDatabase();

            String consulta = "SELECT PRECIO FROM TRATAMIENTOS WHERE ID='" + idTratamiento + "'";
            Cursor datos = sqLiteDB.rawQuery(consulta, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No hay contrato registrado");
            }

            if (datos.moveToFirst()) {
                precioTratamiento += Double.parseDouble(datos.getString(0));
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 143);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return precioTratamiento;

    }

    /*Metodo/Funcion: validacionCamposPaqueteDorado2
     Descripcion: Validacion de que los campos requeridos para el paquete (Dorado 2) hayan sido correctos
   */
    private void validacionCamposPaqueteDorado2() {

        verificarComponentesIguales();
        verificarComponentesDiferentesPaqueteUno();
        verificarCheckBoxsRadioButtons();

        for(int i=0; i < arregloComponentes.length; i++){
            if(arregloComponentes[i]){
                mostrarOcultarMensajesError(i,true);
            }

            boolean valido = validarArreglo(i);
            if(!valido){
                correcto = false;
            }

        }

        if(correcto){
            //Formulario correcto
            enviarDatosGuardar();
        }else{
            //Formulario incorrecto
            Toast.makeText(fragmento.getContext(),"Uno o mas campos vacios", Toast.LENGTH_LONG).show();
        }

    }

    /*Metodo/Funcion: enviarDatosGuardar
     Descripcion: Mostrar alerta de confirmacion al querer terminar el segundo historial
     y actualizar totalhistorial del contrato sumando lo nuevo que se agrego al segundo historial
   */
    private void enviarDatosGuardar() {

        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Confirmación").setMessage("¿Terminar historial clinico?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        double precioPolicarbonato = 0;
                        if(rbPolicarbonato2.isChecked()){
                            if (!cbPolicarbonatoTipo2.isChecked()){
                                //Policarbonato para adulto
                                precioPolicarbonato = 300;
                            }
                        }

                        double precioOtroMaterial = 0;
                        if(rbOtroMaterial2.isChecked()) {
                            precioOtroMaterial = Double.parseDouble(etPrecioMaterialNuevoHistorial2.getText().toString());
                        }
                        double precioOtroBifocal = 0;
                        if(rbOtroBifocal2.isChecked()) {
                            precioOtroBifocal = Double.parseDouble(etPrecioBifocalNuevoHistorial2.getText().toString());
                        }
                        double precioTotalHistorial = obtenerPrecioTotalHistorial(precioPolicarbonato + precioOtroMaterial + precioOtroBifocal);
                        decrementarProductoSeleccionadoBD();

                        double precioTotalFinal = obtenerPrecioTotalContratoActualizadoBD("TOTALHISTORIAL", precioTotalHistorial);

                        //Actualizar TOTALHISTORIAL
                        actualizarPrecioTotalContrato("TOTALHISTORIAL", precioTotalFinal);

                        //Actualizar TOTALREAL
                        global.actualizarAtributoTabla("CONTRATOS", "TOTALREAL", String.valueOf(precioTotalFinal), "ID_CONTRATO", ultimoIdContratoCreado);

                        guardarHistorialClinicoBD();

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();

    }

    /*Metodo/Funcion: obtenerPrecioTotalContratoActualizadoBD
     Descripcion: Obtener precio totalhistorial del contrato, sumarle lo nuevo que se agrego en el segundo historial
     y retornar el precio total sumado
   */
    private double obtenerPrecioTotalContratoActualizadoBD(String atributo, double precioTotalHistorial) {

        double precioTotalFinal = 0;

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT " + atributo + " FROM CONTRATOS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro nada");
            }

            if (datos.moveToFirst()){
                precioTotalFinal = precioTotalHistorial + Double.parseDouble(datos.getString(0));
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 144);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return precioTotalFinal;
    }

    /*Metodo/Funcion: actualizarPrecioTotalContrato
     Descripcion: Actualiza atributo totalhistorial del contrato con el precio final (totalhistorial anterior + lo agregado del segundo historial)
   */
    private void actualizarPrecioTotalContrato(String atributo, double precioTotalFinal) {

        String precioTotalActualizar = String.valueOf(precioTotalFinal);

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET " + atributo + " ='" + precioTotalActualizar
                    + "' WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

            calculoTotal();

        }catch (SQLiteException e) {
            global.escribirError(e, 145);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: guardarHistorialClinicoBD
     Descripcion: Obtener idAlfanumerico para la tabla de HISTORIALCLINICO y guardar el historial clinico
   */
    private void guardarHistorialClinicoBD() {

        //Obtener idAlfanumerico
        String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("HISTORIALCLINICO", 5);

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try{

            //Validar armazon seleccionado
            String fotoArmazonPropio = "";
            if(esArmazonPropio){
                //Armazon propio - Guardar imagen
                if(ivFotoArmazonPropioHistorial2.getDrawable() != null){
                    //Almacenar imagen en galeria
                    fotoArmazonPropio = camara.guardarImagenGaleria(ivFotoArmazonPropioHistorial2, "Foto-Armazon-Propio-" + ultimoIdContratoCreado + "-" +
                            fechaActual.replace("-", "").trim() + time.replace(":", "").trim());
                    global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOARMAZON2", "0", "ID_CONTRATO", ultimoIdContratoCreado);
                }
            }

            String ojoDerechoEsferico2 = etOjoDerechoEsfericoNuevoHistorial2.getText().toString();
            String ojoDerechoCilindro2 = etOjoDerechoCilindroNuevoHistorial2.getText().toString();
            String ojoDerechoEje2 = etOjoDerechoEjeNuevoHistorial2.getText().toString();
            String ojoDerechoAdd2 = etOjoDerechoAddNuevoHistorial2.getText().toString();
            String ojoDerechoAlt2 = etOjoDerechoALTNuevoHistorial2.getText().toString();

            String ojoIzquierdoEsferico2 = etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString();
            String ojoIzquierdoCilindro2 = etOjoIzquierdoCilindroNuevoHistorial2.getText().toString();
            String ojoIzquierdoEje2 = etOjoIzquierdoEjeNuevoHistorial2.getText().toString();
            String ojoIzquierdoAdd2 = etOjoIzquierdoAddNuevoHistorial2.getText().toString();
            String ojoIzquierdoAlt2 = etOjoIzquierdoALTNuevoHistorial2.getText().toString();

            //OJO DERECHO
            //ojoDerechoEsferico
            if(ojoDerechoEsferico2.startsWith(".")) {
                ojoDerechoEsferico2 = "0" + ojoDerechoEsferico2;
            }

            if(ojoDerechoEsferico2.endsWith(".")) {
                ojoDerechoEsferico2 = ojoDerechoEsferico2 + "0";
            }

            if (ojoDerechoEsferico2.length() > 0) {
                if (Double.parseDouble(ojoDerechoEsferico2) > 0) {
                    ojoDerechoEsferico2 = ojoDerechoEsferico2.replace("+", "");
                    ojoDerechoEsferico2 = "+" + ojoDerechoEsferico2;
                }
            }

            //ojoDerechoCilindro
            if(ojoDerechoCilindro2.startsWith(".")) {
                ojoDerechoCilindro2 = "0" + ojoDerechoCilindro2;
            }

            if(ojoDerechoCilindro2.endsWith(".")) {
                ojoDerechoCilindro2 = ojoDerechoCilindro2 + "0";
            }

            if (ojoDerechoCilindro2.length() > 0) {
                if (Double.parseDouble(ojoDerechoCilindro2) > 0) {
                    ojoDerechoCilindro2 = ojoDerechoCilindro2.replace("+", "");
                    ojoDerechoCilindro2 = "-" + ojoDerechoCilindro2;
                }
            }

            //ojoDerechoEje
            if(ojoDerechoEje2.startsWith(".")) {
                ojoDerechoEje2 = "0" + ojoDerechoEje2;
            }

            if(ojoDerechoEje2.endsWith(".")) {
                ojoDerechoEje2 = ojoDerechoEje2 + "0";
            }

            //ojoDerechoAdd
            if(ojoDerechoAdd2.startsWith(".")) {
                ojoDerechoAdd2 = "0" + ojoDerechoAdd2;
            }

            if(ojoDerechoAdd2.endsWith(".")) {
                ojoDerechoAdd2 = ojoDerechoAdd2 + "0";
            }

            if (ojoDerechoAdd2.length() > 0) {
                if (Double.parseDouble(ojoDerechoAdd2) > 0) {
                    ojoDerechoAdd2 = ojoDerechoAdd2.replace("+", "");
                    ojoDerechoAdd2 = "+" + ojoDerechoAdd2;
                }
            }

            //ojoDerechoAlt
            if(ojoDerechoAlt2.startsWith(".")) {
                ojoDerechoAlt2 = "0" + ojoDerechoAlt2;
            }

            if(ojoDerechoAlt2.endsWith(".")) {
                ojoDerechoAlt2 = ojoDerechoAlt2 + "0";
            }

            //OJO IZQUIERDO
            //ojoIzquierdoEsferico
            if(ojoIzquierdoEsferico2.startsWith(".")) {
                ojoIzquierdoEsferico2 = "0" + ojoIzquierdoEsferico2;
            }

            if(ojoIzquierdoEsferico2.endsWith(".")) {
                ojoIzquierdoEsferico2 = ojoIzquierdoEsferico2 + "0";
            }

            if (ojoIzquierdoEsferico2.length() > 0) {
                if (Double.parseDouble(ojoIzquierdoEsferico2) > 0) {
                    ojoIzquierdoEsferico2 = ojoIzquierdoEsferico2.replace("+", "");
                    ojoIzquierdoEsferico2 = "+" + ojoIzquierdoEsferico2;
                }
            }

            //ojoIzquierdoCilindro
            if(ojoIzquierdoCilindro2.startsWith(".")) {
                ojoIzquierdoCilindro2 = "0" + ojoIzquierdoCilindro2;
            }

            if(ojoIzquierdoCilindro2.endsWith(".")) {
                ojoIzquierdoCilindro2 = ojoIzquierdoCilindro2 + "0";
            }

            if (ojoIzquierdoCilindro2.length() > 0) {
                if (Double.parseDouble(ojoIzquierdoCilindro2) > 0) {
                    ojoIzquierdoCilindro2 = ojoIzquierdoCilindro2.replace("+", "");
                    ojoIzquierdoCilindro2 = "-" + ojoIzquierdoCilindro2;
                }
            }

            //ojoIzquierdoEje
            if(ojoIzquierdoEje2.startsWith(".")) {
                ojoIzquierdoEje2 = "0" + ojoIzquierdoEje2;
            }

            if(ojoIzquierdoEje2.endsWith(".")) {
                ojoIzquierdoEje2 = ojoIzquierdoEje2 + "0";
            }

            //ojoIzquierdoAdd
            if(ojoIzquierdoAdd2.startsWith(".")) {
                ojoIzquierdoAdd2 = "0" + ojoIzquierdoAdd2;
            }

            if(ojoIzquierdoAdd2.endsWith(".")) {
                ojoIzquierdoAdd2 = ojoIzquierdoAdd2 + "0";
            }

            if (ojoIzquierdoAdd2.length() > 0) {
                if (Double.parseDouble(ojoIzquierdoAdd2) > 0) {
                    ojoIzquierdoAdd2 = ojoIzquierdoAdd2.replace("+", "");
                    ojoIzquierdoAdd2 = "+" + ojoIzquierdoAdd2;
                }
            }

            //ojoIzquierdoAlt
            if(ojoIzquierdoAlt2.startsWith(".")) {
                ojoIzquierdoAlt2 = "0" + ojoIzquierdoAlt2;
            }

            if(ojoIzquierdoAlt2.endsWith(".")) {
                ojoIzquierdoAlt2 = ojoIzquierdoAlt2 + "0";
            }

            sqLiteDB = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID", idAlfanumerico);
            valores.put("ID_CONTRATO", ultimoIdContratoCreado);
            valores.put("EDAD", etEdadNuevoHistorial2.getText().toString());
            valores.put("FECHAENTREGA", etFechaEntregaNuevoHistorial2.getText().toString());
            valores.put("DIAGNOSTICO ", global.replaceCadena(etDiagnosticoNuevoHistorial2.getText().toString()));
            valores.put("OCUPACION", global.replaceCadena(etOcupacionNuevoHistorial2.getText().toString()));
            valores.put("DIABETES", global.replaceCadena(etDiabetesNuevoHistorial2.getText().toString()));
            valores.put("HIPERTENSION", global.replaceCadena(etHipertensionNuevoHistorial2.getText().toString()));
            valores.put("DOLOR", cbDolorCabeza2.isChecked() ? "1" : "0");
            valores.put("ARDOR", cbArdorOjos2.isChecked() ? "1" : "0");
            valores.put("GOLPEOJOS", cbGolpeCabeza2.isChecked() ? "1" : "0");
            valores.put("OTROM", cbOtroMolestia2.isChecked() ? "1" : "0");
            valores.put("MOLESTIAOTRO", global.replaceCadena(etOtroMoletiaNuevoHistorial2.getText().toString()));
            valores.put("ULTIMOEXAMEN", etUltimoExamenNuevoHistorial2.getText().toString());
            valores.put("ESFERICODER", global.replaceCadena(ojoDerechoEsferico2));
            valores.put("CILINDRODER", global.replaceCadena(ojoDerechoCilindro2));
            valores.put("EJEDER", global.replaceCadena(ojoDerechoEje2));
            valores.put("ADDDER", global.replaceCadena(ojoDerechoAdd2));
            valores.put("ALTDER", global.replaceCadena(ojoDerechoAlt2));
            valores.put("ESFERICOIZQ", global.replaceCadena(ojoIzquierdoEsferico2));
            valores.put("CILINDROIZQ", global.replaceCadena(ojoIzquierdoCilindro2));
            valores.put("EJEIZQ", global.replaceCadena(ojoIzquierdoEje2));
            valores.put("ADDIZQ", global.replaceCadena(ojoIzquierdoAdd2));
            valores.put("ALTIZQ", global.replaceCadena(ojoIzquierdoAlt2));
            valores.put("ID_PRODUCTO", idsProductos.get(spProductoNuevoHistorial2.getSelectedItemPosition()));
            valores.put("ID_PAQUETE", idsPaquetes[spPaquetesNuevoHistorial2.getSelectedItemPosition()]);
            if (rbHiIndex2.isChecked()) {
                valores.put("MATERIAL", rbHiIndex2.isChecked() ? "0" : "");
            } else
                if (rbCR2.isChecked()){
                    valores.put("MATERIAL", rbCR2.isChecked() ? "1" : "");
                }else
                    if(rbPolicarbonato2.isChecked()) {
                        valores.put("MATERIAL", rbPolicarbonato2.isChecked() ? "2" : "");
                        valores.put("POLICARBONATOTIPO", cbPolicarbonatoTipo2.isChecked() ? "1" : "0");
                    }else
                        if(rbOtroMaterial2.isChecked()) {
                            valores.put("MATERIAL", rbOtroMaterial2.isChecked() ? "3" : "");
                        }
            valores.put("MATERIALOTRO", global.replaceCadena(etOtroMaterialNuevoHistorial2.getText().toString()));
            valores.put("COSTOMATERIAL", etPrecioMaterialNuevoHistorial2.getText().toString());
            if (rbFT2.isChecked()) {
                valores.put("BIFOCAL", rbFT2.isChecked() ? "0" : "");
            } else
                if (rbBlend2.isChecked()){
                    valores.put("BIFOCAL", rbBlend2.isChecked() ? "1" : "");
                }else
                    if(rbProgresivo2.isChecked()) {
                        valores.put("BIFOCAL", rbProgresivo2.isChecked() ? "2" : "");
                    }else
                        if(rbNA2.isChecked()) {
                            valores.put("BIFOCAL", rbNA2.isChecked() ? "3" : "");
                        }else
                            if(rbOtroBifocal2.isChecked()) {
                                valores.put("BIFOCAL", rbOtroBifocal2.isChecked() ? "4" : "");
                            }
            valores.put("FOTOCROMATICO", cbTratamientoFotocromatico2.isChecked() ? "1" : "0");
            valores.put("AR", cbTratamientoAR2.isChecked() ? "1" : "0");
            valores.put("TINTE", cbTratamientoTinte2.isChecked() ? "1" : "0");
            valores.put("BLUERAY", cbTratamientoBlueray2.isChecked() ? "1" : "0");
            valores.put("OTROT", cbTratamientoOtro2.isChecked() ? "1" : "0");
            valores.put("TRATAMIENTOOTRO", global.replaceCadena(etOtroTratamientoNuevoHistorial2.getText().toString()));
            valores.put("COSTOTRATAMIENTO", etPrecioTratamientoNuevoHistorial2.getText().toString());
            valores.put("OBSERVACIONES", global.replaceCadena(etObservacionesNuevoHistorial2.getText().toString()));
            valores.put("OBSERVACIONESINTERNO", global.replaceCadena(etObservacionesInternoNuevoHistorial2.getText().toString()));
            valores.put("TIPO", "0");
            valores.put("BIFOCALOTRO", global.replaceCadena(etOtroBifocalNuevoHistorial2.getText().toString()));
            valores.put("COSTOBIFOCAL", etPrecioBifocalNuevoHistorial2.getText().toString());
            valores.put("EMBARAZADA", global.replaceCadena(etEmbarazadaNuevoHistorial2.getText().toString()));
            valores.put("DURMIOSEISOCHOHORAS", global.replaceCadena(etDurmioSeisOchoHorasNuevoHistorial2.getText().toString()));
            valores.put("ACTIVIDADDIA", global.replaceCadena(etActividadDiaNuevoHistorial2.getText().toString()));
            valores.put("PROBLEMASOJOS", global.replaceCadena(etProblemasOjosNuevoHistorial2.getText().toString()));
            valores.put("ID_TRATAMIENTOCOLORTINTE", cbTratamientoTinte2.isChecked() ? idsColorTratamientoTinte[spColorTratamiento2.getSelectedItemPosition()]: "");
            valores.put("ESTILOTINTE", cbTratamientoTinte2.isChecked() ? idsEstiloTratamientoTinte[spEstiloTratamiento2.getSelectedItemPosition()]: "");
            valores.put("POLICARBONATOTIPO", rbPolicarbonato2.isChecked() ? cbPolicarbonatoTipo2.isChecked() ? "1" : "0": "");
            valores.put("POLARIZADO", cbTratamientoPolarizado2.isChecked() ? "1" : "0");
            valores.put("ID_TRATAMIENTOCOLORPOLARIZADO", cbTratamientoPolarizado2.isChecked() ? idsColorTratamientoPolarizado[spColorTratamientoPolarizado2.getSelectedItemPosition()]: "");
            valores.put("ESPEJO", cbTratamientoEspejeado2.isChecked() ? "1" : "0");
            valores.put("ID_TRATAMIENTOCOLORESPEJO", cbTratamientoEspejeado2.isChecked() ? idsColorTratamientoEspejo[spColorTratamientoEspejeado2.getSelectedItemPosition()]: "");
            valores.put("FOTOARMAZON", fotoArmazonPropio);
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT", fechaActual + time);
            valores.put("UPDATED_AT", fechaActual + time);
            sqLiteDB.insert("HISTORIALCLINICO",null, valores);
            sqLiteDB.close();

            if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 1 || spPaquetesNuevoHistorial2.getSelectedItemPosition() == 6) {
                //LECTURA o DORADO 2
                guardarHistorialSinConversion(ultimoIdContratoCreado, idAlfanumerico);
            }

            //Registrar historial clinico archivo historialesclinicos.txt
            global.escribirNuevoContratoOHistorialClinicoEnArchivoTXT(fechaActual, ultimoIdContratoCreado, idAlfanumerico, 2);

            //Mostrar alerta para ingresar numero de referencia para cita agendada
            global.mostrarAlertDialogIngresarReferenciaCitaPrevia(ultimoIdContratoCreado);

            Toast.makeText(fragmento.getContext(), "Se creo correctamente el segundo historial", Toast.LENGTH_LONG).show();

            //Sincronizar
            llamadaSincronizacion();

            //Mandar a vista de verContrato
            Fragment verificadorFragment = new verContrato();
            Bundle bundle = new Bundle();
            bundle.putString("ultimoIdContratoCreado", ultimoIdContratoCreado);
            verificadorFragment.setArguments(bundle);
            FragmentTransaction transaction = ((FragmentActivity)fragmento.getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, verificadorFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack(null);
            transaction.commit();

        }catch (SQLiteException e){
            global.escribirError(e, 146);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void guardarHistorialSinConversion(String idContrato, String idAlfanumerico) {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID_CONTRATO", idContrato);
            valores.put("ID_HISTORIAL", idAlfanumerico);
            valores.put("ESFERICODER", etOjoDerechoEsfericoSinConversionNuevoHistorial2.getText().toString());
            valores.put("CILINDRODER", etOjoDerechoCilindroSinConversionNuevoHistorial2.getText().toString());
            valores.put("EJEDER", etOjoDerechoEjeSinConversionNuevoHistorial2.getText().toString());
            valores.put("ADDDER", etOjoDerechoAddSinConversionNuevoHistorial2.getText().toString());
            valores.put("ESFERICOIZQ", etOjoIzquierdoEsfericoSinConversionNuevoHistorial2.getText().toString());
            valores.put("CILINDROIZQ", etOjoIzquierdoCilindroSinConversionNuevoHistorial2.getText().toString());
            valores.put("EJEIZQ", etOjoIzquierdoEjeSinConversionNuevoHistorial2.getText().toString());
            valores.put("ADDIZQ", etOjoIzquierdoAddSinConversionNuevoHistorial2.getText().toString());
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT", fechaActual + " " + time);
            sqLiteDB2.insert("HISTORIALSINCONVERSION", null, valores);
            sqLiteDB2.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 147);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: obtenerPrecioTotalHistorial
     Descripcion: Obtener precioTotalHistorial de lo nuevo que se agrego en el segundo historial
   */
    private double obtenerPrecioTotalHistorial(double precioOtroMaterial) {

        double precioTotalHistorial = 0;

        try {

            sqLiteDB = conexion.getReadableDatabase();

            String consulta = "SELECT PRECIO FROM TRATAMIENTOS";
            Cursor datos = sqLiteDB.rawQuery(consulta, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay tratamientos");
            }

            if(datos.getCount()>0){

                double totalPrecioTratamientos = 0;
                String[] preciosTratamientos = new String[datos.getCount()];

                for(int i=0; i < datos.getCount(); i++){

                    preciosTratamientos[i] = datos.getString(0);
                    datos.moveToNext();

                }

                if(cbTratamientoFotocromatico2.isChecked() || cbTratamientoAR2.isChecked() || cbTratamientoTinte2.isChecked()
                        || cbTratamientoBlueray2.isChecked() || cbTratamientoOtro2.isChecked() || cbTratamientoEspejeado2.isChecked() || cbTratamientoPolarizado2.isChecked()) {

                    if(cbTratamientoFotocromatico2.isChecked()) {
                        totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[0]);
                    }

                    if(cbTratamientoAR2.isChecked()) {
                        totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[1]);
                    }

                    if(cbTratamientoTinte2.isChecked()) {
                        totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[2]);
                    }

                    if(cbTratamientoBlueray2.isChecked()) {
                        totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[3]);
                    }

                    if (cbTratamientoPolarizado2.isChecked()) {
                        //Si cbTratamientoPolarizado esta chequeado
                        totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[4]);
                    }

                    if (cbTratamientoEspejeado2.isChecked()) {
                        //Si cbTratamientoEspejeado esta chequeado
                        totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[5]);
                    }

                    if(cbTratamientoOtro2.isChecked()) {
                        totalPrecioTratamientos += Double.parseDouble(etPrecioTratamientoNuevoHistorial2.getText().toString());
                    }

                }

                precioTotalHistorial = totalPrecioTratamientos + precioOtroMaterial;


            }

            sqLiteDB.close();
            datos.close();

            return precioTotalHistorial;

        }catch (SQLiteException e) {
            global.escribirError(e, 148);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return precioTotalHistorial;

    }

    /*Metodo/Funcion: decrementarProductoSeleccionadoBD
     Descripcion: Decrementar una pieza al producto que fue seleccionado
   */
    private void decrementarProductoSeleccionadoBD() {

        try {

            sqLiteDB = conexion.getWritableDatabase();
            String consulta = "UPDATE PRODUCTO SET PIEZAS = CAST(PIEZAS AS INTEGER) - 1 WHERE ID='" + idsProductos.get(spProductoNuevoHistorial2.getSelectedItemPosition()) + "'";
            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 149);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: verificarComponentesIguales
     Descripcion: Validacion de componentes iguales en el paquete Dorado 2
   */
    private void verificarComponentesIguales() {

        if(idsProductos.get(spProductoNuevoHistorial2.getSelectedItemPosition()) == ""){
            arregloComponentes[0] = true;
        }

        if(etFechaEntregaNuevoHistorial2.getText().toString().equals("")){
            arregloComponentes[1] = true;
        }
    }

    /*Metodo/Funcion: verificarComponentesDiferentesPaqueteUno
     Descripcion: Validacion de componentes diferentes en el paquete Dorado 2
   */
    private void verificarComponentesDiferentesPaqueteUno() {

        if(etOjoDerechoEsfericoNuevoHistorial2.getText().toString().equals("") || etOjoDerechoEsfericoNuevoHistorial2.getText().toString().equals("+")
                || etOjoDerechoEsfericoNuevoHistorial2.getText().toString().equals("-")){
            arregloComponentes[2] = true;
        }

        if(etOjoDerechoCilindroNuevoHistorial2.getText().toString().equals("") || etOjoDerechoCilindroNuevoHistorial2.getText().toString().equals("+")
                || etOjoDerechoCilindroNuevoHistorial2.getText().toString().equals("-")){
            arregloComponentes[3] = true;
        }

        if(etOjoDerechoEjeNuevoHistorial2.getText().toString().equals("")){
            arregloComponentes[4] = true;
        }

        if(etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString().equals("") || etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString().equals("+")
                || etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString().equals("-")){
            arregloComponentes[7] = true;
        }

        if(etOjoIzquierdoCilindroNuevoHistorial2.getText().toString().equals("") || etOjoIzquierdoCilindroNuevoHistorial2.getText().toString().equals("+")
                || etOjoIzquierdoCilindroNuevoHistorial2.getText().toString().equals("-")){
            arregloComponentes[8] = true;
        }

        if(etOjoIzquierdoEjeNuevoHistorial2.getText().toString().equals("")){
            arregloComponentes[9] = true;
        }

        if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 1 || spPaquetesNuevoHistorial2.getSelectedItemPosition() == 6) { //LECTURA o DORADO 2
            //Validar si son diferente de vacio los valores de historialsinconversion
            if(etOjoDerechoEsfericoSinConversionNuevoHistorial2.getText().toString().equals("") || etOjoDerechoCilindroSinConversionNuevoHistorial2.getText().toString().equals("")
                    || etOjoDerechoEjeSinConversionNuevoHistorial2.getText().toString().equals("") || etOjoDerechoAddSinConversionNuevoHistorial2.getText().toString().equals("")
                    || etOjoIzquierdoEsfericoSinConversionNuevoHistorial2.getText().toString().equals("") || etOjoIzquierdoCilindroSinConversionNuevoHistorial2.getText().toString().equals("")
                    || etOjoIzquierdoEjeSinConversionNuevoHistorial2.getText().toString().equals("") || etOjoIzquierdoAddSinConversionNuevoHistorial2.getText().toString().equals("")) {
                arregloComponentes[17] = true;
            }
        }

    }

    /*Metodo/Funcion: verificarCheckBoxsRadioButtons
     Descripcion: Validacion de CheckBoxs y RadioButtons
   */
    private void verificarCheckBoxsRadioButtons() {

        //MATERIAL
        if(!rbHiIndex2.isChecked() && !rbCR2.isChecked() && !rbPolicarbonato2.isChecked() && !rbOtroMaterial2.isChecked()){
            tvPrecioMaterialNuevoHistorial2.setText("Selecciona al menos una opción");
            arregloComponentes[13] = true;
        }

        if(rbHiIndex2.isChecked() || rbCR2.isChecked() || rbPolicarbonato2.isChecked() || rbOtroMaterial2.isChecked()) {
            if(rbOtroMaterial2.isChecked()) {
                if(etOtroMaterialNuevoHistorial2.getText().toString().equals("")) {
                    arregloComponentes[12] = true;
                }
            }
        }

        if(rbOtroMaterial2.isChecked()) {
            if(etPrecioMaterialNuevoHistorial2.getText().toString().equals("")) {
                tvPrecioMaterialNuevoHistorial2.setText("Campo vacío");
                arregloComponentes[13] = true;
            }
        }

        //TRATAMIENTOS
        if(!cbTratamientoFotocromatico2.isChecked() && !cbTratamientoAR2.isChecked() && !cbTratamientoTinte2.isChecked() && !cbTratamientoBlueray2.isChecked()
                && !cbTratamientoEspejeado2.isChecked() && !cbTratamientoPolarizado2.isChecked() && !cbTratamientoOtro2.isChecked()){
            tvPrecioTratamientoNuevoHistorial2.setText("Selecciona al menos una opción");
            arregloComponentes[15] = true;
        }

        if(cbTratamientoFotocromatico2.isChecked() || cbTratamientoAR2.isChecked() || cbTratamientoTinte2.isChecked()
                || cbTratamientoBlueray2.isChecked() || cbTratamientoEspejeado2.isChecked() || cbTratamientoPolarizado2.isChecked() || cbTratamientoOtro2.isChecked()) {
            if(cbTratamientoOtro2.isChecked()) {
                if(etOtroTratamientoNuevoHistorial2.getText().toString().equals("")) {
                    arregloComponentes[14] = true;
                }
            }
        }

        if(cbTratamientoOtro2.isChecked()) {
            if(etPrecioTratamientoNuevoHistorial2.getText().toString().equals("")) {
                tvPrecioTratamientoNuevoHistorial2.setText("Campo vacío");
                arregloComponentes[15] = true;
            }
        }

        //TIPO BIFOCAL
        if(!rbFT2.isChecked() && !rbBlend2.isChecked() && !rbProgresivo2.isChecked() && !rbNA2.isChecked() && !rbOtroBifocal2.isChecked()){
            arregloComponentes[16] = true;
        }

        if(!rbNA2.isEnabled()){
            if(rbNA2.isChecked()){
                rbNA2.setChecked(false);
                arregloComponentes[16] = true;
            }
        }

        if(rbFT2.isChecked() || rbBlend2.isChecked() || rbProgresivo2.isChecked() || rbNA2.isChecked() || rbOtroBifocal2.isChecked()) {
            if(rbOtroBifocal2.isChecked()) {
                if(etOtroBifocalNuevoHistorial2.getText().toString().equals("")) {
                    arregloComponentes[18] = true;
                }
            }
        }

        if(rbOtroBifocal2.isChecked()) {
            if(etPrecioBifocalNuevoHistorial2.getText().toString().equals("")) {
                arregloComponentes[19] = true;
            }
        }

        //TRATAMIENTO TINTE COLOR Y ESTILO
        if(cbTratamientoTinte2.isChecked()){
            if (idsColorTratamientoTinte[spColorTratamiento2.getSelectedItemPosition()].equals("")){
                arregloComponentes[20] = true;
            }
            if (idsEstiloTratamientoTinte[spEstiloTratamiento2.getSelectedItemPosition()].equals("")){
                arregloComponentes[21] = true;
            }
        }

        //TRATAMIENTO POLARIZADO
        if(cbTratamientoPolarizado2.isChecked()){
            if (idsColorTratamientoPolarizado[spColorTratamientoPolarizado2.getSelectedItemPosition()].equals("")){
                arregloComponentes[22] = true;
            }
        }

        //TRATAMIENTO ESPEJO
        if(cbTratamientoEspejeado2.isChecked()){
            if (idsColorTratamientoEspejo[spColorTratamientoEspejeado2.getSelectedItemPosition()].equals("")){
                arregloComponentes[23] = true;
            }
        }

        //ARMAZON PROPIO IMAGEN
        if(esArmazonPropio && ivFotoArmazonPropioHistorial2.getDrawable() == null) {
            arregloComponentes[24] = true;
        }

    }

    /*Metodo/Funcion: validarArreglo
     Descripcion: Validacion de componentes, para verificar si esta correctos o no, si esta correcto retorna true si no false
   */
    private boolean validarArreglo(int i) {

        switch (i) {
            case 0: //tvProductoNuevoHistorial
                if(!arregloComponentes[0])
                    return true;
                else
                    return false;
            case 1: //tvFechaEntregaNuevoHistorial
                if(!arregloComponentes[1])
                    return true;
                else
                    return false;
            case 2: //tvOjoDerechoEsfericoNuevoHistorial
                if(!arregloComponentes[2])
                    return true;
                else
                    return false;
            case 3: //tvOjoDerechoCilindroNuevoHistorial
                if(!arregloComponentes[3])
                    return true;
                else
                    return false;
            case 4: //tvOjoDerechoEjeNuevoHistorial
                if(!arregloComponentes[4])
                    return true;
                else
                    return false;
            case 5: //tvOjoDerechoAddNuevoHistorial
                if(!arregloComponentes[5])
                    return true;
                else
                    return false;
            case 6: //tvOjoDerechoALTNuevoHistorial
                if(!arregloComponentes[6])
                    return true;
                else
                    return false;
            case 7: //tvOjoIzquierdoEsfericoNuevoHistorial
                if(!arregloComponentes[7])
                    return true;
                else
                    return false;
            case 8: //tvOjoIzquierdoCilindroNuevoHistorial
                if(!arregloComponentes[8])
                    return true;
                else
                    return false;
            case 9: //tvOjoIzquierdoEjeNuevoHistorial
                if(!arregloComponentes[9])
                    return true;
                else
                    return false;
            case 10: //tvOjoIzquierdoAddNuevoHistorial
                if(!arregloComponentes[10])
                    return true;
                else
                    return false;
            case 11: //tvOjoIzquierdoALTNuevoHistorial
                if(!arregloComponentes[11])
                    return true;
                else
                    return false;
            case 12: //tvOtroMaterialNuevoHistorial
                if(rbHiIndex2.isChecked() || rbCR2.isChecked() || rbPolicarbonato2.isChecked() || rbOtroMaterial2.isChecked()) {
                    if(rbOtroMaterial2.isChecked()) {
                        if(etOtroMaterialNuevoHistorial2.getText().toString().equals("")) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            case 13: //tvPrecioMaterialNuevoHistorial
                if(rbOtroMaterial2.isChecked()) {
                    if(etPrecioMaterialNuevoHistorial2.getText().toString().equals("")) {
                        return false;
                    }
                }
                return true;
            case 14: //tvOtroTratamientoNuevoHistorial
                if(cbTratamientoFotocromatico2.isChecked() || cbTratamientoAR2.isChecked() || cbTratamientoTinte2.isChecked()
                        || cbTratamientoPolarizado2.isChecked() || cbTratamientoEspejeado2.isChecked() || cbTratamientoBlueray2.isChecked() || cbTratamientoOtro2.isChecked()) {
                    if(cbTratamientoOtro2.isChecked()) {
                        if(etOtroTratamientoNuevoHistorial2.getText().toString().equals("")) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            case 15: //tvPrecioTratamientoNuevoHistorial
                if(cbTratamientoOtro2.isChecked()) {
                    if(etPrecioTratamientoNuevoHistorial2.getText().toString().equals("")) {
                        return false;
                    }
                }
                return true;
            case 16: //tvTipoBifocalNuevoHistorial
                if(rbFT2.isChecked() || rbBlend2.isChecked() || rbProgresivo2.isChecked() || rbNA2.isChecked() || rbOtroBifocal2.isChecked()) {
                    return true;
                }
                return false;
            case 17: //tvGeneralSinConversionNuevoHistorial
                if(!arregloComponentes[17])
                    return true;
                else
                    return false;
            case 18: //tvOtroBifocalNuevoHistorial
                if(rbFT2.isChecked() || rbBlend2.isChecked() || rbProgresivo2.isChecked() || rbNA2.isChecked() || rbOtroBifocal2.isChecked()) {
                    if(rbOtroBifocal2.isChecked()) {
                        if(etOtroBifocalNuevoHistorial2.getText().toString().equals("")) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            case 19: //tvPrecioBifocalNuevoHistorial
                if(rbOtroBifocal2.isChecked()) {
                    if(etPrecioBifocalNuevoHistorial2.getText().toString().equals("")) {
                        return false;
                    }
                }
                return true;
            case 20: //tvColorTratamiento2
                if(!arregloComponentes[20])
                    return true;
                else
                    return false;
            case 21: //tvEstiloTratamiento2
                if(!arregloComponentes[21])
                    return true;
                else
                    return false;
            case 22: //tvColorTratamientoPolarizado2
                if(!arregloComponentes[22])
                    return true;
                else
                    return false;
            case 23: //tvColorTratamientoEspejo2
                if(!arregloComponentes[23])
                    return true;
                else
                    return false;
            case 24: //tvFotoArmazonPropioHistorial2
                if(esArmazonPropio){
                    if(!arregloComponentes[24])
                        return true;
                    else
                        return false;
                }else {
                    return true;
                }
        }

        return false;
    }

    /*Metodo/Funcion: mostrarOcultarMensajesError
     Descripcion: Validacion de mostrar o ocultar el mensaje de error en los TextViews correspondientes
   */
    private void mostrarOcultarMensajesError(int i, boolean mostrarOcultar){

        switch (i) {
            case 0:
                if(arregloComponentes[0] && mostrarOcultar) {
                    tvProductoNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvProductoNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 1:
                if(arregloComponentes[1] && mostrarOcultar) {
                    tvFechaEntregaNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvFechaEntregaNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 2:
                if(arregloComponentes[2] && mostrarOcultar) {
                    tvOjoDerechoEsfericoNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOjoDerechoEsfericoNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 3:
                if(arregloComponentes[3] && mostrarOcultar) {
                    tvOjoDerechoCilindroNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOjoDerechoCilindroNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 4:
                if(arregloComponentes[4] && mostrarOcultar) {
                    tvOjoDerechoEjeNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOjoDerechoEjeNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 5:
                if(arregloComponentes[5] && mostrarOcultar) {
                    tvOjoDerechoAddNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOjoDerechoAddNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 6:
                if(arregloComponentes[6] && mostrarOcultar) {
                    tvOjoDerechoALTNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOjoDerechoALTNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 7:
                if(arregloComponentes[7] && mostrarOcultar) {
                    tvOjoIzquierdoEsfericoNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOjoIzquierdoEsfericoNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 8:
                if(arregloComponentes[8] && mostrarOcultar) {
                    tvOjoIzquierdoCilindroNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOjoIzquierdoCilindroNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 9:
                if(arregloComponentes[9] && mostrarOcultar) {
                    tvOjoIzquierdoEjeNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOjoIzquierdoEjeNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 10:
                if(arregloComponentes[10] && mostrarOcultar) {
                    tvOjoIzquierdoAddNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOjoIzquierdoAddNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 11:
                if(arregloComponentes[11] && mostrarOcultar) {
                    tvOjoIzquierdoALTNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOjoIzquierdoALTNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 12:
                if(arregloComponentes[12] && mostrarOcultar) {
                    tvOtroMaterialNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOtroMaterialNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 13:
                if(arregloComponentes[13] && mostrarOcultar) {
                    tvPrecioMaterialNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvPrecioMaterialNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 14:
                if(arregloComponentes[14] && mostrarOcultar) {
                    tvOtroTratamientoNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOtroTratamientoNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 15:
                if(arregloComponentes[15] && mostrarOcultar) {
                    tvPrecioTratamientoNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvPrecioTratamientoNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 16:
                if(arregloComponentes[16] && mostrarOcultar) {
                    tvTipoBifocalNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvTipoBifocalNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 17:
                if(arregloComponentes[17] && mostrarOcultar) {
                    tvGeneralSinConversionNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvGeneralSinConversionNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 18:
                if(arregloComponentes[18] && mostrarOcultar) {
                    tvOtroBifocalNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvOtroBifocalNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 19:
                if(arregloComponentes[19] && mostrarOcultar) {
                    tvPrecioBifocalNuevoHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvPrecioBifocalNuevoHistorial2.setVisibility(View.GONE);
                }
                break;
            case 20:
                if(arregloComponentes[20] && mostrarOcultar){
                    tvColorTratamiento2.setVisibility(View.VISIBLE);
                }else{
                    tvColorTratamiento2.setVisibility(View.GONE);
                }
                break;
            case 21:
                if(arregloComponentes[21] && mostrarOcultar){
                    tvEstiloTratamiento2.setVisibility(View.VISIBLE);
                }else{
                    tvEstiloTratamiento2.setVisibility(View.GONE);
                }
                break;
            case 22:
                if(arregloComponentes[22] && mostrarOcultar){
                    tvColorTratamientoPolarizado2.setVisibility(View.VISIBLE);
                }else{
                    tvColorTratamientoPolarizado2.setVisibility(View.GONE);
                }
                break;
            case 23:
                if(arregloComponentes[23] && mostrarOcultar){
                    tvColorTratamientoEspejeado2.setVisibility(View.VISIBLE);
                }else{
                    tvColorTratamientoEspejeado2.setVisibility(View.GONE);
                }
                break;
            case 24:
                if(arregloComponentes[24] && mostrarOcultar) {
                    tvFotoArmazonPropioHistorial2.setVisibility(View.VISIBLE);
                }else {
                    tvFotoArmazonPropioHistorial2.setVisibility(View.GONE);
                }
                break;
        }

    }

    /*Metodo/Funcion: llenarCamposContrato
     Descripcion: Consulta los datos del contrato, se obtienen y se muestran en los campos correspondientes en la vista
   */
    private void llenarCamposContrato() {

        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT NOMBRE FROM CONTRATOS" +
                    " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No hay contrato registrado");
            }

            if (datos.moveToFirst()) {
                etNombreCliente2.setText(datos.getString(0).toUpperCase());
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 150);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: llenarCamposHistorial
     Descripcion: Consulta los datos del primer historial, se obtienen y se muestran en los campos correspondientes en la vista
   */
    private void llenarCamposHistorial() {

        Arrays.fill(arregloTratamientos, Boolean.FALSE);

        boolean historialTipoGarantia = global.obtenerAtributoTabla("HISTORIALCLINICO", "TIPO", "ID", ultimoIdHistorialClinicoCreado).equals("1");

        String idPaquete = "";

        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT EDAD, DIAGNOSTICO, OCUPACION, DIABETES, HIPERTENSION, DOLOR, ARDOR, GOLPEOJOS," +
                    " OTROM, MOLESTIAOTRO, ULTIMOEXAMEN, ID_PAQUETE, ID_PRODUCTO, FECHAENTREGA, ESFERICODER," +
                    " CILINDRODER, EJEDER, ADDDER, ALTDER, ESFERICOIZQ, CILINDROIZQ, EJEIZQ, ADDIZQ," +
                    " ALTIZQ, MATERIAL, MATERIALOTRO, COSTOMATERIAL, FOTOCROMATICO, AR, TINTE, BLUERAY," +
                    " OTROT, TRATAMIENTOOTRO, COSTOTRATAMIENTO, BIFOCAL, OBSERVACIONES, OBSERVACIONESINTERNO," +
                    " BIFOCALOTRO, COSTOBIFOCAL, EMBARAZADA, DURMIOSEISOCHOHORAS, ACTIVIDADDIA, PROBLEMASOJOS, " +
                    " POLICARBONATOTIPO, ID_TRATAMIENTOCOLORTINTE, ESTILOTINTE, POLARIZADO, ID_TRATAMIENTOCOLORPOLARIZADO, " +
                    " ESPEJO, ID_TRATAMIENTOCOLORESPEJO FROM HISTORIALCLINICO " +
                    " WHERE ID='" + ultimoIdHistorialClinicoCreado + "' AND ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                //No hay datos
                Log.i("MENSAJE", "No hay historial registrado");
            }

            if (datos.moveToFirst()) {
                //Si hay datos

                idPaquete = datos.getString(11);

                if(modificarHistorialClinico) {
                    //Modificar historial

                    //Obtener datos del contrato y mostrarlos en los campos de texto
                    if(esHistorialGarantia) {
                        // Es historial con garantia
                        if(!historialTipoGarantia) {
                            //Es crear garantia
                            btnGuardarHistorialNuevo2.setText("CREAR GARANTÍA");
                        }else {
                            //Es actualizar garantia
                            btnGuardarHistorialNuevo2.setText("ACTUALIZAR GARANTÍA");
                        }
                    }else {
                        // No es historial con garantia
                        btnGuardarHistorialNuevo2.setText("ACTUALIZAR");
                    }
                    etEdadNuevoHistorial2.setText(datos.getString(0));
                    etDiagnosticoNuevoHistorial2.setText(datos.getString(1));
                    etOcupacionNuevoHistorial2.setText(datos.getString(2));
                    etDiabetesNuevoHistorial2.setText(datos.getString(3));
                    etHipertensionNuevoHistorial2.setText(datos.getString(4));

                    etEmbarazadaNuevoHistorial2.setText(datos.getString(39));
                    etDurmioSeisOchoHorasNuevoHistorial2.setText(datos.getString(40));
                    etActividadDiaNuevoHistorial2.setText(datos.getString(41));
                    etProblemasOjosNuevoHistorial2.setText(datos.getString(42));

                    if(Integer.parseInt(datos.getString(5)) > 0) {
                        //Si cbDolorCabeza2 fue chequeado
                        cbDolorCabeza2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(6)) > 0) {
                        //Si cbArdorOjos2 fue chequeado
                        cbArdorOjos2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(7)) > 0) {
                        //Si cbGolpeCabeza2 fue chequeado
                        cbGolpeCabeza2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(8)) > 0) {
                        //Si cbOtroMolestia2 fue chequeado
                        cbOtroMolestia2.setChecked(true);
                        etOtroMoletiaNuevoHistorial2.setText(datos.getString(9));
                    }

                    etUltimoExamenNuevoHistorial2.setText(datos.getString(10));
                    spPaquetesNuevoHistorial2.setSelection(Arrays.asList(idsPaquetes).indexOf(idPaquete));
                    spPaquetesNuevoHistorial2.setEnabled(false);

                    llenarSpinnerProductosBD(datos.getString(12));

                    spProductoNuevoHistorial2.setSelection(idsProductos.indexOf(datos.getString(12)));
                    spProductoNuevoHistorial2.setEnabled(false);

                    etFechaEntregaNuevoHistorial2.setText(datos.getString(13));

                    etOjoDerechoEsfericoNuevoHistorial2.setText(datos.getString(14));

                    etOjoDerechoCilindroNuevoHistorial2.setText(datos.getString(15));

                    etOjoDerechoEjeNuevoHistorial2.setText(datos.getString(16));

                    etOjoDerechoAddNuevoHistorial2.setText(datos.getString(17));

                    etOjoDerechoALTNuevoHistorial2.setText(datos.getString(18));

                    etOjoIzquierdoEsfericoNuevoHistorial2.setText(datos.getString(19));

                    etOjoIzquierdoCilindroNuevoHistorial2.setText(datos.getString(20));

                    etOjoIzquierdoEjeNuevoHistorial2.setText(datos.getString(21));

                    etOjoIzquierdoAddNuevoHistorial2.setText(datos.getString(22));

                    etOjoIzquierdoALTNuevoHistorial2.setText(datos.getString(23));

                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 1) { //LECTURA
                        if(Double.parseDouble(datos.getString(15)) != 0 || Double.parseDouble(datos.getString(20)) != 0
                            || Double.parseDouble(datos.getString(14)) > 5.25 || Double.parseDouble(datos.getString(19)) > 5.25) {
                            precioCilindroLectura = 590;
                        }else {
                            precioCilindroLectura = 0;
                        }
                    }else {
                        precioCilindroLectura = 0;
                    }

                    //Se obtiene el idPaqueteSeleccionado
                    idPaqueteSeleccionado = datos.getString(11);

                    if(Integer.parseInt(datos.getString(24)) == 0) {
                        //Si rbHiIndex2 fue chequeado
                        rbHiIndex2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(24)) == 1) {
                        //Si rbCR2 fue chequeado
                        rbCR2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(24)) == 2) {
                        //Si rbPolicarbonato2 fue chequeado
                        rbPolicarbonato2.setChecked(true);
                        cbPolicarbonatoTipo2.setVisibility(View.VISIBLE);
                        policarbonatoBD = true;
                        //CbPolarizadoTipo
                        String valorPolicarbonatoTipo = datos.getString(43);
                        valorPolicarbonatoTipo = (valorPolicarbonatoTipo.equals(""))? "0": valorPolicarbonatoTipo;
                        if (Integer.parseInt(valorPolicarbonatoTipo) > 0){
                            cbPolicarbonatoTipo2.setChecked(true);
                            cbPolicarbonatoTipo2.setText("Niño");
                        }else{
                            //Policarbonato para adulto
                            cbPolicarbonatoTipo2.setChecked(false);
                            cbPolicarbonatoTipo2.setText("Adulto");
                            precioMaterial = precioPolicarbonato;
                        }
                    }
                    if(Integer.parseInt(datos.getString(24)) == 3) {
                        //Si rbOtroMaterial2 fue chequeado
                        rbOtroMaterial2.setChecked(true);
                        etOtroMaterialNuevoHistorial2.setText(datos.getString(25));
                        etPrecioMaterialNuevoHistorial2.setText(datos.getString(26));
                        etOtroMaterialNuevoHistorial2.setEnabled(true);
                        etPrecioMaterialNuevoHistorial2.setEnabled(true);
                        precioMaterialOtro = Double.parseDouble(datos.getString(26));
                    }

                    if(Integer.parseInt(datos.getString(27)) > 0) {
                        //Si cbTratamientoFotocromatico2 fue chequeado
                        cbTratamientoFotocromatico2.setChecked(true);
                        arregloTratamientos[0] = true;
                    }
                    if(Integer.parseInt(datos.getString(28)) > 0) {
                        //Si cbTratamientoAR2 fue chequeado
                        cbTratamientoAR2.setChecked(true);
                        arregloTratamientos[1] = true;
                    }
                    if(Integer.parseInt(datos.getString(29)) > 0) {
                        //Si cbTratamientoTinte2 fue chequeado
                        cbTratamientoTinte2.setChecked(true);
                        llColorEstiloTinte2.setVisibility(View.VISIBLE);
                        spColorTratamiento2.setSelection(Arrays.asList(idsColorTratamientoTinte).indexOf(datos.getString(44)));
                        spEstiloTratamiento2.setSelection(Arrays.asList(idsEstiloTratamientoTinte).indexOf(datos.getString(45)));
                        arregloTratamientos[2] = true;
                    }

                    if(Integer.parseInt(datos.getString(30)) > 0) {
                        //Si cbTratamientoBlueray2 fue chequeado
                        cbTratamientoBlueray2.setChecked(true);
                        arregloTratamientos[3] = true;
                    }

                    String valorTratamientoPolarizado = datos.getString(46);
                    valorTratamientoPolarizado = (valorTratamientoPolarizado.equals(""))? "0": valorTratamientoPolarizado;
                    if(Integer.parseInt(valorTratamientoPolarizado) > 0){
                        //Si cbPolarizado fue chequeado
                        cbTratamientoPolarizado2.setChecked(true);
                        llTratamientoPolarizado2.setVisibility(View.VISIBLE);
                        spColorTratamientoPolarizado2.setSelection(Arrays.asList(idsColorTratamientoPolarizado).indexOf(datos.getString(47)));
                        arregloTratamientos[5] = true;
                    }

                    String valorTratamientoEspejo = datos.getString(48);
                    valorTratamientoEspejo = (valorTratamientoEspejo.equals(""))? "0": valorTratamientoEspejo;
                    if(Integer.parseInt(valorTratamientoEspejo) > 0){
                        //Si cbEspejo fue chequeado
                        cbTratamientoEspejeado2.setChecked(true);
                        llTratamientoEspejeado2.setVisibility(View.VISIBLE);
                        spColorTratamientoEspejeado2.setSelection(Arrays.asList(idsColorTratamientoEspejo).indexOf(datos.getString(49)));
                        arregloTratamientos[6] = true;
                    }

                    if(Integer.parseInt(datos.getString(31)) > 0) {
                        //Si cbTratamientoOtro2 fue chequeado
                        cbTratamientoOtro2.setChecked(true);
                        arregloTratamientos[4] = true;
                        etOtroTratamientoNuevoHistorial2.setText(datos.getString(32));
                        etPrecioTratamientoNuevoHistorial2.setText(datos.getString(33));
                        precioTratamientoOtro = Double.parseDouble(datos.getString(33));
                    }

                    if(Integer.parseInt(datos.getString(34)) == 0) {
                        //Si rbFT2 fue chequeado
                        rbFT2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(34)) == 1) {
                        //Si rbBlend2 fue chequeado
                        rbBlend2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(34)) == 2) {
                        //Si rbProgresivo2 fue chequeado
                        rbProgresivo2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(34)) == 3) {
                        //Si rbNA2 fue chequeado
                        rbNA2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(34)) == 4) {
                        //Si rbOtroBifocal2 fue chequeado
                        rbOtroBifocal2.setChecked(true);
                        etOtroBifocalNuevoHistorial2.setText(datos.getString(37));
                        etPrecioBifocalNuevoHistorial2.setText(datos.getString(38));
                        etOtroBifocalNuevoHistorial2.setEnabled(true);
                        etPrecioBifocalNuevoHistorial2.setEnabled(true);
                        precioBifocalOtro = Double.parseDouble(datos.getString(38));
                        otroBifocalBD = datos.getString(37);
                        otroBifocalPrecioBD = datos.getString(38);
                    }

                    bifocalBD = datos.getString(34);

                    etObservacionesNuevoHistorial2.setText(datos.getString(35));
                    etObservacionesInternoNuevoHistorial2.setText(datos.getString(36));

                    etOjoDerechoEsfericoNuevoHistorial2.setHint("");
                    etOjoDerechoCilindroNuevoHistorial2.setHint("");
                    etOjoDerechoEjeNuevoHistorial2.setHint("");
                    etOjoIzquierdoEsfericoNuevoHistorial2.setHint("");
                    etOjoIzquierdoCilindroNuevoHistorial2.setHint("");
                    etOjoIzquierdoEjeNuevoHistorial2.setHint("");

                    //Validaciones de tratamientos dependiendo del paquete que tiene seleccionado
                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 1) { //LECTURA

                        if(cbTratamientoFotocromatico2.isChecked()) {
                            cbTratamientoAR2.setEnabled(false);
                            cbTratamientoBlueray2.setEnabled(false);
                        }
                        if(cbTratamientoAR2.isChecked()) {
                            cbTratamientoFotocromatico2.setEnabled(false);
                            cbTratamientoBlueray2.setEnabled(false);
                        }
                        if(cbTratamientoBlueray2.isChecked()) {
                            cbTratamientoFotocromatico2.setEnabled(false);
                            cbTratamientoAR2.setEnabled(false);
                        }

                        cbTratamientoTinte2.setEnabled(false);
                        llColorEstiloTinte2.setVisibility(View.GONE);
                        spColorTratamiento2.setSelection(0);
                        spEstiloTratamiento2.setSelection(0);
                        cbTratamientoPolarizado2.setEnabled(false);
                        cbTratamientoEspejeado2.setEnabled(false);
                    }

                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 2) { //PROTECCION
                        if(cbTratamientoBlueray2.isChecked()) {
                            cbTratamientoAR2.setEnabled(false);
                        }

                        cbTratamientoTinte2.setEnabled(false);
                        llColorEstiloTinte2.setVisibility(View.GONE);
                        spColorTratamiento2.setSelection(0);
                        spEstiloTratamiento2.setSelection(0);
                        cbTratamientoPolarizado2.setEnabled(false);
                        cbTratamientoEspejeado2.setEnabled(false);
                    }

                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 3) { //ECO JR
                        if(cbTratamientoBlueray2.isChecked()) {
                            cbTratamientoAR2.setEnabled(false);
                        }
                        if(cbTratamientoTinte2.isChecked()) {
                            cbTratamientoFotocromatico2.setEnabled(false);
                            cbTratamientoAR2.setEnabled(false);
                            cbTratamientoBlueray2.setEnabled(false);
                            llColorEstiloTinte2.setVisibility(View.VISIBLE);
                            spColorTratamiento2.setSelection(Arrays.asList(idsColorTratamientoTinte).indexOf(datos.getString(44)));
                            spEstiloTratamiento2.setSelection(Arrays.asList(idsEstiloTratamientoTinte).indexOf(datos.getString(45)));
                        }
                        cbTratamientoPolarizado2.setEnabled(false);
                        cbTratamientoEspejeado2.setEnabled(false);
                    }

                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 4) { //JR
                        if(cbTratamientoBlueray2.isChecked()) {
                            cbTratamientoAR2.setEnabled(false);
                        }
                        if(cbTratamientoTinte2.isChecked()) {
                            cbTratamientoFotocromatico2.setEnabled(false);
                            cbTratamientoAR2.setEnabled(false);
                            cbTratamientoBlueray2.setEnabled(false);
                            llColorEstiloTinte2.setVisibility(View.VISIBLE);
                            spColorTratamiento2.setSelection(Arrays.asList(idsColorTratamientoTinte).indexOf(datos.getString(44)));
                            spEstiloTratamiento2.setSelection(Arrays.asList(idsEstiloTratamientoTinte).indexOf(datos.getString(45)));
                        }
                        cbTratamientoPolarizado2.setEnabled(false);
                        cbTratamientoEspejeado2.setEnabled(false);
                    }

                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 5) { //DORADO 1
                        if(cbTratamientoBlueray2.isChecked()) {
                            cbTratamientoAR2.setEnabled(false);
                        }
                        if(cbTratamientoTinte2.isChecked()) {
                            cbTratamientoFotocromatico2.setEnabled(false);
                            cbTratamientoAR2.setEnabled(false);
                            cbTratamientoBlueray2.setEnabled(false);
                            llColorEstiloTinte2.setVisibility(View.VISIBLE);
                            spColorTratamiento2.setSelection(Arrays.asList(idsColorTratamientoTinte).indexOf(datos.getString(44)));
                            spEstiloTratamiento2.setSelection(Arrays.asList(idsEstiloTratamientoTinte).indexOf(datos.getString(45)));
                        }
                        cbTratamientoPolarizado2.setEnabled(false);
                        cbTratamientoEspejeado2.setEnabled(false);
                    }

                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 6) { //DORADO 2
                        if(cbTratamientoBlueray2.isChecked()) {
                            cbTratamientoAR2.setEnabled(false);
                        }
                        if(cbTratamientoTinte2.isChecked()) {
                            cbTratamientoFotocromatico2.setEnabled(false);
                            cbTratamientoAR2.setEnabled(false);
                            cbTratamientoBlueray2.setEnabled(false);
                            llColorEstiloTinte2.setVisibility(View.VISIBLE);
                            spColorTratamiento2.setSelection(Arrays.asList(idsColorTratamientoTinte).indexOf(datos.getString(44)));
                            spEstiloTratamiento2.setSelection(Arrays.asList(idsEstiloTratamientoTinte).indexOf(datos.getString(45)));
                        }
                        cbTratamientoPolarizado2.setEnabled(false);
                        cbTratamientoEspejeado2.setEnabled(false);
                    }

                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 7) { //PLATINO
                        if(cbTratamientoBlueray2.isChecked()) {
                            cbTratamientoAR2.setEnabled(false);
                        }
                        if(cbTratamientoTinte2.isChecked()) {
                            cbTratamientoFotocromatico2.setEnabled(false);
                            cbTratamientoAR2.setEnabled(false);
                            cbTratamientoBlueray2.setEnabled(false);
                            llColorEstiloTinte2.setVisibility(View.VISIBLE);
                            spColorTratamiento2.setSelection(Arrays.asList(idsColorTratamientoTinte).indexOf(datos.getString(44)));
                            spEstiloTratamiento2.setSelection(Arrays.asList(idsEstiloTratamientoTinte).indexOf(datos.getString(45)));
                        }
                        cbTratamientoPolarizado2.setEnabled(false);
                        cbTratamientoEspejeado2.setEnabled(false);
                    }

                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == Integer.parseInt(idPaquete)){ //PREMIUM

                        if(cbTratamientoAR2.isChecked()) {
                            cbTratamientoBlueray2.setEnabled(false);
                        }

                        if(cbTratamientoBlueray2.isChecked()) {
                            cbTratamientoAR2.setEnabled(false);
                        }

                        if(cbTratamientoEspejeado2.isChecked()){
                            cbTratamientoPolarizado2.setEnabled(false);
                        }

                        if(cbTratamientoPolarizado2.isChecked()){
                            cbTratamientoEspejeado2.setEnabled(false);
                        }

                        if(cbTratamientoTinte2.isChecked()){
                            cbTratamientoAR2.setEnabled(false);
                            cbTratamientoBlueray2.setEnabled(false);
                            cbTratamientoFotocromatico2.setEnabled(false);
                            etOtroTratamientoNuevoHistorial2.setEnabled(false);
                            etPrecioTratamientoNuevoHistorial2.setEnabled(false);
                        }

                        if(cbTratamientoFotocromatico2.isChecked()){
                            cbTratamientoEspejeado2.setEnabled(false);
                            cbTratamientoPolarizado2.setEnabled(false);
                            cbTratamientoTinte2.setEnabled(false);
                            etOtroTratamientoNuevoHistorial2.setEnabled(false);
                            etPrecioTratamientoNuevoHistorial2.setEnabled(false);
                        }

                    }

                }else {
                    //Segundo historial Dorado 2

                    llVisionCercana.setVisibility(View.VISIBLE);

                    etEdadNuevoHistorial2.setText(datos.getString(0));
                    etDiagnosticoNuevoHistorial2.setText(datos.getString(1));
                    etOcupacionNuevoHistorial2.setText(datos.getString(2));
                    etDiabetesNuevoHistorial2.setText(datos.getString(3));
                    etHipertensionNuevoHistorial2.setText(datos.getString(4));

                    etEmbarazadaNuevoHistorial2.setText(datos.getString(39));
                    etDurmioSeisOchoHorasNuevoHistorial2.setText(datos.getString(40));
                    etActividadDiaNuevoHistorial2.setText(datos.getString(41));
                    etProblemasOjosNuevoHistorial2.setText(datos.getString(42));

                    if(Integer.parseInt(datos.getString(5)) > 0) {
                        //Si cbDolorCabeza2 fue chequeado
                        cbDolorCabeza2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(6)) > 0) {
                        //Si cbArdorOjos2 fue chequeado
                        cbArdorOjos2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(7)) > 0) {
                        //Si cbGolpeCabeza2 fue chequeado
                        cbGolpeCabeza2.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(8)) > 0) {
                        //Si cbOtroMolestia2 fue chequeado
                        cbOtroMolestia2.setChecked(true);
                        etOtroMoletiaNuevoHistorial2.setText(datos.getString(9));
                    }

                    etUltimoExamenNuevoHistorial2.setText(datos.getString(10));
                    spPaquetesNuevoHistorial2.setSelection(Arrays.asList(idsPaquetes).indexOf(idPaquete));
                    spPaquetesNuevoHistorial2.setEnabled(false);

                    llenarSpinnerProductosBD("");

                    //Bloquear o desbloquear algunos campos de texto
                    etOjoDerechoEsfericoNuevoHistorial2.setEnabled(true);
                    etOjoDerechoCilindroNuevoHistorial2.setEnabled(true);
                    etOjoDerechoEjeNuevoHistorial2.setEnabled(true);
                    etOjoDerechoAddNuevoHistorial2.setEnabled(false);
                    etOjoDerechoALTNuevoHistorial2.setEnabled(false);
                    etOjoIzquierdoEsfericoNuevoHistorial2.setEnabled(true);
                    etOjoIzquierdoCilindroNuevoHistorial2.setEnabled(true);
                    etOjoIzquierdoEjeNuevoHistorial2.setEnabled(true);
                    etOjoIzquierdoAddNuevoHistorial2.setEnabled(false);
                    etOjoIzquierdoALTNuevoHistorial2.setEnabled(false);

                    cbTratamientoEspejeado2.setChecked(false);
                    cbTratamientoPolarizado2.setChecked(false);
                    cbTratamientoEspejeado2.setEnabled(false);
                    cbTratamientoPolarizado2.setEnabled(false);

                }
            }

            sqLiteDB.close();
            datos.close();

            if(idPaquete.equals("1") || idPaquete.equals("6")) {
                //LECTURA o DORADO 2
                llenarCamposHistorialSinConversion(ultimoIdHistorialClinicoCreado);
            }

            seleccionarBifocalGuardadoBD();

        }catch (SQLiteException e) {
            global.escribirError(e, 151);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void llenarCamposHistorialSinConversion(String idHistorialClinico) {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ESFERICODER, CILINDRODER, EJEDER, ADDDER, ESFERICOIZQ, CILINDROIZQ, EJEIZQ, ADDIZQ FROM HISTORIALSINCONVERSION" +
                    " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' AND ID_HISTORIAL = '" + idHistorialClinico + "' ORDER BY CREATED_AT DESC LIMIT 1";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                //No hay datos
                Log.i("MENSAJE", "No hay historialsinconversion registrado");
            }

            if (datos.moveToFirst()) {
                //Si hay datos

                //Obtener datos del historialsinconversion y mostrarlos en los campos de texto
                etOjoDerechoEsfericoSinConversionNuevoHistorial2.setText(datos.getString(0));
                etOjoDerechoCilindroSinConversionNuevoHistorial2.setText(datos.getString(1));
                etOjoDerechoEjeSinConversionNuevoHistorial2.setText(datos.getString(2));
                etOjoDerechoAddSinConversionNuevoHistorial2.setText(datos.getString(3));
                etOjoIzquierdoEsfericoSinConversionNuevoHistorial2.setText(datos.getString(4));
                etOjoIzquierdoCilindroSinConversionNuevoHistorial2.setText(datos.getString(5));
                etOjoIzquierdoEjeSinConversionNuevoHistorial2.setText(datos.getString(6));
                etOjoIzquierdoAddSinConversionNuevoHistorial2.setText(datos.getString(7));

                if(!modificarHistorialClinico) {
                    //Segundo historial Dorado 2

                    //Bloquear los campos de texto
                    etOjoDerechoEsfericoSinConversionNuevoHistorial2.setEnabled(false);
                    etOjoDerechoCilindroSinConversionNuevoHistorial2.setEnabled(false);
                    etOjoDerechoEjeSinConversionNuevoHistorial2.setEnabled(false);
                    etOjoDerechoAddSinConversionNuevoHistorial2.setEnabled(false);
                    etOjoIzquierdoEsfericoSinConversionNuevoHistorial2.setEnabled(false);
                    etOjoIzquierdoCilindroSinConversionNuevoHistorial2.setEnabled(false);
                    etOjoIzquierdoEjeSinConversionNuevoHistorial2.setEnabled(false);
                    etOjoIzquierdoAddSinConversionNuevoHistorial2.setEnabled(false);

                    //Cambio de color en las letras de los campos de texto
                    etOjoDerechoEsfericoSinConversionNuevoHistorial2.setTextColor(Color.parseColor("#5C5A5A"));
                    etOjoDerechoCilindroSinConversionNuevoHistorial2.setTextColor(Color.parseColor("#5C5A5A"));
                    etOjoDerechoEjeSinConversionNuevoHistorial2.setTextColor(Color.parseColor("#5C5A5A"));
                    etOjoDerechoAddSinConversionNuevoHistorial2.setTextColor(Color.parseColor("#5C5A5A"));
                    etOjoIzquierdoEsfericoSinConversionNuevoHistorial2.setTextColor(Color.parseColor("#5C5A5A"));
                    etOjoIzquierdoCilindroSinConversionNuevoHistorial2.setTextColor(Color.parseColor("#5C5A5A"));
                    etOjoIzquierdoEjeSinConversionNuevoHistorial2.setTextColor(Color.parseColor("#5C5A5A"));
                    etOjoIzquierdoAddSinConversionNuevoHistorial2.setTextColor(Color.parseColor("#5C5A5A"));

                }

            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 152);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: llenarSpinnerProductosBD
     Descripcion: Obtener id y nombre de los productos de la BD y agregarlos al spProducto
   */
    private void llenarSpinnerProductosBD(String idProducto) {

        try{

            idProductoGarantia = idProducto;

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID, NOMBRE, COLOR, PIEZAS FROM PRODUCTO WHERE ID_TIPOPRODUCTO = '1' ORDER BY NOMBRE";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay productos registrados");
            }

            idsProductos = new ArrayList<>();
            nombresProductos = new ArrayList<>();
            coloresRBGProductos = new int[datos.getCount() + 1];
            idsProductos.add("");
            nombresProductos.add("Seleccionar");
            coloresRBGProductos[0] = Color.WHITE;

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){

                    if(datos.getString(0).contains(idProducto) && idProducto.length() > 0) {
                        //Existe el idProducto entrante pero piezas es menor a 10
                        idsProductos.add(datos.getString(0));
                        nombresProductos.add(datos.getString(1) + " | "+datos.getString(2)+ " | "+datos.getString(3)+"pza.");
                        if(Integer.parseInt(datos.getString(3)) <= 10) {
                            //Producto con menos o igual de 10 piezas
                            coloresRBGProductos[i] = Color.RED;
                        }else{
                            //Producto con mas de 10 piezas
                            coloresRBGProductos[i] = Color.WHITE;
                        }
                    }else {
                        if(Integer.parseInt(datos.getString(3)) > 10) {
                            //El producto tiene piezas mayor a 10
                            idsProductos.add(datos.getString(0));
                            nombresProductos.add(datos.getString(1) + " | "+datos.getString(2)+ " | "+datos.getString(3)+"pza.");
                            coloresRBGProductos[i] = Color.WHITE;
                        }
                        if(Integer.parseInt(datos.getString(3)) <= 10) {
                            //El producto tiene piezas menor o igual a 10
                            idsProductos.add(datos.getString(0));
                            nombresProductos.add(datos.getString(1) + " | "+datos.getString(2)+ " | "+datos.getString(3)+"pza.");
                            coloresRBGProductos[i] = Color.RED;
                        }
                    }

                    datos.moveToNext();
                }

                if(idProducto.length() > 0 && !idsProductos.contains(idProducto)) {
                    //No existe el idProducto entrante
                    idsProductos.set(0, idProducto);
                }

            }

            sqLiteDB.close();
            datos.close();

            //Crear adaptador personalizado para productos
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, nombresProductos) {
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    TextView tvProducto = (TextView) super.getDropDownView(position, convertView, parent);

                    tvProducto.setBackgroundColor(coloresRBGProductos[position]);
                    if(coloresRBGProductos[position] == Color.RED){
                        //Producto con mas de 10 piezas
                        tvProducto.setTextColor(Color.WHITE);
                        tvProducto.setClickable(true);
                    }else{
                        //Producto por agotarse
                        tvProducto.setTextColor(Color.BLACK);
                        tvProducto.setClickable(false);
                    }

                    return tvProducto;
                }
            };

            spProductoNuevoHistorial2.setAdapter(adapter);

        }catch (SQLiteException e){
            global.escribirError(e, 153);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: llenarSpinnerPaqueteBD
     Descripcion: Obtener id y nombre de los paquetes de la BD y agregarlos al spPaquetes
   */
    private void llenarSpinnerPaqueteBD() {

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID, NOMBRE FROM PAQUETES";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay paquetes registrados");
            }

            idsPaquetes = new String[datos.getCount() + 1];
            nombresPaquetes = new String[datos.getCount() + 1];
            idsPaquetes[0] = "";
            nombresPaquetes[0] = "Seleccionar";

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){
                    idsPaquetes[i]= datos.getString(0);
                    nombresPaquetes[i] = datos.getString(1);
                    datos.moveToNext();
                }

            }

            sqLiteDB.close();
            datos.close();

            spPaquetesNuevoHistorial2.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, nombresPaquetes));

        }catch (SQLiteException e){
            global.escribirError(e, 154);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: llenarSpinnerPaqueteBD
    Descripcion: Obtener id y nombre de las caracteristicas del tratamiento tinte
    */
    private void llenarSpinnerColorEstiloTratamiento() {

        //spColorTratamientoTinte
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT INDICE, COLOR FROM TRATAMIENTOSCOLORES WHERE ID_TRATAMIENTO = '5' ORDER BY COLOR ASC";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay colores registrados");
            }

            //spColorTratamientoTinte
            idsColorTratamientoTinte = new String[datos.getCount() + 1];
            colorTratamientoTinte = new String[datos.getCount() + 1];
            idsColorTratamientoTinte[0] = "";
            colorTratamientoTinte[0] = "Seleccionar";

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){
                    idsColorTratamientoTinte[i]= datos.getString(0);
                    colorTratamientoTinte[i] = datos.getString(1);
                    datos.moveToNext();
                }

            }

            sqLiteDB.close();
            datos.close();

            //Llenar spColorTratamiento
            spColorTratamiento2.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, colorTratamientoTinte));

        }catch (SQLiteException e){
            global.escribirError(e, 301);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        //spTratamientoPolarizado
        idsEstiloTratamientoTinte = new String[]{"","0", "1"};
        estiloTratamientoTinte = new String[]{"Seleccionar","DESVANECIDO", "COMPLETO"};
        spEstiloTratamiento2.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, estiloTratamientoTinte));

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT INDICE, COLOR FROM TRATAMIENTOSCOLORES WHERE ID_TRATAMIENTO = '7' ORDER BY COLOR ASC";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay colores registrados");
            }

            //spTratamientoPolarizado
            idsColorTratamientoPolarizado = new String[datos.getCount() + 1];
            colorTratamientoPolarizado = new String[datos.getCount() + 1];
            idsColorTratamientoPolarizado[0] = "";
            colorTratamientoPolarizado[0] = "Seleccionar";

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){
                    idsColorTratamientoPolarizado[i]= datos.getString(0);
                    colorTratamientoPolarizado[i] = datos.getString(1);

                    datos.moveToNext();
                }

            }

            sqLiteDB.close();
            datos.close();

            //Llenar spTratamientoPolarizado
            spColorTratamientoPolarizado2.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, colorTratamientoPolarizado));

        }catch (SQLiteException e){
            global.escribirError(e, 304);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        //spColorTratamientoEspejo
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT INDICE, COLOR FROM TRATAMIENTOSCOLORES WHERE ID_TRATAMIENTO = '8' ORDER BY COLOR ASC";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay colores registrados");
            }

            //spTratamientoEspejo
            idsColorTratamientoEspejo = new String[datos.getCount() + 1];
            colorTratamientoEspejo = new String[datos.getCount() + 1];
            idsColorTratamientoEspejo[0] = "";
            colorTratamientoEspejo[0] = "Seleccionar";

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){
                    idsColorTratamientoEspejo[i]= datos.getString(0);
                    colorTratamientoEspejo[i] = datos.getString(1);

                    datos.moveToNext();
                }
            }

            sqLiteDB.close();
            datos.close();

            //Llenar spTratamientoEspejo
            spColorTratamientoEspejeado2.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, colorTratamientoEspejo));

        }catch (SQLiteException e){
            global.escribirError(e, 305);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: verificarCampoFechaEntregaEstaVacio
     Descripcion: Verificar si campo fecha entrega esta vacio
   */
    public void verificarCampoFechaEntregaEstaVacio() {
        if(verificarPiezasRestantesproduto()){
            if((verificarSpinnerSelected(spProductoNuevoHistorial2) && !esArmazonPropio) && etFechaEntregaNuevoHistorial2.getText().toString().equals("")) {
                showDatePickerDialog(etFechaEntregaNuevoHistorial2);
            }
        }
    }

    /*Metodo/Funcion: verificarSpinnerSelected
     Descripcion: Verificar si spineer a sido seleccionado
   */
    public boolean verificarSpinnerSelected(Spinner spinnerVerificar) {
        if(spinnerVerificar.getSelectedItem().toString() != "Seleccionar") {
            return true;
        }
        return false;
    }

    /*Metodo/Funcion: showDatePickerDialog
     Descripcion: Manda a llamar la clase DatePickerDialog para que muestre el cuadro de diálogo selector de fecha, una vez seleccionada
     mostrar la fecha en el etFechaALlenar
   */
    public void showDatePickerDialog(EditText etFechaALlenar) {

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = year + "-" + twoDigits(month+1) + "-" + twoDigits(day);
                etFechaALlenar.setText(selectedDate);
            }
        });

        newFragment.show(fragmento.getActivity().getSupportFragmentManager(), "datePicker");

    }

    /*Metodo/Funcion: twoDigits
     Descripcion: Se manda a llamar cuando clase DatePickerDialog manda una fecha incompleta - Ejemplo: 9/5/2021
     Su funcion es concatena un 0 para mostrar completa y correcta la fecha en el campo de texto - Ejemplo: 09/05/2021
   */
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    /*Metodo/Funcion: cambiarPrecioTotalATotalHistorial
     Descripcion: Actualizar el TOTAL del contrato con la operacion de (TOTALHISTORIAL + TOTALPRODUCTO - TOTALABONO)
   */
    private void calculoTotal() {

        String sumar = "TOTALHISTORIAL";

        if(obtenerEstadoPromocion()) {
            if(global.obtenerAtributoTabla("CONTRATOS", "ESTATUS", "ID_CONTRATO", ultimoIdContratoCreado).equals("1")) {
                sumar = "TOTALPROMOCION";
            }
        }

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET ENVIADOPAGINA = '0', TOTAL = CASE WHEN (CAST((CASE WHEN LENGTH(" + sumar + ") = 0 THEN '0' ELSE " + sumar + " END) AS DECIMAL(5,2)) -"
                    + " CAST((CASE WHEN LENGTH(TOTALABONO) = 0 THEN '0' ELSE TOTALABONO END) AS DECIMAL(5,2)) +"
                    + " CAST((CASE WHEN LENGTH(TOTALPRODUCTO) = 0 THEN '0' ELSE TOTALPRODUCTO END) AS DECIMAL(5,2))) < 1 THEN '0' ELSE" +
                    " (CAST((CASE WHEN LENGTH(" + sumar + ") = 0 THEN '0' ELSE " + sumar + " END) AS DECIMAL(5,2)) -" +
                    " CAST((CASE WHEN LENGTH(TOTALABONO) = 0 THEN '0' ELSE TOTALABONO END) AS DECIMAL(5,2)) +" +
                    " CAST((CASE WHEN LENGTH(TOTALPRODUCTO) = 0 THEN '0' ELSE TOTALPRODUCTO END) AS DECIMAL(5,2))) END"
                    + " WHERE IDCONTRATORELACION='" + idContratoPadre + "' OR ID_CONTRATO='" + idContratoPadre + "'";

            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 155);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private boolean obtenerEstadoPromocion() {

        boolean respuesta = false;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ESTADO FROM PROMOCIONCONTRATO WHERE ID_CONTRATO='" + idContratoPadre + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.moveToFirst()) {
                if(Integer.parseInt(datos.getString(0)) > 0) {
                    respuesta = true;
                }
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 156);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return respuesta;
    }

    /*Metodo/Funcion: llamadaSincronizacion
     Descripcion: Actualizar atributo ENVIADOPAGINA a 0 del contrato actual y manda a llamar la clase sincronizacion
     con el metodo sincronizarMetodo(0) -> Sincronizar datos (servicio web) (contrato actual y contratos anteriores)
     y subir imagenes de los contratos terminados
   */
    private void llamadaSincronizacion() {
        global.actualizarAtributoTabla("CONTRATOS", "ENVIADOPAGINA", "0", "ID_CONTRATO", ultimoIdContratoCreado);
        sincronizacion.sincronizarMetodo(0, objetosWebService, 0);
    }

    /*Metodo/Funcion: validarComponentesPaqueteTres
     Descripcion: Bloqueo de componentes para el paquete seleccionado (DORADO 1 y PLATINO)
   */
    public void validarComponentesPaqueteTres(boolean desbloquearCheckboxTinteYOtro) {

        etOjoDerechoEsfericoNuevoHistorial2.setEnabled(true);
        etOjoDerechoCilindroNuevoHistorial2.setEnabled(true);
        etOjoDerechoEjeNuevoHistorial2.setEnabled(true);
        etOjoDerechoAddNuevoHistorial2.setEnabled(true);
        etOjoDerechoALTNuevoHistorial2.setEnabled(true);

        etOjoIzquierdoEsfericoNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoCilindroNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoEjeNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoAddNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoALTNuevoHistorial2.setEnabled(true);

    }

    /*Metodo/Funcion: validarComponentesPaqueteUno
     Descripcion: Bloqueo de componentes para el paquete seleccionado (LECTURA, ECO JR, JR y DORADO 2)
   */
    public void validarComponentesPaqueteUno(boolean bloquearCheckboxTinteYOtro) {

        etOjoDerechoEsfericoNuevoHistorial2.setEnabled(true);
        etOjoDerechoCilindroNuevoHistorial2.setEnabled(true);
        etOjoDerechoEjeNuevoHistorial2.setEnabled(true);
        etOjoDerechoAddNuevoHistorial2.setEnabled(false);
        etOjoDerechoALTNuevoHistorial2.setEnabled(false);
        etOjoIzquierdoEsfericoNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoCilindroNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoEjeNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoAddNuevoHistorial2.setEnabled(false);
        etOjoIzquierdoALTNuevoHistorial2.setEnabled(false);

        etOjoDerechoAddNuevoHistorial2.setText("");
        etOjoDerechoALTNuevoHistorial2.setText("");
        etOjoIzquierdoAddNuevoHistorial2.setText("");
        etOjoIzquierdoALTNuevoHistorial2.setText("");

    }

    /*Metodo/Funcion: validarComponentesPaquetePremium
    Descripcion: Bloqueo de componentes para el paquete seleccionado (PREMIUM)
    */
    public void validarComponentesPaquetePremium(){
        etOjoDerechoEsfericoNuevoHistorial2.setEnabled(true);
        etOjoDerechoCilindroNuevoHistorial2.setEnabled(true);
        etOjoDerechoEjeNuevoHistorial2.setEnabled(true);
        etOjoDerechoAddNuevoHistorial2.setEnabled(true);
        etOjoDerechoALTNuevoHistorial2.setEnabled(true);

        etOjoIzquierdoEsfericoNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoCilindroNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoEjeNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoAddNuevoHistorial2.setEnabled(true);
        etOjoIzquierdoALTNuevoHistorial2.setEnabled(true);

        cbTratamientoFotocromatico2.setChecked(false);
        cbTratamientoFotocromatico2.setEnabled(true);
        cbTratamientoAR2.setChecked(false);
        cbTratamientoAR2.setEnabled(true);
        cbTratamientoTinte2.setChecked(false);
        cbTratamientoTinte2.setEnabled(true);
        cbTratamientoBlueray2.setChecked(false);
        cbTratamientoBlueray2.setEnabled(true);
        cbTratamientoTinte2.setChecked(false);
        cbTratamientoPolarizado2.setChecked(false);
        cbTratamientoPolarizado2.setEnabled(true);
        cbTratamientoEspejeado2.setChecked(false);
        cbTratamientoEspejeado2.setEnabled(true);
        cbTratamientoOtro2.setChecked(false);
        cbTratamientoOtro2.setEnabled(true);
    }

    /*Metodo/Funcion: requestFocusCamposOjoDerIzqPaqueteUno
     Descripcion: Verificar que el paquete ha sido seleccionado (LECTURA, ECO JR, JR y DORADO 2)
     y hacer el requestFocus en los campos correspondientes ha ese paquete
   */
    public void requestFocusCamposOjoDerIzqPaqueteUno() {
        etOjoDerechoEsfericoNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoCilindroNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoCilindroNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoEjeNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;

            }
        });
        etOjoDerechoEjeNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEsfericoNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEsfericoNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoCilindroNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoCilindroNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEjeNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEjeNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });
    }

    /*Metodo/Funcion: validarComponentesPaqueteDos
     Descripcion: Bloqueo de componentes para el paquete seleccionado (NO SELECCIONADO y PROTECCION)
   */
    public void validarComponentesPaqueteDos() {

        etOjoDerechoEsfericoNuevoHistorial2.setEnabled(false);
        etOjoDerechoCilindroNuevoHistorial2.setEnabled(false);
        etOjoDerechoEjeNuevoHistorial2.setEnabled(false);
        etOjoDerechoAddNuevoHistorial2.setEnabled(false);
        etOjoDerechoALTNuevoHistorial2.setEnabled(false);

        etOjoIzquierdoEsfericoNuevoHistorial2.setEnabled(false);
        etOjoIzquierdoCilindroNuevoHistorial2.setEnabled(false);
        etOjoIzquierdoEjeNuevoHistorial2.setEnabled(false);
        etOjoIzquierdoAddNuevoHistorial2.setEnabled(false);
        etOjoIzquierdoALTNuevoHistorial2.setEnabled(false);

        etOjoDerechoEsfericoNuevoHistorial2.setText("");
        etOjoDerechoCilindroNuevoHistorial2.setText("");
        etOjoDerechoEjeNuevoHistorial2.setText("");
        etOjoDerechoAddNuevoHistorial2.setText("");
        etOjoDerechoALTNuevoHistorial2.setText("");
        etOjoIzquierdoEsfericoNuevoHistorial2.setText("");
        etOjoIzquierdoCilindroNuevoHistorial2.setText("");
        etOjoIzquierdoEjeNuevoHistorial2.setText("");
        etOjoIzquierdoAddNuevoHistorial2.setText("");
        etOjoIzquierdoALTNuevoHistorial2.setText("");

    }

    /*Metodo/Funcion: requestFocusCamposOjoDerIzqPaqueteTres
     Descripcion: Verificar que el paquete ha sido seleccionado (DORADO 1 y PLATINO)
     y hacer el requestFocus en los campos correspondientes ha ese paquete
   */
    public void requestFocusCamposOjoDerIzqPaqueteTres() {
        etOjoDerechoEsfericoNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoCilindroNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoCilindroNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoEjeNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoEjeNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoAddNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoAddNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoALTNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoALTNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEsfericoNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEsfericoNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoCilindroNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoCilindroNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEjeNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEjeNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoAddNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoAddNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoALTNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoALTNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });
    }

    /*Metodo/Funcion: verificarComponentesDiferentesPaqueteTres
     Descripcion: Validacion de componentes diferentes al paquete tres
   */
    private void verificarComponentesDiferentesPaqueteTres() {

        if(etOjoDerechoEsfericoNuevoHistorial2.getText().toString().equals("") || etOjoDerechoEsfericoNuevoHistorial2.getText().toString().equals("+")
                || etOjoDerechoEsfericoNuevoHistorial2.getText().toString().equals("-")){
            arregloComponentes[2] = true;
        }

        if(etOjoDerechoCilindroNuevoHistorial2.getText().toString().equals("") || etOjoDerechoCilindroNuevoHistorial2.getText().toString().equals("+")
                || etOjoDerechoCilindroNuevoHistorial2.getText().toString().equals("-")){
            arregloComponentes[3] = true;
        }

        if(etOjoDerechoEjeNuevoHistorial2.getText().toString().equals("")){
            arregloComponentes[4] = true;
        }

        if(etOjoDerechoAddNuevoHistorial2.getText().toString().equals("") || etOjoDerechoAddNuevoHistorial2.getText().toString().equals("+")
                || etOjoDerechoAddNuevoHistorial2.getText().toString().equals("-")){
            arregloComponentes[5] = true;
        }

        if(etOjoDerechoALTNuevoHistorial2.getText().toString().equals("")){
            arregloComponentes[6] = true;
        }

        if(etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString().equals("") || etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString().equals("+")
                || etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString().equals("-")){
            arregloComponentes[7] = true;
        }

        if(etOjoIzquierdoCilindroNuevoHistorial2.getText().toString().equals("") || etOjoIzquierdoCilindroNuevoHistorial2.getText().toString().equals("+")
                || etOjoIzquierdoCilindroNuevoHistorial2.getText().toString().equals("-")){
            arregloComponentes[8] = true;
        }

        if(etOjoIzquierdoEjeNuevoHistorial2.getText().toString().equals("")){
            arregloComponentes[9] = true;
        }

        if(etOjoIzquierdoAddNuevoHistorial2.getText().toString().equals("") || etOjoIzquierdoAddNuevoHistorial2.getText().toString().equals("+")
                || etOjoIzquierdoAddNuevoHistorial2.getText().toString().equals("-")){
            arregloComponentes[10] = true;
        }

        if(etOjoIzquierdoALTNuevoHistorial2.getText().toString().equals("")){
            arregloComponentes[11] = true;
        }

    }
    private void verificarComponentesDiferentesPaquetePremium(){

        if(Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial2.getText().toString()) < -10.0 ||
                Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial2.getText().toString()) > 8.0){
            //Esferico ojo derecho fuera de limites de graduacion
            tvOjoDerechoEsfericoNuevoHistorial2.setText("Limite de graduación: -10.0 a 8.0");
            tvOjoDerechoEsfericoNuevoHistorial2.setVisibility(View.VISIBLE);
            arregloComponentes[2] = true;
        }else{
            tvOjoDerechoEsfericoNuevoHistorial2.setText("Campo vacio.");
            tvOjoDerechoEsfericoNuevoHistorial2.setVisibility(View.GONE);
            arregloComponentes[2] = false;
        }

        if(Double.parseDouble(etOjoDerechoCilindroNuevoHistorial2.getText().toString()) < -6.0 ||
                Double.parseDouble(etOjoDerechoCilindroNuevoHistorial2.getText().toString()) > 0.0){
            //Esferico ojo derecho fuera de limites de graduacion
            tvOjoDerechoCilindroNuevoHistorial2.setText("Limite de graduación: -6.0 a 0.0");
            tvOjoDerechoCilindroNuevoHistorial2.setVisibility(View.VISIBLE);
            arregloComponentes[3] = true;
        }else{
            tvOjoDerechoCilindroNuevoHistorial2.setText("Campo vacio.");
            tvOjoDerechoCilindroNuevoHistorial2.setVisibility(View.GONE);
            arregloComponentes[3] = false;
        }

        if(Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString()) < -10.0 ||
                Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString()) > 8.0){
            //Esferico ojo derecho fuera de limites de graduacion
            tvOjoIzquierdoEsfericoNuevoHistorial2.setText("Limite de graduación: -10.0 a 8.0");
            tvOjoIzquierdoEsfericoNuevoHistorial2.setVisibility(View.VISIBLE);
            arregloComponentes[7] = true;
        }else{
            tvOjoIzquierdoEsfericoNuevoHistorial2.setText("Campo vacio.");
            tvOjoIzquierdoEsfericoNuevoHistorial2.setVisibility(View.GONE);
            arregloComponentes[7] = false;
        }

        if(Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial2.getText().toString()) < -6.0 ||
                Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial2.getText().toString()) > 0.0){
            //Esferico ojo derecho fuera de limites de graduacion
            tvOjoIzquierdoCilindroNuevoHistorial2.setText("Limite de graduación: -6.0 a 0.0");
            tvOjoIzquierdoCilindroNuevoHistorial2.setVisibility(View.VISIBLE);
            arregloComponentes[8] = true;
        }else{
            tvOjoIzquierdoCilindroNuevoHistorial2.setText("Campo vacio.");
            tvOjoIzquierdoCilindroNuevoHistorial2.setVisibility(View.GONE);
            arregloComponentes[8] = false;
        }

    }

    public void regresarPantallaVerContrato() {

        //Mandar a vista verContrato
        Fragment verificadorFragment = new verContrato();
        Bundle bundle = new Bundle();
        bundle.putString("ultimoIdContratoCreado", ultimoIdContratoCreado);
        verificadorFragment.setArguments(bundle);
        FragmentTransaction transaction = ((FragmentActivity)fragmento.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public boolean verificarPiezasRestantesproduto(){

        if(!esHistorialGarantia) {
            //Seleccionaste una opcion distinta a la de seleccionar
            if (spProductoNuevoHistorial2.getSelectedItem().toString() != "Seleccionar") {

                //Validar que producto seccionado cuente con mas de 10 piezas
                String producto[] = spProductoNuevoHistorial2.getSelectedItem().toString().split("\\|");
                String piezas = producto[2].trim(); //Eliminar espacios en blanco
                int numpiezas = Integer.parseInt(piezas.substring(0, piezas.length() - 4));  //Extraer solo el total de piezas
                if (numpiezas <= 10) {
                    //Piezas del producto es menor que 10 - Seleccionar por default 1er opcion
                    spProductoNuevoHistorial2.setSelection(0);
                    return false;
                }
            }
        }

        return true;
    }

    public void llenarSpTratamientoTinte(){
        if(rbFT2.isChecked() || rbBlend2.isChecked() || rbProgresivo2.isChecked() || rbOtroBifocal2.isChecked()){
            //Tipo de bifocal FT, Blend, Progresivo, Otro
            idsEstiloTratamientoTinte = new String[]{"","0"};
            estiloTratamientoTinte = new String[]{"Seleccionar","DESVANECIDO"};
        }else{
            //NA tipo bifocal
            idsEstiloTratamientoTinte = new String[]{"","0", "1"};
            estiloTratamientoTinte = new String[]{"Seleccionar","DESVANECIDO", "COMPLETO"};
        }

        spEstiloTratamiento2.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, estiloTratamientoTinte));
        spColorTratamiento2.setSelection(0);
        spEstiloTratamiento2.setSelection(0);
    }

    public void validarTipoBifocalPaquetesGraduacion(int paqueteSeleccionado){

        //Bandera para poder seleccionar tipo de bifocal
        boolean aplicaBifocal = true;
        //Variables
        String ojoDerechoAdd = "";
        String ojoIzquierdoAdd = "";

        if(paqueteSeleccionado != 0 && paqueteSeleccionado != 3 && paqueteSeleccionado != 4){
            //Paquete diferente de seleccionar, Eco Jr y Jr

            //Validar Bifocal por paquete seleccionado
            switch (paqueteSeleccionado) {
                case 1:
                    //Lectura
                case 2:
                    //Proteccion
                case 6:
                    //Dorado 2
                    aplicaBifocal = false;
                    break;
                case 5:
                    //Dorado 1
                case 7:
                    //Platino
                case 8:
                    //Premium
                    aplicaBifocal = true;
                    //Validar Bifocal por graduacion de campo ADD
                    ojoDerechoAdd = etOjoDerechoAddNuevoHistorial2.getText().toString();
                    ojoIzquierdoAdd = etOjoIzquierdoAddNuevoHistorial2.getText().toString();
                    break;
            }

            //Validar bifocal por graduacion de ADD
            if(aplicaBifocal && ((ojoDerechoAdd.equals("0") && ojoIzquierdoAdd.equals("0")) || (ojoDerechoAdd.equals("") && ojoIzquierdoAdd.equals("")))){
                //Graduaciones iguales a 0 -> tipo bifocal es NA
                aplicaBifocal = false;
            }

            //Bloquear/Desbloquear opciones de bifocal
            if(aplicaBifocal){
                //Debes seleccionar bifocal distinto de NA
                rgTipoBifocalNuevoHistorial2.clearCheck();
                rbFT2.setEnabled(true);
                rbBlend2.setEnabled(true);
                rbProgresivo2.setEnabled(true);
                rbNA2.setEnabled(false);
                seleccionarBifocalGuardadoBD();
            }else{
                //Bifocal tipo NA seleccionado por default
                rgTipoBifocalNuevoHistorial2.clearCheck();
                rbFT2.setEnabled(false);
                rbBlend2.setEnabled(false);
                rbProgresivo2.setEnabled(false);
                rbNA2.setEnabled(true);
                rbNA2.setChecked(true);
            }

            rbOtroBifocal2.setEnabled(true);
            etOtroBifocalNuevoHistorial2.setText("");
            etPrecioBifocalNuevoHistorial2.setText("");

        }else{
            //Paquetes de JR y Eco Jr todos los campos de bifocal habilitados
            rgTipoBifocalNuevoHistorial2.clearCheck();
            rbFT2.setEnabled(true);
            rbBlend2.setEnabled(true);
            rbProgresivo2.setEnabled(true);
            rbNA2.setEnabled(true);
            rbOtroBifocal2.setEnabled(true);
            etOtroBifocalNuevoHistorial2.setText("");
            etPrecioBifocalNuevoHistorial2.setText("");
            seleccionarBifocalGuardadoBD();
        }

    }

    public void seleccionarBifocalGuardadoBD(){
        if(!bifocalBD.equals("")){
            //Se selecciono algun bifocal
            switch (Integer.parseInt(bifocalBD)){
                case 0:
                    //rbFT fue chequeado
                    rbFT2.setChecked(true);
                    break;
                case 1:
                    //rbBlend fue chequeado
                    rbBlend2.setChecked(true);
                    break;
                case 2:
                    //rbProgresivo fue chequeado
                    rbProgresivo2.setChecked(true);
                    break;
                case 3:
                    //rbNA fue chequeado
                    rbNA2.setChecked(true);
                    break;
                case 4:
                    //rbOtroBifocal fue chequeado
                    rbOtroBifocal2.setChecked(true);
                    etOtroBifocalNuevoHistorial2.setText(otroBifocalBD);
                    etPrecioBifocalNuevoHistorial2.setText(otroBifocalPrecioBD);
                    etOtroBifocalNuevoHistorial2.setEnabled(true);
                    etPrecioBifocalNuevoHistorial2.setEnabled(true);
                    break;
            }

        }
    }

    public void validarArmazonSeleccionado() {
        //Seleccionaste una opcion distinta a la de seleccionar
        if(spProductoNuevoHistorial2.getSelectedItem().toString() != "Seleccionar") {

            //Optener armazon seleccionado
            String producto[] = spProductoNuevoHistorial2.getSelectedItem().toString().split("\\|");
            String nombreProducto = producto[0].trim(); //Eliminar espacios en blanco

            if(nombreProducto.toUpperCase().contains("PROPIO")){
                //Seleccionaste armazon propio
                if(!modificarHistorialClinico){
                    llFotoArmazonPropioHistorial2.setVisibility(View.VISIBLE);
                    //Bandera de armazon propio verdadero
                    esArmazonPropio = true;
                }else {
                    //Es armazon propio pero es un historial ya creado
                    llFotoArmazonPropioHistorial2.setVisibility(View.GONE);
                    esArmazonPropio = false;
                }

            }else {
                //Armazon distinta de propio
                llFotoArmazonPropioHistorial2.setVisibility(View.GONE);
                ivFotoArmazonPropioHistorial2.setImageDrawable(null);
                //Bandera de armazon propio falso
                esArmazonPropio = false;
            }
        }else{
            llFotoArmazonPropioHistorial2.setVisibility(View.GONE);
            ivFotoArmazonPropioHistorial2.setImageDrawable(null);
            //Bandera de armazon propio falso
            esArmazonPropio = false;
        }

        //Cargar imagen icono de armazon propio
        ivFotoArmazonPropioHistorial2.setImageDrawable(null);
        ivFotoArmazonPropioHistorial2.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.armazonpropio));

    }
}
