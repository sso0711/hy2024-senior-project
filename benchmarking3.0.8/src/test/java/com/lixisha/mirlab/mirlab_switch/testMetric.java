package com.lixisha.mirlab.mirlab_switch;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月15日 下午4:27:33 类说明
 */
public class testMetric {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] values = new String[] { "1.TOPOLOGY_DISCOVERY_TIME", "2.TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP",
				"3.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME", "4.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE",
				"5.REACTIVE_PATH_PROVISIONING_TIME", "6.REACTIVE_PATH_PROVISIONING_RATE",
				"7.*PROACTIVE_PATH_PROVISIONING_TIME", "8.*PROACTIVE_PATH_PROVISIONING_RATE", "9. Test (Metric 1~6)",
				"---------------SDN controller should be reboot to test metric 10~16",
				"10.CONTROL_SESSION_CAPACITY_CCD", "11.NETWORK_DISCOVERY_SIZE_NS", "12.FORWARDING_TABLE_CAPACITY_NRP",
				"13.*EXCEPTION_HANDLING_SECURITY", "14.*DOS_ATTACKS_SECURITY",
				"15.*CONTROLLER_FAILOVER_TIME_RELIABILITY",
				"16.*NETWORK_RE_PROVISIONING_TIME_RELIABILITY_NODE_FAILURE_VS_FAILURE", };

		String a = values[1];

		System.out.println(a.split("\\.")[1]);
	}

}
