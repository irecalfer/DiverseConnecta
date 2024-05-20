package com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos;

import android.app.Dialog;
import android.content.Intent;
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
import com.calferinnovate.mediconnecta.Adaptadores.OpcionesSeguimientoAdapter;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.Model.Seguimiento;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Ediciones.EditaSeguimientoDialogFragment;
import com.calferinnovate.mediconnecta.View.Home.HomeActivity;
import com.calferinnovate.mediconnecta.View.Sesion.MainActivity;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;


public class OpcionesSeguimientoDialogFragment extends DialogFragment {

    private MaterialToolbar toolbarSeguimiento;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private ClaseGlobal claseGlobal;
    public static final String TAG = "OpcionesSeguimientoDialogFragment";

    private PeticionesJson peticionesJson;

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
        View view =  inflater.inflate(R.layout.fragment_opciones_seguimiento_dialog, container, false);
        getActivity().setTitle("Crea Seguimiento");
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



    public void setupToolbar(){
        toolbarSeguimiento.inflateMenu(R.menu.app_bar_opciones);
        toolbarSeguimiento.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_borrar) {
                    dialogBorradoSeguimiento();
                }
                if(item.getItemId() == R.id.action_editar){
                    new EditaSeguimientoDialogFragment().show(getChildFragmentManager(), EditaSeguimientoDialogFragment.TAG);
                }
                return true;
            }
        });
    }

    public void inicializaRecursos(View view){
        claseGlobal = ClaseGlobal.getInstance();

        toolbarSeguimiento = view.findViewById(R.id.toolbarEditaSeguimiento);
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

    public void rellenaUI(View view){
        sharedAlumnosViewModel.getSeguimiento().observe(getViewLifecycleOwner(), new Observer<Seguimiento>() {
            @Override
            public void onChanged(Seguimiento seguimiento) {
                OpcionesSeguimientoAdapter opcionesSeguimientoAdapter = new OpcionesSeguimientoAdapter(seguimiento, requireContext());
                opcionesSeguimientoAdapter.rellenaUI(view);
            }
        });
    }

    public void dialogBorradoSeguimiento(){
        final Dialog dialog = new Dialog(requireContext());

        dialog.setContentView(R.layout.dialog_borrado_seguimiento);

        TextView texto = dialog.findViewById(R.id.TVTextoBorrado);
        texto.setText(R.string.borradoSeguimiento);
        TextView siSalir = dialog.findViewById(R.id.textViewSi);
        TextView noSalir = dialog.findViewById(R.id.textViewNo);

        noSalir.setOnClickListener(v -> dialog.dismiss());

        siSalir.setOnClickListener(v -> {
            llamaBBDDBorrado();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void llamaBBDDBorrado() {
        sharedAlumnosViewModel.getSeguimiento().observe(getViewLifecycleOwner(), new Observer<Seguimiento>() {
            @Override
            public void onChanged(Seguimiento seguimiento) {
                final String idSeguimiento = String.valueOf(seguimiento.getIdSeguimiento());

                String url = Constantes.url_part + "borra_seguimiento.php";

                StringRequest stringRequest;

                stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");

                        if ("Borrado exitoso".equals(message)) {
                            sharedAlumnosViewModel.setSeguimientoUpdated(true);
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

                        parametros.put("id_seguimiento", idSeguimiento.trim());
                        return parametros;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                requestQueue.add(stringRequest);

            }
        });

    }
}