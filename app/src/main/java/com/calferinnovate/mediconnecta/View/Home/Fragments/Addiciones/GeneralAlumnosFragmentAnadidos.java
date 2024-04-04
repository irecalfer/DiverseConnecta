package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.AlumnosFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Fragmento encargado de mostrar los datos personales del paciente.
 */
public class GeneralAlumnosFragmentAnadidos extends Fragment implements IOnBackPressed {


    private ImageView fotoPaciente;
    private Button guardarBtn, nacimientoBtn, ingresoBtn;
    private TextInputEditText nombre, apellidos, nacimientoLugar, dni, cipSns, numSeguridadSocial, numeroHistoriaClinica;
    private Spinner seguroSp, unidadSp, habitacionSp, sexoSp, estadoCivilSp;
    private TextInputEditText fechaNacimiento, fechaIngreso;
    private String unidadSeleccionada, sexoSeleccionado, estadoCivilSeleccionado, seguroSeleccionado, fechaNacimientoSeleccionada,
    fechaIngresoSeleccionada, habitacionSeleccionada;
    private ClaseGlobal claseGlobal;
    private PeticionesJson peticionesJson;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private String imageBase64;
    private int idUnidad, idSeguro;
    private StorageReference storageReference;



    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_sesion.xml.
     * Llama a inicializaVariables() para obtener la fragment_general_pacientes de ClaseGlobal.
     *
     * @param inflater           inflador utilizado para inflar el diseño de la UI.
     * @param container          Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     * @return vista Es la vista inflada del fragmento.
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_pacientes_anadidos, container, false);
        inicializaRecursos(view);
        inicializaViewModel();
        inicializaLauncherSeleccionarImagenes();
        return view;
    }


    /**
     * Método encargado de inicializar variables y enlazar los recursos de la UI.
     *
     * @param view La vista inflada.
     */
    public void inicializaRecursos(View view) {
        storageReference = FirebaseStorage.getInstance().getReference();

        claseGlobal = ClaseGlobal.getInstance();
        fotoPaciente = view.findViewById(R.id.fotoPacienteDetalleNuevo);
        guardarBtn = view.findViewById(R.id.btnGuardar);
        nombre = view.findViewById(R.id.nombrePacienteNuevo);
        apellidos = view.findViewById(R.id.apellidoPacienteNuevo);
        nacimientoLugar = view.findViewById(R.id.lugarNacimientoPacienteNuevo);
        dni = view.findViewById(R.id.dniPacienteNuevo);
        cipSns = view.findViewById(R.id.cipSnsPacienteNuevo);
        numSeguridadSocial = view.findViewById(R.id.numSeguridadSocialPacienteNuevo);
        seguroSp = view.findViewById(R.id.seguroPacienteNuevoSpinner);
        unidadSp = view.findViewById(R.id.spinnerUnidadPacienteNuevo);
        habitacionSp = view.findViewById(R.id.spinnerHabitaciónPacienteNuevo);
        sexoSp = view.findViewById(R.id.spinnerSexoPacienteNuevo);
        estadoCivilSp = view.findViewById(R.id.spinnerEstadoCivilPacienteNuevo);
        fechaNacimiento = view.findViewById(R.id.fechaNacimientoPacienteNuevo);
        fechaIngreso = view.findViewById(R.id.fechaIngresoPacienteNuevo);
        nacimientoBtn = view.findViewById(R.id.btnNacimiento);
        ingresoBtn = view.findViewById(R.id.btnIngreso);
        numeroHistoriaClinica = view.findViewById(R.id.historiaClinica);

    }

    /**
     * Método que configura el ViewModel SharesPacientesViewModel mediante la creación de un ViewModelFactory
     * que proporciona instancias de Peticiones Json y ClaseGloabl al ViewModel.
     */
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

        ViewModelFactory<SharedPacientesViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        sharedPacientesViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedPacientesViewModel.class);
    }

    private void inicializaLauncherSeleccionarImagenes(){
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    Uri selectedImageUri = data.getData();
                    subeImagen(selectedImageUri);
                } else {
                    // Manejar el caso en el que la selección de la imagen fue cancelada
                    Toast.makeText(requireContext(), "Selección de imagen cancelada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Llama a rellenaUIAdaptador() que se encarga de actualizar la vista.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        obtieneSexoPacientes();
        seleccionaFechaNacimiento();
        seleccionaFechaIngreso();
        listenerImageView();

    }



    /*private void obtenerUnidadesYPoblarSpinners() {
        sharedPacientesViewModel.obtieneListaDeUnidades().observe(getViewLifecycleOwner(), new Observer<ArrayList<Unidades>>() {
            @Override
            public void onChanged(ArrayList<Unidades> unidades) {
                ArrayList<String> listaUnidades = new ArrayList<>();
                for (Unidades unidad : unidades) {
                    listaUnidades.add(unidad.getNombreUnidad());
                    unidadesArrayList.add(unidad);
                }
                ArrayAdapter<String> unidadesAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaUnidades);
                unidadesAdapter.setDropDownViewResource(R.layout.my_spinner);
                unidadSp.setAdapter(unidadesAdapter);
                seleccionarUnidad();
            }
        });
    }*/

    /*private void seleccionarUnidad() {
        unidadSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinnerUnidadPacienteNuevo) {
                    unidadSeleccionada = parent.getSelectedItem().toString();
                    obtieneHabitacionesYPoblarSpinner();
                    for(Unidades unidad: unidadesArrayList){
                        if(Objects.equals(unidad.getNombreUnidad(), unidadSeleccionada)){
                            idUnidad = unidad.getId_unidad();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

    /*
    private void obtieneHabitacionesYPoblarSpinner() {
        sharedPacientesViewModel.obtieneHabitacionesUnidades(unidadSeleccionada).observe(getViewLifecycleOwner(), new Observer<ArrayList<Habitaciones>>() {
            @Override
            public void onChanged(ArrayList<Habitaciones> habitaciones) {
                ArrayList<Integer> numHabitacionesLista = new ArrayList<Integer>();
                for (Habitaciones habitacion : habitaciones) {
                    numHabitacionesLista.add(habitacion.getNumHabitacion());
                }
                ArrayAdapter<Integer> habitacionesAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, numHabitacionesLista);
                habitacionesAdapter.setDropDownViewResource(R.layout.my_spinner);
                habitacionSp.setAdapter(habitacionesAdapter);
                habitacionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getId() == R.id.spinnerHabitaciónPacienteNuevo) {
                            habitacionSeleccionada = numHabitacionesLista.get(position).toString();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }*/

    private void obtieneSexoPacientes() {
        sharedPacientesViewModel.obtieneSexoPacientesEnum().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> listaSexo) {
                ArrayAdapter<String> sexoAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaSexo);
                sexoAdapter.setDropDownViewResource(R.layout.my_spinner);
                sexoSp.setAdapter(sexoAdapter);
                sexoSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sexoSeleccionado = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }



    private void seleccionaFechaNacimiento(){
      MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
      materialDateBuilder.setTitleText("Selecciona la fecha de nacimiento");

      final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

      nacimientoBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (getActivity() != null) {
                  materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER_NACIMIENTO");
              }
          }
      });

      materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
          @Override
          public void onPositiveButtonClick(Long selection) {
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
              String fechaFormateada = simpleDateFormat.format(new Date(selection));
              fechaNacimiento.setText(fechaFormateada);
              fechaNacimientoSeleccionada = fechaFormateada;
          }
      });
    }

    private void seleccionaFechaIngreso(){
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Selecciona la fecha de ingreso");

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        ingresoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER_INGRESO");
                }
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fechaFormateada = simpleDateFormat.format(new Date(selection));
                fechaIngreso.setText(fechaFormateada);
                fechaIngresoSeleccionada = fechaFormateada;
            }
        });
    }

    private void listenerImageView(){
        fotoPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleriaFotoPaciente();
            }
        });
    }

    private void abrirGaleriaFotoPaciente(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void subeImagen(Uri file) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference ref = storageRef.child("images/" + file.getLastPathSegment());

        UploadTask uploadTask = ref.putFile(file);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continuamos con el task para obtener la URL de descarga
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String urlFoto = downloadUri.toString();

                    // Ahora puedes enviar la URL al servidor PHP
                    sendImageUrlToServer(urlFoto);
                } else {
                    // Manejar fallos
                    Log.e("FirebaseStorage", "Error al obtener la URL de descarga", task.getException());
                }
            }
        });
    }

    private void sendImageUrlToServer(String url) {
        // Después de guardar la imagen, obtén la URL y llama a sendImageUrlToServer con la URL
        // String imageUrl = "http://192.168.1.136/MediConnecta/imagenes/"+nombreRellenado+apellidosRellenado+".jpg";// Reemplaza con la URL real
        final String imageUrl = url;

        guardarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirAlServidor(imageUrl);
            }
        });
    }

    private void subirAlServidor(String urlFoto) {
        final String nombreRellenado= nombre.getText().toString();
        final String apellidosRellenado = apellidos.getText().toString();
        final String sexo = sexoSeleccionado;
        final String dniRellenado = dni.getText().toString();
        final String lugarNacimientoRellenado = nacimientoLugar.getText().toString();
        final String seguro = String.valueOf(idSeguro);
        final String nacimiento = fechaDateTimeSql(fechaNacimientoSeleccionada);
        final String estadoCivil = estadoCivilSeleccionado;
        final String ingreso = fechaDateTimeSql(fechaIngresoSeleccionada);
        final String unidades = String.valueOf(idUnidad);
        final String habitacion = habitacionSeleccionada;
        final String cipSnsRellenado = cipSns.getText().toString();
        final String numSeguridadSocialRellenado = numSeguridadSocial.getText().toString();
        // Después de guardar la imagen, obtén la URL y llama a sendImageUrlToServer con la URL
        //String imageUrl = "http://192.168.1.136/MediConnecta/imagenes/"+nombreRellenado+apellidosRellenado+".jpg";// Reemplaza con la URL real
        final String fotoPaciente = urlFoto;

        //validacionesDatos();

        String serverUrl = Constantes.url_part+"subir_datos_paciente.php";  // Reemplaza con tu dominio o dirección IP

        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("SubirAlServidor", "Respuesta recibida del servidor: " + response);
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");

                            if (status.equals("success")) {
                                Log.d("SubirAlServidor", "Éxito al cargar datos. URL de la imagen: " + imageBase64);
                                Toast.makeText(requireContext(), "Éxito", Toast.LENGTH_SHORT).show();
                                // Operaciones adicionales con la URL de la imagen en el servidor
                            } else {
                                // Manejar error de carga en el servidor
                                Log.e("SubirAlServidor", "Error al cargar datos. Estado: " + status);
                                Toast.makeText(requireContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("SubirAlServidor", "Error al procesar respuesta del servidor", e);
                            Toast.makeText(requireContext(), "Error al procesar respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar error de red o del servidor
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("foto", fotoPaciente);
                params.put("cip_sns", cipSnsRellenado);
                params.put("nombre", nombreRellenado);
                params.put("apellidos", apellidosRellenado);
                params.put("num_seguridad_social", numSeguridadSocialRellenado);
                params.put("fecha_nacimiento", nacimiento);
                params.put("dni", dniRellenado);
                params.put("estado_civil", estadoCivil);
                params.put("lugar_nacimiento", lugarNacimientoRellenado);
                params.put("sexo", sexo);
                params.put("fecha_ingreso", ingreso);
                params.put("fk_id_unidad", unidades);
                params.put("fk_id_seguro", seguro);
                params.put("fk_num_habitacion", habitacion);
                return params;
            }
        };

        // Agrega la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void validacionesDatos(){

    }

    /**
     * Método encargado de formatear la fecha y hora del parte de caídas a yyyy-MM-dd HH:mm:ss para
     * subirlo a la base de datos
     *
     * @return fecha y hora formateada.
     */
    public String fechaDateTimeSql(String fecha) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(fecha);
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            // Manejar la excepción según sea necesario
            return null;
        }
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