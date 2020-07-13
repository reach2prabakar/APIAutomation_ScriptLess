package com.client.testscript;



import java.util.LinkedHashMap;

import org.json.JSONObject;
import org.testng.ITestNGMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.client.library.AutomationBuddy;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Unit test for simple Api.
 * @author Prabs
 */
public class ClientAPI{

	
	/**
	 * @throws Exception 
	 */
	@BeforeTest
	public void processTestdata() throws Exception{
	
	}
	
	
   /**
	 * @param key
	 * @param Value
	 * @throws Exception
	 */
	@Test(dataProvider="testScripts")
    public void restAPI(Object[] jsonObject,String resource) throws Exception{
		
		String bearer = AutomationBuddy.getPropValues("bearer");
		String uri = AutomationBuddy.getPropValues("uri")+resource.split(",")[1];
		String method= resource.split(",")[0].toUpperCase();
		String target = null;
		String jsonpath = null;
        String propertyname =null;
		int loopcount = 0;
		if(resource.split(",").length>4) {
			target = resource.split(",")[3];
			jsonpath = resource.split(",")[5];
			propertyname = resource.split(",")[4];
			loopcount = Integer.parseInt(resource.split(",")[6]);
		}else {
			loopcount = Integer.parseInt(resource.split(",")[3]);
		}
		String jsoninput = (String) jsonObject[0];
		if(jsoninput.isEmpty()) {
			uri = AutomationBuddy.assignproperty(uri,resource.split(",")[2],loopcount);
		}else {
			jsoninput = AutomationBuddy.assignproperty(jsoninput,resource.split(",")[2],loopcount);
		}
 		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> hmap = (LinkedHashMap<String, String>) jsonObject[1];
		OkHttpClient client = new OkHttpClient().newBuilder().build();
 		MediaType mediaType = MediaType.parse("application/json");
 		RequestBody body = RequestBody.create(mediaType, jsoninput);
 		Request request;
 		Response response = null;
 		if(method.equals("GET")) {
 			request = new Request.Builder()
 	 				  .url(uri+"?"+jsoninput)
 	 				  .method(method, null)
 	 				  .addHeader("Content-Type", "application/json")
 	 				  .addHeader("Authorization", bearer)
 	 				  .build();
 	 		response = client.newCall(request).execute();
 	 		String json = response.body().string();
 	 		JsonPath jp = new JsonPath(json);
 	 		int rescode = response.code();
 	 		if(rescode==200) {
 	 			System.out.println("Response code : "+ rescode);
 	 		}else {
 	 			System.out.println("Output :"+json);
 	 			throw new Exception("The server responded with error code :"+rescode);
 	 		}
 	 		if(hmap.size()>0) {
	 			AutomationBuddy.assertvalidation(json, hmap);
 	 		}
 	 		if(target!=null) {
 	 			AutomationBuddy.assignproperty(target,jp,loopcount, propertyname, jsonpath);
 	 		}
 		}
 		if(method.equals("POST")||method.equals("PUT")) {
 			request = new Request.Builder()
 	 				  .url(uri)
 	 				  .method(method, body)
 	 				  .addHeader("Content-Type", "application/json")
 	 				  .addHeader("Authorization", bearer)
 	 				  .build();
 	 		response = client.newCall(request).execute();
 	 		String json = response.body().string();
 	 		JsonPath jp = new JsonPath(json);
 	 		System.out.println(jp.getString("id"));
 	 		if(jsonpath!=null) {
 	 			if(jsonpath.equalsIgnoreCase("Response")){
 	 	 			jsonpath = json;
 	 	 		  }else {
 	 	 			jp = new JsonPath(json);
 	 	 		  }
 	 		}
 	 		int rescode = response.code();
 	 		if(rescode==200) {
 	 			System.out.println("Response code : "+ rescode);
 	 		}else {
 	 			throw new Exception("The server responded with error code :"+rescode);
 	 		}
 	 		if(hmap.size()>0) {
	 			AutomationBuddy.assertvalidation(json, hmap);
 	 		}
 	 		if(target!=null) {
 	 			AutomationBuddy.assignproperty(target,jp,loopcount, propertyname, jsonpath);
 	 		}
 		}
 	}

	
	
	
	/**
	 * @return 
	 * @throws Exception
	 */
	@DataProvider
	public Object[][] testScripts(ITestNGMethod test) throws Exception {

		return AutomationBuddy.dataprovider(test);
		
	}
}
