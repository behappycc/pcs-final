package com.pcs.sms;

/**
 * Created by hubert on 2015/12/13.
 */

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.mcnlab.lib.smscommunicate.*;

import java.text.DecimalFormat;

public class ExecutorWhere implements Executor{
    @Override
    public JSONObject execute(Context context, int device_id, int count, JSONObject usr_json){
        Log.d("EXECUTOR", "Count= " + count);
        switch (count){
            case 0:
                return new JSONObject();

            case 1:
                final int device_id_closure = device_id;

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        JSONObject new_usr_json = new JSONObject();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        DecimalFormat df = new DecimalFormat("#.####");
                        try{
                            new_usr_json.put("lat", df.format(latitude)).put("lan", df.format(longitude));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        CommandHandler.getSharedCommandHandler().execute("WHERE", device_id_closure, 2, new_usr_json);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                }
                        return null;

        });

            case 2:
                return usr_json;

            case 3:
            default:
                try{
                    double lat = usr_json.getDouble("lat");
                    double lan = usr_json.getDouble("lon");
                }

        }


    }
}
