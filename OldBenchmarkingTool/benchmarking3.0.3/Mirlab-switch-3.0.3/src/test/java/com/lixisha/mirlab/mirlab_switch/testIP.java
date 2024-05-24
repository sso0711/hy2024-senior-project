package com.lixisha.mirlab.mirlab_switch;

import com.mirlab.lib.IP;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月15日 上午1:36:43 类说明
 */
public class testIP {
	public static void main(String[] args) {
		String a = "192.168.1.100";
		String b = "256.168.1.100";
		String c = "192.168.1.";

		System.out.println(IP.isIP(a));
		System.out.println(IP.isIP(b));
		System.out.println(IP.isIP(c));
	}
}
