package com.mir.distributed.client;

import org.json.JSONObject;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月17日 下午11:53:35 类说明
 */
public class DistributedClient {

	// singleton

	private static class SingletonHolder {
		private static final DistributedClient INSTANCE = new DistributedClient();
	}

	private DistributedClient() {
	}

	public static final DistributedClient getInstance() {

		DistributedClient instance = SingletonHolder.INSTANCE;
		return instance;
	}

	// *********************************************************************************

	static final boolean SSL = System.getProperty("ssl") != null;

	private EventLoopGroup group;
	private Channel channel;

	public boolean start(String ip, int port) throws Exception {
		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		} else {
			sslCtx = null;
		}

		group = new NioEventLoopGroup();

		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class)
				.handler(new DistributedClientChannelInitializer(sslCtx, ip, port));

		// Start the connection attempt.
		channel = b.connect(ip, port).sync().channel();

		return channel.isActive();

	}

	public void stop() {
		group.shutdownGracefully();
	}

	public void sendMessage(JSONObject msg) {

		if (channel.isActive()) {
			channel.writeAndFlush(msg.toString());
		}

	}
}
