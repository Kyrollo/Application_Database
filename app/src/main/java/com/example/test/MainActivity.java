package com.example.test;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
        setupLanguageSwitching();
    }

    public void addListenerOnButton() {
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.equals("Kerollos Mansour") && password.equals("admin")) {
                Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid credentials. Please re-write your username and password.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupLanguageSwitching() {
        ImageView iconArabic = findViewById(R.id.icon_arabic);
        ImageView iconEnglish = findViewById(R.id.icon_english);

        iconArabic.setOnClickListener(view -> setLocale("ar"));
        iconEnglish.setOnClickListener(view -> setLocale("en"));
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = new Configuration();
        config.setLocale(locale);
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(config, dm);

        // Restart the activity to apply the language change
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }
}
