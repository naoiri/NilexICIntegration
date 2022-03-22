package nilex;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleModel {
    public static class Data {

        @JsonProperty("ReferenceNo")
        private String referenceNo;

        public String getReferenceNo() {
            return referenceNo;
        }

    }

    public static class Root {
        @JsonProperty("Data")
        private Data data;

        private Object errors;
        private boolean isError;

        public Data getData() {
            return data;
        }

    }
}