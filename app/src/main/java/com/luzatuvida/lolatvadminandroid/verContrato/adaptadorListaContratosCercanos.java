package com.luzatuvida.lolatvadminandroid.verContrato;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;

import java.util.List;

public class adaptadorListaContratosCercanos extends BaseAdapter {

    List<FilaContratosCercanos> filaContratosCercanos;
    Context context;
    ObtenerRol obtenerRol;

    public adaptadorListaContratosCercanos(Fragment fragmento, List<FilaContratosCercanos> filaContratosCercanos) {
        this.context = fragmento.getContext();
        this.filaContratosCercanos = filaContratosCercanos;
        obtenerRol = new ObtenerRol(fragmento);
    }

    private class ViewHolder {
        TextView tvContrato, tvCalle, tvNumero;
    }

    @Override
    public int getCount() {
        return filaContratosCercanos.size();
    }

    @Override
    public Object getItem(int posicion) {
        return filaContratosCercanos.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return filaContratosCercanos.indexOf(getItem(posicion));
    }

    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {

        adaptadorListaContratosCercanos.ViewHolder titular = null;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            view = inflater.inflate(R.layout.fragment_adaptador_lista_contratos_cercanos, null);
            titular = new adaptadorListaContratosCercanos.ViewHolder();
            titular.tvContrato = view.findViewById(R.id.tvContrato);
            titular.tvCalle = view.findViewById(R.id.tvCalle);
            titular.tvNumero = view.findViewById(R.id.tvNumero);

            view.setTag(titular);

        }else {
            titular = (adaptadorListaContratosCercanos.ViewHolder)view.getTag();
        }

        FilaContratosCercanos fila = (FilaContratosCercanos)getItem(posicion);
        titular.tvContrato.setText(fila.getIdContrato());
        titular.tvCalle.setText(fila.getCalle());
        titular.tvNumero.setText(fila.getNumero());

        return view;

    }
}