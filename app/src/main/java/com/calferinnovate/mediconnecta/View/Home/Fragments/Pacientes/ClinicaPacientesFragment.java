package com.calferinnovate.mediconnecta.View.Home.Fragments.Pacientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.Adaptadores.InformesAdapter;
import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.Informes;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

/**
 * Fragmento encargado de mostrar la historía clínica, tratamiento e informes de los pacientes.
 */

public class ClinicaPacientesFragment extends Fragment implements IOnBackPressed {

    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    private ArrayList<Informes> informesPaciente;
    private TableLayout tlInformes;
    private InformesAdapter informesAdapter;
    private TextInputEditText datosSalud;
    private TextInputEditText tratamiento;

    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_clinica_pacientes.xml.
     * Define el título del fragmento en la Toolbar,
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
        View view = inflater.inflate(R.layout.fragment_clinica_pacientes, container, false);
        inicializaVariables(view);
        inicializaViewModel();
        getActivity().setTitle("Datos Clínicos");
        return view;
    }

    /**
     * Método encargado de inicializar varibles y enlazar recursos de la UI.
     * @param view La vista inflada.
     */
    public void inicializaVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        tlInformes = view.findViewById(R.id.tableInformes);
        datosSalud = view.findViewById(R.id.datosSalud);
        tratamiento = view.findViewById(R.id.tratamiento);
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
     * Llama al método getPaciente() del ViewModel para obtener el paciente que fue seleccionado y
     * obtiene informes e historia clínica de este.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), pacientes -> {
            obtieneInformesDelPaciente(pacientes);
            obtieneHistoriaClinicaDelPaciente(pacientes);
        });


    }


    /**
     * Método encargado de obtener la lista de informes del paciente, llama al método getListaMutablePacientes()
     * del ViewModel para obtener dicha lista y llama a creaLaTabla para actualizar el TableLayout.
     * @param paciente
     */
    public void obtieneInformesDelPaciente(Pacientes paciente) {
        sharedPacientesViewModel.getListaMutableInformes(paciente).observe(getViewLifecycleOwner(), informes -> {
            informesPaciente = informes;
            tlInformes.removeAllViews();
            creaLaTabla();
            informesAdapter.notifyDataSetChanged();

        });
    }

    /**
     * Método que se encarga de poblar dinámicamente las filas del TableLayout tlInformes, configura el click
     * del adaptador InformesAdapter para llamar al fragmento pdfViewerFragment cuando se clickee en
     * algunos de los ImageButtons en cierta posición.
     *
     * Crea un nuevo fragmento PDFViewerFragment, pasa los bytes del PDF como argumentos al fragmento y
     * reemplaza el fragmento actual con el fragmento PDFViewerFragment.
     *
     * Verifica que infomesPaciente no sea nulo antes de iterar e infla el header el header del TableLayout,
     * a continuación va inflando las filas dinámicamente.
     */
    public void creaLaTabla() {
        informesAdapter = new InformesAdapter(getContext(), informesPaciente, position -> {
            Informes informe = informesPaciente.get(position);

            PdfViewerFragment pdfViewerFragment = new PdfViewerFragment();
            byte[] pdf = informe.getPdfBytes();

            Bundle bundle = new Bundle();
            bundle.putByteArray("pdfInforme", pdf);
            pdfViewerFragment.setArguments(bundle);

            FragmentTransaction transaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, pdfViewerFragment);
            transaction.setReorderingAllowed(true);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        if (informesPaciente != null) {
            View headerView = informesAdapter.inflaElHeader(tlInformes);
            tlInformes.addView(headerView);
            for (Informes informe : informesPaciente) {
                View rowView = informesAdapter.getView(informesAdapter.getPosition(informe), null, tlInformes);
                tlInformes.addView(rowView);
            }
        }
        informesAdapter.notifyDataSetChanged();
    }

    /**
     * Método que llama al método obtieneHistoriaClinicaPaciente del SharedPacientesViewModel y actualiza la
     * UI con los datos de Salud y Tratamiento.
     * @param paciente
     */
    public void obtieneHistoriaClinicaDelPaciente(Pacientes paciente){
        sharedPacientesViewModel.obtieneHistoriaClinica(paciente).observe(getViewLifecycleOwner(), historiaClinica -> {
            datosSalud.setText(historiaClinica.getDatosSalud());
            tratamiento.setText(historiaClinica.getTratamiento());
        });
    }

    /**
     * Método que agrega la lógica específica del fragmento para manejar el restroceso.
     * Al presionar back volvería al HomeFragment.
     * @return true si el fragmento manejar el retroceso, false en caso contrario.
     */
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}



