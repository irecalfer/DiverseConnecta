package com.calferinnovate.mediconnecta.Home.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.Adaptadores.CaidasAdapter;
import com.calferinnovate.mediconnecta.Adaptadores.ParteAdapter;
import com.calferinnovate.mediconnecta.Interfaces.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.Caidas;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Parte;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.ParteGeneralViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParteGeneralFragment extends Fragment implements IOnBackPressed {
    private EditText fechasSeleccionadas;
    private Button btnFechas;
    private ClaseGlobal claseGlobal;
    private ViewModelArgs viewModelArgs;
    private ParteGeneralViewModel parteGeneralViewModel;
    private PeticionesJson peticionesJson;
    private String fechaInicio, fechaFin;
    private ParteAdapter parteAdapter;
    private CaidasAdapter caidasAdapter;
    private TableLayout tableLayoutPartes, tableLayoutCaidas;
    private ArrayList<Parte> listaPartes;
    private ArrayList<Caidas> listaCaidas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parte_general, container, false);
        claseGlobal = ClaseGlobal.getInstance();
        inicializaVAriables(view);
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

        ViewModelFactory<ParteGeneralViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        parteGeneralViewModel = new ViewModelProvider(requireActivity(), factory).get(ParteGeneralViewModel.class);

        return view;
    }

    public void inicializaVAriables(View view) {
        fechasSeleccionadas = view.findViewById(R.id.fechaSeleccionadaEditText);
        btnFechas = view.findViewById(R.id.buttonFechas);
        tableLayoutPartes = view.findViewById(R.id.tableLayoutParte);
        tableLayoutCaidas = view.findViewById(R.id.tableLayoutCaidas);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instanciaDateRangepPicker();

    }

    public void instanciaDateRangepPicker() {
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
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

                fechasSeleccionadas.setText(materialDatePicker.getHeaderText());

                Date dateInicio = new Date(selection.first);
                fechaInicio = formato.format(dateInicio);
                Date dateFin = new Date(selection.second);
                fechaFin = formato.format(dateFin);

                rellenaPartes();
                rellenaCaidas();
            }
        });

    }

    public void rellenaPartes() {
        parteGeneralViewModel.getListaPartes(fechaInicio, fechaFin).observe(getViewLifecycleOwner(), new Observer<ArrayList<Parte>>() {
            @Override
            public void onChanged(ArrayList<Parte> partes) {
                listaPartes = partes;
                parteAdapter = new ParteAdapter(requireContext(), listaPartes);
                tableLayoutPartes.removeAllViews();
                creaTablaPartes();
                parteAdapter.notifyDataSetChanged();
            }
        });
    }

    public void creaTablaPartes() {

        //Inflamos el Header
        View headerViewParte = parteAdapter.inflaElHeader(tableLayoutPartes);
        tableLayoutPartes.addView(headerViewParte);
        //Inflamos las filas
        for (Parte parte : listaPartes) {
            View rowView = parteAdapter.getView(listaPartes.indexOf(parte), null, tableLayoutPartes);
            tableLayoutPartes.addView(rowView);
        }
        parteAdapter.notifyDataSetChanged();
    }

    public void rellenaCaidas() {
        parteGeneralViewModel.getListaCaidas(fechaInicio, fechaFin).observe(getViewLifecycleOwner(), new Observer<ArrayList<Caidas>>() {
            @Override
            public void onChanged(ArrayList<Caidas> caidas) {
                listaCaidas = caidas;
                caidasAdapter = new CaidasAdapter(requireContext(), listaCaidas);
                tableLayoutCaidas.removeAllViews();
                creaTablaCaidas();
            }
        });
    }

    public void creaTablaCaidas() {
        View headerView = caidasAdapter.inflaElHeader(tableLayoutCaidas);
        tableLayoutCaidas.addView(headerView);
        for (Caidas caida : listaCaidas) {
            View rowView = caidasAdapter.getView(listaCaidas.indexOf(caida), null, tableLayoutCaidas);
            tableLayoutCaidas.addView(rowView);
        }
        caidasAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        return true;
    }
}