package com.pcs.sms;


//Created by hubert on 2015/12/14.

/*
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
*/

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms extends BroadcastReceiver {
    public IncomingSms() {

    }
    /*
    public void onReceive(Context context, Intent intent){
        String sender = intent.getStringExtra("sender_name");
        Toast.makeText(context, "sms " + sender, Toast.LENGTH_LONG).show();
    }
    */

    public void onReceive(Context context, Intent intent) {
        switch(intent.getAction()){
            case "android.provider.Telephony.SMS_RECEIVED":
                String sender = intent.getStringExtra("sender_name");
                Toast.makeText(context, "sms " + sender, Toast.LENGTH_LONG).show();
                break;
            case "my_broadcast":
                String sender1 = intent.getStringExtra("sender_name");
                Toast.makeText(context, "yo " + sender1, Toast.LENGTH_LONG).show();
                break;
        }
    }
}