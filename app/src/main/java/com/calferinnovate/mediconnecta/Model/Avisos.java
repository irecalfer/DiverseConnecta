package com.calferinnovate.mediconnecta.Model;

public class Avisos {
    private String fecha_aviso;
    private String contenido, nombreEmpleado, fotoEmpleado;


    public Avisos() {
    }

    public Avisos(String fecha_aviso, String contenido, String nombreEmpleado, String fotoEmpleado) {
        this.fecha_aviso = fecha_aviso;
        this.contenido = contenido;
        this.nombreEmpleado = nombreEmpleado;
        this.fotoEmpleado = fotoEmpleado;
    }


    public String getFecha_aviso() {
        return fecha_aviso;
    }

    public void setFecha_aviso(String fecha_aviso) {
        this.fecha_aviso = fecha_aviso;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getFotoEmpleado() {
        return fotoEmpleado;
    }

    public void setFotoEmpleado(String fotoEmpleado) {
        this.fotoEmpleado = fotoEmpleado;
    }
}
