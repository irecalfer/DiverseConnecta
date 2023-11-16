package com.calferinnovate.mediconnecta.Home.Fragments.HomeFragments.Rutinas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.Avisos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Fechas;
import com.calferinnovate.mediconnecta.ViewModel.AvisosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.NormasViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AvisosListViewFragment extends Fragment {


    private ListView avisosLV;
    private Avisos avisos;
    private Fechas fechas;
    private String url;
    private ClaseGlobal claseGlobal;
    private AvisosViewModel avisosViewModel;
    private ViewModelArgs viewModelArgs;
    private PeticionesJson peticionesJson;
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
        inicializaVariables(vista);

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

        ViewModelFactory<AvisosViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        avisosViewModel = new ViewModelProvider(requireActivity(), factory).get(AvisosViewModel.class);

        Log.d("aviso", "HA llegado aquí");
        return vista;
    }

    private void inicializaVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        avisos = claseGlobal.getAvisos();
        fechas = claseGlobal.getFechas();
        avisosLV = view.findViewById(R.id.listViewAvisos);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rellenaListView();
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
        avisosViewModel.obtieneAvisosFecha(fechas.getFechaActual()).observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                listaContenidoAvisos = strings;
                avisosAdapter = new ArrayAdapter<>(getActivity(), R.layout.items_avisos_listview, R.id.itemAvisoListView, listaContenidoAvisos);
                avisosLV.setAdapter(avisosAdapter);
            }
        });
    }

}