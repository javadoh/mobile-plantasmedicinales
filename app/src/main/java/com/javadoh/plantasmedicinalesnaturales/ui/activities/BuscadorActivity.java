package com.javadoh.plantasmedicinalesnaturales.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.javadoh.plantasmedicinalesnaturales.R;
import com.javadoh.plantasmedicinalesnaturales.io.Constants;
import com.javadoh.plantasmedicinalesnaturales.utils.GoogleInAppPayUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuscadorActivity extends AppCompatActivity implements View.OnClickListener{

    private final String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE = 12;
    public static final String TAG = BuscadorActivity.class.getName();
    protected EditText editTxtBusqueda;
    protected Button btnHierbas, btnSintomas;
    private static String tipoDeBusqueda;
    private Toolbar toolbar;
    private CheckBox checkBoxTerms;
    private TextView textTermsConditionsHome, textTermsConditionsClickable;
    private TextInputLayout layoutTextInputSearch;
    String[] dataUser;
    private ImageView imgNews;
    private static String inputValidationOk;
    //SONIDO
    final MediaPlayer player = new MediaPlayer();
    int media_length;
    //ADMOB
    AdView mAdView;
    //ADMOB INTERSTITIAL
    InterstitialAd mInterstitialAd;
    //IN APP BILLING STORE
    GoogleInAppPayUtils inAppPayApi = new GoogleInAppPayUtils(BuscadorActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buscador);

        if (ActivityCompat.checkSelfPermission(BuscadorActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(BuscadorActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(BuscadorActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(BuscadorActivity.this, permissions[3]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(BuscadorActivity.this, permissions[4]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(BuscadorActivity.this, permissions[5]) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BuscadorActivity.this, permissions, REQUEST_CODE);
        }

        //AUDIO DE FONDO
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setPlayer(BuscadorActivity.this);
        player.setLooping(true);
        player.start();
        //INICIALIZAMOS ELEMENTOS DE LA VISTA
        imgNews = (ImageView) findViewById(R.id.imgNews);
        editTxtBusqueda = (EditText) findViewById(R.id.txt_busqueda);
        layoutTextInputSearch = (TextInputLayout) findViewById(R.id.layoutTextInputSearch);
        editTxtBusqueda.addTextChangedListener(new MyTextWatcher(editTxtBusqueda));

        btnHierbas = (Button) findViewById(R.id.btn_busqueda_hierbas);
        btnSintomas = (Button) findViewById(R.id.btn_busqueda_sintomas);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //TERMINOS Y CONDICIONES HOME
        checkBoxTerms = (CheckBox) findViewById(R.id.idCheckTerms);
        checkBoxTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBoxTerms.isChecked()){
                    Toast.makeText(getBaseContext(), getString(R.string.errorHomeCheck), Toast.LENGTH_LONG).show();
                }else{
                    savePreferences("CheckBox_Value", checkBoxTerms.isChecked());
                }
            }
        });
        textTermsConditionsHome = (TextView) findViewById(R.id.textTermsConditionsHome);
        textTermsConditionsClickable = (TextView) findViewById(R.id.textTermsConditionsClickable);
        btnHierbas.setOnClickListener(this);
        btnSintomas.setOnClickListener(this);
        //TOOLBAR INICIALIZACION
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        //SECCIÓN DE TEXTO PRINCIPAL PARA TERMINOS Y CONDICIONES
        textTermsConditionsHome.setText(R.string.terminos_pantalla_inicio);
        textTermsConditionsClickable.setClickable(true);
        textTermsConditionsClickable.setMovementMethod(LinkMovementMethod.getInstance());
        textTermsConditionsClickable.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(BuscadorActivity.this);
                View vistaDialogo = li.inflate(R.layout.dialog_terms_conditions, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        BuscadorActivity.this);

                alertDialogBuilder.setView(vistaDialogo);
                alertDialogBuilder.setTitle(getString(R.string.titleTerms));
                //set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.btn_accept),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //SALVAMOS COMO PREFERENCIA EL VALOR DEL CHECKBOX DE TERMINOS
                                        savePreferences("CheckBox_Value", checkBoxTerms.isChecked());
                                        checkBoxTerms.setChecked(true);
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(getString(R.string.btn_cancelar),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });

        //CARGAMOS LAS PREFERENCIAS (CHECKBOX TERMINOS)
        loadSavedPreferences();
        //BILLING INIT
        try{
            if(Constants.internetOn) {
                inAppPayApi.onCreate();
            }else{Toast.makeText(getBaseContext(), getString(R.string.errorNoInternet), Toast.LENGTH_SHORT).show();}
        }catch (Exception e){Toast.makeText(getBaseContext(), getString(R.string.errorInAppPayCreated), Toast.LENGTH_LONG).show();}

        //VALIDAMOS SI EL USUARIO COMPRO LA APLICACION PARA NO MOSTRAR MAS LAS APLICACIONES
        if(!Constants.isAdsDisabled) {
            //ADMOB INICIALIZACION BANNER
            mAdView = (AdView) findViewById(R.id.adBannerView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            Log.i(TAG, "ADREQ CONTENT: "+adRequest.getContentUrl());
            mAdView.loadAd(adRequest);
            //ADMOB INTERSTITIAL
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.adintersticial));
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                }
            });
            requestNewInterstitial();
        }

    }

    //EVENTO DEL BOTON SIGN UP
    public void newsDialog(View V)
    {
        LayoutInflater li = LayoutInflater.from(BuscadorActivity.this);
        final View vistaDialogo = li.inflate(R.layout.dialog_news_updates, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BuscadorActivity.this);

        alertDialogBuilder.setView(vistaDialogo);
        alertDialogBuilder.setTitle(getString(R.string.dialogoNewsTitle));
        //set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buscador, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.action_faq:
                    LayoutInflater li = LayoutInflater.from(BuscadorActivity.this);
                    View vistaDialogoFaq = li.inflate(R.layout.dialog_faq_from_menu, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            BuscadorActivity.this);

                    alertDialogBuilder.setView(vistaDialogoFaq);
                    alertDialogBuilder.setTitle("FAQ");
                    //set dialog message
                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                    break;

                case R.id.action_store:
                        LayoutInflater linflater = LayoutInflater.from(BuscadorActivity.this);
                        View vistaDialogoStore = linflater.inflate(R.layout.dialog_store_from_menu, null);
                        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                                BuscadorActivity.this);

                        alertDialogBuilder2.setView(vistaDialogoStore);
                        //VISTAS LAYOUT
                        TextView txtTituloProducto = (TextView) vistaDialogoStore.findViewById(R.id.txtTituloProducto);
                        Button buttonPay = (Button) vistaDialogoStore.findViewById(R.id.buttonPay);

                        if(Constants.isAdsDisabled) {//SI HAY PAGO MOSTRAMOS MENSAJE Y DESHABILITAMOS BOTON DE PAGO
                            txtTituloProducto.setText(R.string.compra_realizada_store);
                            buttonPay.setEnabled(false);
                            buttonPay.setClickable(false);
                            buttonPay.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_grey_face));
                        }else{
                            txtTituloProducto.setText(R.string.subTituloPago);
                        }

                        buttonPay.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (Constants.isInAppSetupCreated) {
                                    if(Constants.internetOn) {
                                        getPayment();
                                    }else{Toast.makeText(getBaseContext(), getString(R.string.errorNoInternet), Toast.LENGTH_SHORT).show();}
                                }else{
                                    try {
                                        //if (Constants.internetOn) {
                                            //IN APP BILLING GOOGLE
                                            inAppPayApi.onCreate();
                                            if(Constants.isInAppSetupCreated) {
                                                if(Constants.internetOn) {
                                                    getPayment();
                                                }else{Toast.makeText(getBaseContext(), getString(R.string.errorNoInternet), Toast.LENGTH_SHORT).show();}
                                            }else{Toast.makeText(getBaseContext(), getString(R.string.errorInAppPayCreated), Toast.LENGTH_LONG).show();}
                                        //}else{Toast.makeText(getBaseContext(), getString(R.string.errorNoInternet), Toast.LENGTH_LONG).show();}
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error, no se pudo realizar el llamado a pago: ", e);
                                        Toast.makeText(getBaseContext(), getString(R.string.errorInAppPayCreated), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                        //set dialog message
                        alertDialogBuilder2
                                .setNegativeButton(getString(R.string.btn_later), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog2 = alertDialogBuilder2.create();
                        // show it
                        alertDialog2.show();
                        break;

                case R.id.action_compartir:

                    Uri imageUri;
                    try {
                        String uri = "drawable/faqcartoon";
                        int imageResource = getResources().getIdentifier(uri, "drawable", getPackageName());

                        imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                                BitmapFactory.decodeResource(getResources(), imageResource), null, null));

                        Intent shareTxtIntent = new Intent(Intent.ACTION_SEND);
                        shareTxtIntent.setType("*/*");
                        shareTxtIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        shareTxtIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        shareTxtIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + getString(R.string.tituloSharePlant2));
                        shareTxtIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareTxtIntent, "Share"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(BuscadorActivity.this, getString(R.string.errorGral) +ex, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(BuscadorActivity.this, getString(R.string.errorGral) +e, Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.action_enable_disable_sound:
                    player.stop();
                    break;
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.d("LOG_TAG", "Inicio de búsqueda...");

        switch (v.getId()){
            case R.id.btn_busqueda_hierbas:
                tipoDeBusqueda = "hierbas";
                break;
            case R.id.btn_busqueda_sintomas:
                tipoDeBusqueda = "sintomas";
                break;
        }

        try {
            if("OK".equalsIgnoreCase(inputValidationOk) || TextUtils.isEmpty(editTxtBusqueda.getText().toString())){

                //VALIDAMOS SI EL USUARIO COMPRO LA APLICACION PARA NO MOSTRAR MAS LAS APLICACIONES
                if(Constants.isAdsDisabled) {
                    savePreferences("Premium_User", true);
                }

                if (checkBoxTerms.isChecked()) {
                    Intent actividadResBusq = new Intent(getApplicationContext(), BusquedaRespuestaActivity.class);

                    String editTxtBusqValue = editTxtBusqueda.getText().toString();
                    actividadResBusq.putExtra("TXT_BUSQUEDA", editTxtBusqValue);
                    actividadResBusq.putExtra("TXT_TIPO_BUSQUEDA", tipoDeBusqueda);
                    //GOOGLE
                    actividadResBusq.putExtra("DATA_USER", dataUser);
                    //ENVIAMOS LA PETICION DE INICIO DE LA ACTIVIDAD DE RESPUESTA
                    startActivity(actividadResBusq);
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.errorHomeCheck), Toast.LENGTH_LONG).show();
                }
            }else{Toast.makeText(getBaseContext(), getString(R.string.errorInputSearchChars), Toast.LENGTH_LONG).show();}
        }catch (Exception e){
            Log.e(TAG, getString(R.string.errorGral), e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
        media_length = player.getCurrentPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        player.seekTo(media_length);
        player.start();
    }

    public void setPlayer(Context mContext){

            AssetFileDescriptor afd;
        try {
            afd = mContext.getResources().openRawResourceFd(R.raw.riverbirds);
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
        } catch (IOException e) {
            Log.e(TAG, getString(R.string.errorGral),e);
            e.printStackTrace();
        }
    }

    //ADMOB INTERSTITIAL
    private void requestNewInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

        //IN APP BILLING GOOGLE
        inAppPayApi.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Request: " + requestCode + ", Result: " + resultCode + ", data: " + data);
        if (resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "IN APP BILL OK");
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(TAG, "IN APP BILL: The user canceled.");
        }
    }

    @Override
    public void onDestroy() {
        //IN APP BILLING GOOGLE
        inAppPayApi.onDestroy();
        super.onDestroy();
    }

    public void getPayment(){
        //IN APP BILLING GOOGLE
        if(Constants.isInAppSetupCreated) {
            inAppPayApi.purchaseRemoveAds();
        }else{
            Toast.makeText(getBaseContext(), getString(R.string.errorTienda), Toast.LENGTH_LONG).show();
        }
    }

    //SALVADO DE ESCOGENCIA DEL CHECKBOX DE TERMINOS EN SHAREDPREFERENCES
    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean checkBoxValue = sharedPreferences.getBoolean("CheckBox_Value", false);
        boolean checkPremiumUser = sharedPreferences.getBoolean("Premium_User", false);
        if (checkBoxValue) {
            checkBoxTerms.setChecked(true);
        } else {
            checkBoxTerms.setChecked(false);
        }
        if (checkPremiumUser) {
            Constants.isAdsDisabled = true;
        } else {
            Constants.isAdsDisabled = false;
        }
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private boolean validateSearch(View view) {

        String text = editTxtBusqueda.getText().toString();
        Pattern p = Pattern.compile("[^A-Za-zÑñáéíóúÁÉÍÓÚ, ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        boolean b = m.find();
        if (b) {
            editTxtBusqueda.setError(getString(R.string.errorInputSearchChars));
            requestFocus(editTxtBusqueda);
            inputValidationOk = "ERROR";
            return false;
        } else {
            layoutTextInputSearch.setErrorEnabled(false);
            inputValidationOk = "OK";
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
                    validateSearch(view);
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length == 6 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED && grantResults[4] == PackageManager.PERMISSION_GRANTED && grantResults[5] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BuscadorActivity.this, "Permission granted!!!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(BuscadorActivity.this, "Necessary permissions not granted...", Toast.LENGTH_LONG).show();
            }
        }
    }

}