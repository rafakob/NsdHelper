package com.rafakob.nsdhelper;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * NsdHelper is a helper class for {@link NsdManager}.
 */
public class NsdHelper implements DiscoveryTimer.OnTimeoutListener {
    private static final String TAG = "NsdHelper";
    private final NsdManager mNsdManager;
    private NsdListener mNsdListener;

    // Registration
    private boolean mRegistered = false;
    private NsdListenerRegistration mRegistrationListener;
    private NsdService mRegisteredService;
    private NsdServiceInfo mRegisteredServiceInfo = new NsdServiceInfo();


    // Discovery
    private boolean mDiscoveryStarted = false;
    private long mDiscoveryTimeout = 15;
    private String mDiscoveryServiceType;
    private String mDiscoveryServiceName;
    private NsdListenerDiscovery mDiscoveryListener;
    private DiscoveryTimer mDiscoveryTimer;

    // Resolve
    private boolean mAutoResolveEnabled = true;
    private ResolveQueue mResolveQueue;

    // Common
    private boolean mLogEnabled = false;

    /**
     * @param nsdManager Android {@link NsdManager}.
     */
    public NsdHelper(NsdManager nsdManager) {
        this.mNsdManager = nsdManager;
        this.mDiscoveryTimer = new DiscoveryTimer(this, mDiscoveryTimeout);
        this.mResolveQueue = new ResolveQueue(this);
    }

    /**
     * @param nsdManager  Android {@link NsdManager}.
     * @param nsdListener Service discovery listener.
     */
    public NsdHelper(NsdManager nsdManager, NsdListener nsdListener) {
        this.mNsdManager = nsdManager;
        this.mNsdListener = nsdListener;
        this.mDiscoveryTimer = new DiscoveryTimer(this, mDiscoveryTimeout);
        this.mResolveQueue = new ResolveQueue(this);
    }

    /**
     * @param context Context is only needed to create {@link NsdManager} instance.
     */
    public NsdHelper(Context context) {
        this.mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        this.mDiscoveryTimer = new DiscoveryTimer(this, mDiscoveryTimeout);
        this.mResolveQueue = new ResolveQueue(this);
    }

    /**
     * @param context     Context is only needed to create {@link NsdManager} instance.
     * @param nsdListener Service discovery listener.
     */
    public NsdHelper(Context context, NsdListener nsdListener) {
        this.mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        this.mNsdListener = nsdListener;
        this.mDiscoveryTimer = new DiscoveryTimer(this, mDiscoveryTimeout);
        this.mResolveQueue = new ResolveQueue(this);
    }

    public NsdListener getNsdListener() {
        return mNsdListener;
    }

    public void setNsdListener(NsdListener nsdListener) {
        mNsdListener = nsdListener;
    }

    /**
     * @return Is logcat enabled.
     */
    public boolean isLogEnabled() {
        return mLogEnabled;
    }

    /**
     * Enable logcat messages.
     * By default their disabled.
     *
     * @param isLogEnabled If true logcat is enabled.
     */
    public void setLogEnabled(boolean isLogEnabled) {
        this.mLogEnabled = isLogEnabled;
    }

    /**
     * @return Is auto resolving discovered services enabled.
     */
    public boolean isAutoResolveEnabled() {
        return mAutoResolveEnabled;
    }

    /**
     * Enable auto resolving discovered services.
     * By default it's enabled.
     *
     * @param isAutoResolveEnabled If true discovered service will be automatically resolved.
     */
    public void setAutoResolveEnabled(boolean isAutoResolveEnabled) {
        this.mAutoResolveEnabled = isAutoResolveEnabled;
    }

    /**
     * @return onNsdDiscoveryTimeout timeout in seconds.
     */
    public long getDiscoveryTimeout() {
        return mDiscoveryTimeout;
    }

    /**
     * If no new service has been discovered for a timeout interval, discovering will be stopped
     * and {@link com.rafakob.nsdhelper.NsdListener#onNsdDiscoveryFinished()} will be called.
     * Default timeout is set to 15 seconds.
     * Set 0 to infinite.
     */
    public void setDiscoveryTimeout(int seconds) {
        if (seconds < 0)
            throw new IllegalArgumentException("Timeout has to be greater or equal 0!");

        if (seconds == 0)
            mDiscoveryTimeout = Integer.MAX_VALUE;
        else
            mDiscoveryTimeout = seconds;

        mDiscoveryTimer.timeout(mDiscoveryTimeout);
    }

    /**
     * @return True if discovery is running.
     */
    public boolean isDiscoveryRunning() {
        return mDiscoveryStarted;
    }

    /**
     * @return Discovery service type to discover.
     */
    public String getDiscoveryServiceType() {
        return mDiscoveryServiceType;
    }

    /**
     * @return Discovery service name to discover.
     */
    public String getDiscoveryServiceName() {
        return mDiscoveryServiceName;
    }

    public NsdService getRegisteredService() {
        return mRegisteredService;
    }

