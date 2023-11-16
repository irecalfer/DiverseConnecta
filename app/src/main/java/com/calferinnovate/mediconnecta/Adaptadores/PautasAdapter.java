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

    public PautasAdapter(ArrayList<Pautas> listaPautas, Context context) {
        this.listaPautas = listaPautas;
        this.context = context;
    }

    @NonNull
    @Override
    public PautasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_pautas, parent, false);
        return new PautasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PautasViewHolder holder, int position) {
        Pautas pautas = listaPautas.get(position);
        holder.tipoPauta.setText(pautas.getTipoPauta());
        holder.observacionesPauta.setText(pautas.getObservaciones());


    }

    @Override
    public int getItemCount() {
        return listaPautas.size();
    }


    public class PautasViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText tipoPauta;
        private final TextInputEditText observacionesPauta;

        public PautasViewHolder(View itemView) {
            super(itemView);
            tipoPauta = itemView.findViewById(R.id.tipoPauta);
            observacionesPauta = itemView.findViewById(R.id.observacionespauta);
        }
    }


}
