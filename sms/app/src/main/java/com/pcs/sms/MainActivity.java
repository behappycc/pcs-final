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
import android.telephony.SmsManager;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private EditText edtPhoneNumber, edtUsername, edtPassword;
    private Button btnSendSms, btnTestSms, btnTestMyBroadcast, btnLoginServer;
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
        btnLoginServer = (Button) findViewById(R.id.btnLoginServer);
        txtResult = (TextView) findViewById(R.id.txtResult);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSendSms.setOnClickListener(btnSendSmsOnClick);
        btnTestSms.setOnClickListener(btnTestSmsOnClick);
        btnLoginServer.setOnClickListener(btnLoginServerOnClick);
    }

    private Button.OnClickListener btnTestSmsOnClick = new Button.OnClickListener(){
        public void onClick(View v){
            Intent it = new Intent("android.provider.Telephony.SMS_RECEIVED");
            it.putExtra("sender_name", "hellosms");
            sendBroadcast(it);
        }
    };

    private Button.OnClickListener btnSendSmsOnClick = new Button.OnClickListener(){
        public void onClick(View v){
            String strPhoneNumber = edtPhoneNumber.getText().toString();
            if(strPhoneNumber.length()==11){
                /*txtResult.setText(strPhoneNumber);*/
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(strPhoneNumber, null, "GetValue", null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                    txtResult.setText("sent successfully");
                }

                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else{
                txtResult.setText("wrong number");
            }

        }
    };

    private Button.OnClickListener btnLoginServerOnClick = new Button.OnClickListener(){
        public void onClick(View v){
            String strUsername = edtUsername.getText().toString();
            String strPassword = edtPassword.getText().toString();
            if(strUsername.equals("pcs") && strPassword.equals("123")){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ServerActivity.class);
                startActivity(intent);
            }
        }
    };


}
