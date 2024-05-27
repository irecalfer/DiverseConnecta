package com.calferinnovate.mediconnecta.View.Home.Fragments.VisualizacionOpciones;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Adaptadores.Opciones.OpcionesCrisisAdapter;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Crisis;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Ediciones.EditaCrisisFragment;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;


public class OpcionesCrisisFragment extends DialogFragment {

    private MaterialToolbar toolbarOpcionesCrisis;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private ClaseGlobal claseGlobal;
    public static final String TAG = "OpcionesCrisisFragment";
    private PeticionesJson peticionesJson;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_opciones_crisis, container, false);
        inicializaRecursos(view);
        inicializaViewModel();
        setupToolbar();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rellenaUI(view);
    }

    public void inicializaRecursos(View view){
        claseGlobal = ClaseGlobal.getInstance();
        toolbarOpcionesCrisis = view.findViewById(R.id.toolbarOpcionesCrisis);
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
        toolbarOpcionesCrisis.inflateMenu(R.menu.app_bar_opciones);
        toolbarOpcionesCrisis.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_borrar) {
                    dialogBorradoCrisis();
                }
                if(item.getItemId() == R.id.action_editar){
                    new EditaCrisisFragment().show(getChildFragmentManager(), EditaCrisisFragment.TAG);
                }
                return true;
            }
        });
    }

    public void  rellenaUI(View view){
        sharedAlumnosViewModel.getCrisis().observe(getViewLifecycleOwner(), new Observer<Crisis>() {
            @Override
            public void onChanged(Crisis crisis) {
                OpcionesCrisisAdapter opcionesCrisisAdapter = new OpcionesCrisisAdapter(crisis, requireContext());
                opcionesCrisisAdapter.rellenaUI(view);
            }
        });
    }

    public void dialogBorradoCrisis(){
        final Dialog dialog = new Dialog(requireContext());

        dialog.setContentView(R.layout.dialog_borrado_seguimiento);

        TextView texto = dialog.findViewById(R.id.TVTextoBorrado);
        texto.setText(R.string.borradoCrisis);
        TextView siSalir = dialog.findViewById(R.id.textViewSi);
        TextView noSalir = dialog.findViewById(R.id.textViewNo);

        noSalir.setOnClickListener(v -> dialog.dismiss());

        siSalir.setOnClickListener(v -> {
            llamaBBDDBorrado();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void llamaBBDDBorrado(){
        sharedAlumnosViewModel.getCrisis().observe(getViewLifecycleOwner(), new Observer<Crisis>() {
            @Override
            public void onChanged(Crisis crisis) {
                final String idCrisis = String.valueOf(crisis.getIdCrisis());

                String url = Constantes.url_part + "borra_crisis.php";

                StringRequest stringRequest;

                stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");

                        if ("Borrado exitoso".equals(message)) {
                            sharedAlumnosViewModel.setCrisisUpdated(true);
                            Toast.makeText(getContext(), "Seguimiento borrado correctamente", Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Error al borrar el seguimiento", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error en el formato de borrado", Toast.LENGTH_SHORT).show();
                        Log.e("Error de parseo JSON", e.toString()); // Agrega este log para identificar cualquier error de parseo
                    }
                }, error -> {
                    Toast.makeText(getContext(), "Ha habido un error al intentar borrar el seguimiento", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                    Log.e("Error Volley", error.toString());
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> parametros = new Hashtable<>();

                        parametros.put("id_crisis", idCrisis.trim());
                        return parametros;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
            @Override
            public void onChanged(Alumnos alumnos) {
                sharedAlumnosViewModel.getListaCrisis(alumnos).observe(getViewLifecycleOwner(), new Observer<ArrayList<Crisis>>() {
                    @Override
                    public void onChanged(ArrayList<Crisis> crisisArrayList) {
                        dismiss();
                    }
                });
            }
        });
    }
}