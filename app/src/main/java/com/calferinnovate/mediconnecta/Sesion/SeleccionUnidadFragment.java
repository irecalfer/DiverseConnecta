package com.calferinnovate.mediconnecta.Sesion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Home.HomeActivity;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SeleccionUnidadViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SeleccionUnidadFragment extends Fragment {

    Button botonFinalizar;
    NavController navController;
    TextInputEditText nombre, cod_empleado, cargo;
    Spinner areaSP, unidadesSP;
    Empleado empleado;
    ImageView foto;
    private ArrayList<Unidades> unidadesArrayList = new ArrayList<>();
    private ClaseGlobal claseGlobal;
    private Unidades unidades;
    ArrayList<String> listaAreas = new ArrayList<>();
    ArrayList<String> listaUnidades = new ArrayList<>();
    ArrayAdapter<String> areasAdapter;
    ArrayAdapter<String> unidadesAdapter;



    private SeleccionUnidadViewModel seleccionUnidadViewModel;
    private ViewModelArgs viewModelArgs;
    private PeticionesJson peticionesJson;
    private String areaSeleccionada;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista = inflater.inflate(R.layout.fragment_seleccion_unidad, container, false);
        asociacionVariableComponente(vista);
        llamadaAObjetoClaseGlobal();

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

        ViewModelFactory<SeleccionUnidadViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        seleccionUnidadViewModel = new ViewModelProvider(requireActivity(), factory).get(SeleccionUnidadViewModel.class);

        return vista;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        completaDatosEmpleado(empleado);
        obtieneAreasyPoblaSpinner();


        botonFinalizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                seleccionUnidadViewModel.obtieneDatosPacientes(unidades.getNombreUnidad()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean obtenidos) {
                        if(obtenidos){
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getActivity(), "Error al obtener datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    public void completaDatosEmpleado(Empleado e) {
        nombre.setText(e.getNombre() + " " + e.getApellidos());
        cargo.setText(String.valueOf(e.getNombreCargo()));
        cod_empleado.setText(String.valueOf(e.getCod_empleado()));
        //Cargamos la foto del empleado con Glide
        Glide.with(getContext()).load(empleado.getFoto()).circleCrop().into(foto);
    }

    public void asociacionVariableComponente(View view) {
        botonFinalizar = view.findViewById(R.id.AccesoAlHome);
        nombre = view.findViewById(R.id.nombreYApellidos);
        cod_empleado = view.findViewById(R.id.cod_empleado);
        cargo = view.findViewById(R.id.cargo);
        areaSP = view.findViewById(R.id.spinnerArea);
        unidadesSP = view.findViewById(R.id.spinnerUnidad);
        foto = view.findViewById(R.id.fotoEmpleado);
    }

    public void llamadaAObjetoClaseGlobal() {
        claseGlobal = ClaseGlobal.getInstance();
        unidadesArrayList = claseGlobal.getListaUnidades();
        unidades = claseGlobal.getUnidades();
        empleado = claseGlobal.getEmpleado();
    }

    private void obtieneAreasyPoblaSpinner() {
        seleccionUnidadViewModel.obtenerDatosAreas().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                listaAreas = strings;
                areasAdapter = new ArrayAdapter<>(getContext(), R.layout.my_spinner, listaAreas);
                areasAdapter.setDropDownViewResource(R.layout.my_spinner);
                areaSP.setAdapter(areasAdapter);
                poblaSpinner();
            }
        });

    }

    public void poblaSpinner() {
        areaSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinnerArea) {
                    listaUnidades.clear();
                    unidadesArrayList.clear();
                    areaSeleccionada = parent.getSelectedItem().toString();
                    obtieneUnidadesYPoblaSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void obtieneUnidadesYPoblaSpinner() {
        seleccionUnidadViewModel.obtenerDatosUnidades(areaSeleccionada).observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> string) {
                listaUnidades = string;
                unidadesAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_spinner, listaUnidades);
                unidadesAdapter.setDropDownViewResource(R.layout.my_spinner);
                unidadesSP.setAdapter(unidadesAdapter);
                unidadesSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getId() == R.id.spinnerUnidad) {
                            // Obtiene la unidad seleccionada del spinner
                            Unidades unidad = unidadesArrayList.get(position);
                            unidades = unidad;
                            // Establece la unidad seleccionada en ClaseGlobal
                            claseGlobal.setUnidades(unidad);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

    }






}
