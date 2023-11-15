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
import com.calferinnovate.mediconnecta.ViewModel.SesionViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SesionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private RequestQueue rq;
    private JsonRequest jrq;

    // Creamos los objetos donde almacenaremos el usuario y la contraseña y el botón que nos
    // permitirá el acceso
    private TextInputEditText username;
    private TextInputEditText password;
    private Button btnAcceso;
    private NavController navController;
    private Empleado empleado;
    private ClaseGlobal claseGlobal;
    private ViewModelArgs viewModelArgs;
    private SesionViewModel sesionViewModel;
    private PeticionesJson peticionesJson;
    private boolean compruebaDatos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Necesitamos un objeto de tipo View
        View vista = inflater.inflate(R.layout.fragment_sesion, container, false);
        llamadaAClaseGlobal();

        return vista;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        //Asociamos cada componente del layout con nuestras variables
        asociacionVariableComponente(view);
        // Instanciamos RequestQueue
        rq = Volley.newRequestQueue(getContext());

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

    // Programamos el botón de Acceso con el método iniciarSesion
    private void iniciarSesion() {
        // Guardamos en un string nuestra dirección ip y la direccion en donde está nuestro archivo php.
        // y concatenamos con nuestros parámetros de comunicación.
        String url = Constantes.url_part + "inicio_sesion.php?user=" + username.getText().toString() +
                "&pwd=" + password.getText().toString();


        //Request a string response from the provided url
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        // Creamos un objeto Json de tipo array para recuperar ese array que estamos creando en el archivos php
        // con el formato Json. datos es el nombre del array que hemos declarado en el archivo php.
        try {
            JSONArray jsonArray = response.getJSONArray("empleados");
            // Recorrer los datos del usuario
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Empleado empleadoLogueado = new Empleado(object.optString("user"), object.optString("pwd"),
                        object.optString("nombre"), object.optString("apellidos"), object.optString("nombreCargo"),
                        object.optInt("cod_empleado"), object.optInt("fk_cargo"), object.getString("foto"));
                claseGlobal.setEmpleado(empleadoLogueado);
            }
            Toast.makeText(getContext(), "Permitiendo el acceso al usuario " + username.getText().toString(), Toast.LENGTH_SHORT).show();

            //pasoDeDatosAlSiguienteFragmento();
            navController.navigate(R.id.action_sesionFragment_to_seleccionUnidadFragment);

        } catch (JSONException e) {
            Log.d("Exception", String.valueOf(e));
        }
    }
}