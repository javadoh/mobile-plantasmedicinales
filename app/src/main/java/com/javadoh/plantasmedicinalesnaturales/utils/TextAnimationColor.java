package com.javadoh.plantasmedicinalesnaturales.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.javadoh.plantasmedicinalesnaturales.R;

/**
 * Created by Laptop on 30-10-2017.
 */

public class TextAnimationColor implements View.OnTouchListener {

    Activity mContext;

    public TextAnimationColor(Activity mContext){
        this.mContext = mContext;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:

                ((TextView)view).setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText)); //white
                ((TextView)view).setLineSpacing(1, 1);
                ((TextView)view).setBackground(mContext.getResources().getDrawable(R.drawable.detail)); //white
                ((TextView)view).setTextSize(16);

                /*LayoutInflater li = LayoutInflater.from(mContext);
                View vistaDialogoDetail = li.inflate(R.layout.dialog_popup_detail_text, null);
                TextView textContent = (TextView) vistaDialogoDetail.findViewById(R.id.textContent);
                textContent.setText(content);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        mContext);

                alertDialogBuilder.setView(vistaDialogoDetail);
                alertDialogBuilder.setTitle(title);
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
                alertDialog.show();*/
                return true;

            case MotionEvent.ACTION_CANCEL:
                ((TextView)view).setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText)); //white
                ((TextView)view).setBackgroundColor(0x00000000); //white
                ((TextView)view).setTextSize(12); //white
            case MotionEvent.ACTION_UP:
                /*((TextView)view).setTextColor(0xFF000000); //black*/
                ((TextView)view).setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText)); //white
                ((TextView)view).setBackgroundColor(0x00000000); //white
                ((TextView)view).setTextSize(12); //white
                break;
        }
        return false;
    }
}
