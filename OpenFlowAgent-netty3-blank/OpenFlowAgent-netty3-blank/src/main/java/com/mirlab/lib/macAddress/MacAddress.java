package com.mirlab.lib.macAddress;

import java.util.Random;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年12月29日 下午5:23:30 类说明
 */
public class MacAddress {
	private static String SEPARATOR_OF_MAC = ":";

	/**
	 * Generate a random MAC address for qemu/kvm 52-54-00 used by qemu/kvm The
	 * remaining 3 fields are random, range from 0 to 255
	 *
	 * @return MAC address string
	 */
	public static String getRandomMac() {
		Random random = new Random();
		String[] mac = { String.format("%02x", 0x00), 
				String.format("%02x", 0x00), 
				String.format("%02x", 0x00),
				String.format("%02x", random.nextInt(0xff)), 
				String.format("%02x", random.nextInt(0xff)),
				String.format("%02x", random.nextInt(0xff)) };

		return String.join(SEPARATOR_OF_MAC, mac);
	}
}
