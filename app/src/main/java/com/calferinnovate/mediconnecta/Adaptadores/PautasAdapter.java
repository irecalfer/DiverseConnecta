package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Model.Pautas;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

/**
 * Adaptador usado para poblar el RecyclerView de Pautas.
 */

public class PautasAdapter extends RecyclerView.Adapter<PautasAdapter.PautasViewHolder> {

    private final ArrayList<Pautas> listaPautas;
    private final boolean muestraNoPautas;
    private Context context;

    /**
     * Constructor del adaptador
     *
     * @param listaPautas         lista de pautas del paciente
     * @param context             contexto
     * @param muestraNoPautas boolean que indica si el paciente tiene pautas o no
     */
    public PautasAdapter(ArrayList<Pautas> listaPautas, Context context, boolean muestraNoPautas) {
        this.listaPautas = listaPautas;
        this.muestraNoPautas = muestraNoPautas;
        this.context = context;
    }

    /**
     * Método utilizado para crear un ViewHolder nuevo, es invocado por el layout manager.
     * Crea una nueva vista, que define la UI del elemento de la lista.
     *
     * @param parent   El ViewGroup al que se añadirá la nueva View después de que se vincule a una posición de adaptador.
     * @param viewType El tipo de vista de la nueva Vista.
     * @return la vista creada.
     */

    @NonNull
    @Override
    public PautasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (muestraNoPautas) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_no_pautas, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_pautas, parent, false);
        }
        return new PautasViewHolder(view);
    }

    /**
     * Método que reemplaza el contenido de la vista, es invocado por el layout manager.
     * Obtiene el elemento de listaPautas en cierta posición y reemplaza el contenido de la vista con ese elemento.
     * Si la lista de pautas está vacía, indica que no pautas, en caso contrario si tiene indica que tipo.
     * Si tiene pautas pero no hay observaciones indica que no tiene observaciones.
     *
     * @param holder   El ViewHolder que debe actualizarse para representar el contenido del en la posición
     *                 dada en la lista.
     * @param position La posición del elemento dentro de lista del adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull PautasViewHolder holder, int position) {

        if (listaPautas.isEmpty()) {
            holder.textViewNoPautas.setText(R.string.noPautas);
        } else {
            Pautas pautas = listaPautas.get(position);
            holder.tipoPauta.setText(pautas.getTipoPauta());

            String observaciones = pautas.getObservaciones();
            if (observaciones != null && !observaciones.isEmpty()) {
                holder.observacionesPauta.setText(observaciones);
            } else {
                holder.observacionesPauta.setText(R.string.noObservaciones);
            }
        }
    }

    /**
     * En caso de que no haya pautas se suma 1 al tamaño de la lista para que no arroje error,
     * en caso contrario se devuelve el tamaño de la lista.
     * @return Tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return listaPautas.size() + (muestraNoPautas ? 1 : 0);
    }


    /**
     * Proporciona una referencia al tipo de vistas que se están utilizando.
     */
    public static class PautasViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText tipoPauta;
        private final TextInputEditText observacionesPauta;

        private final TextView textViewNoPautas;

        public PautasViewHolder(View itemView) {
            super(itemView);
            tipoPauta = itemView.findViewById(R.id.tipoPauta);
            observacionesPauta = itemView.findViewById(R.id.observacionespauta);
            textViewNoPautas = itemView.findViewById(R.id.text_view_no_pautas);
        }
    }

}
