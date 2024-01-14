package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Habitaciones;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguro;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


/**
 * Fragmento encargado de mostrar los datos personales del paciente.
 */
public class GeneralPacientesFragmentAnadidos extends Fragment implements IOnBackPressed {


    private ImageView fotoPaciente;
    private Button btnGuardar;
    private TextInputEditText nombre, apellidos, nacimiento, dni, cipSns, numSeguridadSocial;
    private Spinner seguroSp, unidadSp, habitacionSp, sexoSp, estadoCivilSp;
    private MaterialDatePicker fechaNacimiento, fechaIngreso;
    private String unidadSeleccionada, sexoSeleccionado, estadoCivilSeleccionado;
    private int habitacionSeleccionada;
    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    private SharedPacientesViewModel sharedPacientesViewModel;


    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_sesion.xml.
     * Llama a inicializaVariables() para obtener la fragment_general_pacientes de ClaseGlobal.
     *
     * @param inflater           inflador utilizado para inflar el diseño de la UI.
     * @param container          Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_pacientes_anadidos, container, false);
        inicializaRecursos(view);
        inicializaViewModel();
        return view;
    }


    /**
     * Método encargado de inicializar variables y enlazar los recursos de la UI.
     *
     * @param view La vista inflada.
     */
    public void inicializaRecursos(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        fotoPaciente = view.findViewById(R.id.fotoPacienteDetalleNuevo);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        nombre = view.findViewById(R.id.nombrePacienteNuevo);
        apellidos = view.findViewById(R.id.apellidoPacienteNuevo);
        nacimiento = view.findViewById(R.id.lugarNacimientoPacienteNuevo);
        dni = view.findViewById(R.id.dniPacienteNuevo);
        cipSns = view.findViewById(R.id.cipSnsPacienteNuevo);
        numSeguridadSocial = view.findViewById(R.id.numSeguridadSocialPacienteNuevo);
        seguroSp = view.findViewById(R.id.seguroPacienteNuevoSpinner);
        unidadSp = view.findViewById(R.id.spinnerUnidadPacienteNuevo);
        habitacionSp = view.findViewById(R.id.spinnerHabitaciónPacienteNuevo);
        sexoSp = view.findViewById(R.id.spinnerSexoPacienteNuevo);
        estadoCivilSp = view.findViewById(R.id.spinnerEstadoCivilPacienteNuevo);
        //fechaNacimiento = view.findViewById(R.id.)
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

        ViewModelFactory<SharedPacientesViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        sharedPacientesViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedPacientesViewModel.class);
    }

    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Llama a rellenaUIAdaptador() que se encarga de actualizar la vista.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        obtenerListasYPoblarSpinners();
        obtenerUnidadesYPoblarSpinners();
        obtieneSexoPacientes();
        obtieneEstadoCivilPaciente();
    }

    private void obtenerListasYPoblarSpinners() {
        sharedPacientesViewModel.obtieneListaSeguros().observe(getViewLifecycleOwner(), new Observer<ArrayList<Seguro>>() {
            @Override
            public void onChanged(ArrayList<Seguro> seguros) {
                ArrayList<String> listaSeguros = new ArrayList<String>();
                for (Seguro seguro : seguros) {
                    String tmp = seguro.getNombreSeguro() + " " + seguro.getTelefono();
                    listaSeguros.add(tmp);
                }
                ArrayAdapter<String> segurosAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaSeguros);
                segurosAdapter.setDropDownViewResource(R.layout.my_spinner);
                seguroSp.setAdapter(segurosAdapter);
            }
        });
    }

    private void obtenerUnidadesYPoblarSpinners() {
        sharedPacientesViewModel.obtieneListaDeUnidades().observe(getViewLifecycleOwner(), new Observer<ArrayList<Unidades>>() {
            @Override
            public void onChanged(ArrayList<Unidades> unidades) {
                ArrayList<String> listaUnidades = new ArrayList<>();
                for (Unidades unidad : unidades) {
                    listaUnidades.add(unidad.getNombreUnidad());
                }
                ArrayAdapter<String> unidadesAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaUnidades);
                unidadesAdapter.setDropDownViewResource(R.layout.my_spinner);
                unidadSp.setAdapter(unidadesAdapter);
                seleccionarUnidad();
            }
        });
    }

    private void seleccionarUnidad() {
        unidadSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinnerUnidadPacienteNuevo) {
                    unidadSeleccionada = parent.getSelectedItem().toString();
                    obtieneHabitacionesYPoblarSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void obtieneHabitacionesYPoblarSpinner() {
        sharedPacientesViewModel.obtieneHabitacionesUnidades(unidadSeleccionada).observe(getViewLifecycleOwner(), new Observer<ArrayList<Habitaciones>>() {
            @Override
            public void onChanged(ArrayList<Habitaciones> habitaciones) {
                ArrayList<Integer> numHabitacionesLista = new ArrayList<Integer>();
                for (Habitaciones habitacion : habitaciones) {
                    numHabitacionesLista.add(habitacion.getNumHabitacion());
                }
                ArrayAdapter<Integer> habitacionesAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, numHabitacionesLista);
                habitacionesAdapter.setDropDownViewResource(R.layout.my_spinner);
                habitacionSp.setAdapter(habitacionesAdapter);
                habitacionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getId() == R.id.spinnerHabitaciónPacienteNuevo) {
                            habitacionSeleccionada = numHabitacionesLista.get(position);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    private void obtieneSexoPacientes() {
        sharedPacientesViewModel.obtieneSexoPacientesEnum().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> listaSexo) {
                ArrayAdapter<String> sexoAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaSexo);
                sexoAdapter.setDropDownViewResource(R.layout.my_spinner);
                sexoSp.setAdapter(sexoAdapter);
                sexoSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sexoSeleccionado = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    private void obtieneEstadoCivilPaciente(){
        sharedPacientesViewModel.obtieneEstadoCivilPacientesEnum().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> listaEstadoCivil) {
                ArrayAdapter<String> estadoCivilAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaEstadoCivil);
                estadoCivilAdapter.setDropDownViewResource(R.layout.my_spinner);
                estadoCivilSp.setAdapter(estadoCivilAdapter);
                estadoCivilSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        estadoCivilSeleccionado = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
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