package com.example.test.API;

import com.example.test.ApiClasses.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/Connection/TestConnection")
    Call<String> testConnection();

    @GET("api/Connection/GetAllUsers")
    Call<List<User>> getUsers();

    @GET("api/DownloadData/GetAllItems")
    Call<List<CategoryResponse>> getCategories();

    @GET("api/DownloadData/GetCategories")
    Call<List<ItemResponse>> getItems();
}