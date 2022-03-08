package infocaption;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nilex.NilexData;

public class ICData {

	private static final String URL_GUIDES = "https://hervar.infocaption.com/API/public/guides?hitsPerPage=600";
	private static final String URL_AUTH = "https://hervar.infocaption.com/oauth2/token";
	
	private Map<Integer, String> categories = new HashMap<Integer, String>(){
		{
			put(1, "default"); //"kbcategoryId 1" means "No category" which appears only on the parent category "Kunskapsbank"  
			put(12, "Teams");
			put(13, "Outlook");
			//Just for now, must be added 
		}
	};

	
	public List<JSONObject> convertResponseToJson(HttpResponse<String> response)
			throws JsonMappingException, JsonProcessingException {

		//This will be returned at the end.
		List<JSONObject> guideList = new ArrayList<JSONObject>();
		
		// deserialize JSON repsone to Modelclass
		ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GuideModel.Root root = om.readValue(response.body(), GuideModel.Root.class);

		// The creation of JSON to post to Nilex
		for (GuideModel.Result result : root.getResults()) {
			JSONObject json = new JSONObject();
			
			
		//Categorising filter starts here
			
			String name = result.getName(); 
			
			Integer kbCategoryId = categorize(name);
			
			if(name.indexOf(categories.get(0)) != -1) {
				json.put("ArticleStatusId", 14);
				json.put("PublishingScopeId", 2);
				json.put("KbCategoryId", kbCategoryId);
				json.put("EntityTypeId", 2);
				json.put("Title", result.getName());
				json.put("AuthorId", 3064);
				JSONObject innerObject = new JSONObject();
				innerObject.put("Question", result.getSummary());
				innerObject.put("Answer", "<a href="+result.getFullURL()+" target=\"_blank\" >Tryck här för att komma till guiden</a>");
				json.put("DynamicProperties", innerObject);
				
				guideList.add(json);
			} 
		}

		return guideList;
	}

	//This method checks the sentence from "result.getName()" if the sentence has one of the category keywords
	//Inparameter: result.getName()
	//Returns a kbCategoryId number which associates with the category keyword
	//This mthod is private because this is only used in convertResponseToJson method in the same class 
	private Integer categorize(String name) {
		
		//To go through the categories with index. This will be used in the for-loop
		List<Integer> keys = new ArrayList<Integer>(this.categories.keySet());
		
		int kbCategoryId = 1; //Initialize with 1. If no category words are found it will remain 1 which make the category keyword to "default"(comes on "Kunskapsbank" on webapplication)
				
		//Loop through the categories(words)
		//Allocates categoryId depending on what word is in the result.getName()
		for(int i = 0; i<keys.size(); i++) {
		
			String currentSearchWord = categories.get(keys.get(i));
			//System.out.println("Current Search Word:" + currentSearchWord + keys.get(i));
			
			//If "name" has one of the category words(t.ex "Teams", "Outlook")
			if(name.indexOf(currentSearchWord) != -1) {
				kbCategoryId = keys.get(i); //Allocates 1, 12, 13....
				
			}
			
		}
		
		return kbCategoryId;
	}
	// ggit et HttpResponse returned.
	public HttpResponse getGuides(String token) throws IOException, InterruptedException {

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().GET().header("accept", "application/json")
				.header("Authorization", "Bearer " + token).uri(URI.create(URL_GUIDES)).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response;
	}

	public String generateAccessToken(String id, String secret) throws IOException, InterruptedException {

		String formatted = id + ":" + secret;
		String encoded = Base64.getEncoder().encodeToString((formatted).getBytes());

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL_AUTH))
				.headers("Accept", "application/json", "Content-Type", "application/x-www-form-urlencoded",
						"Authorization", "Basic " + encoded)
				.POST(BodyPublishers.ofString("grant_type=client_credentials")).build();

		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

		String responseBody = response.body().toString();
		String token = responseBody.substring(17);
		token = token.substring(0, token.length() - 24);

		return token;
	}

}
