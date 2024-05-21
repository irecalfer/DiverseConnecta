package com.calferinnovate.mediconnecta.Model;

public class Crisis {

    private String fecha, tipo, lugar, intensidad, patron, descripcion, duracion, recuperacion;
    private int idAlumno, idCrisis;

    public Crisis() {
    }

    public Crisis(int idCrisis, String fecha, String tipo, String lugar, String intensidad, String patron,
                  String descripcion, String duracion, String recuperacion, int idAlumno) {
        this.idCrisis = idCrisis;
        this.fecha = fecha;
        this.tipo = tipo;
        this.lugar = lugar;
        this.intensidad = intensidad;
        this.patron = patron;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.recuperacion = recuperacion;
        this.idAlumno = idAlumno;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(String intensidad) {
        this.intensidad = intensidad;
    }

    public String getPatron() {
        return patron;
    }

    public void setPatron(String patron) {
        this.patron = patron;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getRecuperacion() {
        return recuperacion;
    }

    public void setRecuperacion(String recuperacion) {
        this.recuperacion = recuperacion;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdCrisis() {
        return idCrisis;
    }

    public void setIdCrisis(int idCrisis) {
        this.idCrisis = idCrisis;
    }
}
