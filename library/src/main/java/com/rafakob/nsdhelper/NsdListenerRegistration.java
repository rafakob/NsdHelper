package com.rafakob.nsdhelper;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

public class NsdListenerRegistration implements NsdManager.RegistrationListener {
    private static final String ERROR_SOURCE = "android.net.nsd.NsdManager.RegistrationListener";
    private final NsdHelper mNsdHelper;

    public NsdListenerRegistration(NsdHelper nsdHelper) {
        this.mNsdHelper = nsdHelper;
    }

    @Override
    public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
        mNsdHelper.logError("Service registration failed!", errorCode, ERROR_SOURCE);
    }

    @Override
    public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
        mNsdHelper.logError("Service unregistration failed!", errorCode, ERROR_SOURCE);
    }

    @Override
    public void onServiceRegistered(NsdServiceInfo serviceInfo) {
        mNsdHelper.logMsg("Registered -> " + serviceInfo.getServiceName());
        mNsdHelper.onRegistered(serviceInfo.getServiceName());
    }

    @Override
    public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
        mNsdHelper.logMsg("Unregistered -> " + serviceInfo.getServiceName());
    }
}
