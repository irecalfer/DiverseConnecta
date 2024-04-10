package com.calferinnovate.mediconnecta.Model;

public class Curso {

    public String añoInicio, añoFin;

    public Curso() {
    }

    public Curso(String añoInicio, String añoFin) {
        this.añoInicio = añoInicio;
        this.añoFin = añoFin;
    }

    public String getAñoInicio() {
        return añoInicio;
    }

    public void setAñoInicio(String añoInicio) {
        this.añoInicio = añoInicio;
    }

    public String getAñoFin() {
        return añoFin;
    }

    public void setAñoFin(String añoFin) {
        this.añoFin = añoFin;
    }
}
