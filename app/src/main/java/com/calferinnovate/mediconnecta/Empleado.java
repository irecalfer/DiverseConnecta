package com.calferinnovate.mediconnecta;

// El objetivo de esta clase es que cuadno el usuario se loguee, recuperar el nombre del empleado,
// guardarlo en la variable name y poder consultarlo desde otras activities.
public class Empleado {

    private String user,pass,name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
