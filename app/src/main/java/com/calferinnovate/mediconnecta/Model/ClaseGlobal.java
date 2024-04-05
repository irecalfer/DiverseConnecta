package com.calferinnovate.mediconnecta.Model;

import android.app.Application;

import java.util.ArrayList;

/**
 * Implementación del patrón de diseño Singleton. Garantizados que una clase tenga solo una instancia
 * y proporciona un punto de acceso global a esa instancia.
 * @author Irene Caldelas Fernández
 */

public class ClaseGlobal extends Application {


    //Variable para almacenar una única instancia de ClaseGlobal
    private static ClaseGlobal instance;

    //Declaración de objetos
    private Empleado empleado;
    private Pae pae;
    private ArrayList<Alumnos> listaAlumnos;
    private ArrayList<Empleado> listaEmpleados;
    private ArrayList<Aulas> listaAulas;
    private ArrayList<EmpleadosTrabajanAulas> listaEmpleadosAulas;
    private ArrayList<NivelEscolar> listaNivelEscolar;
    private ArrayList<String> listaGradosDiscapacidad;




    @Override public void onCreate(){
        super.onCreate();
    }

    /**
     * Constructor para evitar la creación de instancias fuera de la clase.
     * Cuando se accede a getInstance() asegura que la instancia ya tenga valores.
     */
    public ClaseGlobal() {
        empleado = new Empleado();
        listaAlumnos = new ArrayList<>(); // Inicializa la lista de pacientes vacía
        listaEmpleados = new ArrayList<>();
        listaAulas = new ArrayList<>();
        listaEmpleadosAulas = new ArrayList<>();
        listaNivelEscolar = new ArrayList<>();
        listaGradosDiscapacidad = new ArrayList<>();
        pae = new Pae();

    }

    /**
     * Método público y estático que se utiliza para obtener la única instancia de la clase ClaseGlobal.
     * Es estático para que pueda ser llamado sin crear una instancia previa de la clase.
     * @return instance Es la instancia de la clase global
     */
    //
    public static  ClaseGlobal getInstance(){
        if (instance == null) {
            synchronized (ClaseGlobal.class) {
                if (instance == null) {
                    instance = new ClaseGlobal();
                }
            }
        }
        return instance;
    }


    //Getters y Setters de los diferentes objetos.
    public Empleado getEmpleado() {
        return empleado;
    }


    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }


    public ArrayList<Alumnos> getListaAlumnos() {
        return listaAlumnos;
    }

    public void setListaAlumnos(ArrayList<Alumnos> listaPacientes) {
        this.listaAlumnos = listaPacientes;
    }

    public ArrayList<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(ArrayList<Empleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public ArrayList<Aulas> getListaAulas() {
        return listaAulas;
    }

    public void setListaAulas(ArrayList<Aulas> listaAulas) {
        this.listaAulas = listaAulas;
    }

    public ArrayList<EmpleadosTrabajanAulas> getListaEmpleadosAulas() {
        return listaEmpleadosAulas;
    }

    public void setListaEmpleadosAulas(ArrayList<EmpleadosTrabajanAulas> listaEmpleadosAulas) {
        this.listaEmpleadosAulas = listaEmpleadosAulas;
    }

    public ArrayList<NivelEscolar> getListaNivelEscolar() {
        return listaNivelEscolar;
    }

    public void setListaNivelEscolar(ArrayList<NivelEscolar> listaNivelEscolar) {
        this.listaNivelEscolar = listaNivelEscolar;
    }

    public ArrayList<String> getListaGradosDiscapacidad() {
        return listaGradosDiscapacidad;
    }

    public void setListaGradosDiscapacidad(ArrayList<String> listaGradosDiscapacidad) {
        this.listaGradosDiscapacidad = listaGradosDiscapacidad;
    }
}
