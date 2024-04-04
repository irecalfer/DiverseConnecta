package com.calferinnovate.mediconnecta.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Model.Alumnos;
import com.calferinnovate.mediconnecta.Model.Aulas;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.EmpleadosTrabajanAulas;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador que pobla el Fragmento GeneralPacientes con los datos de este.
 */
public class AlumnosGeneralAdapter {

    private ImageView fotoPaciente;
    private TextInputEditText nombre, apellidos, sexo, dni, edad, fechaNacimiento, profesor, aula, ate,
    gradoDiscapacidad;
    private final Alumnos alumno;
    private final Context context;

    /**
     * Constrctor del adaptador
     */
    public AlumnosGeneralAdapter(Alumnos alumno, Context context) {
        this.alumno = alumno;
        this.context = context;
    }

    /**
     * Método que enlaza los recursos de la UI con las variables y setea los datos del paciente.
     *
     * @param view La vista inflada.
     */
    public void rellenaUI(View view) {
        enlazaRecursos(view);
        seteaDatos();
    }

    /**
     * Método que enlaza los recusos de la UI con las variables-
     *
     * @param view La vista inflada.
     */
    public void enlazaRecursos(View view) {
        fotoPaciente = view.findViewById(R.id.fotoPacienteDetalle);
        nombre = view.findViewById(R.id.nombreAlumno);
        apellidos = view.findViewById(R.id.apellidoAlumno);
        sexo = view.findViewById(R.id.sexoAlumno);
        dni = view.findViewById(R.id.dniAlumno);
        edad = view.findViewById(R.id.edadAlumno);
        fechaNacimiento = view.findViewById(R.id.fechaNacimientoAlumno);
        profesor = view.findViewById(R.id.profesorAlumno);
        aula = view.findViewById(R.id.aulaAlumno);
        ate = view.findViewById(R.id.ateAlumno);
        gradoDiscapacidad = view.findViewById(R.id.gradoDiscapacidad);
    }

    /**
     * Método que actualiza la UI con los datos del paciente seleccionado.
     */
    public void seteaDatos() {
        configuraFotoPacientes();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedes ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);

        nombre.setText(alumno.getNombre());
        nombre.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        apellidos.setText(alumno.getApellidos());
        apellidos.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        sexo.setText(alumno.getSexo());
        sexo.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        dni.setText(alumno.getDni());
        dni.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);


        edad.setText(String.valueOf(calculaEdad(alumno)));
        edad.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        fechaNacimiento.setText(formateaFecha(alumno.getFechaNacimiento()));
        fechaNacimiento.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        profesor.setText(obtieneNombreProfesor(alumno));
        profesor.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        aula.setText(obtieneNombreAula(alumno));
        aula.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        ate.setText(obtieneNombreATE(alumno));
        ate.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        gradoDiscapacidad.setText(alumno.getGradoDiscapacidad());
        gradoDiscapacidad.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
    }

    private String obtieneNombreProfesor(Alumnos alumno){
        String nombreEmpleado = "";
        for(EmpleadosTrabajanAulas a: ClaseGlobal.getInstance().getListaEmpleadosAulas()){
            if(a.getFkAula() == alumno.getFkAula()){
                for(Empleado e: ClaseGlobal.getInstance().getListaEmpleados()){
                    if(a.getFkCodEmpleado() == e.getCod_empleado() && e.getFk_cargo() == 9 ){
                        nombreEmpleado =  e.getNombre()+" "+e.getApellidos();
                    }
                }
            }
        }
        return nombreEmpleado;
    }

    private String obtieneNombreATE(Alumnos alumno){
        String nombreEmpleado = "";
        for(EmpleadosTrabajanAulas a: ClaseGlobal.getInstance().getListaEmpleadosAulas()){
            if(a.getFkAula() == alumno.getFkAula()){
                for(Empleado e: ClaseGlobal.getInstance().getListaEmpleados()){
                    if(a.getFkCodEmpleado() == e.getCod_empleado() && e.getFk_cargo() == 10 ){
                        nombreEmpleado =  e.getNombre()+" "+e.getApellidos();
                    }
                }
            }
        }
        return nombreEmpleado;
    }

    private String obtieneNombreAula(Alumnos alumno){
        String nombreaAula = "";
        for(Aulas a: ClaseGlobal.getInstance().getListaAulas()){
            if(a.getIdAula() == alumno.getFkAula()){
                nombreaAula = a.getNombreAula();
            }
        }
        return nombreaAula;
    }

    private void configuraFotoPacientes(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        int targetWidth = Math.min(screenWidth, 300); // Tamaño máximo
        int targetHeight = Math.min(screenHeight, 300); // Tamaño máximo


        Glide.with(context).load(alumno.getFoto()).circleCrop().override(targetWidth, targetHeight).into(fotoPaciente);
    }
    /**
     * Método usado para calcular la edad del paciente a partir de su fecha de nacimiento.
     *
     * @param alumnos Paciente seleccionado
     * @return La edad del paciente.
     */
    private int calculaEdad(Alumnos alumnos) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNac = LocalDate.parse(alumnos.getFechaNacimiento(), fmt);
        LocalDate ahora = LocalDate.now();

        Period period = Period.between(fechaNac, ahora);
        return period.getYears();
    }

    /**
     * Método que formatea la fecha de nacimiento y de ingreso de yyyy-MM-dd a dd-MM-yyyy
     *
     * @param fecha Fecha a formatear
     * @return fecha formateada
     */
    private String formateaFecha(String fecha) {
        // Formato de entrada (año-mes-día)
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parsea la fecha de entrada
        LocalDate fechaEntrada = LocalDate.parse(fecha, formatoEntrada);

        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaEntrada.format(formatoSalida);
        return fechaFormateada;
    }


    /**
     * Método que busca la unidad a la que pertenece el paciente y actualiza la UI.
     *
     * @param paciente Paciente seleccionado
     * @return nombre de la unidad.
     */
    /*
    private String nombreUnidad(Alumnos paciente) {
        String nombreUnidad = "";
        for (Unidades unidades : ClaseGlobal.getInstance().getListaUnidades()) {
            if (paciente.getFkIdUnidad() == unidades.getId_unidad()) {
                nombreUnidad = unidades.getNombreUnidad();
            }
        }
        return nombreUnidad;
    }*/
}
