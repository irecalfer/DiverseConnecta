package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.View;

import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador que actualiza la UI del Parte de caídas con los datos del empleado y del paciente.
 */
public class ParteCaidasAdapter {
    private TextInputEditText fechaHora, nombreApellidoPaciente, empleadoCaida, unidadCaida;
    private final Pacientes paciente;
    private final Context context;
    private final ClaseGlobal claseGlobal;

    /**
     * Constructor del adaptador.
     */
    public ParteCaidasAdapter(Pacientes paciente, Context context, ClaseGlobal claseGlobal) {
        this.paciente = paciente;
        this.context = context;
        this.claseGlobal = claseGlobal;
    }

    /**
     * Método utilizado para actualizar la UI.
     *
     * @param view La vista inflada.
     */
    public void rellenaUI(View view) {
        enlazaRecursos(view);
        seteaDatos();
    }

    /**
     * Método que enlaza los recusos de la UI con las variables.
     *
     * @param view La vista inflada.
     */
    private void enlazaRecursos(View view) {
        fechaHora = view.findViewById(R.id.fechaHoraCaida);
        nombreApellidoPaciente = view.findViewById(R.id.pacienteCaida);
        unidadCaida = view.findViewById(R.id.unidadCaida);
        empleadoCaida = view.findViewById(R.id.empleado);
    }

    /**
     * Método que actualiza la UI con los datos del paciente seleccionado y del empleado que rellena
     * el parte de caídas.
     */
    private void seteaDatos() {
        fechaHora.setText(obtieneFechaYHoraFormateada());
        nombreApellidoPaciente.setText(paciente.getNombre() + " " + paciente.getApellidos());
        empleadoCaida.setText(claseGlobal.getEmpleado().getNombre() + " " + claseGlobal.getEmpleado().getApellidos());
        unidadCaida.setText(claseGlobal.getUnidades().getNombreUnidad());
    }

    /**
     * Método que formatea la fecha del parte de caídas a dd-MM-yyyy HH:mm
     *
     * @return fecha formateada
     */
    private String obtieneFechaYHoraFormateada() {
        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter fechaHora = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaHora.format(LocalDateTime.now());
        return fechaFormateada;
    }
}
