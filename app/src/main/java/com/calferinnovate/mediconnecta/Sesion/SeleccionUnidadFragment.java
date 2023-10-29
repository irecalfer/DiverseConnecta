package com.calferinnovate.mediconnecta.Sesion;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Home.HomeActivity;
import com.calferinnovate.mediconnecta.clases.Area;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Unidades;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Empleado;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SeleccionUnidadFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Button botonFinalizar;
    NavController navController;
    TextInputEditText nombre, cod_empleado, cargo;
    Spinner areaSP, unidadesSP;
    Empleado empleado;
    ImageView foto;
    private Unidades unidades;
    private Area area;

    ArrayList<String> listaAreas = new ArrayList<>();
    ArrayList<String> listaUnidades = new ArrayList<>();
    ArrayAdapter <String> areasAdapter;
    ArrayAdapter <String> unidadesAdapter;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

    View vista = inflater.inflate(R.layout.fragment_seleccion_unidad, container, false);


        return vista;

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        asociacionVariableComponente(view);
        llamadaAObjetoClaseGlobal();

        navController = Navigation.findNavController(view);

        // Instanciamos RequestQueue
        requestQueue = Volley.newRequestQueue(getContext());

        completaDatosEmpleado(empleado);
        poblarSpinner();



        botonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), HomeActivity.class);
               startActivity(intent);
            }
        });

    }

    /** Called when the user taps the Send button */


    public void completaDatosEmpleado(Empleado e){
        nombre.setText(String.valueOf(e.getNombre()+" "+e.getApellidos()));
        cargo.setText(String.valueOf(e.getNombreCargo()));
        cod_empleado.setText(String.valueOf(e.getCod_empleado()));
        //Cargamos la foto del empleado con Glide
        Glide.with(getContext()).load(empleado.getFoto()).into(foto);
    }

    public void asociacionVariableComponente(View view){
        botonFinalizar = (Button) view.findViewById(R.id.AccesoAlHome);
        nombre = (TextInputEditText) view.findViewById(R.id.nombreYApellidos);
        cod_empleado = (TextInputEditText) view.findViewById(R.id.cod_empleado);
        cargo = (TextInputEditText) view.findViewById(R.id.cargo);
        areaSP = (Spinner) view.findViewById(R.id.spinnerArea);
        unidadesSP = (Spinner) view.findViewById(R.id.spinnerUnidad);
        foto = (ImageView) view.findViewById(R.id.fotoEmpleado);
    }

    public void llamadaAObjetoClaseGlobal(){
        empleado = ((ClaseGlobal) getActivity().getApplicationContext()).empleado;
        unidades = ((ClaseGlobal) getActivity().getApplicationContext()).unidades;
        area = ((ClaseGlobal) getActivity().getApplicationContext()).area;
    }

    public void poblarSpinner(){
        String urlArea = Constantes.url_part+"areas.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                urlArea, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("datos_area");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        area.setId_area(jsonObject.optInt("id_area"));
                        area.setNombre(jsonObject.optString("nombre_area"));
                        listaAreas.add(area.getNombre());
                        areasAdapter = new ArrayAdapter<>(getContext(), R.layout.my_spinner, listaAreas);
                        areasAdapter.setDropDownViewResource(R.layout.my_spinner);
                        areaSP.setAdapter(areasAdapter);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.toString();
            }
        });
        requestQueue.add(jsonObjectRequest);
        areaSP.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerArea){
            listaUnidades.clear();
            String areaSeleccionada = parent.getSelectedItem().toString();
            Log.d("nombrePadre", areaSeleccionada);
            String urlUnidades = Constantes.url_part+"unidades.php?nombre_area="+areaSeleccionada;
            requestQueue = Volley.newRequestQueue(getContext());
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlUnidades, null,
                    new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("datos_unidades");
                        for(int i = 0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            unidades.setId_unidad(jsonObject.optInt("id_unidad"));
                            unidades.setNombreUnidad(jsonObject.optString("nombre"));
                            unidades.setFk_area(jsonObject.optInt("fk_id_area"));
                            Log.d("datos", unidades.getNombreUnidad());
                            listaUnidades.add(unidades.getNombreUnidad());
                            unidadesAdapter = new ArrayAdapter<>(getContext(), R.layout.my_spinner, listaUnidades);
                            unidadesAdapter.setDropDownViewResource(R.layout.my_spinner);
                            unidadesSP.setAdapter(unidadesAdapter);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.toString();

                }
            });
            requestQueue.add(jsonObjectRequest);
            unidadesSP.setOnItemSelectedListener(this);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




}