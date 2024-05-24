package com.mir.ui;

import java.util.ArrayList;


import org.json.JSONObject;

import com.mir.ui.GUI;
import com.mirlab.global.Global;

public class N_Result {
	
	private static GUI gui = new GUI();

	// for recording test 0
	public static ArrayList TOPOLOGY_DISCOVERY_TIME_LIST = new ArrayList();

	// for new test 0 new
	public static ArrayList TOPOLOGY_DISCOVERY_LLDP_OUT = new ArrayList();
	public static ArrayList TOPOLOGY_DISCOVERY_LLDP_IN = new ArrayList();

	// for recording test 1
	public static ArrayList TOPOLOGY_CHANGE_DETECTION_TIME_LIST_PORTSTATUS = new ArrayList();
	public static ArrayList TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP = new ArrayList();

	// for recording test 2 3
	public static ArrayList ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN = new ArrayList();
	public static ArrayList ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT = new ArrayList();

	// for recording test 4 5
	public static ArrayList REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN = new ArrayList();
	public static ArrayList REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD = new ArrayList();

	// for recording test 6 7

	// for recording test 8
	public static ArrayList NUMBER_OF_OUT_HELLO = new ArrayList();
	public static ArrayList NUMBER_OF_IN_HELLO = new ArrayList();

	// for Result 11
	public static int TEMP_NUMBER_OF_FLOW_MOD = 0;
	public static int TOTAL_NUMBER_OF_FLOW_MOD = 0;

	public static void ADD_RESULT(String str, int x, int y) {
		gui.N_RESULT_TABLE.getModel().setValueAt(str, x, y);
		if (Global.IS_ENABLE_DISTRIBUTED) {

			int newX = -1;
			int newY = -1;

			if (y < 5) {
				newX = x;
			} else {
				newX = 9 + x;
			}
			
			System.out.println("");

			if (!Global.IS_MASTER) {
				if (Global.MASTER_CHANNEL != null) {
					newY = Global.MY_ID + 3;
					JSONObject message = new JSONObject();
					message.put("msgType", 0);
					message.put("newX", newX);
					message.put("newY", newY);
					message.put("str", str);
					Global.MASTER_CHANNEL.write(message.toString());
				}
			} else {
				newY = 2;

			}

			DistributedFrame.table_1.getModel().setValueAt(str, newX, newY);
		}
	}

}
