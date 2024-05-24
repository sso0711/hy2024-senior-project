package com.mir.distributed.client;
/** 
* @author Haojun E-mail: lovingcloud77@gmail.com
* @version 创建时间：2018年11月18日 上午12:00:15 
* 类说明 
*/

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

public class DistributedClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	private SslContext sslCtx;
	private String ip;
	private int port;

	public DistributedClientChannelInitializer(SslContext sslCtx, String ip, int port) {
		// TODO Auto-generated constructor stub
		this.sslCtx = sslCtx;
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc(), ip, port));
		}
		p.addLast(new StringEncoder(), new StringDecoder(), new DistributedClientHandler());
	}

}
