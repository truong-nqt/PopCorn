package com.truongnqt.popcorn.network.request;

import com.google.gson.annotations.SerializedName;

public class MovieDiscoverRequest {
    @SerializedName("api_key")
    private String apiKey;

    @SerializedName("language")
    private String language;

    @SerializedName("region")
    private String region;

    @SerializedName("sort_by")
    private String sortBy;

    @SerializedName("certification_country")
    private String certificationCountry;

    @SerializedName("certification")
    private String certification;

    @SerializedName("include_adult")
    private boolean includeAdult;

    @SerializedName("include_video")
    private boolean includeVideo;

    @SerializedName("page")
    private int page;

    @SerializedName("year")
    private int year;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getCertificationCountry() {
        return certificationCountry;
    }

    public void setCertificationCountry(String certificationCountry) {
        this.certificationCountry = certificationCountry;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public boolean isIncludeAdult() {
        return includeAdult;
    }

    public void setIncludeAdult(boolean includeAdult) {
        this.includeAdult = includeAdult;
    }

    public boolean isIncludeVideo() {
        return includeVideo;
    }

    public void setIncludeVideo(boolean includeVideo) {
        this.includeVideo = includeVideo;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
