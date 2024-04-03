package com.calferinnovate.mediconnecta.View.Home.Fragments.Pacientes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Adaptadores.FamiliaresAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

/**
 * Fragmento encargado de mostrar a lista de Datos de los contactos de los pacientes.
 */
public class ContactoFamiliaresAlumnoFragment extends Fragment implements IOnBackPressed {
    private ClaseGlobal claseGlobal;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private RecyclerView recyclerView;
    private PeticionesJson peticionesJson;


    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_contacto_familiares_paciente.xml.
     * Configura el RecyclerView.
     *
     * @param inflater inflador utilizado para inflar el diseño de la UI.
     * @param container Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     *
     *
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacto_familiares_paciente, container, false);
        getActivity().setTitle("Contacto Familiares");
        asignarValoresAVariables(view);
        recyclerView.setHasFixedSize(true);
        inicializaViewModel();

        return view;
    }

    /**
     * Método encargado de inicializar variables y enlazar recursos de la UI con variables miembro.
     * @param view
     */
    public void asignarValoresAVariables(View view){
        claseGlobal = ClaseGlobal.getInstance();
        recyclerView = view.findViewById(R.id.recyclerViewFamiliares);
    }

    /**
     * Método que configura el ViewModel SharedPacientesViewModel mediante la creación de un ViewModelFactory
     * que proporciona instancias de Peticiones Json y ClaseGloabl al ViewModel.
     */
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

        ViewModelFactory<SharedPacientesViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        sharedPacientesViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedPacientesViewModel.class);
    }

    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Llama a observaPacienteYObtieneContactos() que establece la escucha de los tabs del TabLayout.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observaPacienteYObtieneContactos();

    }

    /**
     * Método que obtiene el paciente seleccionado y llama a obtieneListaFamiliares para obtener la lista de contactos.
     */
    public void observaPacienteYObtieneContactos(){
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), pacientes -> obtieneListaFamiliares(pacientes));
    }

    /**
     * Método que llama al método obtieneContactoFamiliares del SharedViewModel, obtiene la lista de contactos,
     * configura el adaptador FamiliaresAdapter y actualiza el RecyclerView con la lista obtenida.
     * @param paciente Paciente seleccionado.
     */
    public void obtieneListaFamiliares(Alumnos paciente){
        sharedPacientesViewModel.obtieneContactoFamiliares(paciente).observe(getViewLifecycleOwner(), contactoFamiliares -> {
            FamiliaresAdapter adapter = new FamiliaresAdapter(contactoFamiliares, getContext());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        });


    }


    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}