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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ICData {

	private static final String URL_GUIDES = "https://hervar.infocaption.com/API/public/guides?hitsPerPage=600";

	private static final String URL_AUTH = "https://hervar.infocaption.com/oauth2/token";

	private String token;

	public ICData(String id, String secret) throws IOException, InterruptedException {
		this.token = generateAccessToken(id, secret);
		
	}
	
	//LinkedHashMap is better for categorising because it keeps the order when elements are put in. 
	//Simple HashMap does not work that way
	private Map<Integer, String> categories = new LinkedHashMap();
	{

		categories.put(11, "Övrigt");
		// value "Övrigt" is not good because if the title or summary contains the word "övrigt"
		// it would assign wrong.
		// It is not a problem with current articles, but it may cause a problem
		// with future articles.

		categories.put(4, "Dator");
		categories.put(5, "Skrivare");
		categories.put(6, "Nätverk");
		categories.put(14, "iPad");
		categories.put(15, "Mobiltelefon");

		categories.put(16, "IAG");
		categories.put(17, "Trio");
		categories.put(18, "VPN");
		
		
		categories.put(20, "Nilex");
		categories.put(21, "Office"); //Parent category for kbCategoryNo 22-31
		categories.put(22, "Excel");
		categories.put(23, "PowerPoint");
		categories.put(24, "Word");
		categories.put(25, "Outlook");
		categories.put(26, "Teams");
		categories.put(27, "OneDrive");
		categories.put(28, "Yammer");
		categories.put(29, "Delve");
		categories.put(30, "Sharepoint");
		categories.put(31, "Planner");
		
		categories.put(33, "Windows");

	}

	
	
	public Map<Integer, String> getCategories() {
		return this.categories;
	}
	
	/**
	 * Checks the sentence from API to assign a category number
	 * @param name result.getName() from the API
	 * @param summary result.getSummary() from the API
	 *
	 * @return a category number which associates with the category keyword
	 *
	 * This is private because this is only used in convertResponseToJson()
	 */

	public int categorize(String name, String summary) {

		// To go through the categories with index. This will be used in the for-loop
		List<Integer> keys = new ArrayList<Integer>(this.categories.keySet());
		
		List<String> officeChildren = new ArrayList<String>() {
			{
				add(categories.get(22)); //Excel
				add(categories.get(23)); //PowerPoint
				add(categories.get(24)); //Word
				add(categories.get(25)); //Outlook
				add(categories.get(26)); //Teams
				add(categories.get(27)); //OneDrive
				add(categories.get(28)); //Yammer
				add(categories.get(29)); //Delve
				add(categories.get(30)); //Sharepoint
				add(categories.get(31)); //Planner
				
			}
		};
		int kbCategoryId = 11;
		// Initialize with 11(as "Övrigt").
		// If no other category words are found it ends up as "Övrigt"

		String oneSentence = name + summary;

		// Loop through the categories(words)
		// assigns kbCategoryId depending on what word is in the result.getName()
		// and result.getSummary()
		for (int i = 0; i < keys.size(); i++) {

			String currentSearchWord = categories.get(keys.get(i));
			
			switch(currentSearchWord) {
			
				case "IAG":
					if (oneSentence.indexOf(currentSearchWord) != -1) {
						kbCategoryId = keys.get(i); // assigns "1", "12", "13"....
						break;  //Behövs den här breaken?
	
					}
					break;
				
				case "Office":
					
					String firstFoundWord = null; 
					String secondFoundWord = null;
					
					
					//Get this with contains() 
					for(int index = 0; index < officeChildren.size(); index++) {
						
						//if name and summary has one of the officeChildren words
						if(oneSentence.indexOf(officeChildren.get(index)) != -1) {
							
							if(firstFoundWord == null) {
								firstFoundWord = officeChildren.get(index);
							} else {
								secondFoundWord = officeChildren.get(index);
							}
							
						}
					}
					
					//If it's only "Office" which is found 
					//OR both first and second are filled with any officeChildren word 
					if((firstFoundWord == null && secondFoundWord == null) || (firstFoundWord != null && secondFoundWord != null)) {
						kbCategoryId = 11; 
					} else if(firstFoundWord != null && secondFoundWord == null) {
						
						//Logic for searching kbCategoryNo from categories by just a word(value, it means "firstFoundWord" here) complete this on Tuesday 
						kbCategoryId = getKbIntegersByWord(keys, firstFoundWord);
					} 
					
				
				default:
					// If "name" or "summary" has one of the category words(t.ex "Teams", "Outlook")
					if (oneSentence.indexOf(currentSearchWord) != -1
							|| oneSentence.indexOf(currentSearchWord.toLowerCase()) != -1) {
						kbCategoryId = keys.get(i); // assigns "1", "12", "13"....
					}
					break;
			}
			
			
			
			
			/*

			// When searching IAG, only CAPITAL letters check(To avoid ex. "diagram")
			if (currentSearchWord.equals("IAG")) {
				if (oneSentence.indexOf(currentSearchWord) != -1) {
					kbCategoryId = keys.get(i); // assigns "1", "12", "13"....
					break;

				}
			} else {
				// If "name" or "summary" has one of the category words(t.ex "Teams", "Outlook")
				if (oneSentence.indexOf(currentSearchWord) != -1
						|| oneSentence.indexOf(currentSearchWord.toLowerCase()) != -1) {
					kbCategoryId = keys.get(i); // assigns "1", "12", "13"....
					break;

				}
			}*/

		}

		return kbCategoryId;
	}
	
	private int getKbIntegersByWord(List<Integer> keys, String word){
		int kbInteger = 0; //I don't want to initialize with 0, but this is just to avoid compile error
		String categoryValue;
		for(int i = 11; i<keys.size(); i++) { //start with 11("Excel")
			categoryValue = this.categories.get(keys.get(i));
			if(word.equals(categoryValue)) {
				kbInteger = keys.get(i);
			}
		}
		return kbInteger;
	}

	public List<JSONObject> convertResponseToJson(HttpResponse<String> response)
			throws JsonMappingException, JsonProcessingException {

		// This will be returned at the end.
		List<JSONObject> guideList = new ArrayList<JSONObject>();

		// deserialize JSON repsone to Modelclass
		ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GuideModel.Root root = om.readValue(response.body(), GuideModel.Root.class);

		// The creation of JSON to post to Nilex
		for (GuideModel.Result result : root.getResults()) {
			JSONObject json = new JSONObject();

			// generates kbCategoryId depending on "result.getName()" and
			// "result.getSummary()"
			Integer kbCategoryId = categorize(result.getName(), result.getSummary());
			String getIdAsString = String.valueOf(result.getId());

			json.put("EntityType", "Articles");
			json.put("ArticleStatusId", 14);
			json.put("PublishingScopeId", 2);
			json.put("ReferenceNo", getIdAsString); //Link with infocaptions id(like "Foreign key")
			json.put("KbCategoryId", kbCategoryId);
			json.put("EntityTypeId", 2);
			json.put("Title", result.getName());
			json.put("AuthorId", 3064);
			JSONObject innerObject = new JSONObject();
			innerObject.put("Question", result.getSummary());
			innerObject.put("Answer",
					"<a href=" + result.getFullURL() + " target=\"_blank\" >Tryck här för att komma till guiden</a>");
			json.put("DynamicProperties", innerObject);

			guideList.add(json);

		}

		return guideList;
	}
	
	public List<Integer> collectIds(HttpResponse<String> response) throws JsonMappingException, JsonProcessingException{
		List<Integer> ids = new ArrayList<Integer>();
		
		ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GuideModel.Root root = om.readValue(response.body(), GuideModel.Root.class);

		for(GuideModel.Result result: root.getResults()) {
			ids.add(result.getId());
		}
		
		return ids;
		
	}
	
	public HttpResponse<String> getGuides() throws IOException, InterruptedException {

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().GET().header("accept", "application/json")
				.header("Authorization", "Bearer " + this.token).uri(URI.create(URL_GUIDES)).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		
		return response;
	}

	//(Naoyas anteckning)This goes through all the artcles first. Maybe ineffective.(Better by adding the "latest(last element in list?)")
	// collect only newly added articles by id
	public List<JSONObject> convertOnlyNewGuideToJson(HttpResponse<String> response, List<Integer> icList)
			throws JsonMappingException, JsonProcessingException {

		List<JSONObject> newGuides = new ArrayList<JSONObject>();

		// deserialize JSON repsone to Modelclass
		ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GuideModel.Root root = om.readValue(response.body(), GuideModel.Root.class);

		// The creation of JSON to post to Nilex
		for (GuideModel.Result result : root.getResults()) {
			JSONObject json = new JSONObject();

			// Only the new guide
			for (int newGuideId : icList) {
				if (result.getId() == newGuideId) {
					Integer kbCategoryId = categorize(result.getName(), result.getSummary());
					String getIdAsString = String.valueOf(result.getId());

					json.put("EntityType", "Articles");
					json.put("ArticleStatusId", 14);
					json.put("PublishingScopeId", 2);
					json.put("ReferenceNo", getIdAsString); // Link with infocaptions id(like "Foreign key")
					json.put("KbCategoryId", kbCategoryId);
					json.put("EntityTypeId", 2);
					json.put("Title", result.getName());
					json.put("AuthorId", 3064);
					JSONObject innerObject = new JSONObject();
					innerObject.put("Question", result.getSummary());
					innerObject.put("Answer", "<a href=" + result.getFullURL()
							+ " target=\"_blank\" >Tryck här för att komma till guiden</a>");
					json.put("DynamicProperties", innerObject);

					newGuides.add(json);

				}
			}
		}

		return newGuides;

	}
	
	

	private static String generateAccessToken(String id, String secret) throws IOException, InterruptedException {

		String formatted = id + ":" + secret;
		String encoded = Base64.getEncoder().encodeToString((formatted).getBytes());

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL_AUTH))
				.headers("Accept", "application/json", "Content-Type", "application/x-www-form-urlencoded",
						"Authorization", "Basic " + encoded)
				.POST(BodyPublishers.ofString("grant_type=client_credentials")).build();

		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

		String responseBody = response.body();
		String token = responseBody.substring(17);
		token = token.substring(0, token.length() - 24);

		return token;
	}

}
