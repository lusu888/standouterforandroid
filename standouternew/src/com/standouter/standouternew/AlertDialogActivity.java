package com.standouter.standouternew;


import com.standouter.standouternew.Standouter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Message;
import android.view.Window;

/**
 * Quick dirty activity to display a dialog from services
 */
public class AlertDialogActivity extends Activity {
    // TODO combine this class with ClassZeroActivity

    private Standouter qapp;

	@Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setBackgroundDrawableResource(R.drawable.class_zero_background);
		qapp = (Standouter) getApplication();

		
        String title ="Message";
        String message ="You have received one message!";

        new AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, mOkListener)
        .setCancelable(false)
        .show();
    }

    private final OnClickListener mOkListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
			Message msg = new Message();

        	qapp.getHandler().sendMessage(msg); 
            AlertDialogActivity.this.finish();
        }
    };
}