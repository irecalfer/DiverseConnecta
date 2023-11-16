package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.View;

import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PartePacienteAdapter {
    private Pacientes paciente;
    private ClaseGlobal claseGlobal;
    private Context context;
    private TextInputEditText fechaHora;
    private TextInputEditText pacienteNombreApellido;
    private TextInputEditText empleadoNombre;

    public PartePacienteAdapter(Pacientes paciente, ClaseGlobal claseGlobal, Context context){
        this.paciente = paciente;
        this.claseGlobal = claseGlobal;
        this.context = context;
    }

    public void rellenaUI(View view){
        enlazarVariablesConWidgets(view);
        seteaDatos();
    }

    private void enlazarVariablesConWidgets(View view){
        fechaHora = view.findViewById(R.id.fechaHoraParte);
        pacienteNombreApellido = view.findViewById(R.id.pacienteParte);
        empleadoNombre = view.findViewById(R.id.empleadoParte);
    }

    private void seteaDatos(){
        fechaHora.setText(obtieneFechaYHoraFormateada());
        pacienteNombreApellido.setText(paciente.getNombre()+" "+paciente.getApellidos());
        empleadoNombre.setText(claseGlobal.getEmpleado().getNombre()+" "+claseGlobal.getEmpleado().getApellidos());
    }

    private String obtieneFechaYHoraFormateada(){

        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter fechaHora = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaHora.format(LocalDateTime.now());
        return fechaFormateada;
    }
}
