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
    private ArrayList<Parte> parteArrayList;
    private ArrayList<Caidas> caidasArrayList;



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
        parteArrayList = new ArrayList<>();
        caidasArrayList = new ArrayList<>();
    }

    /**
     * Método público y estático que se utiliza para obtener la única instancia de la clase ClaseGlobal.
     * Es estático para que pueda ser llamado sin crear una instancia previa de la clase.
     * @return instance Es la instancia de la clase global
     */
    //
    public static  ClaseGlobal getInstance(){
        // Aquí se verifica si la variable instance es nula. La primera vez que se llama a getInstance(),
        // instance será nula porque aún no se ha creado una instancia de ClaseGlobal.
        if (instance == null) {
            //Esto inicia una sección crítica sincronizada en la clase ClaseGlobal. El bloque sincronizado garantiza que solo un hilo a
            // la vez pueda ejecutar el código dentro de ese bloque. Esto es importante para evitar que
            // varios hilos creen múltiples instancias de la clase en caso de concurrencia.
            synchronized (ClaseGlobal.class) {
                //dentro del bloque sincronizado, se realiza una segunda verificación de si instance es nula. Esto es necesario porque,
                // después de adquirir el bloque sincronizado, otro hilo podría haber creado la instancia de
                // ClaseGlobal, por lo que necesitas verificar nuevamente si instance es nula.
                if (instance == null) {
                    //Si, en la segunda verificación, instance aún es nula, se crea una nueva instancia de la clase ClaseGlobal y se asigna a la variable instance.
                    instance = new ClaseGlobal();
                }
            }
        }
        //Finalmente, se devuelve la instancia de ClaseGlobal. Si es la primera vez que se llama a getInstance(),
        // se crea una nueva instancia. Si no es la primera vez, se devuelve la instancia existente.
        // Esto asegura que siempre se utilice la misma instancia en toda la aplicación.
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



    public ArrayList<Parte> getParteArrayList() {
        return parteArrayList;
    }

    public void setParteArrayList(ArrayList<Parte> parteArrayList) {
        this.parteArrayList = parteArrayList;
    }

    public ArrayList<Caidas> getCaidasArrayList() {
        return caidasArrayList;
    }

    public void setCaidasArrayList(ArrayList<Caidas> caidasArrayList) {
        this.caidasArrayList = caidasArrayList;
    }

}
