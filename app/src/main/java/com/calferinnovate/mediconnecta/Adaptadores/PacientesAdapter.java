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
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClickListener;
import com.calferinnovate.mediconnecta.clases.Pacientes;

import java.util.ArrayList;

public class PacientesAdapter extends RecyclerView.Adapter<PacientesAdapter.PacientesViewHolder>{

    //Interface para que el fragment implemente el listener y poder capturar el evento fuera del adaptador
    public interface ItemClickListener{
        public void onClick(View view, int position);
    }
    private ArrayList<Pacientes> listaPacientes;
    private Context mContext;
    private ItemClickListener clickListener;

    public PacientesAdapter(ArrayList<Pacientes> listaPacientes, Context context){
        this.listaPacientes = listaPacientes;
        mContext = context;
    }

    @NonNull
    @Override
    public PacientesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_residentes, parent, false);
        return new PacientesViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull PacientesViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Pacientes pacientes = listaPacientes.get(position);
        //Seteando nombre
        //holder.fotoPacienteImageView.setImage
        Glide.with(mContext).load(pacientes.getFoto()).circleCrop().into(holder.fotoPacienteImageView);
        holder.nombreCompletoTextView.setText(pacientes.getNombre()+" "+pacientes.getApellidos());
        holder.habitacionTextView.setText("Habitación:"+" "+pacientes.getFkNumHabitacion());

    }

    @Override
    public int getItemCount() {
        return listaPacientes.size();
    }


    public class PacientesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Campos respectivos de un item
        private final ImageView fotoPacienteImageView;
        private final TextView nombreCompletoTextView;
        private final TextView habitacionTextView;


        public PacientesViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            fotoPacienteImageView = itemView.findViewById(R.id.idResidenteFoto);
            nombreCompletoTextView = itemView.findViewById(R.id.idNombreCompleto);
            habitacionTextView = itemView.findViewById(R.id.idHabitacion);

        }

        public ImageView getFotoPacienteImageView() {
            return fotoPacienteImageView;
        }

        public TextView getNombreCompletoTextView() {
            return nombreCompletoTextView;
        }

        public TextView getHabitacionTextView() {
            return habitacionTextView;
        }

        //Esto propaga el evento hacia afuera, así podemos capturarlo en el punto que queramos de
        //nuestra aplicación
        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }

    //Este metodo se utiliza desde el fragmento que captura el evento de clic de los items
    public void setOnClickListener(ItemClickListener clickListener){
        this.clickListener = clickListener;
    }


}
