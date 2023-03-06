package com.truongnqt.popcorn.broadcastreceivers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.truongnqt.popcorn.utils.NetworkConnection;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    private final ConnectivityReceiverListener mConnectivityReceiverListener;

    public ConnectivityBroadcastReceiver(ConnectivityReceiverListener mConnectivityReceiverListener) {
        this.mConnectivityReceiverListener = mConnectivityReceiverListener;
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (mConnectivityReceiverListener != null && NetworkConnection.isConnected(context)) {
            mConnectivityReceiverListener.onNetworkConnectionConnected();
        } else Toast.makeText(context, "No network connection", Toast.LENGTH_SHORT).show();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionConnected();
    }

}
