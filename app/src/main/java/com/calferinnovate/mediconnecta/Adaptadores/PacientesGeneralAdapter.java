package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador que pobla el Fragmento GeneralPacientes con los datos de este.
 */
public class PacientesGeneralAdapter {

    private ImageView fotoPaciente;
    private TextInputEditText nombre, apellidos, sexo, dni, lugarNacimiento, edad, fechaNacimiento,
            estadoCivil, fechaIngreso, unidad, habitacion, cipSns, numSeguridadSocial;
    private final Pacientes paciente;
    private final Context context;

    /**
     * Constrctor del adaptador
     */
    public PacientesGeneralAdapter(Pacientes paciente, Context context) {
        this.paciente = paciente;
        this.context = context;
    }

    /**
     * Método que enlaza los recursos de la UI con las variables y setea los datos del paciente.
     *
     * @param view La vista inflada.
     */
    public void rellenaUI(View view) {
        enlazaRecursos(view);
        seteaDatos();
    }

    /**
     * Método que enlaza los recusos de la UI con las variables-
     *
     * @param view La vista inflada.
     */
    public void enlazaRecursos(View view) {
        fotoPaciente = view.findViewById(R.id.fotoPacienteDetalle);
        nombre = view.findViewById(R.id.nombrePaciente);
        apellidos = view.findViewById(R.id.apellidoPaciente);
        sexo = view.findViewById(R.id.sexoPaciente);
        dni = view.findViewById(R.id.dniPaciente);
        lugarNacimiento = view.findViewById(R.id.lugarNacimientoPaciente);
        edad = view.findViewById(R.id.edadPaciente);
        fechaNacimiento = view.findViewById(R.id.fechaNacimientoPaciente);
        estadoCivil = view.findViewById(R.id.estadoCivilPaciente);
        fechaIngreso = view.findViewById(R.id.fechaIngresoPaciente);
        unidad = view.findViewById(R.id.unidadPaciente);
        habitacion = view.findViewById(R.id.habitacionPaciente);
        cipSns = view.findViewById(R.id.cipSnsPaciente);
        numSeguridadSocial = view.findViewById(R.id.numSeguridadSocialPaciente);
    }

    /**
     * Método que actualiza la UI con los datos del paciente seleccionado.
     */
    public void seteaDatos() {
        Glide.with(context).load(paciente.getFoto()).circleCrop().into(fotoPaciente);
        nombre.setText(paciente.getNombre());
        apellidos.setText(paciente.getApellidos());
        sexo.setText(paciente.getSexo());
        dni.setText(paciente.getDni());
        lugarNacimiento.setText(paciente.getLugarNacimiento());
        edad.setText(String.valueOf(calculaEdad(paciente)));
        fechaNacimiento.setText(formateaFecha(paciente.getFechaNacimiento()));
        estadoCivil.setText(paciente.getEstadoCivil());
        fechaIngreso.setText(formateaFecha(paciente.getFechaIngreso()));
        unidad.setText(nombreUnidad(paciente));
        habitacion.setText(String.valueOf(paciente.getFkNumHabitacion()));
        cipSns.setText(paciente.getCipSns());
        numSeguridadSocial.setText(String.valueOf(paciente.getNumSeguridadSocial()));
    }

    /**
     * Método usado para calcular la edad del paciente a partir de su fecha de nacimiento.
     *
     * @param pacientes Paciente seleccionado
     * @return La edad del paciente.
     */
    private int calculaEdad(Pacientes pacientes) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNac = LocalDate.parse(pacientes.getFechaNacimiento(), fmt);
        LocalDate ahora = LocalDate.now();

        Period period = Period.between(fechaNac, ahora);
        return period.getYears();
    }

    /**
     * Método que formatea la fecha de nacimiento y de ingreso de yyyy-MM-dd a dd-MM-yyyy
     *
     * @param fecha Fecha a formatear
     * @return fecha formateada
     */
    private String formateaFecha(String fecha) {
        // Formato de entrada (año-mes-día)
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parsea la fecha de entrada
        LocalDate fechaEntrada = LocalDate.parse(fecha, formatoEntrada);

        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaEntrada.format(formatoSalida);
        return fechaFormateada;
    }


    /**
     * Método que busca la unidad a la que pertenece el paciente y actualiza la UI.
     *
     * @param paciente Paciente seleccionado
     * @return nombre de la unidad.
     */
    private String nombreUnidad(Pacientes paciente) {
        String nombreUnidad = "";
        for (Unidades unidades : ClaseGlobal.getInstance().getListaUnidades()) {
            if (paciente.getFkIdUnidad() == unidades.getId_unidad()) {
                nombreUnidad = unidades.getNombreUnidad();
            }
        }
        return nombreUnidad;
    }
}
