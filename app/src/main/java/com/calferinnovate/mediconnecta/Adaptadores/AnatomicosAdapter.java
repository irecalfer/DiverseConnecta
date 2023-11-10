package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Pautas;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AnatomicosAdapter extends RecyclerView.Adapter<AnatomicosAdapter.AnatomicosViewHolder> {

    private ArrayList<Pautas> listaPautas;
    private Context context;

    public AnatomicosAdapter(ArrayList<Pautas> listaPautas, Context context){
        this.listaPautas = listaPautas;
        this.context = context;
    }

    @NonNull
    @Override
    public AnatomicosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_anatomicos, parent, false);
        return new AnatomicosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnatomicosViewHolder holder, int position) {
        Pautas pautas = listaPautas.get(position);
        holder.mañana.setText(pautas.getMañana());
        holder.tarde.setText(pautas.getTarde());
        holder.noche.setText(pautas.getNoche());
    }

    @Override
    public int getItemCount() {
        return listaPautas.size();
    }

    public class AnatomicosViewHolder extends RecyclerView.ViewHolder {
        private TextInputEditText mañana;
        private TextInputEditText tarde;
        private TextInputEditText noche;

        public AnatomicosViewHolder(View itemView){
            super(itemView);
            mañana = itemView.findViewById(R.id.mañanaET);
            tarde = itemView.findViewById(R.id.tardeET);
            noche = itemView.findViewById(R.id.nocheET);
        }
    }
}
