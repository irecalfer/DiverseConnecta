package com.calferinnovate.mediconnecta.Model;

public class Aulas {

    private int idAula, fk_nivel_escolar;
    private String nombreAula;

    public Aulas() {
    }

    public Aulas(int idAula, int fk_nivel_escolar, String nombreAula) {
        this.idAula = idAula;
        this.fk_nivel_escolar = fk_nivel_escolar;
        this.nombreAula = nombreAula;
    }

    public int getIdAula() {
        return idAula;
    }

    public void setIdAula(int idAula) {
        this.idAula = idAula;
    }

    public int getFk_nivel_escolar() {
        return fk_nivel_escolar;
    }

    public void setFk_nivel_escolar(int fk_nivel_escolar) {
        this.fk_nivel_escolar = fk_nivel_escolar;
    }

    public String getNombreAula() {
        return nombreAula;
    }

    public void setNombreAula(String nombreAula) {
        this.nombreAula = nombreAula;
    }
}
