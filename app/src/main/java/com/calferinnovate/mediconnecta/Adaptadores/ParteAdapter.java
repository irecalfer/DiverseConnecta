package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.calferinnovate.mediconnecta.Model.Parte;
import com.calferinnovate.mediconnecta.R;

import java.util.ArrayList;

/**
 * Adaptador que actualiza el TableLayout de Partes en el fragmento ParteGeneral
 */
public class ParteAdapter extends ArrayAdapter<Parte> {
    private final ArrayList<Parte> listaPartes;

    /**
     * Constructor del adaptador
     *
     * @param context     Contexto
     * @param listaPartes Lista de partes.
     */
    public ParteAdapter(Context context, ArrayList<Parte> listaPartes) {
        super(context, 0, listaPartes);
        this.listaPartes = listaPartes;
    }

    /**
     * Método que infla el header el TableLayout usando el layout header_tablelayout_parte.
     *
     * @param parent El ViewGroup al que se añadirá la nueva View después de que se vincule a una posición de adaptador.
     * @return La vista.
     */
    public View inflaElHeader(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.header_tablelayout_parte, parent, false);
        return view;
    }

    /**
     * Método usado para poblar dinámicamente las filas del TableLayout.
     * Se enlazan los recursos de la UI con las variables y se setean los datos.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Parte parte = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.filas_tablelayout_parte, parent, false);

        TextView textViewPaciente = convertView.findViewById(R.id.tvRellenaPaciente);
        TextView textViewDescripcion = convertView.findViewById(R.id.tvRellenaDescripcion);
        TextView textViewEmpleado = convertView.findViewById(R.id.tvRellenaEmpleado);
        TextView textViewFechaParte = convertView.findViewById(R.id.tvRellenafechaParte);

        textViewPaciente.setText(parte.getNombreApellidosPaciente());
        textViewDescripcion.setText(parte.getDescripcion());
        textViewEmpleado.setText(parte.getEmpleado());
        textViewFechaParte.setText(parte.getFecha());

        return convertView;
    }
}
