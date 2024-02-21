package com.luzatuvida.lolatvadminandroid.contratosPrincipal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;

import java.util.List;

public class adaptadorListaContratosPrincipal extends BaseAdapter {

    List<FilaContratos> filaContratos;
    Context context;
    ObtenerRol obtenerRol;
    List<Integer> encabezados;
//    boolean mostrarOcultarIdContratoNombreLista;
//    boolean mostrarOcultarColoniaNotaLista;

    public adaptadorListaContratosPrincipal(Fragment fragmento, List<FilaContratos> filaContratos, List<Integer> encabezados) {
        this.context = fragmento.getContext();
        this.filaContratos = filaContratos;
        obtenerRol = new ObtenerRol(fragmento);
        this.encabezados = encabezados;
//        this.mostrarOcultarIdContratoNombreLista = mostrarOcultarIdContratoNombreLista;
//        this.mostrarOcultarColoniaNotaLista = mostrarOcultarColoniaNotaLista;
    }

    private class ViewHolder {
        TextView tvIdContrato, tvNombreClienteContrato, tvLocalidadClienteContrato, tvColoniaClienteContrato, tvCalleClienteContrato, tvEnviadoPaginaClienteContrato,
                tvObservacionesClienteContrato, tvNumeroClienteContrato, tvEsRutaClienteContrato, tvFormaPagoClienteContrato, tvNotaClienteContrato,
                tvIdContratoPorAsignar, tvEstadoContratoClienteContrato, tvDiasAtrasoContrato, tvUltimoAbonoContrato;
    }

    @Override
    public int getCount() {
        return filaContratos.size();
    }

    @Override
    public Object getItem(int posicion) {
        return filaContratos.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return filaContratos.indexOf(getItem(posicion));
    }

    @SuppressLint({"Range", "ResourceType"})
    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {

        ViewHolder titular = null;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            //Vista es nula
            view = inflater.inflate(R.layout.adaptador_contrato, null);
            titular = new ViewHolder();
            titular.tvIdContrato = view.findViewById(R.id.tvIdContrato);
            titular.tvNombreClienteContrato = view.findViewById(R.id.tvNombreClienteContrato);
            titular.tvLocalidadClienteContrato = view.findViewById(R.id.tvLocalidadClienteContrato);
            titular.tvColoniaClienteContrato = view.findViewById(R.id.tvColoniaClienteContrato);
            titular.tvCalleClienteContrato = view.findViewById(R.id.tvCalleClienteContrato);
            titular.tvEnviadoPaginaClienteContrato = view.findViewById(R.id.tvEnviadoPaginaClienteContrato);
            titular.tvObservacionesClienteContrato = view.findViewById(R.id.tvObservacionesClienteContrato);
            titular.tvNumeroClienteContrato = view.findViewById(R.id.tvNumeroClienteContrato);
            titular.tvEsRutaClienteContrato = view.findViewById(R.id.tvEsRutaClienteContrato);
            titular.tvFormaPagoClienteContrato = view.findViewById(R.id.tvFormaPagoClienteContrato);
            titular.tvNotaClienteContrato = view.findViewById(R.id.tvNotaClienteContrato);
            titular.tvIdContratoPorAsignar = view.findViewById(R.id.tvIdContratoPorAsignar);
            titular.tvEstadoContratoClienteContrato = view.findViewById(R.id.tvEstadoContratoClienteContrato);
            titular.tvDiasAtrasoContrato = view.findViewById(R.id.tvDiasAtrasoContrato);
            titular.tvUltimoAbonoContrato = view.findViewById(R.id.tvUltimoAbonoContrato);

            view.setTag(titular);
        }else {
            //Vista no es nula
            titular = (ViewHolder)view.getTag();
        }

