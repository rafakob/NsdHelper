package com.rafakob.nsdhelper;

import android.net.nsd.NsdServiceInfo;

class NsdListenerResolve implements android.net.nsd.NsdManager.ResolveListener {
    private static final String ERROR_SOURCE = "android.net.nsd.NsdManager.ResolveListener";
    private final NsdHelper mNsdHelper;

    public NsdListenerResolve(NsdHelper nsdHelper) {
        this.mNsdHelper = nsdHelper;
    }

    @Override
    public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
        mNsdHelper.logError(String.format("Failed to resolve service -> %s",
                serviceInfo.getServiceName()), errorCode, ERROR_SOURCE);
    }

    @Override
    public void onServiceResolved(NsdServiceInfo serviceInfo) {
        mNsdHelper.logMsg(String.format("Service resolved -> %s, %s/%s, port %d, %s",
                serviceInfo.getServiceName(), serviceInfo.getHost().getHostName(),
                serviceInfo.getHost().getHostAddress(), serviceInfo.getPort(), serviceInfo.getServiceType()));
        mNsdHelper.onNsdServiceResolved(serviceInfo);
    }
}