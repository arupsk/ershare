package com.Andriod.ER.com;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.Andriod.ER.R;
import com.Andriod.ER.com.API.Api;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class UsersBatteries extends MainActivity {
    private DataRecyclerView batteriesAdapter;
    private JSONArray data;
    RecyclerView batteriessRecyclerView;
    CustomLoader customLoader;
    TextView noresult;
    private ImageView backBtn;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_users_batteries, contentFrameLayout);

//        setContentView(R.layout.activity_users_batteries);
        customLoader = new CustomLoader(this);

        noresult = findViewById(R.id.noresult);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        batteriessRecyclerView = findViewById(R.id.batteriessRecyclerView);
                customLoader.show();

        String user = SharedHelper.getKey(UsersBatteries.this, "UserId");
        Api.getClient().getUserBatteries("3a6db3d5-73c5-433c-83fb-2001782966ad", new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                customLoader.dismiss();

                try {
                    JSONObject object = new JSONObject(String.valueOf(jsonObject));
                    data = object.getJSONArray("data");
                    if (data.length()>0){
                        batteriesAdapter = new DataRecyclerView(UsersBatteries.this, data);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UsersBatteries.this);
                        batteriessRecyclerView.setLayoutManager(layoutManager);
                        batteriessRecyclerView.setAdapter(batteriesAdapter);
                        batteriessRecyclerView.setItemAnimator(new DefaultItemAnimator());

                        batteriessRecyclerView.setVisibility(View.VISIBLE);
                        noresult.setVisibility(View.GONE);

                    }else {
                        noresult.setVisibility(View.VISIBLE);
                        batteriessRecyclerView.setVisibility(View.GONE);
                    }
                    Log.d("Hamza Batteries", data.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                customLoader.dismiss();

            }
        });
       }
}