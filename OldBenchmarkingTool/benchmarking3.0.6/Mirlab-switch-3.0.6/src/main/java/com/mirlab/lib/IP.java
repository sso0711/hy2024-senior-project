package com.mirlab.lib;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月15日 上午1:31:53 类说明
 */
public class IP {

	public static boolean isIP(String addr) {
		if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
			return false;
		}
		/**
		 * judge IP
		 */

		String rexp = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";

		Pattern pat = Pattern.compile(rexp);

		Matcher m = pat.matcher(addr);

		return m.matches();
	}

	public static boolean isHostConnectable(String host, int port, int timeout) {
		Socket connect = new Socket();

		boolean res = false;
		try {
			connect.connect(new InetSocketAddress(host, port), timeout);

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

	public static String getLocalHostIP() throws UnknownHostException {

		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress().toString();
//		String hostName = addr.getHostName().toString();

		return ip;
	}

	public static String generateIPAddr(int a, int b, int c, int d) {

		String ip = a + "." + b + "." + c + "." + d;
		if (isIP(ip)) {
			return ip;
		} else {
			return null;
		}

	}

}
