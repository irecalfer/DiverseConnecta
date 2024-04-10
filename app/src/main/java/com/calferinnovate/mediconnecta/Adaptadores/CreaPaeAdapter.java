package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.Cargo;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Curso;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.R;

import java.util.ArrayList;

public class CreaPaeAdapter {

    private ArrayList<String> cursoArrayList;
    private Alumnos alumno;
    private ClaseGlobal claseGlobal;
    private Context context;


    private AutoCompleteTextView cursosTV, tutorTv, enfermeraTV;

    public CreaPaeAdapter(Alumnos alumno, ArrayList<String> cursoArrayList,ClaseGlobal claseGlobal, Context context){
        this.alumno = alumno;
        this.cursoArrayList = cursoArrayList;
        this.claseGlobal = claseGlobal;
        this.context = context;
    }

    /**
     * Método utilizado para actualizar la UI.
     *
     * @param view La vista inflada.
     */
    public void rellenaUI(View view) {
        enlazaRecursos(view);
        seteaDatos();
    }

    public void enlazaRecursos(View view){
        cursosTV = view.findViewById(R.id.cursos_exposed_dropdown);
        tutorTv = view.findViewById(R.id.tutor_exposed_dropdown);
        enfermeraTV = view.findViewById(R.id.enfermera_exposed_dropdown);

    }

    public void seteaDatos(){
        ArrayAdapter<String> cursosAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, cursoArrayList);
        cursosTV.setAdapter(cursosAdapter);
        cursosTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        obtieneListaTutores();
        obtieneListaEnfermeras();
    }

    public void obtieneListaTutores(){
        ArrayList<String> nombreTutoresArrayList = new ArrayList<>();
        for(Cargo c: claseGlobal.getCargoArrayList()){
            for(Empleado e: claseGlobal.getListaEmpleados()){
                if(e.getFk_cargo() == c.getIdCargo() && c.getNombreCargo().equals("Profesor")){
                    nombreTutoresArrayList.add(e.getNombre());
                }
            }
        }
        ArrayAdapter<String> tutoresAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nombreTutoresArrayList);
        tutorTv.setAdapter(tutoresAdapter);
        tutorTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void obtieneListaEnfermeras(){
        ArrayList<String> nombreEnfermerasArrayList = new ArrayList<>();
        for(Cargo c: claseGlobal.getCargoArrayList()){
            for(Empleado e: claseGlobal.getListaEmpleados()){
                if(e.getFk_cargo() == c.getIdCargo() && c.getNombreCargo().equals("Enfermera")){
                    nombreEnfermerasArrayList.add(e.getNombre());
                }
            }
        }
        ArrayAdapter<String> enfermerasAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nombreEnfermerasArrayList);
        enfermeraTV.setAdapter(enfermerasAdapter);
        enfermeraTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
