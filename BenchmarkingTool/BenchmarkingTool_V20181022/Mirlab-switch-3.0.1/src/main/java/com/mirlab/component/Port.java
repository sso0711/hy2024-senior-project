package com.mirlab.component;

import java.util.ArrayList;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.projectfloodlight.openflow.protocol.OFPortDesc;

/**
 * @author Cloud 20170509 modified
 */
public class Port {
	private OFPortDesc portDesc;
	private int portNum;
	private Node belong2Node;

	private int portState; // 0.up 1.down
	private int linkState; // 0.up 1.down

	private ChannelHandlerContext ctx;
	private Channel channel;
	private ChannelFuture channelFuture;

	private Port connectedPort = null;
	private Host connectedHost = null;
	private DistributedPort distributedPort = null;
	private ArrayList<Port> connectedPortList = null;
	private ArrayList<Host> connectedHostList = null;

	public Port(OFPortDesc portDesc, int portNum, Node node) {
		this.belong2Node = node;
		this.portDesc = portDesc;
		this.portNum = portNum;
		this.portState = 0;
	}

	public void addConnectedHostToList(Host host) {

		if (connectedHostList == null) {
			this.connectedHostList = new ArrayList<Host>();
		}
		this.connectedHostList.add(host);
		host.setConnectedNode(this.belong2Node);
		host.setBelong2Port(this);
		if (this.connectedHostList.size() == 1) {
			this.connectedHost = host;
		} else {
			this.connectedHost = null;
		}
	}

	// 未完 setconnectedport不知道该怎么办
	public void addConnectedPortToList(Port port) {
		if (connectedPortList == null) {
			this.connectedPortList = new ArrayList<Port>();
		}
		this.connectedPortList.add(port);
		this.setConnectedPort(port);
		if (this.connectedPortList.size() == 1) {
			this.connectedPort = port;
		} else {
			this.connectedPort = null;
		}
	}

	public ArrayList<Host> getConnectedHostList() {
		return connectedHostList;
	}

	public ArrayList<Port> getConnectedPortList() {
		return connectedPortList;
	}

	public Node getBelong2Node() {
		return belong2Node;
	}

	public Host getConnectedHost() {
		return connectedHost;
	}

	public int getPortNum() {
		return portNum;
	}

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public void setChannelFuture(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public int getPortState() {
		return portState;
	}

	public void setPortState(int portState) {
		this.portState = portState;
	}

	public int getLinkState() {
		return linkState;
	}

	public void setLinkState(int linkState) {
		this.linkState = linkState;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public OFPortDesc getPortDesc() {
		return portDesc;
	}

	public Port getConnectedPort() {
		return connectedPort;
	}

	public void setConnectedPort(Port connectedPort) {
		this.connectedPort = connectedPort;
		this.linkState = 0;
	}

	/**
	 * @return the distributedPort
	 */
	public DistributedPort getDistributedPort() {
		return distributedPort;
	}

	/**
	 * @param distributedPort the distributedPort to set
	 */
	public void setDistributedPort(DistributedPort distributedPort) {
		this.distributedPort = distributedPort;
	}

}
