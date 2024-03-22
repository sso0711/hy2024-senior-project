package com.mirlab.netty3.openflow;

import java.util.Collections;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.projectfloodlight.openflow.protocol.OFMessage;

import com.mirlab.component.Agent;
import com.mirlab.lib.openFlow13.OpenFlow13;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version ����ʱ�䣺2017��12��29�� ����6:04:40 ��˵��
 */
public class OpenFlow13Handler {
	private Channel channel;

	public void processOFMessage(ChannelHandlerContext ctx, OFMessage msg, Agent agent) {
		channel = ctx.getChannel();

		switch (msg.getType()) {

		case ECHO_REQUEST:
			write(OpenFlow13.echoReply(msg));
			break;

		case FEATURES_REQUEST:
			write(OpenFlow13.featuresReply(msg, agent));
			break;

		case GET_CONFIG_REQUEST:
			write(OpenFlow13.getConfigReply(msg, agent));
			break;

		case SET_CONFIG:
			OpenFlow13.setConfig(msg, agent);
			break;

		case PACKET_OUT:
			OpenFlow13.packetOut(msg, agent);
			break;

		case STATS_REQUEST:
			write(OpenFlow13.statsReply(msg, agent));
			break;

		case BARRIER_REQUEST:
			write(OpenFlow13.barrierReply(msg));
			break;

		case ROLE_REQUEST:
			write(OpenFlow13.roleReply(msg));
			break;

		default:

			break;

		}
	}

	private void write(OFMessage msg) {
		channel.write(Collections.singletonList(msg));
	}
}
