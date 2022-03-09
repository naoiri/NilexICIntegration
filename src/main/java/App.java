import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import org.json.JSONObject;

import infocaption.ICData;
import nilex.NilexData;

public class App {

	public static void main(String[] args) throws IOException, InterruptedException {

		System.out.println("Integration in progress...2");
	
		/*
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		ICData icData = new ICData();
		String icdToken = icData.generateAccessToken("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icData.getGuides(icdToken);
		
		List<JSONObject> jsonObjects = icData.convertResponseToJson(guides);
		System.out.println(jsonObjects.size());
		for (JSONObject jo : jsonObjects) {
			System.out.println(nd.postEntity(jo));
		}*/
		NilexData nData = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		
		for(Object jsonBody: nData.retrieveManyEntities(1, 10)) {
			System.out.println("");
			System.out.println(jsonBody);
		}
		
	}
}
