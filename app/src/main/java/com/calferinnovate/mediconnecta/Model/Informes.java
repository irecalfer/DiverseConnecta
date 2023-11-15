package com.calferinnovate.mediconnecta.Model;

public class Informes {
    private int numHistoriaClinica;
    private String tipoInforme, fecha, centro, responsable, servicioUnidadDispositivo, servicioDeSalud;
    private byte[] pdfBytes; // El PDF en forma de bytes

    public Informes() {
    }

    public Informes(int numHistoriaClinica, String tipoInforme, String fecha, String centro, String responsable,
                    String servicioUnidadDispositivo, String servicioDeSalud, byte[] pdfBytes) {
        this.numHistoriaClinica = numHistoriaClinica;
        this.tipoInforme = tipoInforme;
        this.fecha = fecha;
        this.centro = centro;
        this.responsable = responsable;
        this.servicioUnidadDispositivo = servicioUnidadDispositivo;
        this.servicioDeSalud = servicioDeSalud;
        this.pdfBytes = pdfBytes;
    }

    public int getNumHistoriaClinica() {
        return numHistoriaClinica;
    }

    public void setNumHistoriaClinica(int numHistoriaClinica) {
        this.numHistoriaClinica = numHistoriaClinica;
    }

    public String getTipoInforme() {
        return tipoInforme;
    }

    public void setTipoInforme(String tipoInforme) {
        this.tipoInforme = tipoInforme;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getServicioUnidadDispositivo() {
        return servicioUnidadDispositivo;
    }

    public void setServicioUnidadDispositivo(String servicioUnidadDispositivo) {
        this.servicioUnidadDispositivo = servicioUnidadDispositivo;
    }

    public String getServicioDeSalud() {
        return servicioDeSalud;
    }

    public void setServicioDeSalud(String servicioDeSalud) {
        this.servicioDeSalud = servicioDeSalud;
    }

    public byte[] getPdfBytes() {
        return pdfBytes;
    }

    public void setPdfBytes(byte[] pdfBytes) {
        this.pdfBytes = pdfBytes;
    }
}
