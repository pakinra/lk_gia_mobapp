package com.example.giaapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResult {
    @SerializedName("apiKey")
    @Expose
    private String apiKey;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("shortName")
    @Expose
    private String shortName;
    @SerializedName("userCode")
    @Expose
    private int userCode;

    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getUserCode() { return userCode; }
    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }
}
