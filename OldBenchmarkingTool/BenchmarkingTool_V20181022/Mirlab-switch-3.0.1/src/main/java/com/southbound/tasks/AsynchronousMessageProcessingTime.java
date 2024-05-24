package com.southbound.tasks;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import org.projectfloodlight.openflow.protocol.OFEchoRequest;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mir.ui.Main;
import com.mirlab.component.Host;
import com.mirlab.global.Global;
import com.mirlab.lib.BenchmarkTimer;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.PacketMaker_NEW;
import com.mirlab.lib.Result;
import com.mirlab.lib.Tasks;
import com.mirlab.openflow.BaseHandler;
import com.mirlab.topo.CreateTopo;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年4月19日 上午11:16:45 类说明
 */
public class AsynchronousMessageProcessingTime {
	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	public static boolean HAS_STARTED = false;
	public static DecimalFormat df = new DecimalFormat("#0.00000");

	public static void go() {
		version0();
	}

	private static void version0() {
		try {
			CreateTopo ct = new CreateTopo(Global.SWITCH_ID_OFF_SET);
			ct.go();

			Thread.sleep(2000);

			if (Global.UNKNOWN_PACKET_TYPE == Global.ECHO_PACKET) {

				Host src = Global.hosts[0];
				Host dst = Global.hosts[1];

				OFEchoRequest OFEchoRequest = Global.FACTORY.buildEchoRequest().build();

				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;

				src.getConnectedNode().sendUnknownPacketLatency_ofMessage(OFEchoRequest);

			} else if (Global.UNKNOWN_PACKET_TYPE == Global.ARP_REQUEST) {

				Host src = Global.hosts[0];
				Host dst = Global.hosts[1];

				byte srcTempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, src, dst);
				OFMessage src_ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(src.getBelong2Port(), srcTempPacket);

				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;

				src.getConnectedNode().sendUnknownPacketLatency_ofMessage(src_ofpi);
			} else {// tcp udp?

				Host src = Global.hosts[0];
				Host dst = Global.hosts[1];

				byte srcTempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, src, dst);
				OFMessage src_ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(src.getBelong2Port(), srcTempPacket);

				byte dstTempPacket[] = PacketMaker_NEW.MAKE_ARP_PACKET_BYTE(Global.ARP_REQUEST, dst, src);
				OFMessage dst_ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(dst.getBelong2Port(), dstTempPacket);

				src.getConnectedNode().sendUnknownPacketLatency_ofMessage2(src_ofpi);
				dst.getConnectedNode().sendUnknownPacketLatency_ofMessage2(dst_ofpi);

				Thread.sleep(1000);

				/******************************************************************************************************/

				byte tempPacket[] = PacketMaker_NEW.MAKE_TCP_UDP_PACKET_BYTE(Global.UNKNOWN_PACKET_TYPE, src, dst, 1000,
						1000);// tcp port

				OFMessage ofpi = PacketMaker_NEW.MAKE_OPENFLOW_PACKET_IN(src.getBelong2Port(), tempPacket);
				// return packet in message

				HAS_STARTED = true;
				Tasks.HAS_STARTED = true;
				src.getConnectedNode().sendUnknownPacketLatency_ofMessage(ofpi);

			}

			// new Thread(new Runnable() {
			Thread.sleep(1000);

			if (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size() < 5
					|| Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size() < 5) {
				Result.ADD_RESULT("-", 3, 2);
				Log.ADD_LOG_PANEL("Error! Please Check Reactive Module of SDN Controller!", Tasks.class.toString());
				JOptionPane.showMessageDialog(null,
						"Please Check Reactive Module of SDN Controller or change packet type!", "Error!",
						JOptionPane.ERROR_MESSAGE);
			} else {
				Result.ADD_RESULT(df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
						Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT,
						Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN)), 3, 2);
				Log.ADD_LOG_PANEL("size of in: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.size(),
						Tasks.class.toString());
				Log.ADD_LOG_PANEL("size of out: " + Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size(),
						Tasks.class.toString());
				Log.ADD_LOG_PANEL(
						"Average processing time : " + df.format(BenchmarkTimer.GET_AVERAGE_TIME_WITHOUT_RE(
								Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT,
								Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN)) + " ms",
						Tasks.class.toString());

				Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());

			}
			Main.S_progressBarTotal.setValue(1);
			Initializer.INITIAL_CHANNEL_POOL();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Main.S_progressBarTotal.setValue(1);
			Main.S_progressBar.setValue(Main.S_progressBar.getMaximum());
			Initializer.INITIAL_CHANNEL_POOL();
			JOptionPane.showMessageDialog(null, "Unknown Error! Please try it again!", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
