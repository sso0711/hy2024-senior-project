package com.test.mirlab.northbound;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mirlab.component.Host;
import com.mirlab.component.Node;
import com.mirlab.global.Global;
import com.mirlab.lib.Initializer;
import com.mirlab.lib.Log;
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
	static int count = 0;

	public static long startTime;
	public static long lastTime;
	public static double avgTime = 0;
	
	public static int flowLengthNum = 0; // check flow num   :   This number starts with the number of ( switches *  5 ).
	public static int flowMaxNum = 0; // check flow num :  This number is  of ( switches *  5 + added flow num that user set ).

	public SsyncProactiveProTime() {

	}

	
	// local test  
	public static void main(String args[]) throws ClientProtocolException, IOException {

		Scanner in = new Scanner(System.in);

		System.out.println(" start num ? : ");

		int num = in.nextInt();

		if (num == 1) {

			start();

		}
	}

	public static void start() throws ClientProtocolException, IOException {
		SsyncProactiveProTime cli = new SsyncProactiveProTime();

		cli.version0();

	}

	
	//
	
	public void go() throws ClientProtocolException, IOException {
		version0();
	}

	private void version0() throws ClientProtocolException, IOException {

		
		
		for (int i = 0; i < 10; i++) {

			postJson(i); // include post method

			getRest();

		}

	}

	public static boolean getRest() throws UnsupportedOperationException, IOException {

		String path = "http://192.168.1.114" + ":8181/onos/v1/flows";
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(path);

		String encoding = null;
		try {
			encoding = DatatypeConverter.printBase64Binary("karaf:karaf".getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		httpget.setHeader("Authorization", "Basic " + encoding);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		lastTime = System.currentTimeMillis();

		avgTime += (lastTime - startTime);

		StatusLine statusLine = response.getStatusLine();
		int responseCode = statusLine.getStatusCode();

		System.out.println(responseCode);

		if (responseCode == 200) {

			HttpEntity entity = response.getEntity();
			InputStream input = null;
			input = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));

			String str1 = br.readLine();

			String result = new String(str1.getBytes("gbk"), "UTF-8");

			JSONObject Topojson = new JSONObject(result);
			JSONArray Flows = Topojson.getJSONArray("flows");

			System.out.println("########## flows lenth :: " + Flows.length());
			
			
			
			if (Flows.length() == flowMaxNum) {
				
				System.out.println("########## provisioning avg time : " + avgTime / 10);

			}
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" ");

		} else {
			System.out.println("failed!");
		}

		return false;
	}

	// make a json message 
	public static void postJson(int i) {

		JSONObject jsobjpop = new JSONObject();
		jsobjpop.put("appId", "org.onosproject.testfwd");
		jsobjpop.put("groupId", 0);
		jsobjpop.put("priority", i);
		jsobjpop.put("timeout", 10000);
		jsobjpop.put("deviceId", "of:0000000000000002");// 변경필요
		jsobjpop.put("state", "ADDED");
		jsobjpop.put("life", 9069);
		jsobjpop.put("isPermanent", "false");
		jsobjpop.put("packets", 18129);
		jsobjpop.put("bytes", 1776642);
		JSONArray popinstructions = new JSONArray();

		JSONObject popinstructionsObj = new JSONObject();
		popinstructionsObj.put("type", "OUTPUT");
		popinstructionsObj.put("port", 2); // 변경필요
		popinstructions.put(0, popinstructionsObj);
		JSONArray popdeferred = new JSONArray();
		JSONObject poptreatment = new JSONObject();
		poptreatment.put("instructions", popinstructions);
		poptreatment.put("deferred", popdeferred);
		jsobjpop.put("treatment", poptreatment);

		JSONArray popcriteria = new JSONArray();

		JSONObject popcriteria1 = new JSONObject();
		popcriteria1.put("type", "ETH_DST");
		popcriteria1.put("mac", "4E:1F:61:B1:2E:25");
		popcriteria.put(popcriteria1);
		//
		JSONObject popcriteria2 = new JSONObject();
		popcriteria2.put("type", "ETH_SRC");
		popcriteria2.put("mac", "52:91:81:26:2A:01"); // 변경필요
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
			post(jsobjpop);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 

	//action : post   to Onos 
	public static void post(JSONObject json) throws Exception {

		HttpClient client = new DefaultHttpClient();
		
		String onosURL = null;
		
		String URL = "http://192.168.1.114:8181/onos/v1/flows/of%3A0000000000000002?appId=org.onosproject.testfwd";

		HttpPost post = new HttpPost(URL);
		String encoding = DatatypeConverter.printBase64Binary("karaf:karaf".getBytes("UTF-8"));
		post.setHeader("Authorization", "Basic " + encoding);
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Connection", "keep-alive");
		
		try {

			StringEntity s = new StringEntity(json.toString(), "utf-8");
			
			post.setEntity(s);
			// end request
			startTime = System.currentTimeMillis();

			HttpResponse httpResponse3 = client.execute(post);

			StatusLine statusLine = httpResponse3.getStatusLine();

			int responseCode = statusLine.getStatusCode();

			System.out.println(responseCode);

			if (responseCode == 201) {

				count = count + 1;
				System.out.println("<<MirLab Benchmark>> Number of flows added   count :  " + count);

			}
		} catch (Exception e) {
			System.out.println("Request exception");
			throw new RuntimeException(e);
		}


	}

}
