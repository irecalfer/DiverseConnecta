package com.calferinnovate.mediconnecta.PeticionesHTTP;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class PeticionesJson {
    private static PeticionesJson instance;
    private RequestQueue requestQueue;

    public PeticionesJson(Context context) {
        // Obtén el contexto de la aplicación desde ClaseGlobal
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized PeticionesJson getInstance(Context context) {
        if (instance == null) {
            instance = new PeticionesJson(context);
        }
        return instance;
    }

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

    public interface MyJsonObjectResponseListener {
        void onResponse(JSONObject response);
        void onErrorResponse(VolleyError error);
    }
}
