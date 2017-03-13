package me.kebabman.funfacts.services;

import me.kebabman.funfacts.ApplicationController;
import me.kebabman.funfacts.FunFactsActivity;
import me.kebabman.funfacts.Config;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request.Method;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class GetFactService extends AsyncTask<Void, Void, Void> {

    Activity mActivity;
    ProgressDialog mProgressDialog;
    String fact_string;

    public GetFactService(Activity activity){
        mActivity = activity;
        if (activity != null){
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("getting fresh fact....");
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgressDialog != null) mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        //test sample fact string for async task
        //fact_string = "you gaba gaba";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,Config.FACT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rand_record = response.getJSONObject("rand_record");
                            String fact_string = rand_record.getString("fact_string");
                            if (mProgressDialog != null) mProgressDialog.dismiss();
                            ((FunFactsActivity) mActivity).updateFactText(fact_string);
                        }
                        catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("App", "Error: " + error.getMessage());
                if (mProgressDialog != null) mProgressDialog.dismiss();
                ((FunFactsActivity) mActivity).updateFactText("ohhh no it's broked");
            }
        });

        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

}

