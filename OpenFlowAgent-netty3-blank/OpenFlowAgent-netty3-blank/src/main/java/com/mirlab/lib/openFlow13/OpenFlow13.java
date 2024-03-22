package com.mirlab.lib.openFlow13;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.projectfloodlight.openflow.protocol.OFBarrierReply;
import org.projectfloodlight.openflow.protocol.OFCapabilities;
import org.projectfloodlight.openflow.protocol.OFEchoReply;
import org.projectfloodlight.openflow.protocol.OFEchoRequest;
import org.projectfloodlight.openflow.protocol.OFFactories;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFeaturesReply;
import org.projectfloodlight.openflow.protocol.OFFlowStatsEntry;
import org.projectfloodlight.openflow.protocol.OFGetConfigReply;
import org.projectfloodlight.openflow.protocol.OFHello;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFOxmList;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketInReason;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.OFPortConfig;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.projectfloodlight.openflow.protocol.OFPortFeatures;
import org.projectfloodlight.openflow.protocol.OFPortState;
import org.projectfloodlight.openflow.protocol.OFPortStatsEntry;
import org.projectfloodlight.openflow.protocol.OFRoleReply;
import org.projectfloodlight.openflow.protocol.OFRoleRequest;
import org.projectfloodlight.openflow.protocol.OFSetConfig;
import org.projectfloodlight.openflow.protocol.OFStatsReply;
import org.projectfloodlight.openflow.protocol.OFStatsRequest;
import org.projectfloodlight.openflow.protocol.OFTableStatsEntry;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.action.OFActionOutput;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.oxm.OFOxmInPort;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TableId;
import org.projectfloodlight.openflow.types.U64;

import com.mirlab.component.Agent;
import com.mirlab.component.Port;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年12月29日 下午5:51:32 类说明 OpenFlow 1.3 packet maker
 */
public class OpenFlow13 {
	private static OFFactory factory = OFFactories.getFactory(OFVersion.OF_13);

	// Generating OpenFlow 1.3 Hello message packet
	public static OFMessage hello() {
		return factory.buildHello().setXid((long) 1).build();
	}

	// Generating OpenFlow 1.3 Hello message packet with Xid
	public static OFHello hello(OFMessage msg) {
		return factory.buildHello().setXid(msg.getXid()).build();
	}

	// Generating OpenFlow 1.3 Echo Request message packet
	public static OFEchoRequest echoRequest() {
		return factory.buildEchoRequest().setXid(0L).build();
	}

	// Generating OpenFlow 1.3 Echo Reply message packet
	public static OFEchoReply echoReply(OFMessage msg) {
		OFEchoRequest echoRequest = (OFEchoRequest) msg;
		return factory.buildEchoReply().setXid(echoRequest.getXid()).setData(echoRequest.getData()).build();
	}

	// Generating OpenFlow 1.3 Features Reply message packet
	public static OFFeaturesReply featuresReply(OFMessage msg, Agent agent) {
		return factory.buildFeaturesReply().setXid(msg.getXid())
				.setDatapathId(DatapathId.of(agent.configuration.getDatapathId()))
				.setNBuffers(agent.configuration.getN_buffers()).setNTables(agent.configuration.getN_tables())
				.setCapabilities(EnumSet.<OFCapabilities>of(OFCapabilities.FLOW_STATS, OFCapabilities.TABLE_STATS,
						OFCapabilities.PORT_STATS, OFCapabilities.QUEUE_STATS))
				.build();
	}

	// Generating OpenFlow 1.3 Stats Reply message packet
	public static OFStatsReply statsReply(OFMessage msg, Agent agent) {
		@SuppressWarnings("unchecked")
		OFStatsRequest<OFStatsReply> statsRequest = (OFStatsRequest<OFStatsReply>) msg;

		switch (statsRequest.getStatsType()) {
		case DESC:
			return factory.buildDescStatsReply().setXid(statsRequest.getXid())
					.setMfrDesc(agent.configuration.getManufacturerDesc())
					.setHwDesc(agent.configuration.getHardwareDesc()).setSwDesc(agent.configuration.getSoftwareDesc())
					.setSerialNum(agent.configuration.getSerialNo()).setDpDesc(agent.configuration.getDatapathDesc())
					.build();
		case FLOW:
			return factory.buildFlowStatsReply().setXid(statsRequest.getXid()).setEntries(flowStatsEntry(agent))
					.build();
		case TABLE:
			return factory.buildTableStatsReply().setXid(statsRequest.getXid()).setEntries(tableStatsEntry(agent))
					.build();
		case PORT:
			return factory.buildPortStatsReply().setXid(statsRequest.getXid()).setEntries(portStatsEntry(agent))
					.build();
		case GROUP:
			return factory.buildGroupStatsReply().setXid(statsRequest.getXid()).build();
		case GROUP_DESC:
			return factory.buildGroupDescStatsReply().setXid(statsRequest.getXid()).build();
		case METER:
			return factory.buildMeterStatsReply().setXid(statsRequest.getXid()).build();
		case PORT_DESC:
			return factory.buildPortDescStatsReply().setXid(statsRequest.getXid()).setEntries(portDesc(agent)).build();
		default:
			break;
		}
		return null;
	}

