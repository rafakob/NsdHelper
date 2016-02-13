package com.rafakob.nsdhelper;

public interface NsdListener {
    /**
     * Called when service has been successfully registered
     *
     * @param registeredService Registered service.
     */
    void onNsdRegistered(NsdService registeredService);

    /**
     * Called when discovery has been stopped or finished due to timeout.
     */
    void onNsdDiscoveryFinished();

    /**
     * Called when service has been discovered.
     *
     * @param foundService Discovered service (not resolved yet).
     */
    void onNsdServiceFound(NsdService foundService);

    /**
     * Called when service has been resolved.
     *
     * @param resolvedService Resolved service.
     */
    void onNsdServiceResolved(NsdService resolvedService);

    /**
     * Called when connection with service has been lost.
     *
     * @param lostService Lost service.
     */
    void onNsdServiceLost(NsdService lostService);

    /**
     * Called when error occurred during NSD process (registration, discover or resolve).
     *
     * @param errorMessage Readable error message.
     * @param errorCode    Error code.
     * @param errorSource  Class/listener which caused the error.
     */
    void onNsdError(String errorMessage, int errorCode, String errorSource);
}
