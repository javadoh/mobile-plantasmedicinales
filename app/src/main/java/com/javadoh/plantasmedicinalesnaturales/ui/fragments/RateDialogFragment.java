package com.javadoh.plantasmedicinalesnaturales.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.javadoh.plantasmedicinalesnaturales.R;

/**
 * Created by luiseliberal on 28/08/16.
 */
public class RateDialogFragment extends DialogFragment {
    private static final int LAUNCHES_UNTIL_PROMPT = 10;
    private static final int DAYS_UNTIL_PROMPT = 2;
    private static final int MILLIS_UNTIL_PROMPT = DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000;
    private static final String PREF_NAME = "APP_RATER";
    private static final String LAST_PROMPT = "LAST_PROMPT";
    private static final String LAUNCHES = "LAUNCHES";
    private static final String DISABLED = "DISABLED";

    public static void show(Context context, FragmentManager fragmentManager) {
        boolean shouldShow = false;
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long currentTime = System.currentTimeMillis();
        long lastPromptTime = sharedPreferences.getLong(LAST_PROMPT, 0);
        if (lastPromptTime == 0) {
            lastPromptTime = currentTime;
            editor.putLong(LAST_PROMPT, lastPromptTime);
        }

        if (!sharedPreferences.getBoolean(DISABLED, false)) {
            int launches = sharedPreferences.getInt(LAUNCHES, 0) + 1;
            if (launches > LAUNCHES_UNTIL_PROMPT) {
                if (currentTime > lastPromptTime + MILLIS_UNTIL_PROMPT) {
                    shouldShow = true;
                }
            }
            editor.putInt(LAUNCHES, launches);
        }

        if (shouldShow) {
            editor.putInt(LAUNCHES, 0).putLong(LAST_PROMPT, System.currentTimeMillis()).commit();
            new RateDialogFragment().show(fragmentManager, null);
        } else {
            editor.commit();
        }
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, 0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_rate_app, null))
        //return new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.rate_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                            getSharedPreferences(getActivity()).edit().putBoolean(DISABLED, true).commit();
                            dismiss();
                        }catch (Exception e){
                            Log.d("RateDialogFragment: ", getString(R.string.errorCannotRate));
                            Toast.makeText(getActivity(), getString(R.string.errorCannotRate), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }
                })
                .setNeutralButton(R.string.rate_remind_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.rate_never, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSharedPreferences(getActivity()).edit().putBoolean(DISABLED, true).commit();
                        dismiss();
                    }
                });
        return builder.create();
    }
}
