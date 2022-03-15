package nilex;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleModel{
    public static class Data{

    	@JsonProperty("ReferenceNo")
        public String referenceNo;

        public String getReferenceNo() {
            return referenceNo;
        }

    }

    public static class Root{
    	@JsonProperty("Data")
        public Data data;
    	
        public Object errors;
        public boolean isError;

        public Data getData() {
            return data;
        }

    }
}