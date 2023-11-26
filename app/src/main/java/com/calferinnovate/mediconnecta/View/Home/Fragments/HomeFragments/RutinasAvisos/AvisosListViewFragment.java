package com.calferinnovate.mediconnecta.View.Home.Fragments.HomeFragments.RutinasAvisos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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


/**
 * Fragmento que muestra la lista de avisos para una fecha dada
 */
public class AvisosListViewFragment extends Fragment {

    private ListView avisosLV;
    private String fecha;
    private AvisosViewModel avisosViewModel;
    private PeticionesJson peticionesJson;
    private ArrayList<String> listaContenidoAvisos = new ArrayList<>();
    private ArrayAdapter<String> avisosAdapter;


    /**
     *  Método llamado cuando se crea la vista del fragmento.
     *  Infla el diseño de la UI desde el archivo XML fragment_avisos_list_view.xml.
     *
     * @param inflater           Inflador utilizado para inflar el diseño de la UI.
     * @param container          Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     * @return Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_avisos_list_view, container, false);

        inicializaVariables(vista);
        inicializaViewModel();
        obtieneFecha();

        return vista;
    }

    /**
     * Enlaza los recursos de la UI con nuestras variables.
     *
     * @param view La vista inflada.
     */
    private void inicializaVariables(View view) {
        avisosLV = view.findViewById(R.id.listViewAvisos);
    }

    /**
     * Método que configura el ViewModel AvisosViewModel mediante la creación de un ViewModelFactory
     * que proporciona una instancia de Peticiones Json al ViewModel.
     */
    private void inicializaViewModel(){
        ViewModelArgsJson viewModelArgs = () -> peticionesJson = new PeticionesJson(requireContext());

        ViewModelFactory<AvisosViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        avisosViewModel = new ViewModelProvider(requireActivity(), factory).get(AvisosViewModel.class);
    }

    /**
     * Obtiene la fecha de los argumentos del fragmento y llama a rellenaListView si la fecha es válida.
     */
    public void obtieneFecha(){
        Bundle bundle = getArguments();
        if(bundle != null){
            fecha = bundle.getString("fecha");

            if(fecha != null && !fecha.isEmpty()){
                rellenaListView();
            }
        }
    }


    /**
     * Método que llama al método obtieneAvisosFecha del AvisosViewModel y le pasa la fecha seleccionada.
     * Obtiene una lista con el contenido de los avisos y actualiza el ListView con un ArrayAdapter.
     */
    public void rellenaListView(){
        avisosViewModel.obtieneAvisosFecha(fecha).observe(getViewLifecycleOwner(), strings -> {
            listaContenidoAvisos = strings;
            avisosAdapter = new ArrayAdapter<>(requireActivity(), R.layout.items_avisos_listview, R.id.itemAvisoListView, listaContenidoAvisos);
            avisosLV.setAdapter(avisosAdapter);
        });
    }

}