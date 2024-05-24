package com.mirlab.distributedPort.server;

import com.mirlab.component.DistributedPort;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月16日 下午6:20:18 类说明
 */
public class DistributedPortServer {

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public void start(DistributedPort distributedPort, int port) throws Exception {

		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();

		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new DistributedPortServerChannelInitializer(distributedPort));

		// Bind and start to accept incoming connections.

		b.bind(port).sync();

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
