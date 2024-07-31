package com.tool.rental.beans;

public class Tool {
    private int id;
    private String toolCode;
    private int toolTypeId;
    private String brand;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public int getToolTypeId() {
        return toolTypeId;
    }

    public void setToolTypeId(int toolTypeId) {
        this.toolTypeId = toolTypeId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
