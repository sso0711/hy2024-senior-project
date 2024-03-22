package com.mirlab.netty3.openflow;
/** 
* @author Haojun E-mail: lovingcloud77@gmail.com
* @version ����ʱ�䣺2017��12��28�� ����9:17:17 
* ��˵�� 
*/

import java.util.Collections;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.projectfloodlight.openflow.protocol.OFFactories;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFVersion;

import com.mirlab.component.Agent;
import com.mirlab.lib.openFlow13.OpenFlow13;

public class ClientHandler extends IdleStateAwareChannelHandler {
	private Channel channel;
	private Agent agent;
	private OpenFlow13Handler OpenFlow13Handler = new OpenFlow13Handler();

	OFFactory factory = OFFactories.getFactory(OFVersion.OF_13);

	public ClientHandler(Agent agent) {
		this.agent = agent;
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		super.handleUpstream(ctx, e);

	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		channel = ctx.getChannel(); // returns the Channel that the ChannelPipeline belongs to.
		write(OpenFlow13.hello());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {
		OFMessage msg = (OFMessage) event.getMessage();
		OpenFlow13Handler.processOFMessage(ctx, msg, agent);
	}

	private void write(OFMessage m) {
		channel.write(Collections.singletonList(m));
	}

}
