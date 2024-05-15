package com.calferinnovate.mediconnecta.Model;

public class Seguimiento {

    private int idSeguimiento, idAlumno;
    private String fechaHora, descripcion;

    public Seguimiento() {
    }

    public Seguimiento(int idSeguimiento, String fechaHora, String descripcion, int idAlumno) {
        this.idSeguimiento = idSeguimiento;
        this.fechaHora = fechaHora;
        this.descripcion = descripcion;
        this.idAlumno = idAlumno;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdSeguimiento() {
        return idSeguimiento;
    }

    public void setIdSeguimiento(int idSeguimiento) {
        this.idSeguimiento = idSeguimiento;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }
}
