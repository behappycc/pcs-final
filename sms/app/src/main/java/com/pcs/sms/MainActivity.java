package com.pcs.sms;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private EditText edtPhoneNumber;
    private Button btnSendSms, btnTestSms, btnTestMyBroadcast;
    private TextView txtResult, txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        btnSendSms = (Button) findViewById(R.id.btnSendSms);
        btnTestSms = (Button) findViewById(R.id.btnTestSms);
        btnTestMyBroadcast = (Button) findViewById(R.id.btnTestMyBroadcast);
        txtResult = (TextView) findViewById(R.id.txtResult);
        txtAddress = (TextView) findViewById(R.id.smsAddress);

        btnSendSms.setOnClickListener(btnSendSmsOnClick);
        btnTestSms.setOnClickListener(btnTestSmsOnClick);
        btnTestMyBroadcast.setOnClickListener(btnTestMyBroadcastOnClick);
    }

    private Button.OnClickListener btnTestSmsOnClick = new Button.OnClickListener(){
        public void onClick(View v){
            Intent it = new Intent("android.provider.Telephony.SMS_RECEIVED");
            it.putExtra("sender_name", "hellosms");
            sendBroadcast(it);
        }
    };

    private Button.OnClickListener btnTestMyBroadcastOnClick = new Button.OnClickListener(){
        public void onClick(View v){
            Intent it = new Intent("my_broadcast");
            it.putExtra("sender_name", "hellomybroadcast");
            sendBroadcast(it);
        }
    };

    private Button.OnClickListener btnSendSmsOnClick = new Button.OnClickListener(){
        public void onClick(View v){
            String strPhoneNumber = edtPhoneNumber.getText().toString();
            if(strPhoneNumber.length()==11){
                txtResult.setText(strPhoneNumber);
            }
            else{
                txtResult.setText("wrong number");
            }

        }
    };
}
