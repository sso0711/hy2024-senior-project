package com.mir.ui;

import java.io.IOException;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.Main;
import com.mir.distributed.message.DistributedMessage;
import com.mir.distributed.server.DistributedServer;
import com.mirlab.component.DistributedPort;
import com.mirlab.enumType.MetricType;
import com.mirlab.global.Global;
import com.mirlab.lib.IP;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.Tasks;
import com.mirlab.metric.southbound.AsynchronousMessageProcessingRate;
import com.mirlab.metric.southbound.AsynchronousMessageProcessingTime;
import com.mirlab.metric.southbound.ForwardingTableCapacityNrp;
import com.mirlab.metric.southbound.ReactivePathProvisioningRate;
import com.mirlab.metric.southbound.ReactivePathProvisioningTime;
import com.mirlab.metric.southbound.TestTotal;
import com.mirlab.metric.southbound.TopologyChangeDetection;
import com.mirlab.metric.southbound.TopologyDiscovery;
import com.mirlab.metric.northbound.AsyncClientProactiveProRate;
import com.mirlab.metric.northbound.AsyncForwdTabCapacity1;
import com.mirlab.metric.northbound.AsyncProactiveProTime;
import com.mirlab.metric.northbound.SsyncProactiveProTime;
import com.mirlab.metric.northbound.WRNetworkDiscoverySize;

import com.mir.ui.N_ProgressUpdate;

/****************** northbound */



import io.netty.channel.Channel;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年8月7日 下午12:48:57 类说明
 */
public class N_RunButton {

	private static DistributedMessage distributedMessage = DistributedMessage.INSTANCE;

