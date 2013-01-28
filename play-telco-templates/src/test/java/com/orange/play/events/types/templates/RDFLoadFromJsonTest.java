package com.orange.play.events.types.templates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import com.google.gson.JsonParser;
import com.orange.play.events.types.templates.rdf.UcTelcoEsrRecom;

public class RDFLoadFromJsonTest {

	/*
	public static void main(String[] args) throws IOException {		
		new RDFLoadFromJsonTest();
	}

	public RDFLoadFromJsonTest() throws IOException {
		performTest();
	}
	*/
	
	@Test
	public void performTest() throws IOException {
		String jsonString = readFileAsString(RDFLoadFromJsonTest.class.getClassLoader().getResourceAsStream("RecomJsonExample1.js")); 
		System.out.println(jsonString);
		
		UcTelcoEsrRecom recom = new UcTelcoEsrRecom();
		recom.loadFromJSON(new JsonParser().parse(jsonString).getAsJsonObject());
		System.out.println(recom.toString());
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
