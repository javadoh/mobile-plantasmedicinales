package com.javadoh.plantasmedicinalesnaturales.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.javadoh.plantasmedicinalesnaturales.R;
import com.javadoh.plantasmedicinalesnaturales.io.Constants;

import java.io.IOException;


/**
 * Created by luiseliberal on 20/06/16.
 */
public class Presentation extends Activity {

    public static final String TAG = Presentation.class.getName();
    private ImageView imagePpal;
    private Animation animation, animationEnd;
    private Context context;
    //SONIDO
    final MediaPlayer player = new MediaPlayer();
    //IN APP BILLING STORE
    //GoogleInAppPayUtils inAppPayApi = new GoogleInAppPayUtils(Presentation.this);
    //LICENCIA
    /*private static final String BASE64_PUBLIC_KEY_PLANTS = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnSbkMq/xVyYeCSEdXxj6AmJ24X9/pb6U6LnfpS1ehan4FdIihHkdWL9kwVxA3lAPgQfGvhkB/VtbO7zeu9C7NTbJKlZ1xZp1+UAFFI8KvwvUKl1/7Pbuxu4oC/+U/OthxO/CAt8Gt3C97E4qHqKkbeIsAciYwMqRQpNt4/SWy37yr4w1qRI34JTu6gDfS/AvOtZneZOKLCd0fLshHvHOuQ0qKFXfjvWXrjkWT5vj5wXaaMuxN+X/EQxLTs7XYU9QbZDpfy1neZOAUoBoG/V1oAT2f0xsxHtWXnw75K5qCKeB5ESOiB1Tv4U+iFgUwnu+xC38cwCIzuyRDwcXBsN95wIDAQAB";
    private static final byte[] SALT = new byte[] {-46, 65, 30, -128, -103, -17, 34, -64, 51, 88, -95, -45, 87, -117, -36, -113, -17, 32, -64, 98};
    private Handler mHandler;
    private LicenseChecker mChecker;
    private LicenseCheckerCallback mLicenseCheckerCallback;
    boolean licensed;
    boolean checkingLicense;
    boolean didCheck;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            /*
            String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.i("Device Id", deviceId);  //AN EXAMPLE OF LOGGING THAT YOU SHOULD BE DOING :)

            mHandler = new Handler();
            mLicenseCheckerCallback = new MyLicenseCheckerCallback();
            mChecker = new LicenseChecker(getApplicationContext(), new ServerManagedPolicy(this, new AESObfuscator(SALT, getPackageName(), deviceId)), BASE64_PUBLIC_KEY_PLANTS);

            //CARGAMOS LAS PREFERENCIAS EN MEMORIA PARA VALIDAR SI REVISAMOS LA LICENCIA DE LA APP
            loadSavedPreferences();
            //VERIFICAMOS ESTE REVISADA LA LICENCIA
            if(!didCheck){
                Log.d(TAG, "Checking application license...");
                doCheck();
                Log.i("Checking!", "Checking license!");
            }*/

            //VERIFICAMOS LA CONEXION A INTERNET Y DEPENDIENDO DE ELLO INICIAMOS O NO IN APP PAY CALL
             isConnectedToInternet();

            //if(Constants.internetOn) {
                //IN APP BILLING GOOGLE
                //inAppPayApi.onCreate();
            //}else{
            //    Log.d(TAG, "No hay conexión a internet, por lo tanto no iniciamos las llamadas a inapp pay google");
            //}

        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Información: necesitas de los servicios de google play para usar la tienda.");
        }

            setContentView(R.layout.presentation_fade_in_out);
            context = this;
            imagePpal = (ImageView)findViewById(R.id.imgpresentacion);
            //AUDIO DE FONDO
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            setPlayer(Presentation.this);
            player.setLooping(false);
            player.start();

            animation = AnimationUtils.loadAnimation(context, R.anim.fadein);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                animationEnd = AnimationUtils.loadAnimation(context, R.anim.fadeout);
                imagePpal.startAnimation(animationEnd);
                //ANIMACION ANIDADA DE FIN
                animationEnd.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent actividadBusq = new Intent(getApplicationContext(), BuscadorActivity.class);
                        //ENVIAMOS LA PETICION DE INICIO DE LA ACTIVIDAD DE BUSQUEDA //ALGUN DIA APLICAR VALIDACION LICENSED
                        startActivity(actividadBusq);
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
            imagePpal.startAnimation(animation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //IN APP BILLING GOOGLE
        //inAppPayApi.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "Request: " + requestCode + ", Result: " + resultCode + ", data: " + data);
        if (resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "IN APP BILL OK");
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(TAG, "IN APP BILL: The user canceled.");
        }
    }

    private void isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean NisConnected = activeNetwork.isConnectedOrConnecting();
        if (NisConnected) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                Constants.internetOn = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Constants.internetOn = true;
            }
            else {
                Constants.internetOn = false;
            }
            }
    }

    //LICENSE METHODS
    /*
    private void doCheck() {

        didCheck = false;
        checkingLicense = true;
        setProgressBarIndeterminateVisibility(true);
        mChecker.checkAccess(mLicenseCheckerCallback);
    }

    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        @Override
        public void allow(int reason) {
            // TODO Auto-generated method stub
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            Log.i("License","Accepted!");

            //You can do other things here, like saving the licensed status to a
            //SharedPreference so the app only has to check the license once.
            licensed = true;
            checkingLicense = false;
            didCheck = true;
            savePreferences("DidCheck_Value", true);
            savePreferences("LicenseCheck_Value", true);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void dontAllow(int reason) {
            // TODO Auto-generated method stub
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            Log.i("License","Denied!");
            Log.i("License","Reason for denial: "+reason);
            //You can do other things here, like saving the licensed status to a
            //SharedPreference so the app only has to check the license once.
            licensed = false;
            checkingLicense = false;
            didCheck = true;
            showDialog(0);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void applicationError(int reason) {
            // TODO Auto-generated method stub
            Log.i("License", "Error: " + reason);
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            licensed = true;
            checkingLicense = false;
            didCheck = false;

            showDialog(0);
        }


    }

    protected Dialog onCreateDialog(int id) {
        // We have only one dialog.
        return new AlertDialog.Builder(this)
                .setTitle("UNLICENSED APPLICATION DIALOG TITLE")
                .setMessage("This application is not licensed, please buy it from the play store.")
                .setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "http://market.android.com/details?id=" + getPackageName()));
                        startActivity(marketIntent);
                        finish();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton("Re-Check", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        doCheck();
                    }
                })

                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener(){
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        Log.i("License", "Key Listener");
                        finish();
                        return true;
                    }
                })
                .create();
    }


    //SALVADO DE ESCOGENCIA DEL CHECKBOX DE TERMINOS EN SHAREDPREFERENCES
    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean didCheckValue = sharedPreferences.getBoolean("DidCheck_Value", false);
        boolean licenseCheckValue = sharedPreferences.getBoolean("LicenseCheck_Value", false);
        if (didCheckValue) {didCheck = true;} else{didCheck = false;}
        if (licenseCheckValue) {licensed = true;} else{licensed = false;}
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    */

    @Override
    public void onDestroy() {
        //IN APP BILLING GOOGLE
        //inAppPayApi.onDestroy();
        super.onDestroy();
    }

    public void setPlayer(Context mContext){

        AssetFileDescriptor afd;
        try {
            afd = mContext.getResources().openRawResourceFd(R.raw.javadoh);
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
        } catch (IOException e) {
            Log.e(TAG, getString(R.string.errorGral),e);
            e.printStackTrace();
        }
    }
}
