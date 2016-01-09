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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private String senderNum = "";
    private String username = "";
    private String password = "";

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

                    message = "{"+ message + "}";
                    Log.i("Transfer", "json: " + message);

                    JSONObject jsonObject = new JSONObject(message);
                    if(jsonObject.optString("role").equals("1")) {
                        username = jsonObject.optString("username");
                        password = jsonObject.optString("password");
                        ConnectPiTask connectPiTask = new ConnectPiTask();
                        connectPiTask.execute();
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    private class ConnectPiTask extends AsyncTask<Object, Void, String>
    {
        @Override
        protected String doInBackground(Object... abc) {

            HttpClient httpClient = new DefaultHttpClient();
            // replace with your url
            HttpPost httpPost = new HttpPost("http://140.112.42.151:8000/android");

            //Post Data
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("username", username));
            nameValuePair.add(new BasicNameValuePair("password", password));
            Log.d("Http Post send:", username);
            Log.d("Http Post send:", password);
            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }

            String data = "";
            //making POST request.
            try {
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                //get hheader

                String result = EntityUtils.toString(entity);
                // write response to log
                Log.d("Http Post Response:", result);

                JSONObject jsonObject = new JSONObject(result);
                String uname = jsonObject.optString("username").toString();
                String pword = jsonObject.optString("password").toString();
                String loginInfo = jsonObject.optString("loginInfo").toString();

                if(loginInfo.equals("login successful")){
                    /*data = "Temperature=" + temper + "\nHumidity= " + humid;*/
                    data = "Username=" + uname + "\nPassword= " + pword;
                }
                else if(loginInfo.equals("login unsuccessful")){
                    data = "The email and password you entered are not matched.";
                }
                else if(loginInfo == null ){
                    data = "null";
                }
                else
                    data = "error";

            } catch (ClientProtocolException e) {
                // Log exception
                e.printStackTrace();
                data = "ClientProtocolException";
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
                data = "IOException";
            } catch (org.json.JSONException e){
                data = "JSONException";
            }
            return data;
        }

        protected void onPostExecute(String result) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(senderNum, null, result, null, null);
        }
    }

}