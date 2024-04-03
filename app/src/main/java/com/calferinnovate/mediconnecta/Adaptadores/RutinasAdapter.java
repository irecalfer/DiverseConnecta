package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.R;

import java.util.ArrayList;

/**
 * Adaptador usado para poblar el RecyclerView de lass Rutinas y consultas diarias de los pacientes.
 */
public class RutinasAdapter extends RecyclerView.Adapter<RutinasAdapter.ViewHolder> {

    private final ArrayList<PacientesAgrupadosRutinas> dataSet;
    private final ArrayList<Alumnos> pacientes;
    private final Context context;


    /**
     * Constructor del adaptador
     *
     * @param data      lista de pacientes pertenecientes a una rutina.
     * @param context   contexto
     * @param pacientes lista de pacientes.
     */
    public RutinasAdapter(ArrayList<PacientesAgrupadosRutinas> data, ArrayList<Alumnos> pacientes, Context context) {
        dataSet = data;
        this.pacientes = pacientes;
        this.context = context;
    }

    /**
     * Proporciona una referencia al tipo de vistas que se están utilizando.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreApellidos;
        private final TextView hora;
        private final TextView habitacion;
        private final ImageView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreApellidos = itemView.findViewById(R.id.nombreApellido);
            hora = itemView.findViewById(R.id.hora);
            habitacion = itemView.findViewById(R.id.habitacionPacienteRutina);
            foto = itemView.findViewById(R.id.fotoPacienteRutina);
        }

        public TextView getTextViewDatosPersonales() {
            return nombreApellidos;
        }

        public TextView getTextViewhora() {
            return hora;
        }

        public TextView getTextViewHabitacion() {
            return habitacion;
        }

        public ImageView getImageViewFoto() {
            return foto;
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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_recyclerview_rutinas, viewGroup, false);
        return new ViewHolder(view);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        for (Alumnos paciente : pacientes) {
            if (dataSet.get(position).getFkCipSns().equals(paciente.getIdAlumno())) {
                holder.getTextViewDatosPersonales().setText(paciente.getNombre() + " " + paciente.getApellidos());
                holder.getTextViewHabitacion().setText("Habitacion:" + " " + paciente.getFkAula());
                Glide.with(context).load(paciente.getFoto()).circleCrop().into(holder.getImageViewFoto());
                break;
            }
        }
        //Seteando hora
        holder.getTextViewhora().setText(dataSet.get(position).getHoraRutina());
    }

    /**
     * Método que obtiene el tamaño de la lista.
     *
     * @return Tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
