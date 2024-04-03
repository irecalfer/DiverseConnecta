package com.calferinnovate.mediconnecta.Model;

public class Alumnos {
   private String nombre, apellidos, foto, fechaNacimiento, dni, sexo, gradoDiscapacidad;
   private int idAlumno, fkProfesor, fkAula;

    public Alumnos() {
    }

    public Alumnos(String nombre, String apellidos, String foto, String fechaNacimiento, String dni,
                   String sexo, String gradoDiscapacidad, int idAlumno, int fkProfesor, int fkAula) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.foto = foto;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.sexo = sexo;
        this.gradoDiscapacidad = gradoDiscapacidad;
        this.idAlumno = idAlumno;
        this.fkProfesor = fkProfesor;
        this.fkAula = fkAula;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getGradoDiscapacidad() {
        return gradoDiscapacidad;
    }

    public void setGradoDiscapacidad(String gradoDiscapacidad) {
        this.gradoDiscapacidad = gradoDiscapacidad;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getFkProfesor() {
        return fkProfesor;
    }

    public void setFkProfesor(int fkProfesor) {
        this.fkProfesor = fkProfesor;
    }

    public int getFkAula() {
        return fkAula;
    }

    public void setFkAula(int fkAula) {
        this.fkAula = fkAula;
    }
}
