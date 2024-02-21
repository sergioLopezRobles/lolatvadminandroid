package com.luzatuvida.lolatvadminandroid.verContrato;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luzatuvida.lolatvadminandroid.R;

import java.util.List;

public class adaptadorListaAbonos extends BaseAdapter {

    List<FilaAbonos> filaAbonos;
    Context context;

    public adaptadorListaAbonos(Context context, List<FilaAbonos> filaAbonos) {
        this.context = context;
        this.filaAbonos = filaAbonos;
    }

    private class ViewHolder {
        TextView tvAbono, tvTipoAbono, tvFechaAbono, tvFolioAbono, tvMetodoPago;
    }

    @Override
    public int getCount() {
        return filaAbonos.size();
    }

    @Override
    public Object getItem(int posicion) {
        return filaAbonos.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return filaAbonos.indexOf(getItem(posicion));
    }

    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {

        ViewHolder titular = null;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            view = inflater.inflate(R.layout.adaptador_abonos, null);
            titular = new ViewHolder();
            titular.tvAbono = view.findViewById(R.id.tvAbono);
            titular.tvTipoAbono = view.findViewById(R.id.tvTipoAbono);
            titular.tvFechaAbono = view.findViewById(R.id.tvFechaAbono);
            titular.tvFechaAbono = view.findViewById(R.id.tvFechaAbono);
            titular.tvFolioAbono = view.findViewById(R.id.tvFolioAbono);
            titular.tvMetodoPago = view.findViewById(R.id.tvMetodoPago);

            view.setTag(titular);
        }else {
            titular = (ViewHolder)view.getTag();
        }

        FilaAbonos fila = (FilaAbonos)getItem(posicion);
        titular.tvAbono.setText(fila.getAbono());
        titular.tvTipoAbono.setText(fila.getTipoAbono());
        titular.tvFechaAbono.setText(fila.getFechaAbono());
        titular.tvFolioAbono.setText(fila.getFolioAbono());
        titular.tvMetodoPago.setText(fila.getMetodoPago());

        return view;
    }

}
