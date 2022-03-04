package infocaption;

import java.util.ArrayList;

public class GuideModel {

	public static class Result{

        public String summary;
        public String fullURL;
        public String name;

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

    }

    public static class Root{
        public ArrayList<Result> results;
        public ArrayList<Result> getResults() {
            return results;
        }
        public void setResults(ArrayList<Result> results) {
            this.results = results;
        }
    }

}
