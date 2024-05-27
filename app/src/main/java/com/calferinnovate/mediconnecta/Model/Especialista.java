package com.calferinnovate.mediconnecta.Model;

public class Especialista {

    private int idDoctor, idAlumno;
    private String nombre, centro, especialidad, telefono, email;

    public Especialista() {
    }

    public Especialista(int idDoctor, String nombre, String centro, String especialidad, String telefono,
                        String email, int idAlumno) {
        this.idDoctor = idDoctor;
        this.nombre = nombre;
        this.centro = centro;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.email = email;
        this.idAlumno = idAlumno;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }
}
