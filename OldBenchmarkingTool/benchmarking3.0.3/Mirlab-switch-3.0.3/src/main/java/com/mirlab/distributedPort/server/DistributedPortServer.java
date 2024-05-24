package com.mirlab.distributedPort.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.mirlab.component.DistributedPort;

public class DistributedPortServer {
	private ChannelFactory factory;

	public static ChannelGroup channelGroup = new DefaultChannelGroup();

	public void start(DistributedPort distributedPort, int server_Port) {

		factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setPipelineFactory(new DistributedPortFactory(new DistributedPortServerBaseHandler(distributedPort)));

		bootstrap.bind(new InetSocketAddress(server_Port));
	}

	public void stop() {

		ChannelGroupFuture channelGroupFuture = DistributedPortServer.channelGroup.close();
		channelGroupFuture.awaitUninterruptibly();
		factory.releaseExternalResources();

	}
}
