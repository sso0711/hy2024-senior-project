package com.mirlab.metric.wraparound;


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




public class AsyncProactiveProvisioningTimeWR {
	
	public static boolean HAS_STARTED = false;
	

	public static void go() {
		version0();
		Log.exportLog(ReactivePathProvisioningTime.class.getSimpleName(), 5);
	}

	private static void version0() {
		// TODO Auto-generated method stub

		try {
			
			init();

			Thread.sleep(10000);
			
			HAS_STARTED = true;
			
			AsyncClientProactivePathProvisioningTime_WR agent = new AsyncClientProactivePathProvisioningTime_WR();
			agent.setRestClient(agent);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			
			HAS_STARTED = false;
		}

	}

	public static void init() {
		
		CreateTopo ct = new CreateTopo();
		ct.go();
		
	}

	public void config() {

	}

	public void result() {

	}

}
