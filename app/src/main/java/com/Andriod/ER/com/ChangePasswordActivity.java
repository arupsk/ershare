package com.Andriod.ER.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.Andriod.ER.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{
    CustomLoader customLoader;
    private TextInputEditText etPassword;
    FirebaseAuth authenticateUser;
    TextInputLayout emailTextInput;
    MaterialButton btnSend;
    String connectToInternet, somethingWentWrong, passwordLengthAtLeast6 = "";

    @Override
    protected void onResume() {
        super.onResume();
        try {
            JSONObject Languages = new JSONObject(SharedHelper.getKey(ChangePasswordActivity.this, "Languages"));
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
        setContentView(R.layout.activity_change_password);
        btnSend = findViewById(R.id.btnSend);
        etPassword = findViewById(R.id.etPassword);
        emailTextInput = findViewById(R.id.emailTextInput);
        customLoader = new CustomLoader(this);
        btnSend.setOnClickListener(this);
        authenticateUser = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            if (NetworkManager.isConnectingToInternet(this)) {
                if (etPassword.getText().toString().length()>5) {
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