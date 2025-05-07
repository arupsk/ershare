package com.Andriod.ER.com;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Andriod.ER.R;
import com.Andriod.ER.com.API.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Lanch extends AppCompatActivity {

    //    FirebaseAuth firebaseAuth;
    TextView Weofferyou;
    String connectToInternet, somethingWentWrong, passwordLengthAtLeast6 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanch);
        Weofferyou = findViewById(R.id.Weofferyou);




    }

    @Override
    protected void onResume() {
        super.onResume();
        //        firebaseAuth = FirebaseAuth.getInstance();
        if (NetworkManager.isConnectingToInternet(this)) {
            Api.getClient().getLanguages(SharedHelper.getKeyLanguage(Lanch.this, "LanguageGet"), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    try {
                        JSONObject jsonObj = new JSONObject(String.valueOf(jsonObject));
                        JSONObject data = jsonObj.optJSONObject("data");
//                        String BLCNotSupported = data.optString("BLCNotSupported");
//                        Log.d("Hamza", jsonObj.toString());
//                        Log.d("Hamza", data.toString());
//                        Log.d("Hamza", BLCNotSupported);
                        if (data != null) {
                            SharedHelper.putKey(Lanch.this, "Languages", data.toString());
                            JSONObject Languages = new JSONObject(SharedHelper.getKey(Lanch.this, "Languages"));
                            String email = Languages.optString("email");
//                            Weofferyou.setText("");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String Login = SharedHelper.getKey(Lanch.this, "Login");
                                    if (Login.equals("true")) {
                                        Intent i = new Intent(Lanch.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Intent i = new Intent(Lanch.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }, 2000);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Intent i = new Intent(Lanch.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
            });


        } else {
            Intent i = new Intent(Lanch.this, MainActivity.class);
            startActivity(i);
            finish();
        }


       //skip database
        /*Intent i = new Intent(Lanch.this, MainActivity.class);
        startActivity(i);
        finish();*/

    }
}

