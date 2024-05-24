package com.mirlab.distributedPort.server;

import org.projectfloodlight.openflow.protocol.OFMessage;

import com.mirlab.component.DistributedPort;
import com.mirlab.lib.PacketMaker_NEW;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月16日 下午6:31:05 类说明
 */
public class DistributedPortServerHandler extends SimpleChannelInboundHandler<byte[]> {

	private DistributedPort distributedPort;

	public DistributedPortServerHandler(DistributedPort distributedPort) {
		// TODO Auto-generated constructor stub
		this.distributedPort = distributedPort;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();

		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		// TODO Auto-generated method stub

		System.out.println(msg[0]);
		System.out.println(msg[1]);

		OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(distributedPort.getConnectedPort(), msg);

		distributedPort.getConnectedPort().getBelong2Node().sendPacket(ofpi);
	}
}
