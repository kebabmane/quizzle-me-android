package me.kebabman.funfacts.services;


import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService{

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        // Fetch updated token from the server again and send it to application server.
        new RegistrationAsyncTask(null).execute();
    }
}
