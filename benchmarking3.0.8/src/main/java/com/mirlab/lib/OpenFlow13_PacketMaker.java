package com.mirlab.lib;

import java.util.EnumSet;
import java.util.Random;

import org.projectfloodlight.openflow.protocol.OFBarrierReply;
import org.projectfloodlight.openflow.protocol.OFBarrierRequest;
import org.projectfloodlight.openflow.protocol.OFCapabilities;
import org.projectfloodlight.openflow.protocol.OFControllerRole;
import org.projectfloodlight.openflow.protocol.OFEchoReply;
import org.projectfloodlight.openflow.protocol.OFEchoRequest;
import org.projectfloodlight.openflow.protocol.OFFactories;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFeaturesReply;
import org.projectfloodlight.openflow.protocol.OFFeaturesRequest;
import org.projectfloodlight.openflow.protocol.OFGetConfigReply;
import org.projectfloodlight.openflow.protocol.OFGetConfigRequest;
import org.projectfloodlight.openflow.protocol.OFHello;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFOxmList;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketInReason;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.projectfloodlight.openflow.protocol.OFPortFeatures;
import org.projectfloodlight.openflow.protocol.OFRoleReply;
import org.projectfloodlight.openflow.protocol.OFRoleRequest;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.oxm.OFOxmInPort;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TableId;
import org.projectfloodlight.openflow.types.U64;

import com.mirlab.component.Node;
import com.mirlab.component.Port;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年8月3日 下午2:01:15 类说明
 */
public class OpenFlow13_PacketMaker {
	public static OFFactory factory = OFFactories.getFactory(OFVersion.OF_13);

	public static OFMessage HELLO() {
		OFHello hello = factory.buildHello().build();
		return hello;
	}

	public static OFMessage ECHO_REQUEST() {
		OFEchoRequest echoReq = factory.buildEchoRequest().build();
		return echoReq;
	}

	public static OFMessage ECHO_REPLY(OFEchoRequest echoQuest) {
		OFEchoReply echoReply = factory.buildEchoReply().setXid(echoQuest.getXid()).setData(echoQuest.getData())
				.build();
		return echoReply;
	}

	public static OFMessage FEATURES_REPLY(OFFeaturesRequest featuresReq, Node node) {
		OFFeaturesReply featuresReply = factory.buildFeaturesReply().setXid(featuresReq.getXid())
				.setDatapathId(DatapathId.of(node.getDpid())).setNBuffers((long) 256).setNTables((short) 254)
				.setCapabilities(EnumSet.<OFCapabilities>of(OFCapabilities.FLOW_STATS, OFCapabilities.TABLE_STATS,
						OFCapabilities.PORT_STATS, OFCapabilities.QUEUE_STATS))
				.build();
		return featuresReply;
	}

	public static OFMessage ROLE_REPLY(OFRoleRequest roleReq) {
		OFRoleReply roleReply = factory.buildRoleReply().setXid(roleReq.getXid()).setRole(OFControllerRole.ROLE_MASTER)
				.setGenerationId(U64.ZERO).build();
		return roleReply;
	}

	public static OFMessage BARRIER_REPLY(OFBarrierRequest barrierReq) {
		OFBarrierReply barrierReply = factory.buildBarrierReply().setXid(barrierReq.getXid()).build();
		return barrierReply;
	}

	public static OFMessage GET_CONFIG_REPLY(OFGetConfigRequest getConfigReq) {
		OFGetConfigReply getConfigReply = factory.buildGetConfigReply().setXid(getConfigReq.getXid())
				.setMissSendLen(65535).build();
		return getConfigReply;
	}

	public static OFMessage PACKET_IN(Port port, byte[] data) {
		// openflow packetIn
		OFOxmInPort oxe = factory.oxms().inPort(OFPort.of(port.getPortNum()));

		OFOxmList oxmList = OFOxmList.of(oxe);

		Match m = factory.buildMatchV3().setOxmList(oxmList).build();

		OFPacketIn ofpi = factory.buildPacketIn().setMatch(m).setBufferId(OFBufferId.NO_BUFFER).setTotalLen(data.length)
				.setXid((long) 0).setReason(OFPacketInReason.ACTION).setTableId(TableId.of(0)).setData(data).build();
		return ofpi;
	}

	public static OFPortDesc PORT_DESC(String name, int portNo) {
		Random randomno = new Random();

		OFPortDesc ofpd = factory.buildPortDesc() // builder
				.setName(name)
				.setPortNo(OFPort.of(portNo))
				.setHwAddr(MacAddress.of(randomno.nextLong()))// random value -> type: long
				.setCurr(EnumSet.<OFPortFeatures>of(OFPortFeatures.PF_10GB_FD, OFPortFeatures.PF_COPPER))
				.setCurrSpeed((long) 10000000).build();

		return ofpd;
	}
}
