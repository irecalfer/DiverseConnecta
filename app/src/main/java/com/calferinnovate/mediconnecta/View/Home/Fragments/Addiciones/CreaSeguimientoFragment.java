package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CreaSeguimientoFragment extends Fragment {

private SharedAlumnosViewModel sharedAlumnosViewModel;
private PeticionesJson peticionesJson;
private ClaseGlobal claseGlobal;
private TextInputEditText etFecha, etHora, etSeguimiento;
private TextInputLayout tilFecha, tilHora;
private String fechaSeguimientoSeleccionada, horaSeguimientoSeleccionada;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crea_seguimiento, container, false);
        inicializaRecursos(view);
        inicializaViewModel();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        seleccionaFecha();
        seleccionaHora();
    }

    public void inicializaRecursos(View view){
        claseGlobal = ClaseGlobal.getInstance();
        etFecha = view.findViewById(R.id.ETFechaSeguimiento);
        etHora = view.findViewById(R.id.ETHoraSeguimiento);
        etSeguimiento = view.findViewById(R.id.ETSeguimientoAlumno);
        tilFecha = view.findViewById(R.id.TVFecha);
        tilHora = view.findViewById(R.id.TVHora);
    }

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

        ViewModelFactory<SharedAlumnosViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        sharedAlumnosViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedAlumnosViewModel.class);
    }



    public void seleccionaFecha(){
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Selecciona la fecha en la que se registra el seguimiento");

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        tilFecha.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER_SEGUIMIENTO");
                }
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fechaFormateada = simpleDateFormat.format(new Date(selection));
                etFecha.setText(fechaFormateada);
                fechaSeguimientoSeleccionada = fechaFormateada;
            }
        });
    }

    public void seleccionaHora(){
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setTitleText("Seleccione la hora del seguimiento")
                .build();
        tilHora.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePicker.show(requireActivity().getSupportFragmentManager(),"MATERIAL_TIME_PICKER_SEGUIMIENTO");
            }
        });

        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int horaSeleccionada = materialTimePicker.getHour();
                int minutosSeleccionados = materialTimePicker.getMinute();
                horaSeguimientoSeleccionada = String.valueOf(horaSeleccionada)+":"+String.valueOf(minutosSeleccionados);
                etHora.setText(horaSeguimientoSeleccionada);
            }
        });
    }
}