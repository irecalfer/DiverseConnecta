package com.calferinnovate.mediconnecta.Home.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class ParteGeneralFragment extends Fragment {
    private EditText fechasSeleccionadas;
    private Button btnFechas;
    private ClaseGlobal claseGlobal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parte_general, container, false);
        inicializaVAriables(view);
        instanciaDateRangepPicker();

        return view;
    }

    public void inicializaVAriables(View view){
        claseGlobal = ClaseGlobal.getInstance();
        fechasSeleccionadas = view.findViewById(R.id.fechaSeleccionadaEditText);
        btnFechas = view.findViewById(R.id.buttonFechas);
    }

    public void instanciaDateRangepPicker(){
        //Creamos la instancia del date range picker de Material Design
        MaterialDatePicker.Builder<Pair<Long, Long>> materialFechaBuilder = MaterialDatePicker.Builder.dateRangePicker();

        //Vamos a definir las propiedades del date Picker
        materialFechaBuilder.setTitleText("Selecciona las fechas");

        //Creamos una instancia del material date picker
        final MaterialDatePicker materialDatePicker = materialFechaBuilder.build();

        //Cuando se clicke le botón se abrirá el Date Picker
        btnFechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //con getSupportManager() interactuaremos con los fragmentos asociados al tag del material design picker
                //para obtener cualquier error en el logcat
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        //Ahora manejamos el click del botón del selector de fecha del material design
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                fechasSeleccionadas.setText(materialDatePicker.getHeaderText());
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}