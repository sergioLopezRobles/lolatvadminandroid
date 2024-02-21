package com.luzatuvida.lolatvadminandroid.notascobranza;

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
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.luzatuvida.lolatvadminandroid.MainActivity;
import com.luzatuvida.lolatvadminandroid.contratosPrincipal.FilaContratos;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Sincronizacion;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Controlador {

    Llaves llaves;
    baseDeDatos conexion;
    Global global;
    Fragment fragmento;
    Sincronizacion sincronizacion;
    Object[] objetosWebService;
    ObtenerRol obtenerRol;
    SQLiteDatabase sqLiteDB;

    TableLayout tblNotasCobranza;

    public Controlador(Fragment fragmento, Object[] objetos) {
        this.fragmento = fragmento;
        llaves = new Llaves();
        global = new Global(fragmento);
        obtenerRol = new ObtenerRol(fragmento);
        sincronizacion = new Sincronizacion(fragmento,false);
        objetosWebService = new Object[]{obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN"), "", ""};
        conexion = new baseDeDatos(fragmento.getContext(), "basedatos", null, llaves.versiondb);
        tblNotasCobranza = (TableLayout) objetos[0];
    }

    public void cargarListaNotasCobranza() {
        //Eliminar filas de la tabla
        tblNotasCobranza.removeViewsInLayout(1,tblNotasCobranza.getChildCount()-1);

        sqLiteDB = conexion.getReadableDatabase();
        String SQL = "SELECT * FROM NOTASCOBRANZA WHERE BANDERAELIMINADO = 0 ORDER BY CREATED_AT DESC";
        Cursor datos = sqLiteDB.rawQuery(SQL, null);

        if (datos.getCount() == 0) {
            //No hay datos
            TableRow fila = new TableRow(fragmento.getContext());

            TextView renglon = new TextView(fragmento.getContext());
            renglon.setText("Sin registros");
            renglon.setTextColor(Color.BLACK);
            renglon.setTextSize(12);
            renglon.setGravity(Gravity.CENTER);
            TableRow.LayoutParams sinRegistros = new TableRow.LayoutParams(0,  ViewGroup.LayoutParams.WRAP_CONTENT, 2f);
            renglon.setLayoutParams(sinRegistros);
            fila.addView(renglon);

            tblNotasCobranza.addView(fila);

        } else {
            //Si hay datos

            while (datos.moveToNext()) {
                TableRow nota = new TableRow(fragmento.getContext());

                //Columna de fecha
                TextView fecha = new TextView(fragmento.getContext());
                fecha.setText(datos.getString(2));
                fecha.setTextColor(Color.BLACK);
                fecha.setTextSize(12);
                fecha.setGravity(Gravity.CENTER);
                nota.addView(fecha);

                //Columna de nota
                TextView descripcionNota = new TextView(fragmento.getContext());
                descripcionNota.setText(datos.getString(1));
                descripcionNota.setTextColor(Color.BLACK);
                descripcionNota.setTextSize(12);
                descripcionNota.setMaxEms(20);
                descripcionNota.setMaxLines(1);
                descripcionNota.setGravity(Gravity.CENTER);

                //Asignar un identificador al renglon
                nota.setId(Integer.parseInt(datos.getString(0)));
                nota.addView(descripcionNota);

                //Establecer evento para click de renglon en la tabla
                nota.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      mostrarAlertEditarNotaCobranza(String.valueOf(v.getId()));
                    }
                });

                //Establecer evento por renglon de click largo para eliminar
                nota.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mostrarAlertEliminarNotaCobranza(String.valueOf(v.getId()));
                        return false;
                    }
                });

                tblNotasCobranza.addView(nota);

            }

        }
        sqLiteDB.close();
        datos.close();
    }

    public void guardarNuevaNotaCobranza(String nota){

        try {

            ContentValues valores = new ContentValues();
            sqLiteDB = conexion.getWritableDatabase();

            String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            String ultimoId = global.obtenerUnResultadoQuery("SELECT ID FROM NOTASCOBRANZA ORDER BY CREATED_AT DESC LIMIT 1");
            int siguienteId = 0;
            if(!ultimoId.equals("")){
                siguienteId = Integer.parseInt(ultimoId) + 1;
            }

            valores.put("ID", siguienteId);
            valores.put("CREATED_AT", fechaHora);
            valores.put("NOTA", nota);
            valores.put("ENVIADOPAGINA", "0");
            valores.put("BANDERAELIMINADO", "0");
            sqLiteDB.insert("NOTASCOBRANZA", null, valores);

            if(sqLiteDB.isOpen()){
                sqLiteDB.close();
            }

            //Sincronizar
            llamadaSincronizacion();

        } catch (Exception e) {
            global.escribirError(e, 328);
            Log.i("ERRORBD", e.getMessage() + "");
        }

    }

    public void mostrarAlertEliminarNotaCobranza(String idNotaEliminar){

        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Eliminar nota").setMessage(Html.fromHtml("<font color='#FFACA6'><b>¿Estas seguro que quieres eliminar la nota?</b></font>"))
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Actualizar bandera de nota eliminada a 1 (Nota eliminada).
                        global.actualizarAtributoTabla("NOTASCOBRANZA","BANDERAELIMINADO","1","ID",idNotaEliminar);
                        //Cambiar estado envio pagina de registro
                        global.actualizarAtributoTabla("NOTASCOBRANZA","ENVIADOPAGINA","0","ID",idNotaEliminar);
                        //cargar lista de notas
                        cargarListaNotasCobranza();
                        //Sincronizar
                        llamadaSincronizacion();

                        Toast.makeText(fragmento.getContext(), "Nota eliminada correctamente", Toast.LENGTH_LONG).show();
                    }

                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void mostrarAlertEditarNotaCobranza(String idNotaEditar){
        final EditText etNotasCobranza = new EditText(fragmento.getActivity());
        etNotasCobranza.setHeight(200);
        LinearLayout.LayoutParams layoutReferencia = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etNotasCobranza.setLayoutParams(layoutReferencia);

        //Obtener nota para mostrar en alert
        String notaActual = global.obtenerAtributoTabla("NOTASCOBRANZA","NOTA","ID",idNotaEditar);
        etNotasCobranza.setText(notaActual);

        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(fragmento.getActivity());
        alerta.setTitle("Editar nota")
                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!etNotasCobranza.getText().toString().trim().equals("")){
                            //Actualizar nota
                            global.actualizarAtributoTabla("NOTASCOBRANZA","NOTA",etNotasCobranza.getText().toString(),"ID",idNotaEditar);
                            //Cambiar estado envio pagina de registro
                            global.actualizarAtributoTabla("NOTASCOBRANZA","ENVIADOPAGINA","0","ID",idNotaEditar);
                            //cargar lista de notas
                            cargarListaNotasCobranza();
                            //Sincronizar
                            llamadaSincronizacion();

                            Toast.makeText(fragmento.getContext(), "Nota editada correctamente.", Toast.LENGTH_LONG).show();
                        }else{
                            //Nota vacia
                            Toast.makeText(fragmento.getContext(), "Campo de Nota obligatorio.", Toast.LENGTH_LONG).show();
                        }
                    }

                }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setView(etNotasCobranza);

        final android.app.AlertDialog dialog = alerta.create();
        dialog.show();
    }

    private void llamadaSincronizacion() {
        sincronizacion.sincronizarMetodo(0, objetosWebService, 0);
    }

}