    /**
     * Register new service. Port will be obtain automatically.
     *
     * @param desiredServiceName Desired service name. If the name already exists in network it will be change to something like 'AppChat (1)'.
     * @param serviceType        Service type.
     */
    public void registerService(String desiredServiceName, String serviceType) {
        registerService(desiredServiceName, serviceType, findAvaiablePort());
    }

    /**
     * Register new service with given serivce name, type and port.
     *
     * @param desiredServiceName Desired service name. If the name already exists in network it will be change to something like 'AppChat (1)'.
     * @param serviceType        Service type.
     * @param port               Service port.
     */
    public void registerService(String desiredServiceName, String serviceType, int port) {
        if (port == 0) return;

        mRegisteredServiceInfo = new NsdServiceInfo();
        mRegisteredServiceInfo.setServiceName(desiredServiceName);
        mRegisteredServiceInfo.setServiceType(serviceType);
        mRegisteredServiceInfo.setPort(port);

        mRegistrationListener = new NsdListenerRegistration(this);
        mNsdManager.registerService(mRegisteredServiceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    public void unregisterService() {
        if (mRegistered) {
            mRegistered = false;
            mNsdManager.unregisterService(mRegistrationListener);
        }
    }

    /**
     * Start discovering services.
     *
     * @param serviceType Service type, eg. "_http._tcp.". Typical names can be obtain from {@link com.rafakob.nsdhelper.NsdType}.
     */
    public void startDiscovery(String serviceType) {
        startDiscovery(serviceType, null);
    }

    /**
     * Start discovering services.
     *
     * @param serviceType Service type, eg. "_http._tcp.". Typical names can be obtain from {@link com.rafakob.nsdhelper.NsdType}
     * @param serviceName When service is discovered it will check if it's name contains this text. It is NOT case sensitive.
     */
    public void startDiscovery(String serviceType, String serviceName) {
        if (!mDiscoveryStarted) {
            mDiscoveryStarted = true;
            mDiscoveryTimer.start();
            mDiscoveryServiceType = serviceType;
            mDiscoveryServiceName = serviceName;
            mDiscoveryListener = new NsdListenerDiscovery(this);
            mNsdManager.discoverServices(mDiscoveryServiceType, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        }
    }

    /**
     * Stop discovering services.
     */
    public void stopDiscovery() {
        if (mDiscoveryStarted) {
            mDiscoveryStarted = false;
            mDiscoveryTimer.cancel();
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            if (mNsdListener != null)
                mNsdListener.onNsdDiscoveryFinished();
        }
    }

    /**
     * Resolve service host and port.
     *
     * @param nsdService Service to be resolved.
     */
    public void resolveService(NsdService nsdService) {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(nsdService.getName());
        serviceInfo.setServiceType(nsdService.getType());
        mResolveQueue.enqueue(serviceInfo);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internals
    ///////////////////////////////////////////////////////////////////////////

    void resolveService(NsdServiceInfo serviceInfo) {
        mNsdManager.resolveService(serviceInfo, new NsdListenerResolve(this));
    }

    int findAvaiablePort() {
        ServerSocket socket;
        try {
            socket = new ServerSocket(0);
            return socket.getLocalPort();
        } catch (IOException e) {
            logError("Couldn't assign port to your service.", 0, "java.net.ServerSocket");
            e.printStackTrace();
            return 0;
        }
    }

    void onRegistered(String serviceName) {
        mRegistered = true;
        mRegisteredServiceInfo.setServiceName(serviceName);
        mRegisteredService = new NsdService(mRegisteredServiceInfo);
        if (mNsdListener != null)
            mNsdListener.onNsdRegistered(mRegisteredService);
    }

    void onNsdServiceFound(NsdServiceInfo foundService) {
        mDiscoveryTimer.reset();
        if (mNsdListener != null)
            mNsdListener.onNsdServiceFound(new NsdService(foundService));
        if (mAutoResolveEnabled)
            mResolveQueue.enqueue(foundService);
    }

    void onNsdServiceResolved(NsdServiceInfo resolvedService) {
        mResolveQueue.next();
        mDiscoveryTimer.reset();
        if (mNsdListener != null)
            mNsdListener.onNsdServiceResolved(new NsdService(resolvedService));
    }

    void onNsdServiceLost(NsdServiceInfo lostService) {
        if (mNsdListener != null)
            mNsdListener.onNsdServiceLost(new NsdService(lostService));
    }

    void logMsg(String msg) {
        if (mLogEnabled)
            Log.d(TAG, msg);
    }

    void logError(String errorMessage, int errorCode, String errorSource) {
        Log.e(TAG, errorMessage);
        if (mNsdListener != null)
            mNsdListener.onNsdError(errorMessage, errorCode, errorSource);
    }

    NsdServiceInfo getRegisteredServiceInfo() {
        return mRegisteredServiceInfo;
    }

    @Override
    public void onNsdDiscoveryTimeout() {
        stopDiscovery();
    }


}
