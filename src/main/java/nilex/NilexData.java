package nilex;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NilexData {

    private final String token;

    public NilexData(String email, String password) throws IOException, InterruptedException {
        this.token = generateAccessToken(email, password);

    }

    private static String generateAccessToken(String email, String password) throws IOException, InterruptedException {

        String url_auth = "http://10.142.11.54:1900/api/logon/TakeAuthenticationToken?email=hervar.se%5C" + email
                + "&password=" + password;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url_auth))
                .POST(HttpRequest.BodyPublishers.noBody()).build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        String responseBody = response.body();
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

    //Retrieve the ReferenceNo from nilex
    public String retrieveReferenceNo(Integer id) throws IOException, InterruptedException {

        HttpResponse<String> response = retrieveEntityById(id);
        ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ArticleModel.Root articleModel = om.readValue(response.body(), ArticleModel.Root.class);

        return articleModel.getData().getReferenceNo();
    }

    // Try not to get too many entities because this method is just calling
    // /api/publicapi/saveentity many times
    public List<Object> retrieveManyEntities(int startId, int endId) throws IOException, InterruptedException {
        List<Object> retrievedEntityList = new ArrayList<Object>();

        for (int id = startId; id <= endId; id++) {
            // If the entity with the given id exists
            if (retrieveEntityById(id).statusCode() == 200) {
                retrievedEntityList.add(retrieveEntityById(id).body());

            }

        }

        return retrievedEntityList;
    }


    public List<String> retrieveManyReferenceNos(int startId, int endId) throws IOException, InterruptedException {

        List<String> retrievedEntityList = new ArrayList<String>();

        for (int id = startId; id <= endId; id++) {
            //If the id returns statuscode 404 then it has hit the end of the list of articles and and the loop stops
            if (retrieveEntityById(id).statusCode() == 404){
                break;
                // If the entity with the given id exists
            } else if(retrieveEntityById(id).statusCode() == 200) {
                //Count out ReferenceNos which start "KB"(because these are created on GUI manually)
                if (!retrieveReferenceNo(id).contains("KB")) {
                    retrievedEntityList.add(retrieveReferenceNo(id));
                    System.out.println(id);
                }
            }
        }
        return retrievedEntityList;
    }

    public HttpResponse postEntity(JSONObject article) throws IOException, InterruptedException {

        String requestBody = article.toString();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().header("Authorization", "Bearer " + this.token)
                .uri(URI.create("http://10.142.11.54:1900/api/PublicApi/saveentity"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse softDeleteArticleById(int id) throws IOException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("EntityType", "Articles");
        json.put("EntityTypeId", 2);
        json.put("Id", id);
        json.put("Status", -1);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().header("Authorization", "Bearer " + this.token)
                .uri(URI.create("http://10.142.11.54:1900/api/PublicApi/saveentity"))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString())).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    // Specify startId and endId where you want to delete
    // This will return a list of entity ids that are deleted
    public List<Integer> softDeleteMany(int startId, int endId) throws IOException, InterruptedException {

        List<Integer> deletedIdList = new ArrayList<Integer>();

        for (int id = startId; id <= endId; id++) {
            // If the entity with the given id exists
            if (retrieveEntityById(id).statusCode() == 200) {
                softDeleteArticleById(id);
                deletedIdList.add(id);
                System.out.println(id);
            }

        }

        return deletedIdList;
    }


}
