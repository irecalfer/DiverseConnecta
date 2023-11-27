package com.calferinnovate.mediconnecta.View.Home.Fragments.Pacientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.Adaptadores.PacientesGeneralAdapter;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.text.MessageFormat;

/**
 * Fragmento encargado de mostrar los datos personales del paciente.
 */
public class GeneralPacientesFragment extends Fragment implements IOnBackPressed {


    private TextView seguroTextView;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    // Otras variables miembro
    //La variable pacienteActual se utiliza para almacenar el paciente actual que proviene del ViewModel
    // y segurosCargados es una variable booleana que se establece en true cuando la lista
    // de seguros se ha cargado correctamente. Estas variables ayudan a determinar cuándo se pueden actualizar
    // los datos en la interfaz de usuario, en el método updateUI.
    private Pacientes pacienteActual;


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
        View view = inflater.inflate(R.layout.fragment_general_pacientes, container, false);
        inicializaVariables(view);
        inicializaViewModel();
        return view;
    }


    /**
     * Método encargado de inicializar variables y enlazar los recursos de la UI.
     *
     * @param view La vista inflada.
     */
    public void inicializaVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        seguroTextView = view.findViewById(R.id.seguroPaciente);
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
     * Llama a obtenerYSetearSeguro() para comprobar si el paciente tiene seguro privado.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rellenaUIAdaptador(view);
        SetearSeguro();
    }

    /**
     * Método encargado de actualizar la UI con los datos del paciente. Llama a getPaciente en SharedPacientesViewModel
     * para obtener el paciente que fue seleccionado y actualiza la vista con sus datos.
     *
     * @param view La vista inflada.
     */
    public void rellenaUIAdaptador(View view) {
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), pacientes -> {
            pacienteActual = pacientes;

            PacientesGeneralAdapter pacienteAdapter = new PacientesGeneralAdapter(pacienteActual, requireContext());
            pacienteAdapter.rellenaUI(view);

        });
    }

    /**
     * Llama al método getSeguro() del ViewModel y actualiza la UI con el seguro del paciente si lo tiene,
     * en caso de que no tenga seguro indica que no lo tiene.
     */
    public void SetearSeguro() {
        sharedPacientesViewModel.getSeguro().observe(getViewLifecycleOwner(), seguro -> {
            if (seguro != null) {
                seguroTextView.setText(MessageFormat.format("{0} {1}", seguro.getNombreSeguro(), seguro.getTelefono()));
            } else {
                seguroTextView.setText(R.string.sinSeguro);
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