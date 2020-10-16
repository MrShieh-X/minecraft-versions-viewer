package com.mrshiehx.minecraft_versions_viewer;

public class VersionItem {
    private String number;
    private String versionName;
    private String releaseDate;

    public VersionItem(String number, String versionName, String releaseDate) {
        this.number = number;
        this.versionName = versionName;
        this.releaseDate = releaseDate;
    }


    public String getNumber() {
        return number;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
