package com.mirlab.openflow;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mirlab.component.Node;
import com.mirlab.global.Global;

public class OpenFlowClient {
	private static final Logger logger = LoggerFactory.getLogger(OpenFlowClient.class.getName());
	public static ClientBootstrap bootstrap = new ClientBootstrap(
			new NioClientSocketChannelFactory(Executors.newSingleThreadExecutor(), Executors.newCachedThreadPool()));
	private Node node;
	private String IP;
	private int port;
	ChannelFuture channelFuture = null;

	public OpenFlowClient(Node node, String IP, int port) {
		this.node = node;
		this.IP = IP;
		this.port = port;
	}

	public void start() {
		try {
			bootstrap.setOption("tcpNoDelay", true);// tcp无延迟
			bootstrap.setOption("keepAlive", true);// 保持tcp连接
			bootstrap.setOption("reuseAddress", true);
			bootstrap.setOption("connectTimeoutMillis", 1000 * 10);// timeout时间10s

			bootstrap.setPipelineFactory(new PiplineFactory(new BaseHandler(node)));

			SocketAddress address = new InetSocketAddress(IP, port);

			logger.debug("Connecting to {}.", address);

			channelFuture = bootstrap.connect(address).sync();// netty链接
			channelFuture.awaitUninterruptibly();
			node.setChannelFuture(channelFuture);

			Global.CHANNEL_POOL.add(channelFuture);// 每一个channelFuture加入到global变数arraylist里面

			if (channelFuture.isSuccess()) {

				node.setBootstrap(bootstrap);
			} else {

				bootstrap.releaseExternalResources();

				channelFuture.cancel();
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();

		} finally {
			if (channelFuture == null) {

				Global.BENCHMARK_NO_BUFF = true;
				bootstrap.releaseExternalResources();// 解放所有资源
			} else {

			}
		}
	}

	public void close() {

		bootstrap.releaseExternalResources();

	}
}
