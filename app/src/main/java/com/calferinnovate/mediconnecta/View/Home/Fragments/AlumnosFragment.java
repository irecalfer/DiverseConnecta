package com.calferinnovate.mediconnecta.View.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Adaptadores.AlumnosAdapter;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones.GeneralAlumnosFragmentAnadidos;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos.DetalleAlumnoFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;

/**
 * PacientesFragment es un fragmento que muestra la lista de pacientes pertenecientes a una unidad.
 */
public class AlumnosFragment extends Fragment implements AlumnosAdapter.ItemClickListener, IOnBackPressed {
    private RecyclerView recycler;
    private SearchView searchView;
    private ClaseGlobal claseGlobal;
    private ArrayList<Alumnos> listaPacientes;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private PeticionesJson peticionesJson;
    private AlumnosAdapter adapter;
    private MenuHost menuHost;


    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_pacientes.xml.
     * Implemente la escucha del a
     *
     * @param inflater           inflador utilizado para inflar el diseño de la UI.
     * @param container          Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pacientes, container, false);
        menuHost = requireActivity();
        cambiarToolbar();
        getActivity().setTitle("Alumnos");

        inicializaVariables(view);
        inicializaViewModel();
        poblaRecyclerPacientes();
        actualizaListaPacientes();


        //Implementamos la escucha al item
        adapter.setOnClickListener(this);

        return view;
    }

    public void cambiarToolbar(){
       MenuProvider menuProvider = new MenuProvider() {
           @Override
           public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
               menuInflater.inflate(R.menu.app_bar_usuarios, menu);
           }

           @Override
           public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
               if(menuItem.getItemId() == R.id.action_añadir_usuario){
                   getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new GeneralAlumnosFragmentAnadidos()).commit();
                   return true;
               }
               return false;
           }
       };

       requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

    }

    /**
     * Método que inicializa las variables miembro y enlaza los componentes de la UI con las variables.
     *
     * @param view La vista inflada.
     */
    public void inicializaVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        listaPacientes = claseGlobal.getListaAlumnos();
        recycler = view.findViewById(R.id.recyclerViewPacientes);
        searchView = view.findViewById(R.id.searchPacientes);
    }

    /**
     * Método que configura el ViewModel SharesPacientesViewModel mediante la creación de un ViewModelFactory
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

        ViewModelFactory<SharedAlumnosViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        sharedAlumnosViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedAlumnosViewModel.class);
    }


    /**
     * Método que configura el Recycler View, pasa la lista de pacientes al adaptador, lo configura y
     * implementa un gridlayout con dos columnas.
     */
    public void poblaRecyclerPacientes() {
        recycler.setHasFixedSize(true);

        adapter = new AlumnosAdapter(listaPacientes, getContext(), this);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    /**
     * Método que llama al método getPacientesList del ViewModel y actualiza el adaptador del RecyclerView.
     */
    public void actualizaListaPacientes() {
        sharedAlumnosViewModel.getPacientesList().observe(getViewLifecycleOwner(), pacientes -> {
            adapter.notifyDataSetChanged();
        });
    }


    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Llama a listenerSearchView() que establece la escucha del SearchView.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listenerSearchView();
    }


    /**
     * Implementa la lógica para filtrar la lista de pacientes.
     */
    public void listenerSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filtrado(newText);
                return false;
            }
        });
    }

    /**
     * Maneja el click de un elemento de la lista de pacientes, setea el paciente seleccionado y navega
     * al detalle del paciente.
     *
     * @param position
     */
    @Override
    public void onClick(int position) {
        sharedAlumnosViewModel.setPaciente(position);
        adapter.notifyDataSetChanged();
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetalleAlumnoFragment()).commit();

    }

    /**
     * Método que agrega la lógica específica del fragmento para manejar el restroceso.
     * Al presionar back volvería al HomeFragment.
     *
     * @return true si el fragmento manejar el retroceso, false en caso contrario.
     */
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        return true;
    }

}