package com.calferinnovate.mediconnecta.clases;

public class Area {
    private int id_area;
    private String nombre;

    public Area() {
    }

    public Area(int id_area, String nombre) {
        this.id_area = id_area;
        this.nombre = nombre;
    }

    public int getId_area() {
        return id_area;
    }

    public void setId_area(int id_area) {
        this.id_area = id_area;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
