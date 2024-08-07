package com.example.test.Scan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.R;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ScanAndCount extends AppCompatActivity {
    private RecyclerView recyclerView;
    private scanAdapter adapter;
    private List<String> dataListBarCode;
    private List<Integer> dataListQty;
    TextView textBarCode;
    EditText editQty;
    BroadcastReceiver myBroadcastReceiver;
    private static final int REQUEST_CODE_PERMISSIONS = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_and_count);

        textBarCode = findViewById(R.id.editBarCode);
        editQty = findViewById(R.id.editQty);
        CheckBox changeQtyCheckbox = findViewById(R.id.changeQtyCheckbox);
        Button btnExport = findViewById(R.id.btnExport);

        editQty.setEnabled(false);
        editQty.setFilters(new InputFilter[]{createInputFilter()});

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataListBarCode = new ArrayList<>();
        dataListQty = new ArrayList<>();
        adapter = new scanAdapter(dataListBarCode, dataListQty);
        recyclerView.setAdapter(adapter);

        changeQtyCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String qtyStr = editQty.getText().toString().trim();
            validQuantity(qtyStr, isChecked, editQty);
        });

        btnExport.setOnClickListener(view -> {
            exportData(dataListBarCode, dataListQty);
            clearData();
            Toast.makeText(ScanAndCount.this, "Data exported", Toast.LENGTH_SHORT).show();
        });

        myBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(getResources().getString(R.string.activity_intent_filter_action))) {
                    String scannedData = intent.getStringExtra(getResources().getString(R.string.datawedge_intent_data_String));
                    textBarCode.setText(scannedData);
                    String qtyStr = editQty.getText().toString();
                    int qty = Integer.parseInt(qtyStr);
                    addData(scannedData, qty);
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        filter.addAction(getResources().getString(R.string.activity_intent_filter_action));
        registerReceiver(myBroadcastReceiver, filter);
    }

    private void validQuantity(String qtyStr, boolean isChecked, EditText editQty) {
        int qty = qtyStr.isEmpty() ? 0 : Integer.parseInt(qtyStr);
        if (qty > 0) {
            editQty.setEnabled(isChecked);
        } else {
            editQty.setEnabled(false);
            editQty.setText("1");
            Toast.makeText(ScanAndCount.this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
        }
    }

    private void addData(String barCode, int qty) {
        if (dataListBarCode.contains(barCode)) {
            int index = dataListBarCode.indexOf(barCode);
            int currentQty = dataListQty.get(index);
            dataListQty.set(index, currentQty + qty);
        } else{
            dataListBarCode.add(barCode);
            dataListQty.add(qty);
        }
        adapter.notifyDataSetChanged();
    }

    private void clearData() {
        textBarCode.setText(getString(R.string.waiting_for_scan));
        dataListBarCode.clear();
        dataListQty.clear();
        adapter.notifyDataSetChanged();
    }

    public void exportData(List<String> dataListBarCode, List<Integer> dataListQty) {
        if (!hasPermissions()) {
            requestPermissions();
        }

        String fileName = "Scanned Data.xlsx";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(getString(R.string.exportSheetName));

            XSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue(getString(R.string.Scanner_barcode));
            headerRow.createCell(1).setCellValue(getString(R.string.Recycle_Quantity));

            int rowNum = 1;
            for (String barCode : dataListBarCode) {
                XSSFRow row = sheet.createRow(rowNum++);
                int qty = dataListQty.get(dataListBarCode.indexOf(barCode));
                row.createCell(0).setCellValue(barCode);
                row.createCell(1).setCellValue(qty);
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            Toast.makeText(ScanAndCount.this, getString(R.string.secondCreatedExcelFile), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ScanAndCount.this, getString(R.string.failedCreateExcelFile), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return writePermission == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Uri uri = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                startActivityForResult(intent, REQUEST_CODE_PERMISSIONS);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, REQUEST_CODE_PERMISSIONS);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportData(dataListBarCode, dataListQty);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    exportData(dataListBarCode, dataListQty);
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private InputFilter createInputFilter() {
        return (source, start, end, dest, dstart, dend) -> {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (input > 0) {
                    return null;
                }
            } catch (NumberFormatException e) {
                // Ignore
            }
            return "";
        };
    }
}