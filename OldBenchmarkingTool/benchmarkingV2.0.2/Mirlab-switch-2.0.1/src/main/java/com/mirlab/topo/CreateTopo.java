package com.mirlab.topo;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mirlab.component.Host;
import com.mirlab.component.Node;
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
		return nodes;
	}

	private Node[] create_mininetTopo() {
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

		int link_count = mn_links.length();
		int switch_count = mn_switches.length();

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

		Node[] nodes = new Node[Global.NUMBER_OF_TEST_SWITCH];
		Host[] srcHosts = new Host[Global.NODE_SIZE];
		Host[] dstHosts = new Host[Global.NODE_SIZE];

		// create node
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i] = new Node((long) dpid++, i);
			nodes[i].creatPort(2);
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

		// create link between s and s
		for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].getPortList().get(0).setConnectedPort(nodes[i - 1].getPortList().get(1));

			nodes[i - 1].getPortList().get(1).setConnectedPort(nodes[i].getPortList().get(0));
			nodes[i - 1].setNextNode(nodes[i]);
		}

		// create link between s and h
		for (int n = 0; n < srcHosts.length; n++) {
			nodes[0].getPortList().get(0).addConnectedHostToList(srcHosts[n]);
		}
		for (int n = 0; n < dstHosts.length; n++) {
			nodes[nodes.length - 1].getPortList().get(1).addConnectedHostToList(dstHosts[n]);
		}

		// 最前和最后node赋给global变量
		Global.ROOTNODE = nodes[0];
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];
		Global.nodes = nodes;
		Global.srcHosts = srcHosts;
		Global.dstHosts = dstHosts;

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
		Node[] nodes = new Node[Global.NUMBER_OF_TEST_SWITCH];
		Host[] srcHosts = new Host[Global.NODE_SIZE];
		Host[] dstHosts = new Host[Global.NODE_SIZE];

		// create node
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i] = new Node((long) dpid++, i);
			nodes[i].creatPort(2);
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

		// create link
		for (int i = 1; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			nodes[i].getPortList().get(0).setConnectedPort(nodes[i - 1].getPortList().get(1));

			nodes[i - 1].getPortList().get(1).setConnectedPort(nodes[i].getPortList().get(0));
			nodes[i - 1].setNextNode(nodes[i]);
		}

		// create link between s and h
		nodes[0].addPort();
		for (int n = 0; n < srcHosts.length; n++) {
			nodes[0].getPortList().getLast().addConnectedHostToList(srcHosts[n]);
		}
		nodes[nodes.length / 2].addPort();
		for (int n = 0; n < dstHosts.length; n++) {
			nodes[nodes.length / 2].getPortList().getLast().addConnectedHostToList(dstHosts[n]);
		}

		nodes[Global.NUMBER_OF_TEST_SWITCH - 1].getPortList().get(1).setConnectedPort(nodes[0].getPortList().get(0));
		nodes[0].getPortList().get(0).setConnectedPort(nodes[Global.NUMBER_OF_TEST_SWITCH - 1].getPortList().get(1));
		nodes[Global.NUMBER_OF_TEST_SWITCH - 1].setNextNode(nodes[0]);

		// 最前和最后node赋给global变量
		Global.ROOTNODE = nodes[0];
		Global.LEAFNODE = nodes[Global.NUMBER_OF_TEST_SWITCH - 1];
		Global.nodes = nodes;
		Global.srcHosts = srcHosts;
		Global.dstHosts = dstHosts;

		// start netty
		for (int i = 0; i < Global.NUMBER_OF_TEST_SWITCH; i++) {
			ConnectionThread ct = new ConnectionThread(nodes[i], Global.SDN_CONTROLLER_IP[0],
					Global.SDN_CONTROLLER_PORT);
			ct.start();
		}

		return nodes;
	}

}
