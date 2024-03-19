package com.mirlab.lib;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mir.ui.Main;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.openflow.BaseHandler;

public class Tasks {
	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	public static boolean HAS_STARTED = false;
	public static boolean HAS_LINK_UP = false;
	public static boolean IS_COMPLETED = false;
	public static DecimalFormat df = new DecimalFormat("#0.00000");
	public static OFMessage ofmSrcDown;
	public static OFMessage ofmDstDown;
	public static OFMessage ofmSrcUp;
	public static OFMessage ofmDstUp;

	public static void START_TASK_8_NEW() {

		Main.gui.S_startButton.setEnabled(false);
		Main.gui.S_metricList.setEnabled(false);

		try {
			long dpid = Global.SWITCH_ID_OFF_SET;
			int timeoutTime = 0;
			int nodeId = 1;
			int totalCount = 0;
			int count = 0;
			Node[] nodes = new Node[20000];

			// buf node
			for (int i = 0; i < 10000; i++) {
				nodes[i] = new Node((long) dpid++, nodeId++);

			}

			int j = 0;
			n: while (j != 10000) {
				int stopNumber = j;
				for (int i = 0; i < 100; i++) {
					if (!Global.BENCHMARK_NO_BUFF) {
						nodes[j++].start_OpenFlowClient();
						Thread.sleep(25);
					} else {
						break;
					}
				}

				// Thread.sleep(4000);

				while (true) {
					for (int i = stopNumber; i < stopNumber + 100; i++) {
						if (nodes[i].isHasReceivedHello()) {
							count++;

						}
					}

					logger.info(count + "");
					if (count >= 90) {
						totalCount = totalCount + count;
						count = 0;
						timeoutTime = 0;
						break;

					} else if (timeoutTime == 1) {
						totalCount = totalCount + count;
						break n;
					} else {
						timeoutTime = 1;
						Thread.sleep(Global.THRESHOLD_THREADCONTROL_SESSION_CAPACITY_CCD);
						count = 0;
					}
				}

			}

			Result.ADD_RESULT(totalCount + "", 9, 2);

			Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
			Log.ADD_LOG_PANEL("The number of Hello Secction:" + totalCount, Tasks.class.toString());

			for (int i = 0; i < 10000; i++) {
				if (nodes[i].getChannelFuture() != null) {
					nodes[i].getChannelFuture().getChannel().close().awaitUninterruptibly();
					nodes[i].getChannelFuture().cancel();
				}
				if (nodes[i].getBootstrap() != null) {
					nodes[i].getBootstrap().releaseExternalResources();
				}
				nodes[i] = null;
			}
			nodes = null;
			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			Main.gui.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();

		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.gui.S_progressBarTotal.setValue(1);
			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void START_TASK_9_NEW() {

		Main.gui.S_startButton.setEnabled(false);
		Main.gui.S_metricList.setEnabled(false);

		try {
			int timeoutTime = 0;
			long dpid = Global.SWITCH_ID_OFF_SET;
			int nodeId = 1;
			int totalCount = 0;
			int count = 0;
			Node preNode = null;
			Node[] nodes = new Node[10000];

			nodes[0] = new Node((long) dpid++, 0);
			nodes[0].creatPort(2);
			preNode = nodes[0];

			// buf node
			for (int i = 1; i < 10000; i++) {
				nodes[i] = new Node((long) dpid++, nodeId++);
				nodes[i].creatPort(2);
				nodes[i].getPortList().get(0).setConnectedPort(preNode.getPortList().get(1));
				preNode.getPortList().get(1).setConnectedPort(nodes[i].getPortList().get(0));
				preNode.setNextNode(nodes[i]);
				preNode = nodes[i];
			}

			int j = 0;
			n: while (j != 10000) {
				int stopNumber = j;

				if (j != 0) {

					nodes[j++].start_OpenFlowClient();
					Thread.sleep(1);
					preNode.getPortList().get(1).setConnectedPort(nodes[j - 1].getPortList().get(0));

				} else {

					nodes[j++].start_OpenFlowClient();

					Thread.sleep(1);
				}

				for (int i = 1; i < 99; i++) {
					if (!Global.BENCHMARK_NO_BUFF) {
						nodes[j++].start_OpenFlowClient();

						Thread.sleep(1);
					} else {
						break;
					}
				}

				nodes[j - 1].getPortList().get(1).setConnectedPort(null);
				preNode = nodes[j - 1];
				nodes[j++].start_OpenFlowClient();

				Thread.sleep(8000);

				while (true) {
					for (int i = stopNumber; i < stopNumber + 100; i++) {
						if (nodes[i].getNumberOflldpPacket() >= 2) {
							count++;

						}
					}

					logger.info(count + "");
					if (count >= 90) {
						totalCount = totalCount + count;
						count = 0;
						timeoutTime = 0;
						break;

					} else if (timeoutTime == 1) {
						totalCount = totalCount + count;
						break n;
					} else {
						timeoutTime = 1;
						Thread.sleep(Global.THRESHOLD_NETWORK_DISCOVERY_SIZE_NS);
						count = 0;
					}
				}

			}

			Result.ADD_RESULT(totalCount + "", 0, 5);
			Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
			Log.ADD_LOG_PANEL("The number of Node:" + totalCount, Tasks.class.toString());

			for (int i = 0; i < 10000; i++) {
				if (nodes[i].getChannelFuture() != null) {
					nodes[i].getChannelFuture().getChannel().close().awaitUninterruptibly();
					nodes[i].getChannelFuture().cancel();
				}
				if (nodes[i].getBootstrap() != null) {
					nodes[i].getBootstrap().releaseExternalResources();
				}

				nodes[i] = null;
			}
			nodes = null;

			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			Main.gui.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public static void START_TASK_9() {

		Main.gui.S_startButton.setEnabled(false);
		Main.gui.S_metricList.setEnabled(false);
		try {
			Thread.sleep(3000);

			long dpid = 2000;
			int nodeId = 1;
			Node preNode = null;
			Node node;
			Global.TEST_NODE_DPID = dpid;
			Node root = new Node((long) dpid++, 0);
			root.creatPort(2);

			root.start_OpenFlowClient();

			int timeOutTime = Global.THRESHOLD_NETWORK_DISCOVERY_SIZE_NS;

			preNode = root;
			int times = 0;

			Thread.sleep(timeOutTime);

			while (true) {
				if (preNode.getNumberOflldpPacket() < 2) {

					if (times >= 4) {
						break;
					}
					times++;
					timeOutTime = timeOutTime * Global.MULTIPLE;
					Thread.sleep(timeOutTime);

				} else {

					Global.TEST_NODE_DPID = dpid;
					node = new Node((long) dpid++, nodeId++);

					node.creatPort(2);

					node.start_OpenFlowClient();

					node.getPortList().get(0).setConnectedPort(preNode.getPortList().get(1));
					preNode.getPortList().get(1).setConnectedPort(node.getPortList().get(0));
					preNode.setNextNode(node);
					preNode = node;

					if (times == 0) {
						Global.COUNT_METRIC_9[0]++;
					} else if (times == 1) {
						Global.COUNT_METRIC_9[1]++;
					} else if (times == 2) {
						Global.COUNT_METRIC_9[2]++;
					} else if (times == 3) {
						Global.COUNT_METRIC_9[3]++;
					}

					times = 0;

					timeOutTime = Global.THRESHOLD_NETWORK_DISCOVERY_SIZE_NS;
					Thread.sleep(timeOutTime);
				}

			}

			// Log.ADD_LOG_PANEL("The number of Hello in:" +
			// Result.NUMBER_OF_IN_HELLO.size(), Tasks.class.toString());

			Result.ADD_RESULT(nodeId + "", 0, 5);

			Log.ADD_LOG_PANEL("Time out... Test Completed!", Tasks.class.toString());
			Log.ADD_LOG_PANEL("The number of Node:" + nodeId, Tasks.class.toString());
			Main.gui.S_progressBarTotal.setValue(1);
			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();

			for (int i = 0; i < 4; i++) {
				logger.info("Time out " + (i + 1) + ": " + Global.COUNT_METRIC_9[i]);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void START_TASK_10_NEW() {

		Main.gui.S_startButton.setEnabled(false);
		Main.gui.S_metricList.setEnabled(false);

		if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
			final ArrayList<OFMessage> buf = PacketMaker_NEW.MAKE_ARP_REQUEST_REPLY();
			HAS_STARTED = true;
			Global.ROOTNODE.sendUnknownPacketThroughput_fms_new_arp(buf);

		} else {
			final ArrayList<OFMessage> buf = PacketMaker_NEW.MAKE_TCP_UDP_PACKET(Global.TCP_PACKET, 1000);
			HAS_STARTED = true;
			Global.ROOTNODE.sendUnknownPacketThroughput_fms_new(buf);
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Result.ADD_RESULT(Result.TOTAL_NUMBER_OF_FLOW_MOD + "", 1, 5);
		Log.ADD_LOG_PANEL("size of FlowMod: " + Result.TOTAL_NUMBER_OF_FLOW_MOD, Tasks.class.toString());
		Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());

		Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
		Main.gui.S_progressBarTotal.setValue(1);

		Initializer.INITIAL_CHANNEL_POOL();

	}

	public static void START_TASK_10() {

		Main.gui.S_startButton.setEnabled(false);
		Main.gui.S_metricList.setEnabled(false);

		final ArrayList<OFMessage> buf = PacketMaker_NEW.MAKE_IP_PACKET();
		HAS_STARTED = true;

		// new Thread(new Runnable() {

		// public void run() {

		Global.ROOTNODE.sendUnknownPacketThroughput_fms(buf);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Result.ADD_RESULT(Global.ROOTNODE.getNumberOfFlowEntry() + "", 1, 5);
		Log.ADD_LOG_PANEL("size of FlowMod: " + Global.ROOTNODE.getNumberOfFlowEntry(), Tasks.class.toString());
		Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());

		Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
		Main.gui.S_progressBarTotal.setValue(1);

		Initializer.INITIAL_CHANNEL_POOL();

		for (int i = 0; i < 4; i++) {
			logger.info("Time out " + (i + 1) + ": " + Global.COUNT_METRIC_10[i]);
		}
	}

	public static void START_TASK_2_FLOODLIGHT_() {
		Result.ADD_RESULT(df.format(
				BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT,
						Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN)),
				3, 2);

		Log.ADD_LOG_PANEL("size of in: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size(),
				Tasks.class.toString());
		Log.ADD_LOG_PANEL("size of out: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size(),
				Tasks.class.toString());
		Log.ADD_LOG_PANEL(
				"Average processing time : "
						+ BenchmarkTimer.GET_TIME(Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT,
								Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN)
								/ (double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size()
						+ "ms",
				Tasks.class.toString());

		Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
		Main.gui.S_progressBarTotal.setValue(1);
		Initializer.INITIAL_CHANNEL_POOL();

	}

}
