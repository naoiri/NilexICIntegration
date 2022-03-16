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
		
		/*

		// A good state for testing data in nilex = ID 1864 - 2432
		NilexData nd = new NilexData("naoya.irikura@herrljunga.se", "Praktik2022");
		List<String> nilexList  = nd.retrieveManyReferenceNos(1864, 2432);
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
			System.out.println(IcList);
		}
		
		*/
		
		ICData icData = new ICData("naoyaTest", "naoyaTest");
		HttpResponse<String> guides = icData.getGuides();
		
		//jUST FOR TEST
		int newID631 = 631;
		//int newID633 = IcList.get(1);
		
		System.out.println("This is end of generating id for the new articles.");
		
		List<JSONObject> newGuides = icData.convertOnlyNewGuideToJson(guides, 631); 
		
		System.out.println(newGuides.get(0));
		

	}
	
}
