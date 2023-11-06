package com.calferinnovate.mediconnecta.clases;

public class Seguro {
    private int idSeguro, telefono;
    private String nombreSeguro;

    public Seguro() {
    }

    public Seguro(int idSeguro, int telefono, String nombreSeguro) {
        this.idSeguro = idSeguro;
        this.telefono = telefono;
        this.nombreSeguro = nombreSeguro;
    }

    public int getIdSeguro() {
        return idSeguro;
    }

    public void setIdSeguro(int idSeguro) {
        this.idSeguro = idSeguro;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNombreSeguro() {
        return nombreSeguro;
    }

    public void setNombreSeguro(String nombreSeguro) {
        this.nombreSeguro = nombreSeguro;
    }
}
