package infocaption;

import java.util.ArrayList;

public class GuideModel {

    public static class Result {

        private String summary;
        private String fullURL;
        private String name;
        private int id;

        public String getSummary() {
            return summary;
        }

        public String getFullURL() {
            return fullURL;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return this.id;
        }

    }

    public static class Root {
        private ArrayList<Result> results;

        public ArrayList<Result> getResults() {
            return results;
        }
    }

}
