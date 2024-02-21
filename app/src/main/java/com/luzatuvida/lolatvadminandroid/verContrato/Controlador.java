package com.luzatuvida.lolatvadminandroid.verContrato;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.chrisbanes.photoview.PhotoView;
import com.luzatuvida.lolatvadminandroid.BuildConfig;
import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.global.Camara;
import com.luzatuvida.lolatvadminandroid.global.GenerarIdAlfanumerico;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.HistorialMovimientosContrato;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.Localizacion;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Seguridad;
import com.luzatuvida.lolatvadminandroid.global.Sincronizacion;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;
import com.luzatuvida.lolatvadminandroid.impresoraBluetooth.ImpresoraBluetooth;
import com.luzatuvida.lolatvadminandroid.vista.MapsMarcadoresContratos;
import com.luzatuvida.lolatvadminandroid.vista.inicioSesion;
import com.luzatuvida.lolatvadminandroid.vista.nuevoContrato;
import com.luzatuvida.lolatvadminandroid.vista.nuevoHistorialClinico;
import com.luzatuvida.lolatvadminandroid.vista.principal;
import com.luzatuvida.lolatvadminandroid.vista.verContrato;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.PaymentIntentCreateParams;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Controlador {

    /*
    Identificador error del 157 al 234
     */

    Fragment fragmento;
    baseDeDatos conexion;
    EditText etNumeroContrato, etFechaCreacionContrato, etSaldoContrato, etCreadorContrato, etOptometristaAsignado, etClienteContrato, etCalleContrato,
                etNumero, etColoniaContrato, etLocalidadContrato, etTelefonoContrato, etColorCasaContrato, etEntreCallesContrato, etNombreReferenciaContrato,
                    etTelefonoReferenciaContrato, etDepartamentoContrato, etFechaCobroIniFechaCobroFinContrato, etSaldoContrato2, etAlLadoDeContrato, etFrenteAContrato, etNombrePaqueteContrato,
                        etTotalReal, etTituloPromocionContrato, etAliasContrato;
    EditText etPrecioProductoAlertDialog, etPiezasProductoAlertDialog, etNumPiezasProductoAlertDialog, etPrecioProductoPromocionAlertDialog;
    EditText etAbonoAlertDialog, etAdelantarAbonoAlertDialog, etFolioPoliza;
    CheckBox cbAdelantarAbonoAlertDialog, cbAplicarPoliza, cbCancelacionContratoAlertDialog;
    Spinner spFormaPago, spProductoAlertDialog, spDiaPago, spMetodoPago, spPlanStripe, spLugarEntregaProducto;
    TextView tvEstadoPromocion, tvTituloPromocion, tvInicioPromocion, tvFinPromocion, btnAgregarFormaPago, tvEstadoContrato, tvTextoContrato, tvPrecioMensualStripe, tvNotaContrato;
    LinearLayout llListaPromociones, llTarjetaPension, llListaAbonos, llListaProductos, llSecundario, llPromociones, llAbonoAdelantadoAlertDialog,
                llFormaPago, llDiaPago, llPrecioPromocion, llProductos, llTvYBtnProductos, llCardStripe, llPlanStripe, llFechaCobroIniFechaCobroFinContrato,
                llReportarGarantia, llNotaContrato, llActualizarFotoPagare, llAplicarFolioPoliza, llListaNegra, llCancelacionContrato, llListaContratosCercanos;
    TextView btnNuevoHistorial, btnNuevaPromocion, btnAplicarDiaPago, btnMostrarFotoCasaContrato, tvReportarGarantia, btnMostrarFotoOtros, btnMostrarFotoIneContrato,
            tvAutorizacionArmazon, btnMostrarFotoPagare, btnActualizarFotoCasa, btnCrearMovimientoContrato, tvListaNegra;
    SQLiteDatabase sqLiteDB;
    ImageView ivFotoTarjetaPensionFrente, ivFotoTarjetaPensionAtras, ivIconoUbicacion, ivActualizarFotoPagare, ivActualizarFotoCasa, ivMovimientoContrato;
    String[] idsPromociones, tituloPromociones, idsFormasPago, formasPago, idsProductos, nombresProductos, idsDiaPago, diasDiaPago, idsMetodoPago, metodosPago, idsPlanStripe,
            planesStripe, idsLugarEntregaProducto, opcionesLugarEntregaProcusto;

    int[] coloresRBGMetodoPago;
    ScrollView svScrollPrincipal;
    Button btnMostrarOcultar, btnTerminarContrato;

    EditText etCalleContratoEntrega, etNumeroEntrega, etColoniaContratoEntrega, etLocalidadContratoEntrega, etColorCasaContratoEntrega, etEntreCallesContratoEntrega,
            etDepartamentoContratoEntrega, etAlLadoDeContratoEntrega, etFrenteAContratoEntrega, etMovimientoContrato, etObservacionActualizarFotoPagare;

    ListView lvHistorialesClinicos, lvProductos, lvAbonos, lvHistorialesClinicos2, lvContratosCercanos;
    List<FilaHistorialesClinicos> filaHistorialesClinicos;
    List<FilaHistorialesClinicos> filaHistorialesClinicos2;
    List<FilaProductos> filaProductos;
    List<FilaAbonos> filaAbonos;

    String ultimoIdContratoCreado = "";

    boolean cambioStatusPromocion = false;
    boolean mostrarOcultarListas = false;
    Camara camara;
    ObtenerRol obtenerRol;

    String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
    String rutaTarjetaPensionFrente = "";
    String rutaTarjetaPensionAtras = "";
    String rutaPagare = "";
    LayoutInflater inflater = null;
    View searchView;
    HistorialMovimientosContrato historialMovimientosContrato;

    double precioTotalProductosNoPagados;

    String idContratoPadre;
    String rol = "";

    double pagoMinimo;

    String descuento;
    String fechaActual = "";
    String time = "";
    GenerarIdAlfanumerico generarIdAlfanumerico;
    String idUsuario;
    Sincronizacion sincronizacion;
    Object[] objetosWebService;
    String abonoMinimoSemanal = "";

    String idFranquicia = "";

    DecimalFormat df00 = new DecimalFormat("#0.00");
    DecimalFormat df0 = new DecimalFormat("#0.0");

    boolean contratoHijo;
    String idContratoHijoSinTerminar;
    Global global;
    Llaves llaves;
    CardInputWidget cardInputWidget;
    private Stripe stripe;
    Internet internet;
    double precioACobrarAlMeses;
    int mesesStripe;
    boolean garantia;
    List<Integer> coloresHistorial;
    List<Integer> coloresHistorial2;
    String folioAlfanumerico = "";

    ImpresoraBluetooth impresoraBluetooth;
    Seguridad seguridad;

    boolean imprimirTicket = true;
    boolean banderaHandlerImpresora = true;
    Runnable runnable, runnable2, runnable3;

    Localizacion localizacion;

    String stripe_clavepublicable = "";

    //TEXTVIEW aceptamos dolares
    TextView tvCambioDolarPesos;

    String totalanterior = "0";
    boolean imprimirTicketEntregaProducto = false;

    //TEXTVIEW Para agregar promocion eliminar abonos.
    TextView tvPromocionLeyenda;

    //TEXTVIEW Ticket Abono compartir
    TextView tvTicketAbonoSucursal, tvTicketAbonoTelefono, tvTicketAbonoWhatsapp, tvTicketAbonoNombre, tvTicketAbonoContrato, tvTicketAbonoFolio, tvTicketAbonoSaldoAnt,
             tvTicketAbonoCantidadAbono, tvTicketAbonoSaldoN, tvTicketAbonoProducto, tvTicketAbonoCantidadLetra, tvTicketAbonoFecha, tvTicketAbonoCobrador, tvTicketAbonoPagado;

    LinearLayout llFormatoTicketAbonoCompartir;

    String nombreImagenTicketAbono = "";
    String rutaCasa = "";
    Toolbar toolbar;
    LinearLayout llProgress, llBuscador, llBuscadorIcono;

    String coordenadasAbono = "";

    List<FilaContratosCercanos> filaContratosCercanos;


    public Controlador(Fragment fragmento, Object[] objetos) {
        this.fragmento = fragmento;
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getActivity(), "basedatos", null, llaves.versiondb);
        camara = new Camara(fragmento);
        historialMovimientosContrato = new HistorialMovimientosContrato(fragmento);
        obtenerRol = new ObtenerRol(fragmento);
        global = new Global(fragmento);
        generarIdAlfanumerico = new GenerarIdAlfanumerico(fragmento);
        sincronizacion = new Sincronizacion(fragmento,false);
        internet = new Internet(fragmento.getActivity());
        impresoraBluetooth = new ImpresoraBluetooth(fragmento);
        seguridad = new Seguridad(fragmento);
        toolbar = fragmento.getActivity().findViewById(R.id.toolbar);
        llProgress = toolbar.findViewById(R.id.llProgress);
        llBuscador = toolbar.findViewById(R.id.llBuscador);
        llBuscadorIcono = toolbar.findViewById(R.id.llBuscadorIcono);

        ultimoIdContratoCreado = (String)objetos[0];
        etNumeroContrato = (EditText) objetos[1];
        etFechaCreacionContrato = (EditText)objetos[2];
        etSaldoContrato = (EditText)objetos[3];
        etCreadorContrato = (EditText)objetos[4];
        etOptometristaAsignado = (EditText)objetos[5];
        etClienteContrato = (EditText)objetos[6];
        etCalleContrato = (EditText)objetos[7];
        etNumero = (EditText)objetos[8];
        etColoniaContrato = (EditText)objetos[9];
        etLocalidadContrato = (EditText)objetos[10];
        etTelefonoContrato = (EditText)objetos[11];
        etColorCasaContrato = (EditText)objetos[12];
        spFormaPago = (Spinner)objetos[13];
        lvHistorialesClinicos = (ListView)objetos[14];
        filaHistorialesClinicos = (List<FilaHistorialesClinicos>)objetos[15];
        btnNuevoHistorial = (TextView) objetos[16];
        btnNuevaPromocion = (TextView) objetos[17];

        llListaPromociones = (LinearLayout)objetos[18];
        tvEstadoPromocion = (TextView)objetos[19];
        tvTituloPromocion = (TextView)objetos[20];
        tvInicioPromocion = (TextView)objetos[21];
        tvFinPromocion = (TextView)objetos[22];

        ivFotoTarjetaPensionFrente = (ImageView)objetos[23];
        ivFotoTarjetaPensionAtras = (ImageView)objetos[24];
        llTarjetaPension = (LinearLayout) objetos[25];

        lvProductos = (ListView)objetos[26];
        filaProductos = (List<FilaProductos>)objetos[27];
        lvAbonos = (ListView)objetos[28];
        filaAbonos = (List<FilaAbonos>)objetos[29];

        llListaAbonos = (LinearLayout)objetos[30];
        llListaProductos = (LinearLayout)objetos[31];

        svScrollPrincipal = (ScrollView)objetos[32];
        btnMostrarOcultar = (Button)objetos[33];
        llSecundario = (LinearLayout)objetos[34];
        btnTerminarContrato = (Button)objetos[35];

        llPromociones = (LinearLayout)objetos[36];
        btnAgregarFormaPago = (TextView)objetos[37];

        rol = (String)objetos[38];
        spDiaPago = (Spinner)objetos[39];

        llFormaPago = (LinearLayout)objetos[40];
        llDiaPago = (LinearLayout)objetos[41];
        tvEstadoContrato = (TextView)objetos[42];
        tvTextoContrato = (TextView)objetos[43];
        llProductos = (LinearLayout)objetos[44];
        llTvYBtnProductos = (LinearLayout)objetos[45];
        etEntreCallesContrato = (EditText) objetos[46];
        etNombreReferenciaContrato = (EditText) objetos[47];
        etTelefonoReferenciaContrato = (EditText) objetos[48];
        etDepartamentoContrato = (EditText) objetos[49];
        garantia = (Boolean) objetos[50];
        etFechaCobroIniFechaCobroFinContrato = (EditText) objetos[51];
        llFechaCobroIniFechaCobroFinContrato = (LinearLayout) objetos[52];
        etSaldoContrato2 = (EditText) objetos[53];
        llReportarGarantia = (LinearLayout) objetos[54];
        tvNotaContrato = (TextView) objetos[55];
        etAlLadoDeContrato = (EditText) objetos[56];
        etFrenteAContrato = (EditText) objetos[57];
        btnAplicarDiaPago = (TextView) objetos[58];
        etNombrePaqueteContrato = (EditText) objetos[59];
        etTotalReal = (EditText) objetos[60];
        ivIconoUbicacion = (ImageView) objetos[61];
        etTituloPromocionContrato = (EditText) objetos[62];
        btnMostrarFotoCasaContrato = (TextView) objetos[63];
        tvReportarGarantia = (TextView) objetos[64];
        btnMostrarFotoOtros = (TextView) objetos[65];

        etCalleContratoEntrega = (EditText) objetos[66];
        etNumeroEntrega = (EditText) objetos[67];
        etColoniaContratoEntrega = (EditText) objetos[68];
        etLocalidadContratoEntrega = (EditText) objetos[69];
        etColorCasaContratoEntrega = (EditText) objetos[70];
        etEntreCallesContratoEntrega = (EditText) objetos[71];
        etDepartamentoContratoEntrega = (EditText) objetos[72];
        etAlLadoDeContratoEntrega = (EditText) objetos[73];
        etFrenteAContratoEntrega = (EditText) objetos[74];
        btnMostrarFotoIneContrato = (TextView) objetos[75];
        llNotaContrato = (LinearLayout) objetos[76];
        ivActualizarFotoPagare = (ImageView) objetos[77];
        llActualizarFotoPagare = (LinearLayout) objetos[78];
        etAliasContrato = (EditText) objetos[79];

        lvHistorialesClinicos2 = (ListView)objetos[80];
        filaHistorialesClinicos2 = (List<FilaHistorialesClinicos>)objetos[81];

        btnMostrarFotoPagare = (TextView) objetos[82];
        tvPromocionLeyenda = (TextView) objetos[83];
        ivActualizarFotoCasa = (ImageView) objetos[84];
        btnActualizarFotoCasa = (TextView) objetos[85];

        ivMovimientoContrato = (ImageView) objetos[86];
        etMovimientoContrato = (EditText) objetos[87];
        btnCrearMovimientoContrato = (TextView) objetos[88];

        llListaNegra = (LinearLayout) objetos[89];
        tvListaNegra = (TextView) objetos[90];

        lvContratosCercanos = (ListView)objetos[91];
        filaContratosCercanos = (List<FilaContratosCercanos>)objetos[92];

        spLugarEntregaProducto = (Spinner) objetos[93];
        llListaContratosCercanos = (LinearLayout) objetos[94];
        etObservacionActualizarFotoPagare = (EditText) objetos[95];

        objetosWebService = new Object[]{obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN"), "", ""};

        stripe_clavepublicable = global.obtenerAtributoTablaDescifrado("LLAVES", "LLAVE", "TIPO", "0");
        if(stripe_clavepublicable.length() > 0) {
            //Tiene algo la clave publicable
            stripe = new Stripe(fragmento.getContext(), stripe_clavepublicable);
        }

        localizacion = new Localizacion(fragmento);

    }

    public void llenarCamposBD() {

        obtenerEstadoContratoYMostrarTextView();

        //Obtener FechaActual
        fechaActual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");
        idUsuario = obtenerRol.obtenerAtributoUsuarioLogeado("ID");
        abonoMinimoSemanal = obtenerRol.obtenerAtributoUsuarioLogeado("ABONOMINIMOSEMANAL");
        idFranquicia = obtenerRol.obtenerAtributoUsuarioLogeado("ID_FRANQUICIA");

        //spFormaPago
        idsFormasPago = new String[] {"", "0", "1", "2", "4"};
        formasPago = new String[] {"Seleccionar", "Contado", "Semanal", "Quincenal", "Mensual"};
        spFormaPago.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, formasPago));

        //spDiaPago
        idsDiaPago = new String[] {"10", "0", "1", "2", "3", "4", "5", "6"};
        diasDiaPago = new String[] {"Dia de pago", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
        spDiaPago.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, diasDiaPago));

        int estadocontrato = Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"));

        if(!obtenerEstadoAtributoContrato("PAGO").equals("0")) {
            //Pago semanal, quincenal o mensual
            llDiaPago.setVisibility(View.VISIBLE);
            if(rol.equals("4") && (obtenerEstadoAtributoContrato("DIAPAGO").length() > 0 || estadocontrato == 6)) {
                //Rol cobranza y (diapago es diferente de vacio o estado del contrato es igual a cancelado) (Bloquear)
                spDiaPago.setEnabled(false);
                btnAplicarDiaPago.setEnabled(false);
            }
        }else {
            //Pago de contado
            llDiaPago.setVisibility(View.GONE);
        }

        //Consultar datos del contrato en la base de datos
        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT CONTRATOS.ID_CONTRATO, CONTRATOS.CREATED_AT, CONTRATOS.TOTAL, CONTRATOS.NOMBRE_USUARIOCREACION," +
                    " IFNULL((SELECT NAME FROM USERS WHERE USERS.ID = CONTRATOS.ID_OPTOMETRISTA), 'No encontrado') AS NAME, CONTRATOS.NOMBRE, CONTRATOS.CALLE, CONTRATOS.NUMERO, CONTRATOS.COLONIA," +
                    " CONTRATOS.LOCALIDAD, CONTRATOS.TELEFONO, CONTRATOS.CASACOLOR, CONTRATOS.PAGO, CONTRATOS.TARJETAFRENTE, CONTRATOS.TARJETAATRAS," +
                    " CONTRATOS.DIAPAGO, CONTRATOS.ENTRECALLES, CONTRATOS.NOMBREREFERENCIA, CONTRATOS.TELEFONOREFERENCIA, CONTRATOS.DEPTO, CONTRATOS.FECHACOBROINI, CONTRATOS.FECHACOBROFIN," +
                    " CONTRATOS.ALLADODE, CONTRATOS.FRENTEA, IFNULL(CONTRATOS.NOMBREPAQUETE, ''), CONTRATOS.TOTALREAL, CONTRATOS.TITULOPROMOCION," +
                    " CONTRATOS.CALLEENTREGA, CONTRATOS.NUMEROENTREGA, CONTRATOS.COLONIAENTREGA, CONTRATOS.LOCALIDADENTREGA, CONTRATOS.CASACOLORENTREGA, CONTRATOS.ENTRECALLESENTREGA," +
                    " CONTRATOS.DEPTOENTREGA, CONTRATOS.ALLADODE, CONTRATOS.FRENTEA, CONTRATOS.ESTATUS_ESTADOCONTRATO, CONTRATOS.CONTRATOENVIADO, CONTRATOS.PAGARE, CONTRATOS.ALIAS, CONTRATOS.FOTOCASA," +
                    " CONTRATOS.OPCIONLUGARENTREGA, CONTRATOS.OBSERVACIONPAGARE" +
                    " FROM CONTRATOS WHERE CONTRATOS.ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No se pudo encontrar el contrato");
            }

            if (datos.moveToFirst()) {
                if (datos.getString(36).equals("0")) {
                    //Estado NO TERMINADO
                    etNumeroContrato.setText("POR ASIGNAR");
                }else {
                    //Estado diferente a NO TERMINADO
                    if (datos.getString(37).equals("0")) {
                        //CONTRATOENVIADO es igual a 0
                        etNumeroContrato.setText("POR ASIGNAR");
                    }else {
                        //CONTRATOENVIADO es igual a 1
                        etNumeroContrato.setText(datos.getString(0));

                        if(!rol.equals("4") && !garantia && (datos.getString(36).equals("1") || datos.getString(36).equals("9"))) {
                            //Asistente/Optometrista y no sea garantia y (estado del contrato sea TERMINADO y EN PROCESO DE APROBACION)
                            rutaPagare = datos.getString(38);
                            if (rutaPagare.length() > 0) {
                                File fotoPagare = new File(file_path + "/luzatuvida/.nomedia/" + rutaPagare);
                                Bitmap bitmap = null;
                                if (fotoPagare.exists()) {
                                    bitmap = BitmapFactory.decodeFile(fotoPagare.getAbsolutePath());
                                    ivActualizarFotoPagare.setBackground(null);
                                    ivActualizarFotoPagare.setImageBitmap(bitmap);
                                }
                            }
                            llActualizarFotoPagare.setVisibility(View.VISIBLE);
                        }
                    }
                }
                etFechaCreacionContrato.setText(datos.getString(1));
                etSaldoContrato.setText(df00.format(Double.parseDouble(datos.getString(2))));
                etSaldoContrato2.setText(df00.format(Double.parseDouble(datos.getString(2))));
                etCreadorContrato.setText(datos.getString(3).toUpperCase());
                etOptometristaAsignado.setText(datos.getString(4).toUpperCase());
                etClienteContrato.setText(datos.getString(5).toUpperCase());
                etCalleContrato.setText(datos.getString(6).toUpperCase());
                etNumero.setText(datos.getString(7));
                etColoniaContrato.setText(datos.getString(8).toUpperCase());
                etLocalidadContrato.setText(datos.getString(9).toUpperCase());
                etTelefonoContrato.setText(datos.getString(10));
                etColorCasaContrato.setText(datos.getString(11).toUpperCase());
                etAliasContrato.setText(datos.getString(39).toUpperCase());

                if(rol.equals("4")) {
                    if (datos.getInt(12) == 1 || datos.getInt(12) == 2 || datos.getInt(12) == 4) {
                        idsFormasPago = new String[]{"0", datos.getString(12)};
                        formasPago = new String[]{"Contado", obtenerLetrasCambioFormaPagoSpinnerCobranza(datos.getInt(12))};
                        spFormaPago.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, formasPago));
                    }
                    //Ruta foto casa
                    rutaCasa = datos.getString(40);
                }

                spFormaPago.setSelection(Arrays.asList(idsFormasPago).indexOf(datos.getString(12)));

                if(!rol.equals("4") && datos.getString(36).equals("0")) {
                    rutaTarjetaPensionFrente = datos.getString(13);
                    rutaTarjetaPensionAtras = datos.getString(14);
                    if (rutaTarjetaPensionFrente.length() > 0 || rutaTarjetaPensionAtras.length() > 0) {
                        File fotoTarjetaPensionFrente = new File(file_path + "/luzatuvida/.nomedia/" + rutaTarjetaPensionFrente);
                        File fotoTarjetaPensionAtras = new File(file_path + "/luzatuvida/.nomedia/" + rutaTarjetaPensionAtras);
                        Bitmap bitmap = null;
                        if (fotoTarjetaPensionFrente.exists()) {
                            bitmap = BitmapFactory.decodeFile(fotoTarjetaPensionFrente.getAbsolutePath());
                            ivFotoTarjetaPensionFrente.setBackground(null);
                            ivFotoTarjetaPensionFrente.setImageBitmap(bitmap);
                        }
                        if (fotoTarjetaPensionAtras.exists()) {
                            bitmap = BitmapFactory.decodeFile(fotoTarjetaPensionAtras.getAbsolutePath());
                            ivFotoTarjetaPensionAtras.setBackground(null);
                            ivFotoTarjetaPensionAtras.setImageBitmap(bitmap);
                        }
                    }
                }

                spDiaPago.setSelection(Arrays.asList(idsDiaPago).indexOf(convertirDiaEspanol(datos.getString(15))));
                etEntreCallesContrato.setText(datos.getString(16).toUpperCase());
                etNombreReferenciaContrato.setText(datos.getString(17).toUpperCase());
                etTelefonoReferenciaContrato.setText(datos.getString(18).toUpperCase());
                etDepartamentoContrato.setText(datos.getString(19).toUpperCase());
                etAlLadoDeContrato.setText(datos.getString(22).toUpperCase());
                etFrenteAContrato.setText(datos.getString(23).toUpperCase());
                etNombrePaqueteContrato.setText(datos.getString(24).toUpperCase());
                etTotalReal.setText(df00.format(Double.parseDouble(datos.getString(25))));

                if(datos.getString(26).length() > 0) {
                    etTituloPromocionContrato.setText(datos.getString(26));
                }else {
                    etTituloPromocionContrato.setText("SIN PROMOCIÓN");
                }

                etCalleContratoEntrega.setText(datos.getString(27).toUpperCase());
                etNumeroEntrega.setText(datos.getString(28));
                etColoniaContratoEntrega.setText(datos.getString(29).toUpperCase());
                etLocalidadContratoEntrega.setText(datos.getString(30).toUpperCase());
                etColorCasaContratoEntrega.setText(datos.getString(31).toUpperCase());
                etEntreCallesContratoEntrega.setText(datos.getString(32).toUpperCase());
                etDepartamentoContratoEntrega.setText(datos.getString(33).toUpperCase());
                etAlLadoDeContratoEntrega.setText(datos.getString(34).toUpperCase());
                etFrenteAContratoEntrega.setText(datos.getString(35).toUpperCase());

                if(rol.equals("4")  && datos.getString(36).equals("12")){
                    //Rol de cobranza y contrato en ENVIADO
                    idsLugarEntregaProducto = new String[] {"", "0", "1"};
                    opcionesLugarEntregaProcusto = new String[] {"Seleccionar", "Lugar de venta", "Lugar de cobranza"};
                    spLugarEntregaProducto.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, opcionesLugarEntregaProcusto));

                    //Seleccionar opcion guardada en bd
                    String cad = datos.getString(41);
                    if(datos.getString(41).equals("0")){
                        //Lugar de entrega de producto en lugar de venta
                        spLugarEntregaProducto.setSelection(1);
                    }else{
                        //Por default lugar de cobranza
                        spLugarEntregaProducto.setSelection(2);
                    }
                }

                if(datos.getString(20).length() > 0) {
                    //Tiene fechacobroini y fechacobrofin
                    String fechacobro = datos.getString(20).replace(" ", "-");
                    String[] partsFechaCobroIni = fechacobro.split("-");
                    fechacobro = datos.getString(21).replace(" ", "-");
                    String[] partsFechaCobroFin = fechacobro.split("-");
                    etFechaCobroIniFechaCobroFinContrato.setText(partsFechaCobroIni[2] + "-" + partsFechaCobroFin[2]);
                }else {
                    //No han sido asignadas fechacobroini y fechacobrofin
                    llFechaCobroIniFechaCobroFinContrato.setVisibility(View.GONE);
                }

                etObservacionActualizarFotoPagare.setText(datos.getString(42));

            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 157);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        if (estadocontrato >= 1 && rol.equals("4")) {
            pagoMinimo = 0;
            //Obtener pago minimo
            String abonoMinimo = obtenerEstadoAtributoContrato("ABONOMINIMO");
            if(!abonoMinimo.equals("null") && !abonoMinimo.isEmpty() && !abonoMinimo.equals("0")){
                //Si pago minimo es diferente de null, vacio y de 0
                pagoMinimo = Integer.parseInt(abonoMinimo);
            } else {
                //Abono minimo es BD es vacio o es 0 - asignarlo estatico
                String formapago = obtenerEstadoAtributoContrato("PAGO");

                if (idFranquicia.equals("TXDHF") || idFranquicia.equals("WJPQB")) {
                    //Franquicia es igual a XONACATLAN o ATLACOMULCO
                    if (formapago.equals("1")) { //Semanal
                        pagoMinimo = 150;
                    }
                    if (formapago.equals("2")) { //Quincenal
                        pagoMinimo = 300;
                    }
                    if (formapago.equals("4")) { //Mensual
                        pagoMinimo = 400;
                    }
                }else {
                    //Franquicia diferente a XONACATLAN o ATLACOMULCO
                    if (formapago.equals("1")) { //Semanal
                        pagoMinimo = 200;
                    }
                    if (formapago.equals("2")) { //Quincenal
                        pagoMinimo = 400;
                    }
                    if (formapago.equals("4")) { //Mensual
                        pagoMinimo = 600;
                    }
                }
            }

            //Obtener precio abonoentrega en caso de que sea un contrato actualizado 2024
            if (pagoMinimo == 250 || pagoMinimo == 500 || pagoMinimo == 800) {
                //Pago minimo es igual a 250, 500 o 800
                abonoMinimoSemanal = "250"; //Abono entrega de $250
            }

        }

        contratoHijo = contratoHijo();

        obtenerPrecioDescuentoFormaPagoContado();

        mostrarOcultarLlPromocionYFormaPago();

        mostrarPromocionEnVista();
        productosPagados();
        if(rol.equals("4")) {
            mostrarReportarGarantia();
            cambiarTextoNotaContrato();
            calculoTotal();
        }
        cambiarColorIconoUbicacion();
        obtenerListaHistorialesClinicos();
        obtenerListaHistorialesClinicos2();
        obtenerListaProductos();
        obtenerListaAbonos();
        obtenerListaContratosCercanos();

    }

    private void cambiarColorIconoUbicacion() {
        String coordenadas = obtenerEstadoAtributoContrato("COORDENADAS");
        ivIconoUbicacion.setImageDrawable(null);
        if(coordenadas.length() > 0){
            //Tiene coordenadas
            ivIconoUbicacion.setBackground(ContextCompat.getDrawable(fragmento.getContext(),R.drawable.ic_location_azul));
        } else{
            //No tiene coordenadas
            ivIconoUbicacion.setBackground(ContextCompat.getDrawable(fragmento.getContext(),R.drawable.ic_location_rojo));
        }
    }

    private void cambiarTextoNotaContrato() {
        if(obtenerEstadoAtributoContrato("NOTA").length() > 0) {
            //Ya se escribio algo
            tvNotaContrato.setText("Editar nota");
        }else {
            //No se ha escrito nada
            tvNotaContrato.setText("Agregar nota");
        }
    }

    public void mostrarAlertDialogNotaContrato() {

        if(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("6")) {
            //Estado del contrato cancelado
            Toast.makeText(fragmento.getActivity(), "El contrato se encuentra cancelado, ya no se puede abrir la nota", Toast.LENGTH_LONG).show();
        }else {
            //Estado del contrato diferente de cancelado

            String notaContrato = obtenerEstadoAtributoContrato("NOTA");
            String titleAlerta = "";
            String titleMovimiento = "";
            if (notaContrato.length() > 0) {
                //Ya se escribio algo
                titleAlerta = "Editar";
                titleMovimiento = "edito";
            } else {
                //No se ha escrito nada
                titleAlerta = "Agregar";
                titleMovimiento = "agrego";
            }

            final EditText etNotaContrato = new EditText(fragmento.getActivity());
            etNotaContrato.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
            etNotaContrato.setText(notaContrato);
            etNotaContrato.setHint("Nota");

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle(titleAlerta + " nota").setView(etNotaContrato).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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

            String finalTitleMovimiento = titleMovimiento;
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (etNotaContrato.getText().toString().length() == 0) {
                        //Campo etNotaContrato vacio
                        Toast.makeText(fragmento.getActivity(), "Campo vacio", Toast.LENGTH_LONG).show();
                    } else {
                        //Campo etNotaContrato diferente de vacio

                        //Guardar en historial de movimientos
                        historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                "Se " + finalTitleMovimiento + " la nota: " + etNotaContrato.getText().toString(), "0");

                        actualizarEstadoAtributoContrato("NOTA", etNotaContrato.getText().toString());
                        cambiarTextoNotaContrato();

                        //Sincronizar
                        llamadaSincronizacion();

                        Toast.makeText(fragmento.getActivity(), "Se " + finalTitleMovimiento + " correctamente la nota", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                }
            });

        }

    }

    private void mostrarReportarGarantia() {
        int estadocontrato = Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"));

        if(estadocontrato == 12 || estadocontrato == 2 || estadocontrato == 5 || estadocontrato == 4) {
            //Estado del contrato esta en ENVIADO, ENTREGADO O LIQUIDADO

            if(global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) > 0) {
                //Existe por lo menos un historial con garantia
                String resultado = global.obtenerUnResultadoQuery("SELECT ESTADOGARANTIA FROM GARANTIAS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' ORDER BY CREATED_AT DESC LIMIT 1");
                if(resultado.equals("3")) {
                    //El estadogarantia es igual a 3
                    if(validacionFechaEntregaContratoDias(obtenerEstadoAtributoContrato("FECHAENTREGA"))) {
                        //No ha pasado mas de 15 dias que fue la entrega del producto
                        tvReportarGarantia.setText("Reportar garantía");
                        llReportarGarantia.setEnabled(true);
                    }else {
                        //Ya pasaron mas de 15 dias que fue la entrega del producto
                        tvReportarGarantia.setText("Fecha limite para reportar garantías ya expiró");
                        llReportarGarantia.setEnabled(false);
                    }
                    llReportarGarantia.setVisibility(View.VISIBLE);
                }else {
                    //Ocultar texto
                    llReportarGarantia.setVisibility(View.GONE);
                }
            }else {
                //No existe historial con garantia
                String resultado = global.obtenerUnResultadoQuery("SELECT ESTADOGARANTIA FROM GARANTIAS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' ORDER BY CREATED_AT DESC LIMIT 1");
                if(resultado.equals("")) {
                    //No se encontro resultado
                    if(validacionFechaEntregaContratoDias(obtenerEstadoAtributoContrato("FECHAENTREGA"))) {
                        //No ha pasado mas de 15 dias que fue la entrega del producto
                        tvReportarGarantia.setText("Reportar garantía");
                        llReportarGarantia.setEnabled(true);
                    }else {
                        //Ya pasaron mas de 15 dias que fue la entrega del producto
                        tvReportarGarantia.setText("Fecha limite para reportar garantías ya expiró");
                        llReportarGarantia.setEnabled(false);
                    }
                    llReportarGarantia.setVisibility(View.VISIBLE);
                }else {
                    //Ocultar texto
                    llReportarGarantia.setVisibility(View.GONE);
                }
            }

        }
    }

    private boolean validacionFechaEntregaContratoDias(String fechaentregaBD) {

        boolean respuesta = true;

        if(fechaentregaBD.length() > 0) {
            //fechaentregaBD diferente de vacio

            Date fechaactual = new Date();
            Date fechaentrega = new Date();

            try {
                //Cambiar formato fechaentrega proviniente de la BD a yyyy-MM-dd

                SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                fechaactual = convetDateFormat.parse(obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL"));
                fechaentrega = convetDateFormat.parse(fechaentregaBD);

                long diff = fechaactual.getTime() - fechaentrega.getTime();

                if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 15) {
                    respuesta = false;
                }

            } catch (ParseException e) {
                global.escribirError(e, 253);
                Log.i("MENSAJE", "ERROR: " + e.toString());
            }

        }

        return respuesta;
    }

    private String obtenerLetrasCambioFormaPagoSpinnerCobranza(int formaPago) {
        String letras = "";

        switch (formaPago) {
            case 1:
                letras = "Semanal";
                break;
            case 2:
                letras = "Quicenal";
                break;
            case 4:
                letras = "Mensual";
                break;
        }

        return letras;
    }

    private void mostrarOcultarLlPromocionYFormaPago() {

        int estadocontrato = Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"));

        //Cambio de texto en titulo de Contrato
        if(contratoHijo) {
            if(obtenerEstadoPromocion()) {
                tvTextoContrato.setText("Contrato S");
            }else {
                tvTextoContrato.setText("Contrato");
            }
        }else {
            if(obtenerEstadoPromocion()) {
                tvTextoContrato.setText("Contrato P");
            }else {
                tvTextoContrato.setText("Contrato");
            }
        }

        //Cambio de texto boton "TERMINAR CONTRATO"
        if(rol.equals("4")) {
            if(estadocontrato >= 1 && estadocontrato < 12) {
                btnTerminarContrato.setText("SALIR");
            }else {
                btnTerminarContrato.setText("PRODUCTO ENTREGADO");
            }
        }else {
            if(estadocontrato == 0) {
                if(obtenerEstadoPromocion()) {
                    if(!promocionContratoCompletada()) {
                        if (obtenerEstadoAtributoContrato("PROMOCIONTERMINADA").equals("1") && obtenerEstadoAtributoContrato("ESTATUS").equals("1")
                                && obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("0")) {
                            //Confirmaciones lo paso a estado NO TERMINADO
                            btnTerminarContrato.setText("TERMINAR CONTRATO");
                        }else {
                            btnTerminarContrato.setText("SIGUIENTE CONTRATO");
                        }
                    }
                }else {
                    btnTerminarContrato.setText("TERMINAR CONTRATO");
                }
            }else {
                if(obtenerEstadoPromocion()) {
                    if(obtenerEstadoAtributoContrato("PROMOCIONTERMINADA").equals("1")) {
                        btnTerminarContrato.setText("SALIR");
                    }else {
                        if(!promocionContratoCompletada()) {
                            btnTerminarContrato.setText("SIGUIENTE CONTRATO");
                        }else {
                            btnTerminarContrato.setText("SALIR");
                        }
                    }
                }else {
                    btnTerminarContrato.setText("SALIR");
                }
            }
        }

        //Quitar o mostrar llPromocion y llFormaPago
        if(obtenerTotalContrato() != 0) {
            if(rol.equals("4")) {
                llFormaPago.setVisibility(View.VISIBLE);
                if (!obtenerEstadoAtributoContrato("PAGO").equals("0") && obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("12") && !tieneAbonos(false)
                        && global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) == 0) {
                    spFormaPago.setEnabled(true);
                    btnAgregarFormaPago.setEnabled(true);
                }else {
                    spFormaPago.setEnabled(false);
                    btnAgregarFormaPago.setEnabled(false);
                }
                llPromociones.setVisibility(View.GONE);
                tvPromocionLeyenda.setVisibility(View.GONE);
                if(!obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                    //Pago semanal, quincenal o mensual
                    llDiaPago.setVisibility(View.VISIBLE);
                }else {
                    //Pago de contado
                    llDiaPago.setVisibility(View.GONE);
                }
            }else {
                if(estadocontrato == 0) {
                    llFormaPago.setVisibility(View.VISIBLE);
                    if (contratoHijo) {
                        llPromociones.setVisibility(View.GONE);
                        tvPromocionLeyenda.setVisibility(View.GONE);
                    } else {
                        if (obtenerBanderaEnganche()) {
                            llPromociones.setVisibility(View.GONE);
                            tvPromocionLeyenda.setVisibility(View.VISIBLE);
                        } else {
                            llPromociones.setVisibility(View.VISIBLE);
                            tvPromocionLeyenda.setVisibility(View.GONE);
                        }
                    }
                }else {
                    if(contratoHijo) {
                        llPromociones.setVisibility(View.GONE);
                        tvPromocionLeyenda.setVisibility(View.GONE);
                    }else {
                        if(obtenerBanderaEnganche()) {
                            llPromociones.setVisibility(View.GONE);
                            tvPromocionLeyenda.setVisibility(View.VISIBLE);
                        }else {
                            if(verificarContratoPadreEstaEnTablaPromocion()) {
                                if(global.contadorContratosOHistorialesTablaGarantias(ultimoIdContratoCreado, "", false) == 0) {
                                    //No tiene historiales con garantia
                                    llPromociones.setVisibility(View.VISIBLE);
                                    tvPromocionLeyenda.setVisibility(View.GONE);
                                }else {
                                    //Tiene historiales con garantia
                                    llPromociones.setVisibility(View.GONE);
                                    tvPromocionLeyenda.setVisibility(View.GONE);
                                }
                            }else {
                                llPromociones.setVisibility(View.GONE);
                                tvPromocionLeyenda.setVisibility(View.GONE);
                            }
                        }
                    }
                    llFormaPago.setVisibility(View.VISIBLE);
                    spFormaPago.setEnabled(false);
                    btnAgregarFormaPago.setEnabled(false);
                    ivFotoTarjetaPensionFrente.setEnabled(false);
                    ivFotoTarjetaPensionAtras.setEnabled(false);
                }
            }
        }else {
            llPromociones.setVisibility(View.GONE);
            tvPromocionLeyenda.setVisibility(View.GONE);
            llFormaPago.setVisibility(View.GONE);
            llDiaPago.setVisibility(View.GONE);
        }
    }

    private boolean verificarContratoPadreEstaEnTablaPromocion() {

        boolean respuestaBoleana = true;

        try{

            sqLiteDB = conexion.getReadableDatabase();

            String SQL = "SELECT ID_CONTRATO FROM PROMOCIONCONTRATO WHERE ID_CONTRATO = '" + idContratoPadre + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                respuestaBoleana = false;
            }

            datos.close();
            sqLiteDB.close();

        }catch (SQLiteException e){
            global.escribirError(e, 158);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return respuestaBoleana;
    }

    private boolean obtenerEstadoPromocion() {

        boolean estado = false;
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ESTADO FROM PROMOCIONCONTRATO WHERE ID_CONTRATO='" + idContratoPadre + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()) {
                if(Integer.parseInt(datos.getString(0)) > 0) {
                    estado = true;
                }
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 159);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return estado;
    }

    private boolean contratoHijo() {

        idContratoPadre = "";
        boolean hijo = false;
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT IDCONTRATORELACION FROM CONTRATOS" +
                    " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el IDCONTRATORELACION el en contrato");
            }

            if (datos.moveToFirst()){
                if(datos.getString(0).length() == 0){
                    idContratoPadre = ultimoIdContratoCreado;
                }else{
                    idContratoPadre = datos.getString(0);
                    hijo =  true;
                }

            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 160);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return hijo;
    }

    private boolean promocionContratoCompletada() {

        boolean estadoPromocion = false;
        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT CONTRATOS.ID_PROMOCION, CONTRATOS.CONTADOR, PROMOCION.ARMAZONES FROM CONTRATOS" +
                    " INNER JOIN PROMOCION WHERE CONTRATOS.ID_PROMOCION = PROMOCION.ID AND CONTRATOS.ID_CONTRATO='" + idContratoPadre + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()){
                if(datos.getString(0).length() > 0){
                    if(Integer.parseInt(datos.getString(1)) == Integer.parseInt(datos.getString(2))) {
                        estadoPromocion = true;
                    }
                }
            }
            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 161);
            Log.i("ERRORBD", e.getMessage() + "");
        }
        return estadoPromocion;
    }

    private void obtenerListaHistorialesClinicos() {

        filaHistorialesClinicos.clear();
        coloresHistorial = new ArrayList<>();

        if (!rol.equals("4")) {
            //Asistente/Optometrista

            try {

                sqLiteDB = conexion.getReadableDatabase();
                String SQL = "SELECT H.ID, H.DIAGNOSTICO, H.CREATED_AT, H.FECHAENTREGA, H.ID_PAQUETE, " +
                        "(SELECT COUNT(G.ID) FROM GARANTIAS G WHERE G.ID_HISTORIAL = H.ID AND ESTADOGARANTIA = '1') AS GARANTIA," +
                        " H.TIPO, (SELECT COUNT(G.ID) FROM GARANTIAS G WHERE G.ID_HISTORIALGARANTIA = H.ID) AS EXISTEGARANTIA FROM HISTORIALCLINICO H " +
                        "WHERE H.ID_CONTRATO='" + ultimoIdContratoCreado + "' ORDER BY H.CREATED_AT";

                Cursor datos = sqLiteDB.rawQuery(SQL, null);

                if (datos.getCount() == 0) {
                    lvHistorialesClinicos.removeAllViewsInLayout();
                    lvHistorialesClinicos.postInvalidate();
                } else {

                    while (datos.moveToNext()) {

                        if (Integer.parseInt(datos.getString(4)) == 6) {
                            if (datos.getCount() == 1) {
                                btnNuevoHistorial.setVisibility(View.VISIBLE);
                            }
                        }

                        FilaHistorialesClinicos fila = new FilaHistorialesClinicos();
                        fila.setIdHistorial(datos.getString(0));
                        fila.setDiagnostico(datos.getString(1).toUpperCase());

                        //Convertir la fecha de un formato a otro
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;

                        try {

                            date = simpleDateFormat.parse(datos.getString(2));
                            SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            fila.setFechaVisita(convetDateFormat.format(date));
                            fila.setFechaEntrega(datos.getString(3));
                            if (datos.getInt(6) == 1) {
                                //Tipo historial es igual a 1
                                if (datos.getInt(7) > 0) {
                                    //Existe el historial en la tabla garantias
                                    coloresHistorial.add(2); //Azul
                                } else {
                                    //No existe el historial en la tabla garantias (Es de otro optometrista o ya paso el dia en que lo creo el optometrista)
                                    coloresHistorial.add(3); //Verde
                                }
                            } else {
                                if (datos.getInt(5) > 0) {
                                    //Es un histiorial de garantia por crear
                                    coloresHistorial.add(1); //Rosa
                                } else {
                                    //Es un historial normal
                                    coloresHistorial.add(0); //Normal
                                }
                            }

                        } catch (ParseException e) {
                            global.escribirError(e, 162);
                            e.printStackTrace();
                        }

                        filaHistorialesClinicos.add(fila);
                    }

                    //Agregar la lista en el adaptador
                    adaptadorListaHistorialesClinicos adaptador = new adaptadorListaHistorialesClinicos(fragmento, filaHistorialesClinicos) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup viewGroup) {
                            View view = super.getView(position, convertView, viewGroup);

                            //Color de item de la lista dependiendo de si tiene o no garantia
                            if (coloresHistorial.get(position) == 1) {
                                //Historial garantia por crear
                                view.setBackgroundColor(Color.parseColor("#F48FB1"));
                            }
                            if (coloresHistorial.get(position) == 2) {
                                //Historial garantia creado
                                view.setBackgroundColor(Color.parseColor("#5bc0de"));
                            }
                            if (coloresHistorial.get(position) == 3) {
                                //No existe el historial en la tabla garantias (Es de otro optometrista o ya paso el dia en que lo creo el optometrista)
                                view.setBackgroundColor(Color.parseColor("#5cb85c"));
                            }

                            return view;
                        }
                    };
                    lvHistorialesClinicos.setAdapter(adaptador);

                    if (datos.getCount() > 2) {
                        ListAdapter listadp = lvHistorialesClinicos.getAdapter();
                        if (listadp != null) {
                            int totalHeight = 0;
                            for (int i = 0; i < listadp.getCount(); i++) {
                                View listItem = listadp.getView(i, null, lvHistorialesClinicos);
                                listItem.measure(0, 0);
                                totalHeight += listItem.getMeasuredHeight();
                            }
                            ViewGroup.LayoutParams params = lvHistorialesClinicos.getLayoutParams();
                            params.height = totalHeight + (lvHistorialesClinicos.getDividerHeight() * (listadp.getCount() - 1));
                            lvHistorialesClinicos.setLayoutParams(params);
                            lvHistorialesClinicos.requestLayout();
                        }

                    }

                }

                sqLiteDB.close();
                datos.close();

            } catch (Exception e) {
                global.escribirError(e, 163);
                Log.i("ERRORBD", e.getMessage() + "");
            }

        }

    }

    private void obtenerListaHistorialesClinicos2() {

        filaHistorialesClinicos2.clear();
        coloresHistorial2 = new ArrayList<>();

        if(rol.equals("4")) {
            //Cobranza

            try {

                SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
                String SQL = "SELECT ID, DIAGNOSTICO, OBSERVACIONES, FECHAENTREGA, ID_PAQUETE, OBSERVACIONESINTERNO, TIPO FROM HISTORIALCLINICO WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' " +
                        "ORDER BY CREATED_AT";
                Cursor datos = sqLiteDB2.rawQuery(SQL, null);

                if (datos.getCount() == 0) {
                    lvHistorialesClinicos2.removeAllViewsInLayout();
                    lvHistorialesClinicos2.postInvalidate();
                } else {

                    while (datos.moveToNext()) {

                        FilaHistorialesClinicos fila = new FilaHistorialesClinicos();
                        fila.setIdHistorial(datos.getString(0));
                        fila.setDiagnostico(datos.getString(1).toUpperCase());

                        //Convertir la fecha de un formato a otro
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;

                        fila.setFechaVisita(datos.getString(3)); //Fecha entrega
                        if (datos.getString(2).length() == 0 && datos.getString(5).length() == 0) { //Observacion o Observacion Interno
                            fila.setFechaEntrega("Ninguna");
                        } else {
                            if (datos.getString(2).length() > 0) {
                                fila.setFechaEntrega(datos.getString(2));
                            }
                            if (datos.getString(5).length() > 0) {
                                fila.setFechaEntrega(datos.getString(5));
                            }
                            if (datos.getString(2).length() > 0 && datos.getString(5).length() > 0) {
                                fila.setFechaEntrega(datos.getString(2));
                            }
                        }

                        //Color a los historiales
                        if (datos.getInt(6) == 1) {
                            coloresHistorial2.add(3); //Verde
                        } else {
                            coloresHistorial2.add(0); //Normal
                        }

                        filaHistorialesClinicos2.add(fila);
                    }

                    //Agregar la lista en el adaptador
                    adaptadorListaHistorialesClinicos adaptador = new adaptadorListaHistorialesClinicos(fragmento, filaHistorialesClinicos2) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup viewGroup) {
                            View view = super.getView(position, convertView, viewGroup);

                            //Color de item de la lista dependiendo de si tiene o no garantia
                            if (coloresHistorial2.get(position) == 1) {
                                //Historial garantia por crear
                                view.setBackgroundColor(Color.parseColor("#F48FB1"));
                            }
                            if (coloresHistorial2.get(position) == 2) {
                                //Historial garantia creado
                                view.setBackgroundColor(Color.parseColor("#5bc0de"));
                            }
                            if (coloresHistorial2.get(position) == 3) {
                                //No existe el historial en la tabla garantias (Es de otro optometrista o ya paso el dia en que lo creo el optometrista)
                                view.setBackgroundColor(Color.parseColor("#5cb85c"));
                            }

                            return view;
                        }
                    };
                    lvHistorialesClinicos2.setAdapter(adaptador);

                    if (datos.getCount() > 2) {
                        ListAdapter listadp = lvHistorialesClinicos2.getAdapter();
                        if (listadp != null) {
                            int totalHeight = 0;
                            for (int i = 0; i < listadp.getCount(); i++) {
                                View listItem = listadp.getView(i, null, lvHistorialesClinicos2);
                                listItem.measure(0, 0);
                                totalHeight += listItem.getMeasuredHeight();
                            }
                            ViewGroup.LayoutParams params = lvHistorialesClinicos2.getLayoutParams();
                            params.height = totalHeight + (lvHistorialesClinicos2.getDividerHeight() * (listadp.getCount() - 1));
                            lvHistorialesClinicos2.setLayoutParams(params);
                            lvHistorialesClinicos2.requestLayout();
                        }

                    }

                }

                sqLiteDB2.close();
                datos.close();

            } catch (Exception e) {
                global.escribirError(e, 272);
                Log.i("ERRORBD", e.getMessage() + "");
            }

        }

    }

    public void irAPantallaNuevoHistorialClinico() {
        String idHistorialClinico = obtenerIdHistorialContratoCreado();

        Fragment verificadorFragment = new nuevoHistorialClinico();
        Bundle bundle = new Bundle();
        bundle.putString("ultimoIdContratoCreado", ultimoIdContratoCreado);
        bundle.putString("ultimoIdHistorialClinicoCreado", idHistorialClinico);
        bundle.putInt("posicionLista", 1);
        verificadorFragment.setArguments(bundle);
        FragmentTransaction transaction = ((FragmentActivity)fragmento.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private String obtenerIdHistorialContratoCreado() {

        String idHistorialClinico = "";

        try{
            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID FROM HISTORIALCLINICO WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);
            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el ID el en historial");
            }

            if (datos.moveToFirst()){
                idHistorialClinico = datos.getString(0);
            }

            sqLiteDB.close();
            datos.close();
        }catch (SQLiteException e){
            global.escribirError(e, 164);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return idHistorialClinico;
    }

    public void mostrarAlertDialogPromociones() {

        if(obtenerEstadoAtributoContrato("PAGO").equals("")) {
            Toast.makeText(fragmento.getActivity(), "Debes agregar una forma de pago", Toast.LENGTH_LONG).show();
        }else {

            if(contadorAbonosContratoPadreEHijos() == 0) {

                if (!global.obtenerUnResultadoQuery("SELECT ID FROM PROMOCION DESC LIMIT 1").equals("")) {
                    //Existen promociones

                    AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                    final Spinner spPromociones = new Spinner(fragmento.getActivity());

                    llenarSpinnersPromocionesBD(spPromociones);
                    alerta.setTitle("PROMOCIONES")
                            .setView(spPromociones).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (idsPromociones[spPromociones.getSelectedItemPosition()] == "") {
                                Toast.makeText(fragmento.getActivity(), "Debes seleccionar una promoción", Toast.LENGTH_LONG).show();
                            } else {
                                guardarPromocionBD(spPromociones);
                                dialog.dismiss();
                            }
                        }
                    });

                }else {
                    //No existe ninguna promocion
                    Toast.makeText(fragmento.getActivity(), "No hay promociones activas, comunicate con encargadas para que te habiliten la promoción", Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(fragmento.getActivity(), "No puedes agregar promoción por que se tiene abonos", Toast.LENGTH_LONG).show();
            }

        }

    }

    private int contadorAbonosContratoPadreEHijos() {

        int contador = 0;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT COUNT(A.ID) FROM ABONOS A INNER JOIN CONTRATOS C ON A.ID_CONTRATO = C.ID_CONTRATO" +
                    " WHERE CAST(A.TIPOABONO as INT) != 7 AND (C.ID_CONTRATO='" + ultimoIdContratoCreado + "' OR C.IDCONTRATORELACION='" + idContratoPadre + "')";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()) {
                contador = datos.getInt(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 165);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return contador;

    }

    private void llenarSpinnersPromocionesBD(Spinner spPromociones) {

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID, TITULO FROM PROMOCION";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay promociones registradas");
            }

            idsPromociones = new String[datos.getCount() + 1];
            tituloPromociones = new String[datos.getCount() + 1];
            idsPromociones[0] = "";
            tituloPromociones[0] = "Seleccionar";

            if(datos.getCount()>0){

                for(int i=1; i <= datos.getCount(); i++){

                    idsPromociones[i]= datos.getString(0);
                    tituloPromociones[i] = datos.getString(1);

                    datos.moveToNext();
                }

            }

            sqLiteDB.close();
            datos.close();

            spPromociones.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, tituloPromociones));

        }catch (SQLiteException e){
            global.escribirError(e, 166);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void guardarPromocionBD(Spinner spPromociones) {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("PROMOCIONCONTRATO", 5);

        try {

            sqLiteDB = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID", idAlfanumerico);
            valores.put("ID_CONTRATO", ultimoIdContratoCreado);
            valores.put("ID_PROMOCION", idsPromociones[spPromociones.getSelectedItemPosition()]);
            valores.put("ESTADO", "1");
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT",  fechaActual + time);
            valores.put("UPDATED_AT", fechaActual + time);
            sqLiteDB.insert("PROMOCIONCONTRATO",null, valores);
            sqLiteDB.close();

            historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                    "Se agrego la promoción '" + tituloPromociones[spPromociones.getSelectedItemPosition()] + "'", "0");
            Toast.makeText(fragmento.getActivity(),
                    "Se agrego correctamente la promoción", Toast.LENGTH_LONG).show();

            actualizarContratoPromocion(spPromociones);
            mostrarPromocionEnVista();
            mostrarOcultarLlPromocionYFormaPago();

        }catch (SQLiteException e) {
            global.escribirError(e, 167);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        llamadaSincronizacion();

    }

    private void actualizarContratoPromocion(Spinner spPromociones) {

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET ID_PROMOCION='" + idsPromociones[spPromociones.getSelectedItemPosition()]
                    + "' WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 168);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void mostrarPromocionEnVista() {

        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT PROMOCIONCONTRATO.ESTADO, PROMOCION.TITULO, PROMOCION.INICIO, PROMOCION.FIN FROM PROMOCIONCONTRATO" +
                    " INNER JOIN PROMOCION WHERE PROMOCIONCONTRATO.ID_PROMOCION = PROMOCION.ID AND PROMOCIONCONTRATO.ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                llListaPromociones.setVisibility(View.GONE);
                Log.i("MENSAJE", "No hay datos en la lista promoción");
            }

            if (datos.moveToFirst()) {
                if (Integer.parseInt(datos.getString(0)) > 0) {
                    tvEstadoPromocion.setBackgroundColor(Color.parseColor("#9BE09C"));
                    btnNuevaPromocion.setVisibility(View.GONE);
                    cambioStatusPromocion = true;
                }else {
                    tvEstadoPromocion.setBackgroundColor(Color.parseColor("#FFACA6"));
                    btnNuevaPromocion.setVisibility(View.GONE);
                }
                tvTituloPromocion.setText(datos.getString(1));
                tvInicioPromocion.setText(datos.getString(2));
                tvFinPromocion.setText(datos.getString(3));

                llListaPromociones.setVisibility(View.VISIBLE);
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 169);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    public void validarCambioStatusPromocion() {

        if(obtenerEstadoAtributoContrato("PROMOCIONTERMINADA").equals("1")) {

            if(cambioStatusPromocion) {
                cambiarStatusPromocion();
            }else {

                if (contadorAbonosContratoPadreEHijos() == 0) {
                    cambiarStatusPromocion();
                } else {

                    String mensaje = "";
                    if(obtenerEstadoAtributoContrato("CONTADOR").equals("1")) {
                        mensaje = "No puedes activar promoción porque se tiene abonos";
                    }else {
                        mensaje = "No puedes activar promoción porque contrato Principal/Relacionados tienen abonos: " + obtenerIdsContratosConAbonos();
                    }
                    Toast.makeText(fragmento.getActivity(), mensaje, Toast.LENGTH_LONG).show();

                }

            }

        }

    }

    private String obtenerIdsContratosConAbonos() {

        String IdsContratosConAbonos = "";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT C.ID_CONTRATO FROM ABONOS A INNER JOIN CONTRATOS C ON A.ID_CONTRATO = C.ID_CONTRATO" +
                    " WHERE CAST(A.TIPOABONO as INT) != 7 AND (C.ID_CONTRATO='" + ultimoIdContratoCreado + "' OR C.IDCONTRATORELACION='" + idContratoPadre + "')" +
                    " ORDER BY C.ID_CONTRATO";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            int pos = datos.getCount();

            while (datos.moveToNext()) {

                if(pos == 1) {
                    IdsContratosConAbonos += datos.getString(0) + ". ";
                }else {
                    IdsContratosConAbonos += datos.getString(0) + ", ";
                }
                pos--;
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 170);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return IdsContratosConAbonos;
    }

    private void cambiarStatusPromocion() {

        cambioStatusPromocion = !cambioStatusPromocion;
        String cambioStatus = cambioStatusPromocion ? "1" : "0";
        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE PROMOCIONCONTRATO SET ESTADO='" + cambioStatus + "', ENVIADOPAGINA = '0', UPDATED_AT = '" + fechaActual + time
                    + "' WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

            mostrarPromocionEnVista();
            if (cambioStatusPromocion) {
                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado, "Activo la promoción en el contrato", "0");
                Toast.makeText(fragmento.getActivity(), "Se activo la promoción en el contrato", Toast.LENGTH_LONG).show();
            } else {
                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado, "Desactivo la promoción en el contrato", "0");
                Toast.makeText(fragmento.getActivity(), "Se desactivo la promoción en el contrato", Toast.LENGTH_LONG).show();
            }

            calculoTotal();
            mostrarOcultarLlPromocionYFormaPago();

        } catch (SQLiteException e) {
            global.escribirError(e, 171);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        llamadaSincronizacion();

    }

    private void calculoTotal() {

        actualizarTotalProductoContratoBD();
        actualizarTotalAbonoContratoBD();

        String sumar = "TOTALHISTORIAL";

        if(obtenerEstadoPromocion()) {
            if(obtenerEstadoAtributoContrato("ESTATUS").equals("1")) {
                sumar = "TOTALPROMOCION";
            }
        }

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET ENVIADOPAGINA = '0', TOTAL = CASE WHEN (CAST((CASE WHEN LENGTH(" + sumar + ") = 0 THEN '0' ELSE " + sumar + " END) AS DECIMAL(5,2)) -"
                    + " CAST((CASE WHEN LENGTH(TOTALABONO) = 0 THEN '0' ELSE TOTALABONO END) AS DECIMAL(5,2)) +"
                    + " CAST((CASE WHEN LENGTH(TOTALPRODUCTO) = 0 THEN '0' ELSE TOTALPRODUCTO END) AS DECIMAL(5,2))) < 1 THEN '0' ELSE" +
                    " (CAST((CASE WHEN LENGTH(" + sumar + ") = 0 THEN '0' ELSE " + sumar + " END) AS DECIMAL(5,2)) -" +
                    " CAST((CASE WHEN LENGTH(TOTALABONO) = 0 THEN '0' ELSE TOTALABONO END) AS DECIMAL(5,2)) +" +
                    " CAST((CASE WHEN LENGTH(TOTALPRODUCTO) = 0 THEN '0' ELSE TOTALPRODUCTO END) AS DECIMAL(5,2))) END"
                    + " WHERE IDCONTRATORELACION='" + idContratoPadre + "' OR ID_CONTRATO='" + idContratoPadre + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

            etSaldoContrato.setText(df00.format(obtenerTotalContrato()));
            etSaldoContrato2.setText(df00.format(obtenerTotalContrato()));

        }catch (SQLiteException e) {
            global.escribirError(e, 172);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    private double obtenerTotalContrato() {

        double totalContrato = 0;

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ROUND(CAST(TOTAL AS DECIMAL(5,1)), 1) FROM CONTRATOS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el contrato");
            }

            if (datos.moveToFirst()) {
                totalContrato = datos.getDouble(0);
            }

            sqLiteDB.close();
            datos.close();

            return totalContrato;

        }catch (SQLiteException e){
            global.escribirError(e, 173);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return totalContrato;
    }

    public void validarFormaPagoContrato() {

        if(idsFormasPago[spFormaPago.getSelectedItemPosition()] == "") {
            Toast.makeText(fragmento.getActivity(), "Seleccionar forma de pago", Toast.LENGTH_LONG).show();
        }else {

            String pago = obtenerEstadoAtributoContrato("PAGO");

            if(!pago.equals("0")) {
                //Semanal, Quincenal o Mensual

                if(idsFormasPago[spFormaPago.getSelectedItemPosition()] == "0") {
                    //Quiere cambiar a forma de pago de contado

                    if(!obtenerBanderaEnganche()) {
                        //No tiene el abono de enganche
                        actualizarFormaPagoContrato(false);
                    }else {
                        //Tiene el abono de enganche
                        spFormaPago.setSelection(Arrays.asList(idsFormasPago).indexOf(pago));
                        Toast.makeText(fragmento.getActivity(), "No se puede cambiar la forma de pago por que se tiene el abono de enganche", Toast.LENGTH_LONG).show();
                    }

                }else {
                    //Quiere cambiar a forma de pago de semanal, quicenal o mensual

                    if(idsFormasPago[spFormaPago.getSelectedItemPosition()] == "4") {
                        //Mensual
                        actualizarFormaPagoContrato(true);

                    }else {
                        //Semanal o Quincenal
                        actualizarFormaPagoContrato(false);
                    }

                }

            }else {
                //Contado

                if(idsFormasPago[spFormaPago.getSelectedItemPosition()] != "0") {
                    //Quiere cambiar a forma de pago a semanal, quicenal o mensual
                    if(!obtenerBanderaEnganche()) {

                        if(idsFormasPago[spFormaPago.getSelectedItemPosition()] == "4") {
                            //Mensual
                            actualizarFormaPagoContrato(true);

                        }else {
                            //Semanal o Quincenal
                            actualizarFormaPagoContrato(false);
                        }

                    }else {
                        spFormaPago.setSelection(Arrays.asList(idsFormasPago).indexOf(pago));
                        Toast.makeText(fragmento.getActivity(), "No se puede cambiar la forma de pago por que se tiene el abono de enganche", Toast.LENGTH_LONG).show();
                    }
                }


            }

        }

    }

    private void actualizarFormaPagoContrato(boolean mensual) {

        if(mensual) {

            if(ivFotoTarjetaPensionFrente.getDrawable() == null || ivFotoTarjetaPensionAtras.getDrawable() == null) {
                Toast.makeText(fragmento.getActivity(), "No se han agregado las fotos correspondientes", Toast.LENGTH_LONG).show();
            }else {

                borrarSiExistenImagenesPensionBD();
                time = new SimpleDateFormat("HHmmss").format(new Date());
                rutaTarjetaPensionFrente = camara.guardarImagenGaleria(ivFotoTarjetaPensionFrente, "Foto-Tarjeta-Pension-Frente-Contrato-" + ultimoIdContratoCreado + "-" +
                        fechaActual.replace("-", "") + time);
                rutaTarjetaPensionAtras = camara.guardarImagenGaleria(ivFotoTarjetaPensionAtras, "Foto-Tarjeta-Pension-Atras-Contrato-" + ultimoIdContratoCreado + "-" +
                        fechaActual.replace("-", "") + time);
                guardarFormaPagoContratoBD();
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "TARJETAPENSION", "0", "ID_CONTRATO", ultimoIdContratoCreado);
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "TARJETAPENSIONATRAS", "0", "ID_CONTRATO", ultimoIdContratoCreado);

            }

        }else {
            borrarSiExistenImagenesPensionBD();
            rutaTarjetaPensionFrente = "";
            rutaTarjetaPensionAtras = "";
            ivFotoTarjetaPensionFrente.setImageDrawable(null);
            ivFotoTarjetaPensionAtras.setImageDrawable(null);
            ivFotoTarjetaPensionFrente.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.tarjeta));
            ivFotoTarjetaPensionAtras.setBackground(ContextCompat.getDrawable(fragmento.getContext(), R.drawable.tarjeta));
            guardarFormaPagoContratoBD();
            global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "TARJETAPENSION", "2", "ID_CONTRATO", ultimoIdContratoCreado);
            global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "TARJETAPENSIONATRAS", "2", "ID_CONTRATO", ultimoIdContratoCreado);
        }
    }

    private void borrarSiExistenImagenesPensionBD() {

        if(rutaTarjetaPensionFrente.length() > 0 || rutaTarjetaPensionAtras.length() > 0) {
            File fotoTarjetaPensionFrente = new File(file_path + "/luzatuvida/.nomedia/" + rutaTarjetaPensionFrente);
            File fotoTarjetaPensionAtras = new File(file_path + "/luzatuvida/.nomedia/" + rutaTarjetaPensionAtras);
            if(fotoTarjetaPensionFrente.exists()) {
                fotoTarjetaPensionFrente.delete();
            }
            if(fotoTarjetaPensionAtras.exists()) {
                fotoTarjetaPensionAtras.delete();
            }
        }

    }

    private void guardarFormaPagoContratoBD() {

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET PAGO='" + idsFormasPago[spFormaPago.getSelectedItemPosition()]
                    + "', TARJETAFRENTE='" + rutaTarjetaPensionFrente
                    + "', TARJETAATRAS='" + rutaTarjetaPensionAtras
                    + "' WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

            historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                    "Se actualizo la forma de pago '" + formasPago[spFormaPago.getSelectedItemPosition()] + "'", "0");
            Toast.makeText(fragmento.getActivity(),
                    "Se actualizo correctamente la forma de pago", Toast.LENGTH_LONG).show();

            if(!obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                //Pago semanal, quincenal o mensual
                llDiaPago.setVisibility(View.VISIBLE);
            }else {
                //Pago de contado
                llDiaPago.setVisibility(View.GONE);
            }

            if(rol.equals("4") && obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                obtenerPrecioDescuentoFormaPagoContado();
            }

        }catch (SQLiteException e) {
            global.escribirError(e, 174);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        llamadaSincronizacion();

    }

    public void mostrarAlertDialogProducto() {

        if (obtenerEstadoAtributoContrato("PAGO").equals("")) {
            Toast.makeText(fragmento.getActivity(), "Debes agregar una forma de pago", Toast.LENGTH_LONG).show();
        } else {

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            inflater = (LayoutInflater) fragmento.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            searchView = inflater.inflate(R.layout.dialog_producto, null);

            spProductoAlertDialog = searchView.findViewById(R.id.spProductoAlertDialog);
            etPrecioProductoAlertDialog = searchView.findViewById(R.id.etPrecioProductoAlertDialog);
            etPiezasProductoAlertDialog = searchView.findViewById(R.id.etPiezasProductoAlertDialog);
            etNumPiezasProductoAlertDialog = searchView.findViewById(R.id.etNumPiezasProductoAlertDialog);
            llPrecioPromocion = searchView.findViewById(R.id.llPrecioPromocion);
            etPrecioProductoPromocionAlertDialog = searchView.findViewById(R.id.etPrecioProductoPromocionAlertDialog);
            llAplicarFolioPoliza = searchView.findViewById(R.id.llAplicarFolioPoliza);
            cbAplicarPoliza = searchView.findViewById(R.id.cbAplicarPoliza);
            etFolioPoliza = searchView.findViewById(R.id.etFolioPoliza);
            tvAutorizacionArmazon = searchView.findViewById(R.id.tvAutorizacionArmazon);

            String estatusAutorizacionArmazon = obtenerEstadoAtributoContrato("AUTORIZACION");

            if (rol.equals("4")) {
                //Cobranza
                //Validacion de estatus de autorizacion de armazon
                if (estatusAutorizacionArmazon.equals("0") || estatusAutorizacionArmazon.equals("2")) {
                    tvAutorizacionArmazon.setVisibility(View.VISIBLE);
                    if (estatusAutorizacionArmazon.equals("0")) {
                        //Solicitud de armazon pendiente
                        tvAutorizacionArmazon.setText("Solicitud de armazón pendiente.");
                    } else {
                        //Solicitud de armazon rechazada
                        tvAutorizacionArmazon.setText("Última solicitud de armazón rechazada.");
                    }
                }
            }

            cbAplicarPoliza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        etFolioPoliza.setEnabled(true);
                    } else {
                        etFolioPoliza.setEnabled(false);
                        etFolioPoliza.setText("");
                    }
                }
            });

            llenarSpinnersProductosBD();

            alerta.setTitle("PRODUCTOS").setView(searchView)
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

            spProductoAlertDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    boolean promocionProducto = agregarPrecioYPiezasProducto();

                    String existePolizaVigente = "";
                    String tipoProducto = global.obtenerAtributoTabla("PRODUCTO", "ID_TIPOPRODUCTO", "ID", idsProductos[spProductoAlertDialog.getSelectedItemPosition()]);

                    if (rol.equals("4")) {
                        //Cobranza
                        if (tipoProducto.equals("1")) {
                            //Tipo producto es igual a armazon
                            if (estatusAutorizacionArmazon.equals("1")) {
                                //No se ha hecho ninguna solicitud o SOLICITUD APROBADA
                                tvAutorizacionArmazon.setVisibility(View.VISIBLE);
                                tvAutorizacionArmazon.setText("El abono se vera reflejado en tu corte cuando la solicitud de armazón haya sido aprobada.");
                            }

                            existePolizaVigente = global.obtenerUnResultadoQuery("SELECT CP.CREATED_AT FROM CONTRATOPRODUCTO CP" +
                                    " INNER JOIN PRODUCTO P ON P.ID = CP.ID_PRODUCTO WHERE CP.ID_CONTRATO = '" + ultimoIdContratoCreado + "'" +
                                    " AND P.ID_TIPOPRODUCTO = '2' ORDER BY CP.CREATED_AT DESC LIMIT 1");
                            if (existePolizaVigente.length() > 0) {
                                //Si encontro un producto de tipo poliza
                                if (Double.parseDouble(global.obtenerUnResultadoQuery("SELECT JULIANDAY('" + fechaActual + "') - JULIANDAY('" + existePolizaVigente + "') AS diferencia")) > 365) {
                                    //Supero el año la poliza
                                    existePolizaVigente = "";
                                    Toast.makeText(fragmento.getActivity(), "La poliza ya venció, favor de adquirir una nueva para que aplique descuento", Toast.LENGTH_LONG).show();
                                } else {
                                    //No ha superado mas del año la poliza
                                    llAplicarFolioPoliza.setVisibility(View.VISIBLE); //Mostrar seccion de Aplicar y folio poliza
                                }
                            }
                        }else {
                            //Tipo producto es diferente a armazon
                            if (estatusAutorizacionArmazon.equals("1")) {
                                //No se ha hecho ninguna solicitud o SOLICITUD APROBADA
                                tvAutorizacionArmazon.setVisibility(View.GONE);
                            }
                        }
                    }

                    String finalExistePolizaVigente = existePolizaVigente;
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (idsProductos[spProductoAlertDialog.getSelectedItemPosition()] == "") {
                                Toast.makeText(fragmento.getActivity(), "Debes seleccionar un producto", Toast.LENGTH_LONG).show();
                            } else {

                                if (etNumPiezasProductoAlertDialog.getText().toString().equals("") || Double.parseDouble(etNumPiezasProductoAlertDialog.getText().toString()) == 0) {
                                    Toast.makeText(fragmento.getActivity(), "Favor de agregar el numero de piezas", Toast.LENGTH_LONG).show();
                                } else {

                                    if (Double.parseDouble(etNumPiezasProductoAlertDialog.getText().toString()) > Double.parseDouble(etPiezasProductoAlertDialog.getText().toString())) {
                                        Toast.makeText(fragmento.getActivity(), "El numero de piezas ingresado excede el numero de piezas disponibles", Toast.LENGTH_LONG).show();
                                    } else {

                                        if (!rol.equals("4")) {
                                            //Asistente/Optometrista
                                            guardarProductoContratoProductoBD(promocionProducto, finalExistePolizaVigente, tipoProducto);
                                            dialog.dismiss();
                                        } else {
                                            //Cobranza
                                            boolean respuesta = true;

                                            if (finalExistePolizaVigente.length() > 0 && cbAplicarPoliza.isChecked() && etFolioPoliza.getText().toString().length() == 0) {
                                                Toast.makeText(fragmento.getActivity(), "Debes agregar el folio de la poliza", Toast.LENGTH_LONG).show();
                                            } else {

                                                if (tipoProducto.equals("1")) {
                                                    //Tipo producto es igual a armazon

                                                    if (estatusAutorizacionArmazon.equals("0")) {
                                                        //Se tiene una solicitud pendiente
                                                        Toast.makeText(fragmento.getActivity(), "Hay una solicitud de armazón pendiente", Toast.LENGTH_LONG).show();
                                                    }else {
                                                        //No se tiene solicitud pendiente o la ultima fue rechazada

                                                        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

                                                        String tipoAutorizacion = "8";
                                                        if (finalExistePolizaVigente.length() > 0 && cbAplicarPoliza.isChecked()) {
                                                            //Existe poliza vigente y checkbox aplicarpoliza esta checkeado
                                                            tipoAutorizacion = "9";
                                                        }

                                                        try {

                                                            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
                                                            ContentValues valores = new ContentValues();
                                                            valores.put("ID_CONTRATO", ultimoIdContratoCreado);
                                                            valores.put("ID_PRODUCTO", idsProductos[spProductoAlertDialog.getSelectedItemPosition()]);
                                                            valores.put("ID_USUARIOC", idUsuario);
                                                            valores.put("FOLIOPOLIZA", etFolioPoliza.getText().toString());
                                                            valores.put("TIPO", tipoAutorizacion);
                                                            valores.put("PIEZAS", etNumPiezasProductoAlertDialog.getText().toString());
                                                            valores.put("ENVIADOPAGINA", "0");
                                                            valores.put("CREATED_AT", fechaActual + time);
                                                            sqLiteDB2.insert("AUTORIZACIONESARMAZON", null, valores);
                                                            sqLiteDB2.close();

                                                            actualizarEstadoAtributoContrato("AUTORIZACION", "0");

                                                            llamadaSincronizacion();

                                                            tvAutorizacionArmazon.setVisibility(View.VISIBLE);
                                                            tvAutorizacionArmazon.setText("Solicitud de armazón pendiente.");

                                                            Toast.makeText(fragmento.getActivity(), "Solicitud de armazón generada correctamente", Toast.LENGTH_LONG).show();
                                                            dialog.dismiss();

                                                        } catch (SQLiteException e) {
                                                            global.escribirError(e, 272);
                                                            Log.i("ERRORBD", e.getMessage() + "");
                                                        }

                                                    }

                                                } else {
                                                    //Tipo producto es diferente a armazon

                                                    if (llaves.impresora_termica) {
                                                        //Se utilizara impresora termica
                                                        respuesta = impresoraBluetooth.findBluetoothDevice();
                                                    }

                                                    if (respuesta) {
                                                        //Se conecto correctamente a la impresora

                                                        AlertDialog.Builder alerta2 = new AlertDialog.Builder(fragmento.getActivity());
                                                        alerta2.setTitle("Alerta").setMessage(Html.fromHtml("<font color='#FFACA6'><b>Esta accion no se podra revertir. " +
                                                                        "¿Deseas continuar?</b></font>"))
                                                                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        //Debe ir vacio por que se esta obteniendo abajo
                                                                    }
                                                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog3, int which) {
                                                                        if (llaves.impresora_termica) {
                                                                            //Se utilizara impresora termica
                                                                            if (rol.equals("4")) {
                                                                                //Rol cobranza
                                                                                impresoraBluetooth.closedBluetoothPrinter();
                                                                            }
                                                                        }
                                                                        dialog3.cancel();
                                                                        dialog.dismiss();
                                                                    }
                                                                });

                                                        //Cuando se cierra la alerta por un toque fuera de ella
                                                        alerta2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                            @Override
                                                            public void onDismiss(DialogInterface dialogInterface) {
                                                                if (llaves.impresora_termica) {
                                                                    //Se utilizara impresora termica
                                                                    if (rol.equals("4")) {
                                                                        //Rol cobranza
                                                                        impresoraBluetooth.closedBluetoothPrinter();
                                                                    }
                                                                }
                                                            }
                                                        });

                                                        final AlertDialog dialog2 = alerta2.create();
                                                        dialog2.show();

                                                        dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                guardarProductoContratoProductoBD(promocionProducto, finalExistePolizaVigente, tipoProducto);
                                                                dialog2.dismiss();
                                                                dialog.dismiss();
                                                            }
                                                        });

                                                    }

                                                }

                                            }

                                        }

                                    }

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

    private boolean tieneAbonos(boolean todosLosAbonos) {

        try{

            sqLiteDB = conexion.getReadableDatabase();

            String SQL = "";

            if(todosLosAbonos) {
                SQL = "SELECT CASE WHEN ABONO THEN 1 ELSE 0 END" +
                        " FROM ABONOS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            }else {
                SQL = "SELECT CASE WHEN ABONO THEN 1 ELSE 0 END" +
                        " FROM ABONOS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' AND TIPOABONO != '7'";
            }

            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.moveToFirst()){
                if(datos.getInt(0) == 1) {
                    sqLiteDB.close();
                    datos.close();
                    return true;
                }
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 175);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }
        return false;
    }

    private void llenarSpinnersProductosBD() {

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID, NOMBRE, ID_TIPOPRODUCTO, COLOR FROM PRODUCTO WHERE CAST(PIEZAS as INT) > 10 AND ID_TIPOPRODUCTO > 1 ORDER BY ID_TIPOPRODUCTO, NOMBRE";

            if (rol.equals("4")) {
                //Cobranza
                SQL = "SELECT ID, NOMBRE, ID_TIPOPRODUCTO, COLOR FROM PRODUCTO WHERE CAST(PIEZAS as INT) > 10 ORDER BY ID_TIPOPRODUCTO, NOMBRE";
            }

            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay productos registrados");
            }

            idsProductos = new String[datos.getCount() + 1];
            nombresProductos = new String[datos.getCount() + 1];
            idsProductos[0] = "";
            nombresProductos[0] = "Seleccionar";

            if(datos.getCount()>0){
                datos.moveToFirst();
                for(int i=1; i <= datos.getCount(); i++){
                    idsProductos[i]= datos.getString(0);
                    nombresProductos[i] = datos.getString(1);
                    if (datos.getInt(2) == 1) {
                        //ARMAZON
                        nombresProductos[i] = datos.getString(1) + " | " + datos.getString(3);
                    }
                    datos.moveToNext();
                }
            }

            sqLiteDB.close();
            datos.close();

            spProductoAlertDialog.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, nombresProductos));

        }catch (SQLiteException e){
            global.escribirError(e, 176);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private boolean agregarPrecioYPiezasProducto() {

        boolean valor = false;
        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT PRECIO, PIEZAS, PRECIOP, ACTIVO FROM PRODUCTO" +
                    " WHERE ID='" + idsProductos[spProductoAlertDialog.getSelectedItemPosition()] + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No se pudo encontrar el producto");
            }

            if (datos.moveToFirst()) {
                if(datos.getInt(3) == 1) {
                    llPrecioPromocion.setVisibility(View.VISIBLE);
                    etPrecioProductoAlertDialog.setPaintFlags(etPrecioProductoAlertDialog.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    etPrecioProductoAlertDialog.setText(datos.getString(0));
                    etPiezasProductoAlertDialog.setText(datos.getString(1));
                    etPrecioProductoPromocionAlertDialog.setText(datos.getString(2));
                    valor = true;
                }else {
                    llPrecioPromocion.setVisibility(View.GONE);
                    etPrecioProductoAlertDialog.setPaintFlags(etPrecioProductoAlertDialog.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    etPrecioProductoAlertDialog.setText(datos.getString(0));
                    etPiezasProductoAlertDialog.setText(datos.getString(1));
                }
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 177);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return valor;

    }

    private void guardarProductoContratoProductoBD(boolean promocionProducto, String existePolizaVigente, String tipoProducto) {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        double totalProductoSeleccionado = 0;

        String tipomensaje = "0";
        if (tipoProducto.equals("1")) {
            //Tipo producto armazon
            tipomensaje = "4";
        }

        String mensajeFolioPoliza = "";
        if (existePolizaVigente.length() > 0 && cbAplicarPoliza.isChecked()) {
            //Existe poliza vigente y checkbox aplicarpoliza esta checkeado
            totalProductoSeleccionado = 90 * Double.parseDouble(etNumPiezasProductoAlertDialog.getText().toString());
            mensajeFolioPoliza = " con poliza: " + etFolioPoliza.getText().toString();
        }else {
            //No existe poliza vigente
            if (promocionProducto) {
                totalProductoSeleccionado = Double.parseDouble(etPrecioProductoPromocionAlertDialog.getText().toString()) * Double.parseDouble(etNumPiezasProductoAlertDialog.getText().toString());
            } else {
                totalProductoSeleccionado = Double.parseDouble(etPrecioProductoAlertDialog.getText().toString()) * Double.parseDouble(etNumPiezasProductoAlertDialog.getText().toString());
            }
        }

        String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("CONTRATOPRODUCTO", 5);

        try {

            sqLiteDB = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID", idAlfanumerico);
            valores.put("ID_CONTRATO", ultimoIdContratoCreado);
            valores.put("ID_PRODUCTO", idsProductos[spProductoAlertDialog.getSelectedItemPosition()]);
            valores.put("PIEZAS", etNumPiezasProductoAlertDialog.getText().toString());
            valores.put("TOTAL", String.valueOf(totalProductoSeleccionado));
            valores.put("ID_USUARIO", idUsuario);
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT", fechaActual + time);
            valores.put("UPDATED_AT", fechaActual + time);
            sqLiteDB.insert("CONTRATOPRODUCTO",null, valores);
            sqLiteDB.close();

            historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                    "Se agrego el producto con identificador: " + idsProductos[spProductoAlertDialog.getSelectedItemPosition()] +
                            " | " + nombresProductos[spProductoAlertDialog.getSelectedItemPosition()] +
                            " | cantidad de piezas: '" + etNumPiezasProductoAlertDialog.getText().toString() +
                            "'" + mensajeFolioPoliza, tipomensaje);

            Toast.makeText(fragmento.getActivity(), "Se agrego correctamente el producto", Toast.LENGTH_LONG).show();

            decrementarOAumentarProductoBD(idsProductos[spProductoAlertDialog.getSelectedItemPosition()], "-", etNumPiezasProductoAlertDialog.getText().toString());
            obtenerListaProductos();

            folioAlfanumerico = generarIdAlfanumerico.validarSiExisteFolioAlfanumericoEnAbonosContrato(ultimoIdContratoCreado); //Obtener folioAlfanumerico

            if (rol.equals("4")) {
                //Cobranza

                //Obtener totalanterior para mandarlo al ticket antes que se haga el calculo
                totalanterior = String.valueOf(obtenerTotalContrato());
                if(totalanterior.indexOf(".") > 0) {
                    //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
                    totalanterior = totalanterior.substring(0, totalanterior.indexOf("."));
                }

            }

            guardarAbonoContratoBD(
                    generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("ABONOS", 5),
                    String.valueOf(totalProductoSeleccionado), "7", "0", "0", "0", idAlfanumerico);

            if (rol.equals("4")) {
                //Cobranza
                llamadaHandlerRunnableImpresoraTermica();
            }

            productosPagados();
            calculoTotal();

        }catch (SQLiteException e) {
            global.escribirError(e, 178);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        llamadaSincronizacion();

    }

    private void decrementarOAumentarProductoBD(String idProducto, String operacion, String piezas) {

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE PRODUCTO SET PIEZAS = CAST(PIEZAS AS INTEGER) " + operacion + " CAST(" + piezas + " AS INTEGER)"
                    + " WHERE ID='" + idProducto + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 179);
            Log.i("ERRORBD", e.getMessage() + "");
        }
        
    }

    private void actualizarTotalProductoContratoBD() {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET TOTALPRODUCTO = IFNULL((SELECT SUM(CAST(TOTAL AS DECIMAL(5,2))) FROM CONTRATOPRODUCTO WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'), '0')"
                    + " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";

            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 180);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    public void mostrarAlertDialogAbono() {

        boolean respuesta = true;
        coordenadasAbono = "";

        if(llaves.impresora_termica) {
            //Se utilizara impresora termica

            if (rol.equals("4")) {
                //Rol cobranza
                respuesta = false;
                if (localizacion.tienePermisos() && impresoraBluetooth.findBluetoothDevice()) {
                    //Permisos para localizacion y tiene los permisos de bluetooth
                    boolean tieneEncendidoGPS = localizacion.actualizarUbicacion(localizacion);
                    if (tieneEncendidoGPS) {
                        //Gps esta encendio y tiene permisos
                        Handler handler = new Handler();
                        runnable3 = new Runnable() {
                            @Override
                            public void run() {
                                //Se trata de obtener la ubicacion actual
                                if (localizacion.getLatitud() != null) {
                                    //si latitud es diferente a vacio
                                    //Se pudo obtener ubicacion
                                    coordenadasAbono = localizacion.getLatitud() + "," + localizacion.getLongitud();
                                    handler.removeCallbacks(runnable3);
                                } else {
                                    handler.postDelayed(runnable3, 2000);
                                }
                            }
                        };
                        runnable3.run();
                        respuesta = true;
                    }
                }
            }

        }

        if(respuesta) {
            //Se conecto correctamente a la impresora

            if(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("6")) {
                //Estado del contrato cancelado
                Toast.makeText(fragmento.getActivity(), "El contrato se encuentra cancelado, ya no se puede abonar", Toast.LENGTH_LONG).show();
            }else {
                //Estado del contrato diferente a cancelado

                if (!obtenerEstadoAtributoContrato("SUBSCRIPCION").equals("")) {
                    Toast.makeText(fragmento.getActivity(), "El contrato ya se encuentra subscrito a una cuenta ya no se puede abonar", Toast.LENGTH_LONG).show();
                } else {

                    if (obtenerTotalContrato() <= 0) {
                        Toast.makeText(fragmento.getActivity(), "Ya se encuentra liquidado el contrato", Toast.LENGTH_LONG).show();
                    } else {

                        if (obtenerEstadoAtributoContrato("PAGO").equals("")) {
                            Toast.makeText(fragmento.getActivity(), "Debes agregar una forma de pago", Toast.LENGTH_LONG).show();
                        } else {

                            if (rol.equals("4") && !obtenerEstadoAtributoContrato("PAGO").equals("0") && obtenerEstadoAtributoContrato("DIAPAGO").length() == 0) {
                                //Rol cobranza, pago semanal, quincenal o mensual y no se seleccionado el diapago
                                Toast.makeText(fragmento.getActivity(), "Favor de agregar el dia de pago", Toast.LENGTH_LONG).show();
                            } else {

                                if (btnNuevoHistorial.getVisibility() == View.VISIBLE) {
                                    Toast.makeText(fragmento.getActivity(), "Favor de llenar los historiales clinicos necesarios", Toast.LENGTH_LONG).show();
                                } else {

                                    AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                                    inflater = (LayoutInflater) fragmento.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    searchView = inflater.inflate(R.layout.dialog_abono, null);

                                    etAbonoAlertDialog = searchView.findViewById(R.id.etAbonoAlertDialog);
                                    etAdelantarAbonoAlertDialog = searchView.findViewById(R.id.etAdelantarAbonoAlertDialog);
                                    cbAdelantarAbonoAlertDialog = searchView.findViewById(R.id.cbAdelantarAbonoAlertDialog);
                                    llAbonoAdelantadoAlertDialog = searchView.findViewById(R.id.llAbonoAdelantadoAlertDialog);
                                    spMetodoPago = searchView.findViewById(R.id.spMetodoPago);
                                    cardInputWidget = searchView.findViewById(R.id.cardInputWidget);
                                    llCardStripe = searchView.findViewById(R.id.llCardStripe);
                                    spPlanStripe = searchView.findViewById(R.id.spPlanStripe);
                                    llPlanStripe = searchView.findViewById(R.id.llPlanStripe);
                                    tvPrecioMensualStripe = searchView.findViewById(R.id.tvPrecioMensualStripe);
                                    tvCambioDolarPesos = searchView.findViewById(R.id.tvCambioDolarPesos);
                                    llCancelacionContrato = searchView.findViewById(R.id.llCancelacionContrato);
                                    cbCancelacionContratoAlertDialog = searchView.findViewById(R.id.cbCancelacionContratoAlertDialog);

                                    llenarSpinnerMetodoPago();
                                    obtenerPrecioDescuentoFormaPagoContado();

                                    if (rol.equals("4")) {
                                        //Rol cobranza
                                        llCancelacionContrato.setVisibility(View.VISIBLE); //Se muestra checkbox de cancelacion
                                        String mensajeCancelacion = "\nPara cancelación del contrato se debe de dar: $" + df0.format(Double.parseDouble(obtenerEstadoAtributoContrato("TOTALREAL")) * 0.30) + ".";
                                        tvCambioDolarPesos.setVisibility(View.VISIBLE); //Se habilita la etiqueta de cambio pesos a dolares
                                        String mensajeDolarPesos = "Se aceptan dolares a $" + llaves.preciodolar + " pesos c/u." + mensajeCancelacion;
                                        if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("12") && !garantia) {
                                            //Estado del contrato es igual a ENVIADO y no tiene garantia (Venta nueva)
                                            mensajeDolarPesos += "\nPara adelantos, en la entrega se debera agregar un solo abono.";
                                        }
                                        tvCambioDolarPesos.setText(mensajeDolarPesos); //Se habilita la etiqueta de cambio pesos a dolares
                                    }

                                    spMetodoPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                                            switch (position) {
                                                case 1: //Tarjeta
                                                    if (stripe_clavepublicable.length() > 0) {
                                                        //Tiene clave publicable

                                                        if (internet.verificarConexionInternet()) {
                                                            //Tiene internet

                                                            if (!rol.equals("4")) {
                                                                //Asistente/Opto
                                                                if (obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                                                                    //Forma de pago de contado
                                                                    if (obtenerEstadoPromocion()) {
                                                                        //Tiene promocion
                                                                        if (obtenerEstadoAtributoContrato("PROMOCIONTERMINADA").equals("0")) {
                                                                            //Promocion no terminada
                                                                            if (productosPagados()) {
                                                                                spMetodoPago.setSelection(0);
                                                                                Toast.makeText(fragmento.getActivity(), "No se puede abonar, necesitas de terminar la promoción", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        } else {
                                                                            //Promocion terminada
                                                                            etAbonoAlertDialog.setText(String.valueOf(obtenerTotalContrato()));
                                                                            etAbonoAlertDialog.setEnabled(false);
                                                                        }
                                                                    } else {
                                                                        //No tiene promocion
                                                                        if (productosPagados()) {
                                                                            if (obtenerPaqueteHistorialClinico() == 1 || obtenerPaqueteHistorialClinico() == 2 || obtenerPaqueteHistorialClinico() == 6) {
                                                                                //PAQUETE ES LECTURA, PROTECCION O DORADO2
                                                                                etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato()));
                                                                            } else {
                                                                                //PAQUETE DIFERENTE A LECTURA, PROTECCION O DORADO2
                                                                                etAbonoAlertDialog.setText(String.valueOf(obtenerTotalContrato() - Double.parseDouble(descuento)));
                                                                            }
                                                                            etAbonoAlertDialog.setEnabled(false);
                                                                            if (!tieneAbonos(false)) {
                                                                                llPlanStripe.setVisibility(View.VISIBLE);
                                                                            }
                                                                        } else {
                                                                            etAbonoAlertDialog.setText(String.valueOf(precioTotalProductosNoPagados));
                                                                            etAbonoAlertDialog.setEnabled(false);
                                                                        }
                                                                    }
                                                                } else {
                                                                    //Forma de pago Semanal, quincenal o mensual
                                                                    if (obtenerEstadoPromocion()) {
                                                                        if (obtenerEstadoAtributoContrato("PROMOCIONTERMINADA").equals("0")) {
                                                                            //Promocion no terminada
                                                                            if (productosPagados()) {
                                                                                spMetodoPago.setSelection(0);
                                                                                Toast.makeText(fragmento.getActivity(), "No se puede abonar, necesitas de terminar la promoción", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                //Cobranza
                                                                etAbonoAlertDialog.setText("");
                                                                etAbonoAlertDialog.setEnabled(true);
                                                                if (obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                                                                    if (!tieneAbonos(false) && !obtenerEstadoPromocion()) {
                                                                        llPlanStripe.setVisibility(View.VISIBLE);
                                                                    }
                                                                }
                                                            }
                                                            llCardStripe.setVisibility(View.VISIBLE);

                                                        } else {
                                                            //No tiene internet
                                                            spMetodoPago.setSelection(0);
                                                            Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
                                                        }

                                                    } else {
                                                        //No tiene clave publicable
                                                        spMetodoPago.setSelection(0);
                                                        Toast.makeText(fragmento.getActivity(), "Por el momento no se pueden realizar pagos con tarjeta", Toast.LENGTH_LONG).show();
                                                    }
                                                    break;
                                                default: //Efectivo o transferencia
                                                    llCardStripe.setVisibility(View.GONE);
                                                    if (!rol.equals("4")) {
                                                        if (productosPagados()) {
                                                            etAbonoAlertDialog.setText("");
                                                            etAbonoAlertDialog.setEnabled(true);
                                                        } else {
                                                            etAbonoAlertDialog.setText(String.valueOf(precioTotalProductosNoPagados));
                                                            etAbonoAlertDialog.setEnabled(false);
                                                        }
                                                    } else {
                                                        etAbonoAlertDialog.setText("");
                                                        etAbonoAlertDialog.setEnabled(true);
                                                        if (obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                                                            if (!obtenerEstadoPromocion()) {
                                                                //No tiene promocion
                                                                if (global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) > 0 && obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                                    //Tiene historiales con garatias y ya se habia dado el abono Contado S enganche (Pagado por completo)
                                                                    etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato()));
                                                                } else {
                                                                    //No tiene historiales con garatias
                                                                    if (obtenerPaqueteHistorialClinico() == 1 || obtenerPaqueteHistorialClinico() == 2 || obtenerPaqueteHistorialClinico() == 6
                                                                            || (obtenerTotalContrato() < 400 && !obtenerBanderaContadoEngancheOSinEnganche("4")
                                                                            && !obtenerBanderaContadoEngancheOSinEnganche("5"))) {
                                                                        //PAQUETE ES LECTURA, PROTECCION O DORADO2 O TOTALCONTRATO ES MENOR A 400 Y NO SE TIENE EL ABONO CONTADOENGACHE
                                                                        // NI TAMPOCO EL CONTADOSINENGANCHE (CUANDO ES UNA POLIZA DE SEGURO Y ES SEMANAL, QUICENAL O MENSUAL Y SE PASA A CONTADO)
                                                                        etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato()));
                                                                    } else {
                                                                        //PAQUETE DIFERENTE A LECTURA, PROTECCION O DORADO2 O TOTALCONTRATO ES MAYOR A 400
                                                                        // O SE TIENE EL ABONO CONTADOENGACHE O SE TIENE EL CONTADOSINENGANCHE
                                                                        etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato() - Double.parseDouble(descuento)));
                                                                    }
                                                                }
                                                                etAbonoAlertDialog.setEnabled(false);
                                                            }
                                                        }
                                                    }
                                                    if (obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                                                        spPlanStripe.setSelection(0);
                                                        llPlanStripe.setVisibility(View.GONE);
                                                    }
                                            }

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    spPlanStripe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                                            if (position != 0) {
                                                etAbonoAlertDialog.setText(String.valueOf(obtenerTotalContrato()));
                                                etAbonoAlertDialog.setEnabled(false);
                                                if (position == 1) {
                                                    if (rol.equals("4")) {
                                                        tvPrecioMensualStripe.setText("Pagos mensuales de: " + df00.format(obtenerTotalContrato() / 3) +
                                                                "\nNOTA: Una ves subscrito, el contrato cambiara a estado LIQUIDADO (Ya no se podra agregar/eliminar abonos)");
                                                    } else {
                                                        tvPrecioMensualStripe.setText("Pagos mensuales de: " + df00.format(obtenerTotalContrato() / 3) +
                                                                "\nNOTA: Una ves subscrito, el contrato cambiara a estado TERMINADO (Ya no se podra agregar/eliminar abonos, ni productos)");
                                                    }
                                                } else if (position == 2) {
                                                    if (rol.equals("4")) {
                                                        tvPrecioMensualStripe.setText("Pagos mensuales de: " + df00.format(obtenerTotalContrato() / 6) +
                                                                "\nNOTA: Una ves subscrito, el contrato cambiara a estado LIQUIDADO (Ya no se podra agregar/eliminar abonos)");
                                                    } else {
                                                        tvPrecioMensualStripe.setText("Pagos mensuales de: " + df00.format(obtenerTotalContrato() / 6) +
                                                                "\nNOTA: Una ves subscrito, el contrato cambiara a estado TERMINADO (Ya no se podra agregar/eliminar abonos, ni productos)");
                                                    }
                                                } else if (position == 3) {
                                                    if (rol.equals("4")) {
                                                        tvPrecioMensualStripe.setText("Pagos mensuales de: " + df00.format(obtenerTotalContrato() / 9) +
                                                                "\nNOTA: Una ves subscrito, el contrato cambiara a estado LIQUIDADO (Ya no se podra agregar/eliminar abonos)");
                                                    } else {
                                                        tvPrecioMensualStripe.setText("Pagos mensuales de: " + df00.format(obtenerTotalContrato() / 9) +
                                                                "\nNOTA: Una ves subscrito, el contrato cambiara a estado TERMINADO (Ya no se podra agregar/eliminar abonos, ni productos)");
                                                    }
                                                }
                                                tvPrecioMensualStripe.setVisibility(View.VISIBLE);
                                            } else {
                                                if (obtenerPaqueteHistorialClinico() == 1 || obtenerPaqueteHistorialClinico() == 2 || obtenerPaqueteHistorialClinico() == 6) {
                                                    //PAQUETE ES LECTURA, PROTECCION O DORADO2
                                                    etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato()));
                                                } else {
                                                    //PAQUETE DIFERENTE A LECTURA, PROTECCION O DORADO2
                                                    etAbonoAlertDialog.setText(String.valueOf(obtenerTotalContrato() - Double.parseDouble(descuento)));
                                                }
                                                etAbonoAlertDialog.setEnabled(false);
                                                tvPrecioMensualStripe.setVisibility(View.GONE);
                                            }

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    //MOSTRAR SECCION DE ADELANTAR ABONOS
                                    /*if (Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) >= 1 && rol.equals("4")) {
                                        if (Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) >= 1 && Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) < 12) {
                                            if (obtenerTotalContrato() <= pagoMinimo || Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) == 3) {
                                                llAbonoAdelantadoAlertDialog.setVisibility(View.GONE);
                                            } else {
                                                llAbonoAdelantadoAlertDialog.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }*/

                                    alerta.setTitle("ABONOS").setView(searchView)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    //Debe ir vacio por que se esta obteniendo abajo
                                                }
                                            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (llaves.impresora_termica) {
                                                        //Se utilizara impresora termica
                                                        if (rol.equals("4")) {
                                                            //Rol cobranza
                                                            impresoraBluetooth.closedBluetoothPrinter();
                                                        }
                                                    }
                                                    dialog.cancel();
                                                }
                                            });

                                    //Cuando se cierra la alerta por un toque fuera de ella
                                    alerta.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            if (llaves.impresora_termica) {
                                                //Se utilizara impresora termica
                                                if (rol.equals("4")) {
                                                    //Rol cobranza
                                                    impresoraBluetooth.closedBluetoothPrinter();
                                                }
                                            }
                                        }
                                    });

                                    final AlertDialog dialog = alerta.create();
                                    dialog.show();

                                    cbAdelantarAbonoAlertDialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                etAdelantarAbonoAlertDialog.setEnabled(true);
                                            } else {
                                                etAdelantarAbonoAlertDialog.setEnabled(false);
                                                etAdelantarAbonoAlertDialog.setText("");
                                            }
                                        }
                                    });

                                    cbCancelacionContratoAlertDialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                String resultado = global.obtenerUnResultadoQuery("SELECT ESTADOGARANTIA FROM GARANTIAS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' ORDER BY CREATED_AT DESC LIMIT 1");
                                                if (resultado.equals("0") || resultado.equals("1")) {
                                                    //Tiene garantia reportada o asiganada
                                                    cbCancelacionContratoAlertDialog.setChecked(false);
                                                    Toast.makeText(fragmento.getActivity(), "Deberá primero administración cancelar la garantía.", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    });

                                    if (rol.equals("4")) {
                                        //ROL COBRANZA

                                        if (obtenerTotalContrato() <= 0) {
                                            //Total contrato <= 0
                                            etAbonoAlertDialog.setText("");
                                        } else {
                                            //Total contrato > 0

                                            if (obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                                                //Forma de pago Contado
                                                if (global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) > 0 && obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                    //Tiene historiales con garatias y ya se habia dado el abono Contado S enganche (Pagado por completo)
                                                    etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato()));
                                                } else {
                                                    //No tiene historiales con garatias
                                                    if (obtenerEstadoPromocion()) {
                                                        //Tiene promocion
                                                        etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato()));
                                                    } else {
                                                        //No tiene promocion
                                                        if (obtenerPaqueteHistorialClinico() == 1 || obtenerPaqueteHistorialClinico() == 2 || obtenerPaqueteHistorialClinico() == 6
                                                                || (obtenerTotalContrato() < 400 && !obtenerBanderaContadoEngancheOSinEnganche("4")
                                                                && !obtenerBanderaContadoEngancheOSinEnganche("5"))) {
                                                            //PAQUETE ES LECTURA, PROTECCION O DORADO2 O TOTALCONTRATO ES MENOR A 400 Y NO SE TIENE EL ABONO CONTADOENGACHE
                                                            // NI TAMPOCO EL CONTADOSINENGANCHE (CUANDO ES UNA POLIZA DE SEGURO Y ES SEMANAL, QUICENAL O MENSUAL Y SE PASA A CONTADO)
                                                            etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato()));
                                                        } else {
                                                            //PAQUETE DIFERENTE A LECTURA, PROTECCION O DORADO2 O TOTALCONTRATO ES MAYOR A 400
                                                            // O SE TIENE EL ABONO CONTADOENGACHE O SE TIENE EL CONTADOSINENGANCHE
                                                            etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato() - Double.parseDouble(descuento)));
                                                        }
                                                    }
                                                }
                                                etAbonoAlertDialog.setEnabled(false);
                                            } else {
                                                //Forma de pago Semanal, Quincenal, Mensual
                                                if (obtenerTotalContrato() <= pagoMinimo) {
                                                    //Total contrato <= al pago minimo
                                                    if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("4")) {
                                                        //Tiene abono atrasado
                                                        etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato()));
                                                    } else {
                                                        //No tiene abono atrasado
                                                        etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato()));
                                                        etAbonoAlertDialog.setEnabled(false);
                                                    }

                                                } else {
                                                    //Total contrato > al pago minimo
                                                    if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {
                                                        //No se ha dado el abonoMinimoSemanal de entrega del producto
                                                        etAbonoAlertDialog.setText(abonoMinimoSemanal);
                                                    } else {
                                                        //Ya se dio el abonoMinimoSemanal de entrega del producto
                                                        etAbonoAlertDialog.setText("");
                                                    }
                                                }
                                            }

                                        }

                                    } else {
                                        //ROL ASISTENTE Y OPTOMETRISTA

                                        if (obtenerTotalContrato() <= 0) {
                                            etAbonoAlertDialog.setText("");
                                        } else {
                                            if (productosPagados()) {
                                                if (obtenerTotalContrato() < 100) {
                                                    etAbonoAlertDialog.setText(df0.format(obtenerTotalContrato()));
                                                } else {
                                                    etAbonoAlertDialog.setText("");
                                                }
                                            } else {
                                                etAbonoAlertDialog.setText(String.valueOf(precioTotalProductosNoPagados));
                                                etAbonoAlertDialog.setEnabled(false);
                                            }

                                        }

                                    }

                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Card card = cardInputWidget.getCard();

                                            if (spMetodoPago.getSelectedItemPosition() == 1 && card == null) {
                                                //En caso de elegir metodo pago tarjeta y el card esta vacio
                                                Toast.makeText(fragmento.getActivity(), "Favor de llenar los datos de la tarjeta", Toast.LENGTH_LONG).show();
                                            } else {
                                                //Otro metodo de pago

                                                if (etAbonoAlertDialog.getText().toString().length() == 0) {
                                                    //Campo abono es igual a vacio
                                                    Toast.makeText(fragmento.getActivity(), "Debe agregar un abono", Toast.LENGTH_LONG).show();
                                                } else {
                                                    //Campo abono diferente de vacio

                                                    folioAlfanumerico = "";

                                                    //Validacion para obtener el valor del abono como decimal y string
                                                    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                                                    String valor = etAbonoAlertDialog.getText().toString();
                                                    Number number = null;
                                                    try {
                                                        number = format.parse(valor);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    double abonoEntranteDouble = number.doubleValue();
                                                    String abonoEntranteString = String.valueOf(number.doubleValue());

                                                    folioAlfanumerico = generarIdAlfanumerico.validarSiExisteFolioAlfanumericoEnAbonosContrato(ultimoIdContratoCreado); //Obtener folioAlfanumerico
                                                    if (rol.equals("4")) {
                                                        //ROL COBRANZA

                                                        if (abonoEntranteDouble <= 0) {
                                                            Toast.makeText(fragmento.getActivity(), "Debe agregar un abono", Toast.LENGTH_LONG).show();
                                                        } else {

                                                            String resultado = global.obtenerUnResultadoQuery("SELECT ESTADOGARANTIA FROM GARANTIAS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' ORDER BY CREATED_AT DESC LIMIT 1");

                                                            if (cbCancelacionContratoAlertDialog.isChecked() && (resultado.equals("0") || resultado.equals("1"))) {
                                                                //Es abono de cancelacion y tiene garantia reportada o asignada
                                                                Toast.makeText(fragmento.getActivity(), "Deberá primero administración cancelar la garantía.", Toast.LENGTH_LONG).show();
                                                            }else {
                                                                //Es abono de cancelacion y no tiene garantia

                                                                if (cbCancelacionContratoAlertDialog.isChecked() && abonoEntranteDouble < (Double.parseDouble(obtenerEstadoAtributoContrato("TOTALREAL")) * 0.30)) {
                                                                    //Es abono de cancelacion y el abono que estan dando es menor al requerido para cancelar
                                                                    Toast.makeText(fragmento.getActivity(), "Para poder cancelar el contrato el abono debe ser mayor o igual a $" + df0.format(Double.parseDouble(obtenerEstadoAtributoContrato("TOTALREAL")) * 0.30), Toast.LENGTH_LONG).show();
                                                                }else {
                                                                    //El abono es de cancelacion o deferente de cancelacion

                                                                    imprimirTicket = true; //Bandera para saber si imprimir el ticket o no (Cambia a falso en caso de que sea un pago con tarjeta)
                                                                    banderaHandlerImpresora = true; //Bandera para ejecutar Handler (Cambia a falso cuando no se ingresa el abono por alguna validacion)
                                                                    imprimirTicketEntregaProducto = false; //Bandera para saber si imprimir el ticket de entrega de producto o no

                                                                    //Obtener totalanterior para mandarlo al ticket antes que se haga el calculo
                                                                    totalanterior = String.valueOf(obtenerTotalContrato());
                                                                    if (totalanterior.indexOf(".") > 0) {
                                                                        //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
                                                                        totalanterior = totalanterior.substring(0, totalanterior.indexOf("."));
                                                                    }

                                                                    if (cbCancelacionContratoAlertDialog.isChecked()) {
                                                                        //Abono de cancelacion

                                                                        banderaHandlerImpresora = false;

                                                                        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                                                                        alerta.setTitle("Alerta").setMessage(Html.fromHtml("<font color='#FFACA6'><b>El contrato pasará a cancelado, " +
                                                                                        "ya no podras acceder a el ¿Deseas continuar?</b></font>"))
                                                                                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog2, int which) {
                                                                                        banderaHandlerImpresora = true;
                                                                                        //Bloquear componentes dia pago
                                                                                        spDiaPago.setEnabled(false);
                                                                                        btnAplicarDiaPago.setEnabled(false);
                                                                                        //Ocultar texto de reportar garantia
                                                                                        llReportarGarantia.setVisibility(View.GONE);
                                                                                        //Ocultar texto de nota
                                                                                        llNotaContrato.setVisibility(View.GONE);
                                                                                        //Actualizar estatus a CANCELADO
                                                                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "6");
                                                                                        historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                                                                                "Se agrego el abono de cancelación: '" + df0.format(Double.parseDouble(abonoEntranteString)) + "'", "0");
                                                                                        validacionAbonoContrato(abonoEntranteString, "8", "0", "0");
                                                                                        llamadaSincronizacion();
                                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                                        obtenerEstadoContratoYMostrarTextView();
                                                                                        llamadaHandlerRunnableImpresoraTermica();
                                                                                        dialog2.dismiss();
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog2, int which) {
                                                                                        dialog2.cancel();
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                }).show();

                                                                    } else {
                                                                        //Abono en efectivo, tarjeta o transferencia

                                                                        if (abonoEntranteDouble > obtenerTotalContrato() || (abonoEntranteDouble < pagoMinimo && abonoEntranteDouble != obtenerTotalContrato()
                                                                                && obtenerTotalContrato() < pagoMinimo)) {
                                                                            banderaHandlerImpresora = false;
                                                                            Toast.makeText(fragmento.getActivity(), "Debes abonar $" + df0.format(obtenerTotalContrato()) + " para liquidar el contrato",
                                                                                    Toast.LENGTH_LONG).show();
                                                                        } else {

                                                                            if (obtenerTotalContrato() >= pagoMinimo) {
                                                                                //Total contrato >= pagominimo

                                                                                if (Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) >= 1
                                                                                        && Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) < 12) {
                                                                                    //ESTATUS_ESTADOCONTRATO >= 1 ENTREGADO

                                                                                    if (obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                                                                                        //Forma de pago contado (TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANOFACTURA Y EN PROCESO DE ENVIO)

                                                                                        if (global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) > 0
                                                                                                && obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                                                            //Tiene historiales con garatias  y ya se habia dado el abono Contado S enganche (Pagado por completo)
                                                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"));
                                                                                            validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                        } else {
                                                                                            //No tiene historiales con garatias
                                                                                            if (obtenerEstadoPromocion()) {
                                                                                                //Tiene promocion
                                                                                                validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                            } else {
                                                                                                //No tiene promocion

                                                                                                if (obtenerPaqueteHistorialClinico() == 1 || obtenerPaqueteHistorialClinico() == 2
                                                                                                        || obtenerPaqueteHistorialClinico() == 6
                                                                                                        || (obtenerTotalContrato() < 400 && !obtenerBanderaContadoEngancheOSinEnganche("4")
                                                                                                        && !obtenerBanderaContadoEngancheOSinEnganche("5"))) {
                                                                                                    //PAQUETE ES LECTURA, PROTECCION O DORADO2 O TOTALCONTRATO ES MENOR A 400 Y NO SE TIENE EL ABONO CONTADOENGACHE
                                                                                                    // NI TAMPOCO EL CONTADOSINENGANCHE (CUANDO ES UNA POLIZA DE SEGURO Y ES SEMANAL, QUICENAL O MENSUAL Y SE PASA A CONTADO)
                                                                                                    validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                } else {
                                                                                                    //PAQUETE DIFERENTE A LECTURA, PROTECCION O DORADO2 O TOTALCONTRATO ES MAYOR A 400
                                                                                                    // O SE TIENE EL ABONO CONTADOENGACHE O SE TIENE EL CONTADOSINENGANCHE
                                                                                                    restarOSumarEnganche("-", descuento);
                                                                                                    actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"));
                                                                                                    if (!obtenerBanderaContadoEngancheOSinEnganche("4")) {
                                                                                                        //No trae contadoenganche
                                                                                                        if (!obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                                                                            //No trae contadosinenganche
                                                                                                            validacionAbonoContrato(abonoEntranteString, "5", "0", "0");
                                                                                                        } else {
                                                                                                            //Si trae contadosinenganche
                                                                                                            validacionAbonoContrato(abonoEntranteString, "4", "0", "0");
                                                                                                        }
                                                                                                    } else {
                                                                                                        //Si trae contadoenganche
                                                                                                        if (!obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                                                                            //No trae contadosinenganche
                                                                                                            validacionAbonoContrato(abonoEntranteString, "5", "0", "0");
                                                                                                        } else {
                                                                                                            //Si trae contadosinenganche
                                                                                                            validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                        }

                                                                                                    }

                                                                                                }

                                                                                            }

                                                                                        }
                                                                                        llamadaSincronizacion();
                                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                                        dialog.dismiss();

                                                                                    } else {
                                                                                        //Forma de pago semanal, quincenal o mensual

                                                                                        if (abonoEntranteDouble >= pagoMinimo) {
                                                                                            //Abono es mayor o igual al pago minimo

                                                                                            if (spMetodoPago.getSelectedItemPosition() == 1) {
                                                                                                //Tarjeta

                                                                                                validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                llamadaSincronizacion();
                                                                                                mostrarOcultarLlPromocionYFormaPago();
                                                                                                dialog.dismiss();

                                                                                            } else {
                                                                                                //Efectivo o transferencia

                                                                                                if (abonoEntranteDouble == obtenerTotalContrato()) {
                                                                                                    //Abono == total del contrato

                                                                                                    if (cbAdelantarAbonoAlertDialog.isChecked()) {
                                                                                                        //Adelantar abonos
                                                                                                        banderaHandlerImpresora = false;
                                                                                                        Toast.makeText(fragmento.getActivity(), "No puedes adelantar abonos", Toast.LENGTH_LONG).show();
                                                                                                    } else {
                                                                                                        //Sin adelantar abonos

                                                                                                        if (!validacionEstadosContratoConfirmaciones()) {
                                                                                                            //Si estado del contrato es diferente a TERMINADO, APROBADO,
                                                                                                            //EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                                                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "5");
                                                                                                        }

                                                                                                        if (fechaActualDentroPeriodo()) {
                                                                                                            //Fecha actual esta dentro del periodo

                                                                                                            if (obtenerPrecioAbonoPeriodo() >= pagoMinimo) {
                                                                                                                //Ya cubrio el total del abonoperiodo
                                                                                                                if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("4")) {
                                                                                                                    //Tiene abonoatrasado
                                                                                                                    verificarAbonoLiquidado();
                                                                                                                    validacionAbonoContrato(abonoEntranteString, "6",
                                                                                                                            obtenerEstadoAtributoContrato("COSTOATRASO"), "0");
                                                                                                                    actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                } else {
                                                                                                                    //No tiene abonoatrasado
                                                                                                                    verificarAbonoLiquidado();
                                                                                                                    validacionAbonoContrato(abonoEntranteString, "6", "0", "0");
                                                                                                                }
                                                                                                            } else {
                                                                                                                //No se ha cubierto el total del abonoperiodo
                                                                                                                double costoatrasado = abonoEntranteDouble - (pagoMinimo - obtenerPrecioAbonoPeriodo());
                                                                                                                if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("4")) {
                                                                                                                    //Tiene abonoatrasado
                                                                                                                    validacionAbonoContrato(String.valueOf(pagoMinimo - obtenerPrecioAbonoPeriodo()),
                                                                                                                            "3", "0", "0");
                                                                                                                    if (costoatrasado > 0) {
                                                                                                                        verificarAbonoLiquidado();
                                                                                                                        validacionAbonoContrato(String.valueOf(costoatrasado), "6",
                                                                                                                                obtenerEstadoAtributoContrato("COSTOATRASO"), "0");
                                                                                                                        actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    //No tiene abonoatrasado
                                                                                                                    validacionAbonoContrato(String.valueOf(pagoMinimo - obtenerPrecioAbonoPeriodo()),
                                                                                                                            "3", "0", "0");
                                                                                                                    if (costoatrasado > 0) {
                                                                                                                        verificarAbonoLiquidado();
                                                                                                                        validacionAbonoContrato(String.valueOf(costoatrasado),
                                                                                                                                "6", "0", "0");
                                                                                                                    }
                                                                                                                }
                                                                                                            }

                                                                                                        } else {
                                                                                                            //Fecha actual no esta dentro del periodo
                                                                                                            if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("4")) {
                                                                                                                //Tiene abonoatrasado
                                                                                                                verificarAbonoLiquidado();
                                                                                                                validacionAbonoContrato(abonoEntranteString, "6",
                                                                                                                        obtenerEstadoAtributoContrato("COSTOATRASO"), "0");
                                                                                                                actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                            } else {
                                                                                                                //No tiene abonoatrasado
                                                                                                                verificarAbonoLiquidado();
                                                                                                                validacionAbonoContrato(abonoEntranteString, "6", "0", "0");
                                                                                                            }
                                                                                                        }
                                                                                                        llamadaSincronizacion();
                                                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                                                        dialog.dismiss();

                                                                                                    }

                                                                                                } else {
                                                                                                    //Abono != total del contrato

                                                                                                    if (fechaActualDentroPeriodo()) {
                                                                                                        //Fecha actual esta dentro del periodo

                                                                                                        if (obtenerPrecioAbonoPeriodo() >= pagoMinimo) {
                                                                                                            //Ya cubrio el total del abonoperiodo

                                                                                                            if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("4")) {
                                                                                                                //Tiene abonoatrasado

                                                                                                                if (cbAdelantarAbonoAlertDialog.isChecked()) {
                                                                                                                    //Adelantar abonos

                                                                                                                    if (etAdelantarAbonoAlertDialog.getText().toString().length() == 0) {
                                                                                                                        banderaHandlerImpresora = false;
                                                                                                                        Toast.makeText(fragmento.getActivity(), "Campo adelantar abono vacío",
                                                                                                                                Toast.LENGTH_LONG).show();
                                                                                                                    } else {

                                                                                                                        if (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) <= 0
                                                                                                                                || Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) > 3
                                                                                                                                || (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) +
                                                                                                                                Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR"))) > 3) {
                                                                                                                            banderaHandlerImpresora = false;
                                                                                                                            Toast.makeText(fragmento.getActivity(), "No puede ser menor a 0 / Adelantar mas de 3 veces",
                                                                                                                                    Toast.LENGTH_LONG).show();
                                                                                                                        } else {
                                                                                                                            if (abonoEntranteDouble >= (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) +
                                                                                                                                    (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo))) {
                                                                                                                                actualizarEstadoAtributoContrato("PAGOSADELANTAR",
                                                                                                                                        String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) +
                                                                                                                                                Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString())));
                                                                                                                                actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                                                                                                                validacionAbonoContrato(abonoEntranteString, "0",
                                                                                                                                        obtenerEstadoAtributoContrato("COSTOATRASO"),
                                                                                                                                        etAdelantarAbonoAlertDialog.getText().toString());
                                                                                                                                actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                                llamadaSincronizacion();
                                                                                                                                mostrarOcultarLlPromocionYFormaPago();
                                                                                                                                dialog.dismiss();
                                                                                                                            } else {
                                                                                                                                banderaHandlerImpresora = false;
                                                                                                                                Toast.makeText(fragmento.getActivity(), "Tienes que abonar: $" +
                                                                                                                                                (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) +
                                                                                                                                                        (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo)),
                                                                                                                                        Toast.LENGTH_LONG).show();
                                                                                                                            }
                                                                                                                        }

                                                                                                                    }

                                                                                                                } else {
                                                                                                                    //Sin adelantar abonos

                                                                                                                    double costoAtraso = Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) - abonoEntranteDouble;
                                                                                                                    if (costoAtraso <= 0) {
                                                                                                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                                                                                                        validacionAbonoContrato(abonoEntranteString, "0",
                                                                                                                                obtenerEstadoAtributoContrato("COSTOATRASO"), "0");
                                                                                                                        actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                    } else {
                                                                                                                        validacionAbonoContrato(abonoEntranteString, "0", abonoEntranteString, "0");
                                                                                                                        actualizarEstadoAtributoContrato("COSTOATRASO", String.valueOf(costoAtraso));
                                                                                                                    }
                                                                                                                    llamadaSincronizacion();
                                                                                                                    mostrarOcultarLlPromocionYFormaPago();
                                                                                                                    dialog.dismiss();
                                                                                                                }

                                                                                                            } else {
                                                                                                                //No tiene abonoatrasado

                                                                                                                if (cbAdelantarAbonoAlertDialog.isChecked()) {
                                                                                                                    //Adelantar abonos

                                                                                                                    if (etAdelantarAbonoAlertDialog.getText().toString().length() == 0) {
                                                                                                                        banderaHandlerImpresora = false;
                                                                                                                        Toast.makeText(fragmento.getActivity(), "Campo adelantar abono vacío", Toast.LENGTH_LONG).show();
                                                                                                                    } else {

                                                                                                                        if (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) <= 0
                                                                                                                                || Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) > 3
                                                                                                                                || (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) +
                                                                                                                                Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR"))) > 3) {
                                                                                                                            banderaHandlerImpresora = false;
                                                                                                                            Toast.makeText(fragmento.getActivity(), "No puede ser menor a 0 / Adelantar mas de 3 veces",
                                                                                                                                    Toast.LENGTH_LONG).show();
                                                                                                                        } else {

                                                                                                                            if (abonoEntranteDouble >= (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo)) {
                                                                                                                                actualizarEstadoAtributoContrato("PAGOSADELANTAR",
                                                                                                                                        String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) +
                                                                                                                                                Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString())));
                                                                                                                                validacionAbonoContrato(abonoEntranteString, "0", "0",
                                                                                                                                        etAdelantarAbonoAlertDialog.getText().toString());
                                                                                                                                llamadaSincronizacion();
                                                                                                                                mostrarOcultarLlPromocionYFormaPago();
                                                                                                                                dialog.dismiss();
                                                                                                                            } else {
                                                                                                                                banderaHandlerImpresora = false;
                                                                                                                                Toast.makeText(fragmento.getActivity(), "Tienes que abonar: $" +
                                                                                                                                                (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo),
                                                                                                                                        Toast.LENGTH_LONG).show();
                                                                                                                            }

                                                                                                                        }

                                                                                                                    }

                                                                                                                } else {
                                                                                                                    //Sin adelantar abonos
                                                                                                                    validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                                    llamadaSincronizacion();
                                                                                                                    mostrarOcultarLlPromocionYFormaPago();
                                                                                                                    dialog.dismiss();
                                                                                                                }

                                                                                                            }

                                                                                                        } else {
                                                                                                            //No se ha cubierto el total del abonoperiodo

                                                                                                            if (abonoEntranteDouble >= (pagoMinimo - obtenerPrecioAbonoPeriodo())) {
                                                                                                                //Si da un abonoperiodo >= al resto pago minimo

                                                                                                                if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("4")) {
                                                                                                                    //Tiene abonoatrasado

                                                                                                                    if (cbAdelantarAbonoAlertDialog.isChecked()) {
                                                                                                                        //Adelantar abonos

                                                                                                                        if (etAdelantarAbonoAlertDialog.getText().toString().length() == 0) {
                                                                                                                            banderaHandlerImpresora = false;
                                                                                                                            Toast.makeText(fragmento.getActivity(), "Campo adelantar abono vacío",
                                                                                                                                    Toast.LENGTH_LONG).show();
                                                                                                                        } else {

                                                                                                                            if (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) <= 0
                                                                                                                                    || Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) > 3
                                                                                                                                    || (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) +
                                                                                                                                    Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR"))) > 3) {
                                                                                                                                banderaHandlerImpresora = false;
                                                                                                                                Toast.makeText(fragmento.getActivity(), "No puede ser menor a 0 / Adelantar mas de 3 veces",
                                                                                                                                        Toast.LENGTH_LONG).show();
                                                                                                                            } else {

                                                                                                                                if (abonoEntranteDouble >= ((Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) +
                                                                                                                                        (pagoMinimo - obtenerPrecioAbonoPeriodo())) +
                                                                                                                                        (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo))) {

                                                                                                                                    actualizarEstadoAtributoContrato("PAGOSADELANTAR",
                                                                                                                                            String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) +
                                                                                                                                                    Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString())));
                                                                                                                                    double costoatrasado = abonoEntranteDouble - (pagoMinimo - obtenerPrecioAbonoPeriodo());
                                                                                                                                    if (costoatrasado > 0) {
                                                                                                                                        if (costoatrasado > Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO"))) {
                                                                                                                                            validacionAbonoContrato(String.valueOf(pagoMinimo - obtenerPrecioAbonoPeriodo()), "3", "0", "0");
                                                                                                                                            if (costoatrasado > 0) {
                                                                                                                                                validacionAbonoContrato(String.valueOf(costoatrasado), "0",
                                                                                                                                                        obtenerEstadoAtributoContrato("COSTOATRASO"),
                                                                                                                                                        etAdelantarAbonoAlertDialog.getText().toString());
                                                                                                                                                actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            validacionAbonoContrato(String.valueOf(pagoMinimo - obtenerPrecioAbonoPeriodo()),
                                                                                                                                                    "3", "0", "0");
                                                                                                                                            if (costoatrasado > 0) {
                                                                                                                                                validacionAbonoContrato(String.valueOf(costoatrasado), "0",
                                                                                                                                                        String.valueOf(costoatrasado),
                                                                                                                                                        etAdelantarAbonoAlertDialog.getText().toString());
                                                                                                                                                actualizarEstadoAtributoContrato("COSTOATRASO",
                                                                                                                                                        String.valueOf(Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) - costoatrasado));
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    if (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) <= 0) {
                                                                                                                                        actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                                                                                                                    }
                                                                                                                                    llamadaSincronizacion();
                                                                                                                                    mostrarOcultarLlPromocionYFormaPago();
                                                                                                                                    dialog.dismiss();
                                                                                                                                } else {
                                                                                                                                    banderaHandlerImpresora = false;
                                                                                                                                    Toast.makeText(fragmento.getActivity(), "Tienes que abonar: $" +
                                                                                                                                                    ((Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) +
                                                                                                                                                            (pagoMinimo - obtenerPrecioAbonoPeriodo())) +
                                                                                                                                                            (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo)),
                                                                                                                                            Toast.LENGTH_LONG).show();
                                                                                                                                }

                                                                                                                            }

                                                                                                                        }

                                                                                                                    } else {
                                                                                                                        //Sin adelantar abonos

                                                                                                                        double costoatrasado = abonoEntranteDouble - (pagoMinimo - obtenerPrecioAbonoPeriodo());
                                                                                                                        if (costoatrasado > 0) {
                                                                                                                            if (costoatrasado > Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO"))) {
                                                                                                                                validacionAbonoContrato(String.valueOf(pagoMinimo - obtenerPrecioAbonoPeriodo()),
                                                                                                                                        "3", "0", "0");
                                                                                                                                if (costoatrasado > 0) {
                                                                                                                                    validacionAbonoContrato(String.valueOf(costoatrasado), "0",
                                                                                                                                            obtenerEstadoAtributoContrato("COSTOATRASO"), "0");
                                                                                                                                    actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                validacionAbonoContrato(String.valueOf(pagoMinimo - obtenerPrecioAbonoPeriodo()),
                                                                                                                                        "3", "0", "0");
                                                                                                                                if (costoatrasado > 0) {
                                                                                                                                    validacionAbonoContrato(String.valueOf(costoatrasado), "0",
                                                                                                                                            String.valueOf(costoatrasado), "0");
                                                                                                                                    actualizarEstadoAtributoContrato("COSTOATRASO",
                                                                                                                                            String.valueOf(Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) - costoatrasado));
                                                                                                                                }
                                                                                                                            }

                                                                                                                        } else {
                                                                                                                            validacionAbonoContrato(abonoEntranteString, "3", "0", "0");
                                                                                                                        }
                                                                                                                        if (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) <= 0) {
                                                                                                                            actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                                                                                                        }
                                                                                                                        llamadaSincronizacion();
                                                                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                                                                        dialog.dismiss();

                                                                                                                    }

                                                                                                                } else {
                                                                                                                    //No tiene abonoatrasado

                                                                                                                    double abonoNormal = abonoEntranteDouble - (pagoMinimo - obtenerPrecioAbonoPeriodo());

                                                                                                                    if (cbAdelantarAbonoAlertDialog.isChecked()) {
                                                                                                                        //Adelantar abonos

                                                                                                                        if (etAdelantarAbonoAlertDialog.getText().toString().length() == 0) {
                                                                                                                            banderaHandlerImpresora = false;
                                                                                                                            Toast.makeText(fragmento.getActivity(), "Campo adelantar abono vacío", Toast.LENGTH_LONG).show();
                                                                                                                        } else {

                                                                                                                            if (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) <= 0
                                                                                                                                    || Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) > 3
                                                                                                                                    || (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) +
                                                                                                                                    Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR"))) > 3) {
                                                                                                                                banderaHandlerImpresora = false;
                                                                                                                                Toast.makeText(fragmento.getActivity(), "No puede ser menor a 0 / Adelantar mas de 3 veces", Toast.LENGTH_LONG).show();
                                                                                                                            } else {
                                                                                                                                if (abonoEntranteDouble >= ((pagoMinimo - obtenerPrecioAbonoPeriodo()) +
                                                                                                                                        (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo))) {
                                                                                                                                    actualizarEstadoAtributoContrato("PAGOSADELANTAR",
                                                                                                                                            String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) +
                                                                                                                                                    Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString())));
                                                                                                                                    validacionAbonoContrato(String.valueOf(pagoMinimo - obtenerPrecioAbonoPeriodo()), "3", "0", "0");
                                                                                                                                    if (abonoNormal > 0) {
                                                                                                                                        validacionAbonoContrato(String.valueOf(abonoNormal), "0", "0",
                                                                                                                                                etAdelantarAbonoAlertDialog.getText().toString());
                                                                                                                                    }
                                                                                                                                    llamadaSincronizacion();
                                                                                                                                    mostrarOcultarLlPromocionYFormaPago();
                                                                                                                                    dialog.dismiss();
                                                                                                                                } else {
                                                                                                                                    banderaHandlerImpresora = false;
                                                                                                                                    Toast.makeText(fragmento.getActivity(), "Tienes que abonar: $" +
                                                                                                                                            ((pagoMinimo - obtenerPrecioAbonoPeriodo()) +
                                                                                                                                                    (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo)), Toast.LENGTH_LONG).show();
                                                                                                                                }
                                                                                                                            }

                                                                                                                        }

                                                                                                                    } else {
                                                                                                                        //Sin adelantar abonos

                                                                                                                        if (abonoEntranteDouble == (pagoMinimo - obtenerPrecioAbonoPeriodo())) {
                                                                                                                            validacionAbonoContrato(String.valueOf(pagoMinimo - obtenerPrecioAbonoPeriodo()), "3", "0", "0");
                                                                                                                        } else {
                                                                                                                            validacionAbonoContrato(String.valueOf(pagoMinimo - obtenerPrecioAbonoPeriodo()), "3", "0", "0");
                                                                                                                            if (abonoNormal > 0) {
                                                                                                                                validacionAbonoContrato(String.valueOf(abonoNormal), "0", "0", "0");
                                                                                                                            }
                                                                                                                        }
                                                                                                                        llamadaSincronizacion();
                                                                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                                                                        dialog.dismiss();

                                                                                                                    }

                                                                                                                }

                                                                                                            } else {
                                                                                                                //Si da un abonoperiodo < al resto del pagominimo

                                                                                                                if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("4")) {
                                                                                                                    //Tiene abonoatrasado

                                                                                                                    if (cbAdelantarAbonoAlertDialog.isChecked()) {
                                                                                                                        //Adelantar abonos

                                                                                                                        if (etAdelantarAbonoAlertDialog.getText().toString().length() == 0) {
                                                                                                                            banderaHandlerImpresora = false;
                                                                                                                            Toast.makeText(fragmento.getActivity(), "Campo adelantar abono vacío", Toast.LENGTH_LONG).show();
                                                                                                                        } else {

                                                                                                                            if (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) <= 0
                                                                                                                                    || Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) > 3
                                                                                                                                    || (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) +
                                                                                                                                    Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR"))) > 3) {
                                                                                                                                banderaHandlerImpresora = false;
                                                                                                                                Toast.makeText(fragmento.getActivity(), "No puede ser menor a 0 / Adelantar mas de 3 veces", Toast.LENGTH_LONG).show();
                                                                                                                            } else {
                                                                                                                                banderaHandlerImpresora = false;
                                                                                                                                Toast.makeText(fragmento.getActivity(), "Tienes que abonar: $" +
                                                                                                                                        ((Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) +
                                                                                                                                                (pagoMinimo - obtenerPrecioAbonoPeriodo())) +
                                                                                                                                                (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo)), Toast.LENGTH_LONG).show();

                                                                                                                            }

                                                                                                                        }

                                                                                                                    } else {
                                                                                                                        //Sin adelantar abonos
                                                                                                                        if (abonoEntranteDouble < pagoMinimo) {
                                                                                                                            banderaHandlerImpresora = false;
                                                                                                                            Toast.makeText(fragmento.getActivity(), "Tienes que abonar minimo $" + pagoMinimo, Toast.LENGTH_LONG).show();
                                                                                                                        } else {
                                                                                                                            validacionAbonoContrato(abonoEntranteString, "3", "0", "0");
                                                                                                                            llamadaSincronizacion();
                                                                                                                            mostrarOcultarLlPromocionYFormaPago();
                                                                                                                            dialog.dismiss();
                                                                                                                        }

                                                                                                                    }

                                                                                                                } else {
                                                                                                                    //No tiene abonoatrasado

                                                                                                                    if (cbAdelantarAbonoAlertDialog.isChecked()) {
                                                                                                                        //Adelantar abonos

                                                                                                                        if (etAdelantarAbonoAlertDialog.getText().toString().length() == 0) {
                                                                                                                            banderaHandlerImpresora = false;
                                                                                                                            Toast.makeText(fragmento.getActivity(), "Campo adelantar abono vacío", Toast.LENGTH_LONG).show();
                                                                                                                        } else {

                                                                                                                            if (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) <= 0
                                                                                                                                    || Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) > 3
                                                                                                                                    || (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) +
                                                                                                                                    Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR"))) > 3) {
                                                                                                                                banderaHandlerImpresora = false;
                                                                                                                                Toast.makeText(fragmento.getActivity(), "No puede ser menor a 0 / Adelantar mas de 3 veces", Toast.LENGTH_LONG).show();
                                                                                                                            } else {
                                                                                                                                banderaHandlerImpresora = false;
                                                                                                                                Toast.makeText(fragmento.getActivity(), "Tienes que abonar: $" +
                                                                                                                                        ((pagoMinimo - obtenerPrecioAbonoPeriodo()) +
                                                                                                                                                (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo)), Toast.LENGTH_LONG).show();

                                                                                                                            }

                                                                                                                        }

                                                                                                                    } else {
                                                                                                                        //Sin adelantar abonos
                                                                                                                        banderaHandlerImpresora = false;
                                                                                                                        Toast.makeText(fragmento.getActivity(), "Tienes que abonar minimo $" + (pagoMinimo - obtenerPrecioAbonoPeriodo()), Toast.LENGTH_LONG).show();
                                                                                                                    }

                                                                                                                }

                                                                                                            }

                                                                                                        }

                                                                                                    } else {
                                                                                                        //Fecha actual no esta dentro del periodo

                                                                                                        if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("4")) {
                                                                                                            //Tiene abonoatrasado

                                                                                                            if (cbAdelantarAbonoAlertDialog.isChecked()) {
                                                                                                                //Adelantar abonos

                                                                                                                if (etAdelantarAbonoAlertDialog.getText().toString().length() == 0) {
                                                                                                                    banderaHandlerImpresora = false;
                                                                                                                    Toast.makeText(fragmento.getActivity(), "Campo adelantar abono vacío", Toast.LENGTH_LONG).show();
                                                                                                                } else {

                                                                                                                    if (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) <= 0
                                                                                                                            || Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) > 3
                                                                                                                            || (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) +
                                                                                                                            Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR"))) > 3) {
                                                                                                                        banderaHandlerImpresora = false;
                                                                                                                        Toast.makeText(fragmento.getActivity(), "No puede ser menor a 0 / Adelantar mas de 3 veces", Toast.LENGTH_LONG).show();
                                                                                                                    } else {
                                                                                                                        if (abonoEntranteDouble >= (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) +
                                                                                                                                (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo))) {
                                                                                                                            actualizarEstadoAtributoContrato("PAGOSADELANTAR",
                                                                                                                                    String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) +
                                                                                                                                            Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString())));
                                                                                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                                                                                                            validacionAbonoContrato(abonoEntranteString, "0",
                                                                                                                                    obtenerEstadoAtributoContrato("COSTOATRASO"),
                                                                                                                                    etAdelantarAbonoAlertDialog.getText().toString());
                                                                                                                            actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                            llamadaSincronizacion();
                                                                                                                            mostrarOcultarLlPromocionYFormaPago();
                                                                                                                            dialog.dismiss();
                                                                                                                        } else {
                                                                                                                            banderaHandlerImpresora = false;
                                                                                                                            Toast.makeText(fragmento.getActivity(), "Tienes que abonar: $" +
                                                                                                                                    (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) +
                                                                                                                                            (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo)), Toast.LENGTH_LONG).show();
                                                                                                                        }
                                                                                                                    }

                                                                                                                }

                                                                                                            } else {
                                                                                                                //Sin adelantar abonos

                                                                                                                double costoAtraso = Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) - abonoEntranteDouble;
                                                                                                                if (costoAtraso <= 0) {
                                                                                                                    actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                                                                                                    validacionAbonoContrato(abonoEntranteString, "0", obtenerEstadoAtributoContrato("COSTOATRASO"), "0");
                                                                                                                    actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                } else {
                                                                                                                    validacionAbonoContrato(abonoEntranteString, "0", abonoEntranteString, "0");
                                                                                                                    actualizarEstadoAtributoContrato("COSTOATRASO", String.valueOf(costoAtraso));
                                                                                                                }
                                                                                                                llamadaSincronizacion();
                                                                                                                mostrarOcultarLlPromocionYFormaPago();
                                                                                                                dialog.dismiss();
                                                                                                            }

                                                                                                        } else {
                                                                                                            //No tiene abonoatrasado

                                                                                                            if (cbAdelantarAbonoAlertDialog.isChecked()) {
                                                                                                                //Adelantar abonos

                                                                                                                if (etAdelantarAbonoAlertDialog.getText().toString().length() == 0) {
                                                                                                                    banderaHandlerImpresora = false;
                                                                                                                    Toast.makeText(fragmento.getActivity(), "Campo adelantar abono vacío", Toast.LENGTH_LONG).show();
                                                                                                                } else {

                                                                                                                    if (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) <= 0
                                                                                                                            || Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) > 3
                                                                                                                            || (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) +
                                                                                                                            Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR"))) > 3) {
                                                                                                                        banderaHandlerImpresora = false;
                                                                                                                        Toast.makeText(fragmento.getActivity(), "No puede ser menor a 0 / Adelantar mas de 3 veces", Toast.LENGTH_LONG).show();
                                                                                                                    } else {

                                                                                                                        if (abonoEntranteDouble >= (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo)) {
                                                                                                                            actualizarEstadoAtributoContrato("PAGOSADELANTAR",
                                                                                                                                    String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) +
                                                                                                                                            Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString())));
                                                                                                                            validacionAbonoContrato(abonoEntranteString, "0", "0",
                                                                                                                                    etAdelantarAbonoAlertDialog.getText().toString());
                                                                                                                            llamadaSincronizacion();
                                                                                                                            mostrarOcultarLlPromocionYFormaPago();
                                                                                                                            dialog.dismiss();
                                                                                                                        } else {
                                                                                                                            banderaHandlerImpresora = false;
                                                                                                                            Toast.makeText(fragmento.getActivity(), "Tienes que abonar: $" +
                                                                                                                                    (Integer.parseInt(etAdelantarAbonoAlertDialog.getText().toString()) * pagoMinimo), Toast.LENGTH_LONG).show();
                                                                                                                        }

                                                                                                                    }

                                                                                                                }

                                                                                                            } else {
                                                                                                                //Sin adelantar abonos
                                                                                                                validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                                llamadaSincronizacion();
                                                                                                                mostrarOcultarLlPromocionYFormaPago();
                                                                                                                dialog.dismiss();
                                                                                                            }

                                                                                                        }

                                                                                                    }

                                                                                                }

                                                                                            }

                                                                                        } else {
                                                                                            //Abono es menor al pago minimo
                                                                                            banderaHandlerImpresora = false;
                                                                                            Toast.makeText(fragmento.getActivity(), "El abono debe ser mayor o igual a $" + pagoMinimo, Toast.LENGTH_LONG).show();
                                                                                        }

                                                                                    }

                                                                                } else {
                                                                                    //ESTATUS_ESTADOCONTRATO = 12 ENVIADO

                                                                                    if (!obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                                                                                        //Forma de pago semanal, quincenal o mensual

                                                                                        if (!tieneAbonos(false) && global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) == 0
                                                                                                && (abonoEntranteDouble == obtenerTotalContrato() || abonoEntranteDouble == (obtenerTotalContrato() - 300))) {
                                                                                            //Podra cambiar la forma de pago de contado (No tiene abonos diferentes a tipoproducto,
                                                                                            //no tiene historiales con garantia y (esta dando el total del contrato o el total del contrato menos los 300)

                                                                                            banderaHandlerImpresora = false;

                                                                                            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                                                                                            alerta.setTitle("Confirmación").setMessage("¿Se detecto que estas dando un aproximado a una forma de pago de contado," +
                                                                                                            " deseas cambiarla para poder liquidar el contrato?")
                                                                                                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                                                                        @Override
                                                                                                        public void onClick(DialogInterface dialog2, int which) {
                                                                                                            dialog2.dismiss();
                                                                                                            dialog.dismiss();
                                                                                                            if (llSecundario.getVisibility() == View.VISIBLE) {
                                                                                                                mostrarOcultarListas = !mostrarOcultarListas;
                                                                                                                btnMostrarOcultar.setText("Mostrar mas");
                                                                                                                llSecundario.setVisibility(View.GONE);
                                                                                                                svScrollPrincipal.setVisibility(View.VISIBLE);
                                                                                                            }
                                                                                                            Toast.makeText(fragmento.getActivity(), "Favor de cambiar la forma de pago deseada", Toast.LENGTH_LONG).show();
                                                                                                        }
                                                                                                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                                                                        @Override
                                                                                                        public void onClick(DialogInterface dialog2, int which) {

                                                                                                            banderaHandlerImpresora = true;

                                                                                                            if (abonoEntranteDouble == obtenerTotalContrato()) {
                                                                                                                if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {
                                                                                                                    actualizarBanderaEntregaProductoYFechaEntrega(true);
                                                                                                                    imprimirTicketEntregaProducto = true;
                                                                                                                }
                                                                                                                verificarAbonoLiquidado();
                                                                                                                actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "5");
                                                                                                                validacionAbonoContrato(abonoEntranteString, "6", "0", "0");
                                                                                                                llamadaSincronizacion();
                                                                                                                mostrarOcultarLlPromocionYFormaPago();
                                                                                                                dialog.dismiss();
                                                                                                            } else {
                                                                                                                if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {
                                                                                                                    if (abonoEntranteDouble < Integer.parseInt(abonoMinimoSemanal)) {
                                                                                                                        banderaHandlerImpresora = false;
                                                                                                                        Toast.makeText(fragmento.getActivity(), "El abono debe ser mayor o igual a $" + abonoMinimoSemanal, Toast.LENGTH_LONG).show();
                                                                                                                    } else {
                                                                                                                        actualizarBanderaEntregaProductoYFechaEntrega(true);
                                                                                                                        imprimirTicketEntregaProducto = true;
                                                                                                                        validacionAbonoContrato(abonoEntranteString, "2", "0", "0");
                                                                                                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                                                                                                        llamadaSincronizacion();
                                                                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                                                                        dialog.dismiss();
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    if (obtenerEstadoAtributoContrato("FECHACOBROINI").length() > 0
                                                                                                                            && obtenerEstadoAtributoContrato("FECHACOBROFIN").length() > 0) {
                                                                                                                        //fechacobroini y fechacobrofin son diferentes de vacio
                                                                                                                        if (abonoEntranteDouble >= pagoMinimo) {
                                                                                                                            //Abono es mayor o igual al pago minimo
                                                                                                                            validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                                            //Actualizamos costo atraso en 0 por que se volvera a entregar
                                                                                                                            actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                            llamadaSincronizacion();
                                                                                                                            mostrarOcultarLlPromocionYFormaPago();
                                                                                                                            dialog.dismiss();
                                                                                                                        } else {
                                                                                                                            //Abono es menor al pago minimo
                                                                                                                            banderaHandlerImpresora = false;
                                                                                                                            Toast.makeText(fragmento.getActivity(), "El abono debe ser mayor o igual a $" + pagoMinimo, Toast.LENGTH_LONG).show();
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                                        //Actualizamos costo atraso en 0 por que se volvera a entregar
                                                                                                                        actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                                        llamadaSincronizacion();
                                                                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                                                                        dialog.dismiss();
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            obtenerEstadoContratoYMostrarTextView();
                                                                                                            llamadaHandlerRunnableImpresoraTermica();
                                                                                                            dialog2.cancel();
                                                                                                        }
                                                                                                    }).show();

                                                                                        } else {
                                                                                            //No podra cambiar la forma de pago de contado

                                                                                            if (abonoEntranteDouble == obtenerTotalContrato()) {
                                                                                                if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {
                                                                                                    actualizarBanderaEntregaProductoYFechaEntrega(true);
                                                                                                    imprimirTicketEntregaProducto = true;
                                                                                                }
                                                                                                verificarAbonoLiquidado();
                                                                                                actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "5");
                                                                                                validacionAbonoContrato(abonoEntranteString, "6", "0", "0");
                                                                                                llamadaSincronizacion();
                                                                                                mostrarOcultarLlPromocionYFormaPago();
                                                                                                dialog.dismiss();
                                                                                            } else {
                                                                                                if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {
                                                                                                    if (abonoEntranteDouble < Integer.parseInt(abonoMinimoSemanal)) {
                                                                                                        banderaHandlerImpresora = false;
                                                                                                        Toast.makeText(fragmento.getActivity(), "El abono debe ser mayor o igual a $" + abonoMinimoSemanal, Toast.LENGTH_LONG).show();
                                                                                                    } else {
                                                                                                        actualizarBanderaEntregaProductoYFechaEntrega(true);
                                                                                                        imprimirTicketEntregaProducto = true;
                                                                                                        validacionAbonoContrato(abonoEntranteString, "2", "0", "0");
                                                                                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                                                                                        llamadaSincronizacion();
                                                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                                                        dialog.dismiss();
                                                                                                    }
                                                                                                } else {
                                                                                                    if (obtenerEstadoAtributoContrato("FECHACOBROINI").length() > 0 && obtenerEstadoAtributoContrato("FECHACOBROFIN").length() > 0) {
                                                                                                        //fechacobroini y fechacobrofin son diferentes de vacio
                                                                                                        if (abonoEntranteDouble >= pagoMinimo) {
                                                                                                            //Abono es mayor o igual al pago minimo
                                                                                                            validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                            //Actualizamos costo atraso en 0 por que se volvera a entregar
                                                                                                            actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                            llamadaSincronizacion();
                                                                                                            mostrarOcultarLlPromocionYFormaPago();
                                                                                                            dialog.dismiss();
                                                                                                        } else {
                                                                                                            //Abono es menor al pago minimo
                                                                                                            banderaHandlerImpresora = false;
                                                                                                            Toast.makeText(fragmento.getActivity(), "El abono debe ser mayor o igual a $" + pagoMinimo, Toast.LENGTH_LONG).show();
                                                                                                        }
                                                                                                    } else {
                                                                                                        validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                        //Actualizamos costo atraso en 0 por que se volvera a entregar
                                                                                                        actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                                        llamadaSincronizacion();
                                                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                                                        dialog.dismiss();
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                        }

                                                                                    } else {
                                                                                        //Forma de pago contado

                                                                                        if (global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) > 0 && obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                                                            //Tiene historiales con garatias  y ya se habia dado el abono Contado S enganche (Pagado por completo)
                                                                                            verificarAbonoLiquidado();
                                                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "5");
                                                                                            validacionAbonoContrato(abonoEntranteString, "6", "0", "0");
                                                                                        } else {
                                                                                            //No tiene historiales con garatias
                                                                                            if (obtenerEstadoPromocion()) {
                                                                                                //Tiene promocion
                                                                                                validacionAbonoContrato(abonoEntranteString, "6", "0", "0");
                                                                                            } else {
                                                                                                //No tiene promocion

                                                                                                if (obtenerPaqueteHistorialClinico() == 1 || obtenerPaqueteHistorialClinico() == 2
                                                                                                        || obtenerPaqueteHistorialClinico() == 6
                                                                                                        || (obtenerTotalContrato() < 400 && !obtenerBanderaContadoEngancheOSinEnganche("4")
                                                                                                        && !obtenerBanderaContadoEngancheOSinEnganche("5"))) {
                                                                                                    //PAQUETE ES LECTURA, PROTECCION O DORADO2 O TOTALCONTRATO ES MENOR A 400 Y NO SE TIENE EL ABONO CONTADOENGACHE
                                                                                                    //NI TAMPOCO EL CONTADOSINENGANCHE (CUANDO ES UNA POLIZA DE SEGURO Y ES SEMANAL, QUICENAL O MENSUAL Y SE PASA A CONTADO)
                                                                                                    validacionAbonoContrato(abonoEntranteString, "6", "0", "0");
                                                                                                } else {
                                                                                                    //PAQUETE DIFERENTE A LECTURA, PROTECCION O DORADO2 O TOTALCONTRATO ES MAYOR A 400
                                                                                                    //O SE TIENE EL ABONO CONTADOENGACHE O SE TIENE EL CONTADOSINENGANCHE
                                                                                                    restarOSumarEnganche("-", descuento);
                                                                                                    actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "5");
                                                                                                    actualizarBanderaEntregaProductoYFechaEntrega(true);
                                                                                                    imprimirTicketEntregaProducto = true;
                                                                                                    if (!obtenerBanderaContadoEngancheOSinEnganche("4")) {
                                                                                                        //No trae contadoenganche
                                                                                                        if (!obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                                                                            //No trae contadosinenganche
                                                                                                            validacionAbonoContrato(abonoEntranteString, "5", "0", "0");
                                                                                                        } else {
                                                                                                            //Si trae contadosinenganche
                                                                                                            validacionAbonoContrato(abonoEntranteString, "4", "0", "0");
                                                                                                        }
                                                                                                    } else {
                                                                                                        //Si trae contadoenganche
                                                                                                        if (!obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                                                                            //No trae contadosinenganche
                                                                                                            validacionAbonoContrato(abonoEntranteString, "5", "0", "0");
                                                                                                        } else {
                                                                                                            //Si trae contadosinenganche
                                                                                                            validacionAbonoContrato(abonoEntranteString, "6", "0", "0");
                                                                                                        }

                                                                                                    }

                                                                                                }

                                                                                            }

                                                                                        }
                                                                                        llamadaSincronizacion();
                                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                                        dialog.dismiss();

                                                                                    }

                                                                                }

                                                                            } else {
                                                                                //Total contrato < pagominimo

                                                                                if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("4")) {
                                                                                    //Tiene abonoatrasado
                                                                                    double costoatrasado = Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) - abonoEntranteDouble;
                                                                                    if (costoatrasado > 0) {
                                                                                        validacionAbonoContrato(abonoEntranteString, "0", abonoEntranteString, "0");
                                                                                        actualizarEstadoAtributoContrato("COSTOATRASO",
                                                                                                String.valueOf(Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) - abonoEntranteDouble));
                                                                                    } else {
                                                                                        if (abonoEntranteDouble == obtenerTotalContrato()) {
                                                                                            verificarAbonoLiquidado();
                                                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "5");
                                                                                            validacionAbonoContrato(abonoEntranteString, "6", obtenerEstadoAtributoContrato("COSTOATRASO"), "0");
                                                                                        } else {
                                                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                                                                            validacionAbonoContrato(abonoEntranteString, "0", obtenerEstadoAtributoContrato("COSTOATRASO"), "0");
                                                                                        }
                                                                                        actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                    }
                                                                                } else {
                                                                                    //No tiene abonoatrasado
                                                                                    verificarAbonoLiquidado();
                                                                                    if (!validacionEstadosContratoConfirmaciones()) {
                                                                                        //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                                                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "5");
                                                                                    }
                                                                                    validacionAbonoContrato(abonoEntranteString, "6", obtenerEstadoAtributoContrato("COSTOATRASO"), "0");
                                                                                    actualizarEstadoAtributoContrato("COSTOATRASO", "0");
                                                                                }
                                                                                llamadaSincronizacion();
                                                                                mostrarOcultarLlPromocionYFormaPago();
                                                                                dialog.dismiss();
                                                                            }

                                                                            obtenerEstadoContratoYMostrarTextView();
                                                                            llamadaHandlerRunnableImpresoraTermica();

                                                                        }

                                                                    }

                                                                }

                                                            }

                                                        }

                                                    } else {
                                                        //ROL ASISTENTE Y OPTOMETRISTA

                                                        if (abonoEntranteDouble <= 0) {
                                                            Toast.makeText(fragmento.getActivity(), "Debe agregar un abono", Toast.LENGTH_LONG).show();
                                                        } else {

                                                            if (abonoEntranteDouble > obtenerTotalContrato()) {
                                                                Toast.makeText(fragmento.getActivity(), "Debes abonar $" + df0.format(obtenerTotalContrato()) + " para liquidar el contrato", Toast.LENGTH_LONG).show();
                                                            } else {

                                                                if (obtenerTotalContrato() >= 200) {
                                                                    //Total contrato >= 200

                                                                    if (productosPagados()) {
                                                                        //Ya se pagaron los productos

                                                                        if (obtenerPaqueteHistorialClinico() == 1 || obtenerPaqueteHistorialClinico() == 2 || obtenerPaqueteHistorialClinico() == 6) {
                                                                            //PAQUETE ES LECTURA, PROTECCION O DORADO2

                                                                            if (abonoEntranteDouble >= 100) {
                                                                                validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                llamadaSincronizacion();
                                                                                mostrarOcultarLlPromocionYFormaPago();
                                                                                dialog.dismiss();
                                                                            } else {
                                                                                Toast.makeText(fragmento.getActivity(), "El abono debe ser mayor o igual a 100.0", Toast.LENGTH_LONG).show();
                                                                            }

                                                                        } else {
                                                                            //PAQUETE DIFERENTE A LECTURA, PROTECCION O DORADO2

                                                                            if (!obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                                                                                //Forma de pago semanal, quincenal o mensual

                                                                                if (obtenerEstadoPromocion()) {
                                                                                    //Tiene promocion

                                                                                    if (obtenerEstadoAtributoContrato("PROMOCIONTERMINADA").equals("0")) {
                                                                                        Toast.makeText(fragmento.getActivity(), "No se puede abonar, necesitas de terminar la promoción", Toast.LENGTH_LONG).show();
                                                                                    } else {

                                                                                        if (abonoEntranteDouble >= 100) {
                                                                                            validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                            llamadaSincronizacion();
                                                                                            mostrarOcultarLlPromocionYFormaPago();
                                                                                            dialog.dismiss();
                                                                                        } else {
                                                                                            Toast.makeText(fragmento.getActivity(), "El abono debe ser mayor o igual a 100.0", Toast.LENGTH_LONG).show();
                                                                                        }

                                                                                    }

                                                                                } else {
                                                                                    //No tiene promocion

                                                                                    if (abonoEntranteDouble == obtenerTotalContrato() || abonoEntranteDouble > (obtenerTotalContrato() - 100)) {
                                                                                        if (!obtenerBanderaEnganche()) {
                                                                                            Toast.makeText(fragmento.getActivity(), "El abono debe ser menor o igual a " +
                                                                                                    df0.format(obtenerTotalContrato() - 100), Toast.LENGTH_LONG).show();
                                                                                        } else {
                                                                                            validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                            llamadaSincronizacion();
                                                                                            mostrarOcultarLlPromocionYFormaPago();
                                                                                            dialog.dismiss();
                                                                                        }
                                                                                    } else {

                                                                                        if (abonoEntranteDouble >= 100) {

                                                                                            if (!obtenerBanderaEnganche()) {
                                                                                                actualizarEstadoAtributoContrato("ENGANCHE", "1");
                                                                                                restarOSumarEnganche("-", "100");
                                                                                                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                                                                                        "Se agrego el enganche", "0");
                                                                                                validacionAbonoContrato(abonoEntranteString, "1", "0", "0");
                                                                                            } else {
                                                                                                validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                            }
                                                                                            llamadaSincronizacion();
                                                                                            mostrarOcultarLlPromocionYFormaPago();
                                                                                            dialog.dismiss();

                                                                                        } else {
                                                                                            Toast.makeText(fragmento.getActivity(), "El abono debe ser mayor o igual a 100.0", Toast.LENGTH_LONG).show();
                                                                                        }

                                                                                    }

                                                                                }

                                                                            } else {
                                                                                //Forma de pago de contado

                                                                                if (obtenerEstadoPromocion()) {
                                                                                    //Tiene promocion

                                                                                    if (obtenerEstadoAtributoContrato("PROMOCIONTERMINADA").equals("0")) {
                                                                                        Toast.makeText(fragmento.getActivity(), "No se puede abonar, necesitas de terminar la promoción", Toast.LENGTH_LONG).show();
                                                                                    } else {

                                                                                        if (abonoEntranteDouble >= 100) {
                                                                                            validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                            llamadaSincronizacion();
                                                                                            mostrarOcultarLlPromocionYFormaPago();
                                                                                            dialog.dismiss();
                                                                                        } else {
                                                                                            Toast.makeText(fragmento.getActivity(), "El abono debe ser mayor o igual a 100.0", Toast.LENGTH_LONG).show();
                                                                                        }

                                                                                    }

                                                                                } else {
                                                                                    //No tiene promocion

                                                                                    if (abonoEntranteDouble <= Double.parseDouble(df0.format((obtenerTotalContrato() - Double.parseDouble(descuento))))
                                                                                            || llPlanStripe.getVisibility() == View.VISIBLE) {

                                                                                        if (abonoEntranteDouble == Double.parseDouble(df0.format((obtenerTotalContrato() - Double.parseDouble(descuento))))
                                                                                                || llPlanStripe.getVisibility() == View.VISIBLE) {
                                                                                            //Dan completo de contado

                                                                                            restarOSumarEnganche("-", descuento);
                                                                                            if (!obtenerBanderaContadoEngancheOSinEnganche("4")) {
                                                                                                //No trae contadoenganche
                                                                                                if (!obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                                                                    //No trae contadosinenganche
                                                                                                    validacionAbonoContrato(abonoEntranteString, "5", "0", "0");
                                                                                                } else {
                                                                                                    //Si trae contadosinenganche
                                                                                                    validacionAbonoContrato(abonoEntranteString, "4", "0", "0");
                                                                                                }
                                                                                            } else {
                                                                                                //Si trae contadoenganche
                                                                                                validacionAbonoContrato(abonoEntranteString, "5", "0", "0");
                                                                                            }

                                                                                            llamadaSincronizacion();
                                                                                            mostrarOcultarLlPromocionYFormaPago();
                                                                                            dialog.dismiss();

                                                                                        } else {
                                                                                            //No dan completo de contado

                                                                                            if (abonoEntranteDouble >= 100) {

                                                                                                if (!obtenerBanderaContadoEngancheOSinEnganche("4")) {
                                                                                                    //No tiene contadoenganche
                                                                                                    if (!obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                                                                        //No trae contadosinenganche
                                                                                                        actualizarEstadoAtributoContrato("ENGANCHE", "1");
                                                                                                        restarOSumarEnganche("-", "100");
                                                                                                        historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                                                                                                "Se agrego el enganche", "0");
                                                                                                        validacionAbonoContrato(abonoEntranteString, "4", "0", "0");
                                                                                                    } else {
                                                                                                        //Si trae contadosinenganche
                                                                                                        validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                    }

                                                                                                } else {
                                                                                                    //Si trae contadoenganche
                                                                                                    validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                                                }

                                                                                                llamadaSincronizacion();
                                                                                                mostrarOcultarLlPromocionYFormaPago();
                                                                                                dialog.dismiss();
                                                                                            } else {
                                                                                                Toast.makeText(fragmento.getActivity(), "El abono debe ser mayor o igual a 100.0", Toast.LENGTH_LONG).show();
                                                                                            }

                                                                                        }

                                                                                    } else {
                                                                                        Toast.makeText(fragmento.getActivity(), "El abono debe ser menor o igual a " +
                                                                                                df0.format(obtenerTotalContrato() - Double.parseDouble(descuento)), Toast.LENGTH_LONG).show();
                                                                                    }

                                                                                }

                                                                            }

                                                                        }

                                                                    } else {
                                                                        //No se han pagado los productos
                                                                        validacionAbonoContrato(abonoEntranteString, "7", "0", "0");
                                                                        llamadaSincronizacion();
                                                                        mostrarOcultarLlPromocionYFormaPago();
                                                                        dialog.dismiss();
                                                                    }

                                                                } else {
                                                                    //Total contrato < 200
                                                                    validacionAbonoContrato(abonoEntranteString, "0", "0", "0");
                                                                    llamadaSincronizacion();
                                                                    mostrarOcultarLlPromocionYFormaPago();
                                                                    dialog.dismiss();
                                                                }

                                                            }

                                                        }

                                                    }


                                                }

                                            }

                                        }

                                    });

                                }

                            }

                        }

                    }

                }

            }

        }
    }

    private void llamadaHandlerRunnableImpresoraTermica() {
        if(llaves.impresora_termica) {
            //Se utilizara impresora termica
            if (banderaHandlerImpresora) {
                Handler handler2 = new Handler();
                runnable = new Runnable() {
                    public void run() {
                        if (imprimirTicket) {
                            //Log.i("MENSAJE", "Se detuvo el runnable");
                            impresoraBluetooth.verificarSiBluetoothPrinterEstaCerrado();
                            mandarAImprimirTicketImpresoraBluetooth(folioAlfanumerico);
                            if (imprimirTicketEntregaProducto) {
                                //Imprimir ticket de entrega de producto
                                mandarAImprimirTicketEntregaProductoImpresora();
                            }
                            handler2.removeCallbacks(runnable);
                        } else {
                            //Log.i("MENSAJE", "Entro a runnable");
                            handler2.postDelayed(runnable, 2000);
                        }
                    }
                };
                runnable.run();
            }
        }
    }

    private void mandarAImprimirTicketImpresoraBluetooth(String folio) {
        //Mandar a imprimir por bluetooth

        String abono = obtenerSumaAbono(folio);

        if(abono.indexOf(".") > 0) {
            //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
            abono = abono.substring(0, abono.indexOf("."));
        }

        String total = String.valueOf(obtenerTotalContrato());

        if(total.indexOf(".") > 0) {
            //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
            total = total.substring(0, total.indexOf("."));
        }

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        impresoraBluetooth.verificarSiBluetoothPrinterEstaCerrado();
        Object[] objetos = {obtenerRol.obtenerAtributoUsuarioLogeado("SUCURSAL"), obtenerEstadoAtributoContrato("NOMBRE"), ultimoIdContratoCreado, folio, totalanterior, abono,
                            total, fechaActual + time, obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO"), obtenerRol.obtenerAtributoUsuarioLogeado("TELEFONOATENCIONCLIENTESSUCURSAL"),
                            obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"),obtenerRol.obtenerAtributoUsuarioLogeado("WHATSAPP"),
                            global.obtenerAtributoTabla("ABONOS", "TIPOABONO", "FOLIO",folio)};
        impresoraBluetooth.printData(objetos, 0);
        impresoraBluetooth.closedBluetoothPrinter();
    }

    private String obtenerSumaAbono(String folio) {

        String abono = "";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT SUM(ABONO) FROM ABONOS" +
                    " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' AND FOLIO = '" + folio + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()) {
                abono = datos.getString(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 181);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return abono;

    }

    private int obtenerPaqueteHistorialClinico() {

        int idPaquete = 0;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID_PAQUETE FROM HISTORIALCLINICO WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' ORDER BY CREATED_AT DESC LIMIT 1";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el contrato");
            }

            if (datos.moveToFirst()) {
                idPaquete = datos.getInt(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 182);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return idPaquete;
    }

    private void llenarSpinnerMetodoPago() {
        idsMetodoPago = new String[]{"0", "1", "2"};
        metodosPago = new String[]{"Efectivo", "Tarjeta", "Transferencia"};
        spMetodoPago.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, metodosPago));

        if(obtenerEstadoAtributoContrato("PAGO").equals("0")) {
            //Forma de pago de contado
            if(!tieneAbonos(false) && !obtenerEstadoPromocion()) {
                idsPlanStripe = new String[]{"0", "1", "2", "3"};
                planesStripe = new String[]{"Un pago", "3 meses", "6 meses", "9 meses"};
                spPlanStripe.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, planesStripe));
            }
        }
    }

    private void verificarAbonoLiquidado() {
        String idAbonoLiquidado = obtenerAbonoLiquidadoContrato();
        if(idAbonoLiquidado.length() > 0) {
            actualizarAbonoContratoLiquidadoANormal(idAbonoLiquidado);
        }
    }

    private String obtenerAbonoLiquidadoContrato() {

        String idAbonoLiquidado = "";

        try{
            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID" +
                    " FROM ABONOS WHERE TIPOABONO = '6' AND ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);
            if(datos.moveToFirst()){
                idAbonoLiquidado = datos.getString(0);
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 183);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return idAbonoLiquidado;
    }

    private void actualizarAbonoContratoLiquidadoANormal(String idAbono) {

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE ABONOS SET TIPOABONO = '0', ENVIADOPAGINA = '0' WHERE TIPOABONO = '6' AND ID_CONTRATO='" + ultimoIdContratoCreado + "'" +
                    " AND ID = '" + idAbono + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 184);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private boolean fechaActualDentroPeriodo() {

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID_CONTRATO FROM CONTRATOS" +
                    " WHERE (strftime('%Y-%m-%d', '" + obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL") + "') == strftime('%Y-%m-%d', DIASELECCIONADO)" +
                    " OR (strftime('%Y-%m-%d', '" + obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL") + "')" +
                    " BETWEEN strftime('%Y-%m-%d', FECHACOBROINI) AND strftime('%Y-%m-%d', FECHACOBROFIN)))" +
                    " AND ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() > 0){
                sqLiteDB.close();
                datos.close();
                return true;
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 185);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return false;
    }

    private void obtenerPrecioDescuentoFormaPagoContado() {
        if(obtenerEstadoAtributoContrato("PAGO").equals("0")) {
            if (!obtenerBanderaContadoEngancheOSinEnganche("4")) {
                //No tiene contadoenganche
                if (!obtenerBanderaContadoEngancheOSinEnganche("5")) {
                    descuento = "300";
                }else {
                    descuento = "100";
                }
            }else {
                if (!obtenerBanderaContadoEngancheOSinEnganche("5")) {
                    descuento = "200";
                }else {
                    descuento = "0";
                }
            }
        }
    }

    private boolean obtenerBanderaContadoEngancheOSinEnganche(String tipo) {

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID, TIPOABONO FROM ABONOS WHERE TIPOABONO = '" + tipo + "' AND ID_CONTRATO= '" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                return false;
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 186);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return true;
    }

    private double obtenerPrecioAbonoPeriodo() {

        double precioTotalAbonoPeriodo = 0;

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT SUM(A.ABONO) FROM ABONOS A INNER JOIN CONTRATOS C ON A.ID_CONTRATO = C.ID_CONTRATO" +
                    " WHERE ((strftime('%Y-%m-%d', '" + obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL") + "')" +
                    " BETWEEN strftime('%Y-%m-%d', C.FECHACOBROINI) AND strftime('%Y-%m-%d', C.FECHACOBROFIN))" +
                    " OR strftime('%Y-%m-%d', '" + obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL") + "') == strftime('%Y-%m-%d', C.DIASELECCIONADO))" +
                    " AND A.TIPOABONO = '3'" +
                    " AND (strftime('%Y-%m-%d', A.CREATED_AT) BETWEEN strftime('%Y-%m-%d', C.FECHACOBROINI) AND strftime('%Y-%m-%d', C.FECHACOBROFIN))" +
                    " AND A.ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()){
                precioTotalAbonoPeriodo = datos.getDouble(0);
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 187);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return precioTotalAbonoPeriodo;
    }

    private void restarOSumarEnganche(String operacion, String descuento) {

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET TOTALHISTORIAL = CAST(TOTALHISTORIAL AS DECIMAL(5,2)) " + operacion + " CAST(" + descuento + " AS DECIMAL(5,2))"
                    + " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 188);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private boolean productosPagados() {

        precioTotalProductosNoPagados = 0;
        double precioAbonosProductos = obtenerAbonosProductosContrato();

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT TOTALPRODUCTO FROM CONTRATOS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No hay contrato");
            }

            if(datos.moveToFirst()){
                if(precioAbonosProductos >= Double.parseDouble(datos.getString(0))) {
                    return true;
                }else {
                    precioTotalProductosNoPagados = Double.parseDouble(datos.getString(0)) - precioAbonosProductos;
                    return false;
                }
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 189);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return false;

    }

    private double obtenerAbonosProductosContrato() {

        double precioAbonosProductos = 0;

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT SUM(ABONO) FROM ABONOS" +
                    " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' AND TIPOABONO = '7'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.moveToFirst()) {
                precioAbonosProductos = datos.getDouble(0);
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 190);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return precioAbonosProductos;
    }

    private boolean obtenerBanderaEnganche() {

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ENGANCHE FROM CONTRATOS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el contrato");
            }

            if(datos.moveToFirst()){
                if(Integer.parseInt(datos.getString(0)) > 0) {
                    return true;
                }
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 191);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return false;
    }

    private void validacionAbonoContrato(String abono, String tipoabono, String costoAtraso, String adelantos) {

        String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("ABONOS", 5);

        if(spMetodoPago.getSelectedItemPosition() == 1) {
            //Metodo de pago Tarjeta

            imprimirTicket = false; //Para que no trate de imprimir hasta que se haga la transferencia

            if(llPlanStripe.getVisibility() == View.GONE || spPlanStripe.getSelectedItemPosition() == 0) {

                try {

                    Card card = cardInputWidget.getCard();
                    stripe.createToken(card, new ApiResultCallback<Token>() {
                        @Override
                        public void onSuccess(@NotNull Token result) {
                            String tokenID = result.getId();
                            Log.i("MENSAJE", "Token: " + tokenID);
                            String receiptEmail = obtenerEstadoAtributoContrato("CORREO");
                            String sucursal = obtenerRol.obtenerAtributoUsuarioLogeado("SUCURSAL");
                            if (receiptEmail.length() == 0) {
                                receiptEmail = null;
                            }
                            com.stripe.Stripe.apiKey = global.obtenerAtributoTablaDescifrado("LLAVES", "LLAVE", "TIPO", "1");
                            ChargeCreateParams params = ChargeCreateParams.builder()
                                    .setAmount((long) Double.parseDouble(df0.format(Double.parseDouble(abono) * 100)))
                                    .setCurrency("mxn")
                                    .setDescription("M - " + df0.format(Double.parseDouble(abono)) + " - " + sucursal + " - " + ultimoIdContratoCreado + " - " + idAlfanumerico)
                                    .setReceiptEmail(receiptEmail)
                                    .setSource(tokenID)
                                    .build();

                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            fragmento.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Charge charge = Charge.create(params);
                                        if (charge != null) {
                                            Log.i("MENSAJE", "Charge: " + charge);
                                            guardarPagoTablaPayments(charge.getId(), obtenerEstadoAtributoContrato("CORREO"), idAlfanumerico,
                                                                     charge.getAmount(), charge.getCurrency(), charge.getStatus());
                                            guardarAbonoContratoBD(idAlfanumerico, abono, tipoabono, costoAtraso, adelantos, idsMetodoPago[spMetodoPago.getSelectedItemPosition()], "");
                                            llamadaSincronizacion();
                                            mostrarOcultarLlPromocionYFormaPago();
                                            global.imprimirMensajeEnHilo("Se realizó correctamente el pago con tarjeta");
                                            imprimirTicket = true;
                                        }
                                    } catch (StripeException e) {
                                        //Tarjeta sin dinero (Prueba con mi tarjeta de Bancoppel)
                                        //Requiere autenticacion
                                        global.escribirError(e, 192);
                                        transferenciaFallidaStripe(abono, tipoabono, costoAtraso, adelantos, false);
                                        global.imprimirMensajeEnHilo("La tarjeta no es valida por algunas de las siguientes razones:\n" +
                                                "1- Tarjeta no tiene fondos.\n" +
                                                "2- Requiere autenticación.\n" +
                                                "Error #1");
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }

                        @Override
                        public void onError(@NotNull Exception e) {
                            //Transferencia fallida por que no se tiene internet
                            transferenciaFallidaStripe(abono, tipoabono, costoAtraso, adelantos, false);
                            global.imprimirMensajeEnHilo("La transferencia fallo por algunas de las siguientes razones:\n" +
                                    "1- No se cuenta con internet.\n" +
                                    "2- Internet inestable.\n" +
                                    "Error #2");
                            e.printStackTrace();
                        }
                    });

                } catch (Exception e) {
                    // add a Toast to show the exception
                    global.escribirError(e, 193);
                    transferenciaFallidaStripe(abono, tipoabono, costoAtraso, adelantos, false);
                    global.imprimirMensajeEnHilo(e.getMessage() + "\nError #3");
                    e.printStackTrace();
                }

            }else {
                crearSubcripcionStripe(idAlfanumerico, abono, tipoabono, costoAtraso, adelantos);
            }

        }else {
            //Metodo de pago diferente a tarjeta
            guardarAbonoContratoBD(idAlfanumerico, abono, tipoabono, costoAtraso, adelantos, idsMetodoPago[spMetodoPago.getSelectedItemPosition()], "");
            Toast.makeText(fragmento.getActivity(), "Se agrego correctamente el abono", Toast.LENGTH_LONG).show();
        }

    }

    private void crearSubcripcionStripe(String idAlfanumerico, String abono, String tipoabono, String costoAtraso, String adelantos) {

        precioACobrarAlMeses = 0;
        mesesStripe = 0;

        if(spPlanStripe.getSelectedItemPosition() == 1) {
            mesesStripe = 3;
        }else if(spPlanStripe.getSelectedItemPosition() == 2) {
            mesesStripe = 6;
        }else if(spPlanStripe.getSelectedItemPosition() == 3) {
            mesesStripe = 9;
        }

        precioACobrarAlMeses = obtenerTotalContrato() / mesesStripe;

        Log.i("MENSAJE", "Precio a cobrar al mes " + df00.format(precioACobrarAlMeses));

        try {

            PaymentMethodCreateParams createParams = cardInputWidget.getPaymentMethodCreateParams();
            stripe.createPaymentMethod(createParams, new ApiResultCallback<PaymentMethod>() {
                @Override
                public void onSuccess(PaymentMethod paymentMethod) {
                    Log.i("MENSAJE", "PaymentMethod: " + paymentMethod.id);

                    com.stripe.Stripe.apiKey = global.obtenerAtributoTablaDescifrado("LLAVES", "LLAVE", "TIPO", "1");

                    String receiptEmail = obtenerEstadoAtributoContrato("CORREO");
                    String sucursal = obtenerRol.obtenerAtributoUsuarioLogeado("SUCURSAL");
                    if(receiptEmail.length() == 0) {
                        receiptEmail = null;
                    }

                    PaymentIntentCreateParams.PaymentMethodOptions.Card.Installments.Plan plan =
                            PaymentIntentCreateParams.PaymentMethodOptions.Card.Installments.Plan.builder()
                                    .setCount((long) mesesStripe)
                                    .setType(PaymentIntentCreateParams.PaymentMethodOptions.Card.Installments.Plan.Type.FIXED_COUNT)
                                    .setInterval(PaymentIntentCreateParams.PaymentMethodOptions.Card.Installments.Plan.Interval.MONTH)
                                    .build();
                    PaymentIntentCreateParams.PaymentMethodOptions.Card.Installments i =
                            PaymentIntentCreateParams.PaymentMethodOptions.Card.Installments.builder()
                                    .setEnabled(true)
                                    .setPlan(plan)
                                    .build();
                    PaymentIntentCreateParams.PaymentMethodOptions.Card card =
                            PaymentIntentCreateParams.PaymentMethodOptions.Card.builder()
                                    .setInstallments(i)
                                    .build();
                    PaymentIntentCreateParams.PaymentMethodOptions pmo =
                            PaymentIntentCreateParams.PaymentMethodOptions.builder()
                                    .setCard(card)
                                    .build();
                    PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                            .setAmount((long) obtenerTotalContrato() * 100)
                            .setCurrency("mxn")
                            .setPaymentMethod(paymentMethod.id)
                            .setPaymentMethodOptions(pmo)
                            .setReceiptEmail(receiptEmail)
                            .setDescription("M - " + df0.format(Double.parseDouble(abono)) + " - " + sucursal + " - " + ultimoIdContratoCreado + " - " + idAlfanumerico + " - Subscripción")
                            .setConfirm(true)
                            .build();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    fragmento.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                PaymentIntent intent = PaymentIntent.create(createParams);

                                if(intent != null) {
                                    Log.i("MENSAJE", "Intent: " + intent);

                                    guadarDatosStripeEnTabla(intent.getId(), paymentMethod.id);
                                    actualizarEstadoAtributoContrato("SUBSCRIPCION", String.valueOf(mesesStripe));
                                    actualizarEstadoAtributoContrato("FECHASUBSCRIPCION", fechaActual);
                                    if (obtenerPaqueteHistorialClinico() == 1 || obtenerPaqueteHistorialClinico() == 2 || obtenerPaqueteHistorialClinico() == 6) {
                                        //PAQUETE ES LECTURA, PROTECCION O DORADO2
                                    } else {
                                        //PAQUETE DIFERENTE A LECTURA, PROTECCION O DORADO2
                                        restarOSumarEnganche("+", descuento);
                                    }
                                    if (rol.equals("4")) {
                                        //Cobranza
                                        guardarAbonoContratoBD(idAlfanumerico, abono, "6", costoAtraso, adelantos, idsMetodoPago[spMetodoPago.getSelectedItemPosition()], "");
                                        if(!validacionEstadosContratoConfirmaciones()) {
                                            //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "5");
                                        }
                                    } else {
                                        //Asistente/Optometrista
                                        guardarAbonoContratoBD(idAlfanumerico, abono, "0", costoAtraso, adelantos, idsMetodoPago[spMetodoPago.getSelectedItemPosition()], "");
                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "1");
                                    }

                                    historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                            "Creo una subscripción por " + mesesStripe + " meses con pagos de $" + df00.format(precioACobrarAlMeses), "0");

                                    productosPagados();
                                    obtenerListaAbonos();
                                    obtenerListaProductos();
                                    calculoTotal();
                                    llamadaSincronizacion();
                                    mostrarOcultarLlPromocionYFormaPago();
                                    obtenerEstadoContratoYMostrarTextView();
                                    global.imprimirMensajeEnHilo("Se creo correctamente la subcripción");
                                    imprimirTicket = true;

                                }

                            } catch (StripeException e) {
                                //La tarjeta no tiene fondo
                                //Requiere autenticacion
                                global.escribirError(e, 194);
                                transferenciaFallidaStripe(abono, tipoabono, costoAtraso, adelantos, true);
                                global.imprimirMensajeEnHilo("La tarjeta no es valida por algunas de las siguientes razones:\n" +
                                        "1- Tarjeta de débito (Necesita ser de crédito).\n" +
                                        "2- Tarjeta no tiene fondos.\n" +
                                        "3- Requiere autenticación.\n" +
                                        "Error #1");
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onError(@NotNull Exception e) {
                    //No hay internet
                    transferenciaFallidaStripe(abono, tipoabono, costoAtraso, adelantos, true);
                    global.imprimirMensajeEnHilo("La transferencia fallo por algunas de las siguientes razones:\n" +
                            "1- No se cuenta con internet.\n" +
                            "2- Internet inestable.\n" +
                            "Error #2");
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            // add a Toast to show the exception
            global.escribirError(e, 195);
            transferenciaFallidaStripe(abono, tipoabono, costoAtraso, adelantos, true);
            global.imprimirMensajeEnHilo(e.getMessage() + "\nError #3");
            e.printStackTrace();
        }

    }

    private void guadarDatosStripeEnTabla(String idPaymentIntent, String idPaymentMethod) {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID_CONTRATO", ultimoIdContratoCreado);
            valores.put("ID_PAYMENTINTENT", idPaymentIntent);
            valores.put("ID_PAYMENTMETHOD", idPaymentMethod);
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT", fechaActual + time);
            valores.put("UPDATED_AT", fechaActual + time);
            sqLiteDB2.insert("DATOSSTRIPE", null, valores);
            sqLiteDB2.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 196);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void transferenciaFallidaStripe(String abono, String tipoabono, String costoAtraso, String adelantos, boolean subscripcion) {

        if (tipoabono.equals("0") && rol.equals("4")) {
            //Tipo abono normal y rol cobranza
            actualizarEstadoAtributoContrato("COSTOATRASO", String.valueOf(Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) + Double.parseDouble(costoAtraso)));
            actualizarEstadoAtributoContrato("PAGOSADELANTAR", String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) - Integer.parseInt(adelantos)));
            if (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) > 0) {
                if(!validacionEstadosContratoConfirmaciones()) {
                    //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                    actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "4");
                }
            }
        }
        if(tipoabono.equals("1")) {
            //Tipo abono enganche
            actualizarEstadoAtributoContrato("ENGANCHE", "0");
            restarOSumarEnganche("+", "100");
        }
        if (tipoabono.equals("2")) {
            //Tipo abono entrega producto
            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
            actualizarBanderaEntregaProductoYFechaEntrega(false);
        }
        if (tipoabono.equals("3")) {
            //Tipo abono periodo
            actualizarEstadoAtributoContrato("COSTOATRASO", String.valueOf(Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) + Double.parseDouble(costoAtraso)));
            actualizarEstadoAtributoContrato("PAGOSADELANTAR", String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) - Integer.parseInt(adelantos)));
            if (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) > 0) {
                if(!validacionEstadosContratoConfirmaciones()) {
                    //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                    actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "4");
                }
            }
        }
        if(tipoabono.equals("4") || tipoabono.equals("5")) {
            //Tipo abono contadoenganche o contado sin enganche
            if(rol.equals("4")) {
                //Cobranza
                if(!validacionEstadosContratoConfirmaciones()) {
                    //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                    restarOSumarEnganche("+", descuento);
                    actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                    actualizarBanderaEntregaProductoYFechaEntrega(false);
                }
            }else {
                //Asiste o Opto
                if(!subscripcion) {
                    //No es subscripcion
                    if (Double.parseDouble(df0.format(Double.parseDouble(abono))) == Double.parseDouble(df0.format((obtenerTotalContrato() - Double.parseDouble(descuento))))) {
                        restarOSumarEnganche("+", descuento);
                    } else {
                        actualizarEstadoAtributoContrato("ENGANCHE", "0");
                        restarOSumarEnganche("+", "100");
                    }
                }else {
                    //Es subscripcion
                    actualizarEstadoAtributoContrato("ENGANCHE", "0");
                    restarOSumarEnganche("+", descuento);
                }
            }
        }
        if (tipoabono.equals("6")) {
            //Tipo abono liquidado
            if (obtenerEstadoPromocion()) {
                //Tiene promocion
                if (!obtenerEstadoAtributoContrato("PAGO").equals("0")) {
                    //Forma de pago semanal, quincenal o mensual
                    if (!existeAbonoEntregaProducto()) {
                        actualizarBanderaEntregaProductoYFechaEntrega(false);
                    }
                    if(!validacionEstadosContratoConfirmaciones()) {
                        //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                        if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {
                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                        } else {
                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                        }
                    }
                } else {
                    //Forma pago contado
                    if(!validacionEstadosContratoConfirmaciones()) {
                        //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                        actualizarBanderaEntregaProductoYFechaEntrega(false);
                    }
                }
            } else {
                //No tiene promocion
                actualizarEstadoAtributoContrato("COSTOATRASO", String.valueOf(Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) + Double.parseDouble(costoAtraso)));
                if (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) > 0) {
                    if(!validacionEstadosContratoConfirmaciones()) {
                        //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "4");
                    }
                } else {
                    if (!existeAbonoEntregaProducto()) {
                        actualizarBanderaEntregaProductoYFechaEntrega(false);
                    }
                    if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {
                        if(!validacionEstadosContratoConfirmaciones()) {
                            //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                        }
                    } else {
                        if(global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) > 0 && Integer.parseInt(obtenerEstadoAtributoContrato("PAGO")) == 0) {
                            //Tiene historiales con garantia y forma de pago es de contado
                            if(!validacionEstadosContratoConfirmaciones()) {
                                //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                            }
                        }else {
                            //No tiene historiales con garantia
                            if(!validacionEstadosContratoConfirmaciones()) {
                                //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                            }
                        }
                    }
                }
            }
        }
        obtenerEstadoContratoYMostrarTextView();
        llamadaSincronizacion();
        mostrarOcultarLlPromocionYFormaPago();

    }

    private void guardarPagoTablaPayments(String idPayment, String email, String idAbono, Long amount, String currency, String paymentStatus) {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("PAYMENT_ID", idPayment);
            valores.put("PAYER_EMAIL", email);
            valores.put("ID_ABONO", idAbono);
            valores.put("AMOUNT", String.valueOf(amount / 100));
            valores.put("CURRENCY", currency);
            valores.put("PAYMENT_STATUS", paymentStatus);
            valores.put("TIPOORIGEN", "M");
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT", fechaActual + time);
            valores.put("UPDATED_AT", fechaActual + time);
            sqLiteDB2.insert("PAYMENTS", null, valores);
            sqLiteDB2.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 197);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void guardarAbonoContratoBD(String idAlfanumerico, String abono, String tipoabono, String costoAtraso, String adelantos, String metodoPago, String idcontratoproducto) {

        String corte = "2";

        if (rol.equals("4")) {
            //Rol cobranza
            corte = "0";
        }

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try {

            ContentValues valores = new ContentValues();
            valores.put("ID", idAlfanumerico);
            valores.put("ID_CONTRATO", ultimoIdContratoCreado);
            valores.put("ABONO", df0.format(Double.parseDouble(abono)));
            valores.put("FOLIO", folioAlfanumerico);
            valores.put("TIPOABONO", tipoabono);
            valores.put("ATRASO", costoAtraso);
            valores.put("ADELANTOS", adelantos);
            valores.put("ID_USUARIO", idUsuario);
            valores.put("METODOPAGO", metodoPago);
            valores.put("CORTE", corte);
            valores.put("ENVIADOPAGINA", "0");
            if (tipoabono.equals("7")) {
                //Tipo abono de producto
                valores.put("ID_CONTRATOPRODUCTO", idcontratoproducto);
            }else {
                valores.put("ID_CONTRATOPRODUCTO", "");
            }
            valores.put("COORDENADAS", coordenadasAbono);
            valores.put("CREATED_AT", fechaActual + time);
            valores.put("UPDATED_AT", fechaActual + time);
            valores.put("ENVIADO", "0");
            valores.put("POLIZA", "");
            sqLiteDB = conexion.getWritableDatabase();
            sqLiteDB.insert("ABONOS", null, valores);
            sqLiteDB.close();

            Thread.sleep(1000); //Esperamos 1 segundo para que se inserte el abono.

            actualizarEstadoAtributoContrato("ULTIMOABONO", fechaActual + time);
            historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                    "Se abono la cantidad: '" + df0.format(Double.parseDouble(abono)) + "'", "0");

            //Guardar movimiento en archivo de texto
            guardarMovimientoEnArchivoTxt(fechaActual + time, "Agregado", df0.format(Double.parseDouble(abono)), metodoPago,
                    folioAlfanumerico, idAlfanumerico, adelantos, tipoabono, costoAtraso, coordenadasAbono);

            productosPagados();
            obtenerListaAbonos();
            calculoTotal();

            if (global.obtenerAtributoTabla("ABONOS", "ID", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", idAlfanumerico).length() == 0) {
                //No se inserto el abono (Intentar de nuevo guardarlo)
                guardarAbonoContratoBD(idAlfanumerico, abono, tipoabono, costoAtraso, adelantos, metodoPago, idcontratoproducto);
            }
        } catch (Exception e) {
            global.escribirError(e, 198);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    private void guardarMovimientoEnArchivoTxt(String fechaCreacion, String accion, String abono, String metodoPago, String folioAlfanumerico, String idAbonoAlfanumerico,
                                               String adelantos, String tipoabono, String atraso, String coordenadas) {
        String nombreUsuario = obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO");
        String nombreCliente = obtenerEstadoAtributoContrato("NOMBRE");
        String formaPagoLetra = global.obtenerFormaPagoLetra(Integer.parseInt(obtenerEstadoAtributoContrato("PAGO")));
        String metodoPagoLetra = "";
        if (metodoPago.length() > 0) {
            metodoPagoLetra = global.obtenerMetodoPagoLetra(Integer.parseInt(metodoPago));
        }

        global.escribirMovimientoEnArchivoTxt(fechaCreacion + "," + accion + "," + global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(nombreUsuario)) + "," + ultimoIdContratoCreado + "," + global.limpiarCadenaCaracteresEspeciales(nombreCliente) + "," + abono + "," +
                                              metodoPagoLetra + "," + folioAlfanumerico + "," + idAbonoAlfanumerico + "," + formaPagoLetra + "," +
                                              metodoPago + "," + adelantos + "," + tipoabono + "," + atraso + "," + coordenadas);
        global.removerLineasEnArchivoMovimientosTxt(fechaActual);
    }

    private void actualizarTotalAbonoContratoBD() {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET TOTALABONO = IFNULL((SELECT SUM(CAST(ABONO AS DECIMAL(5,2))) FROM ABONOS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'), '0')"
                    + " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";

            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();


        }catch (SQLiteException e) {
            global.escribirError(e, 199);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void obtenerListaProductos() {

        int estadoContrato = Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"));

        filaProductos.clear();

        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT PRODUCTO.ID, CONTRATOPRODUCTO.ID, PRODUCTO.NOMBRE, PRODUCTO.PRECIO, CONTRATOPRODUCTO.PIEZAS," +
                    " CONTRATOPRODUCTO.TOTAL, PRODUCTO.PRECIOP, PRODUCTO.ACTIVO FROM CONTRATOPRODUCTO" +
                    " INNER JOIN PRODUCTO WHERE CONTRATOPRODUCTO.ID_PRODUCTO = PRODUCTO.ID AND " +
                    "CONTRATOPRODUCTO.ID_CONTRATO='" + ultimoIdContratoCreado + "' ORDER BY CONTRATOPRODUCTO.CREATED_AT DESC";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0) {
                //No se encontraron registros
                if (estadoContrato == 0) {
                    //Estado del contrato es igual a NO TERMINADO
                    llListaProductos.setVisibility(View.GONE);
                } else {
                    //Estado del contrato es diferente a NO TERMINADO
                    llProductos.setVisibility(View.VISIBLE);
                    llListaProductos.setVisibility(View.GONE);
                }
                lvProductos.removeAllViewsInLayout();
                lvProductos.postInvalidate();
            } else {
                //Se encontraron registros
                llListaProductos.setVisibility(View.VISIBLE);
                llTvYBtnProductos.setVisibility(View.VISIBLE);

                while (datos.moveToNext()) {

                    FilaProductos fila = new FilaProductos();
                    fila.setIdProducto(datos.getString(0));
                    fila.setIdProductoContrato(datos.getString(1));
                    fila.setProducto(datos.getString(2));
                    if (datos.getInt(7) == 1) {
                        fila.setPrecio(df0.format(Double.parseDouble(datos.getString(6))));
                    } else {
                        fila.setPrecio(df0.format(Double.parseDouble(datos.getString(3))));
                    }
                    fila.setPiezas(datos.getString(4));
                    fila.setTotal(datos.getString(5));

                    filaProductos.add(fila);
                }

                adaptadorListaProductos adaptador = new adaptadorListaProductos(fragmento.getContext(), filaProductos);
                lvProductos.setAdapter(adaptador);

            }

            sqLiteDB.close();
            datos.close();

        } catch (Exception e) {
            global.escribirError(e, 200);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void obtenerListaAbonos() {

        filaAbonos.clear();

        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID, ABONO, TIPOABONO, CREATED_AT, FOLIO, METODOPAGO FROM ABONOS" +
                    " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' ORDER BY CREATED_AT DESC";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                llListaAbonos.setVisibility(View.GONE);
                lvAbonos.removeAllViewsInLayout();
                lvAbonos.postInvalidate();
            }else {

                llListaAbonos.setVisibility(View.VISIBLE);

                while (datos.moveToNext()) {

                    FilaAbonos fila = new FilaAbonos();
                    fila.setIdAbono(datos.getString(0));
                    fila.setAbono(df0.format(Double.parseDouble(datos.getString(1))));
                    if(datos.getInt(2) == 0) {
                        if(tieneAbonoAtrasado(datos.getString(0))){
                            fila.setTipoAbono("Atraso");
                        }else {
                            fila.setTipoAbono("Normal");
                        }
                    }else if(datos.getInt(2) == 1) {
                        fila.setTipoAbono("Enganche");
                    }else if(datos.getInt(2) == 2) {
                        fila.setTipoAbono("Entrega producto");
                    }else if(datos.getInt(2) == 3) {
                        fila.setTipoAbono("Periodo");
                    }else if(datos.getInt(2) == 4) {
                        fila.setTipoAbono("C enganche");
                    }else if(datos.getInt(2) == 5) {
                        fila.setTipoAbono("C SN/Enganche");
                    }else if(datos.getInt(2) == 6) {
                        fila.setTipoAbono("Liquidado");
                    }else if(datos.getInt(2) == 7) {
                        fila.setTipoAbono("Producto");
                    }else if(datos.getInt(2) == 8) {
                        fila.setTipoAbono("Cancelación");
                    }

                    //Convertir la fecha de un formato a otro
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;

                    try {

                        date = simpleDateFormat.parse(datos.getString(3));
                        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        fila.setFechaAbono(convetDateFormat.format(date));

                    } catch (ParseException e) {
                        global.escribirError(e, 201);
                        e.printStackTrace();
                    }

                    fila.setFolioAbono(datos.getString(4));

                    //Metodo de pago
                    if(datos.getInt(5) == 0) {
                        fila.setMetodoPago("Efectivo");
                    }else if(datos.getInt(5) == 1) {
                        fila.setMetodoPago("Tarjeta");
                    }else if(datos.getInt(5) == 2) {
                        fila.setMetodoPago("Transferencia");
                    }else if(datos.getInt(5) == 3) {
                        fila.setMetodoPago("Cancelacion");
                    }

                    filaAbonos.add(fila);
                }

                adaptadorListaAbonos adaptador = new adaptadorListaAbonos(fragmento.getContext(), filaAbonos);
                lvAbonos.setAdapter(adaptador);

            }

            sqLiteDB.close();
            datos.close();

        }catch (Exception e) {
            global.escribirError(e, 202);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private boolean tieneAbonoAtrasado(String idAbono) {

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL;

            SQL = "SELECT CASE WHEN ATRASO THEN 1 ELSE 0 END" +
                    " FROM ABONOS WHERE ID='" + idAbono + "'";

            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()){
                if(datos.getInt(0) > 0) {
                    return true;
                }
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 203);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return false;
    }

    public void mostrarOcultarListas() {
        mostrarOcultarListas = !mostrarOcultarListas;

        if(mostrarOcultarListas) {
            btnMostrarOcultar.setText("Mostrar menos");
            svScrollPrincipal.setVisibility(View.GONE);
            llSecundario.setVisibility(View.VISIBLE);
        }else {
            btnMostrarOcultar.setText("Mostrar mas");
            svScrollPrincipal.setVisibility(View.VISIBLE);
            llSecundario.setVisibility(View.GONE);
        }

    }

    public void terminarContrato() {

        if(rol.equals("4")) {//ROL COBRANZA

            if(Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) == 6) {
                //Estado del contrato es cancelado

                llamadaSincronizacion();

                Fragment verificadorFragment;
                FragmentTransaction transaction;
                verificadorFragment = new principal(false);
                transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, verificadorFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.commit();

            }else{
                //Estado del contrato es diferente a cancelado

                if (obtenerTotalContrato() > 0) {

                    if (Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) >= 1
                            && Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) < 12) { //ESTATUS_ESTADOCONTRATO = 2 ENTREGADO

                        llamadaSincronizacion();

                        Fragment verificadorFragment;
                        FragmentTransaction transaction;
                        verificadorFragment = new principal(false);
                        transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, verificadorFragment);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    } else { //ESTATUS_ESTADOCONTRATO = 12 ENVIADO

                        if (!obtenerEstadoAtributoContrato("PAGO").equals("0")) { //Forma de pago semanal, quincenal o mensual

                            if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {//Todavia no se ha dado el abonoMinimoSemanal
                                Toast.makeText(fragmento.getActivity(), "Debes abonar al menos $" + abonoMinimoSemanal + " para entregar el producto", Toast.LENGTH_LONG).show();
                            } else {

                                AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                                alerta.setTitle("¿Cambiar estado a producto entregado?")
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

                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                        mandarAImprimirTicketEntregaProductoImpresora();
                                        llamadaSincronizacion();

                                        dialog.dismiss();
                                        Fragment verificadorFragment;
                                        FragmentTransaction transaction;
                                        verificadorFragment = new principal(false);
                                        transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.content, verificadorFragment);
                                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                });

                            }

                        } else { //Forma de pago de contado

                            if (global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) > 0 && obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                //Tiene historiales con garatias y ya se habia dado el abono Contado S enganche (Pagado por completo)
                                Toast.makeText(fragmento.getActivity(), "Debes abonar $" + df0.format(obtenerTotalContrato()) + " para entregar el producto", Toast.LENGTH_LONG).show();
                            } else {
                                //No tiene historiales con garatias
                                if (obtenerEstadoPromocion()) { //Tiene promocion
                                    if (obtenerTotalContrato() > 0) {
                                        Toast.makeText(fragmento.getActivity(), "Debes abonar $" + df0.format(obtenerTotalContrato()) + " para entregar el producto", Toast.LENGTH_LONG).show();
                                    }
                                } else { //No tiene promocion
                                    if (obtenerPaqueteHistorialClinico() == 1 || obtenerPaqueteHistorialClinico() == 2 || obtenerPaqueteHistorialClinico() == 6
                                            || (obtenerTotalContrato() < 400 && !obtenerBanderaContadoEngancheOSinEnganche("4") && !obtenerBanderaContadoEngancheOSinEnganche("5"))) {
                                        //PAQUETE ES LECTURA, PROTECCION O DORADO2 O TOTALCONTRATO ES MENOR A 400
                                        //Y NO SE TIENE EL ABONO CONTADOENGACHE NI TAMPOCO EL CONTADOSINENGANCHE (CUANDO ES UNA POLIZA DE SEGURO Y ES SEMANAL, QUICENAL O MENSUAL Y SE PASA A CONTADO)
                                        Toast.makeText(fragmento.getActivity(), "Debes abonar $" + df0.format(obtenerTotalContrato()) + " para entregar el producto", Toast.LENGTH_LONG).show();
                                    } else {
                                        //PAQUETE DIFERENTE A LECTURA, PROTECCION O DORADO2 O TOTALCONTRATO ES MAYOR A 400 O SE TIENE EL ABONO CONTADOENGACHE O SE TIENE EL CONTADOSINENGANCHE
                                        if ((obtenerTotalContrato() - Double.parseDouble(descuento)) > 0) {
                                            Toast.makeText(fragmento.getActivity(), "Debes abonar $" +
                                                    df0.format(obtenerTotalContrato() - Double.parseDouble(descuento)) + " para entregar el producto", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }

                        }

                    }

                } else {//Ya se encuentra liquidado el contrato

                    if (!validacionEstadosContratoConfirmaciones()) {
                        //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "5");

                        if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {
                            actualizarBanderaEntregaProductoYFechaEntrega(true);
                            mandarAImprimirTicketEntregaProductoImpresora();
                        }

                    }

                    llamadaSincronizacion();

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

        }else { //ROL ASISTENTE Y OPTOMETRISTA

            if (garantia) {

                llamadaSincronizacion();

                Fragment verificadorFragment;
                FragmentTransaction transaction;
                verificadorFragment = new principal(false);
                transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, verificadorFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.commit();

            } else {

                String mensajeAlerta;

                int estadocontrato = Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"));

                if (estadocontrato == 1) {
                    if (obtenerEstadoPromocion()) {
                        if (!promocionContratoCompletada()) {
                            mensajeAlerta = "¿Ir a siguiente contrato?";
                        } else {
                            mensajeAlerta = "¿Deseas salir del contrato?";
                        }
                    } else {
                        mensajeAlerta = "¿Deseas salir del contrato?";
                    }
                } else {
                    mensajeAlerta = "¿Terminar contrato?";
                }

                AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                alerta.setTitle("Confirmación").setMessage(mensajeAlerta)
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

                final AlertDialog dialog = alerta.create(); //Creacion de alerta


                if (obtenerEstadoAtributoContrato("PAGO").equals("")) {
                    Toast.makeText(fragmento.getActivity(), "Debes agregar una forma de pago", Toast.LENGTH_LONG).show();
                } else {

                    if (btnNuevoHistorial.getVisibility() == View.VISIBLE) {
                        Toast.makeText(fragmento.getActivity(), "Favor de llenar los historiales clinicos necesarios", Toast.LENGTH_LONG).show();
                    } else {

                        if (!productosPagados()) {
                            Toast.makeText(fragmento.getActivity(), "Los productos adicionales (Gotas,Polizas de seguro,etc.) se pagan de contado, por favor agrega el abono correspondiente", Toast.LENGTH_LONG).show();
                        } else {

                            if (totalAbonosProductos() != totalProductosContrato()) {
                                Toast.makeText(fragmento.getActivity(), "Los abonos de producto no concuerdan con el total de productos", Toast.LENGTH_LONG).show();
                            } else {

                                //Proceso para mostrar o no alerta de terminar contrato
                                boolean mostrarAlerta = false;

                                if (estadocontrato == 0) {
                                    if (svScrollPrincipal.getVisibility() == View.VISIBLE) {
                                        mostrarOcultarListas = !mostrarOcultarListas;
                                        btnMostrarOcultar.setText("Mostrar menos");
                                        svScrollPrincipal.setVisibility(View.GONE);
                                        llSecundario.setVisibility(View.VISIBLE);
                                    } else {
                                        mostrarAlerta = true;
                                    }
                                } else {
                                    mostrarAlerta = true;
                                }

                                if (mostrarAlerta) {

                                    dialog.show();
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {

                                            if (obtenerEstadoPromocion()) { //Tiene promocion el contrato padre

                                                if (promocionContratoCompletada()) { //Tiene promocion y armazones = contador

                                                    if (ultimoIdContratoCreado.equals(idContratoPadre)) { //Estamos en el contrato padre

                                                        if (obtenerEstadoAtributoContrato("ESTATUS").equals("1")) { //ESTATUS = 1 -> Contrato padre con promocion terminado

                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "1");

                                                            llamadaSincronizacion();

                                                            dialog.dismiss();
                                                            Fragment verificadorFragment;
                                                            FragmentTransaction transaction;
                                                            verificadorFragment = new principal(false);
                                                            transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                            transaction.replace(R.id.content, verificadorFragment);
                                                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                            transaction.addToBackStack(null);
                                                            transaction.commit();

                                                        } else { //ESTATUS = 0 -> Contrato padre tiene promocion y se mandara asi mismo para ver el resultado

                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "1");
                                                            actualizarEstadoAtributoContrato("ESTATUS", "1");

                                                            obtenerPreciosPromocion();

                                                            llamadaSincronizacion();

                                                            dialog.dismiss();
                                                            Fragment verificadorFragment = new verContrato();
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("ultimoIdContratoCreado", idContratoPadre);
                                                            verificadorFragment.setArguments(bundle);
                                                            FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                            transaction.replace(R.id.content, verificadorFragment);
                                                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                            transaction.addToBackStack(null);
                                                            transaction.commit();

                                                        }


                                                    } else { //Estamos en el ultimo contrato hijo

                                                        if (obtenerEstadoAtributoContrato("ESTATUS").equals("1")) { //ESTATUS = 1

                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "1");

                                                            llamadaSincronizacion();

                                                            dialog.dismiss();
                                                            Fragment verificadorFragment;
                                                            FragmentTransaction transaction;
                                                            verificadorFragment = new principal(false);
                                                            transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                            transaction.replace(R.id.content, verificadorFragment);
                                                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                            transaction.addToBackStack(null);
                                                            transaction.commit();

                                                        } else { //ESTATUS = 0

                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "1");
                                                            actualizarEstadoAtributoContrato("ESTATUS", "1");

                                                            obtenerPreciosPromocion();

                                                            llamadaSincronizacion();

                                                            dialog.dismiss();
                                                            Fragment verificadorFragment = new verContrato();
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("ultimoIdContratoCreado", idContratoPadre);
                                                            verificadorFragment.setArguments(bundle);
                                                            FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                            transaction.replace(R.id.content, verificadorFragment);
                                                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                            transaction.addToBackStack(null);
                                                            transaction.commit();

                                                        }

                                                    }

                                                } else { //Tiene promocion pero armozones no es igual a contador

                                                    if (obtenerEstadoAtributoContrato("PROMOCIONTERMINADA").equals("1") && obtenerEstadoAtributoContrato("ESTATUS").equals("1")
                                                            && obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("0")) {
                                                        //Confirmaciones lo paso a estado NO TERMINADO

                                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "1");

                                                        llamadaSincronizacion();

                                                        dialog.dismiss();
                                                        Fragment verificadorFragment;
                                                        FragmentTransaction transaction;
                                                        verificadorFragment = new principal(false);
                                                        transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                        transaction.replace(R.id.content, verificadorFragment);
                                                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                        transaction.addToBackStack(null);
                                                        transaction.commit();

                                                    } else {

                                                        //NUEVO CONTRATO HIJO
                                                        if (ultimoIdContratoCreado.equals(idContratoPadre)) { //Estamos en el contrato padre

                                                            if (obtenerEstadoAtributoContrato("ESTATUS").equals("1")) { //ESTATUS = 1

                                                                if (verificarSiHayContratosHijosSinTerminar(13)) {

                                                                    dialog.dismiss();
                                                                    Fragment verificadorFragment = new nuevoContrato();
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putString("idContratoPadre", idContratoPadre);
                                                                    bundle.putString("idContratoHijo", idContratoHijoSinTerminar);
                                                                    bundle.putBoolean("contratoPromocion", true);
                                                                    verificadorFragment.setArguments(bundle);
                                                                    FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                                    transaction.replace(R.id.content, verificadorFragment);
                                                                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                                    transaction.addToBackStack(null);
                                                                    transaction.commit();

                                                                } else {

                                                                    if (verificarSiHayContratosHijosSinTerminar(0)) {

                                                                        dialog.dismiss();
                                                                        Fragment verificadorFragment = new verContrato();
                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putString("ultimoIdContratoCreado", idContratoHijoSinTerminar);
                                                                        verificadorFragment.setArguments(bundle);
                                                                        FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                                        transaction.replace(R.id.content, verificadorFragment);
                                                                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                                        transaction.addToBackStack(null);
                                                                        transaction.commit();

                                                                    } else {

                                                                        llamadaSincronizacion();

                                                                        dialog.dismiss();
                                                                        Fragment verificadorFragment = new nuevoContrato();
                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putString("idContratoPadre", idContratoPadre);
                                                                        bundle.putString("idContratoHijo", "");
                                                                        bundle.putBoolean("contratoPromocion", true);
                                                                        verificadorFragment.setArguments(bundle);
                                                                        FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                                        transaction.replace(R.id.content, verificadorFragment);
                                                                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                                        transaction.addToBackStack(null);
                                                                        transaction.commit();
                                                                    }

                                                                }

                                                            } else {

                                                                actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "1");
                                                                actualizarEstadoAtributoContrato("ESTATUS", "1");

                                                                llamadaSincronizacion();

                                                                dialog.dismiss();
                                                                Fragment verificadorFragment = new nuevoContrato();
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("idContratoPadre", idContratoPadre);
                                                                bundle.putString("idContratoHijo", "");
                                                                bundle.putBoolean("contratoPromocion", true);
                                                                verificadorFragment.setArguments(bundle);
                                                                FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                                transaction.replace(R.id.content, verificadorFragment);
                                                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                                transaction.addToBackStack(null);
                                                                transaction.commit();

                                                            }

                                                        } else {

                                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "1");
                                                            actualizarEstadoAtributoContrato("ESTATUS", "1");

                                                            if (verificarSiHayContratosHijosSinTerminar(13)) {

                                                                dialog.dismiss();
                                                                Fragment verificadorFragment = new nuevoContrato();
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("idContratoPadre", idContratoPadre);
                                                                bundle.putString("idContratoHijo", idContratoHijoSinTerminar);
                                                                bundle.putBoolean("contratoPromocion", true);
                                                                verificadorFragment.setArguments(bundle);
                                                                FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                                transaction.replace(R.id.content, verificadorFragment);
                                                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                                transaction.addToBackStack(null);
                                                                transaction.commit();

                                                            } else {

                                                                if (verificarSiHayContratosHijosSinTerminar(0)) {

                                                                    dialog.dismiss();
                                                                    Fragment verificadorFragment = new verContrato();
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putString("ultimoIdContratoCreado", idContratoHijoSinTerminar);
                                                                    verificadorFragment.setArguments(bundle);
                                                                    FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                                    transaction.replace(R.id.content, verificadorFragment);
                                                                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                                    transaction.addToBackStack(null);
                                                                    transaction.commit();

                                                                } else {

                                                                    llamadaSincronizacion();

                                                                    dialog.dismiss();
                                                                    Fragment verificadorFragment = new nuevoContrato();
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putString("idContratoPadre", idContratoPadre);
                                                                    bundle.putString("idContratoHijo", "");
                                                                    bundle.putBoolean("contratoPromocion", true);
                                                                    verificadorFragment.setArguments(bundle);
                                                                    FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                                                                    transaction.replace(R.id.content, verificadorFragment);
                                                                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                                                    transaction.addToBackStack(null);
                                                                    transaction.commit();
                                                                }

                                                            }

                                                        }

                                                    }

                                                }
                                            } else { //No tiene promocion

                                                //UN SOLO CONTRATO
                                                actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "1");

                                                llamadaSincronizacion();

                                                dialog.dismiss();
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

                                }

                            }

                        }

                    }

                }

            }

        }

    }

    private String obtenerIdPromocionContratoPadre() {

        String idPromocion = "";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID_PROMOCION" +
                    " FROM CONTRATOS WHERE ID_CONTRATO='" + idContratoPadre + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()){
                idPromocion = datos.getString(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 204);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return idPromocion;
    }

    private void actualizarBanderaEntregaProductoYFechaEntrega(boolean activar) {

        if(activar) {
            actualizarEstadoAtributoContrato("ENTREGAPRODUCTO", "1");
            time = new SimpleDateFormat(" HH:mm:ss").format(new Date());
            actualizarEstadoAtributoContrato("FECHAENTREGA", fechaActual + time);

            //Guardar en historial de movimientos que se entrego el contrato
            historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado, "Cambio el estatus a entregado", "0");

            //Actualizar fechacobroini y fechacobrofin en "" (Esto se debe hacer por que hay veces que el contrato tiene un estado de ENVIADO y ya tiene fechacobroini y fechacobrofin)
            actualizarEstadoAtributoContrato("FECHACOBROINI", "");
            actualizarEstadoAtributoContrato("FECHACOBROFIN", "");
        }else {
            if(global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) == 0) {
                //No tiene historiales con garantia
                actualizarEstadoAtributoContrato("ENTREGAPRODUCTO", "0");
                actualizarEstadoAtributoContrato("FECHAENTREGA", "");
            }else {
                //Tiene historiales con garantia
                if(Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) == 12) {
                    //Tiene estatus ENVIADO o Forma de pago es Semanal, Quincenal o Mensual
                    actualizarEstadoAtributoContrato("ENTREGAPRODUCTO", "0");
                    actualizarEstadoAtributoContrato("FECHAENTREGA", "");
                }
            }
        }

    }

    private boolean verificarSiHayContratosHijosSinTerminar(int estado) {

        boolean respuestaBoleana = false;
        idContratoHijoSinTerminar = "";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL;

            SQL = "SELECT ID_CONTRATO, ESTATUS_ESTADOCONTRATO" +
                    " FROM CONTRATOS WHERE IDCONTRATORELACION='" + idContratoPadre + "'";

            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            int contador = 0;

            while (datos.moveToNext()) {
                if (datos.getInt(1) == estado) {
                    respuestaBoleana = true;
                    idContratoHijoSinTerminar = datos.getString(0);
                }
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 205);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return respuestaBoleana;
    }

    private double totalProductosContrato() {

        double totalProductosContrato = 0;

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT SUM(TOTAL) FROM CONTRATOPRODUCTO" +
                    " WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()){
                totalProductosContrato = datos.getDouble(0);
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 206);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return totalProductosContrato;
    }

    private double totalAbonosProductos() {

        double totalAbonosProductos = 0;

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT SUM(ABONO) FROM ABONOS" +
                    " WHERE TIPOABONO = '7' AND ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()){
                totalAbonosProductos = datos.getDouble(0);
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 207);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return totalAbonosProductos;
    }

    private void actualizarEstadoAtributoContrato(String atributo, String estado) {

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET " + atributo + "='" + estado
                    + "' WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 208);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void obtenerPreciosPromocion() {

        List<Double> preciosPromocion = new ArrayList<>();

        try {

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT TOTALHISTORIAL, ENGANCHE FROM CONTRATOS WHERE IDCONTRATORELACION='" + idContratoPadre + "' OR ID_CONTRATO='" + idContratoPadre + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0) {
                Log.i("MENSAJE", "No hay datos en el idContratoPadre");
            }

            while (datos.moveToNext()) {
                if (Integer.parseInt(datos.getString(1)) > 0) { //Por que ya dieron el enganche y se tiene que sumar los 100 al totalHistorial
                    preciosPromocion.add(Double.parseDouble(datos.getString(0)) + 100);
                } else {
                    preciosPromocion.add(Double.parseDouble(datos.getString(0)));
                }
            }

            obtenerPrecioPromocionMenor(preciosPromocion);
            sqLiteDB.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 209);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private int obtenerTipoPromocion(String idPromocion) {

        int tipoPromocion = 0;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT TIPOPROMOCION" +
                    " FROM PROMOCION WHERE ID='" + idPromocion + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()){
                tipoPromocion = datos.getInt(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 210);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return tipoPromocion;
    }

    private double obtenerPrecioUnoPromocion(String idPromocion) {

        double precioUno = 0;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT PRECIOUNO" +
                    " FROM PROMOCION WHERE ID='" + idPromocion + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()){
                precioUno = datos.getDouble(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 211);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return precioUno;

    }

    private void obtenerPrecioPromocionMenor(List<Double> preciosPromocion) {

        double precioMenor = preciosPromocion.get(0);
        for (int i = 0; i < preciosPromocion.size(); i++) {
            double precioActual = preciosPromocion.get(i);
            if (precioActual < precioMenor) {
                precioMenor = precioActual;
            }
        }

        int tipoPromocion = obtenerTipoPromocion(obtenerIdPromocionContratoPadre());

        double precioPromocionAplicada;
        double precioTotalPromocionAplicada;

        if(tipoPromocion == 1) {
            precioTotalPromocionAplicada = sumarPreciosPromocion(preciosPromocion) - obtenerPrecioUnoPromocion(obtenerIdPromocionContratoPadre());
        }else {
            precioPromocionAplicada = (precioMenor * obtenerPorcentajePromocionBD()) / 100;
            precioTotalPromocionAplicada = sumarPreciosPromocion(preciosPromocion) - precioPromocionAplicada;
        }

        double totalPromocionParaCadaContrato = precioTotalPromocionAplicada / preciosPromocion.size();

        actualizarPrecioTotalYTotalPromocionContratosBD(totalPromocionParaCadaContrato);

    }

    private void actualizarPrecioTotalYTotalPromocionContratosBD(double totalPromocionParaCadaContrato) {

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET TOTALPROMOCION = CAST(CAST(TOTALPROMOCION AS DECIMAL(5,0)) + CAST(" + totalPromocionParaCadaContrato + " AS DECIMAL(5,0)) AS INTEGER), " +
                    "PROMOCIONTERMINADA = '1', ENVIADOPAGINA = '0'" +
                    " WHERE IDCONTRATORELACION='" + idContratoPadre + "' OR ID_CONTRATO='" + idContratoPadre + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();
            calculoTotal();

        }catch (SQLiteException e) {
            global.escribirError(e, 212);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private int obtenerPorcentajePromocionBD() {

        int porcentajePromocion = 0;

        try{
            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT PROMOCION.PRECIOP FROM CONTRATOS INNER JOIN PROMOCION WHERE CONTRATOS.ID_PROMOCION = PROMOCION.ID AND CONTRATOS.ID_CONTRATO='" + idContratoPadre + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);
            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el porcentaje de la promocion");
            }

            if (datos.moveToFirst()){
                porcentajePromocion = Integer.parseInt(datos.getString(0));
            }

            sqLiteDB.close();
            datos.close();
            return porcentajePromocion;

        }catch (SQLiteException e){
            global.escribirError(e, 213);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return porcentajePromocion;
    }

    private double sumarPreciosPromocion(List<Double> preciosPromocion) {
        double precioTotalPromocion = 0;

        for (int i = 0; i < preciosPromocion.size(); i++) {
            precioTotalPromocion += preciosPromocion.get(i);
        }

        return precioTotalPromocion;
    }

    public void abrirHistorialClinico(int i) {

        int estadocontrato = Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"));

        if(estadocontrato == 2 || estadocontrato == 5 || estadocontrato == 12 || estadocontrato == 4) {
            //Estado del contrato es igual a entregado, liquidado o enviado

            if(global.contadorContratosOHistorialesTablaGarantias(ultimoIdContratoCreado, filaHistorialesClinicos.get(i).getIdHistorial(), true) > 0) {
                //Es historial para garantia

                Fragment verificadorFragment = new nuevoHistorialClinico();
                Bundle bundle = new Bundle();
                bundle.putString("ultimoIdContratoCreado", ultimoIdContratoCreado);
                bundle.putString("ultimoIdHistorialClinicoCreado", filaHistorialesClinicos.get(i).getIdHistorial());
                bundle.putBoolean("modificarHistorialClinico", true);
                bundle.putInt("posicionLista", i);
                verificadorFragment.setArguments(bundle);
                FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, verificadorFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.commit();

            }

        }else {
            //Estado contrato es igual a No terminado o Terminado

            if (((obtenerEstadoPromocion() && (Double.parseDouble(obtenerEstadoAtributoContrato("TOTALABONO")) - Double.parseDouble(obtenerEstadoAtributoContrato("TOTALPRODUCTO"))) >
                    (Double.parseDouble(obtenerEstadoAtributoContrato("TOTALPROMOCION")) / 2)) || (!obtenerEstadoPromocion()
                    && (Double.parseDouble(obtenerEstadoAtributoContrato("TOTALABONO")) - Double.parseDouble(obtenerEstadoAtributoContrato("TOTALPRODUCTO"))) >
                    (Double.parseDouble(obtenerEstadoAtributoContrato("TOTALHISTORIAL")) / 2))) && global.contadorHistorialesConGarantia(ultimoIdContratoCreado, false) == 0) {
                             Toast.makeText(fragmento.getActivity(),"Para editar el historial clínico, elimina los abonos agregados.",Toast.LENGTH_LONG).show();
            } else {

                if(global.contadorHistorialesConGarantia(ultimoIdContratoCreado, false) > 0) {

                    if(global.existeHistorialEnTablaGarantias(filaHistorialesClinicos.get(i).getIdHistorial())) {

                        Fragment verificadorFragment = new nuevoHistorialClinico();
                        Bundle bundle = new Bundle();
                        bundle.putString("ultimoIdContratoCreado", ultimoIdContratoCreado);
                        bundle.putString("ultimoIdHistorialClinicoCreado", filaHistorialesClinicos.get(i).getIdHistorial());
                        bundle.putBoolean("modificarHistorialClinico", true);
                        bundle.putInt("posicionLista", i);
                        verificadorFragment.setArguments(bundle);
                        FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, verificadorFragment);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }

                }else {

                    if (obtenerEstadoPromocion()) {

                        if (obtenerEstadoAtributoContrato("PROMOCIONTERMINADA").equals("1")) {

                            Fragment verificadorFragment = new nuevoHistorialClinico();
                            Bundle bundle = new Bundle();
                            bundle.putString("ultimoIdContratoCreado", ultimoIdContratoCreado);
                            bundle.putString("ultimoIdHistorialClinicoCreado", filaHistorialesClinicos.get(i).getIdHistorial());
                            bundle.putBoolean("modificarHistorialClinico", true);
                            bundle.putInt("posicionLista", i);
                            verificadorFragment.setArguments(bundle);
                            FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.content, verificadorFragment);
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }else {
                            Toast.makeText(fragmento.getActivity(),"Para editar el historial clínico, termina la promoción o eliminala.",Toast.LENGTH_LONG).show();
                        }

                    } else {

                        Fragment verificadorFragment = new nuevoHistorialClinico();
                        Bundle bundle = new Bundle();
                        bundle.putString("ultimoIdContratoCreado", ultimoIdContratoCreado);
                        bundle.putString("ultimoIdHistorialClinicoCreado", filaHistorialesClinicos.get(i).getIdHistorial());
                        bundle.putBoolean("modificarHistorialClinico", true);
                        bundle.putInt("posicionLista", i);
                        verificadorFragment.setArguments(bundle);
                        FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, verificadorFragment);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }

                }

            }

        }

    }

    public void guardarDiaPago() {
        if(idsDiaPago[spDiaPago.getSelectedItemPosition()] == "10") {
            Toast.makeText(fragmento.getActivity(), "Favor de seleccionar dia de pago", Toast.LENGTH_LONG).show();
        }else {
            guardarDiaPagoContratoBD();
            historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado, "Se actualizo el dia de pago: " +
                    convertirDiaIngles(Integer.parseInt(idsDiaPago[spDiaPago.getSelectedItemPosition()])), "0");
            llamadaSincronizacion();
            Toast.makeText(fragmento.getActivity(), "Se guardo correctamente el dia de pago", Toast.LENGTH_LONG).show();
        }
    }

    private void guardarDiaPagoContratoBD() {

        try {

            sqLiteDB = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET DIAPAGO='" + convertirDiaIngles(Integer.parseInt(idsDiaPago[spDiaPago.getSelectedItemPosition()]))
                    + "' WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";

            sqLiteDB.execSQL(consulta);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 214);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private String convertirDiaIngles(int i) {

        switch (i) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            case 5:
                return "Saturday";
            case 6:
                return "Sunday";
            default:
                return "";
        }

    }

    private String convertirDiaEspanol(String diaBD) {

        switch (diaBD) {
            case "Monday":
                return "0";
            case "Tuesday":
                return "1";
            case "Wednesday":
                return "2";
            case "Thursday":
                return "3";
            case "Friday":
                return "4";
            case "Saturday":
                return "5";
            case "Sunday":
                return "6";
            default:
                return "10";
         }

    }

    public String obtenerEstadoAtributoContrato(String atributo) {

        String estadoAtributo = "";

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT " + atributo + " FROM CONTRATOS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro el contrato");
            }

            if (datos.moveToFirst()) {
                estadoAtributo = datos.getString(0);
            }

            sqLiteDB.close();
            datos.close();

            return estadoAtributo;

        }catch (SQLiteException e){
            global.escribirError(e, 215);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return estadoAtributo;

    }

    public void mostrarAlertEliminarAbono(int i) {

        String metodoPago = obtenerEstadoAtributoAbono(filaAbonos.get(i).getIdAbono(), "METODOPAGO");

        if(Integer.parseInt(metodoPago) == 1 || Integer.parseInt(metodoPago) == 3) {
            Toast.makeText(fragmento.getActivity(), "No se puede eliminar el abono por que fue pagado con " + global.obtenerMetodoPagoLetra(Integer.parseInt(metodoPago)).toLowerCase(), Toast.LENGTH_LONG).show();
        }else {

            if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("0")
                    || verificarUsuarioYFechaActual(filaAbonos.get(i).getIdAbono(), "ABONOS")) {
                //Estado del contrato es igual a TERMINADO o la fecha del abono es igual a la fecha actual y el usuario es el mismo

                String tipoAbono = obtenerEstadoAtributoAbono(filaAbonos.get(i).getIdAbono(), "TIPOABONO");
                String descripcionAtraso = "";

                if (Integer.parseInt(tipoAbono) == 0) {
                    if (tieneAbonoAtrasado(filaAbonos.get(i).getIdAbono())) {
                        tipoAbono = "atraso";
                        descripcionAtraso = "\nQuedara el contrato con un atraso";
                    } else {
                        tipoAbono = "normal";
                    }
                } else if (Integer.parseInt(tipoAbono) == 1) {
                    tipoAbono = "enganche";
                } else if (Integer.parseInt(tipoAbono) == 2) {
                    tipoAbono = "entrega producto";
                } else if (Integer.parseInt(tipoAbono) == 3) {
                    tipoAbono = "periodo";
                } else if (Integer.parseInt(tipoAbono) == 4) {
                    tipoAbono = "contado enganche";
                } else if (Integer.parseInt(tipoAbono) == 5) {
                    tipoAbono = "contado sin enganche";
                } else if (Integer.parseInt(tipoAbono) == 6) {
                    tipoAbono = "liquidado";
                } else if (Integer.parseInt(tipoAbono) == 7) {
                    tipoAbono = "producto";
                }

                AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                alerta.setTitle("Eliminar abono " + tipoAbono).setMessage("¿Estas seguro de eliminar el abono?" + descripcionAtraso)
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

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (rol.equals("4")) {

                            if (tipoAbono(filaAbonos.get(i).getIdAbono()).equals("0")) {
                                //Tipo abono normal
                                actualizarEstadoAtributoContrato("COSTOATRASO", String.valueOf(Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) +
                                        Double.parseDouble(obtenerEstadoAtributoAbono(filaAbonos.get(i).getIdAbono(), "ATRASO"))));
                                actualizarEstadoAtributoContrato("PAGOSADELANTAR", String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) -
                                        Integer.parseInt(obtenerEstadoAtributoAbono(filaAbonos.get(i).getIdAbono(), "ADELANTOS"))));
                                if (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) > 0) {
                                    if (!validacionEstadosContratoConfirmaciones()) {
                                        //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "4");
                                    }
                                }
                            }
                            if (tipoAbono(filaAbonos.get(i).getIdAbono()).equals("3")) {
                                //Tipo abono periodo
                                actualizarEstadoAtributoContrato("COSTOATRASO", String.valueOf(Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) +
                                        Double.parseDouble(obtenerEstadoAtributoAbono(filaAbonos.get(i).getIdAbono(), "ATRASO"))));
                                actualizarEstadoAtributoContrato("PAGOSADELANTAR", String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) -
                                        Integer.parseInt(obtenerEstadoAtributoAbono(filaAbonos.get(i).getIdAbono(), "ADELANTOS"))));
                                if (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) > 0) {
                                    if (!validacionEstadosContratoConfirmaciones()) {
                                        //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "4");
                                    }
                                }
                            }
                            if (tipoAbono(filaAbonos.get(i).getIdAbono()).equals("5")) {
                                //Tipo abono es de ContadoSinEnganche
                                if (contadorAbonos() == 1) {
                                    if (obtenerBanderaEnganche()) {
                                        restarOSumarEnganche("+", "200");
                                    } else {
                                        restarOSumarEnganche("+", "300");
                                    }
                                    actualizarEstadoAtributoContrato("ENGANCHE", "0");
                                } else {
                                    if (obtenerBanderaContadoEngancheOSinEnganche("4")) {
                                        restarOSumarEnganche("+", "200");
                                    } else {
                                        if (obtenerBanderaEnganche()) {
                                            restarOSumarEnganche("+", "200");
                                        } else {
                                            restarOSumarEnganche("+", "300");
                                        }
                                        actualizarEstadoAtributoContrato("ENGANCHE", "0");
                                    }
                                }
                                if (!validacionEstadosContratoConfirmaciones()) {
                                    //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                    actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                                    actualizarBanderaEntregaProductoYFechaEntrega(false);
                                }
                            }
                            if (tipoAbono(filaAbonos.get(i).getIdAbono()).equals("4")) {
                                //Tipo abono es de ContadoEnganche
                                restarOSumarEnganche("+", "100");
                                if (!obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                    actualizarEstadoAtributoContrato("ENGANCHE", "0");
                                }
                                if (!validacionEstadosContratoConfirmaciones()) {
                                    //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                    actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                                    actualizarBanderaEntregaProductoYFechaEntrega(false);
                                }
                            }
                            if (tipoAbono(filaAbonos.get(i).getIdAbono()).equals("6")) { //Liquidado
                                if (obtenerEstadoPromocion()) { //Tiene promocion
                                    if (!obtenerEstadoAtributoContrato("PAGO").equals("0")) { //Forma de pago semanal, quincenal o mensual
                                        if (!existeAbonoEntregaProducto()) {
                                            actualizarBanderaEntregaProductoYFechaEntrega(false);
                                        }
                                        if (!validacionEstadosContratoConfirmaciones()) {
                                            //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                            if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {
                                                actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                                            } else {
                                                actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                            }
                                        }
                                    } else { //Forma pago contado
                                        if (!validacionEstadosContratoConfirmaciones()) {
                                            //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                                            actualizarBanderaEntregaProductoYFechaEntrega(false);
                                        }
                                    }
                                } else { //No tiene promocion
                                    actualizarEstadoAtributoContrato("COSTOATRASO", String.valueOf(Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) +
                                            Double.parseDouble(obtenerEstadoAtributoAbono(filaAbonos.get(i).getIdAbono(), "ATRASO"))));
                                    if (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) > 0) {
                                        if (!validacionEstadosContratoConfirmaciones()) {
                                            //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                            actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "4");
                                        }
                                    } else {
                                        if (!existeAbonoEntregaProducto()) {
                                            actualizarBanderaEntregaProductoYFechaEntrega(false);
                                        }
                                        if (obtenerEstadoAtributoContrato("ENTREGAPRODUCTO").equals("0")) {
                                            if (!validacionEstadosContratoConfirmaciones()) {
                                                //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                                actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                                            }
                                        } else {
                                            if (global.contadorHistorialesConGarantia(ultimoIdContratoCreado, true) > 0 && Integer.parseInt(obtenerEstadoAtributoContrato("PAGO")) == 0) {
                                                //Tiene historiales con garantia y forma de pago es de contado
                                                if (!validacionEstadosContratoConfirmaciones()) {
                                                    //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                                    actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                                                }
                                            } else {
                                                //No tiene historiales con garantia
                                                if (obtenerEstadoAtributoContrato("PAGO").equals("0") && obtenerTotalContrato() < 400 && !obtenerBanderaContadoEngancheOSinEnganche("4")
                                                        && !obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                                    //FORMA DE PAGO ES DE CONTADO Y TOTALCONTRATO ES MENOR A 400 Y NO SE TIENE EL ABONO CONTADOENGACHE
                                                    // NI TAMPOCO EL CONTADOSINENGANCHE (CUANDO ES UNA POLIZA DE SEGURO Y ES SEMANAL, QUICENAL O MENSUAL Y SE PASA A CONTADO)
                                                    if (!validacionEstadosContratoConfirmaciones()) {
                                                        //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                                                    }
                                                } else {
                                                    //FORMA DE PAGO ES DIFERENTE DE CONTADO O TOTALCONTRATO ES MAYOR A 400 O SE TIENE EL ABONO CONTADOENGACHE O SE TIENE EL CONTADOSINENGANCHE
                                                    if (!validacionEstadosContratoConfirmaciones()) {
                                                        //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                                                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "2");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) >= 1 && Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO")) < 12
                                    && tipoAbono(filaAbonos.get(i).getIdAbono()).equals("2")) {
                                Toast.makeText(fragmento.getActivity(), "No se puede eliminar el abono", Toast.LENGTH_LONG).show();
                            } else {

                                if (tipoAbono(filaAbonos.get(i).getIdAbono()).equals("2")) {
                                    //Tipo abono entrega producto
                                    actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "12");
                                    actualizarBanderaEntregaProductoYFechaEntrega(false);
                                }

                                eliminarAbono(filaAbonos.get(i).getIdAbono(), filaAbonos.get(i).getFolioAbono());
                                Toast.makeText(fragmento.getActivity(), "Se elimino correctamente el abono", Toast.LENGTH_LONG).show();
                                obtenerListaAbonos();
                                calculoTotal();
                                obtenerPrecioDescuentoFormaPagoContado();

                                llamadaSincronizacion();

                                obtenerEstadoContratoYMostrarTextView();

                                mostrarOcultarLlPromocionYFormaPago();
                                dialog.dismiss();
                            }

                        } else { //Asistente/Opto

                            if (global.obtenerAtributoTabla("ABONOS", "POLIZA", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", filaAbonos.get(i).getIdAbono()).length() > 0) {
                                //Poliza es diferente de vacio
                                Toast.makeText(fragmento.getActivity(), "Ya venció el tiempo para eliminar el abono", Toast.LENGTH_LONG).show();
                            }else {
                                //Poliza es igual a vacio

                                if (tipoAbono(filaAbonos.get(i).getIdAbono()).equals("1")) {
                                    //Tipo abono es de enganche 1
                                    actualizarEstadoAtributoContrato("ENGANCHE", "0");
                                    restarOSumarEnganche("+", "100");
                                }
                                if (tipoAbono(filaAbonos.get(i).getIdAbono()).equals("5")) {
                                    //Tipo abono es de ContadoSinEnganche
                                    if (contadorAbonos() == 1) {
                                        if (obtenerBanderaEnganche()) {
                                            restarOSumarEnganche("+", "200");
                                        } else {
                                            restarOSumarEnganche("+", "300");
                                        }
                                        actualizarEstadoAtributoContrato("ENGANCHE", "0");
                                    } else {
                                        if (obtenerBanderaContadoEngancheOSinEnganche("4")) {
                                            restarOSumarEnganche("+", "200");
                                        } else {
                                            if (obtenerBanderaEnganche()) {
                                                restarOSumarEnganche("+", "200");
                                            } else {
                                                restarOSumarEnganche("+", "300");
                                            }
                                            actualizarEstadoAtributoContrato("ENGANCHE", "0");
                                        }
                                    }
                                }
                                if (tipoAbono(filaAbonos.get(i).getIdAbono()).equals("4")) {
                                    //Tipo abono es de ContadoEnganche
                                    restarOSumarEnganche("+", "100");
                                    if (!obtenerBanderaContadoEngancheOSinEnganche("5")) {
                                        actualizarEstadoAtributoContrato("ENGANCHE", "0");
                                    }
                                }

                                if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("1") && tipoAbono(filaAbonos.get(i).getIdAbono()).equals("7")) {
                                    Toast.makeText(fragmento.getActivity(), "No se puede eliminar el abono", Toast.LENGTH_LONG).show();
                                } else {

                                    if (global.obtenerAtributoTabla("ABONOS", "TIPOABONO", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", filaAbonos.get(i).getIdAbono()).equals("7")) {
                                        //Fue un abono de tipo producto (Eliminar producto tambien)

                                        //Obtener idcontratoproducto
                                        String idcontratoproductoabono = global.obtenerAtributoTabla("ABONOS", "ID_CONTRATOPRODUCTO", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", filaAbonos.get(i).getIdAbono());
                                        if (idcontratoproductoabono.length() > 0) {
                                            //Eliminar contrato producto
                                            String idproductocontratoproducto = global.obtenerAtributoTabla("CONTRATOPRODUCTO", "ID_PRODUCTO", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", idcontratoproductoabono);
                                            String piezascontratoproducto = global.obtenerAtributoTabla("CONTRATOPRODUCTO", "PIEZAS", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", idcontratoproductoabono);
                                            eliminarProductoContrato(idcontratoproductoabono, idproductocontratoproducto, piezascontratoproducto);
                                            decrementarOAumentarProductoBD(idproductocontratoproducto, "+", piezascontratoproducto);
                                            historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado, "Se elimino: " +
                                                    filaAbonos.get(i).getAbono() + " de producto", "0");
                                            obtenerListaProductos();
                                            productosPagados();
                                        }
                                    }

                                    eliminarAbono(filaAbonos.get(i).getIdAbono(), filaAbonos.get(i).getFolioAbono());

                                    Toast.makeText(fragmento.getActivity(), "Se elimino correctamente el abono", Toast.LENGTH_LONG).show();
                                    obtenerListaAbonos();
                                    calculoTotal();

                                    llamadaSincronizacion();

                                    obtenerEstadoContratoYMostrarTextView();

                                    mostrarOcultarLlPromocionYFormaPago();
                                    dialog.dismiss();
                                }

                            }

                        }

                    }
                });

            } else {
                Toast.makeText(fragmento.getActivity(), "No se puede eliminar el abono", Toast.LENGTH_LONG).show();
            }

        }
    }

    private boolean validacionEstadosContratoConfirmaciones() {
        boolean respuesta = true;

        int estadoContratoActual = Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"));

        if(estadoContratoActual != 1 && estadoContratoActual != 7 && estadoContratoActual != 9 && estadoContratoActual != 10 && estadoContratoActual != 11) {
            respuesta = false;
        }

        return respuesta;
    }

    private boolean existeAbonoEntregaProducto() {

        boolean respuestaBoleana = false;

        try{
            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID" +
                    " FROM ABONOS WHERE TIPOABONO = '2' AND ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()){
                respuestaBoleana = true;
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 216);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return respuestaBoleana;
    }

    private String obtenerEstadoAtributoAbono(String idAbono, String atributo) {

        String atributoAbono = "";

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT " + atributo + " FROM ABONOS WHERE ID_CONTRATO= '" + ultimoIdContratoCreado + "' AND ID = '" + idAbono + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.moveToFirst()) {
                atributoAbono = datos.getString(0);
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 217);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return atributoAbono;
    }

    private int contadorAbonos() {

        int numAbonos = 0;

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT COUNT(ID) FROM ABONOS WHERE ID_CONTRATO= '" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if(datos.moveToFirst()) {
                numAbonos = datos.getInt(0);
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 218);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return numAbonos;

    }

    private boolean verificarUsuarioYFechaActual(String id, String atributo) {

        try{
            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID_USUARIO, CREATED_AT FROM " + atributo + " WHERE ID='" + id + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No hay abono");
            }

            if(datos.moveToFirst()){

                if(datos.getString(0).equals(idUsuario)) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    try {

                        Date date = simpleDateFormat.parse(datos.getString(1));
                        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        if(obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL").equals(convetDateFormat.format(date))) {
                            return true;
                        }else {
                            return false;
                        }

                    } catch (ParseException e) {
                        global.escribirError(e, 219);
                        e.printStackTrace();
                    }

                }else {
                    return false;
                }

            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 220);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return false;
    }

    private void eliminarAbono(String idAbono, String folio) {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        if(rol.equals("4")) {
            //Rol cobranza

            guardarAbonoOProductoEliminado("ABONOSELIMINADOS", "ID_ABONO", folio, "", "");

            Object[] respuesta = obtenerAbonosMedianteFolio(idAbono, folio);
            List<String> abonos = (List<String>) respuesta[0];
            double atraso = (double) respuesta[1];
            int adelantos = (int) respuesta[2];
            List<String> idsabonos = (List<String>) respuesta[3];

            for (int i = 0; i < abonos.size(); i++) {
                //Guardar en archivo txt
                String coordenadasAbonoTxt = global.obtenerAtributoTabla("ABONOS", "COORDENADAS", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", idsabonos.get(i));
                guardarMovimientoEnArchivoTxt(fechaActual + time, "Eliminado", abonos.get(i), "", folio, idsabonos.get(i), "", "", "", coordenadasAbonoTxt);
                //Guardar historialcontrato
                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado, "Se elimino: " + abonos.get(i) + " de abono", "0");
            }

            global.eliminarTablaORegistroTabla("ABONOS", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND FOLIO", folio);

            if(abonos.size() == 2) {
                //Fueron 2 abonos con el mismo folio
                actualizarEstadoAtributoContrato("COSTOATRASO", String.valueOf(Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) + atraso));
                actualizarEstadoAtributoContrato("PAGOSADELANTAR", String.valueOf(Integer.parseInt(obtenerEstadoAtributoContrato("PAGOSADELANTAR")) - adelantos));
                if (Double.parseDouble(obtenerEstadoAtributoContrato("COSTOATRASO")) > 0) {
                    if (!validacionEstadosContratoConfirmaciones()) {
                        //Si estado del contrato es diferente a TERMINADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                        actualizarEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO", "4");
                    }
                }
            }

        }else {
            //Rol asist/opto

            String abono = global.obtenerAtributoTabla("ABONOS", "ABONO", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", idAbono);
            String metodopago = global.obtenerAtributoTabla("ABONOS", "METODOPAGO", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", idAbono);
            String tipoabono = global.obtenerAtributoTabla("ABONOS", "TIPOABONO", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", idAbono);
            String coordenadasAbonoTxt = global.obtenerAtributoTabla("ABONOS", "COORDENADAS", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", idAbono);

            //Guardar en archivo txt
            guardarMovimientoEnArchivoTxt(fechaActual + time, "Eliminado", abono, metodopago, "", idAbono, "0", tipoabono, "0", coordenadasAbonoTxt);
            //Guardar historialcontrato
            historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado, "Se elimino: " + abono + " de abono", "0");

            guardarAbonoOProductoEliminado("ABONOSELIMINADOS", "ID_ABONO", idAbono, "", "");

            global.eliminarTablaORegistroTabla("ABONOS", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID", idAbono);

        }

        actualizarEstadoAtributoContrato("ULTIMOABONO", obtenerFechaUltimoAbonoDadoEnContrato());

    }

    private Object[] obtenerAbonosMedianteFolio(String idAbono, String folio) {

        List<String> abonos = new ArrayList<>();
        List<String> idsabonos = new ArrayList<>();
        double atraso = 0;
        int adelantos = 0;

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID, ABONO, ATRASO, ADELANTOS FROM ABONOS WHERE FOLIO = '" + folio + "' AND ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            while (datos.moveToNext()) {
                if(!datos.getString(0).equals(idAbono)) {
                    //El idAbono es diferente al que se eligio
                    if(datos.getDouble(2) > 0) {
                        //El abono tiene atraso
                        atraso = datos.getDouble(2);
                    }
                    adelantos = datos.getInt(3);
                }
                abonos.add(datos.getString(1));
                idsabonos.add(datos.getString(0));
            }

            sqLiteDB2.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 221);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        Object[] respuesta = {abonos, atraso, adelantos, idsabonos};

        return respuesta;
    }

    private String obtenerFechaUltimoAbonoDadoEnContrato() {

        String ultimoAbono = "";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT CREATED_AT FROM ABONOS WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "' ORDER BY CREATED_AT DESC LIMIT 1";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.moveToFirst()){
                ultimoAbono =  datos.getString(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 222);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return ultimoAbono;

    }

    private String tipoAbono(String idAbono) {

        String tipoAbono = "";

        try{
            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT TIPOABONO FROM ABONOS WHERE ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID='" + idAbono + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No hay abono");
            }

            if(datos.moveToFirst()){
                tipoAbono = datos.getString(0);
                return tipoAbono;
            }

            sqLiteDB.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 223);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return tipoAbono;
    }

    public void mostrarAlertEliminarProducto(int i) {

        if(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("0")) {

            if(verificarSiProductoFuePagadoConTarjeta()) {
                //Productos fue pagado con tarjeta
                Toast.makeText(fragmento.getActivity(), "No se puede eliminar el producto por que fue pagado con tarjeta", Toast.LENGTH_LONG).show();
            }else {
                //Productos no fue pagado con tarjeta

                AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                alerta.setTitle("Eliminar producto").setMessage("¿Estas seguro de eliminar el producto " + filaProductos.get(i).getProducto() + "?")
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

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("0")
                                || verificarUsuarioYFechaActual(filaProductos.get(i).getIdProductoContrato(), "CONTRATOPRODUCTO")) {
                            //Estado del contrato es igual a TERMINADO o la fecha del abono es igual a la fecha actual y el usuario es el mismo

                            //Eliminar abono (SOLO PUEDEN ASISTENTES Y OPTOS por eso no se obtiene folio)
                            String polizaabono = global.obtenerAtributoTabla("ABONOS", "POLIZA", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID_CONTRATOPRODUCTO", filaProductos.get(i).getIdProductoContrato());

                            if (polizaabono.length() > 0) {
                                //Poliza es diferente de vacio
                                Toast.makeText(fragmento.getActivity(), "Ya venció el tiempo para eliminar el abono", Toast.LENGTH_LONG).show();
                            }else {
                                //Poliza es igual a vacio

                                String idabono = global.obtenerAtributoTabla("ABONOS", "ID", "ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID_CONTRATOPRODUCTO", filaProductos.get(i).getIdProductoContrato());
                                eliminarAbono(idabono, "");
                                obtenerListaAbonos();

                                eliminarProductoContrato(filaProductos.get(i).getIdProductoContrato(), filaProductos.get(i).getIdProducto(), filaProductos.get(i).getPiezas());
                                decrementarOAumentarProductoBD(filaProductos.get(i).getIdProducto(), "+", filaProductos.get(i).getPiezas());
                                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado, "Se elimino: " +
                                        (Double.parseDouble(filaProductos.get(i).getPrecio()) * Integer.parseInt(filaProductos.get(i).getPiezas())) + " de producto", "0");
                                Toast.makeText(fragmento.getActivity(), "Se elimino correctamente el producto", Toast.LENGTH_LONG).show();
                                obtenerListaProductos();
                                productosPagados();
                                calculoTotal();

                                llamadaSincronizacion();

                                obtenerEstadoContratoYMostrarTextView();
                                mostrarOcultarLlPromocionYFormaPago();

                                dialog.dismiss();

                            }

                        } else {
                            Toast.makeText(fragmento.getActivity(), "No se puede eliminar el producto", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

        }

    }

    private boolean verificarSiProductoFuePagadoConTarjeta() {

        boolean respuesta = false;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID FROM ABONOS WHERE ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND TIPOABONO = '7' AND METODOPAGO = '1'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()) {
                respuesta = true;
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 224);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return respuesta;

    }

    private void eliminarProductoContrato(String idProductoContrato, String idProducto, String piezas) {

        guardarAbonoOProductoEliminado("PRODUCTOSELIMINADOS", "ID_CONTRATOPRODUCTO", idProductoContrato, idProducto, piezas);

        try{

            sqLiteDB = conexion.getWritableDatabase();
            String SQL = "DELETE FROM CONTRATOPRODUCTO WHERE ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ID = '" + idProductoContrato + "'";
            sqLiteDB.execSQL(SQL);
            sqLiteDB.close();

        }catch (SQLiteException e){
            global.escribirError(e, 225);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

    }

    public void mostrarAlertDialogObservacion(int i) {

        if(obtenerObservacionHistorialClinico(filaHistorialesClinicos.get(i).getIdHistorial()).length() != 0) {

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle("Observaciones").setMessage(obtenerObservacionHistorialClinico(filaHistorialesClinicos.get(i).getIdHistorial()))
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Debe ir vacio por que se esta obteniendo abajo
                        }
                    });

            final AlertDialog dialog = alerta.create();

            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }

    }

    public void mostrarAlertDialogObservacion2(int i) {

        if(obtenerObservacionHistorialClinico(filaHistorialesClinicos2.get(i).getIdHistorial()).length() != 0) {

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle("Observaciones").setMessage(obtenerObservacionHistorialClinico(filaHistorialesClinicos2.get(i).getIdHistorial()))
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Debe ir vacio por que se esta obteniendo abajo
                        }
                    });

            final AlertDialog dialog = alerta.create();

            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }

    }

    private String obtenerObservacionHistorialClinico(String idHistorial) {

        String observaciones = "";

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT OBSERVACIONES, OBSERVACIONESINTERNO FROM HISTORIALCLINICO WHERE ID='" + idHistorial + "'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No hay atributo en el historial");
            }

            if (datos.moveToFirst()){
                if(datos.getString(0).length() > 0) {
                    observaciones = "- Observaciones laboratorio: " + datos.getString(0) + "\n";
                }
                if(datos.getString(1).length() > 0) {
                    observaciones += "- Observaciones interno: " + datos.getString(1);
                }
            }

            sqLiteDB.close();
            datos.close();
            return observaciones;

        }catch (SQLiteException e){
            global.escribirError(e, 226);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return observaciones;
    }

    private void guardarAbonoOProductoEliminado(String tabla, String atributo, String id, String idProducto, String piezas) {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try {

            sqLiteDB = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID_CONTRATO", ultimoIdContratoCreado);
            valores.put(atributo, id);
            if(tabla.equals("PRODUCTOSELIMINADOS")) {
                valores.put("ID_PRODUCTO", idProducto);
                valores.put("PIEZAS", piezas);
            }
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT",  fechaActual + time);
            sqLiteDB.insert(tabla,null, valores);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 227);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void llamadaSincronizacion() {
        actualizarEstadoAtributoContrato("ENVIADOPAGINA", "0");
        sincronizacion.sincronizarMetodo(0, objetosWebService, 0);
    }

    private void obtenerEstadoContratoYMostrarTextView() {

        int estadoContrato = Integer.parseInt(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO"));

        switch (estadoContrato) {
            case 0: //NO TERMINADO
                tvEstadoContrato.setBackgroundColor(Color.parseColor("#6c757d"));
                tvEstadoContrato.setTextColor(Color.parseColor("#ffffff"));
                tvEstadoContrato.setText("No terminado");
                break;
            case 1: //TERMINADO
                if(rol.equals("4")) {
                    //COBRANZA
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#F48FB1"));
                    tvEstadoContrato.setText("Garantía");
                }else {
                    //ASIS/OPTO
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#5bc0de"));
                    tvEstadoContrato.setText("Terminado");
                }
                tvEstadoContrato.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 2: //ENTREGADO
                if(rol.equals("4")) {
                    //COBRANZA
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#0275d8"));
                    tvEstadoContrato.setText("Entregado");
                }else {
                    //ASIS/OPTO
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#F48FB1"));
                    tvEstadoContrato.setText("Garantía");
                }
                tvEstadoContrato.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 4: //ABONO ATRASADO
                if(rol.equals("4")) {
                    //COBRANZA
                    int diasatrasados = obtenerDiasAtrasados(obtenerEstadoAtributoContrato("FECHAATRASO"));
                    if (diasatrasados <= 10) {
                        //1-10 dias
                        tvEstadoContrato.setBackgroundColor(Color.parseColor("#fff2cc"));
                        tvEstadoContrato.setTextColor(Color.parseColor("#000000"));
                    } else {
                        if (diasatrasados <= 20) {
                            //11-20 dias
                            tvEstadoContrato.setBackgroundColor(Color.parseColor("#fce5cd"));
                            tvEstadoContrato.setTextColor(Color.parseColor("#000000"));
                        } else {
                            if (diasatrasados > 20) {
                                //20 dias en delante
                                tvEstadoContrato.setBackgroundColor(Color.parseColor("#f4cccc"));
                                tvEstadoContrato.setTextColor(Color.parseColor("#000000"));
                            }
                        }
                    }
                    tvEstadoContrato.setText("Abono atrasado");
                }else {
                    //ASIS/OPTO
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#F48FB1"));
                    tvEstadoContrato.setText("Garantía");
                }
                break;
            case 5: //LIQUIDADO
                if(rol.equals("4")) {
                    //COBRANZA
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#5cb85c"));
                    tvEstadoContrato.setText("Pagado");
                }else {
                    //ASIS/OPTO
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#F48FB1"));
                    tvEstadoContrato.setText("Garantía");
                }
                tvEstadoContrato.setTextColor(Color.parseColor("#000000"));
                break;
            case 7:
            case 9:
            case 10:
            case 11: //APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                if(rol.equals("4")) {
                    //COBRANZA
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#F48FB1"));
                    tvEstadoContrato.setText("Garantía");
                    tvEstadoContrato.setTextColor(Color.parseColor("#000000"));
                }
                break;
            case 12: //ENVIADO
                if(rol.equals("4")) {
                    //COBRANZA
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#5bc0de"));
                    tvEstadoContrato.setText("Enviado");
                }else {
                    //ASIS/OPTO
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#F48FB1"));
                    tvEstadoContrato.setText("Garantía");
                }
                tvEstadoContrato.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 6: //RECHAZADO
                if(rol.equals("4")) {
                    //COBRANZA
                    tvEstadoContrato.setBackgroundColor(Color.parseColor("#ff0000"));
                    tvEstadoContrato.setText("Cancelado");
                    tvEstadoContrato.setTextColor(Color.parseColor("#000000"));
                }
                break;
        }

    }

    /*Metodo/Funcion: obtenerDiasAtrasados
      Descripcion: Se obtiene los dias de atraso del contrato que tiene abonos atrasados
    */
    private int obtenerDiasAtrasados(String fechaatrasoBD) {

        int workDays = 0;

        if(fechaatrasoBD.length() > 0) {
            //fechaatraso diferente de vacio

            Date fechaactual = new Date();
            Date fechaatraso = new Date();

            try {
                //Cambiar formato fechaatraso proviniente de la BD a yyyy-MM-dd
                SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                fechaactual = convetDateFormat.parse(obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL"));
                fechaatraso = convetDateFormat.parse(fechaatrasoBD);

            } catch (ParseException e) {
                global.escribirError(e, 228);
                e.printStackTrace();
            }


            Calendar startCal = Calendar.getInstance();
            startCal.setTime(fechaatraso);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(fechaactual);

            if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
                //fechaatraso es igual a la fechaactual
                return 0;
            }

            if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
                startCal.setTime(fechaactual);
                endCal.setTime(fechaatraso);
            }

            do {
                //Conteo de dias quitando los Domingos de cada semana
                startCal.add(Calendar.DAY_OF_MONTH, 1);
                if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    ++workDays;
                }

            } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

        }

        return workDays;
    }

    public void mostrarAlertDialogEliminarPromocion() {

        if(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("0")) {

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle("Eliminar promoción").setMessage("¿Estas seguro de eliminar la promoción?")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            guardarPromocionEliminada(obtenerIdPromocionContrato());
                            eliminarPromocion();
                            actualizarEstadoAtributoContrato("ID_PROMOCION", "");
                            cambioStatusPromocion = false;
                            llListaPromociones.setVisibility(View.GONE);
                            btnNuevaPromocion.setVisibility(View.VISIBLE);
                            mostrarOcultarLlPromocionYFormaPago();
                            historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                    "Se elimino la promoción", "0");
                            Toast.makeText(fragmento.getActivity(), "Se elimino correctamente la promoción", Toast.LENGTH_LONG).show();

                            llamadaSincronizacion();
                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();

        }

    }

    private String obtenerIdPromocionContrato() {

        String idPromocionContrato = "";

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID FROM PROMOCIONCONTRATO WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.moveToFirst()){
                idPromocionContrato =  datos.getString(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 229);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return idPromocionContrato;
    }

    private void guardarPromocionEliminada(String idPromocionContrato) {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try {

            sqLiteDB = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("ID_CONTRATO", ultimoIdContratoCreado);
            valores.put("ID_PROMOCIONCONTRATO", idPromocionContrato);
            valores.put("ENVIADOPAGINA", "0");
            valores.put("CREATED_AT",  fechaActual + time);
            sqLiteDB.insert("PROMOCIONESELIMINADAS",null, valores);
            sqLiteDB.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 230);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void eliminarPromocion() {
        try{

            sqLiteDB = conexion.getWritableDatabase();
            String SQL = "DELETE FROM PROMOCIONCONTRATO WHERE ID_CONTRATO='" + ultimoIdContratoCreado + "'";
            sqLiteDB.execSQL(SQL);
            sqLiteDB.close();

        }catch (SQLiteException e){
            global.escribirError(e, 231);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }
    }

    public boolean existeIdContratoEnTablaImagenesCargadas(String idContrato) {

        boolean existe = true;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();

            String SQL = "SELECT ID_CONTRATO FROM IMAGENESCARGADASCONTRATOS WHERE ID_CONTRATO = '" + idContrato + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                existe = false;
            }

            datos.close();
            sqLiteDB2.close();

        }catch (SQLiteException e){
            global.escribirError(e, 232);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return existe;

    }

    public void mostrarAlertDialogReportarGarantia() {

        if(obtenerEstadoAtributoContrato("ESTATUS_ESTADOCONTRATO").equals("6")) {
            //Estado del contrato cancelado
            Toast.makeText(fragmento.getActivity(), "El contrato se encuentra cancelado, ya no se puede reportar garantia", Toast.LENGTH_LONG).show();
        }else {
            //Estado del contrato diferente de cancelado

            final EditText etEspecificacion = new EditText(fragmento.getActivity());
            etEspecificacion.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
            etEspecificacion.setHint("Especificación");

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle("¿Deseas reportar la garantia del contrato " + ultimoIdContratoCreado + "?").setMessage("Específica porque")
                    .setView(etEspecificacion).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (etEspecificacion.getText().toString().length() == 0) {
                        //Campo especificacion vacio
                        Toast.makeText(fragmento.getActivity(), "Campo vacio", Toast.LENGTH_LONG).show();
                    } else {
                        //Campo especificacion diferente de vacio

                        //Guardar en historial de movimientos
                        historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                "Se reporto garantia: " + etEspecificacion.getText().toString(), "0");

                        String time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

                        String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("GARANTIAS", 5);

                        try {

                            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
                            ContentValues valores = new ContentValues();
                            valores.put("ID", idAlfanumerico);
                            valores.put("ID_CONTRATO", ultimoIdContratoCreado);
                            valores.put("ID_HISTORIAL", "");
                            valores.put("ID_HISTORIALGARANTIA", "");
                            valores.put("ID_OPTOMETRISTA", "");
                            valores.put("ESTADOGARANTIA", "0");
                            valores.put("ENVIADOPAGINA", "0");
                            valores.put("CREATED_AT", fechaActual + time);
                            valores.put("UPDATED_AT", "");
                            sqLiteDB2.insert("GARANTIAS", null, valores);
                            sqLiteDB2.close();

                        } catch (SQLiteException e) {
                            global.escribirError(e, 233);
                            Log.i("ERRORBD", e.getMessage() + "");
                        }

                        mostrarReportarGarantia();

                        //Sincronizar
                        llamadaSincronizacion();

                        Toast.makeText(fragmento.getActivity(), "Se reporto correctamente garantia del contrato", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                }
            });

        }
    }

    public void mostrarAlertOpcionAbonoCobranza(int i) {

        if(verificarUsuarioYFechaActual(filaAbonos.get(i).getIdAbono(), "ABONOS")) {
            //Si la fecha del abono es igual a la fecha actual y el usuario es el mismo

            //Log.i("MENSAJE", "Folio: " + filaAbonos.get(i).getFolioAbono());

            String[] opciones = new String[]{"Mandar a imprimir", "Compartir ticket"};

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle("Elegir una opción").setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int j) {



                        switch (j) {
                            case 0:
                                //Mandar a imprimir
                                if(llaves.impresora_termica) {
                                    //Se utilizara impresora termica
                                    if (impresoraBluetooth.findBluetoothDevice()) {
                                        //Impresora conectada correctamente
                                        String totalabonosImprimir = "0";
                                        if (!filaAbonos.get(i).getTipoAbono().equals("Producto")) {
                                            //Abono de tipo PRODUCTO
                                            totalabonosImprimir = obtenerSumaAbono(filaAbonos.get(i).getFolioAbono());
                                        }
                                        //Obtener totalanterior para mandarlo al ticket antes que se haga el calculo
                                        totalanterior = String.valueOf(obtenerTotalContrato() + Integer.parseInt(totalabonosImprimir));
                                        if (totalanterior.indexOf(".") > 0) {
                                            //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
                                            totalanterior = totalanterior.substring(0, totalanterior.indexOf("."));
                                        }

                                        //Guardar historialcontrato
                                        historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                                "Impresión de ticket con folio: " + filaAbonos.get(i).getFolioAbono(), "0");
                                        //Imprimir ticket
                                        mandarAImprimirTicketImpresoraBluetooth(filaAbonos.get(i).getFolioAbono());

                                        if (filaAbonos.get(i).getTipoAbono().equals("Entrega producto") || filaAbonos.get(i).getTipoAbono().equals("C SN/Enganche")) {
                                            //Abono seleccionado es de entrega de producto o contado sin enganche
                                            mandarAImprimirTicketEntregaProductoImpresora();
                                        }
                                    }
                                }
                                break;
                            case 1:
                                //Compartir ticket

                                String totalabonosImprimir = "0";
                                if (!filaAbonos.get(i).getTipoAbono().equals("Producto")) {
                                    //Abono de tipo PRODUCTO
                                    totalabonosImprimir = obtenerSumaAbono(filaAbonos.get(i).getFolioAbono());
                                }
                                //Obtener totalanterior para mandarlo al ticket antes que se haga el calculo
                                totalanterior = String.valueOf(obtenerTotalContrato() + Integer.parseInt(totalabonosImprimir));
                                if (totalanterior.indexOf(".") > 0) {
                                    //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
                                    totalanterior = totalanterior.substring(0, totalanterior.indexOf("."));
                                }

                                desplegarDialogCompartirTicketAbono(filaAbonos.get(i).getFolioAbono());
                                /*
                                //Eliminar abono
                                if (filaAbonos.get(i).getTipoAbono().equals("Producto")) {
                                    //Abono de tipo PRODUCTO
                                    Toast.makeText(fragmento.getActivity(), "No se pueden eliminar abonos de tipo producto", Toast.LENGTH_LONG).show();
                                }else {
                                    //Abono diferente a tipo PRODUCTO
                                    //mostrarAlertEliminarAbono(i);
                                }
                                 */
                                break;
                        }

                }
            }).create().show();

        }else {
            Toast.makeText(fragmento.getActivity(), "No se puede hacer ninguna acción en el abono", Toast.LENGTH_LONG).show();
        }

    }

    public void mostrarAlertOpcionAbonoVentas(int i) {

        if(verificarUsuarioYFechaActual(filaAbonos.get(i).getIdAbono(), "ABONOS")) {

            String[] opciones = new String[]{"Compartir ticket", "Eliminar abono"};

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle("Elegir una opción").setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int j) {

                    switch (j) {
                        case 0:
                            //Compartir ticket
                            String totalabonosImprimir = "0";
                            if (!filaAbonos.get(i).getTipoAbono().equals("Producto")) {
                                //Abono de tipo PRODUCTO
                                totalabonosImprimir = obtenerSumaAbono(filaAbonos.get(i).getFolioAbono());
                            }
                            //Obtener totalanterior para mandarlo al ticket antes que se haga el calculo
                            totalanterior = String.valueOf(obtenerTotalContrato() + Integer.parseInt(totalabonosImprimir));
                            if (totalanterior.indexOf(".") > 0) {
                                //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
                                totalanterior = totalanterior.substring(0, totalanterior.indexOf("."));
                            }

                            desplegarDialogCompartirTicketAbono(filaAbonos.get(i).getFolioAbono());
                            break;
                        case 1:
                            //Eliminar abono
                            mostrarAlertEliminarAbono(i);
                            break;
                    }

                }
            }).create().show();

        }else {
            Toast.makeText(fragmento.getActivity(), "No se puede hacer ninguna acción en el abono", Toast.LENGTH_LONG).show();
        }

    }

    private void mandarAImprimirTicketEntregaProductoImpresora() {
        try {
            //Thread.sleep(1000); // Le damos tiempo a la impresora de imprimir el otro ticket.
            time = new SimpleDateFormat(" HH:mm:ss").format(new Date());
            if (impresoraBluetooth.findBluetoothDevice()) {
                impresoraBluetooth.verificarSiBluetoothPrinterEstaCerrado();
                Object[] objetos = {obtenerRol.obtenerAtributoUsuarioLogeado("SUCURSAL"), obtenerEstadoAtributoContrato("NOMBRE"), ultimoIdContratoCreado,
                        fechaActual + time, obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO"),
                        obtenerRol.obtenerAtributoUsuarioLogeado("TELEFONOATENCIONCLIENTESSUCURSAL"),
                        obtenerRol.obtenerAtributoUsuarioLogeado("WHATSAPP")};
                impresoraBluetooth.printData(objetos, 3);
                impresoraBluetooth.closedBluetoothPrinter();
            }
        } catch (Exception e) {
            global.escribirError(e, 264);
            e.printStackTrace();
        }
    }

    //Metodo agregarCoordenadasAContrato
    //Descripcion: Actualizar campo coordenadas de un contrato en particular
    public void agregarCoordenadasAContrato(){

        if(localizacion.tienePermisos()) {
            //Permisos para localizacion
            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle("¿Deseas actualizar la ubicación?").setMessage("Es recomendable que estes en el domicilio del cliente o donde se realizara el cobro de los abonos")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Debe ir vacio por que se esta obteniendo abajo
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                        runnable2 = new Runnable() {
                            @Override
                            public void run() {
                                if (localizacion.getLatitud() != null) {
                                    //si latitud es diferente a vacio

                                    //Actualizamos coordenadas
                                    actualizarEstadoAtributoContrato("COORDENADAS", localizacion.getLatitud() + "," + localizacion.getLongitud());
                                    //Cambiar color de icono
                                    cambiarColorIconoUbicacion();
                                    //Llamar sincronizacion
                                    llamadaSincronizacion();
                                    Toast.makeText(fragmento.getActivity(), "Se actualizo la ubicación correctamente", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    handler.removeCallbacks(runnable2);
                                } else {
                                    handler.postDelayed(runnable2, 2000);
                                }
                            }
                        };
                        runnable2.run();

                    }
                }
            });
        }

    }

    public void imprimirRegistroCliente() {

        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("¿Imprimir registro de visita?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Debe ir vacio por que se esta obteniendo abajo
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = alerta.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fechaYHoraUltimoRegistro = global.obtenerUnResultadoQuery("SELECT CREATED_AT FROM HISTORIALFOTOSCONTRATO WHERE ID_CONTRATO = '" + ultimoIdContratoCreado + "' ORDER BY CREATED_AT DESC LIMIT 1");

                if (fechaYHoraUltimoRegistro.equals("")) {
                    //No hubo resultado de algun registro de movimiento de evidencia
                    Toast.makeText(fragmento.getActivity(), "No se encontro ninguna evidencia en el contrato", Toast.LENGTH_LONG).show();
                }else {
                    //Hay un registro de movimiento de evidencia

                    String[] splitFechaYHoraRegistro = fechaYHoraUltimoRegistro.split(" ");
                    String horaRegistro = splitFechaYHoraRegistro[1];

                    if (global.obtenerMinutosPasadosDesdeHoraDispositivoMovil(horaRegistro) > 5) {
                        //Ya paso mas de 5 minutos que se tomo esa evidencia
                        Toast.makeText(fragmento.getActivity(), "Ya pasaron mas de 5 minutos que se tomo la última evidencia", Toast.LENGTH_LONG).show();
                    }else {
                        //Aun no han pasado mas de 5 minutos que se tomo esa evidencia

                        if (llaves.impresora_termica) {
                            //Se utilizara impresora termica

                            if (impresoraBluetooth.findBluetoothDevice()) {

                                time = new SimpleDateFormat(" HH:mm:ss").format(new Date());
                                String saldo = String.valueOf(obtenerTotalContrato());

                                if (saldo.indexOf(".") > 0) {
                                    //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
                                    saldo = saldo.substring(0, saldo.indexOf("."));
                                }
                                impresoraBluetooth.verificarSiBluetoothPrinterEstaCerrado();
                                Object[] objetos = {fechaActual + time, obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO"), obtenerRol.obtenerAtributoUsuarioLogeado("TELEFONOATENCIONCLIENTESSUCURSAL"),
                                        obtenerRol.obtenerAtributoUsuarioLogeado("WHATSAPP"), obtenerEstadoAtributoContrato("NOMBRE"), saldo};
                                impresoraBluetooth.printData(objetos, 2);
                                impresoraBluetooth.closedBluetoothPrinter();
                                //Generamos registro de que se imprimio el ticket de visita
                                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado, "Se imprimio ticket de visita", "8");
                                dialog.dismiss();

                            }

                        }

                    }

                }

            }
        });

    }

    public void mostrarAlertDialogOpcionesIconoUbicacion(){
        if(rol.equals("4")){
            //Tienes rol de cobrador

            final Spinner spOpcionesAlertDialogIconoUbicacion = new Spinner(fragmento.getActivity());
            //Llenar campos spOpcionesAlertDialogIconoUbicacion
            String[] idsAlertDialogOpcionesIconoUbicacion = new String[] {"", "0", "1", "2", "3", "4", "5"};
            String[] opcionesAlertDialogOpcionesIconoUbicacion = new String[] {"Seleccionar", "Actualizar ubicación", "Trazar ruta contrato", "Ubicaciones de abonos", "Mapa contratos", "Mandar a ruta", "Copiar coordenadas"};
            spOpcionesAlertDialogIconoUbicacion.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, opcionesAlertDialogOpcionesIconoUbicacion));

            final Spinner spRutaAlertDialogIconoUbicacion = new Spinner(fragmento.getActivity());
            //Llenar campos spRutaAlertDialogIconoUbicacion
            String[] idsAlertDialogRutaIconoUbicacion = new String[] {"0", "1", "2", "3", "4", "5", "6", "7"};
            String[] opcionesAlertDialogRutaIconoUbicacion = new String[] {"Ruta General", "Ruta Lunes", "Ruta Martes", "Ruta Miercoles", "Ruta Jueves", "Ruta Viernes", "Ruta Sabado", "Ruta Domingo"};
            spRutaAlertDialogIconoUbicacion.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, opcionesAlertDialogRutaIconoUbicacion));

            // Crear un LinearLayout vertical para contener los Spinners
            LinearLayout linearLayout = new LinearLayout(fragmento.getActivity());
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            // Agregar los Spinners al LinearLayout
            linearLayout.addView(spOpcionesAlertDialogIconoUbicacion);
            linearLayout.addView(spRutaAlertDialogIconoUbicacion);

            spOpcionesAlertDialogIconoUbicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    if (idsAlertDialogOpcionesIconoUbicacion[spOpcionesAlertDialogIconoUbicacion.getSelectedItemPosition()].equals("4")) {
                        //Se selecciono opcion Mandar a ruta
                        //Mostrar spRutaAlertDialogIconoUbicacion
                        spRutaAlertDialogIconoUbicacion.setVisibility(View.VISIBLE);
                    }else {
                        //Se selecciono otra opcion
                        //Ocultar spRutaAlertDialogIconoUbicacion
                        spRutaAlertDialogIconoUbicacion.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Acción a realizar si no se ha seleccionado nada en el Spinner
                }
            });

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle("Selecciona una opción").setView(linearLayout).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String opcionSeleccionada = idsAlertDialogOpcionesIconoUbicacion[spOpcionesAlertDialogIconoUbicacion.getSelectedItemPosition()];
                    if (opcionSeleccionada.equals("")) {
                        //No se selecciono ninguna opcion
                        Toast.makeText(fragmento.getActivity(), "Debes seleccionar una opción", Toast.LENGTH_LONG).show();
                    } else {
                        //Se selecciono una de las opciones

                        String opcionRuta = idsAlertDialogRutaIconoUbicacion[spRutaAlertDialogIconoUbicacion.getSelectedItemPosition()];
                        if (opcionSeleccionada.equals("4") && !global.obtenerUnResultadoQuery("SELECT ID_CONTRATO FROM RUTA WHERE ID_CONTRATO = '" + ultimoIdContratoCreado + "' AND ESTADO = '1' AND DIA = '" + opcionRuta + "'").equals("")) {
                            //Opcion seleccionada es igual a Mandar a ruta y el idcontrato ya existe en la seccion de ruta
                            Toast.makeText(fragmento.getActivity(), "El contrato ya se encuentra en ruta", Toast.LENGTH_LONG).show();
                        } else {
                            //No esta en la seccion de ruta

                            try {

                                boolean ocultarAlertDialog = true;

                                switch (opcionSeleccionada) {
                                    case "0": //Actualizar ubicación
                                        agregarCoordenadasAContrato();
                                        break;
                                    case "1": //Trazar ruta contrato
                                        ocultarAlertDialog = global.trazarRutaContrato(etNumeroContrato.getText().toString());
                                        break;
                                    case "2": //Ubicaciones de abonos
                                        opcionesIconoUbicacionAbonos();
                                        break;
                                    case "3": //Mapa contratos
                                        if (internet.verificarConexionInternet()) {
                                            //Si tienes conexion a internet
                                            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                                            alerta.setTitle("Confirmación").setMessage(Html.fromHtml("¿Estas seguro que quieres generar el mapa?<br><br>" +
                                                            "<font color='#FFACA6'><b>Esta acción puede demorar unos minutos</b></font>"))
                                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            crearMarcadoresTodosLosContratos();
                                                        }
                                                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();
                                        } else {
                                            //No tienes conexion a internet
                                            Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
                                            ocultarAlertDialog = false;
                                        }
                                        break;
                                    case "4": //Mandar a ruta
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
                                        valores.put("ID_CONTRATO", ultimoIdContratoCreado);
                                        valores.put("ID_USUARIO", idUsuario);
                                        valores.put("POSICION", String.valueOf(contadorOPosicion));
                                        valores.put("ESTADO", "1");
                                        valores.put("ENVIADOPAGINA", "0");
                                        sqLiteDB2.insert("RUTA", null, valores);
                                        sqLiteDB2.close();

                                        llamadaSincronizacion();
                                        Toast.makeText(fragmento.getActivity(), "Se agrego correctamente el contrato a la ruta", Toast.LENGTH_LONG).show();
                                        break;
                                    case "5":
                                        String coordenadas = global.obtenerAtributoContrato(ultimoIdContratoCreado, "COORDENADAS");
                                        ClipboardManager clipboard = (ClipboardManager) fragmento.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText(coordenadas, coordenadas);
                                        clipboard.setPrimaryClip(clip);
                                        Toast.makeText(fragmento.getContext(), "Coordenadas copiado", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                                if (ocultarAlertDialog) {
                                    //Ocultar alertDialog
                                    dialog.dismiss();
                                }

                            } catch (Exception e) {
                                global.escribirError(e, 324);
                                e.printStackTrace();
                            }
                        }

                    }

                }
            });

        }
    }

    private void crearMarcadoresTodosLosContratos() {

        ArrayList<String> contratosACrearMarcadores = new ArrayList<String>();

        try {

            SQLiteDatabase sqLiteDB3 = conexion.getReadableDatabase();
            String SQL = "SELECT ID_CONTRATO, COORDENADAS FROM CONTRATOS WHERE DATOS = '1'";
            Cursor datos = sqLiteDB3.rawQuery(SQL, null);

            if (datos.getCount() > 0) {
                while (datos.moveToNext()) {
                    if (datos.getString(1).length() > 0) {
                        //Si el campo coordenadas es distinto de vacio
                        contratosACrearMarcadores.add(datos.getString(1) + "," + datos.getString(0));
                    }
                }
            }

            sqLiteDB3.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 321);
            Log.i("ERRORDB", e.getMessage() + "");
        }

        quitarBuscadorYAgregarEncabezado();

        Fragment verificadorFragment = new MapsMarcadoresContratos();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("contratosACrearMarcadores",contratosACrearMarcadores);
        bundle.putInt("bandera", 0);
        verificadorFragment.setArguments(bundle);
        FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void quitarBuscadorYAgregarEncabezado() {
        ((MainActivity)fragmento.getActivity()).setTitle(obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO"));
        ((MainActivity)fragmento.getActivity()).hideMenuHamburguesa();
    }

    public void mostrarAlertDialogFotosContrato(int opcion) {

        if(internet.verificarConexionInternet()) {
            //Se cuenta con internet

            String urlFotoContrato = "";
            String observacionFotoContrato = "";

            switch (opcion) {
                case 0:
                    urlFotoContrato = obtenerEstadoAtributoContrato("FOTOCASA");
                    observacionFotoContrato = obtenerEstadoAtributoContrato("OBSERVACIONFOTOCASA");
                    break;
                case 1:
                    urlFotoContrato = obtenerEstadoAtributoContrato("FOTOOTROS");
                    observacionFotoContrato = obtenerEstadoAtributoContrato("OBSERVACIONFOTOOTROS");
                    break;
                case 2:
                    urlFotoContrato = obtenerEstadoAtributoContrato("FOTOINEFRENTE");
                    observacionFotoContrato = obtenerEstadoAtributoContrato("OBSERVACIONFOTOINE");
                    break;
                case 3:
                    urlFotoContrato = obtenerEstadoAtributoContrato("PAGARE");
                    observacionFotoContrato = obtenerEstadoAtributoContrato("OBSERVACIONPAGARE");
                    break;
            }

            if (observacionFotoContrato.length() == 0) {
                //observacionFotoContrato es igual a vacio
                observacionFotoContrato = "Sin observaciones";
            }

            if (urlFotoContrato.length() > 0) {

                try {

                    String nombreCarpeta = "";
                    switch (opcion) {
                        case 0:
                            nombreCarpeta = "fotocasa";
                            btnMostrarFotoCasaContrato.setEnabled(false);
                            break;
                        case 1:
                            nombreCarpeta = "fotootros";
                            btnMostrarFotoOtros.setEnabled(false);
                            break;
                        case 2:
                            nombreCarpeta = "fotoine";
                            btnMostrarFotoIneContrato.setEnabled(false);
                            break;
                        case 3:
                            nombreCarpeta = "pagare";
                            btnMostrarFotoPagare.setEnabled(false);
                            break;
                    }

                    ImageView ivFotoContrato = new ImageView(fragmento.getActivity());

                    Picasso.Builder picasso = new Picasso.Builder(fragmento.getActivity());
                    String finalObservacionFotoContrato = observacionFotoContrato;
                    picasso.build().load("https://adminlabo.luzatuvida.com.mx/uploads/imagenes/" + llaves.carpeta_principal_ftp + "/" + nombreCarpeta + "/" + urlFotoContrato)
                            .resize(800, 800).centerCrop().into(ivFotoContrato, new Callback() {
                                @Override
                                public void onSuccess() {
                                    //Crear AlerDialog para mostrar el dialog_fotos_contrato con propiedades de zoom
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());

                                    //Convertir imagen descargada del servidor en Bitmap
                                    BitmapDrawable drawable = (BitmapDrawable) ivFotoContrato.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();

                                    //Inicializar dialog de adaptacion para zoom a imagen
                                    inflater = (LayoutInflater) fragmento.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    searchView = inflater.inflate(R.layout.dialog_fotos_contrato, null);
                                    //Crear e inicializar PthotoView
                                    PhotoView adaptadorFotosContrato = (PhotoView) searchView.findViewById(R.id.adaptadorFotosContrato);
                                    EditText etObservacionImagen = (EditText) searchView.findViewById(R.id.etObservacionImagen);
                                    //Agregar a adaptador el mapa de bits de imagen descargada
                                    adaptadorFotosContrato.setImageBitmap(bitmap);

                                    //Obtener observacion foto contrato y agregar texto en editText
                                    etObservacionImagen.setText(finalObservacionFotoContrato);

                                    alerta.setView(searchView).setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //Debe ir vacio por que se esta obteniendo abajo
                                        }
                                    }).setNegativeButton("Recargar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //Debe ir vacio por que se esta obteniendo abajo
                                        }
                                    });

                                    final AlertDialog dialog = alerta.create();
                                    dialog.show();

                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            switch (opcion) {
                                                case 0:
                                                    btnMostrarFotoCasaContrato.setEnabled(true);
                                                    break;
                                                case 1:
                                                    btnMostrarFotoOtros.setEnabled(true);
                                                    break;
                                                case 2:
                                                    btnMostrarFotoIneContrato.setEnabled(true);
                                                    break;
                                                case 3:
                                                    btnMostrarFotoPagare.setEnabled(true);
                                                    break;
                                            }
                                            dialog.dismiss();
                                        }

                                    });
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();

                                            //Bloqueamos botones para evitar nueva peticion
                                            btnMostrarFotoCasaContrato.setEnabled(false);
                                            btnMostrarFotoOtros.setEnabled(false);
                                            btnMostrarFotoIneContrato.setEnabled(false);
                                            btnMostrarFotoPagare.setEnabled(false);
                                            //Imprimir mensaje de informacion
                                            Toast.makeText(fragmento.getActivity(), "Reiniciando descarga, espere un momento.", Toast.LENGTH_SHORT).show();
                                            mostrarAlertDialogFotosContrato(opcion);
                                        }
                                    });
                                }

                                @Override
                                public void onError(Exception e) {
                                    switch (opcion) {
                                        case 0:
                                            btnMostrarFotoCasaContrato.setEnabled(true);
                                            break;
                                        case 1:
                                            btnMostrarFotoOtros.setEnabled(true);
                                            break;
                                        case 2:
                                            btnMostrarFotoIneContrato.setEnabled(true);
                                            break;
                                        case 3:
                                            btnMostrarFotoPagare.setEnabled(true);
                                            break;
                                    }
                                    Toast.makeText(fragmento.getActivity(), "No se pudo obtener la foto", Toast.LENGTH_LONG).show();
                                    Log.i("MENSAJE", e.toString());
                                }
                            });

                } catch (Exception e) {
                    global.escribirError(e, 256); // Escribir error - 13-10-2022
                    Log.i("ERROR", e.toString());
                }

            }else {
                //No se tomo la imagen desde la creacion
                Toast.makeText(fragmento.getActivity(), "El contrato no tiene foto", Toast.LENGTH_LONG).show();
            }

        }else {
            //No se cuenta con internet
            Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
        }
        //Desbloquear botones
        btnMostrarFotoCasaContrato.setEnabled(true);
        btnMostrarFotoOtros.setEnabled(true);
        btnMostrarFotoIneContrato.setEnabled(true);
        btnMostrarFotoPagare.setEnabled(true);
    }

    public void actualizarFotoPagareServidor() {

        if (!rol.equals("4") && !garantia) {
            //Asistente/Opto

            if (ivActualizarFotoPagare.getDrawable() == null) {
                Toast.makeText(fragmento.getActivity(), "No se ha agregado la foto pagare", Toast.LENGTH_LONG).show();
            } else {

                global.borrarSiExisteImagenEnDirectorio(rutaPagare);
                time = new SimpleDateFormat("HHmmss").format(new Date());
                rutaPagare = camara.guardarImagenGaleria(ivActualizarFotoPagare, "Foto-Pagare-Contrato-" + ultimoIdContratoCreado + "-" +
                        fechaActual.replace("-", "") + time);
                global.actualizarAtributoTabla("CONTRATOS", "PAGARE", rutaPagare, "ID_CONTRATO", ultimoIdContratoCreado);
                global.actualizarAtributoTabla("CONTRATOS", "OBSERVACIONPAGARE",
                        global.limpiarCadenaCaracteresEspeciales(global.replaceCadena(etObservacionActualizarFotoPagare.getText().toString())),
                        "ID_CONTRATO", ultimoIdContratoCreado);

                String atributo = global.obtenerAtributoTabla("IMAGENESCARGADASCONTRATOS", "PAGARE", "ID_CONTRATO", ultimoIdContratoCreado);

                if (!atributo.equals("")) {
                    //Existe el registro aun en la tabla IMAGENESCARGADASCONTRATOS
                    global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "PAGARE", "0", "ID_CONTRATO", ultimoIdContratoCreado);
                } else {
                    //No existe el registro porlo cual INSERTAREMOS

                    try {

                        SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
                        ContentValues valores = new ContentValues();
                        valores.put("ID_CONTRATO", ultimoIdContratoCreado);
                        valores.put("FOTOINE", "2");
                        valores.put("FOTOINEATRAS", "2");
                        valores.put("FOTOCASA", "2");
                        valores.put("COMPROBANTEDOMICILIO", "2");
                        valores.put("PAGARE", "0");
                        valores.put("TARJETAPENSION", "2");
                        valores.put("TARJETAPENSIONATRAS", "2");
                        valores.put("FOTOOTROS", "2");
                        valores.put("FOTOMOVIMIENTO", "2");
                        sqLiteDB2.insert("IMAGENESCARGADASCONTRATOS", null, valores);
                        sqLiteDB2.close();

                    } catch (SQLiteException e) {
                        global.escribirError(e, 263);
                        Log.i("ERRORBD", e.getMessage() + "");
                    }

                }

                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                        "Se actualizo la foto pagare", "0");

                Toast.makeText(fragmento.getActivity(),
                        "Se actualizo correctamente la foto pagare", Toast.LENGTH_LONG).show();

                llamadaSincronizacion();

            }

        }

    }

    public void actualizarFotoCasaServidor(){
        if (rol.equals("4")) {
            //Cobranza

            if (ivActualizarFotoCasa.getDrawable() == null) {
                Toast.makeText(fragmento.getActivity(), "No se ha agregado la foto fachada", Toast.LENGTH_LONG).show();
            } else {

                global.borrarSiExisteImagenEnDirectorio(rutaCasa);
                time = new SimpleDateFormat("HHmmss").format(new Date());
                rutaCasa = camara.guardarImagenGaleria(ivActualizarFotoCasa, "Foto-Casa-Contrato-" + ultimoIdContratoCreado + "-" +
                        fechaActual.replace("-", "") + time);
                global.actualizarAtributoTabla("CONTRATOS", "FOTOCASA", rutaCasa, "ID_CONTRATO", ultimoIdContratoCreado);

                String atributo = global.obtenerAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOCASA", "ID_CONTRATO", ultimoIdContratoCreado);

                if (!atributo.equals("")) {
                    //Existe el registro aun en la tabla IMAGENESCARGADASCONTRATOS
                    global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOCASA", "0", "ID_CONTRATO", ultimoIdContratoCreado);
                } else {
                    //No existe el registro porlo cual INSERTAREMOS

                    try {

                        SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
                        ContentValues valores = new ContentValues();
                        valores.put("ID_CONTRATO", ultimoIdContratoCreado);
                        valores.put("FOTOINE", "2");
                        valores.put("FOTOINEATRAS", "2");
                        valores.put("FOTOCASA", "0");
                        valores.put("COMPROBANTEDOMICILIO", "2");
                        valores.put("PAGARE", "2");
                        valores.put("TARJETAPENSION", "2");
                        valores.put("TARJETAPENSIONATRAS", "2");
                        valores.put("FOTOOTROS", "2");
                        valores.put("FOTOMOVIMIENTO", "2");
                        sqLiteDB2.insert("IMAGENESCARGADASCONTRATOS", null, valores);
                        sqLiteDB2.close();

                    } catch (SQLiteException e) {
                        global.escribirError(e, 275);
                        Log.i("ERRORBD", e.getMessage() + "");
                    }

                }

                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                        "Se actualizo la foto casa", "0");

                Toast.makeText(fragmento.getActivity(),
                        "Se actualizo correctamente la foto casa", Toast.LENGTH_LONG).show();

                llamadaSincronizacion();

            }

        }
    }

 public void desplegarDialogCompartirTicketAbono(String folio){
     AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
     inflater = (LayoutInflater) fragmento.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     searchView = inflater.inflate(R.layout.dialog_compartir_ticket, null);

     tvTicketAbonoSucursal = searchView.findViewById(R.id.tvTicketAbonoSucursal);
     tvTicketAbonoTelefono = searchView.findViewById(R.id.tvTicketAbonoTelefono);
     tvTicketAbonoWhatsapp = searchView.findViewById(R.id.tvTicketAbonoWhatsapp);
     tvTicketAbonoNombre = searchView.findViewById(R.id.tvTicketAbonoNombre);
     tvTicketAbonoContrato = searchView.findViewById(R.id.tvTicketAbonoContrato);
     tvTicketAbonoFolio = searchView.findViewById(R.id.tvTicketAbonoFolio);
     tvTicketAbonoSaldoAnt = searchView.findViewById(R.id.tvTicketAbonoSaldoAnt);
     tvTicketAbonoCantidadAbono = searchView.findViewById(R.id.tvTicketAbonoCantidadAbono);
     tvTicketAbonoSaldoN = searchView.findViewById(R.id.tvTicketAbonoSaldoN);
     tvTicketAbonoProducto = searchView.findViewById(R.id.tvTicketAbonoProducto);
     tvTicketAbonoCantidadLetra = searchView.findViewById(R.id.tvTicketAbonoCantidadLetra);
     tvTicketAbonoFecha = searchView.findViewById(R.id.tvTicketAbonoFecha);
     tvTicketAbonoCobrador = searchView.findViewById(R.id.tvTicketAbonoCobrador);
     tvTicketAbonoPagado = searchView.findViewById(R.id.tvTicketAbonoPagado);

     //Datos sucursal
     tvTicketAbonoSucursal.setText("SUC. " + obtenerRol.obtenerAtributoUsuarioLogeado("SUCURSAL"));
     String telefono = obtenerRol.obtenerAtributoUsuarioLogeado("TELEFONOATENCIONCLIENTESSUCURSAL");
     tvTicketAbonoTelefono.setText("TELEFONO: " + telefono);
     if(telefono == null || telefono.length() == 0 || telefono.equals("null")){
         //Telefono es null o vacio - ocultar etiqueta
         tvTicketAbonoTelefono.setVisibility(View.GONE);
     }
     String whatsapp = obtenerRol.obtenerAtributoUsuarioLogeado("WHATSAPP");
     tvTicketAbonoWhatsapp.setText("WHATSAPP: " + whatsapp);
     if(whatsapp == null || whatsapp.length() == 0 || whatsapp.equals("null")){
         //Whatsapp es null o vacio - ocultar etiqueta
         tvTicketAbonoWhatsapp.setVisibility(View.GONE);
     }

     //Datos nombre de cliente, contrato y folio
     tvTicketAbonoNombre.setText(obtenerEstadoAtributoContrato("NOMBRE"));
     tvTicketAbonoContrato.setText(ultimoIdContratoCreado);
     tvTicketAbonoFolio.setText(folio);

     //Tipó abono
     String tipoAbono = global.obtenerAtributoTabla("ABONOS", "TIPOABONO", "FOLIO",folio);

     //Obtener total abono y saldo nuevo
     String abono = obtenerSumaAbono(folio);
     if(abono.indexOf(".") > 0) {
         //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
         abono = abono.substring(0, abono.indexOf("."));
     }

     String total = String.valueOf(obtenerTotalContrato());
     if(total.indexOf(".") > 0) {
         //Quitar la parte que venga despues del punto y el punto (Ejem. 100.10 quedaria como 100)
         total = total.substring(0, total.indexOf("."));
     }

     //Datos saldo y abono
     tvTicketAbonoSaldoAnt.setText("$"+totalanterior);
     tvTicketAbonoCantidadAbono.setText("$"+abono);
     tvTicketAbonoSaldoN.setText("$"+total);
     if(tipoAbono.equals("7")){
         //Es abono de producto
         tvTicketAbonoProducto.setVisibility(View.VISIBLE);
     }else{
         //es abono normal
         tvTicketAbonoProducto.setVisibility(View.GONE);
     }

     tvTicketAbonoCantidadLetra.setText(impresoraBluetooth.cantidadConLetra(total.replace("$", "")));

     //Dato fecha actual y nombre cobrador u Opto
     time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
     tvTicketAbonoFecha.setText(time);

     if(obtenerRol.obtenerAtributoUsuarioLogeado("ROL").equals("4")){
         //Rol de cobranza
         tvTicketAbonoCobrador.setText(obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO"));
     }else{
         //Rol de asistente/Opto - Nombre de opto como responsable
         String nombreOpto = global.obtenerAtributoTabla("USERS","NAME","ID",global.obtenerAtributoContrato(ultimoIdContratoCreado,"ID_OPTOMETRISTA"));
         tvTicketAbonoCobrador.setText(nombreOpto);
     }

     //Mostrar ocultar leyenda de pagado
     if(total.replace("$", "").equals("0") && !tipoAbono.equals("7")){
         //Total del contrato es 0 y abono es diferente de tipo producto - Mostrar leyenda de pagado
         tvTicketAbonoPagado.setVisibility(View.VISIBLE);
     }else{
         //Total del contrato diferente de 0
         tvTicketAbonoPagado.setVisibility(View.GONE);
     }

     alerta.setTitle("COMPARTIR TICKET ABONO").setView(searchView)
             .setPositiveButton("Compartir", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     compartirTicketAbono();
                 }
             }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     dialog.cancel();
                 }
             });

     final AlertDialog dialog = alerta.create();
     dialog.show();
 }

    public void compartirTicketAbono(){

        //Eliminamos imagen del corte en caso de que exista una ya generada anteriormente
        global.eliminarImagenCompartir(nombreImagenTicketAbono);

        llFormatoTicketAbonoCompartir = searchView.findViewById(R.id.llFormatoTicketAbonoCompartir);    //Inicializar layout con diseño de ticket abono
        llFormatoTicketAbonoCompartir.setDrawingCacheEnabled(true); //Se habilita la cache
        llFormatoTicketAbonoCompartir.buildDrawingCache(); //Carga los datos
        Bitmap bitmap = llFormatoTicketAbonoCompartir.getDrawingCache(); // Se crea e inicializa el mapa de bits

        try {
            opcionesCompartirTicket(bitmap);
        } catch (Exception e) {
            global.escribirError(e, 259);
            Log.i("Imagen", "Error al generar archivo .JPG: \n" + e.getMessage());
        } finally {
            llFormatoTicketAbonoCompartir.destroyDrawingCache();
        }
    }

    public void opcionesCompartirTicket(Bitmap bitmap){

        String fecha = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        String hora = new SimpleDateFormat("HH_mm_ss").format(new Date());

        Intent compartir = new Intent(Intent.ACTION_SEND);
        compartir.setType("image/jpg"); // Se asiga el tipo de archivo
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);//creamos la imagen en formato jpg, con la calidad 100
        nombreImagenTicketAbono = "IMG_ABONO_TICKET_"+fecha+"_"+hora; //Asignamos un nombre a la imagen
        String path = MediaStore.Images.Media.insertImage(fragmento.getActivity().getContentResolver(), bitmap, nombreImagenTicketAbono,null); // Insertamos la imagen creada en vista previa a compartir
        Uri imagenUri = Uri.parse(path); // Optenemos la uri de la imagen
        compartir.putExtra(Intent.EXTRA_STREAM, imagenUri); //Adjuta el archivo
        fragmento.startActivity(Intent.createChooser(compartir,"Seleccionar")); // Despliega las opciones de compartir
    }

    public void mostrarAlertDialogObservacionesMovimientoContrato(){

        final EditText etObservaciones = new EditText(fragmento.getActivity());
        etObservaciones.setHint("OBSERVACIONES");
        etObservaciones.setHeight(200);
        LinearLayout.LayoutParams layoutReferencia = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etObservaciones.setLayoutParams(layoutReferencia);

        //Obtenemos texto actual del edit text de la vista y lo asignamos al nuevo edit texto del alert
        etObservaciones.setText(etMovimientoContrato.getText().toString());

        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Movimiento contrato").setMessage((Html.fromHtml("Ingresa las observaciones del movimiento")))
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etMovimientoContrato.setText(etObservaciones.getText().toString());
                    }
                }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setView(etObservaciones);

        final android.app.AlertDialog dialog = alerta.create();
        dialog.show();
    }

    public void agregarMovimientoContrato(){
        String rutaFotoMovimientoContrato = "";

        if (ivMovimientoContrato.getDrawable() == null) {
            //No se a tomado ninguna fotografia
            Toast.makeText(fragmento.getActivity(), "No se ha agregado la foto", Toast.LENGTH_LONG).show();
        } else {
            // Existe fotografia tomada - Forma ruta de almacenamiento
            time = new SimpleDateFormat("HHmmss").format(new Date());
            rutaFotoMovimientoContrato = camara.guardarImagenGaleria(ivMovimientoContrato, "Foto-Movimiento-Contrato-" + ultimoIdContratoCreado + "-" +
                    fechaActual.replace("-", "") + time);

            String atributo = global.obtenerAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOMOVIMIENTO", "ID_CONTRATO", ultimoIdContratoCreado);

            if (!atributo.equals("")) {
                //Existe el registro aun en la tabla IMAGENESCARGADASCONTRATOS
                global.actualizarAtributoTabla("IMAGENESCARGADASCONTRATOS", "FOTOMOVIMIENTO", "0", "ID_CONTRATO", ultimoIdContratoCreado);
            } else {
                //No existe el registro porlo cual INSERTAREMOS

                try {

                    SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    valores.put("ID_CONTRATO", ultimoIdContratoCreado);
                    valores.put("FOTOINE", "2");
                    valores.put("FOTOINEATRAS", "2");
                    valores.put("FOTOCASA", "2");
                    valores.put("COMPROBANTEDOMICILIO", "2");
                    valores.put("PAGARE", "2");
                    valores.put("TARJETAPENSION", "2");
                    valores.put("TARJETAPENSIONATRAS", "2");
                    valores.put("FOTOOTROS", "2");
                    valores.put("FOTOMOVIMIENTO", "0");
                    sqLiteDB2.insert("IMAGENESCARGADASCONTRATOS", null, valores);
                    sqLiteDB2.close();

                } catch (SQLiteException e) {
                    global.escribirError(e, 278);
                    Log.i("ERRORBD", e.getMessage() + "");
                }

            }

            //Observaciones de movimiento
            String observaciones  = "";
            observaciones = etMovimientoContrato.getText().toString();

            //Registrar el movimineto con foto
            historialMovimientosContrato.guardarHistorialMovimientosFotoContrato(ultimoIdContratoCreado,rutaFotoMovimientoContrato, observaciones,"0");

            //Se ingresaron observaciones al campo de texto?
            if(observaciones.length() > 0){
                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                        "Agrego evidencia con foto con las siguientes observaciones: '" + observaciones + "'.", "0");
            }else{
                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                        "Agrego evidencia con foto y sin observaciones.", "0");
            }

            Toast.makeText(fragmento.getActivity(),
                    "Se agrego la evidencia al contrato correctamente", Toast.LENGTH_LONG).show();

            llamadaSincronizacion();

            //Limpiar camps de movimiento
            etMovimientoContrato.setText("");
            ivMovimientoContrato.setImageResource(R.drawable.evidencia);
        }
    }

    public void mostrarAlertDialogHistorialMovimientosContrato() {
        if(internet.verificarConexionInternet()) {
            //Se cuenta con internet

            global.imprimirMensajeEnHilo("Espera, cargando lista de movimientos");
            RequestQueue requestQueue = Volley.newRequestQueue(fragmento.getContext());
            JSONObject parametrosJSON = new JSONObject();

            try {

                parametrosJSON.put("correo", obtenerRol.obtenerAtributoUsuarioLogeado("CORREO"));
                parametrosJSON.put("dispositivo", llaves.dispositivo);
                parametrosJSON.put("idunico", Settings.Secure.getString(fragmento.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                parametrosJSON.put("version", Build.VERSION.RELEASE);
                parametrosJSON.put("modelo", Build.MODEL);
                parametrosJSON.put("versiongradle", BuildConfig.VERSION_NAME);
                parametrosJSON.put("lenguajetelefono", Locale.getDefault().getDisplayName());
                parametrosJSON.put("idContrato", idContratoPadre);

                JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST, llaves.url_historialmovimientos_contrato, parametrosJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "codigo")) {
                                case "5Gn6oZ7QUFxT4uDULhAB": //Usuario o contraseña incorrectos
                                    global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos");
                                    redireccionarLogin();
                                    break;
                                case "Ppts8qWkkqosQQqRKlMz": //Solicitud pendiente por confirmar
                                    global.imprimirMensajeEnHilo("Solicita a sucursal autorizacion para acceder");
                                    redireccionarLogin();
                                    break;
                                case "ozpw6GLnvbCMpL2QjIQl": //Debe mandarse el mensaje que es necesario actualizar la aplicacion
                                    global.imprimirMensajeEnHilo("Es necesario actualizar la aplicación");
                                    redireccionarLogin();
                                    break;
                                case "yqZKQB8et5w3N7dK3ZZC": //El usuario esta logeado en la pagina
                                    global.imprimirMensajeEnHilo("Ya tienes una sesión iniciada en la pagina web");
                                    redireccionarLogin();
                                    break;
                                case "swYbf6Diq6DiRS67lXaA": //Usuario diferente a Opto/Asist/Cobranza
                                    global.imprimirMensajeEnHilo("Usuario no admitido (Por favor contacta a administración)");
                                    redireccionarLogin();
                                    break;
                                case "eQiNoYUY0qV4dSlaHMMf": //No existe el usuario en la tabla usuariosfranquicia en el webservice
                                    global.imprimirMensajeEnHilo("Usuario inexistente o sin permisos para acceder");
                                    redireccionarLogin();
                                    break;

                                case "fiVwIpStKaBRIAJpyVHO": //Respuesta correcta

                                    ScrollView verticalScrollView = new ScrollView(fragmento.getActivity());
                                    HorizontalScrollView horizontalScrollView = new HorizontalScrollView(fragmento.getActivity());
                                    TableLayout tblMovimientosContrato = new TableLayout(fragmento.getActivity());

                                    TableRow fila = new TableRow(fragmento.getContext());
                                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                    fila.setLayoutParams(layoutParams);

                                    //Fila para encabezados tabla movimientos
                                    TextView usuario = new TextView(fragmento.getContext());
                                    usuario.setText("USUARIO");
                                    usuario.setGravity(Gravity.CENTER);
                                    usuario.setTextSize(12);
                                    usuario.setTextColor(Color.BLACK);
                                    usuario.setTypeface(null, Typeface.BOLD);
                                    fila.addView(usuario);

                                    TextView cambio = new TextView(fragmento.getContext());
                                    cambio.setText("CAMBIO");
                                    cambio.setTextSize(12);
                                    cambio.setGravity(Gravity.CENTER);
                                    cambio.setTextColor(Color.BLACK);
                                    cambio.setTypeface(null, Typeface.BOLD);
                                    fila.addView(cambio);

                                    TextView fecha = new TextView(fragmento.getContext());
                                    fecha.setText("FECHA");
                                    fecha.setTextSize(12);
                                    fecha.setGravity(Gravity.CENTER);
                                    fecha.setTextColor(Color.BLACK);
                                    fecha.setTypeface(null, Typeface.BOLD);
                                    fila.addView(fecha);
                                    tblMovimientosContrato.addView(fila);

                                    //Leer datos desde la respuesta
                                    String historialContrato = sincronizacion.obtenerAtributoJSONDecodificado(response.get("datos").toString(), "historialcontrato");

                                    //Convertir cadena a JSON
                                    JSONArray jsonArray = null;
                                    try {
                                        if (!historialContrato.equals("null")) {
                                            jsonArray = new JSONArray(historialContrato);
                                        }

                                    } catch (JSONException e) {
                                        global.escribirError(e, 309);
                                        e.printStackTrace();
                                    }

                                    if (jsonArray != null) {
                                        //Contrato con movimientos
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            //Pintar renglones para la tabla
                                            TableRow filaMovimiento = new TableRow(fragmento.getContext());
                                            filaMovimiento.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                                            TextView nombre = new TextView(fragmento.getContext());
                                            nombre.setText(((JSONObject) jsonArray.get(i)).optString("name"));
                                            nombre.setTextColor(Color.BLACK);
                                            nombre.setTextSize(12);
                                            nombre.setPadding(10, 10, 10, 10);
                                            filaMovimiento.addView(nombre);

                                            TextView movimiento = new TextView(fragmento.getContext());
                                            movimiento.setText(((JSONObject) jsonArray.get(i)).optString("cambios"));
                                            movimiento.setTextColor(Color.BLACK);
                                            nombre.setPadding(10, 10, 10, 10);
                                            movimiento.setTextSize(12);
                                            filaMovimiento.addView(movimiento);

                                            TextView fechaCreacion = new TextView(fragmento.getContext());
                                            fechaCreacion.setText(((JSONObject) jsonArray.get(i)).optString("created_at"));
                                            fechaCreacion.setTextColor(Color.BLACK);
                                            fechaCreacion.setTextSize(12);
                                            fechaCreacion.setPadding(10, 10, 10, 10);
                                            filaMovimiento.addView(fechaCreacion);

                                            tblMovimientosContrato.addView(filaMovimiento);
                                        }

                                    }else{
                                        //Contrato sin movimientos
                                        TableRow filaMovimiento = new TableRow(fragmento.getContext());
                                        filaMovimiento.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                                        TextView nombre = new TextView(fragmento.getContext());
                                        nombre.setText("");
                                        nombre.setTextColor(Color.BLACK);
                                        nombre.setTextSize(12);
                                        nombre.setPadding(10, 10, 10, 10);
                                        filaMovimiento.addView(nombre);

                                        TextView movimiento = new TextView(fragmento.getContext());
                                        movimiento.setText("SIN REGISTROS");
                                        movimiento.setTextColor(Color.BLACK);
                                        nombre.setPadding(10, 10, 10, 10);
                                        movimiento.setTextSize(12);
                                        filaMovimiento.addView(movimiento);

                                        TextView fechaCreacion = new TextView(fragmento.getContext());
                                        fechaCreacion.setText("");
                                        fechaCreacion.setTextColor(Color.BLACK);
                                        fechaCreacion.setTextSize(12);
                                        fechaCreacion.setPadding(10, 10, 10, 10);
                                        filaMovimiento.addView(fechaCreacion);

                                        tblMovimientosContrato.addView(filaMovimiento);
                                    }

                                    //Asignar Scroll para tabla
                                    tblMovimientosContrato.setHorizontalScrollBarEnabled(true);
                                    horizontalScrollView.addView(tblMovimientosContrato);
                                    verticalScrollView.addView(horizontalScrollView);

                                    //Crear Alert Dialog
                                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(fragmento.getActivity());
                                    alerta.setTitle("HISTORIAL DE MOVIMIENTOS")
                                            .setPositiveButton("Recargar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    mostrarAlertDialogHistorialMovimientosContrato();
                                                }
                                            }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            }).setView(verticalScrollView);

                                    final android.app.AlertDialog dialog = alerta.create();
                                    dialog.show();
                                    break;

                                default:
                                    global.imprimirMensajeEnHilo("Usuario o contraseña incorrectos.");
                            }

                        } catch (JSONException e) {
                            global.escribirError(e, 310);
                            Log.i("MENSAJE", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error instanceof TimeoutError) {
                                //Time out error
                                global.imprimirMensajeEnHilo("Error al intentar conectar al servidor, se acabo el tiempo de espera (Sin datos disponibles/sin señal/sin conexion a internet por wifi)");
                            }else if(error instanceof NoConnectionError){
                                //net work error
                                global.imprimirMensajeEnHilo("No se pudo conectar(Sin señal/servicio)");
                            } else if (error instanceof AuthFailureError) {
                                //error
                                global.imprimirMensajeEnHilo("Error de autenticacion");
                            } else if (error instanceof ServerError) {
                                //Erroor
                                global.imprimirMensajeEnHilo("No se pudo conectar al servidor");
                            } else if (error instanceof NetworkError) {
                                //Error
                                global.imprimirMensajeEnHilo("Error de red/señal");
                            } else if (error instanceof ParseError) {
                                //Error
                                global.imprimirMensajeEnHilo("Ocurrio un error al intentar conectar");
                            }else{
                                global.imprimirMensajeEnHilo("Error al intentar conectar");
                            }

                        } catch (Exception e) {
                            global.escribirError(e, 311);
                            global.imprimirMensajeEnHilo("Error: "+e.toString());
                        }
                    }
                });

                peticion.setRetryPolicy(new DefaultRetryPolicy(
                        60000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));

                requestQueue.add(peticion);

            }catch (JSONException e) {
                global.escribirError(e, 312);
                Log.i("ERRORJSON", "" + e);
            }
        }else {
            //No se cuenta con internet
            Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
        }
    }

    public void agregarContratoListaNegra(){
        //Alerta para describir reporte

        final EditText etMotivoContrato = new EditText(fragmento.getActivity());
        etMotivoContrato.setHint("DESCRIPCION");
        etMotivoContrato.setHeight(200);
        LinearLayout.LayoutParams layoutReferencia = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etMotivoContrato.setLayoutParams(layoutReferencia);

        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Agregar a lista negra").setMessage((Html.fromHtml("Ingresa la descripcion del reporte")))
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Registrar reporte
                        if(etMotivoContrato.getText().toString().length() > 0){
                            try {

                                SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
                                ContentValues valores = new ContentValues();
                                valores.put("ID_CONTRATO", ultimoIdContratoCreado);
                                valores.put("DESCRIPCION", etMotivoContrato.getText().toString());
                                valores.put("ESTADO", "0");
                                valores.put("ENVIADOPAGINA", "0");
                                sqLiteDB2.insert("CONTRATOSLISTANEGRA", null, valores);
                                sqLiteDB2.close();

                                historialMovimientosContrato.guardarHistorialMovimientosContrato(ultimoIdContratoCreado,
                                        "Se agrego solicitud de lista negra al contrato, con la siguiente descripción: '" + etMotivoContrato.getText().toString() + "'", "0");

                                Toast.makeText(fragmento.getActivity(), "Se agrego solicitud de lista negra al contrato correctamente", Toast.LENGTH_LONG).show();
                                tvListaNegra.setText("Reporte de lista negra pendiente");
                                verContrato.banderaInsertarContratoListaNegra = false;
                                llamadaSincronizacion();

                            } catch (SQLiteException e) {
                                global.escribirError(e, 319);
                                Log.i("ERRORBD", e.getMessage() + "");
                            }
                        }else{
                            Toast.makeText(fragmento.getActivity(), "Ingresa la descripcion del reporte para registrar en lista negra.", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setView(etMotivoContrato);

        final android.app.AlertDialog dialog = alerta.create();
        dialog.show();

    }

    public void redireccionarLogin(){
        Fragment verificadorFragment;
        FragmentTransaction transaction;
        fragmento.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        verificadorFragment = new inicioSesion();
        transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
        ((MainActivity) fragmento.getActivity()).setTitle("");
        ((MainActivity) fragmento.getActivity()).lockDrawer();
        ((MainActivity) fragmento.getActivity()).hideMenuHamburguesa();
        llBuscador.setVisibility(View.GONE);
        llBuscadorIcono.setVisibility(View.GONE);
        llProgress.setVisibility(View.GONE);
    }

    public void opcionesIconoUbicacionAbonos(){
        if(rol.equals("4")){
            //Tienes rol de cobrador

            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle("¿Deseas ver las ubicaciones de los abonos?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Debe ir vacio por que se esta obteniendo abajo
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            final AlertDialog dialog = alerta.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Si aceptamos crear la ruta
                    if (internet.verificarConexionInternet()) {
                        //Si tienes conexion a internet
                        crearMarcadoresAbonosContrato();
                        dialog.dismiss();
                    } else {
                        //No tienes conexion a internet
                        Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
    }

    private void crearMarcadoresAbonosContrato() {

        ArrayList<String> abonosACrearMarcadores = new ArrayList<String>();

        try {

            SQLiteDatabase sqLiteDB3 = conexion.getReadableDatabase();
            String SQL = "SELECT ID_CONTRATO, COORDENADAS, ID FROM ABONOS WHERE ID_CONTRATO = '" + ultimoIdContratoCreado + "'";
            Cursor datos = sqLiteDB3.rawQuery(SQL, null);

            if (datos.getCount() > 0) {
                while (datos.moveToNext()) {
                    if (datos.getString(1).length() > 0) {
                        //Si el campo coordenadas es distinto de vacio
                        abonosACrearMarcadores.add(datos.getString(1) + "," + datos.getString(0));
                    }
                }
            }

            sqLiteDB3.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 322);
            Log.i("ERRORDB", e.getMessage() + "");
        }

        quitarBuscadorYAgregarEncabezado();

        Fragment verificadorFragment = new MapsMarcadoresContratos();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("contratosACrearMarcadores", abonosACrearMarcadores);
        bundle.putInt("bandera", 1);
        verificadorFragment.setArguments(bundle);
        FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void obtenerListaContratosCercanos(){
        filaContratosCercanos.clear();
        if(rol.equals("4")) {
            //Cobranza

            try {

                String localidadContrato = global.obtenerAtributoContrato(ultimoIdContratoCreado,"LOCALIDADENTREGA");
                String coloniaContrato = global.obtenerAtributoContrato(ultimoIdContratoCreado,"COLONIAENTREGA");
                //Obtener contratos de misma colonia para la localidad
                SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
                String SQL = "SELECT ID_CONTRATO, CALLEENTREGA, NUMEROENTREGA FROM CONTRATOS WHERE ID_CONTRATO != '" + ultimoIdContratoCreado +
                              "' AND LOCALIDADENTREGA = '" + localidadContrato + "' AND COLONIAENTREGA LIKE '%" + coloniaContrato + "%' ORDER BY CALLEENTREGA ASC, NUMEROENTREGA ASC";

                Cursor datos = sqLiteDB2.rawQuery(SQL, null);

                if (datos.getCount() == 0) {
                    llListaContratosCercanos.setVisibility(View.GONE);
                    lvContratosCercanos.removeAllViewsInLayout();
                    lvContratosCercanos.postInvalidate();
                } else {
                    while (datos.moveToNext()) {

                        FilaContratosCercanos fila = new FilaContratosCercanos();
                        fila.setIdContrato(datos.getString(0));
                        fila.setCalle(datos.getString(1).toUpperCase());
                        fila.setNumero(datos.getString(2).toUpperCase());
                        filaContratosCercanos.add(fila);
                    }
                    //Agregar la lista en el adaptador
                    adaptadorListaContratosCercanos adaptador = new adaptadorListaContratosCercanos(fragmento, filaContratosCercanos);
                    lvContratosCercanos.setAdapter(adaptador);
                }
                sqLiteDB2.close();
                datos.close();

            } catch (Exception e) {
                global.escribirError(e, 329);
                Log.i("ERRORBD", e.getMessage() + "");
            }
        }
    }

    public void mostrarAlertAbrirContratoCercano(int i) {

        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("CONTRATO: " + filaContratosCercanos.get(i).getIdContrato()).setMessage("ABRIR CONTRATO")
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

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.agregarAUltimoContratoVisto( filaContratosCercanos.get(i).getIdContrato());
                dialog.cancel();
                Fragment verificadorFragment = new verContrato();
                Bundle bundle = new Bundle();
                bundle.putString("ultimoIdContratoCreado", filaContratosCercanos.get(i).getIdContrato());
                verificadorFragment.setArguments(bundle);
                FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, verificadorFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}