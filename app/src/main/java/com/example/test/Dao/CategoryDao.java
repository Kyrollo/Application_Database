package com.example.test.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.test.Tabels.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);
    
    @Query("SELECT * FROM category WHERE CategoryId = :id")
    Category getCategoryById(int id);

    @Query("SELECT * FROM category")
    List<Category> getAllCategories();

    @Query("SELECT category_desc FROM category")
    List<String> getAllCategoriesNames();

    @Query("SELECT * FROM category WHERE category_desc = :categoryDesc")
    Category getCategoryByDesc(String categoryDesc);

    @Query("DELETE FROM category")
    void deleteAll();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'category'")
    void resetPrimaryKey();
}