package com.mir.distributed.client;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mir.ui.S_RunButton;
import com.mirlab.enumType.Controller;
import com.mirlab.enumType.ControllerMode;
import com.mirlab.enumType.SouthboundMetric;
import com.mirlab.enumType.TopologyType;
import com.mirlab.global.Global;
import com.mirlab.openflow.BaseHandler;

public class DistributedClientBaseHandler extends IdleStateAwareChannelHandler {

	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		super.handleUpstream(ctx, e);

	}

	public DistributedClientBaseHandler() {
		super();

	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		logger.debug("Channel connected.");
		Global.MASTER_CHANNEL = ctx.getChannel();

	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		logger.debug("Channel closed.");
		Global.MASTER_CHANNEL = null;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {
		JSONObject message = new JSONObject(event.getMessage().toString());

		switch (message.getInt("msgType")) {
		case 0:
			Global.MY_ID = message.getInt("BenchmarkingTool_ID");
			Global.SWITCH_ID_OFF_SET = (Global.MY_ID + 1) * 100 * Global.SWITCH_ID_OFF_SET;
			Global.HOST_MAC = Global.SLAVES_HOST_MAC[Global.MY_ID];
			break;
		case 1:
			parseTasks(message);
			break;
		case 2:
			break;
		case 3:
			parseTasksNew(message);
			break;

		default:
			break;
		}
	}

	private void parseTasksNew(JSONObject message) {
		// TODO Auto-generated method stub

		Global.distribudtedTool_IP = new ArrayList<String>();
		Global.distribudtedTool_Port = new ConcurrentHashMap<String, Integer>();
		Global.distribudtedToolSever_Port = new ConcurrentHashMap<String, Integer>();

		Global.distribudtedTool_IP.add(message.getString("Tool_IP1"));
		Global.distribudtedTool_Port.put(message.getString("Tool_IP1"), message.getInt("Tool_Port1"));
		Global.distribudtedToolSever_Port.put(message.getString("Tool_IP1"), message.getInt("Tool_Sever_Port1"));

		Global.distribudtedTool_IP.add(message.getString("Tool_IP2"));
		Global.distribudtedTool_Port.put(message.getString("Tool_IP2"), message.getInt("Tool_Port2"));
		Global.distribudtedToolSever_Port.put(message.getString("Tool_IP2"), message.getInt("Tool_Sever_Port2"));

		S_RunButton.startTasks();
	}

	private void parseTasks(JSONObject message) {

		// ??????????????
		Global.southboundMetric = SouthboundMetric.valueOf(message.getString("metric"));
		Global.ofVersion = org.projectfloodlight.openflow.protocol.OFVersion.valueOf(message.getString("ofVersion"));
		Global.topoType = TopologyType.valueOf(message.getString("topoType"));
		Global.NUMBER_OF_TEST_SWITCH = message.getInt("numberOfSwitch");
		Global.TEST_TIME = message.getInt("testTime");
		Global.conMode = ControllerMode.valueOf(message.getString("controllerMode"));
		Global.SDN_CONTROLLER_IP[0] = message.getString("controllerIp");
		Global.SDN_CONTROLLER_PORT = message.getInt("controllerPort");
		Global.conTroller = Controller.valueOf(message.getString("controllerType"));
		Global.UNKNOWN_PACKET_TYPE = message.getInt("unknownPacketType");
		Global.PROVISINIONING_PACKET_TYPE = message.getInt("pPacketType");
		Global.HOST_IP[0] = message.getInt("hostOffset0");
		Global.HOST_IP[1] = message.getInt("hostOffset1") + ((Global.MY_ID + 1) * 5);

	}
}
