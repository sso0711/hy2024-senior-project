package com.mirlab.component;

import java.util.ArrayList;
import java.util.List;

import org.projectfloodlight.openflow.protocol.OFMessage;

import com.mirlab.netty3.openflow.OpenFlowClient;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年12月27日 下午6:28:56 类说明
 */
public class Agent {
	OpenFlowClient openflowClient;

	public Configuration configuration = new Configuration();
	// list of port object
	public List<Port> portList = new ArrayList<Port>();

	public Agent(long dpid) {
		configuration.setDatapathId(dpid);
		// 0xFFffFFfe local
		Port port = new Port(0xFFffFFfe, this);
		portList.add(port);
	}

	public Port getPort(int portNum) {
		return portList.get(portNum);
	}

	public Port getPortConnectedPort(int portNum) {
		return portList.get(portNum).connectedPort;
	}

	public Agent getPortConnectedAgent(int portNum) {
		return portList.get(portNum).connectedAgent;
	}

	public void addPort() {
		Port port = new Port(portList.size(), this);
		portList.add(port);
	}

	public void createPort(int numCount) {
		for (int i = 0; i < numCount; i++) {
			addPort();
		}
	}

	// send OpenFlow message
	public void sendOFMessage(OFMessage msg) {
		openflowClient.sendOFMessage(msg);
	}

	// start netty
	public void startOpenFlowClient() throws Exception {
		openflowClient = new OpenFlowClient(this.configuration.getIP(), this.configuration.getPort(), this);
		openflowClient.run();
	}

}
