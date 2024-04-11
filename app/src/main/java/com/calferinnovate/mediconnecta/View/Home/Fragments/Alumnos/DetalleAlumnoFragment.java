package com.calferinnovate.mediconnecta.View.Home.Fragments.Alumnos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.Pae;
import com.calferinnovate.mediconnecta.View.Home.Fragments.Addiciones.CrearPaeFragment;
import com.calferinnovate.mediconnecta.View.Home.Fragments.AlumnosFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SharedAlumnosViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * Fragmento que proporciona un TabLayout para desplazarse por los datos del paciente y un fragment container
 * donde se reemplazará los fragmentos de detalle del paciente.
 */
public class DetalleAlumnoFragment extends Fragment implements IOnBackPressed {

    private TabLayout tabLayoutPaciente;
    private SharedAlumnosViewModel sharedAlumnosViewModel;
    private ClaseGlobal claseGlobal;
    private String nombreArea;



    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_detalle_pacientes.xml.
     * Establece el título del Fragmento y configura el ViewModel.
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
        View view = inflater.inflate(R.layout.fragment_detalle_alumnos, container, false);
        asignarValoresAVariables(view);
        sharedAlumnosViewModel = new ViewModelProvider(requireActivity()).get(SharedAlumnosViewModel.class);
        getActivity().setTitle("Detalles Alumno");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listenerTabLayoutDetalle();
    }




    /**
     * Método que inicializa las variables miembro y enlaza con los recursos de la UI.
     * @param view La vista inflada.
     */
    public void asignarValoresAVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        tabLayoutPaciente = view.findViewById(R.id.tabLayoutDetallePacientes);

    }



    /**
     * Método que implementa la escucha de los Tabs del TabLayout, por defecto muestra GeneralPacientesFragment
     * si un tab es seleccionado navega hasta dicho fragmento.
     */
    public void listenerTabLayoutDetalle() {
        if (tabLayoutPaciente.getTabCount() > 0) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new GeneralAlumnosFragment()).commit();
        }

        tabLayoutPaciente.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabSeleccionado = (String) tab.getText();

                assert tabSeleccionado != null;
                navegaAlFragmento(tabSeleccionado);
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * Método que navega al fragmento según el Tab seleccionado. En caso de ser seleccionado el Tab Pautas
     * llama a seleccionarTipoPautasArea().
     * @param tabSeleccionado
     */
    public void navegaAlFragmento(String tabSeleccionado){
        switch (tabSeleccionado) {
            case "General":
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new GeneralAlumnosFragment()).commit();
                break;
            case "PAE":
                sharedAlumnosViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Alumnos>() {
                    @Override
                    public void onChanged(Alumnos alumnos) {
                        sharedAlumnosViewModel.obtienePae(alumnos).observe(getViewLifecycleOwner(), new Observer<ArrayList<Pae>>() {
                            @Override
                            public void onChanged(ArrayList<Pae> paes) {
                                if(paes != null){
                                    if(paes.isEmpty()){
                                        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new CrearPaeFragment()).commit();
                                    }else{
                                        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PaeFragment()).commit();
                                    }
                                }
                            }
                        });
                    }
                });
                break;
            case "Seguimiento":
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new SeguimientoFragment()).commit();
                break;
            case "Crisis":
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new CrisisFragment()).commit();
                break;

        }
    }


    /**
     * Método que agrega la lógica específica del fragmento para manejar el restroceso.
     * Al presionar back volvería al PacientesFragment.
     * @return true si el fragmento manejar el retroceso, false en caso contrario.
     */
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AlumnosFragment()).commit();
        return true;
    }


}