        if(obtenerRol.obtenerAtributoUsuarioLogeado("ROL").equals("4")) {
            //Rol de Cobranza

            FilaContratos fila = (FilaContratos) getItem(posicion);
/*
            if(mostrarOcultarIdContratoNombreLista) {
                titular.tvIdContrato.setText(fila.getIdContrato());
                titular.tvIdContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvIdContrato.setVisibility(View.VISIBLE);
                titular.tvNombreClienteContrato.setVisibility(View.GONE);
            }else {
                titular.tvNombreClienteContrato.setText(fila.getNombreClienteContrato());
                titular.tvNombreClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvNombreClienteContrato.setVisibility(View.VISIBLE);
                titular.tvIdContrato.setVisibility(View.GONE);
            }
*/
/*
            if(mostrarOcultarColoniaNotaLista) {
                titular.tvColoniaClienteContrato.setText(fila.getColoniaClienteContrato());
                titular.tvColoniaClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvColoniaClienteContrato.setVisibility(View.VISIBLE);
                titular.tvNotaClienteContrato.setVisibility(View.GONE);
            }else {
                titular.tvNotaClienteContrato.setText(fila.getNotaClienteContrato());
                titular.tvNotaClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvNotaClienteContrato.setVisibility(View.VISIBLE);
                titular.tvColoniaClienteContrato.setVisibility(View.GONE);
            }
*/
            if(encabezados.contains(posicion)) {
                //La posicion de la lista es un encabezado
                titular.tvDiasAtrasoContrato.setVisibility(View.VISIBLE);
                titular.tvIdContrato.setVisibility(View.GONE);
                titular.tvLocalidadClienteContrato.setVisibility(View.GONE);
                titular.tvColoniaClienteContrato.setVisibility(View.GONE);
                titular.tvCalleClienteContrato.setVisibility(View.GONE);
                titular.tvEnviadoPaginaClienteContrato.setVisibility(View.GONE);
                titular.tvNumeroClienteContrato.setVisibility(View.GONE);
                titular.tvNotaClienteContrato.setVisibility(View.GONE);
                titular.tvObservacionesClienteContrato.setVisibility(View.GONE);
                /*
                if(mostrarOcultarColoniaNotaLista) {
                    titular.tvNumeroClienteContrato.setVisibility(View.GONE);
                }else {
                    titular.tvNumeroClienteContrato.setVisibility(View.VISIBLE);
                    titular.tvNumeroClienteContrato.setText(fila.getNumeroClienteContrato());
                    titular.tvNumeroClienteContrato.setTextColor(Color.parseColor("#000000"));
                }
                 */
                titular.tvFormaPagoClienteContrato.setVisibility(View.GONE);
                titular.tvNombreClienteContrato.setVisibility(View.GONE);
                titular.tvUltimoAbonoContrato.setVisibility(View.GONE);
                titular.tvDiasAtrasoContrato.setText(fila.getDiasAtrasoContrato());
                titular.tvDiasAtrasoContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvDiasAtrasoContrato.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }else {
                //La posicion de la lista no es un encabezado
                /*
                int orientation = context.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // In landscape
                    titular.tvLocalidadClienteContrato.setVisibility(View.VISIBLE);
                    titular.tvNumeroClienteContrato.setVisibility(View.VISIBLE);
                    titular.tvFormaPagoClienteContrato.setVisibility(View.VISIBLE);
                } else {
                    // In portrait
                    titular.tvLocalidadClienteContrato.setVisibility(View.GONE);
                    titular.tvNumeroClienteContrato.setVisibility(View.GONE);
                    titular.tvFormaPagoClienteContrato.setVisibility(View.GONE);
                }
                 */

                //Hacer visibles campos a mostrar
                titular.tvEnviadoPaginaClienteContrato.setVisibility(View.VISIBLE);
                titular.tvFormaPagoClienteContrato.setVisibility(View.VISIBLE);
                titular.tvDiasAtrasoContrato.setVisibility(View.VISIBLE);
                titular.tvIdContrato.setVisibility(View.VISIBLE);
                titular.tvLocalidadClienteContrato.setVisibility(View.VISIBLE);
                titular.tvColoniaClienteContrato.setVisibility(View.VISIBLE);
                titular.tvCalleClienteContrato.setVisibility(View.VISIBLE);
                titular.tvNumeroClienteContrato.setVisibility(View.VISIBLE);
                titular.tvNombreClienteContrato.setVisibility(View.VISIBLE);
                titular.tvUltimoAbonoContrato.setVisibility(View.VISIBLE);
                titular.tvNotaClienteContrato.setVisibility(View.VISIBLE);
                titular.tvObservacionesClienteContrato.setVisibility(View.VISIBLE);

                //Asignar datos
                String[] datosEnviadoPagina = fila.getEnviadoPaginaClienteContrato().split(",");
                titular.tvEnviadoPaginaClienteContrato.setText(datosEnviadoPagina[0]);
                titular.tvEnviadoPaginaClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvFormaPagoClienteContrato.setText(fila.getFormaPagoClienteContrato());
                titular.tvFormaPagoClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvDiasAtrasoContrato.setText(fila.getDiasAtrasoContrato());
                titular.tvDiasAtrasoContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvIdContrato.setText(fila.getIdContrato());
                titular.tvIdContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvLocalidadClienteContrato.setText(fila.getLocalidadClienteContrato());
                titular.tvLocalidadClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvColoniaClienteContrato.setText(fila.getColoniaClienteContrato());
                titular.tvColoniaClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvCalleClienteContrato.setText(fila.getCalleClienteContrato());
                titular.tvCalleClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvNumeroClienteContrato.setText(fila.getNumeroClienteContrato());
                titular.tvNumeroClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvNombreClienteContrato.setText(fila.getNombreClienteContrato());
                titular.tvNombreClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvUltimoAbonoContrato.setText(fila.getUltimoAbono());
                titular.tvUltimoAbonoContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvNotaClienteContrato.setText(fila.getNotaClienteContrato());
                titular.tvNotaClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvObservacionesClienteContrato.setText(fila.getObservacionesClienteContrato());
                titular.tvObservacionesClienteContrato.setTextColor(Color.parseColor("#000000"));
                titular.tvEsRutaClienteContrato.setText(fila.getEsRuta());

            }

        }else {
            //Rol asistente/optometrista

            //Ocultar columnas
            titular.tvFormaPagoClienteContrato.setVisibility(View.GONE);
            titular.tvDiasAtrasoContrato.setVisibility(View.GONE);
            titular.tvNotaClienteContrato.setVisibility(View.GONE);
            titular.tvObservacionesClienteContrato.setVisibility(View.GONE);
            titular.tvUltimoAbonoContrato.setVisibility(View.GONE);

            //Columnas a mostrar
            titular.tvEnviadoPaginaClienteContrato.setVisibility(View.VISIBLE);
            FilaContratos fila = (FilaContratos) getItem(posicion);
            titular.tvIdContrato.setText(fila.getIdContrato());
            titular.tvIdContrato.setTextColor(Color.parseColor("#ffffff"));
            String[] datosEnviadoPagina = fila.getEnviadoPaginaClienteContrato().split(",");

            if (fila.getEstadoContratoClienteContrato().equals("0") || fila.getEstadoContratoClienteContrato().equals("13")
                || (!fila.getEstadoContratoClienteContrato().equals("0")
                    && !fila.getEstadoContratoClienteContrato().equals("13")
                    && datosEnviadoPagina[1].equals("0"))) {
                //Estado del contrato es igual a NO TERMINADO o POR CREAR, o es TERMINADO o PROCESO DE APROBACION y CONTRATOENVIADO es igual a 0
                titular.tvIdContrato.setVisibility(View.GONE);
                titular.tvIdContratoPorAsignar.setVisibility(View.VISIBLE);
                titular.tvIdContratoPorAsignar.setTextColor(Color.parseColor("#ffffff"));
            }

            titular.tvEnviadoPaginaClienteContrato.setText(datosEnviadoPagina[0]);
            titular.tvEnviadoPaginaClienteContrato.setTextColor(Color.parseColor("#ffffff"));
            titular.tvLocalidadClienteContrato.setText(fila.getLocalidadClienteContrato());
            titular.tvLocalidadClienteContrato.setTextColor(Color.parseColor("#ffffff"));
            titular.tvColoniaClienteContrato.setText(fila.getColoniaClienteContrato());
            titular.tvColoniaClienteContrato.setTextColor(Color.parseColor("#ffffff"));
            titular.tvCalleClienteContrato.setText(fila.getCalleClienteContrato());
            titular.tvCalleClienteContrato.setTextColor(Color.parseColor("#ffffff"));
            titular.tvNumeroClienteContrato.setText(fila.getNumeroClienteContrato());
            titular.tvNumeroClienteContrato.setTextColor(Color.parseColor("#ffffff"));
            titular.tvNombreClienteContrato.setText(fila.getNombreClienteContrato());
            titular.tvNombreClienteContrato.setTextColor(Color.parseColor("#ffffff"));

        }

        return view;
    }
}
