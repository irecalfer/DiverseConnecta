package com.calferinnovate.mediconnecta.Sesion;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Unidades;
import com.loopj.android.http.*;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Empleado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class SeleccionUnidadFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Button botonFinalizar;
    NavController navController;
    TextView nombre, cod_empleado, cargo;
    Spinner geriatriaSP, saludMentalSP;
    Empleado empleado;
    private Unidades unidades;

    ArrayList<String> listaUnidadesGeriatria = new ArrayList<>();
    ArrayList<String> listaUnidadesSaludMental = new ArrayList<>();
    ArrayAdapter <String> geriatriaAdapter;
    ArrayAdapter <String> saludMentalAdapter;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

    View vista = inflater.inflate(R.layout.fragment_seleccion_unidad, container, false);

        return vista;

    }


    public void completaDatosEmpleado(Empleado e){
        nombre.setText(String.valueOf(e.getNombre()+" "+e.getApellidos()));
        cargo.setText(String.valueOf(e.getNombreCargo()));
        cod_empleado.setText(String.valueOf(e.getCod_empleado()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        empleado = ((ClaseGlobal) getActivity().getApplicationContext()).empleado;
        unidades = ((ClaseGlobal) getActivity().getApplicationContext()).unidades;
        navController = Navigation.findNavController(view);


        botonFinalizar = (Button) view.findViewById(R.id.AccesoAlHome);
        nombre = view.findViewById(R.id.nombreYApellidos);
        cod_empleado = (TextView) view.findViewById(R.id.cod_empleado);
        cargo = (TextView) view.findViewById(R.id.cargo);
        geriatriaSP = (Spinner) view.findViewById(R.id.spinnerGeriatria);
        saludMentalSP = (Spinner) view.findViewById(R.id.spinnerSaludMental);


        // Instanciamos RequestQueue
        requestQueue = Volley.newRequestQueue(getContext());

        completaDatosEmpleado(empleado);
        poblarSpinner();



        botonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_seleccionUnidadFragment_to_homeActivity);
            }
        });

    }


    public void poblarSpinner(){
        String urlGeriatria = Constantes.url_part+"unidades.php?fk_id_area=1";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                urlGeriatria, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("datos_unidades");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        unidades.setNombreUnidad(jsonObject.optString("nombre"));
                        listaUnidadesGeriatria.add(unidades.getNombreUnidad());
                        geriatriaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listaUnidadesGeriatria);
                        geriatriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        geriatriaSP.setAdapter(geriatriaAdapter);
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
        geriatriaSP.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerGeriatria){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*
    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONArray("datos_unidades");

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                unidades.setNombreUnidad(jsonObject.optString("nombre"));
                Log.d("pruebaUnidad", unidades.getNombreUnidad());
                lista.add(unidades);
                a = new ArrayAdapter<Unidades>(getContext(), android.R.layout.simple_spinner_item, lista);
                a.setDropDownViewResource(android.R.layout.simple_spinner_item);
                geriatriaSP.setAdapter(a);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
        @Override
    public void onErrorResponse(VolleyError error) {
            Toast.makeText(getContext(), "Algo ha fallado", Toast.LENGTH_SHORT).show();
    }

    /*public void poblarSpinner(){
        String urlGeriatria = Constantes.url_part+"seleccion_unidades.php?fk_id_unidad=1";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlGeriatria, response -> {
            JSONObject jsonObject = null;
            ArrayList<Unidades> lista = new ArrayList<Unidades>();
            for (int i =0; i <response.length(); i++){
                try {
                    jsonObject = response.getJSONObject(i);
                    unidades.setNombreUnidad(jsonObject.optString("nombre"));
                    lista.add(unidades);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
            ArrayAdapter <Unidades> a = new ArrayAdapter<Unidades>(getContext(), android.R.layout.simple_dropdown_item_1line, lista);
            geriatriaSP.setAdapter(a);
        }, error -> {
            Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
        });
        rq = Volley.newRequestQueue(getContext());
        rq.add(jsonArrayRequest);
    }*/


}