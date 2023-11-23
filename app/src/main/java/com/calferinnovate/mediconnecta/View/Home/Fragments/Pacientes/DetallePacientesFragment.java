package com.calferinnovate.mediconnecta.View.Home.Fragments.Pacientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.Area;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.google.android.material.tabs.TabLayout;

public class DetallePacientesFragment extends Fragment implements IOnBackPressed {

    private TabLayout tabLayoutPaciente;
    private FragmentContainerView vistasDetallePaciente;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private String nombreArea;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_pacientes, container, false);
        asignarValoresAVariables(view);
        sharedPacientesViewModel = new ViewModelProvider(requireActivity()).get(SharedPacientesViewModel.class);
        listenerTabLayoutDetalle();
        getActivity().setTitle("Detalles Paciente");

        return view;
    }


    public void asignarValoresAVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        tabLayoutPaciente = view.findViewById(R.id.tabLayoutDetallePacientes);
        vistasDetallePaciente = view.findViewById(R.id.fragmentContainerDetallePacientes);
    }


    public void listenerTabLayoutDetalle() {

        // Inicialmente, obtén los datos para el tab actual
        if (tabLayoutPaciente.getTabCount() > 0) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new GeneralPacientesFragment()).commit();
        }

        tabLayoutPaciente.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabSeleccionado = (String) tab.getText();
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

    public void navegaAlFragmento(String tabSeleccionado){
        if (tabSeleccionado.equals("General")) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new GeneralPacientesFragment()).commit();
        } else if (tabSeleccionado.equals("Contactos")) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new ContactoFamiliaresPacienteFragment()).commit();
        } else if (tabSeleccionado.equals("Clínica")) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new ClinicaPacientesFragment()).commit();
        } else if (tabSeleccionado.equals("Pautas")) {
            seleccionarTipoPautasAreas();
        } else if (tabSeleccionado.equals("Parte")) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PartePacientesFragment()).commit();
        } else if (tabSeleccionado.equals("Parte Caídas")) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new ParteCaidasPacientesFragment()).commit();
        }
    }

    public void seleccionarTipoPautasAreas() {
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
        if (nombreArea.equals("Geriatría")) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PautasPacientesGeriatriaFragment()).commit();
        } else {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PautasSaludMentalPacientesFragment()).commit();
        }
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}