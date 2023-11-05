package com.calferinnovate.mediconnecta.Home.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Adaptadores.PacientesAdapter;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Pacientes;

import java.util.ArrayList;


public class ResidentesFragment extends Fragment {

    /*
    Declarar instancias globales
    */
    private RecyclerView recycler;
    private ClaseGlobal claseGlobal;
    private ArrayList<Pacientes> listaPacientes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_residentes, container, false);
        llamaAClaseGLobalYObjetos();
        // Obtener el Recycler
        recycler = view.findViewById(R.id.recyclerViewResidentes);
        recycler.setHasFixedSize(true);

        // added data from arraylist to adapter class.
        PacientesAdapter adapter = new PacientesAdapter(listaPacientes, getContext());

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);

        // at last set adapter to recycler view.
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        return view;
    }

    public void llamaAClaseGLobalYObjetos(){
        claseGlobal = ClaseGlobal.getInstance();
        listaPacientes = claseGlobal.getListaPacientes();
    }
}