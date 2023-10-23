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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Empleado;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SesionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    RequestQueue rq;
    JsonRequest jrq;

    // Creamos los objetos donde almacenaremos el usuario y la contraseña y el botón que nos
    // permitirá el acceso
    private TextInputEditText username;
    private TextInputEditText password;
    private Button btnAcceso;
    private NavController navController;
    Empleado empleado;
    ArrayList<Empleado> sesionEmpleado = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_sesion, container, false);

        // Necesitamos un objeto de tipo View
        View vista = inflater.inflate(R.layout.fragment_sesion, container, false);



        return vista;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        // Vamos a referenciar estos objetos con el XML
        username = (TextInputEditText) view.findViewById(R.id.user);
        password = (TextInputEditText) view.findViewById(R.id.pass);
        btnAcceso = (Button) view.findViewById(R.id.btnAcceso);

        // Instanciamos RequestQueue
        rq = Volley.newRequestQueue(getContext());

        //Relaccionamos el botón de Acceso con el Listener para que actñue cuando sea presionado
        btnAcceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();


            }
        });
    }

    // Programamos el botón de Acceso con el método iniciarSesion
    private void iniciarSesion() {
        // Guardamos en un string nuestra dirección ip y la direccion en donde está nuestro archivo php.
        // y concatenamos con nuestros parámetros de comunicación.
        String url = Constantes.url_part+"inicio_sesion.php?user="+username.getText().toString()+
                "&pwd="+password.getText().toString();

        //Request a string response from the provided url
        jrq = new JsonObjectRequest(Request.Method.GET,url, null,this, this);
        rq.add(jrq);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {

        // Vamos a crear un objeto Empleado, para que lo que nos pase Json podamos parsearlo y pasarselo
        // a esos atributos de la clase Empleado.
       empleado = new Empleado();
        // Creamos un objeto Json de tipo array para recuperar ese array que estamos creando en el archivos php
        // con el formato Json. datos es el nombre del array que hemos declarado en el archivo php.

        try {
            JSONArray jsonArray = response.getJSONArray("datos");
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            // Recorrer los datos del usuario
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

               //String user = object.getString("user");
                // String pwd = object.getString("pwd");
               //String names = object.getString("names");



                empleado.setUser(object.optString("user"));
                empleado.setPass(object.optString("pwd"));
                empleado.setCod_empleado(object.optInt("cod_empleado"));


                Log.d("datos", "Nombre= "+ empleado.getCod_empleado() + "Username= " + empleado.getUser().toString() + "Contraseña= " + empleado.getPass().toString());


                Toast.makeText(getContext(), "Permitiendo el acceso al usuario "+ username.getText().toString(), Toast.LENGTH_SHORT).show();

                // Ahora vamos a guardar los datos del empleado con su código
                String url_datos = Constantes.url_part+"datos_empleados.php?cod_empleado="+empleado.getCod_empleado();
                guardar_datos_empleado(url_datos);

                //Ahora guardamos al empleado dentro de la lista de empleados para poder acceder desde el resto de acitivdades.


            }
        } catch (JSONException e) {
            Log.d("Exception", String.valueOf(e));
        }


    }

    //Cogeremos todos los datos del empleado a través de su código de empleado obtenido anteriormente.
    //Guardaremos todos los datos en un objeto de tipo empleado.
    public void guardar_datos_empleado(String url){
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(url, response -> {
            JSONObject jsonObject = null;
            for (int i=0; i<response.length(); i++ ){
                try{
                    jsonObject = response.getJSONObject(i);
                    //empleado.setFoto(jsonObject.optBlob("E.foto"));
                    empleado.setNombre(jsonObject.optString("nombre"));
                    empleado.setApellidos(jsonObject.optString("apellidos"));
                    empleado.setFk_cargo(jsonObject.optInt("fk_cargo"));
                    empleado.setNombreCargo(jsonObject.optString("nombreCargo"));

                    pasoDeDatosAlSiguienteFragmento();
                    Log.d("datos", "Nombre= "+ empleado.getNombre().toString() + "Apellidos= " + empleado.getApellidos().toString() + "Cargo= " + empleado.getFk_cargo());
                    Log.d("Cargo", empleado.getNombreCargo());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, error -> {
            Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
        });
        rq = Volley.newRequestQueue(getContext());
        rq.add(jsonArrayRequest);

    }

    void pasoDeDatosAlSiguienteFragmento(){
        //Para poder pasar objetos de un fragment tenemos que crear nuestr5o bundle y agregarñp como parámetro
        //al método .navigate()
        Bundle bundle = new Bundle();
        bundle.putParcelable("empleados", empleado);
        Log.d("prueba", bundle.toString());
        //Cargamos el nuevo fragmento
        navController.navigate(R.id.action_sesionFragment_to_seleccionUnidadFragment, bundle);
    }


}