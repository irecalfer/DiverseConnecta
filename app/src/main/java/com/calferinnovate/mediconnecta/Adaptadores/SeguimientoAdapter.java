package com.calferinnovate.mediconnecta.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Seguimiento;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SeguimientoAdapter extends RecyclerView.Adapter<SeguimientoAdapter.SeguimientoViewHolder> {

    /**
     * Interfaz para que el fragmento implemente el listener y poder capturar el evento fuera del adaptador
     */
    public interface ItemClickListener {
        void onClick(int position);
    }


    private final Context mContext;
    private ItemClickListener clickListener;
    private ArrayList<Seguimiento> seguimientoArrayList;

    public SeguimientoAdapter(ArrayList<Seguimiento> seguimientoArrayList, Context mContext, ItemClickListener clickListener){
        this.seguimientoArrayList = seguimientoArrayList;
        this.mContext = mContext;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public SeguimientoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_reciclew_view_seguimiento, parent, false);
        return new SeguimientoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeguimientoViewHolder holder, int position) {
            Seguimiento seguimiento = seguimientoArrayList.get(position);

            configuraDatosYTamaño(holder, seguimiento);
    }

    private void configuraDatosYTamaño(SeguimientoViewHolder holder, Seguimiento seguimiento) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedes ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);

        holder.tilFecha.setText(seguimiento.getFechaHora());
        holder.tilFecha.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        holder.tilDescripcion.setText(seguimiento.getDescripcion());
        holder.tilDescripcion.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
    }

    @Override
    public int getItemCount() {
        return seguimientoArrayList.size();
    }

    public class SeguimientoViewHolder extends RecyclerView.ViewHolder {

        private final TextInputEditText tilFecha;
        private final TextInputEditText tilDescripcion;
        private long doubleClickLastTime =0L;
        public SeguimientoViewHolder(@NonNull View itemView) {
            super(itemView);
            tilFecha = itemView.findViewById(R.id.fechaSeguimientoTil);
            tilDescripcion = itemView.findViewById(R.id.descripcionSeguimientoTil);

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

    /**
     * Este metodo se utiliza desde el fragmento que captura el evento de clic de los items
     */
    public void setOnClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void cleanUp(){
        seguimientoArrayList.clear();
    }
}
