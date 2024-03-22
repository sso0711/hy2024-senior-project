package com.mirlab.component;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年12月27日 下午6:41:54 类说明
 */
public class Host {
	private String ip;
	private String mac;

	private Port belong2Port;
	private Agent connectedAgent;

	public Host(String ip, String mac) {
		this.ip = ip;
		this.mac = mac;
	}

	public Agent getConnectedAgent() {
		return connectedAgent;
	}

	public void setConnectedAgent(Agent connectedAgent) {
		this.connectedAgent = connectedAgent;
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

	public void sendARPrequest() {

	}

	public void sendARPreply() {

	}
}
