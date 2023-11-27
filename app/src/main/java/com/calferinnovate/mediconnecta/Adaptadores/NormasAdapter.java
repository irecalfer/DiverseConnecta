package com.calferinnovate.mediconnecta.Adaptadores;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Model.Normas;
import com.calferinnovate.mediconnecta.R;

import java.util.ArrayList;

/**
 * Adaptador utilizado para poblar el RecyclerView de las normas de la empresa.
 */
public class NormasAdapter extends RecyclerView.Adapter<NormasAdapter.NormasViewHolder> {

    private final ArrayList<Normas> listaNormas;

    /**
     * Constructor del adaptador
     *
     * @param listaNormas La lista de normas.
     */
    public NormasAdapter(ArrayList<Normas> listaNormas) {
        this.listaNormas = listaNormas;
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
    public NormasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_normas_empresa, parent, false);
        return new NormasViewHolder(view);
    }

    /**
     * Método que reemplaza el contenido de la vista, es invocado por el layout manager.
     * Obtiene el elemento de listaNormas en cierta posición y reemplaza el contenido de la vista con ese elemento.
     * Si se ha hecho click en el nombre de una norma, expande el contenido. Si se vuelve a clickar una vez
     * expandido hace invisible el contenido.
     *
     * @param holder   El ViewHolder que debe actualizarse para representar el contenido del en la posición
     *                 dada en la lista.
     * @param position La posición del elemento dentro de lista del adaptador.
     */
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

        holder.itemView.setOnClickListener(v -> {
            boolean expandido1 = norma.isExpandido();
            //Cambiamos el estado
            norma.setExpandido(!expandido1);
            //Notificamos al adaptador que el item ha cambiado
            notifyItemChanged(position);
        });


    }

    /**
     * Devuelve el tamaño de la lista de normas.
     *
     * @return Tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return listaNormas.size();
    }

    /**
     * Proporciona una referencia al tipo de vistas que se están utilizando.
     */
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
