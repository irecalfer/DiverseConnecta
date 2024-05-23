package com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
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

import com.calferinnovate.mediconnecta.Adaptadores.CrisisAdapter;
import com.calferinnovate.mediconnecta.Adaptadores.SeguimientoAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Crisis;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones.CreaSeguimientoFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones.RegistroCrisisFragment;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class CrisisFragment extends Fragment implements CrisisAdapter.ItemClickListener {
    private MenuHost menuHost;
    private RecyclerView recycler;
    private ClaseGlobal claseGlobal;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private PeticionesJson peticionesJson;
    private CrisisAdapter adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crisis, container, false);
        menuHost = requireActivity();
        inicializaVariables(view);
        inicializaViewModel();
        cambiarToolbar();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        obtieneCrisis();
    }

    public void inicializaVariables(View view){
        claseGlobal = ClaseGlobal.getInstance();
        recycler = view.findViewById(R.id.RVCrisisAlumno);
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
                menuInflater.inflate(R.menu.app_bar_menu_seguimiento, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_añadir){
                    new RegistroCrisisFragment().show(getChildFragmentManager(), RegistroCrisisFragment.TAG);
                }
                if(menuItem.getItemId() == R.id.action_fechas){
                    //despliegaCalendarioYFiltra();
                }
                return false;
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    public void obtieneCrisis(){
        recycler.setHasFixedSize(true);
        sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
            @Override
            public void onChanged(Alumnos alumnos) {
                sharedAlumnosViewModel.getListaCrisis(alumnos).observe(getViewLifecycleOwner(), new Observer<ArrayList<Crisis>>() {
                    @Override
                    public void onChanged(ArrayList<Crisis> crisisArrayList) {
                        poblaRVCrisis(crisisArrayList);
                    }
                });

            }
        });
    }

    public void poblaRVCrisis(ArrayList<Crisis> crisisArrayList){
        adapter = new CrisisAdapter(crisisArrayList, requireContext(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());

        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);

        recycler.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(int position) {
        sharedAlumnosViewModel.setCrisis(position);
    }
}