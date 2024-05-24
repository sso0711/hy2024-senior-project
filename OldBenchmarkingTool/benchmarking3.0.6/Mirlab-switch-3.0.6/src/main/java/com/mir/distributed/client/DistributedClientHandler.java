package com.mir.distributed.client;

import org.json.JSONObject;

import com.mir.distributed.message.DistributedMessage;
import com.mirlab.global.Global;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月18日 上午12:04:22 类说明
 */
public class DistributedClientHandler extends ChannelInboundHandlerAdapter {

	DistributedMessage distributedMessage = DistributedMessage.INSTANCE;

	/**
	 * Creates a client-side handler.
	 */

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Global.MASTER_CHANNEL = ctx.channel();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Global.MASTER_CHANNEL = null;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		// Echo back the received object to the server.

		JSONObject j = new JSONObject(msg.toString());
		distributedMessage.clientProcessOf(j);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
