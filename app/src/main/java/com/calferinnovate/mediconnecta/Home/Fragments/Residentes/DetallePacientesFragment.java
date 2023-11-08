package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Area;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.clases.Unidades;
import com.google.android.material.tabs.TabLayout;

public class DetallePacientesFragment extends Fragment {

    private TabLayout tabLayoutPaciente;
    private FragmentContainerView vistasDetallePaciente;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private String nombreArea;

    public DetallePacientesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_pacientes, container, false);
        asignaComponentesAVariables(view);
        llamaAClaseGlobal();
        sharedPacientesViewModel = new ViewModelProvider(requireActivity()).get(SharedPacientesViewModel.class);
        nombrePorAsignarParaMoversePorElTabLayout();
        return view;
    }

    public void asignaComponentesAVariables(View view) {
        tabLayoutPaciente = view.findViewById(R.id.tabLayoutDetallePacientes);
        vistasDetallePaciente = view.findViewById(R.id.fragmentContainerDetallePacientes);
    }

    public void llamaAClaseGlobal() {
        claseGlobal = ClaseGlobal.getInstance();
    }

    public void nombrePorAsignarParaMoversePorElTabLayout() {
        tabLayoutPaciente.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabSeleccionado = (String) tab.getText();
                if (tabSeleccionado.equals("General")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new GeneralPacientesFragment()).commit();
                } else if (tabSeleccionado.equals("Contactos")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new ContactoFamiliaresPacienteFragment()).commit();
                } else if (tabSeleccionado.equals("Clínica")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new ClinicaPacientesFragment()).commit();
                } else if (tabSeleccionado.equals("Pautas")) {
                    sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
                        @Override
                        public void onChanged(Pacientes pacientes) {
                            for (Unidades unidades : claseGlobal.getListaUnidades()) {
                                if (unidades.getId_unidad() == pacientes.getFkIdUnidad()) {
                                    for (Area areas : claseGlobal.getListaAreas()) {
                                        if (unidades.getFk_area() == areas.getId_area()) {
                                            nombreArea = areas.getNombre();
                                        }
                                    }
                                }
                            }
                        }
                    });
                    if(nombreArea.equals("Geriatría")){
                        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PautasPacientesGeriatriaFragment()).commit();
                    }else{
                        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PautasSaludMentalPacientesFragment()).commit();
                    }
                }else if(tabSeleccionado.equals("Parte")){
                    getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PartePacientesFragment()).commit();
                }else{
                    getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new ParteCaidasPacientesFragment()).commit();
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Inicialmente, obtén los datos para el tab actual
        if (tabLayoutPaciente.getTabCount() > 0) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new GeneralPacientesFragment()).commit();
        }

    }


}