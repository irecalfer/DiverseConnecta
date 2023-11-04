package com.calferinnovate.mediconnecta.Home.Fragments.HomeFragments.Rutinas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Adaptadores.RutinasAdapter;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Fechas;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.HiloPeticiones;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ConsultasYRutinasDiariasViewModel;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelFactory;
import com.calferinnovate.mediconnecta.clases.Rutinas;
import com.calferinnovate.mediconnecta.clases.Unidades;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class consultasYRutinasDiariasFragment extends Fragment {

    private RecyclerView rvConsultas;
    private String url;
    private RutinasAdapter rutinasAdapter;
    private TabLayout tabLayout;
    private PacientesAgrupadosRutinas pacientesAgrupadosRutinas;
    private Fechas fechas;
    private Unidades unidades;
    private Rutinas rutinas;
    private ClaseGlobal claseGlobal;
    private Pacientes pacientes;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayList<Rutinas> listaRutinas = new ArrayList<>();
    private EditText fechaRutina;
    private ConsultasYRutinasDiariasViewModel consultasYRutinasDiariasViewModel;
    String tipoRutinaActual;
    private PeticionesJson peticionesJson;
    private ViewModelArgs viewModelArgs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_consultas_y_rutinas_diarias, container, false);
        referenciaVariables(vista);
        llamadaObjetosGlobales();

        //Seteamos la fecha en el EditText
        fechaRutina.setText(fechas.getFechaActual()); // FORMATEAR FECHA PARA QUE APAREZCA DE FORMA dd/MM/YYYY
        // Inicializa el adaptador una sola vez
        rutinasAdapter = new RutinasAdapter(claseGlobal.getListaProgramacion(), claseGlobal.getListaPacientes());
        rvConsultas.setAdapter(rutinasAdapter);

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

        ViewModelFactory<ConsultasYRutinasDiariasViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        consultasYRutinasDiariasViewModel = new ViewModelProvider(this, factory).get(ConsultasYRutinasDiariasViewModel.class);

        // Observa el LiveData y actualiza el adaptador cuando cambien los datos
        consultasYRutinasDiariasViewModel.getListaProgramacionLiveData(fechas.getFechaActual(), unidades.getNombreUnidad(), tipoRutinaActual)
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<PacientesAgrupadosRutinas>>() {
                    @Override
                    public void onChanged(ArrayList<PacientesAgrupadosRutinas> newData) {
                        // Actualizar el adaptador con los nuevos datos
                        rutinasAdapter.actualizarDatos(newData);
                    }
                });

        //Eatablece el listener de los tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tipoRutinaActual = (String) tab.getText();
                // Limpia los datos actuales en el adaptador
                claseGlobal.getListaProgramacion().clear();
                // Notifica al adaptador que los datos han cambiado
                //rutinasAdapter.notifyDataSetChanged();
                obtenerDatosRutinas();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No es necesario implementar esto
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No es necesario implementar esto
            }
        });

        // Inicialmente, obtén los datos para el tab actual
        if (tabLayout.getTabCount() > 0) {
            tipoRutinaActual = (String) tabLayout.getTabAt(0).getText();
            obtenerDatosRutinas();
        }

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void referenciaVariables(View view){
        rvConsultas = view.findViewById(R.id.rvRutinas);
        tabLayout = view.findViewById(R.id.tabLAyoutRutinas);
        fechaRutina = view.findViewById(R.id.fechaActual);
    }

    public void llamadaObjetosGlobales(){
        claseGlobal = ClaseGlobal.getInstance();
        fechas = claseGlobal.getFechas();
        unidades = claseGlobal.getUnidades();
        rutinas = claseGlobal.getRutinas();
        pacientesAgrupadosRutinas = claseGlobal.getPacientesAgrupadosRutinas();
        pacientes = claseGlobal.getPacientes();
    }

    private void obtenerDatosRutinas() {
        // Realiza la solicitud HTTP utilizando el ViewModel y pasa la fecha, unidad y tipo de rutina
        consultasYRutinasDiariasViewModel.obtieneDatosRutinasDiaPacientes(fechas.getFechaActual(),
                unidades.getNombreUnidad(), tipoRutinaActual);


    }

}