package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguimiento;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos.SeguimientoFragment;
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


public class CreaSeguimientoFragment extends DialogFragment {

private SharedAlumnosViewModel sharedAlumnosViewModel;
private PeticionesJson peticionesJson;
private ClaseGlobal claseGlobal;
private TextInputEditText etFecha, etHora, etSeguimiento;
private TextInputLayout tilFecha, tilHora;
private String fechaSeguimientoSeleccionada, horaSeguimientoSeleccionada;
public static final String TAG= "CreaSeguimientoFragment";
private MenuHost menuHost;
private MaterialToolbar toolbarSeguimiento;

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
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_crea_seguimiento, container, false);
        getActivity().setTitle("Crea Seguimiento");
        menuHost = requireActivity();
        inicializaRecursos(view);
        inicializaViewModel();
        setupToolbar();
        //cambiarToolbar();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        seleccionaFecha();
        seleccionaHora();
    }


    public void setupToolbar(){
        toolbarSeguimiento.inflateMenu(R.menu.app_menu_confirmar);
        toolbarSeguimiento.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_confirmar) {
                    sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
                        @Override
                        public void onChanged(Alumnos alumno) {
                            registraElSeguimiento(alumno);
                        }
                    });
                }
                return true;
            }
        });
    }



    public void inicializaRecursos(View view){
        claseGlobal = ClaseGlobal.getInstance();
        etFecha = view.findViewById(R.id.ETFechaSeguimiento);
        etHora = view.findViewById(R.id.ETHoraSeguimiento);
        etSeguimiento = view.findViewById(R.id.ETSeguimientoAlumno);
        tilFecha = view.findViewById(R.id.TVFecha);
        tilHora = view.findViewById(R.id.TVHora);
        toolbarSeguimiento = view.findViewById(R.id.toolbarCreaSeguimiento);
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



    public void seleccionaFecha(){
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
    }

    public void seleccionaHora(){
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setTitleText("Seleccione la hora del seguimiento")
                .build();
        tilHora.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePicker.show(requireActivity().getSupportFragmentManager(),"MATERIAL_TIME_PICKER_SEGUIMIENTO");
            }
        });

        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int horaSeleccionada = materialTimePicker.getHour();
                int minutosSeleccionados = materialTimePicker.getMinute();
                horaSeguimientoSeleccionada = String.valueOf(horaSeleccionada)+":"+String.valueOf(minutosSeleccionados);
                etHora.setText(horaSeguimientoSeleccionada);
            }
        });
    }

    public void registraElSeguimiento(Alumnos alumno){
        final String fechaSeguimiento = formateaFechaSQL();
        final String descripcionSeguimiento = etSeguimiento.getText().toString();
        final String idAlumno = String.valueOf(alumno.getIdAlumno());

        String url = Constantes.url_part+"registra_seguimiento.php";

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
                    sharedAlumnosViewModel.getListaSeguimientos(alumno).observe(getViewLifecycleOwner(), new Observer<ArrayList<Seguimiento>>() {
                        @Override
                        public void onChanged(ArrayList<Seguimiento> seguimientoArrayList) {
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

                parametros.put("id_alumno", idAlumno.trim());
                parametros.put("descripcion", descripcionSeguimiento.trim());
                parametros.put("fecha", fechaSeguimiento.trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    public String formateaFechaSQL(){
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate fechaEntrada = LocalDate.parse(fechaSeguimientoSeleccionada, formatoEntrada);
        String horaFormateada = horaSeguimientoSeleccionada+":"+"00";

        String fechaFormateada = fechaEntrada.format(formatoSalida)+" "+horaFormateada;
        return fechaFormateada;
    }


    public void navegaAlNuevoFragmento(){
        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new SeguimientoFragment()).addToBackStack(null).commit();
    }

}