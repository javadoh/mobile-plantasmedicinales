package com.javadoh.plantasmedicinalesnaturales.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.javadoh.plantasmedicinalesnaturales.utils.TextAnimationColor;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;

import com.javadoh.plantasmedicinalesnaturales.R;
import com.javadoh.plantasmedicinalesnaturales.io.beans.ComentarioBean;
import com.javadoh.plantasmedicinalesnaturales.io.beans.HierbasBean;
import com.javadoh.plantasmedicinalesnaturales.ui.activities.DetailResActivity;
import com.javadoh.plantasmedicinalesnaturales.ui.activities.FacebookLoginComment;
import com.javadoh.plantasmedicinalesnaturales.io.Constants;
import com.javadoh.plantasmedicinalesnaturales.utils.PostAsyncHttpTask;
import com.javadoh.plantasmedicinalesnaturales.utils.bean.MemoryBeanAux;

/**
 * Created by luiseliberal on 08-11-2015.
 */
public class DetailResFragment extends Fragment {

    private static final String TAG = DetailResFragment.class.getName();

    private TextView textViewNombre, textViewNombreCientifico, textViewAlias, textViewDescripcion,
            textViewSeccionUso, textViewPropiedades, textViewIndicaciones, textViewContraIndicaciones,
            textViewSintomas, textViewEmpleo, textViewUbicacion, textViewGastronomia;
    private FloatingActionButton botonRegreso, botonAbrirComentario;
    private TextInputLayout layoutUserName, layoutEmailName, layoutComentario;
    private EditText editTextUserName, editTextEmail, editTextComentario;
    String url;
    private Toolbar toolbar;
    //FACEBOOK
    private ImageView imageViewUrl;
    private AccessToken accessToken;
    private Profile profileUser;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private Profile profileUserFb;
    private Button btnTomarDatosFacecbook;
    private ScrollView scrollFragment;
    private HierbasBean hierba;

    public DetailResFragment() {
    }

