package com.calferinnovate.mediconnecta.clases;

import java.util.ArrayList;

public class Pacientes {
    private String foto, cip_sns, num_seguridad_social, nombre, apellidos, fecha_nacimiento, dni, estado_civil, lugar_nacimiento, sexo;
    private int fk_id_seguro, fk_num_habitacion, fk_num_historia_clinica, fk_id_pauta_geriatria, fk_id_pauta_salud_mental, fk_id_unidad, fk_id_rutina_anatomicos;
    private ArrayList<Pacientes> listaPacientesUnidad;

    public Pacientes() {
    }

    public Pacientes(String foto, String cip_sns, String num_seguridad_social, String nombre, String apellidos, String fecha_nacimiento, String dni, String estado_civil, String lugar_nacimiento, String sexo, int fk_id_seguro, int fk_num_habitacion, int fk_num_historia_clinica, int fk_id_pauta_geriatria, int fk_id_pauta_salud_mental, int fk_id_unidad, int fk_id_rutina_anatomicos, ArrayList<Pacientes> listaPacientesUnidad) {
        this.foto = foto;
        this.cip_sns = cip_sns;
        this.num_seguridad_social = num_seguridad_social;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fecha_nacimiento = fecha_nacimiento;
        this.dni = dni;
        this.estado_civil = estado_civil;
        this.lugar_nacimiento = lugar_nacimiento;
        this.sexo = sexo;
        this.fk_id_seguro = fk_id_seguro;
        this.fk_num_habitacion = fk_num_habitacion;
        this.fk_num_historia_clinica = fk_num_historia_clinica;
        this.fk_id_pauta_geriatria = fk_id_pauta_geriatria;
        this.fk_id_pauta_salud_mental = fk_id_pauta_salud_mental;
        this.fk_id_unidad = fk_id_unidad;
        this.fk_id_rutina_anatomicos = fk_id_rutina_anatomicos;
        this.listaPacientesUnidad = listaPacientesUnidad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCip_sns() {
        return cip_sns;
    }

    public void setCip_sns(String cip_sns) {
        this.cip_sns = cip_sns;
    }

    public String getNum_seguridad_social() {
        return num_seguridad_social;
    }

    public void setNum_seguridad_social(String num_seguridad_social) {
        this.num_seguridad_social = num_seguridad_social;
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

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
    }

    public String getLugar_nacimiento() {
        return lugar_nacimiento;
    }

    public void setLugar_nacimiento(String lugar_nacimiento) {
        this.lugar_nacimiento = lugar_nacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getFk_id_seguro() {
        return fk_id_seguro;
    }

    public void setFk_id_seguro(int fk_id_seguro) {
        this.fk_id_seguro = fk_id_seguro;
    }

    public int getFk_num_habitacion() {
        return fk_num_habitacion;
    }

    public void setFk_num_habitacion(int fk_num_habitacion) {
        this.fk_num_habitacion = fk_num_habitacion;
    }

    public int getFk_num_historia_clinica() {
        return fk_num_historia_clinica;
    }

    public void setFk_num_historia_clinica(int fk_num_historia_clinica) {
        this.fk_num_historia_clinica = fk_num_historia_clinica;
    }

    public int getFk_id_pauta_geriatria() {
        return fk_id_pauta_geriatria;
    }

    public void setFk_id_pauta_geriatria(int fk_id_pauta_geriatria) {
        this.fk_id_pauta_geriatria = fk_id_pauta_geriatria;
    }

    public int getFk_id_pauta_salud_mental() {
        return fk_id_pauta_salud_mental;
    }

    public void setFk_id_pauta_salud_mental(int fk_id_pauta_salud_mental) {
        this.fk_id_pauta_salud_mental = fk_id_pauta_salud_mental;
    }

    public int getFk_id_unidad() {
        return fk_id_unidad;
    }

    public void setFk_id_unidad(int fk_id_unidad) {
        this.fk_id_unidad = fk_id_unidad;
    }

    public int getFk_id_rutina_anatomicos() {
        return fk_id_rutina_anatomicos;
    }

    public void setFk_id_rutina_anatomicos(int fk_id_rutina_anatomicos) {
        this.fk_id_rutina_anatomicos = fk_id_rutina_anatomicos;
    }

    public ArrayList<Pacientes> getListaPacientesUnidad() {
        return listaPacientesUnidad;
    }

    public void setListaPacientesUnidad(ArrayList<Pacientes> listaPacientesUnidad) {
        this.listaPacientesUnidad = listaPacientesUnidad;
    }
}
