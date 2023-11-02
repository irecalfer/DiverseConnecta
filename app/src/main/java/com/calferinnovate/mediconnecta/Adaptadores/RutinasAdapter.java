package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.clases.Rutinas;

import java.util.ArrayList;

public class RutinasAdapter extends ArrayAdapter<PacientesAgrupadosRutinas> {

    private final Context mContext;
    private ArrayList<PacientesAgrupadosRutinas> dataSet;
    private final ArrayList<Pacientes> pacientes;


    private static class ViewHolder {
        TextView nombreApellidos;
        TextView hora;
    }
    public RutinasAdapter(Context c, ArrayList<PacientesAgrupadosRutinas> data, ArrayList<Pacientes> pacientes){
        super(c, R.layout.items_recyclerview_rutinas, data);
        mContext = c;
        this.dataSet = data;
        this.pacientes = pacientes;
    }
    ;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PacientesAgrupadosRutinas pacientesAgrupadosRutinas = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;


        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext()) ;
            convertView = inflater.inflate(R.layout.items_recyclerview_rutinas, parent, false);
            viewHolder.nombreApellidos = convertView.findViewById(R.id.nombreApellido);
            viewHolder.hora = convertView.findViewById(R.id.hora);

            result=convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        //Seteando nombre y apellidos
        for(Pacientes paciente: pacientes){
            if(paciente.getCipSns() == pacientesAgrupadosRutinas.getFkCipSns()){
                viewHolder.nombreApellidos.setText(paciente.getNombre()+" "+paciente.getApellidos());
                break;
            }
        }
        //Seteando hora
        viewHolder.hora.setText(pacientesAgrupadosRutinas.getHoraRutina());


        return convertView;
    }
}
