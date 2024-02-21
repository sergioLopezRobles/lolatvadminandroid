package com.luzatuvida.lolatvadminandroid.nuevoContrato;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import com.luzatuvida.lolatvadminandroid.global.Localizacion;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Sincronizacion;
import com.luzatuvida.lolatvadminandroid.global.Teclado;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;
import com.luzatuvida.lolatvadminandroid.vista.nuevoHistorialClinico;
import com.luzatuvida.lolatvadminandroid.vista.principal;
import com.luzatuvida.lolatvadminandroid.vista.verContrato;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Controlador {

    /*
    Identificador error del 118 al 139
     */

    Fragment fragmento;
    ScrollView svNuevoContratoPrincipal;
    ImageView ivFotoINEFrente, ivFotoINEAtras, ivFotoCasa, ivFotoComprobanteDomicilio, ivFotoPagare, ivFotoOtros, ivFotoArmazonPropio;
    EditText etNombreCliente, etCalleCliente, etNumeroCliente, etDepartamentoCliente, etAlLadoDeCliente, etFrenteACliente, etEntreCallesCliente,
                etColoniaCliente, etLocalidadCliente, etTelefonoCliente, etTelefonoReferenciaCliente, etNombreReferenciaCliente, etTipoCasaCliente,
                etCasaColorCliente, etCorreoCliente, etAliasCliente;
    EditText etEdadNuevoHistorial, etDiagnosticoNuevoHistorial, etOcupacionNuevoHistorial, etDiabetesNuevoHistorial, etHipertensionNuevoHistorial,
            etOtroMoletiaNuevoHistorial, etUltimoExamenNuevoHistorial, etFechaEntregaNuevoHistorial, etOjoDerechoEsfericoNuevoHistorial, etOjoDerechoCilindroNuevoHistorial,
            etOjoDerechoEjeNuevoHistorial, etOjoDerechoAddNuevoHistorial, etOjoDerechoALTNuevoHistorial, etOjoIzquierdoEsfericoNuevoHistorial,
            etOjoIzquierdoCilindroNuevoHistorial, etOjoIzquierdoEjeNuevoHistorial, etOjoIzquierdoAddNuevoHistorial, etOjoIzquierdoALTNuevoHistorial,
            etOtroMaterialNuevoHistorial, etPrecioMaterialNuevoHistorial, etOtroTratamientoNuevoHistorial, etPrecioTratamientoNuevoHistorial, etObservacionesNuevoHistorial,
            etObservacionesInternoNuevoHistorial, etEmbarazadaNuevoHistorial, etDurmioSeisOchoHorasNuevoHistorial, etActividadDiaNuevoHistorial, etProblemasOjosNuevoHistorial,
            etOtroBifocalNuevoHistorial, etPrecioBifocalNuevoHistorial;
    EditText etOjoDerechoEsfericoSinConversionNuevoHistorial, etOjoDerechoCilindroSinConversionNuevoHistorial, etOjoDerechoEjeSinConversionNuevoHistorial, etOjoDerechoAddSinConversionNuevoHistorial,
            etOjoIzquierdoEsfericoSinConversionNuevoHistorial, etOjoIzquierdoCilindroSinConversionNuevoHistorial, etOjoIzquierdoEjeSinConversionNuevoHistorial,
            etOjoIzquierdoAddSinConversionNuevoHistorial;
    RadioGroup rgTipoBifocalNuevoHistorial;
    RadioButton rbHiIndex, rbCR, rbPolicarbonato, rbOtroMaterial, rbFT, rbBlend, rbProgresivo, rbNA, rbOtroBifocal;
    CheckBox cbDolorCabeza, cbArdorOjos, cbGolpeCabeza, cbOtroMolestia, cbTratamientoFotocromatico, cbTratamientoAR,
                cbTratamientoTinte, cbTratamientoBlueray, cbTratamientoOtro, cbDuplicarDocumentos, cbTratamientoPolarizado, cbPolicarbonatoTipo, cbTratamientoEspejeado;
    Spinner spZona, spOptometrista, spProductoNuevoHistorial, spPaquetesNuevoHistorial, spColoniaClienteEntrega, spColorTratamiento, spEstiloTratamiento, spColorTratamientoPolarizado,
            spColorTratamientoEspejeado, spLugarEntrega;
    TextView tvZona, tvOptometrista, tvNombreCliente, tvCalleCliente, tvNumeroCliente, tvDepartamentoCliente, tvAlLadoDeCliente, tvFrenteACliente, tvEntreCallesCliente, tvColoniaCliente,
            tvLocalidadCliente, tvTelefonoCliente, tvTelefonoReferenciaCliente, tvNombreReferenciaCliente, tvTipoCasaCliente, tvCasaColorCliente, tvFotoINEFrente, tvFotoINEAtras, tvFotoCasa,
            tvFotoComprobanteDomicilio, tvCorreoCliente, tvFotoPagare, tvAliasCliente, tvOtraFoto, tvFotoArmazonPropio;
    TextView tvEdadNuevoHistorial, tvDiagnosticoNuevoHistorial, tvOcupacionNuevoHistorial, tvDiabetesNuevoHistorial, tvHipertensionNuevoHistorial,
            tvOtroMoletiaNuevoHistorial, tvProductoNuevoHistorial, tvPaquetesNuevoHistorial, tvFechaEntregaNuevoHistorial, tvOjoDerechoEsfericoNuevoHistorial, tvOjoDerechoCilindroNuevoHistorial,
            tvOjoDerechoEjeNuevoHistorial, tvOjoDerechoAddNuevoHistorial, tvOjoDerechoALTNuevoHistorial, tvOjoIzquierdoEsfericoNuevoHistorial,
            tvOjoIzquierdoCilindroNuevoHistorial, tvOjoIzquierdoEjeNuevoHistorial, tvOjoIzquierdoAddNuevoHistorial, tvOjoIzquierdoALTNuevoHistorial,
            tvOtroMaterialNuevoHistorial, tvPrecioMaterialNuevoHistorial, tvOtroTratamientoNuevoHistorial, tvPrecioTratamientoNuevoHistorial, tvTipoBifocalNuevoHistorial,
            tvGeneralSinConversionNuevoHistorial, tvEmbarazadaNuevoHistorial, tvDurmioSeisOchoHorasNuevoHistorial, tvActividadDiaNuevoHistorial, tvProblemasOjosNuevoHistorial,
            tvOtroBifocalNuevoHistorial, tvPrecioBifocalNuevoHistorial,  tvColorTratamiento, tvEstiloTratamiento, tvColorTratamientoPolarizado, tvColorTratamientoEspejeado;;
    LinearLayout llDuplicarDocumentos, llFotos,  llColorEstiloTinte, llTratamientoPolarizado, llTratamientoEspejeado, llPrincipalNuevoContrato, llPrincipalHistorialClinico,
                  llFotoArmazonPropio;
    AutoCompleteTextView actvContratosLiosFugas;
    Button btnGuardarContratoNuevo, btnCancelarContratoNuevo;

    EditText etCalleClienteEntrega, etNumeroClienteEntrega, etDepartamentoClienteEntrega, etAlLadoDeClienteEntrega, etFrenteAClienteEntrega, etEntreCallesClienteEntrega, etColoniaClienteEntrega,
            etLocalidadClienteEntrega, etTipoCasaClienteEntrega, etCasaColorClienteEntrega;
    TextView tvCalleClienteEntrega, tvNumeroClienteEntrega, tvDepartamentoClienteEntrega, tvAlLadoDeClienteEntrega, tvFrenteAClienteEntrega, tvEntreCallesClienteEntrega, tvColoniaClienteEntrega,
            tvLocalidadClienteEntrega, tvTipoCasaClienteEntrega, tvCasaColorClienteEntrega, btnCopiarDatosLugarVenta, tvLugarEntrega;

    String[] idsZonas, zonas, idsOptometristas, namesOptometristas, idsProductos, nombresProductos, idsPaquetes, nombresPaquetes, idsZonaColonias, nameColonias, nameLocalidad,
             nameColoniaLocalidad, idsColorTratamientoTinte, colorTratamientoTinte, idsEstiloTratamientoTinte, estiloTratamientoTinte, idsColorTratamientoPolarizado,
             colorTratamientoPolarizado, idsColorTratamientoEspejo, colorTratamientoEspejo, idsOpcionesLugarEntrega, opcionesLugarEntrega;

    EditText etObservacionFotoINEFrente, etObservacionFotoINEAtras, etObservacionFotoPagare, etObservacionFotoComprobanteDomicilio, etObservacionFotoCasa, etObservacionOtraFoto;

    int[] coloresRBGProductos;
    SQLiteDatabase sqLiteDB;
    baseDeDatos conexion;
    Camara camara;
    Teclado teclado;
    boolean[] arregloComponentes;
    boolean correcto = true;

    String idContratoPadre = "";
    String idContratoHijo = "";
    boolean contratoPromocion;
    boolean contratoActualizar;
    boolean abrirContratoPorCrear = false;

    public String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
    public String rutaINEFrente = "";
    public String rutaINEAtras = "";
    public String rutaCasa = "";
    public String rutaComprobanteDomicilio = "";
    public String rutaPagare = "";
    public String rutaFotoOtros = "";

    ObtenerRol obtenerRol;
    String time = "";
    String fechaActual = "";
    GenerarIdAlfanumerico generarIdAlfanumerico;
    Sincronizacion sincronizacion;
    Global global;
    int contadorVerificarDatos = 0;
    Llaves llaves;
    String idPaqueteSeleccionado = "";

    List<String> datosContratosLiosFugas;

    Localizacion localizacion;
    HistorialMovimientosContrato historialMovimientosContrato;
    Runnable runnable;
    String coordenadasContrato = "";
    boolean confirmacionAlertaCambioPaqueteDorado1PlatinooViserversa = false;
    String idContratoMovimientoHistorial = "";
    String bifocalBD = "";
    String otroBifocalBD = "";
    String otroBifocalPrecioBD = "";

    boolean esArmazonPropio = false;

    public Controlador(Fragment fragmento, Object[] objetos) {
        this.fragmento = fragmento;
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
        camara = new Camara(fragmento);
        teclado = new Teclado(fragmento);
        obtenerRol = new ObtenerRol(fragmento);
        generarIdAlfanumerico = new GenerarIdAlfanumerico(fragmento);
        sincronizacion = new Sincronizacion(fragmento,false);
        global = new Global(fragmento);
        localizacion = new Localizacion(fragmento);
        historialMovimientosContrato = new HistorialMovimientosContrato(fragmento);
        arregloComponentes = new boolean[71];

        spZona = (Spinner)objetos[0];
        spOptometrista = (Spinner)objetos[1];
        etNombreCliente = (EditText)objetos[2];
        etCalleCliente = (EditText)objetos[3];
        etNumeroCliente = (EditText)objetos[4];
        etDepartamentoCliente = (EditText)objetos[5];
        etAlLadoDeCliente = (EditText)objetos[6];
        etFrenteACliente = (EditText)objetos[7];
        etEntreCallesCliente = (EditText)objetos[8];
        etColoniaCliente = (EditText)objetos[9];
        etLocalidadCliente = (EditText)objetos[10];
        etTelefonoCliente = (EditText)objetos[11];
        etTelefonoReferenciaCliente = (EditText)objetos[12];
        etNombreReferenciaCliente = (EditText)objetos[13];
        etTipoCasaCliente = (EditText)objetos[14];
        etCasaColorCliente = (EditText)objetos[15];
        ivFotoINEFrente = (ImageView)objetos[16];
        ivFotoINEAtras = (ImageView)objetos[17];
        ivFotoCasa = (ImageView)objetos[18];
        ivFotoComprobanteDomicilio = (ImageView)objetos[19];

        etEdadNuevoHistorial = (EditText)objetos[20];
        etDiagnosticoNuevoHistorial = (EditText)objetos[21];
        etOcupacionNuevoHistorial = (EditText)objetos[22];
        etDiabetesNuevoHistorial = (EditText)objetos[23];
        etHipertensionNuevoHistorial = (EditText)objetos[24];
        cbDolorCabeza = (CheckBox)objetos[25];
        cbArdorOjos = (CheckBox)objetos[26];
        cbGolpeCabeza = (CheckBox)objetos[27];
        cbOtroMolestia = (CheckBox)objetos[28];
        etOtroMoletiaNuevoHistorial = (EditText)objetos[29];
        etUltimoExamenNuevoHistorial = (EditText)objetos[30];
        spProductoNuevoHistorial = (Spinner)objetos[31];
        spPaquetesNuevoHistorial = (Spinner)objetos[32];
        etFechaEntregaNuevoHistorial = (EditText)objetos[33];
        etOjoDerechoEsfericoNuevoHistorial = (EditText)objetos[34];
        etOjoDerechoCilindroNuevoHistorial = (EditText)objetos[35];
        etOjoDerechoEjeNuevoHistorial = (EditText)objetos[36];
        etOjoDerechoAddNuevoHistorial = (EditText)objetos[37];
        etOjoDerechoALTNuevoHistorial = (EditText)objetos[38];
        etOjoIzquierdoEsfericoNuevoHistorial = (EditText)objetos[39];
        etOjoIzquierdoCilindroNuevoHistorial = (EditText)objetos[40];
        etOjoIzquierdoEjeNuevoHistorial = (EditText)objetos[41];
        etOjoIzquierdoAddNuevoHistorial = (EditText)objetos[42];
        etOjoIzquierdoALTNuevoHistorial = (EditText)objetos[43];
        rbHiIndex = (RadioButton)objetos[44];
        rbCR = (RadioButton)objetos[45];
        rbPolicarbonato = (RadioButton)objetos[46];
        rbOtroMaterial = (RadioButton)objetos[47];
        etOtroMaterialNuevoHistorial = (EditText)objetos[48];
        etPrecioMaterialNuevoHistorial = (EditText)objetos[49];
        cbTratamientoFotocromatico = (CheckBox)objetos[50];
        cbTratamientoAR = (CheckBox)objetos[51];
        cbTratamientoTinte = (CheckBox)objetos[52];
        cbTratamientoBlueray = (CheckBox)objetos[53];
        cbTratamientoOtro = (CheckBox)objetos[54];
        etOtroTratamientoNuevoHistorial = (EditText)objetos[55];
        etPrecioTratamientoNuevoHistorial = (EditText)objetos[56];
        rbFT = (RadioButton)objetos[57];
        rbBlend = (RadioButton)objetos[58];
        rbProgresivo = (RadioButton)objetos[59];
        rbNA = (RadioButton)objetos[60];
        etObservacionesNuevoHistorial = (EditText)objetos[61];

        tvZona = (TextView)objetos[62];
        tvOptometrista = (TextView)objetos[63];
        tvNombreCliente = (TextView)objetos[64];
        tvCalleCliente = (TextView)objetos[65];
        tvNumeroCliente = (TextView)objetos[66];
        tvDepartamentoCliente = (TextView)objetos[67];
        tvAlLadoDeCliente = (TextView)objetos[68];
        tvFrenteACliente = (TextView)objetos[69];
        tvEntreCallesCliente = (TextView)objetos[70];
        tvColoniaCliente = (TextView)objetos[71];
        tvLocalidadCliente = (TextView)objetos[72];
        tvTelefonoCliente = (TextView)objetos[73];
        tvTelefonoReferenciaCliente = (TextView)objetos[74];
        tvNombreReferenciaCliente = (TextView)objetos[75];
        tvTipoCasaCliente = (TextView)objetos[76];
        tvCasaColorCliente = (TextView)objetos[77];
        tvFotoINEFrente = (TextView)objetos[78];
        tvFotoINEAtras = (TextView)objetos[79];
        tvFotoCasa = (TextView)objetos[80];
        tvFotoComprobanteDomicilio = (TextView)objetos[81];

        tvEdadNuevoHistorial = (TextView)objetos[82];
        tvDiagnosticoNuevoHistorial = (TextView)objetos[83];
        tvOcupacionNuevoHistorial = (TextView)objetos[84];
        tvDiabetesNuevoHistorial = (TextView)objetos[85];
        tvHipertensionNuevoHistorial = (TextView)objetos[86];
        tvOtroMoletiaNuevoHistorial = (TextView)objetos[87];
        tvProductoNuevoHistorial = (TextView)objetos[88];
        tvPaquetesNuevoHistorial = (TextView)objetos[89];
        tvFechaEntregaNuevoHistorial = (TextView)objetos[90];
        tvOjoDerechoEsfericoNuevoHistorial = (TextView)objetos[91];
        tvOjoDerechoCilindroNuevoHistorial = (TextView)objetos[92];
        tvOjoDerechoEjeNuevoHistorial = (TextView)objetos[93];
        tvOjoDerechoAddNuevoHistorial = (TextView)objetos[94];
        tvOjoDerechoALTNuevoHistorial = (TextView)objetos[95];
        tvOjoIzquierdoEsfericoNuevoHistorial = (TextView)objetos[96];
        tvOjoIzquierdoCilindroNuevoHistorial = (TextView)objetos[97];
        tvOjoIzquierdoEjeNuevoHistorial = (TextView)objetos[98];
        tvOjoIzquierdoAddNuevoHistorial = (TextView)objetos[99];
        tvOjoIzquierdoALTNuevoHistorial = (TextView)objetos[100];
        tvOtroMaterialNuevoHistorial = (TextView)objetos[101];
        tvPrecioMaterialNuevoHistorial = (TextView)objetos[102];
        tvOtroTratamientoNuevoHistorial = (TextView)objetos[103];
        tvPrecioTratamientoNuevoHistorial = (TextView)objetos[104];
        tvTipoBifocalNuevoHistorial = (TextView)objetos[105];

        idContratoPadre = (String)objetos[106];
        contratoPromocion = (Boolean)objetos[107];

        llDuplicarDocumentos = (LinearLayout)objetos[108];
        cbDuplicarDocumentos = (CheckBox)objetos[109];

        etCorreoCliente = (EditText)objetos[110];
        tvCorreoCliente = (TextView)objetos[111];

        ivFotoPagare = (ImageView) objetos[112];
        tvFotoPagare = (TextView)objetos[113];
        llFotos = (LinearLayout)objetos[114];

        idContratoHijo = (String)objetos[115];
        svNuevoContratoPrincipal = (ScrollView) objetos[116];
        etObservacionesInternoNuevoHistorial = (EditText) objetos[117];
        actvContratosLiosFugas = (AutoCompleteTextView) objetos[118];
        btnGuardarContratoNuevo = (Button) objetos[119];
        btnCancelarContratoNuevo = (Button) objetos[120];

        etOjoDerechoEsfericoSinConversionNuevoHistorial = (EditText) objetos[121];
        etOjoDerechoCilindroSinConversionNuevoHistorial = (EditText) objetos[122];
        etOjoDerechoEjeSinConversionNuevoHistorial = (EditText) objetos[123];
        etOjoDerechoAddSinConversionNuevoHistorial = (EditText) objetos[124];
        etOjoIzquierdoEsfericoSinConversionNuevoHistorial = (EditText) objetos[125];
        etOjoIzquierdoCilindroSinConversionNuevoHistorial = (EditText) objetos[126];
        etOjoIzquierdoEjeSinConversionNuevoHistorial = (EditText) objetos[127];
        etOjoIzquierdoAddSinConversionNuevoHistorial = (EditText) objetos[128];
        tvGeneralSinConversionNuevoHistorial = (TextView) objetos[129];

        ivFotoOtros = (ImageView) objetos[130];

        etEmbarazadaNuevoHistorial = (EditText) objetos[131];
        etDurmioSeisOchoHorasNuevoHistorial = (EditText) objetos[132];
        etActividadDiaNuevoHistorial = (EditText) objetos[133];
        etProblemasOjosNuevoHistorial = (EditText) objetos[134];
        etOtroBifocalNuevoHistorial = (EditText) objetos[135];
        etPrecioBifocalNuevoHistorial = (EditText) objetos[136];
        rbOtroBifocal = (RadioButton) objetos[137];
        tvEmbarazadaNuevoHistorial = (TextView) objetos[138];
        tvDurmioSeisOchoHorasNuevoHistorial = (TextView) objetos[139];
        tvActividadDiaNuevoHistorial = (TextView) objetos[140];
        tvProblemasOjosNuevoHistorial = (TextView) objetos[141];
        tvOtroBifocalNuevoHistorial = (TextView) objetos[142];
        tvPrecioBifocalNuevoHistorial = (TextView) objetos[143];

        etCalleClienteEntrega = (EditText) objetos[144];
        etNumeroClienteEntrega = (EditText) objetos[145];
        etDepartamentoClienteEntrega = (EditText) objetos[146];
        etAlLadoDeClienteEntrega = (EditText) objetos[147];
        etFrenteAClienteEntrega = (EditText) objetos[148];
        etEntreCallesClienteEntrega = (EditText) objetos[149];
        etColoniaClienteEntrega = (EditText) objetos[150];
        etLocalidadClienteEntrega = (EditText) objetos[151];
        etTipoCasaClienteEntrega = (EditText) objetos[152];
        etCasaColorClienteEntrega = (EditText) objetos[153];
        tvCalleClienteEntrega = (TextView) objetos[154];
        tvNumeroClienteEntrega = (TextView) objetos[155];
        tvDepartamentoClienteEntrega = (TextView) objetos[156];
        tvAlLadoDeClienteEntrega = (TextView) objetos[157];
        tvFrenteAClienteEntrega = (TextView) objetos[158];
        tvEntreCallesClienteEntrega = (TextView) objetos[159];
        tvColoniaClienteEntrega = (TextView) objetos[160];
        tvLocalidadClienteEntrega = (TextView) objetos[161];
        tvTipoCasaClienteEntrega = (TextView) objetos[162];
        tvCasaColorClienteEntrega = (TextView) objetos[163];
        btnCopiarDatosLugarVenta = (TextView) objetos[164];

        etAliasCliente = (EditText)objetos[165];
        tvAliasCliente = (TextView) objetos[166];
        tvOtraFoto = (TextView) objetos[167];
        spColoniaClienteEntrega = (Spinner) objetos[168];

        spColorTratamiento = (Spinner) objetos[169];
        spEstiloTratamiento = (Spinner) objetos[170];
        tvColorTratamiento = (TextView) objetos[171];
        tvEstiloTratamiento = (TextView) objetos[172];
        cbTratamientoPolarizado = (CheckBox) objetos[173];
        llTratamientoPolarizado = (LinearLayout) objetos[174];
        spColorTratamientoPolarizado = (Spinner) objetos[175];
        tvColorTratamientoPolarizado = (TextView) objetos[176];
        cbTratamientoEspejeado = (CheckBox) objetos[177];
        llTratamientoEspejeado = (LinearLayout) objetos[178];
        spColorTratamientoEspejeado = (Spinner) objetos[179];
        tvColorTratamientoEspejeado = (TextView) objetos[180];
        cbPolicarbonatoTipo = (CheckBox) objetos[181];
        rgTipoBifocalNuevoHistorial = (RadioGroup) objetos[182];

        llPrincipalNuevoContrato = (LinearLayout) objetos[183];
        llPrincipalHistorialClinico = (LinearLayout) objetos[184];
        contratoActualizar = (Boolean) objetos[185];

        spLugarEntrega = (Spinner) objetos[186];
        tvLugarEntrega = (TextView) objetos[187];

        llFotoArmazonPropio = (LinearLayout) objetos[188];
        ivFotoArmazonPropio = (ImageView) objetos[189];
        tvFotoArmazonPropio = (TextView) objetos[190];

        etObservacionFotoINEFrente = (EditText)objetos[191];
        etObservacionFotoINEAtras = (EditText)objetos[192];
        etObservacionFotoPagare = (EditText)objetos[193];
        etObservacionFotoComprobanteDomicilio = (EditText)objetos[194];
        etObservacionFotoCasa = (EditText)objetos[195];
        etObservacionOtraFoto = (EditText)objetos[196];

    }

    /*Metodo/Funcion: validar
     Descripcion: Manda a llamar la funcion de llenarSpinners y valida si es un contrato nuevo por promocion
   */
    public void validar() {

        llenarSpinnersBD();
        llenarAutoCompleteTextViewContratosLiosFugas();
        deshabilitarCamposZonaColoniaLocalidad();

        boolean mostrarAlertParaGuardarUbicacion = true;

        if(contratoActualizar){
            //Actualizar contrato que se abrio
            llenarCamposContratoPadre(idContratoHijo, true);
            String idHistorialClinico = obtenerIdHistorialContratoPorCrear();
            llenarCamposHistorialClinico(idHistorialClinico, false);
            mostrarAlertParaGuardarUbicacion = false;
            idContratoMovimientoHistorial = idContratoHijo;

        }else{
            //Contrato nuevo
            ivFotoArmazonPropio.setImageDrawable(null);
            if(contratoPromocion) {
                //Contrato nuevo por promocion

                if(idContratoPadre.length() != 0) {
                    //Tiene idContratoPadre
                    if(idContratoHijo.length() != 0) {
                        //Es un contrato hijo por crear
                        llenarCamposContratoPadre(idContratoHijo, true);
                        String idHistorialClinico = obtenerIdHistorialContratoPorCrear();
                        llenarCamposHistorialClinico(idHistorialClinico, false);
                        mostrarAlertParaGuardarUbicacion = false;
                        //idContrato para registrar movimientos es idContratoHijo
                        idContratoMovimientoHistorial = idContratoHijo;
                    }else {
                        //Es un contrato hijo por primera vez
                        llenarCamposContratoPadre(idContratoPadre, true);
                        //Obtener id contrato para registro de movimientos
                        idContratoMovimientoHistorial = obtenerIdContrato();
                    }
                    llDuplicarDocumentos.setVisibility(View.VISIBLE);
                }else {
                    //No tiene idContratoPadre

                    llenarCamposContratoPadre(idContratoHijo, false);
                    String idHistorialClinico = obtenerIdHistorialContratoPorCrear();

                    if(global.obtenerAtributoContrato(idContratoHijo, "ESTATUS_ESTADOCONTRATO").equals("13")) {
                        //Es un contrato por crear
                        llenarCamposHistorialClinico(idHistorialClinico, false);
                        //idContrato para registrar movimientos es idContratoHijo
                        idContratoMovimientoHistorial = idContratoHijo;
                    }else {
                        //No es un contrato por crear
                        //Duplicar contrato
                        llenarCamposHistorialClinico(idHistorialClinico, true);
                        //Obtener id contrato para registro de movimientos
                        idContratoMovimientoHistorial = obtenerIdContrato();
                    }

                    mostrarAlertParaGuardarUbicacion = false;
                }

            }

            if(mostrarAlertParaGuardarUbicacion || global.obtenerAtributoContrato(idContratoHijo, "COORDENADAS").equals("")) {
                mostrarAlertaGuardarUbicacion();
            }else {
                coordenadasContrato = global.obtenerAtributoContrato(idContratoHijo, "COORDENADAS");
            }

            //Contrato a crear
            if(!idContratoHijo.equals("")){
                //idContratoHijo != vacio -> Contrato hijo, contrato guardado o por crear
                idContratoMovimientoHistorial = idContratoHijo;
            }else{
                //contrato nuevo a crear
                idContratoMovimientoHistorial = obtenerIdContrato();
            }
        }

    }

    private void llenarAutoCompleteTextViewContratosLiosFugas() {

        datosContratosLiosFugas = new ArrayList<String>();

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID_CONTRATO, NOMBRE, COLONIA, CALLE, NUMERO, TELEFONO FROM CONTRATOSLIOSFUGAS";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() > 0) {
                while (datos.moveToNext()) {
                    datosContratosLiosFugas.add(datos.getString(1) + " , " + datos.getString(2) + " , " + datos.getString(3) + " , " +
                                                datos.getString(4) + " , " + datos.getString(5));
                }
            }

            sqLiteDB2.close();
            datos.close();

            actvContratosLiosFugas.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), R.layout.layout_item_autocomplete, R.id.tvCustom, datosContratosLiosFugas));

        }catch (SQLiteException e){
            global.escribirError(e, 118);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void llenarCamposHistorialClinico(String idHistorialClinico, boolean duplicar) {

        String idPaquete = "";

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT EDAD, DIAGNOSTICO, OCUPACION, DIABETES, HIPERTENSION, DOLOR, ARDOR, GOLPEOJOS," +
                    " OTROM, MOLESTIAOTRO, ULTIMOEXAMEN, ID_PAQUETE, ID_PRODUCTO, FECHAENTREGA, ESFERICODER," +
                    " CILINDRODER, EJEDER, ADDDER, ALTDER, ESFERICOIZQ, CILINDROIZQ, EJEIZQ, ADDIZQ," +
                    " ALTIZQ, MATERIAL, MATERIALOTRO, COSTOMATERIAL, FOTOCROMATICO, AR, TINTE, BLUERAY," +
                    " OTROT, TRATAMIENTOOTRO, COSTOTRATAMIENTO, BIFOCAL, OBSERVACIONES, OBSERVACIONESINTERNO," +
                    " BIFOCALOTRO, COSTOBIFOCAL, EMBARAZADA, DURMIOSEISOCHOHORAS, ACTIVIDADDIA, PROBLEMASOJOS, POLICARBONATOTIPO, ID_TRATAMIENTOCOLORTINTE," +
                    " ESTILOTINTE, POLARIZADO, ID_TRATAMIENTOCOLORPOLARIZADO, ESPEJO, ID_TRATAMIENTOCOLORESPEJO, FOTOARMAZON FROM HISTORIALCLINICO" +
                    " WHERE ID='" + idHistorialClinico + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                //No hay datos
                Log.i("MENSAJE", "No hay historial registrado");
            }

            if (datos.moveToFirst()) {
                //Si hay datos

                //Obtener datos del contrato y mostrarlos en los campos de texto
                etEdadNuevoHistorial.setText(datos.getString(0));
                etDiagnosticoNuevoHistorial.setText(datos.getString(1));
                etOcupacionNuevoHistorial.setText(datos.getString(2));
                etDiabetesNuevoHistorial.setText(datos.getString(3));
                etHipertensionNuevoHistorial.setText(datos.getString(4));

                etEmbarazadaNuevoHistorial.setText(datos.getString(39));
                etDurmioSeisOchoHorasNuevoHistorial.setText(datos.getString(40));
                etActividadDiaNuevoHistorial.setText(datos.getString(41));
                etProblemasOjosNuevoHistorial.setText(datos.getString(42));

                if(Integer.parseInt(datos.getString(5)) > 0) {
                    //Si cbDolorCabeza fue chequeado
                    cbDolorCabeza.setChecked(true);
                }
                if(Integer.parseInt(datos.getString(6)) > 0) {
                    //Si cbArdorOjos fue chequeado
                    cbArdorOjos.setChecked(true);
                }
                if(Integer.parseInt(datos.getString(7)) > 0) {
                    //Si cbGolpeCabeza fue chequeado
                    cbGolpeCabeza.setChecked(true);
                }
                if(Integer.parseInt(datos.getString(8)) > 0) {
                    //Si cbOtroMolestia fue chequeado
                    cbOtroMolestia.setChecked(true);
                    etOtroMoletiaNuevoHistorial.setText(datos.getString(9));
                }

                etUltimoExamenNuevoHistorial.setText(datos.getString(10));

                if(!duplicar) {
                    //No es duplicar

                    idPaquete = datos.getString(11);
                    spPaquetesNuevoHistorial.setSelection(Arrays.asList(idsPaquetes).indexOf(idPaquete));

                    spProductoNuevoHistorial.setSelection(Arrays.asList(idsProductos).indexOf(datos.getString(12)));
                    //Verificar si es armazon propio - Cargar imagen si tiene
                    if(Array.get(nombresProductos,Arrays.asList(idsProductos).indexOf(datos.getString(12))).toString().contains("PROPIO")){
                        String rutaFotoArmazonpropio = datos.getString(50);
                        if(rutaFotoArmazonpropio.length() > 0){
                            File foto = new File(file_path + "/luzatuvida/.nomedia/" + rutaFotoArmazonpropio);
                            Bitmap bitmap = null;
                            if(foto.exists()) {
                                //Si la foto existe en el directorio
                                bitmap = BitmapFactory.decodeFile(foto.getAbsolutePath());
                                ivFotoArmazonPropio.setBackground(null);
                                ivFotoArmazonPropio.setImageBitmap(bitmap);
                            }
                        }else{
                            //No tiene imagen
                            ivFotoArmazonPropio.setImageDrawable(null);
                            ivFotoArmazonPropio.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.armazonpropio));
                        }
                    }
                    etFechaEntregaNuevoHistorial.setText(datos.getString(13));

                    etOjoDerechoEsfericoNuevoHistorial.setText(datos.getString(14));

                    etOjoDerechoCilindroNuevoHistorial.setText(datos.getString(15));

                    etOjoDerechoEjeNuevoHistorial.setText(datos.getString(16));

                    etOjoDerechoAddNuevoHistorial.setText(datos.getString(17));

                    etOjoDerechoALTNuevoHistorial.setText(datos.getString(18));

                    etOjoIzquierdoEsfericoNuevoHistorial.setText(datos.getString(19));

                    etOjoIzquierdoCilindroNuevoHistorial.setText(datos.getString(20));

                    etOjoIzquierdoEjeNuevoHistorial.setText(datos.getString(21));

                    etOjoIzquierdoAddNuevoHistorial.setText(datos.getString(22));

                    etOjoIzquierdoALTNuevoHistorial.setText(datos.getString(23));

                    if (datos.getString(24).length() > 0) {
                        if (Integer.parseInt(datos.getString(24)) == 0) {
                            //Si rbHiIndex fue chequeado
                            rbHiIndex.setChecked(true);
                        }
                        if (Integer.parseInt(datos.getString(24)) == 1) {
                            //Si rbCR fue chequeado
                            rbCR.setChecked(true);
                        }

                        if (Integer.parseInt(datos.getString(24)) == 2) {
                            //Si rbPolicarbonato fue chequeado
                            rbPolicarbonato.setChecked(true);
                            cbPolicarbonatoTipo.setVisibility(View.VISIBLE);
                            //CbPolarizadoTipo
                            String valorPolicarbonatoTipo = datos.getString(43);
                            valorPolicarbonatoTipo = (valorPolicarbonatoTipo.equals(""))? "0": valorPolicarbonatoTipo;
                            if (Integer.parseInt(valorPolicarbonatoTipo) > 0){
                                cbPolicarbonatoTipo.setChecked(true);
                                cbPolicarbonatoTipo.setText("Niño");
                            }
                        }

                        if (Integer.parseInt(datos.getString(24)) == 3) {
                            //Si rbOtroMaterial fue chequeado
                            rbOtroMaterial.setChecked(true);
                            etOtroMaterialNuevoHistorial.setText(datos.getString(25));
                            etPrecioMaterialNuevoHistorial.setText(datos.getString(26));
                        }
                    }

                    if (Integer.parseInt(datos.getString(27)) > 0) {
                        //Si cbTratamientoFotocromatico fue chequeado
                        cbTratamientoFotocromatico.setChecked(true);
                    }
                    if (Integer.parseInt(datos.getString(28)) > 0) {
                        //Si cbTratamientoAR fue chequeado
                        cbTratamientoAR.setChecked(true);
                    }
                    if (Integer.parseInt(datos.getString(29)) > 0) {
                        //Si cbTratamientoTinte fue chequeado
                        cbTratamientoTinte.setChecked(true);
                        spColorTratamiento.setSelection(Arrays.asList(idsColorTratamientoTinte).indexOf(datos.getString(44)));
                        spEstiloTratamiento.setSelection(Arrays.asList(idsEstiloTratamientoTinte).indexOf(datos.getString(45)));
                    }
                    if (Integer.parseInt(datos.getString(30)) > 0) {
                        //Si cbTratamientoBlueray fue chequeado
                        cbTratamientoBlueray.setChecked(true);
                    }
                    if(Integer.parseInt(datos.getString(46)) > 0){
                        //Si cbPolarizado fue chequeado
                        cbTratamientoPolarizado.setChecked(true);
                        spColorTratamientoPolarizado.setSelection(Arrays.asList(idsColorTratamientoPolarizado).indexOf(datos.getString(47)));
                    }
                    if(Integer.parseInt(datos.getString(48)) > 0){
                        //Si cbEspejo fue chequeado
                        cbTratamientoEspejeado.setChecked(true);
                        spColorTratamientoEspejeado.setSelection(Arrays.asList(idsColorTratamientoEspejo).indexOf(datos.getString(49)));
                    }

                    //Validaciones para polarizado
                    String valorTratamientoPolarizado = datos.getString(46);
                    valorTratamientoPolarizado = (valorTratamientoPolarizado.equals(""))? "0": valorTratamientoPolarizado;

                    if(Integer.parseInt(valorTratamientoPolarizado) > 0) {
                        //Si cbPolarizado fue seleccionado
                        cbTratamientoPolarizado.setChecked(true);
                    }

                    if (Integer.parseInt(datos.getString(31)) > 0) {
                        //Si cbTratamientoOtro fue chequeado
                        cbTratamientoOtro.setChecked(true);
                        etOtroTratamientoNuevoHistorial.setText(datos.getString(32));
                        etPrecioTratamientoNuevoHistorial.setText(datos.getString(33));
                    }

                    if (datos.getString(34).length() > 0) {
                        if (Integer.parseInt(datos.getString(34)) == 0) {
                            //Si rbFT fue chequeado
                            rbFT.setChecked(true);
                        }
                        if (Integer.parseInt(datos.getString(34)) == 1) {
                            //Si rbBlend fue chequeado
                            rbBlend.setChecked(true);
                        }
                        if (Integer.parseInt(datos.getString(34)) == 2) {
                            //Si rbProgresivo fue chequeado
                            rbProgresivo.setChecked(true);
                        }
                        if (Integer.parseInt(datos.getString(34)) == 3) {
                            //Si rbNA fue chequeado
                            rbNA.setChecked(true);
                        }
                        if (Integer.parseInt(datos.getString(34)) == 4) {
                            //Si rbOtroBifocal fue chequeado
                            rbOtroBifocal.setChecked(true);
                            etOtroBifocalNuevoHistorial.setText(datos.getString(37));
                            etPrecioBifocalNuevoHistorial.setText(datos.getString(38));
                            otroBifocalBD = datos.getString(37);
                            otroBifocalPrecioBD = datos.getString(38);
                        }

                        bifocalBD = datos.getString(34);
                    }

                    etObservacionesNuevoHistorial.setText(datos.getString(35));
                    etObservacionesInternoNuevoHistorial.setText(datos.getString(36));

                }

            }

            sqLiteDB2.close();
            datos.close();

            if(idPaquete.equals("1") || idPaquete.equals("6")) {
                //LECTURA o DORADO 2
                llenarCamposHistorialSinConversion(idHistorialClinico);
            }

        }catch (SQLiteException e) {
            global.escribirError(e, 119);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void llenarCamposHistorialSinConversion(String idHistorialClinico) {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ESFERICODER, CILINDRODER, EJEDER, ADDDER, ESFERICOIZQ, CILINDROIZQ, EJEIZQ, ADDIZQ FROM HISTORIALSINCONVERSION" +
                    " WHERE ID_CONTRATO='" + idContratoHijo + "' AND ID_HISTORIAL = '" + idHistorialClinico + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                //No hay datos
                Log.i("MENSAJE", "No hay historialsinconversion registrado");
            }

            if (datos.moveToFirst()) {
                //Si hay datos

                //Obtener datos del historialsinconversion y mostrarlos en los campos de texto
                etOjoDerechoEsfericoSinConversionNuevoHistorial.setText(datos.getString(0));
                etOjoDerechoCilindroSinConversionNuevoHistorial.setText(datos.getString(1));
                etOjoDerechoEjeSinConversionNuevoHistorial.setText(datos.getString(2));
                etOjoDerechoAddSinConversionNuevoHistorial.setText(datos.getString(3));
                etOjoIzquierdoEsfericoSinConversionNuevoHistorial.setText(datos.getString(4));
                etOjoIzquierdoCilindroSinConversionNuevoHistorial.setText(datos.getString(5));
                etOjoIzquierdoEjeSinConversionNuevoHistorial.setText(datos.getString(6));
                etOjoIzquierdoAddSinConversionNuevoHistorial.setText(datos.getString(7));

            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 120);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private String obtenerIdHistorialContratoPorCrear() {

        String idHistorialClinico = "";

        try{
            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID FROM HISTORIALCLINICO WHERE ID_CONTRATO='" + idContratoHijo + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);
            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el ID el en historial");
            }

            if (datos.moveToFirst()){
                idHistorialClinico = datos.getString(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 121);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return idHistorialClinico;
    }

    /*Metodo/Funcion: llenarCamposContratoPadre
     Descripcion: Consulta los datos del contrato padre, se obtienen y se muestran en los campos correspondientes en la vista
   */
    private void llenarCamposContratoPadre(String idContrato, boolean esContratoHijo) {

        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID_ZONA, ID_OPTOMETRISTA, NOMBRE, CALLE, NUMERO, DEPTO, ALLADODE, FRENTEA, ENTRECALLES, COLONIA," +
                    " LOCALIDAD, TELEFONO, TELEFONOREFERENCIA, NOMBREREFERENCIA, CASATIPO, CASACOLOR, CORREO, FOTOINEFRENTE," +
                    " FOTOINEATRAS, FOTOCASA, COMPROBANTEDOMICILIO, PAGARE, FOTOOTROS, CALLEENTREGA, NUMEROENTREGA, DEPTOENTREGA," +
                    " ALLADODEENTREGA, FRENTEAENTREGA, ENTRECALLESENTREGA, COLONIAENTREGA, LOCALIDADENTREGA, CASATIPOENTREGA, CASACOLORENTREGA, ALIAS, OPCIONLUGARENTREGA," +
                    " OBSERVACIONFOTOINE, OBSERVACIONFOTOINEATRAS, OBSERVACIONFOTOCASA, OBSERVACIONCOMPROBANTEDOMICILIO, OBSERVACIONPAGARE, OBSERVACIONFOTOOTROS" +
                    " FROM CONTRATOS WHERE ID_CONTRATO='" + idContrato + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                //No hay datos
                Log.i("MENSAJE", "No hay contrato padre registrado");
            }

            if (datos.moveToFirst()) {
                //Si hay datos

                if(esContratoHijo) {

                    //Obtener la posicion de la BD del id_zona y agregarla al spZona
                    spZona.setSelection(Arrays.asList(idsZonas).indexOf(datos.getString(0)));

                    //Obtener la posicion de la BD del id_optometrista y agregarla al spOptometrista
                    spOptometrista.setSelection(Arrays.asList(idsOptometristas).indexOf(datos.getString(1)));
                    spOptometrista.setEnabled(false);

                    //Obtener datos del contrato y mostrarlos en los campos de texto
                    etNombreCliente.setText(datos.getString(2));
                    etCalleCliente.setText(datos.getString(3));
                    etNumeroCliente.setText(datos.getString(4));
                    etDepartamentoCliente.setText(datos.getString(5));
                    etAlLadoDeCliente.setText(datos.getString(6));
                    etFrenteACliente.setText(datos.getString(7));
                    etEntreCallesCliente.setText(datos.getString(8));
                    etColoniaCliente.setText(datos.getString(9));
                    etLocalidadCliente.setText(datos.getString(10));
                    etTelefonoCliente.setText(datos.getString(11));
                    etTelefonoReferenciaCliente.setText(datos.getString(12));
                    etNombreReferenciaCliente.setText(datos.getString(13));
                    etTipoCasaCliente.setText(datos.getString(14));
                    etCasaColorCliente.setText(datos.getString(15));
                    etCorreoCliente.setText(datos.getString(16));

                    etCalleClienteEntrega.setText(datos.getString(23));
                    etNumeroClienteEntrega.setText(datos.getString(24));
                    etDepartamentoClienteEntrega.setText(datos.getString(25));
                    etAlLadoDeClienteEntrega.setText(datos.getString(26));
                    etFrenteAClienteEntrega.setText(datos.getString(27));
                    etEntreCallesClienteEntrega.setText(datos.getString(28));
                    spColoniaClienteEntrega.setSelection(Arrays.asList(nameColoniaLocalidad).indexOf(datos.getString(30) + "-"+datos.getString(29)));
                    etColoniaClienteEntrega.setText(datos.getString(29));
                    etLocalidadClienteEntrega.setText(datos.getString(30));
                    etTipoCasaClienteEntrega.setText(datos.getString(31));
                    etCasaColorClienteEntrega.setText(datos.getString(32));
                    etAliasCliente.setText(datos.getString(33));
                    spLugarEntrega.setSelection(Arrays.asList(idsOpcionesLugarEntrega).indexOf(datos.getString(34)));

                    etObservacionFotoINEFrente.setText(datos.getString(35));
                    etObservacionFotoINEAtras.setText(datos.getString(36));
                    etObservacionFotoCasa.setText(datos.getString(37));
                    etObservacionFotoComprobanteDomicilio.setText(datos.getString(38));
                    etObservacionFotoPagare.setText(datos.getString(39));
                    etObservacionOtraFoto.setText(datos.getString(40));

                    //Ocultar boton de copiarDatosLugarVenta
                    btnCopiarDatosLugarVenta.setVisibility(View.GONE);

                    if(idContratoPadre.length() != 0) {
                        //Tiene idContratoPadre

                        rutaINEFrente = datos.getString(17);
                        rutaINEAtras = datos.getString(18);
                        rutaCasa = datos.getString(19);
                        rutaComprobanteDomicilio = datos.getString(20);
                        rutaPagare = datos.getString(21);
                        rutaFotoOtros = datos.getString(22);

                        if(idContratoHijo.length() != 0) {
                            //Es un contrato hijo por crear
                            if (rutaINEFrente.equals(global.obtenerAtributoContrato(idContratoPadre, "FOTOINEFRENTE"))
                                && rutaINEAtras.equals(global.obtenerAtributoContrato(idContratoPadre, "FOTOINEATRAS"))
                                    && rutaCasa.equals(global.obtenerAtributoContrato(idContratoPadre, "FOTOCASA"))
                                        && rutaComprobanteDomicilio.equals(global.obtenerAtributoContrato(idContratoPadre, "COMPROBANTEDOMICILIO"))
                                            && rutaPagare.equals(global.obtenerAtributoContrato(idContratoPadre, "PAGARE"))
                                                && rutaFotoOtros.equals(global.obtenerAtributoContrato(idContratoPadre, "FOTOOTROS"))) {
                                cbDuplicarDocumentos.setChecked(true);
                                llFotos.setVisibility(View.GONE);

                            }else {

                                String[] rutas = new String[] {rutaINEFrente, rutaINEAtras, rutaCasa, rutaComprobanteDomicilio, rutaPagare, rutaFotoOtros};

                                for(int i = 0; i < rutas.length; i++) {
                                    if(rutas[i].length() > 0) {
                                        File foto = new File(file_path + "/luzatuvida/.nomedia/" + rutas[i]);
                                        Bitmap bitmap = null;
                                        if(foto.exists()) {
                                            //Si la foto existe en el directorio
                                            bitmap = BitmapFactory.decodeFile(foto.getAbsolutePath());
                                            if(i == 0) {
                                                ivFotoINEFrente.setBackground(null);
                                                ivFotoINEFrente.setImageBitmap(bitmap);
                                            }else if(i == 1) {
                                                ivFotoINEAtras.setBackground(null);
                                                ivFotoINEAtras.setImageBitmap(bitmap);
                                            }else if(i == 2) {
                                                ivFotoCasa.setBackground(null);
                                                ivFotoCasa.setImageBitmap(bitmap);
                                            }else if(i == 3) {
                                                ivFotoComprobanteDomicilio.setBackground(null);
                                                ivFotoComprobanteDomicilio.setImageBitmap(bitmap);
                                            }else if(i == 4) {
                                                ivFotoPagare.setBackground(null);
                                                ivFotoPagare.setImageBitmap(bitmap);
                                            }else if(i == 5) {
                                                ivFotoOtros.setBackground(null);
                                                ivFotoOtros.setImageBitmap(bitmap);
                                            }

                                        }
                                    }else {
                                        if(i == 0) {
                                            ivFotoINEFrente.setImageDrawable(null);
                                            ivFotoINEFrente.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.ine));
                                        }else if(i == 1) {
                                            ivFotoINEAtras.setImageDrawable(null);
                                            ivFotoINEAtras.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.ine));
                                        }else if(i == 2) {
                                            ivFotoCasa.setImageDrawable(null);
                                            ivFotoCasa.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.casa));
                                        }else if(i == 3) {
                                            ivFotoComprobanteDomicilio.setImageDrawable(null);
                                            ivFotoComprobanteDomicilio.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.comprobante));
                                        }else if(i == 4) {
                                            ivFotoPagare.setImageDrawable(null);
                                            ivFotoPagare.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.pagare));
                                        }else if(i == 5) {
                                            ivFotoOtros.setImageDrawable(null);
                                            ivFotoOtros.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.otrafoto));
                                        }
                                    }
                                }
                                cbDuplicarDocumentos.setChecked(false);
                                llFotos.setVisibility(View.VISIBLE);
                            }
                        }else {
                            //Es un contrato hijo por primera vez
                            cbDuplicarDocumentos.setChecked(true);
                            llFotos.setVisibility(View.GONE);
                        }
                        llDuplicarDocumentos.setVisibility(View.VISIBLE);
                    }

                    //Deseas actualizar el contrato?
                    if(!contratoActualizar){
                        //Bloquear los campos de texto
                        etCalleCliente.setEnabled(false);
                        etNumeroCliente.setEnabled(false);
                        etDepartamentoCliente.setEnabled(false);
                        etAlLadoDeCliente.setEnabled(false);
                        etFrenteACliente.setEnabled(false);
                        etEntreCallesCliente.setEnabled(false);
                        etColoniaCliente.setEnabled(false);
                        etLocalidadCliente.setEnabled(false);
                        etTelefonoReferenciaCliente.setEnabled(false);
                        etNombreReferenciaCliente.setEnabled(false);
                        etTipoCasaCliente.setEnabled(false);
                        etCasaColorCliente.setEnabled(false);

                        etCalleClienteEntrega.setEnabled(false);
                        etNumeroClienteEntrega.setEnabled(false);
                        etDepartamentoClienteEntrega.setEnabled(false);
                        etAlLadoDeClienteEntrega.setEnabled(false);
                        etFrenteAClienteEntrega.setEnabled(false);
                        etEntreCallesClienteEntrega.setEnabled(false);
                        etColoniaClienteEntrega.setEnabled(false);
                        etLocalidadClienteEntrega.setEnabled(false);
                        etTipoCasaClienteEntrega.setEnabled(false);
                        etCasaColorClienteEntrega.setEnabled(false);
                        spLugarEntrega.setEnabled(false);

                        //Cambio de color en las letras de los campos de texto
                        etCalleCliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etNumeroCliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etDepartamentoCliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etAlLadoDeCliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etFrenteACliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etEntreCallesCliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etColoniaCliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etLocalidadCliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etTelefonoReferenciaCliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etNombreReferenciaCliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etTipoCasaCliente.setTextColor(Color.parseColor("#5C5A5A"));
                        etCasaColorCliente.setTextColor(Color.parseColor("#5C5A5A"));

                        etCalleClienteEntrega.setTextColor(Color.parseColor("#5C5A5A"));
                        etNumeroClienteEntrega.setTextColor(Color.parseColor("#5C5A5A"));
                        etDepartamentoClienteEntrega.setTextColor(Color.parseColor("#5C5A5A"));
                        etAlLadoDeClienteEntrega.setTextColor(Color.parseColor("#5C5A5A"));
                        etFrenteAClienteEntrega.setTextColor(Color.parseColor("#5C5A5A"));
                        etEntreCallesClienteEntrega.setTextColor(Color.parseColor("#5C5A5A"));
                        etColoniaClienteEntrega.setTextColor(Color.parseColor("#5C5A5A"));
                        etLocalidadClienteEntrega.setTextColor(Color.parseColor("#5C5A5A"));
                        etTipoCasaClienteEntrega.setTextColor(Color.parseColor("#5C5A5A"));
                        etCasaColorClienteEntrega.setTextColor(Color.parseColor("#5C5A5A"));

                    }else{
                        //Actualizar contrato
                        llFotos.setVisibility(View.VISIBLE);

                        rutaINEFrente = datos.getString(17);
                        rutaINEAtras = datos.getString(18);
                        rutaCasa = datos.getString(19);
                        rutaComprobanteDomicilio = datos.getString(20);
                        rutaPagare = datos.getString(21);
                        rutaFotoOtros = datos.getString(22);
                        String[] rutas = new String[] {rutaINEFrente, rutaINEAtras, rutaCasa, rutaComprobanteDomicilio, rutaPagare, rutaFotoOtros};

                        for(int i = 0; i < rutas.length; i++) {
                            if (rutas[i].length() > 0) {
                                File foto = new File(file_path + "/luzatuvida/.nomedia/" + rutas[i]);
                                Bitmap bitmap = null;
                                if (foto.exists()) {
                                    //Si la foto existe en el directorio
                                    bitmap = BitmapFactory.decodeFile(foto.getAbsolutePath());
                                    if (i == 0) {
                                        ivFotoINEFrente.setBackground(null);
                                        ivFotoINEFrente.setImageBitmap(bitmap);
                                    } else if (i == 1) {
                                        ivFotoINEAtras.setBackground(null);
                                        ivFotoINEAtras.setImageBitmap(bitmap);
                                    } else if (i == 2) {
                                        ivFotoCasa.setBackground(null);
                                        ivFotoCasa.setImageBitmap(bitmap);
                                    } else if (i == 3) {
                                        ivFotoComprobanteDomicilio.setBackground(null);
                                        ivFotoComprobanteDomicilio.setImageBitmap(bitmap);
                                    } else if (i == 4) {
                                        ivFotoPagare.setBackground(null);
                                        ivFotoPagare.setImageBitmap(bitmap);
                                    } else if (i == 5) {
                                        ivFotoOtros.setBackground(null);
                                        ivFotoOtros.setImageBitmap(bitmap);
                                    }

                                }
                            } else {
                                if (i == 0) {
                                    ivFotoINEFrente.setImageDrawable(null);
                                    ivFotoINEFrente.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.ine));
                                } else if (i == 1) {
                                    ivFotoINEAtras.setImageDrawable(null);
                                    ivFotoINEAtras.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.ine));
                                } else if (i == 2) {
                                    ivFotoCasa.setImageDrawable(null);
                                    ivFotoCasa.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.casa));
                                } else if (i == 3) {
                                    ivFotoComprobanteDomicilio.setImageDrawable(null);
                                    ivFotoComprobanteDomicilio.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.comprobante));
                                } else if (i == 4) {
                                    ivFotoPagare.setImageDrawable(null);
                                    ivFotoPagare.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.pagare));
                                } else if (i == 5) {
                                    ivFotoOtros.setImageDrawable(null);
                                    ivFotoOtros.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.otrafoto));
                                }
                            }
                        }
                        pintarIconosImagenesSinCargarContratos(idContrato);
                        llPrincipalHistorialClinico.setVisibility(View.GONE);
                        btnCancelarContratoNuevo.setText("SALIR");
                        btnGuardarContratoNuevo.setText("ACTUALIZAR");
                    }

                }else {

                    //Obtener la posicion de la BD del id_zona y agregarla al spZona
                    spZona.setSelection(Arrays.asList(idsZonas).indexOf(datos.getString(0)));

                    //Obtener la posicion de la BD del id_optometrista y agregarla al spOptometrista
                    spOptometrista.setSelection(Arrays.asList(idsOptometristas).indexOf(datos.getString(1)));

                    //Obtener datos del contrato y mostrarlos en los campos de texto
                    etNombreCliente.setText(datos.getString(2));
                    etCalleCliente.setText(datos.getString(3));
                    etNumeroCliente.setText(datos.getString(4));
                    etDepartamentoCliente.setText(datos.getString(5));
                    etAlLadoDeCliente.setText(datos.getString(6));
                    etFrenteACliente.setText(datos.getString(7));
                    etEntreCallesCliente.setText(datos.getString(8));
                    etColoniaCliente.setText(datos.getString(9));
                    etLocalidadCliente.setText(datos.getString(10));
                    etTelefonoCliente.setText(datos.getString(11));
                    etTelefonoReferenciaCliente.setText(datos.getString(12));
                    etNombreReferenciaCliente.setText(datos.getString(13));
                    etTipoCasaCliente.setText(datos.getString(14));
                    etCasaColorCliente.setText(datos.getString(15));
                    etCorreoCliente.setText(datos.getString(16));

                    etCalleClienteEntrega.setText(datos.getString(23));
                    etNumeroClienteEntrega.setText(datos.getString(24));
                    etDepartamentoClienteEntrega.setText(datos.getString(25));
                    etAlLadoDeClienteEntrega.setText(datos.getString(26));
                    etFrenteAClienteEntrega.setText(datos.getString(27));
                    etEntreCallesClienteEntrega.setText(datos.getString(28));
                    spColoniaClienteEntrega.setSelection(Arrays.asList(nameColoniaLocalidad).indexOf(datos.getString(30) +"-"+datos.getString(29)));
                    etColoniaClienteEntrega.setText(datos.getString(29));
                    etLocalidadClienteEntrega.setText(datos.getString(30));
                    etTipoCasaClienteEntrega.setText(datos.getString(31));
                    etCasaColorClienteEntrega.setText(datos.getString(32));
                    etAliasCliente.setText(datos.getString(33));
                    spLugarEntrega.setSelection(Arrays.asList(idsOpcionesLugarEntrega).indexOf(datos.getString(34)));
                    llFotos.setVisibility(View.VISIBLE);

                    etObservacionFotoINEFrente.setText(datos.getString(35));
                    etObservacionFotoINEAtras.setText(datos.getString(36));
                    etObservacionFotoCasa.setText(datos.getString(37));
                    etObservacionFotoComprobanteDomicilio.setText(datos.getString(38));
                    etObservacionFotoPagare.setText(datos.getString(39));
                    etObservacionOtraFoto.setText(datos.getString(40));

                    rutaINEFrente = datos.getString(17);
                    rutaINEAtras = datos.getString(18);
                    rutaCasa = datos.getString(19);
                    rutaComprobanteDomicilio = datos.getString(20);
                    rutaPagare = datos.getString(21);
                    rutaFotoOtros = datos.getString(22);

                    String[] rutas = new String[] {rutaINEFrente, rutaINEAtras, rutaCasa, rutaComprobanteDomicilio, rutaPagare, rutaFotoOtros};

                    for(int i = 0; i < rutas.length; i++) {
                        if(rutas[i].length() > 0) {
                            File foto = new File(file_path + "/luzatuvida/.nomedia/" + rutas[i]);
                            Bitmap bitmap = null;
                            if(foto.exists()) {
                                //Si la foto existe en el directorio
                                bitmap = BitmapFactory.decodeFile(foto.getAbsolutePath());
                                if(i == 0) {
                                    ivFotoINEFrente.setBackground(null);
                                    ivFotoINEFrente.setImageBitmap(bitmap);
                                }else if(i == 1) {
                                    ivFotoINEAtras.setBackground(null);
                                    ivFotoINEAtras.setImageBitmap(bitmap);
                                }else if(i == 2) {
                                    ivFotoCasa.setBackground(null);
                                    ivFotoCasa.setImageBitmap(bitmap);
                                }else if(i == 3) {
                                    ivFotoComprobanteDomicilio.setBackground(null);
                                    ivFotoComprobanteDomicilio.setImageBitmap(bitmap);
                                }else if(i == 4) {
                                    ivFotoPagare.setBackground(null);
                                    ivFotoPagare.setImageBitmap(bitmap);
                                }else if(i == 5) {
                                    ivFotoOtros.setBackground(null);
                                    ivFotoOtros.setImageBitmap(bitmap);
                                }

                            }
                        }else {
                            if(i == 0) {
                                ivFotoINEFrente.setImageDrawable(null);
                                ivFotoINEFrente.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.ine));
                            }else if(i == 1) {
                                ivFotoINEAtras.setImageDrawable(null);
                                ivFotoINEAtras.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.ine));
                            }else if(i == 2) {
                                ivFotoCasa.setImageDrawable(null);
                                ivFotoCasa.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.casa));
                            }else if(i == 3) {
                                ivFotoComprobanteDomicilio.setImageDrawable(null);
                                ivFotoComprobanteDomicilio.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.comprobante));
                            }else if(i == 4) {
                                ivFotoPagare.setImageDrawable(null);
                                ivFotoPagare.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.pagare));
                            }else if(i == 5) {
                                ivFotoOtros.setImageDrawable(null);
                                ivFotoOtros.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.otrafoto));
                            }
                        }
                    }

                }

            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 122);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: llenarSpinnersBD
     Descripcion: Llenar spZona, spOptometrista, spProductoNuevoHistorial, spPaquetesNuevoHistorial de la BD
   */
    private void llenarSpinnersBD() {

        //spZona
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID, ZONA FROM ZONAS";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay zonas registradas");
            }

            idsZonas = new String[datos.getCount() + 1];
            zonas = new String[datos.getCount() + 1];
            idsZonas[0] = "";
            zonas[0] = "Seleccionar";

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){

                    idsZonas[i]= datos.getString(0);
                    zonas[i] = datos.getString(1);

                    datos.moveToNext();
                }

            }

            sqLiteDB.close();
            datos.close();

            spZona.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, zonas));

        }catch (SQLiteException e){
            global.escribirError(e, 123);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        //spOptometrista
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID, NAME FROM USERS WHERE ROL_ID = '12'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay optometristas registrados");
            }

            idsOptometristas = new String[datos.getCount() + 1];
            namesOptometristas = new String[datos.getCount() + 1];
            idsOptometristas[0] = "";
            namesOptometristas[0] = "Seleccionar";

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){
                    idsOptometristas[i]= datos.getString(0);
                    namesOptometristas[i] = datos.getString(1);
                    datos.moveToNext();
                }

            }

            sqLiteDB.close();
            datos.close();

            spOptometrista.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, namesOptometristas));

        }catch (SQLiteException e){
            global.escribirError(e, 124);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        //Agregar la posicion del id_optometrista de ultimo contrato creado al spOptometrista
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID_OPTOMETRISTA FROM CONTRATOS WHERE DATOS = 1 ORDER BY ID_CONTRATO DESC LIMIT 1";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro optometrista del ultimo contrato");
            }

            if (datos.moveToFirst()){
                int posicion = Arrays.asList(idsOptometristas).indexOf(datos.getString(0));
                spOptometrista.setSelection(posicion);
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 125);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        //spProductoNuevoHistorial
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID, NOMBRE, COLOR, PIEZAS FROM PRODUCTO WHERE ID_TIPOPRODUCTO = '1' ORDER BY NOMBRE";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay productos registrados");
            }

            idsProductos = new String[datos.getCount() + 1];
            nombresProductos = new String[datos.getCount() + 1];
            coloresRBGProductos = new int[datos.getCount() + 1];
            idsProductos[0] = "";
            nombresProductos[0] = "Seleccionar";
            coloresRBGProductos[0] = Color.WHITE;

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){
                    idsProductos[i]= datos.getString(0);
                    nombresProductos[i] = datos.getString(1) + " | "+datos.getString(2)+ " | "+datos.getString(3)+"pza.";

                    if(Integer.parseInt(datos.getString(3)) <= 10){
                        coloresRBGProductos[i] = Color.RED;
                    }else{
                        coloresRBGProductos[i] = Color.WHITE;
                    }
                    datos.moveToNext();
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

            spProductoNuevoHistorial.setAdapter(adapter);

        }catch (SQLiteException e){
            global.escribirError(e, 126);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        //spPaquetesNuevoHistorial
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

            spPaquetesNuevoHistorial.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, nombresPaquetes));

        }catch (SQLiteException e){
            global.escribirError(e, 127);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        // spColoniaClienteEntrega
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID_ZONA, COLONIA, LOCALIDAD FROM COLONIAS ORDER BY  LOCALIDAD ASC, COLONIA ASC";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay colonias registradas");
            }

            idsZonaColonias = new String[datos.getCount() + 1];
            nameColonias = new String[datos.getCount() + 1];
            nameLocalidad = new String[datos.getCount() + 1];
            nameColoniaLocalidad = new String[datos.getCount() + 1];
            idsZonaColonias[0] = "";
            nameColonias[0] = "";
            nameLocalidad[0] = "";
            nameColoniaLocalidad[0] = "Seleccionar";

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){
                    idsZonaColonias[i]= datos.getString(0);
                    nameColonias[i] = datos.getString(1);
                    nameLocalidad[i] = datos.getString(2);
                    nameColoniaLocalidad[i] = datos.getString(2) + "-" + datos.getString(1);
                    datos.moveToNext();
                }

            }

            sqLiteDB.close();
            datos.close();

            //Llenar sp colonias cliente lugar de entrega
            spColoniaClienteEntrega.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, nameColoniaLocalidad));

        }catch (SQLiteException e){
            global.escribirError(e, 173);
            Log.i("ERRORBD", e.getMessage() + "");
        }

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
            spColorTratamiento.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, colorTratamientoTinte));

        }catch (SQLiteException e){
            global.escribirError(e, 300);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        //spTratamientoPolarizado
        idsEstiloTratamientoTinte = new String[]{"","0", "1"};
        estiloTratamientoTinte = new String[]{"Seleccionar","DESVANECIDO", "COMPLETO"};
        spEstiloTratamiento.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, estiloTratamientoTinte));

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
            spColorTratamientoPolarizado.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, colorTratamientoPolarizado));

        }catch (SQLiteException e){
            global.escribirError(e, 302);
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
            spColorTratamientoEspejeado.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, colorTratamientoEspejo));

        }catch (SQLiteException e){
            global.escribirError(e, 303);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        //Sppiner lugar de entrega Ventas
        idsOpcionesLugarEntrega = new String[] {"", "0", "1"};
        opcionesLugarEntrega = new String[] {"Seleccionar", "Lugar de venta", "Lugar de cobranza"};

        //Llenar spLugarEntregaVentas
        spLugarEntrega.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, opcionesLugarEntrega));

    }

    /*Metodo/Funcion: validacionPaquetes
     Descripcion: Valida los paquetes dependiendo de cual fue seleccionado (LECTURA, PROTECCION, ECO JR, ETC.)
   */
    public void validacionPaquetes() {

        correcto = true;
        Arrays.fill(arregloComponentes, Boolean.FALSE);
        for(int i=0;i<arregloComponentes.length;i++){
            mostrarOcultarMensajesError(i,false);
        }

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 0) { //NO SELECCIONADO
            arregloComponentes[27] = true;
            validacionCamposPaqueteDos();
        }

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 1) { //LECTURA
            validacionCamposPaqueteUno();
        }

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 2) { //PROTECCION
            validacionCamposPaqueteDos();
        }

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 3) { //ECO JR
            validacionCamposPaqueteUno();
        }

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 4) { //JR
            validacionCamposPaqueteUno();
        }

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 5) { //DORADO 1
            validacionCamposPaqueteTres();
        }

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 6) { //DORADO 2
            validacionCamposPaqueteUno();
        }

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 7) { //PLATINO
            validacionCamposPaqueteTres();
        }

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 8) { //PREMIUM
            validacionCamposPaquetePremium();
        }

        //Descargar imagenes mediante Url
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //En caso de que el servicio haya sido denegado
            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permisos = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permisos, PERMISO_ALMACENAMIENTO);
            }else {
                camara.iniciarDescargaImagenURL();
            }

        }else {
            camara.iniciarDescargaImagenURL();
        }*/

    }

    /*Metodo/Funcion: validacionCamposPaqueteUno
     Descripcion: Valida los paquetes (LECTURA, ECO JR, JR y DORADO 2)
   */
    private void validacionCamposPaqueteUno() {

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
            Toast.makeText(fragmento.getContext(),"Uno o mas campos vacios",Toast.LENGTH_LONG).show();
        }

    }

    /*Metodo/Funcion: verificarComponentesDiferentesPaqueteUno
     Descripcion: Validacion de componentes diferentes al paquete uno
   */
    private void verificarComponentesDiferentesPaqueteUno() {

        if(etOjoDerechoEsfericoNuevoHistorial.getText().toString().equals("") || etOjoDerechoEsfericoNuevoHistorial.getText().toString().equals("+")
                || etOjoDerechoEsfericoNuevoHistorial.getText().toString().equals("-")){
            arregloComponentes[29] = true;
        }

        if(etOjoDerechoCilindroNuevoHistorial.getText().toString().equals("") || etOjoDerechoCilindroNuevoHistorial.getText().toString().equals("+")
                || etOjoDerechoCilindroNuevoHistorial.getText().toString().equals("-")){
            arregloComponentes[30] = true;
        }

        if(etOjoDerechoEjeNuevoHistorial.getText().toString().equals("")){
            arregloComponentes[31] = true;
        }

        if(etOjoIzquierdoEsfericoNuevoHistorial.getText().toString().equals("") || etOjoIzquierdoEsfericoNuevoHistorial.getText().toString().equals("+")
                || etOjoIzquierdoEsfericoNuevoHistorial.getText().toString().equals("-")){
            arregloComponentes[34] = true;
        }

        if(etOjoIzquierdoCilindroNuevoHistorial.getText().toString().equals("") || etOjoIzquierdoCilindroNuevoHistorial.getText().toString().equals("+")
                || etOjoIzquierdoCilindroNuevoHistorial.getText().toString().equals("-")){
            arregloComponentes[35] = true;
        }

        if(etOjoIzquierdoEjeNuevoHistorial.getText().toString().equals("")){
            arregloComponentes[36] = true;
        }

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 1 || spPaquetesNuevoHistorial.getSelectedItemPosition() == 6) { //LECTURA o DORADO 2
            //Validar si son diferente de vacio los valores de historialsinconversion
            if(etOjoDerechoEsfericoSinConversionNuevoHistorial.getText().toString().equals("") || etOjoDerechoCilindroSinConversionNuevoHistorial.getText().toString().equals("")
                    || etOjoDerechoEjeSinConversionNuevoHistorial.getText().toString().equals("") || etOjoDerechoAddSinConversionNuevoHistorial.getText().toString().equals("")
                    || etOjoIzquierdoEsfericoSinConversionNuevoHistorial.getText().toString().equals("") || etOjoIzquierdoCilindroSinConversionNuevoHistorial.getText().toString().equals("")
                    || etOjoIzquierdoEjeSinConversionNuevoHistorial.getText().toString().equals("") || etOjoIzquierdoAddSinConversionNuevoHistorial.getText().toString().equals("")) {
                arregloComponentes[46] = true;
            }
        }

    }

    /*Metodo/Funcion: validacionCamposPaqueteDos
     Descripcion: Valida los paquetes (NO SELECCIONADO y PROTECCION)
   */
    private void validacionCamposPaqueteDos() {

        verificarComponentesIguales();
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
            Toast.makeText(fragmento.getContext(),"Uno o mas campos vacios",Toast.LENGTH_LONG).show();
        }

    }

    /*Metodo/Funcion: validacionCamposPaqueteTres
     Descripcion: Valida los paquetes (DORADO 1 y PLATINO)
   */
    private void validacionCamposPaqueteTres() {

        verificarComponentesIguales();
        verificarComponentesDiferentesPaqueteTres();
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
            Toast.makeText(fragmento.getContext(),"Uno o mas campos vacios",Toast.LENGTH_LONG).show();
        }

    }

    /*Metodo/Funcion: verificarComponentesDiferentesPaqueteTres
     Descripcion: Validacion de componentes diferentes al paquete tres
   */
    private void verificarComponentesDiferentesPaqueteTres() {

        if(etOjoDerechoEsfericoNuevoHistorial.getText().toString().equals("") || etOjoDerechoEsfericoNuevoHistorial.getText().toString().equals("+")
                || etOjoDerechoEsfericoNuevoHistorial.getText().toString().equals("-")){
            arregloComponentes[29] = true;
        }

        if(etOjoDerechoCilindroNuevoHistorial.getText().toString().equals("") || etOjoDerechoCilindroNuevoHistorial.getText().toString().equals("+")
                || etOjoDerechoCilindroNuevoHistorial.getText().toString().equals("-")){
            arregloComponentes[30] = true;
        }

        if(etOjoDerechoEjeNuevoHistorial.getText().toString().equals("")){
            arregloComponentes[31] = true;
        }

        if(etOjoDerechoAddNuevoHistorial.getText().toString().equals("") || etOjoDerechoAddNuevoHistorial.getText().toString().equals("+")
                || etOjoDerechoAddNuevoHistorial.getText().toString().equals("-")){
            arregloComponentes[32] = true;
        }

        if(etOjoDerechoALTNuevoHistorial.getText().toString().equals("")){
            arregloComponentes[33] = true;
        }

        if(etOjoIzquierdoEsfericoNuevoHistorial.getText().toString().equals("") || etOjoIzquierdoEsfericoNuevoHistorial.getText().toString().equals("+")
                || etOjoIzquierdoEsfericoNuevoHistorial.getText().toString().equals("-")){
            arregloComponentes[34] = true;
        }

        if(etOjoIzquierdoCilindroNuevoHistorial.getText().toString().equals("") || etOjoIzquierdoCilindroNuevoHistorial.getText().toString().equals("+")
                || etOjoIzquierdoCilindroNuevoHistorial.getText().toString().equals("-")){
            arregloComponentes[35] = true;
        }

        if(etOjoIzquierdoEjeNuevoHistorial.getText().toString().equals("")){
            arregloComponentes[36] = true;
        }

        if(etOjoIzquierdoAddNuevoHistorial.getText().toString().equals("") || etOjoIzquierdoAddNuevoHistorial.getText().toString().equals("+")
                || etOjoIzquierdoAddNuevoHistorial.getText().toString().equals("-")){
            arregloComponentes[37] = true;
        }

        if(etOjoIzquierdoALTNuevoHistorial.getText().toString().equals("")){
            arregloComponentes[38] = true;
        }

    }

    /*Metodo/Funcion: validacionCamposPaquetePremium
    Descripcion: Valida los paquetes (Premium)
    */
    private void validacionCamposPaquetePremium() {

        verificarComponentesIguales();
        verificarComponentesDiferentesPaqueteTres();
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
            //Validar graduaciones para esfera y cilindro

            if(validarGraducaionesPaquetePremium()){
                //Formulario correcto
                enviarDatosGuardar();
            }else{
                //Graduaciones incorrectas
                Toast.makeText(fragmento.getContext(),"Uno o mas campos incorrectos, verifica graduaciones",Toast.LENGTH_LONG).show();
            }

        }else{
            //Formulario incorrecto
            Toast.makeText(fragmento.getContext(),"Uno o mas campos vacios",Toast.LENGTH_LONG).show();
        }
    }

    private boolean validarGraducaionesPaquetePremium(){
        boolean correcto = true;

        if(Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial.getText().toString()) < -10.0 ||
                Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial.getText().toString()) > 8.0){
            //Esferico ojo derecho fuera de limites de graduacion
            tvOjoDerechoEsfericoNuevoHistorial.setText("Limite de graduación: -10.0 a 8.0");
            tvOjoDerechoEsfericoNuevoHistorial.setVisibility(View.VISIBLE);
            correcto = false;
        }else{
            tvOjoDerechoEsfericoNuevoHistorial.setText("Campo vacio.");
            tvOjoDerechoEsfericoNuevoHistorial.setVisibility(View.GONE);
        }

        if(Double.parseDouble(etOjoDerechoCilindroNuevoHistorial.getText().toString()) < -6.0 ||
                Double.parseDouble(etOjoDerechoCilindroNuevoHistorial.getText().toString()) > 0.0){
            //Esferico ojo derecho fuera de limites de graduacion
            tvOjoDerechoCilindroNuevoHistorial.setText("Limite de graduación: -6.0 a 0.0");
            tvOjoDerechoCilindroNuevoHistorial.setVisibility(View.VISIBLE);
            correcto = false;
        }else{
            tvOjoDerechoCilindroNuevoHistorial.setText("Campo vacio.");
            tvOjoDerechoCilindroNuevoHistorial.setVisibility(View.GONE);
        }

        if(Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial.getText().toString()) < -10.0 ||
                Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial.getText().toString()) > 8.0){
            //Esferico ojo derecho fuera de limites de graduacion
            tvOjoIzquierdoEsfericoNuevoHistorial.setText("Limite de graduación: -10.0 a 8.0");
            tvOjoIzquierdoEsfericoNuevoHistorial.setVisibility(View.VISIBLE);
            correcto = false;
        }else{
            tvOjoIzquierdoEsfericoNuevoHistorial.setText("Campo vacio.");
            tvOjoIzquierdoEsfericoNuevoHistorial.setVisibility(View.GONE);
        }

        if(Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial.getText().toString()) < -6.0 ||
                Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial.getText().toString()) > 0.0){
            //Esferico ojo derecho fuera de limites de graduacion
            tvOjoIzquierdoCilindroNuevoHistorial.setText("Limite de graduación: -6.0 a 0.0");
            tvOjoIzquierdoCilindroNuevoHistorial.setVisibility(View.VISIBLE);
            correcto = false;
        }else{
            tvOjoIzquierdoCilindroNuevoHistorial.setText("Campo vacio.");
            tvOjoIzquierdoCilindroNuevoHistorial.setVisibility(View.GONE);
        }

        return correcto;
    }

    /*Metodo/Funcion: validarArreglo
     Descripcion: Validacion de componentes, para verificar si esta correctos o no, si esta correcto retorna true si no false
   */
    private boolean validarArreglo(int i) {

        switch (i) {
            case 0: //tvZona
                if(!arregloComponentes[0])
                    return true;
                else
                    return false;
            case 1: //tvOptometrista
                if(!arregloComponentes[1])
                    return true;
                else
                    return false;
            case 2: //tvNombreCliente
                if(!arregloComponentes[2])
                    return true;
                else
                    return false;
            case 3: //tvCalleCliente
                if(!arregloComponentes[3])
                    return true;
                else
                    return false;
            case 4: //tvNumeroCliente
                if(!arregloComponentes[4])
                    return true;
                else
                    return false;
            case 5: //tvDepartamentoCliente
                if(!arregloComponentes[5])
                    return true;
                else
                    return false;
            case 6: //tvAlLadoDeCliente
                if(!arregloComponentes[6])
                    return true;
                else
                    return false;
            case 7: //tvFrenteACliente
                if(!arregloComponentes[7])
                    return true;
                else
                    return false;
            case 8: //tvEntreCallesCliente
                if(!arregloComponentes[8])
                    return true;
                else
                    return false;
            case 9: //tvColoniaCliente
                if(!arregloComponentes[9])
                    return true;
                else
                    return false;
            case 10: //tvLocalidadCliente
                if(!arregloComponentes[10])
                    return true;
                else
                    return false;
            case 11: //tvTelefonoCliente
                if(!arregloComponentes[11])
                    return true;
                else
                    return false;
            case 12: //tvTelefonoReferenciaCliente
                if(!arregloComponentes[12])
                    return true;
                else
                    return false;
            case 13: //tvNombreReferenciaCliente
                if(!arregloComponentes[13])
                    return true;
                else
                    return false;
            case 14: //tvTipoCasaCliente
                if(!arregloComponentes[14])
                    return true;
                else
                    return false;
            case 15: //tvCasaColorCliente
                if(!arregloComponentes[15])
                    return true;
                else
                    return false;
            case 16: //tvFotoINEFrente
                if(!arregloComponentes[16])
                    return true;
                else
                    return false;
            case 17: //tvFotoINEAtras
                if(!arregloComponentes[17])
                    return true;
                else
                    return false;
            case 18: //tvFotoCasa
                if(!arregloComponentes[18])
                    return true;
                else
                    return false;
            case 19: //tvFotoComprobanteDomicilio
                if(!arregloComponentes[19])
                    return true;
                else
                    return false;
            case 20: //tvEdadNuevoHistorial
                if(!arregloComponentes[20])
                    return true;
                else
                    return false;
            case 21: //tvDiagnosticoNuevoHistorial
                if(!arregloComponentes[21])
                    return true;
                else
                    return false;
            case 22: //tvOcupacionNuevoHistorial
                if(!arregloComponentes[22])
                    return true;
                else
                    return false;
            case 23: //tvDiabetesNuevoHistorial
                if(!arregloComponentes[23])
                    return true;
                else
                    return false;
            case 24: //tvHipertensionNuevoHistorial
                if(!arregloComponentes[24])
                    return true;
                else
                    return false;
            case 25: //tvOtroMoletiaNuevoHistorial
                if(cbDolorCabeza.isChecked() || cbArdorOjos.isChecked() || cbGolpeCabeza.isChecked() || cbOtroMolestia.isChecked()) {
                    if(cbOtroMolestia.isChecked()) {
                        if(etOtroMoletiaNuevoHistorial.getText().toString().equals("")) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            case 26: //tvProductoNuevoHistorial
                if(!arregloComponentes[26])
                    return true;
                else
                    return false;
            case 27: //tvPaquetesNuevoHistorial
                if(!arregloComponentes[27])
                    return true;
                else
                    return false;
            case 28: //tvFechaEntregaNuevoHistorial
                if(!arregloComponentes[28])
                    return true;
                else
                    return false;
            case 29: //tvOjoDerechoEsfericoNuevoHistorial
                if(!arregloComponentes[29])
                    return true;
                else
                    return false;
            case 30: //tvOjoDerechoCilindroNuevoHistorial
                if(!arregloComponentes[30])
                    return true;
                else
                    return false;
            case 31: //tvOjoDerechoEjeNuevoHistorial
                if(!arregloComponentes[31])
                    return true;
                else
                    return false;
            case 32: //tvOjoDerechoAddNuevoHistorial
                if(!arregloComponentes[32])
                    return true;
                else
                    return false;
            case 33: //tvOjoDerechoALTNuevoHistorial
                if(!arregloComponentes[33])
                    return true;
                else
                    return false;
            case 34: //tvOjoIzquierdoEsfericoNuevoHistorial
                if(!arregloComponentes[34])
                    return true;
                else
                    return false;
            case 35: //tvOjoIzquierdoCilindroNuevoHistorial
                if(!arregloComponentes[35])
                    return true;
                else
                    return false;
            case 36: //tvOjoIzquierdoEjeNuevoHistorial
                if(!arregloComponentes[36])
                    return true;
                else
                    return false;
            case 37: //tvOjoIzquierdoAddNuevoHistorial
                if(!arregloComponentes[37])
                    return true;
                else
                    return false;
            case 38: //tvOjoIzquierdoALTNuevoHistorial
                if(!arregloComponentes[38])
                    return true;
                else
                    return false;
            case 39: //tvOtroMaterialNuevoHistorial
                if(rbHiIndex.isChecked() || rbCR.isChecked() || rbPolicarbonato.isChecked() || rbOtroMaterial.isChecked()) {
                    if(rbOtroMaterial.isChecked()) {
                        if(etOtroMaterialNuevoHistorial.getText().toString().equals("")) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            case 40: //tvPrecioMaterialNuevoHistorial
                if(rbOtroMaterial.isChecked()) {
                    if(etPrecioMaterialNuevoHistorial.getText().toString().equals("")) {
                        return false;
                    }
                }
                return true;
            case 41: //tvOtroTratamientoNuevoHistorial
                if(cbTratamientoFotocromatico.isChecked() || cbTratamientoAR.isChecked() || cbTratamientoTinte.isChecked()
                        || cbTratamientoPolarizado.isChecked() || cbTratamientoEspejeado.isChecked() || cbTratamientoBlueray.isChecked() || cbTratamientoOtro.isChecked()) {
                    if(cbTratamientoOtro.isChecked()) {
                        if(etOtroTratamientoNuevoHistorial.getText().toString().equals("")) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            case 42: //tvPrecioTratamientoNuevoHistorial
                if(cbTratamientoOtro.isChecked()) {
                    if(etPrecioTratamientoNuevoHistorial.getText().toString().equals("")) {
                        return false;
                    }
                }
                return true;
            case 43: //tvTipoBifocalNuevoHistorial
                if(rbFT.isChecked() || rbBlend.isChecked() || rbProgresivo.isChecked() || rbNA.isChecked() || rbOtroBifocal.isChecked()) {
                    return true;
                }
                return false;
            case 44: //tvCorreoCliente
                if(!arregloComponentes[44])
                    return true;
                else
                    return false;
            case 45: //tvFotoPagare
                if(!arregloComponentes[45])
                    return true;
                else
                    return false;
            case 46: //tvGeneralSinConversionNuevoHistorial
                if(!arregloComponentes[46])
                    return true;
                else
                    return false;
            case 47: //tvEmbarazadaNuevoHistorial
                if(!arregloComponentes[47])
                    return true;
                else
                    return false;
            case 48: //tvDurmioSeisOchoHorasNuevoHistorial
                if(!arregloComponentes[48])
                    return true;
                else
                    return false;
            case 49: //tvActividadDiaNuevoHistorial
                if(!arregloComponentes[49])
                    return true;
                else
                    return false;
            case 50: //tvProblemasOjosNuevoHistorial
                if(!arregloComponentes[50])
                    return true;
                else
                    return false;
            case 51: //tvOtroBifocalNuevoHistorial
                if(rbFT.isChecked() || rbBlend.isChecked() || rbProgresivo.isChecked() || rbNA.isChecked() || rbOtroBifocal.isChecked()) {
                    if(rbOtroBifocal.isChecked()) {
                        if(etOtroBifocalNuevoHistorial.getText().toString().equals("")) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            case 52: //tvPrecioBifocalNuevoHistorial
                if(rbOtroBifocal.isChecked()) {
                    if(etPrecioBifocalNuevoHistorial.getText().toString().equals("")) {
                        return false;
                    }
                }
                return true;
            case 53: //tvCalleClienteEntrega
                if(!arregloComponentes[53])
                    return true;
                else
                    return false;
            case 54: //tvNumeroClienteEntrega
                if(!arregloComponentes[54])
                    return true;
                else
                    return false;
            case 55: //tvDepartamentoClienteEntrega
                if(!arregloComponentes[55])
                    return true;
                else
                    return false;
            case 56: //tvAlLadoDeClienteEntrega
                if(!arregloComponentes[56])
                    return true;
                else
                    return false;
            case 57: //tvFrenteAClienteEntrega
                if(!arregloComponentes[57])
                    return true;
                else
                    return false;
            case 58: //tvEntreCallesClienteEntrega
                if(!arregloComponentes[58])
                    return true;
                else
                    return false;
            case 59: //tvColoniaClienteEntrega
                if(!arregloComponentes[59])
                    return true;
                else
                    return false;
            case 60: //tvLocalidadClienteEntrega
                if(!arregloComponentes[60])
                    return true;
                else
                    return false;
            case 61: //tvTipoCasaClienteEntrega
                if(!arregloComponentes[61])
                    return true;
                else
                    return false;
            case 62: //tvCasaColorClienteEntrega
                if(!arregloComponentes[62])
                    return true;
                else
                    return false;
            case 63: //tvAliasCliente
                if(!arregloComponentes[63])
                    return true;
                else
                    return false;
            case 64: //tvOtraFoto
                if(!arregloComponentes[64])
                    return true;
                else
                    return false;
            case 65: //tvColorTratamiento
                if(!arregloComponentes[65])
                    return true;
                else
                    return false;
            case 66: //tvEstiloTratameinto
                if(!arregloComponentes[66])
                    return true;
                else
                    return false;
            case 67: //tvColorTratamientoPolarizado
                if(!arregloComponentes[67])
                    return true;
                else
                    return false;
            case 68: //tvColorTratamientoEspejo
                if(!arregloComponentes[68])
                    return true;
                else
                    return false;
            case 69: //tvLugarEntregaVentas
                if(!arregloComponentes[69])
                    return true;
                else
                    return false;
            case 70: //tvFotoArmazonPropio
                if(esArmazonPropio){
                    if(!arregloComponentes[70])
                        return true;
                    else
                        return false;
                }else {
                    return true;
                }
        }

        return false;
    }

    /*Metodo/Funcion: verificarComponentesIguales
     Descripcion: Validacion de componentes iguales en todos los paquetes
   */
    private void verificarComponentesIguales() {

        if(idsZonas[spZona.getSelectedItemPosition()] == ""){
            arregloComponentes[0] = true;
        }

        if(idsOptometristas[spOptometrista.getSelectedItemPosition()] == ""){
            arregloComponentes[1] = true;
        }

        if(etNombreCliente.getText().toString().trim().equals("")){
            arregloComponentes[2] = true;
        }

        if(etCalleCliente.getText().toString().trim().equals("")){
            arregloComponentes[3] = true;
        }

        if(etNumeroCliente.getText().toString().trim().equals("")){
            arregloComponentes[4] = true;
        }

        if(etDepartamentoCliente.getText().toString().trim().equals("")){
            arregloComponentes[5] = true;
        }

        if(etAlLadoDeCliente.getText().toString().trim().equals("")){
            arregloComponentes[6] = true;
        }

        if(etFrenteACliente.getText().toString().trim().equals("")){
            arregloComponentes[7] = true;
        }

        if(etEntreCallesCliente.getText().toString().trim().equals("")){
            arregloComponentes[8] = true;
        }

        if(etColoniaCliente.getText().toString().trim().equals("")){
            arregloComponentes[9] = true;
        }

        if(etLocalidadCliente.getText().toString().trim().equals("")){
            arregloComponentes[10] = true;
        }

        if(etTelefonoCliente.getText().toString().equals("")){
            tvTelefonoCliente.setText("Campo vacío");
            arregloComponentes[11] = true;
        }else {
            if(etTelefonoCliente.getText().toString().length() < 10) {
                tvTelefonoCliente.setText("Se requieren 10 dígitos");
                arregloComponentes[11] = true;
            }
        }

        if(etTelefonoReferenciaCliente.getText().toString().equals("")){
            tvTelefonoReferenciaCliente.setText("Campo vacío");
            arregloComponentes[12] = true;
        }else {
            if(etTelefonoReferenciaCliente.getText().toString().length() < 10) {
                tvTelefonoReferenciaCliente.setText("Se requieren 10 dígitos");
                arregloComponentes[12] = true;
            }
        }

        if(etNombreReferenciaCliente.getText().toString().trim().equals("")){
            arregloComponentes[13] = true;
        }

        if(etTipoCasaCliente.getText().toString().trim().equals("")){
            arregloComponentes[14] = true;
        }

        if(etCasaColorCliente.getText().toString().trim().equals("")){
            arregloComponentes[15] = true;
        }

        //Podras crear contratos sin imagenes
        /*
        if(!contratoActualizar) {
            if (idContratoPadre.length() != 0) {
                //Es un contrato hijo
                if (!cbDuplicarDocumentos.isChecked()) {
                    if (ivFotoINEFrente.getDrawable() == null) {
                        arregloComponentes[16] = true;
                    }

                    if (ivFotoINEAtras.getDrawable() == null) {
                        arregloComponentes[17] = true;
                    }

                    if (ivFotoCasa.getDrawable() == null) {
                        arregloComponentes[18] = true;
                    }

                    if (ivFotoComprobanteDomicilio.getDrawable() == null) {
                        arregloComponentes[19] = true;
                    }

                    if (ivFotoPagare.getDrawable() == null) {
                        arregloComponentes[45] = true;
                    }

                    if (ivFotoOtros.getDrawable() == null) {
                        arregloComponentes[64] = true;
                    }
                }
            } else {
                if (ivFotoINEFrente.getDrawable() == null) {
                    arregloComponentes[16] = true;
                }

                if (ivFotoINEAtras.getDrawable() == null) {
                    arregloComponentes[17] = true;
                }

                if (ivFotoCasa.getDrawable() == null) {
                    arregloComponentes[18] = true;
                }

                if (ivFotoComprobanteDomicilio.getDrawable() == null) {
                    arregloComponentes[19] = true;
                }

                if (ivFotoPagare.getDrawable() == null) {
                    arregloComponentes[45] = true;
                }

                if (ivFotoOtros.getDrawable() == null) {
                    arregloComponentes[64] = true;
                }
            }
        }
        */
        if(etEdadNuevoHistorial.getText().toString().trim().equals("")){
            arregloComponentes[20] = true;
        }

        if(etDiagnosticoNuevoHistorial.getText().toString().trim().equals("")){
            arregloComponentes[21] = true;
        }

        if(etOcupacionNuevoHistorial.getText().toString().trim().equals("")){
            arregloComponentes[22] = true;
        }

        if(etDiabetesNuevoHistorial.getText().toString().trim().equals("")){
            arregloComponentes[23] = true;
        }

        if(etHipertensionNuevoHistorial.getText().toString().trim().equals("")){
            arregloComponentes[24] = true;
        }

        if(idsProductos[spProductoNuevoHistorial.getSelectedItemPosition()] == ""){
            arregloComponentes[26] = true;
        }

        if(etFechaEntregaNuevoHistorial.getText().toString().equals("")){
            arregloComponentes[28] = true;
        }

        if(!etCorreoCliente.getText().toString().equals("")) {
            if(!isValidEmail(etCorreoCliente.getText().toString().trim())) {
                arregloComponentes[44] = true;
            }
        }

        if(etEmbarazadaNuevoHistorial.getText().toString().trim().equals("")){
            arregloComponentes[47] = true;
        }

        if(etDurmioSeisOchoHorasNuevoHistorial.getText().toString().trim().equals("")){
            arregloComponentes[48] = true;
        }

        if(etActividadDiaNuevoHistorial.getText().toString().trim().equals("")){
            arregloComponentes[49] = true;
        }

        if(etProblemasOjosNuevoHistorial.getText().toString().trim().equals("")){
            arregloComponentes[50] = true;
        }

        if(etCalleClienteEntrega.getText().toString().trim().equals("")){
            arregloComponentes[53] = true;
        }

        if(etNumeroClienteEntrega.getText().toString().trim().equals("")){
            arregloComponentes[54] = true;
        }

        if(etDepartamentoClienteEntrega.getText().toString().trim().equals("")){
            arregloComponentes[55] = true;
        }

        if(etAlLadoDeClienteEntrega.getText().toString().trim().equals("")){
            arregloComponentes[56] = true;
        }

        if(etFrenteAClienteEntrega.getText().toString().trim().equals("")){
            arregloComponentes[57] = true;
        }

        if(etEntreCallesClienteEntrega.getText().toString().trim().equals("")){
            arregloComponentes[58] = true;
        }

        if(etColoniaClienteEntrega.getText().toString().trim().equals("")){
            arregloComponentes[59] = true;
        }

        if(etLocalidadClienteEntrega.getText().toString().trim().equals("")){
            arregloComponentes[60] = true;
        }

        if(etTipoCasaClienteEntrega.getText().toString().trim().equals("")){
            arregloComponentes[61] = true;
        }

        if(etCasaColorClienteEntrega.getText().toString().trim().equals("")){
            arregloComponentes[62] = true;
        }

        if(etAliasCliente.getText().toString().trim().equals("")){
            arregloComponentes[63] = true;
        }

        if(idsOpcionesLugarEntrega[spLugarEntrega.getSelectedItemPosition()] == ""){
            arregloComponentes[69] = true;
        }
        if(esArmazonPropio && ivFotoArmazonPropio.getDrawable() == null) {
            arregloComponentes[70] = true;
        }

    }

    /*Metodo/Funcion: isValidEmail
     Descripcion: Validacion de email
   */
    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    /*Metodo/Funcion: verificarCheckBoxsRadioButtons
     Descripcion: Validacion de CheckBoxs y RadioButtons
   */
    private void verificarCheckBoxsRadioButtons() {

        //MOLESTIAS
        if(!cbDolorCabeza.isChecked() && !cbArdorOjos.isChecked() && !cbGolpeCabeza.isChecked() && !cbOtroMolestia.isChecked()){
            arregloComponentes[25] = true;
            tvOtroMoletiaNuevoHistorial.setText("Selecciona al menos una opción");
        }
        if(cbDolorCabeza.isChecked() || cbArdorOjos.isChecked() || cbGolpeCabeza.isChecked() || cbOtroMolestia.isChecked()) {
            if(cbOtroMolestia.isChecked()) {
                if(etOtroMoletiaNuevoHistorial.getText().toString().equals("")) {
                    tvOtroMoletiaNuevoHistorial.setText("Campo vacío");
                    arregloComponentes[25] = true;
                }
            }
        }

        //MATERIAL
        if(!rbHiIndex.isChecked() && !rbCR.isChecked() && !rbPolicarbonato.isChecked() && !rbOtroMaterial.isChecked()){
            tvPrecioMaterialNuevoHistorial.setText("Selecciona al menos una opción");
            arregloComponentes[40] = true;
        }

        if(rbHiIndex.isChecked() || rbCR.isChecked() || rbPolicarbonato.isChecked() || rbOtroMaterial.isChecked()) {
            if(rbOtroMaterial.isChecked()) {
                if(etOtroMaterialNuevoHistorial.getText().toString().equals("")) {
                    arregloComponentes[39] = true;
                }
            }
        }

        if(rbOtroMaterial.isChecked()) {
            if(etPrecioMaterialNuevoHistorial.getText().toString().equals("")) {
                tvPrecioMaterialNuevoHistorial.setText("Campo vacío");
                arregloComponentes[40] = true;
            }
        }

        //TRATAMIENTOS
        if(!cbTratamientoFotocromatico.isChecked() && !cbTratamientoAR.isChecked() && !cbTratamientoTinte.isChecked() && !cbTratamientoBlueray.isChecked()
                && !cbTratamientoEspejeado.isChecked() && !cbTratamientoPolarizado.isChecked() && !cbTratamientoOtro.isChecked()){
            tvPrecioTratamientoNuevoHistorial.setText("Selecciona al menos una opción");
            arregloComponentes[42] = true;
        }

        if(cbTratamientoFotocromatico.isChecked() || cbTratamientoAR.isChecked() || cbTratamientoTinte.isChecked()
                || cbTratamientoBlueray.isChecked() || cbTratamientoPolarizado.isChecked() || cbTratamientoEspejeado.isChecked() || cbTratamientoOtro.isChecked()) {
            if(cbTratamientoOtro.isChecked()) {
                if(etOtroTratamientoNuevoHistorial.getText().toString().equals("")) {
                    arregloComponentes[41] = true;
                }
            }
        }

        if(cbTratamientoOtro.isChecked()) {
            if(etPrecioTratamientoNuevoHistorial.getText().toString().equals("")) {
                tvPrecioTratamientoNuevoHistorial.setText("Campo vacío");
                arregloComponentes[42] = true;
            }
        }

        //TIPO BIFOCAL
        if(!rbFT.isChecked() && !rbBlend.isChecked() && !rbProgresivo.isChecked() && !rbNA.isChecked() && !rbOtroBifocal.isChecked()){
            arregloComponentes[43] = true;
        }

        if(!rbNA.isEnabled()){
            if(rbNA.isChecked()){
                rbNA.setChecked(false);
                arregloComponentes[43] = true;
            }
        }

        if(rbFT.isChecked() || rbBlend.isChecked() || rbProgresivo.isChecked() || rbNA.isChecked() || rbOtroBifocal.isChecked()) {
            if(rbOtroBifocal.isChecked()) {
                if(etOtroBifocalNuevoHistorial.getText().toString().equals("")) {
                    arregloComponentes[51] = true;
                }
            }
        }

        if(rbOtroBifocal.isChecked()) {
            if(etPrecioBifocalNuevoHistorial.getText().toString().equals("")) {
                arregloComponentes[52] = true;
            }
        }

        //TRATAMIENTO TINTE COLOR Y ESTILO
        if(cbTratamientoTinte.isChecked()){
            if (idsColorTratamientoTinte[spColorTratamiento.getSelectedItemPosition()].equals("")){
                arregloComponentes[65] = true;
            }
            if (idsEstiloTratamientoTinte[spEstiloTratamiento.getSelectedItemPosition()].equals("")){
                arregloComponentes[66] = true;
            }
        }

        //TRATAMIENTO POLARIZADO
        if(cbTratamientoPolarizado.isChecked()){
            if (idsColorTratamientoPolarizado[spColorTratamientoPolarizado.getSelectedItemPosition()].equals("")){
                arregloComponentes[67] = true;
            }
        }

        //TRATAMIENTO ESPEJO
        if(cbTratamientoEspejeado.isChecked()){
            if (idsColorTratamientoEspejo[spColorTratamientoEspejeado.getSelectedItemPosition()].equals("")){
                arregloComponentes[68] = true;
            }
        }

    }

    /*Metodo/Funcion: mostrarOcultarMensajesError
     Descripcion: Validacion de mostrar o ocultar el mensaje de error en los TextViews correspondientes
   */
    private void mostrarOcultarMensajesError(int i, boolean mostrarOcultar){

        switch (i) {
            case 0:
                if(arregloComponentes[0] && mostrarOcultar) {
                    tvZona.setVisibility(View.VISIBLE);
                }else {
                    tvZona.setVisibility(View.GONE);
                }
                break;
            case 1:
                if(arregloComponentes[1] && mostrarOcultar) {
                    tvOptometrista.setVisibility(View.VISIBLE);
                }else {
                    tvOptometrista.setVisibility(View.GONE);
                }
                break;
            case 2:
                if(arregloComponentes[2] && mostrarOcultar) {
                    tvNombreCliente.setVisibility(View.VISIBLE);
                }else {
                    tvNombreCliente.setVisibility(View.GONE);
                }
                break;
            case 3:
                if(arregloComponentes[3] && mostrarOcultar) {
                    tvCalleCliente.setVisibility(View.VISIBLE);
                }else {
                    tvCalleCliente.setVisibility(View.GONE);
                }
                break;
            case 4:
                if(arregloComponentes[4] && mostrarOcultar) {
                    tvNumeroCliente.setVisibility(View.VISIBLE);
                }else {
                    tvNumeroCliente.setVisibility(View.GONE);
                }
                break;
            case 5:
                if(arregloComponentes[5] && mostrarOcultar) {
                    tvDepartamentoCliente.setVisibility(View.VISIBLE);
                }else {
                    tvDepartamentoCliente.setVisibility(View.GONE);
                }
                break;
            case 6:
                if(arregloComponentes[6] && mostrarOcultar) {
                    tvAlLadoDeCliente.setVisibility(View.VISIBLE);
                }else {
                    tvAlLadoDeCliente.setVisibility(View.GONE);
                }
                break;
            case 7:
                if(arregloComponentes[7] && mostrarOcultar) {
                    tvFrenteACliente.setVisibility(View.VISIBLE);
                }else {
                    tvFrenteACliente.setVisibility(View.GONE);
                }
                break;
            case 8:
                if(arregloComponentes[8] && mostrarOcultar) {
                    tvEntreCallesCliente.setVisibility(View.VISIBLE);
                }else {
                    tvEntreCallesCliente.setVisibility(View.GONE);
                }
                break;
            case 9:
                if(arregloComponentes[9] && mostrarOcultar) {
                    tvColoniaCliente.setVisibility(View.VISIBLE);
                }else {
                    tvColoniaCliente.setVisibility(View.GONE);
                }
                break;
            case 10:
                if(arregloComponentes[10] && mostrarOcultar) {
                    tvLocalidadCliente.setVisibility(View.VISIBLE);
                }else {
                    tvLocalidadCliente.setVisibility(View.GONE);
                }
                break;
            case 11:
                if(arregloComponentes[11] && mostrarOcultar) {
                    tvTelefonoCliente.setVisibility(View.VISIBLE);
                }else {
                    tvTelefonoCliente.setVisibility(View.GONE);
                }
                break;
            case 12:
                if(arregloComponentes[12] && mostrarOcultar) {
                    tvTelefonoReferenciaCliente.setVisibility(View.VISIBLE);
                }else {
                    tvTelefonoReferenciaCliente.setVisibility(View.GONE);
                }
                break;
            case 13:
                if(arregloComponentes[13] && mostrarOcultar) {
                    tvNombreReferenciaCliente.setVisibility(View.VISIBLE);
                }else {
                    tvNombreReferenciaCliente.setVisibility(View.GONE);
                }
                break;
            case 14:
                if(arregloComponentes[14] && mostrarOcultar) {
                    tvTipoCasaCliente.setVisibility(View.VISIBLE);
                }else {
                    tvTipoCasaCliente.setVisibility(View.GONE);
                }
                break;
            case 15:
                if(arregloComponentes[15] && mostrarOcultar) {
                    tvCasaColorCliente.setVisibility(View.VISIBLE);
                }else {
                    tvCasaColorCliente.setVisibility(View.GONE);
                }
                break;
            case 16:
                if(arregloComponentes[16] && mostrarOcultar) {
                    tvFotoINEFrente.setVisibility(View.VISIBLE);
                }else {
                    tvFotoINEFrente.setVisibility(View.GONE);
                }
                break;
            case 17:
                if(arregloComponentes[17] && mostrarOcultar) {
                    tvFotoINEAtras.setVisibility(View.VISIBLE);
                }else {
                    tvFotoINEAtras.setVisibility(View.GONE);
                }
                break;
            case 18:
                if(arregloComponentes[18] && mostrarOcultar) {
                    tvFotoCasa.setVisibility(View.VISIBLE);
                }else {
                    tvFotoCasa.setVisibility(View.GONE);
                }
                break;
            case 19:
                if(arregloComponentes[19] && mostrarOcultar) {
                    tvFotoComprobanteDomicilio.setVisibility(View.VISIBLE);
                }else {
                    tvFotoComprobanteDomicilio.setVisibility(View.GONE);
                }
                break;
            case 20:
                if(arregloComponentes[20] && mostrarOcultar) {
                    tvEdadNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvEdadNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 21:
                if(arregloComponentes[21] && mostrarOcultar) {
                    tvDiagnosticoNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvDiagnosticoNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 22:
                if(arregloComponentes[22] && mostrarOcultar) {
                    tvOcupacionNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOcupacionNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 23:
                if(arregloComponentes[23] && mostrarOcultar) {
                    tvDiabetesNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvDiabetesNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 24:
                if(arregloComponentes[24] && mostrarOcultar) {
                    tvHipertensionNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvHipertensionNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 25:
                if(arregloComponentes[25] && mostrarOcultar) {
                    tvOtroMoletiaNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOtroMoletiaNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 26:
                if(arregloComponentes[26] && mostrarOcultar) {
                    tvProductoNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvProductoNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 27:
                if(arregloComponentes[27] && mostrarOcultar) {
                    tvPaquetesNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvPaquetesNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 28:
                if(arregloComponentes[28] && mostrarOcultar) {
                    tvFechaEntregaNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvFechaEntregaNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 29:
                if(arregloComponentes[29] && mostrarOcultar) {
                    tvOjoDerechoEsfericoNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOjoDerechoEsfericoNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 30:
                if(arregloComponentes[30] && mostrarOcultar) {
                    tvOjoDerechoCilindroNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOjoDerechoCilindroNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 31:
                if(arregloComponentes[31] && mostrarOcultar) {
                    tvOjoDerechoEjeNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOjoDerechoEjeNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 32:
                if(arregloComponentes[32] && mostrarOcultar) {
                    tvOjoDerechoAddNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOjoDerechoAddNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 33:
                if(arregloComponentes[33] && mostrarOcultar) {
                    tvOjoDerechoALTNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOjoDerechoALTNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 34:
                if(arregloComponentes[34] && mostrarOcultar) {
                    tvOjoIzquierdoEsfericoNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOjoIzquierdoEsfericoNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 35:
                if(arregloComponentes[35] && mostrarOcultar) {
                    tvOjoIzquierdoCilindroNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOjoIzquierdoCilindroNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 36:
                if(arregloComponentes[36] && mostrarOcultar) {
                    tvOjoIzquierdoEjeNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOjoIzquierdoEjeNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 37:
                if(arregloComponentes[37] && mostrarOcultar) {
                    tvOjoIzquierdoAddNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOjoIzquierdoAddNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 38:
                if(arregloComponentes[38] && mostrarOcultar) {
                    tvOjoIzquierdoALTNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOjoIzquierdoALTNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 39:
                if(arregloComponentes[39] && mostrarOcultar) {
                    tvOtroMaterialNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOtroMaterialNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 40:
                if(arregloComponentes[40] && mostrarOcultar) {
                    tvPrecioMaterialNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvPrecioMaterialNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 41:
                if(arregloComponentes[41] && mostrarOcultar) {
                    tvOtroTratamientoNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOtroTratamientoNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 42:
                if(arregloComponentes[42] && mostrarOcultar) {
                    tvPrecioTratamientoNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvPrecioTratamientoNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 43:
                if(arregloComponentes[43] && mostrarOcultar) {
                    tvTipoBifocalNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvTipoBifocalNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 44:
                if(arregloComponentes[44] && mostrarOcultar) {
                    tvCorreoCliente.setVisibility(View.VISIBLE);
                }else {
                    tvCorreoCliente.setVisibility(View.GONE);
                }
                break;
            case 45:
                //No se hace nada por que se quito la foto de pagare
                if(arregloComponentes[45] && mostrarOcultar) {
                    tvFotoPagare.setVisibility(View.VISIBLE);
                }else {
                    tvFotoPagare.setVisibility(View.GONE);
                }
                break;
            case 46:
                if(arregloComponentes[46] && mostrarOcultar) {
                    tvGeneralSinConversionNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvGeneralSinConversionNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 47:
                if(arregloComponentes[47] && mostrarOcultar) {
                    tvEmbarazadaNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvEmbarazadaNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 48:
                if(arregloComponentes[48] && mostrarOcultar) {
                    tvDurmioSeisOchoHorasNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvDurmioSeisOchoHorasNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 49:
                if(arregloComponentes[49] && mostrarOcultar) {
                    tvActividadDiaNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvActividadDiaNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 50:
                if(arregloComponentes[50] && mostrarOcultar) {
                    tvProblemasOjosNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvProblemasOjosNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 51:
                if(arregloComponentes[51] && mostrarOcultar) {
                    tvOtroBifocalNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvOtroBifocalNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 52:
                if(arregloComponentes[52] && mostrarOcultar) {
                    tvPrecioBifocalNuevoHistorial.setVisibility(View.VISIBLE);
                }else {
                    tvPrecioBifocalNuevoHistorial.setVisibility(View.GONE);
                }
                break;
            case 53:
                if(arregloComponentes[53] && mostrarOcultar) {
                    tvCalleClienteEntrega.setVisibility(View.VISIBLE);
                }else {
                    tvCalleClienteEntrega.setVisibility(View.GONE);
                }
                break;
            case 54:
                if(arregloComponentes[54] && mostrarOcultar) {
                    tvNumeroClienteEntrega.setVisibility(View.VISIBLE);
                }else {
                    tvNumeroClienteEntrega.setVisibility(View.GONE);
                }
                break;
            case 55:
                if(arregloComponentes[55] && mostrarOcultar) {
                    tvDepartamentoClienteEntrega.setVisibility(View.VISIBLE);
                }else {
                    tvDepartamentoClienteEntrega.setVisibility(View.GONE);
                }
                break;
            case 56:
                if(arregloComponentes[56] && mostrarOcultar) {
                    tvAlLadoDeClienteEntrega.setVisibility(View.VISIBLE);
                }else {
                    tvAlLadoDeClienteEntrega.setVisibility(View.GONE);
                }
                break;
            case 57:
                if(arregloComponentes[57] && mostrarOcultar) {
                    tvFrenteAClienteEntrega.setVisibility(View.VISIBLE);
                }else {
                    tvFrenteAClienteEntrega.setVisibility(View.GONE);
                }
                break;
            case 58:
                if(arregloComponentes[58] && mostrarOcultar) {
                    tvEntreCallesClienteEntrega.setVisibility(View.VISIBLE);
                }else {
                    tvEntreCallesClienteEntrega.setVisibility(View.GONE);
                }
                break;
            case 59:
                if(arregloComponentes[59] && mostrarOcultar) {
                    tvColoniaClienteEntrega.setVisibility(View.VISIBLE);
                }else {
                    tvColoniaClienteEntrega.setVisibility(View.GONE);
                }
                break;
            case 60:
                if(arregloComponentes[60] && mostrarOcultar) {
                    tvLocalidadClienteEntrega.setVisibility(View.VISIBLE);
                }else {
                    tvLocalidadClienteEntrega.setVisibility(View.GONE);
                }
                break;
            case 61:
                if(arregloComponentes[61] && mostrarOcultar) {
                    tvTipoCasaClienteEntrega.setVisibility(View.VISIBLE);
                }else {
                    tvTipoCasaClienteEntrega.setVisibility(View.GONE);
                }
                break;
            case 62:
                if(arregloComponentes[62] && mostrarOcultar) {
                    tvCasaColorClienteEntrega.setVisibility(View.VISIBLE);
                }else {
                    tvCasaColorClienteEntrega.setVisibility(View.GONE);
                }
                break;
            case 63:
                if(arregloComponentes[63] && mostrarOcultar) {
                    tvAliasCliente.setVisibility(View.VISIBLE);
                }else {
                    tvAliasCliente.setVisibility(View.GONE);
                }
                break;
            case 64:
                if(arregloComponentes[64] && mostrarOcultar) {
                    tvOtraFoto.setVisibility(View.VISIBLE);
                }else {
                    tvOtraFoto.setVisibility(View.GONE);
                }
                break;
            case 65:
                if(arregloComponentes[65] && mostrarOcultar){
                    tvColorTratamiento.setVisibility(View.VISIBLE);
                }else{
                    tvColorTratamiento.setVisibility(View.GONE);
                }
                break;
            case 66:
                if(arregloComponentes[66] && mostrarOcultar){
                    tvEstiloTratamiento.setVisibility(View.VISIBLE);
                }else{
                    tvEstiloTratamiento.setVisibility(View.GONE);
                }
                break;
            case 67:
                if(arregloComponentes[67] && mostrarOcultar){
                    tvColorTratamientoPolarizado.setVisibility(View.VISIBLE);
                }else{
                    tvColorTratamientoPolarizado.setVisibility(View.GONE);
                }
                break;
            case 68:
                if(arregloComponentes[68] && mostrarOcultar){
                    tvColorTratamientoEspejeado.setVisibility(View.VISIBLE);
                }else{
                    tvColorTratamientoEspejeado.setVisibility(View.GONE);
                }
                break;
            case 69:
                if(arregloComponentes[69] && mostrarOcultar){
                    tvLugarEntrega.setVisibility(View.VISIBLE);
                }else{
                    tvLugarEntrega.setVisibility(View.GONE);
                }
                break;
            case 70:
                if(arregloComponentes[70] && mostrarOcultar){
                    tvFotoArmazonPropio.setVisibility(View.VISIBLE);
                }else{
                    tvFotoArmazonPropio.setVisibility(View.GONE);
                }
                break;
        }

    }

    /*Metodo/Funcion: enviarDatosGuardar
     Descripcion: Mostrar alerta de confirmacion para crear el contrato, obtener idContrato, guardar imagenes tomadas en directorio y obtener nombre de las imagenes
   */
    private void enviarDatosGuardar() {

        if(contratoActualizar){
            //Actualizar contrato

            String idContrato = idContratoHijo;
            String fotoinefrentepadre = global.obtenerAtributoContrato(idContrato, "FOTOINEFRENTE");
            String fotoineatraspadre = global.obtenerAtributoContrato(idContrato, "FOTOINEATRAS");
            String fotocasapadre = global.obtenerAtributoContrato(idContrato, "FOTOCASA");
            String fotocomprobantedomiciliopadre = global.obtenerAtributoContrato(idContrato, "COMPROBANTEDOMICILIO");
            String fotopagarepadre = global.obtenerAtributoContrato(idContrato, "PAGARE");
            String fotootrospadre = global.obtenerAtributoContrato(idContrato, "FOTOOTROS");
            guardarContratoBD(idContrato, fotoinefrentepadre, fotoineatraspadre, fotocasapadre, fotocomprobantedomiciliopadre, fotopagarepadre, fotootrospadre);

        }else {
            //Crear contrato
            if (contadorVerificarDatos == 0) {
                svNuevoContratoPrincipal.post(new Runnable() {
                    @Override
                    public void run() {
                        svNuevoContratoPrincipal.scrollTo(0, spZona.getTop());
                    }
                });
                Toast.makeText(fragmento.getContext(), "Porfavor verifica que la infomación este correcta", Toast.LENGTH_LONG).show();
                contadorVerificarDatos++;
                if (llaves.scolllento) {
                    svNuevoContratoPrincipal.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                }
            } else {

                if (svNuevoContratoPrincipal.getChildAt(0).getBottom() <= (svNuevoContratoPrincipal.getHeight() + svNuevoContratoPrincipal.getScrollY())) {

                    AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                    alerta.setTitle("Confirmación").setMessage(Html.fromHtml("¿Crear contrato?<br><br>" +
                                    "<font color='#FFACA6'><b>(Al dar clic en \"ACEPTAR\" aceptas que verificaste que el cliente no ha tenido lios/fugas)</b></font>"))
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mostrarAlertDialogCambioPaqueteDorado1APlatinoOViseversa();
                                    generarImagenContratoPorCrear();
                                }
                            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();

                }
            }
        }

    }

    private void mostrarAlertDialogCambioPaqueteDorado1APlatinoOViseversa() {

        String mensaje = "";
        boolean mostrarAlerta = false;
        confirmacionAlertaCambioPaqueteDorado1PlatinooViserversa = false;

        //Se obtiene el paquete seleccionado
        idPaqueteSeleccionado = idsPaquetes[spPaquetesNuevoHistorial.getSelectedItemPosition()];

        String mensajeCambioPaquetesMovimiento = "";

        //Cambio de paquete de Dorado 1 a Platino
        if(idPaqueteSeleccionado.equals("5") && (Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial.getText().toString()) > 3.25
                || Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial.getText().toString()) > 3.25
                || Double.parseDouble(etOjoDerechoAddNuevoHistorial.getText().toString()) > 3.25
                || Double.parseDouble(etOjoIzquierdoAddNuevoHistorial.getText().toString()) > 3.25
                || Double.parseDouble(etOjoDerechoCilindroNuevoHistorial.getText().toString()) != 0
                || Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial.getText().toString()) != 0)) {
            mensaje = "De acuerdo al historial se recomienda actualizar el paquete de Dorado 1 a Platino";
            mensajeCambioPaquetesMovimiento = "Dorado 1 a Platino";
            mostrarAlerta = true;
        }

        //Cambio de paquete de Platino a Dorado 1
        if(idPaqueteSeleccionado.equals("7") && (Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial.getText().toString()) <= 3.25
                && Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial.getText().toString()) <= 3.25
                && Double.parseDouble(etOjoDerechoAddNuevoHistorial.getText().toString()) <= 3.25
                && Double.parseDouble(etOjoIzquierdoAddNuevoHistorial.getText().toString()) <= 3.25
                && Double.parseDouble(etOjoDerechoCilindroNuevoHistorial.getText().toString()) == 0
                && Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial.getText().toString()) == 0)) {
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
                            btnGuardarContratoNuevo.setEnabled(false);
                            btnCancelarContratoNuevo.setEnabled(false);
                            confirmacionAlertaCambioPaqueteDorado1PlatinooViserversa = true;
                            validacionParaGuardarContrato(true,
                                    "La sugerencia de cambio de paquete de " + finalMensajeCambioPaquetesMovimiento + " fue aceptada");
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btnGuardarContratoNuevo.setEnabled(false);
                            btnCancelarContratoNuevo.setEnabled(false);
                            validacionParaGuardarContrato(true,
                                    "La sugerencia de cambio de paquete de " + finalMensajeCambioPaquetesMovimiento + " fue rechazada");
                        }
                    }).show();

        }else {
            //No se hara ningun cambio de paquete (Dorado 1 a Platino/Platino a Dorado 1)
            btnGuardarContratoNuevo.setEnabled(false);
            btnCancelarContratoNuevo.setEnabled(false);
            validacionParaGuardarContrato(false, "");
        }
    }

    private void mostrarAlertaGuardarUbicacion() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Ubicacion").setMessage(Html.fromHtml("¿Deseas guardar la ubicacion?<br><br>" +
                "<font color='#FFACA6'><b>Es recomendable que estes en el domicilio del cliente o donde se realizara el cobro de los abonos</b></font>"))
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Debe ir vacio por que se esta obteniendo abajo
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                coordenadasContrato = "";
                dialog.cancel();
            }
        });

        final AlertDialog dialog = alerta.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Actualizar coordenadas a contrato
                if(localizacion.actualizarUbicacion(localizacion)) {
                    //Gps esta encendio y tiene permisos
                    Handler handler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if(localizacion.getLatitud() != null){
                                coordenadasContrato = localizacion.getLatitud()+","+localizacion.getLongitud();
                                //Toast.makeText(fragmento.getActivity(),"Latitud: "+localizacion.getLatitud()+ "Longitud: "+localizacion.getLongitud(),Toast.LENGTH_LONG).show();
                                handler.removeCallbacks(runnable);
                            }else {
                                handler.postDelayed(runnable,2000);
                            }
                        }
                    };
                    runnable.run();
                    dialog.dismiss();
                }
            }
        });

    }

    private void validacionParaGuardarContrato(boolean guardarmovimientocambiopaquete, String mensajemovimiento) {

        time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        fechaActual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");

        String idContrato = obtenerIdContrato();

        if (guardarmovimientocambiopaquete) {
            //Se guardara el movimiento de cambio de paquete
            historialMovimientosContrato.guardarHistorialMovimientosContrato(idContrato, mensajemovimiento, "0");
        }

        if (global.obtenerAtributoContrato(idContratoHijo, "ESTATUS_ESTADOCONTRATO").equals("13")) {
            borrarFotosYEliminarHistorialContratoPorCrear();
            idContrato = idContratoHijo;
        }

        if (cbDuplicarDocumentos.isChecked()) {
            Log.i("MENSAJE", "Se crea con las mismas fotos");
            String fotoinefrentepadre = global.obtenerAtributoContrato(idContratoPadre, "FOTOINEFRENTE");
            String fotoineatraspadre = global.obtenerAtributoContrato(idContratoPadre, "FOTOINEATRAS");
            String fotocasapadre = global.obtenerAtributoContrato(idContratoPadre, "FOTOCASA");
            String fotocomprobantedomiciliopadre = global.obtenerAtributoContrato(idContratoPadre, "COMPROBANTEDOMICILIO");
            String fotopagarepadre = global.obtenerAtributoContrato(idContratoPadre, "PAGARE");
            String fotootrospadre = global.obtenerAtributoContrato(idContratoPadre, "FOTOOTROS");
            guardarContratoBD(idContrato, fotoinefrentepadre, fotoineatraspadre, fotocasapadre, fotocomprobantedomiciliopadre, fotopagarepadre, fotootrospadre);
            guardarEnTablaImagenesCargadasContratos(idContrato, "2", fotootrospadre);
        } else {

            Log.i("MENSAJE", "Se crea con diferentes fotos");

            String fotoIneFrenteRuta = "";
            String fotoIneAtrasRuta = "";
            String fotoCasaRuta = "";
            String fotoComprobanteDomicilioRuta = "";
            String fotoPagareRuta = "";
            String fotoOtrosRuta = "";

            if(global.obtenerAtributoTabla("IMAGENESCARGADASCONTRATOS","ID_CONTRATO","ID_CONTRATO",idContrato).equals("")){
                //No existe registro en la tabla de imagenescargadascontrato - Insertar registro
                guardarEnTablaImagenesCargadasContratos(idContrato, "2", fotoOtrosRuta);
            }

            if(ivFotoINEFrente.getDrawable() != null){
                fotoIneFrenteRuta = camara.guardarImagenGaleria(ivFotoINEFrente, "Foto-Ine-Frente-Contrato-" + idContrato + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOINE", "0", "ID_CONTRATO", idContrato);
            }

            if(ivFotoINEAtras.getDrawable() != null){
                fotoIneAtrasRuta = camara.guardarImagenGaleria(ivFotoINEAtras, "Foto-Ine-Atras-Contrato-" + idContrato + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOINEATRAS", "0", "ID_CONTRATO", idContrato);
            }

            if(ivFotoCasa.getDrawable() != null){
                fotoCasaRuta = camara.guardarImagenGaleria(ivFotoCasa, "Foto-Casa-Contrato-" + idContrato + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOCASA", "0", "ID_CONTRATO", idContrato);
            }

            if(ivFotoComprobanteDomicilio.getDrawable() != null){
                fotoComprobanteDomicilioRuta = camara.guardarImagenGaleria(ivFotoComprobanteDomicilio, "Foto-Comprobante-Domicilio-Contrato-" + idContrato + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "COMPROBANTEDOMICILIO", "0", "ID_CONTRATO", idContrato);
            }

            if(ivFotoPagare.getDrawable() != null){
                fotoPagareRuta = camara.guardarImagenGaleria(ivFotoPagare, "Foto-Pagare-Contrato-" + idContrato + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "PAGARE", "0", "ID_CONTRATO", idContrato);
            }

            if(ivFotoOtros.getDrawable() != null){
                fotoOtrosRuta = camara.guardarImagenGaleria(ivFotoOtros, "Foto-Otros-Contrato-" + idContrato + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOOTROS", "0", "ID_CONTRATO", idContrato);
            }

            guardarContratoBD(idContrato, fotoIneFrenteRuta, fotoIneAtrasRuta, fotoCasaRuta, fotoComprobanteDomicilioRuta, fotoPagareRuta, fotoOtrosRuta);

        }

    }

    private void borrarFotosYEliminarHistorialContratoPorCrear() {
        String idHistorialClinico = obtenerIdHistorialContratoPorCrear();
        eliminarHistorialClinico(idHistorialClinico);

        //Eliminar historialsinconversion
        global.eliminarTablaORegistroTabla("HISTORIALSINCONVERSION", "ID_CONTRATO = '" + idContratoHijo + "' AND ID_HISTORIAL", idHistorialClinico);

        String[] rutas = new String[] {rutaINEFrente, rutaINEAtras, rutaCasa, rutaComprobanteDomicilio, rutaPagare, rutaFotoOtros};

        for(int i = 0; i < rutas.length; i++) {
            if(rutas[i].length() > 0) {
                File foto = new File(file_path + "/luzatuvida/.nomedia/" + rutas[i]);
                if(foto.exists()) {
                    foto.delete();
                }
            }
        }

    }

    private void eliminarHistorialClinico(String idHistorialClinico) {

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            String SQL = "DELETE FROM HISTORIALCLINICO WHERE ID='" + idHistorialClinico + "'";
            sqLiteDB2.execSQL(SQL);
            sqLiteDB2.close();

        }catch (SQLiteException e){
            global.escribirError(e, 128);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

    }

    /*Metodo/Funcion: guardarContratoBD
     Descripcion: Obtener idContrato, nombre de imagenes, calculo de precio total del historial (precio paquete, precio otro material, etc) y actualizar la informacion en el contrato
   */
    private void guardarContratoBD(String idContrato, String fotoIneFrenteRuta, String fotoIneAtrasRuta, String fotoCasaRuta, String fotoComprobanteDomicilioRuta,
                                   String fotoPagareRuta, String fotoOtrosRuta) {

        //Se obtiene el paquete seleccionado
        idPaqueteSeleccionado = idsPaquetes[spPaquetesNuevoHistorial.getSelectedItemPosition()];

        if (confirmacionAlertaCambioPaqueteDorado1PlatinooViserversa) {
            //Se decidio cambiar el paquete (Dorado 1 a Platino/Platino a Dorado 1)

            //Cambio de paquete de Dorado 1 a Platino
            if (idPaqueteSeleccionado.equals("5") && (Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial.getText().toString()) > 3.25
                    || Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial.getText().toString()) > 3.25
                    || Double.parseDouble(etOjoDerechoAddNuevoHistorial.getText().toString()) > 3.25
                    || Double.parseDouble(etOjoIzquierdoAddNuevoHistorial.getText().toString()) > 3.25
                    || Double.parseDouble(etOjoDerechoCilindroNuevoHistorial.getText().toString()) != 0
                    || Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial.getText().toString()) != 0)) {
                idPaqueteSeleccionado = "7";
            }

            //Cambio de paquete de Platino a Dorado 1
            if (idPaqueteSeleccionado.equals("7") && (Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial.getText().toString()) <= 3.25
                    && Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial.getText().toString()) <= 3.25
                    && Double.parseDouble(etOjoDerechoAddNuevoHistorial.getText().toString()) <= 3.25
                    && Double.parseDouble(etOjoIzquierdoAddNuevoHistorial.getText().toString()) <= 3.25
                    && Double.parseDouble(etOjoDerechoCilindroNuevoHistorial.getText().toString()) == 0
                    && Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial.getText().toString()) == 0)) {
                idPaqueteSeleccionado = "5";
            }

        }

        double precioPaquete = obtenerPrecioPaqueteSeleccionado();

        if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 1) { //LECTURA
            if(Double.parseDouble(etOjoDerechoCilindroNuevoHistorial.getText().toString()) != 0 || Double.parseDouble(etOjoIzquierdoCilindroNuevoHistorial.getText().toString()) != 0
                    || Double.parseDouble(etOjoDerechoEsfericoNuevoHistorial.getText().toString()) > 5.25 || Double.parseDouble(etOjoIzquierdoEsfericoNuevoHistorial.getText().toString()) > 5.25) {
                precioPaquete = precioPaquete + 590;
            }
        }

        double precioPolicarbonato = 0;
        if(rbPolicarbonato.isChecked()){
            if (!cbPolicarbonatoTipo.isChecked()){
                //Policarbonato para adulto
                precioPolicarbonato = 300;
            }
        }
        double precioOtroMaterial = 0;
        if(rbOtroMaterial.isChecked()) {
             precioOtroMaterial = Double.parseDouble(etPrecioMaterialNuevoHistorial.getText().toString());
        }
        double precioOtroBifocal = 0;
        if(rbOtroBifocal.isChecked()) {
            precioOtroBifocal = Double.parseDouble(etPrecioBifocalNuevoHistorial.getText().toString());
        }
        double precioTotal = obtenerPrecioTotalHistorial(precioPaquete, precioPolicarbonato + precioOtroMaterial + precioOtroBifocal);
        decrementarProductoSeleccionadoBD();

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET DATOS='" + "1" + "', ID_USUARIOCREACION='" + obtenerRol.obtenerAtributoUsuarioLogeado("ID")
                    + "', NOMBRE_USUARIOCREACION='" + global.replaceCadena(obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO")) + "', ID_ZONA='" + idsZonas[spZona.getSelectedItemPosition()]
                    + "', NOMBRE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNombreCliente.getText().toString())) + "', CALLE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCalleCliente.getText().toString()))
                    + "', NUMERO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNumeroCliente.getText().toString())) + "', DEPTO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDepartamentoCliente.getText().toString()))
                    + "', ALLADODE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etAlLadoDeCliente.getText().toString())) + "', FRENTEA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etFrenteACliente.getText().toString()))
                    + "', ENTRECALLES='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etEntreCallesCliente.getText().toString())) + "', COLONIA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etColoniaCliente.getText().toString()))
                    + "', LOCALIDAD='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etLocalidadCliente.getText().toString())) + "', TELEFONO='" + etTelefonoCliente.getText().toString()
                    + "', TELEFONOREFERENCIA='" + etTelefonoReferenciaCliente.getText().toString() + "', NOMBREREFERENCIA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNombreReferenciaCliente.getText().toString()))
                    + "', CASATIPO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etTipoCasaCliente.getText().toString())) + "', CASACOLOR='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCasaColorCliente.getText().toString()))
                    + "', CORREO='" + etCorreoCliente.getText().toString().trim()
                    + "', FOTOINEFRENTE='" + fotoIneFrenteRuta + "', FOTOINEATRAS='" + fotoIneAtrasRuta
                    + "', FOTOCASA='" + fotoCasaRuta + "', COMPROBANTEDOMICILIO='" + fotoComprobanteDomicilioRuta
                    + "', ID_OPTOMETRISTA='" + idsOptometristas[spOptometrista.getSelectedItemPosition()]
                    + "', ID_PROMOCION='" + "" + "', TOTAL='" + precioTotal
                    + "', IDCONTRATORELACION='" + idContratoPadre + "', CONTADOR='" + "1"
                    + "', TOTALHISTORIAL='" + precioTotal + "', TOTALPROMOCION='" + "0"
                    + "', TOTALPRODUCTO='" + "0" + "', TOTALABONO='" + "0"
                    + "', ULTIMOABONO='" + ""
                    + "', ESTATUS_ESTADOCONTRATO='" + "0" + "', DIAPAGO='" + ""
                    + "', FECHACOBROINI='" + "" + "', FECHACOBROFIN='" + ""
                    + "', FECHAATRASO='" + "" + "', COSTOATRASO='" + "0"
                    + "', DIASELECCIONADO='" + "" + "', FECHAENTREGA='" + ""
                    + "', PAGOSADELANTAR='" + "0" + "', ENGANCHE='" + "0"
                    + "', ENTREGAPRODUCTO='" + "0" + "', ESTATUS='" + "0"
                    + "', ENVIADOPAGINA='" + "0"
                    + "', CONTRATOENVIADO='" + "0" + "', ALIAS='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etAliasCliente.getText().toString()))
                    + "', PAGARE='" + fotoPagareRuta + "', FOTOOTROS='" + fotoOtrosRuta + "', PROMOCIONTERMINADA='" + "0"
                    + "', SUBSCRIPCION='" + "" + "', FECHASUBSCRIPCION='" + ""
                    + "', TOTALREAL='" + precioTotal + "', DIATEMPORAL='" + ""
                    + "', COORDENADAS='" + coordenadasContrato + "', CALLEENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCalleClienteEntrega.getText().toString()))
                    + "', NUMEROENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNumeroClienteEntrega.getText().toString())) + "', DEPTOENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDepartamentoClienteEntrega.getText().toString()))
                    + "', ALLADODEENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etAlLadoDeClienteEntrega.getText().toString())) + "', FRENTEAENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etFrenteAClienteEntrega.getText().toString()))
                    + "', ENTRECALLESENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etEntreCallesClienteEntrega.getText().toString())) + "', COLONIAENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etColoniaClienteEntrega.getText().toString()))
                    + "', LOCALIDADENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etLocalidadClienteEntrega.getText().toString()))
                    + "', CASATIPOENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etTipoCasaClienteEntrega.getText().toString())) + "', CASACOLORENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCasaColorClienteEntrega.getText().toString()))
                    + "', OPCIONLUGARENTREGA='" + idsOpcionesLugarEntrega[spLugarEntrega.getSelectedItemPosition()]
                    + "', OBSERVACIONFOTOINE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoINEFrente.getText().toString())) + "', OBSERVACIONFOTOINEATRAS='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoINEAtras.getText().toString()))
                    + "', OBSERVACIONFOTOCASA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoCasa.getText().toString())) + "', OBSERVACIONCOMPROBANTEDOMICILIO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoComprobanteDomicilio.getText().toString()))
                    + "', OBSERVACIONPAGARE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoPagare.getText().toString())) + "', OBSERVACIONFOTOOTROS='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionOtraFoto.getText().toString()))
                    + "', CREATED_AT='" + fechaActual + " " + time + "', UPDATED_AT='" + fechaActual + " " + time
                    + "' WHERE ID_CONTRATO='" + idContrato + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

            if(!idContratoPadre.equals("")) {
                //Es contrato hijo
                actualizarContadorContrato();
            }

            guardarHistorialClinicoBD(idContrato, false);

        }catch (SQLiteException e) {
            global.escribirError(e, 129);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void guardarEnTablaImagenesCargadasContratos(String idContrato, String valor, String fotootros) {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID_CONTRATO", idContrato);
            valores.put("FOTOINE", valor);
            valores.put("FOTOINEATRAS", valor);
            valores.put("FOTOCASA", valor);
            valores.put("COMPROBANTEDOMICILIO", valor);
            valores.put("PAGARE", valor);
            valores.put("TARJETAPENSION", valor);
            valores.put("TARJETAPENSIONATRAS", valor);
            valores.put("FOTOOTROS", valor);
            valores.put("FOTOMOVIMIENTO", "2");
            valores.put("FOTOARMAZON1", "2");
            valores.put("FOTOARMAZON2", "2");
            sqLiteDB2.insert("IMAGENESCARGADASCONTRATOS",null, valores);
            sqLiteDB2.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 130);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: actualizarContadorContrato
     Descripcion: Obtener atributo CONTADOR del contrato padre, sumarle uno y actualizar el atributo
   */
    private void actualizarContadorContrato() {

        int contador = obtenerContadorContrato() + 1;

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET ENVIADOPAGINA = '0', CONTADOR = '" + contador
                    + "' WHERE ID_CONTRATO='" + idContratoPadre + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 131);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: obtenerContadorContrato
     Descripcion: Obtener atributo CONTADOR del contrato padre
   */
    private int obtenerContadorContrato() {

        int contador = 0;

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT CONTADOR FROM CONTRATOS" +
                    " WHERE ID_CONTRATO='" + idContratoPadre + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el contrato padre");
            }

            if (datos.moveToFirst()){
                if(Integer.parseInt(datos.getString(0)) > 0){
                    contador += Integer.parseInt(datos.getString(0));
                }
            }

            sqLiteDB.close();
            datos.close();
        }catch (SQLiteException e){
            global.escribirError(e, 132);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return contador;

    }

    /*Metodo/Funcion: obtenerPrecioPaqueteSeleccionado
     Descripcion: Obtener precio del paquete seleccionado en el formulario
   */
    private double obtenerPrecioPaqueteSeleccionado() {

        double precioPaquete = 0;

        try {

            sqLiteDB = conexion.getReadableDatabase();

            String consulta = "SELECT PRECIO FROM PAQUETES WHERE ID='" + idPaqueteSeleccionado + "'";
            Cursor datos = sqLiteDB.rawQuery(consulta, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No se pudo encontrar el precio del paquete");
            }

            if (datos.moveToFirst()) {
                precioPaquete = Double.parseDouble(datos.getString(0));
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 133);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return precioPaquete;

    }

    /*Metodo/Funcion: obtenerPrecioTotalHistorial
     Descripcion: Obtener precio total del historial
   */
    private double obtenerPrecioTotalHistorial(double precioPaquete, double precioOtroMaterial) {

        double precioTotal = 0;

        try {

            sqLiteDB = conexion.getReadableDatabase();

            String consulta = "SELECT PRECIO FROM TRATAMIENTOS";
            Cursor datos = sqLiteDB.rawQuery(consulta, null);

            double totalPrecioTratamientos = 0;
            String[] preciosTratamientos = new String[datos.getCount()];

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay tratamientos");
            }

            if(datos.getCount()>0){

                for(int i=0; i < datos.getCount(); i++){

                    preciosTratamientos[i] = datos.getString(0);
                    datos.moveToNext();

                }

            }

            sqLiteDB.close();
            datos.close();

            if(cbTratamientoFotocromatico.isChecked() || cbTratamientoAR.isChecked() || cbTratamientoTinte.isChecked()
                    || cbTratamientoBlueray.isChecked() || cbTratamientoOtro.isChecked() || cbTratamientoEspejeado.isChecked() || cbTratamientoPolarizado.isChecked()) {
                //Si checkBox de tratamientos estan chequeados

                if(idPaqueteSeleccionado.equals("7")
                        && (Double.parseDouble(etOjoDerechoAddNuevoHistorial.getText().toString()) > 3.25
                        || Double.parseDouble(etOjoIzquierdoAddNuevoHistorial.getText().toString()) > 3.25)) {
                    //Si paquete es igual a PLATINO y add derecho o izquierdo tienen mas de +3.25

                    totalPrecioTratamientos = 0;

                }else {
                    //Si paquete es diferente o igual a platino y add derecho o izquierdo tienen menos o igual a +3.25

                    if (cbTratamientoFotocromatico.isChecked()) {
                        //Si cbTratamientoFotocromatico esta chequeado
                        if (spPaquetesNuevoHistorial.getSelectedItemPosition() == 2) {
                            //PAQUETE PROTECCION
                            totalPrecioTratamientos += 0;
                        } else {
                            //PAQUETE DIFERENTE A PROTECCION
                            totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[0]);
                        }
                    }

                    if (cbTratamientoAR.isChecked()) {
                        //Si cbTratamientoAR esta chequeado
                        totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[1]);
                    }

                    if (cbTratamientoTinte.isChecked()) {
                        //Si cbTratamientoTinte esta chequeado
                        totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[2]);
                    }

                    if (cbTratamientoBlueray.isChecked()) {
                        //Si cbTratamientoBlueray esta chequeado
                        if (spPaquetesNuevoHistorial.getSelectedItemPosition() == 2) {
                            //PAQUETE PROTECCION
                            if (cbTratamientoFotocromatico.isChecked()) {
                                //Tratamiento fotocromatico esta chequeado tambien
                                totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[3]);
                            } else {
                                totalPrecioTratamientos += 0;
                            }
                        } else {
                            //PAQUETE DIFERENTE A PROTECCION
                            totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[3]);
                        }
                    }

                    if (cbTratamientoPolarizado.isChecked()) {
                        //Si cbTratamientoPolarizado esta chequeado
                        totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[4]);
                    }

                    if (cbTratamientoEspejeado.isChecked()) {
                        //Si cbTratamientoEspejeado esta chequeado
                        totalPrecioTratamientos += Double.parseDouble(preciosTratamientos[5]);
                    }

                    if (cbTratamientoOtro.isChecked()) {
                        //Si cbTratamientoOtro esta chequeado
                        totalPrecioTratamientos += Double.parseDouble(etPrecioTratamientoNuevoHistorial.getText().toString());
                    }

                }

            }

            precioTotal = totalPrecioTratamientos + precioPaquete + precioOtroMaterial;
            return precioTotal;

        }catch (SQLiteException e) {
            global.escribirError(e, 134);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return precioTotal;

    }

    /*Metodo/Funcion: decrementarProductoSeleccionadoBD
     Descripcion: Decrementar una pieza al producto que fue seleccionado
   */
    private void decrementarProductoSeleccionadoBD() {

        try {

            sqLiteDB = conexion.getWritableDatabase();
            String consulta = "UPDATE PRODUCTO SET PIEZAS = CAST(PIEZAS AS INTEGER) - 1 WHERE ID='" + idsProductos[spProductoNuevoHistorial.getSelectedItemPosition()] + "'";
            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 135);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: guardarHistorialClinicoBD
     Descripcion: Obtener idAlfanumerico para tabla HISTORIALCLINICO y guardar historial clinico
   */
    private void guardarHistorialClinicoBD(String idContrato, boolean esContratoPorCrear) {

        //Se obtiene el paquete seleccionado
        if(idPaqueteSeleccionado.equals("")) {
            idPaqueteSeleccionado = idsPaquetes[spPaquetesNuevoHistorial.getSelectedItemPosition()];
        }

        //Obtener idAlfanumerico
        String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("HISTORIALCLINICO", 5);

        //Validar armazon seleccionado
        String fotoArmazonPropio = "";
        if(esArmazonPropio){
            //Armazon propio - Guardar imagen
            if(ivFotoArmazonPropio.getDrawable() != null){
                fotoArmazonPropio = camara.guardarImagenGaleria(ivFotoArmazonPropio, "Foto-Armazon-Propio-" + idContrato + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOARMAZON1", "0", "ID_CONTRATO", idContrato);
            }
        }

        //Guardar historial clinico en BD
        try{

            String ojoDerechoEsferico = etOjoDerechoEsfericoNuevoHistorial.getText().toString();
            String ojoDerechoCilindro = etOjoDerechoCilindroNuevoHistorial.getText().toString();
            String ojoDerechoEje = etOjoDerechoEjeNuevoHistorial.getText().toString();
            String ojoDerechoAdd = etOjoDerechoAddNuevoHistorial.getText().toString();
            String ojoDerechoAlt = etOjoDerechoALTNuevoHistorial.getText().toString();

            String ojoIzquierdoEsferico = etOjoIzquierdoEsfericoNuevoHistorial.getText().toString();
            String ojoIzquierdoCilindro = etOjoIzquierdoCilindroNuevoHistorial.getText().toString();
            String ojoIzquierdoEje = etOjoIzquierdoEjeNuevoHistorial.getText().toString();
            String ojoIzquierdoAdd = etOjoIzquierdoAddNuevoHistorial.getText().toString();
            String ojoIzquierdoAlt = etOjoIzquierdoALTNuevoHistorial.getText().toString();

            //OJO DERECHO
            //ojoDerechoEsferico
            if(ojoDerechoEsferico.startsWith(".")) {
                ojoDerechoEsferico = "0" + ojoDerechoEsferico;
            }

            if(ojoDerechoEsferico.endsWith(".")) {
                ojoDerechoEsferico = ojoDerechoEsferico + "0";
            }

            if (ojoDerechoEsferico.length() > 0) {
                if(ojoDerechoEsferico.equals("+") || ojoDerechoEsferico.equals("-")) {
                    ojoDerechoEsferico = "";
                }else {
                    if (Double.parseDouble(ojoDerechoEsferico) > 0) {
                        ojoDerechoEsferico = ojoDerechoEsferico.replace("+", "");
                        ojoDerechoEsferico = "+" + ojoDerechoEsferico;
                    }
                }
            }

            //ojoDerechoCilindro
            if(ojoDerechoCilindro.startsWith(".")) {
                ojoDerechoCilindro = "0" + ojoDerechoCilindro;
            }

            if(ojoDerechoCilindro.endsWith(".")) {
                ojoDerechoCilindro = ojoDerechoCilindro + "0";
            }

            if (ojoDerechoCilindro.length() > 0) {
                if(ojoDerechoCilindro.equals("+") || ojoDerechoCilindro.equals("-")) {
                    ojoDerechoCilindro = "";
                }else {
                    if (Double.parseDouble(ojoDerechoCilindro) > 0) {
                        ojoDerechoCilindro = ojoDerechoCilindro.replace("+", "");
                        ojoDerechoCilindro = "-" + ojoDerechoCilindro;
                    }
                }
            }

            //ojoDerechoEje
            if(ojoDerechoEje.startsWith(".")) {
                ojoDerechoEje = "0" + ojoDerechoEje;
            }

            if(ojoDerechoEje.endsWith(".")) {
                ojoDerechoEje = ojoDerechoEje + "0";
            }

            //ojoDerechoAdd
            if(ojoDerechoAdd.startsWith(".")) {
                ojoDerechoAdd = "0" + ojoDerechoAdd;
            }

            if(ojoDerechoAdd.endsWith(".")) {
                ojoDerechoAdd = ojoDerechoAdd + "0";
            }

            if (ojoDerechoAdd.length() > 0) {
                if(ojoDerechoAdd.equals("+") || ojoDerechoAdd.equals("-")) {
                    ojoDerechoAdd = "";
                }else {
                    if (Double.parseDouble(ojoDerechoAdd) > 0) {
                        ojoDerechoAdd = ojoDerechoAdd.replace("+", "");
                        ojoDerechoAdd = "+" + ojoDerechoAdd;
                    }
                }
            }

            //ojoDerechoAlt
            if(ojoDerechoAlt.startsWith(".")) {
                ojoDerechoAlt = "0" + ojoDerechoAlt;
            }

            if(ojoDerechoAlt.endsWith(".")) {
                ojoDerechoAlt = ojoDerechoAlt + "0";
            }

            //OJO IZQUIERDO
            //ojoIzquierdoEsferico
            if(ojoIzquierdoEsferico.startsWith(".")) {
                ojoIzquierdoEsferico = "0" + ojoIzquierdoEsferico;
            }

            if(ojoIzquierdoEsferico.endsWith(".")) {
                ojoIzquierdoEsferico = ojoIzquierdoEsferico + "0";
            }

            if (ojoIzquierdoEsferico.length() > 0) {
                if(ojoIzquierdoEsferico.equals("+") || ojoIzquierdoEsferico.equals("-")) {
                    ojoIzquierdoEsferico = "";
                }else {
                    if (Double.parseDouble(ojoIzquierdoEsferico) > 0) {
                        ojoIzquierdoEsferico = ojoIzquierdoEsferico.replace("+", "");
                        ojoIzquierdoEsferico = "+" + ojoIzquierdoEsferico;
                    }
                }
            }

            //ojoIzquierdoCilindro
            if(ojoIzquierdoCilindro.startsWith(".")) {
                ojoIzquierdoCilindro = "0" + ojoIzquierdoCilindro;
            }

            if(ojoIzquierdoCilindro.endsWith(".")) {
                ojoIzquierdoCilindro = ojoIzquierdoCilindro + "0";
            }

            if (ojoIzquierdoCilindro.length() > 0) {
                if(ojoIzquierdoCilindro.equals("+") || ojoIzquierdoCilindro.equals("-")) {
                    ojoIzquierdoCilindro = "";
                }else {
                    if (Double.parseDouble(ojoIzquierdoCilindro) > 0) {
                        ojoIzquierdoCilindro = ojoIzquierdoCilindro.replace("+", "");
                        ojoIzquierdoCilindro = "-" + ojoIzquierdoCilindro;
                    }
                }
            }

            //ojoIzquierdoEje
            if(ojoIzquierdoEje.startsWith(".")) {
                ojoIzquierdoEje = "0" + ojoIzquierdoEje;
            }

            if(ojoIzquierdoEje.endsWith(".")) {
                ojoIzquierdoEje = ojoIzquierdoEje + "0";
            }

            //ojoIzquierdoAdd
            if(ojoIzquierdoAdd.startsWith(".")) {
                ojoIzquierdoAdd = "0" + ojoIzquierdoAdd;
            }

            if(ojoIzquierdoAdd.endsWith(".")) {
                ojoIzquierdoAdd = ojoIzquierdoAdd + "0";
            }

            if (ojoIzquierdoAdd.length() > 0) {
                if(ojoIzquierdoAdd.equals("+") || ojoIzquierdoAdd.equals("-")) {
                    ojoIzquierdoAdd = "";
                }else {
                    if (Double.parseDouble(ojoIzquierdoAdd) > 0) {
                        ojoIzquierdoAdd = ojoIzquierdoAdd.replace("+", "");
                        ojoIzquierdoAdd = "+" + ojoIzquierdoAdd;
                    }
                }
            }

            //ojoIzquierdoAlt
            if(ojoIzquierdoAlt.startsWith(".")) {
                ojoIzquierdoAlt = "0" + ojoIzquierdoAlt;
            }

            if(ojoIzquierdoAlt.endsWith(".")) {
                ojoIzquierdoAlt = ojoIzquierdoAlt + "0";
            }

            //Validacion de tratamientos
            String valorCbTratamientoFotocromatico = cbTratamientoFotocromatico.isChecked() ? "1" : "0";
            String valorCbTratamientoAR = cbTratamientoAR.isChecked() ? "1" : "0";
            String valorCbTratamientoTinte = cbTratamientoTinte.isChecked() ? "1" : "0";
            String valorColorTratamientoTinte = cbTratamientoTinte.isChecked() ? idsColorTratamientoTinte[spColorTratamiento.getSelectedItemPosition()] : "";
            String valorEstiloTratamientoTinte = cbTratamientoTinte.isChecked() ? idsEstiloTratamientoTinte[spEstiloTratamiento.getSelectedItemPosition()] : "";
            String valorCbTratamientoBlueRay = cbTratamientoBlueray.isChecked() ? "1" : "0";
            String valorCbTratamientoOtro = cbTratamientoOtro.isChecked() ? "1" : "0";
            String valorTratamientoOtro = global.replaceCadena(etOtroTratamientoNuevoHistorial.getText().toString());
            String valorPrecioTratamientoOtro = etPrecioTratamientoNuevoHistorial.getText().toString();
            String valorCbTratamientoPolarizado = cbTratamientoPolarizado.isChecked() ? "1" : "0";
            String valorColorTratamientoPolarizado = cbTratamientoPolarizado.isChecked() ? idsColorTratamientoPolarizado[spColorTratamientoPolarizado.getSelectedItemPosition()] : "";
            String valorCbTratamientoEspejo = cbTratamientoEspejeado.isChecked() ? "1" : "0";
            String valorColorTratamientoEspejo = cbTratamientoEspejeado.isChecked() ? idsColorTratamientoEspejo[spColorTratamientoEspejeado.getSelectedItemPosition()] : "";

            if(!idPaqueteSeleccionado.equals("") && !etOjoDerechoAddNuevoHistorial.getText().toString().equals("")
                    && !etOjoIzquierdoAddNuevoHistorial.getText().toString().equals("") && !etOjoDerechoAddNuevoHistorial.getText().toString().equals("+")
                    && !etOjoIzquierdoAddNuevoHistorial.getText().toString().equals("-") && !etOjoDerechoAddNuevoHistorial.getText().toString().equals("-")
                    && !etOjoIzquierdoAddNuevoHistorial.getText().toString().equals("+")) {

                if (idPaqueteSeleccionado.equals("7")
                        && (Double.parseDouble(etOjoDerechoAddNuevoHistorial.getText().toString()) > 3.25
                        || Double.parseDouble(etOjoIzquierdoAddNuevoHistorial.getText().toString()) > 3.25)) {
                    //Si paquete es igual a PLATINO y add derecho o izquierdo tienen mas de +3.25
                    valorCbTratamientoFotocromatico = "0";
                    valorCbTratamientoAR = "0";
                    valorCbTratamientoTinte = "0";
                    valorColorTratamientoTinte = "";
                    valorEstiloTratamientoTinte = "";
                    valorCbTratamientoBlueRay = "0";
                    valorCbTratamientoPolarizado = "";
                    valorCbTratamientoEspejo = "";
                    valorColorTratamientoEspejo = "";
                    valorCbTratamientoOtro = "0";
                    valorTratamientoOtro = "";
                    valorPrecioTratamientoOtro = "";
                }

                //Paquete premium

            }

            sqLiteDB = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID", idAlfanumerico);
            valores.put("ID_CONTRATO", idContrato);
            valores.put("EDAD", etEdadNuevoHistorial.getText().toString());
            valores.put("FECHAENTREGA", etFechaEntregaNuevoHistorial.getText().toString());
            valores.put("DIAGNOSTICO", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDiagnosticoNuevoHistorial.getText().toString())));
            valores.put("OCUPACION", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etOcupacionNuevoHistorial.getText().toString())));
            valores.put("DIABETES", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDiabetesNuevoHistorial.getText().toString())));
            valores.put("HIPERTENSION", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etHipertensionNuevoHistorial.getText().toString())));
            valores.put("DOLOR", cbDolorCabeza.isChecked() ? "1" : "0");
            valores.put("ARDOR", cbArdorOjos.isChecked() ? "1" : "0");
            valores.put("GOLPEOJOS", cbGolpeCabeza.isChecked() ? "1" : "0");
            valores.put("OTROM", cbOtroMolestia.isChecked() ? "1" : "0");
            valores.put("MOLESTIAOTRO", global.replaceCadena(etOtroMoletiaNuevoHistorial.getText().toString()));
            valores.put("ULTIMOEXAMEN", etUltimoExamenNuevoHistorial.getText().toString());
            valores.put("ESFERICODER", global.replaceCadena(ojoDerechoEsferico));
            valores.put("CILINDRODER", global.replaceCadena(ojoDerechoCilindro));
            valores.put("EJEDER", global.replaceCadena(ojoDerechoEje));
            valores.put("ADDDER", global.replaceCadena(ojoDerechoAdd));
            valores.put("ALTDER", global.replaceCadena(ojoDerechoAlt));
            valores.put("ESFERICOIZQ", global.replaceCadena(ojoIzquierdoEsferico));
            valores.put("CILINDROIZQ", global.replaceCadena(ojoIzquierdoCilindro));
            valores.put("EJEIZQ", global.replaceCadena(ojoIzquierdoEje));
            valores.put("ADDIZQ", global.replaceCadena(ojoIzquierdoAdd));
            valores.put("ALTIZQ", global.replaceCadena(ojoIzquierdoAlt));
            valores.put("ID_PRODUCTO", idsProductos[spProductoNuevoHistorial.getSelectedItemPosition()]);
            valores.put("ID_PAQUETE", idPaqueteSeleccionado);
            if (rbHiIndex.isChecked()) {
                valores.put("MATERIAL", rbHiIndex.isChecked() ? "0" : "");
                } else
                    if (rbCR.isChecked()){
                        valores.put("MATERIAL", rbCR.isChecked() ? "1" : "");
                    }else
                        if(rbPolicarbonato.isChecked()) {
                            valores.put("MATERIAL", rbPolicarbonato.isChecked() ? "2" : "");
                            valores.put("POLICARBONATOTIPO", cbPolicarbonatoTipo.isChecked() ? "1" : "0");
                        }else
                            if(rbOtroMaterial.isChecked()) {
                                valores.put("MATERIAL", rbOtroMaterial.isChecked() ? "3" : "");
                            }else {
                                valores.put("MATERIAL", "");
                            }
            valores.put("MATERIALOTRO", global.replaceCadena(etOtroMaterialNuevoHistorial.getText().toString()));
            valores.put("COSTOMATERIAL", etPrecioMaterialNuevoHistorial.getText().toString());
            if (rbFT.isChecked()) {
                valores.put("BIFOCAL", rbFT.isChecked() ? "0" : "");
            } else
                if (rbBlend.isChecked()){
                    valores.put("BIFOCAL", rbBlend.isChecked() ? "1" : "");
                }else
                    if(rbProgresivo.isChecked()) {
                        valores.put("BIFOCAL", rbProgresivo.isChecked() ? "2" : "");
                    }else
                        if(rbNA.isChecked()) {
                            valores.put("BIFOCAL", rbNA.isChecked() ? "3" : "");
                        }else
                            if(rbOtroBifocal.isChecked()) {
                                valores.put("BIFOCAL", rbOtroBifocal.isChecked() ? "4" : "");
                            }else {
                                valores.put("BIFOCAL", "");
                            }
            valores.put("FOTOCROMATICO", valorCbTratamientoFotocromatico);
            valores.put("AR", valorCbTratamientoAR);
            valores.put("TINTE", valorCbTratamientoTinte);
            valores.put("BLUERAY", valorCbTratamientoBlueRay);
            valores.put("OTROT", valorCbTratamientoOtro);
            valores.put("TRATAMIENTOOTRO", valorTratamientoOtro);
            valores.put("COSTOTRATAMIENTO", valorPrecioTratamientoOtro);
            valores.put("OBSERVACIONES", global.replaceCadena(etObservacionesNuevoHistorial.getText().toString()));
            valores.put("OBSERVACIONESINTERNO", global.replaceCadena(etObservacionesInternoNuevoHistorial.getText().toString()));
            valores.put("TIPO", "0");
            valores.put("BIFOCALOTRO", global.replaceCadena(etOtroBifocalNuevoHistorial.getText().toString()));
            valores.put("COSTOBIFOCAL", etPrecioBifocalNuevoHistorial.getText().toString());
            valores.put("EMBARAZADA", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etEmbarazadaNuevoHistorial.getText().toString())));
            valores.put("DURMIOSEISOCHOHORAS", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDurmioSeisOchoHorasNuevoHistorial.getText().toString())));
            valores.put("ACTIVIDADDIA", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etActividadDiaNuevoHistorial.getText().toString())));
            valores.put("PROBLEMASOJOS", global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etProblemasOjosNuevoHistorial.getText().toString())));
            valores.put("POLICARBONATOTIPO", rbPolicarbonato.isChecked() ? cbPolicarbonatoTipo.isChecked() ? "1" : "0": "");
            valores.put("ID_TRATAMIENTOCOLORTINTE", valorColorTratamientoTinte);
            valores.put("ESTILOTINTE", valorEstiloTratamientoTinte);
            valores.put("POLARIZADO", valorCbTratamientoPolarizado);
            valores.put("ID_TRATAMIENTOCOLORPOLARIZADO", valorColorTratamientoPolarizado);
            valores.put("ESPEJO", valorCbTratamientoEspejo);
            valores.put("ID_TRATAMIENTOCOLORESPEJO", valorColorTratamientoEspejo);
            valores.put("FOTOARMAZON", fotoArmazonPropio);
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT", fechaActual + " " + time);
            valores.put("UPDATED_AT", fechaActual + " " + time);
            sqLiteDB.insert("HISTORIALCLINICO",null, valores);
            sqLiteDB.close();

            if(idPaqueteSeleccionado.equals("1") || idPaqueteSeleccionado.equals("6")) {
                //LECTURA o DORADO 2
                guardarHistorialSinConversion(idContrato, idAlfanumerico);
            }

            if(esContratoPorCrear) {
                Toast.makeText(fragmento.getContext(), "Se guardo correctamente el contrato", Toast.LENGTH_LONG).show();
            }else {

                Toast.makeText(fragmento.getContext(), "Se creo correctamente el contrato", Toast.LENGTH_LONG).show();

                //Escribir registro de nuevo contrato
                global.escribirNuevoContratoOHistorialClinicoEnArchivoTXT(fechaActual, idContrato, "",1);
                //Escribir registro de nuevo historial clinico para contrato
                global.escribirNuevoContratoOHistorialClinicoEnArchivoTXT(fechaActual, idContrato, idAlfanumerico,2);

                //Sincronizar
                Object[] objetosWebService = {obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN"), "", ""};
                sincronizacion.sincronizarMetodo(0, objetosWebService, 0);

                if (spPaquetesNuevoHistorial.getSelectedItemPosition() == 6) {
                    //DORADO 2 (Manda a creacion de segundo historial)

                    Fragment verificadorFragment = new nuevoHistorialClinico();
                    Bundle bundle = new Bundle();
                    bundle.putString("ultimoIdContratoCreado", idContrato);
                    bundle.putString("ultimoIdHistorialClinicoCreado", idAlfanumerico);
                    bundle.putInt("posicionLista", 1);
                    verificadorFragment.setArguments(bundle);
                    FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, verificadorFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack(null);
                    transaction.commit();

                } else {
                    //Otro paquete diferente al DORADO 2 (Manda a la vista de verContrato)

                    //Mostrar alerta para ingresar numero de referencia para cita agendada
                    global.mostrarAlertDialogIngresarReferenciaCitaPrevia(idContrato);

                    Fragment verificadorFragment = new verContrato();
                    Bundle bundle = new Bundle();
                    bundle.putString("ultimoIdContratoCreado", idContrato);
                    verificadorFragment.setArguments(bundle);
                    FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, verificadorFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }

            }

        }catch (SQLiteException e){
            global.escribirError(e, 136);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void guardarHistorialSinConversion(String idContrato, String idAlfanumerico) {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID_CONTRATO", idContrato);
            valores.put("ID_HISTORIAL", idAlfanumerico);
            valores.put("ESFERICODER", etOjoDerechoEsfericoSinConversionNuevoHistorial.getText().toString());
            valores.put("CILINDRODER", etOjoDerechoCilindroSinConversionNuevoHistorial.getText().toString());
            valores.put("EJEDER", etOjoDerechoEjeSinConversionNuevoHistorial.getText().toString());
            valores.put("ADDDER", etOjoDerechoAddSinConversionNuevoHistorial.getText().toString());
            valores.put("ESFERICOIZQ", etOjoIzquierdoEsfericoSinConversionNuevoHistorial.getText().toString());
            valores.put("CILINDROIZQ", etOjoIzquierdoCilindroSinConversionNuevoHistorial.getText().toString());
            valores.put("EJEIZQ", etOjoIzquierdoEjeSinConversionNuevoHistorial.getText().toString());
            valores.put("ADDIZQ", etOjoIzquierdoAddSinConversionNuevoHistorial.getText().toString());
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT", fechaActual + " " + time);
            sqLiteDB2.insert("HISTORIALSINCONVERSION", null, valores);
            sqLiteDB2.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 137);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: obtenerIdContrato
     Descripcion: Obtener idContrato que aun no ha sido completado/llenado
   */
    private String obtenerIdContrato() {

        String idContrato = "";

        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID_CONTRATO FROM CONTRATOS WHERE DATOS = '0' AND ESTATUS_ESTADOCONTRATO = '' ORDER BY RANDOM() LIMIT 1";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No se encuentro ningun id contrato");
            }

            if (datos.moveToFirst()){
                idContrato = datos.getString(0);
            }

            sqLiteDB.close();
            datos.close();

            return idContrato;

        }catch (SQLiteException e) {
            global.escribirError(e, 138);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return idContrato;
    }

    /*Metodo/Funcion: mostrarAlertaCancelarConfirmacion
     Descripcion: Muestra alerta de confirmacion para cancelar el contrato antes de que este sea creado (redirecciona a vista principal)
   */
    public void mostrarAlertaCancelarConfirmacion() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Confirmación").setMessage("¿Deseas guardar el contrato?")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        guardarInformacionContratoPorCrear();
                        generarImagenContratoPorCrear();
                        Fragment verificadorFragment;
                        FragmentTransaction transaction;
                        verificadorFragment = new principal(false);
                        transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, verificadorFragment);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }).setNegativeButton("No guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Fragment verificadorFragment;
                FragmentTransaction transaction;
                verificadorFragment = new principal(false);
                transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, verificadorFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }).show();

    }

    private void guardarInformacionContratoPorCrear() {

        time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        fechaActual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");

        String idContrato = obtenerIdContrato();

        if(idContratoPadre.length() != 0) {
            //Es un contrato hijo
            if(idContratoHijo.length() != 0) {
                idContrato = idContratoHijo;
                borrarFotosYEliminarHistorialContratoPorCrear();
            }
        }else {
            //No es un contrato hijo

            if(idContratoHijo.length() != 0) {
                //Ya se habia creado el contrato

                if(global.obtenerAtributoContrato(idContratoHijo, "ESTATUS_ESTADOCONTRATO").equals("13")) {
                    //Es un contrato por crear
                    borrarFotosYEliminarHistorialContratoPorCrear();
                    idContrato = idContratoHijo;
                    idContratoPadre = "";
                }

            }
        }

        String fotoIneFrenteRuta = "";
        String fotoIneAtrasRuta = "";
        String fotoCasaRuta = "";
        String fotoComprobanteDomicilioRuta = "";
        String fotoPagareRuta = "";
        String fotoOtrosRuta = "";

        if(cbDuplicarDocumentos.isChecked()) {
            fotoIneFrenteRuta = global.obtenerAtributoContrato(idContratoPadre, "FOTOINEFRENTE");
            fotoIneAtrasRuta = global.obtenerAtributoContrato(idContratoPadre, "FOTOINEATRAS");
            fotoCasaRuta = global.obtenerAtributoContrato(idContratoPadre, "FOTOCASA");
            fotoComprobanteDomicilioRuta = global.obtenerAtributoContrato(idContratoPadre, "COMPROBANTEDOMICILIO");
            fotoPagareRuta = global.obtenerAtributoContrato(idContratoPadre, "PAGARE");
            fotoOtrosRuta = global.obtenerAtributoContrato(idContratoPadre, "FOTOOTROS");
        }

        if(ivFotoINEFrente.getDrawable() != null){
            fotoIneFrenteRuta = camara.guardarImagenGaleria(ivFotoINEFrente, "Foto-Ine-Frente-Contrato-" + idContrato + "-" +
                    fechaActual.replace("-", "") + time.replace(":", ""));
        }

        if(ivFotoINEAtras.getDrawable() != null){
            fotoIneAtrasRuta = camara.guardarImagenGaleria(ivFotoINEAtras, "Foto-Ine-Atras-Contrato-" + idContrato + "-" +
                    fechaActual.replace("-", "") + time.replace(":", ""));
        }

        if(ivFotoCasa.getDrawable() != null){
            fotoCasaRuta = camara.guardarImagenGaleria(ivFotoCasa, "Foto-Casa-Contrato-" + idContrato + "-" +
                    fechaActual.replace("-", "") + time.replace(":", ""));
        }

        if(ivFotoComprobanteDomicilio.getDrawable() != null){
            fotoComprobanteDomicilioRuta = camara.guardarImagenGaleria(ivFotoComprobanteDomicilio, "Foto-Comprobante-Domicilio-Contrato-" + idContrato + "-" +
                    fechaActual.replace("-", "") + time.replace(":", ""));
        }

        if(ivFotoPagare.getDrawable() != null){
            fotoPagareRuta = camara.guardarImagenGaleria(ivFotoPagare, "Foto-Pagare-Contrato-" + idContrato + "-" +
                    fechaActual.replace("-", "") + time.replace(":", ""));
        }

        if(ivFotoOtros.getDrawable() != null){
            fotoOtrosRuta = camara.guardarImagenGaleria(ivFotoOtros, "Foto-Otros-Contrato-" + idContrato + "-" +
                    fechaActual.replace("-", "") + time.replace(":", ""));
        }

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET DATOS='" + "0" + "', ID_USUARIOCREACION='" + obtenerRol.obtenerAtributoUsuarioLogeado("ID")
                    + "', NOMBRE_USUARIOCREACION='" + global.replaceCadena(obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO")) + "', ID_ZONA='" + idsZonas[spZona.getSelectedItemPosition()]
                    + "', NOMBRE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNombreCliente.getText().toString())) + "', CALLE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCalleCliente.getText().toString()))
                    + "', NUMERO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNumeroCliente.getText().toString())) + "', DEPTO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDepartamentoCliente.getText().toString()))
                    + "', ALLADODE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etAlLadoDeCliente.getText().toString())) + "', FRENTEA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etFrenteACliente.getText().toString()))
                    + "', ENTRECALLES='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etEntreCallesCliente.getText().toString())) + "', COLONIA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etColoniaCliente.getText().toString()))
                    + "', LOCALIDAD='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etLocalidadCliente.getText().toString())) + "', TELEFONO='" + etTelefonoCliente.getText().toString()
                    + "', TELEFONOREFERENCIA='" + etTelefonoReferenciaCliente.getText().toString() + "', NOMBREREFERENCIA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNombreReferenciaCliente.getText().toString()))
                    + "', CASATIPO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etTipoCasaCliente.getText().toString())) + "', CASACOLOR='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCasaColorCliente.getText().toString()))
                    + "', CORREO='" + etCorreoCliente.getText().toString().trim()
                    + "', FOTOINEFRENTE='" + fotoIneFrenteRuta + "', FOTOINEATRAS='" + fotoIneAtrasRuta
                    + "', FOTOCASA='" + fotoCasaRuta + "', COMPROBANTEDOMICILIO='" + fotoComprobanteDomicilioRuta
                    + "', ID_OPTOMETRISTA='" + idsOptometristas[spOptometrista.getSelectedItemPosition()]
                    + "', ID_PROMOCION='" + "" + "', TOTAL='" + '0'
                    + "', IDCONTRATORELACION='" + idContratoPadre + "', CONTADOR='" + "1"
                    + "', TOTALHISTORIAL='" + '0' + "', TOTALPROMOCION='" + "0"
                    + "', TOTALPRODUCTO='" + "0" + "', TOTALABONO='" + "0"
                    + "', ULTIMOABONO='" + ""
                    + "', ESTATUS_ESTADOCONTRATO='" + "13" + "', DIAPAGO='" + ""
                    + "', FECHACOBROINI='" + "" + "', FECHACOBROFIN='" + ""
                    + "', FECHAATRASO='" + "" + "', COSTOATRASO='" + "0"
                    + "', DIASELECCIONADO='" + "" + "', FECHAENTREGA='" + ""
                    + "', PAGOSADELANTAR='" + "0" + "', ENGANCHE='" + "0"
                    + "', ENTREGAPRODUCTO='" + "0" + "', ESTATUS='" + "0"
                    + "', ENVIADOPAGINA='" + "0"
                    + "', CONTRATOENVIADO='" + "0" + "', ALIAS='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etAliasCliente.getText().toString()))
                    + "', PAGARE='" + fotoPagareRuta + "', FOTOOTROS='" + fotoOtrosRuta + "', PROMOCIONTERMINADA='" + "0"
                    + "', SUBSCRIPCION='" + "" + "', FECHASUBSCRIPCION='" + ""
                    + "', TOTALREAL='" + "0" + "', DIATEMPORAL='" + ""
                    + "', COORDENADAS='" + coordenadasContrato + "', CALLEENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCalleClienteEntrega.getText().toString()))
                    + "', NUMEROENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNumeroClienteEntrega.getText().toString())) + "', DEPTOENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDepartamentoClienteEntrega.getText().toString()))
                    + "', ALLADODEENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etAlLadoDeClienteEntrega.getText().toString())) + "', FRENTEAENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etFrenteAClienteEntrega.getText().toString()))
                    + "', ENTRECALLESENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etEntreCallesClienteEntrega.getText().toString())) + "', COLONIAENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etColoniaClienteEntrega.getText().toString()))
                    + "', LOCALIDADENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etLocalidadClienteEntrega.getText().toString()))
                    + "', CASATIPOENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etTipoCasaClienteEntrega.getText().toString())) + "', CASACOLORENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCasaColorClienteEntrega.getText().toString()))
                    + "', OPCIONLUGARENTREGA='" + idsOpcionesLugarEntrega[spLugarEntrega.getSelectedItemPosition()]
                    + "', OBSERVACIONFOTOINE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoINEFrente.getText().toString())) + "', OBSERVACIONFOTOINEATRAS='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoINEAtras.getText().toString()))
                    + "', OBSERVACIONFOTOCASA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoCasa.getText().toString())) + "', OBSERVACIONCOMPROBANTEDOMICILIO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoComprobanteDomicilio.getText().toString()))
                    + "', OBSERVACIONPAGARE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoPagare.getText().toString())) + "', OBSERVACIONFOTOOTROS='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionOtraFoto.getText().toString()))
                    + "', CREATED_AT='" + fechaActual + " " + time + "', UPDATED_AT='" + fechaActual + " " + time
                    + "' WHERE ID_CONTRATO='" + idContrato + "'";

            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();

            guardarHistorialClinicoBD(idContrato, true);

        }catch (SQLiteException e) {
            global.escribirError(e, 139);
            Log.i("ERRORBD", e.getMessage() + "");
        }

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

    /*Metodo/Funcion: validarComponentesPaqueteUno
     Descripcion: Bloqueo de componentes para el paquete seleccionado (LECTURA, ECO JR, JR y DORADO 2)
   */
    public void validarComponentesPaqueteUno(boolean bloquearCheckboxTinte) {

        etOjoDerechoEsfericoNuevoHistorial.setEnabled(true);
        etOjoDerechoCilindroNuevoHistorial.setEnabled(true);
        etOjoDerechoEjeNuevoHistorial.setEnabled(true);
        etOjoDerechoAddNuevoHistorial.setEnabled(false);
        etOjoDerechoALTNuevoHistorial.setEnabled(false);
        etOjoIzquierdoEsfericoNuevoHistorial.setEnabled(true);
        etOjoIzquierdoCilindroNuevoHistorial.setEnabled(true);
        etOjoIzquierdoEjeNuevoHistorial.setEnabled(true);
        etOjoIzquierdoAddNuevoHistorial.setEnabled(false);
        etOjoIzquierdoALTNuevoHistorial.setEnabled(false);

        if(bloquearCheckboxTinte) {
            cbTratamientoTinte.setEnabled(false);
        }else {
            cbTratamientoFotocromatico.setEnabled(true);
            cbTratamientoAR.setEnabled(true);
            cbTratamientoTinte.setEnabled(true);
            cbTratamientoBlueray.setEnabled(true);
        }

        cbTratamientoFotocromatico.setChecked(false);
        cbTratamientoAR.setChecked(false);
        cbTratamientoTinte.setChecked(false);
        cbTratamientoBlueray.setChecked(false);
        cbTratamientoPolarizado.setChecked(false);
        cbTratamientoPolarizado.setEnabled(false);
        cbTratamientoEspejeado.setChecked(false);
        cbTratamientoEspejeado.setEnabled(false);
        cbTratamientoOtro.setChecked(false);

        etOjoDerechoAddNuevoHistorial.setText("");
        etOjoDerechoALTNuevoHistorial.setText("");
        etOjoIzquierdoAddNuevoHistorial.setText("");
        etOjoIzquierdoALTNuevoHistorial.setText("");

    }

    /*Metodo/Funcion: validarComponentesPaqueteDos
     Descripcion: Bloqueo de componentes para el paquete seleccionado (NO SELECCIONADO y PROTECCION)
   */
    public void validarComponentesPaqueteDos() {

        etOjoDerechoEsfericoNuevoHistorial.setEnabled(false);
        etOjoDerechoCilindroNuevoHistorial.setEnabled(false);
        etOjoDerechoEjeNuevoHistorial.setEnabled(false);
        etOjoDerechoAddNuevoHistorial.setEnabled(false);
        etOjoDerechoALTNuevoHistorial.setEnabled(false);

        etOjoIzquierdoEsfericoNuevoHistorial.setEnabled(false);
        etOjoIzquierdoCilindroNuevoHistorial.setEnabled(false);
        etOjoIzquierdoEjeNuevoHistorial.setEnabled(false);
        etOjoIzquierdoAddNuevoHistorial.setEnabled(false);
        etOjoIzquierdoALTNuevoHistorial.setEnabled(false);

        cbTratamientoFotocromatico.setEnabled(true);
        cbTratamientoAR.setEnabled(true);
        cbTratamientoTinte.setEnabled(false);
        cbTratamientoBlueray.setEnabled(true);
        cbTratamientoOtro.setEnabled(true);

        cbTratamientoFotocromatico.setChecked(false);
        cbTratamientoAR.setChecked(false);
        cbTratamientoTinte.setChecked(false);
        cbTratamientoBlueray.setChecked(false);
        cbTratamientoPolarizado.setChecked(false);
        cbTratamientoPolarizado.setEnabled(false);
        cbTratamientoEspejeado.setChecked(false);
        cbTratamientoEspejeado.setEnabled(false);
        cbTratamientoOtro.setChecked(false);

        etOjoDerechoEsfericoNuevoHistorial.setText("");
        etOjoDerechoCilindroNuevoHistorial.setText("");
        etOjoDerechoEjeNuevoHistorial.setText("");
        etOjoDerechoAddNuevoHistorial.setText("");
        etOjoDerechoALTNuevoHistorial.setText("");
        etOjoIzquierdoEsfericoNuevoHistorial.setText("");
        etOjoIzquierdoCilindroNuevoHistorial.setText("");
        etOjoIzquierdoEjeNuevoHistorial.setText("");
        etOjoIzquierdoAddNuevoHistorial.setText("");
        etOjoIzquierdoALTNuevoHistorial.setText("");

        vaciarCamposHistorialSinConversion();

    }

    private void vaciarCamposHistorialSinConversion() {
        //Poner vacios los campos de historialsinconversion
        etOjoDerechoEsfericoSinConversionNuevoHistorial.setText("");
        etOjoDerechoCilindroSinConversionNuevoHistorial.setText("");
        etOjoDerechoEjeSinConversionNuevoHistorial.setText("");
        etOjoDerechoAddSinConversionNuevoHistorial.setText("");
        etOjoIzquierdoEsfericoSinConversionNuevoHistorial.setText("");
        etOjoIzquierdoCilindroSinConversionNuevoHistorial.setText("");
        etOjoIzquierdoEjeSinConversionNuevoHistorial.setText("");
        etOjoIzquierdoAddSinConversionNuevoHistorial.setText("");
    }

    /*Metodo/Funcion: validarComponentesPaqueteTres
     Descripcion: Bloqueo de componentes para el paquete seleccionado (DORADO 1 y PLATINO)
   */
    public void validarComponentesPaqueteTres(boolean desbloquearCheckboxTinte, boolean defaultCeroALT) {

        etOjoDerechoEsfericoNuevoHistorial.setEnabled(true);
        etOjoDerechoCilindroNuevoHistorial.setEnabled(true);
        etOjoDerechoEjeNuevoHistorial.setEnabled(true);
        etOjoDerechoAddNuevoHistorial.setEnabled(true);
        etOjoDerechoALTNuevoHistorial.setEnabled(true);

        etOjoIzquierdoEsfericoNuevoHistorial.setEnabled(true);
        etOjoIzquierdoCilindroNuevoHistorial.setEnabled(true);
        etOjoIzquierdoEjeNuevoHistorial.setEnabled(true);
        etOjoIzquierdoAddNuevoHistorial.setEnabled(true);
        etOjoIzquierdoALTNuevoHistorial.setEnabled(true);

        if (defaultCeroALT) {
            //defaultCeroALT es igual a true
            etOjoDerechoALTNuevoHistorial.setText("0");
            etOjoIzquierdoALTNuevoHistorial.setText("0");
        }

        if(desbloquearCheckboxTinte) {
            cbTratamientoFotocromatico.setEnabled(true);
            cbTratamientoAR.setEnabled(true);
            cbTratamientoTinte.setEnabled(true);
            cbTratamientoBlueray.setEnabled(true);
            cbTratamientoOtro.setEnabled(true);
        }

        cbTratamientoFotocromatico.setChecked(false);
        cbTratamientoAR.setChecked(false);
        cbTratamientoBlueray.setChecked(false);
        cbTratamientoTinte.setChecked(false);
        cbTratamientoPolarizado.setChecked(false);
        cbTratamientoPolarizado.setEnabled(false);
        cbTratamientoEspejeado.setChecked(false);
        cbTratamientoEspejeado.setEnabled(false);
        cbTratamientoOtro.setChecked(false);

        vaciarCamposHistorialSinConversion();

    }
    /*Metodo/Funcion: validarComponentesPaquetePremium
    Descripcion: Bloqueo de componentes para el paquete seleccionado (PREMIUM)
    */
    public void validarComponentesPaquetePremium(){
        etOjoDerechoEsfericoNuevoHistorial.setEnabled(true);
        etOjoDerechoCilindroNuevoHistorial.setEnabled(true);
        etOjoDerechoEjeNuevoHistorial.setEnabled(true);
        etOjoDerechoAddNuevoHistorial.setEnabled(true);
        etOjoDerechoALTNuevoHistorial.setEnabled(true);

        etOjoIzquierdoEsfericoNuevoHistorial.setEnabled(true);
        etOjoIzquierdoCilindroNuevoHistorial.setEnabled(true);
        etOjoIzquierdoEjeNuevoHistorial.setEnabled(true);
        etOjoIzquierdoAddNuevoHistorial.setEnabled(true);
        etOjoIzquierdoALTNuevoHistorial.setEnabled(true);

        etOjoDerechoALTNuevoHistorial.setText("0");
        etOjoIzquierdoALTNuevoHistorial.setText("0");

        cbTratamientoFotocromatico.setChecked(false);
        cbTratamientoFotocromatico.setEnabled(true);
        cbTratamientoAR.setChecked(false);
        cbTratamientoAR.setEnabled(true);
        cbTratamientoTinte.setChecked(false);
        cbTratamientoTinte.setEnabled(true);
        cbTratamientoBlueray.setChecked(false);
        cbTratamientoBlueray.setEnabled(true);
        cbTratamientoTinte.setChecked(false);
        cbTratamientoPolarizado.setChecked(false);
        cbTratamientoPolarizado.setEnabled(true);
        cbTratamientoEspejeado.setChecked(false);
        cbTratamientoEspejeado.setEnabled(true);
        cbTratamientoOtro.setChecked(false);
        cbTratamientoOtro.setEnabled(true);

    }

    /*Metodo/Funcion: verificarCampoFechaEntregaEstaVacio
     Descripcion: Verificar si campo fecha entrega esta vacio
   */
    public void verificarCampoFechaEntregaEstaVacio() {
        if(verificarSpinnerSelected(spPaquetesNuevoHistorial) && etFechaEntregaNuevoHistorial.getText().toString().equals("")) {
            //Si seleccion paquete ha sido seleccionado y fecha entrega esta vacia
            showDatePickerDialog(etFechaEntregaNuevoHistorial);
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

    /*Metodo/Funcion: requestFocusCamposOjoDerIzqPaqueteUno
     Descripcion: Verificar que el paquete ha sido seleccionado (LECTURA, ECO JR, JR y DORADO 2)
     y hacer el requestFocus en los campos correspondientes ha ese paquete
   */
    public void requestFocusCamposOjoDerIzqPaqueteUno(boolean historialsinconversion) {
        etFechaEntregaNuevoHistorial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                teclado.showKeyboard();
                if(historialsinconversion) {
                    //Paquete LECTURA o DORADO 2
                    etOjoDerechoEsfericoSinConversionNuevoHistorial.requestFocus();
                }else {
                    //Paquete ECO JR o JR
                    etOjoDerechoEsfericoNuevoHistorial.requestFocus();
                }
            }
        });

        //Empiezan campos sin conversion
        etOjoDerechoEsfericoSinConversionNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoCilindroSinConversionNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoCilindroSinConversionNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoEjeSinConversionNuevoHistorial.requestFocus();
                    return true;
                }

                return false;

            }
        });
        etOjoDerechoEjeSinConversionNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoAddSinConversionNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoAddSinConversionNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEsfericoSinConversionNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEsfericoSinConversionNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoCilindroSinConversionNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoCilindroSinConversionNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEjeSinConversionNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEjeSinConversionNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoAddSinConversionNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoAddSinConversionNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoEsfericoNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        //Terminan campos sin conversion

        etOjoDerechoEsfericoNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoCilindroNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoCilindroNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoEjeNuevoHistorial.requestFocus();
                    return true;
                }

                return false;

            }
        });
        etOjoDerechoEjeNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEsfericoNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEsfericoNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoCilindroNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoCilindroNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEjeNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEjeNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
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

    /*Metodo/Funcion: requestFocusCamposOjoDerIzqPaqueteTres
     Descripcion: Verificar que el paquete ha sido seleccionado (DORADO 1 y PLATINO)
     y hacer el requestFocus en los campos correspondientes ha ese paquete
   */
    public void requestFocusCamposOjoDerIzqPaqueteTres() {
        etFechaEntregaNuevoHistorial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                teclado.showKeyboard();
                etOjoDerechoEsfericoNuevoHistorial.requestFocus();
            }
        });
        etOjoDerechoEsfericoNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoCilindroNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoCilindroNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoEjeNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoEjeNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoAddNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoAddNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoALTNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoALTNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEsfericoNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEsfericoNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoCilindroNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoCilindroNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEjeNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEjeNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoAddNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoAddNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoALTNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoALTNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
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

    public void mostrarAlertDialogContratoLioFuga() {

        String[] datosContratoSeleccionadoAutoComplete = actvContratosLiosFugas.getText().toString().split(" , ");

        String nombre = global.obtenerAtributoTabla("CONTRATOSLIOSFUGAS", "NOMBRE",
                "NOMBRE = '" + datosContratoSeleccionadoAutoComplete[0] +
                        "' AND COLONIA = '" + datosContratoSeleccionadoAutoComplete[1] +
                        "' AND CALLE = '" + datosContratoSeleccionadoAutoComplete[2] +
                        "' AND NUMERO = '" + datosContratoSeleccionadoAutoComplete[3] +
                        "' AND TELEFONO"
                , datosContratoSeleccionadoAutoComplete[4]);
        String colonia = global.obtenerAtributoTabla("CONTRATOSLIOSFUGAS", "COLONIA",
                "NOMBRE = '" + datosContratoSeleccionadoAutoComplete[0] +
                        "' AND COLONIA = '" + datosContratoSeleccionadoAutoComplete[1] +
                        "' AND CALLE = '" + datosContratoSeleccionadoAutoComplete[2] +
                        "' AND NUMERO = '" + datosContratoSeleccionadoAutoComplete[3] +
                        "' AND TELEFONO"
                , datosContratoSeleccionadoAutoComplete[4]);
        String calle = global.obtenerAtributoTabla("CONTRATOSLIOSFUGAS", "CALLE",
                "NOMBRE = '" + datosContratoSeleccionadoAutoComplete[0] +
                        "' AND COLONIA = '" + datosContratoSeleccionadoAutoComplete[1] +
                        "' AND CALLE = '" + datosContratoSeleccionadoAutoComplete[2] +
                        "' AND NUMERO = '" + datosContratoSeleccionadoAutoComplete[3] +
                        "' AND TELEFONO"
                , datosContratoSeleccionadoAutoComplete[4]);
        String numero = global.obtenerAtributoTabla("CONTRATOSLIOSFUGAS", "NUMERO",
                "NOMBRE = '" + datosContratoSeleccionadoAutoComplete[0] +
                        "' AND COLONIA = '" + datosContratoSeleccionadoAutoComplete[1] +
                        "' AND CALLE = '" + datosContratoSeleccionadoAutoComplete[2] +
                        "' AND NUMERO = '" + datosContratoSeleccionadoAutoComplete[3] +
                        "' AND TELEFONO"
                , datosContratoSeleccionadoAutoComplete[4]);
        String telefono = global.obtenerAtributoTabla("CONTRATOSLIOSFUGAS", "TELEFONO",
                "NOMBRE = '" + datosContratoSeleccionadoAutoComplete[0] +
                        "' AND COLONIA = '" + datosContratoSeleccionadoAutoComplete[1] +
                        "' AND CALLE = '" + datosContratoSeleccionadoAutoComplete[2] +
                        "' AND NUMERO = '" + datosContratoSeleccionadoAutoComplete[3] +
                        "' AND TELEFONO"
                , datosContratoSeleccionadoAutoComplete[4]);
        String cambios = global.obtenerAtributoTabla("CONTRATOSLIOSFUGAS", "CAMBIOS",
                "NOMBRE = '" + datosContratoSeleccionadoAutoComplete[0] +
                        "' AND COLONIA = '" + datosContratoSeleccionadoAutoComplete[1] +
                        "' AND CALLE = '" + datosContratoSeleccionadoAutoComplete[2] +
                        "' AND NUMERO = '" + datosContratoSeleccionadoAutoComplete[3] +
                        "' AND TELEFONO"
                , datosContratoSeleccionadoAutoComplete[4]);


        String mensajeCompleto = "<b>Nombre: </b>" + nombre + "<br>" +
                "<b>Domicilio: </b>" + colonia + ", " + calle + " #" + numero + "<br>" +
                "<b>Teléfono: </b>" + telefono + "<br>" +
                "<b>Mensaje: </b>" + cambios;

        actvContratosLiosFugas.setText("");

        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle(Html.fromHtml("<font color='#FFACA6'>Datos del cliente</font>"))
              .setMessage(Html.fromHtml(mensajeCompleto))
              .setPositiveButton("LEIDO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

    }

    public void filtrarListaArmazonesProductos(String filtro) {

        try{

            String condicionFiltro = "";
            //Existe algun texto para filtrar?
            if(filtro.length() > 0){
                condicionFiltro = " AND (NOMBRE LIKE '%"+filtro+"%' OR COLOR LIKE '%"+filtro+"%')";
            }

            //Consulta para obtener lista de productos
            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID, NOMBRE, COLOR, PIEZAS FROM PRODUCTO WHERE ID_TIPOPRODUCTO = '1'" + condicionFiltro + "ORDER BY NOMBRE";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay productos registrados");
            }

            idsProductos = new String[datos.getCount() + 1];
            nombresProductos = new String[datos.getCount() + 1];
            coloresRBGProductos = new int[datos.getCount() + 1];
            idsProductos[0] = "";
            nombresProductos[0] = "Seleccionar";
            coloresRBGProductos[0] = Color.WHITE;

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){
                    idsProductos[i]= datos.getString(0);
                    nombresProductos[i] = datos.getString(1) + " | "+datos.getString(2)+ " | "+datos.getString(3)+"pza.";

                    if(Integer.parseInt(datos.getString(3)) <= 10){
                        coloresRBGProductos[i] = Color.RED;
                    }else{
                        coloresRBGProductos[i] = Color.WHITE;
                    }

                    datos.moveToNext();
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

            spProductoNuevoHistorial.setAdapter(adapter);

        }catch (SQLiteException e){
            global.escribirError(e, 266);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    public void verificarColoniaSeleccionada(Spinner spColonias, EditText etColonia, EditText etLocalidad){
        if(spColonias.getSelectedItemPosition() != 0){
            etColonia.setText(Array.get(nameColonias,spColonias.getSelectedItemPosition()).toString());
            etLocalidad.setText(Array.get(nameLocalidad,spColonias.getSelectedItemPosition()).toString());
        }else{
            etColonia.setText("");
            etLocalidad.setText("");
        }

    }

    public void asignarZonaPorColoniaSeleccionada(){
        String idZonaColonia = Array.get(idsZonaColonias,spColoniaClienteEntrega.getSelectedItemPosition()).toString();
        int indiceZonaAsignada = 0;
        for (int i = 0; i < idsZonas.length; i++){
            if(idZonaColonia.equals(Array.get(idsZonas,i).toString())){
                indiceZonaAsignada = i;
                break;
            }
        }

        spZona.setSelection(indiceZonaAsignada);
    }

    public void deshabilitarCamposZonaColoniaLocalidad(){

        //Bloquear los campos donde se escribe el texto
            spZona.setEnabled(false);
            etColoniaClienteEntrega.setEnabled(false);
            etLocalidadClienteEntrega.setEnabled(false);

    }

    public void filtrarListaColonias(String filtro, Spinner spinnerFiltrar) {

        try{

            String condicionFiltro = "";
            //Existe algun texto para filtrar?
            if(filtro.length() > 0){
                condicionFiltro = " WHERE COLONIA LIKE '%"+filtro+"%' OR LOCALIDAD LIKE '%"+filtro+"%'";
            }

            //Consulta para obtener lista de productos
            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID_ZONA, COLONIA, LOCALIDAD FROM COLONIAS" + condicionFiltro + " ORDER BY LOCALIDAD ASC, COLONIA ASC";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay colonias registradas");
            }

            idsZonaColonias = new String[datos.getCount() + 1];
            nameColonias = new String[datos.getCount() + 1];
            nameLocalidad = new String[datos.getCount() + 1];
            nameColoniaLocalidad = new String[datos.getCount() + 1];
            idsZonaColonias[0] = "";
            nameColonias[0] = "";
            nameLocalidad[0] = "";
            nameColoniaLocalidad[0] = "Seleccionar";

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){
                    idsZonaColonias[i]= datos.getString(0);
                    nameColonias[i] = datos.getString(1);
                    nameLocalidad[i] = datos.getString(2);
                    nameColoniaLocalidad[i] = datos.getString(2) + "-" + datos.getString(1);
                    datos.moveToNext();
                }

            }

            sqLiteDB.close();
            datos.close();

            spinnerFiltrar.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, nameColoniaLocalidad));

        }catch (SQLiteException e){
            global.escribirError(e, 274);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    public void registrarMovimientoBusquedaLioFuga(CharSequence filtro){

        String resultado = "";
        //Verificar si existe movimiento registrado de busqueda de cliente lio/fuga
        resultado = global.obtenerUnResultadoQuery("SELECT CAMBIOS FROM HISTORIALCONTRATO WHERE ID_CONTRATO = '" + idContratoMovimientoHistorial + "' AND TIPOMENSAJE = '1'");

        if(resultado.equals("")){
            //No existe ningun registro de que se busco cliente como lio/fuga - Insertar registro movimiento
            historialMovimientosContrato.guardarHistorialMovimientosContrato(idContratoMovimientoHistorial,"Realizo busqueda de cliente con pago atrasado/lio/fuga mediante filtro: '" + filtro.toString() +"'","1");
        }else{
            //Existe un registro - Validar si cadena es mayor a la registrada
            String filtroDB[] = resultado.split("'");

            if(filtro.toString().length() > filtroDB[1].length()){
                //Filtro de busqueda es de mayor longitud a filtro almacenado en BD - Eliminar movimiento historial y registrar nuevo movimiento
                historialMovimientosContrato.eliminarMovimientoHistorialContrato(idContratoMovimientoHistorial,"TIPOMENSAJE","1");
                historialMovimientosContrato.guardarHistorialMovimientosContrato(idContratoMovimientoHistorial,"Realizo busqueda de cliente con pago atrasado/lio/fuga mediante filtro: '" + filtro.toString() +"'","1");
            }
        }

    }

    public boolean verificarPiezasRestantesproduto(){

        //Seleccionaste una opcion distinta a la de seleccionar
        if(spProductoNuevoHistorial.getSelectedItem().toString() != "Seleccionar") {

            //Validar que producto seccionado cuente con mas de 10 piezas
            String producto[] = spProductoNuevoHistorial.getSelectedItem().toString().split("\\|");
            String piezas = producto[2].trim(); //Eliminar espacios en blanco
            int numpiezas = Integer.parseInt(piezas.substring(0,piezas.length() - 4));  //Extraer solo el total de piezas
            if(numpiezas < 10){
                //Piezas del producto es menor que 10 - Seleccionar por default 1er opcion
                spProductoNuevoHistorial.setSelection(0);
                return false;
            }
        }

        return true;
    }

    public void llenarSpTratamientoTinte(){
        if(rbFT.isChecked() || rbBlend.isChecked() || rbProgresivo.isChecked() || rbOtroBifocal.isChecked()){
            //Tipo de bifocal FT, Blend, Progresivo, Otro
            idsEstiloTratamientoTinte = new String[]{"","0"};
            estiloTratamientoTinte = new String[]{"Seleccionar","DESVANECIDO"};
        }else{
            //NA tipo bifocal
            idsEstiloTratamientoTinte = new String[]{"","0", "1"};
            estiloTratamientoTinte = new String[]{"Seleccionar","DESVANECIDO", "COMPLETO"};
        }

        spEstiloTratamiento.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, estiloTratamientoTinte));
        spColorTratamiento.setSelection(0);
        spEstiloTratamiento.setSelection(0);
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
                    //Dorado2
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
                    ojoDerechoAdd = etOjoDerechoAddNuevoHistorial.getText().toString().trim();
                    ojoIzquierdoAdd = etOjoIzquierdoAddNuevoHistorial.getText().toString().trim();
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
                rgTipoBifocalNuevoHistorial.clearCheck();
                rbFT.setEnabled(true);
                rbBlend.setEnabled(true);
                rbProgresivo.setEnabled(true);
                rbNA.setEnabled(false);
                seleccionarBifocalGuardadoBD();
            }else{
                //Bifocal tipo NA seleccionado por default
                rgTipoBifocalNuevoHistorial.clearCheck();
                rbFT.setEnabled(false);
                rbBlend.setEnabled(false);
                rbProgresivo.setEnabled(false);
                rbNA.setEnabled(true);
                rbNA.setChecked(true);
            }

            rbOtroBifocal.setEnabled(true);
            etOtroBifocalNuevoHistorial.setText("");
            etPrecioBifocalNuevoHistorial.setText("");

        }else{
            //Paquetes de JR y Eco Jr todos los campos de bifocal habilitados
            rgTipoBifocalNuevoHistorial.clearCheck();
            rbFT.setEnabled(true);
            rbBlend.setEnabled(true);
            rbProgresivo.setEnabled(true);
            rbNA.setEnabled(true);
            rbOtroBifocal.setEnabled(true);
            etOtroBifocalNuevoHistorial.setText("");
            etPrecioBifocalNuevoHistorial.setText("");
            seleccionarBifocalGuardadoBD();
        }

        llenarSpTratamientoTinte();

    }

    public void seleccionarBifocalGuardadoBD(){
        if(!bifocalBD.equals("")){
            //Se selecciono algun bifocal
            switch (Integer.parseInt(bifocalBD)){
                case 0:
                    //rbFT fue chequeado
                    rbFT.setChecked(true);
                    break;
                case 1:
                    //rbBlend fue chequeado
                    rbBlend.setChecked(true);
                    break;
                case 2:
                    //rbProgresivo fue chequeado
                    rbProgresivo.setChecked(true);
                    break;
                case 3:
                    //rbNA fue chequeado
                    rbNA.setChecked(true);
                    break;
                case 4:
                    //rbOtroBifocal fue chequeado
                    rbOtroBifocal.setChecked(true);
                    etOtroBifocalNuevoHistorial.setText(otroBifocalBD);
                    etPrecioBifocalNuevoHistorial.setText(otroBifocalPrecioBD);
                    break;
            }

        }
    }

    public void verificarProductoPaqueteSeleccionado(){
        String idProductoSeleccionado = idsProductos[spProductoNuevoHistorial.getSelectedItemPosition()];
        String paqueteSeleccionado = idsPaquetes[spPaquetesNuevoHistorial.getSelectedItemPosition()];

        if(!idProductoSeleccionado.equals("") && !paqueteSeleccionado.equals("")){
            String armazonPremium = global.obtenerAtributoTabla("PRODUCTO","PREMIUM","ID",idProductoSeleccionado);

            if(armazonPremium.equals("1")){
                //Es armazon premium - Verificar paquete seleccionado
                if(spPaquetesNuevoHistorial.getSelectedItemPosition() != 8){
                    //Paquete es diferente de PREMIUM - Limpiar paquete seleccionado
                    spPaquetesNuevoHistorial.setSelection(0);
                }
            }
        }
    }

    public void generarImagenContratoPorCrear(){
        //Generar mapa de bits de LinearLayout principal
        Bitmap bitmap = Bitmap.createBitmap(llPrincipalNuevoContrato.getWidth(), llPrincipalNuevoContrato.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        llPrincipalNuevoContrato.draw(canvas);

        if (bitmap != null) {
            //Bitmap creado correctamente
            String fecha = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
            //Nombre de imagen
            String imageName = "Respaldo_Contrato_" + idContratoMovimientoHistorial + "_" + fecha.replace("/","") + "_" + hora.replace(":","") + ".jpg";

            try {
                //Gnenerar imagen tipo JPg y almacenar en galeria o pictures.
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, bytes);
                MediaStore.Images.Media.insertImage(fragmento.getActivity().getContentResolver(), bitmap, imageName, null);
                Toast.makeText(fragmento.getContext(),"Imagen de respaldo generada correctamente.",Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                global.escribirError(e, 306);
                Log.i("ImagenContrato", "Error al generar imagen de respaldo: " + e.getMessage());
                Toast.makeText(fragmento.getContext(),"Ocurrio un error al intentar generar la imagen.",Toast.LENGTH_LONG).show();
            }

        }

    }

    public void actualizarContratoNoTerminado(){

        //Reiniciar valores para verificar campos vacios
        correcto = true;
        Arrays.fill(arregloComponentes, Boolean.FALSE);
        for(int i=0;i<arregloComponentes.length;i++){
            mostrarOcultarMensajesError(i,false);
        }

        //Verificar campos de contrato
        verificarComponentesIguales();

        //Validar campos de molestias
        if(!cbDolorCabeza.isChecked() && !cbArdorOjos.isChecked() && !cbGolpeCabeza.isChecked() && !cbOtroMolestia.isChecked()){
            arregloComponentes[25] = true;
            tvOtroMoletiaNuevoHistorial.setText("Selecciona al menos una opción");
        }

        if(cbDolorCabeza.isChecked() || cbArdorOjos.isChecked() || cbGolpeCabeza.isChecked() || cbOtroMolestia.isChecked()) {
            if(cbOtroMolestia.isChecked()) {
                if(etOtroMoletiaNuevoHistorial.getText().toString().equals("")) {
                    tvOtroMoletiaNuevoHistorial.setText("Campo vacío");
                    arregloComponentes[25] = true;
                }
            }
        }

        //Mostrar ocultar leyendas de campos incorrectos o vacios
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
            actualizarInformacionContrato();
            actualizarHistorialClinicoContrato();

            //Registrar movimiento
            historialMovimientosContrato.guardarHistorialMovimientosContrato(idContratoHijo,
                    "Actualizo contrato", "0");

            //Mandar a vista principal
            Fragment verificadorFragment;
            FragmentTransaction transaction;
            verificadorFragment = new principal(false);
            transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, verificadorFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack(null);
            transaction.commit();

            Toast.makeText(fragmento.getContext(),"Contrato actualizado correctamente.",Toast.LENGTH_LONG).show();
        }else{
            //Formulario incorrecto
            Toast.makeText(fragmento.getContext(),"Uno o mas campos vacios",Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarInformacionContrato(){
        try {

            time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            fechaActual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");

            String fotoIneFrenteRuta = global.obtenerAtributoContrato(idContratoHijo, "FOTOINEFRENTE");
            String fotoIneAtrasRuta = global.obtenerAtributoContrato(idContratoHijo, "FOTOINEATRAS");
            String fotoCasaRuta = global.obtenerAtributoContrato(idContratoHijo, "FOTOCASA");
            String fotoComprobanteDomicilioRuta = global.obtenerAtributoContrato(idContratoHijo, "COMPROBANTEDOMICILIO");
            String fotoPagareRuta = global.obtenerAtributoContrato(idContratoHijo, "PAGARE");
            String fotoOtrosRuta = global.obtenerAtributoContrato(idContratoHijo, "FOTOOTROS");

            if(global.obtenerAtributoTabla("IMAGENESCARGADASCONTRATOS","ID_CONTRATO","ID_CONTRATO",idContratoHijo).equals("")){
                //No existe registro en la tabla de imagenescargadascontrato - Insertar registro
                guardarEnTablaImagenesCargadasContratos(idContratoHijo, "2", fotoOtrosRuta);
            }

            if(ivFotoINEFrente.getDrawable() != null && camara.banderaFotoIneFrente){
                fotoIneFrenteRuta = camara.guardarImagenGaleria(ivFotoINEFrente, "Foto-Ine-Frente-Contrato-" + idContratoHijo + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOINE", "0", "ID_CONTRATO", idContratoHijo);
                camara.banderaFotoIneFrente = false; //Reiniciar bandera para validar evento de la camara
            }

            if(ivFotoINEAtras.getDrawable() != null && camara.banderaFotoIneAtras){
                fotoIneAtrasRuta = camara.guardarImagenGaleria(ivFotoINEAtras, "Foto-Ine-Atras-Contrato-" + idContratoHijo + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOINEATRAS", "0", "ID_CONTRATO", idContratoHijo);
                camara.banderaFotoIneAtras = false; //Reiniciar bandera para validar evento de la camara
            }

            if(ivFotoCasa.getDrawable() != null && camara.banderaFotoCasa){
                fotoCasaRuta = camara.guardarImagenGaleria(ivFotoCasa, "Foto-Casa-Contrato-" + idContratoHijo + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOCASA", "0", "ID_CONTRATO", idContratoHijo);
                camara.banderaFotoCasa = false; //Reiniciar bandera para validar evento de la camara
            }

            if(ivFotoComprobanteDomicilio.getDrawable() != null && camara.banderaFotoComprobanteDomicilio){
                fotoComprobanteDomicilioRuta = camara.guardarImagenGaleria(ivFotoComprobanteDomicilio, "Foto-Comprobante-Domicilio-Contrato-" + idContratoHijo + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "COMPROBANTEDOMICILIO", "0", "ID_CONTRATO", idContratoHijo);
                camara.banderaFotoComprobanteDomicilio = false; //Reiniciar bandera para validar evento de la camara
            }

            if(ivFotoPagare.getDrawable() != null && camara.banderaFotoPagare){
                fotoPagareRuta = camara.guardarImagenGaleria(ivFotoPagare, "Foto-Pagare-Contrato-" + idContratoHijo + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "PAGARE", "0", "ID_CONTRATO", idContratoHijo);
                camara.banderaFotoPagare = false; //Reiniciar bandera para validar evento de la camara
            }

            if(ivFotoOtros.getDrawable() != null && camara.banderaFotoOtros){
                fotoOtrosRuta = camara.guardarImagenGaleria(ivFotoOtros, "Foto-Otros-Contrato-" + idContratoHijo + "-" +
                        fechaActual.replace("-", "") + time.replace(":", ""));
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOOTROS", "0", "ID_CONTRATO", idContratoHijo);
                camara.banderaFotoOtros = false; //Reiniciar bandera para validar evento de la camara
            }

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET DATOS='" + "1" + "', ID_USUARIOCREACION='" + obtenerRol.obtenerAtributoUsuarioLogeado("ID")
                    + "', NOMBRE_USUARIOCREACION='" + global.replaceCadena(obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO")) + "', ID_ZONA='" + idsZonas[spZona.getSelectedItemPosition()]
                    + "', NOMBRE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNombreCliente.getText().toString())) + "', CALLE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCalleCliente.getText().toString()))
                    + "', NUMERO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNumeroCliente.getText().toString())) + "', DEPTO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDepartamentoCliente.getText().toString()))
                    + "', ALLADODE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etAlLadoDeCliente.getText().toString())) + "', FRENTEA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etFrenteACliente.getText().toString()))
                    + "', ENTRECALLES='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etEntreCallesCliente.getText().toString())) + "', COLONIA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etColoniaCliente.getText().toString()))
                    + "', LOCALIDAD='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etLocalidadCliente.getText().toString())) + "', TELEFONO='" + etTelefonoCliente.getText().toString()
                    + "', TELEFONOREFERENCIA='" + etTelefonoReferenciaCliente.getText().toString() + "', NOMBREREFERENCIA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNombreReferenciaCliente.getText().toString()))
                    + "', CASATIPO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etTipoCasaCliente.getText().toString())) + "', CASACOLOR='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCasaColorCliente.getText().toString()))
                    + "', CORREO='" + etCorreoCliente.getText().toString().trim()
                    + "', FOTOINEFRENTE='" + fotoIneFrenteRuta + "', FOTOINEATRAS='" + fotoIneAtrasRuta
                    + "', FOTOCASA='" + fotoCasaRuta + "', COMPROBANTEDOMICILIO='" + fotoComprobanteDomicilioRuta
                    + "', PAGARE='" + fotoPagareRuta + "', FOTOOTROS='" + fotoOtrosRuta
                    + "', ID_OPTOMETRISTA='" + idsOptometristas[spOptometrista.getSelectedItemPosition()]
                    + "', ENVIADOPAGINA='" + "0"
                    + "', ALIAS='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etAliasCliente.getText().toString()))
                    + "', CALLEENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCalleClienteEntrega.getText().toString()))
                    + "', NUMEROENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etNumeroClienteEntrega.getText().toString())) + "', DEPTOENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etDepartamentoClienteEntrega.getText().toString()))
                    + "', ALLADODEENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etAlLadoDeClienteEntrega.getText().toString())) + "', FRENTEAENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etFrenteAClienteEntrega.getText().toString()))
                    + "', ENTRECALLESENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etEntreCallesClienteEntrega.getText().toString())) + "', COLONIAENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etColoniaClienteEntrega.getText().toString()))
                    + "', LOCALIDADENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etLocalidadClienteEntrega.getText().toString()))
                    + "', CASATIPOENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etTipoCasaClienteEntrega.getText().toString())) + "', CASACOLORENTREGA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etCasaColorClienteEntrega.getText().toString()))
                    + "', OPCIONLUGARENTREGA='" + idsOpcionesLugarEntrega[spLugarEntrega.getSelectedItemPosition()]
                    + "', OBSERVACIONFOTOINE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoINEFrente.getText().toString())) + "', OBSERVACIONFOTOINEATRAS='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoINEAtras.getText().toString()))
                    + "', OBSERVACIONFOTOCASA='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoCasa.getText().toString())) + "', OBSERVACIONCOMPROBANTEDOMICILIO='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoComprobanteDomicilio.getText().toString()))
                    + "', OBSERVACIONPAGARE='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionFotoPagare.getText().toString())) + "', OBSERVACIONFOTOOTROS='" + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionOtraFoto.getText().toString()))
                    + "', UPDATED_AT='" + fechaActual + " " + time
                    + "' WHERE ID_CONTRATO='" + idContratoHijo + "'";

            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 307);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    public void actualizarHistorialClinicoContrato(){

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

            String consulta = "UPDATE HISTORIALCLINICO SET EDAD='" + global.replaceCadena(etEdadNuevoHistorial.getText().toString()) + "', DIAGNOSTICO='" + global.replaceCadena(etDiagnosticoNuevoHistorial.getText().toString())
                    + "', OCUPACION='" + global.replaceCadena(etOcupacionNuevoHistorial.getText().toString()) + "', DIABETES='" + global.replaceCadena(etDiabetesNuevoHistorial.getText().toString())
                    + "', HIPERTENSION='" + global.replaceCadena(etHipertensionNuevoHistorial.getText().toString()) + "', EMBARAZADA='" + global.replaceCadena(etEmbarazadaNuevoHistorial.getText().toString())
                    + "', DURMIOSEISOCHOHORAS='" + global.replaceCadena(etDurmioSeisOchoHorasNuevoHistorial.getText().toString()) + "', ACTIVIDADDIA='" + global.replaceCadena(etActividadDiaNuevoHistorial.getText().toString())
                    + "', PROBLEMASOJOS='" + global.replaceCadena(etProblemasOjosNuevoHistorial.getText().toString())
                    + "', DOLOR='" + (cbDolorCabeza.isChecked() ? "1" : "0") + "', ARDOR='" + (cbArdorOjos.isChecked() ? "1" : "0") + "', GOLPEOJOS='"+ (cbGolpeCabeza.isChecked() ? "1" : "0")
                    + "', OTROM='" + (cbOtroMolestia.isChecked() ? "1" : "0") + "', MOLESTIAOTRO='" + global.replaceCadena(etOtroMoletiaNuevoHistorial.getText().toString())
                    + "', ULTIMOEXAMEN='" + etUltimoExamenNuevoHistorial.getText().toString()
                    + "' WHERE ID_CONTRATO='" + idContratoHijo + "'";

            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 308);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    public void pintarIconosImagenesSinCargarContratos(String idContrato){

        //Declarar e ingresar elementos a arreglo para validar icnos fotos
        ImageView[] imagenesContrato = new ImageView[6];
        imagenesContrato[0] = ivFotoINEFrente;
        imagenesContrato[1] = ivFotoINEAtras;
        imagenesContrato[2] = ivFotoPagare;
        imagenesContrato[3] = ivFotoComprobanteDomicilio;
        imagenesContrato[4] = ivFotoCasa;
        imagenesContrato[5] = ivFotoOtros;

        String[] nombreImagenContrato = {"FOTOINEFRENTE", "FOTOINEATRAS","PAGARE", "COMPROBANTEDOMICILIO", "FOTOCASA", "FOTOOTROS"};
        int indiceNombreImagen = 0;

        for (ImageView imagenContrato : imagenesContrato){

            if(imagenContrato.getDrawable() == null){
                //Imagen es vacio - Verificar que color pertenece a icono
                if(global.obtenerAtributoTabla("IMAGENESSERVIDORCONTRATOS",nombreImagenContrato[indiceNombreImagen],"ID_CONTRATO",idContrato).equals("0")){
                    //Imagen no existe en servidor y no fue encontrada en el movil
                    imagenContrato.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F48A8A")));
                }else if (global.obtenerAtributoTabla("IMAGENESSERVIDORCONTRATOS",nombreImagenContrato[indiceNombreImagen],"ID_CONTRATO",idContrato).equals("1")){
                    //Imagen en el servidor pero no en el movil
                    imagenContrato.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0AA09E")));
                }
            }

            indiceNombreImagen++;
        }
    }

    public boolean validarArmazonSeleccionado() {
        //Seleccionaste una opcion distinta a la de seleccionar
        if(spProductoNuevoHistorial.getSelectedItem().toString() != "Seleccionar") {

            //Optener armazon seleccionado
            String producto[] = spProductoNuevoHistorial.getSelectedItem().toString().split("\\|");
            String nombreProducto = producto[0].trim(); //Eliminar espacios en blanco

            if(nombreProducto.toUpperCase().contains("PROPIO")){
                //Seleccionaste armazon propio
                llFotoArmazonPropio.setVisibility(View.VISIBLE);
                //Bandera de armazon propio verdadero
                esArmazonPropio = true;
                return true;
            }else {
                //Armazon distinta de propio
                llFotoArmazonPropio.setVisibility(View.GONE);
                ivFotoArmazonPropio.setImageDrawable(null);
                //Bandera de armazon propio falso
                esArmazonPropio = false;
            }
        }else{
            llFotoArmazonPropio.setVisibility(View.GONE);
            ivFotoArmazonPropio.setImageDrawable(null);
            //Bandera de armazon propio falso
            esArmazonPropio = false;
        }

        //Cargar imagen icono de armazon propio
        ivFotoArmazonPropio.setImageDrawable(null);
        ivFotoArmazonPropio.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.armazonpropio));
        return false;
    }
}
