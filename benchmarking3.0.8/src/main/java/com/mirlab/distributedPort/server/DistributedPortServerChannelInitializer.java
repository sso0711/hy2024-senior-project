package com.mirlab.distributedPort.server;

import com.mirlab.component.DistributedPort;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2018年11月16日 下午6:48:37 类说明
 */
public class DistributedPortServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	private DistributedPort distributedPort;

	public DistributedPortServerChannelInitializer(DistributedPort distributedPort) {
		this.distributedPort = distributedPort;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();

		p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));

		p.addLast("bytesDecoder", new ByteArrayDecoder());

		p.addLast("frameEncoder", new LengthFieldPrepender(4));
		p.addLast("bytesEncoder", new ByteArrayEncoder());

		p.addLast("handler", new DistributedPortServerHandler(distributedPort));
	}

}
