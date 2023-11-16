package com.calferinnovate.mediconnecta.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Adaptadores.PacientesAdapter;
import com.calferinnovate.mediconnecta.Home.Fragments.Residentes.DetallePacientesFragment;
import com.calferinnovate.mediconnecta.Interfaces.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class PacientesFragment extends Fragment implements PacientesAdapter.ItemClickListener, IOnBackPressed {

    /*
    Declarar instancias globales
    */
    private RecyclerView recycler;
    private ClaseGlobal claseGlobal;
    private ArrayList<Pacientes> listaPacientes;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ViewModelArgs viewModelArgs;
    private PeticionesJson peticionesJson;
    private PacientesAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_residentes, container, false);
        asignarVariables();
        poblaRecyclerPacientes(view);


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
        //sharedPacientesViewModel = new ViewModelProvider(requireActivity()).get(SharedPacientesViewModel.class);
        //sharedPacientesViewModel.obtieneSeguroPacientes();
        sharedPacientesViewModel.getPacientesList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Pacientes>>() {
            @Override
            public void onChanged(ArrayList<Pacientes> pacientes) {
                // Actualizar la lista de pacientes en el adaptador
                adapter.notifyDataSetChanged();
            }
        });


        //Implementamos la escucha al item
        adapter.setOnClickListener(this);

        return view;
    }

    public void asignarVariables() {
        claseGlobal = ClaseGlobal.getInstance();
        listaPacientes = claseGlobal.getListaPacientes();
    }

    public void poblaRecyclerPacientes(View view) {
        // Obtener el Recycler
        recycler = view.findViewById(R.id.recyclerViewResidentes);
        recycler.setHasFixedSize(true);

        // added data from arraylist to adapter class.
        adapter = new PacientesAdapter(listaPacientes, getContext(), this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        // at last set adapter to recycler view.
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(int position) {
        sharedPacientesViewModel.setPaciente(position);
        adapter.notifyDataSetChanged();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetallePacientesFragment()).commit();

    }

    @Override
    public boolean onBackPressed() {
        // Agrega la lógica específica del fragmento para manejar el retroceso.
        // Devuelve true si el fragmento maneja el retroceso, de lo contrario, devuelve false.
        // Por ejemplo, si deseas que al presionar Atrás en este fragmento vuelva a la pantalla principal:
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        return true;
    }
}