package com.Andriod.ER.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.Andriod.ER.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyCodeActivity extends AppCompatActivity implements View.OnClickListener {
    CustomLoader customLoader;
    private TextInputEditText etCode;
    FirebaseAuth authenticateUser;
    TextInputLayout emailTextInput;
    MaterialButton btnSend;
    String connectToInternet, somethingWentWrong, enterValidCode = "";

    @Override
    protected void onResume() {
        super.onResume();
        try {
            JSONObject Languages = new JSONObject(SharedHelper.getKey(VerifyCodeActivity.this, "Languages"));
            String pleaseEnterCode = Languages.optString("pleaseEnterCode");
            String send = Languages.optString("send");
            enterValidCode = Languages.optString("enterValidCode");
            connectToInternet = Languages.optString("connectToInternet");
            somethingWentWrong = Languages.optString("somethingWentWrong");
            emailTextInput.setPlaceholderText(pleaseEnterCode);
            emailTextInput.setHint(pleaseEnterCode);
            btnSend.setText(send);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        btnSend = findViewById(R.id.btnSend);
        etCode = findViewById(R.id.etCode);
        emailTextInput = findViewById(R.id.emailTextInput);
        customLoader = new CustomLoader(this);
        btnSend.setOnClickListener(this);
        authenticateUser = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            if (NetworkManager.isConnectingToInternet(this)) {
                if (etCode.getText().toString().trim().length() >= 5) {
                    verifyCode(etCode.getText().toString());

                } else {
                    etCode.setError(enterValidCode);
                }
            } else {
                Toast.makeText(this, connectToInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void verifyCode(String code) {
//        customLoader.show();

//        Api.getClient().reset_password(
//                email, new Callback<String>() {
//                    @Override
//                    public void success(String s, Response response) {
//                        customLoader.dismiss();
//                        Log.d("Hamza", s);
////                        startActivity(new Intent(ForgetPassword.this, LogIn.class));
////                        finish();
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        customLoader.dismiss();
//                        Log.d("Hamza", error.toString());
//                        Toast.makeText(ForgetPassword.this, "Something went wrong", Toast.LENGTH_SHORT).show();
////
//                    }
//                });


    }
}