    public static DetailResFragment newInstance(Bundle arguments){
        DetailResFragment f = new DetailResFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FACEBOOK VALIDACION SI YA ESTA CONECTADO
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

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //VER QUE HACER AQUI
                profileUserFb = newProfile;
            }
        };

        Profile.fetchProfileForCurrentAccessToken();
        profileUser = Profile.getCurrentProfile();

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {

        int contadorAuxiliarId = 100;
        Context mContext = getActivity();

        View rootView = inflater.inflate(R.layout.detail_res_herb_fragment, parentViewGroup, false);

        this.toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        this.toolbar.setLogo(R.mipmap.ic_launcher);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        this.scrollFragment = (ScrollView) rootView.findViewById(R.id.scrollFragmentLayout);
        this.imageViewUrl = (ImageView) rootView.findViewById(R.id.img_hierba_card_res_ppal);
        this.textViewNombre = (TextView) rootView.findViewById(R.id.txt_hierba_name_detail);
        this.textViewNombreCientifico = (TextView) rootView.findViewById(R.id.txt_hierba_name_cientif_detail);
        this.textViewAlias = (TextView) rootView.findViewById(R.id.txt_hierba_alias_detail);
        this.textViewDescripcion = (TextView) rootView.findViewById(R.id.txt_hierba_description_detail);
        this.textViewSeccionUso = (TextView) rootView.findViewById(R.id.txt_hierba_section_use_detail);
        this.textViewPropiedades = (TextView) rootView.findViewById(R.id.txt_hierba_properties_detail);
        this.textViewIndicaciones = (TextView) rootView.findViewById(R.id.txt_hierba_indications_detail);
        this.textViewContraIndicaciones = (TextView) rootView.findViewById(R.id.txt_hierba_contraindications_detail);
        this.textViewEmpleo = (TextView) rootView.findViewById(R.id.txt_hierba_empleo_detail);
        this.textViewSintomas = (TextView) rootView.findViewById(R.id.txt_hierba_symptoms_detail);
        this.textViewUbicacion = (TextView) rootView.findViewById(R.id.txt_hierba_ubication_detail);
        this.textViewGastronomia = (TextView) rootView.findViewById(R.id.txt_hierba_gastronomy_detail);
        this.botonAbrirComentario = (FloatingActionButton)rootView.findViewById(R.id.btn_open_comment_detail);
        this.botonRegreso = (FloatingActionButton) rootView.findViewById(R.id.btn_regresar_detail);

        //View viewScrollFrag = rootView.findViewById(R.id.scrollFragmentLayout);
        //FrameLayout.LayoutParams viewScrollFragParam = (FrameLayout.LayoutParams) viewScrollFrag.getLayoutParams();
        //View viewBtnComentario = rootView.findViewById(R.id.btn_open_comment_detail);
        //RelativeLayout.LayoutParams viewBtnComentarioParam = (RelativeLayout.LayoutParams) viewBtnComentario.getLayoutParams();

        //View viewBtnRegresar = rootView.findViewById(R.id.btn_regresar_detail);
        //RelativeLayout.LayoutParams viewBtnRegresarParam = (RelativeLayout.LayoutParams) viewBtnRegresar.getLayoutParams();
        /*int leftScrollFrag = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_left_scrollfrag_item));
        int topScrollFrag = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_top_scrollfrag_item));
        int rightScrollFrag = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_right_scrollfrag_item));
        int bottomScrollFrag = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_bottom_scrollfrag_item));
        int bottomPubScrollFrag = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_bottomPub_scrollfrag_item));
        */
        /*int leftFloatComment = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_left_floatbtncom_item));
        int leftFloatRegresar = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_left_floatbtnreg_item));
        int top = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_top_floatbtn_item));
        int rightFloatComment = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_right_floatbtncom_item));
        int rightFloatRegresar = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_right_floatbtnreg_item));
        int bottom = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_bottom_floatbtn_item));
        int bottomPub = getPixelValue(getActivity(), (int) getResources().getDimension(R.dimen.margin_bottomPub_floatbtn_item));
        */
        /*if(!Constants.isAdsDisabled){
            viewBtnComentarioParam.addRule(RelativeLayout.ABOVE, R.id.adBannerView);
            viewBtnRegresarParam.addRule(RelativeLayout.ABOVE, R.id.adBannerView);
            viewBtnComentario.setLayoutParams(viewBtnComentarioParam);
            viewBtnRegresar.setLayoutParams(viewBtnRegresarParam);
        }*/

        if(getArguments() != null) {
            hierba = null;
            hierba = (HierbasBean) getArguments().get("HIERBAS_BEAN");

            //SET DE VALORES A LOS ELEMENTOS DE LA VISTA
            imageViewUrl.setImageURI(Uri.parse(hierba.getImgurl()));
            textViewNombre.setText(hierba.getNombre());
            textViewNombreCientifico.setText("(" + hierba.getNombreCientifico() + ")");
            textViewAlias.setText(Html.fromHtml(getString(R.string.aliasFragTitle) + hierba.getAlias().toString().replaceAll("[^A-Za-zÑñáéíóúÁÉÍÓÚ, ]", "")));
            textViewDescripcion.setText(Html.fromHtml(getString(R.string.descFragTitle)) + hierba.getDescripcion());
            textViewDescripcion.setOnTouchListener(new TextAnimationColor(getActivity()));
            textViewSeccionUso.setText(Html.fromHtml(getString(R.string.sectionFragTitle)) + hierba.getSeccion());
            textViewPropiedades.setText(Html.fromHtml(getString(R.string.propertiesFragTitle)) + hierba.getPropiedades().toString().replaceAll("[^A-Za-zÑñáéíóúÁÉÍÓÚ, ]", "")); //.replaceAll("[^a-zA-Z, ]", ""));
            textViewIndicaciones.setText(Html.fromHtml(getString(R.string.indicationsFragTitle)) + hierba.getIndicaciones());
            textViewIndicaciones.setOnTouchListener(new TextAnimationColor(getActivity()));
            textViewContraIndicaciones.setText(Html.fromHtml(getString(R.string.contraIndicationsFragTitle)) + hierba.getContraIndicaciones());
            textViewContraIndicaciones.setOnTouchListener(new TextAnimationColor(getActivity()));
            textViewEmpleo.setText(Html.fromHtml(getString(R.string.useFragTitle)) + hierba.getEmpleo());
            textViewEmpleo.setOnTouchListener(new TextAnimationColor(getActivity()));
            textViewSintomas.setText(Html.fromHtml(getString(R.string.symptomsFragTitle)) + hierba.getSintomas().toString().replaceAll("[^A-Za-zÑñáéíóúÁÉÍÓÚ, ]", ""));//("[^a-zA-Z, ]", ""));
            textViewSintomas.setOnTouchListener(new TextAnimationColor(getActivity()));
            textViewUbicacion.setText(Html.fromHtml(getString(R.string.locationFragTitle)) + hierba.getUbicacion());
            textViewGastronomia.setText(Html.fromHtml(getString(R.string.gastronomyFragTitle)) + hierba.getGastronomia());
            textViewGastronomia.setOnTouchListener(new TextAnimationColor(getActivity()));

            int x = 1;
            final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_fragment_id);

            //TITULO DE COMENTARIOS
            if (hierba.getComentarios() != null
                    && hierba.getComentarios().size() > 0) {

                TextView textTituloComentarios = new TextView(mContext);
                textTituloComentarios.setText(getString(R.string.titleComments));
                textTituloComentarios.setTextColor(Color.parseColor("#000000"));
                textTituloComentarios.setTextSize(14);
                textTituloComentarios.setTypeface(null, Typeface.BOLD);
                textTituloComentarios.setGravity(Gravity.CENTER);
                textTituloComentarios.setVisibility(View.INVISIBLE);
                linearLayout.addView(textTituloComentarios);

            //LISTA DE COMENTARIOS
            for (ComentarioBean comentarioBean : hierba.getComentarios()) {

                if ("habilitado".equalsIgnoreCase(comentarioBean.getEstado())) {

                    if(textTituloComentarios.getVisibility() == View.INVISIBLE) {
                        textTituloComentarios.setVisibility(View.VISIBLE);
                    }

                    x++;
                    contadorAuxiliarId++;

                    //LINEAR LAYOUT PRINCIPAL CONTENEDOR
                    final LinearLayout linearLayoutCommentPpal = new LinearLayout(mContext);
                    linearLayoutCommentPpal.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams paramsPpal = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramsPpal.setMargins(30, 20, 30, 20);
                    linearLayoutCommentPpal.setLayoutParams(paramsPpal);
                    linearLayoutCommentPpal.setBackgroundColor(Color.parseColor("#f9f4f9"));
                    //use a GradientDrawable with only one color set, to make it a solid color
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFFFFFF); //white background
                    border.setStroke(2, 0xFF000000); //black border with full opacity
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        linearLayoutCommentPpal.setBackgroundDrawable(border);
                    } else {
                        linearLayoutCommentPpal.setBackground(border);
                    }

                    //LINEAR HORIZONTAL PARA IMAGEN DE PERFIL Y BANDERA
                    final LinearLayout linearHorizontalLayout = new LinearLayout(mContext);
                    linearHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams paramsHorizontal = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramsHorizontal.setMargins(30, 0, 30, 10);
                    linearHorizontalLayout.setLayoutParams(paramsHorizontal);

                    try {
                        //PARAMETROS GENERALEES DE LINEAR LAYOUT PARA LA SECCION DE COMENTARIOS
                        LinearLayout.LayoutParams params;

                        if(comentarioBean.getImgFbUrlUsuario() != null && !comentarioBean.getImgFbUrlUsuario().isEmpty()) {
                            ImageView imgProfileFacebook = new ImageView(mContext);
                            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.gravity = Gravity.LEFT;
                            imgProfileFacebook.setLayoutParams(params);
                            Picasso.with(mContext).load(comentarioBean.getImgFbUrlUsuario()).into(imgProfileFacebook);
                            linearHorizontalLayout.addView(imgProfileFacebook);
                        }


                        //IMAGEN BANDERA COMENTARIO Y TEXTO CIUDAD, PAIS
                        if(comentarioBean.getPais() != null) {
                            ImageView imgBandera = new ImageView(mContext);
                            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.gravity= Gravity.RIGHT;
                            imgBandera.setLayoutParams(params);

                            if ("Chile".equalsIgnoreCase(comentarioBean.getPais().trim())) {
                                Picasso.with(mContext).load(R.drawable.chileflag).resize(60, 40).into(imgBandera);
                            } else if ("Venezuela".equalsIgnoreCase(comentarioBean.getPais().trim())) {
                                Picasso.with(mContext).load(R.drawable.vzlaflag).resize(60, 40).into(imgBandera);
                            } else if ("Argentina".equalsIgnoreCase(comentarioBean.getPais().trim())) {
                                Picasso.with(mContext).load(R.drawable.argflag).resize(60, 40).into(imgBandera);
                            } else if ("United States".equalsIgnoreCase(comentarioBean.getPais().trim()) ||
                                    "Estados Unidos".equalsIgnoreCase(comentarioBean.getPais().trim())) {
                                Picasso.with(mContext).load(R.drawable.usaflag).resize(60, 40).into(imgBandera);
                            } else if (comentarioBean.getPais().trim() != ""){
                                //RECORREMOS PAISES DE EUROPA
                                for(String countryEU :Constants.europeCountries){
                                    if(countryEU.equalsIgnoreCase(comentarioBean.getPais().trim())){
                                        Picasso.with(mContext).load(R.drawable.euflag).resize(60, 40).into(imgBandera);
                                    }
                                }
                                //VALIDAMOS SI SIGUE VACIO EL IMAGEVIEW DESPUES DEL FOR DE EUROPA Y ASIGNAMOS INTERNACIONAL
                                if(imgBandera.getDrawable() == null){
                                    Picasso.with(mContext).load(R.drawable.worldflag).resize(60, 40).into(imgBandera);
                                }
                            }
                            else{
                                Picasso.with(mContext).load(R.drawable.worldflag).resize(60, 40).into(imgBandera);
                            }
                            linearHorizontalLayout.addView(imgBandera);
                        }

                        linearLayoutCommentPpal.addView(linearHorizontalLayout);

                        if((comentarioBean.getPais() != null && comentarioBean.getPais() != "")
                                && (comentarioBean.getCiudad() != null && comentarioBean.getCiudad() != "")){

                            TextView txtCiudadPais = new TextView(mContext);
                            //TEXTO CIUDAD PAIS
                            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(20, 20, 20, 10);
                            txtCiudadPais.setLayoutParams(params);
                            if (!TextUtils.isEmpty(comentarioBean.getCiudad())) {
                                txtCiudadPais.setText(comentarioBean.getCiudad() + ", " + comentarioBean.getPais());
                            }
                            txtCiudadPais.setTextSize(10);
                            txtCiudadPais.setGravity(Gravity.LEFT);
                            txtCiudadPais.setTextColor(Color.parseColor("#3d0d0d"));
                            linearLayoutCommentPpal.addView(txtCiudadPais);
                        }

                        //TEXTO DEL COMENTARIO
                        TextView txtComment = new TextView(mContext);
                        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(20, 0, 20, 10);
                        txtComment.setLayoutParams(params);
                        txtComment.setId(contadorAuxiliarId + x + x);
                        txtComment.setTextSize(12);
                        txtComment.setText(Html.fromHtml(getString(R.string.commentUsuario) + comentarioBean.getNombreUsuario() + "<br/>" +
                                getString(R.string.commentEmail) + comentarioBean.getEmailUsuario() + "<br/>" +
                                getString(R.string.commentText) + comentarioBean.getComentario()));

                        txtComment.setTextColor(Color.parseColor("#000000"));
                        txtComment.setGravity(Gravity.LEFT);
                        linearLayoutCommentPpal.addView(txtComment);
                        //AGREGAMOS AL LINEAR SUPERIOR EL LINEAR DE COMENTARIOS
                        linearLayout.addView(linearLayoutCommentPpal);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "Error: ", e);
                    }

                }//FIN HABILITADO

            }

        }//VALIDACION EXISTENCIA DE COMENTARIOS

            Log.d(TAG, "Nombre Hierba en DetailFrag onCreateView: " + hierba.getNombre());
        }

        this.botonAbrirComentario.setOnClickListener(new View.OnClickListener() {

            Context mContext = getActivity();

            @Override
            public void onClick(View v) {

                if (mContext instanceof DetailResActivity) {
                    Log.d(TAG, "ProfileUser en la instancia de comentarios: " + profileUser);

                    if (accessToken != null) {
                        Log.d(TAG, "Access Token: " + accessToken.getUserId());
                    }

                        LayoutInflater li = LayoutInflater.from(mContext);
                        final View vistaDialogo = li.inflate(R.layout.detail_herb_dialogo_comentario, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                mContext);
                        alertDialogBuilder.setView(vistaDialogo);

                        final TextView textTitulo = (TextView) vistaDialogo.findViewById(R.id.textDialogoTitulo);
                        //GET REFERENCIAS DE VISTA
                        layoutUserName = (TextInputLayout) vistaDialogo.findViewById(R.id.layoutNombreUsuario);
                        editTextUserName = (EditText) vistaDialogo.findViewById(R.id.nombreUsuario);
                        layoutEmailName = (TextInputLayout) vistaDialogo.findViewById(R.id.layoutEmailUsuario);
                        editTextEmail = (EditText) vistaDialogo.findViewById(R.id.emailUsuario);
                        layoutComentario = (TextInputLayout) vistaDialogo.findViewById(R.id.layoutComentario);
                        editTextComentario = (EditText) vistaDialogo.findViewById(R.id.comentario);
                        btnTomarDatosFacecbook = (Button) vistaDialogo.findViewById(R.id.btnTomarDatosFacecbook);

                        editTextUserName.addTextChangedListener(new MyTextWatcher(editTextUserName));
                        editTextEmail.addTextChangedListener(new MyTextWatcher(editTextEmail));

                        if(accessToken != null){
                            btnTomarDatosFacecbook.setEnabled(false);
                            btnTomarDatosFacecbook.setBackground(ContextCompat.getDrawable(mContext,R.drawable.btn_grey_face));
                        }

                        //SI EL PROFILE DE FACE ESTA ACTIVO BUSCAMOS SETEAR EL ELEMENTO DE DATOS DE FACEBOOK
                        if (MemoryBeanAux.getUserFbData() == null && accessToken != null){
                            String url = Constants.URL_GRAPH_FACEBOOK_ME_DATA + accessToken.getToken();
                            ProgressBar progressBar = new ProgressBar(mContext);
                            JSONObject jsonObject = new JSONObject();
                            //INVOCAMOS LA CLASE QUE MANEJA TODA LA INVOCACION Y EL PARSEO
                            new PostAsyncHttpTask(mContext, progressBar, "GET_DATA_FACE_WITH_TOKEN", jsonObject,
                                    hierba, hierba.getId(), null).execute(url);
                        }

                        if (MemoryBeanAux.getUserFbData() != null && accessToken != null) {
                            //AGREGAMOS DATOS DE FACEBOOK Y HACEMOS NO EDITABLES LOS CAMPOS NOMBRE Y EMAIL
                            Log.d(TAG, "nameFB: " + MemoryBeanAux.getUserFbData()[0] + ", emailFB: " +
                                    MemoryBeanAux.getUserFbData()[3]);

                            if(!"sinnombre".equalsIgnoreCase(MemoryBeanAux.getUserFbData()[0])) {
                                layoutUserName.setErrorEnabled(false);
                                editTextUserName.setText(MemoryBeanAux.getUserFbData()[0]);
                                editTextUserName.setEnabled(false);
                                editTextUserName.setTextColor(Color.parseColor("#293082"));
                            }

                            if(!"sincorreo".equalsIgnoreCase(MemoryBeanAux.getUserFbData()[3])){
                                layoutEmailName.setErrorEnabled(false);
                                editTextEmail.setText(MemoryBeanAux.getUserFbData()[3]);
                                editTextEmail.setEnabled(false);
                                editTextEmail.setTextColor(Color.parseColor("#293082"));
                            }
                        }

                        editTextComentario.addTextChangedListener(new MyTextWatcher(editTextComentario));

                        // set dialog message
                        alertDialogBuilder
                                .setPositiveButton(getString(R.string.btn_enviar),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                try {
                                                    //GET USERNAME Y PASSWORD
                                                    String usuario = editTextUserName.getText().toString();
                                                    String email = editTextEmail.getText().toString();
                                                    String comentario = editTextComentario.getText().toString();

                                                    //VALIDAMOS LOS DATOS DEL FORMULARIO ANTES DE ENVIAR
                                                    if (MemoryBeanAux.getUserFbData() != null || (!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(email)
                                                            && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                                            && comentario.length() > 4)) {
                                                        //NUEVA CONEXION AL SERVIDOR DEJAMOS COMENTADO LA DE BD LOCAL PARA FUTURAS IMPLEMENTACIONES
                                                        ProgressBar progressBar = new ProgressBar(mContext);
                                                        JSONObject jsonObject = new JSONObject();

                                                        if(hierba.getComentarios() != null){
                                                            jsonObject.put("id", hierba.getComentarios().size() + 1);
                                                        }else{
                                                            jsonObject.put("id", 1);
                                                        }
                                                        String nombreToSend = MemoryBeanAux.getUserFbData() != null ? MemoryBeanAux.getUserFbData()[0] : "sinnombre";
                                                        if("sinnombre".equalsIgnoreCase(nombreToSend)) {nombreToSend = usuario;}
                                                        jsonObject.put("nombreUsuario", nombreToSend);
                                                        jsonObject.put("fechaNacUsuario", MemoryBeanAux.getUserFbData() != null ? MemoryBeanAux.getUserFbData()[2] : "");

                                                        String emailToSend = MemoryBeanAux.getUserFbData() != null ? MemoryBeanAux.getUserFbData()[3] : "sincorreo";
                                                        if("sincorreo".equalsIgnoreCase(emailToSend)){emailToSend = email;}
                                                        jsonObject.put("emailUsuario", emailToSend);
                                                        jsonObject.put("comentario", comentario);
                                                        jsonObject.put("ciudad", MemoryBeanAux.getUserFbData() != null ? MemoryBeanAux.getUserFbData()[4] : "");
                                                        jsonObject.put("pais", MemoryBeanAux.getUserFbData() != null ? MemoryBeanAux.getUserFbData()[5] : "");
                                                        jsonObject.put("imgFbUrlUsuario", MemoryBeanAux.getUserFbUlrImage() != null ? MemoryBeanAux.getUserFbUlrImage() : profileUser != null ? profileUser.getProfilePictureUri(100, 100).toString() : "");
                                                        jsonObject.put("estado", "inhabilitado");//estado inhabilitado por defecto

                                                        //URL DEL API QUE INVOCAMOS CON SU RESPECTIVA OPERACION
                                                        url = Constants.URL_SERVIDOR_RMT_APP_HIERBAS + Constants.URL_POST_COMENTARIO_HIERBA + hierba.getId();

                                                        new PostAsyncHttpTask(mContext, progressBar, "ADD_COMMENT", jsonObject,
                                                                hierba, hierba.getId(), dialog).execute(url);
                                                    } else {
                                                        Toast.makeText(mContext, getString(R.string.errorCommentForm), Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (Exception e) {
                                                    Toast.makeText(mContext, getString(R.string.errorGral2) + e.toString(), Toast.LENGTH_SHORT).show();
                                                    e.printStackTrace();
                                                }

                                            }
                                        })
                                .setNegativeButton(getString(R.string.btn_cancelar),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        final AlertDialog alertDialog = alertDialogBuilder.create();

                        btnTomarDatosFacecbook.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent fbLogin = new Intent(getActivity(), FacebookLoginComment.class);
                                startActivity(fbLogin);
                                alertDialog.dismiss();
                            }
                        });

                        // show it
                    alertDialog.show();
                }
            }
        });

        botonRegreso.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d(TAG, "entro en boton onClick");
                if (getActivity() instanceof DetailResActivity){
                    Log.d(TAG, "entro en boton onClick II etapa");
                    getActivity().finish();
                }

            }
        });

        return rootView;
    }

    /**
     * Valida el edittext de ingreso de nombre en el dialogo
     * @return boolean
     */
    private boolean validateName() {
        if (editTextUserName.getText().toString().trim().isEmpty()) {
            layoutUserName.setError(getString(R.string.error_nombre_dialogo));
            requestFocus(editTextUserName);
            return false;
        } else {
            layoutUserName.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Valida el edittext de ingreso de email del usuario
     * @return boolean
     */
    private boolean validateEmail() {
        String email = editTextEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            layoutEmailName.setError(getString(R.string.error_email_dialogo));
            requestFocus(editTextEmail);
            return false;
        } else {
            layoutEmailName.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Valida el edittext de ingreso del comentario
     * @return
     */
    private boolean validateComment() {
        if (editTextComentario.getText().toString().trim().isEmpty()) {
            layoutComentario.setError(getString(R.string.error_comentarios_dialogo));
            requestFocus(editTextComentario);
            return false;
        } else {
            layoutComentario.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

            switch (view.getId()) {
                case R.id.nombreUsuario:
                    validateName();
                    break;
                case R.id.emailUsuario:
                    validateEmail();
                    break;
                case R.id.comentario:
                    validateComment();
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(profileUser == null) {
            profileUser = Profile.getCurrentProfile();
        }*/
        if(Profile.getCurrentProfile() != null) {
            profileUserFb = Profile.getCurrentProfile();
        }
        if(AccessToken.getCurrentAccessToken() != null){
            accessToken = AccessToken.getCurrentAccessToken();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    public static int getPixelValue(Context context, int dimenId) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dimenId,
                resources.getDisplayMetrics()
        );
    }

}