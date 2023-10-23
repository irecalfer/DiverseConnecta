package com.calferinnovate.mediconnecta.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

// El objetivo de esta clase es que cuadno el usuario se loguee, recuperar el nombre del empleado,
// guardarlo en la variable name y poder consultarlo desde otras activities.
public class Empleado implements Parcelable {

    private String user,pass,nombre, apellidos;
    private int cod_empleado, fk_cargo;

    public ArrayList<Empleado> listaEmpleados= new ArrayList<>();

    public Empleado() {
    }

    public Empleado(String nombre, String apellidos, int cod_empleado, int fk_cargo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cod_empleado = cod_empleado;
        this.fk_cargo = fk_cargo;
    }

    protected Empleado(Parcel in) {
        user = in.readString();
        pass = in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        cod_empleado = in.readInt();
        fk_cargo = in.readInt();
        listaEmpleados = in.createTypedArrayList(Empleado.CREATOR);
    }

    public static final Creator<Empleado> CREATOR = new Creator<Empleado>() {
        @Override
        public Empleado createFromParcel(Parcel in) {
            return new Empleado(in);
        }

        @Override
        public Empleado[] newArray(int size) {
            return new Empleado[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(user);
        dest.writeString(pass);
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeInt(cod_empleado);
        dest.writeInt(fk_cargo);
        dest.writeTypedList(listaEmpleados);
    }
}
