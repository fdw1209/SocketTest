package com.ch.sockettest.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.ch.sockettest.dao.WifiData;
import com.ch.sockettest.shell.MainView;

public class ReceiveThread extends Thread {

	private long startTime;
	private long endTime;
	private long byteReceive;
	private long t1;
	public static boolean stop = false;
	public static boolean isConnect;
	public WifiData wifiData;

	public ReceiveThread(WifiData wifiData) {
		this.wifiData = wifiData;
	}

	@Override
	public void run() {
		try {
			startTime = System.currentTimeMillis();
			t1 = System.currentTimeMillis();
			byteReceive = 0;
			wifiData.totalTraffic = 0;
			int rectimes = 0;
			// 1.����Socket���󣬲�ָ����������IP��ַ�������������Ͷ˿ں�
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(MainView.getProp().getProperty("ServerIP"), 8000), 5000);
			System.out.println(socket + "????????????????");
			// 2.����Socketʵ����getOutputStream()���������������������
			OutputStream os = socket.getOutputStream();
			// PrintWriter writer = new PrintWriter(os);
			byte[] b = new byte[102400];
			while (!stop) {
				isConnect = true;
				/**
				 * ���շ���������Ӧ������(GET)
				 */
				long tt0 = System.currentTimeMillis();
				String content_upstream = "GET /anysize/\r\n";// �մ�app��������100k����
				os.write(content_upstream.getBytes());
				os.flush();
				// socket.shutdownOutput();

				// 1.����Socketʵ����getInputStream()���������շ���������������Ϣ
				InputStream is = socket.getInputStream();
				try {
					// 2.���շ���������Ӧ��������Ϣ
					int len = 0;
					// String str = new String(b, 0, 82);
					// System.out.println("���ǿͻ��ˣ����յ���������������ϢΪ��" + str);
					int bflen = 0;

					while (!stop && (len = is.read(b)) != -1) {
						byteReceive += len;
						wifiData.totalTraffic += len;
						bflen += len;
						System.out.println(byteReceive + "!!!!" + bflen + "!!!!!" + len);
						long t2 = System.currentTimeMillis();
						// System.out.println("GETһ�κ�ʱ:" + (t2 - tt0));
						endTime = System.currentTimeMillis();
						if (t2 - t1 >= 1000) {
							// ͳ�Ƶ�ǰ����
							wifiData.curRate = toByte(byteReceive / (t2 - t1));
							// ͳ��ƽ������
							wifiData.averageRate = toByte(wifiData.totalTraffic / (endTime - startTime));
							wifiData.status = "������";

							t1 = t2;
							byteReceive = 0;
						}
					}
					rectimes++;
					// socket.shutdownInput();
				} catch (IOException e) {
					e.printStackTrace();
				}
				is.close();
				System.out.println("ѭ��һ�κ�ʱ:" + (System.currentTimeMillis() - tt0));
			}
			// 4.�ر���Դ
			os.close();
			socket.close();
			// ͳ��ƽ������
			wifiData.averageRate = toByte(wifiData.totalTraffic / (endTime - startTime));
			wifiData.status = "�ѽ���";
			System.out.println("�ܹ����մ���:" + rectimes);
		} catch (Exception e) {
			stop = true;
			e.printStackTrace();
		} finally {
			isConnect = isServerConnect(stop);
		}
	}

	private double toByte(double data) {
		return new BigDecimal(data * 1000 / 1024 / 1024 * 8).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * �ж��Ƿ����ӣ����ӷ���true,û�з���false
	 * 
	 * @param socket
	 * @return
	 */
	public static Boolean isServerConnect(boolean stop) {
		if (stop) {
			return false;
		} else {
			return true;
		}
	}
}
