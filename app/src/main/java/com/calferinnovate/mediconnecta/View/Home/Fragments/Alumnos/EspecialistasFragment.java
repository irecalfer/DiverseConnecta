package com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Adaptadores.EspecialistasAdapter;
import com.calferinnovate.mediconnecta.Adaptadores.SeguimientoAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Especialista;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones.CreaSeguimientoFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.VisualizacionOpciones.OpcionesEspecialistasDialogFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.VisualizacionOpciones.OpcionesSeguimientoDialogFragment;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class EspecialistasFragment extends Fragment implements EspecialistasAdapter.ItemClickListener {
    private ClaseGlobal claseGlobal;
    private RecyclerView recyclerEspecialistas;
    private PeticionesJson peticionesJson;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private EspecialistasAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_especialistas, container, false);
        inicializaVariables(view);
        inicializaViewModel();
        cambiarToolbar();
        obtieneEspecialistas();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void inicializaVariables(View view){
        claseGlobal = ClaseGlobal.getInstance();
        recyclerEspecialistas = view.findViewById(R.id.RVEspecialistas);
    }

    public void inicializaViewModel() {
        ViewModelArgs viewModelArgs = new ViewModelArgs() {
            @Override
            public PeticionesJson getPeticionesJson() {
                return peticionesJson = new PeticionesJson(requireContext());
            }

            @Override
            public ClaseGlobal getClaseGlobal() {
                return claseGlobal;
            }
        };

        ViewModelFactory<SharedAlumnosViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        sharedAlumnosViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedAlumnosViewModel.class);
    }

    public void cambiarToolbar(){
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.app_bar_anade, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_añadir){
                    //new CreaSeguimientoFragment().show(getChildFragmentManager(), CreaSeguimientoFragment.TAG);
                    //adapter.notifyDataSetChanged();
                }
                return false;
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    public void obtieneEspecialistas(){
        recyclerEspecialistas.setHasFixedSize(true);
        sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
            @Override
            public void onChanged(Alumnos alumnos) {
                sharedAlumnosViewModel.getListaEspecialistas(alumnos).observe(getViewLifecycleOwner(), new Observer<ArrayList<Especialista>>() {
                    @Override
                    public void onChanged(ArrayList<Especialista> especialistas) {
                        poblaRecyclerEspecialistas(especialistas);
                    }
                });
            }
        });
    }

    public void poblaRecyclerEspecialistas(ArrayList<Especialista> especialistaArrayList){
        adapter = new EspecialistasAdapter(especialistaArrayList, requireContext(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());

        recyclerEspecialistas.setLayoutManager(linearLayoutManager);
        recyclerEspecialistas.setAdapter(adapter);

        recyclerEspecialistas.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(int position) {
        sharedAlumnosViewModel.setEspecialista(position);
        new OpcionesEspecialistasDialogFragment().show(getChildFragmentManager(), OpcionesEspecialistasDialogFragment.TAG);
        adapter.notifyDataSetChanged();
    }
}