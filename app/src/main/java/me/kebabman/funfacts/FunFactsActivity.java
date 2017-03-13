package me.kebabman.funfacts;


import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import me.kebabman.funfacts.services.GetFactService;
import me.kebabman.funfacts.services.RegistrationAsyncTask;


public class FunFactsActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private Tracker mTracker;
    private GoogleApiClient client;
    Button showFactButton;
    TextView factLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupScreen();
        setupBackgoundWork();


        showFactButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new GetFactService(FunFactsActivity.this).execute();
                sendClick();
            }
        });

    }


    //Hit builder for google analytics, called everytime the button is pressed
    public void sendClick() {
        mTracker.send(new HitBuilders.EventBuilder()
                .setAction("click")
                .setLabel("new fact")
                .build());
    }

    //moved all screen setup activites to a new method
    public void setupScreen() {
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
        }
        findViewsById();
    }

    private void findViewsById(){
        factLabel = (TextView) findViewById(R.id.factTextView);
        showFactButton = (Button) findViewById(R.id.newFactButton);
    }

    private void setupBackgoundWork() {
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        ApplicationController application = (ApplicationController) getApplication();
        mTracker = application.getDefaultTracker();
        new RegistrationAsyncTask(FunFactsActivity.this).execute(Config.SENDER_ID);
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "quizzle.me", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("https://quizzle.me"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://me.kebabman.quizzle/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }


    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "quizzle.me", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("https://quizzle.me"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://me.kebabman.quizzle/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // You need to do the Play Services APK check here too.
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    public void updateFactText(String fact_string){
        factLabel.setText(fact_string);
    }

}