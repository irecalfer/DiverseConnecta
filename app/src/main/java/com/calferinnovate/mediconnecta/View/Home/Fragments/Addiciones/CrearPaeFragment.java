package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Adaptadores.CreaPaeAdapter;
import com.calferinnovate.mediconnecta.Interfaces.PaeInsertionObserver;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.Aulas;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.ControlSomatometrico;
import com.calferinnovate.mediconnecta.Model.Curso;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.Pae;
import com.calferinnovate.mediconnecta.Model.PaeInsertionObservable;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos.GeneralAlumnosFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos.PaeFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.AlumnosFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class CrearPaeFragment extends Fragment implements IOnBackPressed, CreaPaeAdapter.ItemItemSelectedListener, PaeInsertionObserver {


    private String[] propiedades = {"Peso", "Talla", "IMC", "Percentil", "Tª", "T.A", "F.C", "Sat. O2"};
    private String[] header = {"1er Trimestre", "2º Trimestre", "3er Trimestre"};
    private MenuHost menuHost;
    private ClaseGlobal claseGlobal;
    private TableLayout tablaPae;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private PeticionesJson peticionesJson;
    private CreaPaeAdapter creaPaeAdapter;
    private String cursoSeleccionado, tutorSeleccionado, enfermeraSeleccionada, aulaSeleccionada;
    private Alumnos alumnoSeleccionado;
    private TextInputEditText nombreAlumno, fechaNacimiento, cursoEmision, tutor, enfermera, protesis,
            ortesis, gafas, audifonos, otros, fiebre, alergias, diagnosticoClinico, dietas, medicacion, datosImportantes;
    private String[] cursoActual = new String[2];
    private ArrayList<ControlSomatometrico> controlTrimestres;
    private PaeInsertionObservable paeInsertionObservable = new PaeInsertionObservable();
    private ArrayList<String> cursosActualizadosArrayList = new ArrayList<>();
    private int actualizacionesCompletadas = 0;
    private final int totalActualizaciones = 3; // Total de actualizaciones que se esperan
    private boolean isObservingPae = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crear_pae, container, false);
        getActivity().setTitle("Crea PAE");
        menuHost = requireActivity();

        inicializaViewModel();
        cambiarToolbar();
        inicializaVariables(view);
        enlazaRecursos(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Registra este fragmento como observador
        paeInsertionObservable.addObserver(this);
        rellenaDatosConocidos(view);
        creaTablaSeguimiento(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        paeInsertionObservable.removeObserver(this);
    }

    public void inicializaVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        tablaPae = view.findViewById(R.id.tablaControlPae);
        alumnoSeleccionado = new Alumnos();
        controlTrimestres = new ArrayList<>();
    }

    public void enlazaRecursos(View view) {
        tablaPae = view.findViewById(R.id.tablaControlPae);
        nombreAlumno = view.findViewById(R.id.nombreAlumno);
        fechaNacimiento = view.findViewById(R.id.fechaNacimientoPae);
        cursoEmision = view.findViewById(R.id.cursoEmision);
        tutor = view.findViewById(R.id.tutorAlumno);
        enfermera = view.findViewById(R.id.enfermeraAlumno);
        protesis = view.findViewById(R.id.protesis);
        ortesis = view.findViewById(R.id.ortesis);
        gafas = view.findViewById(R.id.gafas);
        audifonos = view.findViewById(R.id.audifonos);
        otros = view.findViewById(R.id.otros);
        fiebre = view.findViewById(R.id.fiebrePae);
        alergias = view.findViewById(R.id.alergias);
        diagnosticoClinico = view.findViewById(R.id.diagnostico);
        dietas = view.findViewById(R.id.dietas);
        medicacion = view.findViewById(R.id.medicacion);
        datosImportantes = view.findViewById(R.id.datosImportantes);
    }

    public void cambiarToolbar() {
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.app_menu_opciones_pae_editar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_confirmar_pae) {
                    registraElPae();
                    return true;
                }
                return false;
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    public void inicializaViewModel() {
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

        ViewModelFactory<SharedAlumnosViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        sharedAlumnosViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedAlumnosViewModel.class);
    }

    public void rellenaDatosConocidos(View view) {
        sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
            @Override
            public void onChanged(Alumnos alumnos) {
                alumnoSeleccionado = alumnos;
                // Limpia los datos del PAE al cambiar de alumno
                seteaDatos(alumnos, view);
            }
        });

    }

    public void seteaDatos(Alumnos alumno, View view) {

        sharedAlumnosViewModel.obtieneListaCursos().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> cursos) {
                cursosActualizadosArrayList = cursos;
                creaPaeAdapter = new CreaPaeAdapter(alumno, cursosActualizadosArrayList, claseGlobal, requireActivity(), ClaseGlobal.getInstance().getListaAulas());
                creaPaeAdapter.setSpinnerItemSelectedListener(CrearPaeFragment.this); // Pasa el listener al adaptador
                creaPaeAdapter.rellenaUI(view);
            }
        });

    }

    public void creaTablaSeguimiento(View view) {
        // Primero dibujar el encabezado; esto es poner "TALLAS" y a la derecha todas las tallas
        TableRow fila = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(lp);

        // Establecer el fondo de color del encabezado
        fila.setBackgroundColor(Color.parseColor("#006B58")); // Cambia el color según tus preferencias
// Borde
        //fila.setBackgroundResource(R.drawable.borde_tabla);
//El elemento de la izquierda
        TextView tv = new TextView(getActivity());
        tv.setTypeface(null, Typeface.BOLD);
        tv.setText("Control");
        tv.setTextSize(24);
        tv.setTextColor(Color.WHITE); // Texto en blanco

        fila.addView(tv);

// Ahora agregar las tallas
        for (int x = 0; x < header.length; x++) {
            TextView tvTalla = new TextView(getActivity());
            tvTalla.setText(header[x]);
            tvTalla.setTextSize(24);

            tvTalla.setTextColor(Color.WHITE); // Texto en blanco

            fila.addView(tvTalla);
        }
// Finalmente agregar la fila en la primera posición
        tablaPae.addView(fila, 0);
        // Rellenar la tabla con los datos de cada propiedad para cada trimestre
        rellenaFilas(view, lp);

    }

    public void rellenaFilas(View view, TableRow.LayoutParams lp) {
        // Crear una fila para cada propiedad
        for (String propiedad : propiedades) {
            TableRow filaPropiedad = new TableRow(getActivity());
            filaPropiedad.setLayoutParams(lp);
            // Establecer el fondo de color de la fila


            // Agregar el nombre de la propiedad como encabezado
            TextView textViewPropiedad = new TextView(getActivity());
            textViewPropiedad.setText(propiedad);
            textViewPropiedad.setTypeface(null, Typeface.BOLD);
            textViewPropiedad.setTextSize(24); // Tamaño de letra grande
            textViewPropiedad.setTextColor(Color.WHITE); // Texto en blanco
            textViewPropiedad.setBackgroundColor(Color.parseColor("#006B58")); // Fondo verde oscuro

            filaPropiedad.addView(textViewPropiedad);

            // Agregar los datos correspondientes para cada trimestre
            for (int i = 0; i < 3; i++) {
                EditText editText = new EditText(getActivity());
                //String dato = obtenerDatoPropiedad(control, propiedad);
                //editText.setText(dato);
                editText.setTextSize(20);
                filaPropiedad.addView(editText);
            }

            // Agregar la fila a la tabla
            tablaPae.addView(filaPropiedad);
        }
    }

    public ArrayList<ControlSomatometrico> obtenerDatosControl() {
        ArrayList<ControlSomatometrico> control = new ArrayList<>();

        // Itera sobre las columnas, comenzando desde la segunda columna
        int numColumnas = 3;
        for (int j = 1; j <= numColumnas; j++) {
            // Crea un objeto ControlSomatometrico para cada columna
            ControlSomatometrico controlSomatometrico = new ControlSomatometrico();
            controlSomatometrico.setFkTrimestre(j);
            // Itera sobre las filas
            int numFilas = tablaPae.getChildCount();
            for (int i = 1; i < numFilas; i++) { // Comienza desde 1 para omitir la primera fila (encabezado)
                View filaView = tablaPae.getChildAt(i);
                if (filaView instanceof TableRow) {
                    TableRow fila = (TableRow) filaView;
                    // Obtén el EditText correspondiente a esta columna y fila
                    EditText editText = (EditText) fila.getChildAt(j);// No es necesario sumar 1 ya que hemos omitido la primera columna
                    String valorStr = editText.getText().toString();
                    // Verifica si el valor obtenido es nulo
                    if (valorStr == null || valorStr.isEmpty()) {
                        // Si es nulo, asigna un valor vacío ("")
                        valorStr = "";
                    }
                    // Asigna el valor al objeto ControlSomatometrico dependiendo de la columna actual
                    switch (i) {
                        case 1: // Peso
                            controlSomatometrico.setPeso(valorStr.isEmpty() ? 0.0 : Double.parseDouble(valorStr));
                            break;
                        case 2: // Talla
                            controlSomatometrico.setTalla(valorStr);
                            break;
                        case 3: // IMC
                            controlSomatometrico.setImc(valorStr.isEmpty() ? 0.0 : Double.parseDouble(valorStr));
                            break;
                        case 4: // Percentil
                            controlSomatometrico.setPercentil(valorStr.isEmpty() ? 0.0 : Double.parseDouble(valorStr));
                            break;
                        case 5: // Temperatura
                            controlSomatometrico.setTemperatura(valorStr.isEmpty() ? 0.0 : Double.parseDouble(valorStr));
                            break;
                        case 6: // T.A
                            controlSomatometrico.setTensionArterial(valorStr);
                            break;
                        case 7: // F.C
                            controlSomatometrico.setFrecuenciaCardiaca(valorStr.isEmpty() ? 0 : Integer.parseInt(valorStr));
                            break;
                        case 8: // Sat. O2
                            controlSomatometrico.setSaturacionO2(valorStr.isEmpty() ? 0 : Integer.parseInt(valorStr));
                            break;
                    }
                }
            }
            // Agrega el objeto ControlSomatometrico al ArrayList
            control.add(controlSomatometrico);
        }

        return control;
    }

    public void registraElPae() {

        cursoActual = obtieneIdCursoSeleccionado();
        final String cursoEmisionInicio = cursoActual[0];
        final String cursoEmisionFin = cursoActual[1];
        final int tutor = obtieneIdTutor();
        final int enfermera = obtieneIdEnfermera();
        final int idAula = obtieneIdAula();
        final String nombreAulaNueva = aulaSeleccionada;
        final String nombreTutor = tutorSeleccionado;
        final String nombreEnfermera = enfermeraSeleccionada;

        if (idAula == 0 || tutor == 0 || enfermera == 0 || (!cursosActualizadosArrayList.contains(cursoEmisionInicio+"/"+cursoEmisionFin))) {
            verificaCamposEnBBDD(cursoEmisionInicio, cursoEmisionFin,
                    nombreAulaNueva, nombreTutor, nombreEnfermera);

        } else {
            insertaRegistroPae();
        }

         /*validacionesDatos(factoresDeRiesgo, causasCaida, circunstanciasCaida, consecuenciasCaida,
                observacionesCaida, caidaPresenciada, avisadoFamilia);*/
    }

    private void verificaCamposEnBBDD(String cursoInicio, String cursoFin, String nombreAula,
                                      String nombreTutor, String nombreEnfermera) {
        String url = Constantes.url_part + "inserta_nuevos_registros.php";


        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.d("Respuesta del servidor", response);
                JSONObject jsonResponse = new JSONObject(response);
                String message = jsonResponse.getString("message");

                if ("Actualización exitosa".equals(message)) {
                    // Después de completar la inserción del Pae, notifica a los observadores
                    Toast.makeText(getContext(), "Nuevos registros insertados correctamente", Toast.LENGTH_SHORT).show();
                    Log.d("Respuesta PHP", response);
                    // Llama al método run() en el objeto Runnable para ejecutar registraElPae() después de la inserción
                    actualizaViewModels();
                    callbackParaRegistraElPae();
                } else {
                    Toast.makeText(getContext(), "Error al insertar el PAE", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error en el formato de respuesta Insercción", Toast.LENGTH_SHORT).show();
                Log.e("Error de parseo JSON", e.toString()); // Agrega este log para identificar cualquier error de parseo
            }
        }, error -> {
            Toast.makeText(getContext(), "Ha habido un error al intentar insertar el PAE", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
            Log.e("Error Volley", error.toString());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new Hashtable<>();

                parametros.put("cursoEmisionInicio", cursoInicio.trim());
                parametros.put("cursoEmisionFin", cursoFin.trim());
                parametros.put("nombreAulaNueva", nombreAula.trim());
                parametros.put("nombreTutor", nombreTutor.trim());
                parametros.put("nombreEnfermera", nombreEnfermera.trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);


    }

    private void actualizaViewModels() {
        sharedAlumnosViewModel.obtieneListaAulas().observe(getViewLifecycleOwner(), new Observer<ArrayList<Aulas>>() {
            @Override
            public void onChanged(ArrayList<Aulas> aulas) {
                claseGlobal.setListaAulas(aulas);
            }
        });
        sharedAlumnosViewModel.obtieneListaCursos().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> cursos) {
                cursosActualizadosArrayList = cursos;
            }
        });
        sharedAlumnosViewModel.recargaListaEmpleados().observe(getViewLifecycleOwner(), new Observer<ArrayList<Empleado>>() {
            @Override
            public void onChanged(ArrayList<Empleado> empleados) {
                claseGlobal.setListaEmpleados(empleados);

            }
        });
    }

    // Método de retorno de llamada para llamar a registraElPae() cuando la operación asincrónica se complete con éxito
    private void callbackParaRegistraElPae() {
        // Llama a registraElPae() después de que la operación asincrónica se complete con éxito
        registraElPae();
    }

    private void insertaRegistroPae() {
        cursoActual = obtieneIdCursoSeleccionado();
        final String cursoEmisionInicio = cursoActual[0];
        final String cursoEmisionFin = cursoActual[1];
        final int tutor = obtieneIdTutor();
        final int enfermera = obtieneIdEnfermera();
        final String protesisPae = protesis.getText().toString();
        final String ortesisPae = ortesis.getText().toString();
        final String gafasPae = gafas.getText().toString();
        final String audifonosPae = audifonos.getText().toString();
        final String otrosPae = otros.getText().toString();
        final String fiebrePae = fiebre.getText().toString();
        final String alergiasPae = alergias.getText().toString();
        final String diagnostico = diagnosticoClinico.getText().toString();
        final String dietasPae = dietas.getText().toString();
        final String medicacionPae = medicacion.getText().toString();
        final String datos = datosImportantes.getText().toString();
        final int id_alumno = alumnoSeleccionado.getIdAlumno();
        final int enfermeraModifica = claseGlobal.getEmpleado().getCod_empleado();
        final String tiempoModifica = fechaDateTimeSql();
        final int idAula = obtieneIdAula();

        String url = Constantes.url_part + "crea_el_pae.php";

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String message = jsonResponse.getString("message");

                if ("Registro exitoso".equals(message)) {
                    // Después de completar la inserción del Pae, notifica a los observadores
                    paeInsertionObservable.notifyPaeInserted();
                    Toast.makeText(getContext(), "Pae registrado correctamente", Toast.LENGTH_SHORT).show();
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

                parametros.put("cursoEmisionInicio", cursoEmisionInicio.trim());
                parametros.put("cursoEmisionFin", cursoEmisionFin.trim());
                parametros.put("tutor", String.valueOf(tutor).trim());
                parametros.put("enfermera", String.valueOf(enfermera).trim());
                parametros.put("protesisPae", protesisPae.trim());
                parametros.put("ortesisPae", ortesisPae.trim());
                parametros.put("gafasPae", gafasPae.trim());
                parametros.put("audifonosPaae", audifonosPae.trim());
                parametros.put("otrosPae", otrosPae.trim());
                parametros.put("fiebrePae", fiebrePae.trim());
                parametros.put("alergiasPae", alergiasPae.trim());
                parametros.put("diagnostico", diagnostico.trim());
                parametros.put("dietasPae", dietasPae.trim());
                parametros.put("medicacionPae", medicacionPae.trim());
                parametros.put("datos", datos.trim());
                parametros.put("id_alumno", String.valueOf(id_alumno).trim());
                parametros.put("id_enfermera_modifica", String.valueOf(enfermeraModifica).trim());
                parametros.put("tiempo_modificado", tiempoModifica.trim());
                parametros.put("fk_id_aula", String.valueOf(idAula).trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);


    }


    public String fechaDateTimeSql() {
        DateTimeFormatter fechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fechaHora.format(LocalDateTime.now());
    }

    public void observaPae() {
        //Observamos el pae una vez que se ha subido a la base de datos
        if (!isObservingPae) {
            sharedAlumnosViewModel.obtienePae(alumnoSeleccionado).observe(getViewLifecycleOwner(), new Observer<ArrayList<Pae>>() {
                @Override
                public void onChanged(ArrayList<Pae> paeArrayList) {
                    if (paeArrayList != null && !paeArrayList.isEmpty()) {
                        Pae pae = paeArrayList.get(0);
                        if (pae != null) {
                            isObservingPae = true;
                            controlTrimestres = obtenerDatosControl();
                            registraElControl(controlTrimestres, pae);
                        } else {
                            Toast.makeText(requireContext(), "El PAE todavía no se ha subido", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "La lista está vacía", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    public String[] obtieneIdCursoSeleccionado() {
        int tamañoCurso = cursoSeleccionado.length();
        int separadorCurso = cursoSeleccionado.indexOf("/");
        String cursoInicio, cursoFin;
        cursoInicio = cursoSeleccionado.substring(0, separadorCurso);
        cursoFin = cursoSeleccionado.substring(separadorCurso + 1, tamañoCurso);
        cursoActual[0] = cursoInicio;
        cursoActual[1] = cursoFin;
        return cursoActual;
    }

    public int obtieneIdTutor() {
        int codigo = 0;
        for (Empleado e : claseGlobal.getListaEmpleados()) {
            if (e.getNombre().equals(tutorSeleccionado)) {
                codigo = e.getCod_empleado();
                return codigo;
            }
        }
        return codigo;
    }

    public int obtieneIdEnfermera() {
        int codigo = 0;
        for (Empleado e : claseGlobal.getListaEmpleados()) {
            if (e.getNombre().equals(enfermeraSeleccionada)) {
                codigo = e.getCod_empleado();
                return codigo;
            }
        }
        return codigo;
    }

    private int obtieneIdAula(){
        int codigo = 0;
        for(Aulas a: claseGlobal.getListaAulas()){
            if(a.getNombreAula().equals(aulaSeleccionada)){
                codigo = a.getIdAula();
                return codigo;
            }
        }
        return codigo;
    }

    public void registraElControl(ArrayList<ControlSomatometrico> controlTrimestres, Pae pae) {
        // Itera sobre los objetos ControlSomatometrico
        for (ControlSomatometrico controlSomatometrico : controlTrimestres) {
            enviarControlSomatometricoABaseDeDatos(controlSomatometrico, controlSomatometrico.getFkTrimestre(), pae);
        }
        boolean actualizado = true;
    }

    public void enviarControlSomatometricoABaseDeDatos(ControlSomatometrico controlSomatometrico, int numeroTrimestre, Pae pae) {
        String url = Constantes.url_part + "crea_control_somatometrico.php";

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String message = jsonResponse.getString("message");

                if ("Registro exitoso".equals(message)) {
                    Toast.makeText(getContext(), "PAE registrado correctamente", Toast.LENGTH_SHORT).show();
                    actualizacionesCompletadas++;

                    // Verificar si todas las actualizaciones han finalizado
                    if (actualizacionesCompletadas == totalActualizaciones) {
                        // Todas las actualizaciones han finalizado, iniciar la transacción al nuevo fragmento
                        iniciarTransaccionNuevoFragmento();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al registrar el PAE", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(getContext(), "Ha habido un error al intentar registrar el PAE", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Agrega cada propiedad del objeto ControlSomatometrico como un parámetro en la solicitud POST
                params.put("peso", String.valueOf(controlSomatometrico.getPeso()));
                params.put("talla", controlSomatometrico.getTalla());
                params.put("imc", String.valueOf(controlSomatometrico.getImc()));
                params.put("percentil", String.valueOf(controlSomatometrico.getPercentil()));
                params.put("temperatura", String.valueOf(controlSomatometrico.getTemperatura()));
                params.put("tension_arterial", controlSomatometrico.getTensionArterial());
                params.put("frecuencia_cardiaca", String.valueOf(controlSomatometrico.getFrecuenciaCardiaca()));
                params.put("saturacion_o2", String.valueOf(controlSomatometrico.getSaturacionO2()));
                // Agrega más propiedades según sea necesario
                params.put("fk_trimestre", String.valueOf(numeroTrimestre)); // Incluye el número de trimestre como un parámetro
                params.put("fk_id_pae", String.valueOf(pae.getIdPae())); // Incluye el número de trimestre como un parámetro
                return params;
            }
        };

        // Agrega la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AlumnosFragment()).commit();
        return true;
    }


    @Override
    public void onSpinnerItemSelected(String curso, String tutor, String enfermera, String aula) {
        // Aquí recibes los elementos seleccionados de los spinners
        cursoSeleccionado = curso;
        tutorSeleccionado = tutor;
        enfermeraSeleccionada = enfermera;
        aulaSeleccionada = aula;
        // Haz lo que necesites con los elementos seleccionados
    }

    @Override
    public void onPaeInserted() {
        observaPae();
    }

    public void iniciarTransaccionNuevoFragmento() {
        sharedAlumnosViewModel.limpiarDatos();
        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PaeFragment()).addToBackStack(null).commit();
    }
}