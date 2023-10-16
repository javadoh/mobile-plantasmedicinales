package com.javadoh.plantasmedicinalesnaturales.utils;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.javadoh.plantasmedicinalesnaturales.R;
import com.javadoh.plantasmedicinalesnaturales.io.beans.FacebookUserBean;
import com.javadoh.plantasmedicinalesnaturales.io.beans.HierbasBean;
import com.javadoh.plantasmedicinalesnaturales.utils.bean.MemoryBeanAux;

/**
 * Created by luiseliberal on 27-09-2015.
 */
public class ParseResults {

    private static final String TAG = ParseResults.class.getName();
    private Context context;
    private HierbasBean hierbaBean;
    static ArrayList<HierbasBean> hierbasList;
    private FacebookUserBean faceUserBean;

    public ParseResults(ArrayList<HierbasBean> listaHierbas, HierbasBean hierbasBean, Context context){
        this.hierbasList = listaHierbas;
        this.hierbaBean = hierbasBean;
        this.context = context;
    }

    public ArrayList<HierbasBean> parseResult(String result) {

        try {
            hierbasList = new ArrayList<>();
            Type listType = new TypeToken<ArrayList<HierbasBean>>() {}.getType();
            hierbasList = new Gson().fromJson(result.toString(), listType);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, context.getString(R.string.errorGral), e);
        }

        return hierbasList;
    }

    public void parseFaceUserBeanData (String result){

        String[] userFbData = new String[6];
        try {

            Type listType = new TypeToken<FacebookUserBean>() {}.getType();
            faceUserBean = new Gson().fromJson(result.toString(), listType);

            userFbData[0] = faceUserBean.getName() != null ? faceUserBean.getName() : context.getString(R.string.noUserName);
            userFbData[1] = faceUserBean.getGender() != null ? faceUserBean.getGender() : context.getString(R.string.noGender);
            userFbData[2] = faceUserBean.getBirthday() != null ? faceUserBean.getBirthday()  : context.getString(R.string.noBirthday);
            userFbData[3] = faceUserBean.getEmail() != null ? faceUserBean.getEmail() : context.getString(R.string.noEmail);
            if(faceUserBean.getLocation() != null) {
                if(faceUserBean.getLocation().getName() != null) {
                    String location = faceUserBean.getLocation().getName().trim() != "" ? faceUserBean.getLocation().getName() : context.getString(R.string.noCountry);
                    userFbData[4] = location.substring(0, location.indexOf(","));
                    userFbData[5] = location.substring(location.lastIndexOf(",") + 1);
                }
            }else{
                userFbData[4] = context.getString(R.string.noCity);
                userFbData[5] = context.getString(R.string.noCountry);
            }
            //SETEAMOS LA VARIABLE DE MEMORIA
            MemoryBeanAux.setUserFbData(userFbData);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, context.getString(R.string.errorGral), e);
        }
    }
}