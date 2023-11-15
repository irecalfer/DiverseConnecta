package com.calferinnovate.mediconnecta.Model;

public class Parte {
    private String nombreApellidosPaciente, descripcion, empleado, fecha;

    public Parte() {
    }

    public Parte(String nombreApellidosPaciente, String descripcion, String empleado, String fecha) {
        this.nombreApellidosPaciente = nombreApellidosPaciente;
        this.descripcion = descripcion;
        this.empleado = empleado;
        this.fecha = fecha;
    }

    public String getNombreApellidosPaciente() {
        return nombreApellidosPaciente;
    }

    public void setNombreApellidosPaciente(String nombreApellidosPaciente) {
        this.nombreApellidosPaciente = nombreApellidosPaciente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
