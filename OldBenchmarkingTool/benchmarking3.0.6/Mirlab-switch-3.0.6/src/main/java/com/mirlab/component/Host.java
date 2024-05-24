package com.mirlab.component;

public class Host {
	private String ip;
	private String mac;
	private Port belong2Port;

	private Node connectedNode = null;

	public Host(String ip, String mac) {
		this.ip = ip;
		this.mac = mac;
	}

	public Node getConnectedNode() {
		return connectedNode;
	}

	public void setConnectedNode(Node connectedNode) {
		this.connectedNode = connectedNode;
	}

	public String getMac() {
		return mac;
	}

	public String getIp() {
		return ip;
	}

	public Port getBelong2Port() {
		return belong2Port;
	}

	public void setBelong2Port(Port belong2Port) {
		this.belong2Port = belong2Port;
	}
}
