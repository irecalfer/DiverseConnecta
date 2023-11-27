package com.calferinnovate.mediconnecta.View.Home.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Adaptadores.NormasAdapter;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.ViewModel.NormasViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgsJson;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

/**
 * NormasEmpresaFragment es un fragmento donde se mostrarán las normas de la empresa.
 */

public class NormasEmpresaFragment extends Fragment implements IOnBackPressed {

    private PeticionesJson peticionesJson;
    private NormasAdapter normasAdapter;
    private NormasViewModel normasViewModel;
    private RecyclerView recyclerNormas;

    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_normas_empresa.xml.
     *
     * @param inflater inflador utilizado para inflar el diseño de la UI.
     * @param container Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     *
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_normas_empresa, container, false);

        enlazaRecursos(view);
        getActivity().setTitle("Normas de la empresa");
        inicializaViewModel();

        return view;
    }

    /**
     * Método que enlaza los recursos de la UI con las variables miembro.
     * @param view La vista inflada.
     */
    public void enlazaRecursos(View view){
        recyclerNormas = view.findViewById(R.id.recyclerViewNormas);
    }

    /**
     * Método que configura el ViewModel NormasViewModel mediante la creación de un ViewModelFactory
     * que proporciona una instancia de Peticiones Json al ViewModel.
     */
    public void inicializaViewModel(){
        ViewModelArgsJson viewModelArgs = () -> peticionesJson = new PeticionesJson(requireContext());

        ViewModelFactory<NormasViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        normasViewModel = new ViewModelProvider(requireActivity(), factory).get(NormasViewModel.class);
    }

    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Llama a llenaRecyclerView() que rellena el RecyclerView.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                           estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llenaRecyclerView();
    }

    /**
     * Llama al método obtieneNormasEmpresa() de NormasViewModel y actualiza el RecyclerView con el
     * adaptador NormasAdapter. Configura la animación para que los items sean expandidos.
     */
    public void llenaRecyclerView(){
        normasViewModel.obtieneNormasEmpresa().observe(getViewLifecycleOwner(), normas -> {
            normasAdapter = new NormasAdapter(normas);

            ((SimpleItemAnimator) recyclerNormas.getItemAnimator()).setSupportsChangeAnimations(false);

            recyclerNormas.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerNormas.setAdapter(normasAdapter);
        });
    }

    /**
     * Método que agrega la lógica específica del fragmento para manejar el restroceso.
     * Al presionar back volvería al HomeFragment.
     * @return true si el fragmento maneja el retroceso, false en caso contrario.
     */
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        return true;
    }
}