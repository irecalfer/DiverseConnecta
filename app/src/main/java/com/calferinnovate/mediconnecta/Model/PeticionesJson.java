package com.calferinnovate.mediconnecta.Model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Clase para gestionar las peticiones HTTP utilizando la biblioteca Volley.
 */
public class PeticionesJson {
    private static PeticionesJson instance;
    private RequestQueue requestQueue;

    /**
     * Constructor que obtiene el contexto de la clase global
     * @param context Context es el contexto de la aplicación
     */
    public PeticionesJson(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * Método utilizado para obtener una única instancia de la clase. Es estático para que pueda ser
     * llamado sin crear una instancia previa de la clase. Utiliza un patrón de doble comprobación
     * para garintzar que la instancia única se cree solo cuando sea necesario y maneje la concurrencia
     * correctamente
     * @param context Es el contexto de la aplicación.
     * @return instance Instance es la instancia de PeticionesJSON
     */
    public static synchronized PeticionesJson getInstance(Context context) {
        if (instance == null) {
            instance = new PeticionesJson(context);
        }
        return instance;
    }

    /**
     * Método utilizado para realizar las peticiones HTTP a través de un JsonObjectRequest
     * @param url Es la url donde tenemos nuestro PHP
     * @param listener Es el listener que maneja las respuestas y los errores
     */
    public void getJsonObjectRequest(String url, MyJsonObjectResponseListener listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    if (listener != null) {
                        listener.onResponse(response);
                    }
                },
                error -> {
                    if (listener != null) {
                        listener.onErrorResponse(error);
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Interface que actúa como listener para manejar las respuestas JSON
     */
    public interface MyJsonObjectResponseListener {
        void onResponse(JSONObject response);
        void onErrorResponse(VolleyError error);
    }
}
