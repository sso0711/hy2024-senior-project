  package com.mirlab.metric.northbound2;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
import com.mirlab.lib.Result;
import com.mirlab.lib.Tasks;
import com.mirlab.openflow.BaseHandler;
import com.ning.http.client.AsyncHttpClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gilho
 * @version 2018/12/31
 * @param <CloseableHttpAsyncClient>
 */
public class SsyncProactiveProTime {
	public static Logger logger = LoggerFactory.getLogger(BaseHandler.class);
	private static int count = 0;

	public static long startTime;
	public static long lastTime;

	public static long startTime2;
	public static long lastTime2;

	public static double avgTime = 0;

	
	public static int flowLengthNum = 0; // check flow num : This number starts with the number of ( switches * 5 ).
	public static int flowMaxNum = 0; // check flow num : This number is of ( switches * 5 + added flow num that user
										// set ).
	public static int flowNum = 0;

	private static int checkLoop = 1;

	public static String onosIP = null;
 
	public static DecimalFormat df = new DecimalFormat("#0.00");

	static double e = 0;
	
	public static String dstPrimaryKey = null;
	public static String srcPrimaryKey = null;

	public static ConcurrentHashMap<String, Long> srcMap = new ConcurrentHashMap<String, Long>();

	public SsyncProactiveProTime() {

	}
	

	public static String setDst() {
		String dst = null;

		String random1 = String.valueOf((int) (Math.random() * 10));
		String random2 = String.valueOf((int) (Math.random() * 10));
		String random3 = String.valueOf((int) (Math.random() * 10));
		String random4 = String.valueOf((int) (Math.random() * 10));
		String random5 = String.valueOf((int) (Math.random() * 10));

		String key = random1 + random2 + random3 + random4 + random5;

		if (Global.proStartTime.containsKey(key)) {

			setDst();

		} else {

			dst = "4E:1F:61:B" + random1 + ":" + random2 + random3 + ":" + random4 + random5;

			setDstKey(key);

			return dst;
		}

		String notMeanValue = "52:91:81:26:2A:01";

		return notMeanValue;

	}

	public static void setSrcKey(String key) {
		srcPrimaryKey = key;
	}
	
	public static String setSrc() {
		String dst = null;

		String random1 = String.valueOf((int) (Math.random() * 10));
		String random2 = String.valueOf((int) (Math.random() * 10));
		String random3 = String.valueOf((int) (Math.random() * 10));
		String random4 = String.valueOf((int) (Math.random() * 10));
		String random5 = String.valueOf((int) (Math.random() * 10));

		String key = random1 + random2 + random3 + random4 + random5;

		if (srcMap.containsKey(key)) {

			setDst();

		} else {

			dst = "52:91:81:2" + random1 + ":" + random2 + random3 + ":" + random4 + random5;

			setDstKey(key);

			return dst;
		}

		String notMeanValue = "52:91:81:26:2A:01";

		return notMeanValue;

	}

	public static void setDstKey(String key) {
		dstPrimaryKey = key;
	}

	public static void go() throws ClientProtocolException, IOException {

		flowLengthNum = (Global.NUMBER_OF_TEST_SWITCH * 5 + 1);

		flowNum = Global.NUMBER_OF_TEST_FLOW;

		flowMaxNum = flowLengthNum + (flowNum - 1);

		onosIP = Global.SDN_CONTROLLER_IP[0];

		System.out.println("### test flow NUM :   " + flowNum);
		System.out.println("### test flowMaxNum :   " + flowMaxNum);
		System.out.println("### test onosIP :   " + onosIP);
		System.out.println("### test flowLengthNum :   " + flowLengthNum);

		version0();
	}

	public static void version0() throws ClientProtocolException, IOException {

		for (int i = 1; i < flowNum+1; i++) {

			postJson(i); // include post method
			

		}
		
		Log.N_ADD_LOG_PANEL("######### [AVG] (Serialize)Provisioning Time   :    "+String.valueOf(e), SsyncProactiveProTime.class.getSimpleName());;
		Result.N_ADD_RESULT(df.format(e), 9, 2);
		
		Initializer.INITIAL_CHANNEL_POOL();


	}

	public static void version0(int num) throws ClientProtocolException, IOException {
		onosIP = Global.SDN_CONTROLLER_IP[0];

		postJson(num); // include post method

	}

