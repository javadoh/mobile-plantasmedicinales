package com.javadoh.plantasmedicinalesnaturales.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import com.javadoh.plantasmedicinalesnaturales.R;
import com.javadoh.plantasmedicinalesnaturales.io.beans.HierbasBean;

/**
 * Created by luiseliberal on 04/06/16.
 */
public class PostAsyncHttpTask extends AsyncTask<String, Void, Integer> {

        private static final String TAG = AsyncHttpTask.class.getName();
        private ProgressBar progressBar;
        private Context context;
        private JSONObject jsonReqObj;
        private HierbasBean hierba;
        private String flagCall;
        private int hierbaId;
        private DialogInterface dialog;
        private String message;

    public PostAsyncHttpTask(Context context, ProgressBar progressBar,
                             String flagCall, JSONObject jsonReqObj,
                             HierbasBean hierba, int hierbaId, DialogInterface dialog){
        this.context = context;
        this.progressBar = progressBar;
        this.flagCall = flagCall;
        this.jsonReqObj = jsonReqObj;
        this.hierba = hierba;
        this.hierbaId = hierbaId;
        this.dialog = dialog;
    }

        @Override
        public void onPreExecute() {

            ((Activity)context).setProgressBarIndeterminateVisibility(true);
        }

        @Override
        public Integer doInBackground(String... params) {
            Integer result;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("charset", "utf-8");
                //MANEJO DE TIMEOUT
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);

                if("ADD_COMMENT".equalsIgnoreCase(flagCall)) {
                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);//HACE SET DE METODO POST IMPLICITAMENTE
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Accept", "application/json");//cambiar por text/plain si siguen errores

                    byte[] outputBytes = jsonReqObj.toString().getBytes("UTF-8");
                    OutputStream os = urlConnection.getOutputStream();
                    os.write(outputBytes);
                    os.flush();
                    os.close();
                }

                int statusCode = urlConnection.getResponseCode();

                // 200 REPRESENTA HTTP OK
                if (statusCode == 200 || statusCode == 202) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    //INVOCACION DE CLASE DE PARSEO GENERAL
                    if("GET_DATA_FACE_WITH_TOKEN".equalsIgnoreCase(flagCall)) {
                        ParseResults parseResults = new ParseResults(null, hierba, context);
                        parseResults.parseFaceUserBeanData(response.toString());
                    }
                    //hierba = parseResults.hierbasBeanCommentParse(hierba, response.toString());

                    result = 1; //EXITOSO

                }
                else {
                    result = 3; //FALLO AL OBTENER LA DATA
                }

            } catch (SocketTimeoutException | ConnectException e)
            {
                e.printStackTrace();
                result = 3;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, e.getLocalizedMessage());
                throw new RuntimeException(context.getString(R.string.errorGral), e.getCause());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            //DESCARGA COMPLETA , HACEMOS UPDATE DE LA UI
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                    if("GET_DATA_FACE_WITH_TOKEN".equalsIgnoreCase(flagCall)){
                        if(dialog != null) {
                            dialog.dismiss();
                        }
                        Toast.makeText(context, context.getString(R.string.facebookDataRecovered), Toast.LENGTH_SHORT).show();
                    }else {
                        if(dialog != null) {
                            dialog.dismiss();
                        }
                        Toast.makeText(context, context.getString(R.string.facebookCommentSucceed), Toast.LENGTH_SHORT).show();
                    }
            } else if(result == 3){
                if("GET_DATA_FACE_WITH_TOKEN".equalsIgnoreCase(flagCall)){
                    Toast.makeText(context, context.getString(R.string.facebookGralError), Toast.LENGTH_SHORT).show();
                    message = "ERROR";
                }else {
                    if(dialog != null) {
                        dialog.dismiss();
                    }
                    Toast.makeText(context, context.getString(R.string.facebookCommentError), Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if("GET_DATA_FACE_WITH_TOKEN".equalsIgnoreCase(flagCall)){
                    Toast.makeText(context, context.getString(R.string.facebookGralError), Toast.LENGTH_SHORT).show();
                    message = "ERROR";
                }else {
                    if(dialog != null) {
                        dialog.dismiss();
                    }
                    Toast.makeText(context, context.getString(R.string.errorGral2), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
