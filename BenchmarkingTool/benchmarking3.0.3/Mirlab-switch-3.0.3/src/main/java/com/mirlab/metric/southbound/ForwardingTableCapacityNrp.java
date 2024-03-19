package com.mirlab.metric.southbound;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.projectfloodlight.openflow.protocol.OFMessage;

import com.mir.ui.Main;
import com.mirlab.component.Host;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.PacketMaker_NEW;
import com.mirlab.lib.Result;
import com.mirlab.lib.Tasks;
import com.mirlab.topo.CreateTopo;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年8月22日 下午6:03:53 类说明
 */
public class ForwardingTableCapacityNrp {
	public static boolean HAS_STARTED = false;

	public static DecimalFormat df = new DecimalFormat("#0.00000");

	public static void go() {
		version0();
	}

	private static void version0() {
		// TODO Auto-generated method stub

		try {
			initi();

			Thread.sleep(5000);

			start_task();

			result_check();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HAS_STARTED = false;
		}

	}

	private static void initi() {
		CreateTopo ct = new CreateTopo(Global.SWITCH_ID_OFF_SET);
		ct.go();
	}

	private static void start_task() {
		// TODO Auto-generated method stub

		try {// for sleep

			ArrayList<OFMessage> srctemp = new ArrayList<OFMessage>();
			ArrayList<OFMessage> dsttemp = new ArrayList<OFMessage>();

			for (int i = 0; i < Global.NODE_SIZE; i++) {

				Host srcHost = Global.srcHosts[i];
				byte tempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, srcHost, srcHost);
				OFMessage srcofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(srcHost.getBelong2Port(), tempPacket);
				srctemp.add(srcofpi);

				Host dstHost = Global.dstHosts[i];
				byte tempPacket1[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY_GRATUITOUS, dstHost,
						dstHost);
				OFMessage dstofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(dstHost.getBelong2Port(), tempPacket1);
				dsttemp.add(dstofpi);

			}

			Global.srcHosts[0].getConnectedNode().sendPacket(srctemp);
			Thread.sleep(2000);
			Global.dstHosts[0].getConnectedNode().sendPacket(dsttemp);

			Thread.sleep(20000);
			Log.ADD_LOG_PANEL("Sending ARP(Gratuitous) Completed.", Initializer.class.toString());

			if (Global.PROVISINIONING_PACKET_TYPE == Global.ARP_REPLY) {
				ArrayList<OFMessage> temp1 = new ArrayList<OFMessage>();
				ArrayList<OFMessage> temp2 = new ArrayList<OFMessage>();
				for (int j = 0; j < Global.NODE_SIZE; j++) {
					for (int i = 0; i < Global.NODE_SIZE; i++) {
						OFMessage ofpi = null;
						Host srcHost = Global.srcHosts[j];
						Host dstHost = Global.dstHosts[i];

						byte tempPacket1[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, srcHost, dstHost);
						ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(srcHost.getBelong2Port(), tempPacket1);
						temp1.add(ofpi);
					}
				}
				Global.srcHosts[0].getConnectedNode().sendPacket_sleep(temp1, 0, Global.BUFF_SIZE);
				Log.ADD_LOG_PANEL("Sending ARP(Request) Completed.", Initializer.class.toString());

				for (int j = 0; j < Global.NODE_SIZE; j++) {
					for (int i = 0; i < Global.NODE_SIZE; i++) {
						OFMessage ofpi = null;
						Host srcHost = Global.srcHosts[j];
						Host dstHost = Global.dstHosts[i];
						byte tempPacket2[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REPLY, srcHost, dstHost);
						ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(dstHost.getBelong2Port(), tempPacket2);
						temp2.add(ofpi);
					}
				}

				Thread.sleep(5000);
				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;

				Global.dstHosts[0].getConnectedNode().sendUnknownPacketThroughput_fms_new_arp(temp2);

			} else {
				// tcp udp

				ArrayList<OFMessage> temp = new ArrayList<OFMessage>();
				OFMessage ofpi = null;
				for (int i = 0; i < Global.NODE_SIZE; i++) {
					for (int j = 0; j < Global.NODE_SIZE; j++) {
						Host srcHost = Global.srcHosts[i];
						Host dstHost = Global.dstHosts[j];

						byte tempPacket[] = PacketMaker_NEW.MAKE_TCP_UDP_PACKET_BYTE(Global.PROVISINIONING_PACKET_TYPE,
								srcHost, dstHost, 1000, 1000);

						ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(Global.srcHosts[0].getBelong2Port(), tempPacket);
						temp.add(ofpi);
					}
				}

				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;

				Global.srcHosts[0].getConnectedNode().sendUnknownPacketThroughput_fms_new(temp);
			}

			Thread.sleep(5000);

		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.gui.S_progressBarTotal.setValue(1);
			Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private static void result_check() {
		// TODO Auto-generated method stub
		Result.ADD_RESULT(Result.TOTAL_NUMBER_OF_FLOW_MOD + "", 1, 5);
		Log.ADD_LOG_PANEL("size of FlowMod: " + Result.TOTAL_NUMBER_OF_FLOW_MOD, Tasks.class.toString());
		Main.gui.S_progressBar.setValue(Main.gui.S_progressBar.getMaximum());

		Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
		Main.gui.S_progressBarTotal.setValue(1);

		Initializer.INITIAL_CHANNEL_POOL();
	}

}
