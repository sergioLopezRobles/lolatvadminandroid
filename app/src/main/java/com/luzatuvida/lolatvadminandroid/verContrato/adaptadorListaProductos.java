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

public class adaptadorListaProductos extends BaseAdapter {

    List<FilaProductos> filaProductos;
    Context context;

    public adaptadorListaProductos(Context context, List<FilaProductos> filaProductos) {
        this.context = context;
        this.filaProductos = filaProductos;
    }

    private class ViewHolder {
        TextView tvProducto, tvPrecio, tvPiezas, tvTotal;
    }

    @Override
    public int getCount() {
        return filaProductos.size();
    }

    @Override
    public Object getItem(int posicion) {
        return filaProductos.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return filaProductos.indexOf(getItem(posicion));
    }

    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {

        ViewHolder titular = null;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            view = inflater.inflate(R.layout.adaptador_productos, null);
            titular = new ViewHolder();
            titular.tvProducto = view.findViewById(R.id.tvProducto);
            titular.tvPrecio = view.findViewById(R.id.tvPrecio);
            titular.tvPiezas = view.findViewById(R.id.tvPiezas);
            titular.tvTotal = view.findViewById(R.id.tvTotal);

            view.setTag(titular);
        }else {
            titular = (ViewHolder)view.getTag();
        }

        FilaProductos fila = (FilaProductos)getItem(posicion);
        titular.tvProducto.setText(fila.getProducto());
        titular.tvPrecio.setText(fila.getPrecio());
        titular.tvPiezas.setText(fila.getPiezas());
        titular.tvTotal.setText(fila.getTotal());

        return view;
    }

}
