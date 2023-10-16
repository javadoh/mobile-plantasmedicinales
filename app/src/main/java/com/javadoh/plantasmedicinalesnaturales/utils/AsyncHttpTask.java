package com.javadoh.plantasmedicinalesnaturales.utils;

/**
 * Created by luiseliberal on 27-09-2015.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import com.javadoh.plantasmedicinalesnaturales.R;
import com.javadoh.plantasmedicinalesnaturales.io.beans.HierbasBean;
import com.javadoh.plantasmedicinalesnaturales.ui.adapters.BusquedaRespuestaAdapter;

public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

    private static final String TAG = AsyncHttpTask.class.getName();
    ProgressBar progressBar;
    Context context;
    BusquedaRespuestaAdapter adapter;
    RecyclerView mRecyclerView;
    private ArrayList<HierbasBean> hierbasList;
    ImageView imgNoData, imgNoConex;
    String dataUser[];

    public AsyncHttpTask(Context context, RecyclerView mRecyclerView, BusquedaRespuestaAdapter adapter,
                         ProgressBar progressBar, ArrayList<HierbasBean> hierbasList, ImageView imgNoData,
                         ImageView imgNoConex, String[] dataUser){
        this.context = context;
        this.mRecyclerView = mRecyclerView;
        this.adapter = adapter;
        this.progressBar = progressBar;
        this.imgNoData = imgNoData;
        this.imgNoConex = imgNoConex;
        this.hierbasList = hierbasList;
        this.dataUser = dataUser;
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

            //MANEJO DE TIMEOUT
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");

            int statusCode = urlConnection.getResponseCode();

            // 200 REPRESENTA HTTP OK
            if (statusCode == 200) {
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    response.append(line);
                }

                //INVOCACION DE CLASE DE PARSEO GENERAL
                ParseResults parseResults = new ParseResults(hierbasList, null, context);
                hierbasList = parseResults.parseResult(response.toString());

                result = 1; //EXITOSO

                if (hierbasList.size() == 0){
                   result = 2;
                }

            } else {
                result = 3; //FALLO AL OBTENER LA DATA
            }

        } catch (SocketTimeoutException | SocketException e)
        {
            e.printStackTrace();
            result = 3;

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, ex.getLocalizedMessage());
            throw new RuntimeException(context.getString(R.string.errorGral), ex.getCause());
        }
        return result; //"Failed to fetch data!";
    }

    @Override
    protected void onPostExecute(Integer result) {
        //DESCARGA COMPLETA , HACEMOS UPDATE DE LA UI
        progressBar.setVisibility(View.GONE);

        if (result == 1) {
            adapter = new BusquedaRespuestaAdapter(context, hierbasList, dataUser);
            mRecyclerView.setAdapter(adapter);
        } else if(result == 2){
            imgNoData.setVisibility(View.VISIBLE);
            Toast.makeText(context, context.getString(R.string.errorSinResultados), Toast.LENGTH_SHORT).show();
        }else if(result == 3){
            imgNoConex.setVisibility(View.VISIBLE);
            Toast.makeText(context, context.getString(R.string.errorSinConexion), Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(context, context.getString(R.string.errorGral2), Toast.LENGTH_SHORT).show();
        }
    }

}