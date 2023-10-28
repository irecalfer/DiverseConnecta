package com.calferinnovate.mediconnecta.Adaptadores;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.clases.Avisos;

import java.util.ArrayList;

public class AvisosAdapter extends RecyclerView.Adapter{

    private ArrayList<Avisos> listaAvisos;

    // Obtener referencias de los componentes visuales para cada elemento
    // Es decir, referencias de los EditText, TextViews, Buttons
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // en este ejemplo cada elemento consta solo de un título
        public TextView textViewAvisos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        /*public ViewHolder(TextView tv) {
            super(v);
            textView = tv;
        }*/
    }

    // Este es nuestro constructor (puede variar según lo que queremos mostrar)
    public AvisosAdapter(ArrayList<Avisos> lAvisos) {
        listaAvisos = lAvisos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
