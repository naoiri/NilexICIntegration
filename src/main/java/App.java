import java.io.IOException;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import infocaption.ICData;
import nilex.NilexData;

public class App {

	public static void main(String[] args) throws IOException, InterruptedException {

		System.out.println("Integration in progress...");
	
		// InfoCaption Test
		/*
		 * ICData icd = new ICData(); String icdToken =
		 * icd.generateAccessToken("naoyaTest", "naoyaTest"); HttpResponse<String>
		 * guides = icd.getGuides(icdToken);
		 * 
		 * System.out.println(icd.convertResponseToJson(guides).size());
		 * 
		 * for(JSONObject jo: icd.convertResponseToJson(guides)) {
		 * System.out.println(jo); }
		 */

		// Integration test

		/*
		ICData icd = new ICData();
		String icdToken = icd.generateAccessToken("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icd.getGuides(icdToken);
		System.out.println(icd.convertResponseToJson(guides).size());
		*/
		
		//NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		/*
		for(int i: nd.softDeleteMany(1238, 1244)) {
			System.out.println(i + "deleted!");
		}*/
		//System.out.println(nd.retrieveEntityById(1237));		
		//System.out.println(nd.softDeleteArticleById(1238));
		
		/*
		for (JSONObject jo : icd.convertResponseToJson(guides)) {
			System.out.println(nd.postEntity(nilexToken, jo));
		}*/
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		ICData icData = new ICData();
		String icdToken = icData.generateAccessToken("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icData.getGuides(icdToken);
		for (JSONObject jo : icData.convertResponseToJson(guides)) {
			System.out.println(nd.postEntity(jo));
		}
		
	}
}
