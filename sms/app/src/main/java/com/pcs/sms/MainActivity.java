package com.pcs.sms;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private EditText edtPhoneNumber;
    private Button btnSendSms, btnTestSms;
    private TextView txtResult, txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupViewComponent();
        /*
        //Define user variable
        UserDefined.filter = "$FINDME$";

        //Initialize database and command handler
        Recorder.init(this, "MainActivity");
        CommandHandler.init(this);

        CommandHandler.getSharedCommandHandler().addExecutor("WHERE", new ExecutorWhere());
        */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewComponent(){
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        btnSendSms = (Button) findViewById(R.id.btnSendSms);
        btnTestSms = (Button) findViewById(R.id.btnTestSms);
        txtResult = (TextView) findViewById(R.id.txtResult);
        txtAddress = (TextView) findViewById(R.id.smsAddress);

        btnSendSms.setOnClickListener(btnSendSmsOnClick);
        btnTestSms.setOnClickListener(btnTestSmsOnClick);
    }

    private Button.OnClickListener btnTestSmsOnClick = new Button.OnClickListener(){
        public void onClick(View v){
            Intent it = new Intent("android.provider.Telephony.SMS_RECEIVED");
            it.putExtra("sender_name", "hello");
            sendBroadcast(it);
        }
    };

    private Button.OnClickListener btnSendSmsOnClick = new Button.OnClickListener(){
        public void onClick(View v){
            String strPhoneNumber = edtPhoneNumber.getText().toString();
            if(strPhoneNumber.length()==11){
                txtResult.setText(strPhoneNumber);
                /*
                Recorder rec = Recorder.getSharedRecorder();
                CommandHandler hdlr = CommandHandler.getSharedCommandHandler();
                SQLiteDatabase db = rec.getWritableDatabase();
                int device_id = rec.getDeviceIdByPhonenumberOrCreate(db, strPhoneNumber);
                db.close();
                hdlr.execute("WHERE", device_id, 0, null);
                */
            }
            else{
                txtResult.setText("wrong number");
            }

        }
    };
}
