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
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nilex.NilexData;

public class ICData {

	private static final String URL_GUIDES = "https://hervar.infocaption.com/API/public/guides?hitsPerPage=600";
	private static final String URL_AUTH = "https://hervar.infocaption.com/oauth2/token";
	private GuideModel guideModel;

	public ICData() {
		this.guideModel = new GuideModel();
	}

	public GuideModel getGuideModel() {
		return this.guideModel;
	}

	public List<JSONObject> convertResponseToJson(HttpResponse<String> response)
			throws JsonMappingException, JsonProcessingException {

		List<JSONObject> guideList = new ArrayList<JSONObject>();

		// deserialize JSON repsone to Modelclass
		ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GuideModel.Root root = om.readValue(response.body(), GuideModel.Root.class);

		// Convert to JSONObject
		// JSONObject jsonObject = new JSONObject(root);

		// Beautify the JSONobject to JSON tree structure
		for (GuideModel.Result result : root.getResults()) {
			JSONObject json = new JSONObject();
			json.put("EntityType", "Articles");
			json.put("Title", result.getSummary());
			json.put("EntityTypeId", 7);
			json.put("ArticleStatusId", 14);
			json.put("PublishingScopeId", 2);
			json.put("AuthorId", 3065); // Kommentera in den för att det ska vara synlig på testnspssp
			json.put("Keywords", result.getName());
			json.put("KbCategoryId", 7);

			JSONObject innerObject = new JSONObject();
			innerObject.put("Article Description",
					"<a href="+result.getFullURL()+" target=\"_blank\" >Tryck här för att komma till guiden</a>");
			json.put("DynamicProperties", innerObject);
			guideList.add(json);
		}

		return guideList;
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
