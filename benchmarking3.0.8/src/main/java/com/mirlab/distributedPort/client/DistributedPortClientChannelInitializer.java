package com.mirlab.distributedPort.client;
/** 
* @author Haojun E-mail: lovingcloud77@gmail.com
* @version 创建时间：2018年11月18日 上午12:00:15 
* 类说明 
*/

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class DistributedPortClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	public void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline p = ch.pipeline();

		p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));

		p.addLast("bytesDecoder", new ByteArrayDecoder());

		p.addLast("frameEncoder", new LengthFieldPrepender(4));
		p.addLast("bytesEncoder", new ByteArrayEncoder());

	}

}
