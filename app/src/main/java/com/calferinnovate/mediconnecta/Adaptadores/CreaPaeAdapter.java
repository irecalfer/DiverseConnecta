package com.calferinnovate.mediconnecta.Adaptadores;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.lifecycle.Observer;

import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.Cargo;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class CreaPaeAdapter {

    private ArrayList<String> cursoArrayList;
    private Alumnos alumno;
    private ClaseGlobal claseGlobal;
    private Context context;

    private AutoCompleteTextView cursosTV, tutorTv, enfermeraTV;
    private TextInputEditText nombreTV, nacimientoTV;
    private String curso, tutor, enfermera;
    private ArrayList<String> nombreTutoresArrayList, nombreEnfermerasArrayList;
    private ItemItemSelectedListener listener;




    public interface ItemItemSelectedListener {
        void onSpinnerItemSelected(String curso, String tutor, String enfermera);
    }

    // Método para establecer el escuchador de eventos
    public void setSpinnerItemSelectedListener(ItemItemSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * Este metodo se utiliza desde el fragmento que captura el evento de clic de los items
     */


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
        seteaDatosConocidos();
        setupSpinners();
    }

    public void enlazaRecursos(View view){
        cursosTV = view.findViewById(R.id.cursos_exposed_dropdown);
        tutorTv = view.findViewById(R.id.tutor_exposed_dropdown);
        enfermeraTV = view.findViewById(R.id.enfermera_exposed_dropdown);
        nombreTV = view.findViewById(R.id.nombreAlumno);
        nacimientoTV = view.findViewById(R.id.fechaNacimientoPae);
    }


    public void seteaDatosConocidos(){
        nombreTV.setText(alumno.getNombre()+" "+alumno.getApellidos());
        nacimientoTV.setText(alumno.getFechaNacimiento());
    }


    public String getCurso() {
        return curso;
    }

    public String getTutor() {
        return tutor;
    }

    public String getEnfermera() {
        return enfermera;
    }

    // Método para inicializar los spinners y establecer los listeners
    public void setupSpinners() {
        setupCursosSpinner();
        setupTutorSpinner();
        setupEnfermeraSpinner();
    }

    private void setupCursosSpinner() {
        ArrayAdapter<String> cursosAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, cursoArrayList);
        cursosTV.setAdapter(cursosAdapter);
        cursosTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curso = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });
    }

    private void setupTutorSpinner() {
        nombreTutoresArrayList = new ArrayList<>();
        for(Cargo c: claseGlobal.getCargoArrayList()){
            for(Empleado e: claseGlobal.getListaEmpleados()){
                if(e.getFk_cargo() == c.getIdCargo() && c.getNombreCargo().equals("Profesor")){
                    nombreTutoresArrayList.add(e.getNombre());
                }
            }
        }
        ArrayAdapter<String> tutoresAdapter = new ArrayAdapter<>(context,R.layout.my_spinner, nombreTutoresArrayList);
        tutorTv.setAdapter(tutoresAdapter);
        tutorTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tutor = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });
    }

    private void setupEnfermeraSpinner() {
        nombreEnfermerasArrayList = new ArrayList<>();
        for(Cargo c: claseGlobal.getCargoArrayList()){
            for(Empleado e: claseGlobal.getListaEmpleados()){
                if(e.getFk_cargo() == c.getIdCargo() && c.getNombreCargo().equals("Enfermera")){
                    nombreEnfermerasArrayList.add(e.getNombre());
                }
            }
        }
        ArrayAdapter<String> enfermerasAdapter = new ArrayAdapter<>(context, R.layout.my_spinner, nombreEnfermerasArrayList);
        enfermeraTV.setAdapter(enfermerasAdapter);
        enfermeraTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                enfermera = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });
    }

    private void notifyItemSelectedListener() {
        if (listener != null) {
            listener.onSpinnerItemSelected(curso, tutor, enfermera);
        }
    }

}
