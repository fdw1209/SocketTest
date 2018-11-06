package com.ch.sockettest.socket;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.ch.sockettest.dao.WifiData;
import com.ch.sockettest.shell.MainView;

/**
 * （2）客户端实现步骤： <1>创建Socket对象，并指明服务器的IP地址（或主机名）和端口号
 * <2>调用Socket实例的getOutputStream()方法，向服务器发送内容
 * <3>调用Socket实例的getInputStream()方法，接收服务器发送来的信息 <4>关闭所有资源
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
			// 1.创建Socket对象，并指明服务器的IP地址（或主机名）和端口号
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(MainView.getProp().getProperty("ServerIP"), 8000), 5000);
			System.out.println(socket + "////////////////");
			// 2.调用Socket实例的getOutputStream()方法，向服务器发送内容
			OutputStream os = socket.getOutputStream();
			byte[] arrayOfByte = new byte[102400];

			while (!stop) {
				isConnect = true;
				/**
				 * 向服务器端发送数据
				 */
				// 3.发送数据(POST)
				long tt0 = System.currentTimeMillis();
				String content_downstream = "POST 123456789\r\n";
				os.write(content_downstream.getBytes());
				os.flush();
				os.write(arrayOfByte);
				// 因为PrintWriter是字符流，所以在写入完成后一定要调用字符流的flush()方法，冲刷缓冲区保证所有内容已经从流里写出去了
				os.flush();

				bytePost += arrayOfByte.length;
				dataBuffer.totalTraffic += arrayOfByte.length;
				long t2 = System.currentTimeMillis();
				System.out.println("发送一次耗时：" + (t2 - tt0));
				endTime = System.currentTimeMillis();
				if (t2 - t1 >= 1000) {
					// 统计当前速率
					long tt1 = System.currentTimeMillis();
					dataBuffer.curRate = toByte(bytePost / (t2 - t1));
					// 统计平均速率
					dataBuffer.averageRate = toByte(dataBuffer.totalTraffic / (endTime - startTime));
					dataBuffer.status = "进行中";
					System.out.println("当前速率!!!" + dataBuffer.curRate);
					t1 = t2;
					bytePost = 0;
					System.out.println("统计一次耗时:" + (System.currentTimeMillis() - tt1));
				}
				System.out.println("循环一次耗时:" + (System.currentTimeMillis() - tt0));
			}
			os.close();
			socket.close();
			// 统计平均速率
			dataBuffer.averageRate = toByte(dataBuffer.totalTraffic / (endTime - startTime));
			dataBuffer.status = "已结束";
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
