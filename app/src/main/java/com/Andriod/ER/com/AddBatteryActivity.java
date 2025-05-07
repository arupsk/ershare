package com.Andriod.ER.com;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.Andriod.ER.R;
import com.Andriod.ER.com.API.Api;
import com.Andriod.ER.com.Model.LoginResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class AddBatteryActivity extends MainActivity {
    private ImageView backBtn;
    private String BatteryName = "";
    private TextInputEditText etName;
    private Button submitbtn;
    MaterialTextView tvCreateBattery;
    TextInputLayout nameTextInput;
    CustomLoader customLoader;
    String connectToInternet, somethingWentWrong,pleaseEnterName,success,batteryAddedSuccess,ok= "";
    String date="";
    //    FirebaseAuth mAuth;
//    FirebaseUser user;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            JSONObject Languages = new JSONObject(SharedHelper.getKey(AddBatteryActivity.this, "Languages"));
            String createBattery = Languages.optString("createBattery");
             ok = Languages.optString("ok");
            String name = Languages.optString("name");
            batteryAddedSuccess = Languages.optString("batteryAddedSuccess");
             pleaseEnterName = Languages.optString("pleaseEnterName");
            String submit = Languages.optString("submit");
            success = Languages.optString("success");
            connectToInternet = Languages.optString("connectToInternet");
            somethingWentWrong = Languages.optString("somethingWentWrong");
            tvCreateBattery.setText(createBattery);
            nameTextInput.setHint(name+"*");
            nameTextInput.setPlaceholderText(pleaseEnterName);
            submitbtn.setText(submit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_add_battery, contentFrameLayout);



//        setContentView(R.layout.activity_add_battery);
        backBtn = findViewById(R.id.backBtn);
        etName = findViewById(R.id.etName);
        submitbtn = findViewById(R.id.btnSubmit);
        nameTextInput = findViewById(R.id.nameTextInput);
        tvCreateBattery = findViewById(R.id.tvCreateBattery);
        customLoader = new CustomLoader(this);
         date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("HamzaDate",date);
//        mAuth = FirebaseAuth.getInstance();
//        user = mAuth.getCurrentUser();


        if (getIntent() != null) {
            if (getIntent().getStringExtra("BatteryName") != null) {
                BatteryName = getIntent().getStringExtra("BatteryName");
                etName.setText(BatteryName);
                Log.d("BatteryName= ", BatteryName);
            }
        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectingToInternet(AddBatteryActivity.this)) {
                    if (!Validation()) {
                        customLoader.show();
                        registerBatteryToFirebase();
                    }
                } else {
                    Toast.makeText(AddBatteryActivity.this, connectToInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerBatteryToFirebase() {
        String user = SharedHelper.getKey(AddBatteryActivity.this, "UserId");
        String firstName = SharedHelper.getKey(AddBatteryActivity.this, "UserName");

        if (user != null) {
            Api.getClient().storeBattery(
                    etName.getText().toString(), "", "", date, false, user, firstName,false,
                    new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject jsonObject, Response response) {
                            customLoader.dismiss();
//                            Log.d("Hamza", jsonObject.optJSONObject("data").toString());
                            //                        finish();

                            try {
                                JSONObject object = new JSONObject(String.valueOf(jsonObject));
                                String error = object.optString("error");
                                if (error!=null &&error!=""){
                                    AlertDialog alertDialog = new AlertDialog.Builder(AddBatteryActivity.this).create();
                                    alertDialog.setTitle("Error");
                                    alertDialog.setMessage(error);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, ok,
                                            (dialog, which) -> {
                                                dialog.dismiss();
                                                finish();
                                            });
                                    alertDialog.show();
                                    alertDialog.setCancelable(false);
                                }else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(AddBatteryActivity.this).create();
                                    alertDialog.setTitle(success);
                                    alertDialog.setMessage(batteryAddedSuccess);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, ok,
                                            (dialog, which) -> {
                                                dialog.dismiss();
                                                finish();
                                            });
                                    alertDialog.show();
                                    alertDialog.setCancelable(false);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            customLoader.dismiss();
                            Log.d("Hamza", error.toString());
                            Toast.makeText(AddBatteryActivity.this, somethingWentWrong, Toast.LENGTH_SHORT).show();
                            finish();


                        }
                    }
            );
        }
//        ReadWriteBatteryDetails writeBatteryDetails = new ReadWriteBatteryDetails(
//                etName.getText().toString().trim()
//        );
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BatteryData");
//        if (user != null) {
//
//            reference.child(user.getUid()).push().setValue(writeBatteryDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    customLoader.dismiss();
//                    if (task.isSuccessful()) {
//                        AlertDialog alertDialog = new AlertDialog.Builder(AddBatteryActivity.this).create();
//                        alertDialog.setTitle("Success");
//                        alertDialog.setMessage("Your Battery has been Added successfully");
//                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                (dialog, which) -> {
//                                    dialog.dismiss();
//                                });
//                        alertDialog.show();
//                        alertDialog.setCancelable(false);
//                    } else {
//                        Toast.makeText(AddBatteryActivity.this, "Adding process has been failed", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }
//            });
//        }
    }

    public boolean Validation() {
        boolean failFlag = false;

        if (etName.getText().toString().trim().length() == 0) {
            etName.setError(pleaseEnterName);
            etName.requestFocus();
            failFlag = true;
        }

        return failFlag;
    }

}