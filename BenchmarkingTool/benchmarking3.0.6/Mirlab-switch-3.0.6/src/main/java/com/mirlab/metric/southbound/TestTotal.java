package com.mirlab.metric.southbound;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年8月30日 下午11:01:02 类说明
 */
public class TestTotal {
	// sleeptime min
	public static int sleepTime = 5;

	public static void go() {
		version0();
	}

	private static void version0() {
		// TODO Auto-generated method stub

		try {
			for (int i = 0; i < 10; i++) {
				TopologyDiscovery.go();
				Thread.sleep(1000 * 60 * sleepTime);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