	// Generating OpenFlow 1.3 Barrier Reply message packet
	public static OFBarrierReply barrierReply(OFMessage msg) {
		return factory.buildBarrierReply().setXid(msg.getXid()).build();
	}

	// Setting Config
	public static void setConfig(OFMessage msg, Agent agent) {
		OFSetConfig setConfig = (OFSetConfig) msg;
		agent.configuration.setMissSendLen(setConfig.getMissSendLen());
	}

	// Generating OpenFlow 1.3 Get Config Reply message packet
	public static OFGetConfigReply getConfigReply(OFMessage msg, Agent agent) {
		return factory.buildGetConfigReply().setXid(msg.getXid()).setMissSendLen(agent.configuration.getMissSendLen())
				.build();
	}

	// Generating OpenFlow 1.3 Role Reply message packet
	public static OFRoleReply roleReply(OFMessage msg) {
		OFRoleRequest roleRequest = (OFRoleRequest) msg;
		return factory.buildRoleReply().setXid(roleRequest.getXid()).setRole(roleRequest.getRole())
				.setGenerationId(roleRequest.getGenerationId()).build();
	}

	// PacketOut
	public static void packetOut(OFMessage msg, Agent agent) {
		OFPacketOut packetOut = (OFPacketOut) msg;
		List<OFAction> action = packetOut.getActions();

		for (int i = 0; i < action.size(); i++) {
			OFActionOutput actionOutput = (OFActionOutput) action.get(i);
			int output = actionOutput.getPort().getPortNumber();

			if (agent.getPort(output).connectedPort != null) {
				OFPacketIn packetIn = packetIn(packetOut.getData(), agent.getPortConnectedPort(output));
				agent.getPortConnectedAgent(output).sendOFMessage(packetIn);
			}
		}
	}

	// Generating OpenFlow 1.3 PacketIn message packet
	public static OFPacketIn packetIn(byte[] data, Port port) {
		OFOxmInPort oxe = factory.oxms().inPort(OFPort.of(port.getPortNo()));

		OFOxmList oxmList = OFOxmList.of(oxe);

		Match match = factory.buildMatchV3().setOxmList(oxmList).build();

		return factory.buildPacketIn().setXid((long) 0).setMatch(match).setReason(OFPacketInReason.ACTION)
				.setTableId(TableId.ZERO).setBufferId(OFBufferId.NO_BUFFER).setTotalLen(data.length).setData(data)
				.build();
	}

	// Generating OpenFlow 1.3 Port Desc message
	private static List<OFPortDesc> portDesc(Agent agent) {
		List<OFPortDesc> list = new ArrayList<OFPortDesc>();

		OFPortDesc portDesc = factory.buildPortDesc().setName(agent.getPort(0).getName())
				.setPortNo(OFPort.of(agent.getPort(0).getPortNo()))
				.setHwAddr(MacAddress.of(agent.getPort(0).getMacAddress()))
				.setConfig(EnumSet.<OFPortConfig>of(OFPortConfig.PORT_DOWN))
				.setState(EnumSet.<OFPortState>of(OFPortState.LINK_DOWN)).setCurrSpeed((long) 10000000).build();
		list.add(portDesc);

		for (int i = 1; i < agent.portList.size(); i++) {
			portDesc = factory.buildPortDesc().setName(agent.getPort(i).getName())
					.setPortNo(OFPort.of(agent.getPort(i).getPortNo()))
					.setHwAddr(MacAddress.of(agent.getPort(i).getMacAddress()))
					.setCurr(EnumSet.<OFPortFeatures>of(OFPortFeatures.PF_10GB_FD, OFPortFeatures.PF_COPPER))
					.setCurrSpeed((long) 10000000).build();
			list.add(portDesc);
		}
		return list;
	}

	// Generating OpenFlow 1.3 Port Stats message
	private static List<OFPortStatsEntry> portStatsEntry(Agent agent) {
		List<OFPortStatsEntry> list = new ArrayList<OFPortStatsEntry>();

		for (int i = 0; i < agent.portList.size(); i++) {
			OFPortStatsEntry portStatsEntry = factory.buildPortStatsEntry()
					.setPortNo(OFPort.of(agent.getPort(i).getPortNo())).setDurationSec((long) 153)
					.setDurationNsec((long) 211000000).build();
			list.add(portStatsEntry);
		}

		return list;
	}

	// Generating OpenFlow 1.3 Table Stats message
	private static List<OFTableStatsEntry> tableStatsEntry(Agent agent) {
		List<OFTableStatsEntry> list = new ArrayList<OFTableStatsEntry>();

		for (int i = 0; i < agent.configuration.getN_tables(); i++) {
			OFTableStatsEntry tableStatsEntry = factory.buildTableStatsEntry().setTableId(TableId.of(i))
					.setActiveCount(0L).setLookupCount(U64.of(0)).setMatchedCount(U64.of(0)).build();
			list.add(tableStatsEntry);
		}

		return list;
	}

	// Generating OpenFlow 1.3 Flow Stats message
	private static List<OFFlowStatsEntry> flowStatsEntry(Agent agent) {
		// debug
		List<OFFlowStatsEntry> list = new ArrayList<OFFlowStatsEntry>();

		for (int i = 0; i < 5; i++) {
			OFFlowStatsEntry flowStatsEntry = factory.buildFlowStatsEntry().build();
			list.add(flowStatsEntry);
		}

		return list;
	}

}
