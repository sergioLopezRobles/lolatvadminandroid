package com.luzatuvida.lolatvadminandroid.vista;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luzatuvida.lolatvadminandroid.global.Camara;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Teclado;
import com.luzatuvida.lolatvadminandroid.nuevoContrato.Controlador;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nuevoContrato#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nuevoContrato extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Variables
    boolean blueRayActivo = false;
    boolean[] banderaSigno;

    //Declaración de componentes
    ScrollView svNuevoContratoPrincipal;
    ImageView ivFotoINEFrente, ivFotoINEAtras, ivFotoCasa, ivFotoComprobanteDomicilio, ivFotoPagare, ivFotoOtros, ivFotoArmazonPropio;
    EditText etNombreCliente, etCalleCliente, etNumeroCliente, etDepartamentoCliente, etAlLadoDeCliente, etFrenteACliente, etEntreCallesCliente, etColoniaCliente,
                etLocalidadCliente, etTelefonoCliente, etTelefonoReferenciaCliente, etNombreReferenciaCliente, etTipoCasaCliente, etCasaColorCliente, etCorreoCliente, etAliasCliente;
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
    RadioButton rbHiIndex, rbCR, rbPolicarbonato, rbOtroMaterial, rbFT, rbBlend, rbProgresivo, rbNA, rbOtroBifocal;
    CheckBox cbDolorCabeza, cbArdorOjos, cbGolpeCabeza, cbOtroMolestia, cbTratamientoFotocromatico, cbTratamientoAR,
                cbTratamientoTinte, cbTratamientoBlueray, cbTratamientoOtro, cbDuplicarDocumentos, cbTratamientoPolarizado, cbPolicarbonatoTipo, cbTratamientoEspejeado;
    LinearLayout llDuplicarDocumentos, llVisionLejana, llFotos, llDatosSinConversionNuevoHistorial, llVisionLejanaSinConversion, llColorEstiloTinte, llTratamientoPolarizado,
                 llTratamientoEspejeado, llPrincipalNuevoContrato, llPrincipalHistorialClinico, llFotoArmazonPropio;
    Button btnGuardarContratoNuevo, btnCancelarContratoNuevo;
    Spinner spZona, spOptometrista, spProductoNuevoHistorial, spPaquetesNuevoHistorial, spColoniaClienteEntrega, spColorTratamiento, spEstiloTratamiento, spColorTratamientoPolarizado,
            spColorTratamientoEspejeado, spLugarEntrega;
    TextView tvZona, tvOptometrista, tvNombreCliente, tvCalleCliente, tvNumeroCliente, tvDepartamentoCliente, tvAlLadoDeCliente, tvFrenteACliente, tvEntreCallesCliente, tvColoniaCliente,
            tvLocalidadCliente, tvTelefonoCliente, tvTelefonoReferenciaCliente, tvNombreReferenciaCliente, tvTipoCasaCliente, tvCasaColorCliente, tvFotoINEFrente, tvFotoINEAtras, tvFotoCasa,
            tvFotoComprobanteDomicilio, tvCorreoCliente, tvFotoPagare, tvAliasCliente, tvOtraFoto;
    TextView tvEdadNuevoHistorial, tvDiagnosticoNuevoHistorial, tvOcupacionNuevoHistorial, tvDiabetesNuevoHistorial, tvHipertensionNuevoHistorial,
                tvOtroMoletiaNuevoHistorial, tvProductoNuevoHistorial, tvPaquetesNuevoHistorial, tvFechaEntregaNuevoHistorial, tvOjoDerechoEsfericoNuevoHistorial, tvOjoDerechoCilindroNuevoHistorial,
                tvOjoDerechoEjeNuevoHistorial, tvOjoDerechoAddNuevoHistorial, tvOjoDerechoALTNuevoHistorial, tvOjoIzquierdoEsfericoNuevoHistorial,
                tvOjoIzquierdoCilindroNuevoHistorial, tvOjoIzquierdoEjeNuevoHistorial, tvOjoIzquierdoAddNuevoHistorial, tvOjoIzquierdoALTNuevoHistorial,
                tvOtroMaterialNuevoHistorial, tvPrecioMaterialNuevoHistorial, tvOtroTratamientoNuevoHistorial, tvPrecioTratamientoNuevoHistorial, tvTipoBifocalNuevoHistorial,
                tvGeneralSinConversionNuevoHistorial, tvEmbarazadaNuevoHistorial, tvDurmioSeisOchoHorasNuevoHistorial, tvActividadDiaNuevoHistorial, tvProblemasOjosNuevoHistorial,
                tvOtroBifocalNuevoHistorial, tvPrecioBifocalNuevoHistorial;
    AutoCompleteTextView actvContratosLiosFugas;

    EditText etCalleClienteEntrega, etNumeroClienteEntrega, etDepartamentoClienteEntrega, etAlLadoDeClienteEntrega, etFrenteAClienteEntrega, etEntreCallesClienteEntrega, etColoniaClienteEntrega,
            etLocalidadClienteEntrega, etTipoCasaClienteEntrega, etCasaColorClienteEntrega;
    TextView tvCalleClienteEntrega, tvNumeroClienteEntrega, tvDepartamentoClienteEntrega, tvAlLadoDeClienteEntrega, tvFrenteAClienteEntrega, tvEntreCallesClienteEntrega, tvColoniaClienteEntrega,
            tvLocalidadClienteEntrega, tvTipoCasaClienteEntrega, tvCasaColorClienteEntrega, btnCopiarDatosLugarVenta, tvColorTratamiento, tvEstiloTratamiento, tvColorTratamientoPolarizado,
            tvColorTratamientoEspejeado, tvLugarEntrega, tvFotoArmazonPropio;

    //Declaración de clases
    Camara camara;
    Controlador controlador;
    Teclado teclado;

    String idContratoPadre = "";
    String idContratoHijo = "";
    boolean contratoPromocion = false;
    boolean contratoActualizar = false;
    boolean validarTratamientosContrato = true;
    Global global;

    Fragment fragmento;

    //Encabezados para configuracion de diseño
    TextView tvContratoEncabezado, tvHistorialClinicoEncabezado, tvProductoEncabezado, tvSinConversionEncabezado, tvOjoDerechoSCEncabezado, tvOjoIzquierdoSCEncabezado, tvConConversionEncabezado,
            tvSCVisionLejana, tvCCVisionLejana, tvOjoDerechoCCEncabezado, tvOjoIzquierdoCCEncabezado, tvMaterialEncabezado, tvTratamientoEncabezado, tvTipoBifocalEncabezado;

    EditText etFiltrarArmazones, etFiltrarColoniasLugarEntrega, etObservacionFotoINEFrente, etObservacionFotoINEAtras, etObservacionFotoPagare, etObservacionFotoComprobanteDomicilio,
                etObservacionFotoCasa, etObservacionOtraFoto;
    TextView btnFiltrarArmazones, btnLimpiarFiltroArmazones, btnFiltrarColoniasLugarEntrega, btnLimpiarFiltroColoniasLugarEntrega;
    RadioGroup rgTipoBifocalNuevoHistorial;

    public nuevoContrato() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nuevoContrato.
     */
    // TODO: Rename and change types and number of parameters
    public static nuevoContrato newInstance(String param1, String param2) {
        nuevoContrato fragment = new nuevoContrato();
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
        Identificador error del 237 al 242

        Throwable identificador error 4
        */

        fragmento = nuevoContrato.this;
        global = new Global(fragmento);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                global.escribirErrorClasePrincipalThrowable(throwable, 4);
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

        View view = inflater.inflate(R.layout.fragment_nuevo_contrato, container, false);

        //Inicialización de Componentes
        //COMPONENTES DE CONTRATO
        //ImageViews
        ivFotoINEFrente = view.findViewById(R.id.ivFotoINEFrente);
        ivFotoINEAtras = view.findViewById(R.id.ivFotoINEAtras);
        ivFotoCasa = view.findViewById(R.id.ivFotoCasa);
        ivFotoComprobanteDomicilio = view.findViewById(R.id.ivFotoComprobanteDomicilio);
        ivFotoPagare = view.findViewById(R.id.ivFotoPagare);
        ivFotoOtros = view.findViewById(R.id.ivOtraFoto);
        ivFotoArmazonPropio = view.findViewById(R.id.ivFotoArmazonPropio);
        //Spinners
        spZona = view.findViewById(R.id.spZona);
        spOptometrista = view.findViewById(R.id.spOptometrista);
        spColoniaClienteEntrega = view.findViewById(R.id.spColoniaClienteEntrega);
        //EditTexts
        etNombreCliente = view.findViewById(R.id.etNombreCliente);
        etAliasCliente = view.findViewById(R.id.etAliasCliente);
        etCalleCliente = view.findViewById(R.id.etCalleCliente);
        etNumeroCliente = view.findViewById(R.id.etNumeroCliente);
        etDepartamentoCliente = view.findViewById(R.id.etDepartamentoCliente);
        etAlLadoDeCliente = view.findViewById(R.id.etAlLadoDeCliente);
        etFrenteACliente = view.findViewById(R.id.etFrenteACliente);
        etEntreCallesCliente = view.findViewById(R.id.etEntreCallesCliente);
        etColoniaCliente = view.findViewById(R.id.etColoniaCliente);
        etLocalidadCliente = view.findViewById(R.id.etLocalidadCliente);
        etTelefonoCliente = view.findViewById(R.id.etTelefonoCliente);
        etTelefonoReferenciaCliente = view.findViewById(R.id.etTelefonoReferenciaCliente);
        etNombreReferenciaCliente = view.findViewById(R.id.etNombreReferenciaCliente);
        etTipoCasaCliente = view.findViewById(R.id.etTipoCasaCliente);
        etCasaColorCliente = view.findViewById(R.id.etCasaColorCliente);

        etCalleClienteEntrega = view.findViewById(R.id.etCalleClienteEntrega);
        etNumeroClienteEntrega = view.findViewById(R.id.etNumeroClienteEntrega);
        etDepartamentoClienteEntrega = view.findViewById(R.id.etDepartamentoClienteEntrega);
        etAlLadoDeClienteEntrega = view.findViewById(R.id.etAlLadoDeClienteEntrega);
        etFrenteAClienteEntrega = view.findViewById(R.id.etFrenteAClienteEntrega);
        etEntreCallesClienteEntrega = view.findViewById(R.id.etEntreCallesClienteEntrega);
        etColoniaClienteEntrega = view.findViewById(R.id.etColoniaClienteEntrega);
        etLocalidadClienteEntrega = view.findViewById(R.id.etLocalidadClienteEntrega);
        etTipoCasaClienteEntrega = view.findViewById(R.id.etTipoCasaClienteEntrega);
        etCasaColorClienteEntrega = view.findViewById(R.id.etCasaColorClienteEntrega);
        //TextView
        tvZona = view.findViewById(R.id.tvZona);
        tvOptometrista = view.findViewById(R.id.tvOptometrista);
        tvNombreCliente = view.findViewById(R.id.tvNombreCliente);
        tvAliasCliente = view.findViewById(R.id.tvAliasCliente);
        tvCalleCliente = view.findViewById(R.id.tvCalleCliente);
        tvNumeroCliente = view.findViewById(R.id.tvNumeroCliente);
        tvDepartamentoCliente = view.findViewById(R.id.tvDepartamentoCliente);
        tvAlLadoDeCliente = view.findViewById(R.id.tvAlLadoDeCliente);
        tvFrenteACliente = view.findViewById(R.id.tvFrenteACliente);
        tvEntreCallesCliente = view.findViewById(R.id.tvEntreCallesCliente);
        tvColoniaCliente = view.findViewById(R.id.tvColoniaCliente);
        tvLocalidadCliente = view.findViewById(R.id.tvLocalidadCliente);
        tvTelefonoCliente = view.findViewById(R.id.tvTelefonoCliente);
        tvTelefonoReferenciaCliente = view.findViewById(R.id.tvTelefonoReferenciaCliente);
        tvNombreReferenciaCliente = view.findViewById(R.id.tvNombreReferenciaCliente);
        tvTipoCasaCliente = view.findViewById(R.id.tvTipoCasaCliente);
        tvCasaColorCliente = view.findViewById(R.id.tvCasaColorCliente);
        tvFotoINEFrente = view.findViewById(R.id.tvFotoINEFrente);
        tvFotoINEAtras = view.findViewById(R.id.tvFotoINEAtras);
        tvFotoCasa = view.findViewById(R.id.tvFotoCasa);
        tvFotoComprobanteDomicilio = view.findViewById(R.id.tvFotoComprobanteDomicilio);
        tvFotoPagare = view.findViewById(R.id.tvFotoPagare);
        tvOtraFoto = view.findViewById(R.id.tvOtraFoto);

        tvCalleClienteEntrega = view.findViewById(R.id.tvCalleClienteEntrega);
        tvNumeroClienteEntrega = view.findViewById(R.id.tvNumeroClienteEntrega);
        tvDepartamentoClienteEntrega = view.findViewById(R.id.tvDepartamentoClienteEntrega);
        tvAlLadoDeClienteEntrega = view.findViewById(R.id.tvAlLadoDeClienteEntrega);
        tvFrenteAClienteEntrega = view.findViewById(R.id.tvFrenteAClienteEntrega);
        tvEntreCallesClienteEntrega = view.findViewById(R.id.tvEntreCallesClienteEntrega);
        tvColoniaClienteEntrega = view.findViewById(R.id.tvColoniaClienteEntrega);
        tvLocalidadClienteEntrega = view.findViewById(R.id.tvLocalidadClienteEntrega);
        tvTipoCasaClienteEntrega = view.findViewById(R.id.tvTipoCasaClienteEntrega);
        tvCasaColorClienteEntrega = view.findViewById(R.id.tvCasaColorClienteEntrega);
        tvLugarEntrega = view.findViewById(R.id.tvLugarEntrega);
        //Buttons
        btnGuardarContratoNuevo = view.findViewById(R.id.btnGuardarContratoNuevo);
        btnCancelarContratoNuevo = view.findViewById(R.id.btnCancelarContratoNuevo);
        btnCopiarDatosLugarVenta = view.findViewById(R.id.btnCopiarDatosLugarVenta);
        btnFiltrarArmazones = view.findViewById(R.id.btnFiltrarArmazones);
        btnLimpiarFiltroArmazones = view.findViewById(R.id.btnLimpiarFiltroArmazones);
        btnFiltrarColoniasLugarEntrega = view.findViewById(R.id.btnFiltrarColoniasLugarEntrega);
        btnLimpiarFiltroColoniasLugarEntrega = view.findViewById(R.id.btnLimpiarFiltroColoniasLugarEntrega);

        //COMPONENTES DE HISTORIAL
        //EditTexts
        etEdadNuevoHistorial = view.findViewById(R.id.etEdadNuevoHistorial);
        etDiagnosticoNuevoHistorial = view.findViewById(R.id.etDiagnosticoNuevoHistorial);
        etOcupacionNuevoHistorial = view.findViewById(R.id.etOcupacionNuevoHistorial);
        etDiabetesNuevoHistorial = view.findViewById(R.id.etDiabetesNuevoHistorial);
        etHipertensionNuevoHistorial = view.findViewById(R.id.etHipertensionNuevoHistorial);
        etOtroMoletiaNuevoHistorial = view.findViewById(R.id.etOtroMoletiaNuevoHistorial);
        etUltimoExamenNuevoHistorial = view.findViewById(R.id.etUltimoExamenNuevoHistorial);
        etFechaEntregaNuevoHistorial = view.findViewById(R.id.etFechaEntregaNuevoHistorial);
        etOjoDerechoEsfericoNuevoHistorial = view.findViewById(R.id.etOjoDerechoEsfericoNuevoHistorial);
        etOjoDerechoCilindroNuevoHistorial = view.findViewById(R.id.etOjoDerechoCilindroNuevoHistorial);
        etOjoDerechoEjeNuevoHistorial = view.findViewById(R.id.etOjoDerechoEjeNuevoHistorial);
        etOjoDerechoAddNuevoHistorial = view.findViewById(R.id.etOjoDerechoAddNuevoHistorial);
        etOjoDerechoALTNuevoHistorial = view.findViewById(R.id.etOjoDerechoALTNuevoHistorial);
        etOjoIzquierdoEsfericoNuevoHistorial = view.findViewById(R.id.etOjoIzquierdoEsfericoNuevoHistorial);
        etOjoIzquierdoCilindroNuevoHistorial = view.findViewById(R.id.etOjoIzquierdoCilindroNuevoHistorial);
        etOjoIzquierdoEjeNuevoHistorial = view.findViewById(R.id.etOjoIzquierdoEjeNuevoHistorial);
        etOjoIzquierdoAddNuevoHistorial = view.findViewById(R.id.etOjoIzquierdoAddNuevoHistorial);
        etOjoIzquierdoALTNuevoHistorial = view.findViewById(R.id.etOjoIzquierdoALTNuevoHistorial);
        etOtroMaterialNuevoHistorial = view.findViewById(R.id.etOtroMaterialNuevoHistorial);
        etPrecioMaterialNuevoHistorial = view.findViewById(R.id.etPrecioMaterialNuevoHistorial);
        etOtroTratamientoNuevoHistorial = view.findViewById(R.id.etOtroTratamientoNuevoHistorial);
        etPrecioTratamientoNuevoHistorial = view.findViewById(R.id.etPrecioTratamientoNuevoHistorial);
        etObservacionesNuevoHistorial = view.findViewById(R.id.etObservacionesNuevoHistorial);
        etObservacionesInternoNuevoHistorial = view.findViewById(R.id.etObservacionesInternoNuevoHistorial);
        etEmbarazadaNuevoHistorial = view.findViewById(R.id.etEmbarazadaNuevoHistorial);
        etDurmioSeisOchoHorasNuevoHistorial = view.findViewById(R.id.etDurmioSeisOchoHorasNuevoHistorial);
        etActividadDiaNuevoHistorial = view.findViewById(R.id.etActividadDiaNuevoHistorial);
        etProblemasOjosNuevoHistorial = view.findViewById(R.id.etProblemasOjosNuevoHistorial);
        etOtroBifocalNuevoHistorial = view.findViewById(R.id.etOtroBifocalNuevoHistorial);
        etPrecioBifocalNuevoHistorial = view.findViewById(R.id.etPrecioBifocalNuevoHistorial);
        etFiltrarArmazones = view.findViewById(R.id.etFiltrarArmazones);
        etFiltrarColoniasLugarEntrega = view.findViewById(R.id.etFiltrarColoniasLugarEntrega);
        //LinearLayout
        llPrincipalHistorialClinico = view.findViewById(R.id.llPrincipalHistorialClinico);
        llFotoArmazonPropio = view.findViewById(R.id.llFotoArmazonPropio);
        //RadioButtons
        rgTipoBifocalNuevoHistorial = view.findViewById(R.id.rgTipoBifocalNuevoHistorial);
        rbHiIndex = view.findViewById(R.id.rbHiIndex);
        rbCR = view.findViewById(R.id.rbCR);
        rbPolicarbonato = view.findViewById(R.id.rbPolicarbonato);
        rbOtroMaterial = view.findViewById(R.id.rbOtroMaterial);
        rbFT = view.findViewById(R.id.rbFT);
        rbBlend = view.findViewById(R.id.rbBlend);
        rbProgresivo = view.findViewById(R.id.rbProgresivo);
        rbNA = view.findViewById(R.id.rbNA);
        rbOtroBifocal = view.findViewById(R.id.rbOtroBifocal);
        //CheckBox
        cbDolorCabeza = view.findViewById(R.id.cbDolorCabeza);
        cbArdorOjos = view.findViewById(R.id.cbArdorOjos);
        cbGolpeCabeza = view.findViewById(R.id.cbGolpeCabeza);
        cbOtroMolestia = view.findViewById(R.id.cbOtroMolestia);
        cbTratamientoFotocromatico = view.findViewById(R.id.cbTratamientoFotocromatico);
        cbTratamientoAR = view.findViewById(R.id.cbTratamientoAR);
        cbTratamientoTinte = view.findViewById(R.id.cbTratamientoTinte);
        cbTratamientoBlueray = view.findViewById(R.id.cbTratamientoBlueray);
        cbTratamientoOtro = view.findViewById(R.id.cbTratamientoOtro);
        cbTratamientoPolarizado = view.findViewById(R.id.cbTratamientoPolarizado);
        cbPolicarbonatoTipo = view.findViewById(R.id.cbPolicarbonatoTipo);
        cbTratamientoEspejeado = view.findViewById(R.id.cbTratamientoEspejeado);
        //Spinners
        spProductoNuevoHistorial = view.findViewById(R.id.spProductoNuevoHistorial);
        spPaquetesNuevoHistorial = view.findViewById(R.id.spPaquetesNuevoHistorial);
        spColorTratamiento = view.findViewById(R.id.spColorTratamiento);
        spEstiloTratamiento = view.findViewById(R.id.spEstiloTratamiento);
        spColorTratamientoPolarizado = view.findViewById(R.id.spColorTratamientoPolarizado);
        spColorTratamientoEspejeado = view.findViewById(R.id.spColorTratamientoEspejeado);
        spLugarEntrega = view.findViewById(R.id.spLugarEntrega);
        //LinearLayout tratamiento tinte
        llColorEstiloTinte = view.findViewById(R.id.llColorEstiloTinte);
        llTratamientoPolarizado = view.findViewById(R.id.llTratamientoPolarizado);
        llTratamientoEspejeado = view.findViewById(R.id.llTratamientoEspejeado);
        llPrincipalNuevoContrato = view.findViewById(R.id.llPrincipalNuevoContrato);
        //TextView
        tvEdadNuevoHistorial = view.findViewById(R.id.tvEdadNuevoHistorial);
        tvDiagnosticoNuevoHistorial = view.findViewById(R.id.tvDiagnosticoNuevoHistorial);
        tvOcupacionNuevoHistorial = view.findViewById(R.id.tvOcupacionNuevoHistorial);
        tvDiabetesNuevoHistorial = view.findViewById(R.id.tvDiabetesNuevoHistorial);
        tvHipertensionNuevoHistorial = view.findViewById(R.id.tvHipertensionNuevoHistorial);
        tvOtroMoletiaNuevoHistorial = view.findViewById(R.id.tvOtroMoletiaNuevoHistorial);
        tvProductoNuevoHistorial = view.findViewById(R.id.tvProductoNuevoHistorial);
        tvPaquetesNuevoHistorial = view.findViewById(R.id.tvPaquetesNuevoHistorial);
        tvFechaEntregaNuevoHistorial = view.findViewById(R.id.tvFechaEntregaNuevoHistorial);
        tvOjoDerechoEsfericoNuevoHistorial = view.findViewById(R.id.tvOjoDerechoEsfericoNuevoHistorial);
        tvOjoDerechoCilindroNuevoHistorial = view.findViewById(R.id.tvOjoDerechoCilindroNuevoHistorial);
        tvOjoDerechoEjeNuevoHistorial = view.findViewById(R.id.tvOjoDerechoEjeNuevoHistorial);
        tvOjoDerechoAddNuevoHistorial = view.findViewById(R.id.tvOjoDerechoAddNuevoHistorial);
        tvOjoDerechoALTNuevoHistorial = view.findViewById(R.id.tvOjoDerechoALTNuevoHistorial);
        tvOjoIzquierdoEsfericoNuevoHistorial = view.findViewById(R.id.tvOjoIzquierdoEsfericoNuevoHistorial);
        tvOjoIzquierdoCilindroNuevoHistorial = view.findViewById(R.id.tvOjoIzquierdoCilindroNuevoHistorial);
        tvOjoIzquierdoEjeNuevoHistorial = view.findViewById(R.id.tvOjoIzquierdoEjeNuevoHistorial);
        tvOjoIzquierdoAddNuevoHistorial = view.findViewById(R.id.tvOjoIzquierdoAddNuevoHistorial);
        tvOjoIzquierdoALTNuevoHistorial = view.findViewById(R.id.tvOjoIzquierdoALTNuevoHistorial);
        tvOtroMaterialNuevoHistorial = view.findViewById(R.id.tvOtroMaterialNuevoHistorial);
        tvPrecioMaterialNuevoHistorial = view.findViewById(R.id.tvPrecioMaterialNuevoHistorial);
        tvOtroTratamientoNuevoHistorial = view.findViewById(R.id.tvOtroTratamientoNuevoHistorial);
        tvPrecioTratamientoNuevoHistorial = view.findViewById(R.id.tvPrecioTratamientoNuevoHistorial);
        tvTipoBifocalNuevoHistorial = view.findViewById(R.id.tvTipoBifocalNuevoHistorial);
        tvEmbarazadaNuevoHistorial = view.findViewById(R.id.tvEmbarazadaNuevoHistorial);
        tvDurmioSeisOchoHorasNuevoHistorial = view.findViewById(R.id.tvDurmioSeisOchoHorasNuevoHistorial);
        tvActividadDiaNuevoHistorial = view.findViewById(R.id.tvActividadDiaNuevoHistorial);
        tvProblemasOjosNuevoHistorial = view.findViewById(R.id.tvProblemasOjosNuevoHistorial);
        tvOtroBifocalNuevoHistorial = view.findViewById(R.id.tvOtroBifocalNuevoHistorial);
        tvPrecioBifocalNuevoHistorial = view.findViewById(R.id.tvPrecioBifocalNuevoHistorial);
        tvColorTratamiento = view.findViewById(R.id.tvColorTratamiento);
        tvEstiloTratamiento = view.findViewById(R.id.tvEstiloTratamiento);
        tvColorTratamientoPolarizado = view.findViewById(R.id.tvColorTratamientoPolarizado);
        tvColorTratamientoEspejeado = view.findViewById(R.id.tvColorTratamientoEspejeado);
        tvFotoArmazonPropio = view.findViewById(R.id.tvFotoArmazonPropio);

        llDuplicarDocumentos = view.findViewById(R.id.llDuplicarDocumentos);
        llVisionLejana = view.findViewById(R.id.llVisionLejana);
        cbDuplicarDocumentos = view.findViewById(R.id.cbDuplicarDocumentos);

        etCorreoCliente = view.findViewById(R.id.etCorreoCliente);
        tvCorreoCliente = view.findViewById(R.id.tvCorreoCliente);

        llFotos = view.findViewById(R.id.llFotos);
        svNuevoContratoPrincipal = view.findViewById(R.id.svNuevoContratoPrincipal);

        actvContratosLiosFugas = view.findViewById(R.id.actvContratosLiosFugas);

        llDatosSinConversionNuevoHistorial = view.findViewById(R.id.llDatosSinConversionNuevoHistorial);
        llVisionLejanaSinConversion = view.findViewById(R.id.llVisionLejanaSinConversion);
        etOjoDerechoEsfericoSinConversionNuevoHistorial = view.findViewById(R.id.etOjoDerechoEsfericoSinConversionNuevoHistorial);
        etOjoDerechoCilindroSinConversionNuevoHistorial = view.findViewById(R.id.etOjoDerechoCilindroSinConversionNuevoHistorial);
        etOjoDerechoEjeSinConversionNuevoHistorial = view.findViewById(R.id.etOjoDerechoEjeSinConversionNuevoHistorial);
        etOjoDerechoAddSinConversionNuevoHistorial = view.findViewById(R.id.etOjoDerechoAddSinConversionNuevoHistorial);
        etOjoIzquierdoEsfericoSinConversionNuevoHistorial = view.findViewById(R.id.etOjoIzquierdoEsfericoSinConversionNuevoHistorial);
        etOjoIzquierdoCilindroSinConversionNuevoHistorial = view.findViewById(R.id.etOjoIzquierdoCilindroSinConversionNuevoHistorial);
        etOjoIzquierdoEjeSinConversionNuevoHistorial = view.findViewById(R.id.etOjoIzquierdoEjeSinConversionNuevoHistorial);
        etOjoIzquierdoAddSinConversionNuevoHistorial = view.findViewById(R.id.etOjoIzquierdoAddSinConversionNuevoHistorial);
        tvGeneralSinConversionNuevoHistorial = view.findViewById(R.id.tvGeneralSinConversionNuevoHistorial);

        //ENCABEZADOS DE NUEVO CONTRATO
        //TextView
        tvContratoEncabezado = view.findViewById(R.id.tvContratoEncabezado);
        tvHistorialClinicoEncabezado = view.findViewById(R.id.tvHistorialClinicoEncabezado);
        tvProductoEncabezado = view.findViewById(R.id.tvProductoEncabezado);
        tvSinConversionEncabezado = view.findViewById(R.id.tvSinConversionEncabezado);
        tvOjoDerechoSCEncabezado = view.findViewById(R.id.tvOjoDerechoSCEncabezado);
        tvOjoIzquierdoSCEncabezado = view.findViewById(R.id.tvOjoIzquierdoSCEncabezado);
        tvConConversionEncabezado = view.findViewById(R.id.tvConConversionEncabezado);
        tvSCVisionLejana = view.findViewById(R.id.tvSCVisionLejana);
        tvCCVisionLejana = view.findViewById(R.id.tvCCVisionLejana);
        tvOjoDerechoCCEncabezado = view.findViewById(R.id.tvOjoDerechoCCEncabezado);
        tvOjoIzquierdoCCEncabezado = view.findViewById(R.id.tvOjoIzquierdoCCEncabezado);
        tvMaterialEncabezado = view.findViewById(R.id.tvMaterialEncabezado);
        tvTratamientoEncabezado = view.findViewById(R.id.tvTratamientoEncabezado);
        tvTipoBifocalEncabezado = view.findViewById(R.id.tvTipoBifocalEncabezado);


        etObservacionFotoINEFrente = view.findViewById(R.id.etObservacionFotoINEFrente);
        etObservacionFotoINEAtras = view.findViewById(R.id.etObservacionFotoINEAtras);
        etObservacionFotoPagare = view.findViewById(R.id.etObservacionFotoPagare);
        etObservacionFotoComprobanteDomicilio = view.findViewById(R.id.etObservacionFotoComprobanteDomicilio);
        etObservacionFotoCasa = view.findViewById(R.id.etObservacionFotoCasa);
        etObservacionOtraFoto = view.findViewById(R.id.etObservacionOtraFoto);

        banderaSigno = new boolean[6];
        Arrays.fill(banderaSigno, Boolean.FALSE);

        Bundle bundle = getArguments();
        if(bundle != null){
            if(bundle.getString("idContratoPadre").length() > 0) {
                idContratoPadre = bundle.getString("idContratoPadre");
            }
            if(bundle.getString("idContratoHijo").length() > 0) {
                idContratoHijo = bundle.getString("idContratoHijo");
            }
            contratoPromocion = bundle.getBoolean("contratoPromocion");
            contratoActualizar = bundle.getBoolean("contratoActualizar");
            validarTratamientosContrato = bundle.getBoolean("validarTratamientosContrato");
        }

        Object[] objetos = {spZona, spOptometrista, etNombreCliente,
                etCalleCliente, etNumeroCliente, etDepartamentoCliente, etAlLadoDeCliente, etFrenteACliente, etEntreCallesCliente,
                etColoniaCliente, etLocalidadCliente, etTelefonoCliente, etTelefonoReferenciaCliente, etNombreReferenciaCliente,
                etTipoCasaCliente, etCasaColorCliente, ivFotoINEFrente, ivFotoINEAtras, ivFotoCasa, ivFotoComprobanteDomicilio,
                etEdadNuevoHistorial, etDiagnosticoNuevoHistorial, etOcupacionNuevoHistorial, etDiabetesNuevoHistorial,
                etHipertensionNuevoHistorial, cbDolorCabeza, cbArdorOjos, cbGolpeCabeza, cbOtroMolestia, etOtroMoletiaNuevoHistorial,
                etUltimoExamenNuevoHistorial, spProductoNuevoHistorial, spPaquetesNuevoHistorial, etFechaEntregaNuevoHistorial,
                etOjoDerechoEsfericoNuevoHistorial, etOjoDerechoCilindroNuevoHistorial, etOjoDerechoEjeNuevoHistorial, etOjoDerechoAddNuevoHistorial,
                etOjoDerechoALTNuevoHistorial, etOjoIzquierdoEsfericoNuevoHistorial, etOjoIzquierdoCilindroNuevoHistorial,
                etOjoIzquierdoEjeNuevoHistorial, etOjoIzquierdoAddNuevoHistorial, etOjoIzquierdoALTNuevoHistorial,
                rbHiIndex, rbCR, rbPolicarbonato, rbOtroMaterial, etOtroMaterialNuevoHistorial, etPrecioMaterialNuevoHistorial, cbTratamientoFotocromatico,
                cbTratamientoAR, cbTratamientoTinte, cbTratamientoBlueray, cbTratamientoOtro, etOtroTratamientoNuevoHistorial,
                etPrecioTratamientoNuevoHistorial, rbFT, rbBlend, rbProgresivo, rbNA, etObservacionesNuevoHistorial, tvZona, tvOptometrista, tvNombreCliente,
                tvCalleCliente, tvNumeroCliente, tvDepartamentoCliente, tvAlLadoDeCliente, tvFrenteACliente, tvEntreCallesCliente, tvColoniaCliente, tvLocalidadCliente,
                tvTelefonoCliente, tvTelefonoReferenciaCliente, tvNombreReferenciaCliente, tvTipoCasaCliente, tvCasaColorCliente, tvFotoINEFrente, tvFotoINEAtras,
                tvFotoCasa, tvFotoComprobanteDomicilio, tvEdadNuevoHistorial, tvDiagnosticoNuevoHistorial, tvOcupacionNuevoHistorial, tvDiabetesNuevoHistorial,
                tvHipertensionNuevoHistorial, tvOtroMoletiaNuevoHistorial, tvProductoNuevoHistorial, tvPaquetesNuevoHistorial, tvFechaEntregaNuevoHistorial,
                tvOjoDerechoEsfericoNuevoHistorial, tvOjoDerechoCilindroNuevoHistorial, tvOjoDerechoEjeNuevoHistorial, tvOjoDerechoAddNuevoHistorial,
                tvOjoDerechoALTNuevoHistorial, tvOjoIzquierdoEsfericoNuevoHistorial, tvOjoIzquierdoCilindroNuevoHistorial, tvOjoIzquierdoEjeNuevoHistorial,
                tvOjoIzquierdoAddNuevoHistorial, tvOjoIzquierdoALTNuevoHistorial, tvOtroMaterialNuevoHistorial, tvPrecioMaterialNuevoHistorial,
                tvOtroTratamientoNuevoHistorial, tvPrecioTratamientoNuevoHistorial, tvTipoBifocalNuevoHistorial, idContratoPadre, contratoPromocion,
                llDuplicarDocumentos, cbDuplicarDocumentos, etCorreoCliente, tvCorreoCliente, ivFotoPagare, tvFotoPagare, llFotos, idContratoHijo, svNuevoContratoPrincipal,
                etObservacionesInternoNuevoHistorial, actvContratosLiosFugas, btnGuardarContratoNuevo, btnCancelarContratoNuevo, etOjoDerechoEsfericoSinConversionNuevoHistorial,
                etOjoDerechoCilindroSinConversionNuevoHistorial, etOjoDerechoEjeSinConversionNuevoHistorial, etOjoDerechoAddSinConversionNuevoHistorial,
                etOjoIzquierdoEsfericoSinConversionNuevoHistorial, etOjoIzquierdoCilindroSinConversionNuevoHistorial, etOjoIzquierdoEjeSinConversionNuevoHistorial,
                etOjoIzquierdoAddSinConversionNuevoHistorial, tvGeneralSinConversionNuevoHistorial, ivFotoOtros, etEmbarazadaNuevoHistorial, etDurmioSeisOchoHorasNuevoHistorial,
                etActividadDiaNuevoHistorial, etProblemasOjosNuevoHistorial, etOtroBifocalNuevoHistorial, etPrecioBifocalNuevoHistorial, rbOtroBifocal, tvEmbarazadaNuevoHistorial,
                tvDurmioSeisOchoHorasNuevoHistorial, tvActividadDiaNuevoHistorial, tvProblemasOjosNuevoHistorial, tvOtroBifocalNuevoHistorial, tvPrecioBifocalNuevoHistorial,
                etCalleClienteEntrega, etNumeroClienteEntrega, etDepartamentoClienteEntrega, etAlLadoDeClienteEntrega, etFrenteAClienteEntrega, etEntreCallesClienteEntrega,
                etColoniaClienteEntrega, etLocalidadClienteEntrega, etTipoCasaClienteEntrega, etCasaColorClienteEntrega, tvCalleClienteEntrega, tvNumeroClienteEntrega,
                tvDepartamentoClienteEntrega, tvAlLadoDeClienteEntrega, tvFrenteAClienteEntrega, tvEntreCallesClienteEntrega, tvColoniaClienteEntrega, tvLocalidadClienteEntrega,
                tvTipoCasaClienteEntrega, tvCasaColorClienteEntrega, btnCopiarDatosLugarVenta, etAliasCliente, tvAliasCliente, tvOtraFoto, spColoniaClienteEntrega,
                spColorTratamiento, spEstiloTratamiento, tvColorTratamiento, tvEstiloTratamiento, cbTratamientoPolarizado, llTratamientoPolarizado, spColorTratamientoPolarizado,
                tvColorTratamientoPolarizado, cbTratamientoEspejeado, llTratamientoEspejeado, spColorTratamientoEspejeado, tvColorTratamientoEspejeado, cbPolicarbonatoTipo, rgTipoBifocalNuevoHistorial,
                llPrincipalNuevoContrato, llPrincipalHistorialClinico, contratoActualizar, spLugarEntrega, tvLugarEntrega, llFotoArmazonPropio, ivFotoArmazonPropio, tvFotoArmazonPropio,
                etObservacionFotoINEFrente, etObservacionFotoINEAtras, etObservacionFotoPagare, etObservacionFotoComprobanteDomicilio, etObservacionFotoCasa, etObservacionOtraFoto};

        //Inicialización de Clases
        controlador = new Controlador(fragmento, objetos);
        camara = new Camara(fragmento);
        teclado = new Teclado(fragmento);

        controlador.validar();

        String estadoContrato = global.obtenerAtributoContrato(idContratoHijo, "ESTATUS_ESTADOCONTRATO");

        //Verificar que tengamos los permisos
        if(camara.mayRequestStoragePermission() && (!contratoPromocion || (estadoContrato.equals("13") || estadoContrato.equals("0") || estadoContrato.equals("1")))) {
            ivFotoINEFrente.setEnabled(true);
            ivFotoINEAtras.setEnabled(true);
            ivFotoCasa.setEnabled(true);
            ivFotoComprobanteDomicilio.setEnabled(true);
            ivFotoPagare.setEnabled(true);
            ivFotoOtros.setEnabled(true);
        }else{
            ivFotoINEFrente.setEnabled(false);
            ivFotoINEAtras.setEnabled(false);
            ivFotoCasa.setEnabled(false);
            ivFotoComprobanteDomicilio.setEnabled(false);
            ivFotoPagare.setEnabled(false);
            ivFotoOtros.setEnabled(false);
        }

        //Verificar configuracion de diseño para encabezados
        global.verificarConfiguracionEncabezados(tvContratoEncabezado);
        global.verificarConfiguracionEncabezados(tvHistorialClinicoEncabezado);
        global.verificarConfiguracionEncabezados(tvProductoEncabezado);
        global.verificarConfiguracionEncabezados(tvSinConversionEncabezado);
        global.verificarConfiguracionEncabezados(tvOjoDerechoSCEncabezado);
        global.verificarConfiguracionEncabezados(tvOjoIzquierdoSCEncabezado);
        global.verificarConfiguracionEncabezados(tvConConversionEncabezado);
        global.verificarConfiguracionEncabezados(tvSCVisionLejana);
        global.verificarConfiguracionEncabezados(tvCCVisionLejana);
        global.verificarConfiguracionEncabezados(tvOjoDerechoCCEncabezado);
        global.verificarConfiguracionEncabezados(tvOjoIzquierdoCCEncabezado);
        global.verificarConfiguracionEncabezados(tvMaterialEncabezado);
        global.verificarConfiguracionEncabezados(tvTratamientoEncabezado);
        global.verificarConfiguracionEncabezados(tvTipoBifocalEncabezado);

        //Verificar configuracion de diseño para iconos (botones agregar imagenes)
        global.verificarConfiguracionLogoIconos(ivFotoINEFrente, 1);
        global.verificarConfiguracionLogoIconos(ivFotoINEAtras,1);
        //global.verificarConfiguracionLogoIconos(ivFotoPagare, 1); //Por que se quito la foto de pagare
        global.verificarConfiguracionLogoIconos(ivFotoComprobanteDomicilio, 1);
        global.verificarConfiguracionLogoIconos(ivFotoCasa, 1);
        global.verificarConfiguracionLogoIconos(ivFotoOtros, 1);

        etNombreCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                if(charSequence.toString().contains("/") || charSequence.toString().contains("\"") || charSequence.toString().contains("\\\\")) {
                    String cadenaLimpia = charSequence.toString().replace("/", "");
                    cadenaLimpia = cadenaLimpia.replace("\"", "");
                    cadenaLimpia = cadenaLimpia.replace("\\\\", "");
                    etNombreCliente.setText(cadenaLimpia);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etAliasCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                if(charSequence.toString().contains("/") || charSequence.toString().contains("\"") || charSequence.toString().contains("\\\\")) {
                    String cadenaLimpia = charSequence.toString().replace("/", "");
                    cadenaLimpia = cadenaLimpia.replace("\"", "");
                    cadenaLimpia = cadenaLimpia.replace("\\\\", "");
                    etAliasCliente.setText(cadenaLimpia);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCalleCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                if(charSequence.toString().contains("/") || charSequence.toString().contains("\"") || charSequence.toString().contains("\\\\")) {
                    String cadenaLimpia = charSequence.toString().replace("/", "");
                    cadenaLimpia = cadenaLimpia.replace("\"", "");
                    cadenaLimpia = cadenaLimpia.replace("\\\\", "");
                    etCalleCliente.setText(cadenaLimpia);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNumeroCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                if(charSequence.toString().contains("/") || charSequence.toString().contains("\"") || charSequence.toString().contains("\\\\")) {
                    String cadenaLimpia = charSequence.toString().replace("/", "");
                    cadenaLimpia = cadenaLimpia.replace("\"", "");
                    cadenaLimpia = cadenaLimpia.replace("\\\\", "");
                    etNumeroCliente.setText(cadenaLimpia);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etColoniaCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                if(charSequence.toString().contains("/") || charSequence.toString().contains("\"") || charSequence.toString().contains("\\\\")) {
                    String cadenaLimpia = charSequence.toString().replace("/", "");
                    cadenaLimpia = cadenaLimpia.replace("\"", "");
                    cadenaLimpia = cadenaLimpia.replace("\\\\", "");
                    etColoniaCliente.setText(cadenaLimpia);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etLocalidadCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                if(charSequence.toString().contains("/") || charSequence.toString().contains("\"") || charSequence.toString().contains("\\\\")) {
                    String cadenaLimpia = charSequence.toString().replace("/", "");
                    cadenaLimpia = cadenaLimpia.replace("\"", "");
                    cadenaLimpia = cadenaLimpia.replace("\\\\", "");
                    etLocalidadCliente.setText(cadenaLimpia);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEntreCallesCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                if(charSequence.toString().contains("/") || charSequence.toString().contains("\"") || charSequence.toString().contains("\\\\")) {
                    String cadenaLimpia = charSequence.toString().replace("/", "");
                    cadenaLimpia = cadenaLimpia.replace("\"", "");
                    cadenaLimpia = cadenaLimpia.replace("\\\\", "");
                    etEntreCallesCliente.setText(cadenaLimpia);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Acciones EditTextDatePicker
        etUltimoExamenNuevoHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.showDatePickerDialog(etUltimoExamenNuevoHistorial);
            }
        });

        etFechaEntregaNuevoHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.showDatePickerDialog(etFechaEntregaNuevoHistorial);
            }
        });

        //Acciones CheckBox
        cbOtroMolestia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {
                    etOtroMoletiaNuevoHistorial.setEnabled(true);
                    etOtroMoletiaNuevoHistorial.requestFocus();
                    teclado.showKeyboard();
                }else {
                    etOtroMoletiaNuevoHistorial.setEnabled(false);
                    etOtroMoletiaNuevoHistorial.setText("");
                }

            }
        });

        cbTratamientoFotocromatico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 1) {
                    if ( isChecked ) {
                        cbTratamientoAR.setEnabled(false);
                        cbTratamientoBlueray.setEnabled(false);

                        cbTratamientoAR.setChecked(false);
                        cbTratamientoBlueray.setChecked(false);

                    }else {
                        cbTratamientoAR.setEnabled(true);
                        cbTratamientoBlueray.setEnabled(true);
                    }
                }else {
                    if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 8){ //Paquete premium
                        if(cbTratamientoFotocromatico.isChecked()){
                            cbTratamientoTinte.setChecked(false);
                            cbTratamientoTinte.setEnabled(false);
                            spColorTratamiento.setSelection(0);
                            spEstiloTratamiento.setSelection(0);
                            cbTratamientoPolarizado.setChecked(false);
                            cbTratamientoPolarizado.setEnabled(false);
                            spColorTratamientoPolarizado.setSelection(0);
                            cbTratamientoEspejeado.setChecked(false);
                            cbTratamientoEspejeado.setEnabled(false);
                            spColorTratamientoEspejeado.setSelection(0);
                            cbTratamientoOtro.setChecked(false);
                            etOtroTratamientoNuevoHistorial.setText("");
                            etOtroTratamientoNuevoHistorial.setEnabled(false);
                            etPrecioTratamientoNuevoHistorial.setText("");
                            etPrecioTratamientoNuevoHistorial.setEnabled(false);
                        }else{
                            cbTratamientoTinte.setChecked(false);
                            cbTratamientoTinte.setEnabled(true);
                            spColorTratamiento.setSelection(0);
                            spEstiloTratamiento.setSelection(0);
                            cbTratamientoPolarizado.setChecked(false);
                            cbTratamientoPolarizado.setEnabled(true);
                            spColorTratamientoPolarizado.setSelection(0);
                            cbTratamientoEspejeado.setChecked(false);
                            cbTratamientoEspejeado.setEnabled(true);
                            spColorTratamientoEspejeado.setSelection(0);
                            cbTratamientoOtro.setChecked(false);
                            etOtroTratamientoNuevoHistorial.setText("");
                            etOtroTratamientoNuevoHistorial.setEnabled(true);
                            etPrecioTratamientoNuevoHistorial.setText("");
                            etPrecioTratamientoNuevoHistorial.setEnabled(true);
                        }

                    }else{
                        if(cbTratamientoTinte.isChecked()){
                            cbTratamientoFotocromatico.setChecked(false);
                            cbTratamientoFotocromatico.setEnabled(false);
                            cbTratamientoAR.setChecked(false);
                            cbTratamientoAR.setEnabled(false);
                            cbTratamientoBlueray.setChecked(false);
                            cbTratamientoBlueray.setEnabled(false);
                        }
                    }
                }

            }
        });

        cbTratamientoAR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 1) {
                    if ( isChecked ) {
                        cbTratamientoFotocromatico.setEnabled(false);
                        cbTratamientoBlueray.setEnabled(false);

                        cbTratamientoFotocromatico.setChecked(false);
                        cbTratamientoBlueray.setChecked(false);
                    }else {
                        cbTratamientoFotocromatico.setEnabled(true);
                        cbTratamientoBlueray.setEnabled(true);
                    }
                }else {
                    if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 8){
                        if(isChecked){
                            cbTratamientoBlueray.setChecked(false);
                            cbTratamientoBlueray.setEnabled(false);
                        }else{
                            cbTratamientoBlueray.setChecked(false);
                            cbTratamientoBlueray.setEnabled(true);
                        }
                    }else {
                        if (isChecked) {
                            if (cbTratamientoTinte.isChecked()) {
                                cbTratamientoAR.setChecked(false);
                                cbTratamientoAR.setEnabled(false);
                            }
                            if(cbTratamientoBlueray.isChecked()){
                                cbTratamientoBlueray.setChecked(false);
                                cbTratamientoBlueray.setEnabled(true);
                            }
                        } else {
                            if (cbTratamientoTinte.isChecked()) {
                                cbTratamientoAR.setChecked(false);
                                cbTratamientoAR.setEnabled(false);
                                cbTratamientoFotocromatico.setEnabled(false);
                                cbTratamientoBlueray.setEnabled(false);
                            }
                        }
                    }
                }

            }
        });

        cbTratamientoTinte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {
                    cbTratamientoFotocromatico.setEnabled(false);
                    cbTratamientoAR.setEnabled(false);
                    cbTratamientoBlueray.setEnabled(false);

                    cbTratamientoFotocromatico.setChecked(false);
                    cbTratamientoAR.setChecked(false);
                    cbTratamientoBlueray.setChecked(false);
                    cbTratamientoOtro.setChecked(false);
                    llColorEstiloTinte.setVisibility(View.VISIBLE);
                }else {
                    if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 1) {
                        cbTratamientoFotocromatico.setEnabled(true);
                        cbTratamientoAR.setEnabled(true);
                        cbTratamientoBlueray.setEnabled(true);
                    }else {
                        cbTratamientoFotocromatico.setEnabled(true);
                        cbTratamientoAR.setEnabled(true);
                        cbTratamientoBlueray.setEnabled(true);

                        blueRayActivo = false;
                    }
                    spColorTratamiento.setSelection(0);
                    spEstiloTratamiento.setSelection(0);
                    llColorEstiloTinte.setVisibility(View.GONE);
                }
            }
        });

        cbTratamientoBlueray.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 1) {
                    if ( isChecked ) {
                        cbTratamientoFotocromatico.setChecked(false);
                        cbTratamientoAR.setChecked(false);

                        cbTratamientoFotocromatico.setEnabled(false);
                        cbTratamientoAR.setEnabled(false);
                        blueRayActivo = true;

                    }else {
                        if(!blueRayActivo){
                            cbTratamientoAR.setChecked(true);
                            cbTratamientoAR.setEnabled(true);
                        }

                        if(!cbTratamientoTinte.isChecked() && blueRayActivo == true){
                            cbTratamientoFotocromatico.setChecked(false);
                            cbTratamientoAR.setChecked(false);
                            cbTratamientoFotocromatico.setEnabled(true);
                            cbTratamientoAR.setEnabled(true);
                            blueRayActivo = false;
                        }

                    }
                }else {
                    if ( isChecked ) {
                        cbTratamientoAR.setChecked(false);
                        cbTratamientoAR.setEnabled(false);
                        blueRayActivo = true;
                    }else {
                        if(!blueRayActivo){
                            cbTratamientoAR.setChecked(true);
                            cbTratamientoAR.setEnabled(true);
                        }

                        if(!cbTratamientoTinte.isChecked() && blueRayActivo == true){
                            cbTratamientoAR.setChecked(false);
                            cbTratamientoAR.setEnabled(true);
                            blueRayActivo = false;
                        }

                    }
                }

            }
        });

        cbTratamientoOtro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {
                    etOtroTratamientoNuevoHistorial.setEnabled(true);
                    etPrecioTratamientoNuevoHistorial.setEnabled(true);
                    etOtroTratamientoNuevoHistorial.requestFocus();
                    teclado.showKeyboard();
                }else {
                    etOtroTratamientoNuevoHistorial.setEnabled(false);
                    etPrecioTratamientoNuevoHistorial.setEnabled(false);
                    etOtroTratamientoNuevoHistorial.setText("");
                    etPrecioTratamientoNuevoHistorial.setText("");
                }
            }
        });

        cbDuplicarDocumentos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {
                    llFotos.setVisibility(View.GONE);
                }else {
                    llFotos.setVisibility(View.VISIBLE);
                    ivFotoINEFrente.setEnabled(true);
                    ivFotoINEAtras.setEnabled(true);
                    ivFotoCasa.setEnabled(true);
                    ivFotoComprobanteDomicilio.setEnabled(true);
                    ivFotoPagare.setEnabled(true);
                    ivFotoOtros.setEnabled(true);

                    ivFotoINEFrente.setImageDrawable(null);
                    ivFotoINEAtras.setImageDrawable(null);
                    ivFotoCasa.setImageDrawable(null);
                    ivFotoComprobanteDomicilio.setImageDrawable(null);
                    ivFotoPagare.setImageDrawable(null);
                    ivFotoOtros.setImageDrawable(null);

                    ivFotoINEFrente.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ine));
                    ivFotoINEAtras.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ine));
                    ivFotoCasa.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.casa));
                    ivFotoComprobanteDomicilio.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.comprobante));
                    ivFotoPagare.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pagare));
                    ivFotoOtros.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.otrafoto));

                }
            }
        });
        cbTratamientoPolarizado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 8) {

                    if (isChecked) {
                        llTratamientoPolarizado.setVisibility(View.VISIBLE);
                        cbTratamientoEspejeado.setChecked(false);
                        cbTratamientoEspejeado.setEnabled(false);
                        spColorTratamientoEspejeado.setSelection(0);
                    }else{
                        llTratamientoPolarizado.setVisibility(View.GONE);
                        cbTratamientoEspejeado.setChecked(false);
                        cbTratamientoEspejeado.setEnabled(true);
                        spColorTratamientoPolarizado.setSelection(0);
                    }

                }
            }
        });

        cbTratamientoEspejeado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(spPaquetesNuevoHistorial.getSelectedItemPosition() == 8) {
                    if(isChecked){
                        llTratamientoEspejeado.setVisibility(View.VISIBLE);
                        cbTratamientoPolarizado.setChecked(false);
                        cbTratamientoPolarizado.setEnabled(false);
                        spColorTratamientoPolarizado.setSelection(0);
                    }else{
                        llTratamientoEspejeado.setVisibility(View.GONE);
                        cbTratamientoPolarizado.setChecked(false);
                        cbTratamientoPolarizado.setEnabled(true);
                        spColorTratamientoEspejeado.setSelection(0);
                    }
                }
            }
        });

        cbPolicarbonatoTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbPolicarbonatoTipo.isChecked()){
                    cbPolicarbonatoTipo.setText("Niño");
                }else{
                    cbPolicarbonatoTipo.setText("Adulto");
                }
            }
        });

        //Acciones RadioButton
        rbHiIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked)
                    etOtroMaterialNuevoHistorial.setEnabled(false);
                    etPrecioMaterialNuevoHistorial.setEnabled(false);
                    etOtroMaterialNuevoHistorial.setText("");
                    etPrecioMaterialNuevoHistorial.setText("");
                    cbPolicarbonatoTipo.setText("Adulto");
                    cbPolicarbonatoTipo.setChecked(false);
                    cbPolicarbonatoTipo.setVisibility(View.GONE);
            }
        });

        rbCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked)
                    etOtroMaterialNuevoHistorial.setEnabled(false);
                    etPrecioMaterialNuevoHistorial.setEnabled(false);
                    etOtroMaterialNuevoHistorial.setText("");
                    etPrecioMaterialNuevoHistorial.setText("");
                    cbPolicarbonatoTipo.setText("Adulto");
                    cbPolicarbonatoTipo.setChecked(false);
                    cbPolicarbonatoTipo.setVisibility(View.GONE);
            }
        });

        rbPolicarbonato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked)
                    etOtroMaterialNuevoHistorial.setEnabled(false);
                    etPrecioMaterialNuevoHistorial.setEnabled(false);
                    etOtroMaterialNuevoHistorial.setText("");
                    etPrecioMaterialNuevoHistorial.setText("");
                    cbPolicarbonatoTipo.setChecked(false);
                    cbPolicarbonatoTipo.setVisibility(View.VISIBLE);
            }
        });

        rbOtroMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    cbPolicarbonatoTipo.setText("Adulto");
                    cbPolicarbonatoTipo.setChecked(false);
                    cbPolicarbonatoTipo.setVisibility(View.GONE);
                    etOtroMaterialNuevoHistorial.setEnabled(true);
                    etPrecioMaterialNuevoHistorial.setEnabled(true);
                    etOtroMaterialNuevoHistorial.requestFocus();
                    teclado.showKeyboard();
                }
            }
        });

        rbFT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked) {
                    etOtroBifocalNuevoHistorial.setEnabled(false);
                    etPrecioBifocalNuevoHistorial.setEnabled(false);
                    etOtroBifocalNuevoHistorial.setText("");
                    etPrecioBifocalNuevoHistorial.setText("");
                    controlador.llenarSpTratamientoTinte();
                }
            }
        });

        rbBlend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked) {
                    etOtroBifocalNuevoHistorial.setEnabled(false);
                    etPrecioBifocalNuevoHistorial.setEnabled(false);
                    etOtroBifocalNuevoHistorial.setText("");
                    etPrecioBifocalNuevoHistorial.setText("");
                    controlador.llenarSpTratamientoTinte();
                }
            }
        });

        rbProgresivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked) {
                    etOtroBifocalNuevoHistorial.setEnabled(false);
                    etPrecioBifocalNuevoHistorial.setEnabled(false);
                    etOtroBifocalNuevoHistorial.setText("");
                    etPrecioBifocalNuevoHistorial.setText("");
                    controlador.llenarSpTratamientoTinte();
                }
            }
        });

        rbNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked) {
                    etOtroBifocalNuevoHistorial.setEnabled(false);
                    etPrecioBifocalNuevoHistorial.setEnabled(false);
                    etOtroBifocalNuevoHistorial.setText("");
                    etPrecioBifocalNuevoHistorial.setText("");
                    controlador.llenarSpTratamientoTinte();
                }
            }
        });

        rbOtroBifocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    etOtroBifocalNuevoHistorial.setEnabled(true);
                    etPrecioBifocalNuevoHistorial.setEnabled(true);
                    etOtroBifocalNuevoHistorial.requestFocus();
                    controlador.llenarSpTratamientoTinte();
                    teclado.showKeyboard();
                }
            }
        });
