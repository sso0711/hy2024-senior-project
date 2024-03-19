package com.mirlab.component;

import com.mirlab.distributedPort.client.DistributedPortClient;
import com.mirlab.distributedPort.server.DistributedPortServer;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年10月17日 上午9:23:44 类说明
 */
public class DistributedPort {

	private Port connectedPort = null;

	private String distributedServer_IP;
	private int distributedServer_Port;
	private int server_Port;

	private DistributedPortServer server;
	private DistributedPortClient client;

	public DistributedPort(Port connectedPort, String distributedServer_IP, int distributedServer_Port,
			int server_Port) {
		// TODO Auto-generated constructor stub
		this.connectedPort = connectedPort;

		this.distributedServer_IP = distributedServer_IP;
		this.distributedServer_Port = distributedServer_Port;
		this.server_Port = server_Port;

		this.startServer();
		;
	}

	public void startClient() {
		if (client == null) {
			client = new DistributedPortClient();
			client.start(distributedServer_IP, distributedServer_Port);
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
			server.start(this, server_Port);
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

	/**
	 * @return the distributedServer_IP
	 */
	public String getDistributedServer_IP() {
		return distributedServer_IP;
	}

	/**
	 * @param distributedServer_IP the distributedServer_IP to set
	 */
	public void setDistributedServer_IP(String distributedServer_IP) {
		this.distributedServer_IP = distributedServer_IP;
	}

	/**
	 * @return the distributedServer_Port
	 */
	public int getDistributedServer_Port() {
		return distributedServer_Port;
	}

	/**
	 * @param distributedServer_Port the distributedServer_Port to set
	 */
	public void setDistributedServer_Port(int distributedServer_Port) {
		this.distributedServer_Port = distributedServer_Port;
	}

	public int getServer_Port() {
		return server_Port;
	}

	public void setServer_Port(int server_Port) {
		this.server_Port = server_Port;
	}
}
