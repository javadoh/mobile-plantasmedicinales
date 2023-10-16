package com.javadoh.plantasmedicinalesnaturales.io;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by luiseliberal on 20-09-2015.
 */
public class Constants {

    public static final String URL_SERVIDOR_RMT_APP_HIERBAS = "http://207.244.75.230:8100/";
    public static final String GET_ALL_OPERACION = "hierbas/findAll";
    public static final String GET_HIERBAS_OPERACION = "hierbas/findByName";//?nombre= &localization=ES o EN
    public static final String GET_SINTOMAS_OPERACION = "hierbas/findBySymptom";//?sintoma= &localization=ES o EN
    public static final String INSERT_HIERBAS_OPERACION = "";
    public static final String INSERT_SINTOMAS_OPERACION = "";
    public static final String URL_IMAGEN_PREDEFINIDA = "/drawable/back_herb_predef.png";
    public static final String URL_POST_COMENTARIO_HIERBA = "hierbas/addComment/";
    public static boolean isAdsDisabled;
    public static boolean isInAppSetupCreated;
    public static boolean internetOn;
    public static String[] europeCountries = {"Spain","España","Francia","France","Alemania","Germany", "Deutschland",
            "Holanda","Holland", "Bélgica","Belgium","Italia","Italy","Grecia","Grecce", "Suiza", "Switzerland", "Suecia",
            "Sweden", "Noruega", "Norwey", "Turquía", "Turkey", "Bulgaria", "Croatia", "Croacia", "Austria", "Cyprus",
            "Chipre", "Czech Republic", "República Checa", "Estonia", "Denmark", "Dinamarca", "Finland", "Finlandia",
            "Hungary", "Hungría", "Irlanda", "Ireland", "Latvia", "Lithuania", "Slovakia", "Eslovaquia", "Slovenia",
            "Eslovenia", "Europe", "Europa", "European Union", "Unión Europea"};

    public static final String URL_GRAPH_FACEBOOK_ME_DATA = "https://graph.facebook.com/me?fields=id,name,gender,birthday,email,location&access_token=";
    //APP KEY
    public static final String APP_KEY_ACC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnSbkMq/xVyYeCSEdXxj6AmJ24X9/pb6U6LnfpS1ehan4FdIihHkdWL9kwVxA3lAPgQfGvhkB/VtbO7zeu9C7NTbJKlZ1xZp1+UAFFI8KvwvUKl1/7Pbuxu4oC/+U/OthxO/CAt8Gt3C97E4qHqKkbeIsAciYwMqRQpNt4/SWy37yr4w1qRI34JTu6gDfS/AvOtZneZOKLCd0fLshHvHOuQ0qKFXfjvWXrjkWT5vj5wXaaMuxN+X/EQxLTs7XYU9QbZDpfy1neZOAUoBoG/V1oAT2f0xsxHtWXnw75K5qCKeB5ESOiB1Tv4U+iFgUwnu+xC38cwCIzuyRDwcXBsN95wIDAQAB";
    public static final String REMOVE_ADD_PRODUCT = "remove_ads";
}