	// make a json message
	public static void postJson(int i) {

		
		String setDst = setDst(); // for distinguish to FlowMod message
		String setSrc = setSrc(); // for distinguish to FlowMod message

		JSONObject jsobjpop = new JSONObject();
		jsobjpop.put("appId", "org.onosproject.testfwd");
		jsobjpop.put("groupId", 0);
		jsobjpop.put("priority", i);
		jsobjpop.put("timeout", 10000);
		jsobjpop.put("deviceId", "of:0000000000000001");// 변경필요
		jsobjpop.put("state", "ADDED");
		jsobjpop.put("life", 9069);
		jsobjpop.put("isPermanent", "false");
		jsobjpop.put("packets", 18129);
		jsobjpop.put("bytes", 1776642);
		JSONArray popinstructions = new JSONArray();

		JSONObject popinstructionsObj = new JSONObject();
		popinstructionsObj.put("type", "OUTPUT");
		popinstructionsObj.put("port", 2); // 변경필요

		//
		JSONObject popinstructionsObj2 = new JSONObject();
		popinstructionsObj2.put("type", "GROUP");
		popinstructionsObj2.put("groupid", 1); // 변경필요

		popinstructions.put(0, popinstructionsObj);
		JSONArray popdeferred = new JSONArray();
		JSONObject poptreatment = new JSONObject();
		poptreatment.put("instructions", popinstructions);
		poptreatment.put("deferred", popdeferred);
		jsobjpop.put("treatment", poptreatment);

		JSONArray popcriteria = new JSONArray();

		JSONObject popcriteria1 = new JSONObject();
		popcriteria1.put("type", "ETH_DST");
		popcriteria1.put("mac", setDst);
		popcriteria.put(popcriteria1);
		//
		JSONObject popcriteria2 = new JSONObject();
		popcriteria2.put("type", "ETH_SRC");
		popcriteria2.put("mac", setSrc); // 변경필요
		popcriteria.put(popcriteria2);

		JSONObject popcriteria3 = new JSONObject();
		popcriteria3.put("type", "IN_PORT");
		popcriteria3.put("port", 3); // 변경필요
		popcriteria.put(popcriteria3);

		JSONObject popselectorCont = new JSONObject();
		popselectorCont.put("criteria", popcriteria);
		jsobjpop.put("selector", popselectorCont);

		try {
//			System.out.println((i + 1) + "loop post");
			post(jsobjpop, i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// action : post to Onos
	public static void post(JSONObject json, int num) throws Exception {

		HttpClient client = new DefaultHttpClient();
		System.out.println("from now on ");
//		String URL = "http://192.168.1.114:8181/onos/v1/flows/of%3A0000000000000002?appId=org.onosproject.testfwd";
		String URL = "http://" + onosIP + ":8181/onos/v1/flows/of%3A0000000000000001?appId=org.onosproject.testfwd";

		
		HttpPost post = new HttpPost(URL);
		String encoding = DatatypeConverter.printBase64Binary("karaf:karaf".getBytes("UTF-8"));
		post.setHeader("Authorization", "Basic " + encoding);
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Connection", "keep-alive");
		
		System.out.println("##req header : " + encoding);
	
		//

		try {

			StringEntity s = new StringEntity(json.toString(), "utf-8");

			post.setEntity(s);
			// end request
			startTime = System.nanoTime() / 1000000;
			System.out.println("send ::::: " + num);
			if (num > 1) {
				Global.startTime.put(num, String.valueOf(startTime));
			}
			HttpResponse httpResponse3 = client.execute(post);

//			CloseableHttpResponse httpResponse3 = (CloseableHttpResponse) client.execute(post);
			StatusLine statusLine = httpResponse3.getStatusLine();

			StringEntity parseStringParams = new StringEntity(httpResponse3.toString(), "UTF-8");
			System.out.println("############# get Params : " + parseStringParams);
			int responseCode = statusLine.getStatusCode();


			System.out.println(responseCode);

			if (responseCode == 201) {
				///
				lastTime = System.nanoTime() / 1000000;

				System.out.println("response  ::::: " + num);

				if (num > 1) {
					Global.lastTime.put(num, lastTime);
				}

				count = count + 1;
				System.out.println("<<MirLab Benchmark>> Number of flows added   count :  " + count);
				Log.N_ADD_LOG_PANEL("<<MirLab Benchmark>> Number of flows added   count :  " + count + startTime, SsyncProactiveProTime.class.getSimpleName());
				Log.N_ADD_LOG_PANEL("\n", SsyncProactiveProTime.class.getSimpleName());
				System.out.println("########## Current StartTime :: " + startTime);
				System.out.println("########## Current LastTime :: " + lastTime);
				System.out.println("########## Current Provisioning Time :: " + (lastTime - startTime));
				Log.N_ADD_LOG_PANEL("########## Current StartTime :: " + startTime, SsyncProactiveProTime.class.getSimpleName());
				Log.N_ADD_LOG_PANEL("########## Current LastTime :: " + lastTime, SsyncProactiveProTime.class.getSimpleName());
				Log.N_ADD_LOG_PANEL("\n", SsyncProactiveProTime.class.getSimpleName());
				Log.N_ADD_LOG_PANEL("########## Current Provisioning Time :: " + (lastTime - startTime), SsyncProactiveProTime.class.getSimpleName());
				Log.N_ADD_LOG_PANEL("\n", SsyncProactiveProTime.class.getSimpleName());
				Log.N_ADD_LOG_PANEL("\n", SsyncProactiveProTime.class.getSimpleName());

				
				if(num > 1) {
				avgTime += (lastTime - startTime);
				}
				checkLoop++;

				System.out.println(" ");
				System.out.println(" ");
				System.out.println(" ");
				
				System.out.println("num : " + num   +  " flownum :  " + flowNum);
				if(num == flowNum) {
					
					e =  Math.round((avgTime / (flowNum-1))*100)/100.0;
					
					System.out.println("######### [AVG] Provisioning Time : " + e );
					

				}


			}
			
			
			
		} catch (Exception e) {
			System.out.println("Request exception");
			e.printStackTrace();
		}

	}

}
