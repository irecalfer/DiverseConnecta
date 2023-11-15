package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.Informes;

import java.util.ArrayList;

public class InformesAdapter extends ArrayAdapter<Informes> {

    private ItemClickListener clickListener;
    private Context context;
    private ArrayList<Informes> informes;




    //Interface para que el fragment implemente el listener y poder capturar el evento fuera del adaptador
    public interface ItemClickListener{
        public void onClick(int position);
    }

    public InformesAdapter(Context context, ArrayList<Informes> informes, ItemClickListener clickListener) {
        super(context, 0, informes);
        this.clickListener = clickListener;
    }

    public View inflaElHeader(@NonNull ViewGroup parent){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.header_tablelayout_informes, parent, false);
        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Informes informe = getItem(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.filas_tablelayout_informes, parent, false);

            TextView textViewTipoInforme = convertView.findViewById(R.id.tvRellenaTipo);
            TextView textViewFecha = convertView.findViewById(R.id.tvRellenaFecha);
            TextView textViewCentro = convertView.findViewById(R.id.tvRellenaCentro);
            TextView textViewResponsable = convertView.findViewById(R.id.tvRellenaResponsable);
            TextView textViewServicioUnidad = convertView.findViewById(R.id.tvRellenaServicioUnidad);
            TextView textViewServicioSalud = convertView.findViewById(R.id.tvRellenaServicioSalud);
            Button buttonPDF = convertView.findViewById(R.id.buttonPDF);

            // Configura los valores de las vistas de texto con los datos del informe
            textViewTipoInforme.setText(String.valueOf(informe.getTipoInforme()));
            textViewFecha.setText(informe.getFecha());
            textViewCentro.setText(informe.getCentro());
            textViewResponsable.setText(informe.getCentro());
            textViewServicioUnidad.setText(informe.getServicioUnidadDispositivo());
            textViewServicioSalud.setText(informe.getServicioDeSalud());
            // Configura un botón para ver el PDF y maneja su acción aquí
            //Esto propaga el evento hacia afuera, así podemos capturarlo en el punto que queramos de
            //nuestra aplicación
            buttonPDF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Invocar el método click del ClickListener
                    if (clickListener != null) {
                        clickListener.onClick(position);
                    }
                }
            });


        return convertView;
    }


    //Este metodo se utiliza desde el fragmento que captura el evento de clic de los items
    public void setOnClickListener(ItemClickListener clickListener){
        this.clickListener = clickListener;
    }
}
