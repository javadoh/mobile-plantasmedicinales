package com.javadoh.plantasmedicinalesnaturales.utils.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.javadoh.plantasmedicinalesnaturales.io.beans.HierbasBean;

/**
 * Created by luiseliberal on 06/06/16.
 */
public class MemoryBeanAux implements Serializable {

    public static ArrayList<HierbasBean> listaHierbas;
    public static int posicionHierba;
    public static String[] userFbData;
    public static String userFbUlrImage;

    public static ArrayList<HierbasBean> getListaHierbas() {
        if (listaHierbas == null){
            listaHierbas = new ArrayList<>();
        }
        return listaHierbas;
    }

    public static String hierbaImagePath;

    public static void setListaHierbas(ArrayList<HierbasBean> listaHierbas) {
        MemoryBeanAux.listaHierbas = listaHierbas;
    }

    public static int getPosicionHierba() {
        return posicionHierba;
    }

    public static void setPosicionHierba(int posicionHierba) {
        MemoryBeanAux.posicionHierba = posicionHierba;
    }

    public static String getHierbaImagePath() {
        return hierbaImagePath;
    }

    public static void setHierbaImagePath(String hierbaImagePath) {
        MemoryBeanAux.hierbaImagePath = hierbaImagePath;
    }

    public static String[] getUserFbData() {
        return userFbData;
    }

    public static String getUserFbUlrImage() {
        return userFbUlrImage;
    }

    public static void setUserFbUlrImage(String userFbUlrImage) {
        MemoryBeanAux.userFbUlrImage = userFbUlrImage;
    }

    public static void setUserFbData(String[] userFbData) {
        MemoryBeanAux.userFbData = userFbData;
    }
}
