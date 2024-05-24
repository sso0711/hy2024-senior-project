package com.mirlab.temp;

import org.projectfloodlight.openflow.protocol.OFMessage;

import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.PacketMaker_NEW;
import com.mirlab.openflow.ConnectionThread;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年8月4日 下午2:42:18 类说明
 */
public class temp {
	public static void main(String[] args) {
		Node node1 = new Node((long) 1, 1);
		node1.addPort();

		Node node2 = new Node((long) 2, 2);
		node2.creatPort(2);

		Host host1 = new Host("10.0.0.1", "aa:aa:aa:aa:aa:aa");
		Host host2 = new Host("10.0.0.2", "aa:aa:aa:aa:aa:a1");

		node1.getPortList().getLast().setConnectedPort(node2.getPortList().get(0));
		node2.getPortList().getFirst().setConnectedPort(node1.getPortList().get(0));

		node1.addPort();
		node1.getPortList().getLast().addConnectedHostToList(host1);
		node2.getPortList().get(1).addConnectedHostToList(host2);

		ConnectionThread ct1 = new ConnectionThread(node1, "192.168.1.109", 6633);
		ct1.start();

		ConnectionThread ct2 = new ConnectionThread(node2, "192.168.1.109", 6633);
		ct2.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] arpPacket1 = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, host1, host2);
		OFMessage ofpi1 = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(node1.getPortList().getLast(), arpPacket1);
		// node1.sendPacket(ofpi1);
		node1.sendPacketIn(ofpi1);

		byte[] arpPacket2 = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, host2, host1);
		OFMessage ofpi2 = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(node2.getPortList().get(1), arpPacket2);
		// node2.sendPacket(ofpi2);
		node2.sendPacket(ofpi2);
	}
}
