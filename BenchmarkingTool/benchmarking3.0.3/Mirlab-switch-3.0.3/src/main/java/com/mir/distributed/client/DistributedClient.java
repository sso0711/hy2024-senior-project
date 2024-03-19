package com.mir.distributed.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mir.distributed.server.DistributedServer;
import com.mir.ui.Main;
import com.mir.ui.distributedGUI.DistributedFrame;
import com.mirlab.global.Global;

public class DistributedClient {
	private static final Logger logger = LoggerFactory.getLogger(DistributedClient.class.getName());
	private ClientBootstrap bootstrap;

	private boolean isSuccess = false;

	private SocketAddress address;

	private ChannelFactory factory;
	private ChannelFuture channelFuture = null;

	public DistributedClient() {

	}

	public boolean start(String ip, int port) {
		try {

			address = new InetSocketAddress(ip, port);

			factory = new NioClientSocketChannelFactory(Executors.newSingleThreadExecutor(),
					Executors.newCachedThreadPool());

			bootstrap = new ClientBootstrap(factory);

			bootstrap.setOption("tcpNoDelay", true);
			bootstrap.setOption("keepAlive", true);
			bootstrap.setOption("reuseAddress", true);
			bootstrap.setOption("connectTimeoutMillis", 1000 * 10);

			bootstrap.setPipelineFactory(new DistributedFactory(new DistributedClientBaseHandler()));

			logger.debug("Connecting to {}.", address);

			channelFuture = bootstrap.connect(address).sync();
			channelFuture.awaitUninterruptibly();

			Global.CHANNEL_POOL.add(channelFuture);

		} catch (InterruptedException e1) {
			e1.printStackTrace();

		} finally {
			if (channelFuture == null) {

				DistributedFrame.btnStop.setEnabled(false);
				DistributedFrame.btnNewButton.setEnabled(true);
				JOptionPane.showMessageDialog(null, "Please check if Master Benchmark is running.", "Disconnected",
						JOptionPane.ERROR_MESSAGE);

				Global.IS_ENABLE_DISTRIBUTED = false;
				Global.IS_MASTER = true;

				return false;
			} else {
				JOptionPane.showMessageDialog(null, "Connected with Master Benchmark", "Connected",
						JOptionPane.INFORMATION_MESSAGE);
				DistributedFrame.table.getModel().setValueAt(Global.MASTER_IP, 0, 0);
				DistributedFrame.table.getModel().setValueAt("Master", 0, 1);
				Main.gui.S_startButton.setEnabled(false);

				return true;
			}
		}
	}

	public void stop() {

		ChannelGroupFuture channelGroupFuture = DistributedServer.channelGroup.close();
		channelGroupFuture.awaitUninterruptibly();
		factory.releaseExternalResources();
		System.out.println("Server is stopped.");
	}
}
