package com.calferinnovate.mediconnecta.View.Home.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones.GeneralPacientesFragmentAnadidos;

public class AnadirPacienteFragment extends Fragment {

    private Button datosGeneralesBtn, contactosBtn, clinicaBtn, pautasBtn;
    private ImageView imagenNuevoPaciente;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anadir_paciente, container, false);
        enlazaRecursos(view);
        return view;
    }


    public void enlazaRecursos(View view){
        datosGeneralesBtn = view.findViewById(R.id.btnDatosGenerales);
        contactosBtn = view.findViewById(R.id.btnDatosContacto);
        clinicaBtn = view.findViewById(R.id.btnClinica);
        pautasBtn = view.findViewById(R.id.btnPautas);
        imagenNuevoPaciente = view.findViewById(R.id.imagenDelPacienteNuevo);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datosGeneralesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GeneralPacientesFragmentAnadidos()).addToBackStack(null).commit();
            }
        });
    }
}