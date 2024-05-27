package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Adaptadores.Registros.RegistraCrisisAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Crisis;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class RegistroCrisisFragment extends DialogFragment implements RegistraCrisisAdapter.ItemItemSelectedListener {

    private MaterialToolbar toolbarCrisis;
    private ClaseGlobal claseGlobal;
    private TextInputLayout tilFecha, tilHora;
    private TextInputEditText etFecha, etHora, etIntensidad, etPatrones, etDuracion, etRecuperacion, etDescripcion;
    private AutoCompleteTextView atvLugares, atvTipo;
    private PeticionesJson peticionesJson;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private String fechaCrisisSeleccionada, horaCrisisSeleccionada, tipoSeleccionado, lugarSeleccionado;
    private RegistraCrisisAdapter registraCrisisAdapter;
    public static final String TAG= "RegistroCrisisFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_registro_crisis, container, false);
        inicializaRecursos(view);
        inicializaViewModel();
        setupToolbar();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializaSpinners(view);
        seleccionaFecha();
        seleccionaHora();
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.75);
        //THIS WILL MAKE WIDTH 90% OF SCREEN
        //HEIGHT WILL BE WRAP_CONTENT
        getDialog().getWindow().setLayout(width, height);
    }

    public void inicializaRecursos(View view){
        claseGlobal = ClaseGlobal.getInstance();
        tilFecha = view.findViewById(R.id.TilFecha);
        tilHora = view.findViewById(R.id.TilHora);
        etFecha = view.findViewById(R.id.ETFechaCrisisOpciones);
        etHora = view.findViewById(R.id.ETHoraCrisisOpciones);
        toolbarCrisis = view.findViewById(R.id.toolbarOpcionesCrisis);
        atvLugares = view.findViewById(R.id.ATVLugaresCrisisOpciones);
        atvTipo = view.findViewById(R.id.ATVTipoCrisisOpciones);
        etIntensidad = view.findViewById(R.id.ETIntensidadCrisisOpciones);
        etPatrones = view.findViewById(R.id.ETPatronesOpciones);
        etDuracion = view.findViewById(R.id.etDuracionOpciones);
        etRecuperacion = view.findViewById(R.id.etRecuperacionOpciones);
        etDescripcion = view.findViewById(R.id.etDescripcionOpciones);
    }

    public void inicializaViewModel(){
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

    public void setupToolbar(){
        toolbarCrisis.inflateMenu(R.menu.app_menu_confirmar);
        toolbarCrisis.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_confirmar) {
                    sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
                        @Override
                        public void onChanged(Alumnos alumno) {
                            registraLaCrisis(alumno);
                        }
                    });
                }
                return true;
            }
        });
    }

    public void inicializaSpinners(View view){
        sharedAlumnosViewModel.getListaTotalCrisis().observe(getViewLifecycleOwner(), new Observer<ArrayList<Crisis>>() {
            @Override
            public void onChanged(ArrayList<Crisis> crisisArrayList) {
                registraCrisisAdapter = new RegistraCrisisAdapter(claseGlobal, requireActivity(), crisisArrayList);
                registraCrisisAdapter.setSpinnerItemSelectedListener(RegistroCrisisFragment.this);
                registraCrisisAdapter.rellenaUI(view);
            }
        });
    }
    public void seleccionaFecha(){
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Selecciona la fecha en la que se registra la crisis");

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        tilFecha.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER_CRISIS");
                }
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fechaFormateada = simpleDateFormat.format(new Date(selection));
                etFecha.setText(fechaFormateada);
                fechaCrisisSeleccionada = fechaFormateada;
            }
        });
    }

    public void seleccionaHora(){
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setTitleText("Seleccione la hora de la crisis")
                .build();
        tilHora.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePicker.show(requireActivity().getSupportFragmentManager(),"MATERIAL_TIME_PICKER_CRISIS");
            }
        });

        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int horaSeleccionada = materialTimePicker.getHour();
                int minutosSeleccionados = materialTimePicker.getMinute();
                horaCrisisSeleccionada = String.valueOf(horaSeleccionada)+":"+String.valueOf(minutosSeleccionados);
                etHora.setText(horaCrisisSeleccionada);
            }
        });
    }

    public void registraLaCrisis(Alumnos alumnos){
        final String fechaCrisis = formateaFechaSQL();
        final String tipoCrisis = atvTipo.getText().toString();
        final String lugarCrisis = atvLugares.getText().toString();
        final String intensidadCrisis = etIntensidad.getText().toString();
        final String patronCrisis = etPatrones.getText().toString();
        final String descripcionCrisis = etDescripcion.getText().toString();
        final String duracionCrisis = etDuracion.getText().toString();
        final String recuperacionCrisis = etRecuperacion.getText().toString();
        final String idAlumno = String.valueOf(alumnos.getIdAlumno());

        String url = Constantes.url_part+"registra_crisis.php";

        StringRequest stringRequest;

        stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String message = jsonResponse.getString("message");

                if ("Registro exitoso".equals(message)) {
                    // Después de completar la inserción del Pae, notifica a los observadores
                    Toast.makeText(getContext(), "Seguimiento registrado correctamente", Toast.LENGTH_SHORT).show();
                    Log.d("Respuesta PHP", response);
                    // Llama al método run() en el objeto Runnable para ejecutar registraElPae() después de la inserción
                    //navegaAlNuevoFragmento();
                    sharedAlumnosViewModel.getListaCrisis(alumnos).observe(getViewLifecycleOwner(), new Observer<ArrayList<Crisis>>() {
                        @Override
                        public void onChanged(ArrayList<Crisis> crises) {
                            dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Error al insertar el seguimiento", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error en el formato de respuesta Insercción", Toast.LENGTH_SHORT).show();
                Log.e("Error de parseo JSON", e.toString()); // Agrega este log para identificar cualquier error de parseo
            }
        }, error -> {
            Toast.makeText(getContext(), "Ha habido un error al intentar insertar el seguimiento", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
            Log.e("Error Volley", error.toString());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new Hashtable<>();

                parametros.put("fecha", fechaCrisis.trim());
                parametros.put("tipo", tipoCrisis.trim());
                parametros.put("lugar", lugarCrisis.trim());
                parametros.put("intensidad", intensidadCrisis.trim());
                parametros.put("patron", patronCrisis.trim());
                parametros.put("descripcion", descripcionCrisis.trim());
                parametros.put("duracion", duracionCrisis.trim());
                parametros.put("recuperacion", recuperacionCrisis.trim());
                parametros.put("id_alumno", idAlumno.trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    public String formateaFechaSQL(){
        DateTimeFormatter formatoFechaSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoFechaEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate fecha = LocalDate.parse(fechaCrisisSeleccionada, formatoFechaEntrada);
        String horaFormateada = horaCrisisSeleccionada+":"+"00";


        return fecha.format(formatoFechaSalida)+" "+horaFormateada;
    }

    @Override
    public void onSpinnerItemSelected(String tipo, String lugar) {
        tipoSeleccionado = tipo;
        lugarSeleccionado = lugar;
    }
}