package com.calferinnovate.mediconnecta.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Model.Especialista;
import com.calferinnovate.mediconnecta.Model.Seguimiento;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class EspecialistasAdapter extends RecyclerView.Adapter<EspecialistasAdapter.EspecialistasViewHolder> {

    /**
     * Interfaz para que el fragmento implemente el listener y poder capturar el evento fuera del adaptador
     */
    public interface ItemClickListener {
        void onClick(int position);
    }

    private final Context mContext;
    private ItemClickListener clickListener;
    private ArrayList<Especialista> especialistaArrayList;

    public EspecialistasAdapter(ArrayList<Especialista> especialistaArrayList, Context mContext, ItemClickListener clickListener){
        this.especialistaArrayList = especialistaArrayList;
        this.mContext = mContext;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public EspecialistasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_reciclew_view_especialistas, parent, false);
        return new EspecialistasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EspecialistasViewHolder holder, int position) {
        Especialista especialista = especialistaArrayList.get(position);

        configuraDatosYTamaño(holder, especialista);
    }

    @Override
    public int getItemCount() {
        return especialistaArrayList.size();
    }


    public class EspecialistasViewHolder extends RecyclerView.ViewHolder {

        private final TextInputEditText tietEspecialidad;
        private final TextInputEditText tietNombre;
        private final TextInputEditText tietCentro;
        private final TextInputEditText tietTelefono;
        private final TextInputEditText tietEmail;
        private long doubleClickLastTime =0L;
        public EspecialistasViewHolder(@NonNull View itemView) {
            super(itemView);
            tietEspecialidad = itemView.findViewById(R.id.etEspecialidad);
            tietNombre = itemView.findViewById(R.id.etNombre);
            tietCentro = itemView.findViewById(R.id.etCentro);
            tietTelefono = itemView.findViewById(R.id.etTelefono);
            tietEmail = itemView.findViewById(R.id.etEmail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(System.currentTimeMillis() - doubleClickLastTime < 300){
                        doubleClickLastTime = 0;
                        clickListener.onClick(getAdapterPosition());
                    }else{
                        doubleClickLastTime = System.currentTimeMillis();
                    }
                }
            });
        }
    }

    public void configuraDatosYTamaño(EspecialistasViewHolder holder, Especialista especialista){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedes ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);

        holder.tietEspecialidad.setText(especialista.getEspecialidad());
        holder.tietEspecialidad.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.tietNombre.setText(especialista.getNombre());
        holder.tietNombre.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.tietCentro.setText(especialista.getCentro());
        holder.tietCentro.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.tietTelefono.setText(especialista.getTelefono());
        holder.tietTelefono.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.tietEmail.setText(especialista.getEmail());
        holder.tietEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
    }
}
