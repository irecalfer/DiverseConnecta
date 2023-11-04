package com.calferinnovate.mediconnecta.clases.PeticionesHTTP;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HiloPeticiones {
    private static  HiloPeticiones instance;
    private RequestQueue requestQueue;

    private HiloPeticiones(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized HiloPeticiones getInstance(Context context){
        if (instance == null){
            instance = new HiloPeticiones(context);
        }
        return instance;
    }

    public <T> void añadirALaColaDePeticiones(Request<T> request){
        requestQueue.add(request);
    }
}
