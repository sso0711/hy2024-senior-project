package com.mir.ui;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import com.mir.distributed.server.DistributedServer;
import com.mirlab.global.Global;
import com.mirlab.lib.FindOptimizationalResult;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.Tasks;
import com.southbound.tasks.AsynchronousMessageProcessingRate;
import com.southbound.tasks.AsynchronousMessageProcessingTime;
import com.southbound.tasks.ReactivePathProvisioningRate;
import com.southbound.tasks.ReactivePathProvisioningTime;
import com.southbound.tasks.TopologyChangeDetection;
import com.southbound.tasks.TopologyDiscovery;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年8月11日 上午9:58:03 类说明
 */
public class N_RunButton {
	public static void go() {
		try {
			Initializer.TEST_CONTROLLER_IF_IT_IS_OPEN();// 创建一个node链接controller？
			Thread.sleep(1000);

		} catch (InterruptedException e) {
			Global.CONTROLLER_IS_OPEN = false;
			e.printStackTrace();
		}

		if (Global.CONTROLLER_IS_OPEN) {// controller 在线

			checkDistributed();

			startTasks();

		} else {// controller 不在线
			Initializer.INITIAL_CHANNEL_POOL();
			System.out.println("Controller is offline!");
			JOptionPane.showMessageDialog(null, "Please check if controller is running.", "Disconnected",
					JOptionPane.ERROR_MESSAGE);// 跳出新窗口

		}
	}

	private static void checkDistributed() {// 把所有的变数全都放到json格式变量里面

		if (Global.IS_ENABLE_DISTRIBUTED && Global.IS_MASTER) {
			JSONObject message = new JSONObject();
			message.put("msgType", 1);
			message.put("metric", Global.TEST_METRIC);
			message.put("ofVersion", Global.OPENFLOW_VERSION);
			message.put("topoType", Global.TOPO_TYPE);
			message.put("numberOfSwitch", Global.NUMBER_OF_TEST_SWITCH);
			message.put("testTime", Global.TEST_TIME);
			message.put("controllerMode", Global.CONTROLLER_MODE);
			message.put("controllerIp", Global.SDN_CONTROLLER_IP[0]);
			message.put("controllerPort", Global.SDN_CONTROLLER_PORT);
			message.put("controllerType", Global.CONTROLLER_TYPE);
			message.put("unknownPacketType", Global.UNKNOWN_PACKET_TYPE);
			message.put("pPacketType", Global.PROVISINIONING_PACKET_TYPE);
			message.put("hostOffset0", Global.HOST_IP[0]);
			message.put("hostOffset1", Global.HOST_IP[1]);

			DistributedServer.channelGroup.write(message.toString());
		}

	}

