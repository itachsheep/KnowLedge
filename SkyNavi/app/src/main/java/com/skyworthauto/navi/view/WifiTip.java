package com.skyworthauto.navi.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.skyworthauto.navi.GlobalContext;
import com.skyworthauto.navi.R;
import com.skyworthauto.navi.util.L;

public class WifiTip extends ImageView {

    private static final String TAG = "wifiTip";
    private boolean mAttached;
    private boolean mWifiEnabled;
    private boolean mWifiConnected;

    private int mWifiRssi;
    private int mWifiLevel;

    private int[] WIFI_ICONS =
            {R.drawable.auto_ic_status_bar_wifi_0, R.drawable.auto_ic_status_bar_wifi_1,
                    R.drawable.auto_ic_status_bar_wifi_2, R.drawable.auto_ic_status_bar_wifi_3,
                    R.drawable.auto_ic_status_bar_wifi_4,};

    public WifiTip(Context context) {
        this(context, null);
    }

    public WifiTip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WifiTip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
            getContext().registerReceiver(mIntentReceiver, filter);
        }

        updateWifiTip();
    }

    private void updateWifiTip() {
        Context context = GlobalContext.getContext();

        WifiManager wifiManager = null;
        if(context != null){
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }

        if(wifiManager != null){
            mWifiEnabled = wifiManager.isWifiEnabled();
        }

        mWifiConnected = isWifiConnected();
        refreshView();
    }

    public boolean isWifiConnected() {
        NetworkInfo networkInfo = getActiveNetworkInfo();

        if (null == networkInfo) {
            L.d(TAG, "isWifiConnected: networkInfo is null");
            return false;
        }

        switch (networkInfo.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
                return networkInfo.isConnected();
            default:
                return false;
        }

    }

    private NetworkInfo getActiveNetworkInfo() {
        Context context = GlobalContext.getContext();
        ConnectivityManager connMgr = null;
        if(context != null){
            connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo info = null;
        if(connMgr != null){
            info = connMgr.getActiveNetworkInfo();
        }
        return info;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateWifiState(intent);
        }
    };

    protected void updateWifiState(Intent intent) {
        final String action = intent.getAction();
        L.d(TAG, "receive action=" + action);

        if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            mWifiEnabled =
                    intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                            == WifiManager.WIFI_STATE_ENABLED;

        } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            final NetworkInfo networkInfo =
                    intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            mWifiConnected = networkInfo != null && networkInfo.isConnected();
        } else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
            mWifiRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -200);
            mWifiLevel = WifiManager.calculateSignalLevel(mWifiRssi, WIFI_ICONS.length);
        }

        refreshView();
    }

    private void refreshView() {
        if (mWifiEnabled && mWifiConnected) {
            setImageResource(WIFI_ICONS[mWifiLevel]);
        } else {
            setImageResource(R.drawable.auto_ic_status_bar_wifi_no_signal);
        }
    }
}
