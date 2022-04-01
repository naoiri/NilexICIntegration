package infocaption;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.*;

public class ICData {

	private static final String URL_GUIDES = "https://hervar.infocaption.com/API/public/guides?hitsPerPage=600";

	private static final String URL_AUTH = "https://hervar.infocaption.com/oauth2/token";

	private final String token;
	// LinkedHashMap is better for categorising because it keeps the order when
	// elements are put in.
	// Simple HashMap does not work that way
	private final Map<Integer, String> categories = new LinkedHashMap();

	{

		categories.put(11, "övrigt");
		// value "Övrigt" is not good because if the title or summary contains the word
		// "övrigt"
		// it would assign wrong.
		// It is not a problem with current articles, but it may cause a problem
		// with future articles.

		categories.put(4, "dator");
		categories.put(5, "skrivare");
		categories.put(6, "nätverk");
		categories.put(14, "ipad");
		categories.put(15, "mobiltelefon");

		categories.put(16, "IAG");
		categories.put(17, "trio");
		categories.put(18, "vpn");

		categories.put(20, "nilex");

		categories.put(33, "windows");
		categories.put(21, "office"); // Parent category for kbCategoryNo 22-31
		categories.put(22, "excel");
		categories.put(23, "powerpoint");
		categories.put(24, "word");
		categories.put(25, "outlook");
		categories.put(26, "team"); // Value should be "teams" but this messes with categorize method. Do not touch
		categories.put(27, "oneDrive");
		categories.put(28, "yammer");
		categories.put(29, "delve");
		categories.put(30, "sharepoint");
		categories.put(31, "planner");

	}

	public ICData(String id, String secret) throws IOException, InterruptedException {
		this.token = generateAccessToken(id, secret);

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

	public Map<Integer, String> getCategories() {
		return this.categories;
	}

	/**
	 * Checks the sentence from API to assign a category number
	 *
	 * @param name    result.getName() from the API
	 * @param summary result.getSummary() from the API
	 * @return a category number which associates with the category keyword
	 *         <p>
	 *         This is private because this is only used in convertResponseToJson()
	 */

	public int categorize(String name, String summary) {

		int kbCategoryId = 0;
		String nameAndSummary = name + summary;
		nameAndSummary = nameAndSummary.toLowerCase();

		boolean isOvrigt = true;
		boolean containsAnyOfficeChildren = false;

		// To go through the categories with index. This will be used in the for-loop
		List<Integer> keys = new ArrayList<Integer>(this.categories.keySet());

		List<String> officeWords = new ArrayList<String>() {
			{
				add(categories.get(21)); // Office
				add(categories.get(22)); // Excel
				add(categories.get(23)); // PowerPoint
				add(categories.get(24)); // Word
				add(categories.get(25)); // Outlook
				add(categories.get(26)); // Team
				add(categories.get(27)); // OneDrive
				add(categories.get(28)); // Yammer
				add(categories.get(29)); // Delve
				add(categories.get(30)); // Sharepoint
				add(categories.get(31)); // Planner

			}
		};

		// 1. Check first if there is any category word. If no, it would be "Övrigt" and
		// categorising done.
		for (int i = 0; i < keys.size(); i++) {
			if (nameAndSummary.contains(categories.get(keys.get(i)))) {
				isOvrigt = false; // If any word is found, it would not be Övrigt.
				break; // No more checks in this for-loop
			}

		}

		if (isOvrigt) {

			kbCategoryId = 11;
			return kbCategoryId; // Categorizing done! No more further checks. End of the whole method

		} else { // 2. Check if there is any officeChildren word.

			for (int i = 0; i < officeWords.size(); i++) {
				if (nameAndSummary.contains(officeWords.get(i))) {
					containsAnyOfficeChildren = true;
					break; // No more checks in this for-loop
				}
			}

		}

		// 3. Logic for Office related words
		// If any office related word is found:
		if (containsAnyOfficeChildren) {
			List<String> foundWords = new ArrayList<>(); //List of words that only consists of officeWords

			//Create 
			for (String officeWord : officeWords) {
				if (nameAndSummary.contains(officeWord)) {
					foundWords.add(officeWord);
				}
			}

			if (foundWords.size() == 1) { // If there is just one word, it is the category word
				kbCategoryId = getKbIntegersByWord(keys, foundWords.get(0));
				return kbCategoryId;

			} else if (foundWords.size() >= 3) { // If three or more, it would automatically be "Office" t.ex "word",
													// "excel", "powerpoint"
				kbCategoryId = 21;
				return kbCategoryId;
			}

			else { // If two office words

				if (!foundWords.contains("office")) { // If the word "Office" not found. t.ex "word" and "Excel"
					kbCategoryId = 21;
					return kbCategoryId;

				} else if (foundWords.indexOf("office") == 0) { // If the word "office" is the first in foundWords. t.ex
																// "office" and "word"

					kbCategoryId = getKbIntegersByWord(keys, foundWords.get(1));
					return kbCategoryId;
				} else if (foundWords.indexOf("office") == 1) { // If the word "office" is the second in foundWords t.ex
																// "word" and "office"
					kbCategoryId = getKbIntegersByWord(keys, foundWords.get(0));
					return kbCategoryId;
				}

			}
		}

		else

		{ // 4. Logic for other words.

			return 200;

		}

		return kbCategoryId;

	}

	private int getKbIntegersByWord(List<Integer> keys, String word) {
		int kbInteger = 0; // I don't want to initialize with 0, but this is just to avoid compile error
		String categoryValue;
		for (int i = 11; i < keys.size(); i++) { // start with 11("Excel")
			categoryValue = this.categories.get(keys.get(i));
			if (word.equals(categoryValue)) {
				kbInteger = keys.get(i);
			}
		}
		return kbInteger;
	}

	public List<JSONObject> convertResponseToJson(HttpResponse<String> response) throws JsonProcessingException {

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
			createJSON(guideList, result, json);

		}

		return guideList;
	}

	private void createJSON(List<JSONObject> guideList, GuideModel.Result result, JSONObject json) {
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
		innerObject.put("Answer",
				"<a href=" + result.getFullURL() + " target=\"_blank\" >Tryck här för att komma till guiden</a>");
		json.put("DynamicProperties", innerObject);

		guideList.add(json);
	}

	public List<Integer> collectIds(HttpResponse<String> response) throws JsonProcessingException {
		List<Integer> ids = new ArrayList<Integer>();

		ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GuideModel.Root root = om.readValue(response.body(), GuideModel.Root.class);

		for (GuideModel.Result result : root.getResults()) {
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

	// (Naoyas anteckning)This goes through all the artcles first. Maybe
	// ineffective.(Better by adding the "latest(last element in list?)")
	// collect only newly added articles by id
	public List<JSONObject> convertOnlyNewGuideToJson(HttpResponse<String> response, List<Integer> icList)
			throws JsonProcessingException {

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
					createJSON(newGuides, result, json);

				}
			}
		}

		return newGuides;

	}

}
