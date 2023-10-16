package com.javadoh.plantasmedicinalesnaturales.ui.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.javadoh.plantasmedicinalesnaturales.R;
import com.javadoh.plantasmedicinalesnaturales.io.beans.HierbasBean;
import com.javadoh.plantasmedicinalesnaturales.ui.fragments.DetailResFragment;
import com.javadoh.plantasmedicinalesnaturales.io.Constants;

/**
 * Created by luiseliberal on 08-11-2015.
 */
public class DetailResActivity extends AppCompatActivity{

    private static final String TAG = DetailResActivity.class.getName();
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private HierbasBean hierba;
    //ADMOB
    AdView mAdView;
    //ADMOB INTERSTITIAL
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String hashKey = DetailResActivity.printKeyHash(DetailResActivity.this);
        Log.d(TAG, "KEYHASH: " + hashKey);

        //INICIALIZAMOS FACEBOOK (AGREGAR VALIDACION SI YA ESTA CONECTADO)
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);

        accessToken = AccessToken.getCurrentAccessToken();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                accessToken = newToken;
            }
        };

        if(accessToken != null) {
            Log.d(TAG, "AccessToken: " + accessToken.getToken().toString());
        }

        setContentView(R.layout.detail_res_herb_activity);
        //VALIDAMOS SI EL USUARIO COMPRO LA APLICACION PARA NO MOSTRAR MAS LAS APLICACIONES
        if(!Constants.isAdsDisabled) {
            //ADMOB INICIALIZACION BANNER
            mAdView = (AdView) findViewById(R.id.adBannerView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
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

        if (savedInstanceState == null) {

            //OBTENEMOS EL OBJETO BEAN DE LAS HIERBAS DESDE LA OTRA ACTIVIDAD
            Bundle extras = getIntent().getExtras();
            hierba = (HierbasBean) getIntent().getSerializableExtra("HIERBAS_BEAN");
            String[] dataUser = extras.getStringArray("DATA_USER");

            //SETEAMOS EL OBJETO HIERBAS EN UN BUNDLE
            Bundle hierbaObject = new Bundle();
            hierbaObject.putSerializable("HIERBAS_BEAN", hierba);
            hierbaObject.putStringArray("DATA_USER", dataUser);

            DetailResFragment detailResFrag = new DetailResFragment();
            //ENVIAMOS EL OBJETO HIERBAS AL FRAGMENTO
            detailResFrag.setArguments(hierbaObject);

            DetailResFragment fragment = DetailResFragment.newInstance(hierbaObject);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.detail_res_fragment_id, fragment, "");
            ft.addToBackStack(null);
            ft.commit();
        }

        if(!Constants.isAdsDisabled){
            FrameLayout viewFrameDetail = (FrameLayout) findViewById(R.id.detail_res_fragment_id);
            RelativeLayout.LayoutParams viewFrameParam = (RelativeLayout.LayoutParams) viewFrameDetail.getLayoutParams();
            viewFrameParam.addRule(RelativeLayout.ABOVE, R.id.adBannerView);
            viewFrameDetail.setLayoutParams(viewFrameParam);
            /*FrameLayout viewFrameDetail = (FrameLayout) findViewById(R.id.detail_res_fragment_id);
            FrameLayout.LayoutParams viewFrameParam = (FrameLayout.LayoutParams) viewFrameDetail.getLayoutParams();
            viewFrameParam.setMargins(0, 0, 0, 30);
            viewFrameDetail.setLayoutParams(viewFrameParam);*/

            //VALIDAMOS SI EL USUARIO COMPRO LA APLICACION PARA NO MOSTRAR MAS LAS APLICACIONES
            //ADMOB INTERSTITIAL
            //if (mInterstitialAd.isLoaded()) {
            //    mInterstitialAd.show();
            //}
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onBackPressed() {
        //VALIDAMOS SI EL USUARIO COMPRO LA APLICACION PARA NO MOSTRAR MAS LAS APLICACIONES
        /*if(!Constants.isAdsDisabled) {
            //ADMOB INTERSTITIAL
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }*/
            this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buscador, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem menuFaq = menu.findItem(R.id.action_faq);
        MenuItem menuItemTienda = menu.findItem(R.id.action_store);
        MenuItem menuItemFavoritos = menu.findItem(R.id.action_favoritos);
        MenuItem menuItemCompartir = menu.findItem(R.id.action_compartir);
        MenuItem menuItemFaceLogOut = menu.findItem(R.id.action_logout_facebook);
        MenuItem menuItemSound = menu.findItem(R.id.action_enable_disable_sound);
        menuFaq.setVisible(false);
        menuItemTienda.setVisible(false);
        menuItemFavoritos.setVisible(false);
        menuItemCompartir.setVisible(true);
        menuItemSound.setVisible(false);
        menuItemFaceLogOut.setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_faq:
                LayoutInflater li = LayoutInflater.from(DetailResActivity.this);
                View vistaDialogoFaq = li.inflate(R.layout.dialog_faq_from_menu, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        DetailResActivity.this);

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
            case R.id.action_favoritos:
                Toast.makeText(DetailResActivity.this, getString(R.string.incomingFav), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_compartir:
                String imagePath = "";
                //IMAGEN
                if(hierba.getImgurl() != null){
                    if(hierba.getImgurl().contains(".png")){
                        imagePath = hierba.getImgurl().toString().replace(".png", "");
                    }else if(hierba.getImgurl().contains(".jpg")){
                        imagePath = hierba.getImgurl().toString().replace(".jpg", "");
                    }else if(hierba.getImgurl().contains(".jpeg")){
                        imagePath = hierba.getImgurl().toString().replace(".jpeg", "");
                    }
                }
                Uri imageUri;
                try {
                    String uri = "@drawable/" + imagePath;
                    int imageResource = getResources().getIdentifier(uri, "drawable", getPackageName());

                    imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                            BitmapFactory.decodeResource(getResources(), imageResource), null, null));

                Intent shareTxtIntent = new Intent(Intent.ACTION_SEND);
                shareTxtIntent.setType("*/*");
                shareTxtIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.tituloSharePlant) + hierba.getNombre() + getString(R.string.tituloSharePlantSubject));
                    shareTxtIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareTxtIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.tituloSharePlant) +hierba.getNombre()+ getString(R.string.tituloSharePlant2));
                shareTxtIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareTxtIntent, "Share"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailResActivity.this, getString(R.string.errorGral) +ex, Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.action_logout_facebook:
                Intent fbLogin = new Intent(this, FacebookLoginComment.class);
                startActivity(fbLogin);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //VALIDAMOS SI EL USUARIO COMPRO LA APLICACION PARA NO MOSTRAR MAS LAS APLICACIONES
        if(!Constants.isAdsDisabled) {
            //ADMOB INTERSTITIAL
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
    }

    //ADMOB INTERSTITIAL
    private void requestNewInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

}