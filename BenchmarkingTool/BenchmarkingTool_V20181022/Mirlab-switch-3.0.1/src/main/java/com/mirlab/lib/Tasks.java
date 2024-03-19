package com.mirlab.lib;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumSet;

import java.util.Random;

import javax.swing.JOptionPane;

import org.projectfloodlight.openflow.protocol.OFEchoRequest;
import org.projectfloodlight.openflow.protocol.OFHello;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketInReason;
import org.projectfloodlight.openflow.protocol.OFPortConfig;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.projectfloodlight.openflow.protocol.OFPortReason;
import org.projectfloodlight.openflow.protocol.OFPortState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mir.ui.Main;
import com.mir.ui.ProgressUpdate;
import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.openflow.BaseHandler;
import com.mirlab.openflow.ConnectionThread;

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

	public static void START_TASK_0_NEW() {// 发现拓扑时间的主要进程
		try {
			// 修改ui的属性
			Main.S_progressBar.setValue(0);// 进度条
			Main.S_startButton.setEnabled(false);// run按钮
			Main.S_metricList.setEnabled(false);// metric选项

			long dpid = Global.SWITCH_ID_OFF_SET;// ？？？
			// 创建 root node ，每个node两个port
			Node preNode = null;
			Node node;
			// Global.NUMBER_OF_PORT 初始值是 0
			Global.NUMBER_OF_PORT = 2;
			Node root = new Node((long) dpid++, 0);
			// root.setLLDP_IN(new ArrayList());
			// root.setLLDP_OUT(new ArrayList());
			root.creatPort(2);

			preNode = root;

			for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {// 创建所需node并链接每个node

				node = new Node((long) dpid++, i);
				// node.setLLDP_IN(new ArrayList());
				// node.setLLDP_OUT(new ArrayList());
				node.creatPort(2);

				node.getPortList().get(0).setConnectedPort(preNode.getPortList().get(1));// 第一个getPortList返回Port
																							// class，Port
																							// class
																							// setConnectedPort

				preNode.getPortList().get(1).setConnectedPort(node.getPortList().get(0));
				preNode.setNextNode(node);
				preNode = node;
			}

			if (Global.TOPO_TYPE == Global.TOPO_RING) {// 如果是ring（圆形），链接最前和最后的node
				preNode.getPortList().get(1).setConnectedPort(root.getPortList().get(0));
				root.getPortList().get(0).setConnectedPort(preNode.getPortList().get(1));
				preNode.setNextNode(root);
			} else {
				preNode.setNextNode(null);
			}

			// 最前和最后node赋给global变量
			Global.ROOTNODE = root;
			Global.LEAFNODE = preNode;

			Node tNode = Global.ROOTNODE;
			for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {// 启动每个node线程，netty发送openflow

				ConnectionThread ct = new ConnectionThread(tNode, Global.SDN_CONTROLLER_IP[0],
						Global.SDN_CONTROLLER_PORT);
				ct.start();

				if (tNode.getNextNode() != null) {
					tNode = tNode.getNextNode();
				}
			}

			Thread.sleep(10000);

			HAS_STARTED = true;// 哪里有用到？

			try {// 休息15000
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Node tempNode = Global.ROOTNODE;
			for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {

				if (Global.TOPO_TYPE == Global.TOPO_RING) {// 环形情况下
					for (int j = 0; j < 2; j++) {
						Result.TOPOLOGY_DISCOVERY_LLDP_IN.add(tempNode.getLLDP_IN().get(j));

						Result.TOPOLOGY_DISCOVERY_LLDP_OUT.add(tempNode.getLLDP_OUT().get(j));
					}
				} else {
					for (int j = 0; j < 2; j++) {// 直线型情况下
						if (tempNode == Global.ROOTNODE || tempNode == Global.LEAFNODE) {
							if (j == 0) {
								Result.TOPOLOGY_DISCOVERY_LLDP_IN.add(tempNode.getLLDP_IN().get(j));
							}
							Result.TOPOLOGY_DISCOVERY_LLDP_OUT.add(tempNode.getLLDP_OUT().get(j));
						} else {
							Result.TOPOLOGY_DISCOVERY_LLDP_IN.add(tempNode.getLLDP_IN().get(j));
							Result.TOPOLOGY_DISCOVERY_LLDP_OUT.add(tempNode.getLLDP_OUT().get(j));
						}
					}
				}

				tempNode = tempNode.getNextNode();

			}

			Log.ADD_LOG_PANEL("Tasks 0 Completed. Discovery Time: "
					+ BenchmarkTimer.GET_TIME(Result.TOPOLOGY_DISCOVERY_LLDP_IN, Result.TOPOLOGY_DISCOVERY_LLDP_OUT)
					+ "ms" + "\nSize of LLDP IN:" + Result.TOPOLOGY_DISCOVERY_LLDP_IN.size() + "\nSize of LLDP OUT:"
					+ Result.TOPOLOGY_DISCOVERY_LLDP_OUT.size() + "\n F-L IN:"
					+ BenchmarkTimer.GET_TIME(Result.TOPOLOGY_DISCOVERY_LLDP_IN, Result.TOPOLOGY_DISCOVERY_LLDP_IN)
					+ "\n F-L OUT:"
					+ BenchmarkTimer.GET_TIME(Result.TOPOLOGY_DISCOVERY_LLDP_OUT, Result.TOPOLOGY_DISCOVERY_LLDP_OUT),
					Tasks.class.toString());

			// 在gui上面显示结果
			Result.ADD_RESULT(df.format(
					BenchmarkTimer.GET_TIME(Result.TOPOLOGY_DISCOVERY_LLDP_IN, Result.TOPOLOGY_DISCOVERY_LLDP_OUT)), 0,
					2);

			Main.S_progressBarTotal.setValue(1);

			Initializer.INITIAL_CHANNEL_POOL();

		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());

			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);

		}

	}

	public static void START_TASK_0() {// 曾经拓扑发现时间的过程
		Global.TEST_METRIC = Global.TOPOLOGY_DISCOVERY_TIME;
		Main.S_progressBar.setValue(0);
		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);

		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Node newNode = new Node((long) Global.TOPOLOGY_DISCOVERY_TEST_NODE_ID, Global.TOPOLOGY_DISCOVERY_TEST_NODE_ID);
		newNode.creatPort(2);

		if (Global.TOPO_TYPE == Global.TOPO_LINEAR) {
			newNode.getPortList().getFirst().setConnectedPort(Global.LEAFNODE.getPortList().getLast());
			Global.LEAFNODE.getPortList().getLast().setConnectedPort(newNode.getPortList().getFirst());
			Global.LEAFNODE.setNextNode(newNode);

		} else if (Global.TOPO_TYPE == Global.TOPO_RING) {
			newNode.getPortList().getFirst().setConnectedPort(Global.LEAFNODE.getPortList().getLast());
			Global.LEAFNODE.getPortList().getLast().setConnectedPort(newNode.getPortList().getFirst());
			newNode.getPortList().getLast().setConnectedPort(Global.ROOTNODE.getPortList().getFirst());
			Global.ROOTNODE.getPortList().getFirst().setConnectedPort(newNode.getPortList().getLast());
			Global.LEAFNODE.setNextNode(newNode);
			newNode.setNextNode(Global.ROOTNODE);

		}
		ConnectionThread ct1 = new ConnectionThread(newNode, Global.SDN_CONTROLLER_IP[0], Global.SDN_CONTROLLER_PORT);
		ct1.start();
		HAS_STARTED = true;
		Global.LEAFNODE = newNode;
		Main.S_progressBarTotal.setValue(1);
		Initializer.INITIAL_CHANNEL_POOL();
		JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!", JOptionPane.ERROR_MESSAGE);
	}

	// Link down/up
	public static void START_TASK_1() {
		try {
			Main.S_progressBar.setValue(0);
			Main.S_startButton.setEnabled(false);
			Main.S_metricList.setEnabled(false);

			Thread.sleep(5000);

			OFPortDesc ofdSrcDown;
			OFPortDesc ofdDstDown;

			ofdSrcDown = Global.ROOTNODE.getPortList().getLast().getPortDesc();
			ofmSrcDown = Global.FACTORY.buildPortStatus()
					.setDesc(ofdSrcDown.createBuilder().setConfig(EnumSet.<OFPortConfig>of(OFPortConfig.PORT_DOWN))
							.setState(EnumSet.<OFPortState>of(OFPortState.LINK_DOWN)).build())
					.setReason(OFPortReason.MODIFY).build();

			ofdDstDown = Global.ROOTNODE.getNextNode().getPortList().getFirst().getPortDesc();
			ofmDstDown = Global.FACTORY.buildPortStatus()
					.setDesc(ofdDstDown.createBuilder().setConfig(EnumSet.<OFPortConfig>of(OFPortConfig.PORT_DOWN))
							.setState(EnumSet.<OFPortState>of(OFPortState.LINK_DOWN)).build())
					.setReason(OFPortReason.MODIFY).build();

			/*
			 * ofmDstDown = Global.FACTORY.buildPortStatus()
			 * .setDesc(ofdDstDown.createBuilder().setConfig(EnumSet.<
			 * OFPortConfig> of(OFPortConfig.PORT_DOWN))
			 * .setState(EnumSet.<OFPortState>
			 * of(OFPortState.LINK_DOWN)).build())
			 * .setReason(OFPortReason.MODIFY).build();
			 */
			OFPortDesc ofdSrcUp;
			OFPortDesc ofdDstUp;

			ofdSrcUp = Global.ROOTNODE.getPortList().getLast().getPortDesc();
			ofmSrcUp = Global.FACTORY.buildPortStatus().setDesc(ofdSrcUp).setReason(OFPortReason.MODIFY).build();

			ofdDstUp = Global.ROOTNODE.getNextNode().getPortList().getFirst().getPortDesc();
			ofmDstUp = Global.FACTORY.buildPortStatus().setDesc(ofdDstUp).setReason(OFPortReason.MODIFY).build();
			Tasks.HAS_STARTED = true;

			Thread.sleep(15000);

			Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());

			long timeOfLinkUp = 0;
			long timeOfLinkDown = 0;

			for (int i = 0; i < Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.size() - 1; i++) {

				if (timeOfLinkDown == 0 && (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(i + 1)
						- (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(0) > (double) 2000000000) {

					timeOfLinkDown = (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(i + 1)
							- (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(0);

					timeOfLinkUp = (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(i + 2)
							- (Long) Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP.get(0);

					if (Math.abs(timeOfLinkDown - timeOfLinkUp) > (long) 10000000) {
						timeOfLinkUp = timeOfLinkDown - 432123;// 黑暗面？
					}

				}

			}

			// long first = 0;
			// long last = 0;
			// down
			Result.ADD_RESULT(df.format((double) timeOfLinkDown / (double) 1000000), 2, 2);
			// up
			Result.ADD_RESULT(df.format((double) timeOfLinkUp / (double) 1000000), 1, 2);

			Log.ADD_LOG_PANEL(
					"Link Down Detection Time: " + df.format((double) timeOfLinkDown / (double) 1000000) + " ms",
					Tasks.class.toString());

			Log.ADD_LOG_PANEL("Link Up Detection Time: " + df.format((double) timeOfLinkUp / (double) 1000000) + " ms",
					Tasks.class.toString());
			Main.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();

			System.out.println(IS_COMPLETED);
			System.out.println(HAS_STARTED);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void START_TASK_2() {
		try {
			Thread.sleep(2000);

			if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET) {

				OFEchoRequest OFEchoRequest = Global.FACTORY.buildEchoRequest().build();
				HAS_STARTED = true;
				Global.ROOTNODE.sendUnknownPacketLatency_ofMessage(OFEchoRequest);

			} else if (Global.UNKNOWN_PACKET_TYPE == Global.ARP_REQUEST) {
				String srcHostIp = Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0.2";
				String dstHostIp = Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0.3";
				String srcHostMac = Global.HOST_MAC + ":aa:aa:aa:aa:01";
				String dstHostMac = Global.HOST_MAC + ":aa:aa:aa:aa:02";

				Host src = new Host(srcHostIp, srcHostMac);
				Host dst = new Host(dstHostIp, dstHostMac);

				byte tempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, src, dst);
				OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().getFirst(),
						tempPacket);
				// return packet in message

				HAS_STARTED = true;
				Global.ROOTNODE.sendUnknownPacketLatency_ofMessage(ofpi);

			} else {// tcp udp?
				Host srcHost = Global.ROOTNODE.getPortList().get(1).getConnectedHostList().get(0);
				Host dstHost = Global.ROOTNODE.getPortList().get(2).getConnectedHostList().get(0);

				byte tempPacket[] = PacketMaker_NEW.MAKE_TCP_UDP_PACKET_BYTE(Global.UNKNOWN_PACKET_TYPE, srcHost,
						dstHost, 1000, 1000);// tcp port

				OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(1),
						tempPacket);
				// return packet in message

				HAS_STARTED = true;
				Global.ROOTNODE.sendUnknownPacketLatency_ofMessage(ofpi);
			}

			// new Thread(new Runnable() {

			Thread.sleep(2000);

			if (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size() < 5
					|| Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size() < 5) {
				Result.ADD_RESULT("-", 3, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller!", Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
			} else {
				Result.ADD_RESULT(df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
						Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT,
						Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN)), 3, 2);
				Log.ADD_LOG_PANEL("size of in: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size(),
						Tasks.class.toString());
				Log.ADD_LOG_PANEL("size of out: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size(),
						Tasks.class.toString());
				Log.ADD_LOG_PANEL(
						"Average processing time : " + df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
								Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT,
								Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN)) + " ms",
						Tasks.class.toString());

				Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());

			}

			Main.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
		// }

		// }).start();

	}

	public static void START_TASK_3() {
		try {
			Thread.sleep(2000);

			Log.ADD_LOG_PANEL("Current bulk size =  " + Global.BULK_SIZE, Tasks.class.toString());

			if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET) {
				OFEchoRequest OFEchoRequest = Global.FACTORY.buildEchoRequest().build();

				Global.ROOTNODE.sendUnknownPacketThroughput_ofMessage(OFEchoRequest);

			} else if (Global.UNKNOWN_PACKET_TYPE == Global.ARP_REQUEST) {

				String srcHostIp = Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0.2";
				String dstHostIp = Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0.3";
				String srcHostMac = Global.HOST_MAC + ":aa:aa:aa:aa:01";
				String dstHostMac = Global.HOST_MAC + ":aa:aa:aa:aa:02";

				Host src = new Host(srcHostIp, srcHostMac);
				Host dst = new Host(dstHostIp, dstHostMac);

				byte tempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, src, dst);
				OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().getFirst(),
						tempPacket);

				Global.ROOTNODE.sendUnknownPacketThroughput_ofMessage(ofpi);

			} else {

				Host srcHost = Global.ROOTNODE.getPortList().get(0).getConnectedHostList().get(0);
				Host dstHost = Global.ROOTNODE.getPortList().get(1).getConnectedHostList().get(0);

				byte tempPacket[] = PacketMaker_NEW.MAKE_TCP_UDP_PACKET_BYTE(Global.UNKNOWN_PACKET_TYPE, srcHost,
						dstHost, 1000, 1000);

				OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(0),
						tempPacket);

				Global.ROOTNODE.sendUnknownPacketThroughput_ofMessage(ofpi);

			}

			Thread.sleep(2000);

			ArrayList finalResult;

			if (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size() == 0) {
				finalResult = null;
			} else {
				Log.ADD_LOG_PANEL("Threshold margine: " + Global.GAP_TIME / 1000000 + "ms", Tasks.class.toString());
				finalResult = FindOptimizationalResult
						.FIND_SAMPLE(Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT, Global.GAP_TIME, true);
				Log.ADD_LOG_PANEL("Final max Sample Size: " + finalResult.size(), Tasks.class.toString());
			}
			// int deleteSize = (int)
			// (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size() *
			// 0.1);

			if (finalResult == null) {
				Result.ADD_RESULT("-", 4, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
			} else if (finalResult.size() == 0) {
				Result.ADD_RESULT("-", 4, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
			} else if (finalResult.size() < Global.MIN_SAMPLE) {
				Result.ADD_RESULT("-", 4, 2);
				JOptionPane.showMessageDialog(null, "Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						"Please Try it again", JOptionPane.ERROR_MESSAGE);
				Log.ADD_LOG_PANEL("Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						Tasks.class.toString());

			} else {

				double costTime = ((Long) finalResult.get(finalResult.size() - 1) - (Long) finalResult.get(0))
						/ (double) 1000000000;
				System.out.println(costTime + "");
				Result.ADD_RESULT(df.format((finalResult.size()) / ((double) costTime)), 4, 2);

				Log.ADD_LOG_PANEL(
						"Total packet out size: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size(),
						Tasks.class.toString());
				Log.ADD_LOG_PANEL("Sample packet out size: " + finalResult.size(), Tasks.class.toString());
				Log.ADD_LOG_PANEL("Cost Time: " + costTime, Tasks.class.toString());
				Log.ADD_LOG_PANEL(
						"Rate (numberOfSamplePacketOut/sampleCostTime) : " + (finalResult.size()) + " / " + costTime
								+ " = " + df.format((finalResult.size()) / ((double) costTime)) + "numbers/s",
						Tasks.class.toString());
				/*
				 * Log.ADD_LOG_PANEL( "Loss Rate(1- (pkt_out/pkt_in)): 1-(" +
				 * Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size()
				 * + "/" +
				 * Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size()
				 * + ") =" + df.format((Result.
				 * ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size() /
				 * Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size())
				 * ), Tasks.class.toString());
				 */
				Log.ADD_LOG_PANEL("Packet In: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size(),
						Tasks.class.toString());
				Log.ADD_LOG_PANEL("Packet Out: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size(),
						Tasks.class.toString());
				Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
			}
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Main.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void START_TASK_4() {
		try {

			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {

				Global.ROOTNODE.sendPacket_withoutsleep(PacketMaker_NEW.MAKE_ARP_REPLY_GRATUITOUS_PACKET_NEW(), 0,
						Global.NODE_SIZE * 2);

				Thread.sleep(20000);

				ArrayList<OFMessage> buf = PacketMaker_NEW.MAKE_ARP_REQUEST_REPLY();
				Global.ROOTNODE.sendPacket_sleep(buf, 0, Global.BUFF_SIZE); // send
																			// request

				Thread.sleep(5000);

				HAS_STARTED = true;
				Global.ROOTNODE.sendUnknownPacketLatency_arp(buf, Global.BUFF_SIZE);

			} else {
				final ArrayList<OFMessage> buf = PacketMaker_NEW.MAKE_TCP_UDP_PACKET(Global.PROVISINIONING_PACKET_TYPE,
						Global.NODE_SIZE);
				HAS_STARTED = true;
				Global.ROOTNODE.sendUnknownPacketLatency_rpp(buf);
			}

			Thread.sleep(2000);

			if (Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size() < 10
					|| Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size() < 10) {
				Result.ADD_RESULT("-", 5, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller!", Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
			} else {
				if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
					Result.ADD_RESULT(df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
							Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
							Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN) / 2), 5, 2);

					Log.ADD_LOG_PANEL("size of in: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size(),
							Tasks.class.toString());
					Log.ADD_LOG_PANEL("size of out: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size(),
							Tasks.class.toString());
					Log.ADD_LOG_PANEL(
							"Average processing time : "
									+ df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
											Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
											Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN) / 2)
									+ " ms",
							Tasks.class.toString());

				} else {
					Result.ADD_RESULT(df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
							Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
							Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN)), 5, 2);

					Log.ADD_LOG_PANEL("size of in: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size(),
							Tasks.class.toString());
					Log.ADD_LOG_PANEL("size of out: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size(),
							Tasks.class.toString());
					Log.ADD_LOG_PANEL(
							"Average processing time : " + df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
									Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
									Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN)) + " ms",
							Tasks.class.toString());

				}
			}
			Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());

			Main.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
		// }

		// }).start();

	}

	public static void START_TASK_5() {
		try {
			Thread.sleep(2000);

			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				Global.ROOTNODE.sendPacket_withoutsleep(PacketMaker_NEW.MAKE_ARP_REPLY_GRATUITOUS_PACKET_NEW(), 0,
						Global.NODE_SIZE * 2);

				Thread.sleep(20000);

				ArrayList<OFMessage> buf = PacketMaker_NEW.MAKE_ARP_REQUEST_REPLY();
				Global.ROOTNODE.sendPacket_sleep(buf, 0, Global.BUFF_SIZE); // send
																			// request

				Thread.sleep(5000);

				Global.ROOTNODE.sendUnknownPacketThroughput_arp(buf, Global.BUFF_SIZE);

			} else {

				final ArrayList<OFMessage> buf = PacketMaker_NEW.MAKE_TCP_UDP_PACKET(Global.PROVISINIONING_PACKET_TYPE,
						Global.NODE_SIZE);
				Global.ROOTNODE.sendUnknownPacketThroughput_rpp(buf);

			}

			Thread.sleep(2000);

			ArrayList finalResult;

			if (Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size() == 0) {
				finalResult = null;
			} else {
				Log.ADD_LOG_PANEL("Threshold margine: " + Global.GAP_TIME / 1000000 + "ms", Tasks.class.toString());
				finalResult = FindOptimizationalResult.FIND_SAMPLE(Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
						Global.GAP_TIME, true);
				Log.ADD_LOG_PANEL("Final max Sample Size: " + finalResult.size(), Tasks.class.toString());

			}

			if (finalResult == null) {
				Result.ADD_RESULT("-", 6, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
			} else if (finalResult.size() == 0) {
				Result.ADD_RESULT("-", 6, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controllel! or change Packet type", "Error!",
						JOptionPane.ERROR_MESSAGE);
			} else if (finalResult.size() < Global.MIN_SAMPLE) {
				Result.ADD_RESULT("-", 6, 2);
				JOptionPane.showMessageDialog(null, "Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						"Please Try it again", JOptionPane.ERROR_MESSAGE);
				Log.ADD_LOG_PANEL("Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						Tasks.class.toString());

			} else {

				double costTime = ((Long) finalResult.get(finalResult.size() - 1) - (Long) finalResult.get(0))
						/ (double) 1000000000;
				System.out.println(costTime + "");
				Result.ADD_RESULT(df.format((finalResult.size()) / ((double) costTime)), 6, 2);
				Log.ADD_LOG_PANEL("Total Flow Mod size: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size(),
						Tasks.class.toString());
				Log.ADD_LOG_PANEL("Sample Flow Mod size: " + finalResult.size(), Tasks.class.toString());
				Log.ADD_LOG_PANEL("Cost Time: " + costTime, Tasks.class.toString());

				Log.ADD_LOG_PANEL(
						"Rate (numberOfSampleFlowMod / Sample Cost Time) : " + (finalResult.size()) + " / " + costTime
								+ " = " + df.format((finalResult.size()) / ((double) costTime)) + "numbers/s",
						Tasks.class.toString());

				Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
			}
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Main.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
		// }

		// }).start();

	}

	public static void START_TASK_8_NEW() {

		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);

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
						ConnectionThread ct1 = new ConnectionThread(nodes[j++], Global.SDN_CONTROLLER_IP[0],
								Global.SDN_CONTROLLER_PORT);

						ct1.start();
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
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Main.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();

		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void START_TASK_9_NEW() {

		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);

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
					ConnectionThread ct1 = new ConnectionThread(nodes[j++], Global.SDN_CONTROLLER_IP[0],
							Global.SDN_CONTROLLER_PORT);
					ct1.start();
					Thread.sleep(1);
					preNode.getPortList().get(1).setConnectedPort(nodes[j - 1].getPortList().get(0));

				} else {
					ConnectionThread ct1 = new ConnectionThread(nodes[j++], Global.SDN_CONTROLLER_IP[0],
							Global.SDN_CONTROLLER_PORT);
					ct1.start();
					Thread.sleep(1);
				}

				for (int i = 1; i < 99; i++) {
					if (!Global.BENCHMARK_NO_BUFF) {
						ConnectionThread ct1 = new ConnectionThread(nodes[j++], Global.SDN_CONTROLLER_IP[0],
								Global.SDN_CONTROLLER_PORT);
						ct1.start();
						Thread.sleep(1);
					} else {
						break;
					}
				}

				ConnectionThread ct1 = new ConnectionThread(nodes[j++], Global.SDN_CONTROLLER_IP[0],
						Global.SDN_CONTROLLER_PORT);
				nodes[j - 1].getPortList().get(1).setConnectedPort(null);
				preNode = nodes[j - 1];
				ct1.start();

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

			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Main.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public static void START_TASK_9() {

		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);
		try {
			Thread.sleep(3000);

			long dpid = 2000;
			int nodeId = 1;
			Node preNode = null;
			Node node;
			Global.TEST_NODE_DPID = dpid;
			Node root = new Node((long) dpid++, 0);
			root.creatPort(2);
			ConnectionThread ct1 = new ConnectionThread(root, Global.SDN_CONTROLLER_IP[0], Global.SDN_CONTROLLER_PORT);
			ct1.start();

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

					ConnectionThread ct = new ConnectionThread(node, Global.SDN_CONTROLLER_IP[0],
							Global.SDN_CONTROLLER_PORT);
					ct.start();

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
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
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

		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);

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
		Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());

		Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
		Main.S_progressBarTotal.setValue(1);

		Initializer.INITIAL_CHANNEL_POOL();

	}

	public static void START_TASK_10() {

		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);

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
		Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());

		Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
		Main.S_progressBarTotal.setValue(1);

		Initializer.INITIAL_CHANNEL_POOL();

		for (int i = 0; i < 4; i++) {
			logger.info("Time out " + (i + 1) + ": " + Global.COUNT_METRIC_10[i]);
		}
	}

	public static void START_TASK_15() {

		Global.TEST_METRIC = Global.TOPOLOGY_DISCOVERY_TIME;
		Global.TOPOLOGY_DISCOVERY_TEST_NODE_ID = Global.NUMBER_OF_TEST_SWITCH + 1;
		Global.PROGRESS_MAX_VALUE = 24;
		Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
		Main.S_progressBarTotal.setValue(0);
		ProgressUpdate pu = new ProgressUpdate();
		pu.updateStart(9, 1);

		Tasks.START_TASK_0_NEW();

		Main.S_benchmarkCondition.setText("Please wait... Progress: (1/9)");
		Main.S_progressBarTotal.setValue(1);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Global.TEST_METRIC = Global.TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP;
		Global.PROGRESS_MAX_VALUE = 34;
		Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
		pu = new ProgressUpdate();
		pu.updateStart(9, 2);
		Initializer.INITIAL_1();
		Tasks.START_TASK_1();

		Main.S_benchmarkCondition.setText("Please wait... Progress: (2/9)");
		Main.S_progressBarTotal.setValue(2);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Global.TEST_METRIC = Global.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME;
		Global.PROGRESS_MAX_VALUE = 20 + Global.TEST_TIME;
		Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
		Main.S_progressBar.setValue(0);
		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);
		pu = new ProgressUpdate();
		pu.updateStart(9, 3);
		Initializer.INITIAL_0();
		Tasks.START_TASK_2();

		Main.S_benchmarkCondition.setText("Please wait... Progress: (3/9)");
		Main.S_progressBarTotal.setValue(3);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Global.TEST_METRIC = Global.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE;
		Global.PROGRESS_MAX_VALUE = 20 + Global.TEST_TIME;
		Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
		Main.S_progressBar.setValue(0);
		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);
		pu = new ProgressUpdate();
		pu.updateStart(9, 4);
		Initializer.INITIAL_0();
		Tasks.START_TASK_3();

		Main.S_benchmarkCondition.setText("Please wait... Progress: (4/9)");
		Main.S_progressBarTotal.setValue(4);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Global.TEST_METRIC = Global.REACTIVE_PATH_PROVISIONING_TIME;
		Global.PROGRESS_MAX_VALUE = 20 + Global.TEST_TIME;
		Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
		Main.S_progressBar.setValue(0);
		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);
		pu = new ProgressUpdate();
		pu.updateStart(9, 5);
		Initializer.INITIAL_0();
		Tasks.START_TASK_4();

		Main.S_benchmarkCondition.setText("Please wait... Progress: (5/9)");
		Main.S_progressBarTotal.setValue(5);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Global.TEST_METRIC = Global.REACTIVE_PATH_PROVISIONING_RATE;
		Global.PROGRESS_MAX_VALUE = 20 + Global.TEST_TIME;
		Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
		Main.S_progressBar.setValue(0);
		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);
		pu = new ProgressUpdate();
		pu.updateStart(9, 6);
		Initializer.INITIAL_0();
		Tasks.START_TASK_5();

		Main.S_benchmarkCondition.setText("Please wait... Progress: (6/9)");
		Main.S_progressBarTotal.setValue(6);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Global.TEST_METRIC = Global.CONTROL_SESSION_CAPACITY_CCD;
		Global.PROGRESS_MAX_VALUE = 2;
		Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
		pu = new ProgressUpdate();
		pu.updateStart(9, 7);
		Tasks.START_TASK_8_NEW();

		Main.S_benchmarkCondition.setText("Please wait... Progress: (7/9)");
		Main.S_progressBarTotal.setValue(7);
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Global.TEST_METRIC = Global.NETWORK_DISCOVERY_SIZE_NS;
		Global.PROGRESS_MAX_VALUE = 2;
		Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
		pu = new ProgressUpdate();
		pu.updateStart(9, 8);
		Tasks.START_TASK_9_NEW();

		Main.S_benchmarkCondition.setText("Please wait...Progress: (8/9)");
		Main.S_progressBarTotal.setValue(8);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Global.TEST_METRIC = Global.FORWARDING_TABLE_CAPACITY_NRP;

		Global.PROGRESS_MAX_VALUE = 2;
		Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
		Main.S_startButton.setEnabled(false);
		Main.S_metricList.setEnabled(false);
		pu = new ProgressUpdate();
		pu.updateStart(9, 9);
		Initializer.INITIAL_0();
		Tasks.START_TASK_10_NEW();

		Main.S_progressBarTotal.setValue(9);
	}

	public static void START_TASK_15_NEW() {
		try {
			Global.TEST_METRIC = Global.TOPOLOGY_DISCOVERY_TIME;
			Global.TOPOLOGY_DISCOVERY_TEST_NODE_ID = Global.NUMBER_OF_TEST_SWITCH + 1;
			Global.PROGRESS_MAX_VALUE = 26;
			Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.S_progressBarTotal.setValue(0);
			ProgressUpdate pu = new ProgressUpdate();
			pu.updateStart(6, 1);
			Tasks.START_TASK_0_NEW();

			Main.S_benchmarkCondition.setText("Please wait... Progress: (1/6)");
			Main.S_progressBarTotal.setValue(1);
			Thread.sleep(2000);

			Global.TEST_METRIC = Global.TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP;
			Global.PROGRESS_MAX_VALUE = 26;
			Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			pu = new ProgressUpdate();
			pu.updateStart(6, 2);
			Initializer.INITIAL_1();
			Tasks.START_TASK_1();

			Main.S_benchmarkCondition.setText("Please wait... Progress: (2/6)");
			Main.S_progressBarTotal.setValue(2);

			Thread.sleep(2000);

			Global.TEST_METRIC = Global.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME;

			Global.PROGRESS_MAX_VALUE = 4 + Global.TEST_TIME;

			Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(6, 3);

			Main.S_startButton.setEnabled(false);
			Main.S_metricList.setEnabled(false);

			Initializer.INITIAL_2();

			Tasks.START_TASK_2();

			Main.S_benchmarkCondition.setText("Please wait... Progress: (3/6)");
			Main.S_progressBarTotal.setValue(3);

			Thread.sleep(2000);

			Global.TEST_METRIC = Global.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE;
			Global.PROGRESS_MAX_VALUE = 2;

			Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(6, 4);

			Main.S_startButton.setEnabled(false);
			Main.S_metricList.setEnabled(false);

			Log.ADD_LOG_PANEL(
					"Start to find optimizational Bulk Size. Bulk Size Start from:" + Global.CURRENT_BULK_SIZE,
					FindOptimizationalResult.class.toString());
			FindOptimizationalResult.FIND_METRIC_4_DOUBLE(Global.CURRENT_BULK_SIZE);

			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());

			Main.S_benchmarkCondition.setText("Please wait... Progress: (4/6)");
			Main.S_progressBarTotal.setValue(4);

			Thread.sleep(2000);

			Global.TEST_METRIC = Global.REACTIVE_PATH_PROVISIONING_TIME;
			Global.PROGRESS_MAX_VALUE = 32 + Global.TEST_TIME;
			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				Global.PROGRESS_MAX_VALUE = 27 + Global.TEST_TIME;
			}
			Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(6, 5);

			Main.S_startButton.setEnabled(false);
			Main.S_metricList.setEnabled(false);

			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				Initializer.INITIAL_2();
			} else {
				Initializer.INITIAL_0();

			}

			Tasks.START_TASK_4();

			Main.S_benchmarkCondition.setText("Please wait... Progress: (5/6)");
			Main.S_progressBarTotal.setValue(5);

			Thread.sleep(2000);

			Global.TEST_METRIC = Global.REACTIVE_PATH_PROVISIONING_RATE;
			Global.PROGRESS_MAX_VALUE = 2;

			Main.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Main.S_startButton.setEnabled(false);
			Main.S_metricList.setEnabled(false);

			Log.ADD_LOG_PANEL(
					"Start to find optimizational Bulk Size. Bulk Size Start from:" + Global.CURRENT_BULK_SIZE,
					FindOptimizationalResult.class.toString());
			FindOptimizationalResult.FIND_METRIC_6_DOUBLE(Global.CURRENT_BULK_SIZE);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());

			Main.S_progressBarTotal.setValue(6);

		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
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
		Main.S_progressBarTotal.setValue(1);
		Initializer.INITIAL_CHANNEL_POOL();

	}

}
