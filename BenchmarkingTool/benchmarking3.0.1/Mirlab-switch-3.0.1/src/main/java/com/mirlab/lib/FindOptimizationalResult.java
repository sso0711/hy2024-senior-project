package com.mirlab.lib;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.projectfloodlight.openflow.protocol.OFEchoRequest;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketInReason;

import com.mir.ui.Main;
import com.mirlab.component.Host;
import com.mirlab.global.Global;

public class FindOptimizationalResult {

	public static void FIND_METRIC_4_DOUBLE(int TEMP_BULK_SIZE) {

		Global.BULK_SIZE = TEMP_BULK_SIZE;
		Log.ADD_LOG_PANEL("*********************************************", FindOptimizationalResult.class.toString());
		Log.ADD_LOG_PANEL("Current bulk size = " + Global.BULK_SIZE, FindOptimizationalResult.class.toString());

		try {
			Thread.sleep(2000);

			Initializer.INITIAL_2();

			Thread.sleep(2000);

			START_TASK_4();

			ArrayList finalResult;

			if (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size() == 0) {
				finalResult = null;
			} else {

				finalResult = FindOptimizationalResult
						.FIND_SAMPLE(Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT, Global.GAP_TIME, false);
				Log.ADD_LOG_PANEL("Final max Sample Size: " + finalResult.size(), Tasks.class.toString());

			}

			if (finalResult == null) {
				Result.ADD_RESULT("-", 4, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
				Initializer.INITIAL_CHANNEL_POOL();
				return;
			} else if (finalResult.size() == 0) {
				Result.ADD_RESULT("-", 4, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
				Initializer.INITIAL_CHANNEL_POOL();
				return;
			} else if (finalResult.size() < Global.MIN_SAMPLE) {
				Result.ADD_RESULT("-", 6, 2);
				JOptionPane.showMessageDialog(null, "Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						"Please Try it again", JOptionPane.ERROR_MESSAGE);
				Log.ADD_LOG_PANEL("Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						Tasks.class.toString());
				Initializer.INITIAL_CHANNEL_POOL();
				return;

			}

			else {
				double costTime = ((Long) finalResult.get(finalResult.size() - 1) - (Long) finalResult.get(0))
						/ (double) 1000000000;
				Log.ADD_LOG_PANEL(
						"Rate (numberOfSamplePacketOut/sampleCostTime) : " + (finalResult.size()) + " / " + costTime
								+ " = " + Tasks.df.format((finalResult.size()) / ((double) costTime)) + "numbers/s",
						Tasks.class.toString());

				Log.ADD_LOG_PANEL("Packet In: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size(),
						FindOptimizationalResult.class.toString());
				Log.ADD_LOG_PANEL("Packet Out: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size(),
						FindOptimizationalResult.class.toString());
				Log.ADD_LOG_PANEL(
						"Packet Loss Rate: " + Tasks.df.format(
								(double) (1 - ((double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size()
										/ (double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size()))),
						FindOptimizationalResult.class.toString());

				if ((double) 1 - ((double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size()
						/ (double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN
								.size()) > Global.PACKET_LOSS_RATE) {

					if (Global.BULK_SIZE <= 2) {

						Log.ADD_LOG_PANEL(
								"Packet Loss Rate > " + Global.PACKET_LOSS_RATE * 100
										+ "%, The most optimizational bulk size  = 1",
								FindOptimizationalResult.class.toString());

						Global.BULK_SIZE = 1;
						Global.CURRENT_BULK_SIZE = 1;
						Global.BULK_SIZE_OFF_SET = 0;
						Initializer.INITIAL_CHANNEL_POOL();
						// start Tasks
						{
							Initializer.INITIAL_2();
							Tasks.START_TASK_3();
						}
						return;
					} else {

						Log.ADD_LOG_PANEL(
								"Packet Loss Rate > " + Global.PACKET_LOSS_RATE * 100
										+ "%, Restart to find Optimizational Result.. ",
								FindOptimizationalResult.class.toString());

						Global.BULK_SIZE_OFF_SET = Global.CURRENT_BULK_SIZE / 4;

						Global.PRE_BULK_SIZE = Global.CURRENT_BULK_SIZE;

						Global.CURRENT_BULK_SIZE = Global.CURRENT_BULK_SIZE - Global.BULK_SIZE_OFF_SET;
						Initializer.INITIAL_CHANNEL_POOL();
						FIND_METRIC_4_DETAIL(Global.CURRENT_BULK_SIZE);

					}

				} else {

					Global.CURRENT_BULK_SIZE = Global.CURRENT_BULK_SIZE * 2;

					Initializer.INITIAL_CHANNEL_POOL();

					Log.ADD_LOG_PANEL(
							"Packet Loss Rate < " + Global.PACKET_LOSS_RATE * 100
									+ "%, Restart to find Optimizational Result.. ",
							FindOptimizationalResult.class.toString());

					FIND_METRIC_4_DOUBLE(Global.CURRENT_BULK_SIZE);

				}

			}

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void FIND_METRIC_6_DOUBLE(int TEMP_BULK_SIZE) {

		Global.BULK_SIZE = TEMP_BULK_SIZE;
		Log.ADD_LOG_PANEL("*********************************************", FindOptimizationalResult.class.toString());
		Log.ADD_LOG_PANEL("Current bulk size = " + Global.BULK_SIZE, FindOptimizationalResult.class.toString());

		try {
			Thread.sleep(2000);
			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				Initializer.INITIAL_2();
			} else {
				Initializer.INITIAL_0();

			}

			Thread.sleep(2000);

			START_TASK_6();

			ArrayList finalResult;

			if (Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size() == 0) {
				finalResult = null;
			} else {

				finalResult = FindOptimizationalResult.FIND_SAMPLE(Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
						Global.GAP_TIME, false);
				Log.ADD_LOG_PANEL("Final max Sample Size: " + finalResult.size(), Tasks.class.toString());

			}

			if (finalResult == null) {
				Result.ADD_RESULT("-", 6, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
				Initializer.INITIAL_CHANNEL_POOL();
				return;
			} else if (finalResult.size() == 0) {
				Result.ADD_RESULT("-", 6, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controllel! or change Packet type", "Error!",
						JOptionPane.ERROR_MESSAGE);
				Initializer.INITIAL_CHANNEL_POOL();
				return;
			} else if (finalResult.size() < Global.MIN_SAMPLE) {
				Result.ADD_RESULT("-", 6, 2);
				JOptionPane.showMessageDialog(null, "Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						"Please Try it again", JOptionPane.ERROR_MESSAGE);
				Log.ADD_LOG_PANEL("Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						Tasks.class.toString());
				Initializer.INITIAL_CHANNEL_POOL();
				return;

			} else {
				double costTime = ((Long) finalResult.get(finalResult.size() - 1) - (Long) finalResult.get(0))
						/ (double) 1000000000;
				Log.ADD_LOG_PANEL(
						"Rate (numberOfSamplePacketOut/sampleCostTime) : " + (finalResult.size()) + " / " + costTime
								+ " = " + Tasks.df.format((finalResult.size()) / ((double) costTime)) + "numbers/s",
						Tasks.class.toString());
				Log.ADD_LOG_PANEL("Packet In: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size(),
						FindOptimizationalResult.class.toString());
				Log.ADD_LOG_PANEL("Flow Mod: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size(),
						FindOptimizationalResult.class.toString());
				Log.ADD_LOG_PANEL(
						"Packet Loss Rate: " + Tasks.df.format(
								(double) (1 - ((double) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size()
										/ (double) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size()))),
						FindOptimizationalResult.class.toString());

				if ((double) 1 - ((double) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size()
						/ (double) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size()) > Global.PACKET_LOSS_RATE) {

					if (Global.BULK_SIZE <= 2) {

						Log.ADD_LOG_PANEL(
								"Packet Loss Rate > " + Global.PACKET_LOSS_RATE * 100
										+ "%, The most optimizational bulk size  = 1",
								FindOptimizationalResult.class.toString());

						Global.BULK_SIZE = 1;
						Global.CURRENT_BULK_SIZE = 1;
						Global.BULK_SIZE_OFF_SET = 0;
						Initializer.INITIAL_CHANNEL_POOL();

						// start task
						{
							if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
								Initializer.INITIAL_2();
							} else {
								Initializer.INITIAL_0();

							}

							Tasks.START_TASK_5();
						}

						return;
					} else {

						Log.ADD_LOG_PANEL(
								"Packet Loss Rate > " + Global.PACKET_LOSS_RATE * 100
										+ "%, Restart to find Optimizational Result.. ",
								FindOptimizationalResult.class.toString());

						Global.BULK_SIZE_OFF_SET = Global.CURRENT_BULK_SIZE / 4;

						Global.PRE_BULK_SIZE = Global.CURRENT_BULK_SIZE;

						Global.CURRENT_BULK_SIZE = Global.CURRENT_BULK_SIZE - Global.BULK_SIZE_OFF_SET;
						Initializer.INITIAL_CHANNEL_POOL();
						FIND_METRIC_6_DETAIL(Global.CURRENT_BULK_SIZE);

					}

				} else {

					Global.CURRENT_BULK_SIZE = Global.CURRENT_BULK_SIZE * 2;

					Initializer.INITIAL_CHANNEL_POOL();

					Log.ADD_LOG_PANEL(
							"Packet Loss Rate < " + Global.PACKET_LOSS_RATE * 100
									+ "%, Restart to find Optimizational Result.. ",
							FindOptimizationalResult.class.toString());

					FIND_METRIC_6_DOUBLE(Global.CURRENT_BULK_SIZE);

					// other method

				}

			}

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private static void FIND_METRIC_4_DETAIL(int TEMP_BULK_SIZE) {

		Global.BULK_SIZE = TEMP_BULK_SIZE;

		if (Global.BULK_SIZE_OFF_SET == 1) {
			Log.ADD_LOG_PANEL("Bulk offset = 1, The most optimizational bulk size  = " + Global.CURRENT_BULK_SIZE,
					FindOptimizationalResult.class.toString());
			Global.BULK_SIZE_OFF_SET = 0;
			Initializer.INITIAL_CHANNEL_POOL();
			// start Tasks
			{
				Initializer.INITIAL_2();
				Tasks.START_TASK_3();
			}

			return;

		}

		Log.ADD_LOG_PANEL("*********************************************", FindOptimizationalResult.class.toString());
		Log.ADD_LOG_PANEL("Current bulk size = " + Global.BULK_SIZE, FindOptimizationalResult.class.toString());

		try {
			Thread.sleep(2000);

			Initializer.INITIAL_2();

			Thread.sleep(2000);

			START_TASK_4();

			ArrayList finalResult;

			if (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size() == 0) {
				finalResult = null;
			} else {

				finalResult = FindOptimizationalResult
						.FIND_SAMPLE(Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT, Global.GAP_TIME, false);
				Log.ADD_LOG_PANEL("Final max Sample Size: " + finalResult.size(), Tasks.class.toString());

			}

			if (finalResult == null) {
				Result.ADD_RESULT("-", 4, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
				Initializer.INITIAL_CHANNEL_POOL();
				return;
			} else if (finalResult.size() == 0) {
				Result.ADD_RESULT("-", 4, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
				Initializer.INITIAL_CHANNEL_POOL();
				return;
			} else if (finalResult.size() < Global.MIN_SAMPLE) {
				Result.ADD_RESULT("-", 6, 2);
				JOptionPane.showMessageDialog(null, "Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						"Please Try it again", JOptionPane.ERROR_MESSAGE);
				Log.ADD_LOG_PANEL("Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						Tasks.class.toString());
				Initializer.INITIAL_CHANNEL_POOL();
				return;

			} else {
				double costTime = ((Long) finalResult.get(finalResult.size() - 1) - (Long) finalResult.get(0))
						/ (double) 1000000000;
				Log.ADD_LOG_PANEL(
						"Rate (numberOfSamplePacketOut/sampleCostTime) : " + (finalResult.size()) + " / " + costTime
								+ " = " + Tasks.df.format((finalResult.size()) / ((double) costTime)) + "numbers/s",
						Tasks.class.toString());
				Log.ADD_LOG_PANEL("Packet In: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size(),
						FindOptimizationalResult.class.toString());
				Log.ADD_LOG_PANEL("Packet Out: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size(),
						FindOptimizationalResult.class.toString());
				Log.ADD_LOG_PANEL(
						"Packet Loss Rate: " + Tasks.df.format(
								(double) (1 - ((double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size()
										/ (double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size()))),
						FindOptimizationalResult.class.toString());

				if ((double) 1 - ((double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size()
						/ (double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN
								.size()) > Global.PACKET_LOSS_RATE) {

					Initializer.INITIAL_CHANNEL_POOL();

					Log.ADD_LOG_PANEL(
							"Packet Loss Rate > " + Global.PACKET_LOSS_RATE * 100
									+ "%,  Restart to find Optimizational Result.. ",
							FindOptimizationalResult.class.toString());
					if (Global.BULK_SIZE_OFF_SET != 1) {
						Global.BULK_SIZE_OFF_SET = Global.BULK_SIZE_OFF_SET / 2;
					}

					Global.PRE_BULK_SIZE = Global.CURRENT_BULK_SIZE;
					Global.CURRENT_BULK_SIZE = Global.CURRENT_BULK_SIZE - Global.BULK_SIZE_OFF_SET;

					FIND_METRIC_4_DETAIL(Global.CURRENT_BULK_SIZE);

				} else {

					Initializer.INITIAL_CHANNEL_POOL();
					Log.ADD_LOG_PANEL(
							"Packet Loss Rate < " + Global.PACKET_LOSS_RATE * 100
									+ "%, Restart to find Optimizational Result..",
							FindOptimizationalResult.class.toString());

					if (Global.BULK_SIZE_OFF_SET != 1) {
						Global.BULK_SIZE_OFF_SET = Global.BULK_SIZE_OFF_SET / 2;
					}

					Global.PRE_BULK_SIZE = Global.CURRENT_BULK_SIZE;
					Global.CURRENT_BULK_SIZE = Global.CURRENT_BULK_SIZE + Global.BULK_SIZE_OFF_SET;
					FIND_METRIC_4_DETAIL(Global.CURRENT_BULK_SIZE);
					// other method

				}
			}

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static void FIND_METRIC_6_DETAIL(int TEMP_BULK_SIZE) {
		Global.BULK_SIZE = TEMP_BULK_SIZE;

		if (Global.BULK_SIZE_OFF_SET == 1) {
			Log.ADD_LOG_PANEL("Bulk offset = 1, The most optimizational bulk size  = " + Global.CURRENT_BULK_SIZE,
					FindOptimizationalResult.class.toString());
			Global.BULK_SIZE_OFF_SET = 0;
			Initializer.INITIAL_CHANNEL_POOL();

			// start task
			{
				if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
					Initializer.INITIAL_2();
				} else {
					Initializer.INITIAL_0();

				}

				Tasks.START_TASK_5();
			}

			return;

		}

		Log.ADD_LOG_PANEL("*********************************************", FindOptimizationalResult.class.toString());
		Log.ADD_LOG_PANEL("Current bulk size = " + Global.BULK_SIZE, FindOptimizationalResult.class.toString());
		try {
			Thread.sleep(2000);
			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				Initializer.INITIAL_2();
			} else {
				Initializer.INITIAL_0();

			}

			Thread.sleep(2000);

			START_TASK_6();

			ArrayList finalResult;

			if (Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size() == 0) {
				finalResult = null;
			} else {
				finalResult = FindOptimizationalResult.FIND_SAMPLE(Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD,
						Global.GAP_TIME, false);
				Log.ADD_LOG_PANEL("Final max Sample Size: " + finalResult.size(), Tasks.class.toString());

			}
			if (finalResult == null) {
				Result.ADD_RESULT("-", 6, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
				Initializer.INITIAL_CHANNEL_POOL();
				return;
			} else if (finalResult.size() == 0) {
				Result.ADD_RESULT("-", 6, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
						Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controllel! or change Packet type", "Error!",
						JOptionPane.ERROR_MESSAGE);
				Initializer.INITIAL_CHANNEL_POOL();
				return;
			} else if (finalResult.size() < Global.MIN_SAMPLE) {
				Result.ADD_RESULT("-", 6, 2);
				JOptionPane.showMessageDialog(null, "Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						"Please Try it again", JOptionPane.ERROR_MESSAGE);
				Log.ADD_LOG_PANEL("Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
						Tasks.class.toString());
				Initializer.INITIAL_CHANNEL_POOL();
				return;

			} else {
				double costTime = ((Long) finalResult.get(finalResult.size() - 1) - (Long) finalResult.get(0))
						/ (double) 1000000000;
				Log.ADD_LOG_PANEL(
						"Rate (numberOfSamplePacketOut/sampleCostTime) : " + (finalResult.size()) + " / " + costTime
								+ " = " + Tasks.df.format((finalResult.size()) / ((double) costTime)) + "numbers/s",
						Tasks.class.toString());
				Log.ADD_LOG_PANEL("Packet In: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size(),
						FindOptimizationalResult.class.toString());
				Log.ADD_LOG_PANEL("Flow Mod: " + Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size(),
						FindOptimizationalResult.class.toString());
				Log.ADD_LOG_PANEL(
						"Packet Loss Rate: " + Tasks.df.format(
								(double) (1 - ((double) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size()
										/ (double) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size()))),
						FindOptimizationalResult.class.toString());

				if ((double) 1 - ((double) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size()
						/ (double) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size()) > Global.PACKET_LOSS_RATE) {

					Initializer.INITIAL_CHANNEL_POOL();

					Log.ADD_LOG_PANEL(
							"Packet Loss Rate > " + Global.PACKET_LOSS_RATE * 100
									+ "%,  Restart to find Optimizational Result.. ",
							FindOptimizationalResult.class.toString());
					if (Global.BULK_SIZE_OFF_SET != 1) {
						Global.BULK_SIZE_OFF_SET = Global.BULK_SIZE_OFF_SET / 2;
					}

					Global.PRE_BULK_SIZE = Global.CURRENT_BULK_SIZE;
					Global.CURRENT_BULK_SIZE = Global.CURRENT_BULK_SIZE - Global.BULK_SIZE_OFF_SET;

					FIND_METRIC_6_DETAIL(Global.CURRENT_BULK_SIZE);

				} else {

					Initializer.INITIAL_CHANNEL_POOL();
					Log.ADD_LOG_PANEL(
							"Packet Loss Rate < " + Global.PACKET_LOSS_RATE * 100
									+ "%, Restart to find Optimizational Result..",
							FindOptimizationalResult.class.toString());

					if (Global.BULK_SIZE_OFF_SET != 1) {
						Global.BULK_SIZE_OFF_SET = Global.BULK_SIZE_OFF_SET / 2;
					}

					Global.PRE_BULK_SIZE = Global.CURRENT_BULK_SIZE;
					Global.CURRENT_BULK_SIZE = Global.CURRENT_BULK_SIZE + Global.BULK_SIZE_OFF_SET;
					FIND_METRIC_6_DETAIL(Global.CURRENT_BULK_SIZE);
					// other method

				}

			}

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private static void START_TASK_4() {
		if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET) {
			OFEchoRequest OFEchoRequest = Global.FACTORY.buildEchoRequest().build();

			Global.ROOTNODE.sendUnknownPacketThroughput_ofMessage(OFEchoRequest);

		} else if (Global.UNKNOWN_PACKET_TYPE == Global.ARP_REQUEST) {

			String srcHostIp = Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0.2";
			String dstHostIp = Global.HOST_IP[0] + "." + Global.HOST_IP[1] + ".0.3";
			String srcHostMac = Global.HOST_MAC + ":aa:aa:aa:aa:01";
			String dstHostMac = Global.HOST_MAC + ":aa:aa:aa:aa:02";

			Host src = new Host(srcHostIp, srcHostMac);
			Host dst = new Host(dstHostIp, dstHostMac);

			byte tempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, src, dst);
			OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().getFirst(),
					tempPacket);

			Global.ROOTNODE.sendUnknownPacketThroughput_ofMessage(ofpi);

		} else {// udp tcp

			Host srcHost = Global.ROOTNODE.getPortList().get(1).getConnectedHostList().get(0);
			Host dstHost = Global.ROOTNODE.getPortList().get(2).getConnectedHostList().get(0);

			byte tempPacket[] = PacketMaker_NEW.MAKE_TCP_UDP_PACKET_BYTE(Global.UNKNOWN_PACKET_TYPE, srcHost, dstHost,
					1000, 1000);

			OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().get(1), tempPacket);

			System.out.println(1);
			Global.ROOTNODE.sendUnknownPacketThroughput_ofMessage(ofpi);

		}

	}

	private static void START_TASK_6() {
		try {
			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				Global.ROOTNODE.sendPacket_withoutsleep(PacketMaker_NEW.MAKE_ARP_REPLY_GRATUITOUS_PACKET_NEW(), 0,
						Global.NODE_SIZE * 2);

				Thread.sleep(20000);

				ArrayList<OFMessage> buf = PacketMaker_NEW.MAKE_ARP_REQUEST_REPLY();

				Global.ROOTNODE.sendPacket_sleep(buf, 0, Global.BUFF_SIZE); // send
																			// request

				Thread.sleep(5000);

				Global.ROOTNODE.sendUnknownPacketThroughput_arp(buf, Global.BUFF_SIZE);

			} else {

				final ArrayList<OFMessage> buf = PacketMaker_NEW.MAKE_TCP_UDP_PACKET(Global.PROVISINIONING_PACKET_TYPE,
						Global.NODE_SIZE);
				Global.ROOTNODE.sendUnknownPacketThroughput_rpp(buf);

			}

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static ArrayList FIND_SAMPLE(ArrayList inTime, long jianxi, boolean disPlay) {
		ArrayList finalResult = new ArrayList();
		ArrayList maxResult = new ArrayList();
		if (disPlay) {
			Log.ADD_LOG_PANEL("Total Packet Size: " + inTime.size(), Tasks.class.toString());
		}
		int count = 0;

		for (int i = 0; i < inTime.size() - 1; i++) {

			if (finalResult.size() == Global.MAX_SAMPLE) {
				if (disPlay) {
					Log.ADD_LOG_PANEL("Sample packet " + (count++) + " size : " + finalResult.size(),
							Tasks.class.toString());
				}
				return finalResult;

			}

			if ((Long) inTime.get(i + 1) - (Long) inTime.get(i) < jianxi) {
				finalResult.add(inTime.get(i));
			} else {
				if (disPlay) {
					Log.ADD_LOG_PANEL("Sample packet " + (count++) + " size : " + finalResult.size(),
							Tasks.class.toString());
				}
				if (finalResult.size() > maxResult.size()) {
					maxResult = finalResult;
				}
				finalResult = new ArrayList();

			}

			if (i == inTime.size() - 2) {
				if (disPlay) {
					Log.ADD_LOG_PANEL("Sample packet " + (count++) + " size : " + finalResult.size(),
							Tasks.class.toString());
				}
				if (finalResult.size() > maxResult.size()) {
					maxResult = finalResult;
				}
			}

		}

		return maxResult;
	}

}
