package com.calferinnovate.mediconnecta.View.Home.Fragments.Pacientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Adaptadores.AnatomicosAdapter;
import com.calferinnovate.mediconnecta.Adaptadores.PautasAdapter;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Pautas;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;

/**
 * Fragmento encargado de mostrar las puautas generales y las rutinas de pañal de los pacientes geriátricos.
 */

public class PautasPacientesGeriatriaFragment extends Fragment implements IOnBackPressed {
    private SharedPacientesViewModel sharedPacientesViewModel;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewAnatomicos;
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;

    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_pautas_pacientes_geriatria.xml.
     * Establece el título del fragmento.
     * Configura el RecyclerView e inicializa el ViewModel.
     *
     * @param inflater           inflador utilizado para inflar el diseño de la UI.
     * @param container          Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pautas_pacientes_geriatria, container, false);

        inicializaVariables();
        getActivity().setTitle("Pautas Geriátricas");

        recyclerView = view.findViewById(R.id.recyclerViewPautas);
        recyclerView.setHasFixedSize(true);
        recyclerViewAnatomicos = view.findViewById(R.id.recyclerViewAnatomicos);
        recyclerViewAnatomicos.setHasFixedSize(true);

        inicializaViewModel();

        return view;
    }

    /**
     * Obtiene instancia de ClaseGlobal.
     */
    public void inicializaVariables() {
        claseGlobal = ClaseGlobal.getInstance();
    }

    /**
     * Método que configura el ViewModel SharedPacientesViewModel mediante la creación de un ViewModelFactory
     * que proporciona instancias de Peticiones Json y ClaseGloabl al ViewModel.
     */
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

        ViewModelFactory<SharedPacientesViewModel> factory = new ViewModelFactory<>(viewModelArgs);

        sharedPacientesViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedPacientesViewModel.class);
    }

    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Obtiene del ViewModel el paciente seleccionado y llama a obtienePautasGeneralesDelPaciente().
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), paciente -> {
            obtienePautasGeneralesDelPaciente(paciente);
        });
    }


    /**
     * Método que llama el método obtienePautasPaciente del ViewModel para obtener la lista de pautas generales,
     * llama a rellenaPautasGenerales para actualizar la UI con las pautas y llama a rellenaPautasPañal
     * para actualiza la UI con la rutina de pañal que tiene el paciente.
     *
     * @param paciente Paciente seleccionado.
     */
    public void obtienePautasGeneralesDelPaciente(Pacientes paciente) {
        sharedPacientesViewModel.obtienePautasPaciente(paciente).observe(getViewLifecycleOwner(), pautas -> {
            rellenaPautasGenerales(pautas);
            rellenaPautasPañal(pautas);
        });

    }

    /**
     * Método encargado de actualizar la UI con las pautas del paciente.
     * Llama a determinaSiHayPautas() para verificar si el paciente tiene asociadas pautas o no.
     * Posteriormente actualiza el RecyclerView.
     *
     * @param pautas Lista de pautas del paciente.
     */
    public void rellenaPautasGenerales(ArrayList<Pautas> pautas) {
        boolean mostrarTextViewNoPautas = determinaSiHayPautas(pautas);
        PautasAdapter pautasAdapter = new PautasAdapter(pautas, getContext(), mostrarTextViewNoPautas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pautasAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        pautasAdapter.notifyDataSetChanged();

    }

    /**
     * Método encargado de actualizar la UI con la rutina de pañal del paciente.
     * Llama a determinaSiTieneAnatomicos() para verificar si el paciente tiene asociadas rutina de pañal.
     * Posteriormente actualiza el RecyclerView.
     *
     * @param pautas Pautas de pañal del paciente.
     */
    public void rellenaPautasPañal(ArrayList<Pautas> pautas) {
        boolean mostrarEditTextNoAnatomicos = determinaSiTieneAnatomicos(pautas);
        AnatomicosAdapter anatomicosAdapter = new AnatomicosAdapter(pautas, getContext(), mostrarEditTextNoAnatomicos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAnatomicos.setLayoutManager(linearLayoutManager);
        recyclerViewAnatomicos.setAdapter(anatomicosAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        anatomicosAdapter.notifyDataSetChanged();

    }

    /**
     * Método encargado de verificar si el paciente tiene Pautas.
     *
     * @param pautas Lista de Pautas.
     * @return true si la lista de pautas está vacía, false en caso contrario-
     */
    private boolean determinaSiHayPautas(ArrayList<Pautas> pautas) {
        return pautas.isEmpty();
    }

    /**
     * Método encargado de verificar si el paciente tiene rutina de pañal.
     *
     * @param pautas Lista de Pautas.
     * @return true si la lista de pautas está vacía, false en caso contrario.
     */
    private boolean determinaSiTieneAnatomicos(ArrayList<Pautas> pautas) {
        return pautas.isEmpty();
    }

    /**
     * Método que agrega la lógica específica del fragmento para manejar el restroceso.
     * Al presionar back volvería al PacientesFragment.
     *
     * @return true si el fragmento manejar el retroceso, false en caso contrario.
     */
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}