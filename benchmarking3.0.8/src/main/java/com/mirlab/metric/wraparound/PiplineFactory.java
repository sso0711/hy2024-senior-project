package com.mirlab.metric.wraparound;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpResponseDecoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;

import io.netty.handler.codec.http.HttpObjectAggregator;


public class PiplineFactory implements ChannelPipelineFactory {

	private BaseHandler baseHandler;
	static OrderedMemoryAwareThreadPoolExecutor e = new OrderedMemoryAwareThreadPoolExecutor(16, 0, 0);
	static HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();
	static ExecutionHandler executionHandler = new ExecutionHandler(e);

	public PiplineFactory(BaseHandler baseHandler) {
		super();
		this.baseHandler = baseHandler;
	}

	public PiplineFactory() {
		// TODO Auto-generated constructor stub
	}

	public ChannelPipeline getPipeline() throws Exception {
		System.out.println("## pipe line ");
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("codec", new HttpClientCodec());
		pipeline.addLast("decoder", new HttpResponseDecoder());
		pipeline.addLast("executor", executionHandler);
		pipeline.addLast("handler", baseHandler);
		pipeline.addLast("timeout", new ReadTimeoutHandler(hashedWheelTimer, 100, TimeUnit.MILLISECONDS));
//		pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
		return pipeline;
	}

}
