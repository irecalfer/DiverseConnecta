package com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Adaptadores.PartePacienteAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.AlumnosFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Map;

/**
 * Fragmento encargado de crear los partes de cada paciente y subirlo a la base de datos.
 */
public class PartePacientesFragment extends Fragment implements IOnBackPressed {


    private TextInputEditText descripcion;
    private Button registrarParteBtn;
    private ClaseGlobal claseGlobal;
    private SharedAlumnosViewModel sharedAlumnosViewModel;

    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_parte_pacientes.xml.
     * Obtiene instancia de ClaseGlobal.
     * Establece el título del fragmento.
     * Inicializa el View Model.
     *
     * @param inflater           inflador utilizado para inflar el diseño de la UI.
     * @param container          Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parte_pacientes, container, false);
        claseGlobal = ClaseGlobal.getInstance();
        enlazaRecursos(view);
        getActivity().setTitle("Parte");
        sharedAlumnosViewModel = new ViewModelProvider(requireActivity()).get(SharedAlumnosViewModel.class);
        return view;
    }

    /**
     * Método encargado de enlazar los recursos de la UI con las variables miembro.
     *
     * @param view La vista inflada.
     */
    public void enlazaRecursos(View view) {
        descripcion = view.findViewById(R.id.descripcionParte);
        registrarParteBtn = view.findViewById(R.id.registraParte);
    }


    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Obtiene el paciente seleccionado, rellena la UI con los datos existentes y establece la
     * escucha del botón.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), paciente -> {
            rellenaUI(paciente, view);
            clickListenerButtonRegistrar(paciente);
        });


    }

    /**
     * Método encargado de configurar el Adaptador PartePacienteAdapter y actualizar la UI con los datos del paciente.
     *
     * @param pacienteActual Paciente seleccionado.
     * @param view           Vista inflada.
     */
    public void rellenaUI(Alumnos pacienteActual, View view) {
        PartePacienteAdapter partePacienteAdapter = new PartePacienteAdapter(pacienteActual, claseGlobal, requireContext());
        partePacienteAdapter.rellenaUI(view);
    }

    /**
     * Método que establece la escucha del botón registrarParteBtn, si se ha pulsado llama a registraElParte().
     *
     * @param pacienteActual Paciente seleccionado.
     */
    public void clickListenerButtonRegistrar(Alumnos pacienteActual) {
        registrarParteBtn.setOnClickListener(v -> {
            registraElParte(pacienteActual);
            descripcion.setText("");
        });
    }


    /**
     * Método encargado de subir el parte a la base de datos.
     * Asigna a variable el contenido del EditText de descripción.
     * Valida si se ha introducido la descripción.
     * Construye la url para la solcitud POST al servidor, crea la solicitud Volley incluyendo los parámetros
     * de la solicitud en getParams() y procesa y maneja la respuesta JSON del servidor.
     *
     * @param paciente Paciente Seleccionado.
     */
    public void registraElParte(Alumnos alumno) {
        final String fecha = fechaDateTimeSql();
        final int idAlumno = alumno.getIdAlumno();
        final String descripcionParte = descripcion.getText().toString();
        final int codEmpleado = claseGlobal.getEmpleado().getCod_empleado();

        if (TextUtils.isEmpty(descripcionParte)) {
            descripcion.setError("La descripción no puede estar vacía");
            return;
        }

        String url = Constantes.url_part + "crea_parte.php";
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, url, response -> {
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
        }, error -> {
            Toast.makeText(getContext(), "Ha habido un error al intentar registrar el parte", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new Hashtable<>();
                parametros.put("fk_id_alumno", String.valueOf(idAlumno));
                parametros.put("descripcion", descripcionParte.trim());
                parametros.put("fk_cod_empleado", String.valueOf(codEmpleado));
                parametros.put("fecha", fecha.trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    /**
     * Método encargado de formatear la fecha y hora del parte a yyyy-MM-dd HH:mm:ss para
     * subirlo a la base de datos
     *
     * @return fecha y hora formateada.
     */
    public String fechaDateTimeSql() {
        DateTimeFormatter fechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fechaHora.format(LocalDateTime.now());
    }

    /**
     * Método que agrega la lógica específica del fragmento para manejar el restroceso.
     * Al presionar back volvería al PacientesFragment.
     *
     * @return true si el fragmento manejar el retroceso, false en caso contrario.
     */
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AlumnosFragment()).commit();
        return true;
    }
}