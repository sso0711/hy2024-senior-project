package com.mir.distributed.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月16日 下午6:48:37 类说明
 */
public class DistributedServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	private SslContext sslCtx;

	public DistributedServerChannelInitializer(SslContext sslCtx) {
		// TODO Auto-generated constructor stub

		this.sslCtx = sslCtx;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}
		p.addLast(new StringEncoder(), new StringDecoder(), new DistributedServerHandler());
	}

}
