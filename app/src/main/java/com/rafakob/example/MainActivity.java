package com.rafakob.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rafakob.nsdhelper.NsdHelper;
import com.rafakob.nsdhelper.NsdListener;
import com.rafakob.nsdhelper.NsdService;
import com.rafakob.nsdhelper.NsdType;

public class MainActivity extends AppCompatActivity implements NsdListener {
    private NsdHelper nsdHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nsdHelper = new NsdHelper(this, this);
        nsdHelper.setLogEnabled(true);

        nsdHelper.registerService("Chat", NsdType.HTTP);
        nsdHelper.startDiscovery(NsdType.HTTP);
    }

    @Override
    protected void onStop() {
        super.onStop();
        nsdHelper.stopDiscovery();
        nsdHelper.unregisterService();
    }

    @Override
    public void onNsdRegistered(NsdService registeredService) {

    }

    @Override
    public void onNsdDiscoveryFinished() {

    }

    @Override
    public void onNsdServiceFound(NsdService foundService) {

    }

    @Override
    public void onNsdServiceResolved(NsdService resolvedService) {

    }

    @Override
    public void onNsdServiceLost(NsdService lostService) {

    }

    @Override
    public void onNsdError(String errorMessage, int errorCode, String errorSource) {

    }
}
