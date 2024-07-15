package com.example.test.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.test.Tabels.Item;
import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);

    @Query("SELECT itemId, item_desc, qty, categoryId FROM item WHERE categoryId = :id")
    List<Item> getItemsById(int id);


    @Query("SELECT * FROM item")
    List<Item> getAllItems();

    @Query("DELETE FROM item")
    void deleteAll();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'item'")
    void resetPrimaryKey();
}