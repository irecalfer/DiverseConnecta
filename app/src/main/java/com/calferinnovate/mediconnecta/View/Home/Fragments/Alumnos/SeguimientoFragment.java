package com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

import com.calferinnovate.mediconnecta.Adaptadores.SeguimientoAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguimiento;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones.CreaSeguimientoFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Ediciones.EditaSeguimientoDialogFragment;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class SeguimientoFragment extends Fragment implements SeguimientoAdapter.ItemClickListener, EditaSeguimientoDialogFragment.OnSeguimientoUpdatedListener {

    private RecyclerView recycler;
    private ClaseGlobal claseGlobal;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private PeticionesJson peticionesJson;
    private SeguimientoAdapter adapter;
    private MenuHost menuHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_seguimiento, container, false);
        getActivity().setTitle("Seguimiento");
        menuHost = requireActivity();
        inicializaVariables(view);
        inicializaViewModel();
        cambiarToolbar();
        obtieneSeguimientos();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       sharedAlumnosViewModel.getSeguimientoUpdated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
           @Override
           public void onChanged(Boolean actualizado) {
               if(actualizado){
                   obtieneSeguimientos();
                   FragmentManager fragmentManager = getChildFragmentManager();
                   DialogFragment editaSeguimientoDialog = (DialogFragment) fragmentManager.findFragmentByTag(EditaSeguimientoDialogFragment.TAG);
                   DialogFragment opcionesSeguimientoDialog = (DialogFragment) fragmentManager.findFragmentByTag(OpcionesSeguimientoDialogFragment.TAG);
                   if (editaSeguimientoDialog != null) {
                       editaSeguimientoDialog.dismiss();
                   }
                   if (opcionesSeguimientoDialog != null) {
                       opcionesSeguimientoDialog.dismiss();
                   }
               }
           }
       });
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

    public void cambiarToolbar(){
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.app_bar_menu_anadir, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_añadir){
                    new CreaSeguimientoFragment().show(getChildFragmentManager(), CreaSeguimientoFragment.TAG);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
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
        sharedAlumnosViewModel.setSeguimiento(position);
        new OpcionesSeguimientoDialogFragment().show(getChildFragmentManager(), OpcionesSeguimientoDialogFragment.TAG);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSeguimientoUpdated() {
        // Aquí cierras ambos DialogFragments
        FragmentManager fragmentManager = getChildFragmentManager();
        DialogFragment editaSeguimientoDialog = (DialogFragment) fragmentManager.findFragmentByTag(EditaSeguimientoDialogFragment.TAG);
        DialogFragment opcionesSeguimientoDialog = (DialogFragment) fragmentManager.findFragmentByTag(OpcionesSeguimientoDialogFragment.TAG);

        if (editaSeguimientoDialog != null) {
            editaSeguimientoDialog.dismiss();
        }
        if (opcionesSeguimientoDialog != null) {
            opcionesSeguimientoDialog.dismiss();
        }
    }
}