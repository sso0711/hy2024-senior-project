package com.mirlab.lib;

import java.util.ArrayList;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Main;
import com.mirlab.component.DistributedPort;
import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.enumType.SouthboundMetric;
import com.mirlab.enumType.TopologyType;
import com.mirlab.global.Global;
import com.mirlab.openflow.OpenFlowClient;

public class Initializer {

	public static Logger logger = LoggerFactory.getLogger(Initializer.class);

	public static void INITIAL_CHANNEL_POOL() {

		OpenFlowClient.bootstrap.shutdown(); // all client state is shutdown 
		Global.CHANNEL_BOOTSTRAP_POOL = new ArrayList<ClientBootstrap>();
		Global.CHANNEL_POOL = new ArrayList<ChannelFuture>();
		Tasks.HAS_STARTED = false;
		Tasks.HAS_LINK_UP = false;
		Tasks.IS_COMPLETED = false;

		Global.BUFF_SIZE = 10000;

		Global.BUFF_INDEX = 0;
		Global.ROOTNODE = null;
		Global.LEAFNODE = null;
		Global.BENCHMARK_NO_BUFF = false;
		Global.BUFF_INDEX = 0;
		PacketHandler.COUNT = 0;

		if (Global.distributedPortList.size() > 0) {
			for (DistributedPort distributedPort : Global.distributedPortList) {
				distributedPort.closeClient();
				distributedPort.closeServer();

			}
		}

		//for WR_proactive 
		
		// for recording test 0
		Result.TOPOLOGY_DISCOVERY_TIME_LIST = new ArrayList();

		// for new test 0 new
		Result.TOPOLOGY_DISCOVERY_LLDP_OUT = new ArrayList();
		Result.TOPOLOGY_DISCOVERY_LLDP_IN = new ArrayList();

		// for recording test 1
		Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_PORTSTATUS = new ArrayList();
		Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP = new ArrayList();

		// for recording test 2 3
		Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN = new ArrayList();
		Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT = new ArrayList();

		// for recording test 4 5
		Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN = new ArrayList();
		Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD = new ArrayList();

		// for recording test 6 7

		// for recording test 8
		Result.NUMBER_OF_OUT_HELLO = new ArrayList();
		Result.NUMBER_OF_IN_HELLO = new ArrayList();

		// for 11
		Result.TEMP_NUMBER_OF_FLOW_MOD = 0;
		Result.TOTAL_NUMBER_OF_FLOW_MOD = 0;

		Main.gui.S_startButton.setEnabled(true);
		Main.gui.S_metricList.setEnabled(true);
		
		Main.gui.SN_startButton.setEnabled(true);
		Main.gui.SN_metricList.setEnabled(true);
		
		Main.gui.N_startButton.setEnabled(true);
		Main.gui.N_metricList.setEnabled(true);
		Global.PROGRESS_MAX_VALUE = 0;

		OpenFlowClient.bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newSingleThreadExecutor(), Executors.newCachedThreadPool()));

	}

	public static void INITIAL_0() {
		creatTopoOneNode();
		creatHost1();
	}

	public static void INITIAL_2() {
		creatTopoOneNode();

	}

	public static void INITIAL_1() {
		creatTopoWithoutHost();
	}

	public static void TEST_CONTROLLER_IF_IT_IS_OPEN() {
		new Thread(new Runnable() {

			public void run() {
				Node rootNode = new Node((long) (1121199 + Global.SWITCH_ID_OFF_SET), 0);
				rootNode.start_OpenFlowClient();
			}
		}).start();

	}

	public static void creatHost1() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Global.ROOTNODE.sendPacket(PacketMaker_NEW.MAKE_ARP_REPLY_GRATUITOUS_PACKET_NEW());

		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.ADD_LOG_PANEL("Sending ARP(Gratuitous) Completed.", Initializer.class.toString());
	}

	private static void creatTopoOneNode() {
		Node rootNode;
		if (Global.southboundMetric == SouthboundMetric.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME) {
			rootNode = new Node((long) (1 + Global.SWITCH_ID_OFF_SET), 0);
		} else if (Global.southboundMetric == SouthboundMetric.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE) {
			rootNode = new Node((long) (2 + Global.SWITCH_ID_OFF_SET), 0);
		} else if (Global.southboundMetric == SouthboundMetric.REACTIVE_PATH_PROVISIONING_TIME) {
			rootNode = new Node((long) (3 + Global.SWITCH_ID_OFF_SET), 0);
		} else if (Global.southboundMetric == SouthboundMetric.REACTIVE_PATH_PROVISIONING_RATE) {
			rootNode = new Node((long) (4 + Global.SWITCH_ID_OFF_SET), 0);
		} else {
			rootNode = new Node((long) (5 + Global.SWITCH_ID_OFF_SET), 0);
		}

		rootNode.creatPort(3);

		for (int i = 1; i < Global.NODE_SIZE + 1; i++) {

			// add host
			Host h1;
			if (i < 16) {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (i + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + "0" + Integer.toHexString(i));
			} else {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (i + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + Integer.toHexString(i));
			}

			rootNode.getPortList().get(1).addConnectedHostToList(h1);

		}

		// other port

		for (int i = 1; i < Global.NODE_SIZE + 1; i++) {

			// add host
			Host h5;
			if (i < 16) {
				h5 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".1." + (i + 1),
						Global.HOST_MAC + ":aa:aa:aa:ae:" + "0" + Integer.toHexString(i));
			} else {
				h5 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".1." + (i + 1),
						Global.HOST_MAC + ":aa:aa:aa:ae:" + Integer.toHexString(i));
			}

			rootNode.getPortList().get(2).addConnectedHostToList(h5);

		}

		rootNode.start_OpenFlowClient();
		Global.ROOTNODE = rootNode;

	}

	private static void creatTopoWithoutHost() {
		long dpid = (long) Global.SWITCH_ID_OFF_SET;
		Node preNode = null;
		Node node;
		Global.NUMBER_OF_PORT = 2;
		Node root = new Node((long) dpid++, 0);
		root.creatPort(2);
		root.start_OpenFlowClient();

		preNode = root;

		for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {

			node = new Node((long) dpid++, i);
			node.creatPort(2);

			node.start_OpenFlowClient();

			node.getPortList().get(0).setConnectedPort(preNode.getPortList().get(1));
			preNode.getPortList().get(1).setConnectedPort(node.getPortList().get(0));
			preNode.setNextNode(node);
			preNode = node;
		}

		if (Global.topoType == TopologyType.RING) {
			preNode.getPortList().get(1).setConnectedPort(root.getPortList().get(0));
			root.getPortList().get(0).setConnectedPort(preNode.getPortList().get(1));
			preNode.setNextNode(root);
		} else {
			preNode.setNextNode(null);
		}

		Global.ROOTNODE = root;
		Global.LEAFNODE = preNode;
		Log.ADD_LOG_PANEL("Creating Topology Completed.", Initializer.class.toString());

	}

	private static void creatTopo() {
		long dpid = 1;
		Node preNode = null;
		Node node;
		Global.NUMBER_OF_PORT = 3;
		Node root = new Node((long) dpid++, 0);

		root.creatPort(3);
		// add host
		Host h;
		h = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0.1", "aa:aa:aa:aa:aa:01");
		root.getPortList().getLast().addConnectedHostToList(h);
		h.setConnectedNode(root);
		// end add host

		root.start_OpenFlowClient();

		preNode = root;

		for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {

			node = new Node((long) dpid++, i);
			node.creatPort(3);

			// add host
			Host h1;
			if (i < 10) {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (i + 1),
						"aa:aa:aa:aa:aa:" + "0" + (i + 1));
			} else {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (i + 1),
						"aa:aa:aa:aa:aa:" + (i + 1));
			}
			node.getPortList().getLast().addConnectedHostToList(h1);
			h1.setConnectedNode(node);
			// end add host

			node.start_OpenFlowClient();

			node.getPortList().get(0).setConnectedPort(preNode.getPortList().get(1));
			preNode.getPortList().get(1).setConnectedPort(node.getPortList().get(0));
			preNode.setNextNode(node);
			preNode = node;
		}

		if (Global.topoType == TopologyType.RING) {
			preNode.getPortList().get(1).setConnectedPort(root.getPortList().get(0));
			root.getPortList().get(0).setConnectedPort(preNode.getPortList().get(1));
			preNode.setNextNode(root);
		} else {
			preNode.setNextNode(null);
		}

		// PacketHandler.HANDEL_HELLO_PACKET();

		Global.ROOTNODE = root;
		Global.LEAFNODE = preNode;
		Log.ADD_LOG_PANEL("Creating Topology Completed.", Initializer.class.toString());

	}

}
