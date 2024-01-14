package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguro;
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
    private Spinner seguro, unidad, habitacion, sexo;
    private MaterialDatePicker fechaNacimiento, fechaIngreso;
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
        seguro = view.findViewById(R.id.seguroPacienteNuevoSpinner);
        unidad = view.findViewById(R.id.spinnerUnidadPacienteNuevo);
        habitacion = view.findViewById(R.id.spinnerHabitaciónPacienteNuevo);
        sexo = view.findViewById(R.id.spinnerSexoPacienteNuevo);
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
    }

    private void obtenerListasYPoblarSpinners() {
        sharedPacientesViewModel.obtieneListaSeguros().observe(getViewLifecycleOwner(), new Observer<ArrayList<Seguro>>() {
            @Override
            public void onChanged(ArrayList<Seguro> seguros) {
                ArrayList<String> listaSeguros = new ArrayList<String>();
                for(Seguro seguro: seguros){
                    String tmp = seguro.getNombreSeguro() + " " + seguro.getTelefono();
                    listaSeguros.add(tmp);
                }
                ArrayAdapter<String> segurosAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaSeguros);
                segurosAdapter.setDropDownViewResource(R.layout.my_spinner);
                seguro.setAdapter(segurosAdapter);
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