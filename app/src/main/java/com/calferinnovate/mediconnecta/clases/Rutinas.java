package com.calferinnovate.mediconnecta.clases;

import java.util.ArrayList;

public class Rutinas {

    private int idTipoRutina;
    private String diario;
    private ArrayList<Rutinas> listaRutinas;

    public Rutinas() {
    }

    public Rutinas(int idTipoRutina, String diario) {
        this.idTipoRutina = idTipoRutina;
        this.diario = diario;
    }

    public int getIdTipoRutina() {
        return idTipoRutina;
    }

    public void setIdTipoRutina(int idTipoRutina) {
        this.idTipoRutina = idTipoRutina;
    }

    public String getDiario() {
        return diario;
    }

    public void setDiario(String diario) {
        this.diario = diario;
    }

    public ArrayList<Rutinas> getListaRutinas() {
        return listaRutinas;
    }

    public void setListaRutinas(ArrayList<Rutinas> listaRutinas) {
        this.listaRutinas = listaRutinas;
    }
}
