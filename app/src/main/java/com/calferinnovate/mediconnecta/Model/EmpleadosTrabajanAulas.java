package com.calferinnovate.mediconnecta.Model;

public class EmpleadosTrabajanAulas {

    private int idRelacion, fkCodEmpleado, fkAula;

    public EmpleadosTrabajanAulas() {
    }

    public EmpleadosTrabajanAulas(int idRelacion, int fkCodEmpleado, int fkAula) {
        this.idRelacion = idRelacion;
        this.fkCodEmpleado = fkCodEmpleado;
        this.fkAula = fkAula;
    }

    public int getIdRelacion() {
        return idRelacion;
    }

    public void setIdRelacion(int idRelacion) {
        this.idRelacion = idRelacion;
    }

    public int getFkCodEmpleado() {
        return fkCodEmpleado;
    }

    public void setFkCodEmpleado(int fkCodEmpleado) {
        this.fkCodEmpleado = fkCodEmpleado;
    }

    public int getFkAula() {
        return fkAula;
    }

    public void setFkAula(int fkAula) {
        this.fkAula = fkAula;
    }
}
