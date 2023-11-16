package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.Adaptadores.PacientesGeneralAdapter;
import com.calferinnovate.mediconnecta.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.Interfaces.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Seguro;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

public class GeneralPacientesFragment extends Fragment implements IOnBackPressed {


    private TextView seguroTextView;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private ViewModelArgs viewModelArgs;
    private PeticionesJson peticionesJson;
    private String nombreSeguro;
    // Otras variables miembro
    //La variable pacienteActual se utiliza para almacenar el paciente actual que proviene del ViewModel
    // y segurosCargados es una variable booleana que se establece en true cuando la lista
    // de seguros se ha cargado correctamente. Estas variables ayudan a determinar cuándo se pueden actualizar
    // los datos en la interfaz de usuario, en el método updateUI.
    private Pacientes pacienteActual;


    public GeneralPacientesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_pacientes, container, false);
        asignarValoresAVariables(view);

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



    public void asignarValoresAVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        seguroTextView = view.findViewById(R.id.seguroPaciente);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rellenaUIAdaptador(view);
        obtenerYSetearSeguro();
    }


    public void rellenaUIAdaptador(View view) {
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                Log.d("Paciente", pacientes.getNombre());
                pacienteActual = pacientes;
                // Utiliza la clase adaptadora para poblar los datos en la interfaz de usuario
                PacientesGeneralAdapter pacienteAdapter = new PacientesGeneralAdapter(pacienteActual, requireContext());
                pacienteAdapter.rellenaUI(view);
                // Llama al ViewModel para obtener el seguro
                sharedPacientesViewModel.obtieneSeguroPacientes(pacienteActual);

            }
        });
    }

    public void obtenerYSetearSeguro() {
        sharedPacientesViewModel.getSeguro().observe(getViewLifecycleOwner(), new Observer<Seguro>() {
            @Override
            public void onChanged(Seguro seguro) {
                if (seguro != null) {
                    seguroTextView.setText(seguro.getNombreSeguro() + " " + seguro.getTelefono());
                } else {
                    seguroTextView.setText("Sin seguro");
                }
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}