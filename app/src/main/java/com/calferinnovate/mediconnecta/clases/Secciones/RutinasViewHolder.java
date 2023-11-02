package com.calferinnovate.mediconnecta.clases.Secciones;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.R;

final class RutinasViewHolder extends RecyclerView.ViewHolder {
     final TextView datosPersonales;
     final TextView hora;
     final View rootView;

    public RutinasViewHolder(View view){
        super(view);
        rootView = view;
        datosPersonales = view.findViewById(R.id.nombreApellido);
        hora = view.findViewById(R.id.hora);
    }
}
