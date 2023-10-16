package com.javadoh.plantasmedicinalesnaturales.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.javadoh.plantasmedicinalesnaturales.R;
import com.javadoh.plantasmedicinalesnaturales.io.beans.HierbasBean;
import com.javadoh.plantasmedicinalesnaturales.ui.activities.DetailResActivity;

/**
 * Created by luiseliberal on 08-11-2015.
 */
public class DetailResAdapter extends RecyclerView.Adapter<DetailResAdapter.DetailViewHolder>{

    private static final String TAG = BusquedaRespuestaAdapter.class.getName();
    private ArrayList<HierbasBean> hierbaBean;
    private Context mContext;
    private int positionHierba;
    private Button botonRegreso, btnAbrirComentario;
    private String url;

    public DetailResAdapter(Context context, ArrayList<HierbasBean> hierbaBean, int positionHierba) {
        this.hierbaBean = hierbaBean;
        this.mContext = context;
        this.positionHierba = positionHierba;
    }
    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detail_res_herb_fragment, null);
        DetailViewHolder viewHolder = new DetailViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DetailViewHolder customViewHolder, int i) {

            final HierbasBean itemHierba = this.hierbaBean.get(positionHierba);
            //customViewHolder.imageView.setImageURI(Uri.parse(itemHierba.getImgurl()));
            customViewHolder.textViewNombre.setText(itemHierba.getNombre().toString());
            customViewHolder.textViewAlias.setText(itemHierba.getAlias().toString().toString());
            customViewHolder.textViewDescripcion.setText(itemHierba.getDescripcion().toString());
            customViewHolder.textViewPropiedades.setText(itemHierba.getPropiedades().toString());
            customViewHolder.textViewIndicaciones.setText(itemHierba.getIndicaciones().toString());
            customViewHolder.textViewEmpleo.setText(itemHierba.getEmpleo().toString());
            customViewHolder.textViewSintomas.setText(itemHierba.getSintomas().toString());

            //NUEVOS ELEMENTOS
            customViewHolder.textViewContraindicaciones.setText(itemHierba.getContraIndicaciones().toString());
            customViewHolder.textViewUbicacion.setText(itemHierba.getUbicacion().toString());
            customViewHolder.textViewGastronomia.setText(itemHierba.getGastronomia().toString());


            String uri = "@drawable/hierba_card_background";

            int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());

            Drawable res = mContext.getResources().getDrawable(imageResource);
            customViewHolder.imageView.setImageDrawable(res);

            this.positionHierba = i;

        this.botonRegreso.setOnClickListener(new View.OnClickListener() {

                @Override
            public void onClick(View v) {
                    if(mContext instanceof DetailResActivity){
                    ((DetailResActivity)mContext).finish();
                    }

            }
        });
    }

    @Override
    public int getItemCount() {

        Log.d(TAG, "getItemCount: " + positionHierba);
        return positionHierba;
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder{
        //################ DECLARAR TODOS LOS CAMPOS PENDIENTES ################//
        protected ImageView imageView;
        protected TextView textViewNombre;
        protected TextView textViewAlias;
        protected TextView textViewDescripcion;
        protected TextView textViewPropiedades;
        protected TextView textViewIndicaciones;
        protected TextView textViewEmpleo;
        protected TextView textViewSintomas;
        protected TextView textViewContraindicaciones;
        protected TextView textViewUbicacion;
        protected TextView textViewGastronomia;
        protected Button botonRegreso;
        protected Button botonAbrirComentario;

        public DetailViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.img_hierba_card_res_ppal);
            this.textViewNombre = (TextView) view.findViewById(R.id.txt_hierba_name_detail);
            this.textViewAlias = (TextView) view.findViewById(R.id.txt_hierba_alias_detail);
            this.textViewDescripcion = (TextView) view.findViewById(R.id.txt_hierba_description_detail);
            this.textViewPropiedades = (TextView) view.findViewById(R.id.txt_hierba_properties_detail);
            this.textViewIndicaciones = (TextView) view.findViewById(R.id.txt_hierba_indications_detail);
            this.textViewEmpleo = (TextView) view.findViewById(R.id.txt_hierba_empleo_detail);
            this.textViewSintomas = (TextView) view.findViewById(R.id.txt_hierba_symptoms_detail);
            this.textViewContraindicaciones = (TextView) view.findViewById(R.id.txt_hierba_contraindications_detail);
            this.textViewUbicacion = (TextView) view.findViewById(R.id.txt_hierba_ubication_detail);
            this.textViewGastronomia = (TextView) view.findViewById(R.id.txt_hierba_gastronomy_detail);
            this.botonAbrirComentario = (Button) view.findViewById(R.id.btn_open_comment_detail);
            this.botonRegreso = (Button) view.findViewById(R.id.btn_regresar_detail);
        }
    }
}
