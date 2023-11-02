package com.calferinnovate.mediconnecta.clases;

import android.app.Application;

public class ClaseGlobal extends Application {
    private Empleado empleado;
    private Unidades unidades;
    private Area area;
    private Avisos avisos;
    private Fechas fechas;
    private Pacientes pacientes;
    private Rutinas rutinas;
    private PacientesAgrupadosRutinas pacientesAgrupadosRutinas;

    @Override public void onCreate(){
        super.onCreate();
        empleado = new Empleado();
        unidades = new Unidades();
        area = new Area();
        avisos = new Avisos();
        fechas = new Fechas();
        pacientes = new Pacientes();
        rutinas = new Rutinas();
        pacientesAgrupadosRutinas = new PacientesAgrupadosRutinas();
    }

    public ClaseGlobal() {
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
}
