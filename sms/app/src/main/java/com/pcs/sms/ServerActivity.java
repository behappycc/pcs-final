package com.pcs.sms;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerActivity extends ActionBarActivity {
    private Button btnServerConnect;
    private EditText edtUsername, edtPassword;
    private TextView txtResult;

    public String httpResponseResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        setupViewComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewComponent(){
        btnServerConnect = (Button) findViewById(R.id.btnServerConnect);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtResult = (TextView) findViewById((R.id.txtResult));

        btnServerConnect.setOnClickListener(btnServerConnectOnClick);
    }


    private Button.OnClickListener btnServerConnectOnClick = new Button.OnClickListener(){
        public void onClick(View v){
            String strUsername = edtUsername.getText().toString();
            String strPassword = edtPassword.getText().toString();

            //SendResultTask sendResultTask = new SendResultTask(strUsername, strPassword);
            //sendResultTask.execute();

            ConnectPiTask connectPiTask = new ConnectPiTask(strUsername, strPassword);
            connectPiTask.execute();
        }
    };

    private class ConnectPiTask extends AsyncTask<Object, Integer, Long>
    {
        private String username;
        private String password;
        private String uname;
        private String pword;
        private String loginInfo;
        public ConnectPiTask(
                String username,
                String password
        ){
            this.username = username;
            this.password = password;
        }

        protected Long doInBackground(Object... abc)
        {
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

            //making POST request.
            try {
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                //get hheader

                String result = EntityUtils.toString(entity);
                // write response to log
                Log.d("Http Post Response:", result);

                JSONObject jsonObject = new JSONObject(result);
                uname = jsonObject.optString("username").toString();
                pword = jsonObject.optString("password").toString();
                loginInfo = jsonObject.optString("loginInfo").toString();

            } catch (ClientProtocolException e) {
                // Log exception
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            } catch (org.json.JSONException e){

            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress)
        {

        }

        protected void onPostExecute(Long result)
        {
            if(loginInfo.equals("login successful")){
                txtResult.setText("login successful");
            }
            else if(loginInfo.equals("login unsuccessful")){
                txtResult.setText("The email and password you entered don't match.");
            }
            else if(loginInfo == null ){
                txtResult.setText("null");
            }
            else
                txtResult.setText("error");
        }
    }
}