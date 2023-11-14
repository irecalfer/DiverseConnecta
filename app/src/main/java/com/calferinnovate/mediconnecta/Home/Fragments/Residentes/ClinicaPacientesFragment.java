package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.Adaptadores.InformesAdapter;
import com.calferinnovate.mediconnecta.Home.Fragments.HomeFragment;
import com.calferinnovate.mediconnecta.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.HistoriaClinica;
import com.calferinnovate.mediconnecta.clases.IOnBackPressed;
import com.calferinnovate.mediconnecta.clases.Informes;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class ClinicaPacientesFragment extends Fragment implements IOnBackPressed {

    private ViewModelArgs viewModelArgs;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    private ArrayList<Informes> informesPaciente;
    private TableLayout tlInformes;
    private InformesAdapter informesAdapter;
    private TextInputEditText datosSalud;
    private TextInputEditText tratamiento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clinica_pacientes, container, false);
        llamaAClaseGlobal();
        asignaComponentesAVariables(view);

        //Creas un objeto ViewModelFactory y obtienes una instancia de ConsultasYRutinasDiariasViewModel utilizando este factory.
        //Luego, observas el LiveData del ViewModel para mantener actualizada la lista de programación en el RecyclerView.
        viewModelArgs = new ViewModelArgs() {
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



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observación de LiveData: Has configurado la observación de un LiveData en el ViewModel utilizando el método observe(). Cuando los datos cambian en el LiveData,
        // el adaptador se actualiza automáticamente para reflejar los cambios en el RecyclerView.
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                Pacientes nuevoPaciente = pacientes;
                obtieneInformesDelPaciente(nuevoPaciente);
                obtieneHistoriaClinicaDelPaciente(nuevoPaciente);
            }
        });


    }

    public void llamaAClaseGlobal() {
        claseGlobal = ClaseGlobal.getInstance();
    }

    public void asignaComponentesAVariables(View view) {
        tlInformes = view.findViewById(R.id.tableInformes);
        datosSalud = view.findViewById(R.id.datosSalud);
        tratamiento = view.findViewById(R.id.tratamiento);

    }

    public ArrayList<Informes> obtieneInformesDelPaciente(Pacientes paciente) {
        sharedPacientesViewModel.getListaMutableInformes(paciente).observe(getViewLifecycleOwner(), new Observer<ArrayList<Informes>>() {
            @Override
            public void onChanged(ArrayList<Informes> informes) {
                informesPaciente = informes;
                // Limpia las vistas existentes en la tabla
                tlInformes.removeAllViews();
                creaLaTabla(); // Mover la llamada a creaLaTabla aquí para asegurarse de que se llame después de obtener los informes
                informesAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado

            }
        });
        return informesPaciente;
    }

    public void creaLaTabla() {
        informesAdapter = new InformesAdapter(getContext(), informesPaciente, new InformesAdapter.ItemClickListener() {
            @Override
            public void onClick(int position) {
                // Se ha hecho clic en el botón PDF en la posición "position"
                Informes informe = informesPaciente.get(position);

                // Crear un nuevo fragmento PdfViewerFragment
                PdfViewerFragment pdfViewerFragment = new PdfViewerFragment();
                byte[] pdf = informe.getPdfBytes();

                // Pasar los bytes del PDF como argumentos al fragmento
                Bundle bundle = new Bundle();
                bundle.putByteArray("pdfInforme", pdf);
                pdfViewerFragment.setArguments(bundle);

                // Reemplazar el fragmento actual con PdfViewerFragment
                FragmentTransaction transaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, pdfViewerFragment);
                transaction.addToBackStack(null);  // Opcional: Agregar a la pila de retroceso para poder volver al fragmento anterior
                transaction.commit();
            }
        });

        // Verificar que informesPaciente no sea nulo antes de intentar iterar
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

    public void obtieneHistoriaClinicaDelPaciente(Pacientes paciente){
        sharedPacientesViewModel.obtieneHistoriaClinica(paciente).observe(getViewLifecycleOwner(), new Observer<HistoriaClinica>() {
            @Override
            public void onChanged(HistoriaClinica historiaClinica) {
                datosSalud.setText(historiaClinica.getDatosSalud());
                tratamiento.setText(historiaClinica.getTratamiento());
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        // Agrega la lógica específica del fragmento para manejar el retroceso.
        // Devuelve true si el fragmento maneja el retroceso, de lo contrario, devuelve false.
        // Por ejemplo, si deseas que al presionar Atrás en este fragmento vuelva a la pantalla principal:
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}



