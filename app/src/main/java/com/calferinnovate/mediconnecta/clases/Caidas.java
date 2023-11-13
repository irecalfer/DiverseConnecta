package com.calferinnovate.mediconnecta.clases;

public class Caidas {
    private String fechaHora, nombreApellidosPaciente, lugarCaida, factoresRiesgo, causas,
            circunstancias, consecuencias, unidad, caidaPresenciada, avisadoFamiliares, observaciones,
            empleadoNombreApellido;

    public Caidas() {
    }

    public Caidas(String fechaHora, String nombreApellidosPaciente, String lugarCaida, String factoresRiesgo,
                  String causas, String circunstancias, String consecuencias, String unidad, String caidaPresenciada,
                  String avisadoFamiliares, String observaciones, String empleadoNombreApellido) {
        this.fechaHora = fechaHora;
        this.nombreApellidosPaciente = nombreApellidosPaciente;
        this.lugarCaida = lugarCaida;
        this.factoresRiesgo = factoresRiesgo;
        this.causas = causas;
        this.circunstancias = circunstancias;
        this.consecuencias = consecuencias;
        this.unidad = unidad;
        this.caidaPresenciada = caidaPresenciada;
        this.avisadoFamiliares = avisadoFamiliares;
        this.observaciones = observaciones;
        this.empleadoNombreApellido = empleadoNombreApellido;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNombreApellidosPaciente() {
        return nombreApellidosPaciente;
    }

    public void setNombreApellidosPaciente(String nombreApellidosPaciente) {
        this.nombreApellidosPaciente = nombreApellidosPaciente;
    }

    public String getLugarCaida() {
        return lugarCaida;
    }

    public void setLugarCaida(String lugarCaida) {
        this.lugarCaida = lugarCaida;
    }

    public String getFactoresRiesgo() {
        return factoresRiesgo;
    }

    public void setFactoresRiesgo(String factoresRiesgo) {
        this.factoresRiesgo = factoresRiesgo;
    }

    public String getCausas() {
        return causas;
    }

    public void setCausas(String causas) {
        this.causas = causas;
    }

    public String getCircunstancias() {
        return circunstancias;
    }

    public void setCircunstancias(String circunstancias) {
        this.circunstancias = circunstancias;
    }

    public String getConsecuencias() {
        return consecuencias;
    }

    public void setConsecuencias(String consecuencias) {
        this.consecuencias = consecuencias;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getCaidaPresenciada() {
        return caidaPresenciada;
    }

    public void setCaidaPresenciada(String caidaPresenciada) {
        this.caidaPresenciada = caidaPresenciada;
    }

    public String getAvisadoFamiliares() {
        return avisadoFamiliares;
    }

    public void setAvisadoFamiliares(String avisadoFamiliares) {
        this.avisadoFamiliares = avisadoFamiliares;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEmpleadoNombreApellido() {
        return empleadoNombreApellido;
    }

    public void setEmpleadoNombreApellido(String empleadoNombreApellido) {
        this.empleadoNombreApellido = empleadoNombreApellido;
    }
}
