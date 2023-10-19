package com.calferinnovate.mediconnecta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SesionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    RequestQueue rq;
    JsonRequest jrq;

    // Creamos los objetos donde almacenaremos el usuario y la contraseña y el botón que nos
    // permitirá el acceso
    private TextInputEditText username;
    private TextInputEditText password;
    private Button btnAcceso;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_sesion, container, false);

        // Necesitamos un objeto de tipo View
        View vista = inflater.inflate(R.layout.fragment_sesion, container, false);
        // Vamos a referenciar estos objetos con el XML
        username = (TextInputEditText) vista.findViewById(R.id.user);
        password = (TextInputEditText) vista.findViewById(R.id.pass);
        btnAcceso = (Button) vista.findViewById(R.id.btnAcceso);

        // Instanciamos RequestQueue
        rq = Volley.newRequestQueue(getContext());

        //Relaccionamos el botón de Acceso con el Listener para que actñue cuando sea presionado
        btnAcceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        return vista;
    }

    // Programamos el botón de Acceso con el método iniciarSesion
    private void iniciarSesion() {
        // Guardamos en un string nuestra dirección ip y la direccion en donde está nuestro archivo php.
        // y concatenamos con nuestros parámetros de comunicación.
        String url = "http://192.168.1.13/MediConnecta/sesion.php?user="+username.getText().toString()+
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
        Empleado empleado = new Empleado();
        // Creamos un objeto Json de tipo array para recuperar ese array que estamos creando en el archivos php
        // con el formato Json. datos es el nombre del array que hemos declarado en el archivo php.

        try {
            JSONArray jsonArray = response.getJSONArray("datos");
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            // Recorrer los datos del usuario
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

               // String user = object.getString("user");
               // String pwd = object.getString("pwd");
               // String names = object.getString("names");



                empleado.setUser(object.optString("user"));
                empleado.setPass(object.optString("pwd"));
                empleado.setNames(object.optString("names"));

                //Log.d("datos", "Nombre= "+ names + "Username= " + user + "Contraseña= " + pwd);

                Toast.makeText(getContext(), "Permitiendo el acceso al usuario "+ username.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d("Exception", String.valueOf(e));
        }




        Intent intencion = new Intent(getContext(), SeleccionUnidadFragment.class);
        startActivity(intencion);


    }
}