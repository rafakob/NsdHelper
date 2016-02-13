package com.rafakob.nsdhelper;

import android.net.nsd.NsdServiceInfo;

class NsdListenerDiscovery implements android.net.nsd.NsdManager.DiscoveryListener {
    private static final String ERROR_SOURCE = "android.net.nsd.NsdHelper.DiscoveryListener";
    private final NsdHelper mNsdHelper;

    public NsdListenerDiscovery(NsdHelper nsdHelper) {
        this.mNsdHelper = nsdHelper;
    }

    @Override
    public void onStartDiscoveryFailed(String serviceType, int errorCode) {
        mNsdHelper.logError("Starting service discovery failed!", errorCode, ERROR_SOURCE);
        mNsdHelper.stopDiscovery();
    }

    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {
        mNsdHelper.logError("Stopping service discovery failed!", errorCode, ERROR_SOURCE);
        mNsdHelper.stopDiscovery();
    }

    @Override
    public void onDiscoveryStarted(String serviceType) {
        mNsdHelper.logMsg("Service discovery started.");
    }

    @Override
    public void onDiscoveryStopped(String serviceType) {
        mNsdHelper.logMsg("Service discovery stopped.");
    }

    @Override
    public void onServiceFound(NsdServiceInfo serviceInfo) {
        if (serviceInfo.getServiceType().equals(mNsdHelper.getDiscoveryServiceType())) {
            if (!serviceInfo.getServiceName().equals(mNsdHelper.getRegisteredServiceInfo().getServiceName())) {
                if (mNsdHelper.getDiscoveryServiceName() == null || serviceInfo.getServiceName().toLowerCase().equals(mNsdHelper.getDiscoveryServiceName().toLowerCase())) {
                    mNsdHelper.logMsg("Service found -> " + serviceInfo.getServiceName());
                    mNsdHelper.onNsdServiceFound(serviceInfo);
                }
            }
        }
    }

    @Override
    public void onServiceLost(NsdServiceInfo serviceInfo) {
        mNsdHelper.logMsg("Service lost -> " + serviceInfo.getServiceName());
        mNsdHelper.onNsdServiceLost(serviceInfo);
    }
}