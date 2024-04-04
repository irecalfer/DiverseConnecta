package com.calferinnovate.mediconnecta.Model;

public class ControlSomatometrico {

    private int idControl, frecuenciaCardiaca, saturacionO2, fkTrimestre, fkIdPae;

    private double peso, imc, percentil, temperatura;

    private String talla, tensionArterial;

    public ControlSomatometrico() {
    }

    public ControlSomatometrico(int idControl, int frecuenciaCardiaca, int saturacionO2, int fkTrimestre,
                                int fkIdPae, double peso, double imc, double percentil, double temperatura,
                                String talla, String tensionArterial) {
        this.idControl = idControl;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.saturacionO2 = saturacionO2;
        this.fkTrimestre = fkTrimestre;
        this.fkIdPae = fkIdPae;
        this.peso = peso;
        this.imc = imc;
        this.percentil = percentil;
        this.temperatura = temperatura;
        this.talla = talla;
        this.tensionArterial = tensionArterial;
    }



    public int getIdControl() {
        return idControl;
    }

    public void setIdControl(int idControl) {
        this.idControl = idControl;
    }

    public int getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(int frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public int getSaturacionO2() {
        return saturacionO2;
    }

    public void setSaturacionO2(int saturacionO2) {
        this.saturacionO2 = saturacionO2;
    }

    public int getFkTrimestre() {
        return fkTrimestre;
    }

    public void setFkTrimestre(int fkTrimestre) {
        this.fkTrimestre = fkTrimestre;
    }

    public int getFkIdPae() {
        return fkIdPae;
    }

    public void setFkIdPae(int fkIdPae) {
        this.fkIdPae = fkIdPae;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public double getPercentil() {
        return percentil;
    }

    public void setPercentil(double percentil) {
        this.percentil = percentil;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getTensionArterial() {
        return tensionArterial;
    }

    public void setTensionArterial(String tensionArterial) {
        this.tensionArterial = tensionArterial;
    }
}
