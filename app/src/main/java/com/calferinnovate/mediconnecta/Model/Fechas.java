package com.calferinnovate.mediconnecta.Model;

public class Fechas {
    private String fechaActual, fechaInicio, fechaFin;

    public Fechas(String fechaActual, String fechaInicio, String fechaFin) {
        this.fechaActual = fechaActual;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Fechas() {
    }

    public String getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(String fechaActual) {
        this.fechaActual = fechaActual;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
