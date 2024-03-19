package com.lixisha.mirlab.mirlab_switch;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年10月17日 下午1:47:08 类说明
 */
public class justTester {
	public static void main(String[] args) throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		System.out.println(addr.getHostName());
		System.out.println(addr.getHostAddress());

	}
}
