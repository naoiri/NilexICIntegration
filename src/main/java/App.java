import java.io.IOException;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import infocaption.ICData;
import nilex.NilexData;

public class App {

	public static void main(String[] args) throws IOException, InterruptedException {

		System.out.println("Integration in progress...2");
	
		//Below is the code for imigrating(Posting) articles.
		/*
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		ICData icData = new ICData();
		String icdToken = icData.generateAccessToken("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icData.getGuides(icdToken);
		for (JSONObject jo : icData.convertResponseToJson(guides)) {
			System.out.println(nd.postEntity(jo));
		}*/
		
		//Below is the code just for checking the articles. No imigration
		ICData icData = new ICData();
		String icdToken = icData.generateAccessToken("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icData.getGuides(icdToken);
		
		System.out.println(icData.convertResponseToJson(guides).size());
		icData.showCounts();
		
	}
}
