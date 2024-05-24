package com.mirlab.lib;

import java.util.LinkedList;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFOxmList;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketInReason;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.oxm.OFOxmInPort;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TableId;

import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.component.Port;
import com.mirlab.global.Global;
import com.mirlab.protocol.ARP;
import com.mirlab.protocol.EthernetHeader;
import com.mirlab.protocol.Ip;

public class PacketMaker {

	public static OFMessage MAKE_PACKET(int packetType, Node node,
			LinkedList<OFMessage> l) {
		OFMessage temp = null;

		switch (packetType) {
		case Global.IP_PACKET:
			temp = MAKE_IP_PACKET(node, l);
			break;

		case Global.ARP_REPLY_GRATUITOUS:

			
		
		case Global.ARP_REPLY:
		
		case Global.ARP_REQUEST:
			temp = MAKE_ARP_PACKET(packetType, node, l);
			break;
		

		default:
			break;

		}

		return temp;

	}

	private static OFMessage MAKE_ARP_PACKET(int packetType, Node node,
			LinkedList<OFMessage> l) {
		Port dstPort = null;
		Port srcPort = null;
		OFMessage ofpi = null;
		Node dstNode = null;



		for (int i = 0; i < node.getPortList().size(); i++) {
			if (node.getPortList().get(i).getConnectedHost() != null) {
				srcPort = node.getPortList().get(i);
			}
		}

		// if (Global.TOPO_TYPE == Global.TOPO_LINEAR && node ==
		// Global.LEAFNODE) {
		// dstNode = Global.ROOTNODE;
		// } else {

		// }

		if (packetType == Global.ARP_REPLY_GRATUITOUS) {
			dstNode = node;
		} else if (packetType == Global.ARP_REQUEST) {
			if (Global.TOPO_TYPE == Global.TOPO_LINEAR
					&& node == Global.LEAFNODE) {
				dstNode = Global.ROOTNODE;
			} else {
				dstNode = node.getPortList().get(1).getConnectedPort()
						.getBelong2Node();
			}
		}else if(packetType == Global.ARP_REPLY){
			
		}

		for (int i = 0; i < dstNode.getPortList().size(); i++) {
			if (dstNode.getPortList().get(i).getConnectedHost() != null) {
				dstPort = dstNode.getPortList().get(i);
			}
		}

		
		if (srcPort != null && dstPort != null) {

			Host srcHost = srcPort.getConnectedHost();
			Host dstHost = dstPort.getConnectedHost();
			byte tempPacket[] = MAKE_ARP_PACKET_BYTE(packetType, srcHost,
					dstHost);

			ofpi = MAKE_OPENFLOW_PACKET_IN(srcPort, tempPacket,
					OFPacketInReason.ACTION);


		}
		
		if (l != null && ofpi != null) {
			for (int i = 0; i < Global.BUFF_SIZE; i++) {
				l.add(ofpi);
			}
		}

		return ofpi;
	}

	private static OFMessage MAKE_IP_PACKET(Node node, LinkedList<OFMessage> l) {
		Port dstPort = null;
		Port srcPort = null;
		OFMessage ofpi = null;
		Node dstNode = null;

		System.out.println("NodeID"+ node.getNodeId());
		for (int i = 0; i < node.getPortList().size(); i++) {
			if (node.getPortList().get(i).getConnectedHost() != null) {
				srcPort = node.getPortList().get(i);
			}
		}

		if (Global.TOPO_TYPE == Global.TOPO_LINEAR && node == Global.LEAFNODE) {
			dstNode = Global.ROOTNODE;
		} else {
			dstNode = node.getPortList().get(1).getConnectedPort()
					.getBelong2Node();
		}

		for (int i = 0; i < dstNode.getPortList().size(); i++) {
			if (dstNode.getPortList().get(i).getConnectedHost() != null) {
				dstPort = dstNode.getPortList().get(i);
			}
		}

		if (srcPort != null && dstPort != null) {
			Host srcHost = srcPort.getConnectedHost();
			Host dstHost = dstPort.getConnectedHost();
			byte tempPacket[] = MAKE_IP_PACKET_BYTE(srcHost, dstHost);
			ofpi = MAKE_OPENFLOW_PACKET_IN(srcPort, tempPacket,
					OFPacketInReason.ACTION);

			// add to buff
			if (l != null && ofpi != null) {
				for (int i = 0; i < Global.BUFF_SIZE; i++) {
					l.add(ofpi);
				}
			}

		}
		return ofpi;

	}

