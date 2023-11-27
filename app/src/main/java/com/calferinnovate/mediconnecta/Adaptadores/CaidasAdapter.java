package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.calferinnovate.mediconnecta.Model.Caidas;
import com.calferinnovate.mediconnecta.R;

import java.util.ArrayList;

/**
 * Adaptador usado para poblar el TableLayout de caídas en el parte general.
 */

public class CaidasAdapter extends ArrayAdapter<Caidas> {

    /**
     * Constructor del adaptador
     *
     * @param context         Contexto
     * @param caidasArrayList Lista de partes de caídas.
     */
    public CaidasAdapter(Context context, ArrayList<Caidas> caidasArrayList) {
        super(context, 0, caidasArrayList);
    }

    /**
     * Método que infla el header el TableLayout usando el layout header_tablelayout_caidas.
     *
     * @param parent El ViewGroup al que se añadirá la nueva View después de que se vincule a una posición de adaptador.
     * @return La vista.
     */
    public View inflaElHeader(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.header_tablelayout_caidas, parent, false);
        return view;
    }

    /**
     * Método usado para poblar dinámicamente las filas del TableLayout.
     * Se enlazan los recursos de la UI con las variables y se setean los datos.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Caidas caidas = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.filas_tablelayout_caidas, parent, false);

        TextView textViewFecha = convertView.findViewById(R.id.tvRellenaFechaCaida);
        TextView textViewPaciente = convertView.findViewById(R.id.tvRellenaPacienteCaida);
        TextView textViewLugar = convertView.findViewById(R.id.tvRellenaLugarCaida);
        TextView textViewFactoresRiesgo = convertView.findViewById(R.id.tvRellenaFactoresRiesgo);
        TextView textViewCausas = convertView.findViewById(R.id.tvRellenaCausas);
        TextView textViewCircunstancias = convertView.findViewById(R.id.tvRellenaCircunstancias);
        TextView textViewConsecuencias = convertView.findViewById(R.id.tvRellenaConsecuencias);
        TextView textViewUnidad = convertView.findViewById(R.id.tvRellenaUnidadCaida);
        TextView textViewCaidaPresenciada = convertView.findViewById(R.id.tvRellenaCaidaPresenciada);
        TextView textViewAvisadoFamiliares = convertView.findViewById(R.id.tvRellenaAvisoFamiliares);
        TextView textViewObservaciones = convertView.findViewById(R.id.tvRellenaObservacionesCaida);
        TextView textViewEmpleado = convertView.findViewById(R.id.tvRellenaEmpleadoVeCaida);


        // Configura los valores de las vistas de texto con los datos de la caida
        textViewFecha.setText(caidas.getFechaHora());
        textViewPaciente.setText(caidas.getNombreApellidosPaciente());
        textViewLugar.setText(caidas.getLugarCaida());
        textViewFactoresRiesgo.setText(caidas.getFactoresRiesgo());
        textViewCausas.setText(caidas.getCausas());
        textViewCircunstancias.setText(caidas.getCircunstancias());
        textViewConsecuencias.setText(caidas.getConsecuencias());
        textViewUnidad.setText(caidas.getUnidad());
        textViewCaidaPresenciada.setText(caidas.getCaidaPresenciada());
        textViewAvisadoFamiliares.setText(caidas.getAvisadoFamiliares());
        textViewObservaciones.setText(caidas.getObservaciones());
        textViewEmpleado.setText(caidas.getEmpleadoNombreApellido());


        return convertView;
    }
}
