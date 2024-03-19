package com.mirlab.lib;

import java.util.ArrayList;

import org.json.JSONObject;

import com.Main;
import com.mir.distributed.client.DistributedClient;
import com.mir.distributed.message.DistributedMessage;
import com.mir.ui.distributedGUI.DistributedFrame;
import com.mirlab.global.Global;

public class Result {

	// for recording test 0
	public static ArrayList<Long> TOPOLOGY_DISCOVERY_TIME_LIST = new ArrayList<Long>();

	// for new test 0 new
	public static ArrayList<Long> TOPOLOGY_DISCOVERY_LLDP_OUT = new ArrayList<Long>();
	public static ArrayList<Long> TOPOLOGY_DISCOVERY_LLDP_IN = new ArrayList<Long>();

	// for recording test 1
	public static ArrayList<Long> TOPOLOGY_CHANGE_DETECTION_TIME_LIST_PORTSTATUS = new ArrayList<Long>();
	public static ArrayList<Long> TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP = new ArrayList<Long>();

	// for recording test 2 3
	public static ArrayList<Long> ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN = new ArrayList<Long>();
	public static ArrayList<Long> ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT = new ArrayList<Long>();

	// for recording test 4 5
	public static ArrayList<Long> REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN = new ArrayList<Long>();
	public static ArrayList<Long> REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD = new ArrayList<Long>();

	// for recording test 6 7

	// for recording test 8
	public static ArrayList<Long> NUMBER_OF_OUT_HELLO = new ArrayList<Long>();
	public static ArrayList<Long> NUMBER_OF_IN_HELLO = new ArrayList<Long>();

	// for Result 11
	public static int TEMP_NUMBER_OF_FLOW_MOD = 0;
	public static int TOTAL_NUMBER_OF_FLOW_MOD = 0;

	
	public static void ADD_RESULT(String str, int x, int y) {
		Main.gui.S_RESULT_TABLE.getModel().setValueAt(str, x, y);
		if (Global.IS_ENABLE_DISTRIBUTED) {

			int newX = -1;
			int newY = -1;

			if (y < 5) {
				newX = x;
			} else {
				newX = 9 + x;
			}

			if (!Global.IS_MASTER) {

				newY = Global.MY_ID + 3;
				JSONObject msg = DistributedMessage.INSTANCE.MsgTypeResult(newX, newY, str);
				DistributedClient.getInstance().sendMessage(msg);

			} else {
				newY = 2;

			}

			DistributedFrame.table_1.getModel().setValueAt(str, newX, newY);
		}
	}

}
