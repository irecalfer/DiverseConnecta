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
    private Unidades unidades;
    private ArrayList<Unidades> listaUnidades;
    private ArrayList<Pacientes> listaPacientes;
    private ArrayList<Area> listaAreas;



    @Override public void onCreate(){
        super.onCreate();
    }

    /**
     * Constructor para evitar la creación de instancias fuera de la clase.
     * Cuando se accede a getInstance() asegura que la instancia ya tenga valores.
     */
    public ClaseGlobal() {
        empleado = new Empleado();
        unidades = new Unidades();
        listaUnidades = new ArrayList<>();
        listaPacientes = new ArrayList<>(); // Inicializa la lista de pacientes vacía
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

    public Unidades getUnidades() {
        return unidades;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setUnidades(Unidades unidades) {
        this.unidades = unidades;
    }

    public ArrayList<Unidades> getListaUnidades() {
        return listaUnidades;
    }


    public ArrayList<Pacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(ArrayList<Pacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public ArrayList<Area> getListaAreas() {
        return listaAreas;
    }

    public void setListaAreas(ArrayList<Area> listaAreas) {
        this.listaAreas = listaAreas;
    }


}
