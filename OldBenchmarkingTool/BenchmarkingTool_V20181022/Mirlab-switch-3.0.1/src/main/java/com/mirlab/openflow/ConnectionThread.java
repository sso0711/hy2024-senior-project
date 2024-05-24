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

//20170308
//修改成多线程，之后不显示时间
public class ConnectionThread {
	private static final Logger logger = LoggerFactory.getLogger(ConnectionThread.class.getName());
	public static ClientBootstrap bootstrap = new ClientBootstrap(
			new NioClientSocketChannelFactory(Executors.newSingleThreadExecutor(), Executors.newCachedThreadPool()));
	private Node node;
	private String IP;
	private int port;
	ChannelFuture channelFuture = null;

	public ConnectionThread(Node node, String IP, int port) {
		this.node = node;
		this.IP = IP;
		this.port = port;

		bootstrap.setOption("tcpNoDelay", true);// tcp无延迟
		bootstrap.setOption("keepAlive", true);// 保持tcp连接
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("connectTimeoutMillis", 1000 * 10);// timeout时间10s

		bootstrap.setPipelineFactory(new PiplineFactory(new BaseHandler(node)));

	}

	public void start() {
		try {

			SocketAddress address = new InetSocketAddress(IP, port);

			logger.debug("Connecting to {}.", address);

			channelFuture = bootstrap.connect(address).sync();// netty链接
			channelFuture.awaitUninterruptibly();
			node.setChannelFuture(channelFuture);

			Global.CHANNEL_POOL.add(channelFuture);// 每一个channelFuture加入到global变数arraylist里面

			if (channelFuture.isSuccess()) {
				Global.CONTROLLER_IS_OPEN = true;
				node.setBootstrap(bootstrap);
			} else {
				bootstrap.releaseExternalResources();
				Global.CONTROLLER_IS_OPEN = false;
				channelFuture.cancel();
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();

		} finally {
			if (channelFuture == null) {
				Global.CONTROLLER_IS_OPEN = false;
				Global.BENCHMARK_NO_BUFF = true;
				bootstrap.releaseExternalResources();// 解放所有资源
			} else {

			}
		}
	}
}
