package com.mir.distributed.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mir.distributed.message.DistributedMessage;
import com.mir.ui.distributedGUI.DistributedFrame;
import com.mirlab.global.Global;
import com.mirlab.openflow.BaseHandler;

public class DistributedServerBaseHandler extends IdleStateAwareChannelHandler {

	DistributedMessage distributedMessage = DistributedMessage.INSTANCE;

	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	public static int count = 0;
	public static ArrayList<Channel> ac = new ArrayList<Channel>();

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		super.handleUpstream(ctx, e);

	}

	public DistributedServerBaseHandler() {
		super();

	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		logger.debug("Channel connected.");

		DistributedServer.channelGroup.add(ctx.getChannel());
		ac.add(ctx.getChannel());

		DistributedFrame.table.getModel()
				.setValueAt("Slave " + count + ": " + ctx.getChannel().getRemoteAddress().toString(), count, 0);
		DistributedFrame.table.getModel().setValueAt("Slave ", count, 1);

		JSONObject j = distributedMessage.msgType0(count);

		ctx.getChannel().write(j.toString());
		count++;

		// ****************20181017
		InetSocketAddress socketAddr = (InetSocketAddress) ctx.getChannel().getRemoteAddress();
		Global.distribudtedTool_IP_List.add(socketAddr.getHostString());

		if (!DistributedServer.channelMap.containsKey(socketAddr.getHostString())) {
			DistributedServer.channelMap.put(socketAddr.getHostString(), ctx.getChannel());
		}

	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		logger.debug("Channel closed.");
		DistributedServer.channelGroup.remove(ctx.getChannel());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {

		JSONObject message = new JSONObject(event.getMessage().toString());
		switch (message.getInt("msgType")) {
		case 0:

			DistributedFrame.table_1.getModel().setValueAt(message.getString("str"), message.getInt("newX"),
					message.getInt("newY"));

			break;
		default:
			break;
		}

	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

	}
}
