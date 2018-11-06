package com.ch.sockettest.socket;

import java.util.concurrent.TimeUnit;

import com.ch.sockettest.shell.MainView;

/**
 * 基于TCP的Socket通信
 * 
 * @author fdw
 *
 */
public class Serversocket {

	public static boolean createServerSocket() {

		// new Thread(new ServerThread()).start();// 服务器端线程

		int count = 0;
		while (count < 6) {
			MainView.wifiDatas.get(count).id = count + 1;
			if (count > 2) {
				new Thread(new ReceiveThread(MainView.wifiDatas.get(count))).start();// 下行客户端线程
			} else {
				new Thread(new PostThread(MainView.wifiDatas.get(count))).start();// 上行客户端线程
			}
			count++;
		}
		try {
			TimeUnit.MILLISECONDS.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ReceiveThread.isConnect && PostThread.isConnect) {
			return true;
		}else {
			return false;
		}
	}
}
