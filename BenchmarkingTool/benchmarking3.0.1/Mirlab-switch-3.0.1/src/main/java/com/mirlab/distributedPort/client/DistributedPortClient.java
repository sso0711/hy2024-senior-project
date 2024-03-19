package com.mirlab.distributedPort.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class DistributedPortClient {
	ClientBootstrap bootstrap;
	SocketAddress address;
	ChannelFuture channelFuture = null;

	public void start(String controller_IP, int controller_Port) {
		try {

			address = new InetSocketAddress(controller_IP, controller_Port);

			bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newSingleThreadExecutor(),
					Executors.newCachedThreadPool()));

			bootstrap.setOption("tcpNoDelay", true);
			bootstrap.setOption("keepAlive", true);
			bootstrap.setOption("reuseAddress", true);
			bootstrap.setOption("connectTimeoutMillis", 1000 * 10);

			bootstrap.setPipelineFactory(new DistributedPortFactory(new DistributedPortClientBaseHandler()));

			channelFuture = bootstrap.connect(address).sync();
			channelFuture.awaitUninterruptibly();

		} catch (InterruptedException e1) {
			e1.printStackTrace();

		} finally {

		}
	}

	public void sendMessage(byte[] data) {
		if (channelFuture.getChannel().isWritable()) {
			ChannelBuffer buf = ChannelBuffers.buffer(data.length);
			buf.writeBytes(data);
			channelFuture.getChannel().write(buf);
		}

	}
}