	public static void startTasks() {// 按 run按钮之后的主要实行代码
		// start Metric
		Initializer.INITIAL_CHANNEL_POOL();

		Log.ADD_LOG("***************SDN Controller Information***************\n");
		Log.ADD_LOG("Controller Mode: " + Global.CONTROLLER_MODE_NAME[Global.CONTROLLER_MODE] + "\n");
		Log.ADD_LOG("Controller Type: " + Global.CONTROLLER_TYPE_NAME[Global.CONTROLLER_TYPE] + "\n");
		Log.ADD_LOG("Ip/Port: " + Global.SDN_CONTROLLER_IP[0] + ":" + Global.SDN_CONTROLLER_PORT + "\n");

		Log.ADD_LOG("*****************Benchmark  Information*****************\n");
		Log.ADD_LOG("OpenFlow Version: "
				+ (Global.OPENFLOW_VERSION == Global.OF_V_3 ? "Openflow 1.3" : "please select 1.3 version.") + " \n");

		switch (Global.TOPO_TYPE) {
		case Global.TOPO_LINEAR:
			Log.ADD_LOG("Topology Type: Linear\n");
			break;
		case Global.TOPO_RING:
			Log.ADD_LOG("Topology Type: Ring\n");
			break;
		case Global.TOPO_MININET:
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

		Log.ADD_LOG_PANEL("\"" + Global.METRIC_NAME[Global.TEST_METRIC] + "\" Test Start... \n", Main.class.toString());

		// 临时先这么用，有时间再加class
		Main.N_TOPO_TEXT.setText("Topology Type: " + (Global.TOPO_TYPE == 0 ? "Linear" : "Ring") + "\nNumber Of Node: "
				+ Global.NUMBER_OF_TEST_SWITCH);

		Main.N_progressBarTotal.setMaximum(1);// 进度条
		Main.N_progressBarTotal.setValue(0);

		switch (Global.TEST_METRIC) {
		case Global.TOPOLOGY_DISCOVERY_TIME:// 拓扑发现时间

			Global.TOPOLOGY_DISCOVERY_TEST_NODE_ID = Global.NUMBER_OF_TEST_SWITCH + 1;
			Global.PROGRESS_MAX_VALUE = 26;
			Main.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();
			// 获取现在时间并赋值给global变数

			ProgressUpdate pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			TopologyDiscovery.go();

			break;
		case Global.TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP:

			Global.PROGRESS_MAX_VALUE = 26;
			Main.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			TopologyChangeDetection.go();

			break;

		case Global.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME:

			Global.PROGRESS_MAX_VALUE = 4 + Global.TEST_TIME;

			Main.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Main.N_startButton.setEnabled(false);
			Main.N_metricList.setEnabled(false);

			AsynchronousMessageProcessingTime.go();

			break;

		case Global.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE:

			Global.PROGRESS_MAX_VALUE = 2;

			Main.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Main.N_startButton.setEnabled(false);
			Main.N_metricList.setEnabled(false);

			AsynchronousMessageProcessingRate.go();

			Main.N_progressBar.setValue(Main.N_progressBar.getMaximum());
			Main.N_progressBarTotal.setValue(Main.N_progressBarTotal.getMaximum());

			break;
		case Global.REACTIVE_PATH_PROVISIONING_TIME:

			Global.PROGRESS_MAX_VALUE = 32 + Global.TEST_TIME;
			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				Global.PROGRESS_MAX_VALUE = 27 + Global.TEST_TIME;
			}
			Main.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Main.N_startButton.setEnabled(false);
			Main.N_metricList.setEnabled(false);

			ReactivePathProvisioningTime.go();

			break;

		case Global.REACTIVE_PATH_PROVISIONING_RATE:

			Global.PROGRESS_MAX_VALUE = 2;

			Main.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Main.N_startButton.setEnabled(false);
			Main.N_metricList.setEnabled(false);

			Log.ADD_LOG_PANEL(
					"Start to find optimizational Bulk Size. Bulk Size Start from:" + Global.CURRENT_BULK_SIZE,
					FindOptimizationalResult.class.toString());

			ReactivePathProvisioningRate.go();

			Main.N_progressBar.setValue(Main.N_progressBar.getMaximum());
			Main.N_progressBarTotal.setValue(Main.N_progressBarTotal.getMaximum());

			break;

		case Global.PROACTIVE_PATH_PROVISIONING_TIME:
			Initializer.INITIAL_CHANNEL_POOL();
			// 未完待续
			break;
		case Global.PROACTIVE_PATH_PROVISIONING_RATE:
			Initializer.INITIAL_CHANNEL_POOL();
			// 未完待续
			break;
		case Global.CONTROL_SESSION_CAPACITY_CCD:

			Global.PROGRESS_MAX_VALUE = 2;
			Main.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();// 1970.1.1时起的毫秒数

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Tasks.START_TASK_8_NEW();

			break;
		case Global.NETWORK_DISCOVERY_SIZE_NS:

			Global.PROGRESS_MAX_VALUE = 2;
			Main.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Tasks.START_TASK_9_NEW();

			break;

		case Global.FORWARDING_TABLE_CAPACITY_NRP:

			Global.NODE_SIZE = 1000;
			Global.BUFF_SIZE = 1000000;
			Global.PROGRESS_MAX_VALUE = 2;
			Main.N_progressBar.setMaximum(Global.PROGRESS_MAX_VALUE);
			Main.N_progressBar.setValue(0);
			Global.TEST_INIT_TIME = System.currentTimeMillis();
			Main.N_startButton.setEnabled(false);
			Main.N_metricList.setEnabled(false);

			pu = new ProgressUpdate();
			pu.updateStart(1, 1);

			Main.N_startButton.setEnabled(false);
			Main.N_metricList.setEnabled(false);

			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				Initializer.INITIAL_2();
			} else {

				Initializer.INITIAL_0();

			}

			Tasks.START_TASK_10_NEW();
			break;

		case Global.EXCEPTION_HANDLING_SECURITY:
		case Global.DOS_ATTACKS_SECURITY:
			Initializer.INITIAL_CHANNEL_POOL();
			break;
		case Global.CONTROLLER_FAILOVER_TIME_RELIABILITY:
			Initializer.INITIAL_CHANNEL_POOL();
			break;

		case Global.NETWORK_RE_PROVISIONING_TIME_RELIABILITY_NODE_FAILURE_VS_FAILURE:
			Initializer.INITIAL_CHANNEL_POOL();
			break;

		case Global.TEST_TOTAL:

			Global.TEST_INIT_TIME = System.currentTimeMillis();
			Main.N_progressBarTotal.setMaximum(6);
			Main.N_progressBarTotal.setValue(0);
			Tasks.START_TASK_15_NEW();

			break;
		default:
			Initializer.INITIAL_CHANNEL_POOL();
			break;
		}

	}
}
