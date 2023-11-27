package com.calferinnovate.mediconnecta.View.Home.Fragments.Pacientes;

import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Adaptadores.ParteCaidasAdapter;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Map;

/**
 * Fragmento encargado de crear los partes de caídas
 */
public class ParteCaidasPacientesFragment extends Fragment implements IOnBackPressed {
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private Spinner lugarSpinner;
    private String lugarSeleccionado, presenciado, avisado;
    private TextInputEditText factoresRiesgo, causas, circunstancias, consecuencias, observaciones;
    private Button registrarCaidaBtn;
    private RadioButton siCaida, noCaida, siAvisado, noAvisado;
    private RadioGroup radioGroupVisto, radioGroupAvisado;


    private PeticionesJson peticionesJson;


    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_sesion.xml.
     * Establece el título del fragmento y obtiene una instancia de la clase global.
     *
     * @param inflater           inflador utilizado para inflar el diseño de la UI.
     * @param container          Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parte_caidas_pacientes, container, false);
        claseGlobal = ClaseGlobal.getInstance();
        enlazaRecursos(view);
        getActivity().setTitle("Parte de Caídas");
        implementaViewModel();

        return view;
    }

    /**
     * Método encargado de enlazar los recusos de la UI con las variables miembro.
     *
     * @param view La vista inflada.
     */
    private void enlazaRecursos(View view) {
        lugarSpinner = view.findViewById(R.id.spinnerLugarCaida);

        factoresRiesgo = view.findViewById(R.id.factoresRiesgo);
        causas = view.findViewById(R.id.causas);
        circunstancias = view.findViewById(R.id.circunstancias);
        consecuencias = view.findViewById(R.id.consecuencias);

        observaciones = view.findViewById(R.id.observacionesCaida);
        registrarCaidaBtn = view.findViewById(R.id.registrarCaidaBtn);
        radioGroupVisto = view.findViewById(R.id.caidaPresenciadaRadioGroup);
        radioGroupAvisado = view.findViewById(R.id.avisadoFamiliaresRadioGroup);
        siCaida = view.findViewById(R.id.siCaida);
        noCaida = view.findViewById(R.id.noCaida);
        siAvisado = view.findViewById(R.id.siAvisado);
        noAvisado = view.findViewById(R.id.noAvisado);
    }

    /**
     * Método que configura el ViewModel SharedPacientesViewModel mediante la creación de un ViewModelFactory
     * que proporciona instancias de Peticiones Json y ClaseGloabl al ViewModel.
     */
    public void implementaViewModel() {
        ViewModelArgs viewModelArgs = new ViewModelArgs() {
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
        sharedPacientesViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedPacientesViewModel.class);
    }


    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Llama a obtenerListaLugaresYPoblarSpinner() que obtiene la lista de lugares y pobla el spinner.
     * Llama a completaParteCaidas() para setear los datos existentes en la UI y registrar el parte.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        obtenerListaLugaresYPoblarSpinner();
        completaParteCaidas(view);
    }

    /**
     * Llama al método getListaLugaresLiveData del ViewModel, obtiene la lista de lugares donde se ha
     * podido producir la caída, configura el adaptador lugaresAdapter que es un ArrayAdapter y pobla
     * el Spinner.
     */
    public void obtenerListaLugaresYPoblarSpinner() {
        sharedPacientesViewModel.getListaLugaresLiveData().observe(getViewLifecycleOwner(), listaLugares -> {
            ArrayAdapter<String> lugaresAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaLugares);
            lugaresAdapter.setDropDownViewResource(R.layout.my_spinner);
            lugarSpinner.setAdapter(lugaresAdapter);
        });
    }

    /**
     * Método encargado de obtener los datos del paciente seleccionado, rellena la UI, obtiene el lugar
     * seleccionado del spinner, establece la escucha de los RadioGroup para avistado y avisado a familiares
     * y establece la escucha al botón registrarCaidaBtn para subir el parte de caídas a la base de datos.
     *
     * @param view La vista inflada.
     */
    public void completaParteCaidas(View view) {
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), paciente -> {
            rellenaUI(paciente, view);
            lugarSeleccionado();
            seleccionaCaidaPresenciada();
            seleccionaAvisadoFamiliares();
            clickListenerButtonRegistrar(paciente);
        });
    }

    /**
     * Método encargado de configurar el Adaptador parteCaidasAdapter y actualizar la UI con los datos del paciente.
     *
     * @param pacienteActual Paciente seleccionado.
     * @param view           Vista inflada.
     */
    public void rellenaUI(Pacientes pacienteActual, View view) {
        ParteCaidasAdapter parteCaidasAdapter = new ParteCaidasAdapter(pacienteActual, requireContext(), claseGlobal);
        parteCaidasAdapter.rellenaUI(view);
    }

    /**
     * Método que establece la escucha en el Spinner y asigna el nombre del lugar seleccionado a la variable
     * miembro lugarSeleccionado.
     */
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

    /**
     * Método encargado de establecer la escucha del RadioGroup radioGroupVisto y asignar a las variable
     * miembro presenciado si se ha seleccionado Sí o no en la caída presenciada.
     */
    public void seleccionaCaidaPresenciada() {
        radioGroupVisto.setOnCheckedChangeListener((group, checkedId) -> {
            int idRadioSeleccionado = radioGroupVisto.getCheckedRadioButtonId();
            if (idRadioSeleccionado == siCaida.getId()) {
                presenciado = siCaida.getText().toString();
            } else {
                presenciado = noCaida.getText().toString();
            }
        });
    }

    /**
     * Método encargado de establecer la escucha del RadioGroup radioGroupAvisado y asignar a las variable
     * miembro avisado si se ha seleccionado Sí o no en el aviso a familiares.
     */
    public void seleccionaAvisadoFamiliares() {
        radioGroupAvisado.setOnCheckedChangeListener((group, checkedId) -> {
            int idRadioSeleccionado = radioGroupAvisado.getCheckedRadioButtonId();
            if (idRadioSeleccionado == siAvisado.getId()) {
                avisado = siAvisado.getText().toString();
            } else {
                avisado = noAvisado.getText().toString();
            }
        });
    }

    /**
     * Método que establece la escucha del botón registrarCaidaBtn, si se ha pulsado llama a registraElParteDeCaidas().
     *
     * @param pacienteActual Paciente seleccionado.
     */
    public void clickListenerButtonRegistrar(Pacientes pacienteActual) {
        registrarCaidaBtn.setOnClickListener(v -> registraElParteDeCaidas(pacienteActual));
    }

    /**
     * Método encargado de subir el parte de caídas a la base de datos.
     * Asigna a variables el contenido de los EditText, RadioButtons y Spinner.
     * Valida si se han introducido todos los campos, de no ser así muestra un error.
     * Construye la url para la solcitud POST al servidor, crea la solicitud Volley incluyendo los parámetros
     * de la solicitud en getParams() y procesa y maneja la respuesta JSON del servidor.
     *
     * @param paciente Paciente Seleccionado.
     */
    public void registraElParteDeCaidas(Pacientes paciente) {
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

        validacionesDatos(factoresDeRiesgo, causasCaida, circunstanciasCaida, consecuenciasCaida,
                observacionesCaida, caidaPresenciada, avisadoFamilia);

        String url = Constantes.url_part + "crear_parte_caidas.php";

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, url, response -> {
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
        }, error -> {
            Toast.makeText(getContext(), "Ha habido un error al intentar registrar el parte", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new Hashtable<>();

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


    /**
     * Método que realiza las validaciones de los campos antes de ejecutar la solicitud, en caso de no
     * haber completado alguno de los campos muestra un error.
     *
     * @param factoresDeRiesgo    Contenido de factores de riesgo.
     * @param causasCaida         Contenido de causas.
     * @param circunstanciasCaida Contenido de circunstancias.
     * @param consecuenciasCaida  Contenido de consecuencias.
     * @param observacionesCaida  Contenido de observaciones.
     * @param caidaPresenciada    Contenido de caída presenciada.
     * @param avisadoAFamiliares  Contenido de avisado a familiares.
     */
    public void validacionesDatos(String factoresDeRiesgo, String causasCaida, String circunstanciasCaida,
                                  String consecuenciasCaida, String observacionesCaida, String caidaPresenciada,
                                  String avisadoAFamiliares) {
        //Realizamos las validaciones
        if (TextUtils.isEmpty(factoresDeRiesgo)) {
            factoresRiesgo.setError("Los factores de riesgo no pueden estar vacíos");
        }
        if (TextUtils.isEmpty(causasCaida)) {
            causas.setError("Las causas no pueden estar vacías");
        }
        if (TextUtils.isEmpty(circunstanciasCaida)) {
            circunstancias.setError("Las circunstancias no pueden estar vacías");
        }
        if (TextUtils.isEmpty(consecuenciasCaida)) {
            consecuencias.setError("Las consecuencias no pueden estar vacías");
        }
        if (TextUtils.isEmpty(observacionesCaida)) {
            observaciones.setError("Las observaciones no pueden estar vacías");
        }
        if (TextUtils.isEmpty(caidaPresenciada)) {
            Toast.makeText(getContext(), "Tiene que indicar si la caída ha sido presenciada", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(avisadoAFamiliares)) {
            Toast.makeText(getContext(), "Tiene que indicar si se ha avisado a los familiares", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(factoresDeRiesgo) || TextUtils.isEmpty(causasCaida) ||
                TextUtils.isEmpty(circunstanciasCaida) || TextUtils.isEmpty(consecuenciasCaida) ||
                TextUtils.isEmpty(observacionesCaida) || TextUtils.isEmpty(caidaPresenciada) ||
                TextUtils.isEmpty(avisadoAFamiliares)) {
            Toast.makeText(getContext(), "Tiene que completar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Método encargado de formatear la fecha y hora del parte de caídas a yyyy-MM-dd HH:mm:ss para
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
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}