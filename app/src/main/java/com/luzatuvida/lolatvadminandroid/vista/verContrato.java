package com.luzatuvida.lolatvadminandroid.vista;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luzatuvida.lolatvadminandroid.global.Camara;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.verContrato.Controlador;
import com.luzatuvida.lolatvadminandroid.verContrato.FilaAbonos;
import com.luzatuvida.lolatvadminandroid.verContrato.FilaContratosCercanos;
import com.luzatuvida.lolatvadminandroid.verContrato.FilaHistorialesClinicos;
import com.luzatuvida.lolatvadminandroid.verContrato.FilaProductos;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link verContrato#newInstance} factory method to
 * create an instance of this fragment.
 */

public class verContrato extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Declaración de componentes
    EditText etNumeroContrato, etFechaCreacionContrato, etSaldoContrato, etCreadorContrato, etOptometristaAsignado, etClienteContrato, etCalleContrato,
             etNumero, etColoniaContrato, etLocalidadContrato, etTelefonoContrato, etColorCasaContrato, etEntreCallesContrato, etNombreReferenciaContrato,
             etTelefonoReferenciaContrato, etDepartamentoContrato, etFechaCobroIniFechaCobroFinContrato, etSaldoContrato2, etAlLadoDeContrato, etFrenteAContrato,
             etNombrePaqueteContrato, etTotalReal, etTituloPromocionContrato, etAliasContrato, etMovimientoContrato;
    Spinner spFormaPago, spDiaPago, spLugarEntregaProducto;
    TextView tvEstadoPromocion, tvTituloPromocion, tvInicioPromocion, tvFinPromocion, tvDiagnosticoEncabezado, tvDiagnosticoEncabezado2, tvFechaVisitaEncabezado,
            tvFechaVisitaEncabezado2, tvFechaEntregaEncabezado, tvFechaEntregaEncabezado2, tvEstadoContrato, tvTextoContrato, tvNotaContrato;
    LinearLayout llListaPromociones, llTarjetaPension, llAbonos, llListaAbonos, llListaProductos, llSecundario, llPromociones, llHistorialesClinicos, llHistorialesClinicos2, llProductos,
                 llAbonosSecundario, llDiaPago, llCreadorContrato, llOptometristaAsignado, llFormaPago, llTvYBtnProductos, llEntreCalles, llNombreTelefonoReferencia,
                 llDepartamentoContrato, llFechaCobroIniFechaCobroFinContrato, llTotalReal, llReportarGarantia, llNotaContrato, llAlLadoDe, llFrenteA, llNombrePaquete,
                 llTituloPromocion, llBtnMostrarFotoCasaContrato, llLugarVenta, llLugarEntrega, llActualizarFotoPagare, llDivision5, llActualizarFotoCasa, llMovimientoContrato,
                 llHistorialMovimientosContrato, llListaNegra, llListaContratosCercanos, llLugarEntregaProducto;
    TextView btnNuevoHistorial, btnNuevaPromocion, btnAgregarFormaPago, btnNuevoProducto, btnNuevoAbono, btnAplicarDiaPago, btnMostrarFotoCasaContrato, tvReportarGarantia,
                btnMostrarFotoOtros, btnMostrarFotoIneContrato, btnActualizarFotoPagare, btnMostrarFotoPagare, btnActualizarFotoCasa, btnCrearMovimientoContrato,
                tvHistorialMovimientosContrato, tvListaNegra;
    ImageView ivFotoTarjetaPensionFrente, ivFotoTarjetaPensionAtras, ivIconoUbicacion, ivIconoNotaRegistroImprimir, ivActualizarFotoPagare, ivActualizarFotoCasa, ivMovimientoContrato;
    ListView lvHistorialesClinicos, lvProductos, lvAbonos, lvHistorialesClinicos2, lvContratosCercanos;

    ScrollView svScrollPrincipal;
    Button btnMostrarOcultar, btnTerminarContrato;

    EditText etCalleContratoEntrega, etNumeroEntrega, etColoniaContratoEntrega, etLocalidadContratoEntrega, etColorCasaContratoEntrega, etEntreCallesContratoEntrega,
            etDepartamentoContratoEntrega, etAlLadoDeContratoEntrega, etFrenteAContratoEntrega, etObservacionActualizarFotoPagare;
    LinearLayout llCalle, llNumeroDepartamentoColonia, llLocalidadColorCasa;

    //Declaración de clases
    Controlador controlador;
    Camara camara;
    List<FilaHistorialesClinicos> filaHistorialesClinicos;
    List<FilaHistorialesClinicos> filaHistorialesClinicos2;
    List<FilaContratosCercanos> filaContratosCercanos;
    List<FilaProductos> filaProductos;
    List<FilaAbonos> filaAbonos;

    ObtenerRol obtenerRol;
    Runnable runnable;
    String ultimoIdContratoCreado = "";

    Global global;
    boolean garantia = false;
    String estadoReporteContratoListaNegra = "";
    public static boolean banderaInsertarContratoListaNegra = true;

    Fragment fragmento;

    //TEXTVIEW Para agregar promocion eliminar abonos.
    TextView tvPromocionLeyenda;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public verContrato() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment verContrato.
     */
    // TODO: Rename and change types and number of parameters
    public static verContrato newInstance(String param1, String param2) {
        verContrato fragment = new verContrato();
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
        Identificador error del 249 al 251

        Throwable identificador error 7
        */

        fragmento = verContrato.this;
        global = new Global(fragmento);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                global.escribirErrorClasePrincipalThrowable(throwable, 7);
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
        View view = inflater.inflate(R.layout.fragment_ver_contrato, container, false);

        //Inicialización de Componentes
        etNumeroContrato = view.findViewById(R.id.etNumeroContrato);
        etFechaCreacionContrato = view.findViewById(R.id.etFechaCreacionContrato);
        etSaldoContrato = view.findViewById(R.id.etSaldoContrato);
        etCreadorContrato = view.findViewById(R.id.etCreadorContrato);
        etOptometristaAsignado = view.findViewById(R.id.etOptometristaAsignado);
        etClienteContrato = view.findViewById(R.id.etClienteContrato);
        etCalleContrato = view.findViewById(R.id.etCalleContrato);
        etNumero = view.findViewById(R.id.etNumero);
        etColoniaContrato = view.findViewById(R.id.etColoniaContrato);
        etLocalidadContrato = view.findViewById(R.id.etLocalidadContrato);
        etTelefonoContrato = view.findViewById(R.id.etTelefonoContrato);
        etColorCasaContrato = view.findViewById(R.id.etColorCasaContrato);
        etEntreCallesContrato = view.findViewById(R.id.etEntreCallesContrato);
        etNombreReferenciaContrato = view.findViewById(R.id.etNombreReferenciaContrato);
        etTelefonoReferenciaContrato = view.findViewById(R.id.etTelefonoReferenciaContrato);
        etFechaCobroIniFechaCobroFinContrato = view.findViewById(R.id.etFechaCobroIniFechaCobroFinContrato);
        etSaldoContrato2 = view.findViewById(R.id.etSaldoContrato2);
        etAliasContrato = view.findViewById(R.id.etAliasContrato);

        etCalleContratoEntrega = view.findViewById(R.id.etCalleContratoEntrega);
        etNumeroEntrega = view.findViewById(R.id.etNumeroEntrega);
        etColoniaContratoEntrega = view.findViewById(R.id.etColoniaContratoEntrega);
        etLocalidadContratoEntrega = view.findViewById(R.id.etLocalidadContratoEntrega);
        etColorCasaContratoEntrega = view.findViewById(R.id.etColorCasaContratoEntrega);
        etEntreCallesContratoEntrega = view.findViewById(R.id.etEntreCallesContratoEntrega);
        etDepartamentoContratoEntrega = view.findViewById(R.id.etDepartamentoContratoEntrega);
        etAlLadoDeContratoEntrega = view.findViewById(R.id.etAlLadoDeContratoEntrega);
        etFrenteAContratoEntrega = view.findViewById(R.id.etFrenteAContratoEntrega);

        spFormaPago = view.findViewById(R.id.spFormaPago);
        ivFotoTarjetaPensionFrente = view.findViewById(R.id.ivFotoTarjetaPensionFrente);
        ivFotoTarjetaPensionAtras = view.findViewById(R.id.ivFotoTarjetaPensionAtras);
        btnNuevoHistorial = view.findViewById(R.id.btnNuevoHistorial);
        btnNuevaPromocion = view.findViewById(R.id.btnNuevaPromocion);

        lvHistorialesClinicos = view.findViewById(R.id.lvHistorialesClinicos);
        lvHistorialesClinicos2 = view.findViewById(R.id.lvHistorialesClinicos2);

        llListaPromociones = view.findViewById(R.id.llListaPromociones);
        tvEstadoPromocion = view.findViewById(R.id.tvEstadoPromocion);
        tvTituloPromocion = view.findViewById(R.id.tvTituloPromocion);
        tvInicioPromocion = view.findViewById(R.id.tvInicioPromocion);
        tvFinPromocion = view.findViewById(R.id.tvFinPromocion);
        tvEstadoContrato = view.findViewById(R.id.tvEstadoContrato);
        tvTextoContrato = view.findViewById(R.id.tvTextoContrato);

        llTarjetaPension = view.findViewById(R.id.llTarjetaPension);
        btnAgregarFormaPago = view.findViewById(R.id.btnAgregarFormaPago);
        btnNuevoProducto = view.findViewById(R.id.btnNuevoProducto);
        btnNuevoAbono = view.findViewById(R.id.btnNuevoAbono);

        lvProductos = view.findViewById(R.id.lvProductos);
        lvAbonos = view.findViewById(R.id.lvAbonos);

        llAbonos = view.findViewById(R.id.llAbonos);
        llListaAbonos = view.findViewById(R.id.llListaAbonos);
        llListaProductos = view.findViewById(R.id.llListaProductos);

        svScrollPrincipal = view.findViewById(R.id.svScrollPrincipal);
        btnMostrarOcultar = view.findViewById(R.id.btnMostrarOcultar);
        llSecundario = view.findViewById(R.id.llSecundario);
        btnTerminarContrato = view.findViewById(R.id.btnTerminarContrato);

        llPromociones = view.findViewById(R.id.llPromociones);
        llHistorialesClinicos = view.findViewById(R.id.llHistorialesClinicos);
        llProductos = view.findViewById(R.id.llProductos);
        llEntreCalles = view.findViewById(R.id.llEntreCalles);
        llNombreTelefonoReferencia = view.findViewById(R.id.llNombreTelefonoReferencia);
        llFechaCobroIniFechaCobroFinContrato = view.findViewById(R.id.llFechaCobroIniFechaCobroFinContrato);

        llHistorialesClinicos2 = view.findViewById(R.id.llHistorialesClinicos2);
        tvDiagnosticoEncabezado2 = view.findViewById(R.id.tvDiagnosticoEncabezado2);
        tvFechaVisitaEncabezado2 = view.findViewById(R.id.tvFechaVisitaEncabezado2);
        tvFechaEntregaEncabezado2 = view.findViewById(R.id.tvFechaEntregaEncabezado2);

        lvContratosCercanos = view.findViewById(R.id.lvContratosCercanos);
        llListaContratosCercanos = view.findViewById(R.id.llListaContratosCercanos);

        llLugarEntregaProducto = view.findViewById(R.id.llLugarEntregaProducto);
        spLugarEntregaProducto = view.findViewById(R.id.spLugarEntregaProducto);

        llAbonosSecundario = view.findViewById(R.id.llAbonosSecundario);
        llDiaPago = view.findViewById(R.id.llDiaPago);
        spDiaPago = view.findViewById(R.id.spDiaPago);
        btnAplicarDiaPago = view.findViewById(R.id.btnAplicarDiaPago);

        llCreadorContrato = view.findViewById(R.id.llCreadorContrato);
        llOptometristaAsignado = view.findViewById(R.id.llOptometristaAsignado);

        llFormaPago = view.findViewById(R.id.llFormaPago);
        llTvYBtnProductos = view.findViewById(R.id.llTvYBtnProductos);

        tvDiagnosticoEncabezado = view.findViewById(R.id.tvDiagnosticoEncabezado);
        tvFechaVisitaEncabezado = view.findViewById(R.id.tvFechaVisitaEncabezado);
        tvFechaEntregaEncabezado = view.findViewById(R.id.tvFechaEntregaEncabezado);

        etDepartamentoContrato = view.findViewById(R.id.etDepartamentoContrato);
        llDepartamentoContrato = view.findViewById(R.id.llDepartamentoContrato);

        llReportarGarantia = view.findViewById(R.id.llReportarGarantia);

        llNotaContrato = view.findViewById(R.id.llNotaContrato);
        tvNotaContrato = view.findViewById(R.id.tvNotaContrato);

        llAlLadoDe = view.findViewById(R.id.llAlLadoDe);
        etAlLadoDeContrato = view.findViewById(R.id.etAlLadoDeContrato);
        llFrenteA = view.findViewById(R.id.llFrenteA);
        etFrenteAContrato = view.findViewById(R.id.etFrenteAContrato);

        llNombrePaquete = view.findViewById(R.id.llNombrePaquete);
        etNombrePaqueteContrato = view.findViewById(R.id.etNombrePaqueteContrato);
        llTotalReal = view.findViewById(R.id.llTotalReal);
        etTotalReal = view.findViewById(R.id.etTotalReal);

        ivIconoUbicacion = view.findViewById(R.id.ivIconoUbicacion);

        llTituloPromocion = view.findViewById(R.id.llTituloPromocion);
        etTituloPromocionContrato = view.findViewById(R.id.etTituloPromocionContrato);

        ivIconoNotaRegistroImprimir = view.findViewById(R.id.ivIconoNotaRegistroImprimir);
        btnMostrarFotoCasaContrato = view.findViewById(R.id.btnMostrarFotoCasaContrato);
        llBtnMostrarFotoCasaContrato = view.findViewById(R.id.llBtnMostrarFotoCasaContrato);
        btnMostrarFotoIneContrato = view.findViewById(R.id.btnMostrarFotoIneContrato);
        btnMostrarFotoPagare = view.findViewById(R.id.btnMostrarFotoPagare);

        llLugarVenta = view.findViewById(R.id.llLugarVenta);
        llLugarEntrega = view.findViewById(R.id.llLugarEntrega);
        llCalle = view.findViewById(R.id.llCalle);
        llNumeroDepartamentoColonia = view.findViewById(R.id.llNumeroDepartamentoColonia);
        llLocalidadColorCasa = view.findViewById(R.id.llLocalidadColorCasa);
        llDivision5 = view.findViewById(R.id.llDivision5);

        tvReportarGarantia = view.findViewById(R.id.tvReportarGarantia);

        //Boton Ver otro
        btnMostrarFotoOtros = view.findViewById(R.id.btnMostrarFotoOtros);
        llActualizarFotoPagare = view.findViewById(R.id.llActualizarFotoPagare);
        ivActualizarFotoPagare = view.findViewById(R.id.ivActualizarFotoPagare);
        btnActualizarFotoPagare = view.findViewById(R.id.btnActualizarFotoPagare);
        llActualizarFotoCasa = view.findViewById(R.id.llActualizarFotoCasa);
        ivActualizarFotoCasa = view.findViewById(R.id.ivActualizarFotoCasa);
        btnActualizarFotoCasa = view.findViewById(R.id.btnActualizarFotoCasa);
        llMovimientoContrato = view.findViewById(R.id.llMovimientoContrato);
        ivMovimientoContrato = view.findViewById(R.id.ivMovimientoContrato);
        btnCrearMovimientoContrato = view.findViewById(R.id.btnCrearMovimientoContrato);

        //Leyenda para agregar promocion eliminar abonos.
        tvPromocionLeyenda = view.findViewById(R.id.tvPromocionLeyenda);

        //Campo de observaciones para movimientos contratos
        etMovimientoContrato = view.findViewById(R.id.etMovimientoContrato);

        //Historial de movimientos
        llHistorialMovimientosContrato = view.findViewById(R.id.llHistorialMovimientosContrato);
        tvHistorialMovimientosContrato = view.findViewById(R.id.tvHistorialMovimientosContrato);

        //Lista Negra
        llListaNegra = view.findViewById(R.id.llListaNegra);
        tvListaNegra = view.findViewById(R.id.tvListaNegra);

        etObservacionActualizarFotoPagare = view.findViewById(R.id.etObservacionActualizarFotoPagare);

        Bundle bundle = getArguments();
        if(bundle != null){
            ultimoIdContratoCreado = bundle.getString("ultimoIdContratoCreado");
        }

        filaHistorialesClinicos = new ArrayList<>();
        filaHistorialesClinicos2 = new ArrayList<>();
        filaContratosCercanos = new ArrayList<>();
        filaProductos = new ArrayList<>();
        filaAbonos = new ArrayList<>();

        //Inicialización de Clases
        obtenerRol = new ObtenerRol(fragmento);
        String rol = obtenerRol.obtenerAtributoUsuarioLogeado("ROL");

        if(!rol.equals("4")) {
            //Asistente/Optometrista
            if(global.contadorContratosOHistorialesTablaGarantias(ultimoIdContratoCreado, "", false) > 0) {
                garantia = true;
            }
            if (garantia) {
                btnMostrarOcultar.setVisibility(View.GONE);
                tvEstadoPromocion.setEnabled(false);
            }
        }

        Object[] objetos = {ultimoIdContratoCreado, etNumeroContrato, etFechaCreacionContrato, etSaldoContrato, etCreadorContrato,
                etOptometristaAsignado,  etClienteContrato, etCalleContrato, etNumero, etColoniaContrato, etLocalidadContrato, etTelefonoContrato,
                etColorCasaContrato, spFormaPago, lvHistorialesClinicos, filaHistorialesClinicos, btnNuevoHistorial, btnNuevaPromocion,
                llListaPromociones, tvEstadoPromocion, tvTituloPromocion, tvInicioPromocion, tvFinPromocion, ivFotoTarjetaPensionFrente,
                ivFotoTarjetaPensionAtras, llTarjetaPension, lvProductos, filaProductos, lvAbonos, filaAbonos, llListaAbonos, llListaProductos,
                svScrollPrincipal, btnMostrarOcultar, llSecundario, btnTerminarContrato, llPromociones, btnAgregarFormaPago, rol, spDiaPago, llFormaPago,
                llDiaPago, tvEstadoContrato, tvTextoContrato, llProductos, llTvYBtnProductos, etEntreCallesContrato, etNombreReferenciaContrato, etTelefonoReferenciaContrato,
                etDepartamentoContrato, garantia, etFechaCobroIniFechaCobroFinContrato, llFechaCobroIniFechaCobroFinContrato, etSaldoContrato2, llReportarGarantia, tvNotaContrato,
                etAlLadoDeContrato, etFrenteAContrato, btnAplicarDiaPago, etNombrePaqueteContrato, etTotalReal, ivIconoUbicacion, etTituloPromocionContrato, btnMostrarFotoCasaContrato,
                tvReportarGarantia, btnMostrarFotoOtros, etCalleContratoEntrega, etNumeroEntrega, etColoniaContratoEntrega, etLocalidadContratoEntrega, etColorCasaContratoEntrega,
                etEntreCallesContratoEntrega, etDepartamentoContratoEntrega, etAlLadoDeContratoEntrega, etFrenteAContratoEntrega, btnMostrarFotoIneContrato, llNotaContrato,
                ivActualizarFotoPagare, llActualizarFotoPagare, etAliasContrato, lvHistorialesClinicos2, filaHistorialesClinicos2, btnMostrarFotoPagare, tvPromocionLeyenda,
                ivActualizarFotoCasa, btnActualizarFotoCasa, ivMovimientoContrato, etMovimientoContrato, btnCrearMovimientoContrato, llListaNegra, tvListaNegra,
                lvContratosCercanos, filaContratosCercanos, spLugarEntregaProducto, llListaContratosCercanos, etObservacionActualizarFotoPagare};

        controlador = new Controlador(fragmento, objetos);
        camara = new Camara(fragmento);

        if(rol.equals("4")) {
            //Cobranza
            llHistorialesClinicos.setVisibility(View.GONE);
            llHistorialesClinicos2.setVisibility(View.VISIBLE);
            llListaContratosCercanos.setVisibility(View.VISIBLE);
            llDivision5.setVisibility(View.VISIBLE);
            tvDiagnosticoEncabezado2.setVisibility(View.GONE);
            tvFechaVisitaEncabezado2.setText("Fecha entrega");
            tvFechaEntregaEncabezado2.setText("Observaciones");
            llCreadorContrato.setVisibility(View.GONE);
            llOptometristaAsignado.setVisibility(View.GONE);
            llEntreCalles.setVisibility(View.VISIBLE);
            llNombreTelefonoReferencia.setVisibility(View.VISIBLE);
            llDepartamentoContrato.setVisibility(View.VISIBLE);
            llFechaCobroIniFechaCobroFinContrato.setVisibility(View.VISIBLE);
            llNotaContrato.setVisibility(View.VISIBLE);
            llAlLadoDe.setVisibility(View.VISIBLE);
            llFrenteA.setVisibility(View.VISIBLE);
            llNombrePaquete.setVisibility(View.VISIBLE);
            llTotalReal.setVisibility(View.VISIBLE);
            llTituloPromocion.setVisibility(View.VISIBLE);
            ivIconoNotaRegistroImprimir.setVisibility(View.VISIBLE);
            llBtnMostrarFotoCasaContrato.setVisibility(View.VISIBLE);
            llLugarVenta.setVisibility(View.VISIBLE);
            llLugarEntrega.setVisibility(View.VISIBLE);
            llActualizarFotoCasa.setVisibility(View.VISIBLE);
            llMovimientoContrato.setVisibility(View.VISIBLE);
            if(obtenerRol.obtenerAtributoUsuarioLogeado("SUPERVISORCOBRANZA").equals("1")){
                //Es SUPERVISOR
                llHistorialMovimientosContrato.setVisibility(View.VISIBLE);
            }
            //Mostrar u ocultra spinner de lugar de entrega - contratos en estatus ENVIADO
            if(controlador.obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("12")){
                llLugarEntregaProducto.setVisibility(View.VISIBLE);
                spLugarEntregaProducto.setEnabled(false);
            }

            //Seccion para ingresar contratos a lista negra
            llListaNegra.setVisibility(View.VISIBLE);
            tvListaNegra.setText("Agregar a lista negra");
            estadoReporteContratoListaNegra = global.obtenerAtributoTabla("CONTRATOSLISTANEGRA","ESTADO","ID_CONTRATO",ultimoIdContratoCreado);
            if(estadoReporteContratoListaNegra.length() > 0){
                //Existe contrato en reporte de lista negra
                if(estadoReporteContratoListaNegra.equals("0")){
                    //Ya se encuentra realizado un reporte y esta pendiente por aceptar
                    tvListaNegra.setText("Reporte de lista negra pendiente");
                    banderaInsertarContratoListaNegra = false;
                }else if (estadoReporteContratoListaNegra.equals("1")){
                    tvListaNegra.setText("Este contrato pertenece a lista negra");
                    banderaInsertarContratoListaNegra = false;
                }else if (estadoReporteContratoListaNegra.equals("2")){
                    tvListaNegra.setText("Agregar a lista negra");
                    banderaInsertarContratoListaNegra = true;
                }
            }else{
                //No presenta ningun reporte de lista negra
                tvListaNegra.setText("Agregar a lista negra");
                banderaInsertarContratoListaNegra = true;
            }

        }else {
            //Asistente/Opto
            llCalle.setVisibility(View.GONE);
            llNumeroDepartamentoColonia.setVisibility(View.GONE);
            llLocalidadColorCasa.setVisibility(View.GONE);

            if (garantia) {
                //Es garantia el contrato
                llCalle.setVisibility(View.VISIBLE);
                llNumeroDepartamentoColonia.setVisibility(View.VISIBLE);
                llLocalidadColorCasa.setVisibility(View.VISIBLE);
                llEntreCalles.setVisibility(View.VISIBLE);
                llNombreTelefonoReferencia.setVisibility(View.VISIBLE);
                llDepartamentoContrato.setVisibility(View.VISIBLE);
                llAlLadoDe.setVisibility(View.VISIBLE);
                llFrenteA.setVisibility(View.VISIBLE);
                llLugarVenta.setVisibility(View.VISIBLE);
                llLugarEntrega.setVisibility(View.VISIBLE);
                llBtnMostrarFotoCasaContrato.setVisibility(View.VISIBLE);
            }
        }

        controlador.llenarCamposBD();

        //Spinners
        spFormaPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(controlador.obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("0")) {
                    if (position == 4) {
                        llTarjetaPension.setVisibility(View.VISIBLE);
                    } else {
                        llTarjetaPension.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //ImageViews
        ivFotoTarjetaPensionFrente.setOnClickListener(new View.OnClickListener() {
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
                        camara.openCamera(ivFotoTarjetaPensionFrente, 6);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivFotoTarjetaPensionFrente, 6);
                }
            }
        });

        ivFotoTarjetaPensionAtras.setOnClickListener(new View.OnClickListener() {
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
                        camara.openCamera(ivFotoTarjetaPensionAtras, 7);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivFotoTarjetaPensionAtras, 7);
                }
            }
        });

        ivActualizarFotoPagare.setOnClickListener(new View.OnClickListener() {
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
                        camara.openCamera(ivActualizarFotoPagare, 4);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivActualizarFotoPagare, 4);
                }
            }
        });

        ivActualizarFotoCasa.setOnClickListener(new View.OnClickListener() {
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
                        camara.openCamera(ivActualizarFotoCasa, 2);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivActualizarFotoCasa, 2);
                }
            }
        });

        ivMovimientoContrato.setOnClickListener(new View.OnClickListener() {
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
                        camara.openCamera(ivMovimientoContrato, 8);
                    }
                }else {
                    camara.crearDirectorios();
                    camara.openCamera(ivMovimientoContrato, 8);
                }
            }
        });

        //TextViews
        tvEstadoPromocion.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                controlador.mostrarAlertDialogEliminarPromocion();
                return false;
            }
        });

        tvEstadoPromocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.validarCambioStatusPromocion();
            }
        });

        //Buttons
        btnNuevoHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.irAPantallaNuevoHistorialClinico();
            }
        });

        btnNuevaPromocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNuevaPromocion.setVisibility(View.INVISIBLE);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            global.escribirError(e, 331);
                            Log.i("ERROR", e.toString());
                        }

                        fragmento.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnNuevaPromocion.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
                controlador.mostrarAlertDialogPromociones();
            }
        });

        btnAgregarFormaPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAgregarFormaPago.setVisibility(View.INVISIBLE);
                controlador.validarFormaPagoContrato();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            global.escribirError(e, 249);
                            Log.i("ERROR", e.toString());
                        }

                        fragmento.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnAgregarFormaPago.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
            }
        });

        btnNuevoProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNuevoProducto.setVisibility(View.INVISIBLE);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            global.escribirError(e, 332);
                            Log.i("ERROR", e.toString());
                        }

                        fragmento.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnNuevoProducto.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
                controlador.mostrarAlertDialogProducto();
            }
        });

        btnNuevoAbono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNuevoAbono.setVisibility(View.INVISIBLE);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            global.escribirError(e, 250);
                            Log.i("ERROR", e.toString());
                        }

                        fragmento.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnNuevoAbono.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
                controlador.mostrarAlertDialogAbono();
            }
        });

        btnMostrarOcultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.mostrarOcultarListas();
            }
        });

        btnTerminarContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlador.terminarContrato();
            }
        });

        btnAplicarDiaPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAplicarDiaPago.setVisibility(View.INVISIBLE);
                controlador.guardarDiaPago();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            global.escribirError(e, 251);
                            Log.i("ERROR", e.toString());
                        }

                        fragmento.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnAplicarDiaPago.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
            }
        });

        //ListViews
        lvHistorialesClinicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(rol.equals("4")) {
                    //Rol cobranza
                    controlador.mostrarAlertDialogObservacion(i);
                }else {
                    //Rol Asis/Opto
                    controlador.abrirHistorialClinico(i);
                }
            }
        });

        lvHistorialesClinicos2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(rol.equals("4")) {
                    //Rol cobranza
                    controlador.mostrarAlertDialogObservacion2(i);
                }
            }
        });

        lvAbonos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(rol.equals("4")) {
                    //Cobranza
                    controlador.mostrarAlertOpcionAbonoCobranza(i);
                }else {
                    //Asiste/Opto
                    controlador.mostrarAlertOpcionAbonoVentas(i);
                }
                return true;
            }
        });

        lvProductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                controlador.mostrarAlertEliminarProducto(i);
                return true;
            }
        });

        lvContratosCercanos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(rol.equals("4")) {
                    //Cobranza
                    controlador.mostrarAlertAbrirContratoCercano(i);
                }
                return true;
            }
        });

        if(!rol.equals("4")) {
            //Verificar si las imagenes ya se subieron del contrato (Cambiar tvTextoContrato)
            Handler handler = new Handler();
            runnable = new Runnable() {
                public void run() {
                    if(!controlador.existeIdContratoEnTablaImagenesCargadas(ultimoIdContratoCreado)) {
                        Log.i("MENSAJE", "Texto cambiado - Ya se subieron imagenes");
                        global.verificarConfiguracionEncabezados(tvTextoContrato);
//                        tvTextoContrato.setTextColor(Color.parseColor("#0AA09E"));
                        handler.removeCallbacks(runnable);
                    }else {
                        Log.i("MENSAJE", "Texto cambiado - No se han subido imagenes");
                        tvTextoContrato.setTextColor(Color.parseColor("#EEEEEE"));
                        handler.postDelayed(this, 3000);
                    }
                }
            };
            runnable.run();
        }

        etNumeroContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) fragmento.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(etNumeroContrato.getText().toString(), etNumeroContrato.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(fragmento.getContext(),"Texto copiado", Toast.LENGTH_SHORT).show();
            }
        });

        etTelefonoContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol.equals("4")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", etTelefonoContrato.getText().toString(), null));
                    startActivity(intent);
                }
            }
        });

        etTelefonoReferenciaContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol.equals("4")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", etTelefonoReferenciaContrato.getText().toString(), null));
                    startActivity(intent);
                }
            }
        });

        etMovimientoContrato.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && rol.equals("4")) {
                    controlador.mostrarAlertDialogObservacionesMovimientoContrato();
                }
            }
        });

        etMovimientoContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol.equals("4")) {
                    controlador.mostrarAlertDialogObservacionesMovimientoContrato();
                }
            }
        });

        llHistorialMovimientosContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol.equals("4") && obtenerRol.obtenerAtributoUsuarioLogeado("SUPERVISORCOBRANZA").equals("1")) {
                    controlador.mostrarAlertDialogHistorialMovimientosContrato();
                }
            }
          });

        llReportarGarantia.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                controlador.mostrarAlertDialogReportarGarantia();
                return false;
            }
        });

        tvNotaContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol.equals("4")) {
                    controlador.mostrarAlertDialogNotaContrato();
                }
            }
        });

        llListaNegra.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(rol.equals("4")) {
                    if(banderaInsertarContratoListaNegra){
                        //Contrato tiene disponible la opcion para crear solicitu de lista negra
                        controlador.agregarContratoListaNegra();
                    }else{
                        //Contrato ya cuenta con una solicitu de lista negra
                        Toast.makeText(fragmento.getActivity(), "No se puede generar un nuevo informe, hay uno ya registrado.", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        ivIconoUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    controlador.mostrarAlertDialogOpcionesIconoUbicacion();
            }
        });

        ivIconoNotaRegistroImprimir.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(rol.equals("4")) {
                    controlador.imprimirRegistroCliente();
                }
                return false;
            }
        });

        btnMostrarFotoCasaContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol.equals("4") || garantia) {
                    btnMostrarFotoCasaContrato.setVisibility(View.INVISIBLE);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                global.escribirError(e, 333);
                                Log.i("ERROR", e.toString());
                            }

                            fragmento.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnMostrarFotoCasaContrato.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    };
                    thread.start(); //start the thread
                    controlador.mostrarAlertDialogFotosContrato(0);
                }
            }
        });

        btnMostrarFotoOtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol.equals("4") || garantia) {
                    btnMostrarFotoOtros.setVisibility(View.INVISIBLE);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                global.escribirError(e, 334);
                                Log.i("ERROR", e.toString());
                            }

                            fragmento.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnMostrarFotoOtros.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    };
                    thread.start(); //start the thread
                    controlador.mostrarAlertDialogFotosContrato(1);
                }
            }
        });

        btnMostrarFotoIneContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol.equals("4") || garantia) {
                    btnMostrarFotoIneContrato.setVisibility(View.INVISIBLE);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                global.escribirError(e, 335);
                                Log.i("ERROR", e.toString());
                            }

                            fragmento.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnMostrarFotoIneContrato.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    };
                    thread.start(); //start the thread
                    controlador.mostrarAlertDialogFotosContrato(2);
                }
            }
        });

        btnMostrarFotoPagare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rol.equals("4") || garantia) {
                    btnMostrarFotoPagare.setVisibility(View.INVISIBLE);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                global.escribirError(e, 336);
                                Log.i("ERROR", e.toString());
                            }

                            fragmento.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnMostrarFotoPagare.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    };
                    thread.start(); //start the thread
                    controlador.mostrarAlertDialogFotosContrato(3);
                }
            }
        });

        btnActualizarFotoPagare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActualizarFotoPagare.setVisibility(View.INVISIBLE);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            global.escribirError(e, 337);
                            Log.i("ERROR", e.toString());
                        }

                        fragmento.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnActualizarFotoPagare.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
                controlador.actualizarFotoPagareServidor();
            }
        });

        btnActualizarFotoCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActualizarFotoCasa.setVisibility(View.INVISIBLE);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            global.escribirError(e, 338);
                            Log.i("ERROR", e.toString());
                        }

                        fragmento.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnActualizarFotoCasa.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
                controlador.actualizarFotoCasaServidor();
            }

        });

        btnCrearMovimientoContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCrearMovimientoContrato.setVisibility(View.INVISIBLE);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            global.escribirError(e, 339);
                            Log.i("ERROR", e.toString());
                        }

                        fragmento.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnCrearMovimientoContrato.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
                controlador.agregarMovimientoContrato();
            }

        });

        etObservacionActualizarFotoPagare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.mostrarAlertDialogObservacionImagenesContrato(etObservacionActualizarFotoPagare, "PAGARE");
            }
        });

        etObservacionActualizarFotoPagare.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    global.mostrarAlertDialogObservacionImagenesContrato(etObservacionActualizarFotoPagare, "PAGARE");
                }
            }
        });

        return view;

    }

    //Metodo que se utiliza para escuchar la respuesta de las funciones de las fotos
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camara.onActivityResult(requestCode, resultCode, data);
    }

}