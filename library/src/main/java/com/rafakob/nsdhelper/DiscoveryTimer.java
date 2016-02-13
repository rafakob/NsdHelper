package com.rafakob.nsdhelper;

import android.os.CountDownTimer;

class DiscoveryTimer {
    private final OnTimeoutListener mOnTimeoutListener;
    private CountDownTimer mTimer;

    public DiscoveryTimer(OnTimeoutListener onTimeoutListener, long seconds) {
        this.mOnTimeoutListener = onTimeoutListener;
        this.mTimer = createTimer(seconds);
    }

    public void start() {
        mTimer.start();
    }

    public void cancel() {
        mTimer.cancel();
    }

    public void reset(){
        mTimer.cancel();
        mTimer.start();
    }

    public void timeout(long seconds) {
        mTimer.cancel();
        mTimer = null;
        mTimer = createTimer(seconds);
    }

    private CountDownTimer createTimer(long seconds) {
        return new CountDownTimer(1000 * seconds, 1000 * seconds) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mOnTimeoutListener.onNsdDiscoveryTimeout();
            }
        };
    }

    interface OnTimeoutListener {
        void onNsdDiscoveryTimeout();
    }
}
