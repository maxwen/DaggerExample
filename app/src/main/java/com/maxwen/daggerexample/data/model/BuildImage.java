package com.maxwen.daggerexample.data.model;

public class BuildImage {
    private String mFilename;
    private Long mTimestamp;

    public BuildImage(String filename, Long timestamp) {
        this.mFilename = filename;
        this.mTimestamp = timestamp;
    }

    public String getFilename() {
        return mFilename;
    }

    public void setFilename(String mFilename) {
        this.mFilename = mFilename;
    }

    public Long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Long mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    @Override
    public String toString() {
        return "filename: " + mFilename + " timestamp: " + mTimestamp;
    }
}
