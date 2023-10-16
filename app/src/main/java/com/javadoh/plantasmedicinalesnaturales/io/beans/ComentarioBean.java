package com.javadoh.plantasmedicinalesnaturales.io.beans;

import java.io.Serializable;

/**
 * Created by luiseliberal on 05/06/16.
 */
public class ComentarioBean implements Serializable{

    private int id;
    private String nombreUsuario;
    private String emailUsuario;
    private String comentario;
    private String estado;
    //GOOGLE PLAY SERVICES
    private String ciudad;
    private String pais;
    private String sexoUsuario;
    private String fechaNacUsuario;
    private String imgFbUrlUsuario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getSexoUsuario() {
        return sexoUsuario;
    }

    public void setSexoUsuario(String sexoUsuario) {
        this.sexoUsuario = sexoUsuario;
    }

    public String getFechaNacUsuario() {
        return fechaNacUsuario;
    }

    public void setFechaNacUsuario(String fechaNacUsuario) {
        this.fechaNacUsuario = fechaNacUsuario;
    }

    public String getImgFbUrlUsuario() {
        return imgFbUrlUsuario;
    }

    public void setImgFbUrlUsuario(String imgFbUrlUsuario) {
        this.imgFbUrlUsuario = imgFbUrlUsuario;
    }
}
