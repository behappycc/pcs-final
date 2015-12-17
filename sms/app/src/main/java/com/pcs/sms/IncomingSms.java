package com.pcs.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by hubert on 2015/12/16.
 */

public class IncomingSms extends BroadcastReceiver {
    public IncomingSms() {

    }

    public void onReceive(Context context, Intent intent) {
        switch(intent.getAction()){
            case "android.provider.Telephony.SMS_RECEIVED":
                String sender = intent.getStringExtra("sender_name");
                Toast.makeText(context, "sms " + sender, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