	private static byte[] MAKE_IP_PACKET_BYTE(Host srcHost, Host dstHost) {
		byte tempPacket[] = new byte[64];
		EthernetHeader e = new EthernetHeader();
		byte[] tempHeader;
		byte[] tempIp;

		e.setSourceMac(srcHost.getMac());
		e.setDestinationMac(dstHost.getMac());
		e.setEthernetType("ip");
		tempHeader = e.toByte();

		Ip ip = new Ip();
		ip.setSrcIp(srcHost.getIp());
		ip.setDstIp(dstHost.getIp());
		tempIp = ip.toByte();

		// ethernet header
		for (int i = 0; i < 14; i++) {
			tempPacket[i] = tempHeader[i];
		}

		for (int i = 0; i < 50; i++) {
			tempPacket[i + 14] = tempIp[i];
		}

		return tempPacket;
	}

	private static OFMessage MAKE_OPENFLOW_PACKET_IN(Port port,
			byte[] tempPacket, OFPacketInReason r) {
		// openflow
		OFOxmInPort oxe = Global.FACTORY.oxms().inPort(
				OFPort.of(port.getPortNum()));

		OFOxmList oxmList = OFOxmList.of(oxe);

		Match m = Global.FACTORY.buildMatchV3().setOxmList(oxmList).build();

		OFPacketIn ofpi = Global.FACTORY.buildPacketIn().setMatch(m)
				.setBufferId(OFBufferId.NO_BUFFER)
				.setTotalLen(tempPacket.length).setXid((long) 0).setReason(r)
				.setTableId(TableId.of(0)).setData(tempPacket).build();
		return ofpi;
	}

	public static byte[] MAKE_ARP_PACKET_BYTE(int op, Host src, Host dst) {

		byte tempPacket[] = new byte[60];
		EthernetHeader e = new EthernetHeader();
		ARP arp = new ARP();
		byte[] tempHeader;
		byte[] tempArp;
		switch (op) {
		// request
		case Global.ARP_REQUEST:
			e.setSourceMac(src.getMac());
			e.setDestinationMac("ff:ff:ff:ff:ff:ff");
			e.setEthernetType("arp");
			tempHeader = e.toByte();

			arp.setSendIp(src.getIp());
			arp.setSendMac(src.getMac());
			arp.setTargetIp(dst.getIp());
			arp.setTargetMac("00:00:00:00:00:00");
			arp.setOpCode(1);

			tempArp = arp.toByte();

			// ethernet header
			for (int i = 0; i < 14; i++) {
				tempPacket[i] = tempHeader[i];
			}

			// Arp payload
			for (int i = 0; i < 28; i++) {
				tempPacket[i + 14] = tempArp[i];
			}

			// ethernet trailer
			for (int i = 42; i < 60; i++) {
				tempPacket[i] = 0x00;
			}
			break;

		// reply
		case Global.ARP_REPLY:
			break;

		// gratuitous
		case Global.ARP_REPLY_GRATUITOUS:
			e.setSourceMac(src.getMac());
			e.setDestinationMac("ff:ff:ff:ff:ff:ff");
			e.setEthernetType("arp");
			tempHeader = e.toByte();

			arp.setSendIp(src.getIp());
			arp.setSendMac(src.getMac());
			arp.setTargetIp(src.getIp());
			arp.setTargetMac(src.getMac());
			arp.setOpCode(2);

			tempArp = arp.toByte();

			// ethernet header
			for (int i = 0; i < 14; i++) {
				tempPacket[i] = tempHeader[i];
			}

			// Arp payload
			for (int i = 0; i < 28; i++) {
				tempPacket[i + 14] = tempArp[i];
			}

			// ethernet trailer
			for (int i = 42; i < 60; i++) {
				tempPacket[i] = 0x00;
			}
			break;
		}

		return tempPacket;
	}
}
