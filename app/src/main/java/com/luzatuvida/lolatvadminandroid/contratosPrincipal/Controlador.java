package com.luzatuvida.lolatvadminandroid.contratosPrincipal;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Camara;
import com.luzatuvida.lolatvadminandroid.global.GenerarIdAlfanumerico;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.HistorialMovimientosContrato;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.Localizacion;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Seguridad;
import com.luzatuvida.lolatvadminandroid.global.Sincronizacion;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;
import com.luzatuvida.lolatvadminandroid.impresoraBluetooth.ImpresoraBluetooth;
import com.luzatuvida.lolatvadminandroid.vista.MapsMarcadoresContratos;
import com.luzatuvida.lolatvadminandroid.vista.nuevoContrato;
import com.luzatuvida.lolatvadminandroid.vista.verContrato;

import java.io.File;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Controlador {

    /*
    Identificador error del 8 al 47, 254
     */

    ListView lvListaContratos;
    List<FilaContratos> filaContratos;
    private baseDeDatos conexion;
    Fragment fragmento;
    SQLiteDatabase sqLiteDB;
    String rol = "";
    String nombreUsuarioLogeado = "";
    HistorialMovimientosContrato historialMovimientosContrato;
    List<Integer> estadoContratos;
    List<Integer> encabezados;
    List<Integer> diasAtrasados;

    ObtenerRol obtenerRol;
    LinearLayout llBuscador, llBuscadorIcono;
    ImageView ivQuitarBuscadorIcono;
    Global global;
    Sincronizacion sincronizacion;

    String fechaactual = "";
    String idusuario = "";
    String id_zona = "";
    String time = "";
    Object[] objetosWebService;
    Llaves llaves;
    TextView tvIdContratoClienteContratoPrincipal, tvNombreClienteContratoPrincipal, tvColoniaClienteContratoPrincipal, tvNotaClienteContratoPrincipal, tvUltimoAbonoContratoPrincipal;
//    boolean mostrarOcultarIdContratoNombre = false;
//    boolean mostrarOcultarIdContratoNombreLista;
//    boolean mostrarOcultarColoniaNota = false;
//    boolean mostrarOcultarColoniaNotaLista;
    Seguridad seguridad;
    EditText etBuscador;
    String valorHamburguesaRuta = "";

    GenerarIdAlfanumerico generarIdAlfanumerico;

    String[] posActualDiasTemporales, tituloDiasTemporales, idsUsuariosVentas, nombreUsuariosVentas;

    LinearLayout llProgress;

    Localizacion localizacion;
    Runnable runnable;
    Internet internet;
    Camara camara;

    //Top ventas
    TableLayout tblTopVenta;
    ImpresoraBluetooth impresoraBluetooth;
    String  fechaLunesAnterior;
    String  fechaDomingoSiguiente;
    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    public Controlador(Fragment fragmento, Object[] objetos) {
        this.fragmento = fragmento;
        lvListaContratos = (ListView)objetos[0];
        filaContratos = (List<FilaContratos>)objetos[1];
        rol = (String)objetos[2];
        llaves = new Llaves();
        conexion = new baseDeDatos(fragmento.getContext(), "basedatos", null, llaves.versiondb);
        historialMovimientosContrato = new HistorialMovimientosContrato(fragmento);
        obtenerRol = new ObtenerRol(fragmento);
        global = new Global(fragmento);
        sincronizacion = new Sincronizacion(fragmento,false);
        llBuscador = (LinearLayout)objetos[3];
        llBuscadorIcono = (LinearLayout)objetos[4];
        ivQuitarBuscadorIcono = (ImageView) objetos[5];
        nombreUsuarioLogeado = (String)objetos[6];
        tvIdContratoClienteContratoPrincipal = (TextView) objetos[7];
        tvNombreClienteContratoPrincipal = (TextView) objetos[8];
        tvColoniaClienteContratoPrincipal = (TextView) objetos[9];
        tvNotaClienteContratoPrincipal = (TextView) objetos[10];
        etBuscador = (EditText) objetos[11];
        llProgress = (LinearLayout) objetos[12];
        seguridad = new Seguridad(fragmento);
        generarIdAlfanumerico = new GenerarIdAlfanumerico(fragmento);
        objetosWebService = new Object[]{obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN"), "", ""};
        localizacion = new Localizacion(fragmento);
        internet = new Internet(fragmento.getActivity());
        fechaactual = obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL");
        idusuario = obtenerRol.obtenerAtributoUsuarioLogeado("ID");
        id_zona = obtenerRol.obtenerAtributoUsuarioLogeado("ID_ZONA");
        camara = new Camara(fragmento);
        //Top Ventas
        tblTopVenta = (TableLayout) objetos[13];
        tvUltimoAbonoContratoPrincipal = (TextView) objetos[14];
        impresoraBluetooth = new ImpresoraBluetooth(fragmento);

        //Obtener fechas de semana actual
        fechaLunesAnterior = fechaactual;
        fechaDomingoSiguiente = fechaactual;
        Calendar calendar = Calendar.getInstance();
        //Obtener fecha del lunes
        while (calendar.get(Calendar.DAY_OF_WEEK) !=  Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
            fechaLunesAnterior = formatoFecha.format(calendar.getTime());
        }
        //Fecha domingo siguiente
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            fechaDomingoSiguiente = formatoFecha.format(calendar.getTime());
        }
    }

    /*Metodo/Funcion: obtenerListaContratos
      Descripcion: Se obtiene la lista de los contratos dependiendo del rol Asistente/Optometrista o Cobranza
    */
    public void obtenerListaContratos(boolean sincronizar) {

        filaContratos.clear();
        estadoContratos = new ArrayList<>();
        encabezados = new ArrayList<>();
        diasAtrasados = new ArrayList<>();

        if(rol.equals("4")) {
            //Cobranza

            verificarContratosConTipoAbonoLiquidadoYTotalMayorACero();
            verificarContratosConTipoAbonoEntregaProductoYCambiarEstadoContratoAEntregado();
            verificarSiHayMasDeUnAbonoLiquidadoEnLosContratosYCambiarANormal();

            boolean valorHamburguesaTodos = false;
            try {
                //Obtener valorHamburguesaTodos y valorHamburguesaRuta
                valorHamburguesaTodos = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaTodos"));
                valorHamburguesaRuta = seguridad.leerSharedPreferences("valorHamburguesaRuta");
                if (valorHamburguesaRuta.length() == 0) {
                    //valorHamburguesaRuta es igual a vacio
                    valorHamburguesaRuta = "0";
                }
            } catch (Exception e) {
                global.escribirError(e, 8);
                e.printStackTrace();
            }
/*
            if (tvIdContratoClienteContratoPrincipal.getVisibility() == View.VISIBLE) {
                mostrarOcultarIdContratoNombreLista = true;
            } else {
                mostrarOcultarIdContratoNombreLista = false;
            }

            if (tvColoniaClienteContratoPrincipal.getVisibility() == View.VISIBLE) {
                mostrarOcultarColoniaNotaLista = true;
            } else {
                mostrarOcultarColoniaNotaLista = false;
            }
*/
            if(valorHamburguesaTodos) {
                //valorHamburguesaTodos esta chequeado

                //Obtener como dira el encabezado de la ruta dependiendo el dia de la ruta
                String encabezadoRuta = "";
                switch (valorHamburguesaRuta) {
                    case "0": //General
                        encabezadoRuta = "R - GE";
                        break;
                    case "1": //Lunes
                        encabezadoRuta = "R - LU";
                        break;
                    case "2": //Martes
                        encabezadoRuta = "R - MA";
                        break;
                    case "3": //Miercoles
                        encabezadoRuta = "R - MI";
                        break;
                    case "4": //Jueves
                        encabezadoRuta = "R - JU";
                        break;
                    case "5": //Viernes
                        encabezadoRuta = "R - VI";
                        break;
                    case "6": //Sabado
                        encabezadoRuta = "R - SA";
                        break;
                    case "7": //Domingo
                        encabezadoRuta = "R - DO";
                        break;
                }

                try {
                    // ULTIMO CONTRATO ABIERTO
                    pintarUltimoContratoVisto();

                } catch (SQLiteException e) {
                    global.escribirError(e, 9);
                    Log.i("ERRORBD", e.getMessage() + "");
                }

                try {
                    //CONTRATOS EN RUTA -> Enrutados por cobranza

                    cambiarEstadoCeroARegistrosDuplicadosRuta();

                    sqLiteDB = conexion.getReadableDatabase();
                    String SQL = "SELECT (SELECT C.ID_CONTRATO FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT C.NOMBRE FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT C.ESTATUS_ESTADOCONTRATO FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT C.LOCALIDADENTREGA FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT C.COLONIAENTREGA FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT C.CALLEENTREGA FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT C.ENVIADOPAGINA FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT COUNT(H.ID) FROM HISTORIALCLINICO H WHERE R.ID_CONTRATO = H.ID_CONTRATO " +
                            "AND (LENGTH(H.OBSERVACIONES) > 0 OR LENGTH(H.OBSERVACIONESINTERNO) > 0)) AS OBSERVACION," +
                            " (SELECT C.NUMEROENTREGA FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT C.FECHAATRASO FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT C.PAGO FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT C.NOTA FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT C.CONTRATOENVIADO FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                            " (SELECT CREATED_AT FROM ABONOS A WHERE R.ID_CONTRATO = A.ID_CONTRATO AND A.TIPOABONO NOT IN (7) ORDER BY A.CREATED_AT DESC LIMIT 1) AS ULTIMOABONO " +
                            " FROM RUTA R WHERE R.ID_CONTRATO IN (SELECT C.ID_CONTRATO FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO) " +
                            "AND R.ESTADO = '1' AND R.DIA = '" + valorHamburguesaRuta + "' " +
                            "AND (ULTIMOABONO = '' OR (ULTIMOABONO <= '" + fechaLunesAnterior + "' OR ULTIMOABONO >= '" + fechaDomingoSiguiente +"')) " +
                            " ORDER BY CAST(R.POSICION AS INTEGER) ASC";

                    Cursor datos = sqLiteDB.rawQuery(SQL, null);

                    if (datos.getCount() == 0) {
                        //No hay datos
                        lvListaContratos.removeAllViewsInLayout();
                        lvListaContratos.postInvalidate();
                    } else {
                        //Si hay datos

                        FilaContratos fila = new FilaContratos();

                        //Agregacion de encabezado RUTA
                        fila.setDiasAtrasoContrato(encabezadoRuta);

                        /*
                        if (mostrarOcultarIdContratoNombreLista) {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setIdContrato(encabezadoRuta);
                            }else {
                                fila.setNumeroClienteContrato(encabezadoRuta);
                            }
                        } else {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setNombreClienteContrato(encabezadoRuta);
                            }else {
                                fila.setNumeroClienteContrato(encabezadoRuta);
                            }
                        }

 */
                        fila.setEsRuta("");

                        estadoContratos.add(20);
                        filaContratos.add(fila);
                        encabezados.add(filaContratos.size() - 1);
                        diasAtrasados.add(0);

                        while (datos.moveToNext()) {
                            //Obtener datos y agregarlos a la lista
                            fila = new FilaContratos();
                            fila.setIdContrato(datos.getString(0));
                            fila.setNombreClienteContrato(datos.getString(1).toUpperCase().trim());
                            fila.setUltimoAbono(datos.getString(13));
                            fila.setLocalidadClienteContrato(datos.getString(3).toUpperCase());
                            fila.setColoniaClienteContrato(datos.getString(4).toUpperCase());
                            fila.setCalleClienteContrato(datos.getString(5).toUpperCase());
                            fila.setNumeroClienteContrato(datos.getString(8).toUpperCase());
                            fila.setEsRuta("1");
                            fila.setFormaPagoClienteContrato(obtenerLetraFormaPagoContrato(datos.getInt(10)));
                            fila.setNotaClienteContrato(datos.getString(11).toUpperCase());
                            if (datos.getString(6).equals("0")) {
                                fila.setEnviadoPaginaClienteContrato("P," + datos.getString(12));
                            } else {
                                fila.setEnviadoPaginaClienteContrato("S," + datos.getString(12));
                            }
                            //Observaciones historial
                            if (datos.getInt(7) > 0) {
                                //Sin observaciones
                                fila.setObservacionesClienteContrato("S");
                            } else {
                                //Con observaciones
                                fila.setObservacionesClienteContrato("N");
                            }
                            fila.setEstadoContratoClienteContrato(datos.getString(2));
                            estadoContratos.add(Integer.parseInt(datos.getString(2)));

                            if (datos.getInt(2) == 4) {
                                //Estado del contrato es igual a ABONOATRASADO
                                diasAtrasados.add(obtenerDiasAtrasados(datos.getString(9)));
                                fila.setDiasAtrasoContrato(Integer.toString(obtenerDiasAtrasados(datos.getString(9))));
                            } else {
                                //Estado del contrato es diferente a ABONOATRASADO por lo tanto no tiene fechaatraso
                                diasAtrasados.add(0);
                                fila.setDiasAtrasoContrato("0");
                            }

                            filaContratos.add(fila);
                        }

                    }

                    sqLiteDB.close();
                    datos.close();

                } catch (SQLiteException e) {
                    global.escribirError(e, 10);
                    Log.i("ERRORBD", e.getMessage() + "");
                }

                try {
                    //CONTRATOS ATRASADOS

                    sqLiteDB = conexion.getReadableDatabase();
                    String SQL = "SELECT C.ID_CONTRATO, C.NOMBRE, C.ESTATUS_ESTADOCONTRATO, C.FECHAATRASO, C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.ENVIADOPAGINA," +
                            " (SELECT COUNT(H.ID) FROM HISTORIALCLINICO H WHERE C.ID_CONTRATO = H.ID_CONTRATO " +
                            "AND (LENGTH(H.OBSERVACIONES) > 0 OR LENGTH(H.OBSERVACIONESINTERNO) > 0)) AS OBSERVACION, C.NUMEROENTREGA, C.PAGO, C.NOTA, C.CONTRATOENVIADO, " +
                            "(SELECT CREATED_AT FROM ABONOS A WHERE A.ID_CONTRATO = C.ID_CONTRATO AND A.TIPOABONO NOT IN (7) ORDER BY A.CREATED_AT DESC LIMIT 1) AS ULTIMOABONO " +
                            " FROM CONTRATOS C WHERE C.DATOS = '1' AND C.ID_ZONA = '" + id_zona + "' " +
                            " AND (ULTIMOABONO = '' OR (ULTIMOABONO <= '" + fechaLunesAnterior + "' OR ULTIMOABONO >= '" + fechaDomingoSiguiente +"')) " +
                            " AND C.ESTATUS_ESTADOCONTRATO = '4' ORDER BY C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.NUMEROENTREGA, C.ID_CONTRATO ASC";
                    Cursor datos = sqLiteDB.rawQuery(SQL, null);

                    if (datos.getCount() == 0) {
                        //No hay datos
                        lvListaContratos.removeAllViewsInLayout();
                        lvListaContratos.postInvalidate();
                    } else {
                        //Si hay datos

                        FilaContratos fila = new FilaContratos();

                        //Agregacion de encabezado ATRASADOS
                        fila.setDiasAtrasoContrato("ATRASADO");
                        /*
                        if (mostrarOcultarIdContratoNombreLista) {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setIdContrato("ATRASADO");
                            }else {
                                fila.setNumeroClienteContrato("ATRASADO");
                            }
                        } else {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setNombreClienteContrato("ATRASADO");
                            }else {
                                fila.setNumeroClienteContrato("ATRASADO");
                            }
                        }
                        */
                        fila.setEsRuta("");

                        estadoContratos.add(20);
                        filaContratos.add(fila);
                        encabezados.add(filaContratos.size() - 1);
                        diasAtrasados.add(0);

                        while (datos.moveToNext()) {
                            //Obtener datos y agregarlos a la lista
                            fila = new FilaContratos();
                            fila.setIdContrato(datos.getString(0));
                            fila.setNombreClienteContrato(datos.getString(1).trim().toUpperCase());
                            fila.setUltimoAbono(datos.getString(13));
                            fila.setLocalidadClienteContrato(datos.getString(4).toUpperCase());
                            fila.setColoniaClienteContrato(datos.getString(5).toUpperCase());
                            fila.setCalleClienteContrato(datos.getString(6).toUpperCase());
                            fila.setNumeroClienteContrato(datos.getString(9).toUpperCase());
                            fila.setEsRuta("0");
                            fila.setFormaPagoClienteContrato(obtenerLetraFormaPagoContrato(datos.getInt(10)));
                            fila.setNotaClienteContrato(datos.getString(11).toUpperCase());
                            if (datos.getString(7).equals("0")) {
                                fila.setEnviadoPaginaClienteContrato("P," + datos.getString(12));
                            } else {
                                fila.setEnviadoPaginaClienteContrato("S," + datos.getString(12));
                            }
                            //Observaciones historial
                            if (datos.getInt(8) > 0) {
                                //Sin observaciones
                                fila.setObservacionesClienteContrato("S");
                            } else {
                                //Con observaciones
                                fila.setObservacionesClienteContrato("N");
                            }
                            fila.setEstadoContratoClienteContrato(datos.getString(2));
                            estadoContratos.add(Integer.parseInt(datos.getString(2)));
                            diasAtrasados.add(obtenerDiasAtrasados(datos.getString(3)));
                            fila.setDiasAtrasoContrato(Integer.toString(obtenerDiasAtrasados(datos.getString(3))));
                            filaContratos.add(fila);

                        }

                    }

                    sqLiteDB.close();
                    datos.close();

                } catch (SQLiteException e) {
                    global.escribirError(e, 11);
                    Log.i("ERRORBD", e.getMessage() + "");
                }


                try {
                    //CONTRATOS PRIORITARIOS -> diaseleccionado = fechaactual

                    sqLiteDB = conexion.getReadableDatabase();
                    String SQL = "SELECT C.ID_CONTRATO, C.NOMBRE, C.ESTATUS_ESTADOCONTRATO, C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.ENVIADOPAGINA," +
                          " (SELECT COUNT(H.ID) FROM HISTORIALCLINICO H WHERE C.ID_CONTRATO = H.ID_CONTRATO AND (LENGTH(H.OBSERVACIONES) > 0 OR LENGTH(H.OBSERVACIONESINTERNO) > 0)) AS OBSERVACION," +
                          " C.NUMEROENTREGA, C.PAGO, C.NOTA, C.CONTRATOENVIADO, (SELECT CREATED_AT FROM ABONOS A WHERE A.ID_CONTRATO = C.ID_CONTRATO AND A.TIPOABONO NOT IN (7) " +
                          " ORDER BY A.CREATED_AT DESC LIMIT 1) AS ULTIMOABONO FROM CONTRATOS C WHERE C.DATOS = '1' AND C.ID_ZONA = '" + id_zona + "' AND ((C.ESTATUS_ESTADOCONTRATO = '2'" +
                          " AND C.DIASELECCIONADO != '' AND CAST(C.TOTAL as DOUBLE) > 0) OR C.ESTATUS_ESTADOCONTRATO IN (1,7,9,10,11))" +
                          " AND (ULTIMOABONO = '' OR (ULTIMOABONO <= '" + fechaLunesAnterior + "' OR ULTIMOABONO >= '" + fechaDomingoSiguiente +"')) " +
                          " ORDER BY C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.NUMEROENTREGA, C.ID_CONTRATO ASC";
                    Cursor datos = sqLiteDB.rawQuery(SQL, null);

                    if (datos.getCount() == 0) {
                        //No hay datos
                        lvListaContratos.removeAllViewsInLayout();
                        lvListaContratos.postInvalidate();
                    } else {
                        //Si hay datos

                        FilaContratos fila = new FilaContratos();

                        //Agregacion de encabezado PRIORITARIOS
                        fila.setDiasAtrasoContrato("PRIORITARIOS");
                        /*
                        if (mostrarOcultarIdContratoNombreLista) {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setIdContrato("PRIORITARIOS");
                            }else {
                                fila.setNumeroClienteContrato("PRIORITARIOS");
                            }
                        } else {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setNombreClienteContrato("PRIORITARIOS");
                            }else {
                                fila.setNumeroClienteContrato("PRIORITARIOS");
                            }
                        }*/
                        fila.setEsRuta("");

                        estadoContratos.add(20);
                        filaContratos.add(fila);
                        encabezados.add(filaContratos.size() - 1);

                        while (datos.moveToNext()) {
                            //Obtener datos y agregarlos a la lista
                            fila = new FilaContratos();
                            fila.setIdContrato(datos.getString(0));
                            fila.setNombreClienteContrato(datos.getString(1).toUpperCase().trim());
                            fila.setUltimoAbono(datos.getString(12));
                            fila.setLocalidadClienteContrato(datos.getString(3).toUpperCase());
                            fila.setColoniaClienteContrato(datos.getString(4).toUpperCase());
                            fila.setCalleClienteContrato(datos.getString(5).toUpperCase());
                            fila.setNumeroClienteContrato(datos.getString(8).toUpperCase());
                            fila.setEsRuta("0");
                            fila.setFormaPagoClienteContrato(obtenerLetraFormaPagoContrato(datos.getInt(9)));
                            fila.setNotaClienteContrato(datos.getString(10).toUpperCase());
                            if (datos.getString(6).equals("0")) {
                                fila.setEnviadoPaginaClienteContrato("P," + datos.getString(11));
                            } else {
                                fila.setEnviadoPaginaClienteContrato("S," + datos.getString(11));
                            }
                            //Observaciones historial
                            if (datos.getInt(7) > 0) {
                                //Sin observaciones
                                fila.setObservacionesClienteContrato("S");
                            } else {
                                //Con observaciones
                                fila.setObservacionesClienteContrato("N");
                            }
                            fila.setEstadoContratoClienteContrato(datos.getString(2));
                            estadoContratos.add(Integer.parseInt(datos.getString(2)));

                            filaContratos.add(fila);
                        }

                    }

                    sqLiteDB.close();
                    datos.close();

                } catch (SQLiteException e) {
                    global.escribirError(e, 12);
                    Log.i("ERRORBD", e.getMessage() + "");
                }


                try {
                    //CONTRATOS PERIODO -> fechaini >= fechaactual && fechafin <= fechaactual

                    sqLiteDB = conexion.getReadableDatabase();
                    String SQL = "SELECT C.ID_CONTRATO, C.NOMBRE, C.ESTATUS_ESTADOCONTRATO, C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.ENVIADOPAGINA," +
                          " (SELECT COUNT(H.ID) FROM HISTORIALCLINICO H WHERE C.ID_CONTRATO = H.ID_CONTRATO AND (LENGTH(H.OBSERVACIONES) > 0 OR LENGTH(H.OBSERVACIONESINTERNO) > 0)) AS OBSERVACION," +
                          " C.NUMEROENTREGA, C.PAGO, C.NOTA, C.CONTRATOENVIADO, (SELECT CREATED_AT FROM ABONOS A WHERE A.ID_CONTRATO = C.ID_CONTRATO AND A.TIPOABONO NOT IN (7) " +
                          " ORDER BY A.CREATED_AT DESC LIMIT 1) AS ULTIMOABONO  FROM CONTRATOS C WHERE C.DATOS = '1' AND C.ID_ZONA = '" + id_zona + "' AND C.ESTATUS_ESTADOCONTRATO = '2'" +
                          " AND CAST(C.TOTAL as DOUBLE) > 0 AND C.DIASELECCIONADO = ''" +
                          " AND (ULTIMOABONO = '' OR (ULTIMOABONO <= '" + fechaLunesAnterior + "' OR ULTIMOABONO >= '" + fechaDomingoSiguiente +"')) " +
                          " ORDER BY C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.NUMEROENTREGA, C.ID_CONTRATO ASC";
                    Cursor datos = sqLiteDB.rawQuery(SQL, null);

                    if (datos.getCount() == 0) {
                        //No hay datos
                        lvListaContratos.removeAllViewsInLayout();
                        lvListaContratos.postInvalidate();
                    } else {
                        //Si hay datos

                        FilaContratos fila = new FilaContratos();

                        //Agregacion de encabezado PERIODO
                        fila.setDiasAtrasoContrato("PERIODO");
                        /*
                        if (mostrarOcultarIdContratoNombreLista) {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setIdContrato("PERIODO");
                            }else {
                                fila.setNumeroClienteContrato("PERIODO");
                            }
                        } else {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setNombreClienteContrato("PERIODO");
                            }else {
                                fila.setNumeroClienteContrato("PERIODO");
                            }
                        }
                         */
                        fila.setEsRuta("");

                        estadoContratos.add(20);
                        filaContratos.add(fila);
                        encabezados.add(filaContratos.size() - 1);

                        while (datos.moveToNext()) {
                            //Obtener datos y agregarlos a la lista
                            fila = new FilaContratos();
                            fila.setIdContrato(datos.getString(0));
                            fila.setNombreClienteContrato(datos.getString(1).toUpperCase().trim());
                            fila.setUltimoAbono(datos.getString(12));
                            fila.setLocalidadClienteContrato(datos.getString(3).toUpperCase());
                            fila.setColoniaClienteContrato(datos.getString(4).toUpperCase());
                            fila.setCalleClienteContrato(datos.getString(5).toUpperCase());
                            fila.setNumeroClienteContrato(datos.getString(8).toUpperCase());
                            fila.setEsRuta("0");
                            fila.setFormaPagoClienteContrato(obtenerLetraFormaPagoContrato(datos.getInt(9)));
                            fila.setNotaClienteContrato(datos.getString(10).toUpperCase());
                            if (datos.getString(6).equals("0")) {
                                fila.setEnviadoPaginaClienteContrato("P," + datos.getString(11));
                            } else {
                                fila.setEnviadoPaginaClienteContrato("S," + datos.getString(11));
                            }
                            //Observaciones historial
                            if (datos.getInt(7) > 0) {
                                //Sin observaciones
                                fila.setObservacionesClienteContrato("S");
                            } else {
                                //Con observaciones
                                fila.setObservacionesClienteContrato("N");
                            }
                            fila.setEstadoContratoClienteContrato(datos.getString(2));
                            estadoContratos.add(Integer.parseInt(datos.getString(2)));

                            filaContratos.add(fila);
                        }

                    }

                    sqLiteDB.close();
                    datos.close();

                } catch (SQLiteException e) {
                    global.escribirError(e, 13);
                    Log.i("ERRORBD", e.getMessage() + "");
                }


                try {
                    //CONTRATOS POR ENTREGAR -> estadocontrato = terminado

                    sqLiteDB = conexion.getReadableDatabase();
                    String SQL = "SELECT C.ID_CONTRATO, C.NOMBRE, C.ESTATUS_ESTADOCONTRATO, C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.ENVIADOPAGINA," +
                          " (SELECT COUNT(H.ID) FROM HISTORIALCLINICO H WHERE C.ID_CONTRATO = H.ID_CONTRATO AND (LENGTH(H.OBSERVACIONES) > 0 OR LENGTH(H.OBSERVACIONESINTERNO) > 0)) AS OBSERVACION," +
                          " C.NUMEROENTREGA, C.PAGO, C.NOTA, C.CONTRATOENVIADO, (SELECT CREATED_AT FROM ABONOS A WHERE A.ID_CONTRATO = C.ID_CONTRATO AND A.TIPOABONO NOT IN (7) " +
                          " ORDER BY A.CREATED_AT DESC LIMIT 1) AS ULTIMOABONO, C.OPCIONLUGARENTREGA, C.LOCALIDAD, C.COLONIA, C.CALLE, C.NUMERO FROM CONTRATOS C WHERE C.DATOS = '1' AND C.ID_ZONA = '" + id_zona + "' AND C.ESTATUS_ESTADOCONTRATO = '12'" +
                          " AND (ULTIMOABONO = '' OR (ULTIMOABONO <= '" + fechaLunesAnterior + "' OR ULTIMOABONO >= '" + fechaDomingoSiguiente +"')) " +
                          " ORDER BY C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.NUMEROENTREGA, C.ID_CONTRATO ASC";
                    Cursor datos = sqLiteDB.rawQuery(SQL, null);

                    if (datos.getCount() == 0) {
                        //No hay datos
                        lvListaContratos.removeAllViewsInLayout();
                        lvListaContratos.postInvalidate();
                    } else {
                        //Si hay datos

                        FilaContratos fila = new FilaContratos();

                        //Agregacion de encabezado POR ENTREGAR
                        fila.setDiasAtrasoContrato("POR ENTREGAR");
                        /*
                        if (mostrarOcultarIdContratoNombreLista) {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setIdContrato("POR ENTREGAR");
                            }else {
                                fila.setNumeroClienteContrato("POR ENTREGAR");
                            }
                        } else {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setNombreClienteContrato("POR ENTREGAR");
                            }else {
                                fila.setNumeroClienteContrato("POR ENTREGAR");
                            }
                        }
                         */
                        fila.setEsRuta("");

                        estadoContratos.add(20);
                        filaContratos.add(fila);
                        encabezados.add(filaContratos.size() - 1);

                        while (datos.moveToNext()) {
                            //Obtener datos y agregarlos a la lista
                            fila = new FilaContratos();
                            fila.setIdContrato(datos.getString(0));
                            fila.setNombreClienteContrato(datos.getString(1).toUpperCase().trim());
                            fila.setUltimoAbono(datos.getString(12));
                            if(datos.getString(13).equals("0")){
                                //Entrega de producto en lugar de venta
                                fila.setLocalidadClienteContrato(datos.getString(14).toUpperCase());
                                fila.setColoniaClienteContrato(datos.getString(15).toUpperCase());
                                fila.setCalleClienteContrato(datos.getString(16).toUpperCase());
                                fila.setNumeroClienteContrato(datos.getString(17).toUpperCase());
                            }else{
                                //Entrega de producto en lugar de cobranza
                                fila.setLocalidadClienteContrato(datos.getString(3).toUpperCase());
                                fila.setColoniaClienteContrato(datos.getString(4).toUpperCase());
                                fila.setCalleClienteContrato(datos.getString(5).toUpperCase());
                                fila.setNumeroClienteContrato(datos.getString(8).toUpperCase());
                            }
                            fila.setEsRuta("0");
                            fila.setFormaPagoClienteContrato(obtenerLetraFormaPagoContrato(datos.getInt(9)));
                            fila.setNotaClienteContrato(datos.getString(10).toUpperCase());
                            if (datos.getString(6).equals("0")) {
                                fila.setEnviadoPaginaClienteContrato("P," + datos.getString(11));
                            } else {
                                fila.setEnviadoPaginaClienteContrato("S," + datos.getString(11));
                            }
                            //Observaciones historial
                            if (datos.getInt(7) > 0) {
                                //Sin observaciones
                                fila.setObservacionesClienteContrato("S");
                            } else {
                                //Con observaciones
                                fila.setObservacionesClienteContrato("N");
                            }
                            fila.setEstadoContratoClienteContrato(datos.getString(2));
                            estadoContratos.add(Integer.parseInt(datos.getString(2)));

                            filaContratos.add(fila);
                        }

                    }

                    sqLiteDB.close();
                    datos.close();

                } catch (SQLiteException e) {
                    global.escribirError(e, 14);
                    Log.i("ERRORBD", e.getMessage() + "");
                }


                try {
                    //CONTRATOS ENTREGADOS/LIQUIDADOS donde la fecha de entrega o el ultimo abono sea igual a la fecha actual

                    sqLiteDB = conexion.getReadableDatabase();
                    String SQL = "SELECT C.ID_CONTRATO, C.NOMBRE, C.ESTATUS_ESTADOCONTRATO, C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.ENVIADOPAGINA," +
                          " (SELECT COUNT(H.ID) FROM HISTORIALCLINICO H WHERE C.ID_CONTRATO = H.ID_CONTRATO AND (LENGTH(H.OBSERVACIONES) > 0 OR LENGTH(H.OBSERVACIONESINTERNO) > 0)) AS OBSERVACION," +
                          " C.NUMEROENTREGA, C.PAGO, C.NOTA, C.CONTRATOENVIADO, (SELECT CREATED_AT FROM ABONOS A WHERE A.ID_CONTRATO = C.ID_CONTRATO AND A.TIPOABONO NOT IN (7) " +
                          " ORDER BY A.CREATED_AT DESC LIMIT 1) AS ULTIMOABONO FROM CONTRATOS C WHERE C.DATOS = '1' AND C.ID_ZONA = '" + id_zona +
                          "' AND C.FECHAENTREGA != '' AND strftime('%Y-%m-%d', C.FECHAENTREGA) == strftime('%Y-%m-%d', '" + fechaactual + "')" +
                          " AND (C.ESTATUS_ESTADOCONTRATO = '2' OR C.ESTATUS_ESTADOCONTRATO = '5')" +
                          " AND (ULTIMOABONO = '' OR (ULTIMOABONO <= '" + fechaLunesAnterior + "' OR ULTIMOABONO >= '" + fechaDomingoSiguiente +"')) " +
                          " ORDER BY C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.NUMEROENTREGA, C.ID_CONTRATO ASC";
                    Cursor datos = sqLiteDB.rawQuery(SQL, null);

                    if (datos.getCount() == 0) {
                        //No hay datos
                        lvListaContratos.removeAllViewsInLayout();
                        lvListaContratos.postInvalidate();
                    } else {
                        //Si hay datos

                        FilaContratos fila = new FilaContratos();

                        //Agregacion de encabezado TODOS
                        fila.setDiasAtrasoContrato("TODOS");
                        /*
                        if (mostrarOcultarIdContratoNombreLista) {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setIdContrato("TODOS");
                            }else {
                                fila.setNumeroClienteContrato("TODOS");
                            }
                        } else {
                            if(mostrarOcultarColoniaNotaLista) {
                                fila.setNombreClienteContrato("TODOS");
                            }else {
                                fila.setNumeroClienteContrato("TODOS");
                            }
                        }
                         */
                        fila.setEsRuta("");

                        estadoContratos.add(20);
                        filaContratos.add(fila);
                        encabezados.add(filaContratos.size() - 1);

                        while (datos.moveToNext()) {
                            //Obtener datos y agregarlos a la lista
                            fila = new FilaContratos();
                            fila.setIdContrato(datos.getString(0));
                            fila.setNombreClienteContrato(datos.getString(1).toUpperCase().trim());
                            fila.setUltimoAbono(datos.getString(12));
                            fila.setLocalidadClienteContrato(datos.getString(3).toUpperCase());
                            fila.setColoniaClienteContrato(datos.getString(4).toUpperCase());
                            fila.setCalleClienteContrato(datos.getString(5).toUpperCase());
                            fila.setNumeroClienteContrato(datos.getString(8).toUpperCase());
                            fila.setEsRuta("");
                            fila.setFormaPagoClienteContrato(obtenerLetraFormaPagoContrato(datos.getInt(9)));
                            fila.setNotaClienteContrato(datos.getString(10).toUpperCase());
                            if (datos.getString(6).equals("0")) {
                                fila.setEnviadoPaginaClienteContrato("P," + datos.getString(11));
                            } else {
                                fila.setEnviadoPaginaClienteContrato("S," + datos.getString(11));
                            }
                            //Observaciones historial
                            if (datos.getInt(7) > 0) {
                                //Sin observaciones
                                fila.setObservacionesClienteContrato("S");
                            } else {
                                //Con observaciones
                                fila.setObservacionesClienteContrato("N");
                            }
                            fila.setEstadoContratoClienteContrato(datos.getString(2));
                            estadoContratos.add(Integer.parseInt(datos.getString(2)));

                            filaContratos.add(fila);
                        }

                    }

                    sqLiteDB.close();
                    datos.close();

                } catch (SQLiteException e) {
                    global.escribirError(e, 15);
                    Log.i("ERRORBD", e.getMessage() + "");
                }

            }else {
                //valorHamburguesa es igual a Lunes, Martes, Miercoles, Jueves, Viernes, Sabado, Domingo, Contado, Semanal, Quincenal, Mensual
                obtenerListaContratosPorDiaUltimoAbono();
            }

        }else {
            //Asistente/Optometrista

            verificarSiQuedoContratoPendienteDiaAnteriorSinPromocion();
            verificarSiQuedaronContratosPendientesDiaAnteriorConPromocion();

            try {

                sqLiteDB = conexion.getReadableDatabase();
                String SQL = "SELECT ID_CONTRATO, NOMBRE, ESTATUS_ESTADOCONTRATO, ENVIADOPAGINA, CONTRATOENVIADO, LOCALIDADENTREGA, COLONIAENTREGA, CALLEENTREGA, NUMEROENTREGA FROM CONTRATOS" +
                        " WHERE ESTATUS_ESTADOCONTRATO IN (0,2,5,12,13,4) OR (ESTATUS_ESTADOCONTRATO == '1'" +
                        " AND (strftime('%Y-%m-%d', '" + fechaactual + "' ) == strftime('%Y-%m-%d', CREATED_AT) OR strftime('%Y-%m-%d', '" + fechaactual + "') == strftime('%Y-%m-%d', UPDATED_AT)))" +
                        " ORDER BY LOCALIDADENTREGA, COLONIAENTREGA, NUMEROENTREGA ASC";
                Cursor datos = sqLiteDB.rawQuery(SQL, null);

                if (datos.getCount() == 0) {
                    //No hay datos
                    lvListaContratos.removeAllViewsInLayout();
                    lvListaContratos.postInvalidate();
                } else {
                    //Si hay datos
                    while (datos.moveToNext()) {
                        //Obtener datos y agregarlos a la lista
                        FilaContratos fila = new FilaContratos();
                        fila.setIdContrato(datos.getString(0));
                        fila.setNombreClienteContrato(datos.getString(1).toUpperCase().trim());
                        if (datos.getString(3).equals("0")) {
                            fila.setEnviadoPaginaClienteContrato("P," + datos.getString(4));
                        }else {
                            fila.setEnviadoPaginaClienteContrato("S," + datos.getString(4));
                        }
                        fila.setEstadoContratoClienteContrato(datos.getString(2));
                        estadoContratos.add(Integer.parseInt(datos.getString(2)));
                        fila.setLocalidadClienteContrato(datos.getString(5).toUpperCase());
                        fila.setColoniaClienteContrato(datos.getString(6).toUpperCase());
                        fila.setCalleClienteContrato(datos.getString(7).toUpperCase());
                        fila.setNumeroClienteContrato(datos.getString(8).toUpperCase());
                        filaContratos.add(fila);
                    }

                }

                sqLiteDB.close();
                datos.close();

            } catch (SQLiteException e) {
                global.escribirError(e, 16);
                Log.i("ERRORBD", e.getMessage() + "");
            }

        }

        //Agregar la lista en el adaptador
        adaptadorListaContratosPrincipal adaptador = new adaptadorListaContratosPrincipal(fragmento, filaContratos, encabezados) {
            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                View view = super.getView(position, convertView, viewGroup);

                //Color de item de la lista dependiendo del estado del contrato
                switch (estadoContratos.get(position)) {
                    case 0:
                        //NO TERMINADO
                        view.setBackgroundColor(Color.parseColor("#6c757d"));
                        break;
                    case 1:
                        //TERMINADO
                        if(rol.equals("4")) {
                            //Rol cobranza
                            view.setBackgroundColor(Color.parseColor("#F48FB1"));
                        }else {
                            //Rol opto garantia
                            view.setBackgroundColor(Color.parseColor("#5bc0de"));
                        }
                        break;
                    case 2:
                        //ENTREGADO
                        if(rol.equals("4")) {
                            //Rol cobranza
                            view.setBackgroundColor(Color.parseColor("#0275d8"));
                        }else {
                            //Rol opto garantia
                            view.setBackgroundColor(Color.parseColor("#F48FB1"));
                        }
                        break;
                    case 3:
                        //PRE-CANCELADO
                        view.setBackgroundColor(Color.parseColor("#d9534f"));
                        break;
                    case 4:
                        //ABONO ATRASADO
                        if(rol.equals("4")) {
                            //Rol cobranza
                            if (diasAtrasados.get(position) <= 10) {
                                //1-10 dias
                                view.setBackgroundColor(Color.parseColor("#fff2cc"));
                            } else {
                                if (diasAtrasados.get(position) <= 20) {
                                    //11-20 dias
                                    view.setBackgroundColor(Color.parseColor("#fce5cd"));
                                } else {
                                    if (diasAtrasados.get(position) > 20) {
                                        //20 dias en delante
                                        view.setBackgroundColor(Color.parseColor("#f4cccc"));
                                    }
                                }
                            }
                        }else {
                            //Rol opto garantia
                            view.setBackgroundColor(Color.parseColor("#F48FB1"));
                        }
                        break;
                    case 5:
                        //PAGADO
                        if(rol.equals("4")) {
                            //Rol cobranza
                            view.setBackgroundColor(Color.parseColor("#5cb85c"));
                        }else {
                            //Rol opto garantia
                            view.setBackgroundColor(Color.parseColor("#F48FB1"));
                        }
                        break;
                    case 7:
                    case 9:
                    case 10:
                    case 11: //APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                        if(rol.equals("4")) {
                            //Rol cobranza
                            view.setBackgroundColor(Color.parseColor("#F48FB1"));
                        }
                        break;
                    case 12:
                        //ENVIADO
                        if(rol.equals("4")) {
                            //Rol cobranza
                            view.setBackgroundColor(Color.parseColor("#5bc0de"));
                        }else {
                            //Rol opto garantia
                            view.setBackgroundColor(Color.parseColor("#F48FB1"));
                        }
                        break;
                    case 13:
                        //POR CREAR
                        view.setBackgroundColor(Color.parseColor("#b900ff"));
                        break;
                    case 20:
                        //COLOR ENCABEZADO
                        view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        break;
                    case 6:
                        //CENCELADO
                        view.setBackgroundColor(Color.parseColor("#ff0000"));
                        break;
                }

                return view;
            }
        };

        if(sincronizar) {
            llamadaSincronizacion();
        }

        lvListaContratos.setAdapter(adaptador);
    }

    private String obtenerLetraFormaPagoContrato(int formaPago) {
        String letra = "";

        switch (formaPago) {
            case 0:
                letra = "C";
                break;
            case 1:
                letra = "S";
                break;
            case 2:
                letra = "Q";
                break;
            case 4:
                letra = "M";
                break;
        }

        return letra;
    }

    private void obtenerListaContratosPorDiaUltimoAbono() {

        try {

            boolean valorHamburguesaLunes = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaLunes"));
            boolean valorHamburguesaMartes = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaMartes"));
            boolean valorHamburguesaMiercoles = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaMiercoles"));
            boolean valorHamburguesaJueves = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaJueves"));
            boolean valorHamburguesaViernes = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaViernes"));
            boolean valorHamburguesaSabado = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaSabado"));
            boolean valorHamburguesaDomingo = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaDomingo"));
            boolean valorHamburguesaContado = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaContado"));
            boolean valorHamburguesaSemanal = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaSemanal"));
            boolean valorHamburguesaQuincenal = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaQuincenal"));
            boolean valorHamburguesaMensual = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaMensual"));

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT C.ID_CONTRATO, C.NOMBRE, C.ESTATUS_ESTADOCONTRATO, C.FECHAATRASO, C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.ENVIADOPAGINA," +
                    " (SELECT COUNT(H.ID) FROM HISTORIALCLINICO H WHERE C.ID_CONTRATO = H.ID_CONTRATO AND (LENGTH(H.OBSERVACIONES) > 0 OR LENGTH(H.OBSERVACIONESINTERNO) > 0)) AS OBSERVACION," +
                    " C.NUMEROENTREGA, C.DIASELECCIONADO, C.PAGO, C.NOTA, C.CONTRATOENVIADO, (SELECT CREATED_AT FROM ABONOS A WHERE A.ID_CONTRATO = C.ID_CONTRATO AND A.TIPOABONO NOT IN (7) " +
                    " ORDER BY A.CREATED_AT DESC LIMIT 1) AS ULTIMOABONO, C.OPCIONLUGARENTREGA, C.LOCALIDAD, C.COLONIA, C.CALLE, C.NUMERO " +
                    " FROM CONTRATOS C WHERE C.DATOS = '1' AND C.ID_ZONA = '" + id_zona + "' " +
                    " AND (ULTIMOABONO = '' OR (ULTIMOABONO <= '" + fechaLunesAnterior + "' OR ULTIMOABONO >= '" + fechaDomingoSiguiente +"')) " +
                    " ORDER BY C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.NUMEROENTREGA, C.ID_CONTRATO ASC";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0) {
                //No hay datos
                lvListaContratos.removeAllViewsInLayout();
                lvListaContratos.postInvalidate();
            } else {
                //Si hay datos

                if(valorHamburguesaContado || valorHamburguesaSemanal || valorHamburguesaQuincenal || valorHamburguesaMensual) {
                    //valorHamburguesa es igual a Contado, Semanal, Quincenal, Mensual

                    while (datos.moveToNext()) {
                        //Obtener datos y agregarlos a la lista

                        if (datos.getInt(11) == 0 && valorHamburguesaContado) {
                            agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                       datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                       datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                       datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                       datos.getString(17), datos.getString(18), datos.getString(19));
                        }

                        if (datos.getInt(11) == 1 && valorHamburguesaSemanal) {
                            agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                       datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                       datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                       datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                       datos.getString(17), datos.getString(18), datos.getString(19));
                        }

                        if (datos.getInt(11) == 2 && valorHamburguesaQuincenal) {
                            agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                      datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                      datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                      datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                      datos.getString(17), datos.getString(18), datos.getString(19));
                        }

                        if (datos.getInt(11) == 4 && valorHamburguesaMensual) {
                            agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                       datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                       datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                       datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                       datos.getString(17), datos.getString(18), datos.getString(19));
                        }

                    }

                }else {
                    //valorHamburguesa es igual a Lunes, Martes, Miercoles, Jueves, Viernes, Sabado, Domingo

                    if (Locale.getDefault().getDisplayLanguage().equals("English")) {
                        //Idioma telefono en Ingles

                        while (datos.moveToNext()) {
                            //Obtener datos y agregarlos a la lista

                            switch (obtenerDiaUltimoAbono(datos.getString(10))) {
                                case "Monday":
                                    if (valorHamburguesaLunes) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "Tuesday":
                                    if (valorHamburguesaMartes) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                  datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                  datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                  datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                  datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "Wednesday":
                                    if (valorHamburguesaMiercoles) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "Thursday":
                                    if (valorHamburguesaJueves) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "Friday":
                                    if (valorHamburguesaViernes) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "Saturday":
                                    if (valorHamburguesaSabado) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "Sunday":
                                    if (valorHamburguesaDomingo) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;

                            }

                        }

                    } else {
                        //Idioma telefono en Espanol

                        while (datos.moveToNext()) {
                            //Obtener datos y agregarlos a la lista

                            switch (obtenerDiaUltimoAbono(datos.getString(10))) {
                                case "lunes":
                                    if (valorHamburguesaLunes) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "martes":
                                    if (valorHamburguesaMartes) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "miercoles":
                                    if (valorHamburguesaMiercoles) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "jueves":
                                    if (valorHamburguesaJueves) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "viernes":
                                    if (valorHamburguesaViernes) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "sabado":
                                    if (valorHamburguesaSabado) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;
                                case "domingo":
                                    if (valorHamburguesaDomingo) {
                                        agregarAFilaListaContratos(datos.getString(0), datos.getString(1), datos.getString(4), datos.getString(5),
                                                                   datos.getString(6), datos.getString(9), datos.getString(7), datos.getInt(8),
                                                                   datos.getInt(2), datos.getString(3), datos.getInt(11), datos.getString(12),
                                                                   datos.getString(13), datos.getString(14), datos.getString(15), datos.getString(16),
                                                                   datos.getString(17), datos.getString(18), datos.getString(19));
                                    }
                                    break;

                            }

                        }

                    }

                }
                if (filaContratos.size() == 0) {
                    //No hubo contratos
                    Toast.makeText(fragmento.getActivity(), "No se encontro ningun resultado", Toast.LENGTH_LONG).show();
                }

            }

            sqLiteDB.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 17);
            Log.i("ERRORBD", e.getMessage() + "");
        } catch (Exception e) {
            global.escribirError(e, 18);
            e.printStackTrace();
        }

    }

    private void agregarAFilaListaContratos(String idContrato, String nombreClienteContrato, String localidadClienteContrato, String coloniaClienteContrato, String calleClienteContrato,
                                            String numeroClienteContrato, String enviadoPaginaClienteContrato, int observacionesClienteContrato, int estadoContrato, String fechaatraso,
                                            int formapago, String notaClienteContrato, String contratoEnviado, String ultimoAbono, String opcionLugarEntrega, String localidadClienteContrato2,
                                            String coloniaClienteContrato2, String calleClienteContrato2, String numeroClienteContrato2) {

        FilaContratos fila = new FilaContratos();
        fila.setIdContrato(idContrato);
        fila.setNombreClienteContrato(nombreClienteContrato.toUpperCase());
        fila.setUltimoAbono(ultimoAbono);
        if(estadoContrato == 12 && opcionLugarEntrega.equals("0")){
            //Contrato en ENVIADO y Entrega de producto en lugar de venta
            fila.setLocalidadClienteContrato(localidadClienteContrato2.toUpperCase());
            fila.setColoniaClienteContrato(coloniaClienteContrato2.toUpperCase());
            fila.setCalleClienteContrato(calleClienteContrato2.toUpperCase());
            fila.setNumeroClienteContrato(numeroClienteContrato2.toUpperCase());
        }else{
            //Entrega de producto en lugar de cobranza
            fila.setLocalidadClienteContrato(localidadClienteContrato.toUpperCase());
            fila.setColoniaClienteContrato(coloniaClienteContrato.toUpperCase());
            fila.setCalleClienteContrato(calleClienteContrato.toUpperCase());
            fila.setNumeroClienteContrato(numeroClienteContrato.toUpperCase());
        }
        fila.setEsRuta("0");
        fila.setFormaPagoClienteContrato(obtenerLetraFormaPagoContrato(formapago));
        fila.setNotaClienteContrato(notaClienteContrato);
        if (enviadoPaginaClienteContrato.equals("0")) {
            fila.setEnviadoPaginaClienteContrato("P," + contratoEnviado);
        } else {
            fila.setEnviadoPaginaClienteContrato("S," + contratoEnviado);
        }
        //Observaciones historial
        if (observacionesClienteContrato > 0) {
            //Sin observaciones
            fila.setObservacionesClienteContrato("S");
        } else {
            //Con observaciones
            fila.setObservacionesClienteContrato("N");
        }
        estadoContratos.add(estadoContrato);
        if (estadoContrato == 4) {
            //Estado del contrato es igual a ABONOATRASADO
            diasAtrasados.add(obtenerDiasAtrasados(fechaatraso));
        } else {
            //Estado del contrato es diferente a ABONOATRASADO por lo tanto no tiene fechaatraso
            diasAtrasados.add(0);
        }

        filaContratos.add(fila);

    }

    private String obtenerDiaUltimoAbono(String fechaUltimoAbono) {

        String diaSemana = "";

        if(fechaUltimoAbono.length() > 0) {
            //fechaUltimoAbono es diferente de vacio

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = sdf.parse(fechaUltimoAbono);
                diaSemana = (String) DateFormat.format("EEEE", date); //Obtener nombre del dia del ultimoabono

                if (!Locale.getDefault().getDisplayLanguage().equals("English")) {
                    //Idioma es igual a espanol
                    String cadenaNormalize = Normalizer.normalize(diaSemana, Normalizer.Form.NFD);
                    diaSemana = cadenaNormalize.replaceAll("[^\\p{ASCII}]", ""); //Quitar acentos
                }
            } catch (Exception e) {
                global.escribirError(e, 19);
                e.printStackTrace();
            }

        }

        return diaSemana;
    }

    private void verificarSiQuedoContratoPendienteDiaAnteriorSinPromocion() {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID_CONTRATO FROM CONTRATOS WHERE DATOS = '1' AND ID_USUARIOCREACION = '" + idusuario +
                    "' AND strftime('%Y-%m-%d', CREATED_AT) < strftime('%Y-%m-%d', '" + fechaactual + "')" +
                    " AND ESTATUS_ESTADOCONTRATO = '0' AND ID_PROMOCION = ''";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro contrato sin promocion de ayer");
            }

            if (datos.moveToFirst()) {
                global.actualizarAtributoTabla("CONTRATOS", "CREATED_AT", fechaactual + time, "ID_CONTRATO", datos.getString(0));
                global.actualizarAtributoTabla("CONTRATOS", "UPDATED_AT", fechaactual + time, "ID_CONTRATO", datos.getString(0));
                global.actualizarAtributoTabla("CONTRATOS", "ENVIADOPAGINA", "0", "ID_CONTRATO", datos.getString(0));
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 20);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void verificarSiQuedaronContratosPendientesDiaAnteriorConPromocion() {

        time = new SimpleDateFormat(" HH:mm:ss").format(new Date());

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID_CONTRATO FROM CONTRATOS WHERE DATOS = '1' AND ID_USUARIOCREACION = '" + idusuario +
                    "' AND strftime('%Y-%m-%d', CREATED_AT) < strftime('%Y-%m-%d', '" + fechaactual + "')" +
                    " AND ID_PROMOCION != '' AND PROMOCIONTERMINADA = '0'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No se encontro contrato con promocion de ayer");
            }

            if (datos.moveToFirst()) {
                global.actualizarAtributoTabla("CONTRATOS", "CREATED_AT", fechaactual + time, "ID_CONTRATO", datos.getString(0));
                global.actualizarAtributoTabla("CONTRATOS", "UPDATED_AT", fechaactual + time, "ID_CONTRATO", datos.getString(0));
                global.actualizarAtributoTabla("CONTRATOS", "CREATED_AT", fechaactual + time, "IDCONTRATORELACION", datos.getString(0));
                global.actualizarAtributoTabla("CONTRATOS", "UPDATED_AT", fechaactual + time, "IDCONTRATORELACION", datos.getString(0));
                global.actualizarAtributoTabla("CONTRATOS", "ENVIADOPAGINA", "0", "ID_CONTRATO", datos.getString(0));
                global.actualizarAtributoTabla("CONTRATOS", "ENVIADOPAGINA", "0", "IDCONTRATORELACION", datos.getString(0));
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 21);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: verificarContratosConTipoAbonoLiquidadoYTotalMayorACero
      Descripcion: Validacion de contratos con abonos liquidados antes de cargar lista de contratos.
      (Si tiene el contrato un abono de tipo liquidado && total > 0 del contrato entonces cambiar estatus del contrato y cambiar el abono tipo liquidado a tipo abono normal)
    */
    private void verificarContratosConTipoAbonoLiquidadoYTotalMayorACero() {

        try {

            sqLiteDB = conexion.getReadableDatabase();

            String SQL = "SELECT C.ID_CONTRATO, C.COSTOATRASO, C.ENTREGAPRODUCTO, A.ID" +
                    " FROM ABONOS A INNER JOIN CONTRATOS C ON C.ID_CONTRATO = A.ID_CONTRATO WHERE A.TIPOABONO = '6' AND CAST(C.TOTAL as DOUBLE) > 0";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0) {
                //No hay datos
                Log.i("MENSAJE", "No hay abono tipo liquidado y total > 0");
            } else {
                //Si hay datos

                while (datos.moveToNext()) {
                    //Obtener datos

                    if(global.contadorHistorialesConGarantia(datos.getString(0), true) == 0) {
                        //No tiene el contrato historiales con garantia
                        String estadoContrato = null;

                        if (datos.getDouble(1) > 0) {
                            //Tiene costo atraso
                            estadoContrato = "4";
                        }
                        if (datos.getInt(2) > 0 && datos.getDouble(1) < 1) {
                            //Tiene el abono de entrega producto y no tiene costo atrasado
                            estadoContrato = "2";
                        }
                        if (datos.getInt(2) < 1 && datos.getDouble(1) < 1) {
                            //No tiene el abono de entrega producto y no tiene costo atrasado
                            estadoContrato = "12";
                        }

                        //Actualizar atributo ESTATUS_ESTADOCONTRATO del contrato
                        global.actualizarAtributoTabla("CONTRATOS", "ESTATUS_ESTADOCONTRATO", estadoContrato, "ID_CONTRATO", datos.getString(0));
                        global.actualizarAtributoTabla("CONTRATOS", "ENVIADOPAGINA", "0", "ID_CONTRATO", datos.getString(0));
                        //Actualizar actualizar el tipo abono liquidado a tipo abono normal del contrato actual
                        actualizarAbonoContratoLiquidadoANormal(datos.getString(3));

                    }

                }

            }

            sqLiteDB.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 22);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void verificarContratosConTipoAbonoEntregaProductoYCambiarEstadoContratoAEntregado() {

        try {

            sqLiteDB = conexion.getReadableDatabase();

            String SQL = "SELECT ID_CONTRATO, TOTAL" +
                    " FROM CONTRATOS WHERE ENTREGAPRODUCTO = '1' AND ESTATUS_ESTADOCONTRATO = '12'";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0) {
                //No hay datos
                Log.i("MENSAJE", "No hay abono tipo entrega producto y estado del contrato sea igual a TERMINADO");
            } else {
                //Si hay datos

                while (datos.moveToNext()) {
                    //Obtener datos

                    if(global.contadorHistorialesConGarantia(datos.getString(0), true) == 0) {
                        //No tiene el contrato historiales con garantia
                        if (datos.getDouble(1) > 0) {
                            //Actualizar atributo ESTATUS_ESTADOCONTRATO a ENTREGADO del contrato
                            global.actualizarAtributoTabla("CONTRATOS", "ESTATUS_ESTADOCONTRATO", "2", "ID_CONTRATO", datos.getString(0));
                        } else {
                            //Actualizar atributo ESTATUS_ESTADOCONTRATO a LIQUIDADO del contrato
                            global.actualizarAtributoTabla("CONTRATOS", "ESTATUS_ESTADOCONTRATO", "5", "ID_CONTRATO", datos.getString(0));
                        }
                        global.actualizarAtributoTabla("CONTRATOS", "ENVIADOPAGINA", "0", "ID_CONTRATO", datos.getString(0));
                    }

                }

            }

            sqLiteDB.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 23);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void verificarSiHayMasDeUnAbonoLiquidadoEnLosContratosYCambiarANormal() {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();

            String SQL = "SELECT COUNT(A.ID), C.ID_CONTRATO" +
                    " FROM ABONOS A INNER JOIN CONTRATOS C ON C.ID_CONTRATO = A.ID_CONTRATO WHERE A.TIPOABONO = '6' AND C.ESTATUS_ESTADOCONTRATO = '5'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0) {
                //No hay datos
                Log.i("MENSAJE", "No hay abono tipo liquidado y total > 0");
            } else {
                //Si hay datos

                while (datos.moveToNext()) {
                    //Obtener datos
                    if(datos.getInt(0) > 1) {
                        //Tiene mas de 1 abono liquidado el contrato
                        obtenerAbonosLiquidadosContrato(datos.getString(1));
                    }
                }

            }

            sqLiteDB2.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 24);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void obtenerAbonosLiquidadosContrato(String idContrato) {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();

            String SQL = "SELECT A.ID" +
                    " FROM ABONOS A INNER JOIN CONTRATOS C ON C.ID_CONTRATO = A.ID_CONTRATO WHERE A.TIPOABONO = '6' AND C.ID_CONTRATO = '" + idContrato + "'" +
                    " ORDER BY strftime('%Y-%m-%d', A.CREATED_AT) ASC";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0) {
                //No hay datos
                Log.i("MENSAJE", "No hay abono tipo liquidado y total > 0");
            } else {
                //Si hay datos

                int pos = datos.getCount();

                while (datos.moveToNext()) {
                    //Obtener datos
                    if(pos != 1) {
                        actualizarAbonoContratoLiquidadoANormal(datos.getString(0));
                    }
                    pos--;
                }

            }

            sqLiteDB2.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 25);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    /*Metodo/Funcion: actualizarAbonoContratoLiquidadoANormal
      Descripcion: Actualizar/Cambiar el abono tipo liquidado a tipo abono normal
    */
    private void actualizarAbonoContratoLiquidadoANormal(String idAbono) {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

            String consulta = "UPDATE ABONOS SET TIPOABONO = '0', ENVIADOPAGINA = '0' WHERE ID='" + idAbono + "'";

            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 26);
            Log.i("ERRORBD", e.getMessage() + "");
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
                global.escribirError(e, 27);
                Log.i("MENSAJE", "ERROR: " + e.toString());
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

    /*Metodo/Funcion: abrirContrato
      Descripcion: Abrir contratos, dependiendo del estado del contrato
    */
    public void abrirContrato(int position) {

        String estadoContrato = global.obtenerAtributoContrato(filaContratos.get(position).getIdContrato(), "ESTATUS_ESTADOCONTRATO");

        if(rol.equals("4")) {
            //Cobranza

            try {

                //Validar si fecha de ultimo abono y ultima conexion tienen menos de 12 hrs
                SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                Date horaActual = new Date();
                Date ultimaConexion = new Date();

                horaActual = convetDateFormat.parse(fechaHora);
                ultimaConexion = (!obtenerRol.obtenerAtributoUsuarioLogeado("ULTIMACONEXION").equals("null"))? convetDateFormat.parse(obtenerRol.obtenerAtributoUsuarioLogeado("ULTIMACONEXION")):horaActual;

                long horasTranscurridasUltimaConexion = horaActual.getTime() - ultimaConexion.getTime();

                if(TimeUnit.HOURS.convert(horasTranscurridasUltimaConexion, TimeUnit.MILLISECONDS) <= 12) {
                    //Ultima conexion del usuario menor o igual a 12 hrs

                    if(estadoContrato.equals("12") || estadoContrato.equals("2") || estadoContrato.equals("4") || estadoContrato.equals("5")
                            || estadoContrato.equals("1") || estadoContrato.equals("7") || estadoContrato.equals("9") || estadoContrato.equals("10") || estadoContrato.equals("11")) {
                        //ENVIADO, ENTREGADO, ABONO ATRASADO, PAGADO, APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, O EN PROCESO DE ENVIO
                        if(!camara.mayRequestStoragePermission()) {
                            Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                            if(!Environment.isExternalStorageManager()){
                                Toast.makeText(fragmento.getActivity(), "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                                camara.mandarAPantallaConfiguracionTelefono();
                            }else {
                                camara.crearDirectorios();
                                camara.crearDirectorios();
                                mandarAPantallaVerContrato(filaContratos.get(position).getIdContrato());
                            }
                        }else {
                            camara.crearDirectorios();
                            camara.crearDirectorios();
                            mandarAPantallaVerContrato(filaContratos.get(position).getIdContrato());
                        }
                    }

                }else{
                    //Ultima conexion hace mas de 12 hrs
                    sincronizacion.sesioncaducada();
                }

            } catch (ParseException e) {
                global.escribirError(e, 320);
                Log.i("MENSAJE", "ERROR: " + e.toString());
            }

        }else {
            //Asistente/Optometrista

            if(estadoContrato.equals("0") || estadoContrato.equals("1") ) {
                Fragment verificadorFragment = new verContrato();
                Bundle bundle = new Bundle();
                bundle.putString("ultimoIdContratoCreado", filaContratos.get(position).getIdContrato());
                verificadorFragment.setArguments(bundle);
                FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, verificadorFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            if(estadoContrato.equals("13")) {
                Fragment verificadorFragment = new nuevoContrato();
                Bundle bundle = new Bundle();
                if(global.obtenerAtributoContrato(filaContratos.get(position).getIdContrato(), "IDCONTRATORELACION").length() != 0) {
                    //Contrato hijo
                    bundle.putString("idContratoPadre", global.obtenerAtributoContrato(filaContratos.get(position).getIdContrato(), "IDCONTRATORELACION"));
                }else {
                    bundle.putString("idContratoPadre", "");
                }
                bundle.putString("idContratoHijo", filaContratos.get(position).getIdContrato());
                bundle.putBoolean("contratoPromocion", true);
                bundle.putBoolean("validarTratamientosContrato", true);
                verificadorFragment.setArguments(bundle);
                FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, verificadorFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            if(estadoContrato.equals("2") || estadoContrato.equals("5") || estadoContrato.equals("12") || estadoContrato.equals("4")) {
                Fragment verificadorFragment = new verContrato();
                Bundle bundle = new Bundle();
                bundle.putString("ultimoIdContratoCreado", filaContratos.get(position).getIdContrato());
                verificadorFragment.setArguments(bundle);
                FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, verificadorFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        }
    }

    private void mandarAPantallaVerContrato(String idContrato) {
        quitarBuscadorYAgregarEncabezado();
        Fragment verificadorFragment = new verContrato();
        Bundle bundle = new Bundle();
        bundle.putString("ultimoIdContratoCreado", idContrato);
        verificadorFragment.setArguments(bundle);
        FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();

        agregarAUltimoContratoVisto(idContrato);
    }

    private void quitarBuscadorYAgregarEncabezado() {
        llBuscadorIcono.setVisibility(View.GONE);
        llBuscador.setVisibility(View.GONE);
        ivQuitarBuscadorIcono.setVisibility(View.GONE);
        ((MainActivity)fragmento.getActivity()).setTitle(nombreUsuarioLogeado);
        ((MainActivity)fragmento.getActivity()).hideMenuHamburguesa();
    }

    /*Metodo/Funcion: verificarContratoFechaEntregaOPrioritariosOPeriodo
      Descripcion: Validar si (Fecha entrega del contrato / fecha diaseleccionado / fechacobroini y fechacobrofin) estan dentro de la fechaactual
    */
    private boolean verificarContratoFechaEntregaOPrioritariosOPeriodo(String idContrato, String atributo) {

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL;

            if(atributo.length() == 0) {
                //fechacobroini y fechacobrofin
                SQL = "SELECT CASE WHEN strftime('%Y-%m-%d', '" + fechaactual + "')" +
                        " BETWEEN strftime('%Y-%m-%d', FECHACOBROINI) AND strftime('%Y-%m-%d', FECHACOBROFIN) THEN 1 ELSE 0 END" +
                        " FROM CONTRATOS WHERE ID_CONTRATO='" + idContrato + "'";
            }else {
                //diaseleccionado
                SQL = "SELECT CASE WHEN strftime('%Y-%m-%d', " + atributo + ") == strftime('%Y-%m-%d', '" + fechaactual + "') THEN 1 ELSE 0 END" +
                        " FROM CONTRATOS WHERE ID_CONTRATO='" + idContrato + "'";
            }

            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No hay contrato");
            }

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
            global.escribirError(e, 28);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return false;
    }

    /*Metodo/Funcion: mostrarAlertCancelarContrato
      Descripcion: Muestra alerta para poder cancelar contrato
    */
    public void mostrarAlertCancelarContrato(int i) {

        if(rol.equals("4")) {
            //COBRANZA

            if(!global.obtenerAtributoContrato(filaContratos.get(i).getIdContrato(), "ESTATUS_ESTADOCONTRATO").equals("6")) {
                //Estado del contrato diferente a rechazado

                if (camara.mayRequestStoragePermission()) {
                    //TIENE PERMISOS

                    if (filaContratos.get(i).getEsRuta().length() > 0) {
                        //Tiene un 0 o un 1

                        boolean esRuta = "1".equals(filaContratos.get(i).getEsRuta());

                        String[] opciones;

                        if (esRuta) {
                            //Esta en la seccion de RUTA
                            opciones = new String[]{"Mapa contratos", "Copiar código", "Agregar dia temporal", "Quitar de ruta", "Mandar contrato seleccionado a mapa",
                                    "Mandar contratos a mapa", "Mandar hacia arriba", "Mandar hacia abajo", "Mandar primera posición",
                                    "Mandar última posición", "Limpiar lista ruta"};
                        } else {
                            //No esta en la seccion de RUTA
                            opciones = new String[]{"Mapa contratos", "Copiar código", "Agregar dia temporal", "Mandar a ruta"};
                        }

                        if (!esRuta && existeIdContratoEnTablaRuta(filaContratos.get(i).getIdContrato())) {
                            //No esta en la seccion de ruta y el idcontrato ya existe en la seccion de ruta
                            Toast.makeText(fragmento.getActivity(), "El contrato ya se encuentra en ruta", Toast.LENGTH_LONG).show();
                        } else {

                            String titleAlertaPrincipal = "";
                            //Mostrar idcontrato en alerta
                            titleAlertaPrincipal = "Elegir una opción para\n" + filaContratos.get(i).getIdContrato();
                           /*
                            if (mostrarOcultarIdContratoNombreLista) {
                                titleAlertaPrincipal = "Elegir una opción para\n" + filaContratos.get(i).getIdContrato();
                            } else {
                                //Mostrar nombre del contrato en alerta
                                titleAlertaPrincipal = "Elegir una opción para\n" + filaContratos.get(i).getNombreClienteContrato();
                            }
                            */

                            String idContratoSeleccionado = filaContratos.get(i).getIdContrato();
                            int contadorOPosicion = obtenerContadorRegistrosTablaRuta(true);

                            if (contadorOPosicion > 0) {
                                //Hay mas de un registro en la tabla
                                contadorOPosicion = obtenerContadorRegistrosTablaRuta(false) + 1;
                            }

                            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                            int finalContadorOPosicion = contadorOPosicion;
                            alerta.setTitle(titleAlertaPrincipal).setItems(opciones, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    switch (i) {
                                        case 0:
                                            //Crear marcadores en mapa de todos los contratos
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
                                                Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                        case 1:
                                            //Copiar idcontrato en portapapeles
                                            ClipboardManager clipboard = (ClipboardManager) fragmento.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText(idContratoSeleccionado, idContratoSeleccionado);
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(fragmento.getContext(), "Codigo del contrato copiado", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 2:
                                            //Mostrar alerta agregar dia temporal
                                            mostrarAlertDialogAgregarDiaTemporal(idContratoSeleccionado);
                                            break;
                                        case 3:
                                            //Mandar a ruta o eliminar de la ruta
                                            String mensaje = "";

                                            if (valorHamburguesaRuta.equals("8")) {
                                                //No hay seleccionada una ruta (Esta en lunes, martes, contado, semanal, etc)
                                                mensaje = "Para continuar selecciona una ruta";
                                            } else {
                                                //Esta seleccionado en una ruta

                                                try {

                                                    if (esRuta) {
                                                        //Esta en la seccion de RUTA (Se actualizara el ESTADO de la tabla RUTA)
                                                        global.actualizarAtributoTabla("RUTA", "ENVIADOPAGINA = '0', ESTADO", "0",
                                                                "DIA = '" + valorHamburguesaRuta + "' AND ID_CONTRATO", idContratoSeleccionado);
                                                        mensaje = "Se quito correctamente de la ruta";
                                                    } else {
                                                        //No esta en la seccion de RUTA (Agregar idcontrato a tabla RUTA)

                                                        String idAlfanumerico = generarIdAlfanumerico.validarSiExisteYObtenerIdAlfanumerico("RUTA", 20);

                                                        SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
                                                        ContentValues valores = new ContentValues();
                                                        valores.put("ID", idAlfanumerico);
                                                        valores.put("DIA", valorHamburguesaRuta);
                                                        valores.put("ID_CONTRATO", idContratoSeleccionado);
                                                        valores.put("ID_USUARIO", idusuario);
                                                        valores.put("POSICION", String.valueOf(finalContadorOPosicion));
                                                        valores.put("ESTADO", "1");
                                                        valores.put("ENVIADOPAGINA", "0");
                                                        sqLiteDB2.insert("RUTA", null, valores);
                                                        sqLiteDB2.close();
                                                        mensaje = "Se agrego correctamente el contrato a la ruta";
                                                    }

                                                } catch (SQLiteException e) {
                                                    global.escribirError(e, 29);
                                                    Log.i("ERRORBD", e.getMessage() + "");
                                                }

                                                if (etBuscador.getText().toString().length() > 0) {
                                                    //Esta filtrado
                                                    llamadaSincronizacion();
                                                    llProgress.setVisibility(View.GONE);
                                                    obtenerListaContratosBuscador(etBuscador.getText().toString());
                                                } else {
                                                    //No esta filtrado
                                                    obtenerListaContratos(true);
                                                }

                                            }

                                            Toast.makeText(fragmento.getActivity(), mensaje, Toast.LENGTH_LONG).show();
                                            break;
                                        case 4:
                                            //Mandar contrato unico a mapa
                                            //Validar si tiene valor atributo coordenada
                                            //Si esta vacio obtener calle,numero,colonia,localidad

                                            if (internet.verificarConexionInternet()) {
                                                //Si tienes conexion a internet

                                                String coordenadasContrato = global.obtenerAtributoContrato(idContratoSeleccionado, "COORDENADAS");

                                                if (coordenadasContrato.length() > 0) {
                                                    //Tiene coordenadas el contrato

                                                    //Actualizar ubicacion actual
                                                    if (localizacion.actualizarUbicacion(localizacion)) {
                                                        //Gps esta encendio y tiene permisos

                                                        //Generar metodo en segundo plano para obtener coordenadas
                                                        Handler handler = new Handler();
                                                        runnable = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (localizacion.getLatitud() != null) {
                                                                    //si latitud es diferente a vacio mandamos llamar el metodo trazarRutaUnicoDestino y enviamos los datos
                                                                    localizacion.trazarRutaDestinos(localizacion.getLatitud() + "," + localizacion.getLongitud(),
                                                                            "/" + coordenadasContrato);
                                                                    handler.removeCallbacks(runnable);
                                                                } else {
                                                                    handler.postDelayed(runnable, 2000);
                                                                }
                                                            }
                                                        };
                                                        runnable.run();

                                                    }

                                                } else {
                                                    //No tiene coordenadas el contrato
                                                    ClipboardManager clipboard2 = (ClipboardManager) fragmento.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                                    ClipData clip2 = ClipData.newPlainText(idContratoSeleccionado, idContratoSeleccionado);
                                                    clipboard2.setPrimaryClip(clip2);
                                                    Toast.makeText(fragmento.getActivity(), "El contrato no tiene ubicación", Toast.LENGTH_LONG).show();
                                                }

                                            } else {
                                                //No tiene internet
                                                Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                        case 5:
                                            ///Mandar contratos a mapa a partir de indice seleccionado
                                            if (internet.verificarConexionInternet()) {
                                                //Si tienes conexion a internet
                                                crearRutaDesdeIndiceSeleccionado(idContratoSeleccionado);
                                            } else {
                                                Toast.makeText(fragmento.getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                        case 6:
                                            //Mandar hacia arriba
                                            cambiarPosicionContratoSeleccionado(idContratoSeleccionado, true);
                                            break;
                                        case 7:
                                            //Mandar hacia abajo
                                            cambiarPosicionContratoSeleccionado(idContratoSeleccionado, false);
                                            break;
                                        case 8:
                                            //Mandar primera posicion
                                            cambiarPosicionesListaRuta(idContratoSeleccionado, true);
                                            break;
                                        case 9:
                                            //Mandar ultima posicion
                                            cambiarPosicionesListaRuta(idContratoSeleccionado, false);
                                            break;
                                        case 10:
                                            //Limpiar lista ruta
                                            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                                            alerta.setTitle("Confirmación").setMessage("¿Estas seguro de eliminar todo de la lista de ruta?")
                                                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            global.actualizarAtributoTabla("RUTA", "ENVIADOPAGINA = '0', ESTADO", "0", "DIA", valorHamburguesaRuta);
                                                            obtenerListaContratos(true);
                                                            Toast.makeText(fragmento.getActivity(), "Se elimino correctamente la lista de ruta", Toast.LENGTH_LONG).show();
                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();
                                            break;
                                    }
                                }
                            }).create().show();

                        }

                    }

                }

            }

        } else {
            //ASIST/OPTO

            String estadoContrato = global.obtenerAtributoContrato(filaContratos.get(i).getIdContrato(), "ESTATUS_ESTADOCONTRATO");

            if ((estadoContrato.equals("0") || estadoContrato.equals("1"))) {
                //Rol es asistente/optometrista y el estado del contrato esta en NO TERMINADO y TERMINADO

                String[] opciones;
                String idContratoPadre = global.obtenerAtributoContrato(filaContratos.get(i).getIdContrato(), "IDCONTRATORELACION");
                String contratoenviado = global.obtenerAtributoTabla("CONTRATOS", "CONTRATOENVIADO", "ID_CONTRATO", filaContratos.get(i).getIdContrato());

                String idContratoMostrarAlerta = filaContratos.get(i).getIdContrato();
                if (contratoenviado.equals("0")) {
                    idContratoMostrarAlerta = "POR ASIGNAR";
                }

                if (estadoContrato.equals("0") && idContratoPadre.equals("")) {
                    //NO TERMINADO y es padre
                    opciones = new String[]{"Duplicar", "Editar", "Cancelar contrato"};
                } else {
                    //TERMINADO o NO TERMINADO y es hijo
                    if(estadoContrato.equals("0")){
                        //NO TERMINADO
                        opciones = new String[]{"Duplicar", "Editar"};
                    }else{
                        opciones = new String[]{"Duplicar"};
                    }
                }

                AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                String finalIdContratoMostrarAlerta = idContratoMostrarAlerta;
                alerta.setTitle("Elegir una opción para\n" + idContratoMostrarAlerta).setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {

                        switch (j) {
                            case 0:
                                //Duplicar contrato

                                String tienePromocion;
                                if(idContratoPadre.equals("")) {
                                    //Es padre
                                    tienePromocion = global.obtenerUnResultadoQuery("SELECT ESTADO FROM PROMOCIONCONTRATO WHERE ID_CONTRATO= '" + filaContratos.get(i).getIdContrato() + "'");
                                }else {
                                    //Es hijo
                                    tienePromocion = global.obtenerUnResultadoQuery("SELECT ESTADO FROM PROMOCIONCONTRATO WHERE ID_CONTRATO= '" + idContratoPadre + "'");
                                }

                                if(tienePromocion.equals("") || tienePromocion.equals("0")) {
                                    //No tiene promocion o ya fue creada pero se desactivo
                                    mandarAPantallaNuevoContratoDuplicar(filaContratos.get(i).getIdContrato(),false);
                                }else {
                                    //Tiene promocion y esta activa
                                    if(global.obtenerAtributoContrato(filaContratos.get(i).getIdContrato(), "PROMOCIONTERMINADA").equals("1")) {
                                        //Ya termino la promocion
                                        mandarAPantallaNuevoContratoDuplicar(filaContratos.get(i).getIdContrato(),false);
                                    }else {
                                        //No se ha terminado la promocion
                                        Toast.makeText(fragmento.getActivity(), "Para duplicar termina los contratos pendientes de la promoción", Toast.LENGTH_LONG).show();
                                    }
                                }
                                break;
                            case 1:
                                //Actualizar  - Contrato NO TERMINADO
                                mandarAPantallaNuevoContratoDuplicar(filaContratos.get(i).getIdContrato(), true);
                                break;
                            case 2:
                                //Cancelar contrato
                                final EditText etEspecificacion = new EditText(fragmento.getActivity());
                                etEspecificacion.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
                                etEspecificacion.setHint("Especificación");

                                AlertDialog.Builder alerta2 = new AlertDialog.Builder(fragmento.getActivity());
                                alerta2.setTitle("¿Deseas cancelar el contrato " + finalIdContratoMostrarAlerta + "?").setMessage("Específica porque")
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

                                final AlertDialog dialog = alerta2.create();
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
                                            historialMovimientosContrato.guardarHistorialMovimientosContrato(filaContratos.get(i).getIdContrato(), etEspecificacion.getText().toString(), "0");

                                            //Actualizar ESTATUS_ESTADOCONTRATO a PRE-CANCELADO
                                            global.actualizarAtributoTabla("CONTRATOS", "ESTATUS_ESTADOCONTRATO", "3", "ID_CONTRATO", filaContratos.get(i).getIdContrato());
                                            //Actualizar atributo ENVIADOPAGINA a 0 del contrato actual
                                            global.actualizarAtributoTabla("CONTRATOS", "ENVIADOPAGINA", "0", "ID_CONTRATO", filaContratos.get(i).getIdContrato());
                                            //Actualizar atributo CONTRATOENVIADO a 0 del contrato actual
                                            global.actualizarAtributoTabla("CONTRATOS", "CONTRATOENVIADO", "0", "ID_CONTRATO", filaContratos.get(i).getIdContrato());
                                            //Actualizar lista de contratos
                                            obtenerListaContratos(true);

                                            Toast.makeText(fragmento.getActivity(), "Se cancelo correctamente el contrato", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        }

                                    }
                                });
                                break;
                        }
                    }
                }).create().show();

            }

            if (estadoContrato.equals("13")) {
                //Rol es asistente/optometrista y el estado del contrato esta en POR CREAR

                AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                alerta.setTitle("Eliminar contrato").setMessage("¿Deseas eliminar el contrato POR ASIGNAR?")
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
                        String idContrato = filaContratos.get(i).getIdContrato();
                        actualizarContratoPorCrearAVacioYEliminarHistorialClinico(filaContratos.get(i).getIdContrato());
                        //Actualizar lista de contratos
                        obtenerListaContratos(false);
                        Toast.makeText(fragmento.getActivity(), "Se elimino correctamente el contrato", Toast.LENGTH_LONG).show();
                        //Eliminar movimiento de busqueda cliente abono atrasado/lio/fuga
                        historialMovimientosContrato.eliminarMovimientoHistorialContrato(idContrato,"TIPOMENSAJE","1");
                        dialog.dismiss();
                    }
                });

            }

        }
    }

    private void mandarAPantallaNuevoContratoDuplicar(String idContrato, boolean contratoActualizar) {
        Fragment verificadorFragment = new nuevoContrato();
        Bundle bundle = new Bundle();
        bundle.putString("idContratoPadre", "");
        bundle.putString("idContratoHijo", idContrato);
        bundle.putBoolean("contratoPromocion", true);
        bundle.putBoolean("contratoActualizar", contratoActualizar);
        bundle.putBoolean("validarTratamientosContrato", true);
        verificadorFragment.setArguments(bundle);
        FragmentTransaction transaction = ((FragmentActivity) fragmento.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void crearRutaDesdeIndiceSeleccionado(String idContratoSeleccionado) {

        String posicionContratoSeleccionado = global.obtenerAtributoTabla("RUTA","POSICION",
                "ESTADO = '1' AND DIA = '" + valorHamburguesaRuta +"' AND ID_CONTRATO",idContratoSeleccionado);
        String direccionDestino = "";
        List<String> idsContratosDireccionesInvalidas = new ArrayList<String>();

        try {

            String SQL = "SELECT (SELECT C.ID_CONTRATO FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)," +
                    " (SELECT C.COORDENADAS FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)" +
                    " FROM RUTA R WHERE R.ID_CONTRATO IN (SELECT C.ID_CONTRATO FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO) " +
                    "AND R.ESTADO = '1' AND R.DIA = '" + valorHamburguesaRuta + "' AND CAST(R.POSICION AS INTERGER) >= CAST('"+posicionContratoSeleccionado+"' AS INTERGER)" +
                    " ORDER BY CAST(R.POSICION AS INTERGER) ASC";

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() > 0) {
                while (datos.moveToNext()) {
                    if(datos.getString(1).length() > 0) {
                        //Tiene coordenadas el contrato
                        direccionDestino += "/" + datos.getString(1);
                    } else {
                        //No tiene coordenadas el contrato
                        idsContratosDireccionesInvalidas.add(datos.getString(0));
                    }
                }
            }

            sqLiteDB2.close();
            datos.close();

        } catch (SQLiteException e){
            global.escribirError(e, 30);
            Log.i("ERRORDB",e.getMessage()+"");
        }

        if(direccionDestino.length() > 0) {
            //Hay contratos a trazar

            //Actualizar ubicacion actual
            if(localizacion.actualizarUbicacion(localizacion)) {
                //Gps esta encendio y tiene permisos

                Handler handler = new Handler();
                String finalDireccionDestino = direccionDestino;
                runnable = new Runnable() {
                    @Override
                    public void run() {

                        if (localizacion.getLatitud() != null) {
                            //si latitud es diferente a vacio mandamos llamar el metodo  trazarRutaDestino y crearRutaDesdeIndiceSeleccionado para obtener destinos

                            if (idsContratosDireccionesInvalidas.size() > 0) {
                                //Hay contratos sin ubicacion

                                String idsContratosSinUbicacion = "";
                                //Recorremos arreglo
                                for (int i = 0; i < idsContratosDireccionesInvalidas.size(); i++) {
                                    idsContratosSinUbicacion += idsContratosDireccionesInvalidas.get(i) + "\n";
                                }

                                ClipboardManager clipboard = (ClipboardManager) fragmento.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText(idsContratosSinUbicacion, idsContratosSinUbicacion);
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(fragmento.getContext(), "Texto copiado", Toast.LENGTH_SHORT).show();

                                //Imprime alerta de contratos con direccion incorrecta
                                AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
                                alerta.setTitle("Contratos sin ubicación").setMessage(idsContratosSinUbicacion)
                                        .setPositiveButton("LEIDO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                            }

                            localizacion.trazarRutaDestinos(localizacion.getLatitud() + "," + localizacion.getLongitud(), finalDireccionDestino);

                            handler.removeCallbacks(runnable);
                        } else {
                            handler.postDelayed(runnable, 2000);
                        }
                    }
                };
                runnable.run();

            }

        }else {
            //Todos los contratos no tienen ubicacion
            Toast.makeText(fragmento.getActivity(),"Todos los contratos no tienen ubicación",Toast.LENGTH_LONG).show();

            String idsContratosSinUbicacion = "";
            //Recorremos arreglo
            for (int i = 0; i < idsContratosDireccionesInvalidas.size(); i++) {
                idsContratosSinUbicacion += idsContratosDireccionesInvalidas.get(i) + "\n";
            }
            ClipboardManager clipboard = (ClipboardManager) fragmento.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(idsContratosSinUbicacion, idsContratosSinUbicacion);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(fragmento.getContext(), "Texto copiado", Toast.LENGTH_SHORT).show();
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
            global.escribirError(e, 31);
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

    private void mostrarAlertDialogAgregarDiaTemporal(String idContratoSeleccionado) {

        int posActual = 0;
        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            //Idioma telefono en Ingles
            posActual = obtenerPosActual(obtenerDiaUltimoAbono(fechaactual), 0);
        }else {
            //Idioma telefono en Espanol
            posActual = obtenerPosActual(obtenerDiaUltimoAbono(fechaactual), 1);
        }

        String diatemporal = global.obtenerAtributoContrato(idContratoSeleccionado, "DIATEMPORAL");
        String titleAlerta = "";
        String titleMovimiento = "";
        if(diatemporal.length() > 0) {
            //Ya se escribio algo
            titleAlerta = "Editar";
            titleMovimiento = "edito";
        }else {
            //No se ha escrito nada
            titleAlerta = "Agregar";
            titleMovimiento = "agrego";
        }

        final Spinner spDiaTemporalContrato = new Spinner(fragmento.getActivity());

        //spDiaTemporalContrato
        posActualDiasTemporales = new String[] {"", "0", "1", "2", "3", "4", "5", "6"};
        tituloDiasTemporales = new String[] {"Seleccionar", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
        spDiaTemporalContrato.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, tituloDiasTemporales));

        if(diatemporal.length() > 0) {
            //Ya tenia una fecha previa
            if (Locale.getDefault().getDisplayLanguage().equals("English")) {
                spDiaTemporalContrato.setSelection(Arrays.asList(posActualDiasTemporales).indexOf(String.valueOf(obtenerPosActual(obtenerDiaUltimoAbono(diatemporal), 0))));
            }else {
                spDiaTemporalContrato.setSelection(Arrays.asList(posActualDiasTemporales).indexOf(String.valueOf(obtenerPosActual(obtenerDiaUltimoAbono(diatemporal), 1))));
            }
        }

        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle(titleAlerta + " dia temporal").setView(spDiaTemporalContrato).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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
        int finalPosActual = posActual;
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (posActualDiasTemporales[spDiaTemporalContrato.getSelectedItemPosition()] == "") {
                    Toast.makeText(fragmento.getActivity(), "Debes seleccionar un dia", Toast.LENGTH_LONG).show();
                } else {

                    if(Integer.parseInt(posActualDiasTemporales[spDiaTemporalContrato.getSelectedItemPosition()]) > finalPosActual) {

                        String diaTemporalAsignado = obtenerDiaTemporalAsignado(Integer.parseInt(posActualDiasTemporales[spDiaTemporalContrato.getSelectedItemPosition()]) - finalPosActual);

                        //Guardar en historial de movimientos
                        historialMovimientosContrato.guardarHistorialMovimientosContrato(idContratoSeleccionado,
                                "Se " + finalTitleMovimiento + " el dia temporal con fecha: " + diaTemporalAsignado, "0");

                        //Actualizar atributo DIATEMPORAL
                        global.actualizarAtributoTabla("CONTRATOS", "ENVIADOPAGINA = '0', DIATEMPORAL", diaTemporalAsignado, "ID_CONTRATO", idContratoSeleccionado);

                        //Sincronizar
                        llamadaSincronizacion();

                        Toast.makeText(fragmento.getActivity(), "Se " + finalTitleMovimiento + " correctamente el dia temporal", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }else {
                        Toast.makeText(fragmento.getActivity(), "No puedes seleccionar un dia menor o igual al actual", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }

    private void llamadaSincronizacion() {
        sincronizacion.sincronizarMetodo(0, objetosWebService, 0);
    }

    private String obtenerDiaTemporalAsignado(int diasAdelanto) {

        String diatemporal = "";

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(fechaactual);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, diasAdelanto);
            Date yesterday = calendar.getTime();

            diatemporal = sdf.format(yesterday);

        }catch (Exception e) {
            global.escribirError(e, 32);
            e.printStackTrace();
        }

        return diatemporal;
    }

    private int obtenerPosActual(String diaActual, int opcion) {

        int posActual = 0;

        if(opcion == 0) {
            //Ingles
            switch (diaActual) {
                case "Monday":
                    posActual = 0;
                    break;
                case "Tuesday":
                    posActual = 1;
                    break;
                case "Wednesday":
                    posActual = 2;
                    break;
                case "Thursday":
                    posActual = 3;
                    break;
                case "Friday":
                    posActual = 4;
                    break;
                case "Saturday":
                    posActual = 5;
                    break;
                case "Sunday":
                    posActual = 6;
                    break;
            }
        }else {
            //Espanol
            switch (diaActual) {
                case "lunes":
                    posActual = 0;
                    break;
                case "martes":
                    posActual = 1;
                    break;
                case "miercoles":
                    posActual = 2;
                    break;
                case "jueves":
                    posActual = 3;
                    break;
                case "viernes":
                    posActual = 4;
                    break;
                case "sabado":
                    posActual = 5;
                    break;
                case "domingo":
                    posActual = 6;
                    break;

            }
        }

        return posActual;
    }

    private boolean existeIdContratoEnTablaRuta(String idContrato) {

        boolean respuesta = true;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();

            String SQL = "SELECT ID_CONTRATO FROM RUTA WHERE ID_CONTRATO = '" + idContrato + "' AND ESTADO = '1' AND DIA = '" + valorHamburguesaRuta + "'";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                respuesta = false;
            }

            datos.close();
            sqLiteDB2.close();

        }catch (SQLiteException e){
            global.escribirError(e, 33);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

        return respuesta;
    }

    private int obtenerContadorRegistrosTablaRuta(boolean esContador) {

        int contadorOPosicion = 0;

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "";
            if(esContador) {
                SQL = "SELECT COUNT(ID_CONTRATO) FROM RUTA WHERE DIA = '" + valorHamburguesaRuta + "'";
            }else {
                SQL = "SELECT POSICION FROM RUTA WHERE DIA = '" + valorHamburguesaRuta + "' ORDER BY CAST(POSICION AS INTEGER) DESC LIMIT 1";
            }
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.moveToFirst()) {
                contadorOPosicion = datos.getInt(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 34);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return contadorOPosicion;

    }

    private void cambiarPosicionContratoSeleccionado(String idContratoSeleccionado, boolean arriba) {

        int posicionContratoSeleccionado = Integer.parseInt(global.obtenerAtributoTabla("RUTA", "POSICION",
                "ESTADO = '1' AND DIA = '" + valorHamburguesaRuta + "' AND ID_CONTRATO", idContratoSeleccionado));
        boolean bandera = false;
        int contadorRegistrosTablaRuta = 0;

        int i = 0;
        if(arriba) {
            //Mandar hacia arriba
            i = posicionContratoSeleccionado - 1;
        }else {
            //Mandar hacia abajo
            i = posicionContratoSeleccionado + 1;
            contadorRegistrosTablaRuta = obtenerContadorRegistrosTablaRuta(true);
        }

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();

            while(!bandera) {

                String SQL = "SELECT R.ID_CONTRATO, R.POSICION FROM RUTA R" +
                        " WHERE R.ID_CONTRATO IN (SELECT C.ID_CONTRATO FROM CONTRATOS C WHERE R.ID_CONTRATO = C.ID_CONTRATO)" +
                        " AND R.POSICION = '" + i + "' AND ESTADO = '1' AND DIA = '" + valorHamburguesaRuta + "'";
                Cursor datos = sqLiteDB2.rawQuery(SQL, null);

                if (datos.moveToFirst()) {
                    //Existe registro
                    global.actualizarAtributoTabla("RUTA", "ENVIADOPAGINA = '0', POSICION", datos.getString(1),
                            "ESTADO = '1' AND DIA = '" + valorHamburguesaRuta + "' AND ID_CONTRATO", idContratoSeleccionado);
                    global.actualizarAtributoTabla("RUTA", "ENVIADOPAGINA = '0', POSICION", String.valueOf(posicionContratoSeleccionado),
                            "ESTADO = '1' AND DIA = '" + valorHamburguesaRuta + "' AND ID_CONTRATO", datos.getString(0));
                    bandera = true;
                }

                datos.close();

                if(!bandera) {

                    if (arriba) {
                        if (i <= 0) {
                            bandera = true;
                        }
                        i--;
                    } else {
                        if (i >= contadorRegistrosTablaRuta) {
                            bandera = true;
                        }
                        i++;
                    }

                }

            }

            sqLiteDB2.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 35);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        obtenerListaContratos(true);

    }

    private void cambiarPosicionesListaRuta(String idContratoSeleccionado, boolean arriba) {

        String posicionActual = global.obtenerAtributoTabla("RUTA", "POSICION", "ESTADO = '1' AND DIA = '" + valorHamburguesaRuta + "' AND ID_CONTRATO", idContratoSeleccionado);

        if(arriba) {
            //Mandar primera posicion

            if (!posicionActual.equals("0")) {
                //Posicion es diferente de 0

                try {
                    //CONTRATOS EN RUTA

                    SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
                    String SQL = "SELECT ID_CONTRATO, ESTADO" +
                            " FROM RUTA WHERE CAST(POSICION AS INTEGER) <= '" + posicionActual + "' AND DIA = '" + valorHamburguesaRuta + "' ORDER BY CAST(POSICION AS INTEGER) ASC";
                    Cursor datos = sqLiteDB2.rawQuery(SQL, null);

                    if (datos.getCount() > 0) {
                        //Si hay datos

                        int posicionActualizar = 1;
                        int contador = datos.getCount();

                        while (datos.moveToNext()) {
                            //Cambiar posiciones
                            if(contador > 1) {
                                //Subir una posicion a los contratos que seguiran despues de la primera posicion
                                global.actualizarAtributoTabla("RUTA", "ENVIADOPAGINA = '0', ESTADO = '" + datos.getString(1) + "', ID_CONTRATO",
                                        datos.getString(0), "DIA = '" + valorHamburguesaRuta + "' AND POSICION", String.valueOf(posicionActualizar));
                                posicionActualizar++;
                                contador--;
                            }else {
                                //Contrato que se pasara al principio de la lista (Posicion 0)
                                global.actualizarAtributoTabla("RUTA", "ENVIADOPAGINA = '0', ESTADO = '" + datos.getString(1) + "', ID_CONTRATO",
                                        datos.getString(0), "DIA = '" + valorHamburguesaRuta + "' AND POSICION", "0");
                            }

                        }

                    }

                    sqLiteDB2.close();
                    datos.close();

                } catch (SQLiteException e) {
                    global.escribirError(e, 36);
                    Log.i("ERRORBD", e.getMessage() + "");
                }

            }

        }else {
            //Mandar ultima posicion

            String ultimaPosicionActiva = global.obtenerUnResultadoQuery("SELECT POSICION FROM RUTA WHERE ESTADO = '1' AND DIA = '" + valorHamburguesaRuta +
                                                                         "' ORDER BY CAST(POSICION AS INTEGER) DESC LIMIT 1");

            if (!posicionActual.equals(ultimaPosicionActiva)) {
                //Posicion es diferente a la ultima

                int ultimaPosicion = obtenerContadorRegistrosTablaRuta(false);

                try {
                    //CONTRATOS EN RUTA

                    SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
                    String SQL = "SELECT ID_CONTRATO, ESTADO" +
                            " FROM RUTA WHERE CAST(POSICION AS INTEGER) >= '" + posicionActual + "' AND DIA = '" + valorHamburguesaRuta + "' ORDER BY CAST(POSICION AS INTEGER) DESC";
                    Cursor datos = sqLiteDB2.rawQuery(SQL, null);

                    if (datos.getCount() > 0) {
                        //Si hay datos

                        int posicionActualizar = ultimaPosicion - 1;
                        int contador = datos.getCount();

                        while (datos.moveToNext()) {
                            //Cambiar posiciones
                            if(contador > 1) {
                                //Bajar una posicion a los contratos que estaran antes de la ultima posicion
                                global.actualizarAtributoTabla("RUTA", "ENVIADOPAGINA = '0', ESTADO = '" + datos.getString(1) + "', ID_CONTRATO",
                                        datos.getString(0), "DIA = '" + valorHamburguesaRuta + "' AND POSICION", String.valueOf(posicionActualizar));
                                posicionActualizar--;
                                contador--;
                            }else {
                                //Contrato que se pasara a la ultima posicion de la lista (Ultima posicion)
                                global.actualizarAtributoTabla("RUTA", "ENVIADOPAGINA = '0', ESTADO = '" + datos.getString(1) + "', ID_CONTRATO",
                                        datos.getString(0), "DIA = '" + valorHamburguesaRuta + "' AND POSICION", String.valueOf(ultimaPosicion));
                            }

                        }

                    }

                    sqLiteDB2.close();
                    datos.close();

                } catch (SQLiteException e) {
                    global.escribirError(e, 37);
                    Log.i("ERRORBD", e.getMessage() + "");
                }

            }

        }

        obtenerListaContratos(true);

    }

    private void cambiarEstadoCeroARegistrosDuplicadosRuta() {

        try {
            //CONTRATOS EN RUTA

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID, ID_CONTRATO, DIA, COUNT(*)" +
                    " FROM RUTA WHERE DIA = '" + valorHamburguesaRuta + "' GROUP BY ID_CONTRATO, DIA HAVING COUNT(*) > 1";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() > 0) {
                //Si hay datos

                while (datos.moveToNext()) {
                    global.actualizarAtributoTabla("RUTA", "ENVIADOPAGINA = '0', ESTADO", "0", "ID != '" + datos.getString(0) +
                                                         "' AND DIA = '" + valorHamburguesaRuta + "' AND ID_CONTRATO", datos.getString(1));
                }

            }

            sqLiteDB2.close();
            datos.close();

        } catch (SQLiteException e) {
            global.escribirError(e, 254);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private void actualizarContratoPorCrearAVacioYEliminarHistorialClinico(String idContrato) {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

            String consulta = "UPDATE CONTRATOS SET DATOS='" + "0" + "', ID_USUARIOCREACION='" + obtenerRol.obtenerAtributoUsuarioLogeado("ID")
                    + "', NOMBRE_USUARIOCREACION='" + obtenerRol.obtenerAtributoUsuarioLogeado("USUARIO") + "', ID_ZONA='" + ""
                    + "', NOMBRE='" + "" + "', CALLE='" + ""
                    + "', NUMERO='" + "" + "', DEPTO='" + ""
                    + "', ALLADODE='" + "" + "', FRENTEA='" + ""
                    + "', ENTRECALLES='" + "" + "', COLONIA='" + ""
                    + "', LOCALIDAD='" + "" + "', TELEFONO='" + ""
                    + "', TELEFONOREFERENCIA='" + "" + "', NOMBREREFERENCIA='" + ""
                    + "', CASATIPO='" + "" + "', CASACOLOR='" + ""
                    + "', CORREO='" + ""
                    + "', FOTOINEFRENTE='" + "" + "', FOTOINEATRAS='" + ""
                    + "', FOTOCASA='" + "" + "', COMPROBANTEDOMICILIO='" + ""
                    + "', ID_OPTOMETRISTA='" + ""
                    + "', ID_PROMOCION='" + "" + "', TOTAL='" + ""
                    + "', IDCONTRATORELACION='" + "" + "', CONTADOR='" + ""
                    + "', TOTALHISTORIAL='" + "" + "', TOTALPROMOCION='" + ""
                    + "', TOTALPRODUCTO='" + "" + "', TOTALABONO='" + ""
                    + "', ULTIMOABONO='" + ""
                    + "', ESTATUS_ESTADOCONTRATO='" + "" + "', DIAPAGO='" + ""
                    + "', FECHACOBROINI='" + "" + "', FECHACOBROFIN='" + ""
                    + "', FECHAATRASO='" + "" + "', COSTOATRASO='" + ""
                    + "', DIASELECCIONADO='" + "" + "', FECHAENTREGA='" + ""
                    + "', PAGOSADELANTAR='" + "0" + "', ENGANCHE='" + ""
                    + "', ENTREGAPRODUCTO='" + "" + "', ESTATUS='" + "0"
                    + "', ENVIADOPAGINA='" + "1"
                    + "', CONTRATOENVIADO='" + "0"
                    + "', PAGARE='" + "" + "', FOTOOTROS='" + "" + "', PROMOCIONTERMINADA='" + ""
                    + "', SUBSCRIPCION='" + "" + "', FECHASUBSCRIPCION='" + ""
                    + "', TOTALREAL='" + "" + "', DIATEMPORAL='" + ""
                    + "', COORDENADAS='" + "" + "', CALLEENTREGA='" + ""
                    + "', NUMEROENTREGA='" + "" + "', DEPTOENTREGA='" + ""
                    + "', ALLADODEENTREGA='" + "" + "', FRENTEAENTREGA='" + ""
                    + "', ENTRECALLESENTREGA='" + "" + "', COLONIAENTREGA='" + ""
                    + "', LOCALIDADENTREGA='" + ""
                    + "', CASATIPOENTREGA='" + "" + "', CASACOLORENTREGA='" + ""
                    + "', ALIAS='"+ "" + "', OPCIONLUGARENTREGA='" + ""
                    + "', OBSERVACIONFOTOINE='" + "" + "', OBSERVACIONFOTOINEATRAS='" + ""
                    + "', OBSERVACIONFOTOCASA='" + "" + "', OBSERVACIONCOMPROBANTEDOMICILIO='" + ""
                    + "', OBSERVACIONPAGARE='" + "" + "', OBSERVACIONFOTOOTROS='" + ""
                    + "', CREATED_AT='" + "" + "', UPDATED_AT='" + ""
                    + "' WHERE ID_CONTRATO='" + idContrato + "'";

            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();

            String idHistorialClinico = obtenerIdHistorialContratoPorCrear(idContrato);
            eliminarHistorialClinico(idHistorialClinico);

            //Eliminar historialsinconversion
            global.eliminarTablaORegistroTabla("HISTORIALSINCONVERSION", "ID_CONTRATO = '" + idContrato + "' AND ID_HISTORIAL", idHistorialClinico);

        }catch (SQLiteException e) {
            global.escribirError(e, 38);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    private String obtenerIdHistorialContratoPorCrear(String idContrato) {

        String idHistorialClinico = "";

        try{
            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID FROM HISTORIALCLINICO WHERE ID_CONTRATO='" + idContrato + "'";
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
            global.escribirError(e, 39);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return idHistorialClinico;
    }

    private void eliminarHistorialClinico(String idHistorialClinico) {

        try{

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();
            String SQL = "DELETE FROM HISTORIALCLINICO WHERE ID='" + idHistorialClinico + "'";
            sqLiteDB2.execSQL(SQL);
            sqLiteDB2.close();

        }catch (SQLiteException e){
            global.escribirError(e, 40);
            Log.i("ERRORBD", "ERROR: NO SE ENCONTRO RESULTADO: " + e.getMessage());
        }

    }

    /*Metodo/Funcion: obtenerListaContratosBuscador
      Descripcion: Obtener lista de contratos con el buscador
    */
    public void obtenerListaContratosBuscador(String textoBuscar) {

        if (textoBuscar.length() == 0) {
            //textoBuscar vacio
            obtenerListaContratos(false);
        } else {
            //textoBuscar diferente de vacio

            textoBuscar = impresoraBluetooth.limpiarAcentos(textoBuscar.trim());

            boolean valorHamburguesaTodos = false;
            try {
                //Obtener valorHamburguesaTodos y valorHamburguesaRuta
                valorHamburguesaTodos = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaTodos"));
                valorHamburguesaRuta = seguridad.leerSharedPreferences("valorHamburguesaRuta");
                if (valorHamburguesaRuta.length() == 0) {
                    //valorHamburguesaRuta es igual a vacio
                    valorHamburguesaRuta = "0";
                }
            } catch (Exception e) {
                global.escribirError(e, 41);
                e.printStackTrace();
            }

            filaContratos.clear();
            estadoContratos = new ArrayList<>();
            encabezados = new ArrayList<>();
            diasAtrasados = new ArrayList<>();

            /*
            if(tvIdContratoClienteContratoPrincipal.getVisibility() == View.VISIBLE) {
                mostrarOcultarIdContratoNombreLista = true;
            }else {
                mostrarOcultarIdContratoNombreLista = false;
            }

            if(tvColoniaClienteContratoPrincipal.getVisibility() == View.VISIBLE) {
                mostrarOcultarColoniaNotaLista = true;
            }else {
                mostrarOcultarColoniaNotaLista = false;
            }
*/
            try {
                //BUSCAR

                SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();

                String queryExtra = " ";
                if(!valorHamburguesaTodos) {
                    //No esta seleccionado el valor de todos

                    boolean valorHamburguesaLunes = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaLunes"));
                    boolean valorHamburguesaMartes = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaMartes"));
                    boolean valorHamburguesaMiercoles = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaMiercoles"));
                    boolean valorHamburguesaJueves = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaJueves"));
                    boolean valorHamburguesaViernes = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaViernes"));
                    boolean valorHamburguesaSabado = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaSabado"));
                    boolean valorHamburguesaDomingo = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaDomingo"));
                    boolean valorHamburguesaContado = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaContado"));
                    boolean valorHamburguesaSemanal = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaSemanal"));
                    boolean valorHamburguesaQuincenal = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaQuincenal"));
                    boolean valorHamburguesaMensual = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaMensual"));

                    if(valorHamburguesaContado || valorHamburguesaSemanal || valorHamburguesaQuincenal || valorHamburguesaMensual) {
                        //Estan chequeados los valores de forma de pago

                        if (valorHamburguesaContado && valorHamburguesaSemanal && valorHamburguesaQuincenal && valorHamburguesaMensual) {
                            //Los 4 estan checkeados
                            queryExtra = " AND C.PAGO IN (0,1,2,4)";
                        }else {
                            //Alguno de los 4 checkbox no esta checkeado
                            if (valorHamburguesaContado) {
                                //Contado esta checkeado
                                queryExtra = " AND C.PAGO IN (0)";
                                if (valorHamburguesaSemanal) {
                                    //Semanal esta checkeado
                                    queryExtra = " AND C.PAGO IN (0,1)";
                                    if (valorHamburguesaQuincenal) {
                                        //Quincenal esta checkeado
                                        queryExtra = " AND C.PAGO IN (0,1,2)";
                                        if (valorHamburguesaMensual) {
                                            //Mensual esta checkeado
                                            queryExtra = " AND C.PAGO IN (0,1,2,4)";
                                        }
                                    }else {
                                        //Quincenal no esta checkeado
                                        if (valorHamburguesaMensual) {
                                            //Mensual esta checkeado
                                            queryExtra = " AND C.PAGO IN (0,1,4)";
                                        }
                                    }
                                }else {
                                    //Semanal no esta checkeado
                                    if (valorHamburguesaQuincenal) {
                                        //Quincenal esta checkeado
                                        queryExtra = " AND C.PAGO IN (0,2)";
                                        if(valorHamburguesaMensual) {
                                            //Mensual esta checkeado
                                            queryExtra = " AND C.PAGO IN (0,2,4)";
                                        }
                                    }else {
                                        //Quincenal no esta checkeado
                                        if(valorHamburguesaMensual) {
                                            //Mensual esta checkeado
                                            queryExtra = " AND C.PAGO IN (0,4)";
                                        }
                                    }
                                }
                            }else {
                                //Contado no esta checkeado
                                if (valorHamburguesaSemanal) {
                                    //Semanal esta checkeado
                                    queryExtra = " AND C.PAGO IN (1)";
                                    if (valorHamburguesaQuincenal) {
                                        //Quincenal esta checkeado
                                        queryExtra = " AND C.PAGO IN (1,2)";
                                        if (valorHamburguesaMensual) {
                                            //Mensual esta checkeado
                                            queryExtra = " AND C.PAGO IN (1,2,4)";
                                        }
                                    }else {
                                        //Quincenal no esta checkeado
                                        if (valorHamburguesaMensual) {
                                            //Mensual esta checkeado
                                            queryExtra = " AND C.PAGO IN (1,4)";
                                        }
                                    }
                                }else {
                                    //Semanal no esta checkeado
                                    if (valorHamburguesaQuincenal) {
                                        //Quincenal esta checkeado
                                        queryExtra = " AND C.PAGO IN (2)";
                                        if(valorHamburguesaMensual) {
                                            //Mensual esta checkeado
                                            queryExtra = " AND C.PAGO IN (2,4)";
                                        }
                                    }else {
                                        //Quincenal no esta checkeado
                                        if(valorHamburguesaMensual) {
                                            //Mensual esta checkeado
                                            queryExtra = " AND C.PAGO IN (4)";
                                        }
                                    }
                                }
                            }
                        }

                    }else {
                        //Estan chequeados los valores de dia pago
                        queryExtra = obtenerQueryDiasValoresHamburguesasSeleccionados(valorHamburguesaLunes, valorHamburguesaMartes, valorHamburguesaMiercoles, valorHamburguesaJueves,
                                valorHamburguesaViernes, valorHamburguesaSabado, valorHamburguesaDomingo);
                    }

                }

                String SQL = "SELECT C.ID_CONTRATO, C.NOMBRE, C.ESTATUS_ESTADOCONTRATO, C.FECHAATRASO, C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.ENVIADOPAGINA," +
                  " (SELECT COUNT(H.ID) FROM HISTORIALCLINICO H WHERE C.ID_CONTRATO = H.ID_CONTRATO AND (LENGTH(H.OBSERVACIONES) > 0 OR LENGTH(H.OBSERVACIONESINTERNO) > 0)) AS OBSERVACION," +
                  " C.NUMEROENTREGA, C.PAGO, C.NOTA, C.CONTRATOENVIADO, (SELECT CREATED_AT FROM ABONOS A WHERE A.ID_CONTRATO = C.ID_CONTRATO AND A.TIPOABONO NOT IN (7) " +
                  " ORDER BY A.CREATED_AT DESC LIMIT 1) AS ULTIMOABONO, C.OPCIONLUGARENTREGA, C.LOCALIDAD, C.COLONIA, C.CALLE, C.NUMERO " +
                  " FROM CONTRATOS C WHERE C.DATOS = '1' AND C.ID_ZONA = '" + id_zona + "' AND C.ESTATUS_ESTADOCONTRATO != '3'" +
                    queryExtra +
                  " AND (C.ID_CONTRATO LIKE '%" + textoBuscar + "%' OR C.NOMBRE LIKE '%" + textoBuscar + "%' OR C.TELEFONO LIKE '%" + textoBuscar + "%'" +
                  " OR C.LOCALIDAD LIKE '%" + textoBuscar + "%'" + " OR C.COLONIA LIKE '%" + textoBuscar + "%' OR C.CALLE LIKE '%" + textoBuscar + "%' OR C.NUMERO LIKE '%" + textoBuscar + "%'" +
                  " OR C.LOCALIDADENTREGA LIKE '%" + textoBuscar + "%'" + " OR C.COLONIAENTREGA LIKE '%" + textoBuscar + "%' OR C.CALLEENTREGA LIKE '%" + textoBuscar + "%' OR C.NUMEROENTREGA LIKE '%" + textoBuscar + "%' OR C.ALIAS LIKE '%" + textoBuscar + "%')" +
                  " AND (ULTIMOABONO = '' OR (ULTIMOABONO <= '" + fechaLunesAnterior + "' OR ULTIMOABONO >= '" + fechaDomingoSiguiente +"')) " +
                  " ORDER BY C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.NUMEROENTREGA, C.ID_CONTRATO ASC";
                Cursor datos = sqLiteDB2.rawQuery(SQL, null);

                if (datos.getCount() == 0) {
                    //No hay datos
                    lvListaContratos.removeAllViewsInLayout();
                    lvListaContratos.postInvalidate();
                } else {
                    //Si hay datos

                    while (datos.moveToNext()) {
                        //Obtener datos y agregarlos a la lista

                        FilaContratos fila = new FilaContratos();
                        fila.setIdContrato(datos.getString(0));
                        fila.setNombreClienteContrato(datos.getString(1).toUpperCase());
                        fila.setUltimoAbono(datos.getString(13));
                        if(datos.getString(2).equals("12") && datos.getString(14).equals("0")){
                            //Contrato en ENVIADO y Entrega de producto en lugar de venta
                            fila.setLocalidadClienteContrato(datos.getString(15).toUpperCase());
                            fila.setColoniaClienteContrato(datos.getString(16).toUpperCase());
                            fila.setCalleClienteContrato(datos.getString(17).toUpperCase());
                            fila.setNumeroClienteContrato(datos.getString(18).toUpperCase());
                        }else{
                            //Entrega de producto en lugar de cobranza
                            fila.setLocalidadClienteContrato(datos.getString(4).toUpperCase());
                            fila.setColoniaClienteContrato(datos.getString(5).toUpperCase());
                            fila.setCalleClienteContrato(datos.getString(6).toUpperCase());
                            fila.setNumeroClienteContrato(datos.getString(9).toUpperCase());
                        }
                        fila.setEsRuta("0");
                        fila.setFormaPagoClienteContrato(obtenerLetraFormaPagoContrato(datos.getInt(10)));
                        fila.setNotaClienteContrato(datos.getString(11).toUpperCase());
                        if (datos.getString(7).equals("0")) {
                            fila.setEnviadoPaginaClienteContrato("P," + datos.getString(12));
                        }else {
                            fila.setEnviadoPaginaClienteContrato("S," + datos.getString(12));
                        }
                        //Observaciones historial
                        if(datos.getInt(8) > 0) {
                            //Sin observaciones
                            fila.setObservacionesClienteContrato("S");
                        }else {
                            //Con observaciones
                            fila.setObservacionesClienteContrato("N");
                        }
                        estadoContratos.add(datos.getInt(2));
                        if(datos.getInt(2) == 4) {
                            //Estado del contrato es igual a ABONOATRASADO
                            diasAtrasados.add(obtenerDiasAtrasados(datos.getString(3)));
                        }else {
                            //Estado del contrato es diferente a ABONOATRASADO por lo tanto no tiene fechaatraso
                            diasAtrasados.add(0);
                        }

                        filaContratos.add(fila);

                    }

                }

                sqLiteDB2.close();
                datos.close();

            } catch (Exception e) {
                global.escribirError(e, 42);
                Log.i("ERRORBD", e.getMessage() + "");
            }

            //Agregar la lista en el adaptador
            adaptadorListaContratosPrincipal adaptador = new adaptadorListaContratosPrincipal(fragmento, filaContratos, encabezados) {
                @Override
                public View getView(int position, View convertView, ViewGroup viewGroup) {
                    View view = super.getView(position, convertView, viewGroup);

                    //Color de item de la lista dependiendo del estado del contrato
                    switch (estadoContratos.get(position)) {
                        case 0:
                            //NO TERMINADO
                            view.setBackgroundColor(Color.parseColor("#6c757d"));
                            break;
                        case 1:
                            //TERMINADO
                            if(rol.equals("4")) {
                                //Rol cobranza
                                view.setBackgroundColor(Color.parseColor("#F48FB1"));
                            }else {
                                //Rol opto garantia
                                view.setBackgroundColor(Color.parseColor("#5bc0de"));
                            }
                            break;
                        case 2:
                            //ENTREGADO
                            if(rol.equals("4")) {
                                //Rol cobranza
                                view.setBackgroundColor(Color.parseColor("#0275d8"));
                            }else {
                                //Rol opto garantia
                                view.setBackgroundColor(Color.parseColor("#F48FB1"));
                            }
                            break;
                        case 3:
                            //PRE-CANCELADO
                            view.setBackgroundColor(Color.parseColor("#d9534f"));
                            break;
                        case 4:
                            //ABONO ATRASADO
                            if(rol.equals("4")) {
                                //Rol cobranza
                                if (diasAtrasados.get(position) <= 10) {
                                    //1-10 dias
                                    view.setBackgroundColor(Color.parseColor("#fff2cc"));
                                } else {
                                    if (diasAtrasados.get(position) <= 20) {
                                        //11-20 dias
                                        view.setBackgroundColor(Color.parseColor("#fce5cd"));
                                    } else {
                                        if (diasAtrasados.get(position) > 20) {
                                            //20 dias en delante
                                            view.setBackgroundColor(Color.parseColor("#f4cccc"));
                                        }
                                    }
                                }
                            }else {
                                //Rol opto garantia
                                view.setBackgroundColor(Color.parseColor("#F48FB1"));
                            }
                            break;
                        case 5:
                            //PAGADO
                            if(rol.equals("4")) {
                                //Rol cobranza
                                view.setBackgroundColor(Color.parseColor("#5cb85c"));
                            }else {
                                //Rol opto garantia
                                view.setBackgroundColor(Color.parseColor("#F48FB1"));
                            }
                            break;
                        case 7:
                        case 9:
                        case 10:
                        case 11: //APROBADO, EN PROCESO DE APROBACION, MANUFACTURA, EN PROCESO DE ENVIO
                            if(rol.equals("4")) {
                                //Rol cobranza
                                view.setBackgroundColor(Color.parseColor("#F48FB1"));
                            }
                            break;
                        case 12:
                            //ENVIADO
                            if(rol.equals("4")) {
                                //Rol cobranza
                                view.setBackgroundColor(Color.parseColor("#5bc0de"));
                            }else {
                                //Rol opto garantia
                                view.setBackgroundColor(Color.parseColor("#F48FB1"));
                            }
                            break;
                        case 13:
                            //POR CREAR
                            view.setBackgroundColor(Color.parseColor("#b900ff"));
                            break;
                        case 20:
                            //COLOR ENCABEZADO
                            view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                            break;
                        case 6:
                            //CENCELADO
                            view.setBackgroundColor(Color.parseColor("#ff0000"));
                            break;
                    }

                    return view;
                }
            };

            lvListaContratos.setAdapter(adaptador);
        }

    }

    private String obtenerQueryDiasValoresHamburguesasSeleccionados(boolean valorHamburguesaLunes, boolean valorHamburguesaMartes, boolean valorHamburguesaMiercoles, boolean valorHamburguesaJueves,
                                                                    boolean valorHamburguesaViernes, boolean valorHamburguesaSabado, boolean valorHamburguesaDomingo) {

        String valorLunes = "'Monday',";
        String valorMartes = "'Tuesday',";
        String valorMiercoles = "'Wednesday',";
        String valorJueves = "'Thursday',";
        String valorViernes = "'Friday',";
        String valorSabado = "'Saturday',";
        String valorDomingo = "'Sunday',";

        List<Boolean> valoresHamburguesas = new ArrayList<>();
        valoresHamburguesas.add(valorHamburguesaLunes);
        valoresHamburguesas.add(valorHamburguesaMartes);
        valoresHamburguesas.add(valorHamburguesaMiercoles);
        valoresHamburguesas.add(valorHamburguesaJueves);
        valoresHamburguesas.add(valorHamburguesaViernes);
        valoresHamburguesas.add(valorHamburguesaSabado);
        valoresHamburguesas.add(valorHamburguesaDomingo);

        String cadena = "";
        for (int i = 0; i < valoresHamburguesas.size(); i++) {
            switch (i) {
                case 0: //valorHamburguesaLunes
                    if(valoresHamburguesas.get(i)) {
                        cadena += valorLunes;
                    }
                    break;
                case 1: //valorHamburguesaMartes
                    if(valoresHamburguesas.get(i)) {
                        cadena += valorMartes;
                    }
                    break;
                case 2: //valorHamburguesaMiercoles
                    if(valoresHamburguesas.get(i)) {
                        cadena += valorMiercoles;
                    }
                    break;
                case 3: //valorHamburguesaJueves
                    if(valoresHamburguesas.get(i)) {
                        cadena += valorJueves;
                    }
                    break;
                case 4: //valorHamburguesaViernes
                    if(valoresHamburguesas.get(i)) {
                        cadena += valorViernes;
                    }
                    break;
                case 5: //valorHamburguesaSabado
                    if(valoresHamburguesas.get(i)) {
                        cadena += valorSabado;
                    }
                    break;
                case 6: //valorHamburguesaDomingo
                    if(valoresHamburguesas.get(i)) {
                        cadena += valorDomingo;
                    }
                    break;
            }
        }

        return " AND C.DIAPAGO IN (" + cadena.substring(0, cadena.length() - 1) + ")";

    }

    public void consultarMensajes() {

        //Consulta de los MENSAJES
        try {

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ID, DESCRIPCION FROM MENSAJES" +
                    " WHERE CAST(NUMINTENTOS AS INTEGER) < CAST(MAXINTENTOS AS INTEGER)" +
                    " AND strftime('%Y-%m-%d', '" + obtenerRol.obtenerAtributoUsuarioLogeado("FECHAACTUAL") + "') <= strftime('%Y-%m-%d', FECHALIMITE)";
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if(datos.getCount() == 0){
                Log.i("MENSAJE", "No hay mensajes");
            }else {

                String mensajeCompleto = "";

                while (datos.moveToNext()) {

                    aumentarIntento(datos.getString(0));
                    mensajeCompleto += " - " + datos.getString(1) + "\n";

                }

                androidx.appcompat.app.AlertDialog.Builder alerta = new androidx.appcompat.app.AlertDialog.Builder(fragmento.getActivity());
                alerta.setTitle("MENSAJE").setMessage(mensajeCompleto).setPositiveButton("LEIDO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            }

            sqLiteDB2.close();
            datos.close();

        }catch (Exception e) {
            global.escribirError(e, 43);
            Log.i("ERRORBD", e.getMessage() + "");
        }
    }

    /*Metodo/Funcion: aumentarIntento
     Descripcion: Aumentar un intento a los registros de la tabla MENSAJES
   */
    private void aumentarIntento(String id) {

        try {

            SQLiteDatabase sqLiteDB2 = conexion.getWritableDatabase();

            String consulta = "UPDATE MENSAJES SET NUMINTENTOS = CAST(NUMINTENTOS AS INTEGER) + 1"
                    + " WHERE ID='" + id + "'";

            sqLiteDB2.execSQL(consulta);
            sqLiteDB2.close();

        }catch (SQLiteException e) {
            global.escribirError(e, 44);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    public void subirImagenesAFTPContratosTerminados() {

        String condicionImagenesPendientes = "";

        try{

            //Rol es Asistente/Opto?
            if(!rol.equals("4")){
                //Si rol es asis/opto - Verificar contratos terminados
                condicionImagenesPendientes = "  WHERE C.ESTATUS_ESTADOCONTRATO IN (1,9)";
            }

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT C.ID_CONTRATO, C.FOTOINEFRENTE, C.FOTOINEATRAS, C.FOTOCASA, C.COMPROBANTEDOMICILIO, C.TARJETAFRENTE, C.TARJETAATRAS, C.PAGARE, C.FOTOOTROS," +
                    " IFNULL((SELECT HC.FOTO FROM HISTORIALFOTOSCONTRATO HC WHERE HC.ID_CONTRATO = C.ID_CONTRATO),'') AS FOTOMOVIMIENTO, " +
                    " IFNULL((SELECT HC.ID_PAQUETE FROM HISTORIALCLINICO HC WHERE HC.ID_CONTRATO = C.ID_CONTRATO ORDER BY HC.CREATED_AT ASC LIMIT 1),'') AS PAQUETE, " +
                    " IFNULL((SELECT HC.FOTOARMAZON FROM HISTORIALCLINICO HC WHERE HC.ID_CONTRATO = C.ID_CONTRATO ORDER BY HC.CREATED_AT ASC LIMIT 1),'') AS FOTOARMAZON1, " +
                    " IFNULL((SELECT HC.FOTOARMAZON FROM HISTORIALCLINICO HC WHERE HC.ID_CONTRATO = C.ID_CONTRATO ORDER BY HC.CREATED_AT DESC LIMIT 1),'') AS FOTOARMAZON2 " +
                    " FROM CONTRATOS C INNER JOIN IMAGENESCARGADASCONTRATOS I ON C.ID_CONTRATO = I.ID_CONTRATO " + condicionImagenesPendientes;
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            if (datos.getCount() == 0){
                Log.i("MENSAJE", "No hay contratos para subir fotos a FTP");
            }

            while(datos.moveToNext()){
                //Subir imagenes a FTP
                if(!datos.getString(10).equals("6")){
                    //Paquete diferente DORADO 2 - Maximo 1 imagen armazon propio
                    sincronizacion.validacionImagenesFTP(datos.getString(0), datos.getString(1), datos.getString(2),
                            datos.getString(3), datos.getString(4), datos.getString(5),
                            datos.getString(6), datos.getString(7), datos.getString(8), datos.getString(9), datos.getString(11), "");
                }else{
                    //Paquete DORADO 2 - Maximo 2 imagenes de armazon propio
                    sincronizacion.validacionImagenesFTP(datos.getString(0), datos.getString(1), datos.getString(2),
                            datos.getString(3), datos.getString(4), datos.getString(5),
                            datos.getString(6), datos.getString(7), datos.getString(8), datos.getString(9), datos.getString(11), datos.getString(12));
                }

            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 45);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    public int obtenerNumeroImagenesNoSubidasFTP() {

        int numeroImagenes = 0;
        String condicionImagenesPendientes = "";

        try{

            //Rol es Asistente/Opto?
            if(!rol.equals("4")){
                //Si rol es asis/opto - Verificar contratos terminados
                condicionImagenesPendientes = " WHERE C.ESTATUS_ESTADOCONTRATO = '1'";
            }

            SQLiteDatabase sqLiteDB2 = conexion.getReadableDatabase();
            String SQL = "SELECT ((SELECT COUNT(I.FOTOINE) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.FOTOINE IN (0,1))" +
                    " + (SELECT COUNT(I.FOTOINEATRAS) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.FOTOINEATRAS IN (0,1))" +
                    " + (SELECT COUNT(I.FOTOCASA) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.FOTOCASA IN (0,1))" +
                    " + (SELECT COUNT(I.COMPROBANTEDOMICILIO) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.COMPROBANTEDOMICILIO IN (0,1))" +
                    " + (SELECT COUNT(I.PAGARE) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.PAGARE IN (0,1))" +
                    " + (SELECT COUNT(I.FOTOOTROS) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.FOTOOTROS IN (0,1))" +
                    " + (SELECT COUNT(I.TARJETAPENSION) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.TARJETAPENSION IN (0,1))" +
                    " + (SELECT COUNT(I.TARJETAPENSIONATRAS) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.TARJETAPENSIONATRAS IN (0,1))" +
                    " + (SELECT COUNT(I.FOTOMOVIMIENTO) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.FOTOMOVIMIENTO IN (0,1))" +
                    " + (SELECT COUNT(I.FOTOARMAZON1) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.FOTOARMAZON1 IN (0,1))" +
                    " + (SELECT COUNT(I.FOTOARMAZON2) FROM IMAGENESCARGADASCONTRATOS I WHERE I.ID_CONTRATO = C.ID_CONTRATO AND I.FOTOARMAZON2 IN (0,1))" +
                    ") AS TOTAL FROM CONTRATOS C " + condicionImagenesPendientes;
            Cursor datos = sqLiteDB2.rawQuery(SQL, null);

            while(datos.moveToNext()){
                numeroImagenes += datos.getInt(0);
            }

            sqLiteDB2.close();
            datos.close();

        }catch (SQLiteException e){
            global.escribirError(e, 46);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        return numeroImagenes;

    }
/*
    public void mostrarOcultarTvIdContratoNombre() {
        mostrarOcultarIdContratoNombre = !mostrarOcultarIdContratoNombre;

        if(mostrarOcultarIdContratoNombre) {
            tvNombreClienteContratoPrincipal.setVisibility(View.VISIBLE);
            tvIdContratoClienteContratoPrincipal.setVisibility(View.GONE);
        }else {
            tvIdContratoClienteContratoPrincipal.setVisibility(View.VISIBLE);
            tvNombreClienteContratoPrincipal.setVisibility(View.GONE);
        }
    }

    public void mostrarOcultarTvColoniaNota() {
        mostrarOcultarColoniaNota = !mostrarOcultarColoniaNota;

        if(mostrarOcultarColoniaNota) {
            tvNotaClienteContratoPrincipal.setVisibility(View.VISIBLE);
            tvColoniaClienteContratoPrincipal.setVisibility(View.GONE);
        }else {
            tvColoniaClienteContratoPrincipal.setVisibility(View.VISIBLE);
            tvNotaClienteContratoPrincipal.setVisibility(View.GONE);
        }
    }

 */
    private void agregarAUltimoContratoVisto(String idContrato) {

        String idUsuario = "";
        String idUltimoContrato = idContrato;

        try {
            idUsuario = seguridad.cifrar((obtenerRol.obtenerAtributoUsuarioLogeado("ID")).getBytes("UTF-16LE"));
            idUltimoContrato = seguridad.cifrar((idUltimoContrato).getBytes("UTF-16LE"));
        } catch (Exception e) {
            global.escribirError(e, 47);
            e.printStackTrace();
        }

        global.actualizarAtributoTabla("MOVIL","ULTIMOCONTRATO",idUltimoContrato,"ID",idUsuario);

    }

    public void pintarUltimoContratoVisto(){

        String idUltimoContrato = obtenerRol.obtenerAtributoUsuarioLogeado("ULTIMOCONTRATO");

        sqLiteDB = conexion.getReadableDatabase();
        String SQL = "SELECT C.ID_CONTRATO, C.NOMBRE, C.ESTATUS_ESTADOCONTRATO, C.FECHAATRASO, C.LOCALIDADENTREGA, C.COLONIAENTREGA, C.CALLEENTREGA, C.ENVIADOPAGINA," +
                " (SELECT COUNT(H.ID) FROM HISTORIALCLINICO H WHERE C.ID_CONTRATO = H.ID_CONTRATO AND (LENGTH(H.OBSERVACIONES) > 0 OR LENGTH(H.OBSERVACIONESINTERNO) > 0)) AS OBSERVACION," +
                " C.NUMEROENTREGA, C.PAGO, C.NOTA, C.CONTRATOENVIADO, (SELECT CREATED_AT FROM ABONOS A WHERE A.ID_CONTRATO = C.ID_CONTRATO AND A.TIPOABONO NOT IN (7) " +
                " ORDER BY A.CREATED_AT DESC LIMIT 1) AS ULTIMOABONO, C.OPCIONLUGARENTREGA, C.LOCALIDAD, C.COLONIA, C.CALLE, C.NUMERO FROM CONTRATOS C WHERE C.ID_CONTRATO ='" + idUltimoContrato + "' LIMIT 1";
        Cursor datos = sqLiteDB.rawQuery(SQL, null);

        if (datos.getCount() == 0) {
            //No hay datos
            lvListaContratos.removeAllViewsInLayout();
            lvListaContratos.postInvalidate();
        } else {
            //Si hay datos

            FilaContratos fila = new FilaContratos();

            //Agregacion de encabezado Ultimo Contrato
            fila.setDiasAtrasoContrato("ULTIMO CONTRATO");
            /*
            if (mostrarOcultarIdContratoNombreLista) {
                if (mostrarOcultarColoniaNotaLista) {
                    fila.setIdContrato("ULTIMO CONTRATO");
                } else {
                    fila.setNumeroClienteContrato("ULTIMO CONTRATO");
                }
            } else {
                if (mostrarOcultarColoniaNotaLista) {
                    fila.setNombreClienteContrato("ULTIMO CONTRATO");
                } else {
                    fila.setNumeroClienteContrato("ULTIMO CONTRATO");
                }
            }
             */
            fila.setEsRuta("");

            estadoContratos.add(20);
            filaContratos.add(fila);
            encabezados.add(filaContratos.size() - 1);
            diasAtrasados.add(0);

            while (datos.moveToNext()) {
                //Obtener datos y agregarlos a la lista
                fila = new FilaContratos();
                fila.setIdContrato(datos.getString(0));
                fila.setNombreClienteContrato(datos.getString(1).toUpperCase());
                fila.setUltimoAbono(datos.getString(13));
                if(datos.getString(2).equals("12") && datos.getString(14).equals("0")){
                    //Contrato en ENVIADO y Entrega de producto en lugar de venta
                    fila.setLocalidadClienteContrato(datos.getString(15).toUpperCase());
                    fila.setColoniaClienteContrato(datos.getString(16).toUpperCase());
                    fila.setCalleClienteContrato(datos.getString(17).toUpperCase());
                    fila.setNumeroClienteContrato(datos.getString(18).toUpperCase());
                }else{
                    //Entrega de producto en lugar de cobranza
                    fila.setLocalidadClienteContrato(datos.getString(4).toUpperCase());
                    fila.setColoniaClienteContrato(datos.getString(5).toUpperCase());
                    fila.setCalleClienteContrato(datos.getString(6).toUpperCase());
                    fila.setNumeroClienteContrato(datos.getString(9).toUpperCase());
                }
                fila.setEsRuta("0");
                fila.setFormaPagoClienteContrato(obtenerLetraFormaPagoContrato(datos.getInt(10)));
                fila.setNotaClienteContrato(datos.getString(11).toUpperCase());
                if (datos.getString(7).equals("0")) {
                    fila.setEnviadoPaginaClienteContrato("P," + datos.getString(12));
                } else {
                    fila.setEnviadoPaginaClienteContrato("S," + datos.getString(12));
                }
                //Observaciones historial
                if (datos.getInt(8) > 0) {
                    //Sin observaciones
                    fila.setObservacionesClienteContrato("S");
                } else {
                    //Con observaciones
                    fila.setObservacionesClienteContrato("N");
                }
                fila.setEstadoContratoClienteContrato(datos.getString(2));
                estadoContratos.add(Integer.parseInt(datos.getString(2)));

                if (datos.getInt(2) == 4) {
                    //Estado del contrato es igual a ABONOATRASADO
                    diasAtrasados.add(obtenerDiasAtrasados(datos.getString(3)));
                    fila.setDiasAtrasoContrato(Integer.toString(obtenerDiasAtrasados(datos.getString(3))));
                } else {
                    //Estado del contrato es diferente a ABONOATRASADO por lo tanto no tiene fechaatraso
                    diasAtrasados.add(0);
                    fila.setDiasAtrasoContrato("0");
                }

                fila.setNotaClienteContrato(datos.getString(11).toUpperCase());

                filaContratos.add(fila);
            }

        }
        sqLiteDB.close();
        datos.close();
    }

    public void crearTablaTopVentas(ArrayList<String> topOptometristas,ArrayList<String> topAsistentes){
        //Eliminar filas de la tabla
        tblTopVenta.removeViewsInLayout(1,tblTopVenta.getChildCount()-1);
        //Llenar tabla con optometristas
        for (int i = 0; i < topOptometristas.size() ; i++) {
            String atributos [] = topOptometristas.get(i).split("-");

            //Fila de nombre
            TableRow fila = new TableRow(fragmento.getContext());
            TextView nombreOpto = new TextView(fragmento.getContext());
            nombreOpto.setText(atributos[0].toUpperCase());
            nombreOpto.setWidth(95);
            nombreOpto.setTextColor(Color.BLACK);
            nombreOpto.setTextSize(10);
            nombreOpto.setPadding(0,0,0,10);
            fila.addView(nombreOpto);

            //Numero de ventas
            TextView numVentas = new TextView(fragmento.getContext());
            numVentas.setText(atributos[1]);
            numVentas.setTextColor(Color.BLACK);
            numVentas.setTextSize(10);
            numVentas.setGravity(Gravity.CENTER);
            numVentas.setPadding(35,0,0,0);
            fila.addView(numVentas);

            //Nombre de sucursal
            TextView sucursal = new TextView(fragmento.getContext());
            sucursal.setText(atributos[2].toUpperCase());
            sucursal.setTextColor(Color.BLACK);
            sucursal.setTextSize(10);
            sucursal.setPadding(35,0,0,0);
            fila.addView(sucursal);
            tblTopVenta.addView(fila);
        }

        //Agregamos Asistentes a la tabla

        //Fila para Asistentes
        TableRow fila = new TableRow(fragmento.getContext());
        TextView encabezado = new TextView(fragmento.getContext());
        encabezado.setText("ASISTENTE");
        encabezado.setWidth(95);
        encabezado.setTextColor(Color.parseColor("#0AA09E"));
        encabezado.setTextSize(12);
        encabezado.setTypeface(null, Typeface.BOLD);
        encabezado.setPadding(0,0,0,10);
        fila.addView(encabezado);
        tblTopVenta.addView(fila);

        for (int i = 0; i < topAsistentes.size() ; i++) {
            String atributos [] = topAsistentes.get(i).split("-");

            //Fila de nombre
            TableRow filaAsistente = new TableRow(fragmento.getContext());
            TextView nombreAsistente = new TextView(fragmento.getContext());
            nombreAsistente.setText(atributos[0].toUpperCase());
            nombreAsistente.setWidth(95);
            nombreAsistente.setTextColor(Color.BLACK);
            nombreAsistente.setTextSize(10);
            nombreAsistente.setPadding(0,0,0,10);
            filaAsistente.addView(nombreAsistente);

            //Numero de ventas
            TextView numVentas = new TextView(fragmento.getContext());
            numVentas.setText(atributos[1]);
            numVentas.setTextColor(Color.BLACK);
            numVentas.setTextSize(10);
            numVentas.setGravity(Gravity.CENTER);
            numVentas.setPadding(35,0,0,0);
            filaAsistente.addView(numVentas);

            //Nombre de sucursal
            TextView sucursal = new TextView(fragmento.getContext());
            sucursal.setText(atributos[2].toUpperCase());
            sucursal.setTextColor(Color.BLACK);
            sucursal.setTextSize(10);
            sucursal.setPadding(35,0,0,0);
            filaAsistente.addView(sucursal);
            tblTopVenta.addView(filaAsistente);
        }

    }

    public void subirImagenesAFTUltimas48Horas(String imagenSubir, String banderaEstadoImagen) {
        //Estado imagen
        // 0 -> por subir
        // 1 -> por verificar en servidor
        // 2 -> en el servidor

        //Directorio para entrar a carpeta de imagenes de app
        String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File direccionNoMedia = new File(file_path + "/luzatuvida/.nomedia");

        //Verificar si existe imagen en el movil
        File existeImagenMovil = new File(direccionNoMedia + "/" + imagenSubir);
        if(existeImagenMovil.exists()) {

            String[] carpetas = new String[] {"fotoine", "fotoineatras", "fotocasa", "comprobantedomicilio", "tarjetapension", "tarjetapensionatras", "pagare", "fotootros", "fotomovimiento"};
            int indiceCarpeta = 0;

            //Optener carpeta a la que pertenece la imagen u subir a servidor
            if (imagenSubir.contains("Ine-Frente")) {
                //Foto Ine frente
                indiceCarpeta = 0;
            } else if (imagenSubir.contains("Ine-Atras")) {
                //Foto Ine Atras
                indiceCarpeta = 1;
            } else if (imagenSubir.contains("Casa")) {
                //Foto casa
                indiceCarpeta = 2;
            } else if (imagenSubir.contains("Comprobante-Domicilio")) {
                //Foto comprobante
                indiceCarpeta = 3;
            } else if (imagenSubir.contains("Tarjeta-Pension-Contrato")) {
                //Tarjeta pension
                indiceCarpeta = 4;
            } else if (imagenSubir.contains("Tarjeta-Pension-Atras-Contrato")) {
                indiceCarpeta = 5;
            } else if (imagenSubir.contains("Pagare-Contrato")) {
                indiceCarpeta = 6;
            } else if (imagenSubir.contains("Foto-Otros")) {
                indiceCarpeta = 7;
            }

            File direccionImagenSubir = new File(direccionNoMedia + "/" + imagenSubir);

            if (banderaEstadoImagen.equals("0")) {
                //Subir imagen a ftp en su carpeta correspondiente
                Log.i("MENSAJE", imagenSubir + " subiendo a FTP");
                Sincronizacion.subirArchivoPendienteFtp cargar = new Sincronizacion.subirArchivoPendienteFtp(direccionImagenSubir.getAbsolutePath(), imagenSubir, carpetas[indiceCarpeta]);
                cargar.execute();
            } else if (banderaEstadoImagen.equals("1")) {
                //Verificar si ya se subio imagen a servidor
                Log.i("MENSAJE", "Verificando " + imagenSubir + " en servidor FTP");
                Sincronizacion.verificarArchivoEnFTP cargar = new Sincronizacion.verificarArchivoEnFTP(imagenSubir, carpetas[indiceCarpeta]);
                cargar.execute();
            } else if (banderaEstadoImagen.equals("2")) {
                //Eliminar registros con bandera en 2
                Log.i("MENSAJE", "Limpiando tabla IMAGENESPENDIENTESMOVIL");
                global.eliminarRegistroTablaImagenesPendientesMovil();
            }

        }
    }

    public void mostrarAlertDialogRegistrarHoraSalida() {
        final Spinner spListaAsistenciaVentas = new Spinner(fragmento.getActivity());

        try{

            sqLiteDB = conexion.getReadableDatabase();
            String SQL = "SELECT ID_USUARIO, NOMBRE FROM ASISTENCIA WHERE REGISTROSALIDA = ''";
            Cursor datos = sqLiteDB.rawQuery(SQL, null);

            if (datos.moveToFirst()==false){
                Log.i("MENSAJE", "No hay usuarios registrados");
            }

            idsUsuariosVentas = new String[datos.getCount() + 1];
            nombreUsuariosVentas = new String[datos.getCount() + 1];
            idsUsuariosVentas[0] = "";
            nombreUsuariosVentas[0] = "SELECCIONAR USUARIO";

            if(datos.getCount()>0){
                for(int i=1; i <= datos.getCount(); i++){
                    idsUsuariosVentas[i]= datos.getString(0);
                    nombreUsuariosVentas[i] = datos.getString(1);
                    datos.moveToNext();
                }
            }
            sqLiteDB.close();
            datos.close();

            spListaAsistenciaVentas.setAdapter(new ArrayAdapter<String>(fragmento.getActivity(), android.R.layout.simple_spinner_dropdown_item, nombreUsuariosVentas));

        }catch (SQLiteException e){
            global.escribirError(e, 331);
            Log.i("ERRORBD", e.getMessage() + "");
        }

        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Registrar salida").setView(spListaAsistenciaVentas).setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
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

                if (idsUsuariosVentas[spListaAsistenciaVentas.getSelectedItemPosition()] == "") {
                    //No selecciono usuario
                    Toast.makeText(fragmento.getActivity(), "Debes seleccionar un usuario", Toast.LENGTH_LONG).show();
                } else {
                    String idUsuario = idsUsuariosVentas[spListaAsistenciaVentas.getSelectedItemPosition()];
                    String asistencia = global.obtenerAtributoTabla("ASISTENCIA","ASISTENCIA","ID_USUARIO", idUsuario);
                    if(!asistencia.equals("0")){
                        //Tiene asistencia o retardo el usuario en su registro de entrada
                        String registroSalida = global.obtenerAtributoTabla("ASISTENCIA","REGISTROSALIDA","ID_USUARIO", idUsuario);
                        if(registroSalida.length() == 0){
                            //No cuenta con registro de salida
                            time = new SimpleDateFormat(" HH:mm:ss").format(new Date());
                            global.actualizarAtributoTabla("ASISTENCIA","REGISTROSALIDA",fechaactual +" "+time,"ID_USUARIO",idUsuario);
                            global.actualizarAtributoTabla("ASISTENCIA","ENVIADOPAGINA","0","ID_USUARIO",idUsuario);
                            Toast.makeText(fragmento.getActivity(), "Hora de salida registrada correctamente", Toast.LENGTH_LONG).show();
                            llamadaSincronizacion();
                        }else{
                            //Ya cuenta con registro de salida
                            Toast.makeText(fragmento.getActivity(), "Ya registraste salida para el usuario", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        //El usuario cuenta con falta
                        Toast.makeText(fragmento.getActivity(), "No puedes registrar la salida, usuario con inasistencia", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }
}
