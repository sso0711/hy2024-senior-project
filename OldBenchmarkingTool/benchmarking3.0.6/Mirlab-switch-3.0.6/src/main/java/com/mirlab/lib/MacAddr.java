package com.mirlab.lib;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月19日 下午11:58:52 类说明
 */
public class MacAddr {

	public static String generateMacAddr(int mac4, int mac5, int mac6) {

		String SEPARATOR_OF_MAC = ":";

		int mac1 = Integer.valueOf("aa", 16);
		int mac2 = Integer.valueOf("aa", 16);
		int mac3 = Integer.valueOf("aa", 16);

		String[] mac = { String.format("%02x", mac1), String.format("%02x", mac2), String.format("%02x", mac3),
				String.format("%02x", mac4), String.format("%02x", mac5), String.format("%02x", mac6) };

		return String.join(SEPARATOR_OF_MAC, mac);
	}
}
