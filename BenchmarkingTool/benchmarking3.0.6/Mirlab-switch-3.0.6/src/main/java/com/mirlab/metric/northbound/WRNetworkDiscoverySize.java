package com.mirlab.metric.northbound;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Main;
import com.mir.ui.GUI;
import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mir.ui.N_Result;
import com.mirlab.lib.Tasks;
import com.mirlab.openflow.BaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Haojun E-mail: lovingcloud77@gmail.com
 * @version 创建时间：2017年4月14日 下午12:02:33 类说明
 */
public class WRNetworkDiscoverySize {
	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);

	public void go() throws ClientProtocolException, IOException {
		version0();
	}

	private void version0() throws ClientProtocolException, IOException {

		Main.gui.N_startButton.setEnabled(false);

		
		WRNetDisSizThread getTopo =new WRNetDisSizThread();
		getTopo.start();
		
		
		

		
		
		// int timeoutTime = 0;
		long dpid = Global.SWITCH_ID_OFF_SET;
		int nodeId = 1;
		// int totalCount = 0;
		int count = 0;
		Node preNode = null;
		Node[] nodes = new Node[10000];

		nodes[0] = new Node((long) dpid++, Global.FACTORY, 0);
		nodes[0].creatPort(2);
		preNode = nodes[0];
		ConnectionThread root = new ConnectionThread(nodes[0], "192.168.1.107", 6633);
		root.start();
		// buf node
		
		for (int i = 1; i < 350; i++) {

			nodes[i] = new Node((long) dpid++, Global.FACTORY, nodeId++);
			nodes[i].creatPort(2);
			nodes[i].getPortList().get(0).setConnectedPort(preNode.getPortList().get(1));
			preNode.getPortList().get(1).setConnectedPort(nodes[i].getPortList().get(0));
			preNode.setNextNode(nodes[i]);
			preNode = nodes[i];
			ConnectionThread node = new ConnectionThread(nodes[i], "192.168.1.107", 6633);

			node.start();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (i==349){
				System.out.println("全发完了");	
			}
		}

		
		
		


		// int j = 0;
		// n: while (j != 10000) {
		// int stopNumber = j;
		//
		// if (j != 0) {
		// ConnectionThread ct1 = new ConnectionThread(nodes[j++],
		// Global.SDN_CONTROLLER_IP[0],
		// Global.SDN_CONTROLLER_PORT);
		// ct1.start();
		// Thread.sleep(1);
		// preNode.getPortList().get(1).setConnectedPort(nodes[j -
		// 1].getPortList().get(0));
		//
		// } else {
		// ConnectionThread ct1 = new ConnectionThread(nodes[j++],
		// Global.SDN_CONTROLLER_IP[0],
		// Global.SDN_CONTROLLER_PORT);
		// ct1.start();
		// Thread.sleep(1);
		// }
		//
		// for (int i = 1; i < 99; i++) {
		// if (!Global.BENCHMARK_NO_BUFF) {
		// ConnectionThread ct1 = new ConnectionThread(nodes[j++],
		// Global.SDN_CONTROLLER_IP[0],
		// Global.SDN_CONTROLLER_PORT);
		// ct1.start();
		// Thread.sleep(1);
		// } else {
		// break;
		// }
		// }
		//
		// ConnectionThread ct1 = new ConnectionThread(nodes[j++],
		// Global.SDN_CONTROLLER_IP[0],
		// Global.SDN_CONTROLLER_PORT);
		// nodes[j - 1].getPortList().get(1).setConnectedPort(null);
		// preNode = nodes[j - 1];
		// ct1.start();
		//
		// Thread.sleep(8000);
		//
		// while (true) {
		// for (int i = stopNumber; i < stopNumber + 100; i++) {
		// if (nodes[i].getNumberOflldpPacket() >= 2) {
		// count++;
		//
		// }
		// }
		//
		// logger.info(count + "");
		// if (count >= 90) {
		// totalCount = totalCount + count;
		// count = 0;
		// timeoutTime = 0;
		// break;
		//
		// } else if (timeoutTime == 1) {
		// totalCount = totalCount + count;
		// break n;
		// } else {
		// timeoutTime = 1;
		// Thread.sleep(Global.THRESHOLD_NETWORK_DISCOVERY_SIZE_NS);
		// count = 0;
		// }
		// }
		//
		// }
		//
		// Result.ADD_RESULT(totalCount + "", 0, 5);
		// Log.ADD_LOG_PANEL("Test Completed!", Tasks.class.toString());
		// Log.ADD_LOG_PANEL("The number of Node:" + totalCount,
		// Tasks.class.toString());
		//
		// for (int i = 0; i < 10000; i++) {
		// if (nodes[i].getChannelFuture() != null) {
		// nodes[i].getChannelFuture().getChannel().close().awaitUninterruptibly();
		// nodes[i].getChannelFuture().cancel();
		// }
		// if (nodes[i].getBootstrap() != null) {
		// nodes[i].getBootstrap().releaseExternalResources();
		// }
		//
		// nodes[i] = null;
		// }
		// nodes = null;
		//
		// Main.progressBar.setValue(Main.progressBar.getMaximum());
		// Main.progressBarTotal.setValue(1);
		// Initializer.INITIAL_CHANNEL_POOL();
		//

	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		WRNetworkDiscoverySize nds = new WRNetworkDiscoverySize();
		nds.go();
	}
}
