package com.mirlab.global;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.json.JSONObject;
import org.projectfloodlight.openflow.protocol.OFFactories;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFVersion;

import com.mirlab.component.DistributedPort;
import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.enumType.Controller;
import com.mirlab.enumType.ControllerMode;
import com.mirlab.enumType.SouthboundMetric;
import com.mirlab.enumType.TopologyType;

public class Global {
	public static long GAP_TIME = 10;
	public static int MIN_SAMPLE = 500;
	public static int MAX_SAMPLE = 35000;
	public static int CURRENT_BULK_SIZE = 1;
	public static int PRE_BULK_SIZE = 50;
	public static int BULK_SIZE_OFF_SET = 0;
	public static double PACKET_LOSS_RATE = 0.05;

	public static int BULK_SIZE = 50;

	public static String[] SDN_CONTROLLER_IP = { "192.168.1.103", "166.104.28.130", "166.104.28.131" };
	public static int SDN_CONTROLLER_PORT = 6633;
	public static long TEST_NODE_DPID = 00000000001l;
	public static int PROGRESS_MAX_VALUE = 1;
	public static boolean IS_MASTER = true;
	public static boolean IS_ENABLE_DISTRIBUTED = false;

	public static String MASTER_IP = "127.0.0.1";
	public static int MASTER_PORT = 8911;
	public static Channel MASTER_CHANNEL = null;

	public static int[] HOST_IP = { 0, 1 };
	public static String HOST_MAC = "aa";
	public static int SWITCH_ID_OFF_SET = 100;

	/************************* for slaves */
	public static int MY_ID = -1;
	public static String[] SLAVES_HOST_MAC = { "ab", "ac", "ad" };
	public static int[] SLAVES_ID_OFF_SET = { 10000, 20000, 30000 };

	/*************************/

	public static long HELLO_THRESHOLD = 5000000000l; // nano second
	public static final long PACKET_TIMEOUT_THRESHOLD = 5l;
	public static ArrayList<ChannelFuture> CHANNEL_POOL = new ArrayList<ChannelFuture>();
	public static ArrayList<ClientBootstrap> CHANNEL_BOOTSTRAP_POOL = new ArrayList<ClientBootstrap>();

	public static boolean CONTROLLER_IS_OPEN = false;
	public static long TEST_INIT_TIME = 0;
	public static boolean BENCHMARK_NO_BUFF = false;
	public static final int WARMING_TIME = 0;
	public static OFFactory FACTORY = OFFactories.getFactory(OFVersion.OF_13);

	/************************/
	public static final int THRESHOLD_THREADCONTROL_SESSION_CAPACITY_CCD = 10000;
	public static final int THRESHOLD_NETWORK_DISCOVERY_SIZE_NS = 10000;
	public static final int THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP = 5000;
	public static final int MULTIPLE = 2;
	/************************/
	public static int COUNT_METRIC_8[] = { 0, 0, 0, 0 };
	public static int COUNT_METRIC_9[] = { 0, 0, 0, 0 };
	public static int COUNT_METRIC_10[] = { 0, 0, 0, 0 };

	/************************/
	public static SouthboundMetric southboundMetric;
	public static OFVersion ofVersion;
	public static TopologyType topoType;
	public static ControllerMode conMode;
	public static Controller conTroller;
//	public static EthType ethType; 
	
	public static int NUMBER_OF_TEST_SWITCH = 0;
	public static int NUMBER_OF_PORT = 0;
	public static int NUMBER_OF_TEST_HOST_PER_SWITCH = 0;

	public static int TEST_TIME = 0;
	public static int TEST_TYPE = -1;
	public static int TOPOLOGY_DISCOVERY_TEST_NODE_ID = -1;
	public static int UNKNOWN_PACKET_TYPE = -1;
	public static int PROVISINIONING_PACKET_TYPE = -1;

	
	
	public static Node ROOTNODE = null;
	public static Node LEAFNODE = null;
	public static int NODE_SIZE = 100; // 1012 ??
	public static int BUFF_SIZE = 19999;
	// public static int NODE_SIZE = 100; // 1012
	// public static int BUFF_SIZE = 10000;
	public static int BUFF_INDEX = 0;

	/***********************/
	public static final int IP_PACKET = 0;
	public static final int TCP_PACKET = 6;
	public static final int UDP_PACKET = 17;
	public static final int ECHO_PACKET = 18;
	public static final int ARP_REQUEST = 1;
	public static final int ARP_REPLY = 2;
	public static final int ARP_REPLY_GRATUITOUS = 3;

	/*******************************/
	public static JSONObject JSONMSG = null;
	public static Node[] nodes = null;

	public static Host[] srcHosts = null;
	public static Host[] dstHosts = null;

	public static int count = -1;

	/******************************** 20181018 distributed tool */
	public static ArrayList<String> distribudtedTool_IP_List = new ArrayList<String>();

	public static ArrayList<String> distribudtedTool_IP = new ArrayList<String>();

	public static ConcurrentMap<String, Integer> distribudtedTool_Port = new ConcurrentHashMap<String, Integer>();
	public static ConcurrentMap<String, Integer> distribudtedToolSever_Port = new ConcurrentHashMap<String, Integer>();

	public static int[] distribudtedTool_PortList = { 16000, 16001 };

	public static ArrayList<DistributedPort> distributedPortList = new ArrayList<>();

	public static String local_IP;
}
