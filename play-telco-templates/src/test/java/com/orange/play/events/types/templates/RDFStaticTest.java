package com.orange.play.events.types.templates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.junit.Test;

public class RDFStaticTest {	
	
	/*
	public static void main(String[] args) throws URISyntaxException, IOException {		
		new RDFStaticTest();
	}

	public RDFStaticTest() throws URISyntaxException, IOException {
		performTests();
	}
	*/
	
	@Test
	public void performTests() throws URISyntaxException, IOException {
		String httpBody = readFileAsString(RDFStaticTest.class.getClassLoader().getResourceAsStream("RolandExample1.xml")); 
		System.out.println(httpBody);
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("s-proxy", 3128));
		try {
			HttpPost httpPost = new HttpPost(DSBSender.ORANGE_ANTONIO_DSB_URI);    
			httpPost.setHeader(HTTP.CONTENT_TYPE, "text/xml; charset=utf-8");
			StringEntity stringEntity = new StringEntity(httpBody);
			httpPost.setEntity(stringEntity); 
			HttpResponse httpResponse = httpClient.execute(httpPost);
			System.out.println(httpResponse.toString());
			httpResponse.getEntity().writeTo(System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String readFileAsString(InputStream inputStream) throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
