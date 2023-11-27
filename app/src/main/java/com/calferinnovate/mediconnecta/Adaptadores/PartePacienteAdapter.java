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
 * Adaptador que actualiza la UI del Parte con los datos del empleado y del paciente.
 */

public class PartePacienteAdapter {
    private final Pacientes paciente;
    private final ClaseGlobal claseGlobal;
    private final Context context;
    private TextInputEditText fechaHora;
    private TextInputEditText pacienteNombreApellido;
    private TextInputEditText empleadoNombre;

    /**
     * Constructor del adaptador.
     */
    public PartePacienteAdapter(Pacientes paciente, ClaseGlobal claseGlobal, Context context) {
        this.paciente = paciente;
        this.claseGlobal = claseGlobal;
        this.context = context;
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
        fechaHora = view.findViewById(R.id.fechaHoraParte);
        pacienteNombreApellido = view.findViewById(R.id.pacienteParte);
        empleadoNombre = view.findViewById(R.id.empleadoParte);
    }

    /**
     * Método que actualiza la UI con los datos del paciente seleccionado y del empleado que rellena
     * el parte.
     */
    private void seteaDatos() {
        fechaHora.setText(obtieneFechaYHoraFormateada());
        pacienteNombreApellido.setText(paciente.getNombre() + " " + paciente.getApellidos());
        empleadoNombre.setText(claseGlobal.getEmpleado().getNombre() + " " + claseGlobal.getEmpleado().getApellidos());
    }

    /**
     * Método que formatea la fecha del parte a dd-MM-yyyy HH:mm
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
