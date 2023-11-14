package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Adaptadores.AnatomicosAdapter;
import com.calferinnovate.mediconnecta.Adaptadores.PautasAdapter;
import com.calferinnovate.mediconnecta.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.IOnBackPressed;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.Pautas;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class PautasPacientesGeriatriaFragment extends Fragment implements IOnBackPressed {
    private SharedPacientesViewModel sharedPacientesViewModel;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewAnatomicos;
    private ViewModelArgs viewModelArgs;
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;
    private PautasAdapter pautasAdapter;
    private AnatomicosAdapter anatomicosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pautas_pacientes_geriatria, container, false);

        llamaAClaseGlobal();

        // Obtener el Recycler
        recyclerView = view.findViewById(R.id.recyclerViewPautas);
        recyclerView.setHasFixedSize(true);
        recyclerViewAnatomicos = view.findViewById(R.id.recyclerViewAnatomicos);
        recyclerViewAnatomicos.setHasFixedSize(true);

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


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                Pacientes pacienteActual = pacientes;
                // Limpia los datos anteriores
                obtienePautasGeneralesDelPaciente(pacienteActual);
            }
        });
    }

    public void llamaAClaseGlobal() {
        claseGlobal = ClaseGlobal.getInstance();
    }

    public void obtienePautasGeneralesDelPaciente(Pacientes pacientes) {
        sharedPacientesViewModel.getListaMutablePautas(pacientes).observe(getViewLifecycleOwner(), new Observer<ArrayList<Pautas>>() {
            @Override
            public void onChanged(ArrayList<Pautas> pautas) {

                pautasAdapter = new PautasAdapter(pautas, getContext());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                // at last set adapter to recycler view.
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(pautasAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
                anatomicosAdapter = new AnatomicosAdapter(pautas, getContext());
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
                recyclerViewAnatomicos.setLayoutManager(linearLayoutManager2);
                recyclerViewAnatomicos.setAdapter(anatomicosAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

                pautasAdapter.notifyDataSetChanged();
                anatomicosAdapter.notifyDataSetChanged();


            }
        });

    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}