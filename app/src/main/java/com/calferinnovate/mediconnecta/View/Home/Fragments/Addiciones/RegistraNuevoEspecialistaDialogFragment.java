package com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
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
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Crisis;
import com.calferinnovate.mediconnecta.Model.Especialista;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;


public class RegistraNuevoEspecialistaDialogFragment extends DialogFragment {


    private MaterialToolbar toolbarRegistroEspecialista;
    private ClaseGlobal claseGlobal;
    private TextInputEditText tietEspecialidad,tietNombre, tietCentro, tietTelefono,tietEmail;
    private PeticionesJson peticionesJson;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    public static final String TAG= "RegistraNuevoEspecialistaDialogFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.70);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);
        //THIS WILL MAKE WIDTH 90% OF SCREEN
        //HEIGHT WILL BE WRAP_CONTENT
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registra_nuevo_especialista_dialog, container, false);
        inicializaRecursos(view);
        toolbarRegistroEspecialista.setTitle("Registro especialista");
        toolbarRegistroEspecialista.setTitleTextColor(getResources().getColor(R.color.blanco_de_cinc));
        inicializaViewModel();
        setupToolbar();
        return view;
    }

    public void inicializaRecursos(View view){
        claseGlobal = ClaseGlobal.getInstance();
        toolbarRegistroEspecialista = view.findViewById(R.id.materialToolbarRegistraEspecialista);
        tietEspecialidad = view.findViewById(R.id.etEspecialidad);
        tietNombre = view.findViewById(R.id.etNombre);
        tietCentro = view.findViewById(R.id.etCentro);
        tietTelefono = view.findViewById(R.id.etTelefono);
        tietEmail = view.findViewById(R.id.etEmail);
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
        toolbarRegistroEspecialista.inflateMenu(R.menu.app_menu_confirmar);
        toolbarRegistroEspecialista.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_confirmar) {
                    sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
                        @Override
                        public void onChanged(Alumnos alumno) {
                            registraElEspecialista(alumno);
                        }
                    });
                }
                return true;
            }
        });
    }

    public void registraElEspecialista(Alumnos alumnos){
        final String especialidad = tietEspecialidad.getText().toString();
        final String nombre = tietNombre.getText().toString();
        final String centro = tietCentro.getText().toString();
        final String telefono = tietTelefono.getText().toString();
        final String email = tietEmail.getText().toString();
        final String idAlumno = String.valueOf(alumnos.getIdAlumno());

        String url = Constantes.url_part+"registra_especialista.php";

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
                    sharedAlumnosViewModel.getListaEspecialistas(alumnos).observe(getViewLifecycleOwner(), new Observer<ArrayList<Especialista>>() {
                        @Override
                        public void onChanged(ArrayList<Especialista> especialistaArrayList) {
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

                parametros.put("nombre", nombre.trim());
                parametros.put("centro", centro.trim());
                parametros.put("especialidad", especialidad.trim());
                parametros.put("telefono", telefono.trim());
                parametros.put("email", email.trim());
                parametros.put("fk_id_alumno", idAlumno.trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }
}