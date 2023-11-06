package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.clases.Unidades;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class GeneralPacientesFragment extends Fragment {


    private ImageView fotoPaciente;
    private TextInputEditText nombre, apellidos, sexo, dni, lugarNacimiento, seguro, edad, fechaNacimiento,
            estadoCivil, fechaIngreso, unidad, habitacion, cipSns, numSeguridadSocial;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;


    public GeneralPacientesFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_pacientes, container, false);
        llamaAClaseGlobal();
        asignaVariablesAComponentes(view);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPacientesViewModel = new ViewModelProvider(requireActivity()).get(SharedPacientesViewModel.class);
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                updateUI(pacientes);
            }
        });
    }

    public void llamaAClaseGlobal(){
        claseGlobal = ClaseGlobal.getInstance();
    }

    public void asignaVariablesAComponentes(View view){
        fotoPaciente = view.findViewById(R.id.fotoPacienteDetalle);
        nombre = view.findViewById(R.id.nombrePaciente);
        apellidos = view.findViewById(R.id.apellidoPaciente);
        sexo = view.findViewById(R.id.sexoPaciente);
        dni = view.findViewById(R.id.dniPaciente);
        lugarNacimiento = view.findViewById(R.id.lugarNacimientoPaciente);
        seguro = view.findViewById(R.id.seguroPaciente);
        edad = view.findViewById(R.id.edadPaciente);
        fechaNacimiento = view.findViewById(R.id.fechaNacimientoPaciente);
        estadoCivil = view.findViewById(R.id.estadoCivilPaciente);
        fechaIngreso = view.findViewById(R.id.fechaIngresoPaciente);
        unidad = view.findViewById(R.id.unidadPaciente);
        habitacion = view.findViewById(R.id.habitacionPaciente);
        cipSns = view.findViewById(R.id.cipSnsPaciente);
        numSeguridadSocial = view.findViewById(R.id.numSeguridadSocialPaciente);
    }

    private void updateUI(Pacientes pacientes){
        Glide.with(requireContext()).load(pacientes.getFoto()).circleCrop().into(fotoPaciente);
        nombre.setText(pacientes.getNombre());
        apellidos.setText(pacientes.getApellidos());
        sexo.setText(pacientes.getSexo());
        dni.setText(pacientes.getDni());
        lugarNacimiento.setText(pacientes.getLugarNacimiento());
        //seguro.setText(); FALTA PARSEARLO PARA QUE MUESTRE EL NOMBRE DEL SEGURO Y EL TELEFONO
        edad.setText(String.valueOf(calculaEdad(pacientes)));
        fechaNacimiento.setText(formateaFecha(pacientes));
        estadoCivil.setText(pacientes.getEstadoCivil());
        fechaIngreso.setText(pacientes.getFechaIngreso());
        unidad.setText(nombreUnidad(pacientes));
        habitacion.setText(String.valueOf(pacientes.getFkNumHabitacion()));
        cipSns.setText(pacientes.getCipSns());
        numSeguridadSocial.setText(String.valueOf(pacientes.getNumSeguridadSocial()));
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