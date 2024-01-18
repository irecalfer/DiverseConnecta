package com.calferinnovate.mediconnecta.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Model.Avisos;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.R;

import java.util.ArrayList;

public class AvisosAdapter extends RecyclerView.Adapter<AvisosAdapter.AvisosViewHolder> {

    private final ArrayList<Avisos> avisosArrayList;
    private final Context context;

    public AvisosAdapter(ArrayList<Avisos> avisosArrayList, Context context) {
        this.avisosArrayList = avisosArrayList;
        this.context = context;
    }

    /**
     * Proporciona una referencia al tipo de vistas que se están utilizando.
     */
    public class AvisosViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreApellidos;
        private final TextView contenido;
        private final ImageView foto;

        public AvisosViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreApellidos = itemView.findViewById(R.id.nombreEmpleadoAvisos);
            contenido = itemView.findViewById(R.id.itemAvisoListView);
            foto = itemView.findViewById(R.id.fotoEmpleadoAvisos);
        }

    }

    /**
     * Método utilizado para crear un ViewHolder nuevo, es invocado por el layout manager.
     * Crea una nueva vista, que define la UI del elemento de la lista.
     *
     * @param viewGroup El ViewGroup al que se añadirá la nueva View después de que se vincule a una posición de adaptador.
     * @param viewType  El tipo de vista de la nueva Vista.
     * @return la vista creada.
     */
    @Override
    public AvisosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_avisos_listview, viewGroup, false);
        return new AvisosViewHolder(view);
    }

    /**
     * Método que reemplaza el contenido de la vista, es invocado por el layout manager.
     * Obtiene el elemento de dataSet en cierta posición, mira cual es su CIP SNS y lo compara con la
     * lista de pacientes. Si encuentra el paciente actualiza la UI con nombre, apellidos, foto del paciente
     * y hora a la que tiene esa rutina.
     *
     * @param holder   El ViewHolder que debe actualizarse para representar el contenido del en la posición
     *                 dada en la lista.
     * @param position La posición del elemento dentro de lista del adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull AvisosViewHolder holder, int position) {
        //Seteando hora
        Avisos aviso = avisosArrayList.get(position);
        configuraFotoEmpleado(holder, aviso);
        holder.nombreApellidos.setText(aviso.getNombreEmpleado());
        holder.contenido.setText(aviso.getContenido());
    }

    private void configuraFotoEmpleado(AvisosViewHolder holder, Avisos aviso){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        int targetWidth = Math.min(screenWidth, 150); // Tamaño máximo
        int targetHeight = Math.min(screenHeight, 150); // Tamaño máximo


        Glide.with(context)
                .load(aviso.getFotoEmpleado())
                .circleCrop()
                .override(targetWidth, targetHeight)  // Aquí se establece el tamaño deseado
                .into(holder.foto);
    }
    /**
     * Método que obtiene el tamaño de la lista.
     *
     * @return Tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return avisosArrayList.size();
    }

}
