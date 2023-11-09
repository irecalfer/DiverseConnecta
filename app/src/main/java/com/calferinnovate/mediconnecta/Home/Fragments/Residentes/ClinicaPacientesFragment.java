package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Informes;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class ClinicaPacientesFragment extends Fragment {

    private ViewModelArgs viewModelArgs;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    private ArrayList<Informes> informesPaciente;
    private TableLayout tlInformes;
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
        // Observación de LiveData: Has configurado la observación de un LiveData en el ViewModel utilizando el método observe(). Cuando los datos cambian en el LiveData,
        // el adaptador se actualiza automáticamente para reflejar los cambios en el RecyclerView.
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                obtieneInformesDelPaciente(pacientes);
            }
        });

        creaLaTabla();
        return view;
    }

    public void llamaAClaseGlobal(){
        claseGlobal = ClaseGlobal.getInstance();
    }

    public void asignaComponentesAVariables(View view){
        tlInformes = view.findViewById(R.id.tableInformes);
    }

    public ArrayList<Informes> obtieneInformesDelPaciente(Pacientes paciente){
        sharedPacientesViewModel.getListaMutableInformes(paciente).observe(getViewLifecycleOwner(), new Observer<ArrayList<Informes>>() {
            @Override
            public void onChanged(ArrayList<Informes> informes) {
                informesPaciente = informes;
            }
        });
        return informesPaciente;
    }
    public void creaLaTabla(){
        for(Informes informe: informesPaciente){
            addRow(tlInformes, "Tipo de Informe", informe.getTipoInforme());
            addRow(tlInformes, "Fecha", informe.getFecha());
            addRow(tlInformes, "Centro", informe.getCentro());
            addRow(tlInformes, "Responsable", informe.getResponsable());
            addRow(tlInformes, "Servicio/Unidad/Dispositivo", informe.getServicioUnidadDispositivo());
            addRow(tlInformes, "Servicio de Salud", informe.getServicioDeSalud());
        }
/*
        //Diseño de la fila
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT);
        //Con ese atributo decimos que la anchura de la fila será con el atributo WRAP_CONTENT.
        fila.setLayoutParams(layoutParams);

        // Agregamos el encabezado
        for (int x = 0; x < 9; x++) { //MOdificarlo para no poner el 9 directamente
            TextView tvEncabezado = new TextView(getActivity());
            tv.setText(tallas[x]);
            fila.addView(tvTalla);
        }
        // Finalmente agregar la fila en la primera posición
        tableLayout.addView(fila, 0);*/
    }

    private void addRow(TableLayout tableLayout, String label, String value) {
        TableRow row = new TableRow(getContext());
        TextView labelView = new TextView(getContext());
        TextView valueView = new TextView(getContext());

        labelView.setText(label);
        valueView.setText(value);

        row.addView(labelView);
        row.addView(valueView);
        tableLayout.addView(row);
    }
}