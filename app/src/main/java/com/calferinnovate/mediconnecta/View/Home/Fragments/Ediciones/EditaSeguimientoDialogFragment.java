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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Adaptadores.OpcionesSeguimientoAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguimiento;
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
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;


public class EditaSeguimientoDialogFragment extends DialogFragment {

    public interface OnSeguimientoUpdatedListener {
        void onSeguimientoUpdated();
    }

    public static final String TAG = "EditaSeguimientoDialogFragment";
    private MaterialToolbar toolbarConfirmar;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;
    private TextInputEditText etFecha, etHora, etSeguimiento;
    private TextInputLayout tilFecha, tilHora;
    private String fechaSeguimientoSeleccionada, horaSeguimientoSeleccionada;

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
        View view = inflater.inflate(R.layout.fragment_edita_seguimiento_dialog, container, false);
        inicializaRecursos(view);
        inicializaViewModel();
        setupToolbar();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rellenaUI(view);
        seleccionaFecha();
        seleccionaHora();
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
                            sharedAlumnosViewModel.getSeguimiento().observe(getViewLifecycleOwner(), new Observer<Seguimiento>() {
                                @Override
                                public void onChanged(Seguimiento seguimiento) {
                                    registraElSeguimiento(seguimiento, alumnos);
                                }
                            });
                        }
                    });
                }
                return true;
            }
        });
    }

    public void inicializaRecursos(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        etFecha = view.findViewById(R.id.ETFechaSeguimiento);
        etHora = view.findViewById(R.id.ETHoraSeguimiento);
        etSeguimiento = view.findViewById(R.id.ETSeguimientoAlumno);
        tilFecha = view.findViewById(R.id.TilFecha);
        tilHora = view.findViewById(R.id.TilHora);
        toolbarConfirmar = view.findViewById(R.id.toolbarConfirmaSeguimiento);
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

    public void seleccionaFecha() {
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Selecciona la fecha en la que se registra el seguimiento");

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        tilFecha.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    materialDatePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER_SEGUIMIENTO");
                }
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fechaFormateada = simpleDateFormat.format(new Date(selection));
                etFecha.setText(fechaFormateada);
                fechaSeguimientoSeleccionada = fechaFormateada;
            }
        });
        fechaSeguimientoSeleccionada = etFecha.getText().toString();
    }

    public void seleccionaHora() {
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setTitleText("Seleccione la hora del seguimiento")
                .build();
        horaSeguimientoSeleccionada = etHora.getText().toString();
        tilHora.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_TIME_PICKER_SEGUIMIENTO");
            }
        });

        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int horaSeleccionada = materialTimePicker.getHour();
                int minutosSeleccionados = materialTimePicker.getMinute();
                horaSeguimientoSeleccionada = String.valueOf(horaSeleccionada) + ":" + String.valueOf(minutosSeleccionados);
                etHora.setText(horaSeguimientoSeleccionada);
            }
        });
    }


    public void rellenaUI(View view) {
        sharedAlumnosViewModel.getSeguimiento().observe(getViewLifecycleOwner(), new Observer<Seguimiento>() {
            @Override
            public void onChanged(Seguimiento seguimiento) {
                OpcionesSeguimientoAdapter opcionesSeguimientoAdapter = new OpcionesSeguimientoAdapter(seguimiento, requireContext());
                opcionesSeguimientoAdapter.rellenaUI(view);
            }
        });
    }

    public void registraElSeguimiento(Seguimiento seguimiento, Alumnos alumno) {
        final String fechaSeguimiento = formateaFechaSQL(seguimiento);
        final String descripcionSeguimiento = etSeguimiento.getText().toString();
        final String idSeguimiento = String.valueOf(seguimiento.getIdSeguimiento());

        String url = Constantes.url_part + "actualiza_seguimiento.php";

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
                    sharedAlumnosViewModel.setSeguimientoUpdated(true);
                    dismiss();

                    /*sharedAlumnosViewModel.getListaSeguimientos(alumno).observe(getViewLifecycleOwner(), new Observer<ArrayList<Seguimiento>>() {
                        @Override
                        public void onChanged(ArrayList<Seguimiento> seguimientoArrayList) {
                            dismiss();
                        }
                    });*/
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

                parametros.put("id_seguimiento", idSeguimiento.trim());
                parametros.put("descripcion", descripcionSeguimiento.trim());
                parametros.put("fecha", fechaSeguimiento.trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    public String formateaFechaSQL(Seguimiento seguimiento) {
        if(horaSeguimientoSeleccionada.isEmpty() && fechaSeguimientoSeleccionada.isEmpty()){
            return seguimiento.getFechaHora();

        } else if(fechaSeguimientoSeleccionada.isEmpty()){
            DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime fechaEntrada = LocalDateTime.parse(seguimiento.getFechaHora(), formatoEntrada);

            String horaFormateada = horaSeguimientoSeleccionada + ":" + "00";

            return fechaEntrada.format(formatoSalida) + " " + horaFormateada;
        }else if(horaSeguimientoSeleccionada.isEmpty()){
            DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate fechaEntrada = LocalDate.parse(fechaSeguimientoSeleccionada, formatoEntrada);

            DateTimeFormatter horaSalida = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter DateEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime horaEntrada = LocalDateTime.parse(seguimiento.getFechaHora(), DateEntrada);

            return fechaEntrada.format(formatoSalida) + " " + horaEntrada.format(horaSalida);
        }else{
            DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate fechaEntrada = LocalDate.parse(fechaSeguimientoSeleccionada, formatoEntrada);

            String horaFormateada = horaSeguimientoSeleccionada + ":" + "00";

            return fechaEntrada.format(formatoSalida) + " " + horaFormateada;
        }
    }


}