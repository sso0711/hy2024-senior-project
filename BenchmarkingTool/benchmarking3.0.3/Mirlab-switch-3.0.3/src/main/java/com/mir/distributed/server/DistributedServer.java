package com.mir.distributed.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.mirlab.global.Global;

public class DistributedServer {
	private ChannelFactory factory;

	public static ChannelGroup channelGroup;
	// key=IP
	public static ConcurrentMap<String, Channel> channelMap;

	public DistributedServer() {
		channelGroup = new DefaultChannelGroup();
		channelMap = new ConcurrentHashMap<String, Channel>();
	}

	public boolean start(int port) {
		try {

			factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool());

			ServerBootstrap bootstrap = new ServerBootstrap(factory);
			bootstrap.setOption("child.tcpNoDelay", true);
			bootstrap.setOption("child.keepAlive", true);
			bootstrap.setOption("reuseAddress", true);
			bootstrap.setPipelineFactory(new DistributedFactory(new DistributedServerBaseHandler()));

			bootstrap.bind(new InetSocketAddress(port));

			try {
				InetAddress addr = InetAddress.getLocalHost();
				Global.distribudtedTool_IP_List.add(addr.getHostAddress());
				Global.local_IP = addr.getHostAddress();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("hhhh");
			return false;
		}

		System.out.println("Server is started...");
		return true;
	}

	public void stop() {

		ChannelGroupFuture channelGroupFuture = DistributedServer.channelGroup.close();
		channelGroupFuture.awaitUninterruptibly();
		factory.releaseExternalResources();
		System.out.println("Server is stopped.");
	}
}
