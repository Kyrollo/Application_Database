package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.List;
import com.example.test.Database.AppDatabase;
import com.example.test.MenuPages.DataDisplay;
import com.example.test.MenuPages.DataEntry;
import com.example.test.Tabels.Category;
import com.example.test.Tabels.Item;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private AppDatabase db;
    private static final int PICK_EXCEL_FILE = 1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Retrieve the username from the intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        // Find the TextView and set the username
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderTitle = headerView.findViewById(R.id.nav_header_title);
        navHeaderTitle.setText(username);

        Button btnDeleteAll = findViewById(R.id.btnDeleteAll);
        Button btnExport = findViewById(R.id.btnExport);

        db = AppDatabase.getDatabase(this);

        btnDeleteAll.setOnClickListener(view -> showDeleteDialog());

        btnExport.setOnClickListener(view -> createExcelFiles());
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Data")
                .setMessage("Are you sure you want to delete all data?")
                .setPositiveButton("YES", (dialog, which) -> {
                    deleteAllData();
                })
                .setNegativeButton("NO", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void deleteAllData() {
        db.categoryDao().deleteAll();
        db.itemDao().deleteAll();
        db.categoryDao().resetPrimaryKey();
        db.itemDao().resetPrimaryKey();
        Toast.makeText(HomeActivity.this, "All data deleted", Toast.LENGTH_SHORT).show();
    }

    public void createExcelFiles() {
        requestPermission();

        List<Category> categories = db.categoryDao().getAllCategories();

        for (Category category : categories) {
            List<Item> items = db.itemDao().getItemsById(category.getCategoryId());
            String fileName = category.getCategoryDesc() + ".xlsx";
            File file = new File(Environment.getExternalStorageDirectory(), fileName);

            try {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet(category.getCategoryDesc());

                XSSFRow headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Item ID");
                headerRow.createCell(1).setCellValue("Description");
                headerRow.createCell(2).setCellValue("Quantity");

                int rowNum = 1;
                for (Item item : items) {
                    XSSFRow row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(item.getItemId());
                    row.createCell(1).setCellValue(item.getItemDesc());
                    row.createCell(2).setCellValue(item.getQty());
                }

                FileOutputStream outputStream = new FileOutputStream(file);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();

                Toast.makeText(HomeActivity.this, "Excel file for " + category.getCategoryDesc() + " created successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(HomeActivity.this, "Failed to create Excel file for " + category.getCategoryDesc(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Uri uri = Uri.parse("package:com.example.test");
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                    startActivity(intent);
                    Toast.makeText(HomeActivity.this, "Permission accepted", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void importExcelFile(Uri fileUri) {
        try {
            FileInputStream inputStream = (FileInputStream) getContentResolver().openInputStream(fileUri);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null){
                    String itemDesc = row.getCell(0).getStringCellValue();
                    int qty = (int) row.getCell(1).getNumericCellValue();
                    String categoryDesc = row.getCell(2).getStringCellValue();
                    if (categoryDesc == null || categoryDesc.trim().isEmpty()) {
                        break;
                    }
                    else{
                        // Skip creating an item if the category description is empty or null
                        Category category = db.categoryDao().getCategoryByDesc(categoryDesc);

                        // Check if category already exists
                        if (category == null) {
                            // Insert new category
                            Category newCategory = new Category();
                            newCategory.setCategoryDesc(categoryDesc);
                            db.categoryDao().insert(newCategory);
                            category = db.categoryDao().getCategoryByDesc(categoryDesc);

                        }
                        // Insert item's attributes
                        Item item = new Item();
                        item.setItemDesc(itemDesc);
                        item.setQty(qty);
                        item.setCategoryId(category.getCategoryId());
                        db.itemDao().insert(item);
                    }
                }
                else{
                    // Skip creating loop if row is empty
                    break;
                }
            }
            workbook.close();
            inputStream.close();

            Toast.makeText(HomeActivity.this, "Excel file imported successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(HomeActivity.this, "Failed to import Excel file", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        startActivityForResult(intent, PICK_EXCEL_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_EXCEL_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                importExcelFile(fileUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PackageManager.PERMISSION_GRANTED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createExcelFiles();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_dataEntry) {
            Intent intent = new Intent(HomeActivity.this, DataEntry.class);
            startActivity(intent);
        } else if (id == R.id.action_dataDisplay) {
            Intent intent = new Intent(HomeActivity.this, DataDisplay.class);
            startActivity(intent);
        } else if (id == R.id.action_import_excel) {
            openFilePicker();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
