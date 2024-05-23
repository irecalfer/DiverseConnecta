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

import com.calferinnovate.mediconnecta.Model.Crisis;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CrisisAdapter extends RecyclerView.Adapter<CrisisAdapter.CrisisViewHolder> {

    /**
     * Interfaz para que el fragmento implemente el listener y poder capturar el evento fuera del adaptador
     */
    public interface ItemClickListener {
        void onClick(int position);
    }

    private final Context mContext;
    private ItemClickListener clickListener;
    private ArrayList<Crisis> crisisArrayList;

    public CrisisAdapter(ArrayList<Crisis> crisisArrayList, Context mContext, ItemClickListener clickListener){
        this.crisisArrayList = crisisArrayList;
        this.mContext = mContext;
        this.clickListener = clickListener;
    }

    public class CrisisViewHolder extends RecyclerView.ViewHolder {

        private final TextInputEditText fechaAdapter;
        private final TextInputEditText lugarAdapter;
        private final TextInputEditText descripcionAdapter;
        private final TextInputEditText duracionAdapter;
        private final TextInputEditText recuperacionAdapter;
        private long doubleClickLastTime =0L;
        public CrisisViewHolder(@NonNull View itemView) {
            super(itemView);
            fechaAdapter = itemView.findViewById(R.id.etFechaRV);
            lugarAdapter = itemView.findViewById(R.id.etLugarRV);
            descripcionAdapter = itemView.findViewById(R.id.etDescripcionOpciones);
            duracionAdapter = itemView.findViewById(R.id.etDuracionOpciones);
            recuperacionAdapter = itemView.findViewById(R.id.etRecuperacionOpciones);

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

    @NonNull
    @Override
    public CrisisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_recycler_view_crisis, parent, false);
        return new CrisisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrisisViewHolder holder, int position) {
        Crisis crisis = crisisArrayList.get(position);

        configuraDatosYTamaño(holder, crisis);
    }

    public void configuraDatosYTamaño(CrisisViewHolder holder, Crisis crisis){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedes ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);

        holder.fechaAdapter.setText(fechaFormateada(crisis.getFecha()));
        holder.fechaAdapter.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.lugarAdapter.setText(crisis.getLugar());
        holder.lugarAdapter.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.descripcionAdapter.setText(crisis.getDescripcion());
        holder.descripcionAdapter.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.duracionAdapter.setText(crisis.getDuracion());
        holder.duracionAdapter.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.recuperacionAdapter.setText(crisis.getRecuperacion());
        holder.recuperacionAdapter.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
    }

    private String fechaFormateada(String fechaHora){
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Parsea la fecha de entrada
        LocalDateTime fechaEntrada = LocalDateTime.parse(fechaHora, formatoEntrada);

        // Formatea la fecha en el formato de salida
        return fechaEntrada.format(formatoSalida);
    }
    @Override
    public int getItemCount() {
        return crisisArrayList.size();
    }

    /**
     * Este metodo se utiliza desde el fragmento que captura el evento de clic de los items
     */
    public void setOnClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


}
