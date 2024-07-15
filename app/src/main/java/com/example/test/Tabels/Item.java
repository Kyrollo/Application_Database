package com.example.test.Tabels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int itemId;

    @ColumnInfo(name = "item_desc")
    public String itemDesc;

    @ColumnInfo(name = "qty")
    public int qty;

    @ColumnInfo(name = "categoryId")
    public int categoryId;

    // Constructor
    public Item() {
        this.itemDesc = itemDesc;
        this.qty = qty;
        this.categoryId = categoryId;
    }

    // Getters and setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
