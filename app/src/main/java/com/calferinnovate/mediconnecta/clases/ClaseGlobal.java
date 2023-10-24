package com.calferinnovate.mediconnecta.clases;

import android.app.Application;

public class ClaseGlobal extends Application {
    public Empleado empleado;
    public Unidades unidades;
    public Area area;
    @Override public void onCreate(){
        super.onCreate();
        empleado = new Empleado();
        unidades = new Unidades();
        area = new Area();

    }
}
