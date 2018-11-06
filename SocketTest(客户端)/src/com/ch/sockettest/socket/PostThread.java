package com.ch.sockettest.socket;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.ch.sockettest.dao.WifiData;
import com.ch.sockettest.shell.MainView;

/**
 * ��2���ͻ���ʵ�ֲ��裺 <1>����Socket���󣬲�ָ����������IP��ַ�������������Ͷ˿ں�
 * <2>����Socketʵ����getOutputStream()���������������������
 * <3>����Socketʵ����getInputStream()���������շ���������������Ϣ <4>�ر�������Դ
 * 
 * @author fdw
 *
 */
public class PostThread extends Thread {

	private long startTime;
	private long endTime;
	private long bytePost;
	private long t1;
	public static boolean stop = false;
	public static boolean isConnect;
	public WifiData dataBuffer;

	public PostThread(WifiData db) {
		this.dataBuffer = db;
	}

	public PostThread() {

	}

	@Override
	public void run() {
		try {
			startTime = System.currentTimeMillis();
			t1 = System.currentTimeMillis();
			bytePost = 0;
			dataBuffer.totalTraffic = 0;
			// 1.����Socket���󣬲�ָ����������IP��ַ�������������Ͷ˿ں�
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(MainView.getProp().getProperty("ServerIP"), 8000), 5000);
			System.out.println(socket + "////////////////");
			// 2.����Socketʵ����getOutputStream()���������������������
			OutputStream os = socket.getOutputStream();
			byte[] arrayOfByte = new byte[102400];

			while (!stop) {
				isConnect = true;
				/**
				 * ��������˷�������
				 */
				// 3.��������(POST)
				long tt0 = System.currentTimeMillis();
				String content_downstream = "POST 123456789\r\n";
				os.write(content_downstream.getBytes());
				os.flush();
				os.write(arrayOfByte);
				// ��ΪPrintWriter���ַ�����������д����ɺ�һ��Ҫ�����ַ�����flush()��������ˢ��������֤���������Ѿ�������д��ȥ��
				os.flush();

				bytePost += arrayOfByte.length;
				dataBuffer.totalTraffic += arrayOfByte.length;
				long t2 = System.currentTimeMillis();
				System.out.println("����һ�κ�ʱ��" + (t2 - tt0));
				endTime = System.currentTimeMillis();
				if (t2 - t1 >= 1000) {
					// ͳ�Ƶ�ǰ����
					long tt1 = System.currentTimeMillis();
					dataBuffer.curRate = toByte(bytePost / (t2 - t1));
					// ͳ��ƽ������
					dataBuffer.averageRate = toByte(dataBuffer.totalTraffic / (endTime - startTime));
					dataBuffer.status = "������";
					System.out.println("��ǰ����!!!" + dataBuffer.curRate);
					t1 = t2;
					bytePost = 0;
					System.out.println("ͳ��һ�κ�ʱ:" + (System.currentTimeMillis() - tt1));
				}
				System.out.println("ѭ��һ�κ�ʱ:" + (System.currentTimeMillis() - tt0));
			}
			os.close();
			socket.close();
			// ͳ��ƽ������
			dataBuffer.averageRate = toByte(dataBuffer.totalTraffic / (endTime - startTime));
			dataBuffer.status = "�ѽ���";
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
