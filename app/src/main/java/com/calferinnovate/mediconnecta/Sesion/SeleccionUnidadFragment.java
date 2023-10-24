package com.calferinnovate.mediconnecta.Sesion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Unidades;
import com.loopj.android.http.*;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.Empleado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class SeleccionUnidadFragment extends Fragment {

    Button botonFinalizar;
    NavController navController;
    TextView nombre, cod_empleado, cargo;
    Spinner geriatriaSP, saludMentalSP;
    Empleado empleado;
    private Unidades unidades;








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

        empleado = ((ClaseGlobal) getActivity().getApplicationContext()).empleado;
        unidades = ((ClaseGlobal) getActivity().getApplicationContext()).unidades;
        navController = Navigation.findNavController(view);


        botonFinalizar = (Button) view.findViewById(R.id.AccesoAlHome);
        nombre = view.findViewById(R.id.nombreYApellidos);
        cod_empleado = (TextView) view.findViewById(R.id.cod_empleado);
        cargo = (TextView) view.findViewById(R.id.cargo);
        geriatriaSP = (Spinner) view.findViewById(R.id.spinnerGeriatria);
        saludMentalSP = (Spinner) view.findViewById(R.id.spinnerSaludMental);



        completaDatosEmpleado(empleado);
        poblarSpinner();




        botonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_seleccionUnidadFragment_to_homeActivity);
            }
        });

    }



    public void poblarSpinner(){
        String urlGeriatria = Constantes.url_part+"seleccion_unidades.php?fk_id_unidad=1";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlGeriatria, response -> {
            JSONObject jsonObject = null;
            for (int i =0; i <response.length(); i++){
                try {
                    jsonObject = response.getJSONObject(i);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, error -> {});
    }

    private void cargarSpinner(String s) {
        ArrayList<Unidades> lista = new ArrayList<Unidades>();
        try{
            JSONArray jsonArray = new JSONArray(s);
            for(int i = 0; i < jsonArray.length(); i++){
                Unidades unidades = new Unidades();
                unidades.setNombreUnidad(jsonArray.getJSONObject(i).getString("nombre"));
                lista.add(unidades);
            }
            ArrayAdapter <Unidades> a = new ArrayAdapter<Unidades>(getContext(), android.R.layout.simple_dropdown_item_1line, lista);
            geriatriaSP.setAdapter(a);
        }catch (Exception e){
            e.printStackTrace();

        }
    }
}