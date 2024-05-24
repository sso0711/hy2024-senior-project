package com.mirlab.distributedPort.client;

import io.netty.bootstrap.Bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月17日 下午11:53:35 类说明
 */
public class DistributedPortClient {

	private EventLoopGroup group;
	private Channel channel;

	public void start(String ip, int port) throws Exception {

		group = new NioEventLoopGroup();

		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class).handler(new DistributedPortClientChannelInitializer());

		// Start the connection attempt.
		channel = b.connect(ip, port).sync().channel();

	}

	public void stop() {
		group.shutdownGracefully();
	}

	public void sendMessage(byte[] data) {

		if (channel != null && channel.isActive()) {
			channel.writeAndFlush(data);
		}

	}
}
