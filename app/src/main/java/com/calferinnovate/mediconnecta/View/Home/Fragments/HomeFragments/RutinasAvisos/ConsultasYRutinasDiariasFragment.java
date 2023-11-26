package com.calferinnovate.mediconnecta.View.Home.Fragments.HomeFragments.RutinasAvisos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Adaptadores.RutinasAdapter;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.HomeFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.ConsultasYRutinasDiariasViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Fragment que muestra la lista de pacientes que tienen una rutina en la fecha dada.
 * Utiliza un RecyclerView para mostrar la información y un TabLayout para cambiar entre los tipos de rutinas.
 */
public class ConsultasYRutinasDiariasFragment extends Fragment implements IOnBackPressed {

    private RecyclerView rvConsultas;
    private RutinasAdapter rutinasAdapter;
    private TabLayout tabLayout;
    private Unidades unidades;
    private ClaseGlobal claseGlobal;
    private ArrayList<Pacientes> listaPacientes;
    private EditText fechaRutina;
    private ConsultasYRutinasDiariasViewModel consultasYRutinasDiariasViewModel;
    private String tipoRutinaActual;
    private PeticionesJson peticionesJson;
    private String fecha;


    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_consultas_y_rutinas_diarias.xml.
     * Cambia el título de la Toolbar a Consultas y Rutinas.
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
        View vista = inflater.inflate(R.layout.fragment_consultas_y_rutinas_diarias, container, false);

        inicializaVariables(vista);
        getActivity().setTitle("Consultas y rutinas");
        inicializaViewModel();
        obtieneFechaYFormatea();

        return vista;
    }

    /**
     * Método que inicializa las variables miembro y enlaza los recursos de la UI con dichas variables.
     *
     * @param view La vista inflada.
     */
    public void inicializaVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        unidades = claseGlobal.getUnidades();
        listaPacientes = claseGlobal.getListaPacientes();

        rvConsultas = view.findViewById(R.id.rvRutinas);
        tabLayout = view.findViewById(R.id.tabLayoutRutinas);
        fechaRutina = view.findViewById(R.id.fechaActual);
    }


    /**
     * Método que configura el ViewModel ConsultasYRutinasViewModel mediante la creación de un ViewModelFactory
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

        ViewModelFactory<ConsultasYRutinasDiariasViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        consultasYRutinasDiariasViewModel = new ViewModelProvider(this, factory).get(ConsultasYRutinasDiariasViewModel.class);
    }


    /**
     * Obtiene la fecha de los argumentos del fragmento y llama a setearFecha si la fecha es válida.
     */
    public void obtieneFechaYFormatea() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            fecha = bundle.getString("fecha");
            if (fecha != null && !fecha.isEmpty()) {
                setearFecha(fecha);
            }
        }
    }

    /**
     * Método que formatea la fecha del EditText a dd-MM-yyy
     *
     * @param fecha obtenida del HomeFragment que necesita ser formateada.
     */
    private void setearFecha(String fecha) {
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate fechaEntrada = LocalDate.parse(fecha, formatoEntrada);

        String fechaFormateada = fechaEntrada.format(formatoSalida);
        fechaRutina.setText(fechaFormateada);
    }


    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Llama a listenerTabs() que establece la escucha de los tabs del TabLayout.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listenerTabs();


    }

    /**
     * Establece un listener en el TabLayout para manejar eventos cuando se seleccionan diferentes tabs.
     * Por defecto obtiene los datos para la primera pestaña que es Visitas.
     * Cuando se selecciona un tab se llama a ObtenerDatosRutinas()
     */
    public void listenerTabs() {
        if (tabLayout.getTabCount() > 0) {
            tipoRutinaActual = (String) tabLayout.getTabAt(0).getText();
            obtenerDatosRutinas();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tipoRutinaActual = (String) tab.getText();
                obtenerDatosRutinas();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No es necesario implementar esto
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No es necesario implementar esto
            }
        });
    }

    /**
     * Obtiene la lista de pacientes pertenecientes a una unidad, fecha dada y tipo de rutina llamando al método
     * obtieneDatosRutinasDiaPacientes del ConsultasYRutinasDiariasViewModel y actualiza el RecyclerView con los datos.
     */
    private void obtenerDatosRutinas() {
        // Realiza la solicitud HTTP utilizando el ViewModel y pasa la fecha, unidad y tipo de rutina
        consultasYRutinasDiariasViewModel.obtieneDatosRutinasDiaPacientes(fecha,
                unidades.getNombreUnidad(), tipoRutinaActual).observe(getViewLifecycleOwner(), pacientesAgrupadosRutinas -> {
            rutinasAdapter = new RutinasAdapter(pacientesAgrupadosRutinas, listaPacientes, getContext());
            rvConsultas.setAdapter(rutinasAdapter);

            rutinasAdapter.notifyDataSetChanged();
        });


    }


    /**
     * Método que agrega la lógica específica del fragmento para manejar el restroceso.
     * Al presionar back volvería al HomeFragment.
     * @return true si el fragmento manejar el retroceso, false en caso contrario.
     */
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        return true;
    }
}