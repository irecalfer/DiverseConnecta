package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelArgs;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ParteCaidasPacientesFragment extends Fragment {
    private ViewModelArgs viewModelArgs;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private Spinner lugarSpinner;
    private String lugarSeleccionado;
    private TextInputEditText fechaHora, nombreApellidoPaciente, factoresRiesgo, causas, circunstancias,
            consecuencias, unidadCaida, empleadoCaida, observaciones;
    private Button registrar;
    private RadioButton siCaida, noCaida, siAvisado, noAvisado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parte_caidas_pacientes, container, false);
        claseGlobal = ClaseGlobal.getInstance();
        asociaVariablesAComponentes(view);
        sharedPacientesViewModel = new ViewModelProvider(requireActivity()).get(SharedPacientesViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        poblarSpinner();
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                Pacientes pacienteActual = pacientes;
                rellenaUIdatosExistentes(pacienteActual);
            }
        });
    }

    private void asociaVariablesAComponentes(View view){
        lugarSpinner = view.findViewById(R.id.spinnerLugarCaida);
        fechaHora = view.findViewById(R.id.fechaHoraCaida);
        nombreApellidoPaciente = view.findViewById(R.id.pacienteCaida);
        factoresRiesgo = view.findViewById(R.id.factoresRiesgo);
        causas = view.findViewById(R.id.causas);
        circunstancias = view.findViewById(R.id.circunstancias);
        consecuencias = view.findViewById(R.id.consecuencias);
        unidadCaida = view.findViewById(R.id.unidadCaida);
        empleadoCaida = view.findViewById(R.id.empleado);
        observaciones = view.findViewById(R.id.observacionesCaida);
        registrar = view.findViewById(R.id.registrarCaidaBtn);
        siCaida = view.findViewById(R.id.siCaida);
        noCaida = view.findViewById(R.id.noCaida);
        siAvisado = view.findViewById(R.id.siAvisado);
        noAvisado = view.findViewById(R.id.noAvisado);
    }

    public void poblarSpinner(){
        obtieneLosLugaresDeCaidas();
        //Llenamos el spinner con el arraylist de Strings obtenido
        //Llamamos a getActivity() para recibir el contexto, utilizamos el layout por defecto para los spinners, y le pasamos el array.
        lugarSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                claseGlobal.getListaLugares()));
        //Obtenemos la opción seleccionada del spinner y se la asignamos a nuestra variable miembro lugarSeleccionado
        obtieneLugarSeleccionado();

    }

    public void obtieneLosLugaresDeCaidas(){
        String url = Constantes.url_part+"caidas_enum.php";
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);

                            //Accedemos a los valores del ENUM
                            JSONArray lugarCaida = jsonArray.getJSONObject(0).getJSONArray("lugar_caida");

                            // Itera sobre los valores ENUM
                            for (int i = 0; i < lugarCaida.length(); i++) {
                                String enumLugar = lugarCaida.getString(i);
                                //Añadimos el valor a nuestro arrayList
                                claseGlobal.getListaLugares().add(enumLugar);
                            }
                            claseGlobal.setListaLugares(claseGlobal.getListaLugares());
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                requestQueue.add(stringRequest);
            }
        });
    }

    public void obtieneLugarSeleccionado(){
        lugarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Si ha sido seleccionado un lugar lo asignamos a la variable lugarSeleccionado
                lugarSeleccionado = (String) lugarSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No seleccionaron nada
            }
        });
    }
    public void rellenaUIdatosExistentes(Pacientes paciente){
        fechaHora.setText(obtieneFechaYHoraFormateada());
        nombreApellidoPaciente.setText(paciente.getNombre()+" "+paciente.getApellidos());
        empleadoCaida.setText(claseGlobal.getEmpleado().getNombre()+" "+claseGlobal.getEmpleado().getApellidos());
    }

    public String obtieneFechaYHoraFormateada(){
        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter fechaHora = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaHora.format(LocalDateTime.now());
        return fechaFormateada;
    }
}