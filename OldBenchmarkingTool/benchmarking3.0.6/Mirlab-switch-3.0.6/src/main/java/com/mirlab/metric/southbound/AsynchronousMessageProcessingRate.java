package com.mirlab.metric.southbound;

import java.text.DecimalFormat;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.jboss.netty.channel.ChannelFuture;
import org.projectfloodlight.openflow.protocol.OFEchoRequest;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.PacketMaker_NEW;
import com.mirlab.lib.Result;
import com.mirlab.lib.Tasks;
import com.mirlab.topo.CreateTopo;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年5月10日 下午8:57:50 类说明
 */
public class AsynchronousMessageProcessingRate {
	private static final int bulk_size_initial_value = 1;
	private static final double inter_arrival_time_initial_value = 1;

	private static DecimalFormat df = new DecimalFormat("#0.00000");

	public static Logger logger = LoggerFactory.getLogger(Node.class);

	public static long SLEEP_TIME_zs = 0;
	public static int SLEEP_TIME_xs = 0;
	public static boolean HAS_STARTED = false;

	public static void go() {
		version1(bulk_size_initial_value, inter_arrival_time_initial_value);
		Log.exportLog(AsynchronousMessageProcessingRate.class.getSimpleName(), 4);
	}

	private static void version1(int bulk_size, double inter_arrival_time) {
		// inter-arrival time sleep time
		SLEEP_TIME_zs = (long) Math.floor(inter_arrival_time);
		SLEEP_TIME_xs = (int) ((inter_arrival_time - Math.floor(inter_arrival_time)) * 1000000);

		Global.BULK_SIZE = bulk_size;

		Log.ADD_LOG_PANEL("*********************************************",
				AsynchronousMessageProcessingRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Current bulk size = " + Global.BULK_SIZE,
				AsynchronousMessageProcessingRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Inter-arrival Time = " + df.format(inter_arrival_time),
				AsynchronousMessageProcessingRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Duration Time = " + Global.TEST_TIME,
				AsynchronousMessageProcessingRate.class.getSimpleName());

		try {
			Thread.sleep(10000);

			initi();

			Thread.sleep(2000);

			start_task();

			loss_check(bulk_size, inter_arrival_time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SLEEP_TIME_zs = 0;
			SLEEP_TIME_xs = 0;
			HAS_STARTED = false;
		}

	}

	private static void initi() {
		// TODO Auto-generated method stub
		CreateTopo ct = new CreateTopo(Global.SWITCH_ID_OFF_SET);
		ct.go();
	}

	private static void start_task() {
		// TODO Auto-generated method stub
		if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET) {
			// 跟原来一样，没改动
			OFEchoRequest OFEchoRequest = Global.FACTORY.buildEchoRequest().build();

			sendPacket(Global.ROOTNODE, OFEchoRequest);

		} else if (Global.UNKNOWN_PACKET_TYPE == Global.ARP_REQUEST) {// arp

			Host src = Global.srcHosts[0];
			Host dst = Global.dstHosts[0];

			byte tempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, src, dst);
			OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(Global.ROOTNODE.getPortList().getFirst(),
					tempPacket);

			sendPacket(src.getConnectedNode(), ofpi);

		} else {// udp tcp

			Host srcHost = Global.srcHosts[0];
			Host dstHost = Global.dstHosts[0];

			byte srcTempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, srcHost, dstHost);
			OFMessage src_ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(srcHost.getBelong2Port(), srcTempPacket);

