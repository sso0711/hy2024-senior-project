package com.mirlab.netty3.openflow;
/** 
* @author Haojun E-mail: lovingcloud77@gmail.com
* @version 创建时间：2017年12月28日 下午9:18:43 
* 类说明 
*/

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;

/**
 * Creates a newly configured {@link ChannelPipeline} for a client-side channel.
 */
public class PiplineFactory implements ChannelPipelineFactory {

	private ClientHandler clientHandler;
	static OrderedMemoryAwareThreadPoolExecutor e = new OrderedMemoryAwareThreadPoolExecutor(16, 0, 0);
	static HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();
	static ExecutionHandler executionHandler = new ExecutionHandler(e);

	public PiplineFactory(ClientHandler clientHandler) {
		super();
		this.clientHandler = clientHandler;
	}

	public ChannelPipeline getPipeline() throws Exception {

		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("executor", executionHandler);
		pipeline.addLast("decoder", new MessageDecoder());
		pipeline.addLast("encoder", new MessageEncoder());
		pipeline.addLast("handler", clientHandler);
		pipeline.addLast("timeout", new ReadTimeoutHandler(hashedWheelTimer, 100));
		return pipeline;
	}
}
