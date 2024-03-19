package com.mirlab.metric.northbound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class testMetric {

	private static final String USER_AGENT = "Mozilla/5.0";

	private static final String GET_URL = "http://localhost:9090/SpringMVCExample";

	private static final String POST_URL = "http://localhost:9090/SpringMVCExample/home";

	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);

		System.out.println(" input number , if you want to excute post , type 1");
		int testNum = in.nextInt();

		while (testNum == 1) {

			try {
				sendPOST();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private static void sendPOST() throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(POST_URL);
		httpPost.addHeader("User-Agent", USER_AGENT);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("userName", "Pankaj Kumar"));

		
		HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
		httpPost.setEntity(postParams);

		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

		System.out.println("POST Response Status:: " + httpResponse.getStatusLine().getStatusCode());

		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();

		// print result
		System.out.println(response.toString());
		httpClient.close();

	}

	public void whenPostJsonUsingHttpClient_thenCorrect() throws ClientProtocolException, IOException {
	CloseableHttpClient client = HttpClients.createDefault();
	HttpPost httpPost = new HttpPost("http://www.example.com");
	
	String json = "{"id":1,"name":"John"}";
	StringEntity entity = new StringEntity(json);
	httpPost.setEntity(entity);
	httpPost.setHeader("Accept", "application/json");
	httpPost.setHeader("Content-type", "application/json");
	CloseableHttpResponse response = client.execute(httpPost);
	assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
	client.close();
	}
}
