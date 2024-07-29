package com.example.test.MenuPages;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Database.AppDatabase;
import com.example.test.R;
import com.example.test.Tabels.Category;
import com.example.test.Tabels.Item;

import java.util.ArrayList;
import java.util.List;

public class DataDisplay extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private AppDatabase db;
    private Spinner spin;
    private List<Category> categories;
    private List<String> categoryDescList;
    private List<String> itemDescList;
    private List<Integer> itemQtyList;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        // Initialize database
        db = AppDatabase.getDatabase(this);

        // Initialize views
        spin = findViewById(R.id.categoriesDesc);
        spin.setOnItemSelectedListener(this);

        // Fetch category descriptions from database
        loadCategoryDescriptions();

        // Setup spinner adapter
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryDescList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinAdapter);


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.itemDescQt);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemDescList = new ArrayList<>();
        itemQtyList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemDescList, itemQtyList);
        recyclerView.setAdapter(itemAdapter);
    }

    private void loadCategoryDescriptions() {
        categories = db.categoryDao().getAllCategories();
        categoryDescList = new ArrayList<>();
        categoryDescList.add(getString(R.string.selectCategory));

        for (Category category : categories) {
            categoryDescList.add(category.getCategoryDesc());
        }
    }

    private void loadItemData(int categoryId) {
        List<Item> allItems = db.itemDao().getAllItems();

        // Clear existing lists
        itemDescList.clear();
        itemQtyList.clear();

        for(Item item : allItems){
            if(item.getCategoryId() == categoryId){
                itemDescList.add(item.getItemDesc());
                itemQtyList.add(item.getQty());
            }
        }
        // Notify the adapter that the data has changed
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            // Clear the RecyclerView when "Select a Category" is selected
            itemDescList.clear();
            itemQtyList.clear();
            itemAdapter.notifyDataSetChanged();
        } else {
            Category selectedCategory = categories.get(position - 1); // Adjust for "Select a Category" option
            int selectedCategoryId = selectedCategory.getCategoryId();

            // Get all items for selected category
            loadItemData(selectedCategoryId);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
