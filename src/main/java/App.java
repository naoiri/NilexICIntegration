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

		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		// A good state for testing data in nilex = ID 3580 - 4148
		//At the moment this is a problem.
		//endId is set to a value that probably never is going to be reached.
		//this is a temporary solution until we get retrieveAllReferenceNo() method working.
		List<String> nilexList  = nd.retrieveManyReferenceNos(3580, 10000);

		List<Integer> nilexListInt = new ArrayList<Integer>();
		for (String s: nilexList) {
			nilexListInt.add(Integer.parseInt(s));
		}

		Collections.sort(nilexListInt);

		ICData icData = new ICData("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icData.getGuides();
		List<Integer> IcList = icData.collectIds(guides);

		Collections.sort(IcList);

		if (nilexListInt.equals(IcList)){
			System.out.println("No update has been detected");
		} else {
			IcList.removeAll(nilexListInt);
			List<JSONObject> newGuides = icData.convertOnlyNewGuideToJson(guides, IcList);
			for (JSONObject jo : newGuides) {
				System.out.println(nd.postEntity(jo));
			}
		}

		/*
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		ICData icData = new ICData("naoyaTest", "naoyaTest");

		HttpResponse<String> guides = icData.getGuides();
		List<JSONObject> json = icData.convertResponseToJson(guides);
		for (JSONObject jo: json) {
			System.out.println(nd.postEntity(jo));
		}
		*/
	}
	
}
