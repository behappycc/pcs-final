package com.pcs.sms;

/*import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

*//**
 * Created by hubert on 2015/12/16.
 *//*

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
}*/

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    String senderNum = "";

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: " + senderNum + ", message: " + message, duration);
                    toast.show();

                    if(message.equals("GetValue")) {
                        ConnectPiTask connectPiTask = new ConnectPiTask();
                        connectPiTask.execute("apple");
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    private class ConnectPiTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... str) {
            try
            {
                /*String get_url = "http://cdict.net/?q=" + str[0].replace(" ", "%20");*/
                String get_url = "http://140.112.91.221:8888/android";
                HttpClient Client = new DefaultHttpClient();
                HttpGet httpget;
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                httpget = new HttpGet(get_url);
                String content = Client.execute(httpget, responseHandler);

                /*String strJson="{\"temperature\": \"17C\", \"humidity\": \"80%\"}";*/
                String strJson = content;
                String data = "";
                try {
                    JSONObject jsonObject = new JSONObject(strJson);

                    String temper = jsonObject.optString("temperature").toString();
                    String humid = jsonObject.optString("humidity").toString();

                    data = "Temperature=" + temper + "\nHumidity= " + humid;
                } catch (JSONException e) {e.printStackTrace();}

                return data;
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            return "Cannot Connect";
        }

        protected void onPostExecute(String result) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(senderNum, null, result, null, null);
        }
    }

}