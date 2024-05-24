package com.mirlab.distributedPort.client;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.util.HashedWheelTimer;

public class DistributedPortFactory implements ChannelPipelineFactory {

	private DistributedPortClientBaseHandler baseHandler;
	static OrderedMemoryAwareThreadPoolExecutor e = new OrderedMemoryAwareThreadPoolExecutor(16, 0, 0);
	static HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();
	static ExecutionHandler executionHandler = new ExecutionHandler(e);

	public DistributedPortFactory(DistributedPortClientBaseHandler baseHandler) {
		super();
		this.baseHandler = baseHandler;
	}

	public ChannelPipeline getPipeline() throws Exception {

		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("executor", executionHandler);
		pipeline.addLast("handler", baseHandler);
//	    pipeline.addLast("timeout", new ReadTimeoutHandler(hashedWheelTimer, 100));
		return pipeline;
	}

}
