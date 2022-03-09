package nilex;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NilexData {
	
	//private static String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjYyQ0EwQjA5NURGN0Y2QkQyMTUyODVCQzFGNkVENjY2OTcxODA4M0MiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJZc29MQ1YzMzlyMGhVb1c4SDI3V1pwY1lDRHcifQ.eyJuYmYiOjE2NDY2MzYwNTMsImV4cCI6MTY0NjYzNzI1MywiaXNzIjoiaHR0cHM6Ly90ZXN0bnNwYXNjLmhlcnZhci5zZSIsImF1ZCI6Imh0dHBzOi8vdGVzdG5zcGFzYy5oZXJ2YXIuc2UvcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoibnNwLXdlYi1hcGkiLCJzdWIiOiIzMDY1IiwiYXV0aF90aW1lIjoxNjQ2NjMyNDUzLCJpZHAiOiJsb2NhbCIsInVzZXJfbmFtZSI6Im1laHJkYWQuYmFkZWllQGhlcnJsanVuZ2Euc2UiLCJ1c2VyX3R5cGVfaWQiOiIxIiwidXNlcl90eXBlX25hbWUiOiJBZ2VudCIsImdpdmVuX25hbWUiOiJNZWhyZGFkIiwiZmFtaWx5X25hbWUiOiJCYWRlaWUiLCJsYW5ndWFnZV9pZCI6IjEiLCJsYW5ndWFnZV9jb2RlIjoic3YtU0UiLCJpc19zdXBlcl9hZG1pbiI6IkZhbHNlIiwibGljZW5zZV9leHBpcnlfZGF0ZSI6IiMyMTIwLTA5LTI5VDA5OjUzOjAwWiIsInVzZXJfZnVsbF9uYW1lX2Zvcm1hdCI6IltbRmlyc3ROYW1lXV0gW1tMYXN0TmFtZV1dIiwic2Vzc2lvbl90aW1lb3V0IjoiNTc2MCIsInJvbGUiOlsiMyIsIjYiLCIxNiJdLCJlbWFpbCI6Im1laHJkYWQuYmFkZWllQGhlcnJsanVuZ2Euc2UiLCJncm91cF9tZW1iZXJzaGlwIjpbIjEiLCIxMSIsIjE5Il0sInVzZXJfYWNjZXNzX3ZlY3RvciI6IntcIlZlY3RvclwiOltcIlUyOTA2XCIsXCJPNVwiLFwiRzFcIixcIkcxNFwiLFwiRzRcIixcIlIxXCIsXCJSMlwiLFwiUjIwXCIsXCJSM1wiLFwiUjEwXCIsXCJSNFwiLFwiUjhcIixcIlUqXCIsXCJPKlwiLFwiRypcIixcIlIqXCIsXCJFXCJdLFwiVXNlclNjb3BlVmVjdG9yQ29tcG9uZW50c1wiOltcIlUyOTA2XCIsXCJPNVwiLFwiRzFcIixcIkcxNFwiLFwiRzRcIixcIlUqXCIsXCJPKlwiLFwiRypcIixcIkVcIl0sXCJSb2xlVmVjdG9yQ29tcG9uZW50c1wiOltcIlIxXCIsXCJSMlwiLFwiUjIwXCIsXCJSM1wiLFwiUjEwXCIsXCJSNFwiLFwiUjhcIixcIlIqXCIsXCJFXCJdfSIsImFjX3JvbGUiOlsiOCIsIjEwIiwiMjAiLCI0IiwiMSIsIjMiLCIyIl0sImFjX3VzZXJfc2NvcGUiOlsiNSIsIjEiLCIxNCIsIjQiXSwiYWNfdXNlcl9pZCI6IjI5MDYiLCJpc19wb3dlcl91c2VyIjoiRmFsc2UiLCJzY29wZSI6WyJuc3BXZWJBcGkiXSwiYW1yIjpbInB3ZCJdfQ.J_83lZOzYK01kqa-NMp_4av5gW7UJSX6Pn_ry6a5ERMskOP973UU9ZMBIdctdIPFXWhAoq02Pth2wSYKRSinxdiBFqMLOEKKK0FBu9oUsRGRSAFMCUp0s4S84P70-xeeg9pCmfvgPaOT9pXJ0iuAvcH5yUFkizxCHJIM7N6EUd5ERz15b7f8-u8DgvIM-_dUCdaNhp_Tyeufb_VQemfrVWOv3K1Cn01_08Ve9teCtc7gVuiqmehby0TpGjI6YrVekd2f0D-5W-tqW6-a9_BDs6ieK0YHDLxTOHaWjFeR6oO5RrbDCUVOqwO5kYAZ368gmDvIJZ2E6umX8-7nUMO-EQ";
	
	private String token; 
	
	public NilexData(String email, String password) throws IOException, InterruptedException {
		this.token = generateAccessToken(email, password);;
	}
	
	private static String generateAccessToken(String email, String password) throws IOException, InterruptedException {
	
		String url_auth = "http://10.142.11.54:1900/api/logon/TakeAuthenticationToken?email=hervar.se%5C" + email + "&password=" + password;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url_auth))
				.POST(HttpRequest.BodyPublishers.noBody())
				.build();

		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

		String responseBody = response.body().toString();
		String token = responseBody.substring(20);
		token = token.substring(0, token.length() - 141);

		
		return token;
	}
	
	public HttpResponse retrieveEntityById(Integer id) throws IOException, InterruptedException {

		Map<Object, Object> values = new HashMap<Object, Object>();
		values.put("EntityType", "Articles");
		values.put("Id", id);

		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = objectMapper.writeValueAsString(values);

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().header("accept", "application/json")
				.header("Authorization", "Bearer " + this.token)
				.uri(URI.create("http://10.142.11.54:1900/api/PublicApi/getentitybyid"))
				.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response;
	}

	//Try not to get too many entities because this method is just calling /api/publicapi/saveentity many times
	public List<Object> retrieveManyEntities(int startId, int endId) throws IOException, InterruptedException {
		List<Object> retrievedEntityList  = new ArrayList<Object>();

		for (int id = startId; id <= endId; id++) {
			// If the entity with the given id exists
			if (retrieveEntityById(id).statusCode() == 200) {
				retrievedEntityList.add(retrieveEntityById(id).body());
			
			}

		}

		return retrievedEntityList;
	}
	
	public HttpResponse postEntity(JSONObject article) throws IOException, InterruptedException {
		
        String requestBody = article.toString();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
				.header("Authorization", "Bearer " + this.token)
                .uri(URI.create("http://10.142.11.54:1900/api/PublicApi/saveentity"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        
        return response;
	}
	
	public HttpResponse softDeleteArticleById(int id) throws IOException, InterruptedException {
		JSONObject json = new JSONObject();
		json.put("EntityType","Articles");
		json.put("EntityTypeId", 2);
		json.put("Id", id);
		json.put("Status", -1);
		
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
				.header("Authorization", "Bearer " + this.token)
                .uri(URI.create("http://10.142.11.54:1900/api/PublicApi/saveentity"))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        
        return response;
	}

	//Specify startId and endId where you want to delete
	//This will return a list of entity ids that are deleted 
	public List<Integer> softDeleteMany(int startId, int endId) throws IOException, InterruptedException {
		
		List<Integer> deletedIdList = new ArrayList<Integer>();
		
		for(int id = startId; id <= endId; id++) {
			//If the entity with the given id exists
			if(retrieveEntityById(id).statusCode() == 200) {
				softDeleteArticleById(id);
				deletedIdList.add(id);
			
			}
			
		}
	
		return deletedIdList;
	}
}
