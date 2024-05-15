package com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Adaptadores.AlumnosAdapter;
import com.calferinnovate.mediconnecta.Adaptadores.SeguimientoAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguimiento;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones.CreaSeguimientoFragment;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class SeguimientoFragment extends Fragment implements SeguimientoAdapter.ItemClickListener {

    private RecyclerView recycler;
    private ClaseGlobal claseGlobal;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private PeticionesJson peticionesJson;
    private SeguimientoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_seguimiento, container, false);
        getActivity().setTitle("Seguimiento");

        inicializaVariables(view);
        inicializaViewModel();
        obtieneSeguimientos();

        return view;
    }

    public void inicializaVariables(View view){
        claseGlobal = ClaseGlobal.getInstance();
        recycler = view.findViewById(R.id.RVSeguimientoAlumno);
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

    public void obtieneSeguimientos(){
        recycler.setHasFixedSize(true);

        sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
            @Override
            public void onChanged(Alumnos alumno) {
                sharedAlumnosViewModel.getListaSeguimientos(alumno).observe(getViewLifecycleOwner(), new Observer<ArrayList<Seguimiento>>() {
                    @Override
                    public void onChanged(ArrayList<Seguimiento> seguimientoArrayList) {
                        poblarRecyclerSeguimiento(seguimientoArrayList);
                    }
                });
            }
        });

    }

    public void poblarRecyclerSeguimiento(ArrayList<Seguimiento> seguimientoArrayList){
        adapter = new SeguimientoAdapter(seguimientoArrayList, requireContext(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());

        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);

        recycler.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onClick(int position) {
        //sharedAlumnosViewModel.setSeguimiento(position);
        //adapter.notifyDataSetChanged();
        //CreaSeguimientoFragment dialog = new CreaSeguimientoFragment();
        //dialog.show(getParentFragmentManager(), "CreaSeguimientoDialog");
        //dialog.show(getParentFragmentManager(), "CreaSeguimientoDialog");
        //getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreaSeguimientoFragment()).commit();
        new CreaSeguimientoFragment().show(getChildFragmentManager(), CreaSeguimientoFragment.TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}