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

	private static final String URL_GUIDES = "https://hervar.infocaption.com/API/public/guides?hitsPerPage=1";
	private static final String URL_AUTH = "https://hervar.infocaption.com/oauth2/token";

	private String token;

	public ICData(String id, String secret) throws IOException, InterruptedException {
		this.token = generateAccessToken(id, secret);
	}

	private Map<Integer, String> categories = new HashMap<Integer, String>() {
		{
			put(11, "Övrigt");
			// value "Övrigt" is not good because if the title contains the word "övrigt"
			// it would allocate wrong.
			// It is not a problem with current articles, but it may cause a problem
			// with future articles.

			put(4, "Dator");
			put(5, "Skrivare");
			put(6, "Nätverk");
			put(14, "iPad");
			put(15, "Mobiltelefon");

			put(12, "Teams");
			put(13, "Outlook");
			put(16, "IAG");
			put(17, "Trio");
			put(18, "VPN");
		}
	};

	/**
	 * Checks the sentence from API to allocate a category number
	 * @param name result.getName() from the API
	 * @param summary result.getSummary() from the API
	 *
	 * @return a category number which associates with the category keyword
	 *
	 * This is private because this is only used in convertResponseToJson()
	 */

	private int categorize(String name, String summary) {

		// To go through the categories with index. This will be used in the for-loop
		List<Integer> keys = new ArrayList<Integer>(this.categories.keySet());

		int kbCategoryId = 11;
		// Initialize with 11(as "Övrigt").
		// If no other category words are found it ends up as "Övrigt"

		String oneSentence = name + summary;

		// Loop through the categories(words)
		// Allocates categoryId depending on what word is in the result.getName()
		// and result.getSummary()
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
