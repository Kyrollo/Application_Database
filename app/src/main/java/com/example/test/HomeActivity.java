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
import android.widget.FrameLayout;
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

import com.example.test.API.ApiService;
import com.example.test.ApiClasses.CategoryResponse;
import com.example.test.ApiClasses.ItemResponse;
import com.example.test.Scan.ScanAndCount;
import com.google.android.material.navigation.NavigationView;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.List;
import java.util.Scanner;

import com.example.test.Database.AppDatabase;
import com.example.test.MenuPages.DataDisplay;
import com.example.test.MenuPages.DataEntry;
import com.example.test.Tabels.Category;
import com.example.test.Tabels.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PICK_EXCEL_FILE = 1;
    private DrawerLayout drawer;
    private AppDatabase db;
    private ApiService apiService;
    private FrameLayout progressBarContainer;
    private int apiCallCounter = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.77:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

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

        // Find the buttons
        Button btnDeleteAll = findViewById(R.id.btnDeleteAll);
        Button btnExport = findViewById(R.id.btnExport);
        Button btnDownload = findViewById(R.id.btnDownload);

        // Find the progress bar
        progressBarContainer = findViewById(R.id.progressBarContainer);

        // Initialize the database
        db = AppDatabase.getDatabase(this);

        // Set the onClickListeners for the buttons
        btnDeleteAll.setOnClickListener(view -> showDeleteDialog());

        btnExport.setOnClickListener(view -> createExcelFiles());

        btnDownload.setOnClickListener(view ->{
            progressBarContainer.setVisibility(View.VISIBLE);
            apiCallCounter = 0;
            fetchCategories();
            fetchItems();
        } );
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.deleteAllDialog))
                .setMessage(getString(R.string.confirmDeleteDialog))
                .setPositiveButton(getString(R.string.yesDeleteDialog), (dialog, which) -> {
                    deleteAllData();
                })
                .setNegativeButton(getString(R.string.noDeleteDialog), (dialog, which) -> {
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
        Toast.makeText(HomeActivity.this, getString(R.string.dataDeleted), Toast.LENGTH_SHORT).show();
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

                Toast.makeText(HomeActivity.this, getString(R.string.firstCreatedExcelFile) + category.getCategoryDesc() + getString(R.string.secondCreatedExcelFile), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(HomeActivity.this, getString(R.string.failedCreateExcelFile) + category.getCategoryDesc(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(HomeActivity.this, getString(R.string.permessionAccepted), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, getString(R.string.permessionDenied), Toast.LENGTH_SHORT).show();
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

            Toast.makeText(HomeActivity.this, getString(R.string.importSucceded), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(HomeActivity.this, getString(R.string.importSucceded), Toast.LENGTH_SHORT).show();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        startActivityForResult(intent, PICK_EXCEL_FILE);
    }

    private void fetchCategories() {
        apiService.getCategories().enqueue(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryResponse>> call, Response<List<CategoryResponse>> response) {
                List<CategoryResponse> categories = response.body();
                if (response.isSuccessful()) {
                    insertCategories(categories);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.getCatgoriesFailed), Toast.LENGTH_LONG).show();
                }
                checkApiCallsCompletion();
            }

            @Override
            public void onFailure(Call<List<CategoryResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.getCatgoriesFailed), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void insertCategories(List<CategoryResponse> categories) {
        int count = 0;
        for (CategoryResponse categoryResponse : categories) {
            if (count == 10) {
                break;
            }
            if (!isCategoryExist(categoryResponse.getCategoryID())){
                Category category = new Category();
                category.setCategoryDesc(categoryResponse.getCategoryID());
                db.categoryDao().insert(category);
            }
            count++;
        }
    }

    private boolean isCategoryExist(String categoryDesc) {
        Category category = db.categoryDao().getCategoryByDesc(categoryDesc);
        if (category == null) {
            return false;
        } else {
            return true;
        }
    }

    private void fetchItems() {
        apiService.getItems().enqueue(new Callback<List<ItemResponse>>() {
            @Override
            public void onResponse(Call<List<ItemResponse>> call, Response<List<ItemResponse>> response) {
                List<ItemResponse> items = response.body();
                if (response.isSuccessful()) {
                    insertItems(items);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.getItemsFailed), Toast.LENGTH_LONG).show();
                }
                checkApiCallsCompletion();
            }

            @Override
            public void onFailure(Call<List<ItemResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.getItemsFailed), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void insertItems(List<ItemResponse> items) {
        int count = 0;
        for (ItemResponse itemResponse : items) {
            if (count == 10) {
                break;
            }
            Item item = new Item();
            Category category = db.categoryDao().getCategoryByDesc(itemResponse.getCategoryID());
            item.setItemDesc(itemResponse.getItemDesc());
            item.setQty(itemResponse.getItemID());
            item.setCategoryId(category.getCategoryId());
            db.itemDao().insert(item);
            count++;
        }
    }

    private void checkApiCallsCompletion() {
        apiCallCounter++;
        if (apiCallCounter == 2) {
            progressBarContainer.setVisibility(View.GONE);
            Toast.makeText(HomeActivity.this, getString(R.string.loadingComplete), Toast.LENGTH_SHORT).show();
        }
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
        } else if (id == R.id.action_scan_and_count) {
            Intent intent = new Intent(HomeActivity.this, ScanAndCount.class);
            startActivity(intent);
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
