package com.mir.distributed.message;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import com.mir.ui.N_RunButton;
import com.mir.ui.S_RunButton;
import com.mir.ui.distributedGUI.DistributedFrame;
import com.mirlab.component.DistributedPort;
import com.mirlab.enumType.Controller;
import com.mirlab.enumType.ControllerMode;
import com.mirlab.enumType.MetricType;
import com.mirlab.enumType.SouthboundMetric;
import com.mirlab.enumType.TopologyType;
import com.mirlab.global.Global;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月13日 下午8:23:36 类说明
 */
public enum DistributedMessage {
	// Singleton*********************************
	INSTANCE;

	private DistributedMessage() {

	}

	// master -> slave
	// when benchmarking tool slaves connect to master, master will send msgType0 to
	// tell slaves their ID
	public JSONObject msgType0(int BenchmarkingTool_ID, String slaveIP) {

		DistributedFrame.table.getModel().setValueAt("Slave " + BenchmarkingTool_ID + ": " + slaveIP,
				BenchmarkingTool_ID, 0);
		DistributedFrame.table.getModel().setValueAt("Slave ", BenchmarkingTool_ID, 1);

		JSONObject message = new JSONObject();

		message.put("msgType", "0");
		message.put("BenchmarkingTool_ID", BenchmarkingTool_ID);

		return message;

	}

	// when benchmarking tool slaves get the msgType0, they process it
	public void processMsgType0(JSONObject message) {

		JOptionPane.showMessageDialog(null, "Connected with Master Benchmark", "Connected",
				JOptionPane.INFORMATION_MESSAGE);
		DistributedFrame.table.getModel().setValueAt(Global.MASTER_IP, 0, 0);
		DistributedFrame.table.getModel().setValueAt("Master", 0, 1);

		Global.MY_ID = message.getInt("BenchmarkingTool_ID");
		Global.SWITCH_ID_OFF_SET = (Global.MY_ID + 1) * 100 * Global.SWITCH_ID_OFF_SET;
		Global.HOST_MAC = Global.SLAVES_HOST_MAC[Global.MY_ID];

	}

	// master -> slave
	// master sends parameter to slaves
	public JSONObject msgType1() {

		JSONObject message = new JSONObject();
		message.put("msgType", "1");
		message.put("metric", Global.southboundMetric);
		message.put("ofVersion", Global.ofVersion);
		message.put("topoType", Global.topoType);
		message.put("numberOfSwitch", Global.NUMBER_OF_TEST_SWITCH);
		message.put("testTime", Global.TEST_TIME);
		message.put("controllerMode", Global.conMode);
		message.put("controllerIp", Global.SDN_CONTROLLER_IP[0]);
		message.put("controllerPort", Global.SDN_CONTROLLER_PORT);
		message.put("controllerType", Global.conTroller);
		message.put("unknownPacketType", Global.UNKNOWN_PACKET_TYPE);
		message.put("pPacketType", Global.PROVISINIONING_PACKET_TYPE);

		return message;
	}

	// when benchmarking tool slaves get the msgType1, they process it
	public void processMsgType1(JSONObject message) {

		Global.southboundMetric = SouthboundMetric.valueOf(message.get("metric").toString());
		Global.ofVersion = org.projectfloodlight.openflow.protocol.OFVersion
				.valueOf(message.get("ofVersion").toString());
		Global.topoType = TopologyType.valueOf(message.get("topoType").toString());
		Global.NUMBER_OF_TEST_SWITCH = message.getInt("numberOfSwitch");
		Global.TEST_TIME = message.getInt("testTime");
		Global.conMode = ControllerMode.valueOf(message.get("controllerMode").toString());
		Global.SDN_CONTROLLER_IP[0] = message.getString("controllerIp");
		Global.SDN_CONTROLLER_PORT = message.getInt("controllerPort");
		Global.conTroller = Controller.valueOf(message.get("controllerType").toString());
		Global.UNKNOWN_PACKET_TYPE = message.getInt("unknownPacketType");
		Global.PROVISINIONING_PACKET_TYPE = message.getInt("pPacketType");

		// host ip
		Global.HOST_IP[1] = Global.MY_ID + 1;

	}

	// master -> slave
	// master sends distributed ports' parameter to slaves

