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

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.ContactoFamiliares;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;

import java.util.ArrayList;

public class ContactoFamiliaresPacienteFragment extends Fragment {
    private ClaseGlobal claseGlobal;
    private SharedPacientesViewModel sharedPacientesViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacto_familiares_paciente, container, false);
        llamaAClaseGlobal();
        //asignaVariablesAComponentes(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPacientesViewModel = new ViewModelProvider(requireActivity()).get(SharedPacientesViewModel.class);

        /*sharedPacientesViewModel.obtieneContactoFamiliares().observe(getViewLifecycleOwner(), new Observer<ArrayList<ContactoFamiliares>>() {
            @Override
            public void onChanged(ArrayList<ContactoFamiliares> contactoFamiliares) {

            }
        });*/
    }

    public void llamaAClaseGlobal(){
        claseGlobal = ClaseGlobal.getInstance();
    }
}