package com.calferinnovate.mediconnecta.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.ControlSomatometrico;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.Pae;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

public class PaeAdapter {

    private Alumnos alumno;
    private Context context;
    private Pae pae;
    private TextInputEditText nombre, fechaNacimiento, cursoEmision, tutor, enfermera, fiebre, alergias,
    protesis, ortesis, gafas, audifonos, otros, diagnostico, dietas, medicacion;

    public PaeAdapter(Alumnos alumno, Pae pae, Context context) {
        this.alumno = alumno;
        this.pae = pae;
        this.context = context;
    }

    public void rellenaUI(View view) {
        enlazaRecursos(view);
        seteaDatos();
    }

    public void enlazaRecursos(View view){
        nombre = view.findViewById(R.id.nombreAlumno);
        fechaNacimiento = view.findViewById(R.id.fechaNacimientoPae);
        cursoEmision = view.findViewById(R.id.cursoEmision);
        tutor = view.findViewById(R.id.tutorAlumno);
        enfermera = view.findViewById(R.id.enfermeraAlumno);
        fiebre = view.findViewById(R.id.fiebrePae);
        alergias = view.findViewById(R.id.alergias);
        protesis = view.findViewById(R.id.protesis);
        ortesis = view.findViewById(R.id.ortesis);
        gafas = view.findViewById(R.id.gafas);
        audifonos = view.findViewById(R.id.audifonos);
        otros = view.findViewById(R.id.otros);
        diagnostico = view.findViewById(R.id.diagnostico);
        dietas = view.findViewById(R.id.dietas);
        medicacion = view.findViewById(R.id.medicacion);
    }

    public void seteaDatos(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedesfiebre ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);

        nombre.setText(alumno.getNombre());
        nombre.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        fechaNacimiento.setText(alumno.getFechaNacimiento());
        fechaNacimiento.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        cursoEmision.setText(pae.getCursoEmision()+"/"+pae.getCursoEmisionFin());
        cursoEmision.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        tutor.setText(obtieneNombreTutor(pae));
        tutor.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        enfermera.setText(obtieneNombreEnfermera(pae));
        enfermera.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        fiebre.setText(pae.getFiebre());
        fiebre.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        alergias.setText(pae.getAlergias());
        alergias.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        protesis.setText(pae.getProtesis());
        protesis.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        ortesis.setText(pae.getOrtesis());
        ortesis.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        gafas.setText(pae.getGafas());
        gafas.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        audifonos.setText(pae.getAudifonos());
        audifonos.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        otros.setText(pae.getOtros());
        otros.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        diagnostico.setText(pae.getDiagnosticoClinico());
        diagnostico.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        dietas.setText(pae.getDieta());
        dietas.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        medicacion.setText(pae.getMedicacion());
        medicacion.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
    }

    private String obtieneNombreTutor(Pae pae){
        String nombreProfesor = "";
        for(Empleado e: ClaseGlobal.getInstance().getListaEmpleados()){
            if(e.getCod_empleado() == pae.getFkIdProfesor()){
                nombreProfesor = e.getNombre()+" "+e.getApellidos();
            }
        }
        return nombreProfesor;
    }

    private String obtieneNombreEnfermera(Pae pae){
        String nombreEnfermera = "";
        for(Empleado e: ClaseGlobal.getInstance().getListaEmpleados()){
            if(e.getCod_empleado() == pae.getFkIdEnfermero()){
                nombreEnfermera = e.getNombre()+" "+e.getApellidos();
            }
        }
        return nombreEnfermera;
    }
}
