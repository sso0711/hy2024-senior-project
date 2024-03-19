package com.mirlab.topo;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mirlab.component.DistributedPort;
import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.component.Port;
import com.mirlab.global.Global;
import com.mirlab.lib.Log;
import com.mirlab.openflow.ConnectionThread;

public class CreateTopo {

	long dpid = 0;

	public CreateTopo() {
	}

	public CreateTopo(long startDpid) {
		this.dpid = startDpid;
	}

	public Node[] go() {
		Node[] nodes = null;

		if (Global.IS_ENABLE_DISTRIBUTED) {
			nodes = create_ringTopo_Dis();
		} else {
			switch (Global.TOPO_TYPE) {
			case Global.TOPO_LINEAR:
				nodes = create_linearTopo();
				break;
			case Global.TOPO_RING:
				nodes = create_ringTopo();
				break;
			case Global.TOPO_MININET:
				nodes = create_mininetTopo();
				break;
			}
		}

		return nodes;
	}

	private Node[] create_ringTopo_Dis() {
		// TODO Auto-generated method stub
		// no host
		Node[] nodes = null;
		nodes = new Node[Global.NUMBER_OF_TEST_SWITCH];

		// create node
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i] = new Node((long) dpid++, i);
			nodes[i].creatPort(2);
		}
		// create link
		for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].getPortList().get(0).setConnectedPort(nodes[i - 1].getPortList().get(1));

			nodes[i - 1].getPortList().get(1).setConnectedPort(nodes[i].getPortList().get(0));
			nodes[i - 1].setNextNode(nodes[i]);
		}

		nodes[Global.NUMBER_OF_TEST_SWITCH - 1].getPortList().get(1).setConnectedPort(nodes[0].getPortList().get(0));
		nodes[0].getPortList().get(0).setConnectedPort(nodes[Global.NUMBER_OF_TEST_SWITCH - 1].getPortList().get(1));
		nodes[Global.NUMBER_OF_TEST_SWITCH - 1].setNextNode(nodes[0]);

		// *****************

		Port port1 = nodes[0].addPort();
		String distribudtedTool_IP1 = Global.distribudtedTool_IP.get(0);

		DistributedPort distributedPort1 = new DistributedPort(port1, distribudtedTool_IP1,
				Global.distribudtedTool_Port.get(distribudtedTool_IP1),
				Global.distribudtedToolSever_Port.get(distribudtedTool_IP1));
		port1.setDistributedPort(distributedPort1);

		Port port2 = nodes[Global.NUMBER_OF_TEST_SWITCH - 1].addPort();
		String distribudtedTool_IP2 = Global.distribudtedTool_IP.get(1);

		DistributedPort distributedPort2 = new DistributedPort(port2, distribudtedTool_IP2,
				Global.distribudtedTool_Port.get(distribudtedTool_IP2),
				Global.distribudtedToolSever_Port.get(distribudtedTool_IP2));
		port2.setDistributedPort(distributedPort2);

		Global.distributedPortList.add(distributedPort1);
		Global.distributedPortList.add(distributedPort2);
		distributedPort1.startClient();
		distributedPort2.startClient();

		// *****************

		// 最前和最后node赋给global变量
		Global.ROOTNODE = nodes[0];
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];
		Global.nodes = nodes;

		// start netty
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			ConnectionThread ct = new ConnectionThread(nodes[i], Global.SDN_CONTROLLER_IP[0],
					Global.SDN_CONTROLLER_PORT);
			ct.start();
		}

		return nodes;
	}

	private Node[] create_mininetTopo() {
		// TODO Auto-generated method stub
		Node[] nodes = null;
		Host[] hosts = null;
		JSONObject JSONMSG = null;

		JSONMSG = Global.JSONMSG;

		JSONArray mn_links = JSONMSG.getJSONArray("links");
		JSONArray mn_switches = JSONMSG.getJSONArray("switches");
		JSONArray mn_hosts = JSONMSG.getJSONArray("hosts");

		int link_count = mn_links.length();
		int switch_count = mn_switches.length();
		int host_count = mn_hosts.length();

		nodes = new Node[switch_count];
		hosts = new Host[host_count];

		// create nodes
		for (int i = 0; i < switch_count; i++) {
			nodes[i] = new Node((long) dpid++, i);
		}

		// create hosts
		// host ip和mac 配置
		for (int i = 0; i < host_count; i++) {

			if (i < 16) {
				hosts[i] = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (i + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + "0" + Integer.toHexString(i));
			} else {
				hosts[i] = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (i + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + Integer.toHexString(i));
			}
		}

		// create links
		for (int i = 0; i < link_count; i++) {
			String src = mn_links.getJSONObject(i).getString("src");
			String dest = mn_links.getJSONObject(i).getString("dest");
			String src_type = src.replaceAll("[^(A-Za-z)]", "");
			String dest_type = dest.replaceAll("[^(A-Za-z)]", "");
			int src_num = Integer.parseInt(src.replaceAll("[^(0-9)]", "")) - 1;
			int dest_num = Integer.parseInt(dest.replaceAll("[^(0-9)]", "")) - 1;

			if (src_type.equals(dest_type)) {// between switch and switch

				nodes[src_num].addPort();
				nodes[dest_num].addPort();

				nodes[src_num].getPortList().getLast().setConnectedPort(nodes[dest_num].getPortList().getLast());
				nodes[dest_num].getPortList().getLast().setConnectedPort(nodes[src_num].getPortList().getLast());

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
		Global.nodes = nodes;
		Global.hosts = hosts;

		// start netty
		for (int i = 0; i < switch_count; i++) {
			ConnectionThread ct = new ConnectionThread(nodes[i], Global.SDN_CONTROLLER_IP[0],
					Global.SDN_CONTROLLER_PORT);
			ct.start();
		}
		return nodes;
	}

	public Node[] create_mininetTopoWith200hosts() {
		// the mininet topology's hosts shoud be 2 or it will make an error

		// TODO Auto-generated method stub
		Node[] nodes = null;
		Host[] srcHosts = new Host[Global.NODE_SIZE];
		Host[] dstHosts = new Host[Global.NODE_SIZE];
		JSONObject JSONMSG = null;
		int portToHost_count = 2;

		JSONMSG = Global.JSONMSG;

		JSONArray mn_links = JSONMSG.getJSONArray("links");
		JSONArray mn_switches = JSONMSG.getJSONArray("switches");
		// JSONArray mn_hosts = JSONMSG.getJSONArray("hosts");

		int link_count = mn_links.length();
		int switch_count = mn_switches.length();
		// int host_count = mn_hosts.length();

		nodes = new Node[switch_count];

		// create nodes
		for (int i = 0; i < switch_count; i++) {
			nodes[i] = new Node((long) dpid++, i);
		}

		// create srchosts
		for (int n = 1; n < Global.NODE_SIZE + 1; n++) {

			// add host
			Host h1;
			if (n < 16) {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + "0" + Integer.toHexString(n));

			} else {
				h1 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:aa:" + Integer.toHexString(n));
			}

			srcHosts[n - 1] = h1;
		}

		// create dstHosts
		for (int n = 1; n < Global.NODE_SIZE + 1; n++) {

			// add host
			Host h5;
			if (n < 16) {
				h5 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".1." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:ae:" + "0" + Integer.toHexString(n));

			} else {
				h5 = new Host(Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".1." + (n + 1),
						Global.HOST_MAC + ":aa:aa:aa:ae:" + Integer.toHexString(n));
			}

			dstHosts[n - 1] = h5;

		}

		// create links
		for (int i = 0; i < link_count; i++) {
			String src = mn_links.getJSONObject(i).getString("src");
			String dest = mn_links.getJSONObject(i).getString("dest");
			String src_type = src.replaceAll("[^(A-Za-z)]", "");
			String dest_type = dest.replaceAll("[^(A-Za-z)]", "");
			int src_num = Integer.parseInt(src.replaceAll("[^(0-9)]", "")) - 1;
			int dest_num = Integer.parseInt(dest.replaceAll("[^(0-9)]", "")) - 1;

			if (src_type.equals(dest_type)) {// between switch and switch

				nodes[src_num].addPort();
				nodes[dest_num].addPort();

				nodes[src_num].getPortList().getLast().setConnectedPort(nodes[dest_num].getPortList().getLast());
				nodes[dest_num].getPortList().getLast().setConnectedPort(nodes[src_num].getPortList().getLast());
				// LinkTable.addLinkSS(src_num, dest_num);

				Log.ADD_LOG_PANEL("s" + (src_num + 1) + " : " + nodes[src_num].getPortList().size() + "---" + "s"
						+ (dest_num + 1) + " : " + nodes[dest_num].getPortList().size(), this.getClass().toString());

			} else {// between switch and host

				if (src_type.equals("s")) {

					nodes[src_num].addPort();

					if (portToHost_count == 2) {
						for (int n = 0; n < Global.NODE_SIZE; n++) {
							nodes[src_num].getPortList().getLast().addConnectedHostToList(dstHosts[n]);
						}
						portToHost_count--;
					} else {
						for (int n = 0; n < Global.NODE_SIZE; n++) {
							nodes[src_num].getPortList().getLast().addConnectedHostToList(srcHosts[n]);
						}
					}

					Log.ADD_LOG_PANEL(
							"s" + (src_num + 1) + " : " + nodes[src_num].getPortList().size() + "---" + "100h",
							this.getClass().toString());
				} else {

					nodes[dest_num].addPort();

					if (portToHost_count == 2) {
						for (int n = 0; n < Global.NODE_SIZE; n++) {
							nodes[dest_num].getPortList().getLast().addConnectedHostToList(dstHosts[n]);
						}
						portToHost_count--;
					} else {
						for (int n = 0; n < Global.NODE_SIZE; n++) {
							nodes[dest_num].getPortList().getLast().addConnectedHostToList(srcHosts[n]);
						}
					}
					Log.ADD_LOG_PANEL(
							"s" + (dest_num + 1) + " : " + nodes[dest_num].getPortList().size() + "---" + "100h",
							this.getClass().toString());
				}

			}
		}

		// 最前和最后node赋给global变量
		Global.ROOTNODE = nodes[0];
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];
		Global.nodes = nodes;
		Global.srcHosts = srcHosts;
		Global.dstHosts = dstHosts;

		// start netty
		for (int i = 0; i < switch_count; i++) {
			ConnectionThread ct = new ConnectionThread(nodes[i], Global.SDN_CONTROLLER_IP[0],
					Global.SDN_CONTROLLER_PORT);
			ct.start();
		}
		return nodes;
	}

	private Node[] create_linearTopo() {
		// no host
		Node[] nodes = null;
		nodes = new Node[Global.NUMBER_OF_TEST_SWITCH];

		// create node
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i] = new Node((long) dpid++, i);
			nodes[i].creatPort(2);
		}

		// create link
		for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].getPortList().get(0).setConnectedPort(nodes[i - 1].getPortList().get(1));

			nodes[i - 1].getPortList().get(1).setConnectedPort(nodes[i].getPortList().get(0));
			nodes[i - 1].setNextNode(nodes[i]);
		}

		// 最前和最后node赋给global变量
		Global.ROOTNODE = nodes[0];
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];
		Global.nodes = nodes;

		// start netty
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			ConnectionThread ct = new ConnectionThread(nodes[i], Global.SDN_CONTROLLER_IP[0],
					Global.SDN_CONTROLLER_PORT);
			ct.start();
		}

		return nodes;
	}

	private Node[] create_ringTopo() {
		// no host
		Node[] nodes = null;
		nodes = new Node[Global.NUMBER_OF_TEST_SWITCH];

		// create node
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i] = new Node((long) dpid++, i);
			nodes[i].creatPort(2);
		}
		// create link
		for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].getPortList().get(0).setConnectedPort(nodes[i - 1].getPortList().get(1));

			nodes[i - 1].getPortList().get(1).setConnectedPort(nodes[i].getPortList().get(0));
			nodes[i - 1].setNextNode(nodes[i]);
		}

		nodes[Global.NUMBER_OF_TEST_SWITCH - 1].getPortList().get(1).setConnectedPort(nodes[0].getPortList().get(0));
		nodes[0].getPortList().get(0).setConnectedPort(nodes[Global.NUMBER_OF_TEST_SWITCH - 1].getPortList().get(1));
		nodes[Global.NUMBER_OF_TEST_SWITCH - 1].setNextNode(nodes[0]);

		// 最前和最后node赋给global变量
		Global.ROOTNODE = nodes[0];
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];
		Global.nodes = nodes;

		// start netty
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			ConnectionThread ct = new ConnectionThread(nodes[i], Global.SDN_CONTROLLER_IP[0],
					Global.SDN_CONTROLLER_PORT);
			ct.start();
		}

		return nodes;
	}

}
