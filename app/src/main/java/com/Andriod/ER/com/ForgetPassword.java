package com.Andriod.ER.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.Andriod.ER.R;
import com.Andriod.ER.com.API.Api;
import com.Andriod.ER.com.Model.LoginResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {
    CustomLoader customLoader;
    private TextInputEditText etEmail;
    TextInputLayout emailTextInput;
    //    FirebaseAuth authenticateUser;
    MaterialButton btnSend;
    String connectToInternet, somethingWentWrong, enterValidEmail,userNotFound,ok,success,emailSentWithInstruction = "";

    @Override
    protected void onResume() {
        super.onResume();
        try {
            JSONObject Languages = new JSONObject(SharedHelper.getKey(ForgetPassword.this, "Languages"));
            String pleaseEnterEmail = Languages.optString("pleaseEnterEmail");
            String email = Languages.optString("email");
            String send = Languages.optString("send");
            emailSentWithInstruction = Languages.optString("emailSentWithInstruction");
            ok = Languages.optString("ok");
            success = Languages.optString("success");
            userNotFound = Languages.optString("userNotFound");
            connectToInternet = Languages.optString("connectToInternet");
            somethingWentWrong = Languages.optString("somethingWentWrong");
            enterValidEmail = Languages.optString("enterValidEmail");
            emailTextInput.setHint(email);
            emailTextInput.setPlaceholderText(pleaseEnterEmail);
            btnSend.setText(send);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        btnSend = findViewById(R.id.btnSend);
        etEmail = findViewById(R.id.etEmail);
        emailTextInput = findViewById(R.id.emailTextInput);
        customLoader = new CustomLoader(this);
        btnSend.setOnClickListener(this);
//        authenticateUser = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            if (NetworkManager.isConnectingToInternet(this)) {
                if (isValidEmail(etEmail.getText().toString().trim())) {
                    forgetPassword(etEmail.getText().toString());

                } else {
                    etEmail.setError(enterValidEmail);
                }
            } else {
                Toast.makeText(this, connectToInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void forgetPassword(String email) {
        customLoader.show();
//        authenticateUser.sendPasswordResetEmail(email)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        customLoader.dismiss();
//                        AlertDialog alertDialog = new AlertDialog.Builder(ForgetPassword.this).create();
//                        alertDialog.setTitle("Success");
//                        alertDialog.setMessage("Email sent with instruction.");
//                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                (dialog, which) -> {
//                                    dialog.dismiss();
//                                    finish();
//                                });
//                        alertDialog.show();
//                        alertDialog.setCancelable(false);
//                        Log.d("Hamza", "Email sent.");
//                    }else {
//                        customLoader.dismiss();
//
//                        Toast.makeText(ForgetPassword.this, "Failed to send email!", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("Hamza", e.toString());
//                Toast.makeText(ForgetPassword.this, "Failed to send email!", Toast.LENGTH_SHORT).show();
//
//            }
//        });
        Api.getClient().reset_password(
                email, new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject s, Response response) {
                        customLoader.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(s));
//                            String data = jsonObject.optString("data");
                            AlertDialog alertDialog = new AlertDialog.Builder(ForgetPassword.this).create();
                            alertDialog.setTitle(success);
                            alertDialog.setMessage(emailSentWithInstruction);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, ok,
                                    (dialog, which) -> {
                                        dialog.dismiss();
                                        finish();
                                    });
                            alertDialog.show();
                            alertDialog.setCancelable(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                    @Override
                    public void failure(RetrofitError error) {
                        customLoader.dismiss();
                        Toast.makeText(ForgetPassword.this, userNotFound, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}