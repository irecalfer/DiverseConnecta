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

public class PacientesGeneralAdapter {

    private ImageView fotoPaciente;
    private TextInputEditText nombre, apellidos, sexo, dni, lugarNacimiento, edad, fechaNacimiento,
            estadoCivil, fechaIngreso, unidad, habitacion, cipSns, numSeguridadSocial;
    private Pacientes paciente;
    private Context context;

    public PacientesGeneralAdapter(Pacientes paciente, Context context){
        this.paciente = paciente;
        this.context = context;
    }

    public void rellenaUI(View view){
        enlazarVariablesConWidgets(view);
        seteaDatos();
    }

    public void enlazarVariablesConWidgets(View view){
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

    public void seteaDatos(){
        Glide.with(context).load(paciente.getFoto()).circleCrop().into(fotoPaciente);
        nombre.setText(paciente.getNombre());
        apellidos.setText(paciente.getApellidos());
        sexo.setText(paciente.getSexo());
        dni.setText(paciente.getDni());
        lugarNacimiento.setText(paciente.getLugarNacimiento());
        edad.setText(String.valueOf(calculaEdad(paciente)));
        fechaNacimiento.setText(formateaFecha(paciente));
        estadoCivil.setText(paciente.getEstadoCivil());
        fechaIngreso.setText(paciente.getFechaIngreso());
        unidad.setText(nombreUnidad(paciente));
        habitacion.setText(String.valueOf(paciente.getFkNumHabitacion()));
        cipSns.setText(paciente.getCipSns());
        numSeguridadSocial.setText(String.valueOf(paciente.getNumSeguridadSocial()));
    }

    private int calculaEdad(Pacientes pacientes){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNac = LocalDate.parse(pacientes.getFechaNacimiento(), fmt);
        LocalDate ahora = LocalDate.now();

        Period period = Period.between(fechaNac, ahora);
        return period.getYears();
    }

    private String formateaFecha(Pacientes pacientes){
        // Formato de entrada (año-mes-día)
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parsea la fecha de entrada
        LocalDate fechaEntrada = LocalDate.parse(pacientes.getFechaNacimiento(), formatoEntrada);

        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaEntrada.format(formatoSalida);
        return fechaFormateada;
    }

    private String nombreUnidad(Pacientes paciente){
        String nombreUnidad = new String();
        for(Unidades unidades: ClaseGlobal.getInstance().getListaUnidades()){
            if(paciente.getFkIdUnidad() == unidades.getId_unidad()){
                nombreUnidad = unidades.getNombreUnidad();
            }
        }
        return nombreUnidad;
    }
}
