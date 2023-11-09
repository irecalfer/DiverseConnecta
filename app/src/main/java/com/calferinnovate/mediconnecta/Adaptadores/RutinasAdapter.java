package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.clases.Rutinas;

import java.util.ArrayList;

public class RutinasAdapter extends RecyclerView.Adapter<RutinasAdapter.ViewHolder> {

    private ArrayList<PacientesAgrupadosRutinas> dataSet;
    private ArrayList<Pacientes> pacientes;
    private Context context;



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreApellidos;
        private TextView hora;
        private TextView habitacion;
        private ImageView foto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreApellidos = itemView.findViewById(R.id.nombreApellido);
            hora = itemView.findViewById(R.id.hora);
            habitacion = itemView.findViewById(R.id.habitacionPacienteRutina);
            foto = itemView.findViewById(R.id.fotoPacienteRutina);
        }

        public TextView getTextViewDatosPersonales(){
            return nombreApellidos;
        }

        public TextView getTextViewhora(){
            return hora;
        }

        public TextView getTextViewHabitacion() {
            return habitacion;
        }

        public ImageView getImageViewFoto() {
            return foto;
        }
    }
    public RutinasAdapter(ArrayList<PacientesAgrupadosRutinas> data, ArrayList<Pacientes> pacientes, Context context){
        dataSet = data;
        this.pacientes = pacientes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_recyclerview_rutinas, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //Seteando nombre y apellidos
        for(Pacientes paciente: pacientes){
            if(dataSet.get(position).getFkCipSns().equals(paciente.getCipSns())){
                holder.getTextViewDatosPersonales().setText(paciente.getNombre()+" "+paciente.getApellidos());
                holder.getTextViewHabitacion().setText("Habitacion:"+" "+String.valueOf(paciente.getFkNumHabitacion()));
                Glide.with(context).load(paciente.getFoto()).circleCrop().into(holder.getImageViewFoto());
                break;
            }
        }
        //Seteando hora
        holder.getTextViewhora().setText(dataSet.get(position).getHoraRutina());
    }

    @Override
    public int getItemCount() {
        // Return the size of your dataset (invoked by the layout manager)
        return dataSet.size();
    }

    //
    public void actualizarDatos(ArrayList<PacientesAgrupadosRutinas> nuevosDatos) {
        // Itera a través de los datos existentes
        for (PacientesAgrupadosRutinas nuevoDato : nuevosDatos) {
            // Busca si el dato ya existe en la lista
            boolean encontrado = false;
            for (PacientesAgrupadosRutinas datoExistente : dataSet) {
                if (nuevoDato.getFkCipSns().equals(datoExistente.getFkCipSns())) {
                    // Actualiza los valores del dato existente
                    datoExistente.setHoraRutina(nuevoDato.getHoraRutina());
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                // Si el dato no existe en la lista, agrégalo
                dataSet.add(nuevoDato);
            }
        }
        notifyDataSetChanged();
    }

}
