package com.mirlab.netty3.openflow;
/** 
* @author Haojun E-mail: lovingcloud77@gmail.com
* @version ����ʱ�䣺2017��12��28�� ����9:13:37 
* ��˵�� 
*/

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.projectfloodlight.openflow.protocol.OFMessage;

import com.mirlab.component.Agent;

/**
 * Sends a sequence of integers to a {@link FactorialServer} to calculate the
 * factorial of the specified integer.
 */
public final class OpenFlowClient {

	private String IP;
	private int port;
	private Agent agent;
	private Channel channel; //org.jboss.netty.channel.Channel

	public OpenFlowClient(String IP, int port, Agent agent) {
		this.IP = IP;
		this.port = port;
		this.agent = agent;
	}

	public void run() {
		ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newSingleThreadExecutor(), Executors.newCachedThreadPool())); // 인스턴스 생성
		try {
			// configuring a channel
			bootstrap.setOption("tcpNoDelay", true);// tcp���ӳ�
			bootstrap.setOption("keepAlive", true);// ����tcp����
			bootstrap.setOption("reuseAddress", true);
			bootstrap.setOption("connectTimeoutMillis", 1000 * 10);// timeoutʱ��10s

			// configuring a channel pipeline
			bootstrap.setPipelineFactory(new PiplineFactory(new ClientHandler(agent)));

			SocketAddress address = new InetSocketAddress(IP, port);

			ChannelFuture channelFuture = bootstrap.connect(address).sync();// netty����
			channelFuture.awaitUninterruptibly();

			assert channelFuture.isDone();
			if(!channelFuture.isCancelled() && channelFuture.isSuccess())
				channel = channelFuture.getChannel();

		} catch (InterruptedException e1) {
			e1.printStackTrace();

		} finally {
			// bootstrap.releaseExternalResources();// ���������Դ
		}
	}

	public void sendOFMessage(OFMessage msg) {
		channel.write(Collections.singletonList(msg));
	}

}
