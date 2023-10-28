package com.calferinnovate.mediconnecta.Home.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Avisos;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;

import java.sql.Date;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private CalendarView calendario;
    private Button abrirFragmentoListaAvisos;
    private FragmentContainerView avisosDiarios;
    private String fechaSeleccionada;
    private Avisos avisos;
    ArrayList<String> listaAvisos = new ArrayList<>();
    ArrayAdapter<String> avisosAdapter;
    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_home, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        asociarVariablesConComponentes(view);
        objetoAvisosClaseGlobal();

        abrirFragmentoListaAvisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "Empieza primer fragmento");
                obtieneFecha();
               abrirAvisosDiarios();
                Log.d("tag", "Termina segundo fragmento");
            }
        });
    }

    public void objetoAvisosClaseGlobal(){
        avisos = ((ClaseGlobal) getActivity().getApplicationContext()).avisos;
    }

    public void asociarVariablesConComponentes(View view){
        calendario = (CalendarView) view.findViewById(R.id.calendarView);
        abrirFragmentoListaAvisos = (Button) view.findViewById(R.id.abrirAvisos);

    }

    public String obtieneFecha(){
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                fechaSeleccionada= year+"-"+month+"-"+dayOfMonth;
            }
        });
        return fechaSeleccionada;
    }
    public void abrirAvisosDiarios(){
        navController.navigate(R.id.action_homeFragment_to_listaAvisosFragment2);

    }


}