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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Avisos;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment{

    private CalendarView calendario;
    private Button abrirFragmentoListaAvisos;
    private ListView listViewEmpleados;
    private String fechaSeleccionada;
    private String url;
    private Calendar calendar;
    String comillas = "\"";
    private ArrayAdapter avisosAdapter;
    private Avisos avisos;

    ArrayList<String> listaAvisos = new ArrayList<>();
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

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
        calendar = Calendar.getInstance();


        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                fechaSeleccionada = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            }
        });

        abrirFragmentoListaAvisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirAvisosDiarios(fechaSeleccionada);
            }
        });
    }

    public void objetoAvisosClaseGlobal(){
        avisos = ((ClaseGlobal) getActivity().getApplicationContext()).avisos;
    }

    public void asociarVariablesConComponentes(View view){
        calendario = (CalendarView) view.findViewById(R.id.calendarView);
        abrirFragmentoListaAvisos = (Button) view.findViewById(R.id.abrirAvisos);
        listViewEmpleados = (ListView) view.findViewById(R.id.listViewAvisos);
    }

    public void abrirAvisosDiarios(String fecha){
        url = Constantes.url_part+"avisos.php?fecha_aviso="+comillas+fechaSeleccionada+comillas;

        requestQueue = Volley.newRequestQueue(getContext());

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray = response.getJSONArray("avisos");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        avisos = new Avisos(jsonObject.optInt("num_aviso"), jsonObject.optString(("fecha_aviso")), jsonObject.optString("contenido"));
                        listaAvisos.add(avisos.getContenido());
                        avisosAdapter = new ArrayAdapter<>(getContext(), R.layout.fragment_home, listaAvisos);
                        listViewEmpleados.setAdapter(avisosAdapter);
                        Log.d("aviso", avisos.getContenido());
                    }
                }catch(JSONException jsonException){
                    throw new RuntimeException(jsonException);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.toString();
                Log.d("error", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


}