package com.mir.distributed.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class DistributedServer {
	private ChannelFactory factory;

	public static ChannelGroup channelGroup = new DefaultChannelGroup();

	public void start() {

		factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setPipelineFactory(new DistributedFactory(new DistributedServerBaseHandler()));
		
		
		Channel channel = bootstrap.bind(new InetSocketAddress(8911));
		System.out.println("Server is started...");
	}

	public void stop() {

		ChannelGroupFuture channelGroupFuture = DistributedServer.channelGroup.close();
		channelGroupFuture.awaitUninterruptibly();
		factory.releaseExternalResources();
		System.out.println("Server is stopped.");
	}
}
