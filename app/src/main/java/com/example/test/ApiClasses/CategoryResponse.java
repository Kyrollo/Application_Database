package com.example.test.ApiClasses;

public class CategoryResponse {
    private String CategoryID;

    public CategoryResponse(String categoryID) {
        CategoryID = categoryID;
    }

    // Getters and Setters

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }
}
