package com.javadoh.plantasmedicinalesnaturales.ui.adapters;

    import android.app.Activity;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.res.Configuration;
    import android.graphics.drawable.Drawable;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.widget.RecyclerView;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.ads.AdListener;
    import com.google.android.gms.ads.AdRequest;
    import com.google.android.gms.ads.InterstitialAd;
    import com.javadoh.plantasmedicinalesnaturales.io.Constants;
    import com.javadoh.plantasmedicinalesnaturales.ui.activities.BusquedaRespuestaActivity;
    import com.squareup.picasso.Picasso;
    import java.util.ArrayList;
    import com.javadoh.plantasmedicinalesnaturales.R;
    import com.javadoh.plantasmedicinalesnaturales.io.beans.HierbasBean;
    import com.javadoh.plantasmedicinalesnaturales.ui.activities.DetailResActivity;
    import com.javadoh.plantasmedicinalesnaturales.utils.bean.MemoryBeanAux;

/**
 * Created by luiseliberal on 27-09-2015.
 */
    public class BusquedaRespuestaAdapter extends RecyclerView.Adapter<BusquedaRespuestaAdapter.CustomViewHolder> {

        private static final String TAG = BusquedaRespuestaAdapter.class.getName();
        private ArrayList<HierbasBean> hierbasBeanList;
        private Context mContext;
        private String[] dataUser;

        public BusquedaRespuestaAdapter(Context context, ArrayList<HierbasBean> hierbasBeanList, String[] dataUser) {
            this.hierbasBeanList = hierbasBeanList;
            this.mContext = context;
            this.dataUser = dataUser;
        }



    @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.respuesta_card_main, null);

            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

            final HierbasBean itemHierba = hierbasBeanList.get(i);
            String imagePath = "";
                customViewHolder.textView.setText(itemHierba.getNombre());
                customViewHolder.textViewAlias.setText(itemHierba.getAlias().toString().replaceAll("[^A-Za-zÑñáéíóúÁÉÍÓÚ, ]", ""));

                if(itemHierba.getImgurl() != null){

                    if(itemHierba.getImgurl().contains(".png")){
                        imagePath = itemHierba.getImgurl().toString().replace(".png", "");
                    }else if(itemHierba.getImgurl().contains(".jpg")){
                        imagePath = itemHierba.getImgurl().toString().replace(".jpg", "");
                    }else if(itemHierba.getImgurl().contains(".jpeg")){
                        imagePath = itemHierba.getImgurl().toString().replace(".jpeg", "");
                    }
                }

                MemoryBeanAux.setHierbaImagePath(imagePath);
                String uri = "@drawable/" + imagePath;

                // extension removed from the String
                int imageResource = mContext.getResources().getIdentifier(uri, "drawable", mContext.getPackageName());

            Drawable res;
            if (imageResource != 0) {

                Picasso.with(mContext).load(imageResource).resize(180, 100).into(customViewHolder.imageView);
                }else {
                    uri = "@drawable/" + "hierba_card_background";
                    imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());

                Picasso.with(mContext).load(imageResource).resize(180, 100).into(customViewHolder.imageView);
                }

                customViewHolder.textView.setTag(customViewHolder);
                customViewHolder.textViewAlias.setTag(customViewHolder);
                customViewHolder.imageView.setTag(customViewHolder);

        }

        @Override
        public int getItemCount() {
            return (null != hierbasBeanList ? hierbasBeanList.size() : 0);
        }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView imageView;
        protected TextView textView;
        protected TextView textViewAlias;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textView = (TextView) view.findViewById(R.id.tituloHierba);
            this.textViewAlias = (TextView) view.findViewById(R.id.aliasHierba);

            textView.setOnClickListener(this);
            textViewAlias.setOnClickListener(this);
            imageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            try {

                if (v.getId() == textView.getId() || v.getId() == textViewAlias.getId()) {

                    Intent actividadDetalleBusq = new Intent(mContext, DetailResActivity.class);
                    //INSTANCIAMOS LA ACTIVIDAD PARA LA ANIMACION
                    //Activity activity = (Activity) mContext;
                    //PARAMETROS DE SESION
                    actividadDetalleBusq.putExtra("HIERBAS_BEAN", (HierbasBean) hierbasBeanList.get(position));
                    actividadDetalleBusq.putExtra("DATA_USER", dataUser);
                    //ENVIAMOS LA PETICION DE INICIO DE LA ACTIVIDAD DE RESPUESTA
                    mContext.startActivity(actividadDetalleBusq);
                    //activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                } else if (v.getId() == imageView.getId()) {


                    LayoutInflater li = LayoutInflater.from(mContext);
                    View vistaDialogo = li.inflate(R.layout.dialog_image_big, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            mContext);
                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(vistaDialogo);

                    final TextView textTitulo = (TextView) vistaDialogo.findViewById(R.id.textDialogTitulo);
                    final ImageView imageDialog = (ImageView) vistaDialogo.findViewById(R.id.imageBigDialog);

                    String nombreArchivoImg = hierbasBeanList.get(getAdapterPosition()).getImgurl();
                    nombreArchivoImg = nombreArchivoImg.substring(0, nombreArchivoImg.length() -4);

                    textTitulo.setText(mContext.getString(R.string.detailPlant)+" "+hierbasBeanList.get(getAdapterPosition()).getNombre().toUpperCase());

                    String uri = "@drawable/" +nombreArchivoImg;

                    int imageResource = mContext.getResources().getIdentifier(uri, "drawable",
                            mContext.getPackageName());

                    if(imageResource != 0) {
                        //REVISAMOS SI ESTA EN SENTIDO HORIZONTAL
                        if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                            Picasso.with(mContext).load(imageResource).resize(900, 600).centerCrop().into(imageDialog);
                        }else {
                            Picasso.with(mContext).load(imageResource).resize(700, 700).centerCrop().into(imageDialog);
                        }

                    }else{Toast.makeText(mContext, mContext.getString(R.string.errorImagesAdapter), Toast.LENGTH_LONG).show();}


                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setNegativeButton(mContext.getString(R.string.btn_cancelar),
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

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Ocurrió un error llamando a la actividad DetailResActivity: ", e);
            }
        }
        }

    }
