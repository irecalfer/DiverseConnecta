package com.calferinnovate.mediconnecta.clases;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;

// El objetivo de esta clase es que cuadno el usuario se loguee, recuperar el nombre del empleado,
// guardarlo en la variable name y poder consultarlo desde otras activities.
public class Empleado{

    private String user,pass,nombre, apellidos, nombreCargo;
    private int cod_empleado, fk_cargo;
    private Blob foto;


    public Empleado() {
    }

    public Empleado(String user, String pass, String nombre, String apellidos, String nombreCargo, int cod_empleado, int fk_cargo, Blob foto) {
        this.user = user;
        this.pass = pass;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nombreCargo = nombreCargo;
        this.cod_empleado = cod_empleado;
        this.fk_cargo = fk_cargo;
        this.foto = foto;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public int getCod_empleado() {
        return cod_empleado;
    }

    public void setCod_empleado(int cod_empleado) {
        this.cod_empleado = cod_empleado;
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

    public int getFk_cargo() {
        return fk_cargo;
    }

    public void setFk_cargo(int fk_cargo) {
        this.fk_cargo = fk_cargo;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public Blob getFoto() {
        return foto;
    }

    public void setFoto(Blob foto) {
        this.foto = foto;
    }


}
