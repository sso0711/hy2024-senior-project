package com.mirlab.lib;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.projectfloodlight.openflow.protocol.OFBadRequestCode;
import org.projectfloodlight.openflow.protocol.OFBarrierReply;
import org.projectfloodlight.openflow.protocol.OFCapabilities;
import org.projectfloodlight.openflow.protocol.OFControllerRole;
import org.projectfloodlight.openflow.protocol.OFEchoReply;
import org.projectfloodlight.openflow.protocol.OFEchoRequest;
import org.projectfloodlight.openflow.protocol.OFFeaturesReply;
import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.projectfloodlight.openflow.protocol.OFGetConfigReply;
import org.projectfloodlight.openflow.protocol.OFHello;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.projectfloodlight.openflow.protocol.OFPortDescStatsReply;
import org.projectfloodlight.openflow.protocol.OFPortStatsEntry;
import org.projectfloodlight.openflow.protocol.OFPortStatsReply;
import org.projectfloodlight.openflow.protocol.OFRoleReply;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.OFErrorCauseData;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.U64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.southbound.tasks.AsynchronousMessageProcessingRate;
import com.southbound.tasks.AsynchronousMessageProcessingTime;
import com.southbound.tasks.ReactivePathProvisioningRate;
import com.southbound.tasks.ReactivePathProvisioningTime;
import com.southbound.tasks.TopologyChangeDetection;

public class PacketHandler {
	public static Logger logger = LoggerFactory.getLogger(PacketHandler.class);
	public static int COUNT = 0;

	public static void HANDEL_HELLO_IN_PACKET(ChannelHandlerContext ctx) {

		OFHello hello = Global.FACTORY.buildHello().build();
		SEND_MSG(ctx, hello);
		// ctx.getChannel().write(hello);

	}

	public static void HANDEL_HELLO_OUT_PACKET(ChannelHandlerContext ctx, OFMessage ofMessage, Node node) {

		if (Global.TEST_METRIC == Global.CONTROL_SESSION_CAPACITY_CCD) {
			node.setHasReceivedHello(true);
		}

	}

