package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParteCaidasAdapter {
    private TextInputEditText fechaHora, nombreApellidoPaciente, empleadoCaida, unidadCaida;
    private Pacientes paciente;
    private Unidades unidad;
    private Empleado empleado;
    private Context context;
    private ClaseGlobal claseGlobal;

    public ParteCaidasAdapter(Pacientes paciente, Context context, ClaseGlobal claseGlobal){
        this.paciente = paciente;
        this.context = context;
        this.claseGlobal =claseGlobal;
    }

    public void rellenaUI(View view){
        enlazarVariablesConWidgets(view);
        seteaDatos();
    }

    private void enlazarVariablesConWidgets(View view){
        fechaHora = view.findViewById(R.id.fechaHoraCaida);
        nombreApellidoPaciente = view.findViewById(R.id.pacienteCaida);
        unidadCaida = view.findViewById(R.id.unidadCaida);
        empleadoCaida = view.findViewById(R.id.empleado);
    }

    private void seteaDatos(){
        fechaHora.setText(obtieneFechaYHoraFormateada());
        nombreApellidoPaciente.setText(paciente.getNombre() + " " + paciente.getApellidos());
        empleadoCaida.setText(claseGlobal.getEmpleado().getNombre() + " " + claseGlobal.getEmpleado().getApellidos());
        unidadCaida.setText(claseGlobal.getUnidades().getNombreUnidad());
    }

    private String obtieneFechaYHoraFormateada() {
        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter fechaHora = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaHora.format(LocalDateTime.now());
        return fechaFormateada;
    }
}
