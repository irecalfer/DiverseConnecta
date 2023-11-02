package com.calferinnovate.mediconnecta.clases;

import java.util.ArrayList;

public class Pacientes {
   private String nombre, apellidos, foto, fechaNacimiento, dni, lugarNacimiento, sexo, cipSns;
   private int numSeguridadSocial, fkIdUnidad, fkIdSeguro, fkNumHabitacion, fkNumHistoriaClinica;
   private ArrayList<Pacientes> listaPacientes;

    public Pacientes() {
    }

    public Pacientes(String nombre, String apellidos, String foto, String fechaNacimiento, String dni, String lugarNacimiento,
                     String sexo, String cipSns, int numSeguridadSocial, int fkIdUnidad, int fkIdSeguro, int fkNumHabitacion,
                     int fkNumHistoriaClinica) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.foto = foto;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.lugarNacimiento = lugarNacimiento;
        this.sexo = sexo;
        this.cipSns = cipSns;
        this.numSeguridadSocial = numSeguridadSocial;
        this.fkIdUnidad = fkIdUnidad;
        this.fkIdSeguro = fkIdSeguro;
        this.fkNumHabitacion = fkNumHabitacion;
        this.fkNumHistoriaClinica = fkNumHistoriaClinica;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCipSns() {
        return cipSns;
    }

    public void setCipSns(String cipSns) {
        this.cipSns = cipSns;
    }

    public int getNumSeguridadSocial() {
        return numSeguridadSocial;
    }

    public void setNumSeguridadSocial(int numSeguridadSocial) {
        this.numSeguridadSocial = numSeguridadSocial;
    }

    public int getFkIdUnidad() {
        return fkIdUnidad;
    }

    public void setFkIdUnidad(int fkIdUnidad) {
        this.fkIdUnidad = fkIdUnidad;
    }

    public int getFkIdSeguro() {
        return fkIdSeguro;
    }

    public void setFkIdSeguro(int fkIdSeguro) {
        this.fkIdSeguro = fkIdSeguro;
    }

    public int getFkNumHabitacion() {
        return fkNumHabitacion;
    }

    public void setFkNumHabitacion(int fkNumHabitacion) {
        this.fkNumHabitacion = fkNumHabitacion;
    }

    public int getFkNumHistoriaClinica() {
        return fkNumHistoriaClinica;
    }

    public void setFkNumHistoriaClinica(int fkNumHistoriaClinica) {
        this.fkNumHistoriaClinica = fkNumHistoriaClinica;
    }

    public ArrayList<Pacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(ArrayList<Pacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }
}
