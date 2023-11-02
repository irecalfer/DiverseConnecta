package com.calferinnovate.mediconnecta.clases;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PacientesAgrupadosRutinas {
   private int fkIdRutina;
   private String horaRutina, fkCipSns;
   private ArrayList<PacientesAgrupadosRutinas> listaProgramacion;

    public PacientesAgrupadosRutinas() {
    }

    public PacientesAgrupadosRutinas(String fkCipSns, int fkIdRutina, String horaRutina) {
        this.fkCipSns = fkCipSns;
        this.fkIdRutina = fkIdRutina;
        this.horaRutina = horaRutina;
    }

    public String getFkCipSns() {
        return fkCipSns;
    }

    public void setFkCipSns(String fkCipSns) {
        this.fkCipSns = fkCipSns;
    }

    public int getFkIdRutina() {
        return fkIdRutina;
    }

    public void setFkIdRutina(int fkIdRutina) {
        this.fkIdRutina = fkIdRutina;
    }

    public String getHoraRutina() {
        return horaRutina;
    }

    public void setHoraRutina(String horaRutina) {
        this.horaRutina = horaRutina;
    }

    public ArrayList<PacientesAgrupadosRutinas> getListaProgramacion() {
        return listaProgramacion;
    }

    public void setListaProgramacion(ArrayList<PacientesAgrupadosRutinas> listaProgramacion) {
        this.listaProgramacion = listaProgramacion;
    }
}
