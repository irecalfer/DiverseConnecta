package com.calferinnovate.mediconnecta.clases;

import android.app.Application;

import java.util.ArrayList;

public class ClaseGlobal extends Application {
    private Empleado empleado;
    private Unidades unidades;
    private Area area;
    private Avisos avisos;
    private Fechas fechas;
    private Pacientes pacientes;
    private Rutinas rutinas;
    private PacientesAgrupadosRutinas pacientesAgrupadosRutinas;
    private ArrayList<Unidades> listaUnidades;
    private ArrayList<Pacientes> listaPacientes;
    private ArrayList<PacientesAgrupadosRutinas> listaProgramacion;

    @Override public void onCreate(){
        super.onCreate();
    }

    public ClaseGlobal() {
        empleado = new Empleado();
        unidades = new Unidades();
        area = new Area();
        avisos = new Avisos();
        fechas = new Fechas();
        pacientes = new Pacientes();
        rutinas = new Rutinas();
        pacientesAgrupadosRutinas = new PacientesAgrupadosRutinas();
        pacientesAgrupadosRutinas.setListaProgramacion(new ArrayList<>());
        listaUnidades = new ArrayList<>();
        listaPacientes = new ArrayList<>(); // Inicializa la lista de pacientes vacía
        listaProgramacion = new ArrayList<>();

    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Unidades getUnidades() {
        return unidades;
    }

    public Area getArea() {
        return area;
    }

    public Avisos getAvisos() {
        return avisos;
    }

    public Fechas getFechas() {
        return fechas;
    }

    public Pacientes getPacientes() {
        return pacientes;
    }

    public Rutinas getRutinas() {
        return rutinas;
    }

    public PacientesAgrupadosRutinas getPacientesAgrupadosRutinas() {
        return pacientesAgrupadosRutinas;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setUnidades(Unidades unidades) {
        this.unidades = unidades;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setAvisos(Avisos avisos) {
        this.avisos = avisos;
    }

    public void setFechas(Fechas fechas) {
        this.fechas = fechas;
    }

    public void setPacientes(Pacientes pacientes) {
        this.pacientes = pacientes;
    }

    public void setRutinas(Rutinas rutinas) {
        this.rutinas = rutinas;
    }

    public void setPacientesAgrupadosRutinas(PacientesAgrupadosRutinas pacientesAgrupadosRutinas) {
        this.pacientesAgrupadosRutinas = pacientesAgrupadosRutinas;
    }

    public ArrayList<Unidades> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(ArrayList<Unidades> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public ArrayList<Pacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(ArrayList<Pacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public ArrayList<PacientesAgrupadosRutinas> getListaProgramacion() {
        return listaProgramacion;
    }

    public void setListaProgramacion(ArrayList<PacientesAgrupadosRutinas> listaProgramacion) {
        this.listaProgramacion = listaProgramacion;
    }
}
