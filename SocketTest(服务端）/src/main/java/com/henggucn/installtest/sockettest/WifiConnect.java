package com.henggucn.installtest.sockettest;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.Iterator;

/**
 * Created by fdw on 2018/3/28.
 */

public class WifiConnect {
    private static final String TAG = "SocketTest";
    private WifiInfo mWifiInfo;
    WifiManager wifiManager;

    public WifiConnect(WifiManager paramWifiManager) {
        this.wifiManager = paramWifiManager;
        this.mWifiInfo = paramWifiManager.getConnectionInfo();
    }

    private WifiConfiguration CreateWifiInfo(String paramString1, String paramString2) {
        WifiConfiguration localWifiConfiguration = new WifiConfiguration();
        localWifiConfiguration.SSID = ("\"" + paramString1 + "\"");
        Log.i("SocketTest", "SSID: " + localWifiConfiguration.SSID);
        localWifiConfiguration.hiddenSSID = true;
        localWifiConfiguration.allowedAuthAlgorithms.clear();
        localWifiConfiguration.allowedGroupCiphers.clear();
        localWifiConfiguration.allowedKeyManagement.clear();
        localWifiConfiguration.allowedPairwiseCiphers.clear();
        localWifiConfiguration.allowedProtocols.clear();
        //localWifiConfiguration.allowedKeyManagement.set(0);
        localWifiConfiguration.wepKeys[0] = "\"" + "\"";
        localWifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        localWifiConfiguration.wepTxKeyIndex = 0;

        int i = this.wifiManager.addNetwork(localWifiConfiguration);
        Log.i("SocketTest", "add Network returned " + i);
        boolean bool = this.wifiManager.enableNetwork(i, true);
        Log.i("SocketTest", "enableNetwork returned " + bool);
        return localWifiConfiguration;
    }

    private WifiConfiguration IsExsits(String paramString) {
        Iterator localIterator = this.wifiManager.getConfiguredNetworks().iterator();
        while (localIterator.hasNext()) {
            WifiConfiguration localWifiConfiguration = (WifiConfiguration) localIterator.next();
            if (localWifiConfiguration.SSID.equals("\"" + paramString + "\"")) {
                return localWifiConfiguration;
            }
        }
        return null;
    }

    private String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (paramInt >> 8 & 0xFF) + "." + (paramInt >> 16 & 0xFF) + "." + (paramInt >> 24 & 0xFF);
    }

    public boolean OpenWifi() {
        boolean bool = true;
        if (!this.wifiManager.isWifiEnabled()) {
            bool = this.wifiManager.setWifiEnabled(true);
        }
        return bool;
    }

    public boolean connect(String paramString1, String paramString2) {
        WifiConfiguration localWifiConfiguration1;
        if (!OpenWifi()) {
            return false;
        }
        do {
            Log.i("SocketTest", "getWifiState " + this.wifiManager.getWifiState());
            while (this.wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {//wifi正在启动
                try {
                    Thread.currentThread();
                    Thread.sleep(100L);
                } catch (InterruptedException localInterruptedException) {
                }
            }
            localWifiConfiguration1 = CreateWifiInfo(paramString1, paramString2);
        } while (localWifiConfiguration1 == null);
        WifiConfiguration localWifiConfiguration2 = IsExsits(paramString1);
        if (localWifiConfiguration2 != null) {
            this.wifiManager.removeNetwork(localWifiConfiguration2.networkId);
        }
        int i = this.wifiManager.addNetwork(localWifiConfiguration1);
        return this.wifiManager.enableNetwork(i, false);
    }

    public String getIPAddress() {
        if (this.mWifiInfo == null) {
            return "0";
        }
        return intToIp(this.mWifiInfo.getIpAddress());
    }
}
