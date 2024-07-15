package com.example.test.MenuPages;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.example.test.Database.AppDatabase;
import com.example.test.R;
import com.example.test.Tabels.Category;
import com.example.test.Tabels.Item;
import java.util.List;

public class DataEntry extends AppCompatActivity {
    private EditText editCategory, editItemDesc, editQty;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        // Initialize views
        editCategory = findViewById(R.id.editCategory);
        editItemDesc = findViewById(R.id.editItemDesc);
        editQty = findViewById(R.id.editQty);
        Button btnAddCategory = findViewById(R.id.btnCategory);
        Button btnAddItem = findViewById(R.id.btnItem);

        // Initialize database
        db = AppDatabase.getDatabase(this);

        // Set onClick listener for Add Category button
        btnAddCategory.setOnClickListener(view -> {
            String categoryDesc = editCategory.getText().toString().trim();

            // Ensure category description field is not empty
            if (categoryDesc.isEmpty()) {
                Toast.makeText(DataEntry.this, "Please enter a category name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create Category object
            Category category = new Category();
            category.setCategoryDesc(categoryDesc);

            // Insert category in database
            insertCategory(category, categoryDesc);

        });

        // Set onClick listener for Add Item button
        btnAddItem.setOnClickListener(view -> {
            String categoryDesc = editCategory.getText().toString().trim();
            String itemDesc = editItemDesc.getText().toString().trim();
            String qtyStr = editQty.getText().toString().trim();
            int qty;

            // Ensure category description field is not empty
            if (categoryDesc.isEmpty()) {
                Toast.makeText(DataEntry.this, "Please enter a category name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ensure item description field is not empty
            if (itemDesc.isEmpty()) {
                Toast.makeText(DataEntry.this, "Please enter an item description", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ensure quantity field is not empty and parse it
            if (qtyStr.matches("\\d+")) {
                qty = Integer.parseInt(qtyStr);
            } else {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isCategoryExist(categoryDesc) == 0) {
                Toast.makeText(DataEntry.this, "Please Click on Add Category Button", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create Item object
            Item item = new Item();
            item.setItemDesc(itemDesc);
            item.setQty(qty);

            // Insert category and item in separate threads
            insertItem(item, categoryDesc);
        });
    }

    private void insertCategory(Category category, String categoryDesc) {
        // Insert category if it doesn't exist
        if (isCategoryExist(categoryDesc) == 0) {
            db.categoryDao().insert(category);
            Toast.makeText(this, "Category Added", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Category is already exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertItem(Item item, String categoryDesc) {
        item.setCategoryId(getCategoryId(categoryDesc));
        db.itemDao().insert(item);
        Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();


        // Clear fields after insertion
        editCategory.setText("");
        editItemDesc.setText("");
        editQty.setText("");
    }

    private int isCategoryExist(String categoryDesc) {
        List<Category> categories = db.categoryDao().getAllCategories();
        for (Category category : categories) {
            if (categoryDesc.equals(category.getCategoryDesc())) {
                return 1;
            }
        }
        return 0;
    }

    private int getCategoryId(String categoryDesc) {
        List<Category> categories = db.categoryDao().getAllCategories();
        for (Category category : categories) {
            if (categoryDesc.equals(category.getCategoryDesc())) {
                return category.getCategoryId();
            }
        }
        return -1;
    }

}
