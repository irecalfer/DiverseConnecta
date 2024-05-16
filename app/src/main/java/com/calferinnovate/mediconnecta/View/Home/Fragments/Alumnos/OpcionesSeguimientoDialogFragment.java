package com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Adaptadores.OpcionesSeguimientoAdapter;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguimiento;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Ediciones.EditaSeguimientoDialogFragment;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.appbar.MaterialToolbar;


public class OpcionesSeguimientoDialogFragment extends DialogFragment {

    private MaterialToolbar toolbarSeguimiento;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private ClaseGlobal claseGlobal;
    public static final String TAG = "OpcionesSeguimientoDialogFragment";

    private PeticionesJson peticionesJson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_opciones_seguimiento_dialog, container, false);
        getActivity().setTitle("Crea Seguimiento");
        inicializaRecursos(view);
        inicializaViewModel();
        setupToolbar();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rellenaUI(view);
    }



    public void setupToolbar(){
        toolbarSeguimiento.inflateMenu(R.menu.app_bar_opciones);
        toolbarSeguimiento.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_borrar) {
                    //dialogBorradoSeguimiento();
                }
                if(item.getItemId() == R.id.action_editar){
                    new EditaSeguimientoDialogFragment().show(getChildFragmentManager(), EditaSeguimientoDialogFragment.TAG);
                }
                return true;
            }
        });
    }

    public void inicializaRecursos(View view){
        claseGlobal = ClaseGlobal.getInstance();

        toolbarSeguimiento = view.findViewById(R.id.toolbarEditaSeguimiento);
    }

    public void inicializaViewModel(){
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

    public void rellenaUI(View view){
        sharedAlumnosViewModel.getSeguimiento().observe(getViewLifecycleOwner(), new Observer<Seguimiento>() {
            @Override
            public void onChanged(Seguimiento seguimiento) {
                OpcionesSeguimientoAdapter opcionesSeguimientoAdapter = new OpcionesSeguimientoAdapter(seguimiento, requireContext());
                opcionesSeguimientoAdapter.rellenaUI(view);
            }
        });
    }

}