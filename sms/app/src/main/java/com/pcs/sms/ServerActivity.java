package com.pcs.sms;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.Objects;

public class ServerActivity extends ActionBarActivity {
    private Button btnServerConnect;
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

        btnServerConnect.setOnClickListener(btnServerConnectOnClick);
    }

    private Button.OnClickListener btnServerConnectOnClick = new Button.OnClickListener(){
        public void onClick(View v){
            ConnectPiTask connectPiTask = new ConnectPiTask();
            connectPiTask.execute("apple");
        }
    };

    /*private class ConnectPiTask extends AsyncTask<Object, Integer, Long>
    {
        public ConnectPiTask(){

        }

        protected Long doInBackground(Object... abc)
        {
            return null;
        }

        protected void onProgressUpdate(Integer... progress)
        {

        }

        protected void onPostExecute(Long result) {

        }
    }*/

    private class ConnectPiTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... str) {
            try
            {
                /*String get_url = "http://10.109.9.42:3001/receive?q=" + str[0].replace(" ", "%20");*/
                String get_url = "http://cdict.net/?q=" + str[0].replace(" ", "%20");
                HttpClient Client = new DefaultHttpClient();
                HttpGet httpget;
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                httpget = new HttpGet(get_url);
                String content = Client.execute(httpget, responseHandler);
                return content;
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            return "Cannot Connect";
        }

        protected void onPostExecute(String result) {
            TextView tv = (TextView) findViewById(R.id.txtServerState);
            tv.setText(result);
        }
    }

}
