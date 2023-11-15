package com.calferinnovate.mediconnecta.Sesion;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.ViewModel.NormasViewModel;
import com.calferinnovate.mediconnecta.ViewModel.SeleccionUnidadViewModel;
import com.calferinnovate.mediconnecta.ViewModel.SesionViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SesionFragment extends Fragment{

    private RequestQueue rq;
    private JsonRequest jrq;

    // Creamos los objetos donde almacenaremos el usuario y la contraseña y el botón que nos
    // permitirá el acceso
    private TextInputEditText username;
    private TextInputEditText password;
    private Button btnAcceso;
    private NavController navController;
    private ClaseGlobal claseGlobal;
    private ViewModelArgs viewModelArgs;
    private SesionViewModel sesionViewModel;
    private PeticionesJson peticionesJson;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Necesitamos un objeto de tipo View
        View vista = inflater.inflate(R.layout.fragment_sesion, container, false);
        llamadaAClaseGlobal();

        viewModelArgs = new ViewModelArgs() {
            @Override
            public PeticionesJson getPeticionesJson() {
                return peticionesJson = new PeticionesJson(requireContext());
            }

            @Override
            public ClaseGlobal getClaseGlobal() {
                return claseGlobal;
            }
        };

        ViewModelFactory<SesionViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        sesionViewModel = new ViewModelProvider(requireActivity(), factory).get(SesionViewModel.class);


        return vista;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        //Asociamos cada componente del layout con nuestras variables
        asociacionVariableComponente(view);


        verificaEstadoInicioSesion();

        //Relaccionamos el botón de Acceso con el Listener para que actúe cuando sea presionado
        btnAcceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    public void asociacionVariableComponente(View view) {
        // Vamos a referenciar estos objetos con el XML
        username = view.findViewById(R.id.user);
        password = view.findViewById(R.id.pass);
        btnAcceso = view.findViewById(R.id.btnAcceso);

    }

    public void llamadaAClaseGlobal() {
        claseGlobal = ClaseGlobal.getInstance();
    }

    public void verificaEstadoInicioSesion(){
        sesionViewModel.getEmpleadoIniciaSesion().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sesion) {
                if(sesion){
                    Toast.makeText(getContext(), "Permitiendo el acceso al usuario " + username.getText().toString(), Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_sesionFragment_to_seleccionUnidadFragment);
                }else{
                    Toast.makeText(getContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Programamos el botón de Acceso con el método iniciarSesion
    private void iniciarSesion() {
        sesionViewModel.inicioSesion( username.getText().toString(), password.getText().toString());
    }

}