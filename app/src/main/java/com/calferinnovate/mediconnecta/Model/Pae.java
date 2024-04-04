package com.calferinnovate.mediconnecta.Model;

public class Pae {

    private int idPae, cursoEmision, cursoEmisionFin, fkIdEnfermero, fkIdProfesor, fkAula, fkIdAlumno;

    private String alergias, diagnosticoClinico, fiebre, dieta, protesis, ortesis, gafas, audifonos, otros,
    medicacion;

    public Pae() {
    }

    public Pae(int idPae, int cursoEmision, int cursoEmisionFin,int fkIdEnfermero, int fkIdProfesor, int fkIdAlumno,
               String alergias, String diagnosticoClinico, String fiebre, String dieta, String protesis,
               String ortesis, String gafas, String audifonos, String otros, String medicacion) {
        this.idPae = idPae;
        this.cursoEmision = cursoEmision;
        this.cursoEmisionFin = cursoEmisionFin;
        this.fkIdEnfermero = fkIdEnfermero;
        this.fkIdProfesor = fkIdProfesor;
        this.fkIdAlumno = fkIdAlumno;
        this.alergias = alergias;
        this.diagnosticoClinico = diagnosticoClinico;
        this.fiebre = fiebre;
        this.dieta = dieta;
        this.protesis = protesis;
        this.ortesis = ortesis;
        this.gafas = gafas;
        this.audifonos = audifonos;
        this.otros = otros;
        this.medicacion = medicacion;
    }

    public int getIdPae() {
        return idPae;
    }

    public void setIdPae(int idPae) {
        this.idPae = idPae;
    }

    public int getCursoEmision() {
        return cursoEmision;
    }

    public void setCursoEmision(int cursoEmision) {
        this.cursoEmision = cursoEmision;
    }

    public int getFkIdEnfermero() {
        return fkIdEnfermero;
    }

    public void setFkIdEnfermero(int fkIdEnfermero) {
        this.fkIdEnfermero = fkIdEnfermero;
    }

    public int getFkIdProfesor() {
        return fkIdProfesor;
    }

    public void setFkIdProfesor(int fkIdProfesor) {
        this.fkIdProfesor = fkIdProfesor;
    }

    public int getFkAula() {
        return fkAula;
    }

    public void setFkAula(int fkAula) {
        this.fkAula = fkAula;
    }

    public int getFkIdAlumno() {
        return fkIdAlumno;
    }

    public void setFkIdAlumno(int fkIdAlumno) {
        this.fkIdAlumno = fkIdAlumno;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getDiagnosticoClinico() {
        return diagnosticoClinico;
    }

    public void setDiagnosticoClinico(String diagnosticoClinico) {
        this.diagnosticoClinico = diagnosticoClinico;
    }

    public String getFiebre() {
        return fiebre;
    }

    public void setFiebre(String fiebre) {
        this.fiebre = fiebre;
    }

    public String getDieta() {
        return dieta;
    }

    public void setDieta(String dieta) {
        this.dieta = dieta;
    }

    public String getProtesis() {
        return protesis;
    }

    public void setProtesis(String protesis) {
        this.protesis = protesis;
    }

    public String getOrtesis() {
        return ortesis;
    }

    public void setOrtesis(String ortesis) {
        this.ortesis = ortesis;
    }

    public String getGafas() {
        return gafas;
    }

    public void setGafas(String gafas) {
        this.gafas = gafas;
    }

    public String getAudifonos() {
        return audifonos;
    }

    public void setAudifonos(String audifonos) {
        this.audifonos = audifonos;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public String getMedicacion() {
        return medicacion;
    }

    public void setMedicacion(String medicacion) {
        this.medicacion = medicacion;
    }

    public int getCursoEmisionFin() {
        return cursoEmisionFin;
    }

    public void setCursoEmisionFin(int cursoEmisionFin) {
        this.cursoEmisionFin = cursoEmisionFin;
    }
}
