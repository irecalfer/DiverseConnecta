package com.calferinnovate.mediconnecta.Model;

public class ContactoFamiliares {
    private String dniFamiliar, nombre, apellidos;
    private int telefono1, telefono2;

    public ContactoFamiliares() {
    }

    public ContactoFamiliares(String dniFamiliar, String nombre, String apellidos, int telefono1, int telefono2) {
        this.dniFamiliar = dniFamiliar;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
    }

    public String getDniFamiliar() {
        return dniFamiliar;
    }

    public void setDniFamiliar(String dniFamiliar) {
        this.dniFamiliar = dniFamiliar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(int telefono1) {
        this.telefono1 = telefono1;
    }

    public int getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(int telefono2) {
        this.telefono2 = telefono2;
    }
}
