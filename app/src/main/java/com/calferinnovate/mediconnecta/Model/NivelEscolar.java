package com.calferinnovate.mediconnecta.Model;

public class NivelEscolar {

    private int idNivelEscolar;
    private String nombreNivel;

    public NivelEscolar() {
    }

    public NivelEscolar(int idNivelEscolar, String nombreNivel) {
        this.idNivelEscolar = idNivelEscolar;
        this.nombreNivel = nombreNivel;
    }

    public int getIdNivelEscolar() {
        return idNivelEscolar;
    }

    public void setIdNivelEscolar(int idNivelEscolar) {
        this.idNivelEscolar = idNivelEscolar;
    }

    public String getNombreNivel() {
        return nombreNivel;
    }

    public void setNombreNivel(String nombreNivel) {
        this.nombreNivel = nombreNivel;
    }
}
