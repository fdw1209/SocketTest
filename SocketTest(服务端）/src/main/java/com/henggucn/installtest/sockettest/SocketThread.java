package com.henggucn.installtest.sockettest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fdw on 2018/3/28.
 */
class SocketThread extends Thread {

    private static final String TAG = "SocketReceiver";
    private BufferedReader bufferedReader;
    private Socket curSocket;
    private InputStream input;
    private OutputStream output;
    private PrintWriter printWriter;

    public SocketThread(Socket paramSocket)
            throws IOException {
        this.bufferedReader = new BufferedReader(new InputStreamReader(paramSocket.getInputStream()));
        this.printWriter = new PrintWriter(paramSocket.getOutputStream());
        this.input = paramSocket.getInputStream();
        this.output = paramSocket.getOutputStream();
        this.curSocket = paramSocket;
    }

    public void run() {
        byte[] arrayOfByte = new byte[102400];
        for (; ; ) {
            try {
                String str1 = this.bufferedReader.readLine();
                //System.out.println(str1);
                String[] arrayOfString = str1.split(" ");
                String str2 = arrayOfString[0];
                String str3 = arrayOfString[1].substring(0, 9);
                String str4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E a").format(new Date(System.currentTimeMillis()));
                System.out.println(str4);
                //Log.d("SocketReceiver", "method: " + str2);
                if ("GET".equalsIgnoreCase(str2)) {
                    Log.d("SocketReceiver", "prefix:4rrrrrrrrrrrrrrrrrrrrrrrrrrr " + str3);
                    if ("/anysize/".equalsIgnoreCase(str3)) {
                        this.printWriter.write("HTTP/1.1 200 OK\r\n");
                        this.printWriter.write("Connection: Close\r\n");
                        this.printWriter.write("Content-Type: text/plain\r\n");
                        this.printWriter.write("Content-Length: ");
                        this.printWriter.write(arrayOfString[1].substring(9));
                        this.printWriter.write("\r\n\r\n");
                        this.printWriter.flush();
                        int j = 0;
                        while (j < Integer.MAX_VALUE) {
                            this.output.write(arrayOfByte);
                            j++;
                            continue;
                        }
                        this.printWriter.close();
                        this.output.close();
                        this.curSocket.close();
                        Log.d("SocketReceiver", "------over1-------");
                        return;
                    }
                    this.printWriter.write("HTTP/1.1 200 OK\r\n");
                    this.printWriter.write("Connection: Close\r\n");
                    this.printWriter.write("Content-Type: text/plain\r\n");
                    this.printWriter.write("\r\n");
                    this.printWriter.write("This is a java process for wifi throutput test!\r\n");
                    this.printWriter.write("Author: fdw\r\n");
                    this.printWriter.write(str4);
                    this.printWriter.flush();
                    this.printWriter.close();
                    this.output.close();
                    this.curSocket.close();
                    Log.d("SocketReceiver", "------over2-------");
                    return;
                }else{
//                if ("POST".equalsIgnoreCase(str2)) {
                    int i = this.input.read(arrayOfByte);
                    if (i != -1) {
                        continue;
                    }
                }
                return;
            } catch (IOException localIOException) {
            }
        }
    }
}

