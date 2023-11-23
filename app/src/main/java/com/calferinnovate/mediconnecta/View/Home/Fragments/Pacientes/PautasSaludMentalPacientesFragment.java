package com.calferinnovate.mediconnecta.View.Home.Fragments.Pacientes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Adaptadores.PautasAdapter;
import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Pautas;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;

public class PautasSaludMentalPacientesFragment extends Fragment implements IOnBackPressed {
    private RecyclerView recyclerView;
    private ViewModelArgs viewModelArgs;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private PeticionesJson peticionesJson;
    private PautasAdapter pautasAdapter;
    private ClaseGlobal claseGlobal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pautas_salud_mental_pacientes, container, false);

        claseGlobal = ClaseGlobal.getInstance();


        obtieneRecyclerView(view);
        inicializaViewModel();


        return view;
    }

    public void obtieneRecyclerView(View view){
        // Obtener el Recycler
        recyclerView = view.findViewById(R.id.recyclerViewPautasSaludMental);
        recyclerView.setHasFixedSize(true);
    }
    public void inicializaViewModel(){
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                obtienePautasPaciente(pacientes);
            }
        });
    }

    public void obtienePautasPaciente(Pacientes pacientes){
        sharedPacientesViewModel.getListaMutablePautas(pacientes).observe(getViewLifecycleOwner(), new Observer<ArrayList<Pautas>>() {
            @Override
            public void onChanged(ArrayList<Pautas> pautas) {
                boolean mostrarEditTextNoPautas = determinaSiHayPautas(pautas);
                pautasAdapter = new PautasAdapter(pautas, getContext(), mostrarEditTextNoPautas);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                // at last set adapter to recycler view.
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(pautasAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
                pautasAdapter.notifyDataSetChanged();
            }
        });
    }
    private boolean determinaSiHayPautas(ArrayList<Pautas> pautas){
        if(pautas.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}