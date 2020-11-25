package com.upgrade.qa.test.util;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.HashMap;

public class HttpClient {

	private String baseUrl;
	private CloseableHttpClient httpClient;
	
	public HttpClient(String baseUrl) {
		this.baseUrl = baseUrl;
		httpClient = HttpClients.createDefault();
	}

	public CloseableHttpResponse postRequest(String endPoint, HashMap<String, String> headers, String payload)
			throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(this.baseUrl + endPoint);
		for (String key : headers.keySet()) {
			post.addHeader(key, headers.get(key));
		}
		post.setEntity(new StringEntity(payload));

		CloseableHttpResponse response = httpClient.execute(post);
		return response;

	}
}
