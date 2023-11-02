package com.calferinnovate.mediconnecta.Home.Fragments.HomeFragments.Rutinas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Fechas;
import com.calferinnovate.mediconnecta.clases.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.clases.Rutinas;
import com.calferinnovate.mediconnecta.clases.Secciones.SeccionesRutinas;
import com.calferinnovate.mediconnecta.clases.Unidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class consultasYRutinasDiariasFragment extends Fragment {

    private RecyclerView principalRV;
    private String url;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private PacientesAgrupadosRutinas pacientesAgrupadosRutinas;
    private Fechas fechas;
    private Unidades unidades;
    private Rutinas rutinas;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_consultas_y_rutinas_diarias, container, false);
        referenciaVariables(vista);
        llamadaObjetosGlobales();
        requestQueue = Volley.newRequestQueue(getContext());
        //principalRV.setLayoutManager(new LinearLayoutManager(getContext()));
        // Create an instance of SectionedRecyclerViewAdapter
        sectionAdapter = new SectionedRecyclerViewAdapter();
        //principalRV.setAdapter(rutinasAdapter);
        //Log.d("UnidadActual", unidades.getNombreUnidad());
        //Agregamos secciones y elementos al adaptador
        obtieneDatosRutinasDia();
        //agregaSeccionesyElementos();
        return vista;
    }

    public void referenciaVariables(View view){
        principalRV = view.findViewById(R.id.rvRutinas);
    }

    public void llamadaObjetosGlobales(){
        fechas = ((ClaseGlobal) getActivity().getApplicationContext()).getFechas();
        unidades = ((ClaseGlobal) getActivity().getApplicationContext()).getUnidades();
        rutinas = ((ClaseGlobal) getActivity().getApplicationContext()).getRutinas();
        pacientesAgrupadosRutinas = ((ClaseGlobal) getActivity().getApplicationContext()).getPacientesAgrupadosRutinas();

    }

    public void obtieneDatosRutinasDia(){
        url = Constantes.url_part+"rutinas.php?"+Constantes.comillas+fechas.getFechaActual()+Constantes.comillas+"&nombre="+unidades.getNombreUnidad();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //jsonObject.optString("diario")
                    JSONArray jsonArray = response.getJSONArray("pacientesAgrupados");
                    for(int i =0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        rutinas = new Rutinas(jsonObject.optString("nombre"),
                                jsonObject.optString("apellidos"), jsonObject.optString("hora_rutina"));
                        pacientesAgrupadosRutinas.getListRutinas().add(rutinas);
                        // Add your Sections
                        sectionAdapter.addSection(new SeccionesRutinas());
                        principalRV.setLayoutManager(new LinearLayoutManager(getContext()));
                        principalRV.setAdapter(sectionAdapter);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

 //HACER DE NUEVO PHP DE RUTINAS Y MODELARLO EN JAVA

}