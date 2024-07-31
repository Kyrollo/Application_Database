package com.example.test.ApiClasses;

public class ItemResponse {
    private int ItemID;
    private String ItemDesc;
    private String CategoryID;

    public ItemResponse(int itemID, String itemDesc, String categoryID) {
        ItemID = itemID;
        ItemDesc = itemDesc;
        CategoryID = categoryID;
    }

    // Getters and Setters
    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }
}