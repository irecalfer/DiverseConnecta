package com.calferinnovate.mediconnecta.Sesion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Area;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Empleado;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.Unidades;
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
    private Area area;
    private ArrayList<Unidades> unidadesArrayList = new ArrayList<>();
    private ClaseGlobal claseGlobal;
    private Pacientes pacientes;
    private Unidades unidades;
    ArrayList<String> listaAreas = new ArrayList<>();
    ArrayList<String> listaUnidades = new ArrayList<>();
    ArrayAdapter<String> areasAdapter;
    ArrayAdapter<String> unidadesAdapter;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    private ObtenerDatosCallback obtenerDatosCallback;


    //Creamos una interfaz
    public interface ObtenerDatosCallback {
        void onDatosObtenidos();

        void onError();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista = inflater.inflate(R.layout.fragment_seleccion_unidad, container, false);
        asociacionVariableComponente(vista);
        llamadaAObjetoClaseGlobal();
        return vista;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        // Instanciamos RequestQueue
        requestQueue = Volley.newRequestQueue(getContext());

        completaDatosEmpleado(empleado);
        poblarSpinner();

        // Inicializamos la interfaz que hemos creado para el callback
        obtenerDatosCallback = new ObtenerDatosCallback() {
            @Override
            public void onDatosObtenidos() {
                // Los datos se han obtenido con éxito, inicia la nueva actividad
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError() {
                // Se produjo un error al obtener los datos, puedes mostrar un mensaje de error si deseas
                Toast.makeText(getActivity(), "Error al obtener datos", Toast.LENGTH_SHORT).show();
            }
        };

        botonFinalizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                obtieneDatosPacientesUnidad();
            }
        });


    }

    public void completaDatosEmpleado(Empleado e) {
        nombre.setText(String.valueOf(e.getNombre() + " " + e.getApellidos()));
        cargo.setText(String.valueOf(e.getNombreCargo()));
        cod_empleado.setText(String.valueOf(e.getCod_empleado()));
        //Cargamos la foto del empleado con Glide
        Glide.with(getContext()).load(empleado.getFoto()).into(foto);
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
        empleado = ((ClaseGlobal) getActivity().getApplicationContext()).getEmpleado();
        area = ((ClaseGlobal) getActivity().getApplicationContext()).getArea();
        claseGlobal = (ClaseGlobal) getActivity().getApplicationContext();
        pacientes = claseGlobal.getPacientes();
        unidades = claseGlobal.getUnidades();
    }

    public void poblarSpinner() {
        String urlArea = Constantes.url_part + "areas.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                urlArea, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("datos_area");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        area.setId_area(jsonObject.optInt("id_area"));
                        area.setNombre(jsonObject.optString("nombre_area"));
                        listaAreas.add(area.getNombre());
                    }
                    areasAdapter = new ArrayAdapter<>(getContext(), R.layout.my_spinner, listaAreas);
                    areasAdapter.setDropDownViewResource(R.layout.my_spinner);
                    areaSP.setAdapter(areasAdapter);
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
        if (parent.getId() == R.id.spinnerArea) {
            listaUnidades.clear();
            String areaSeleccionada = parent.getSelectedItem().toString();
            Log.d("nombrePadre", areaSeleccionada);
            String urlUnidades = Constantes.url_part + "unidades.php?nombre_area=" + areaSeleccionada;

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlUnidades, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("datos_unidades");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Unidades nuevaUnidad = new Unidades(jsonObject.optInt("id_unidad"), jsonObject.optInt("fk_id_area"),
                                            jsonObject.optString("nombre"));
                                    unidades.getListaUnidades().add(nuevaUnidad);
                                    listaUnidades.add(nuevaUnidad.getNombreUnidad());
                                }
                                // Actualiza la lista de Unidades en ClaseGlobal
                                unidades.setListaUnidades(unidades.getListaUnidades());
                                unidadesAdapter = new ArrayAdapter<>(getContext(), R.layout.my_spinner, listaUnidades);
                                unidadesAdapter.setDropDownViewResource(R.layout.my_spinner);
                                unidadesSP.setAdapter(unidadesAdapter);
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
            unidadesSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getId() == R.id.spinnerUnidad) {
                        // Obtiene la unidad seleccionada del spinner
                        Unidades unidadSeleccionada = unidades.getListaUnidades().get(position);

                        // Establece la unidad seleccionada en ClaseGlobal
                        claseGlobal.setUnidades(unidadSeleccionada);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Prueba commit
    public void obtieneDatosPacientesUnidad() {
        Unidades unidadSeleccionada = claseGlobal.getUnidades();
        String nombreUnidad = unidadSeleccionada.getNombreUnidad();
        String url = Constantes.url_part + "pacientes.php?nombre=" + unidadSeleccionada.getNombreUnidad();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("pacientes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Pacientes nuevoPacientes = new Pacientes(jsonObject.optString("nombre"), jsonObject.optString("apellidos"),
                                jsonObject.optString("foto"), jsonObject.optString("fecha_nacimiento"),
                                jsonObject.optString("dni"), jsonObject.optString("lugar_nacimiento"),
                                jsonObject.optString("sexo"), jsonObject.optString("cip_sns"),
                                jsonObject.optInt("num_seguridad_social"), jsonObject.optInt("fk_id_unidad"),
                                jsonObject.optInt("fk_id_seguro"), jsonObject.optInt("fk_num_habitacion"),
                                jsonObject.optInt("fk_num_historia_clinica"));
                        // Agrega un nuevo paciente a la lista en tu fragmento
                        pacientes.getListaPacientes().add(nuevoPacientes);
                    }
                    // Actualiza la lista de pacientes en ClaseGlobal
                    pacientes.setListaPacientes(pacientes.getListaPacientes());

                    obtenerDatosCallback.onDatosObtenidos(); // Llama al callback en caso de éxito
                } catch (JSONException e) {
                    e.printStackTrace();
                    obtenerDatosCallback.onError(); // Llama al callback en caso de error
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                obtenerDatosCallback.onError(); // Llama al callback en caso de error
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


}