	public JSONObject msgType2(String DistributedPort_1_Remote_Server_IP, int DistributedPort_1_Remote_Server_Port,
			int DistributedPort_1_Local_Server_Port, String DistributedPort_2_Remote_Server_IP,
			int DistributedPort_2_Remote_Server_Port, int DistributedPort_2_Local_Server_Port) {

		JSONObject message = new JSONObject();

		message.put("msgType", "2");

		message.put("DistributedPort_1_Remote_Server_IP", DistributedPort_1_Remote_Server_IP);
		message.put("DistributedPort_1_Remote_Server_Port", DistributedPort_1_Remote_Server_Port);
		message.put("DistributedPort_1_Local_Server_Port", DistributedPort_1_Local_Server_Port);

		System.out.println("IP1:" + DistributedPort_1_Remote_Server_IP + "   tool_port1:"
				+ DistributedPort_1_Remote_Server_Port + "    server port1:" + DistributedPort_1_Local_Server_Port);

		message.put("DistributedPort_2_Remote_Server_IP", DistributedPort_2_Remote_Server_IP);
		message.put("DistributedPort_2_Remote_Server_Port", DistributedPort_2_Remote_Server_Port);
		message.put("DistributedPort_2_Local_Server_Port", DistributedPort_2_Local_Server_Port);

		System.out.println("IP2:" + DistributedPort_2_Remote_Server_IP + "   tool_port2:"
				+ DistributedPort_2_Remote_Server_Port + "    server port2:" + DistributedPort_2_Local_Server_Port);

		return message;
	}

	// when benchmarking tool slaves get the msgType2, they process it
	public void processMsgType2(JSONObject message) {

		Global.distributedPortList = new ArrayList<DistributedPort>();

		String DistributedPort_1_Remote_Server_IP = message.getString("DistributedPort_1_Remote_Server_IP");
		int DistributedPort_1_Remote_Server_Port = message.getInt("DistributedPort_1_Remote_Server_Port");
		int DistributedPort_1_Local_Server_Port = message.getInt("DistributedPort_1_Local_Server_Port");

		DistributedPort distributedPort1 = new DistributedPort(DistributedPort_1_Remote_Server_IP,
				DistributedPort_1_Remote_Server_Port, DistributedPort_1_Local_Server_Port);

		String DistributedPort_2_Remote_Server_IP = message.getString("DistributedPort_2_Remote_Server_IP");
		int DistributedPort_2_Remote_Server_Port = message.getInt("DistributedPort_2_Remote_Server_Port");
		int DistributedPort_2_Local_Server_Port = message.getInt("DistributedPort_2_Local_Server_Port");

		DistributedPort distributedPort2 = new DistributedPort(DistributedPort_2_Remote_Server_IP,
				DistributedPort_2_Remote_Server_Port, DistributedPort_2_Local_Server_Port);

		Global.distributedPortList.add(distributedPort1);
		Global.distributedPortList.add(distributedPort2);

	}

	// master -> slave
	// master sends slaves Run message
	public JSONObject MsgTypeRun(MetricType metricType) {

		JSONObject message = new JSONObject();

		message.put("msgType", "Run");

		switch (metricType) {
	
		case SOUTHBOUND:
			message.put("metricType", MetricType.SOUTHBOUND);
			break;
		case NORTHBOUND:
			message.put("metricType", MetricType.NORTHBOUND);
			break;
		case SNBOUND:
			message.put("metricType", MetricType.SNBOUND);
			break;
		}

		return message;
	}

	// when benchmarking tool slaves get the msgTypeRun, they process it
	public void processMsgTypeRun(JSONObject message) {

		MetricType metricType = MetricType.valueOf(message.get("metricType").toString());

		switch (metricType) {
		case SOUTHBOUND:
			S_RunButton.startTasks();

			break;
		case NORTHBOUND:
			N_RunButton.startTasks();

			break;
		case SNBOUND:

			break;
		}

	}

	// slave -> master
	// slave sends master result
	public JSONObject MsgTypeResult(int x, int y, String result) {

		JSONObject message = new JSONObject();
		message.put("msgType", "Result");
		message.put("newX", x);
		message.put("newY", y);
		message.put("str", result);

		return message;
	}

	public void processMsgTypeResult(JSONObject message) {

		DistributedFrame.table_1.getModel().setValueAt(message.getString("str"), message.getInt("newX"),
				message.getInt("newY"));

	}

	// distributed client handler
	public void clientProcessOf(JSONObject message) {

		switch (message.getString("msgType")) {
		case "0":
			processMsgType0(message);
			break;
		case "1":
			processMsgType1(message);
			break;
		case "2":
			processMsgType2(message);
			break;
		case "Run":
			processMsgTypeRun(message);
			break;

		default:
			break;
		}
	}

	// distributed server handler
	public void serverProcessOf(JSONObject message) {
		switch (message.getString("msgType")) {
		case "Result":
			processMsgTypeResult(message);
			break;
		default:
			break;
		}
	}

}
