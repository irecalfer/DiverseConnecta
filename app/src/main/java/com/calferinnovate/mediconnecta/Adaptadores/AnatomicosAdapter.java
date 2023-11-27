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
 * Adaptador usado para poblar el RecyclerView de Pautas rutina pañal.
 */
public class AnatomicosAdapter extends RecyclerView.Adapter<AnatomicosAdapter.AnatomicosViewHolder> {

    private final ArrayList<Pautas> listaPautas;
    private final boolean muestraNoAnatomicos;
    private Context context;

    /**
     * Constructor del adaptador
     *
     * @param listaPautas         lista de pautas del paciente
     * @param context             contexto
     * @param muestraNoAnatomicos boolean que indica si el paciente tiene rutina de pañal o no
     */
    public AnatomicosAdapter(ArrayList<Pautas> listaPautas, Context context, boolean muestraNoAnatomicos) {
        this.listaPautas = listaPautas;
        this.muestraNoAnatomicos = muestraNoAnatomicos;
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
    public AnatomicosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (muestraNoAnatomicos) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_no_anatomicos, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_anatomicos, parent, false);
        }
        return new AnatomicosViewHolder(view);
    }


    /**
     * Método que reemplaza el contenido de la vista, es invocado por el layout manager.
     * Obtiene el elemento de listaPautas en cierta posición y reemplaza el contenido de la vista con ese elemento.
     * Si la lista de pautas está vacía, indica que no tiene rutina de anatómicos, en caso contrario si tiene
     * rutina de pañal indica que tipo. Si tiene pautas pero no rutina de pañal, indica que no tiene anatómico.
     *
     * @param holder   El ViewHolder que debe actualizarse para representar el contenido del en la posición
     *                 dada en la lista.
     * @param position La posición del elemento dentro de lista del adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull AnatomicosViewHolder holder, int position) {
        if (listaPautas.isEmpty()) {
            holder.textViewNoAnatomicos.setText("No tiene rutina de anatómicos");
        } else {
            Pautas pautas = listaPautas.get(position);
            String mañanaPauta = pautas.getMañana();
            if (mañanaPauta != null && !mañanaPauta.isEmpty()) {
                holder.mañana.setText(pautas.getMañana());
            } else {
                holder.mañana.setText("Sin anatómico");
            }

            String tardePauta = pautas.getTarde();
            if (tardePauta != null && !tardePauta.isEmpty()) {
                holder.tarde.setText(pautas.getTarde());
            } else {
                holder.tarde.setText("Sin anatómico");
            }

            String nochePauta = pautas.getNoche();
            if (nochePauta != null && !nochePauta.isEmpty()) {
                holder.noche.setText(pautas.getNoche());
            } else {
                holder.noche.setText("Sin anatómico");
            }
        }
    }

    /**
     * En caso de que no haya rutina de pañal se suma 1 al tamaño de la lista para que no arroje error,
     * en caso contrario se devuelve el tamaño de la lista.
     * @return Tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return listaPautas.size() + (muestraNoAnatomicos ? 1 : 0);
    }

    /**
     * Proporciona una referencia al tipo de vistas que se están utilizando.
     */
    public static class AnatomicosViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText mañana;
        private final TextInputEditText tarde;
        private final TextInputEditText noche;
        private final TextView textViewNoAnatomicos;

        public AnatomicosViewHolder(View itemView) {
            super(itemView);
            mañana = itemView.findViewById(R.id.mañanaET);
            tarde = itemView.findViewById(R.id.tardeET);
            noche = itemView.findViewById(R.id.nocheET);
            textViewNoAnatomicos = itemView.findViewById(R.id.text_view_no_anatomicos);
        }
    }
}