	public static void go() {

		boolean controller_isOpen = IP.isHostConnectable(Global.SDN_CONTROLLER_IP[0], Global.SDN_CONTROLLER_PORT, 1000);

		if (controller_isOpen) {// controller is open

			checkDistributed();

			startTasks();

		} else {// controller offline

			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Please check if controller is running.", "Disconnected",
					JOptionPane.ERROR_MESSAGE);// 跳出新窗口

		}

	}

	private static void checkDistributed() {// 把所有的变数全都放到json格式变量里面

		DistributedServer distributedServer = DistributedServer.getInstance();

		if (Global.IS_ENABLE_DISTRIBUTED && Global.IS_MASTER) {

			JSONObject msgType1 = distributedMessage.msgType1();

			for (Channel channel : distributedServer.channelMap.values()) {
				channel.writeAndFlush(msgType1.toString());
			}

			// ******************************

			int listSize = Global.distribudtedTool_IP_List.size();
			int distributedPortCount = Global.distribudtedTool_PortList.length;

			String IP1 = Global.distribudtedTool_IP_List.get((listSize - 1) % listSize);

			Global.distribudtedTool_IP.add(IP1);

			int tool_port1 = Global.distribudtedTool_PortList[1 % distributedPortCount];
			Global.distribudtedTool_Port.put(IP1, tool_port1);

			int server_port1 = Global.distribudtedTool_PortList[0 % distributedPortCount];
			Global.distribudtedToolSever_Port.put(IP1, server_port1);

			DistributedPort distributedPort1 = new DistributedPort(IP1, tool_port1, server_port1);

			// *********************************************
			String IP2 = Global.distribudtedTool_IP_List.get(1 % listSize);

			Global.distribudtedTool_IP.add(IP2);

			int tool_port2 = Global.distribudtedTool_PortList[0 % distributedPortCount];
			Global.distribudtedTool_Port.put(IP2, tool_port2);

			int server_port2 = Global.distribudtedTool_PortList[1 % distributedPortCount];

			DistributedPort distributedPort2 = new DistributedPort(IP2, tool_port2, server_port2);

			Global.distribudtedToolSever_Port.put(IP2, server_port2);

			Global.distributedPortList = new ArrayList<>();

			Global.distributedPortList.add(distributedPort1);
			Global.distributedPortList.add(distributedPort2);

			// **********************************************

			for (int i = 1; i < listSize; i++) {
				System.out.println();

				String IP = Global.distribudtedTool_IP_List.get(i);

				JSONObject message = distributedMessage.msgType2(
						Global.distribudtedTool_IP_List.get((i + listSize - 1) % listSize),
						Global.distribudtedTool_PortList[1 % distributedPortCount],
						Global.distribudtedTool_PortList[0 % distributedPortCount],
						Global.distribudtedTool_IP_List.get((i + 1) % listSize),
						Global.distribudtedTool_PortList[0 % distributedPortCount],
						Global.distribudtedTool_PortList[1 % distributedPortCount]);

				System.out.println("IP:" + IP);
				System.out.println("channelMap Size : " + distributedServer.channelMap.size());

				distributedServer.channelMap.get(IP).writeAndFlush(message.toString());
				System.out.println(IP);
				System.out.println("msgType=2 send!");
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (Channel channel : distributedServer.channelMap.values()) {
				channel.writeAndFlush(distributedMessage.MsgTypeRun(MetricType.NORTHBOUND).toString());
			}
		}

	}

	public static void startTasks() {// 按 run按钮之后的主要实行代码
		// start Metric

		Main.gui.S_LOG_TEXT.setText("");
		Main.gui.S_TOPO_TEXT.setText("");

		Log.ADD_LOG("***************SDN Controller Information***************\n");
		Log.ADD_LOG("Controller Mode: " + Global.conMode + "\n");
		Log.ADD_LOG("Controller Type: " + Global.conTroller + "\n");
		Log.ADD_LOG("Ip/Port: " + Global.SDN_CONTROLLER_IP[0] + ":" + Global.SDN_CONTROLLER_PORT + "\n");

		Log.ADD_LOG("*****************Benchmark  Information*****************\n");
		Log.ADD_LOG("OpenFlow Version: "
				+ (Global.ofVersion == org.projectfloodlight.openflow.protocol.OFVersion.OF_13 ? "Openflow 1.3"
						: "please select 1.3 version.")
				+ " \n");

		switch (Global.topoType) {
		case LINEAR:
			Log.ADD_LOG("Topology Type: Linear\n");
			break;
		case RING:
			Log.ADD_LOG("Topology Type: Ring\n");
			break;
		case MININET:
			Log.ADD_LOG("Topology Type: Mininet\n");
		}

		Log.ADD_LOG("Number of Switch: " + Global.NUMBER_OF_TEST_SWITCH + " \n");
		Log.ADD_LOG("Number of Host per Switch: " + Global.NODE_SIZE + " \n");
		Log.ADD_LOG("Test Time: " + Global.TEST_TIME + "s \n");
		Log.ADD_LOG("Bulk size start from: " + Global.CURRENT_BULK_SIZE + "\n");
		Log.ADD_LOG("The packet loss threshoud : " + Global.PACKET_LOSS_RATE * 100 + "%\n");

		// unknowns type
		if (Global.UNKNOWN_PACKET_TYPE == Global.TCP_PACKET) {
			Log.ADD_LOG("Asynchronous Message Type: TCP");
		} else if (Global.UNKNOWN_PACKET_TYPE == Global.UDP_PACKET) {
			Log.ADD_LOG("Asynchronous Message Type: UDP");
		} else if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET) {
			Log.ADD_LOG("Asynchronous Message Type: ECHO Request");
		} else {
			Log.ADD_LOG("Asynchronous Message Type: ARP Request");
		}

		// provisioning unknow type
		if (Global.PROVISINIONING_PACKET_TYPE == Global.TCP_PACKET) {
			Log.ADD_LOG("\nReactive Path Provisioning Packet Type: TCP");
		} else if (Global.PROVISINIONING_PACKET_TYPE == Global.UDP_PACKET) {
			Log.ADD_LOG("\nReactive Path Provisioning Packet Type: UDP");
		} else {
			Log.ADD_LOG("\nReactive Path Provisioning Packet Type: ARP");
		}

		Log.ADD_LOG("\n*******************************************************\n");

		Log.ADD_LOG_PANEL("\"" + Global.southboundMetric + "\" Test Start... \n", Main.class.toString());

		Main.gui.S_TOPO_TEXT.append("Topology Type:" + Global.topoType + "\n");

		Main.gui.S_TOPO_TEXT.append("Number Of Node: " + Global.NUMBER_OF_TEST_SWITCH);

		Main.gui.N_progressBarTotal.setMaximum(1);// 进度条
		Main.gui.N_progressBarTotal.setValue(0);
		
		N_ProgressUpdate pu = new N_ProgressUpdate();
		ProgressUpdate pu2 = new ProgressUpdate();
		
		System.out.println("############## what : " + Global.northboundMetric.toString());
		
		switch (Global.northboundMetric) {
		case TOPOLOGY_DISCOVERY_TIME:

			Global.TOPOLOGY_DISCOVERY_TEST_NODE_ID = Global.NUMBER_OF_TEST_SWITCH + 1;
			Global.PROGRESS_MAX_VALUE = 26;
			Main.gui.S_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.S_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis(); // 현재 시간 가져온 후 global 변수에 할당

			pu2.updateStart(1, 1);

			TopologyDiscovery.go();

			break;
		

		case PROACTIVE_PATH_PROVISIONING_TIME:
			
			Global.PROGRESS_MAX_VALUE = 2;
			Main.gui.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new N_ProgressUpdate();
			pu.NupdateStart(1, 1);

			try {
				System.out.println("##### pr test time ");
				SsyncProactiveProTime.go();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			// 더 이상 지속되지 않음
			break;
		case PROACTIVE_PATH_PROVISIONING_RATE:

			Global.PROGRESS_MAX_VALUE = 2;
			Main.gui.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new N_ProgressUpdate();
			pu.NupdateStart(1, 1);

			try {
				System.out.println("##### pr test rate ");

				AsyncClientProactiveProRate.post();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			// 더 이상 지속되지 않음
			break;
		case CONTROL_SESSION_CAPACITY_CCD:

			Global.PROGRESS_MAX_VALUE = 2;
			Main.gui.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();// 1970.1.1时起的毫秒数

			pu = new N_ProgressUpdate();
			pu.NupdateStart(1, 1);

			Tasks.START_TASK_8_NEW();

			break;
		case NETWORK_DISCOVERY_SIZE_NS:

			Global.PROGRESS_MAX_VALUE = 2;
			Main.gui.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new N_ProgressUpdate();
			pu.NupdateStart(1, 1);

		//	Tasks.START_TASK_9_NEW();
			WRNetworkDiscoverySize NDS = new WRNetworkDiscoverySize();
			try {
				NDS.go();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case FORWARDING_TABLE_CAPACITY_NRP:

			Global.NODE_SIZE = 1000;
			Global.BUFF_SIZE = 1000000;
			Global.PROGRESS_MAX_VALUE = 2;
			Main.gui.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.gui.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();
			Main.gui.N_startButton.setEnabled(false);
			Main.gui.N_metricList.setEnabled(false);

			pu = new N_ProgressUpdate();
			pu.NupdateStart(1, 1);

			Main.gui.N_startButton.setEnabled(false);
			Main.gui.N_metricList.setEnabled(false);

			try {
				AsyncForwdTabCapacity1.post();
			//	AsyncForwdTabCapacity2.post();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			break;
		

		case EXCEPTION_HANDLING_SECURITY:
		case DOS_ATTACKS_SECURITY:
			Initializer.INITIAL_CHANNEL_POOL();
			break;
		case CONTROLLER_FAILOVER_TIME_RELIABILITY:
			Initializer.INITIAL_CHANNEL_POOL();
			break;

		case NETWORK_RE_PROVISIONING_TIME_RELIABILITY_NODE_FAILURE_VS_FAILURE:
			Initializer.INITIAL_CHANNEL_POOL();
			break;

		case TEST_TOTAL:

			Global.TEST_INIT_TIME = System.currentTimeMillis();
			Main.gui.N_progressBarTotal.setMaximum(6);
			Main.gui.N_progressBarTotal.setValue(0);
//			Tasks.START_TASK_15_NEW();

			break;
		default:
			Initializer.INITIAL_CHANNEL_POOL();
			break;
		}

	}
}

