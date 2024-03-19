package com.mir.distributed.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mir.ui.DistributedFrame;
import com.mir.ui.Main;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.openflow.BaseHandler;
import com.mirlab.openflow.ConnectionThread;
import com.mirlab.openflow.PiplineFactory;

public class DistributedClient {
	private static final Logger logger = LoggerFactory.getLogger(ConnectionThread.class.getName());
	public static ClientBootstrap bootstrap = new ClientBootstrap(
			new NioClientSocketChannelFactory(Executors.newSingleThreadExecutor(), Executors.newCachedThreadPool()));

	SocketAddress address;
	ChannelFuture channelFuture = null;

	public DistributedClient() {

		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("connectTimeoutMillis", 1000 * 10);

		bootstrap.setPipelineFactory(new DistributedFactory(new DistributedClientBaseHandler()));

	}

	public void start() {
		try {

			SocketAddress address = new InetSocketAddress(Global.MASTER_IP, Global.MASTER_PORT);

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
			} else {
				JOptionPane.showMessageDialog(null, "Connected with Master Benchmark", "Connected",
						JOptionPane.INFORMATION_MESSAGE);
				DistributedFrame.table.getModel().setValueAt(Global.MASTER_IP, 0, 0);
				DistributedFrame.table.getModel().setValueAt("Master", 0, 1);
				Main.S_startButton.setEnabled(false);
			}
		}
	}
}
