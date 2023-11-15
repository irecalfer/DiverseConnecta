package com.calferinnovate.mediconnecta.Model;

public class Pautas {
    private String tipoPauta, observaciones, mañana, tarde, noche;

    public Pautas() {
    }

    public Pautas(String tipoPauta, String observaciones, String mañana, String tarde, String noche) {
        this.tipoPauta = tipoPauta;
        this.observaciones = observaciones;
        this.mañana = mañana;
        this.tarde = tarde;
        this.noche = noche;
    }

    public String getTipoPauta() {
        return tipoPauta;
    }

    public void setTipoPauta(String tipoPauta) {
        this.tipoPauta = tipoPauta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getMañana() {
        return mañana;
    }

    public void setMañana(String mañana) {
        this.mañana = mañana;
    }

    public String getTarde() {
        return tarde;
    }

    public void setTarde(String tarde) {
        this.tarde = tarde;
    }

    public String getNoche() {
        return noche;
    }

    public void setNoche(String noche) {
        this.noche = noche;
    }
}
