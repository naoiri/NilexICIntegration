package nilex;

public class ArticleModel{
    public static class Data{

        public String referenceNo;

        public String getReferenceNo() {
            return referenceNo;
        }

    }

    public static class Root{
        public Data data;
        public Object errors;
        public boolean isError;

        public Data getData() {
            return data;
        }

    }
}

