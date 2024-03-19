package com.mir.distributed.server;

import java.net.InetSocketAddress;

import org.json.JSONObject;

import com.mir.distributed.message.DistributedMessage;
import com.mirlab.global.Global;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月16日 下午6:31:05 类说明
 */
public class DistributedServerHandler extends ChannelInboundHandlerAdapter {

	DistributedServer distributedServer = DistributedServer.getInstance();
	DistributedMessage distributedMessage = DistributedMessage.INSTANCE;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		int benchmarkingTool_ID = distributedServer.channelMap.size();

		JSONObject j = distributedMessage.msgType0(benchmarkingTool_ID, ctx.channel().remoteAddress().toString());

		ctx.writeAndFlush(j.toString());

		InetSocketAddress socketAddr = (InetSocketAddress) ctx.channel().remoteAddress();
		Global.distribudtedTool_IP_List.add(socketAddr.getHostString());

		if (!distributedServer.channelMap.containsKey(socketAddr.getHostString())) {

			distributedServer.channelMap.put(socketAddr.getHostString(), ctx.channel());

		} else {

			distributedServer.channelMap.replace(socketAddr.getHostString(), ctx.channel());

		}
	}

	@Override

	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		InetSocketAddress socketAddr = (InetSocketAddress) ctx.channel().remoteAddress();

		System.out.println("channel in active");

		if (distributedServer.channelMap.containsKey(socketAddr.getHostString())) {
			distributedServer.channelMap.remove(socketAddr.getHostString());
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		// Echo back the received object to the client.
		JSONObject message = new JSONObject(msg.toString());
		distributedMessage.serverProcessOf(message);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();

		InetSocketAddress socketAddr = (InetSocketAddress) ctx.channel().remoteAddress();

		if (distributedServer.channelMap.containsKey(socketAddr.getHostString())) {
			distributedServer.channelMap.remove(socketAddr.getHostString());
		}
		ctx.close();
	}
}
