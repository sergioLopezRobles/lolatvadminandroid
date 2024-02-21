package com.luzatuvida.lolatvadminandroid.verContrato;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;

import java.util.List;

public class adaptadorListaHistorialesClinicos extends BaseAdapter {

    List<FilaHistorialesClinicos> filaHistorialesClinicos;
    Context context;
    ObtenerRol obtenerRol;

    public adaptadorListaHistorialesClinicos(Fragment fragmento, List<FilaHistorialesClinicos> filaHistorialesClinicos) {
        this.context = fragmento.getContext();
        this.filaHistorialesClinicos = filaHistorialesClinicos;
        obtenerRol = new ObtenerRol(fragmento);
    }

    private class ViewHolder {
        TextView tvDiagnostico, tvFechaVisita, tvFechaEntrega;
    }

    @Override
    public int getCount() {
        return filaHistorialesClinicos.size();
    }

    @Override
    public Object getItem(int posicion) {
        return filaHistorialesClinicos.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return filaHistorialesClinicos.indexOf(getItem(posicion));
    }

    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {

        ViewHolder titular = null;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            view = inflater.inflate(R.layout.adaptador_historiales_clinicos, null);
            titular = new ViewHolder();
            titular.tvDiagnostico = view.findViewById(R.id.tvDiagnostico);
            titular.tvFechaVisita = view.findViewById(R.id.tvFechaVisita);
            titular.tvFechaEntrega = view.findViewById(R.id.tvFechaEntrega);

            view.setTag(titular);
        }else {
            titular = (ViewHolder)view.getTag();
        }

        FilaHistorialesClinicos fila = (FilaHistorialesClinicos)getItem(posicion);
        titular.tvDiagnostico.setText(fila.getDiagnostico());
        titular.tvFechaVisita.setText(fila.getFechaVisita());
        titular.tvFechaEntrega.setText(fila.getFechaEntrega());

        if(obtenerRol.obtenerAtributoUsuarioLogeado("ROL").equals("4")) {
            titular.tvDiagnostico.setVisibility(View.GONE);
        }

        return view;

    }
}
