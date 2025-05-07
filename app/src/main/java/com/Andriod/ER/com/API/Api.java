package com.Andriod.ER.com.API;

import okhttp3.OkHttpClient;
import retrofit.RestAdapter;

public class Api {
    public static ApiInterface getClient() {

        RestAdapter adapter = new RestAdapter.Builder()
//                .setEndpoint("http://174.138.39.62:4000/api/v1/auth/") //Setting the Root URL
                .setEndpoint("http://143.244.170.79:8080/api/v1") //Setting the Root URL
        .setLogLevel(RestAdapter.LogLevel.FULL)
                .build(); //Finally building the adapter

        //Creating object for our interface
        ApiInterface api = adapter.create(ApiInterface.class);
        return api;
    }
}
