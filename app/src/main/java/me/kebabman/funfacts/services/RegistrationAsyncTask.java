package me.kebabman.funfacts.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import me.kebabman.funfacts.ApplicationController;
import me.kebabman.funfacts.Config;
import me.kebabman.funfacts.FunFactsActivity;
import com.jaredrummler.android.device.DeviceName;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class RegistrationAsyncTask extends AsyncTask<String, Void, Void>{

    Activity mActivity;
    ProgressDialog mProgressDialog;
    String token;
    String osVersion = android.os.Build.VERSION.RELEASE;
    int sdkVersion = android.os.Build.VERSION.SDK_INT;
    String deviceName = DeviceName.getDeviceName();
    String deviceTimeZone = TimeZone.getDefault().getDisplayName();

    public RegistrationAsyncTask(Activity activity){
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgressDialog != null) mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String senderId = params[0];
        Log.d("test", "sender id : " + senderId);

        try {
            // Get the token from GCM server.
            InstanceID instanceID = InstanceID.getInstance(mActivity);
            token = instanceID.getToken(senderId,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i("test", "GCM Registration Token: " + token);

            sendTokenToServer();

            // Storing that the token has already been sent.
            sharedPreferences.edit().putBoolean(Config.KEY_TOKEN_SENT_TO_SEVER, true).apply();

        } catch (Exception e) {
            Log.d("test", "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(Config.KEY_TOKEN_SENT_TO_SEVER, false).apply();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }


    private void sendTokenToServer(){

        StringRequest request = new StringRequest(Request.Method.POST,Config.APPLICATION_SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Show error toast here.
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("registration_id", token);
                params.put("device_name", deviceName);
                params.put("device_time_zone", deviceTimeZone);
                params.put("osVersion", osVersion);
                params.put("sdkVersion", String.valueOf(sdkVersion));
                return params;
            }
        };

        ApplicationController.getInstance().addToRequestQueue(request);
    }


}
