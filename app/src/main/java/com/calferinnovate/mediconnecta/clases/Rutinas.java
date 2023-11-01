package com.calferinnovate.mediconnecta.clases;

public class Rutinas {
    private String nombre, apellidos, horaRutina, diario;

    public Rutinas() {
    }

    public Rutinas(String nombre, String apellidos, String horaRutina, String diario) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.horaRutina = horaRutina;
        this.diario = diario;
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

    public String getHoraRutina() {
        return horaRutina;
    }

    public void setHoraRutina(String horaRutina) {
        this.horaRutina = horaRutina;
    }

    public String getDiario() {
        return diario;
    }

    public void setDiario(String diario) {
        this.diario = diario;
    }


}
