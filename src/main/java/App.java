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
		


		// A good state for testing data in nilex = ID 1864 - 2432
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		List<String> nilexList  = nd.retrieveManyReferenceNos(1864, 2432); 
		//At the moment this is a problem. 
		//endId for retriveManyReferenceNos() has to be changed every time there is an update on Nilex.
		//retrieveAllReferenceNos() would have solved it. 
		
		
		List<Integer> nilexListInt = new ArrayList<Integer>();
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
			List<JSONObject> newGuides = icData.convertOnlyNewGuideToJson(guides, IcList);
			for (JSONObject jo : newGuides) {
				System.out.println(jo);
			}
		}

	}
	
}
