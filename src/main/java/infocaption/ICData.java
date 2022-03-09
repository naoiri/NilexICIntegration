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

public class ICData {

	private static final String URL_GUIDES = "https://hervar.infocaption.com/API/public/guides?hitsPerPage=600";
	private static final String URL_AUTH = "https://hervar.infocaption.com/oauth2/token";

	private Map<Integer, String> categories = new HashMap<Integer, String>() {
		{
			put(11, "Övrigt"); 
			// value "Övrigt" is not good because if the title contains the word "övrigt" it would allocate wrong. null would cause NullPointerException

			put(4, "Dator");
			put(5, "Skrivare");
			put(6, "Nätverk");
			put(14, "iPad");
			put(15, "Mobiltelefon");

			put(12, "Teams");
			put(13, "Outlook");
			put(16, "IAG");
			put(17, "Trio");
		}
	};

	// This method checks the sentence from "result.getName() and result.getSummary()" if the sentence has
	// one of the category keywords
	// Inparameter: result.getName(), result.getSummary()
	// Returns a kbCategoryId number which associates with the category keyword
	// This method is private because this is only used in convertResponseToJson()
	private Integer categorize(String name, String summary) {

		// To go through the categories with index. This will be used in the for-loop
		List<Integer> keys = new ArrayList<Integer>(this.categories.keySet());

		int kbCategoryId = 11; 
		//Initialize with 11(as "Övrigt"). 
		//If no other category words are found it ends up as "Övrigt"
	
		String oneSentence = name + summary;

		// Loop through the categories(words)
		// Allocates categoryId depending on what word is in the result.getName()
		for (int i = 0; i < keys.size(); i++) {

			String currentSearchWord = categories.get(keys.get(i));

			// When searching IAG, only CAPITAL letters check(To avoid ex. "diagram")
			if (currentSearchWord.equals("IAG")) {
				if (oneSentence.indexOf(currentSearchWord) != -1) {
					kbCategoryId = keys.get(i); // Allocates "1", "12", "13"....
				}
			} else {
				// If "name" has one of the category words(t.ex "Teams", "Outlook")
				if (oneSentence.indexOf(currentSearchWord) != -1
						|| oneSentence.indexOf(currentSearchWord.toLowerCase()) != -1) {
					kbCategoryId = keys.get(i); // Allocates "1", "12", "13"....

				}
			}

		}

		return kbCategoryId;
	}


	public List<JSONObject> convertResponseToJson(HttpResponse<String> response)
			throws JsonMappingException, JsonProcessingException {

		// This will be returned at the end.
		List<JSONObject> guideList = new ArrayList<JSONObject>();

		// deserialize JSON repsone to Modelclass
		ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GuideModel.Root root = om.readValue(response.body(), GuideModel.Root.class);

		// Convert to JSONObject
		// JSONObject jsonObject = new JSONObject(root);

		// The creation of JSON to post to Nilex
		for (GuideModel.Result result : root.getResults()) {
			JSONObject json = new JSONObject();


			// generates kbCategoryId depending on "result.getName()" and "result.getSummary()"
			Integer kbCategoryId = categorize(result.getName(), result.getSummary());

			json.put("EntityType", "Articles");
			json.put("ArticleStatusId", 14);
			json.put("PublishingScopeId", 2);
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
