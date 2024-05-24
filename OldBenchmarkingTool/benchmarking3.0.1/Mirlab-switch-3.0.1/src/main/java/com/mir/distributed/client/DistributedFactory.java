package com.mir.distributed.client;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;




public class DistributedFactory implements ChannelPipelineFactory {

	  private DistributedClientBaseHandler baseHandler;
	  static OrderedMemoryAwareThreadPoolExecutor e = new OrderedMemoryAwareThreadPoolExecutor(16, 0, 0);  
	  static HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();  
	  static ExecutionHandler executionHandler = new ExecutionHandler(e);

	  public DistributedFactory(DistributedClientBaseHandler baseHandler) {
	    super();
	    this.baseHandler = baseHandler;
	  }
	  
	  public ChannelPipeline getPipeline() throws Exception {
		  
	    ChannelPipeline pipeline = Channels.pipeline();
	    pipeline.addLast("executor", executionHandler );  
	    pipeline.addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
	    pipeline.addLast("encoder", new ObjectEncoder());
	    pipeline.addLast("handler", baseHandler);
	    pipeline.addLast("timeout", new ReadTimeoutHandler(hashedWheelTimer, 100));
	    return pipeline;
	  }
	

}