	public static void HANDEL_ECHO_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage) {
		OFEchoRequest echoQuest = (OFEchoRequest) ofMessage;

		OFEchoReply echoReply = Global.FACTORY.buildEchoReply().setXid(ofMessage.getXid()).build();
		SEND_MSG(ctx, echoReply);
		// ctx.getChannel().write(echoReply);
	}

	public static void HANDEL_FEATURES_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage, Node node) {

		OFFeaturesReply featuresReply = Global.FACTORY.buildFeaturesReply().setXid(ofMessage.getXid())
				.setDatapathId(DatapathId.of(node.getDpid())).setNBuffers((long) 256).setNTables((short) 254)
				.setCapabilities(EnumSet.<OFCapabilities>of(OFCapabilities.FLOW_STATS, OFCapabilities.TABLE_STATS,
						OFCapabilities.PORT_STATS, OFCapabilities.QUEUE_STATS))
				.build();

		// ctx.getChannel().write(featuresReply);
		SEND_MSG(ctx, featuresReply);
	}

	public static void HANDEL_ROLE_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage) {
		OFRoleReply roleReply = Global.FACTORY.buildRoleReply().setXid(ofMessage.getXid())
				.setRole(OFControllerRole.ROLE_MASTER).setGenerationId(U64.ZERO).build();
		// ctx.getChannel().write(roleReply);
		SEND_MSG(ctx, roleReply);

	}

	public static void HANDEL_BARRIER_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage) {
		OFBarrierReply barrierReply = Global.FACTORY.buildBarrierReply().setXid(ofMessage.getXid()).build();
		// ctx.getChannel().write(barrierReply);
		SEND_MSG(ctx, barrierReply);
	}

	public static void HANDEL_GET_CONFIG_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage) {
		OFGetConfigReply getConfigReply = Global.FACTORY.buildGetConfigReply().setXid(ofMessage.getXid())
				.setMissSendLen(65535).build();
		SEND_MSG(ctx, getConfigReply);
		// ctx.getChannel().write(getConfigReply);
	}

	public static void HANDEL_PACKET_OUT(OFMessage ofMessage, final Node node) {

		OFPacketOut packetOut = (OFPacketOut) ofMessage;
		byte[] data = packetOut.getData();
		String action = packetOut.getActions().get(0).toString();
		int actionPortNum = Integer.parseInt(action.substring(action.indexOf("=") + 1, action.indexOf(","))) - 1;

		try {

			if (node.getPortList().get(actionPortNum).getConnectedPort() != null) {
				// this port connects with another node's port
				if (node.getPortList().get(actionPortNum).getPortState() == 0
						&& node.getPortList().get(actionPortNum).getConnectedPort().getPortState() == 0
						&& node.getPortList().get(actionPortNum).getLinkState() == 0
						&& node.getPortList().get(actionPortNum).getConnectedPort().getLinkState() == 0) {

					OFMessage ofpi = PacketMaker_NEW
							.MAKE_OPENFLOW_PACKET_IN(node.getPortList().get(actionPortNum).getConnectedPort(), data);

					node.getPortList().get(actionPortNum).getConnectedPort().getBelong2Node().sendPacket(ofpi);
				}
			} else if (node.getPortList().get(actionPortNum).getConnectedHostList() != null) {
				// this port connects with host or hosts

				if (Global.UNKNOWN_PACKET_TYPE != Global.ECHO_PACKET) {

					if (AsynchronousMessageProcessingTime.HAS_STARTED
							|| AsynchronousMessageProcessingRate.HAS_STARTED) {

						Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.add(System.nanoTime());

					}

				}

			} else if (node.getPortList().get(actionPortNum).getDistributedPort() != null) {
				node.getPortList().get(actionPortNum).getDistributedPort().sendByteMessage(data);

			} else {
				// this port connects nothing--error
				logger.debug("error");
			}

			if ((Byte.toUnsignedInt(data[13]) == 0xcc && Byte.toUnsignedInt(data[12]) == 136)
					|| (Byte.toUnsignedInt(data[13]) == 66 && Byte.toUnsignedInt(data[12]) == 137)) {
				// lldp message

				logger.debug("LLDP Received..Action Port is " + actionPortNum);

				// made by haojun 20170406
				if (Global.TEST_METRIC == Global.TOPOLOGY_DISCOVERY_TIME) {

					if (node.getLLDP_OUT().size() < node.getPortList().size() && Tasks.HAS_STARTED
							&& (Byte.toUnsignedInt(data[13]) == 0xcc && Byte.toUnsignedInt(data[12]) == 136)) {

						BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_OUT());// 向getLLDP_OUT添加现在时间

					}

				}

				if (Global.TEST_METRIC == Global.TOPOLOGY_DISCOVERY_TIME) {
					if (Tasks.HAS_STARTED) {

						if ((Byte.toUnsignedInt(data[13]) == 0xcc && Byte.toUnsignedInt(data[12]) == 136)) {
							if (Global.TOPO_TYPE == Global.TOPO_LINEAR) {
								if (node == Global.ROOTNODE || node == Global.LEAFNODE) {
									if (node.getLLDP_IN().size() < 1) {
										BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_IN());
									}
								} else {
									if (node.getLLDP_IN().size() < 2) {
										BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_IN());
									}
								}
							} else {
								if (node.getLLDP_IN().size() < node.getPortList().size()) {
									BenchmarkTimer.ADD_CURRENT_TIME(node.getLLDP_IN());
								}
							}
						}
					}
				}

				// made by haojun 20170406

				// made by haojun 20170407
				if (Global.TEST_METRIC == Global.TOPOLOGY_CHANGE_DETECTION_TIME_LINK_DOWN_UP
						&& (Byte.toUnsignedInt(data[13]) == 0xcc && Byte.toUnsignedInt(data[12]) == 136)) {
					if (TopologyChangeDetection.HAS_STARTED) {

						BenchmarkTimer.ADD_CURRENT_TIME(Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_LLDP);

						if (node == Global.ROOTNODE) {
							new Thread(new Runnable() {

								public void run() {
									node.sendPacket((OFMessage) TopologyChangeDetection.ofmSrcDown);
									node.getPortList().getLast().getConnectedPort().getBelong2Node()
											.sendPacket(TopologyChangeDetection.ofmDstDown);
									node.getPortList().getLast().setLinkState(1);
									node.getPortList().getLast().setPortState(1);
									node.getPortList().getLast().getConnectedPort().setLinkState(1);
									node.getPortList().getLast().getConnectedPort().setPortState(1);
									BenchmarkTimer
											.ADD_CURRENT_TIME(Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_PORTSTATUS);

									try {
										if (Global.CONTROLLER_TYPE == Global.CONTROLLER_TYPE_ONOS) {
											Thread.sleep(3000);
										} else if (Global.CONTROLLER_TYPE == Global.CONTROLLER_TYPE_OPENDAYLIGHT) {
											Thread.sleep(5000);
										} else {

											Thread.sleep(15000);

										}
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									node.sendPacket((OFMessage) TopologyChangeDetection.ofmSrcUp);
									node.getPortList().getLast().getConnectedPort().getBelong2Node()
											.sendPacket((OFMessage) TopologyChangeDetection.ofmDstUp);
									node.getPortList().getLast().setLinkState(0);
									node.getPortList().getLast().setPortState(0);
									node.getPortList().getLast().getConnectedPort().setLinkState(0);
									node.getPortList().getLast().getConnectedPort().setPortState(0);

									BenchmarkTimer
											.ADD_CURRENT_TIME(Result.TOPOLOGY_CHANGE_DETECTION_TIME_LIST_PORTSTATUS);

									try {
										Thread.sleep(5000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// TopologyChangeDetection.IS_COMPLETED =
									// true;
									TopologyChangeDetection.HAS_STARTED = false;
								}
							}).start();

						}

					}

				} else if (Global.TEST_METRIC == Global.NETWORK_DISCOVERY_SIZE_NS
						&& (Byte.toUnsignedInt(data[13]) == 0xcc && Byte.toUnsignedInt(data[12]) == 136)) {
					if (node.getNumberOflldpPacket() < 2) {
						node.numberOflldpPacketPlusOne();
					}
				}

			} else {// asynchronous metric 非lldp message

				logger.debug("Received Packet_out(unknown packet reply)");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\n not writeable! Nodeid: " + node.getNodeId() + "; Port Number: " + actionPortNum + 1);
		}
	}

	public static void HANDEL_STATES_REQUEST(ChannelHandlerContext ctx, OFMessage ofMessage, Node node) {

		if (ofMessage.toString().contains("OFPortDescStats")) {
			// Node Discovery end
			// Timer.ADD_CURRENT_TIME(Global.MULTIPART_REPLY_OFPMP_PORT_DESC_TIME_LIST);

			org.projectfloodlight.openflow.protocol.OFPortDescStatsReply.Builder buildPortDescStats = Global.FACTORY
					.buildPortDescStatsReply();
			List<OFPortDesc> list = new ArrayList<OFPortDesc>();

			for (int i = 0; i < node.getPortList().size(); i++) {
				list.add(node.getPortList().get(i).getPortDesc());
			}

			buildPortDescStats.setEntries(ImmutableList.<OFPortDesc>copyOf(list)).setXid(ofMessage.getXid());
			OFPortDescStatsReply portDescStatsReply = buildPortDescStats.build();

			// ctx.getChannel().write(portDescStatsReply);
			SEND_MSG(ctx, portDescStatsReply);
		} else if (ofMessage.toString().contains("OFDescStats")) {
			SEND_MSG(ctx, Global.FACTORY.buildDescStatsReply().setXid(ofMessage.getXid()).setMfrDesc("mirlab-vswitch")
					.setHwDesc("open vSwitch").setSwDesc("2.0.2").setSerialNum("1").setDpDesc("1").build());
			/*
			 * ctx.getChannel().write( Global.FACTORY.buildDescStatsReply()
			 * .setXid(ofMessage.getXid()) .setMfrDesc("mirlab-vswitch")
			 * .setHwDesc("open vSwitch").setSwDesc("2.0.2")
			 * .setSerialNum("1").setDpDesc("1").build());
			 */

		} else if (ofMessage.toString().contains("OFPortStats")) {
			org.projectfloodlight.openflow.protocol.OFPortStatsReply.Builder buildPortStats = Global.FACTORY
					.buildPortStatsReply();

			List<OFPortStatsEntry> list = new ArrayList<OFPortStatsEntry>();

			for (int i = 0; i < node.getPortList().size(); i++) {
				OFPortStatsEntry pse = Global.FACTORY.buildPortStatsEntry().setPortNo(OFPort.of(i))
						.setDurationSec((long) 153).setDurationNsec((long) 211000000).build();
				list.add(pse);
			}

			buildPortStats.setXid(ofMessage.getXid()).setEntries(ImmutableList.<OFPortStatsEntry>copyOf(list));
			OFPortStatsReply portStatsReply = buildPortStats.build();
			SEND_MSG(ctx, portStatsReply);
			// ctx.getChannel().write(portStatsReply);
		} else if (ofMessage.toString().contains("OFMeterStatsRequest")) {

			SEND_MSG(ctx, Global.FACTORY.buildMeterStatsReply().setXid(ofMessage.getXid()).build());
			// ctx.getChannel().write(Global.FACTORY.buildMeterStatsReply().setXid(ofMessage.getXid()).build());
		} else if (ofMessage.toString().contains("OFGroupDescStatsRequest")) {
			SEND_MSG(ctx, Global.FACTORY.errorMsgs().buildBadRequestErrorMsg().setCode(OFBadRequestCode.BAD_TYPE)
					.setXid(ofMessage.getXid())
					.setData(OFErrorCauseData.of("0007000000000000".getBytes(), Global.FACTORY.getVersion())).build());
			/*
			 * ctx.getChannel().write( Global.FACTORY .errorMsgs()
			 * .buildBadRequestErrorMsg() .setCode(OFBadRequestCode.BAD_TYPE)
			 * .setXid(ofMessage.getXid()) .setData( OFErrorCauseData.of(
			 * "0007000000000000".getBytes(), Global.FACTORY.getVersion())) .build());
			 */
		} else if (ofMessage.toString().contains("OFGroupStatsRequest")) {
			SEND_MSG(ctx,
					Global.FACTORY.errorMsgs().buildBadRequestErrorMsg().setCode(OFBadRequestCode.BAD_TYPE)
							.setXid(ofMessage.getXid()).setData(OFErrorCauseData
									.of("0006000000000000fffffffc00000000".getBytes(), Global.FACTORY.getVersion()))
							.build());
			/*
			 * ctx.getChannel().write( Global.FACTORY .errorMsgs()
			 * .buildBadRequestErrorMsg() .setCode(OFBadRequestCode.BAD_TYPE)
			 * .setXid(ofMessage.getXid()) .setData( OFErrorCauseData.of(
			 * "0006000000000000fffffffc00000000" .getBytes(), Global.FACTORY
			 * .getVersion())).build());
			 */
		} else if (ofMessage.toString().contains("OFTableFeaturesStats")) {
			SEND_MSG(ctx, Global.FACTORY.errorMsgs().buildBadRequestErrorMsg().setCode(OFBadRequestCode.BAD_TYPE)
					.setXid(ofMessage.getXid())
					.setData(OFErrorCauseData.of("0007000000000000".getBytes(), Global.FACTORY.getVersion())).build());
		}

	}

	public static void HANDEL_FLOW_MOD(ChannelHandlerContext ctx, OFMessage ofMessage, final Node node) {
		OFFlowMod flowMod = (OFFlowMod) ofMessage;

		if (Global.TEST_METRIC == Global.REACTIVE_PATH_PROVISIONING_TIME
				|| Global.TEST_METRIC == Global.REACTIVE_PATH_PROVISIONING_RATE) {

			String action = flowMod.getActions().get(0).toString();
			if (action.contains("controller")) {

			} else {
				int actionPortNum = Integer.parseInt(action.substring(action.indexOf("=") + 1, action.indexOf(",")))
						- 1;

				if (node.getPortList().get(actionPortNum).getConnectedPort() != null) {
					// this port connects with another node's port

				} else if (node.getPortList().get(actionPortNum).getConnectedHostList() != null) {
					// this port connects with host or hosts

					if (ReactivePathProvisioningRate.HAS_STARTED || ReactivePathProvisioningTime.HAS_STARTED) {

						if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
							if (COUNT == 1) {
								BenchmarkTimer.ADD_CURRENT_TIME(Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD);
								COUNT = 0;
							}
							COUNT++;

						} else if (Global.PROVISINIONING_PACKET_TYPE == Global.UDP_PACKET
								|| Global.PROVISINIONING_PACKET_TYPE == Global.TCP_PACKET) {
							// udp tcp

							BenchmarkTimer.ADD_CURRENT_TIME(Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD);

						} else {
							System.out.println("unknown packet type");
						}
					}

				} else {
					// this port connects nothing--error
					logger.debug("error");
				}

			}

		} else if (Global.TEST_METRIC == Global.FORWARDING_TABLE_CAPACITY_NRP) {
			if (Tasks.HAS_STARTED) {
				// Global.ROOTNODE.numberOfFlowEntryPlusOne();
				Result.TEMP_NUMBER_OF_FLOW_MOD++;
			}
		}
	}

	public static void SEND_MSG(ChannelHandlerContext ctx, OFMessage ofMessage) {// 发送信息
		if (ctx.getChannel().isOpen()) {
			ctx.getChannel().write(ofMessage);

		} else {
			logger.debug("Channel is not writable!");
		}
	}

	public static void ECHO_REPLY(ChannelHandlerContext ctx, OFMessage ofMessage) {
		if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET
				&& (Global.TEST_METRIC == Global.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME
						|| Global.TEST_METRIC == Global.ASYNCHRONOUS_MESSAGE_PROCESSING_RATE)) {
			if (Tasks.HAS_STARTED) {
				Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.add(System.nanoTime());
			}

		} else {

		}

	}
}
