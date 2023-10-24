package com.calferinnovate.mediconnecta.clases;

import android.app.Application;

public class ClaseGlobal extends Application {
    public Empleado empleado;
    public Unidades unidades;
    @Override public void onCreate(){
        super.onCreate();
        empleado = new Empleado();
        unidades = new Unidades();

    }
}