			byte dstTempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, dstHost, srcHost);
			OFMessage dst_ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(dstHost.getBelong2Port(), dstTempPacket);

			srcHost.getConnectedNode().getChannelFuture().getChannel().write((OFMessage) src_ofpi);
			dstHost.getConnectedNode().getChannelFuture().getChannel().write((OFMessage) dst_ofpi);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*****************************************************************************************************/

			byte tempPacket[] = PacketMaker_NEW.MAKE_TCP_UDP_PACKET_BYTE(Global.UNKNOWN_PACKET_TYPE, srcHost, dstHost,
					1000, 1000);

			OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(srcHost.getBelong2Port(), tempPacket);
			HAS_STARTED = true;

			sendPacket(srcHost.getConnectedNode(), ofpi);

		}
	}

	private static void loss_check(int bulk_size, double inter_arrival_time) {
		// TODO Auto-generated method stub
		ArrayList<Long> finalResult;

		if (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size() == 0) {
			finalResult = null;
		} else {

			finalResult = find_max_sample_size(Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT, Global.GAP_TIME);
			Log.ADD_LOG_PANEL("Final max Sample Size: " + finalResult.size(),
					AsynchronousMessageProcessingRate.class.getSimpleName());

		}

		if (finalResult == null || finalResult.size() == 0) {
			Result.ADD_RESULT("-", 4, 2);
			Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller or change packet type!!",
					AsynchronousMessageProcessingRate.class.getSimpleName());
			JOptionPane.showMessageDialog(null, "Please Check Reactive Module of SDN Controller or change packet type!",
					"Error!", JOptionPane.ERROR_MESSAGE);
			Initializer.INITIAL_CHANNEL_POOL();
			return;
		} else if (finalResult.size() < Global.MIN_SAMPLE) {
			Result.ADD_RESULT("-", 6, 2);
			Log.ADD_LOG_PANEL("Sample packet < " + Global.MIN_SAMPLE + ", Please Try it again!",
					AsynchronousMessageProcessingRate.class.getSimpleName());

			Initializer.INITIAL_CHANNEL_POOL();
			version1(bulk_size, inter_arrival_time);

		} else {// 成功情况下

			Log.ADD_LOG_PANEL("Packet In: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size(),
					AsynchronousMessageProcessingRate.class.toString());
			Log.ADD_LOG_PANEL("Packet Out: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size(),
					AsynchronousMessageProcessingRate.class.toString());
			Log.ADD_LOG_PANEL("Packet Loss Rate: " + df.format(find_loss_rate()),
					AsynchronousMessageProcessingRate.class.toString());

			if (find_loss_rate() > Global.PACKET_LOSS_RATE) {// 大于loss

				Log.ADD_LOG_PANEL(
						"Packet Loss Rate > " + Global.PACKET_LOSS_RATE * 100
								+ "%, Restart to find Optimizational Result.. ",
						AsynchronousMessageProcessingRate.class.toString());

				Initializer.INITIAL_CHANNEL_POOL();
				version1(bulk_size, inter_arrival_time + 0.1);

			} else {
				// if (find_loss_rate() < Global.PACKET_LOSS_RATE &&
				// find_loss_rate() >= 0) {// 小于loss

				if (inter_arrival_time != 1) {
					Log.ADD_LOG_PANEL("Packet Loss Rate < " + Global.PACKET_LOSS_RATE * 100 + "%, This is the rate ",
							AsynchronousMessageProcessingRate.class.toString());

					display_detail(Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT, Global.GAP_TIME);
					Initializer.INITIAL_CHANNEL_POOL();

				} else {
					// Global.CURRENT_BULK_SIZE = Global.CURRENT_BULK_SIZE * 2;

					Log.ADD_LOG_PANEL(
							"Packet Loss Rate < " + Global.PACKET_LOSS_RATE * 100
									+ "%, Restart to find Optimizational Result.. ",
							AsynchronousMessageProcessingRate.class.toString());

					Initializer.INITIAL_CHANNEL_POOL();
					version1(bulk_size + 1, inter_arrival_time);
				}

			}
			// else {
			// Log.ADD_LOG_PANEL("Packet Loss Rate < 0, Error",
			// FindOptimizationalResult.class.toString());
			// Log.ADD_LOG_PANEL("Restart after 1 min",
			// FindOptimizationalResult.class.toString());
			// Initializer.INITIAL_CHANNEL_POOL();
			// try {
			// Thread.sleep(60000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// version1(bulk_size_initial_value,
			// inter_arrival_time_initial_value, duration_time_initial_value);
			// }

		}
	}

	private static double find_loss_rate() {
		// find the loss

		return (double) 1 - ((double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size()
				/ (double) Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size());

	}

	private static ArrayList<Long> find_max_sample_size(ArrayList<Long> inTime, long jianxi) {
		ArrayList<Long> finalResult = new ArrayList<Long>();
		ArrayList<Long> maxResult = new ArrayList<Long>();

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
				finalResult = new ArrayList<Long>();

			}

			if (i == inTime.size() - 2) {

				if (finalResult.size() > maxResult.size()) {
					maxResult = finalResult;
				}

			}

		}

		return maxResult;

	}

	private static void display_detail(ArrayList<Long> inTime, long jianxi) {
		double totalAverRate = 0;
		double sampleAverRate = 0;
		double maxRate = 0;

		int sampleTotalSize = 0;
		double sampleTotalCostTime = 0;

		double tempRate = 0;
		ArrayList<Long> tempResult = new ArrayList<Long>();
		ArrayList<Long> maxResult = new ArrayList<Long>();

		Log.ADD_LOG_PANEL("----------------detail--------------------",
				AsynchronousMessageProcessingRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Total Packet Size: " + inTime.size(),
				AsynchronousMessageProcessingRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("------------------------------------------",
				AsynchronousMessageProcessingRate.class.getSimpleName());

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
						AsynchronousMessageProcessingRate.class.getSimpleName());
				Log.ADD_LOG_PANEL("Sample packet " + (count++) + " size : " + tempResult.size(),
						AsynchronousMessageProcessingRate.class.getSimpleName());
				Log.ADD_LOG_PANEL("Cost Time : " + costTime, AsynchronousMessageProcessingRate.class.getSimpleName());
				Log.ADD_LOG_PANEL(
						"Rate : " + (tempResult.size()) + " / " + costTime + " = " + df.format(tempRate) + "numbers/s",
						AsynchronousMessageProcessingRate.class.getSimpleName());
				Log.ADD_LOG_PANEL("------------------------------------------",
						AsynchronousMessageProcessingRate.class.getSimpleName());

				tempResult = new ArrayList<Long>();
				continue;
			}

			if ((Long) inTime.get(i + 1) - (Long) inTime.get(i) < jianxi) {
				tempResult.add(inTime.get(i));
			} else {

				Log.ADD_LOG_PANEL("Sample packet " + (count++) + " size : " + tempResult.size(),
						AsynchronousMessageProcessingRate.class.getSimpleName());

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
							AsynchronousMessageProcessingRate.class.getSimpleName());
					Log.ADD_LOG_PANEL("Cost Time : " + costTime,
							AsynchronousMessageProcessingRate.class.getSimpleName());
					Log.ADD_LOG_PANEL("Rate : " + (tempResult.size()) + " / " + costTime + " = " + df.format(tempRate)
							+ "numbers/s", AsynchronousMessageProcessingRate.class.getSimpleName());
					Log.ADD_LOG_PANEL("------------------------------------------",
							AsynchronousMessageProcessingRate.class.getSimpleName());

				}

				if (tempResult.size() > maxResult.size()) {
					maxResult = tempResult;
				}

				tempResult = new ArrayList<Long>();

			}

			if (i == inTime.size() - 2) {

				Log.ADD_LOG_PANEL("Sample packet " + (count++) + " size : " + tempResult.size(),
						AsynchronousMessageProcessingRate.class.getSimpleName());

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

					Log.ADD_LOG_PANEL("Cost Time : " + costTime,
							AsynchronousMessageProcessingRate.class.getSimpleName());
					Log.ADD_LOG_PANEL("Rate : " + (tempResult.size()) + " / " + costTime + " = " + df.format(tempRate)
							+ "numbers/s", AsynchronousMessageProcessingRate.class.getSimpleName());
					Log.ADD_LOG_PANEL("------------------------------------------",
							AsynchronousMessageProcessingRate.class.getSimpleName());

				}

				if (tempResult.size() > maxResult.size()) {
					maxResult = tempResult;
				}

				tempResult = new ArrayList<Long>();

			}

		}

		Log.ADD_LOG_PANEL("------------------------------------------",
				AsynchronousMessageProcessingRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Total PacketOut Size : " + inTime.size(),
				AsynchronousMessageProcessingRate.class.getSimpleName());
		double time = ((Long) inTime.get(inTime.size() - 1) - (Long) inTime.get(0)) / (double) 1000000000;
		Log.ADD_LOG_PANEL("Cost Time : " + time, AsynchronousMessageProcessingRate.class.getSimpleName());
		totalAverRate = (inTime.size()) / ((double) time);
		Log.ADD_LOG_PANEL("Total Average Rate : " + (inTime.size()) + " / " + time + " = " + df.format(totalAverRate)
				+ "numbers/s" + "\n", AsynchronousMessageProcessingRate.class.getSimpleName());
		time = 0;

		Log.ADD_LOG_PANEL("Total PacketOut Sample Size : " + sampleTotalSize,
				AsynchronousMessageProcessingRate.class.getSimpleName());
		Log.ADD_LOG_PANEL("Cost Time : " + sampleTotalCostTime,
				AsynchronousMessageProcessingRate.class.getSimpleName());
		sampleAverRate = (sampleTotalSize) / ((double) sampleTotalCostTime);

		Result.ADD_RESULT(Double.toString(sampleAverRate), 4, 2);
		// metric4Res = sampleAverRate;

		Log.ADD_LOG_PANEL(
				"Sample Average Rate : " + (sampleTotalSize) + " / " + sampleTotalCostTime + " = "
						+ df.format(sampleAverRate) + "numbers/s" + "\n",
				AsynchronousMessageProcessingRate.class.getSimpleName());

		Log.ADD_LOG_PANEL("PacketOut largest Sample Size : " + maxResult.size(),
				AsynchronousMessageProcessingRate.class.getSimpleName());
		time = ((Long) maxResult.get(maxResult.size() - 1) - (Long) maxResult.get(0)) / (double) 1000000000;
		Log.ADD_LOG_PANEL("Cost Time : " + time, AsynchronousMessageProcessingRate.class.getSimpleName());
		Log.ADD_LOG_PANEL(
				"Largest Sample Rate : " + (maxResult.size()) + " / " + time + " = "
						+ df.format((maxResult.size()) / ((double) time)) + "numbers/s" + "\n",
				AsynchronousMessageProcessingRate.class.getSimpleName());

		Log.ADD_LOG_PANEL("MAX Sample Rate : " + df.format(maxRate) + "numbers/s" + "\n",
				AsynchronousMessageProcessingRate.class.getSimpleName());

	}

	private static void sendPacket(Node node, OFMessage of) {
		long iniTime = 0;

		ChannelFuture nodeChannelF = node.getChannelFuture();
		List<OFMessage> msgList = new ArrayList<OFMessage>();

		try {
			for (int i = 0; i < Global.BULK_SIZE; i++) {
				msgList.add(of);
			}

			new Thread(new Runnable() {

				public void run() {

					Tasks.HAS_STARTED = true;
					AsynchronousMessageProcessingRate.HAS_STARTED = true;

				}

			}).start();

			// 预热
			for (int i = 4; i > 1; i--) {
				for (int j = 0; j < 5; j++) {
					nodeChannelF.getChannel().write(msgList);
					Thread.sleep(i);
				}

			}

			iniTime = System.nanoTime();

			while (true) {
				if (nodeChannelF.getChannel().isWritable()) {

					if (System.nanoTime() - iniTime < ((double) Global.TEST_TIME * 1000000000d)) {

						/* Thread.sleep(0,1); */
						try {
							Thread.sleep(AsynchronousMessageProcessingRate.SLEEP_TIME_zs,
									AsynchronousMessageProcessingRate.SLEEP_TIME_xs);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						nodeChannelF.getChannel().write(msgList);
						for (int i = 0; i < Global.BULK_SIZE; i++) {
							Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.add(System.nanoTime());
						}
					} else {

						break;
					}

				}

				else {
					logger.debug("Channel is not writable!");

				}

			}

			Tasks.HAS_STARTED = false;
			AsynchronousMessageProcessingRate.HAS_STARTED = false;

			Thread.sleep(2000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
