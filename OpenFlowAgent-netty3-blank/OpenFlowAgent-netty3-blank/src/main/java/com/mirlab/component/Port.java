package com.mirlab.component;

import java.util.ArrayList;

import com.mirlab.lib.macAddress.MacAddress;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年12月27日 下午6:41:47 类说明
 */
public class Port {
	private int portNo;
	private String name;
	private String macAddress;

	// point
	public Agent belong2Agent;
	public Port connectedPort = null;
	public Agent connectedAgent = null;

	private ArrayList<Host> connectedHostList;

	public Port(int portNo, Agent node) {
		this.portNo = portNo;
		this.belong2Agent = node;
		this.name = "s" + Long.toString(node.configuration.getDatapathId()) + "-Eth" + portNo;
		this.macAddress = MacAddress.getRandomMac();
	}

	public int getPortNo() {
		return portNo;
	}

	public String getName() {
		return name;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setConnectedPort(Port connectedPort) {
		this.connectedPort = connectedPort;
		this.connectedAgent = connectedPort.belong2Agent;
		connectedPort.connectedPort = this;
		connectedPort.connectedAgent = this.belong2Agent;
	}

	public void addConnectedHostToList(Host host) {
		if (connectedHostList == null) {
			this.connectedHostList = new ArrayList<Host>();
		}
		this.connectedHostList.add(host);
		host.setConnectedAgent(this.belong2Agent);
		host.setBelong2Port(this);
	}

}
