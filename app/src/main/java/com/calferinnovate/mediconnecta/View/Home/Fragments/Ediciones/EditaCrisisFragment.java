package com.calferinnovate.mediconnecta.View.Home.Fragments.Ediciones;

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
import com.calferinnovate.mediconnecta.Adaptadores.Ediciones.EditaCrisisAdapter;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;


public class EditaCrisisFragment extends DialogFragment implements EditaCrisisAdapter.ItemItemSelectedListener{

    public static final String TAG = "EditaCrisisFragment";
    private MaterialToolbar toolbarConfirmar;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;
    private TextInputEditText etFecha, etHora, etIntensidad, etPatrones, etDuracion, etRecuperacion, etDescipcion;
    private TextInputLayout tilFecha, tilHora;
    private AutoCompleteTextView atvLugar, atvTipo;
    private String fechaCrisisSeleccionada, horaCrisisSeleccionada, tipoSeleccionado, lugarSeleccionado;

    @Override
    public void onSpinnerItemSelected(String tipo, String lugar) {
        tipoSeleccionado = tipo;
        lugarSeleccionado = lugar;
    }

    public interface OnCrisisUpdatedListener {
        void onCrisisUpdated();
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
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edita_crisis, container, false);
        inicializaRecursos(view);
        inicializaViewModel();
        setupToolbar();
        toolbarConfirmar.setTitle("Edita Crisis");
        toolbarConfirmar.setTitleTextColor(getResources().getColor(R.color.blanco_de_cinc));
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        seteaDatosCrisis(view);
        seleccionaFecha();
        seleccionaHora();
    }

    public void inicializaRecursos(View view){
        claseGlobal = ClaseGlobal.getInstance();
        toolbarConfirmar = view.findViewById(R.id.toolbarEdicionCrisis);
        etFecha = view.findViewById(R.id.ETFechaCrisisOpciones);
        etHora = view.findViewById(R.id.ETHoraCrisisOpciones);
        tilFecha = view.findViewById(R.id.TilFecha);
        tilHora = view.findViewById(R.id.TilHora);
        etIntensidad = view.findViewById(R.id.ETIntensidadCrisisOpciones);
        etPatrones = view.findViewById(R.id.ETPatronesOpciones);
        etDuracion = view.findViewById(R.id.etDuracionOpciones);
        etRecuperacion = view.findViewById(R.id.etRecuperacionOpciones);
        etDescipcion = view.findViewById(R.id.etDescripcionOpciones);
        atvLugar = view.findViewById(R.id.ATVLugaresCrisisOpciones);
        atvTipo = view.findViewById(R.id.ATVTipoCrisisOpciones);
    }
    public void setupToolbar() {
        toolbarConfirmar.inflateMenu(R.menu.app_menu_confirmar);
        toolbarConfirmar.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_confirmar) {
                    sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
                        @Override
                        public void onChanged(Alumnos alumnos) {
                            sharedAlumnosViewModel.getCrisis().observe(getViewLifecycleOwner(), new Observer<Crisis>() {
                                @Override
                                public void onChanged(Crisis crisis) {
                                    registraEdicionCrisis(crisis, alumnos);
                                }
                            });
                        }
                    });
                }
                return true;
            }
        });
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

    public void seteaDatosCrisis(View view){
        sharedAlumnosViewModel.getListaTotalCrisis().observe(getViewLifecycleOwner(), new Observer<ArrayList<Crisis>>() {
            @Override
            public void onChanged(ArrayList<Crisis> crisisArrayList) {
                sharedAlumnosViewModel.getCrisis().observe(getViewLifecycleOwner(), new Observer<Crisis>() {
                    @Override
                    public void onChanged(Crisis crisis) {
                        EditaCrisisAdapter editaCrisisAdapter = new EditaCrisisAdapter(crisis, crisisArrayList, requireContext());
                        editaCrisisAdapter.setSpinnerItemSelectedListener(EditaCrisisFragment.this);
                        editaCrisisAdapter.rellenaUI(view);
                    }
                });
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
        fechaCrisisSeleccionada = etFecha.getText().toString();
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
        horaCrisisSeleccionada = etHora.getText().toString();
    }

    public void registraEdicionCrisis(Crisis crisis, Alumnos alumno){
        final String fechaCrisis = formateaFechaSQL(crisis);
        final String tipoCrisis = tipoSeleccionado;
        final String lugarCrisis = lugarSeleccionado;
        final String intensidadCrisis = etIntensidad.getText().toString();
        final String patronCrisis = etPatrones.getText().toString();
        final String descripcionCrisis = etDescipcion.getText().toString();
        final String duracionCrisis = etDuracion.getText().toString();
        final String recuperacionCrisis = etRecuperacion.getText().toString();
        final String idCrisis = String.valueOf(crisis.getIdCrisis());

        String url = Constantes.url_part+"actualiza_crisis.php";

        StringRequest stringRequest;

        stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String message = jsonResponse.getString("message");

                if ("Actualización exitosa".equals(message)) {
                    // Después de completar la inserción del Pae, notifica a los observadores
                    Toast.makeText(getContext(), "Seguimiento registrado correctamente", Toast.LENGTH_SHORT).show();
                    Log.d("Respuesta PHP", response);
                    // Llama al método run() en el objeto Runnable para ejecutar registraElPae() después de la inserción
                    //navegaAlNuevoFragmento();
                    sharedAlumnosViewModel.setCrisisUpdated(true);
                    dismiss();
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
                parametros.put("id_crisis", idCrisis.trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    public String formateaFechaSQL(Crisis crisis) {
        if(horaCrisisSeleccionada.isEmpty() && fechaCrisisSeleccionada.isEmpty()){
            return crisis.getFecha();

        } else if(fechaCrisisSeleccionada.isEmpty()){
            DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime fechaEntrada = LocalDateTime.parse(crisis.getFecha(), formatoEntrada);

            String horaFormateada = horaCrisisSeleccionada + ":" + "00";

            return fechaEntrada.format(formatoSalida) + " " + horaFormateada;
        }else if(horaCrisisSeleccionada.isEmpty()){
            DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate fechaEntrada = LocalDate.parse(fechaCrisisSeleccionada, formatoEntrada);

            DateTimeFormatter horaSalida = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter DateEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime horaEntrada = LocalDateTime.parse(crisis.getFecha(), DateEntrada);

            return fechaEntrada.format(formatoSalida) + " " + horaEntrada.format(horaSalida);
        }else{
            DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate fechaEntrada = LocalDate.parse(fechaCrisisSeleccionada, formatoEntrada);

            String horaFormateada = horaCrisisSeleccionada + ":" + "00";

            return fechaEntrada.format(formatoSalida) + " " + horaFormateada;
        }
    }
}