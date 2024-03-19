package com.southbound.tasks;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.projectfloodlight.openflow.protocol.OFMessage;

import com.mir.ui.Main;
import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.FindOptimizationalResult;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.PacketMaker_NEW;
import com.mirlab.lib.Result;
import com.mirlab.lib.Tasks;
import com.mirlab.topo.CreateTopo;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年7月25日 下午4:03:14 类说明
 */
public class ReactivePathProvisioningRate {
	private static final int bulk_size_initial_value = 1;
	private static final double inter_arrival_time_initial_value = 1;

	private static DecimalFormat df = new DecimalFormat("#0.00000");

	public static boolean HAS_STARTED = false;
	public static long SLEEP_TIME_zs = 0;
	public static int SLEEP_TIME_xs = 0;

	public static void go() {
		version0(bulk_size_initial_value, inter_arrival_time_initial_value);
	}

	private static void version0(int bulk_size, double inter_arrival_time) {
		// TODO Auto-generated method stub

		SLEEP_TIME_zs = (long) Math.floor(inter_arrival_time);
		SLEEP_TIME_xs = (int) ((inter_arrival_time - Math.floor(inter_arrival_time)) * 1000000);

		Global.BULK_SIZE = bulk_size;

		Log.ADD_LOG_PANEL("*********************************************",
				ReactivePathProvisioningRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Current bulk size = " + Global.BULK_SIZE,
				ReactivePathProvisioningRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Inter-arrival Time = " + df.format(inter_arrival_time),
				ReactivePathProvisioningRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Duration Time = " + Global.TEST_TIME, ReactivePathProvisioningRate.class.getSimpleName());

		try {
			Thread.sleep(20000);

			initi();

			Thread.sleep(2000);

			start_task();

			loss_check(bulk_size, inter_arrival_time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void initi() {
		CreateTopo ct = new CreateTopo(Global.SWITCH_ID_OFF_SET);
		ct.create_mininetTopoWith200hosts();
	}

	private static void start_task() {
		try {// for sleep

			ArrayList<OFMessage> srctemp = new ArrayList<OFMessage>();
			ArrayList<OFMessage> dsttemp = new ArrayList<OFMessage>();

			for (int i = 0; i < Global.srcHosts.length; i++) {

				Host srcHost = Global.srcHosts[i];
				byte tempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, srcHost, srcHost);
				OFMessage srcofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(srcHost.getBelong2Port(), tempPacket);
				srctemp.add(srcofpi);
			}

			for (int i = 0; i < Global.dstHosts.length; i++) {
				Host dstHost = Global.dstHosts[i];
				byte tempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, dstHost, dstHost);
				OFMessage dstofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(dstHost.getBelong2Port(), tempPacket);
				dsttemp.add(dstofpi);
			}

			Global.srcHosts[0].getConnectedNode().sendPacket(srctemp);
			Thread.sleep(2000);
			Global.dstHosts[0].getConnectedNode().sendPacket(dsttemp);

			Thread.sleep(20000);
			Log.ADD_LOG_PANEL("Sending ARP(Gratuitous) Completed.", ReactivePathProvisioningRate.class.getSimpleName());

			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				ArrayList<OFMessage> srcOFPacketIn = new ArrayList<OFMessage>();
				ArrayList<OFMessage> dstOFPacketIn = new ArrayList<OFMessage>();

				for (int j = 0; j < Global.srcHosts.length; j++) {
					for (int i = 0; i < Global.dstHosts.length; i++) {
						OFMessage ofpi = null;
						Host srcHost = Global.srcHosts[j];
						Host dstHost = Global.dstHosts[i];

						byte tempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, srcHost, dstHost);
						ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(srcHost.getBelong2Port(), tempPacket);
						srcOFPacketIn.add(ofpi);
					}
				}
				Global.srcHosts[0].getConnectedNode().sendPacket_sleep(srcOFPacketIn, 0, Global.BUFF_SIZE);

				Log.ADD_LOG_PANEL("Sending ARP(Request) Completed.",
						ReactivePathProvisioningRate.class.getSimpleName());

				for (int j = 0; j < Global.srcHosts.length; j++) {
					for (int i = 0; i < Global.dstHosts.length; i++) {
						OFMessage ofpi = null;
						Host srcHost = Global.srcHosts[j];
						Host dstHost = Global.dstHosts[i];
						byte tempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY, srcHost, dstHost);
						ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(dstHost.getBelong2Port(), tempPacket);
						dstOFPacketIn.add(ofpi);
					}
				}

				Thread.sleep(5000);
				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;

				Global.dstHosts[0].getConnectedNode().sendUnknownPacketThroughput_RPPR(dstOFPacketIn);

			} else if (Global.PROVISINIONING_PACKET_TYPE == Global.UDP_PACKET
					|| Global.PROVISINIONING_PACKET_TYPE == Global.TCP_PACKET) {
				// tcp udp

				ArrayList<OFMessage> OFPacketIn = new ArrayList<OFMessage>();
				OFMessage ofpi = null;
				for (int i = 0; i < Global.NODE_SIZE; i++) {
					for (int j = 0; j < Global.NODE_SIZE; j++) {
						Host srcHost = Global.srcHosts[i];
						Host dstHost = Global.dstHosts[j];

						byte tempPacket[] = PacketMaker_NEW.MAKE_TCP_UDP_PACKET_BYTE(Global.PROVISINIONING_PACKET_TYPE,
								srcHost, dstHost, 1000, 1000);

						ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(Global.srcHosts[0].getBelong2Port(), tempPacket);
						OFPacketIn.add(ofpi);
					}
				}

				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;
				Global.srcHosts[0].getConnectedNode().sendUnknownPacketThroughput_RPPR(OFPacketIn);

			} else {
				System.out.println("unknown packet type");
			}

			Thread.sleep(5000);

		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			HAS_STARTED = false;
			SLEEP_TIME_zs = 0;
			SLEEP_TIME_xs = 0;
		}
	}

	private static void loss_check(int bulk_size, double inter_arrival_time) {
		// TODO Auto-generated method stub
		ArrayList ofin = Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN;
		ArrayList flowMod = Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD;
		ArrayList finalResult;

		if (Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size() == 0) {
			finalResult = null;
		} else {

			finalResult = find_max_sample_size(flowMod, Global.GAP_TIME);
			Log.ADD_LOG_PANEL("Final max Sample Size: " + finalResult.size(),
					ReactivePathProvisioningRate.class.getSimpleName());

		}

		if (finalResult == null || finalResult.size() == 0) {
			Result.ADD_RESULT("-", 4, 2);
			Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
					ReactivePathProvisioningRate.class.getSimpleName());
			JOptionPane.showMessageDialog(null, "Please Check Reactive Module of SDN Controller or change packet type!",
					"Error!", JOptionPane.ERROR_MESSAGE);
			Initializer.INITIAL_CHANNEL_POOL();
			return;
		} else if (finalResult.size() < Global.MIN_SAMPLE) {
			Result.ADD_RESULT("-", 6, 2);
			Log.ADD_LOG_PANEL("Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
					ReactivePathProvisioningRate.class.getSimpleName());

			Initializer.INITIAL_CHANNEL_POOL();
			version0(bulk_size, inter_arrival_time);

		} else {// 成功情况下

			Log.ADD_LOG_PANEL("Packet In : " + ofin.size(), ReactivePathProvisioningRate.class.getSimpleName());
			Log.ADD_LOG_PANEL(
					"Packet In cost time : "
							+ ((Long) ofin.get(ofin.size() - 1) - (Long) ofin.get(0)) / (double) 1000000000,
					ReactivePathProvisioningRate.class.getSimpleName());
			Log.ADD_LOG_PANEL("Flow Mod : " + flowMod.size(), ReactivePathProvisioningRate.class.getSimpleName());
			Log.ADD_LOG_PANEL(
					"Flow Mod Time : "
							+ ((Long) flowMod.get(flowMod.size() - 1) - (Long) flowMod.get(0)) / (double) 1000000000,
					ReactivePathProvisioningRate.class.getSimpleName());
			Log.ADD_LOG_PANEL("Packet Loss Rate: " + df.format(find_loss_rate()),
					ReactivePathProvisioningRate.class.getSimpleName());

			if (find_loss_rate() > Global.PACKET_LOSS_RATE) {// 大于loss

				Log.ADD_LOG_PANEL(
						"Packet Loss Rate > " + Global.PACKET_LOSS_RATE * 100
								+ "%, Restart to find Optimizational Result.. ",
						ReactivePathProvisioningRate.class.getSimpleName());

				Initializer.INITIAL_CHANNEL_POOL();
				version0(bulk_size, inter_arrival_time + 0.1);

			} else if (find_loss_rate() < Global.PACKET_LOSS_RATE && find_loss_rate() >= 0) {// 小于loss

				if (inter_arrival_time != 1) {
					Log.ADD_LOG_PANEL("Packet Loss Rate < " + Global.PACKET_LOSS_RATE * 100 + "%, This is the rate ",
							ReactivePathProvisioningRate.class.getSimpleName());

					display_detail(flowMod, Global.GAP_TIME);
					Initializer.INITIAL_CHANNEL_POOL();

				} else {
					// Global.CURRENT_BULK_SIZE = Global.CURRENT_BULK_SIZE * 2;

					Log.ADD_LOG_PANEL(
							"Packet Loss Rate < " + Global.PACKET_LOSS_RATE * 100
									+ "%, Restart to find Optimizational Result.. ",
							ReactivePathProvisioningRate.class.getSimpleName());

					Initializer.INITIAL_CHANNEL_POOL();
					version0(bulk_size + 1, inter_arrival_time);
				}

			} else {
				// 不会用到
				Log.ADD_LOG_PANEL("Packet Loss Rate < 0, Error", ReactivePathProvisioningRate.class.getSimpleName());
				Log.ADD_LOG_PANEL("Restart after 1 min", ReactivePathProvisioningRate.class.getSimpleName());
				Initializer.INITIAL_CHANNEL_POOL();
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				version0(bulk_size_initial_value, inter_arrival_time_initial_value);
			}

		}
	}

	private static double find_loss_rate() {
		// find the loss
		double result = (double) 1 - ((double) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD.size()
				/ (double) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size());

		if (result <= 0) {
			return 0;
		} else {
			return result;
		}
	}

	private static ArrayList find_max_sample_size(ArrayList inTime, long jianxi) {
		ArrayList finalResult = new ArrayList();
		ArrayList maxResult = new ArrayList();

		int count = 0;

		for (int i = 0; i < inTime.size() - 1; i++) {

			if (finalResult.size() == Global.MAX_SAMPLE) {

				return finalResult;

			}

			if ((Long) inTime.get(i + 1) - (Long) inTime.get(i) < jianxi) {
				finalResult.add(inTime.get(i));
			} else {

				if (finalResult.size() > maxResult.size()) {
					maxResult = finalResult;
				}
				finalResult = new ArrayList();

			}

			if (i == inTime.size() - 2) {

				if (finalResult.size() > maxResult.size()) {
					maxResult = finalResult;
				}

			}

		}

		return maxResult;

	}

	private static void display_detail(ArrayList inTime, long jianxi) {
		double totalAverRate = 0;
		double sampleAverRate = 0;
		double maxRate = 0;

		int sampleTotalSize = 0;
		double sampleTotalCostTime = 0;

		double tempRate = 0;
		ArrayList tempResult = new ArrayList();
		ArrayList maxResult = new ArrayList();

		Log.ADD_LOG_PANEL("----------------detail--------------------",
				ReactivePathProvisioningRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Total Packet Size: " + inTime.size(), ReactivePathProvisioningRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("------------------------------------------",
				ReactivePathProvisioningRate.class.getSimpleName());
		double temp = ((Long) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN
				.get(Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size() - 1)
				- (Long) Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.get(0)) / (double) 1000000000;
		Log.ADD_LOG_PANEL("PacketIn : " + temp, ReactivePathProvisioningRate.class.getSimpleName());

		int count = 0;

		for (int i = 0; i < inTime.size() - 1; i++) {

			if (tempResult.size() == Global.MAX_SAMPLE) {

				double costTime = ((Long) tempResult.get(tempResult.size() - 1) - (Long) tempResult.get(0))
						/ (double) 1000000000;
				sampleTotalSize = sampleTotalSize + tempResult.size();
				sampleTotalCostTime = sampleTotalCostTime + costTime;
				tempRate = (tempResult.size() / ((double) costTime));

				if (maxRate < tempRate) {
					maxRate = tempRate;
				}
				if (tempResult.size() > maxResult.size()) {
					maxResult = tempResult;
				}

				Log.ADD_LOG_PANEL("------------------------------------------",
						ReactivePathProvisioningRate.class.getSimpleName());
				Log.ADD_LOG_PANEL("Sample packet " + (count++) + " size : " + tempResult.size(),
						ReactivePathProvisioningRate.class.getSimpleName());
				Log.ADD_LOG_PANEL("Cost Time : " + costTime, ReactivePathProvisioningRate.class.getSimpleName());
				Log.ADD_LOG_PANEL(
						"Rate : " + (tempResult.size()) + " / " + costTime + " = " + df.format(tempRate) + "numbers/s",
						ReactivePathProvisioningRate.class.getSimpleName());
				Log.ADD_LOG_PANEL("------------------------------------------",
						ReactivePathProvisioningRate.class.getSimpleName());

				tempResult = new ArrayList();
				continue;
			}

			if ((Long) inTime.get(i + 1) - (Long) inTime.get(i) < jianxi) {
				tempResult.add(inTime.get(i));
			} else {

				Log.ADD_LOG_PANEL("Sample packet " + (count++) + " size : " + tempResult.size(),
						ReactivePathProvisioningRate.class.getSimpleName());

				if (tempResult.size() > Global.MIN_SAMPLE) {// 如果大于sample size
															// 计算 rate
					double costTime = ((Long) tempResult.get(tempResult.size() - 1) - (Long) tempResult.get(0))
							/ (double) 1000000000;
					sampleTotalSize = sampleTotalSize + tempResult.size();
					sampleTotalCostTime = sampleTotalCostTime + costTime;
					tempRate = (tempResult.size() / ((double) costTime));

					if (maxRate < tempRate) {
						maxRate = tempRate;
					}

					Log.ADD_LOG_PANEL("------------------------------------------",
							ReactivePathProvisioningRate.class.getSimpleName());
					Log.ADD_LOG_PANEL("Cost Time : " + costTime, ReactivePathProvisioningRate.class.getSimpleName());
					Log.ADD_LOG_PANEL("Rate : " + (tempResult.size()) + " / " + costTime + " = " + df.format(tempRate)
							+ "numbers/s", ReactivePathProvisioningRate.class.getSimpleName());
					Log.ADD_LOG_PANEL("------------------------------------------",
							ReactivePathProvisioningRate.class.getSimpleName());

				}

				if (tempResult.size() > maxResult.size()) {
					maxResult = tempResult;
				}

				tempResult = new ArrayList();

			}

			if (i == inTime.size() - 2) {

				Log.ADD_LOG_PANEL("Sample packet " + (count++) + " size : " + tempResult.size(),
						ReactivePathProvisioningRate.class.getSimpleName());

				if (tempResult.size() > Global.MIN_SAMPLE) {// 如果大于sample size
															// 计算 rate
					double costTime = ((Long) tempResult.get(tempResult.size() - 1) - (Long) tempResult.get(0))
							/ (double) 1000000000;
					sampleTotalSize = sampleTotalSize + tempResult.size();
					sampleTotalCostTime = sampleTotalCostTime + costTime;
					tempRate = (tempResult.size() / ((double) costTime));

					if (maxRate < tempRate) {
						maxRate = tempRate;
					}

					Log.ADD_LOG_PANEL("Cost Time : " + costTime, ReactivePathProvisioningRate.class.getSimpleName());
					Log.ADD_LOG_PANEL("Rate : " + (tempResult.size()) + " / " + costTime + " = " + df.format(tempRate)
							+ "numbers/s", ReactivePathProvisioningRate.class.getSimpleName());
					Log.ADD_LOG_PANEL("------------------------------------------",
							ReactivePathProvisioningRate.class.getSimpleName());

				}

				if (tempResult.size() > maxResult.size()) {
					maxResult = tempResult;
				}

				tempResult = new ArrayList();

			}

		}

		Log.ADD_LOG_PANEL("------------------------------------------",
				ReactivePathProvisioningRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Total PacketOut Size : " + inTime.size(),
				ReactivePathProvisioningRate.class.getSimpleName());
		double time = ((Long) inTime.get(inTime.size() - 1) - (Long) inTime.get(0)) / (double) 1000000000;
		Log.ADD_LOG_PANEL("Cost Time : " + time, ReactivePathProvisioningRate.class.getSimpleName());
		totalAverRate = (inTime.size()) / ((double) time);
		Log.ADD_LOG_PANEL("Total Average Rate : " + (inTime.size()) + " / " + time + " = " + df.format(totalAverRate)
				+ "numbers/s" + "\n", ReactivePathProvisioningRate.class.getSimpleName());
		time = 0;

		Log.ADD_LOG_PANEL("Total PacketOut Sample Size : " + sampleTotalSize,
				ReactivePathProvisioningRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Cost Time : " + sampleTotalCostTime, ReactivePathProvisioningRate.class.getSimpleName());
		sampleAverRate = (sampleTotalSize) / ((double) sampleTotalCostTime);

		Result.ADD_RESULT(Double.toString(sampleAverRate), 4, 2);
		// metric4Res = sampleAverRate;

		Log.ADD_LOG_PANEL(
				"Sample Average Rate : " + (sampleTotalSize) + " / " + sampleTotalCostTime + " = "
						+ df.format(sampleAverRate) + "numbers/s" + "\n",
				ReactivePathProvisioningRate.class.getSimpleName());

		Log.ADD_LOG_PANEL("PacketOut largest Sample Size : " + maxResult.size(), Tasks.class.toString());
		time = ((Long) maxResult.get(maxResult.size() - 1) - (Long) maxResult.get(0)) / (double) 1000000000;
		Log.ADD_LOG_PANEL("Cost Time : " + time, ReactivePathProvisioningRate.class.getSimpleName());
		Log.ADD_LOG_PANEL(
				"Largest Sample Rate : " + (maxResult.size()) + " / " + time + " = "
						+ df.format((maxResult.size()) / ((double) time)) + "numbers/s" + "\n",
				ReactivePathProvisioningRate.class.getSimpleName());

		Log.ADD_LOG_PANEL("MAX Sample Rate : " + df.format(maxRate) + "numbers/s" + "\n",
				ReactivePathProvisioningRate.class.getSimpleName());

	}
}
