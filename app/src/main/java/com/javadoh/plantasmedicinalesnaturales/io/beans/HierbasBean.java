package com.javadoh.plantasmedicinalesnaturales.io.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luiseliberal on 13-09-2015.
 */
public class HierbasBean implements Serializable{

    private int id;
    private String nombre;
    private String nombrecientifico;
    private List<String> alias;
    private String descripcion;
    private String seccion;
    private List<String> propiedades;
    private String indicaciones;
    private String contraindicaciones;
    private String empleo;
    private List<String> sintomas;
    private String ubicacion;
    private String imgurl;
    private String estado;
    private String gastronomia;
    private String localization;
    private ArrayList<ComentarioBean> comentarios;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(List<String> propiedades) {
        this.propiedades = propiedades;
    }

    public String getEmpleo() {
        return empleo;
    }

    public void setEmpleo(String empleo) {
        this.empleo = empleo;
    }

    public List<String> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<String> sintomas) {
        this.sintomas = sintomas;
    }

    public List<String> getPropiedadesHierba() {
        if(propiedades == null){
            propiedades = new ArrayList<String>();
        }
        return propiedades;
    }

    public void setPropiedadesHierba(List<String> propiedades) {
        this.propiedades = propiedades;
    }


    public List<String> getSintomasHierba() {
        if(sintomas == null){
            sintomas = new ArrayList<String>();
        }
        return sintomas;
    }

    public void setSintomasHierba(List<String> sintomas) {
        this.sintomas = sintomas;
    }

    public List<String> getAlias() {
        if(alias == null){
            alias = new ArrayList<String>();
        }
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreCientifico() {
        return nombrecientifico;
    }

    public void setNombreCientifico(String nombrecientifico) {
        this.nombrecientifico = nombrecientifico;
    }

    public String getContraIndicaciones() {
        return contraindicaciones;
    }

    public void setContraIndicaciones(String contraindicaciones) {
        this.contraindicaciones = contraindicaciones;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getGastronomia() {
        return gastronomia;
    }

    public void setGastronomia(String gastronomia) {
        this.gastronomia = gastronomia;
    }

    public ArrayList<ComentarioBean> getComentarios() {

        if(comentarios == null){
            comentarios = new ArrayList<>();
        }
        return comentarios;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public void setComentarios(ArrayList<ComentarioBean> comentarios) {
        if(comentarios == null){
            comentarios = new ArrayList<>();
        }
        this.comentarios = comentarios;
    }
}
