package com.calferinnovate.mediconnecta.clases;

import java.sql.Date;

public class Avisos {
    private int num_aviso;
    private String fecha_aviso;
    private String contenido;

    public Avisos() {
    }

    public Avisos(int num_aviso, String fecha_aviso, String contenido) {
        this.num_aviso = num_aviso;
        this.fecha_aviso = fecha_aviso;
        this.contenido = contenido;
    }

    public int getNum_aviso() {
        return num_aviso;
    }

    public void setNum_aviso(int num_aviso) {
        this.num_aviso = num_aviso;
    }

    public String getFecha_aviso() {
        return fecha_aviso;
    }

    public void setFecha_aviso(String fecha_aviso) {
        this.fecha_aviso = fecha_aviso;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
