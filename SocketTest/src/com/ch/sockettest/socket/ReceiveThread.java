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
			// 1.创建Socket对象，并指明服务器的IP地址（或主机名）和端口号
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(MainView.getProp().getProperty("ServerIP"), 8000), 5000);
			System.out.println(socket + "????????????????");
			// 2.调用Socket实例的getOutputStream()方法，向服务器发送内容
			OutputStream os = socket.getOutputStream();
			// PrintWriter writer = new PrintWriter(os);
			byte[] b = new byte[102400];
			while (!stop) {
				isConnect = true;
				/**
				 * 接收服务器端相应的数据(GET)
				 */
				long tt0 = System.currentTimeMillis();
				String content_upstream = "GET /anysize/\r\n";// 收从app发过来的100k数据
				os.write(content_upstream.getBytes());
				os.flush();
				// socket.shutdownOutput();

				// 1.调用Socket实例的getInputStream()方法，接收服务器发送来的信息
				InputStream is = socket.getInputStream();
				try {
					// 2.接收服务器端响应的数据信息
					int len = 0;
					// String str = new String(b, 0, 82);
					// System.out.println("我是客户端，接收到服务器发来的信息为：" + str);
					int bflen = 0;

					while (!stop && (len = is.read(b)) != -1) {
						byteReceive += len;
						wifiData.totalTraffic += len;
						bflen += len;
						System.out.println(byteReceive + "!!!!" + bflen + "!!!!!" + len);
						long t2 = System.currentTimeMillis();
						// System.out.println("GET一次耗时:" + (t2 - tt0));
						endTime = System.currentTimeMillis();
						if (t2 - t1 >= 1000) {
							// 统计当前速率
							wifiData.curRate = toByte(byteReceive / (t2 - t1));
							// 统计平均速率
							wifiData.averageRate = toByte(wifiData.totalTraffic / (endTime - startTime));
							wifiData.status = "进行中";

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
				System.out.println("循环一次耗时:" + (System.currentTimeMillis() - tt0));
			}
			// 4.关闭资源
			os.close();
			socket.close();
			// 统计平均速率
			wifiData.averageRate = toByte(wifiData.totalTraffic / (endTime - startTime));
			wifiData.status = "已结束";
			System.out.println("总共接收次数:" + rectimes);
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
	 * 判断是否连接，连接返回true,没有返回false
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
