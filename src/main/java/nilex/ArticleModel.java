package nilex;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ArticleModel{
    public static class Data{
        @JsonProperty("Version")
        public double version;
        @JsonProperty("VersionAsString")
        public String versionAsString;
        @JsonProperty("PreviousVersionId")
        public Object previousVersionId;
        @JsonProperty("Title")
        public String title;
        @JsonProperty("LanguageId")
        public int languageId;
        @JsonProperty("ReferenceNo")
        public String referenceNo;
        @JsonProperty("GroupIds")
        public String groupIds;
        @JsonProperty("KbCategoryId")
        public int kbCategoryId;
        @JsonProperty("ArticleStatusId")
        public int articleStatusId;
        @JsonProperty("PublishingScopeId")
        public int publishingScopeId;
        @JsonProperty("AuthorId")
        public int authorId;
        @JsonProperty("Keywords")
        public String keywords;
        @JsonProperty("AllowComments")
        public boolean allowComments;
        @JsonProperty("AllowFeedback")
        public boolean allowFeedback;
        @JsonProperty("EntityTypeId")
        public int entityTypeId;
        @JsonProperty("PersonPermissions")
        public Object personPermissions;
        @JsonProperty("InsertUserId")
        public int insertUserId;
        @JsonProperty("InsertTime")
        public Date insertTime;
        @JsonProperty("InsertIpAddress")
        public String insertIpAddress;
        @JsonProperty("LastUpdateUserId")
        public int lastUpdateUserId;
        @JsonProperty("LastUpdateTime")
        public Date lastUpdateTime;
        @JsonProperty("LastUpdateIpAddress")
        public String lastUpdateIpAddress;
        @JsonProperty("Status")
        public int status;
        @JsonProperty("Id")
        public int id;

        public double getVersion() {
            return version;
        }

        public void setVersion(double version) {
            this.version = version;
        }

        public String getVersionAsString() {
            return versionAsString;
        }

        public void setVersionAsString(String versionAsString) {
            this.versionAsString = versionAsString;
        }

        public Object getPreviousVersionId() {
            return previousVersionId;
        }

        public void setPreviousVersionId(Object previousVersionId) {
            this.previousVersionId = previousVersionId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getLanguageId() {
            return languageId;
        }

        public void setLanguageId(int languageId) {
            this.languageId = languageId;
        }

        public String getReferenceNo() {
            return referenceNo;
        }

        public void setReferenceNo(String referenceNo) {
            this.referenceNo = referenceNo;
        }

        public String getGroupIds() {
            return groupIds;
        }

        public void setGroupIds(String groupIds) {
            this.groupIds = groupIds;
        }

        public int getKbCategoryId() {
            return kbCategoryId;
        }

        public void setKbCategoryId(int kbCategoryId) {
            this.kbCategoryId = kbCategoryId;
        }

        public int getArticleStatusId() {
            return articleStatusId;
        }

        public void setArticleStatusId(int articleStatusId) {
            this.articleStatusId = articleStatusId;
        }

        public int getPublishingScopeId() {
            return publishingScopeId;
        }

        public void setPublishingScopeId(int publishingScopeId) {
            this.publishingScopeId = publishingScopeId;
        }

        public int getAuthorId() {
            return authorId;
        }

        public void setAuthorId(int authorId) {
            this.authorId = authorId;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public boolean isAllowComments() {
            return allowComments;
        }

        public void setAllowComments(boolean allowComments) {
            this.allowComments = allowComments;
        }

        public boolean isAllowFeedback() {
            return allowFeedback;
        }

        public void setAllowFeedback(boolean allowFeedback) {
            this.allowFeedback = allowFeedback;
        }

        public int getEntityTypeId() {
            return entityTypeId;
        }

        public void setEntityTypeId(int entityTypeId) {
            this.entityTypeId = entityTypeId;
        }

        public Object getPersonPermissions() {
            return personPermissions;
        }

        public void setPersonPermissions(Object personPermissions) {
            this.personPermissions = personPermissions;
        }

        public int getInsertUserId() {
            return insertUserId;
        }

        public void setInsertUserId(int insertUserId) {
            this.insertUserId = insertUserId;
        }

        public Date getInsertTime() {
            return insertTime;
        }

        public void setInsertTime(Date insertTime) {
            this.insertTime = insertTime;
        }

        public String getInsertIpAddress() {
            return insertIpAddress;
        }

        public void setInsertIpAddress(String insertIpAddress) {
            this.insertIpAddress = insertIpAddress;
        }

        public int getLastUpdateUserId() {
            return lastUpdateUserId;
        }

        public void setLastUpdateUserId(int lastUpdateUserId) {
            this.lastUpdateUserId = lastUpdateUserId;
        }

        public Date getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(Date lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public String getLastUpdateIpAddress() {
            return lastUpdateIpAddress;
        }

        public void setLastUpdateIpAddress(String lastUpdateIpAddress) {
            this.lastUpdateIpAddress = lastUpdateIpAddress;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Root{
        @JsonProperty("Data")
        public Data data;
        @JsonProperty("Errors")
        public Object errors;
        @JsonProperty("IsError")
        public boolean isError;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public Object getErrors() {
            return errors;
        }

        public void setErrors(Object errors) {
            this.errors = errors;
        }

        public boolean isError() {
            return isError;
        }

        public void setError(boolean error) {
            isError = error;
        }
    }
}