/*
        //Accion Spinners
        spZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(controlador.verificarSpinnerSelected(spZona) && !controlador.verificarSpinnerSelected(spOptometrista)) {
                    spOptometrista.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/
        spOptometrista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(controlador.verificarSpinnerSelected(spOptometrista) && etNombreCliente.getText().toString().equals("")) {
                    etNombreCliente.requestFocus();
                    teclado.showKeyboard();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spProductoNuevoHistorial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(controlador.verificarPiezasRestantesproduto()){
                    //Seleccionaste un producto con mas de 10 piezas en existencia
                    if(!controlador.validarArmazonSeleccionado() && controlador.verificarSpinnerSelected(spProductoNuevoHistorial) &&
                       !controlador.verificarSpinnerSelected(spPaquetesNuevoHistorial)) {
                        //Armazon es diferente de PROPIO - Aun no se selecciona un paquete
                        spPaquetesNuevoHistorial.performClick();
                    }
                }
                controlador.verificarProductoPaqueteSeleccionado();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spPaquetesNuevoHistorial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                llVisionLejana.setVisibility(View.GONE);
                llDatosSinConversionNuevoHistorial.setVisibility(View.GONE);
                llVisionLejanaSinConversion.setVisibility(View.GONE);
                //Evento lo generó el usuario al seleccionar un paquete?
                if(validarTratamientosContrato){
                    //No es un contrato por crear - Limpiar tratamientos y validar

                    if(position == 0) { //Seleccionar
                        controlador.validarComponentesPaqueteTres(true, false);
                    }
                    if(position == 1) { //LECTURA
                        llDatosSinConversionNuevoHistorial.setVisibility(View.VISIBLE);
                        controlador.validarComponentesPaqueteUno(true);
                        controlador.verificarCampoFechaEntregaEstaVacio();
                        controlador.requestFocusCamposOjoDerIzqPaqueteUno(true);
                    }
                    if(position == 2) { //PROTECCION
                        controlador.validarComponentesPaqueteDos();
                        controlador.verificarCampoFechaEntregaEstaVacio();
                    }
                    if(position == 3) { //ECO JR
                        controlador.validarComponentesPaqueteUno(false);
                        controlador.verificarCampoFechaEntregaEstaVacio();
                        controlador.requestFocusCamposOjoDerIzqPaqueteUno(false);
                    }
                    if(position == 4) { //JR
                        controlador.validarComponentesPaqueteUno(false);
                        controlador.verificarCampoFechaEntregaEstaVacio();
                        controlador.requestFocusCamposOjoDerIzqPaqueteUno(false);
                    }
                    if(position == 5) { //DORADO 1
                        controlador.validarComponentesPaqueteTres(true, true);
                        controlador.verificarCampoFechaEntregaEstaVacio();
                        controlador.requestFocusCamposOjoDerIzqPaqueteTres();
                    }
                    if(position == 6) { //DORADO 2
                        llVisionLejana.setVisibility(View.VISIBLE);
                        llDatosSinConversionNuevoHistorial.setVisibility(View.VISIBLE);
                        llVisionLejanaSinConversion.setVisibility(View.VISIBLE);
                        controlador.validarComponentesPaqueteUno(false);
                        controlador.verificarCampoFechaEntregaEstaVacio();
                        controlador.requestFocusCamposOjoDerIzqPaqueteUno(true);
                    }
                    if(position == 7) { //PLATINO
                        controlador.validarComponentesPaqueteTres(true, true);
                        controlador.verificarCampoFechaEntregaEstaVacio();
                        controlador.requestFocusCamposOjoDerIzqPaqueteTres();
                    }
                    if(position == 8){ //PREMIUM
                        controlador.validarComponentesPaquetePremium();
                        controlador.verificarCampoFechaEntregaEstaVacio();
                    }
                }

                if(estadoContrato.equals("13")){
                    //Entro al evento por selección de paquete al cargar informacion del contrato por crear al abrirlo
                    if(global.obtenerAtributoTabla("HISTORIALCLINICO","AR","ID_CONTRATO",idContratoHijo).equals("0") && !validarTratamientosContrato){
                        //No tiene tratamiento AR en BD y entraste a seguir editando contrato por crear - Limpiar AR que carga por default
                        cbTratamientoAR.setChecked(false);
                    }
                    // Habilitar bandera para validaciones para edicion de contrato
                    validarTratamientosContrato = true;
                }
                controlador.verificarProductoPaqueteSeleccionado();
                controlador.validarTipoBifocalPaquetesGraduacion(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //Acciones Botones
        ivFotoINEFrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivFotoINEFrente, 0);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivFotoINEFrente, 0);
                }
            }
        });

        ivFotoINEAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivFotoINEAtras, 1);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivFotoINEAtras, 1);
                }
            }
        });

        ivFotoCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivFotoCasa, 2);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivFotoCasa, 2);
                }
            }
        });

        ivFotoComprobanteDomicilio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivFotoComprobanteDomicilio, 3);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivFotoComprobanteDomicilio, 3);
                }
            }
        });

        ivFotoPagare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivFotoPagare, 4);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivFotoPagare, 4);
                }
            }
        });

        ivFotoOtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivFotoOtros, 5);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivFotoOtros, 5);
                }
            }
        });

        ivFotoArmazonPropio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!camara.mayRequestStoragePermission()) {
                    Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                        camara.mandarAPantallaConfiguracionTelefono();
                    }else {
                        camara.crearDirectorios();
                        camara.openCamera(ivFotoArmazonPropio, 8);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivFotoArmazonPropio, 8);
                }
            }
        });

        btnGuardarContratoNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (svNuevoContratoPrincipal.getChildAt(0).getBottom() <= (svNuevoContratoPrincipal.getHeight() + svNuevoContratoPrincipal.getScrollY())) {
                    //Crear contrato?
                    if(!contratoActualizar){
                        //scroll view esta en la parte final
                        controlador.validacionPaquetes();
                    }else{
                        controlador.actualizarContratoNoTerminado();
                    }
                }
            }
        });

        btnCancelarContratoNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!contratoActualizar){
                    //Contrato nuevo a crear
                    controlador.mostrarAlertaCancelarConfirmacion();
                }else{
                    //Contrato en estatus de TERMINADO - cancelar actualizacion
                    Fragment verificadorFragment;
                    FragmentTransaction transaction;
                    verificadorFragment = new principal(false);
                    transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, verificadorFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        btnCopiarDatosLugarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCalleClienteEntrega.setText(etCalleCliente.getText().toString());
                etNumeroClienteEntrega.setText(etNumeroCliente.getText().toString());
                etDepartamentoClienteEntrega.setText(etDepartamentoCliente.getText().toString());
                etAlLadoDeClienteEntrega.setText(etAlLadoDeCliente.getText().toString());
                etFrenteAClienteEntrega.setText(etFrenteACliente.getText().toString());
                etEntreCallesClienteEntrega.setText(etEntreCallesCliente.getText().toString());
                etTipoCasaClienteEntrega.setText(etTipoCasaCliente.getText().toString());
                etCasaColorClienteEntrega.setText(etCasaColorCliente.getText().toString());
            }
        });

        //Acciones EditText
        etNombreCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etAliasCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etAliasCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etCalleCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etCalleCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etNumeroCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etNumeroCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etDepartamentoCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etDepartamentoCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etAlLadoDeCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etAlLadoDeCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etFrenteACliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etFrenteACliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etEntreCallesCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etEntreCallesCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etColoniaCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etColoniaCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etLocalidadCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etLocalidadCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etTelefonoCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etTelefonoCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etNombreReferenciaCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etNombreReferenciaCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etTelefonoReferenciaCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etTelefonoReferenciaCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etTipoCasaCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etTipoCasaCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etCasaColorCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etCasaColorCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etCalleClienteEntrega.requestFocus();
                    return true;
                }

                return false;
            }
        });

        etCalleClienteEntrega.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etNumeroClienteEntrega.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etNumeroClienteEntrega.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etDepartamentoClienteEntrega.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etDepartamentoClienteEntrega.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etAlLadoDeClienteEntrega.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etAlLadoDeClienteEntrega.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etFrenteAClienteEntrega.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etFrenteAClienteEntrega.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etEntreCallesClienteEntrega.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etEntreCallesClienteEntrega.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etColoniaClienteEntrega.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etColoniaClienteEntrega.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etLocalidadClienteEntrega.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etLocalidadClienteEntrega.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etTipoCasaClienteEntrega.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etTipoCasaClienteEntrega.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etCasaColorClienteEntrega.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etCasaColorClienteEntrega.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etCorreoCliente.requestFocus();
                    return true;
                }

                return false;
            }
        });

        etCorreoCliente.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });

        etEdadNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etDiagnosticoNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etDiagnosticoNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOcupacionNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOcupacionNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etDiabetesNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etDiabetesNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etHipertensionNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etHipertensionNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etEmbarazadaNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etEmbarazadaNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etDurmioSeisOchoHorasNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etDurmioSeisOchoHorasNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etActividadDiaNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etActividadDiaNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etProblemasOjosNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etProblemasOjosNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });
        etOtroMoletiaNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });
        etOtroMaterialNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etPrecioMaterialNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etPrecioMaterialNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });
        etOtroTratamientoNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etPrecioTratamientoNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etPrecioTratamientoNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });
        etObservacionesNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etObservacionesInternoNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etObservacionesInternoNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });
        etOtroBifocalNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etPrecioBifocalNuevoHistorial.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etPrecioBifocalNuevoHistorial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });

        etOjoDerechoEsfericoNuevoHistorial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {

                    if (position == 0 && !banderaSigno[0] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[0] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '+')) || (position < charSequence.toString().length() && banderaSigno[0]
                            && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoDerechoEsfericoNuevoHistorial.setText(sb);
                    } else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[0] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoDerechoEsfericoNuevoHistorial.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoDerechoEsfericoNuevoHistorial.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[0] = false;
                    }

                }catch (Exception e) {
                    global.escribirError(e, 237);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoDerechoCilindroNuevoHistorial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {

                    if (position == 0 && !banderaSigno[1] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[1] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '+')) || (position < charSequence.toString().length() && banderaSigno[1]
                            && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoDerechoCilindroNuevoHistorial.setText(sb);
                    } else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[1] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoDerechoCilindroNuevoHistorial.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoDerechoCilindroNuevoHistorial.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[1] = false;
                    }
                }catch (Exception e) {
                    global.escribirError(e, 238);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoDerechoAddNuevoHistorial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {

                    if (position == 0 && !banderaSigno[2] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[2] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '+')) || (position < charSequence.toString().length() && banderaSigno[2]
                            && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoDerechoAddNuevoHistorial.setText(sb);
                    }else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[2] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoDerechoAddNuevoHistorial.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoDerechoAddNuevoHistorial.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[2] = false;
                    }
                }catch (Exception e) {
                    global.escribirError(e, 239);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoDerechoAddNuevoHistorial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    controlador.validarTipoBifocalPaquetesGraduacion(spPaquetesNuevoHistorial.getSelectedItemPosition());
                }
            }
        });

        etOjoDerechoAddSinConversionNuevoHistorial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    controlador.validarTipoBifocalPaquetesGraduacion(spPaquetesNuevoHistorial.getSelectedItemPosition());
                }
            }
        });
        etOjoIzquierdoEsfericoNuevoHistorial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {

                    if (position == 0 && !banderaSigno[3] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[3] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '+'))
                            || (position < charSequence.toString().length() && banderaSigno[3] && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoIzquierdoEsfericoNuevoHistorial.setText(sb);
                    } else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[3] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoIzquierdoEsfericoNuevoHistorial.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoIzquierdoEsfericoNuevoHistorial.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[3] = false;
                    }
                }catch (Exception e) {
                    global.escribirError(e, 240);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoIzquierdoCilindroNuevoHistorial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {

                    if (position == 0 && !banderaSigno[4] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[4] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '+'))
                            || (position < charSequence.toString().length() && banderaSigno[4] && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoIzquierdoCilindroNuevoHistorial.setText(sb);
                    }else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[4] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoIzquierdoCilindroNuevoHistorial.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoIzquierdoCilindroNuevoHistorial.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[4] = false;
                    }
                }catch (Exception e) {
                    global.escribirError(e, 241);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoIzquierdoAddNuevoHistorial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {

                    if (position == 0 && !banderaSigno[5] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[5] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '+'))
                            || (position < charSequence.toString().length() && banderaSigno[5] && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoIzquierdoAddNuevoHistorial.setText(sb);
                    }else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[5] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoIzquierdoAddNuevoHistorial.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoIzquierdoAddNuevoHistorial.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[5] = false;
                    }

                }catch (Exception e) {
                    global.escribirError(e, 242);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoIzquierdoAddNuevoHistorial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    controlador.validarTipoBifocalPaquetesGraduacion(spPaquetesNuevoHistorial.getSelectedItemPosition());
                }
            }
        });
        etOjoIzquierdoAddSinConversionNuevoHistorial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    controlador.validarTipoBifocalPaquetesGraduacion(spPaquetesNuevoHistorial.getSelectedItemPosition());
                }
            }
        });
        svNuevoContratoPrincipal.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (svNuevoContratoPrincipal.getChildAt(0).getBottom()
                                <= (svNuevoContratoPrincipal.getHeight() + svNuevoContratoPrincipal.getScrollY())) {
                            //scroll view esta en la parte final
                            btnGuardarContratoNuevo.setTextColor(Color.parseColor("#ffffff"));
                        } else {
                            //scroll view no esta en la parte final
                            btnGuardarContratoNuevo.setTextColor(Color.parseColor("#5C5C5C"));
                        }
                    }
                });

        actvContratosLiosFugas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                controlador.mostrarAlertDialogContratoLioFuga();
            }
        });

        actvContratosLiosFugas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                controlador.registrarMovimientoBusquedaLioFuga(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnFiltrarArmazones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cadenaFiltro = etFiltrarArmazones.getText().toString();
                if(cadenaFiltro.length() > 0){
                    //Se escribio algo en el campo de texto
                    controlador.filtrarListaArmazonesProductos(cadenaFiltro);
                    teclado.hideKeyboard();
                    spProductoNuevoHistorial.performClick();
                }else{
                    //Campo de filtrar es vacio
                    Toast.makeText(fragmento.getActivity(), "Campo filtrar productos vacio.", Toast.LENGTH_LONG).show();
                    teclado.hideKeyboard();
                }
            }
        });

        btnLimpiarFiltroArmazones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Limpiamos edit text para filtrar
                etFiltrarArmazones.setText("");
                //Consulta vacia para cargar toda la lista de armazones al select
                controlador.filtrarListaArmazonesProductos("");
                teclado.hideKeyboard();
                spProductoNuevoHistorial.performClick();
            }
        });

        etFiltrarArmazones.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    if(etFiltrarArmazones.getText().toString().length() > 0){
                        etFiltrarArmazones.setText(etFiltrarArmazones.getText().toString().trim());
                        controlador.filtrarListaArmazonesProductos(etFiltrarArmazones.getText().toString());
                        teclado.hideKeyboard();
                        spProductoNuevoHistorial.performClick();
                    }

                    return true;
                }

                return false;
            }
        });

        btnFiltrarColoniasLugarEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cadenaFiltro = etFiltrarColoniasLugarEntrega.getText().toString();
                if(cadenaFiltro.length() > 0){
                    //Se escribio algo en el campo de texto
                    controlador.filtrarListaColonias(cadenaFiltro, spColoniaClienteEntrega);
                    teclado.hideKeyboard();
                    spColoniaClienteEntrega.performClick();
                }else{
                    //Campo de filtrar es vacio
                    Toast.makeText(fragmento.getActivity(), "Campo filtrar colonias vacio.", Toast.LENGTH_LONG).show();
                    teclado.hideKeyboard();
                }

            }

        });

        btnLimpiarFiltroColoniasLugarEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Limpiamos edit text para filtrar
                etFiltrarColoniasLugarEntrega.setText("");
                //Consulta vacia para cargar toda la lista de colonias al select
                controlador.filtrarListaColonias("", spColoniaClienteEntrega);
                teclado.hideKeyboard();
                spColoniaClienteEntrega.performClick();
            }
        });

        spColoniaClienteEntrega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                controlador.verificarColoniaSeleccionada(spColoniaClienteEntrega, etColoniaClienteEntrega, etLocalidadClienteEntrega);
                controlador.asignarZonaPorColoniaSeleccionada();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etObservacionFotoINEFrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.mostrarAlertDialogObservacionImagenesContrato(etObservacionFotoINEFrente, "INE FRENTE");
            }
        });

        etObservacionFotoINEFrente.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    global.mostrarAlertDialogObservacionImagenesContrato(etObservacionFotoINEFrente, "INE FRENTE");
                }
            }
        });

        etObservacionFotoINEAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.mostrarAlertDialogObservacionImagenesContrato(etObservacionFotoINEAtras, "INE ATRAS");
            }
        });

        etObservacionFotoINEAtras.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    global.mostrarAlertDialogObservacionImagenesContrato(etObservacionFotoINEAtras, "INE ATRAS");
                }
            }
        });

        etObservacionFotoPagare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.mostrarAlertDialogObservacionImagenesContrato(etObservacionFotoPagare, "PAGARE");
            }
        });

        etObservacionFotoPagare.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    global.mostrarAlertDialogObservacionImagenesContrato(etObservacionFotoPagare, "PAGARE");
                }
            }
        });

        etObservacionFotoComprobanteDomicilio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.mostrarAlertDialogObservacionImagenesContrato(etObservacionFotoComprobanteDomicilio, "COMPROBANTE DOMICILIO");
            }
        });

        etObservacionFotoComprobanteDomicilio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    global.mostrarAlertDialogObservacionImagenesContrato(etObservacionFotoComprobanteDomicilio, "COMPROBANTE DOMICILIO");
                }
            }
        });

        etObservacionFotoCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.mostrarAlertDialogObservacionImagenesContrato(etObservacionFotoCasa, "CASA");
            }
        });

        etObservacionFotoCasa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    global.mostrarAlertDialogObservacionImagenesContrato(etObservacionFotoCasa, "CASA");
                }
            }
        });

        etObservacionOtraFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.mostrarAlertDialogObservacionImagenesContrato(etObservacionOtraFoto, "OTRO");
            }
        });

        etObservacionOtraFoto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    global.mostrarAlertDialogObservacionImagenesContrato(etObservacionOtraFoto, "OTRO");
                }
            }
        });

        return view;

    }

    private boolean tieneUnValorEnMedioOAlFinal(String string) {
        for(int i = 0;i<string.length();i++){
            if((string.charAt(i) == '-' || string.charAt(i) == '+') && i > 0){
                return  true;
            }
        }
        return false;
    }

    private String eliminarValorEnMedioOAlFinal(String string) {
        String nuevoString = "";
        for(int i = 0;i<string.length();i++){
            if((string.charAt(i) == '-' || string.charAt(i) == '+') && i > 0){
              continue;
            }
            nuevoString += string.charAt(i);
        }
        return nuevoString;
    }

    private boolean valirdarSiContieneMenosOMas(String string) {
        int contador = 0;
        for(int i = 0;i<string.length();i++){
            if(string.charAt(i) == '-' || string.charAt(i) == '+'){
                contador++;
                if(contador == 1){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean valorRepetido(String string) {
        int contador = 0;
        for(int i = 0;i<string.length();i++){
            if(string.charAt(i) == '-' || string.charAt(i) == '+'){
                contador++;
                if(contador == 2){
                    return true;
                }
            }
        }
        return false;
    }

    private String  eliminarRepetidos(String string) {

        int contador = 0;
        String nuevoString = "";
        for(int i = 0;i<string.length();i++){
            if(string.charAt(i) == '-' || string.charAt(i) == '+'){
                contador++;
                if(contador == 2){
                    contador = 1;
                    continue;
                }
            }
            nuevoString += string.charAt(i);
        }
        return nuevoString;
    }

    public static boolean stringContieneCaracter(String str) {
        str = str.toLowerCase();
        for (int i = 0; i < str.length(); i++) {
            if ((str.charAt(i) == '+') || (str.charAt(i) == '-')) {
                return true;
            }
        }

        return false;
    }

    //Metodo que se utiliza para escuchar la respuesta de las funciones de las fotos
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camara.onActivityResult(requestCode, resultCode, data);
    }

    //En caso de que el usuario acepte los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean desbloquear = camara.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(contratoPromocion && desbloquear && global.obtenerAtributoContrato(idContratoPadre, "ESTATUS_ESTADOCONTRATO").equals("1")) {
            ivFotoINEFrente.setEnabled(false);
            ivFotoINEAtras.setEnabled(false);
            ivFotoCasa.setEnabled(false);
            ivFotoComprobanteDomicilio.setEnabled(false);
            ivFotoPagare.setEnabled(false);
            ivFotoOtros.setEnabled(false);
        }else {
            ivFotoINEFrente.setEnabled(true);
            ivFotoINEAtras.setEnabled(true);
            ivFotoCasa.setEnabled(true);
            ivFotoComprobanteDomicilio.setEnabled(true);
            ivFotoPagare.setEnabled(true);
            ivFotoOtros.setEnabled(true);
        }
    }

}