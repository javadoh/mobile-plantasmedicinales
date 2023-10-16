package com.javadoh.plantasmedicinalesnaturales.ui.activities;

    import android.content.DialogInterface;
    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.LinearLayoutManager;
    import android.support.v7.widget.RecyclerView;
    import android.support.v7.widget.Toolbar;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.ProgressBar;
    import android.widget.RelativeLayout;
    import android.widget.Toast;
    import com.google.android.gms.ads.AdListener;
    import com.google.android.gms.ads.AdRequest;
    import com.google.android.gms.ads.AdView;
    import com.google.android.gms.ads.InterstitialAd;
    import java.io.UnsupportedEncodingException;
    import java.net.URLEncoder;
    import java.util.ArrayList;
    import com.javadoh.plantasmedicinalesnaturales.R;
    import com.javadoh.plantasmedicinalesnaturales.io.beans.HierbasBean;
    import com.javadoh.plantasmedicinalesnaturales.ui.adapters.BusquedaRespuestaAdapter;
    import com.javadoh.plantasmedicinalesnaturales.ui.fragments.RateDialogFragment;
    import com.javadoh.plantasmedicinalesnaturales.utils.AsyncHttpTask;
    import com.javadoh.plantasmedicinalesnaturales.io.Constants;

/**
 * Created by luiseliberal on 27-09-2015.
 */
    public class BusquedaRespuestaActivity extends AppCompatActivity {

    private static final String TAG = BusquedaRespuestaActivity.class.getName();
    private ArrayList<HierbasBean> hierbasBeanList;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private BusquedaRespuestaAdapter adapter;
    private ImageView imgNoData, imgNoConex;
    private Toolbar toolbar;
    private FloatingActionButton btnRegresar;
    String url = "";
    String[] dataUser;
    //ADMOB
    AdView mAdView;
    //ADMOB INTERSTITIAL
    InterstitialAd mInterstitialAd;
    private boolean showedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta_main);

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
                    //requestNewInterstitial();
                }
                @Override
                public void onAdLoaded() {

                    if(!showedAd) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                            showedAd = true;
                        }
                    }
                }
            });
            requestNewInterstitial();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        //INICIALIZACION DE RECYCLER VIEW
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if(!Constants.isAdsDisabled) {
            View viewRecycler = findViewById(R.id.recycler_view);
            RelativeLayout.LayoutParams viewParamsLayout = (RelativeLayout.LayoutParams) viewRecycler.getLayoutParams();
            viewParamsLayout.addRule(RelativeLayout.ABOVE, R.id.adBannerView);
            viewRecycler.setLayoutParams(viewParamsLayout);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BusquedaRespuestaAdapter(this, hierbasBeanList, dataUser);
        hierbasBeanList = new ArrayList<>();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        btnRegresar = (FloatingActionButton) findViewById(R.id.btn_regresar_detail);
        if(!Constants.isAdsDisabled) {
            RelativeLayout.LayoutParams floatLayoutParams = (RelativeLayout.LayoutParams) btnRegresar.getLayoutParams();
            floatLayoutParams.addRule(RelativeLayout.ABOVE, R.id.adBannerView);
            //AL FINAL DEL RECYCLER VIEW
            floatLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, mRecyclerView.getId());
            btnRegresar.setLayoutParams(floatLayoutParams);
        }else{
            RelativeLayout.LayoutParams floatLayoutParams = (RelativeLayout.LayoutParams) btnRegresar.getLayoutParams();
            //AL FINAL DEL RECYCLER VIEW
            floatLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, mRecyclerView.getId());
            btnRegresar.setLayoutParams(floatLayoutParams);
        }

        imgNoData = (ImageView) findViewById(R.id.img_no_records);
        imgNoConex = (ImageView) findViewById(R.id.img_no_conex);
        btnRegresar.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                BusquedaRespuestaActivity.this.finish();
            }
        });

        //CAPTURA DE VALORES DE SESION - TEXTO BUSQUEDA Y TIPO
        Bundle busquedaSesion = getIntent().getExtras();

        String txtBusqueda = busquedaSesion.getString("TXT_BUSQUEDA");
        String tipoBusqueda = busquedaSesion.getString("TXT_TIPO_BUSQUEDA");
        String[] dataUser = busquedaSesion.getStringArray("DATA_USER");
        //URL DEL API QUE INVOCAMOS CON SU RESPECTIVA OPERACION
        try {
            if (("hierbas").equalsIgnoreCase(tipoBusqueda)) {
                url = Constants.URL_SERVIDOR_RMT_APP_HIERBAS + Constants.GET_HIERBAS_OPERACION + "?nombre=" + URLEncoder.encode(txtBusqueda, "utf-8")+"&localization="+getString(R.string.flagLocalization);
            } else if (("sintomas").equalsIgnoreCase(tipoBusqueda)) {

                url = Constants.URL_SERVIDOR_RMT_APP_HIERBAS + Constants.GET_SINTOMAS_OPERACION + "?sintoma=" + URLEncoder.encode(txtBusqueda, "utf-8")+"&localization="+getString(R.string.flagLocalization);
            }
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d(TAG, "Error: ", e);
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_LONG).show();
            }

        Log.d(TAG, "BusquedaResAct URL: " + url);
        //INVOCAMOS LA CLASE QUE MANEJA TODA LA INVOCACION Y EL PARSEO
        new AsyncHttpTask(this, mRecyclerView, adapter, progressBar, hierbasBeanList, imgNoData,
                imgNoConex, dataUser).execute(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
        MenuItem menuItemShare = menu.findItem(R.id.action_compartir);
        MenuItem menuItemSound = menu.findItem(R.id.action_enable_disable_sound);
        menuFaq.setVisible(true);
        menuItemTienda.setVisible(false);
        menuItemShare.setVisible(false);
        menuItemSound.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_faq:
                LayoutInflater li = LayoutInflater.from(BusquedaRespuestaActivity.this);
                View vistaDialogoFaq = li.inflate(R.layout.dialog_faq_from_menu, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        BusquedaRespuestaActivity.this);

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
        }

        return super.onOptionsItemSelected(item);
    }

    //ADMOB INTERSTITIAL
    private void requestNewInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //RATE DIALOG
        RateDialogFragment.show(this, getSupportFragmentManager());
        //PEDIMOS OTRO AVISO PUBLICITARIO
        if(!Constants.isAdsDisabled) {
            requestNewInterstitial();
        }
    }
}
