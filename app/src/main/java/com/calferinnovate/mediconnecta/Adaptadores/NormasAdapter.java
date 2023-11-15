package com.calferinnovate.mediconnecta.Adaptadores;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.Normas;

import java.util.ArrayList;

public class NormasAdapter extends RecyclerView.Adapter<NormasAdapter.NormasViewHolder> {

    private final ArrayList<Normas> listaNormas;

    public NormasAdapter(ArrayList<Normas> listaNormas) {
        this.listaNormas = listaNormas;
    }

    @NonNull
    @Override
    public NormasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_normas_empresa, parent, false);
        return new NormasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NormasViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Normas norma = listaNormas.get(position);

        boolean expandido = norma.isExpandido();
        if (expandido) {
            holder.contenido.setVisibility(View.VISIBLE);
        } else {
            holder.contenido.setVisibility(View.GONE);
        }
        holder.nombre.setText(norma.getNombre());
        holder.contenido.setText(norma.getContenido());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expandido = norma.isExpandido();
                //Cambiamos el estado
                norma.setExpandido(!expandido);
                //Notificamos al adaptador que el item ha cambiado
                notifyItemChanged(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaNormas.size();
    }

    public class NormasViewHolder extends RecyclerView.ViewHolder {

        private final TextView nombre;
        private final TextView contenido;

        public NormasViewHolder(View view) {
            super(view);

            nombre = view.findViewById(R.id.nombreNorma);
            contenido = view.findViewById(R.id.contenidoNorma);
        }
    }


}
