package com.calferinnovate.mediconnecta.Model;

public class Habitaciones {

    private int numHabitacion, fkUnidad;

    public Habitaciones() {
    }

    public Habitaciones(int numHabitacion, int fkUnidad) {
        this.numHabitacion = numHabitacion;
        this.fkUnidad = fkUnidad;
    }

    public int getNumHabitacion() {
        return numHabitacion;
    }

    public void setNumHabitacion(int numHabitacion) {
        this.numHabitacion = numHabitacion;
    }

    public int getFkUnidad() {
        return fkUnidad;
    }

    public void setFkUnidad(int fkUnidad) {
        this.fkUnidad = fkUnidad;
    }
}
