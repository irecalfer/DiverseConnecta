package com.calferinnovate.mediconnecta.Home.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Adaptadores.NormasAdapter;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Interfaces.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.Normas;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.ViewModel.NormasViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class NormasEmpresaFragment extends Fragment implements IOnBackPressed {

    private ClaseGlobal claseGlobal;
    private ViewModelArgs viewModelArgs;
    private PeticionesJson peticionesJson;
    private NormasAdapter normasAdapter;
    private NormasViewModel normasViewModel;
    private RecyclerView recyclerNormas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_normas_empresa, container, false);
        claseGlobal = ClaseGlobal.getInstance();

        recyclerNormas = view.findViewById(R.id.recyclerViewNormas);

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

        ViewModelFactory<NormasViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        normasViewModel = new ViewModelProvider(requireActivity(), factory).get(NormasViewModel.class);

        //Hacemos la petición para obtener las normas
        normasViewModel.obtieneNormasEmpresa();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        normasViewModel.getArrayListLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Normas>>() {
            @Override
            public void onChanged(ArrayList<Normas> normas) {
                normasAdapter = new NormasAdapter(normas);

                ((SimpleItemAnimator) recyclerNormas.getItemAnimator()).setSupportsChangeAnimations(false);

                recyclerNormas.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerNormas.setAdapter(normasAdapter);
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        return true;
    }
}