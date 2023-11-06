package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.calferinnovate.mediconnecta.R;
import com.google.android.material.tabs.TabLayout;

public class DetallePacientesFragment extends Fragment {

    private TabLayout tabLayoutPaciente;
    private FragmentContainerView vistasDetallePaciente;


    public DetallePacientesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_pacientes, container, false);
        asignaComponentesAVariables(view);
        nombrePorAsignarParaMoversePorElTabLayout();
        return view;
    }

    public void asignaComponentesAVariables(View view) {
        tabLayoutPaciente = view.findViewById(R.id.tabLayoutDetallePacientes);
        vistasDetallePaciente = view.findViewById(R.id.fragmentContainerDetallePacientes);
    }

    public void nombrePorAsignarParaMoversePorElTabLayout() {
        tabLayoutPaciente.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabSeleccionado = (String) tab.getText();
                if (tabSeleccionado.equals("General")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new GeneralPacientesFragment()).commit();

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