import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import infocaption.ICData;
import nilex.NilexData;
import org.json.JSONObject;

public class App {

	public static void main(String[] args) throws IOException, InterruptedException {

		System.out.println("Integration in progress...");
		/*ICData icData = new ICData("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icData.getGuides();
		List<JSONObject> jsonObjects = icData.convertResponseToJson(guides);
		System.out.println(jsonObjects.size());*/

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

		/*
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		nd.softDeleteMany(1800,1900);
		*/

		//NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		//System.out.println(nd.retrieveManyReferenceNos(1840,1863));

		// A good state for testing data in nilex = ID 1864 - 2432
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		List<String> nilexList  = nd.retrieveManyReferenceNos(1864, 2432);
		List<Integer> nilexListInt = new ArrayList<>();
		for (String s: nilexList) {
			nilexListInt.add(Integer.parseInt(s));
		}
		Collections.sort(nilexListInt);
		//System.out.println(nilexListInt);


		ICData icData = new ICData("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icData.getGuides();
		List<Integer> IcList = icData.collectIds(guides);
		Collections.sort(IcList);
		//System.out.println(IcList);

		if (nilexListInt.equals(IcList)){
			System.out.println("No update has been detected");
		} else {
			IcList.removeAll(nilexListInt);
			System.out.println(IcList);
		}

	}
}
