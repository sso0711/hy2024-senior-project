package com.mir.distributed.server;

import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.mirlab.global.Global;
import com.mirlab.lib.IP;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月16日 下午6:20:18 类说明
 */
public class DistributedServer {

	// singleton

	private static class SingletonHolder {
		private static final DistributedServer INSTANCE = new DistributedServer();
	}

	private DistributedServer() {
	}

	public static final DistributedServer getInstance() {
		return SingletonHolder.INSTANCE;
	}
	// *********************************************************************************

	static final boolean SSL = System.getProperty("ssl") != null;

	public ConcurrentMap<String, Channel> channelMap;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private Channel channel;

	protected int workerThreads = 0;

	public boolean start(int port) throws Exception {

		channelMap = new ConcurrentHashMap<String, Channel>();

		final SslContext sslCtx;
		
		if (SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}

		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup(workerThreads);

		ServerBootstrap b = new ServerBootstrap();
		
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new DistributedServerChannelInitializer(sslCtx));

		// Bind and start to accept incoming connections.

		channel = b.bind(port).sync().channel();

		try {

			Global.distribudtedTool_IP_List.add(IP.getLocalHostIP());
			Global.local_IP = IP.getLocalHostIP();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return channel.isActive();

	}

	public void stop() {

		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();

		// Wait until all threads are terminated.
		try {
			bossGroup.terminationFuture().sync();
			workerGroup.terminationFuture().sync();
		} catch (InterruptedException e) {
			// log.warn("Interrupted while stopping", e);
			Thread.currentThread().interrupt();
		}

	}

}
