import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import infocaption.ICData;
import nilex.NilexData;

public class App {

	public static void main(String[] args) throws IOException, InterruptedException {

		System.out.println("Integration in progress...");


		//NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		//System.out.println(nd.retrieveEntityById(1864).body());
		//System.out.println(nd.retrieveReferenceNo(2433));
		//System.out.println(nd.retrieveManyReferenceNos(1864, 2432));

		/*NilexData nd = new NilexData("aiste.pakstyte@herrljunga.se", "Praktik2022");
		List<String> refNumbers = nd.retrieveManyReferenceNos(1859, 1860);
		for (String refNumber : refNumbers) {
			System.out.println(refNumber);
		}*/

		/*
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		ICData icData = new ICData("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icData.getGuides();

		List<JSONObject> jsonObjects = icData.convertResponseToJson(guides);
		System.out.println(jsonObjects.size());
		for (JSONObject jo : jsonObjects) {
			System.out.println(nd.retrieveEntityById(2433).body());
		}*/

		/*
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		ICData icData = new ICData("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icData.getGuides();
		
		List<JSONObject> jsonObjects = icData.convertResponseToJson(guides);
		for (JSONObject jo : jsonObjects) {
			System.out.println(nd.postEntity(jo));
		}
		*/

		/*
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		nd.changeCategory(1758, 18);
		*/

		//NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		//nd.softDeleteMany(1800,1900);

		//NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		//System.out.println(nd.retrieveManyReferenceNos(1840,1863));

	}
}
