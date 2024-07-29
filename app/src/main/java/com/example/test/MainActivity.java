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
import com.example.test.API.ApiService;
import com.example.test.ApiClasses.User;
import java.util.Locale;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private ApiService apiService;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.77:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        addListenerOnButton();
        setupLanguageSwitching();
    }

    public void addListenerOnButton() {
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);


        loginButton.setOnClickListener(view -> {
            testConnection(this::fetchUsers);
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

    private void doLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (validateUser(username, password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.loginSucceeded), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.loginFailed), Toast.LENGTH_LONG).show();
        }
    }

    private void fetchUsers() {
        apiService.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    users = response.body();
                    doLogin();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.getUsersFailed), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.getUsersFailed), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateUser(String username, String password) {
        if (users == null) {
            return false;
        }
        for (User user : users) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void testConnection(Runnable onSuccess) {
        apiService.testConnection().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && "Success".equals(response.body())) {
                    onSuccess.run();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.connectionFailed), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.connectionFailed), Toast.LENGTH_LONG).show();
            }
        });
    }
}