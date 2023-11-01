package com.calferinnovate.mediconnecta.Home.Fragments.HomeFragments.Rutinas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Avisos;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Fechas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AvisosListViewFragment extends Fragment {


    private ListView avisosLV;
    private Avisos avisos;
    private Fechas fechas;
    private String url;
    private ArrayList<String> listaContenidoAvisos = new ArrayList<>();
    private ArrayAdapter<String> avisosAdapter;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_avisos_list_view, container, false);
        //Declaramos los objetos globales y rellenaremos el ListView
        llamadaAObjetosGlobales(vista);
        rellenaListView();
        Log.d("aviso", "HA llegado aquí");
        return vista;
    }

    private void llamadaAObjetosGlobales(View view) {
        avisos = ((ClaseGlobal) getActivity().getApplicationContext()).getAvisos();
        fechas = ((ClaseGlobal) getActivity().getApplicationContext()).getFechas();
        avisosLV = (ListView) view.findViewById(R.id.listViewAvisos);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    //Accederemos a nuestro avisos.php e iremos cogiendo cada fila y la guardaremos en el objeto avisos
    //Guardaremos el contenido de los mensajes en una lista, ya que es lo único que deseamos que se
    //muestre en el listView.
    //A continuación llamamos a nuestro adaptador, donde le pasaremos el layout donde se encuentra el TextView
    //que será lo que insertemos en el list View, pasamos también el id del TextView y por último la lista.
    //Seteamos nuestro adaptador en el ListView
    //Y añadiamos a la cola nuestro json request.
    //Tras terminar podremos ver como en HomeFragment se encuentra el lisview completado con los avisos
    //correspondientes a la fecha elegida.
    public void rellenaListView(){
        url = Constantes.url_part+"avisos.php?fecha_aviso="+Constantes.comillas+fechas.getFechaActual()+Constantes.comillas;

        requestQueue = Volley.newRequestQueue(getContext());

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray = response.getJSONArray("avisos");
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //avisos = new Avisos(jsonObject.optInt("num_aviso"), jsonObject.optString(("fecha_aviso")), jsonObject.optString("contenido"));
                        avisos.setNum_aviso(jsonObject.optInt("num_aviso"));
                        avisos.setFecha_aviso(jsonObject.optString("fecha_aviso"));
                        avisos.setContenido(jsonObject.optString("contenido"));
                        listaContenidoAvisos.add(avisos.getContenido());
                    }
                    avisosAdapter = new ArrayAdapter<>(getActivity(), R.layout.items_avisos_listview, R.id.itemAvisoListView, listaContenidoAvisos);
                    avisosLV.setAdapter(avisosAdapter);
                    Log.d("aviso", avisos.getContenido());
                }catch(JSONException jsonException){
                    throw new RuntimeException(jsonException);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.toString();
                Log.d("error", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}