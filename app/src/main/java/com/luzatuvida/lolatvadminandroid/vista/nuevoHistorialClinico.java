package com.luzatuvida.lolatvadminandroid.vista;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Camara;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Teclado;
import com.luzatuvida.lolatvadminandroid.nuevoHistorialClinico.Controlador;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nuevoHistorialClinico#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nuevoHistorialClinico extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Variables
    boolean blueRayActivo = false;

    //Componentes
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
            tvOtroMaterialNuevoHistorial2, tvPrecioMaterialNuevoHistorial2, tvOtroTratamientoNuevoHistorial2, tvPrecioTratamientoNuevoHistorial2, tvTipoBifocalNuevoHistorial2, tvVisionCercana,
            tvGeneralSinConversionNuevoHistorial2, tvVisionCercanaSinConversion, tvOtroBifocalNuevoHistorial2, tvPrecioBifocalNuevoHistorial2,  tvColorTratamiento2, tvEstiloTratamiento2,
            tvColorTratamientoPolarizado2, tvColorTratamientoEspejeado2;
    RadioButton rbHiIndex2, rbCR2, rbPolicarbonato2, rbOtroMaterial2, rbFT2, rbBlend2, rbProgresivo2, rbNA2, rbOtroBifocal2;
    CheckBox cbDolorCabeza2, cbArdorOjos2, cbGolpeCabeza2, cbOtroMolestia2, cbTratamientoFotocromatico2, cbTratamientoAR2, cbTratamientoTinte2, cbTratamientoBlueray2, cbTratamientoOtro2,
             cbTratamientoPolarizado2, cbPolicarbonatoTipo2, cbTratamientoEspejeado2;
    Button btnGuardarHistorialNuevo2, btnRegresarHistorialNuevo2;
    Spinner spProductoNuevoHistorial2, spPaquetesNuevoHistorial2, spColorTratamiento2, spEstiloTratamiento2, spColorTratamientoPolarizado2,
            spColorTratamientoEspejeado2;
    LinearLayout llVisionCercana, llDatosSinConversionNuevoHistorial2, llVisionCercanaSinConversion, llColorEstiloTinte2, llTratamientoPolarizado2,
            llTratamientoEspejeado2, llFotoArmazonPropioHistorial2;

    ImageView ivFotoArmazonPropioHistorial2;

    //Declaracion de clases
    Controlador controlador;
    Teclado teclado;
    int posicionLista = 0;
    boolean[] banderaSigno;
    Global global;
    Camara camara;
    boolean esHistorialGarantia = false;

    Fragment fragmento;

    //TextView encabezados
    TextView tvNuevoHistorialClinico, tvProductoNuevoHistorial, tvNuevoHistorialSC, tvOjoDerechoNuevoHistorialSC, tvOjoIzquierdoNuevoHistorialSC, tvNuevoHistorialCC,
            tvOjoDerechoNuevoHistorialCC, tvOjoIzquierdoNuevoHistorialCC, tvMaterialNuevoHistorial, tvTratamientoNuevoHistorial, tvTipoBifocalNuevoHistorial,
            tvFotoArmazonPropioHistorial2;
    RadioGroup rgTipoBifocalNuevoHistorial2;
    public nuevoHistorialClinico() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nuevoHistorialClinico.
     */
    // TODO: Rename and change types and number of parameters
    public static nuevoHistorialClinico newInstance(String param1, String param2) {
        nuevoHistorialClinico fragment = new nuevoHistorialClinico();
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
        Identificador error del 243 al 248

        Throwable identificador error 5
        */

        fragmento = nuevoHistorialClinico.this;
        global = new Global(fragmento);
        camara = new Camara(fragmento);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                global.escribirErrorClasePrincipalThrowable(throwable, 5);
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

        View view = inflater.inflate(R.layout.fragment_nuevo_historial_clinico, container, false);

        //HISTORIAL
        //EditText
        etEdadNuevoHistorial2 = view.findViewById(R.id.etEdadNuevoHistorial2);
        etDiagnosticoNuevoHistorial2 = view.findViewById(R.id.etDiagnosticoNuevoHistorial2);
        etOcupacionNuevoHistorial2 = view.findViewById(R.id.etOcupacionNuevoHistorial2);
        etDiabetesNuevoHistorial2 = view.findViewById(R.id.etDiabetesNuevoHistorial2);
        etHipertensionNuevoHistorial2 = view.findViewById(R.id.etHipertensionNuevoHistorial2);
        etOtroMoletiaNuevoHistorial2 = view.findViewById(R.id.etOtroMoletiaNuevoHistorial2);
        etUltimoExamenNuevoHistorial2 = view.findViewById(R.id.etUltimoExamenNuevoHistorial2);
        etFechaEntregaNuevoHistorial2 = view.findViewById(R.id.etFechaEntregaNuevoHistorial2);
        etOjoDerechoEsfericoNuevoHistorial2 = view.findViewById(R.id.etOjoDerechoEsfericoNuevoHistorial2);
        etOjoDerechoCilindroNuevoHistorial2 = view.findViewById(R.id.etOjoDerechoCilindroNuevoHistorial2);
        etOjoDerechoEjeNuevoHistorial2 = view.findViewById(R.id.etOjoDerechoEjeNuevoHistorial2);
        etOjoDerechoAddNuevoHistorial2 = view.findViewById(R.id.etOjoDerechoAddNuevoHistorial2);
        etOjoDerechoALTNuevoHistorial2 = view.findViewById(R.id.etOjoDerechoALTNuevoHistorial2);
        etOjoIzquierdoEsfericoNuevoHistorial2 = view.findViewById(R.id.etOjoIzquierdoEsfericoNuevoHistorial2);
        etOjoIzquierdoCilindroNuevoHistorial2 = view.findViewById(R.id.etOjoIzquierdoCilindroNuevoHistorial2);
        etOjoIzquierdoEjeNuevoHistorial2 = view.findViewById(R.id.etOjoIzquierdoEjeNuevoHistorial2);
        etOjoIzquierdoAddNuevoHistorial2 = view.findViewById(R.id.etOjoIzquierdoAddNuevoHistorial2);
        etOjoIzquierdoALTNuevoHistorial2 = view.findViewById(R.id.etOjoIzquierdoALTNuevoHistorial2);
        etOtroMaterialNuevoHistorial2 = view.findViewById(R.id.etOtroMaterialNuevoHistorial2);
        etPrecioMaterialNuevoHistorial2 = view.findViewById(R.id.etPrecioMaterialNuevoHistorial2);
        etOtroTratamientoNuevoHistorial2 = view.findViewById(R.id.etOtroTratamientoNuevoHistorial2);
        etPrecioTratamientoNuevoHistorial2 = view.findViewById(R.id.etPrecioTratamientoNuevoHistorial2);
        etObservacionesNuevoHistorial2 = view.findViewById(R.id.etObservacionesNuevoHistorial2);
        etObservacionesInternoNuevoHistorial2 = view.findViewById(R.id.etObservacionesInternoNuevoHistorial2);
        etEmbarazadaNuevoHistorial2 = view.findViewById(R.id.etEmbarazadaNuevoHistorial2);
        etDurmioSeisOchoHorasNuevoHistorial2 = view.findViewById(R.id.etDurmioSeisOchoHorasNuevoHistorial2);
        etActividadDiaNuevoHistorial2 = view.findViewById(R.id.etActividadDiaNuevoHistorial2);
        etProblemasOjosNuevoHistorial2 = view.findViewById(R.id.etProblemasOjosNuevoHistorial2);
        etOtroBifocalNuevoHistorial2 = view.findViewById(R.id.etOtroBifocalNuevoHistorial2);
        etPrecioBifocalNuevoHistorial2 = view.findViewById(R.id.etPrecioBifocalNuevoHistorial2);
        //RadioButtons
        rgTipoBifocalNuevoHistorial2 = view.findViewById(R.id.rgTipoBifocalNuevoHistorial2);
        rbHiIndex2 = view.findViewById(R.id.rbHiIndex2);
        rbCR2 = view.findViewById(R.id.rbCR2);
        rbPolicarbonato2 = view.findViewById(R.id.rbPolicarbonato2);
        rbOtroMaterial2 = view.findViewById(R.id.rbOtroMaterial2);
        rbFT2 = view.findViewById(R.id.rbFT2);
        rbBlend2 = view.findViewById(R.id.rbBlend2);
        rbProgresivo2 = view.findViewById(R.id.rbProgresivo2);
        rbNA2 = view.findViewById(R.id.rbNA2);
        rbOtroBifocal2 = view.findViewById(R.id.rbOtroBifocal2);
        //CheckBox
        cbDolorCabeza2 = view.findViewById(R.id.cbDolorCabeza2);
        cbArdorOjos2 = view.findViewById(R.id.cbArdorOjos2);
        cbGolpeCabeza2 = view.findViewById(R.id.cbGolpeCabeza2);
        cbOtroMolestia2 = view.findViewById(R.id.cbOtroMolestia2);
        cbTratamientoFotocromatico2 = view.findViewById(R.id.cbTratamientoFotocromatico2);
        cbTratamientoAR2 = view.findViewById(R.id.cbTratamientoAR2);
        cbTratamientoTinte2 = view.findViewById(R.id.cbTratamientoTinte2);
        cbTratamientoBlueray2 = view.findViewById(R.id.cbTratamientoBlueray2);
        cbTratamientoOtro2 = view.findViewById(R.id.cbTratamientoOtro2);
        cbTratamientoPolarizado2 = view.findViewById(R.id.cbTratamientoPolarizado2);
        cbPolicarbonatoTipo2 = view.findViewById(R.id.cbPolicarbonatoTipo2);
        cbTratamientoEspejeado2 = view.findViewById(R.id.cbTratamientoEspejeado2);
        //Spinners
        spProductoNuevoHistorial2 = view.findViewById(R.id.spProductoNuevoHistorial2);
        spPaquetesNuevoHistorial2 = view.findViewById(R.id.spPaquetesNuevoHistorial2);
        spColorTratamiento2 = view.findViewById(R.id.spColorTratamiento2);
        spEstiloTratamiento2 = view.findViewById(R.id.spEstiloTratamiento2);
        spColorTratamientoPolarizado2 = view.findViewById(R.id.spColorTratamientoPolarizado2);
        spColorTratamientoEspejeado2 = view.findViewById(R.id.spColorTratamientoEspejeado2);
        //LinearLayout tratamiento
        llColorEstiloTinte2 = view.findViewById(R.id.llColorEstiloTinte2);
        llTratamientoPolarizado2 = view.findViewById(R.id.llTratamientoPolarizado2);
        llTratamientoEspejeado2 = view.findViewById(R.id.llTratamientoEspejeado2);
        llFotoArmazonPropioHistorial2 = view.findViewById(R.id.llFotoArmazonPropioHistorial2);
        //TextView
        tvProductoNuevoHistorial2 = view.findViewById(R.id.tvProductoNuevoHistorial2);
        tvFechaEntregaNuevoHistorial2 = view.findViewById(R.id.tvFechaEntregaNuevoHistorial2);
        tvOjoDerechoEsfericoNuevoHistorial2 = view.findViewById(R.id.tvOjoDerechoEsfericoNuevoHistorial2);
        tvOjoDerechoCilindroNuevoHistorial2 = view.findViewById(R.id.tvOjoDerechoCilindroNuevoHistorial2);
        tvOjoDerechoEjeNuevoHistorial2 = view.findViewById(R.id.tvOjoDerechoEjeNuevoHistorial2);
        tvOjoDerechoAddNuevoHistorial2 = view.findViewById(R.id.tvOjoDerechoAddNuevoHistorial2);
        tvOjoDerechoALTNuevoHistorial2 = view.findViewById(R.id.tvOjoDerechoALTNuevoHistorial2);
        tvOjoIzquierdoEsfericoNuevoHistorial2 = view.findViewById(R.id.tvOjoIzquierdoEsfericoNuevoHistorial2);
        tvOjoIzquierdoCilindroNuevoHistorial2 = view.findViewById(R.id.tvOjoIzquierdoCilindroNuevoHistorial2);
        tvOjoIzquierdoEjeNuevoHistorial2 = view.findViewById(R.id.tvOjoIzquierdoEjeNuevoHistorial2);
        tvOjoIzquierdoAddNuevoHistorial2 = view.findViewById(R.id.tvOjoIzquierdoAddNuevoHistorial2);
        tvOjoIzquierdoALTNuevoHistorial2 = view.findViewById(R.id.tvOjoIzquierdoALTNuevoHistorial2);
        tvOtroMaterialNuevoHistorial2 = view.findViewById(R.id.tvOtroMaterialNuevoHistorial2);
        tvPrecioMaterialNuevoHistorial2 = view.findViewById(R.id.tvPrecioMaterialNuevoHistorial2);
        tvOtroTratamientoNuevoHistorial2 = view.findViewById(R.id.tvOtroTratamientoNuevoHistorial2);
        tvPrecioTratamientoNuevoHistorial2 = view.findViewById(R.id.tvPrecioTratamientoNuevoHistorial2);
        tvTipoBifocalNuevoHistorial2 = view.findViewById(R.id.tvTipoBifocalNuevoHistorial2);
        llVisionCercana = view.findViewById(R.id.llVisionCercana);
        tvVisionCercana = view.findViewById(R.id.tvVisionCercana);
        tvOtroBifocalNuevoHistorial2 = view.findViewById(R.id.tvOtroBifocalNuevoHistorial2);
        tvPrecioBifocalNuevoHistorial2 = view.findViewById(R.id.tvPrecioBifocalNuevoHistorial2);
        tvColorTratamiento2 = view.findViewById(R.id.tvColorTratamiento2);
        tvEstiloTratamiento2 = view.findViewById(R.id.tvEstiloTratamiento2);
        tvColorTratamientoPolarizado2 = view.findViewById(R.id.tvColorTratamientoPolarizado2);
        tvColorTratamientoEspejeado2 = view.findViewById(R.id.tvColorTratamientoEspejeado2);
        tvFotoArmazonPropioHistorial2 = view.findViewById(R.id.tvFotoArmazonPropioHistorial2);

        btnGuardarHistorialNuevo2 = view.findViewById(R.id.btnGuardarHistorialNuevo2);
        btnRegresarHistorialNuevo2 = view.findViewById(R.id.btnRegresarHistorialNuevo2);

        //CONTRATO
        //EditText
        etNombreCliente2 = view.findViewById(R.id.etNombreCliente2);

        llDatosSinConversionNuevoHistorial2 = view.findViewById(R.id.llDatosSinConversionNuevoHistorial2);
        llVisionCercanaSinConversion = view.findViewById(R.id.llVisionCercanaSinConversion);
        etOjoDerechoEsfericoSinConversionNuevoHistorial2 = view.findViewById(R.id.etOjoDerechoEsfericoSinConversionNuevoHistorial2);
        etOjoDerechoCilindroSinConversionNuevoHistorial2 = view.findViewById(R.id.etOjoDerechoCilindroSinConversionNuevoHistorial2);
        etOjoDerechoEjeSinConversionNuevoHistorial2 = view.findViewById(R.id.etOjoDerechoEjeSinConversionNuevoHistorial2);
        etOjoDerechoAddSinConversionNuevoHistorial2 = view.findViewById(R.id.etOjoDerechoAddSinConversionNuevoHistorial2);
        etOjoIzquierdoEsfericoSinConversionNuevoHistorial2 = view.findViewById(R.id.etOjoIzquierdoEsfericoSinConversionNuevoHistorial2);
        etOjoIzquierdoCilindroSinConversionNuevoHistorial2 = view.findViewById(R.id.etOjoIzquierdoCilindroSinConversionNuevoHistorial2);
        etOjoIzquierdoEjeSinConversionNuevoHistorial2 = view.findViewById(R.id.etOjoIzquierdoEjeSinConversionNuevoHistorial2);
        etOjoIzquierdoAddSinConversionNuevoHistorial2 = view.findViewById(R.id.etOjoIzquierdoAddSinConversionNuevoHistorial2);
        tvGeneralSinConversionNuevoHistorial2 = view.findViewById(R.id.tvGeneralSinConversionNuevoHistorial2);
        tvVisionCercanaSinConversion = view.findViewById(R.id.tvVisionCercanaSinConversion);

        //TEXTVIEW
        //Encabezados
        tvNuevoHistorialClinico = view.findViewById(R.id.tvNuevoHistorialClinico);
        tvProductoNuevoHistorial = view.findViewById(R.id.tvProductoNuevoHistorial);
        tvNuevoHistorialSC = view.findViewById(R.id.tvNuevoHistorialSC);
        //tvVisionCercanaSinConversion ya declarado
        tvOjoDerechoNuevoHistorialSC = view.findViewById(R.id.tvOjoDerechoNuevoHistorialSC);
        tvOjoIzquierdoNuevoHistorialSC = view.findViewById(R.id.tvOjoIzquierdoNuevoHistorialSC);
        tvNuevoHistorialCC = view.findViewById(R.id.tvNuevoHistorialCC);
        //tvVisionCercana ya declarado
        tvOjoDerechoNuevoHistorialCC = view.findViewById(R.id.tvOjoDerechoNuevoHistorialCC);
        tvOjoIzquierdoNuevoHistorialCC = view.findViewById(R.id.tvOjoIzquierdoNuevoHistorialCC);
        tvMaterialNuevoHistorial = view.findViewById(R.id.tvMaterialNuevoHistorial);
        tvTratamientoNuevoHistorial = view.findViewById(R.id.tvTratamientoNuevoHistorial);
        tvTipoBifocalNuevoHistorial = view.findViewById(R.id.tvTipoBifocalNuevoHistorial);
        //ImagenView
        ivFotoArmazonPropioHistorial2 = view.findViewById(R.id.ivFotoArmazonPropioHistorial2);
        //Configuracion de diseño para encabezados
        global.verificarConfiguracionEncabezados(tvNuevoHistorialClinico);
        global.verificarConfiguracionEncabezados(tvProductoNuevoHistorial);
        global.verificarConfiguracionEncabezados(tvNuevoHistorialSC);
        global.verificarConfiguracionEncabezados(tvVisionCercanaSinConversion);
        global.verificarConfiguracionEncabezados(tvOjoDerechoNuevoHistorialSC);
        global.verificarConfiguracionEncabezados(tvOjoIzquierdoNuevoHistorialSC);
        global.verificarConfiguracionEncabezados(tvNuevoHistorialCC);
        global.verificarConfiguracionEncabezados(tvVisionCercana);
        global.verificarConfiguracionEncabezados(tvOjoDerechoNuevoHistorialCC);
        global.verificarConfiguracionEncabezados(tvOjoIzquierdoNuevoHistorialCC);
        global.verificarConfiguracionEncabezados(tvMaterialNuevoHistorial);
        global.verificarConfiguracionEncabezados(tvTratamientoNuevoHistorial);
        global.verificarConfiguracionEncabezados(tvTipoBifocalNuevoHistorial);

        banderaSigno = new boolean[6];
        Arrays.fill(banderaSigno, Boolean.FALSE);

        String ultimoIdContratoCreado = "";
        String ultimoIdHistorialClinicoCreado = "";
        boolean modificarHistorialClinico = false;
        Bundle bundle = getArguments();
        if(bundle != null){
            ultimoIdContratoCreado = bundle.getString("ultimoIdContratoCreado");
            ultimoIdHistorialClinicoCreado = bundle.getString("ultimoIdHistorialClinicoCreado");
            modificarHistorialClinico = bundle.getBoolean("modificarHistorialClinico");
            posicionLista = bundle.getInt("posicionLista");
        }

        //Inicialización de Clases
        if(global.contadorContratosOHistorialesTablaGarantias(ultimoIdContratoCreado, ultimoIdHistorialClinicoCreado, true) > 0
            || global.obtenerAtributoTabla("HISTORIALCLINICO", "TIPO", "ID", ultimoIdHistorialClinicoCreado).equals("1")) {
            esHistorialGarantia = true;
        }

        Object[] objetos = {etEdadNuevoHistorial2, etDiagnosticoNuevoHistorial2, etOcupacionNuevoHistorial2, etDiabetesNuevoHistorial2,
                etHipertensionNuevoHistorial2, cbDolorCabeza2, cbArdorOjos2, cbGolpeCabeza2, cbOtroMolestia2, etOtroMoletiaNuevoHistorial2,
                etUltimoExamenNuevoHistorial2, spProductoNuevoHistorial2, spPaquetesNuevoHistorial2, etFechaEntregaNuevoHistorial2,
                etOjoDerechoEsfericoNuevoHistorial2, etOjoDerechoCilindroNuevoHistorial2, etOjoDerechoEjeNuevoHistorial2, etOjoDerechoAddNuevoHistorial2,
                etOjoDerechoALTNuevoHistorial2, etOjoIzquierdoEsfericoNuevoHistorial2, etOjoIzquierdoCilindroNuevoHistorial2,
                etOjoIzquierdoEjeNuevoHistorial2, etOjoIzquierdoAddNuevoHistorial2, etOjoIzquierdoALTNuevoHistorial2,
                rbHiIndex2, rbCR2, rbPolicarbonato2, rbOtroMaterial2, etOtroMaterialNuevoHistorial2, etPrecioMaterialNuevoHistorial2, cbTratamientoFotocromatico2,
                cbTratamientoAR2, cbTratamientoTinte2, cbTratamientoBlueray2, cbTratamientoOtro2, etOtroTratamientoNuevoHistorial2,
                etPrecioTratamientoNuevoHistorial2, rbFT2, rbBlend2, rbProgresivo2, rbNA2, etObservacionesNuevoHistorial2,
                tvProductoNuevoHistorial2, tvFechaEntregaNuevoHistorial2, tvOjoDerechoEsfericoNuevoHistorial2, tvOjoDerechoCilindroNuevoHistorial2,
                tvOjoDerechoEjeNuevoHistorial2, tvOjoDerechoAddNuevoHistorial2, tvOjoDerechoALTNuevoHistorial2,
                tvOjoIzquierdoEsfericoNuevoHistorial2, tvOjoIzquierdoCilindroNuevoHistorial2, tvOjoIzquierdoEjeNuevoHistorial2,
                tvOjoIzquierdoAddNuevoHistorial2, tvOjoIzquierdoALTNuevoHistorial2, tvOtroMaterialNuevoHistorial2,
                tvPrecioMaterialNuevoHistorial2, tvOtroTratamientoNuevoHistorial2, tvPrecioTratamientoNuevoHistorial2, tvTipoBifocalNuevoHistorial2,
                ultimoIdContratoCreado, ultimoIdHistorialClinicoCreado, etNombreCliente2, modificarHistorialClinico, btnGuardarHistorialNuevo2,
                llVisionCercana, etObservacionesInternoNuevoHistorial2, esHistorialGarantia, etOjoDerechoEsfericoSinConversionNuevoHistorial2,
                etOjoDerechoCilindroSinConversionNuevoHistorial2, etOjoDerechoEjeSinConversionNuevoHistorial2, etOjoDerechoAddSinConversionNuevoHistorial2,
                etOjoIzquierdoEsfericoSinConversionNuevoHistorial2, etOjoIzquierdoCilindroSinConversionNuevoHistorial2, etOjoIzquierdoEjeSinConversionNuevoHistorial2,
                etOjoIzquierdoAddSinConversionNuevoHistorial2, tvGeneralSinConversionNuevoHistorial2, etEmbarazadaNuevoHistorial2, etDurmioSeisOchoHorasNuevoHistorial2,
                etActividadDiaNuevoHistorial2, etProblemasOjosNuevoHistorial2, etOtroBifocalNuevoHistorial2, etPrecioBifocalNuevoHistorial2, rbOtroBifocal2,
                tvOtroBifocalNuevoHistorial2, tvPrecioBifocalNuevoHistorial2, llColorEstiloTinte2, spColorTratamiento2, spEstiloTratamiento2, tvColorTratamiento2,
                tvEstiloTratamiento2, cbTratamientoPolarizado2, llTratamientoPolarizado2, spColorTratamientoPolarizado2, tvColorTratamientoPolarizado2,
                cbTratamientoEspejeado2, llTratamientoEspejeado2, spColorTratamientoEspejeado2, tvColorTratamientoEspejeado2,
                cbPolicarbonatoTipo2, rgTipoBifocalNuevoHistorial2, llFotoArmazonPropioHistorial2, ivFotoArmazonPropioHistorial2, tvFotoArmazonPropioHistorial2};

        controlador = new Controlador(fragmento, objetos);
        teclado = new Teclado(fragmento);

        controlador.llenarCamposHistorialBD();

        if(modificarHistorialClinico && !esHistorialGarantia) {
            btnRegresarHistorialNuevo2.setVisibility(View.VISIBLE);
        }

        //Accion Spinner
        spProductoNuevoHistorial2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                controlador.validarArmazonSeleccionado();
                controlador.verificarCampoFechaEntregaEstaVacio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Acciones ImagenView
        ivFotoArmazonPropioHistorial2.setOnClickListener(new View.OnClickListener() {
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
                        camara.openCamera(ivFotoArmazonPropioHistorial2, 8);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivFotoArmazonPropioHistorial2, 8);
                }
            }
        });

        //Acciones EditTextDatePicker
        etFechaEntregaNuevoHistorial2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.showDatePickerDialog(etFechaEntregaNuevoHistorial2);
            }
        });

        //Acciones CheckBox
        cbOtroMolestia2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {
                    etOtroMoletiaNuevoHistorial2.setEnabled(true);
                }else {
                    etOtroMoletiaNuevoHistorial2.setEnabled(false);
                    etOtroMoletiaNuevoHistorial2.setText("");
                }

            }
        });

        cbTratamientoFotocromatico2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 1) {
                    if ( isChecked ) {
                        cbTratamientoAR2.setEnabled(false);
                        cbTratamientoBlueray2.setEnabled(false);

                        cbTratamientoAR2.setChecked(false);
                        cbTratamientoBlueray2.setChecked(false);

                    }else {
                        cbTratamientoAR2.setEnabled(true);
                        cbTratamientoBlueray2.setEnabled(true);
                    }
                }else {
                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 8){ //Paquete premium
                        if(cbTratamientoFotocromatico2.isChecked()){
                            cbTratamientoTinte2.setChecked(false);
                            cbTratamientoTinte2.setEnabled(false);
                            spColorTratamiento2.setSelection(0);
                            spEstiloTratamiento2.setSelection(0);
                            cbTratamientoPolarizado2.setChecked(false);
                            cbTratamientoPolarizado2.setEnabled(false);
                            spColorTratamientoPolarizado2.setSelection(0);
                            cbTratamientoEspejeado2.setChecked(false);
                            cbTratamientoEspejeado2.setEnabled(false);
                            spColorTratamientoEspejeado2.setSelection(0);
                            cbTratamientoOtro2.setChecked(false);
                            etOtroTratamientoNuevoHistorial2.setText("");
                            etOtroTratamientoNuevoHistorial2.setEnabled(false);
                            etPrecioTratamientoNuevoHistorial2.setText("");
                            etPrecioTratamientoNuevoHistorial2.setEnabled(false);
                        }else{
                            cbTratamientoTinte2.setChecked(false);
                            cbTratamientoTinte2.setEnabled(true);
                            spColorTratamiento2.setSelection(0);
                            spEstiloTratamiento2.setSelection(0);
                            cbTratamientoPolarizado2.setChecked(false);
                            cbTratamientoPolarizado2.setEnabled(true);
                            spColorTratamientoPolarizado2.setSelection(0);
                            cbTratamientoEspejeado2.setChecked(false);
                            cbTratamientoEspejeado2.setEnabled(true);
                            spColorTratamientoEspejeado2.setSelection(0);
                            cbTratamientoOtro2.setChecked(false);
                            etOtroTratamientoNuevoHistorial2.setText("");
                            etOtroTratamientoNuevoHistorial2.setEnabled(true);
                            etPrecioTratamientoNuevoHistorial2.setText("");
                            etPrecioTratamientoNuevoHistorial2.setEnabled(true);
                        }

                    }else {
                        if (cbTratamientoTinte2.isChecked()) {
                            cbTratamientoFotocromatico2.setChecked(false);
                            cbTratamientoFotocromatico2.setEnabled(false);
                            cbTratamientoAR2.setChecked(false);
                            cbTratamientoAR2.setEnabled(false);
                            cbTratamientoBlueray2.setChecked(false);
                            cbTratamientoBlueray2.setEnabled(false);
                        }
                    }

                }

            }
        });

        cbTratamientoAR2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 1) {
                    if ( isChecked ) {
                        cbTratamientoFotocromatico2.setEnabled(false);
                        cbTratamientoBlueray2.setEnabled(false);

                        cbTratamientoFotocromatico2.setChecked(false);
                        cbTratamientoBlueray2.setChecked(false);
                    }else {
                        cbTratamientoFotocromatico2.setEnabled(true);
                        cbTratamientoBlueray2.setEnabled(true);
                    }
                }else {
                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() != 8) {
                        //Paquete diferente de PREMIUM
                        if (isChecked) {
                            cbTratamientoBlueray2.setChecked(false);
                            cbTratamientoBlueray2.setEnabled(false);
                            if (cbTratamientoTinte2.isChecked()) {
                                cbTratamientoAR2.setChecked(false);
                                cbTratamientoAR2.setEnabled(false);
                            }
                        } else {
                            cbTratamientoBlueray2.setChecked(false);
                            cbTratamientoBlueray2.setEnabled(true);
                            if (cbTratamientoTinte2.isChecked()) {
                                cbTratamientoAR2.setChecked(false);
                                cbTratamientoAR2.setEnabled(false);
                                cbTratamientoFotocromatico2.setEnabled(false);
                                cbTratamientoBlueray2.setEnabled(false);
                            }
                        }
                    }else{
                        //Es paquete PREMIUM
                        if(isChecked){
                            cbTratamientoBlueray2.setEnabled(false);
                            cbTratamientoBlueray2.setChecked(false);
                        }else{
                            cbTratamientoBlueray2.setEnabled(true);
                        }
                    }
                }

            }
        });

        cbTratamientoTinte2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {
                    cbTratamientoFotocromatico2.setEnabled(false);
                    cbTratamientoAR2.setEnabled(false);
                    cbTratamientoBlueray2.setEnabled(false);

                    cbTratamientoFotocromatico2.setChecked(false);
                    cbTratamientoAR2.setChecked(false);
                    cbTratamientoBlueray2.setChecked(false);
                    cbTratamientoOtro2.setChecked(false);

                    llColorEstiloTinte2.setVisibility(View.VISIBLE);
                }else {
                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 1) {
                        cbTratamientoFotocromatico2.setEnabled(true);
                        cbTratamientoAR2.setEnabled(true);
                        cbTratamientoBlueray2.setEnabled(true);
                    }else {
                        cbTratamientoFotocromatico2.setEnabled(true);
                        cbTratamientoAR2.setEnabled(true);
                        cbTratamientoBlueray2.setEnabled(true);
                        cbTratamientoOtro2.setEnabled(true);

                        blueRayActivo = false;
                    }

                    spColorTratamiento2.setSelection(0);
                    spEstiloTratamiento2.setSelection(0);
                    llColorEstiloTinte2.setVisibility(View.GONE);
                }
            }
        });

        cbTratamientoBlueray2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 1) {
                    if ( isChecked ) {
                        cbTratamientoFotocromatico2.setChecked(false);
                        cbTratamientoAR2.setChecked(false);

                        cbTratamientoFotocromatico2.setEnabled(false);
                        cbTratamientoAR2.setEnabled(false);
                        blueRayActivo = true;

                    }else {
                        if(!blueRayActivo){
                            cbTratamientoAR2.setChecked(true);
                            cbTratamientoAR2.setEnabled(true);
                        }

                        if(!cbTratamientoTinte2.isChecked() && blueRayActivo == true){
                            cbTratamientoFotocromatico2.setChecked(false);
                            cbTratamientoAR2.setChecked(false);
                            cbTratamientoFotocromatico2.setEnabled(true);
                            cbTratamientoAR2.setEnabled(true);
                            blueRayActivo = false;
                        }

                    }
                }else {
                    if(spPaquetesNuevoHistorial2.getSelectedItemPosition() != 8) {
                        //Diferente de paquete PREMIUM
                        if ( isChecked ) {
                            cbTratamientoAR2.setChecked(false);
                            cbTratamientoAR2.setEnabled(false);
                            blueRayActivo = true;
                        }else {
                            if(!blueRayActivo){
                                cbTratamientoAR2.setChecked(true);
                                cbTratamientoAR2.setEnabled(true);
                            }

                            if(!cbTratamientoTinte2.isChecked() && blueRayActivo == true){
                                cbTratamientoAR2.setChecked(false);
                                cbTratamientoAR2.setEnabled(true);
                                blueRayActivo = false;
                            }

                        }
                    }else{
                        //Es paquete PREMIUM
                        if(isChecked){
                            cbTratamientoAR2.setEnabled(false);
                            cbTratamientoAR2.setChecked(false);
                        }else {
                            cbTratamientoAR2.setEnabled(true);
                        }
                    }
                }

            }
        });

        cbTratamientoOtro2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {
                    etOtroTratamientoNuevoHistorial2.setEnabled(true);
                    etPrecioTratamientoNuevoHistorial2.setEnabled(true);
                    etOtroTratamientoNuevoHistorial2.requestFocus();
                    teclado.showKeyboard();
                }else {
                    etOtroTratamientoNuevoHistorial2.setEnabled(false);
                    etPrecioTratamientoNuevoHistorial2.setEnabled(false);
                    etOtroTratamientoNuevoHistorial2.setText("");
                    etPrecioTratamientoNuevoHistorial2.setText("");
                }
            }
        });
        cbPolicarbonatoTipo2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(cbPolicarbonatoTipo2.isChecked()){
                    cbPolicarbonatoTipo2.setText("Niño");
                }else{
                    cbPolicarbonatoTipo2.setText("Adulto");
                }
            }
        });
        cbTratamientoPolarizado2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 8) {

                    if (isChecked) {
                        llTratamientoPolarizado2.setVisibility(View.VISIBLE);
                        cbTratamientoEspejeado2.setChecked(false);
                        cbTratamientoEspejeado2.setEnabled(false);
                        spColorTratamientoEspejeado2.setSelection(0);
                    }else{
                        llTratamientoPolarizado2.setVisibility(View.GONE);
                        cbTratamientoEspejeado2.setEnabled(true);
                        cbTratamientoEspejeado2.setChecked(false);
                        spColorTratamientoPolarizado2.setSelection(0);
                    }

                }
            }
        });

        cbTratamientoEspejeado2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(spPaquetesNuevoHistorial2.getSelectedItemPosition() == 8) {
                    if(isChecked){
                        llTratamientoEspejeado2.setVisibility(View.VISIBLE);
                        cbTratamientoPolarizado2.setChecked(false);
                        cbTratamientoPolarizado2.setEnabled(false);
                        spColorTratamientoPolarizado2.setSelection(0);
                    }else{
                        llTratamientoEspejeado2.setVisibility(View.GONE);
                        cbTratamientoPolarizado2.setChecked(false);
                        cbTratamientoPolarizado2.setEnabled(true);
                        spColorTratamientoEspejeado2.setSelection(0);
                    }
                }
            }
        });


        //Acciones RadioButton
        rbHiIndex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked)
                    etOtroMaterialNuevoHistorial2.setEnabled(false);
                    etPrecioMaterialNuevoHistorial2.setEnabled(false);
                    etOtroMaterialNuevoHistorial2.setText("");
                    etPrecioMaterialNuevoHistorial2.setText("");
                    cbPolicarbonatoTipo2.setText("Adulto");
                    cbPolicarbonatoTipo2.setChecked(false);
                    cbPolicarbonatoTipo2.setVisibility(View.GONE);

            }
        });

        rbCR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked)
                    etOtroMaterialNuevoHistorial2.setEnabled(false);
                    etPrecioMaterialNuevoHistorial2.setEnabled(false);
                    etOtroMaterialNuevoHistorial2.setText("");
                    etPrecioMaterialNuevoHistorial2.setText("");
                    cbPolicarbonatoTipo2.setText("Adulto");
                    cbPolicarbonatoTipo2.setChecked(false);
                    cbPolicarbonatoTipo2.setVisibility(View.GONE);

            }
        });

        rbPolicarbonato2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked)
                    etOtroMaterialNuevoHistorial2.setEnabled(false);
                    etPrecioMaterialNuevoHistorial2.setEnabled(false);
                    etOtroMaterialNuevoHistorial2.setText("");
                    etPrecioMaterialNuevoHistorial2.setText("");
                    cbPolicarbonatoTipo2.setChecked(false);
                    cbPolicarbonatoTipo2.setVisibility(View.VISIBLE);

            }
        });

        rbOtroMaterial2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    etOtroMaterialNuevoHistorial2.setEnabled(true);
                    etPrecioMaterialNuevoHistorial2.setEnabled(true);
                    cbPolicarbonatoTipo2.setText("Adulto");
                    cbPolicarbonatoTipo2.setChecked(false);
                    cbPolicarbonatoTipo2.setVisibility(View.GONE);
                    etOtroMaterialNuevoHistorial2.requestFocus();
                    teclado.showKeyboard();
                }
            }
        });

        rbFT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked) {
                    etOtroBifocalNuevoHistorial2.setEnabled(false);
                    etPrecioBifocalNuevoHistorial2.setEnabled(false);
                    etOtroBifocalNuevoHistorial2.setText("");
                    etPrecioBifocalNuevoHistorial2.setText("");
                    controlador.llenarSpTratamientoTinte();
                }
            }
        });

        rbBlend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked) {
                    etOtroBifocalNuevoHistorial2.setEnabled(false);
                    etPrecioBifocalNuevoHistorial2.setEnabled(false);
                    etOtroBifocalNuevoHistorial2.setText("");
                    etPrecioBifocalNuevoHistorial2.setText("");
                    controlador.llenarSpTratamientoTinte();
                }
            }
        });

        rbProgresivo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked) {
                    etOtroBifocalNuevoHistorial2.setEnabled(false);
                    etPrecioBifocalNuevoHistorial2.setEnabled(false);
                    etOtroBifocalNuevoHistorial2.setText("");
                    etPrecioBifocalNuevoHistorial2.setText("");
                    controlador.llenarSpTratamientoTinte();
                }
            }
        });

        rbNA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked) {
                    etOtroBifocalNuevoHistorial2.setEnabled(false);
                    etPrecioBifocalNuevoHistorial2.setEnabled(false);
                    etOtroBifocalNuevoHistorial2.setText("");
                    etPrecioBifocalNuevoHistorial2.setText("");
                    controlador.llenarSpTratamientoTinte();
                }
            }
        });

        rbOtroBifocal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    etOtroBifocalNuevoHistorial2.setEnabled(true);
                    etPrecioBifocalNuevoHistorial2.setEnabled(true);
                    etOtroBifocalNuevoHistorial2.requestFocus();
                    controlador.llenarSpTratamientoTinte();
                    teclado.showKeyboard();
                }
            }
        });

        //Accion Spinners
        spPaquetesNuevoHistorial2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0) { //Seleccionar
                    controlador.validarComponentesPaqueteTres(true);
                }
                if(position == 1) { //LECTURA
                    llDatosSinConversionNuevoHistorial2.setVisibility(View.VISIBLE);
                    controlador.validarComponentesPaqueteUno(true);
                    controlador.verificarCampoFechaEntregaEstaVacio();
                    controlador.requestFocusCamposOjoDerIzqPaqueteUno();
                }
                if(position == 2) { //PROTECCION
                    controlador.validarComponentesPaqueteDos();
                    controlador.verificarCampoFechaEntregaEstaVacio();
                }
                if(position == 3) { //ECO JR
                    controlador.validarComponentesPaqueteUno(false);
                    controlador.verificarCampoFechaEntregaEstaVacio();
                    controlador.requestFocusCamposOjoDerIzqPaqueteUno();
                }
                if(position == 4) { //JR
                    controlador.validarComponentesPaqueteUno(false);
                    controlador.verificarCampoFechaEntregaEstaVacio();
                    controlador.requestFocusCamposOjoDerIzqPaqueteUno();
                }
                if(position == 5) { //DORADO 1
                    controlador.validarComponentesPaqueteTres(true);
                    controlador.verificarCampoFechaEntregaEstaVacio();
                    controlador.requestFocusCamposOjoDerIzqPaqueteTres();
                }
                if(position == 6) { //DORADO 2
                    llVisionCercana.setVisibility(View.VISIBLE);
                    llDatosSinConversionNuevoHistorial2.setVisibility(View.VISIBLE);
                    llVisionCercanaSinConversion.setVisibility(View.VISIBLE);
                    if(posicionLista == 0 || posicionLista == 2) {
                        tvVisionCercana.setText("Visión lejana");
                        tvVisionCercanaSinConversion.setText("Visión lejana");
                    }else {
                        tvVisionCercana.setText("Visión cercana");
                        tvVisionCercanaSinConversion.setText("Visión cercana");
                    }
                    controlador.validarComponentesPaqueteUno(false);
                    controlador.verificarCampoFechaEntregaEstaVacio();
                    controlador.requestFocusCamposOjoDerIzqPaqueteUno();
                }
                if(position == 7) { //PLATINO
                    controlador.validarComponentesPaqueteTres(true);
                    controlador.verificarCampoFechaEntregaEstaVacio();
                    controlador.requestFocusCamposOjoDerIzqPaqueteTres();
                }
                if(position == 8){ //PREMIUM
                    controlador.validarComponentesPaquetePremium();
                    controlador.verificarCampoFechaEntregaEstaVacio();
                }

                controlador.validarTipoBifocalPaquetesGraduacion(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //Acciones Botones
        btnGuardarHistorialNuevo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.validacionPaquete();
            }
        });

        btnRegresarHistorialNuevo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.regresarPantallaVerContrato();
            }
        });

        //Acciones EditText
        etFechaEntregaNuevoHistorial2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                teclado.showKeyboard();
                if(llDatosSinConversionNuevoHistorial2.getVisibility() == View.VISIBLE) {
                    //Paquete LECTURA o DORADO 2
                    etOjoDerechoEsfericoSinConversionNuevoHistorial2.requestFocus();
                }else {
                    //Paquete diferente a LECTURA o DORADO 2
                    etOjoDerechoEsfericoNuevoHistorial2.requestFocus();
                }
            }
        });
        etOjoDerechoEsfericoSinConversionNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoCilindroSinConversionNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoCilindroSinConversionNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoEjeSinConversionNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoEjeSinConversionNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoAddSinConversionNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoAddSinConversionNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEsfericoSinConversionNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoDerechoAddNuevoHistorial2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    controlador.validarTipoBifocalPaquetesGraduacion(spPaquetesNuevoHistorial2.getSelectedItemPosition());
                }
            }
        });

        etOjoDerechoAddSinConversionNuevoHistorial2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    controlador.validarTipoBifocalPaquetesGraduacion(spPaquetesNuevoHistorial2.getSelectedItemPosition());
                }
            }
        });
        etOjoIzquierdoEsfericoSinConversionNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoCilindroSinConversionNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoCilindroSinConversionNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoEjeSinConversionNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoEjeSinConversionNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoIzquierdoAddSinConversionNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoAddSinConversionNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etOjoDerechoEsfericoNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etOjoIzquierdoAddNuevoHistorial2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    controlador.validarTipoBifocalPaquetesGraduacion(spPaquetesNuevoHistorial2.getSelectedItemPosition());
                }
            }
        });
        etOjoIzquierdoAddSinConversionNuevoHistorial2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    controlador.validarTipoBifocalPaquetesGraduacion(spPaquetesNuevoHistorial2.getSelectedItemPosition());
                }
            }
        });
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

        etOtroMaterialNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etPrecioMaterialNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etPrecioMaterialNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });
        etOtroTratamientoNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etPrecioTratamientoNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etPrecioTratamientoNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });
        etObservacionesNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etObservacionesInternoNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etObservacionesInternoNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });
        etOtroBifocalNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    etPrecioBifocalNuevoHistorial2.requestFocus();
                    return true;
                }

                return false;
            }
        });
        etPrecioBifocalNuevoHistorial2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP) {
                    teclado.hideKeyboard();
                    return true;
                }

                return false;
            }
        });

        etOjoDerechoEsfericoNuevoHistorial2.addTextChangedListener(new TextWatcher() {
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
                        etOjoDerechoEsfericoNuevoHistorial2.setText(sb);
                    } else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[0] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoDerechoEsfericoNuevoHistorial2.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoDerechoEsfericoNuevoHistorial2.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[0] = false;
                    }


                }catch (Exception e) {
                    global.escribirError(e, 243);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoDerechoCilindroNuevoHistorial2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {
                    if(position == 0 && !banderaSigno[1] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[1] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '+')) || (position < charSequence.toString().length() && banderaSigno[1]
                            && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoDerechoCilindroNuevoHistorial2.setText(sb);
                    } else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[1] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoDerechoCilindroNuevoHistorial2.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoDerechoCilindroNuevoHistorial2.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[1] = false;
                    }
                }catch (Exception e) {
                    global.escribirError(e, 244);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoDerechoAddNuevoHistorial2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {
                    if(position == 0 && !banderaSigno[2] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[2] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '+')) || (position < charSequence.toString().length()
                            && banderaSigno[2] && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoDerechoAddNuevoHistorial2.setText(sb);
                    }else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[2] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoDerechoAddNuevoHistorial2.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoDerechoAddNuevoHistorial2.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[2] = false;
                    }
                }catch (Exception e) {
                    global.escribirError(e, 245);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoIzquierdoEsfericoNuevoHistorial2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {
                    if(position == 0 && !banderaSigno[3] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[3] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '+')) || (position < charSequence.toString().length()
                            && banderaSigno[3] && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoIzquierdoEsfericoNuevoHistorial2.setText(sb);
                    } else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[3] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoIzquierdoEsfericoNuevoHistorial2.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoIzquierdoEsfericoNuevoHistorial2.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[3] = false;
                    }
                }catch (Exception e) {
                    global.escribirError(e, 246);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoIzquierdoCilindroNuevoHistorial2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {
                    if (position == 0 && !banderaSigno[4] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[4] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '+')) || (position < charSequence.toString().length()
                            && banderaSigno[4] && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoIzquierdoCilindroNuevoHistorial2.setText(sb);
                    }else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[4] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoIzquierdoCilindroNuevoHistorial2.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoIzquierdoCilindroNuevoHistorial2.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[4] = false;
                    }
                }catch (Exception e) {
                    global.escribirError(e, 247);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOjoIzquierdoAddNuevoHistorial2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                try {
                    if (position == 0 && !banderaSigno[5] && (charSequence.toString().contains("-") || charSequence.toString().contains("+"))) {
                        banderaSigno[5] = true;
                    } else if ((position < charSequence.toString().length() && position > 0 && (charSequence.toString().charAt(position) == '-'
                            || charSequence.toString().charAt(position) == '+')) || (position < charSequence.toString().length() && banderaSigno[5]
                            && (charSequence.toString().charAt(position) == '-' || charSequence.toString().charAt(position) == '-') && position > 0)) {
                        StringBuilder sb = new StringBuilder(charSequence.toString());
                        sb.deleteCharAt(position);
                        etOjoIzquierdoAddNuevoHistorial2.setText(sb);
                    }else if (!valirdarSiContieneMenosOMas(charSequence.toString())) {
                        banderaSigno[5] = false;
                    }else if(valorRepetido(charSequence.toString())){
                        etOjoIzquierdoAddNuevoHistorial2.setText(eliminarRepetidos(charSequence.toString()));
                    }else if(tieneUnValorEnMedioOAlFinal(charSequence.toString())){
                        etOjoIzquierdoAddNuevoHistorial2.setText(eliminarValorEnMedioOAlFinal(charSequence.toString()));
                        banderaSigno[5] = false;
                    }
                }catch (Exception e) {
                    global.escribirError(e, 248);
                    Log.i("MENSAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contieneUnSigno();
    }

    private void contieneUnSigno() {


        //etOjoIzquierdoAddNuevoHistorial2
        String etOjoDerechoEsfericoNuevoHistorial2String  = etOjoDerechoEsfericoNuevoHistorial2.getText().toString();
        for(int i = 0;i<etOjoDerechoEsfericoNuevoHistorial2String.length();i++){
            if(etOjoDerechoEsfericoNuevoHistorial2String.charAt(i) == '-' || etOjoDerechoEsfericoNuevoHistorial2String.charAt(i) == '+'){
                banderaSigno[0] = true;
            }
        }
        //etOjoIzquierdoAddNuevoHistorial2
        String etOjoDerechoAddNuevoHistorial2String  = etOjoDerechoAddNuevoHistorial2.getText().toString();
        for(int i = 0;i<etOjoDerechoAddNuevoHistorial2String.length();i++){
            if(etOjoDerechoAddNuevoHistorial2String.charAt(i) == '-' || etOjoDerechoAddNuevoHistorial2String.charAt(i) == '+'){
                banderaSigno[1] = true;
            }
        }
        //etOjoIzquierdoAddNuevoHistorial2
        String etOjoDerechoCilindroNuevoHistorial2String  = etOjoDerechoCilindroNuevoHistorial2.getText().toString();
        for(int i = 0;i<etOjoDerechoCilindroNuevoHistorial2String.length();i++){
            if(etOjoDerechoCilindroNuevoHistorial2String.charAt(i) == '-' || etOjoDerechoCilindroNuevoHistorial2String.charAt(i) == '+'){
                banderaSigno[2] = true;
            }
        }
        //etOjoIzquierdoAddNuevoHistorial2
        String etOjoIzquierdoEsfericoNuevoHistorial2String  = etOjoIzquierdoEsfericoNuevoHistorial2.getText().toString();
        for(int i = 0;i<etOjoIzquierdoEsfericoNuevoHistorial2String.length();i++){
            if(etOjoIzquierdoEsfericoNuevoHistorial2String.charAt(i) == '-' || etOjoIzquierdoEsfericoNuevoHistorial2String.charAt(i) == '+'){
                banderaSigno[3] = true;
            }
        }
        //etOjoIzquierdoAddNuevoHistorial2
        String etOjoIzquierdoCilindroNuevoHistorial2String  = etOjoIzquierdoCilindroNuevoHistorial2.getText().toString();
        for(int i = 0;i<etOjoIzquierdoCilindroNuevoHistorial2String.length();i++){
            if(etOjoIzquierdoCilindroNuevoHistorial2String.charAt(i) == '-' || etOjoIzquierdoCilindroNuevoHistorial2String.charAt(i) == '+'){
                banderaSigno[4] = true;
            }
        }
        //etOjoIzquierdoAddNuevoHistorial2
        String etOjoIzquierdoAddNuevoHistorial2String  = etOjoIzquierdoAddNuevoHistorial2.getText().toString();
        for(int i = 0;i<etOjoIzquierdoAddNuevoHistorial2String.length();i++){
            if(etOjoIzquierdoAddNuevoHistorial2String.charAt(i) == '-' || etOjoIzquierdoAddNuevoHistorial2String.charAt(i) == '+'){
                banderaSigno[5] = true;
            }
        }

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

    }

}