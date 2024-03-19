package com.lixisha.mirlab.mirlab_switch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月15日 下午11:45:52 类说明
 */
public class testPing {

	public static void main(String[] args) {
		String ip = "192.168.1.115";
		int timeout = 1000;// 1s
		boolean isReachable = false;

		try {
			isReachable = InetAddress.getByName(ip).isReachable(timeout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(isReachable);

		System.out.println(isHostConnectable("192.168.1.114", 6633));

	}

	public static boolean isHostConnectable(String host, int port) {
		Socket connect = new Socket();

		boolean res = false;
		try {
			connect.connect(new InetSocketAddress(host, port), 1000);

			res = connect.isConnected();

		} catch (IOException e) {
//			e.printStackTrace();
		} finally {
			try {
				connect.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;

	}

}
