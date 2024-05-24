package com.mirlab.component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.plaf.synth.SynthSpinnerUI;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.projectfloodlight.openflow.protocol.OFPortFeatures;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.OpenFlow13_PacketMaker;
import com.mirlab.lib.Result;
import com.mirlab.lib.Tasks;
import com.southbound.tasks.AsynchronousMessageProcessingRate;
import com.southbound.tasks.ReactivePathProvisioningRate;

public class Node {

	private long dpid;
	private int nodeId;
	private LinkedList<Port> portList = new LinkedList<Port>();
	private ChannelFuture channelFuture;
	private Table[] tables;
	public static Logger logger = LoggerFactory.getLogger(Node.class);
	private Node nextNode = null;
	private long iniTime = 0;
	private boolean hasReceivedHello = false;
	private int numberOflldpPacket = 0;
	private int numberOfFlowEntry = 0;
	private ArrayList LLDP_OUT = new ArrayList();
	private ArrayList LLDP_IN = new ArrayList();
	private ClientBootstrap bootstrap;
	long tempTime = 0;

	public ClientBootstrap getBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(ClientBootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	public ArrayList getLLDP_OUT() {
		return LLDP_OUT;
	}

	public void setLLDP_OUT(ArrayList lLDP_OUT) {
		LLDP_OUT = lLDP_OUT;
	}

	public ArrayList getLLDP_IN() {
		return LLDP_IN;
	}

	public void setLLDP_IN(ArrayList lLDP_IN) {
		LLDP_IN = lLDP_IN;
	}

	public int getNumberOfFlowEntry() {
		return numberOfFlowEntry;
	}

	public void numberOfFlowEntryPlusOne() {
		this.numberOfFlowEntry = this.numberOfFlowEntry + 1;
	}

	public int getNumberOflldpPacket() {
		return numberOflldpPacket;
	}

	public void numberOflldpPacketPlusOne() {
		this.numberOflldpPacket = this.numberOflldpPacket + 1;
	}

	public boolean isHasReceivedHello() {
		return hasReceivedHello;
	}

	public void setHasReceivedHello(boolean hasReceivedHello) {
		this.hasReceivedHello = hasReceivedHello;
	}

	public long getIniTime() {
		return iniTime;
	}

	public void setIniTime(long iniTime) {
		this.iniTime = iniTime;
	}

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public void setChannelFuture(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
		for (int i = 0; i < portList.size(); i++) {
			portList.get(i).setChannelFuture(channelFuture);
		}
	}

	public Table[] getTables() {
		return tables;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}

	public void setTables(Table[] tables) {
		this.tables = tables;
	}

	public Node(long dpid, int nodeId) {// 构造器
		this.dpid = dpid;
		this.nodeId = nodeId;

	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public void creatPort(int numberOfPort) {// 创建端口
		for (int i = 0; i < numberOfPort; i++) {
			String name = "Eth" + (i + 1);
			OFPortDesc temp = OpenFlow13_PacketMaker.PORT_DESC(name, i + 1);
			Port port = new Port(temp, i + 1, this);// Port class，
													// 在component文件夹下
			portList.add(port);

		}
	}

	public void addPort() {// 添加端口
		String name = "Eth" + (this.portList.size() + 1);
		OFPortDesc temp = OpenFlow13_PacketMaker.PORT_DESC(name, this.portList.size() + 1);

		Port port = new Port(temp, (this.portList.size() + 1), this);
		if ((this.portList.size() + 1) >= 2) {
			port.setChannelFuture(this.portList.get(0).getChannelFuture());
		}
		portList.add(port);
	}

	public LinkedList<Port> getPortList() {
		return portList;
	}

	public void setPortList(LinkedList<Port> portList) {
		this.portList = portList;
	}

	public long getDpid() {
		return dpid;
	}

	public void setDpid(long dpid) {
		this.dpid = dpid;
	}

	public void sendPacket(OFMessage ofpi) {
		ChannelFuture nodeChannelF = this.getChannelFuture();

		if (nodeChannelF.getChannel().isWritable()) {

			nodeChannelF.getChannel().write(ofpi);

		} else {
			logger.debug("Channel is not writable!");
		}

	}

	public void sendPacket(ArrayList<OFMessage> buff) {
	
		int count = 0;
	
		for (int i = 0; i < buff.size(); i++) {
	
			ChannelFuture nodeChannelF = this.getChannelFuture();
	
			if (nodeChannelF.getChannel().isWritable()) {
	
				nodeChannelF.getChannel().write(buff.get(i));
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.debug("Channel is not writable!");
				i = i - 1;
	
			}
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	public void sendUnknownPacketLatency(ArrayList<OFMessage> buff) {
		ChannelFuture nodeChannelF = this.getChannelFuture();
		iniTime = System.nanoTime();

		n: while (true) {// 这个地方就是跳转标识,可用来标记地方 里层循环 break直接跳出外层循环，继续执行外层循环

			while (Global.BUFF_INDEX != Global.BUFF_SIZE - 1) {

				if (nodeChannelF.getChannel().isWritable()) {

					// record time
					if (System.nanoTime() - iniTime < (long) Global.TEST_TIME * 1000000000) {
						if (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN
								.size() == Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size()) {

							nodeChannelF.getChannel().write((OFMessage) buff.get(Global.BUFF_INDEX++));
							tempTime = System.nanoTime();
							Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.add(tempTime);

						}
					} else {

						break n;
					}

				} else {
					logger.debug("Channel is not writable!");
				}

			}

			Global.BUFF_INDEX = 0;
		}
	}

	public void sendUnknownPacketLatency_ofMessage(OFMessage oFEchoRequest) {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ChannelFuture nodeChannelF = this.getChannelFuture();
		iniTime = System.nanoTime();

		while (true) {

			if (nodeChannelF.getChannel().isWritable()) {

				// record time
				if (System.nanoTime() - iniTime < (long) Global.TEST_TIME * 1000000000) {
					if (Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN
							.size() == Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFOUT.size()) {

						nodeChannelF.getChannel().write((OFMessage) oFEchoRequest);
						Result.ASYNCHRONOUS_MESSAGE_PROCESSING_TIME_LIST_OFIN.add(System.nanoTime());

					}
				} else {

					break;
				}

			} else {
				logger.debug("Channel is not writable!");
			}

		}

	}

	public void sendUnknownPacketLatency_arp(ArrayList<OFMessage> buff, int endIndex) {
		ChannelFuture nodeChannelF = this.getChannelFuture();
		iniTime = System.nanoTime();

		int count = 1;

		while (count < buff.size() - 3) {

			if (nodeChannelF.getChannel().isWritable()) {

				// record time
				if (System.nanoTime() - iniTime < (long) Global.TEST_TIME * 1000000000) {// 返回系统计时器的当前值，以毫微秒为单位。
					if (Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD
							.size() == Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size()) {

						nodeChannelF.getChannel().write((OFMessage) buff.get(count++));

						tempTime = System.nanoTime();
						Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.add(tempTime);
					}
				} else {

					break;
				}
			} else {
				logger.debug("Channel is not writable!");
			}
		}

	}

	public void sendUnknownPacketLatency_rpp(ArrayList<OFMessage> buff) {
		ChannelFuture nodeChannelF = this.getChannelFuture();
		iniTime = System.nanoTime();
		int count = 0;

		n: while (true) {

			while (count < buff.size() - 3) {

				if (nodeChannelF.getChannel().isWritable()) {

					// record time
					if (System.nanoTime() - iniTime < (long) Global.TEST_TIME * 1000000000) {
						if (Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD
								.size() == Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size()) {

							nodeChannelF.getChannel().write((OFMessage) buff.get(count++));
							tempTime = System.nanoTime();
							Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.add(tempTime);
						}
					} else {

						break n;
					}
				} else {
					logger.debug("Channel is not writable!");
				}
			}

		}
	}

	public void sendUnknownPacketLatency_rpp2(ArrayList<OFMessage> buff) {
		ChannelFuture nodeChannelF = this.getChannelFuture();
		iniTime = System.nanoTime();
		int count = 0;

		n: while (true) {

			while (count < buff.size() - 3) {

				if (nodeChannelF.getChannel().isWritable()) {

					// record time
					if (System.nanoTime() - iniTime < (long) Global.TEST_TIME * 1000000000) {
						if (Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_FLOWMOD
								.size() == Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.size()) {

							Global.count = count;
							// Log.ADD_LOG_PANEL(String.valueOf(Global.count),
							// Node.class.toString());

							nodeChannelF.getChannel().write((OFMessage) buff.get(count++));
							tempTime = System.nanoTime();
							Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.add(tempTime);
						}
					} else {

						break n;
					}
				} else {
					logger.debug("Channel is not writable!");
				}
			}

		}
	}

	public void sendUnknownPacketThroughput_ofMessage(OFMessage oFEchoRequest) {
		List<OFMessage> msgList = new ArrayList<OFMessage>();
		try {
			for (int i = 0; i < Global.BULK_SIZE; i++) {
				msgList.add(oFEchoRequest);
			}

			new Thread(new Runnable() {

				public void run() {
					try {
						Thread.sleep(1);
						Tasks.HAS_STARTED = true;
						AsynchronousMessageProcessingRate.HAS_STARTED = true;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();

			ChannelFuture nodeChannelF = this.getChannelFuture();
			int x = 0;
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

	public void sendUnknownPacketThroughput(ArrayList<OFMessage> buff) {
		new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(Global.WARMING_TIME);
					Tasks.HAS_STARTED = true;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

		ChannelFuture nodeChannelF = this.getChannelFuture();

		iniTime = System.nanoTime();

		n: while (true) {

			while (Global.BUFF_INDEX != Global.BUFF_SIZE - 1) {

				if (nodeChannelF.getChannel().isWritable()) {

					if (System.nanoTime() - iniTime < (long) (Global.TEST_TIME + Global.WARMING_TIME / 1000)
							* 1000000000) {
						nodeChannelF.getChannel().write(buff.get(Global.BUFF_INDEX++));
					} else {

						break n;
					}

				}

				else {
					logger.debug("Channel is not writable!");
				}
			}
			Global.BUFF_INDEX = 0;

		}
		Tasks.HAS_STARTED = false;

	}

	public void sendUnknownPacketThroughput_arp(ArrayList<OFMessage> buff, int endIndex) {
		Global.BUFF_INDEX = 1;

		Tasks.HAS_STARTED = true;

		List<List> l = new ArrayList<List>();

		try {
			ChannelFuture nodeChannelF = this.getChannelFuture();
			iniTime = System.nanoTime();
			n: while (true) {

				List<OFMessage> msgList = new ArrayList<OFMessage>();

				while (msgList.size() != Global.BULK_SIZE) {
					if (Global.BUFF_INDEX >= buff.size()) {
						break n;
					} else {

						msgList.add(buff.get(Global.BUFF_INDEX++));
						Global.BUFF_INDEX++;

					}
				}
				l.add(msgList);

			}

			int count = 0;
			for (int i = 4; i > 1; i--) {
				for (int j = 0; j < 5; j++) {
					nodeChannelF.getChannel().write(l.get(count++));
					Thread.sleep(i);
				}

			}

			for (int i = 15; i < l.size() - 1;) {

				if (nodeChannelF.getChannel().isWritable()) {

					nodeChannelF.getChannel().write(l.get(i));
					for (int j = 0; j < l.get(i).size() * 2; j++) {
						Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.add(System.nanoTime());
					}

					i++;

					Thread.sleep(0, 1);

				}

				else {
					System.out.println("Channel is not writable!");
					logger.debug("Channel is not writable!");

				}
			}

			Tasks.HAS_STARTED = false;

			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendUnknownPacketThroughput_rpp(ArrayList<OFMessage> buff) {

		Tasks.HAS_STARTED = true;

		List<List> l = new ArrayList<List>();
		try {
			ChannelFuture nodeChannelF = this.getChannelFuture();
			iniTime = System.nanoTime();
			n: while (true) {

				List<OFMessage> msgList = new ArrayList<OFMessage>();

				while (msgList.size() != Global.BULK_SIZE) {
					if (Global.BUFF_INDEX == buff.size()) {
						break n;
					} else {
						msgList.add(buff.get(Global.BUFF_INDEX++));

					}
				}
				l.add(msgList);

			}

			int count = 0;
			for (int i = 4; i > 1; i--) {
				for (int j = 0; j < 5; j++) {
					nodeChannelF.getChannel().write(l.get(count++));
					Thread.sleep(i);
				}

			}

			for (int i = 15; i < l.size() - 1;) {

				if (nodeChannelF.getChannel().isWritable()) {

					nodeChannelF.getChannel().write(l.get(i));
					Thread.sleep(0, 1);
					for (int j = 0; j < l.get(i).size(); j++) {
						Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.add(System.nanoTime());
					}
					i++;
				}

				else {
					logger.debug("Channel is not writable!");
				}
			}
			Tasks.HAS_STARTED = false;

			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendUnknownPacketThroughput_RPPR(ArrayList<OFMessage> buff) {

		List<List> l = new ArrayList<List>();
		try {
			ChannelFuture nodeChannelF = this.getChannelFuture();

			n: while (true) {

				List<OFMessage> msgList = new ArrayList<OFMessage>();

				while (msgList.size() != Global.BULK_SIZE) {
					if (Global.BUFF_INDEX == buff.size()) {
						break n;
					} else {
						msgList.add(buff.get(Global.BUFF_INDEX++));

					}
				}
				l.add(msgList);

			}

			int count = 0;
			for (int i = 4; i > 1; i--) {
				for (int j = 0; j < 5; j++) {
					nodeChannelF.getChannel().write(l.get(count++));
					Thread.sleep(i);
				}

			}

			// ReactivePathProvisioningRate.HAS_STARTED = true;

			for (int i = 15; i < l.size() - 1;) {

				if (nodeChannelF.getChannel().isWritable()) {

					nodeChannelF.getChannel().write(l.get(i));

					for (int j = 0; j < l.get(i).size(); j++) {
						Result.REACTIVE_PATH_PROVISIONING_TIME_LIST_OFIN.add(System.nanoTime());
					}
					i++;
					Thread.sleep(ReactivePathProvisioningRate.SLEEP_TIME_zs,
							ReactivePathProvisioningRate.SLEEP_TIME_xs);
				}

				else {
					logger.debug("Channel is not writable!");
				}
			}
			ReactivePathProvisioningRate.HAS_STARTED = false;
			Thread.sleep(5000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendUnknownPacketThroughput_fms(ArrayList<OFMessage> buff) {
		ChannelFuture nodeChannelF = this.getChannelFuture();
		int times = 0;
		int numberOfFlowMod = 0;
		int timeOutTime = Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP;
		n: while (true) {
			for (int i = 0; i < Global.BUFF_SIZE; i++) {

				if (numberOfFlowMod != Global.ROOTNODE.getNumberOfFlowEntry()) {
					if (times >= 4) {
						break n;
					}
					timeOutTime = timeOutTime * Global.MULTIPLE;
					times++;
					try {
						Thread.sleep(timeOutTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					if (nodeChannelF.getChannel().isWritable()) {

						nodeChannelF.getChannel().write(buff.get(i));
						numberOfFlowMod++;

						if (times == 0) {
							Global.COUNT_METRIC_10[0]++;
						} else if (times == 1) {
							Global.COUNT_METRIC_10[1]++;
						} else if (times == 2) {
							Global.COUNT_METRIC_10[2]++;
						} else if (times == 3) {
							Global.COUNT_METRIC_10[3]++;
						}

						times = 0;

						timeOutTime = Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP;
						try {
							Thread.sleep(timeOutTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						logger.debug("Channel is not writable!");
					}

				}

			}
		}
	}

	public void sendPacket_sleep(ArrayList<OFMessage> buff, int start, int end) {

		for (; start < buff.size() - 2;) {
			ChannelFuture nodeChannelF = this.getChannelFuture();

			if (nodeChannelF.getChannel().isWritable()) {

				nodeChannelF.getChannel().write(buff.get(start++));

			} else {
				logger.debug("Channel is not writable!");
			}

		}

	}

	public void sendPacket_withoutsleep(ArrayList<OFMessage> buff, int start, int end) {

		for (; start < end; start++) {
			ChannelFuture nodeChannelF = this.getChannelFuture();

			if (nodeChannelF.getChannel().isWritable()) {

				nodeChannelF.getChannel().write(buff.get(start));

			} else {
				logger.debug("Channel is not writable!");
			}

		}

	}

	public void sendUnknownPacketThroughput_fms_new_arp(ArrayList<OFMessage> buf) {
		int dialogResult = JOptionPane.CANCEL_OPTION;
		Object[] options = { "Continue", "Return result", "Remind me later" };
		long iniTime = System.currentTimeMillis();

		ChannelFuture nodeChannelF = this.getChannelFuture();
		try {

			int timeoutTime = 0;
			int k = 0;
			n: while (true) {

				while (k <= (Global.BUFF_SIZE * 2 - 10000)) {
					if (System.currentTimeMillis() - iniTime > (long) 1800000
							&& dialogResult == JOptionPane.CANCEL_OPTION) {
						dialogResult = JOptionPane.showOptionDialog(null,
								"Would you like to continue the test? \n Cost Time: "
										+ ((System.currentTimeMillis() - Global.TEST_INIT_TIME) / 1000)
										+ " s\n Current Flow Number: " + Result.TOTAL_NUMBER_OF_FLOW_MOD,
								"Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
								options, options[2]);

						if (dialogResult == JOptionPane.YES_OPTION) {

						} else if (dialogResult == JOptionPane.NO_OPTION) {
							break n;
						} else {
							iniTime = System.currentTimeMillis();
						}

					}

					for (int i = 0; i < 10000; i++) {
						if (nodeChannelF.getChannel().isWritable()) {

							nodeChannelF.getChannel().write(buf.get(k++));
							Thread.sleep(3);
							nodeChannelF.getChannel().write(buf.get(k++));

						} else {
							logger.debug("Channel is not writable!");
						}

					}

					while (true) {

						logger.info(Result.TEMP_NUMBER_OF_FLOW_MOD + "");
						if (Result.TEMP_NUMBER_OF_FLOW_MOD >= 8000) {
							Result.TOTAL_NUMBER_OF_FLOW_MOD = Result.TOTAL_NUMBER_OF_FLOW_MOD
									+ Result.TEMP_NUMBER_OF_FLOW_MOD;
							Result.TEMP_NUMBER_OF_FLOW_MOD = 0;
							timeoutTime = 0;
							break;

						} else if (timeoutTime == 2) {
							Result.TOTAL_NUMBER_OF_FLOW_MOD = Result.TOTAL_NUMBER_OF_FLOW_MOD
									+ Result.TEMP_NUMBER_OF_FLOW_MOD;
							break n;
						} else if (timeoutTime == 1) {
							timeoutTime = 2;
							Thread.sleep(Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP * 10);
						} else if (timeoutTime == 0) {
							timeoutTime = 1;
							Thread.sleep(Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP);
						}

					}
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendUnknownPacketThroughput_fms_new(ArrayList<OFMessage> buf) {
		int dialogResult = JOptionPane.CANCEL_OPTION;
		Object[] options = { "Continue", "Return result", "Remind me later" };
		long iniTime = System.currentTimeMillis();

		ChannelFuture nodeChannelF = this.getChannelFuture();
		try {

			int timeoutTime = 0;
			int k = 0;
			n: while (true) {

				while (k <= (Global.BUFF_SIZE - 10000)) {
					if (System.currentTimeMillis() - iniTime > (long) 1800000
							&& dialogResult == JOptionPane.CANCEL_OPTION) {
						dialogResult = JOptionPane.showOptionDialog(null,
								"Would you like to continue the test? \n Cost Time: "
										+ ((System.currentTimeMillis() - Global.TEST_INIT_TIME) / 1000)
										+ " s\n Current Flow Number: " + Result.TOTAL_NUMBER_OF_FLOW_MOD,
								"Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
								options, options[2]);

						if (dialogResult == JOptionPane.YES_OPTION) {

						} else if (dialogResult == JOptionPane.NO_OPTION) {
							break n;
						} else {
							iniTime = System.currentTimeMillis();
						}

					}

					for (int i = 0; i < 10000; i++) {
						if (nodeChannelF.getChannel().isWritable()) {

							nodeChannelF.getChannel().write(buf.get(k++));
							Thread.sleep(0, 333333);

						} else {
							logger.debug("Channel is not writable!");
						}

					}

					while (true) {

						logger.info(Result.TEMP_NUMBER_OF_FLOW_MOD + "");
						if (Result.TEMP_NUMBER_OF_FLOW_MOD >= 8000) {
							Result.TOTAL_NUMBER_OF_FLOW_MOD = Result.TOTAL_NUMBER_OF_FLOW_MOD
									+ Result.TEMP_NUMBER_OF_FLOW_MOD;
							Result.TEMP_NUMBER_OF_FLOW_MOD = 0;
							timeoutTime = 0;
							break;

						} else if (timeoutTime == 2) {
							Result.TOTAL_NUMBER_OF_FLOW_MOD = Result.TOTAL_NUMBER_OF_FLOW_MOD
									+ Result.TEMP_NUMBER_OF_FLOW_MOD;
							break n;
						} else if (timeoutTime == 1) {
							timeoutTime = 2;
							Thread.sleep(Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP * 10);
						} else if (timeoutTime == 0) {
							timeoutTime = 1;
							Thread.sleep(Global.THRESHOLD_FORWARDING_TABLE_CAPACITY_NRP);
						}

					}
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendUnknownPacketLatency_test(ArrayList<OFMessage> buf) {
		ChannelFuture nodeChannelF = this.getChannelFuture();
		nodeChannelF.getChannel().write(buf.get(0));
		nodeChannelF.getChannel().write(buf.get(1));

	}

	public long sendPacketIn(OFMessage ofpi) {
		ChannelFuture nodeChannelF = this.getChannelFuture();
		if (nodeChannelF.getChannel().isWritable()) {

			nodeChannelF.getChannel().write(ofpi);

			return System.nanoTime();
		} else {
			logger.debug("Channel is not writable!");
		}
		return 0;
	}

}
