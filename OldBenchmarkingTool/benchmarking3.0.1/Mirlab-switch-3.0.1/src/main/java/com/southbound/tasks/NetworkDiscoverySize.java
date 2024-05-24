package com.southbound.tasks;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.global.Global;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年4月14日 下午12:02:33 类说明
 */
public class NetworkDiscoverySize {
	public void go() {
		version0();
	}

	private void version0() {
		JSONObject JSONMSG = Global.JSONMSG;

		JSONArray mn_links = JSONMSG.getJSONArray("links");
		JSONArray mn_switches = JSONMSG.getJSONArray("switches");
		JSONArray mn_hosts = JSONMSG.getJSONArray("hosts");

		int link_count = mn_links.length();
		int switch_count = mn_switches.length();
		int host_count = mn_hosts.length();

		Node[] nodes = new Node[switch_count];
		Host[] hosts = new Host[host_count];

		long dpid = Global.SWITCH_ID_OFF_SET;

		// create nodes
		for (int i = 0; i < switch_count; i++) {
			nodes[i] = new Node((long) dpid++, i);
		}
		// create hosts
		// host ip和mac 配置

		// if (withHost) {
		// for (int i = 0; i < host_count; i++) {
		// String HostIp = "10.0.0." + i;
		// String HostMac = Global.HOST_MAC + ":aa:aa:aa:aa:" +
		// String.format("%02x", i);
		//
		// hosts[i] = new Host(HostIp, HostMac);
		// }
		// }

		// create links
		for (int i = 0; i < link_count; i++) {
			String src = mn_links.getJSONObject(i).getString("src");
			String dest = mn_links.getJSONObject(i).getString("dest");
			String src_type = src.replaceAll("[^(A-Za-z)]", "");
			String dest_type = dest.replaceAll("[^(A-Za-z)]", "");
			int src_num = Integer.parseInt(src.replaceAll("[^(0-9)]", "")) - 1;
			int dest_num = Integer.parseInt(dest.replaceAll("[^(0-9)]", "")) - 1;

			if (src_type.equals(dest_type)) {// between switch and switch

				// nodes[src_num].addPort();
				// nodes[dest_num].addPort();
				//
				// nodes[src_num].getPortList().getLast().setConnectedPort(nodes[dest_num].getPortList().getLast());
				// nodes[dest_num].getPortList().getLast().setConnectedPort(nodes[src_num].getPortList().getLast());

			} else {// between switch and host

				if (src_type.equals("s")) {

					nodes[src_num].addPort();
					nodes[src_num].getPortList().getLast().addConnectedHostToList(hosts[dest_num]);

				} else {

					nodes[dest_num].addPort();
					nodes[dest_num].getPortList().getLast().addConnectedHostToList(hosts[src_num]);

				}

			}

		}

		// 最前和最后node赋给global变量
		Global.ROOTNODE = nodes[0];
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];

		// one by one

		int[] state = new int[switch_count];
		// 0 white
		// 1 gray
		// 2 black
		int now = 0;
		int pro = 0;

		// ConnectionThread root = new ConnectionThread(nodes[now]);
		state[now] = 1;
		// root.start();

	}

	public static void main(String[] args) {
		NetworkDiscoverySize nds = new NetworkDiscoverySize();
		nds.go();
	}
}
