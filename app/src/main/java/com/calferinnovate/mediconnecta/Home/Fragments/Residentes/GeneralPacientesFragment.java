package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelFactory;
import com.calferinnovate.mediconnecta.clases.Seguro;
import com.calferinnovate.mediconnecta.clases.Unidades;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class GeneralPacientesFragment extends Fragment {


    private ImageView fotoPaciente;
    private TextInputEditText nombre, apellidos, sexo, dni, lugarNacimiento, seguroTextView, edad, fechaNacimiento,
            estadoCivil, fechaIngreso, unidad, habitacion, cipSns, numSeguridadSocial;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private ViewModelArgs viewModelArgs;
    private PeticionesJson peticionesJson;
    private String nombreSeguro;
    // Otras variables miembro
    //La variable pacienteActual se utiliza para almacenar el paciente actual que proviene del ViewModel
    // y segurosCargados es una variable booleana que se establece en true cuando la lista
    // de seguros se ha cargado correctamente. Estas variables ayudan a determinar cuándo se pueden actualizar
    // los datos en la interfaz de usuario, en el método updateUI.
    private Pacientes pacienteActual;


    public GeneralPacientesFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_pacientes, container, false);
        llamaAClaseGlobal();
        asignaVariablesAComponentes(view);

        //Creas un objeto ViewModelFactory y obtienes una instancia de ConsultasYRutinasDiariasViewModel utilizando este factory.
        //Luego, observas el LiveData del ViewModel para mantener actualizada la lista de programación en el RecyclerView.
        viewModelArgs = new ViewModelArgs() {
            @Override
            public PeticionesJson getPeticionesJson() {
                return peticionesJson = new PeticionesJson(requireContext());
            }

            @Override
            public ClaseGlobal getClaseGlobal() {
                return claseGlobal;
            }
        };

        ViewModelFactory<SharedPacientesViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        sharedPacientesViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedPacientesViewModel.class);

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                Log.d("Paciente", pacientes.getNombre());
                pacienteActual = pacientes;
                updateUI();
                // Llama al ViewModel para obtener el seguro
                sharedPacientesViewModel.obtieneSeguroPacientes(pacienteActual);

            }
        });

        sharedPacientesViewModel.getSeguro().observe(getViewLifecycleOwner(), new Observer<Seguro>() {
            @Override
            public void onChanged(Seguro seguro) {
               if(seguro!= null){
                   seguroTextView.setText(seguro.getNombreSeguro()+" "+String.valueOf(seguro.getTelefono()));
               }else{
                   seguroTextView.setText("Sin seguro");
               }
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
        seguroTextView = view.findViewById(R.id.seguroPaciente);
        edad = view.findViewById(R.id.edadPaciente);
        fechaNacimiento = view.findViewById(R.id.fechaNacimientoPaciente);
        estadoCivil = view.findViewById(R.id.estadoCivilPaciente);
        fechaIngreso = view.findViewById(R.id.fechaIngresoPaciente);
        unidad = view.findViewById(R.id.unidadPaciente);
        habitacion = view.findViewById(R.id.habitacionPaciente);
        cipSns = view.findViewById(R.id.cipSnsPaciente);
        numSeguridadSocial = view.findViewById(R.id.numSeguridadSocialPaciente);
    }


    private void updateUI(){
            Glide.with(requireContext()).load(pacienteActual.getFoto()).circleCrop().into(fotoPaciente);
            nombre.setText(pacienteActual.getNombre());
            apellidos.setText(pacienteActual.getApellidos());
            sexo.setText(pacienteActual.getSexo());
            dni.setText(pacienteActual.getDni());
            lugarNacimiento.setText(pacienteActual.getLugarNacimiento());
            edad.setText(String.valueOf(calculaEdad(pacienteActual)));
            fechaNacimiento.setText(formateaFecha(pacienteActual));
            estadoCivil.setText(pacienteActual.getEstadoCivil());
            fechaIngreso.setText(pacienteActual.getFechaIngreso());
            unidad.setText(nombreUnidad(pacienteActual));
            habitacion.setText(String.valueOf(pacienteActual.getFkNumHabitacion()));
            cipSns.setText(pacienteActual.getCipSns());
            numSeguridadSocial.setText(String.valueOf(pacienteActual.getNumSeguridadSocial()));
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