package nilex;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NilexData {
	
	private static String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjYyQ0EwQjA5NURGN0Y2QkQyMTUyODVCQzFGNkVENjY2OTcxODA4M0MiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJZc29MQ1YzMzlyMGhVb1c4SDI3V1pwY1lDRHcifQ.eyJuYmYiOjE2NDYzOTg3MTgsImV4cCI6MTY0NjM5OTkxOCwiaXNzIjoiaHR0cHM6Ly90ZXN0bnNwYXNjLmhlcnZhci5zZSIsImF1ZCI6Imh0dHBzOi8vdGVzdG5zcGFzYy5oZXJ2YXIuc2UvcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoibnNwLXdlYi1hcGkiLCJzdWIiOiIzMDY0IiwiYXV0aF90aW1lIjoxNjQ2Mzk1MTE4LCJpZHAiOiJsb2NhbCIsInVzZXJfbmFtZSI6Im5hb3lhLmlyaWt1cmFAaGVycmxqdW5nYS5zZSIsInVzZXJfdHlwZV9pZCI6IjEiLCJ1c2VyX3R5cGVfbmFtZSI6IkFnZW50IiwiZ2l2ZW5fbmFtZSI6Ik5hb3lhIiwiZmFtaWx5X25hbWUiOiJJcmlrdXJhIiwibGFuZ3VhZ2VfaWQiOiIxIiwibGFuZ3VhZ2VfY29kZSI6InN2LVNFIiwiaXNfc3VwZXJfYWRtaW4iOiJGYWxzZSIsImxpY2Vuc2VfZXhwaXJ5X2RhdGUiOiIjMjEyMC0wOS0yOVQwOTo1MzowMFoiLCJ1c2VyX2Z1bGxfbmFtZV9mb3JtYXQiOiJbW0ZpcnN0TmFtZV1dIFtbTGFzdE5hbWVdXSIsInNlc3Npb25fdGltZW91dCI6IjU3NjAiLCJyb2xlIjpbIjMiLCI2IiwiMTYiXSwiZW1haWwiOiJuYW95YS5pcmlrdXJhQGhlcnJsanVuZ2Euc2UiLCJncm91cF9tZW1iZXJzaGlwIjpbIjEiLCIxMSIsIjE5Il0sInVzZXJfYWNjZXNzX3ZlY3RvciI6IntcIlZlY3RvclwiOltcIlUyOTA1XCIsXCJPNVwiLFwiRzFcIixcIkcxNFwiLFwiRzRcIixcIlIxXCIsXCJSMlwiLFwiUjIwXCIsXCJSM1wiLFwiUjEwXCIsXCJSNFwiLFwiUjhcIixcIlUqXCIsXCJPKlwiLFwiRypcIixcIlIqXCIsXCJFXCJdLFwiVXNlclNjb3BlVmVjdG9yQ29tcG9uZW50c1wiOltcIlUyOTA1XCIsXCJPNVwiLFwiRzFcIixcIkcxNFwiLFwiRzRcIixcIlUqXCIsXCJPKlwiLFwiRypcIixcIkVcIl0sXCJSb2xlVmVjdG9yQ29tcG9uZW50c1wiOltcIlIxXCIsXCJSMlwiLFwiUjIwXCIsXCJSM1wiLFwiUjEwXCIsXCJSNFwiLFwiUjhcIixcIlIqXCIsXCJFXCJdfSIsImFjX3JvbGUiOlsiOCIsIjEwIiwiMjAiLCI0IiwiMSIsIjMiLCIyIl0sImFjX3VzZXJfc2NvcGUiOlsiNSIsIjEiLCIxNCIsIjQiXSwiYWNfdXNlcl9pZCI6IjI5MDUiLCJpc19wb3dlcl91c2VyIjoiRmFsc2UiLCJzY29wZSI6WyJuc3BXZWJBcGkiXSwiYW1yIjpbInB3ZCJdfQ.SXgRgxh_oeFxrBSvB4QFnU2MI3ea1y7goH9foO1q0D2kttfauhWCqhygG7c2wrT6RDcycle7f8NDL0i_31h2t1zmYIjW6quXhAJNxT_Cm64b8kvaSlX7Vttqplnv4TCxRPwWs8WrNe8-qANpmAwYPYjnXbvTHABsauUTH1Hsbcc1WydlDK02mK2DeYNo6kOZ9lRdMbgUUFq62HqhOsjJxbZt3cFjaPGLiR0wmC4rbViHkTg4xDpO-jFEE9rspd_vuOcl7E0maXvw8ywd8uj6l84hbAdzrjgRasIYZ5nZMlyvD0-5iLZemwUbYKqVWg7LKe0eT24ZvRCUXuWma2VJ8Q";
	public HttpResponse retrieveEntityById(String id) throws IOException, InterruptedException {

		Map<String, String> values = new HashMap<String, String>();
		values.put("EntityType", "Articles");
		values.put("Id", id);

		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = objectMapper.writeValueAsString(values);

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().header("accept", "application/json")
				.header("Authorization", "Bearer " + token)
				.uri(URI.create("http://10.142.11.54:1900/api/PublicApi/getentitybyid"))
				.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response;
	}
	
	public HttpResponse postEntity(JSONObject article) throws IOException, InterruptedException {
		
        String requestBody = article.toString();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
				.header("Authorization", "Bearer " + token)
                .uri(URI.create("http://10.142.11.54:1900/api/PublicApi/saveentity"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        
        return response;
	}
}
