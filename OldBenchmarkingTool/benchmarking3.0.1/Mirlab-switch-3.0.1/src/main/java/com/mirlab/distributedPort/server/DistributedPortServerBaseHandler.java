package com.mirlab.distributedPort.server;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mirlab.component.DistributedPort;
import com.mirlab.lib.PacketMaker_NEW;
import com.mirlab.openflow.BaseHandler;

public class DistributedPortServerBaseHandler extends IdleStateAwareChannelHandler {

	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	DistributedPort distributedPort;

	public DistributedPortServerBaseHandler(DistributedPort distributedPort) {
		super();
		this.distributedPort = distributedPort;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {
		ChannelBuffer buf = (ChannelBuffer) event.getMessage();
		byte[] data = buf.array();

		OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(distributedPort.getConnectedPort(), data);

		distributedPort.getConnectedPort().getBelong2Node().sendPacket(ofpi);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}
