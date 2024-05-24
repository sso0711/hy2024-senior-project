package com.mirlab.metric.northbound;


/**
 * 
 * @author Lee Gilho

 * Wrab around metric : AsyncProvisioningPathTime 
 * 
 * This Class is start to metric 
 * 
 */

import com.mirlab.lib.Log;
import com.mirlab.metric.southbound.ReactivePathProvisioningTime;
import com.mirlab.topo.CreateTopo;




public class AsyncProactiveProvisioningTime {
	
	public static boolean HAS_STARTED = false;
	

	public static void go() {
		version0();
		Log.exportLog(ReactivePathProvisioningTime.class.getSimpleName(), 5);
	}

	private static void version0() {
		// TODO Auto-generated method stub

		try {
			
			Thread.sleep(5000);
			
			HAS_STARTED = true;
			
			AsyncClientProactivePathProvisioningTime agent = new AsyncClientProactivePathProvisioningTime();
			agent.setRestClient(agent);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			
			HAS_STARTED = false;
		}

	}

	public void config() {

	}

	public void result() {

	}

}
