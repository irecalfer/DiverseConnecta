package com.calferinnovate.mediconnecta.View.Home.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.calferinnovate.mediconnecta.R;

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
}