import java.io.IOException;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import infocaption.ICData;
import nilex.NilexData;


public class App {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		System.out.println("Integration in progress...");
		
		//Nilex Test
		NilexData nd = new NilexData();
		System.out.println(nd.generateAccessToken("naoya.irikura@herrljunga.se", "Praktik2022"));
		
		//InfoCaption Test
		/*
		ICData icd = new ICData();
		String icdToken = icd.generateAccessToken("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icd.getGuides(icdToken);
		
		for(String fullUrl: icd.getFullUrlList(guides)) {
			System.out.println(fullUrl);
		}*/
		
		//Integration test
		/*
		ICData icd = new ICData();
		String icdToken = icd.generateAccessToken("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icd.getGuides(icdToken);
		
		
		NilexData nd = new NilexData();
		for(JSONObject jo: icd.convertResponseToJson(guides)) {
			System.out.println(nd.postEntity(jo));
		};*/
		
		
	}
}
