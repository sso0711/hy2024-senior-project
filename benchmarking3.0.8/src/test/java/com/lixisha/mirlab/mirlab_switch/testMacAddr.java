package com.lixisha.mirlab.mirlab_switch;

import java.util.Random;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月19日 下午11:10:45 类说明
 */
public class testMacAddr {

	public static void main(String[] args) {

//		System.out.println(Integer.toHexString(15));
//		System.out.println(Integer.toHexString(20));
//		System.out.println(Integer.toHexString(30));
//
//		System.out.println(randomMac4Qemu());

		int y = Integer.valueOf("aa", 16);
		System.out.println(y);
	}

	private static String SEPARATOR_OF_MAC = ":";

	public static String randomMac4Qemu() {
		Random random = new Random();

		int y = Integer.valueOf("aa", 16);

		String[] mac = { String.format("%02x", y), String.format("%02x", 20), String.format("%02x", 30),
				String.format("%02x", random.nextInt(0xff)), String.format("%02x", random.nextInt(0xff)),
				String.format("%02x", random.nextInt(0xff)) };

		return String.join(SEPARATOR_OF_MAC, mac);
	}

}
