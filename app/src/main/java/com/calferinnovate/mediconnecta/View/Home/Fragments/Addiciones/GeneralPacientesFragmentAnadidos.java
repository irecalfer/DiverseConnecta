package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Habitaciones;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguro;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Fragmento encargado de mostrar los datos personales del paciente.
 */
public class GeneralPacientesFragmentAnadidos extends Fragment implements IOnBackPressed {


    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int PERMISSION_REQUEST_CODE = 456;
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
        return view;
    }


    /**
     * Método encargado de inicializar variables y enlazar los recursos de la UI.
     *
     * @param view La vista inflada.
     */
    public void inicializaRecursos(View view) {
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
        fechaIngreso = view.findViewById(R.id.fechaIngresoPaciente);
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
        obtenerListasYPoblarSpinners();
        obtenerUnidadesYPoblarSpinners();
        obtieneSexoPacientes();
        obtieneEstadoCivilPaciente();
        seleccionaFechaNacimiento();
        seleccionaFechaIngreso();
        seleccionaImagen(view);
    }

    private void obtenerListasYPoblarSpinners() {
        sharedPacientesViewModel.obtieneListaSeguros().observe(getViewLifecycleOwner(), new Observer<ArrayList<Seguro>>() {
            @Override
            public void onChanged(ArrayList<Seguro> seguros) {
                ArrayList<String> listaSeguros = new ArrayList<String>();
                for (Seguro seguro : seguros) {
                    String tmp = seguro.getNombreSeguro() + " " + seguro.getTelefono();
                    listaSeguros.add(tmp);
                }
                ArrayAdapter<String> segurosAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaSeguros);
                segurosAdapter.setDropDownViewResource(R.layout.my_spinner);
                seguroSp.setAdapter(segurosAdapter);

                seguroSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getId() == R.id.seguroPacienteNuevoSpinner) {
                            seguroSeleccionado = parent.getSelectedItem().toString();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    private void obtenerUnidadesYPoblarSpinners() {
        sharedPacientesViewModel.obtieneListaDeUnidades().observe(getViewLifecycleOwner(), new Observer<ArrayList<Unidades>>() {
            @Override
            public void onChanged(ArrayList<Unidades> unidades) {
                ArrayList<String> listaUnidades = new ArrayList<>();
                for (Unidades unidad : unidades) {
                    listaUnidades.add(unidad.getNombreUnidad());
                }
                ArrayAdapter<String> unidadesAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaUnidades);
                unidadesAdapter.setDropDownViewResource(R.layout.my_spinner);
                unidadSp.setAdapter(unidadesAdapter);
                seleccionarUnidad();
            }
        });
    }

    private void seleccionarUnidad() {
        unidadSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinnerUnidadPacienteNuevo) {
                    unidadSeleccionada = parent.getSelectedItem().toString();
                    obtieneHabitacionesYPoblarSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

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
    }

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

    private void obtieneEstadoCivilPaciente(){
        sharedPacientesViewModel.obtieneEstadoCivilPacientesEnum().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> listaEstadoCivil) {
                ArrayAdapter<String> estadoCivilAdapter = new ArrayAdapter<>(requireActivity(), R.layout.my_spinner, listaEstadoCivil);
                estadoCivilAdapter.setDropDownViewResource(R.layout.my_spinner);
                estadoCivilSp.setAdapter(estadoCivilAdapter);
                estadoCivilSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        estadoCivilSeleccionado = parent.getItemAtPosition(position).toString();
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
                    materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER_NACIMIENTO");
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

    private void seleccionaImagen(View view){
        fotoPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleriaFotoPaciente(view);
            }
        });
    }
    private void abrirGaleriaFotoPaciente(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                // Convierte la URI de la imagen seleccionada a un Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);

                // Muestra el Bitmap en el ImageView
                fotoPaciente.setImageBitmap(bitmap);
                String imageString = convertBitmapToString(bitmap);

                // Envia la imagen al servidor
                subirAlServidor(imageString);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void subirAlServidor(String imageString) {
        final String nombreRellenado= nombre.getText().toString();
        final String apellidosRellenado = apellidos.getText().toString();
        final String sexo = sexoSeleccionado;
        final String dniRellenado = dni.getText().toString();
        final String lugarNacimientoRellenado = nacimientoLugar.getText().toString();
        final String seguro = seguroSeleccionado;
        final String nacimiento = fechaNacimientoSeleccionada;
        final String estadoCivil = estadoCivilSeleccionado;
        final String ingreso = fechaIngresoSeleccionada;
        final String unidades = unidadSeleccionada;
        final String habitacion = habitacionSeleccionada;
        final String cipSnsRellenado = cipSns.getText().toString();
        final String numSeguridadSocialRellenado = numSeguridadSocial.getText().toString();
        final String foto = imageString;
        final String numHistoriaClinica = numeroHistoriaClinica.getText().toString();

        validacionesDatos();

        String serverUrl = "http://tu-domino.com/upload.php";  // Reemplaza con tu dominio o dirección IP

        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");

                            if (status.equals("success")) {
                                String imageUrl = jsonResponse.getString("url");
                                // Operaciones adicionales con la URL de la imagen en el servidor
                            } else {
                                // Manejar error de carga en el servidor
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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
                params.put("foto", foto);
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
                params.put("fk_num_historia_clinica",numHistoriaClinica);
                return params;
            }
        };

        // Agrega la solicitud a la cola de Volley
        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }

    private void validacionesDatos(){

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