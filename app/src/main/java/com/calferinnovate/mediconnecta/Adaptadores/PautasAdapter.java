package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Model.Pautas;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class PautasAdapter extends RecyclerView.Adapter<PautasAdapter.PautasViewHolder> {

    private final ArrayList<Pautas> listaPautas;
    private final Context context;
    private final boolean muestraNoPautas;

    public PautasAdapter(ArrayList<Pautas> listaPautas, Context context, boolean muestraNoPautas) {
        this.listaPautas = listaPautas;
        this.context = context;
        this.muestraNoPautas = muestraNoPautas;
    }

    @NonNull
    @Override
    public PautasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EDIT_TEXT && muestraNoPautas) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_no_pautas, parent, false);
            return new PautasViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_pautas, parent, false);
            return new PautasViewHolder(view);
        }
    }

    // Constante para identificar el tipo de vista del EditText dinámico
    private static final int VIEW_TYPE_EDIT_TEXT = 1;

    // Métodos para determinar el tipo de vista en una posición específica
    @Override
    public int getItemViewType(int position) {
        if (muestraNoPautas && position == getItemCount() - 1) {
            return VIEW_TYPE_EDIT_TEXT;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PautasViewHolder holder, int position) {

        if (holder.getItemViewType() == VIEW_TYPE_EDIT_TEXT) {
            holder.editTextNoPautas.setText("No tiene pautas");
        } else {
            Pautas pautas = listaPautas.get(position);
            holder.tipoPauta.setText(pautas.getTipoPauta());
            // Verificar si las observaciones son nulas o vacías
            String observaciones = pautas.getObservaciones();
            if (observaciones != null && !observaciones.isEmpty()) {
                holder.observacionesPauta.setText(pautas.getObservaciones());
            } else {
                holder.observacionesPauta.setText("No tiene observaciones.");
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaPautas.size() + (muestraNoPautas ? 1 : 0);
    }


    public class PautasViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText tipoPauta;
        private final TextInputEditText observacionesPauta;

        private final TextInputEditText editTextNoPautas;

        public PautasViewHolder(View itemView) {
            super(itemView);
            tipoPauta = itemView.findViewById(R.id.tipoPauta);
            observacionesPauta = itemView.findViewById(R.id.observacionespauta);
            editTextNoPautas = itemView.findViewById(R.id.edit_text_no_pautas);
        }
    }

}
