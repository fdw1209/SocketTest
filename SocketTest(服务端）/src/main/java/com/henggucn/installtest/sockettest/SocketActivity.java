package com.henggucn.installtest.sockettest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SocketActivity extends Activity {

    private static final String TAG = "r";
    private ThreadHandler mThreadHandler = null;
    private TextView tvIp;
    private Button button;
    private RadioButton rb, rb1;
    private String type = "2.4G";
    private WifiConnect wifiConnect;
    private WifiManager mWifiManager;

    private void getWifiInfo(String type) {
        this.wifiConnect = new WifiConnect((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        if (this.wifiConnect.OpenWifi()) {
            boolean bool = this.wifiConnect.connect("scty" + type, "");
            Log.i(TAG, "wifiConnect if Wifi" + bool);
            if (bool) {
                if (this.wifiConnect.getIPAddress().equals("0.0.0.0")) {
                    SystemClock.sleep(3000L);
                    getWifiInfo(type);
                }
                for (; ; ) {
                    Log.i(TAG, "wifiConnect if Wifi" + this.wifiConnect.getIPAddress());
                    Toast.makeText(getApplicationContext(), "scty" + type + "已连接", Toast.LENGTH_SHORT).show();
                    this.tvIp.setText("Ip地址:" + this.wifiConnect.getIPAddress());
                    if (this.mThreadHandler == null) {
                        this.mThreadHandler = new ThreadHandler();
                        this.mThreadHandler.start();
                    }
                    return;
                }
            }
            this.tvIp.setText("scty WIFI未连接,请检查");
            return;
        } else {
            this.tvIp.setText("WIFI连接未打开");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_layout);
        tvIp = findViewById(R.id.textIP);
        button = findViewById(R.id.button);
        rb = findViewById(R.id.RadioButton);
        rb1 = findViewById(R.id.RadioButton1);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "2.4G";
                }
            }
        });
        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "5G";
                }
            }
        });
        mWifiManager.setWifiEnabled(true);
        button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    button.setBackgroundColor(Color.parseColor("#FFFF4081"));
                }else{
                    button.setBackgroundColor(Color.parseColor("#FFAAAAAA"));
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (mWifiManager.isWifiEnabled()) {
                    loadingDialog();
                    SocketActivity.this.getWifiInfo(type);
                }
            }
        });
    }

    /**
     * 加载等待弹窗
     */
    private void loadingDialog() {
        LoadingDialog.Builder builder1 = new LoadingDialog.Builder(SocketActivity.this)
                .setMessage("连接中...")
                .setCancelable(false);
        final LoadingDialog dialog1 = builder1.create();
        dialog1.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog1.dismiss();
            }
        }, 2000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        if (this.mThreadHandler != null) {
            this.mThreadHandler.stopThread();
            this.mThreadHandler = null;
        }
    }

    protected void onStop() {
        super.onStop();
    }

    public class ThreadHandler extends Thread {
        private ServerSocket mServerSocket = null;

        public ThreadHandler() {
            setName("ThreadHandler");
        }

        public void SocketServer() {
            try {
                this.mServerSocket = new ServerSocket(8000);
                Log.i(TAG, "--------------->ServerSocket");
                Socket localObject = null;
                for (; ; ) {
                    if (this.mServerSocket == null) {
                        return;
                    }
                    Log.i(TAG, "--------------->connected 1");
                    try {
                        Socket localSocket = this.mServerSocket.accept();
                        localObject = localSocket;
                    } catch (IOException localIOException3) {
                        for (; ; ) {
                            Log.i(TAG, "--------------->Socket IOException");
                            localIOException3.printStackTrace();
                        }
                    }
                    Log.i(TAG, "--------------->connected 2");
                    System.out.println(localObject.getInetAddress().getHostAddress() + "连接进入");
                    Log.i(TAG, "--------------->connected 3");
                    try {
                        new SocketThread(localObject).start();
                    } catch (IOException localIOException1) {
                        Log.i(TAG, "--------------->SocketThread IOException");
                        localIOException1.printStackTrace();
                    }
                }
            } catch (IOException localIOException2) {
                for (; ; ) {
                    Log.i(TAG, "--------------->ServerSocket IOException");
                    this.mServerSocket = null;
                    localIOException2.printStackTrace();
                }
            }
        }

        public void run() {
            SocketServer();
        }

        public void stopThread() {
            Log.i(TAG, "--------------->ServerSocket stop");
            try {
                if (this.mServerSocket != null) {
                    this.mServerSocket.close();
                }
                this.mServerSocket = null;
                return;
            } catch (IOException localIOException) {
                Log.i(TAG, "--------------->ServerSocket close IOException");
                localIOException.printStackTrace();
            }
        }
    }

    //判断SSID是否存在
    public boolean isWifiExist(String SSID) {
        boolean isExist;
        List<ScanResult> mWifiList = mWifiManager.getScanResults();
        if (mWifiList == null) {
            return false;
        }
        for (ScanResult existingWifi : mWifiList) {
            if (existingWifi.SSID.equals(SSID)) {
                isExist = true;
                return isExist;
            }
        }
        return false;
    }
}
