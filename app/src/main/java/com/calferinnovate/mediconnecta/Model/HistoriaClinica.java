package com.calferinnovate.mediconnecta.Model;

public class HistoriaClinica {
    private String datosSalud, tratamiento;

    public HistoriaClinica() {
    }

    public HistoriaClinica(String datosSalud, String tratamiento) {
        this.datosSalud = datosSalud;
        this.tratamiento = tratamiento;
    }

    public String getDatosSalud() {
        return datosSalud;
    }

    public void setDatosSalud(String datosSalud) {
        this.datosSalud = datosSalud;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
}
