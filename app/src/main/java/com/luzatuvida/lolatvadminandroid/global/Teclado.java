package com.luzatuvida.lolatvadminandroid.global;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

public class Teclado {

    Fragment fragmento;

    public Teclado(Fragment fragmento) {
        this.fragmento = fragmento;
    }

    /*Metodo/Funcion: showKeyboard
     Descripcion: Mostrar Teclado
   */
    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) fragmento.getActivity().getSystemService(fragmento.getActivity().INPUT_METHOD_SERVICE);
        View view = fragmento.getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(fragmento.getActivity());
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /*Metodo/Funcion: hideKeyboard
     Descripcion: Ocultar Teclado
   */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) fragmento.getActivity().getSystemService(fragmento.getActivity().INPUT_METHOD_SERVICE);
        View view = fragmento.getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(fragmento.getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
