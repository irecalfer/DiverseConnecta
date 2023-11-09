package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Informes;

import java.util.ArrayList;

public class InformesAdapter extends ArrayAdapter<Informes> {
    public InformesAdapter(Context context, ArrayList<Informes> informes) {
        super(context, 0, informes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Informes informe = getItem(position);

        // Si la vista actual es nula y la posición es 0, infla la primera fila de encabezados
        if (convertView == null && position == 0) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.header_tablelayout_informes, parent, false);
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.filas_tablelayout_informes, parent, false);
            }

            TextView textViewTipoInforme = convertView.findViewById(R.id.tvRellenaTipo);
            TextView textViewFecha = convertView.findViewById(R.id.tvRellenaFecha);
            TextView textViewCentro = convertView.findViewById(R.id.tvRellenaCentro);
            TextView textViewResponsable = convertView.findViewById(R.id.tvRellenaResponsable);
            TextView textViewServicioUnidad = convertView.findViewById(R.id.tvRellenaServicioUnidad);
            TextView textViewServicioSalud = convertView.findViewById(R.id.tvRellenaServicioSalud);
            Button buttonPDF = convertView.findViewById(R.id.buttonPDF);

            // Configura los valores de las vistas de texto con los datos del informe
            textViewTipoInforme.setText(String.valueOf(informe.getTipoInforme()));
            textViewFecha.setText(informe.getFecha());
            textViewCentro.setText(informe.getCentro());
            textViewResponsable.setText(informe.getCentro());
            textViewServicioUnidad.setText(informe.getServicioUnidadDispositivo());
            textViewServicioSalud.setText(informe.getServicioDeSalud());

            // Configura un botón para ver el PDF y maneja su acción aquí
            buttonPDF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarPDF(informe.getPdfBytes());
                }
            });
        }
        return convertView;
    }

    public void mostrarPDF(byte[] pdfBytes){

    }
}
