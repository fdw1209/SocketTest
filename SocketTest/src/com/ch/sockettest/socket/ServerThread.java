package com.ch.sockettest.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ��1���������˵�ʵ�ֲ��裺 
 * <1>����ServerSocket��ָ���˿ں�
 * <2>����ServerSocket��accept()�������÷���Ϊ����������ֱ���пͻ������ӽ����Ż�ִ�и÷���֮��Ĵ��룩���ȴ��ͻ��˵����ӣ�
 * ���пͻ������Ӻ�ͻ᷵��һ��Socketʵ�� 
 * <3>����Scoketʵ����getInputStream()��������ȡ�ͻ��˷��͹�������Ϣ
 * <4>����Socketʵ����getOutputStream()��������ͻ���д����������͸��ÿͻ��˵���Ϣ 
 * <5>�ر�������Դ
 * 
 * @author fdw
 *
 */
public class ServerThread extends Thread {

	ServerSocket serversocket;
	PrintWriter printWriter;
	BufferedReader bufferedReader;
	private OutputStream output;
	private InputStream input;

	public ServerThread() {
	}

	@Override
	public void run() {

		try {
			// 1.����ServerSocket��ָ���˿ں�
			serversocket = new ServerSocket(8000);
			System.out.println("���������Ѿ��������ȴ��ͻ������ӡ�����");

			byte[] arrayOfByte = new byte[102400];

			while (true) {
				/**
				 * ��ȡ�ͻ��˷��͵�����
				 */
				// 2.����ServerSocket��accept()����,�ȴ��ͻ��˵�����
				Socket socket = serversocket.accept();
				// 4.����Scoketʵ����getInputStream()��������ȡ�ͻ��˷��͹�������Ϣ
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String str1 = bufferedReader.readLine();

				System.out.println("���Ƿ��������ͻ��˷�������Ϣ�ǣ�" + str1);

                String[] arrayOfString = str1.split(" ");
                String str2 = arrayOfString[0];
                String str3 = arrayOfString[1].substring(0, 9);
                String str4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E a").format(new Date(System.currentTimeMillis()));
                System.out.println(str4);

				/**
				 * ��������Ӧ�ͻ���
				 */
				input = socket.getInputStream();
				output = socket.getOutputStream();
				printWriter = new PrintWriter(output);
			
				if ("GET".equalsIgnoreCase(str2)) {
                    //Log.d("SocketReceiver", "prefix:4rrrrrrrrrrrrrrrrrrrrrrrrrrr " + str3);
                    if ("/anysize/".equalsIgnoreCase(str3)) {
                    	printWriter.write("HTTP/1.1 200 OK\r\n");
                    	printWriter.write("Connection: Close\r\n");
                    	printWriter.write("Content-Type: text/plain\r\n");
                    	printWriter.write("Content-Length: ");
                    	printWriter.write(arrayOfString[1].substring(9));
                    	printWriter.write("\r\n\r\n");
                    	printWriter.flush();
                        int j = 0;
                        while (j < Integer.MAX_VALUE) {
                            this.output.write(arrayOfByte);
                            j++;
                            continue;
                        }
                        printWriter.close();
                        output.close();
                        socket.close();
                        //Log.d("SocketReceiver", "------over1-------");
                        return;
                    }
                    printWriter.write("HTTP/1.1 200 OK\r\n");
                    printWriter.write("Connection: Close\r\n");
                    printWriter.write("Content-Type: text/plain\r\n");
                    printWriter.write("\r\n");
                    printWriter.write("This is a java process for wifi throutput test!\r\n");
                    printWriter.write("Author: fdw\r\n");
                    printWriter.write(str4);
                    printWriter.flush();
                    printWriter.close();
                    output.close();
                    socket.close();
                    //Log.d("SocketReceiver", "------over2-------");
                    return;
                }else {
                	//if ("POST".equalsIgnoreCase(str2)) {
                	int i = input.read(arrayOfByte);
					if (i != -1) {
						continue;
					}
				}
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Thread(new ServerThread()).start();
	}
}
