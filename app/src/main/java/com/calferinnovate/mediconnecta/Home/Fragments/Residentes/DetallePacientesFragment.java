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
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

public class DetallePacientesFragment extends Fragment {

    private ClaseGlobal claseGlobal;
    private TabLayout tabLayout;
    private ImageView fotoPaciente;
    private TextInputEditText nombre, apellidos, sexo, dni, lugarNacimiento, seguro, edad, fechaNacimiento,
            estadoCivil, fechaIngreso, unidad, habitacion, cipSns, numSeguridadSocial;
    private SharedPacientesViewModel sharedPacientesViewModel;


    public DetallePacientesFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_pacientes, container, false);
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
        tabLayout = view.findViewById(R.id.tabLayoutDetallePacientes);
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
    }
}