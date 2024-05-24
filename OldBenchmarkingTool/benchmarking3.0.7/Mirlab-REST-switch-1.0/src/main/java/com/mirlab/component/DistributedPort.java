package com.mirlab.component;

import com.mirlab.distributedPort.client.DistributedPortClient;
import com.mirlab.distributedPort.server.DistributedPortServer;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年10月17日 上午9:23:44 类说明
 */
public class DistributedPort {

	private Port connectedPort = null;

	private String remote_server_IP;
	private int remote_server_Port;
	private int local_server_Port;

	private DistributedPortServer server;
	private DistributedPortClient client;

	public DistributedPort(String remote_server_IP, int remote_server_Port, int local_server_Port) {
		// TODO Auto-generated constructor stub

		this.remote_server_IP = remote_server_IP;
		this.remote_server_Port = remote_server_Port;
		this.local_server_Port = local_server_Port;

		this.startServer();

	}

	public void startClient() {
		if (client == null) {
			client = new DistributedPortClient();
			try {
				client.start(remote_server_IP, remote_server_Port);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void closeClient() {
		if (client != null) {
			client.stop();
		}
	}

	public void sendByteMessage(byte[] data) {
		if (client != null) {
			client.sendMessage(data);
		}

	}

	public void startServer() {
		if (server == null) {
			server = new DistributedPortServer();
			try {
				server.start(this, local_server_Port);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void closeServer() {
		if (server != null) {
			server.stop();
		}
	}

	/**
	 * @return the connectedPort
	 */
	public Port getConnectedPort() {
		return connectedPort;
	}

	/**
	 * @param connectedPort the connectedPort to set
	 */
	public void setConnectedPort(Port connectedPort) {
		this.connectedPort = connectedPort;
	}

}
