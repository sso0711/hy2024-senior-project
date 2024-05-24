package com.lixisha.mirlab.mirlab_switch;

import com.mirlab.distributedPort.client.DistributedPortClient;
import com.mirlab.distributedPort.server.DistributedPortServer;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月19日 下午6:33:46 类说明
 */
public class testDistributedPort {

	public static void main(String[] args) throws Exception {
		DistributedPortServer server = new DistributedPortServer();
		DistributedPortClient client = new DistributedPortClient();

		byte[] a = { 1, 2 };

		server.start(null, 9933);

		Thread.sleep(2000);

		client.start("127.0.0.1", 9933);

		client.sendMessage(a);

	}

}
