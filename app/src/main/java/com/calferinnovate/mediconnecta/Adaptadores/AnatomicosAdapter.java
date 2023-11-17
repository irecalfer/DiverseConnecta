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

public class AnatomicosAdapter extends RecyclerView.Adapter<AnatomicosAdapter.AnatomicosViewHolder> {

    private final ArrayList<Pautas> listaPautas;
    private final Context context;
    private final boolean muestraNoAnatomicos;
    private static final int VIEW_TYPE_EDIT_TEXT = 1;

    public AnatomicosAdapter(ArrayList<Pautas> listaPautas, Context context, boolean muestraNoAnatomicos) {
        this.listaPautas = listaPautas;
        this.context = context;
        this.muestraNoAnatomicos = muestraNoAnatomicos;
    }

    @NonNull
    @Override
    public AnatomicosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EDIT_TEXT && muestraNoAnatomicos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_no_anatomicos, parent, false);
            return new AnatomicosViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_anatomicos, parent, false);
            return new AnatomicosViewHolder(view);
        }
    }

    // Métodos para determinar el tipo de vista en una posición específica
    @Override
    public int getItemViewType(int position) {
        if (muestraNoAnatomicos && position == getItemCount() - 1) {
            return VIEW_TYPE_EDIT_TEXT;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AnatomicosViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_EDIT_TEXT) {
            holder.editTextNoAnatomicos.setText("No tiene rutina de anatómicos");
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

    @Override
    public int getItemCount() {
        return listaPautas.size() + (muestraNoAnatomicos ? 1 : 0);
    }

    public class AnatomicosViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText mañana;
        private final TextInputEditText tarde;
        private final TextInputEditText noche;
        private final TextInputEditText editTextNoAnatomicos;

        public AnatomicosViewHolder(View itemView) {
            super(itemView);
            mañana = itemView.findViewById(R.id.mañanaET);
            tarde = itemView.findViewById(R.id.tardeET);
            noche = itemView.findViewById(R.id.nocheET);
            editTextNoAnatomicos = itemView.findViewById(R.id.edit_text_no_anatomicos);
        }
    }
}
