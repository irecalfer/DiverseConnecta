package com.calferinnovate.mediconnecta.Home.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Adaptadores.AvisosAdapter;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Avisos;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;


public class listaAvisosFragment extends Fragment {

    private RecyclerView listAvisosRV;
    private ArrayList<String> listaAvisos = new ArrayList<>();
    private AvisosAdapter avisosAdapter;
    private Avisos avisos = ((ClaseGlobal) getActivity().getApplicationContext()).avisos;;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

/*
    public static listaAvisosFragment newInstance(String param){
        listaAvisosFragment fragment = new listaAvisosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_avisos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestQueue = Volley.newRequestQueue(getContext());
        listAvisosRV = (RecyclerView) getActivity().findViewById(R.id.recyclerViewAvisos);
/*
        // Nuestro RecyclerView usará un linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listAvisosRV.setLayoutManager(layoutManager);

        // Obtenemos la lista de avisos de la base de datos
        obtenerAvisos();
        // Asociamos un adapter
        //avisosAdapter = new AvisosAdapter();
        listAvisosRV.setAdapter(avisosAdapter);*/
    }

    private void obtenerAvisos() {
        //String url = Constantes.url_part+"avisos.php?fecha_aviso="+fecha_avisos;
    }




}