package com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
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

import com.calferinnovate.mediconnecta.Adaptadores.CrisisAdapter;
import com.calferinnovate.mediconnecta.Adaptadores.SeguimientoAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Crisis;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones.CreaSeguimientoFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones.RegistroCrisisFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Ediciones.EditaCrisisFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Ediciones.EditaSeguimientoDialogFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.VisualizacionOpciones.OpcionesCrisisFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.VisualizacionOpciones.OpcionesSeguimientoDialogFragment;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CrisisFragment extends Fragment implements CrisisAdapter.ItemClickListener, EditaCrisisFragment.OnCrisisUpdatedListener {
    private MenuHost menuHost;
    private RecyclerView recycler;
    private ClaseGlobal claseGlobal;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private PeticionesJson peticionesJson;
    private CrisisAdapter adapter;
    private String fechaFin, fechaInicio;



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
        getActivity().setTitle("Diario de crisis");
        inicializaViewModel();
        cambiarToolbar();
        obtieneCrisis();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedAlumnosViewModel.getCrisisUpdate().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean update) {
                if(update){
                    obtieneCrisis();
                    FragmentManager fragmentManager = getChildFragmentManager();
                    DialogFragment editaCrisisDialog = (DialogFragment) fragmentManager.findFragmentByTag(EditaCrisisFragment.TAG);
                    DialogFragment opcionesCrisisDialog = (DialogFragment) fragmentManager.findFragmentByTag(OpcionesCrisisFragment.TAG);
                    if (editaCrisisDialog != null) {
                        editaCrisisDialog.dismiss();
                    }
                    if (opcionesCrisisDialog != null) {
                        opcionesCrisisDialog.dismiss();
                    }
                }
            }
        });
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
                    despliegaCalendarioYFiltra();
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
        new OpcionesCrisisFragment().show(getChildFragmentManager(), OpcionesCrisisFragment.TAG);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCrisisUpdated() {
        FragmentManager fragmentManager = getChildFragmentManager();
        DialogFragment editaCrisisDialog = (DialogFragment) fragmentManager.findFragmentByTag(EditaCrisisFragment.TAG);
        DialogFragment opcionesCrisisDialog = (DialogFragment) fragmentManager.findFragmentByTag(OpcionesCrisisFragment.TAG);

        if (editaCrisisDialog != null) {
            editaCrisisDialog.dismiss();
        }
        if (opcionesCrisisDialog != null) {
            opcionesCrisisDialog.dismiss();
        }
    }

    public void despliegaCalendarioYFiltra(){
        MaterialDatePicker.Builder<Pair<Long, Long>> materialFechaBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialFechaBuilder.setTitleText("Selecciona las fechas");
        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = materialFechaBuilder.build();

        //obtieneFechasSeleccionadas(materialDatePicker);
        materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>) selection -> {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

            Date dateInicio = new Date(selection.first);
            fechaInicio = formato.format(dateInicio);
            Date dateFin = new Date(selection.second);
            fechaFin = formato.format(dateFin);

            filtraRecyclerViewSeguimientos(fechaInicio, fechaFin);
        });
    }

    public void filtraRecyclerViewSeguimientos(String fechaInicio, String fechaFin){
        sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
            @Override
            public void onChanged(Alumnos alumnos) {
                sharedAlumnosViewModel.getListaCrisisFecha(alumnos, fechaInicio, fechaFin).observe(getViewLifecycleOwner(), new Observer<ArrayList<Crisis>>() {
                    @Override
                    public void onChanged(ArrayList<Crisis> crisisArrayList) {
                        poblarRecyclerSeguimientoFechas(crisisArrayList);
                    }
                });
            }
        });
    }

    public void poblarRecyclerSeguimientoFechas(ArrayList<Crisis> crisisArrayList){
        recycler.setHasFixedSize(true);

        adapter = new CrisisAdapter(crisisArrayList, requireContext(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());

        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);

        recycler.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }
}