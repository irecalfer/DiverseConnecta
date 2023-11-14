package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.IOnBackPressed;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ParteCaidasPacientesFragment extends Fragment implements IOnBackPressed {
    private ViewModelArgs viewModelArgs;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private Spinner lugarSpinner;
    private String lugarSeleccionado, presenciado, avisado;
    private TextInputEditText fechaHora, nombreApellidoPaciente, factoresRiesgo, causas, circunstancias, consecuencias, unidadCaida, empleadoCaida, observaciones;
    private Button registrar;
    private RadioButton siCaida, noCaida, siAvisado, noAvisado;
    private RadioGroup radioGroupVisto, radioGroupAvisado;

    private PeticionesJson peticionesJson;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parte_caidas_pacientes, container, false);
        claseGlobal = ClaseGlobal.getInstance();
        asociaVariablesAComponentes(view);


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

        ViewModelFactory<SharedPacientesViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        sharedPacientesViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedPacientesViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPacientesViewModel.getListaLugaresLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                //Llenamos el spinner con el arraylist de Strings obtenido
                //Llamamos a getActivity() para recibir el contexto, utilizamos el layout por defecto para los spinners, y le pasamos el array.
                lugarSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                        strings));
            }
        });
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                Pacientes pacienteActual = pacientes;
                //Rellenamos los datos existentes
                rellenaUIdatosExistentes(pacienteActual);
                lugarSeleccionado();
                //Obtenemos la opción seleccionada del spinner y se la asignamos a nuestra variable miembro lugarSeleccionado
                seleccionaCaidaPresenciada(view);
                seleccionaAvisadoFamiliares(view);
                registrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registraElParteDeCaidas(pacienteActual);
                    }
                });
            }
        });
    }

    private void asociaVariablesAComponentes(View view) {
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
        radioGroupVisto = view.findViewById(R.id.caidaPresenciadaRadioGroup);
        radioGroupAvisado = view.findViewById(R.id.avisadoFamiliaresRadioGroup);
        siCaida = view.findViewById(R.id.siCaida);
        noCaida = view.findViewById(R.id.noCaida);
        siAvisado = view.findViewById(R.id.siAvisado);
        noAvisado = view.findViewById(R.id.noAvisado);
    }

    public void lugarSeleccionado() {
       lugarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               lugarSeleccionado = parent.getItemAtPosition(position).toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

    }




    public void rellenaUIdatosExistentes(Pacientes paciente) {
        fechaHora.setText(obtieneFechaYHoraFormateada());
        nombreApellidoPaciente.setText(paciente.getNombre() + " " + paciente.getApellidos());
        empleadoCaida.setText(claseGlobal.getEmpleado().getNombre() + " " + claseGlobal.getEmpleado().getApellidos());
        unidadCaida.setText(claseGlobal.getUnidades().getNombreUnidad());
    }

    public String obtieneFechaYHoraFormateada() {
        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter fechaHora = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaHora.format(LocalDateTime.now());
        return fechaFormateada;
    }



    public void seleccionaCaidaPresenciada(View view){
        radioGroupVisto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int idRadioSeleccionado = radioGroupVisto.getCheckedRadioButtonId();
                if(idRadioSeleccionado == siCaida.getId()){
                    presenciado = siCaida.getText().toString();
                }else{
                    presenciado = noCaida.getText().toString();
                }
            }
        });
    }

    public void seleccionaAvisadoFamiliares(View view){
        radioGroupAvisado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int idRadioSeleccionado = radioGroupAvisado.getCheckedRadioButtonId();
                if(idRadioSeleccionado == siAvisado.getId()){
                    avisado = siAvisado.getText().toString();
                }else{
                    avisado = noAvisado.getText().toString();
                }
            }
        });
    }

    public void registraElParteDeCaidas(Pacientes paciente){
        final String fecha = fechaDateTimeSql();
        final String cipSns = paciente.getCipSns();
        final String lugarCaida = lugarSeleccionado;
        final String factoresDeRiesgo = factoresRiesgo.getText().toString();
        final String causasCaida = causas.getText().toString();
        final String circunstanciasCaida = circunstancias.getText().toString();
        final String consecuenciasCaida = consecuencias.getText().toString();
        final String unidadCaida = claseGlobal.getUnidades().getNombreUnidad();
        final int empleadoCod = claseGlobal.getEmpleado().getCod_empleado();
        final String caidaPresenciada = presenciado;
        final String avisadoFamilia = avisado;
        final String observacionesCaida = observaciones.getText().toString();

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String url = Constantes.url_part+"crear_parte_caidas.php";
                // creating a new variable for our request queue
                StringRequest stringRequest;
                stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");

                            if ("Registro exitoso".equals(message)) {
                                Toast.makeText(getContext(), "Caída registrada correctamente", Toast.LENGTH_SHORT).show();
                                factoresRiesgo.setText("");
                                causas.setText("");
                                circunstancias.setText("");
                                consecuencias.setText("");
                                observaciones.setText("");
                            } else {
                                Toast.makeText(getContext(), "Error al registrar la caída", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Ha habido un error al intentar registrar el parte", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        Log.d("ErrorVolley", error.toString());
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new Hashtable<String, String>();

                        parametros.put("fecha_y_hora", fecha.trim());
                        parametros.put("fk_cip_sns_paciente", cipSns.trim());
                        parametros.put("lugar_caida", lugarCaida.trim());
                        parametros.put("factores_de_riesgo", factoresDeRiesgo.trim());
                        parametros.put("causas", causasCaida.trim());
                        parametros.put("circunstancias", circunstanciasCaida.trim());
                        parametros.put("consecuencias", consecuenciasCaida.trim());
                        parametros.put("unidad", unidadCaida.trim());
                        parametros.put("caida_presenciada", caidaPresenciada.trim());
                        parametros.put("avisado_a_familiares", avisadoFamilia.trim());
                        parametros.put("observaciones", observacionesCaida.trim());
                        parametros.put("fk_cod_empleado", String.valueOf(empleadoCod).trim());
                        return parametros;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        });

    }

    public String fechaDateTimeSql(){
        DateTimeFormatter fechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaHora.format(LocalDateTime.now());
        return fechaFormateada;
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}