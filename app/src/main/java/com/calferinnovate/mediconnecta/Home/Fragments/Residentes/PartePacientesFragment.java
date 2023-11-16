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
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Interfaces.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PartePacientesFragment extends Fragment implements IOnBackPressed {

    private TextInputEditText fechaHora;
    private TextInputEditText pacienteNombreApellido;
    private TextInputEditText descripcion;
    private TextInputEditText empleadoNombre;
    private Button registrar;
    private ClaseGlobal claseGlobal;
    private SharedPacientesViewModel sharedPacientesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parte_pacientes, container, false);
        claseGlobal = ClaseGlobal.getInstance();
        asociaComponentesAVariables(view);
        sharedPacientesViewModel = new ViewModelProvider(requireActivity()).get(SharedPacientesViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                Pacientes pacienteActual = pacientes;
                rellenaUIdatosExistentes(pacienteActual);

                registrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registraElParte(pacienteActual);
                        descripcion.setText("");
                    }
                });
            }
        });


    }

    public void asociaComponentesAVariables(View view){
        fechaHora = view.findViewById(R.id.fechaHoraParte);
        pacienteNombreApellido = view.findViewById(R.id.pacienteParte);
        descripcion = view.findViewById(R.id.descripcionParte);
        empleadoNombre = view.findViewById(R.id.empleadoParte);
        registrar = view.findViewById(R.id.registraParte);
    }

    public void rellenaUIdatosExistentes(Pacientes pacientes){
        fechaHora.setText(obtieneFechaYHoraFormateada());
        pacienteNombreApellido.setText(pacientes.getNombre()+" "+pacientes.getApellidos());
        empleadoNombre.setText(claseGlobal.getEmpleado().getNombre()+" "+claseGlobal.getEmpleado().getApellidos());
    }

    public String obtieneFechaYHoraFormateada(){

            // Formato de salida (día-mes-año abreviado)
            DateTimeFormatter fechaHora = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            // Formatea la fecha en el formato de salida
            String fechaFormateada = fechaHora.format(LocalDateTime.now());
            return fechaFormateada;
    }

    public void registraElParte(Pacientes paciente){
        final String fecha = fechaDateTimeSql();
        final String cipSns = paciente.getCipSns();
        final String descripcionParte = descripcion.getText().toString();
        final int codEmpleado = claseGlobal.getEmpleado().getCod_empleado();

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String url = Constantes.url_part+"crea_parte.php";
                // creating a new variable for our request queue
                StringRequest stringRequest;
                stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");

                            if ("Registro exitoso".equals(message)) {
                                Toast.makeText(getContext(), "Parte registrado correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Error al registrar el parte", Toast.LENGTH_SHORT).show();
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
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new Hashtable<String, String>();
                        parametros.put("fk_cip_sns", cipSns.trim());
                        parametros.put("descripcion", descripcionParte.trim());
                        parametros.put("fk_cod_empleado", String.valueOf(codEmpleado));
                        parametros.put("fecha", fecha.trim());
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