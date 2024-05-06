package com.example.giaapp;

public class ProfileProperty {
    public ProfileProperty (String propertyName, String propertyValue){
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }
    private String propertyName;
    private String propertyValue;

    public String getPropertyName() {
        return propertyName;
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
