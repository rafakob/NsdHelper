package com.rafakob.nsdhelper;

import android.net.nsd.NsdServiceInfo;

import java.util.LinkedList;

class ResolveQueue {
    private final NsdHelper mNsdHelper;
    private final LinkedList<NsdServiceInfo> mTasks = new LinkedList<>();
    private boolean mIsRunning = false;

    public ResolveQueue(NsdHelper nsdHelper) {
        this.mNsdHelper = nsdHelper;
    }

    public void enqueue(NsdServiceInfo serviceInfo) {
        mTasks.add(serviceInfo);
        if (!mIsRunning) {
            mIsRunning = true;
            run();
        }
    }

    public void next() {
        mIsRunning = true;
        run();
    }

    private void run() {
        NsdServiceInfo nsdServiceInfo = mTasks.pollFirst();
        if (nsdServiceInfo != null)
            mNsdHelper.resolveService(nsdServiceInfo);
        else
            mIsRunning = false;
    }
}
