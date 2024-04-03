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
    private ArrayList<Alumnos> listaAlumnos;
    private ArrayList<Empleado> listaEmpleados;




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
}
