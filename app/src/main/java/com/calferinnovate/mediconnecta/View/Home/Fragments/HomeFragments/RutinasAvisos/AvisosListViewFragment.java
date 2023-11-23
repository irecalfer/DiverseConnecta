package com.calferinnovate.mediconnecta.View.Home.Fragments.HomeFragments.RutinasAvisos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.AvisosViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgsJson;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class AvisosListViewFragment extends Fragment {


    private ListView avisosLV;
    private String fecha;
    private AvisosViewModel avisosViewModel;
    private ViewModelArgsJson viewModelArgs;
    private PeticionesJson peticionesJson;
    private ArrayList<String> listaContenidoAvisos = new ArrayList<>();
    private ArrayAdapter<String> avisosAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_avisos_list_view, container, false);
        //Declaramos los objetos globales y rellenaremos el ListView
        inicializaVariables(vista);
        inicializaViewModel();
        obtieneFecha();
        Log.d("aviso", "HA llegado aquí");
        return vista;
    }

    private void inicializaVariables(View view) {
        avisosLV = view.findViewById(R.id.listViewAvisos);
    }

    private void inicializaViewModel(){
        viewModelArgs = () -> peticionesJson = new PeticionesJson(requireContext());

        ViewModelFactory<AvisosViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        avisosViewModel = new ViewModelProvider(requireActivity(), factory).get(AvisosViewModel.class);
    }

    public void obtieneFecha(){
        Bundle bundle = getArguments();
        if(bundle != null){
            fecha = bundle.getString("fecha");

            if(fecha != null && !fecha.isEmpty()){
                rellenaListView();
            }
        }
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
        avisosViewModel.obtieneAvisosFecha(fecha).observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                listaContenidoAvisos = strings;
                avisosAdapter = new ArrayAdapter<>(getActivity(), R.layout.items_avisos_listview, R.id.itemAvisoListView, listaContenidoAvisos);
                avisosLV.setAdapter(avisosAdapter);
            }
        });
    }

}