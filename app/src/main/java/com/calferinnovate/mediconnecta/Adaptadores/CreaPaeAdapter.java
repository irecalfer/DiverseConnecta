package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Curso;
import com.calferinnovate.mediconnecta.R;

import java.util.ArrayList;

public class CreaPaeAdapter {

    private ArrayList<String> cursoArrayList;
    private Alumnos alumno;
    private ClaseGlobal claseGlobal;
    private Context context;


    private AutoCompleteTextView cursosTV;

    public CreaPaeAdapter(Alumnos alumno, ArrayList<String> cursoArrayList, ClaseGlobal claseGlobal, Context context){
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
    }

    public void seteaDatos(){
        ArrayAdapter<String> cursosAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, cursoArrayList);
        cursosTV.setAdapter(cursosAdapter);
        cursosTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }
}
