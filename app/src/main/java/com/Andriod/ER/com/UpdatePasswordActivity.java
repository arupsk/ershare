package com.Andriod.ER.com;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Andriod.ER.R;
import com.Andriod.ER.com.API.Api;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UpdatePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    CustomLoader customLoader;
    private TextInputEditText etPassword;
    FirebaseAuth authenticateUser;
    TextInputLayout emailTextInput;
    MaterialButton btnSend;
    String connectToInternet, somethingWentWrong, passwordLengthAtLeast6 = "";
    private ImageView backBtn;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            JSONObject Languages = new JSONObject(SharedHelper.getKey(UpdatePasswordActivity.this, "Languages"));
            String pleaseEnterPassword = Languages.optString("pleaseEnterPassword");
            String newPassword = Languages.optString("newPassword");
            String submit = Languages.optString("submit");
            connectToInternet = Languages.optString("connectToInternet");
            somethingWentWrong = Languages.optString("somethingWentWrong");
            passwordLengthAtLeast6 = Languages.optString("passwordLengthAtLeast6");
            emailTextInput.setHint(newPassword);
            emailTextInput.setPlaceholderText(pleaseEnterPassword);
            btnSend.setText(submit);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        btnSend = findViewById(R.id.btnSend);
        etPassword = findViewById(R.id.etPassword);
        emailTextInput = findViewById(R.id.emailTextInput);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customLoader = new CustomLoader(this);
        btnSend.setOnClickListener(this);
        authenticateUser = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            if (NetworkManager.isConnectingToInternet(this)) {
                if (etPassword.getText().toString().length() > 5) {
                    SubmitPassword(etPassword.getText().toString());
                } else {
                    etPassword.setError(passwordLengthAtLeast6);
                }
            } else {
                Toast.makeText(this, connectToInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SubmitPassword(String Password) {
        customLoader.show();
        String data = SharedHelper.getKey(UpdatePasswordActivity.this, "UserToken");


        Api.getClient().change_password("Bearer " + data, Password, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject s, Response response) {
                customLoader.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(s));
                    Toast.makeText(UpdatePasswordActivity.this, jsonObject.optString("data"), Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void failure(RetrofitError error) {
                customLoader.dismiss();
                Toast.makeText(UpdatePasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }
}