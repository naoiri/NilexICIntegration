package infocaption;

import java.util.ArrayList;

public class GuideModel {

	public static class Result{

        private String summary;
        private String fullURL;
        private String name;
        private int id;

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getFullURL() {
            return fullURL;
        }

        public void setFullURL(String fullURL) {
            this.fullURL = fullURL;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        public int getId() {
        	return this.id;
        }
        
        public void setId(int id) {
        	this.id = id;
        }

    }

    public static class Root{
        private ArrayList<Result> results;
        public ArrayList<Result> getResults() {
            return results;
        }
        public void setResults(ArrayList<Result> results) {
            this.results = results;
        }
    }

}
