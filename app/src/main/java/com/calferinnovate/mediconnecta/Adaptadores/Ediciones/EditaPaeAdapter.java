package com.calferinnovate.mediconnecta.Adaptadores.Ediciones;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.Aulas;
import com.calferinnovate.mediconnecta.Model.Cargo;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.ControlSomatometrico;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.Pae;
import com.calferinnovate.mediconnecta.Model.PaeInsertionObservable;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EditaPaeAdapter {

    private ArrayList<String> cursoArrayList;
    private Alumnos alumno;
    private ClaseGlobal claseGlobal;
    private Context context;
private Pae pae;
    private AutoCompleteTextView cursosTV, tutorTv, enfermeraTV, aulasTV;
    private ArrayList<String> nombreTutoresArrayList, nombreEnfermerasArrayList, nombreAulasArrayList;
    private String[] cursoActual = new String[2];
    private ArrayList<ControlSomatometrico> controlTrimestres;
    private PaeInsertionObservable paeInsertionObservable = new PaeInsertionObservable();
    private ItemItemSelectedListener listener;
    private TextInputEditText nombreAlumno, fechaNacimiento, protesis,
            ortesis, gafas, audifonos, otros, fiebre, alergias, diagnosticoClinico, dietas, medicacion, datosImportantes, ultimaModidicacion;
    private String curso, tutor, enfermera, aulas;


    public interface ItemItemSelectedListener {
        void onSpinnerItemSelected(String curso, String tutor, String enfermera, String aulas);
    }

    // Método para establecer el escuchador de eventos
    public void setSpinnerItemSelectedListener(EditaPaeAdapter.ItemItemSelectedListener listener) {
        this.listener = listener;
    }


    public EditaPaeAdapter(Alumnos alumno, ArrayList<String> cursoArrayList, Pae pae, ClaseGlobal claseGlobal, Context context) {
        this.alumno = alumno;
        this.cursoArrayList = cursoArrayList;
        this.pae = pae;
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
        setupSpinners();
    }

    public void enlazaRecursos(View view) {
        nombreAlumno = view.findViewById(R.id.nombreAlumno);
        fechaNacimiento = view.findViewById(R.id.fechaNacimientoPae);
        fiebre = view.findViewById(R.id.fiebrePae);
        alergias = view.findViewById(R.id.alergias);
        protesis = view.findViewById(R.id.protesis);
        ortesis = view.findViewById(R.id.ortesis);
        gafas = view.findViewById(R.id.gafas);
        audifonos = view.findViewById(R.id.audifonos);
        otros = view.findViewById(R.id.otros);
        diagnosticoClinico = view.findViewById(R.id.diagnostico);
        dietas = view.findViewById(R.id.dietas);
        medicacion = view.findViewById(R.id.medicacion);
        cursosTV = view.findViewById(R.id.cursos_exposed_dropdown);
        tutorTv = view.findViewById(R.id.tutor_exposed_dropdown);
        enfermeraTV = view.findViewById(R.id.enfermera_exposed_dropdown);
        aulasTV = view.findViewById(R.id.aulaAlumnoPae);
        ultimaModidicacion = view.findViewById(R.id.modificado);
        datosImportantes = view.findViewById(R.id.datosImportantes);
    }

    public void seteaDatos() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedesfiebre ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);

        nombreAlumno.setText(alumno.getNombre());
        nombreAlumno.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        fechaNacimiento.setText(alumno.getFechaNacimiento());
        fechaNacimiento.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        cursosTV.setText(String.valueOf(pae.getCursoEmision()) + "/" + String.valueOf(pae.getCursoEmisionFin()));
        cursosTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        tutorTv.setText(obtieneNombreTutor(pae));
        tutorTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        enfermeraTV.setText(obtieneNombreEnfermera(pae));
        enfermeraTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

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

        diagnosticoClinico.setText(pae.getDiagnosticoClinico());
        diagnosticoClinico.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        dietas.setText(pae.getDieta());
        dietas.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        medicacion.setText(pae.getMedicacion());
        medicacion.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        aulasTV.setText(obtieneNombreAula(pae));
        aulasTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        ultimaModidicacion.setText(seteaUltimoModificado(pae));
        ultimaModidicacion.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        datosImportantes.setText(pae.getDatosImportantes());
        datosImportantes.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
    }

    public void setupSpinners() {
        setupCursosSpinner();
        setupTutorSpinner();
        setupEnfermeraSpinner();
        setupAulasSpinner();
    }

    private void setupCursosSpinner() {
        ArrayAdapter<String> cursosAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, cursoArrayList);
        cursosTV.setThreshold(1);
        cursosTV.setAdapter(cursosAdapter);

        cursosTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curso = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });

        cursosTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No es necesario implementar este método
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No es necesario implementar este método
            }

            @Override
            public void afterTextChanged(Editable s) {
                String cursoText = s.toString();
                if (!cursoArrayList.contains(cursoText)) {
                    // El texto ingresado no coincide con ningún elemento de la lista
                    // Considerarlo como un nuevo elemento y guardarlo en la base de datos
                    curso = cursoText;
                    notifyItemSelectedListener();
                }
            }
        });

        //SI no se ha seleccionado nada
        curso = cursosTV.getText().toString();
        notifyItemSelectedListener();
    }

    private void setupTutorSpinner() {
        nombreTutoresArrayList = new ArrayList<>();
        for (Cargo c : claseGlobal.getCargoArrayList()) {
            for (Empleado e : claseGlobal.getListaEmpleados()) {
                if (e.getFk_cargo() == c.getIdCargo() && c.getNombreCargo().equals("Profesor")) {
                    nombreTutoresArrayList.add(e.getNombre());
                }
            }
        }
        ArrayAdapter<String> tutoresAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, nombreTutoresArrayList);
        tutorTv.setThreshold(1);
        tutorTv.setAdapter(tutoresAdapter);
        tutorTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tutor = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });

        tutorTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No es necesario implementar este método
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No es necesario implementar este método
            }

            @Override
            public void afterTextChanged(Editable s) {
                String tutorText = s.toString();
                if (!nombreTutoresArrayList.contains(tutorText)) {
                    // El texto ingresado no coincide con ningún elemento de la lista
                    // Considerarlo como un nuevo elemento y guardarlo en la base de datos
                    tutor = tutorText;
                    notifyItemSelectedListener();
                }
            }
        });

        //Si no se ha seleccionado nada
        tutor = tutorTv.getText().toString();
        notifyItemSelectedListener();
    }

    private void setupEnfermeraSpinner() {
        nombreEnfermerasArrayList = new ArrayList<>();
        for (Cargo c : claseGlobal.getCargoArrayList()) {
            for (Empleado e : claseGlobal.getListaEmpleados()) {
                if (e.getFk_cargo() == c.getIdCargo() && c.getNombreCargo().equals("Enfermera")) {
                    nombreEnfermerasArrayList.add(e.getNombre());
                }
            }
        }
        ArrayAdapter<String> enfermerasAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, nombreEnfermerasArrayList);
        enfermeraTV.setThreshold(1);
        enfermeraTV.setAdapter(enfermerasAdapter);
        enfermeraTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                enfermera = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });

        enfermeraTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No es necesario implementar este método
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No es necesario implementar este método
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enfermeraText = s.toString();
                if (!nombreEnfermerasArrayList.contains(enfermeraText)) {
                    // El texto ingresado no coincide con ningún elemento de la lista
                    // Considerarlo como un nuevo elemento y guardarlo en la base de datos
                    enfermera = enfermeraText;
                    notifyItemSelectedListener();
                }
            }
        });
        //Si no se ha seleccionado nada
        enfermera = enfermeraTV.getText().toString();
        notifyItemSelectedListener();
    }

    private void setupAulasSpinner() {
        nombreAulasArrayList = new ArrayList<>();
        for (Aulas a : claseGlobal.getListaAulas()) {
                    nombreAulasArrayList.add(a.getNombreAula());
            }

        ArrayAdapter<String> aulasAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, nombreAulasArrayList);
        aulasTV.setThreshold(1);
        aulasTV.setAdapter(aulasAdapter);
        aulasTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aulas = (String) parent.getItemAtPosition(position);
                notifyItemSelectedListener();
            }
        });

        aulasTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No es necesario implementar este método
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No es necesario implementar este método
            }

            @Override
            public void afterTextChanged(Editable s) {
                String aulaText = s.toString();
                if (!nombreAulasArrayList.contains(aulaText)) {
                    // El texto ingresado no coincide con ningún elemento de la lista
                    // Considerarlo como un nuevo elemento y guardarlo en la base de datos
                    aulas = aulaText;
                    notifyItemSelectedListener();
                }
            }
        });

        //Si no se ha seleccionado nada
        aulas = aulasTV.getText().toString();
        notifyItemSelectedListener();
    }


    private String obtieneNombreTutor(Pae pae){
        String nombreProfesor = "";
        for(Empleado e: ClaseGlobal.getInstance().getListaEmpleados()){
            if(e.getCod_empleado() == pae.getFkIdProfesor()){
                nombreProfesor = e.getNombre();
            }
        }
        return nombreProfesor;
    }

    private String obtieneNombreEnfermera(Pae pae){
        String nombreEnfermera = "";
        for(Empleado e: ClaseGlobal.getInstance().getListaEmpleados()){
            if(e.getCod_empleado() == pae.getFkIdEnfermero()){
                nombreEnfermera = e.getNombre();
            }
        }
        return nombreEnfermera;
    }

    private String obtieneNombreAula(Pae pae){
        String nombreAula = "";
        for(Aulas a: ClaseGlobal.getInstance().getListaAulas()){
            if(a.getIdAula() == pae.getFkAula()){
                nombreAula = a.getNombreAula();
            }
        }
        return nombreAula;
    }

    private void notifyItemSelectedListener() {
        if (listener != null) {
            listener.onSpinnerItemSelected(curso, tutor, enfermera, aulas);
        }
    }

    private String seteaUltimoModificado(Pae pae){
        String enfermeraModificacion = "", tiempoModificacion, ultimoModificado;

        for(Empleado e: ClaseGlobal.getInstance().getListaEmpleados()){
            if(e.getCod_empleado() == pae.getIdEnfermeroModifica()){
                enfermeraModificacion = e.getNombre();
            }
        }

        tiempoModificacion = formateaFechaModificacion(pae.getTiempoDeModificacion());
        ultimoModificado = enfermeraModificacion+" "+"a: "+tiempoModificacion;
        return ultimoModificado;
    }

    private String formateaFechaModificacion(String fechaHora){
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        LocalDateTime fechaEntrada = LocalDateTime.parse(fechaHora, formatoEntrada);

        return fechaEntrada.format(formatoSalida);
    }



}
