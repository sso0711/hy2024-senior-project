package com.mirlab.metric.northbound;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.jboss.netty.channel.ChannelEvent;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jhlabs.image.RescaleFilter;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

public class BaseHandler extends IdleStateAwareChannelHandler {
    private int count=0;
	public static long lastTime = 0;

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		super.handleUpstream(ctx, e);

	}

	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);

	private AsyncClientProactivePathProvisioningTime agent;

	public BaseHandler(AsyncClientProactivePathProvisioningTime agent) {
		super();
		this.agent = agent;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		System.out.println("connect rest server ## ");
		logger.debug("Channel connected.");
													// message
		// Timer.ADD_CURRENT_TIME(Global.HELLO_TIME_LIST);

	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		logger.debug("Channel closed.");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent msg) throws Exception {
		
		//it is that arrive time of flow from onos 
		lastTime = System.nanoTime() / 1000000;
		
		HttpResponse response = (HttpResponse)msg.getMessage();
		
//		System.out.println("## STATUS " + response.getStatus());
		
		String index = msg.getMessage().toString().substring(76, 82);
		
		StringTokenizer stk = new StringTokenizer(index, "/");
		
		String inputIndex = stk.nextToken();
	
//		System.out.println("## location header check  : " + inputIndex);
		
		
		AsyncClientProactivePathProvisioningTime.calTime(inputIndex,lastTime);
		
        System.err.println(msg); 
        
        ctx.getChannel().close(); // it it need for capacity program 
        
        System.out.println("### close channel :");
	}
	

}
