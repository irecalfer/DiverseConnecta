package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Parte;

import java.util.ArrayList;

public class ParteAdapter extends ArrayAdapter<Parte> {
    private final ArrayList<Parte> listaPartes;


    public ParteAdapter(Context context, ArrayList<Parte> listaPartes) {
        super(context, 0, listaPartes);
        this.listaPartes = listaPartes;
    }

    public View inflaElHeader(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.header_tablelayout_parte, parent, false);
        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Parte parte = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.filas_tablelayout_parte, parent, false);
        }
        //convertView = LayoutInflater.from(getContext()).inflate(R.layout.filas_tablelayout_parte, parent, false);

        TextView textViewPaciente = convertView.findViewById(R.id.tvRellenaPaciente);
        TextView textViewDescripcion = convertView.findViewById(R.id.tvRellenaDescripcion);
        TextView textViewEmpleado = convertView.findViewById(R.id.tvRellenaEmpleado);
        TextView textViewFechaParte = convertView.findViewById(R.id.tvRellenafechaParte);


        // Configura los valores de las vistas de texto con los datos del parte
        if (parte != null) {
            textViewPaciente.setText(parte.getNombreApellidosPaciente());
            textViewDescripcion.setText(parte.getDescripcion());
            textViewEmpleado.setText(parte.getEmpleado());
            textViewFechaParte.setText(parte.getFecha());
        }
        return convertView;

    }

}
