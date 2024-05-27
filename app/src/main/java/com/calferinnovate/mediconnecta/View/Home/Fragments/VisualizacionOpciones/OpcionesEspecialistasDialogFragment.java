package com.calferinnovate.mediconnecta.View.Home.Fragments.VisualizacionOpciones;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Adaptadores.Opciones.OpcionesEspecialistaAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Especialista;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Ediciones.EditaCrisisFragment;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.appbar.MaterialToolbar;


public class OpcionesEspecialistasDialogFragment extends DialogFragment {

    public static final String TAG = "OpcionesEspecialistasDialogFragment";
    private MaterialToolbar toolbarOpcionesEspecialistas;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.70);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.35);
        //THIS WILL MAKE WIDTH 90% OF SCREEN
        //HEIGHT WILL BE WRAP_CONTENT
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_opciones_especialistas_dialog, container, false);
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

    public void inicializaRecursos(View view){
        claseGlobal = ClaseGlobal.getInstance();
        toolbarOpcionesEspecialistas = view.findViewById(R.id.materialToolbarOpcionesEspecialista);
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

    public void setupToolbar(){
        toolbarOpcionesEspecialistas.inflateMenu(R.menu.app_bar_opciones);
        toolbarOpcionesEspecialistas.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_borrar) {
                    //dialogBorradoEspecialista();
                }
                if(item.getItemId() == R.id.action_editar){
                    //new EditaCrisisFragment().show(getChildFragmentManager(), EditaCrisisFragment.TAG);
                }
                return true;
            }
        });
    }

    public void rellenaUI(View view){
        sharedAlumnosViewModel.getEspecialista().observe(getViewLifecycleOwner(), new Observer<Especialista>() {
            @Override
            public void onChanged(Especialista especialista) {
                OpcionesEspecialistaAdapter opcionesEspecialistaAdapter = new OpcionesEspecialistaAdapter(especialista, requireContext());
                opcionesEspecialistaAdapter.rellenaUI(view);
            }
        });
    }
}