package com.calferinnovate.mediconnecta.Sesion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Empleado;


public class SeleccionUnidadFragment extends Fragment {

    Button botonFinalizar;
    NavController navController;
    TextView nombre, cod_empleado, cargo;
    Empleado empleado;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

    View vista = inflater.inflate(R.layout.fragment_seleccion_unidad, container, false);

        return vista;

    }


    public void completaDatosEmpleado(Empleado e){
        nombre.setText(String.valueOf(e.getNombre()+" "+e.getApellidos()));
        cargo.setText(String.valueOf(e.getNombreCargo()));
        cod_empleado.setText(String.valueOf(e.getCod_empleado()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        empleado = (Empleado) getActivity().getApplicationContext();
        navController = Navigation.findNavController(view);

        botonFinalizar = (Button) view.findViewById(R.id.AccesoAlHome);
        nombre = view.findViewById(R.id.nombreYApellidos);
        cod_empleado = (TextView) view.findViewById(R.id.cod_empleado);
        cargo = (TextView) view.findViewById(R.id.cargo);



        completaDatosEmpleado(empleado);




        botonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_seleccionUnidadFragment_to_homeActivity);
            }
        });

    }
}