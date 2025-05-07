package com.Andriod.ER.com;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.Andriod.ER.R;
import com.Andriod.ER.com.API.Api;
import com.Andriod.ER.com.Model.LoginResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    //    private EditText user_name, pass_word;
    private TextInputEditText etEmail, etPassword;
    private TextInputLayout emailTextInput, passwordTextInput;
    //    FirebaseAuth authenticateUser;
    CustomLoader customLoader;
    MaterialTextView tvForgetPassword, tvCreateAccount;
    MaterialButton btnLogin;
    String connectToInternet, somethingWentWrong, passwordLengthAtLeast6, enterValidEmail,ok, activateAccountEmailText = "";

    @Override
    protected void onResume() {
        super.onResume();
        try {
            JSONObject Languages = new JSONObject(SharedHelper.getKey(LogIn.this, "Languages"));
            String email = Languages.optString("email");
            String password = Languages.optString("password");
            String forgetPassword = Languages.optString("forgetPassword");
            String createAccount = Languages.optString("createAccount");
            enterValidEmail = Languages.optString("enterValidEmail");
            activateAccountEmailText = Languages.optString("activateAccountEmailText");
            ok = Languages.optString("ok");
            String enterValidPassword = Languages.optString("enterValidPassword");
            String logIn = Languages.optString("logIn");
            connectToInternet = Languages.optString("connectToInternet");
            somethingWentWrong = Languages.optString("somethingWentWrong");
            passwordLengthAtLeast6 = Languages.optString("passwordLengthAtLeast6");
            emailTextInput.setHint(email + "*");
            emailTextInput.setPlaceholderText(enterValidEmail);
            passwordTextInput.setHint(password + "*");
            passwordTextInput.setPlaceholderText(enterValidPassword);
            tvForgetPassword.setText(forgetPassword);
            tvCreateAccount.setText(createAccount);
            btnLogin.setText(logIn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetworkManager.isConnectingToInternet(this)) {
            Api.getClient().getLanguages(SharedHelper.getKeyLanguage(LogIn.this, "LanguageGet"), new Callback<JsonObject>() {
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
                            SharedHelper.putKey(LogIn.this, "Languages", data.toString());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LogIn.this, somethingWentWrong, Toast.LENGTH_SHORT).show();

                }
            });


        } else {
            Toast.makeText(this, connectToInternet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);

        btnLogin = findViewById(R.id.btnLogIn);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        emailTextInput = findViewById(R.id.emailTextInput);
        passwordTextInput = findViewById(R.id.passwordTextInput);

        customLoader = new CustomLoader(this);

        btnLogin.setOnClickListener(this);
        tvCreateAccount.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);

//        authenticateUser = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvCreateAccount) {
            startActivity(new Intent(this, RegistrationPage.class));
        } else if (v.getId() == R.id.tvForgetPassword) {
            startActivity(new Intent(this, ForgetPassword.class));

        } else if (v.getId() == R.id.btnLogIn) {


            if (NetworkManager.isConnectingToInternet(this)) {
                if (isValidEmail(etEmail.getText().toString().trim())) {
                    if (etPassword.getText().toString().length() > 5) {
                        loginUser(etEmail.getText().toString(),
                                etPassword.getText().toString());
                    } else {
                        etPassword.setError(passwordLengthAtLeast6);
                    }
                } else {
                    etEmail.setError(enterValidEmail);
                }
            } else {
                Toast.makeText(this, connectToInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginUser(String email, String password) {
        customLoader.show();

        Api.getClient().login(
                email,
                password,
                new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject loginResponse, Response response) {
                        Log.d("Hamza", loginResponse.toString());
                        if (loginResponse != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(loginResponse));
//                                JSONObject data = jsonObject.optJSONObject("data");
                                String data = jsonObject.optString("data");
                                SharedHelper.putKey(LogIn.this, "UserToken", data);

                                Api.getClient().user("Bearer " + data, new Callback<JsonObject>() {
                                    @Override
                                    public void success(JsonObject jsonObject, Response response) {
                                        try {
                                            customLoader.dismiss();
                                            SharedHelper.putKey(LogIn.this, "Login", "true");

                                            JSONObject jsonObject1 = new JSONObject(String.valueOf(jsonObject));
                                            JSONObject dataJsonObj = jsonObject1.optJSONObject("data");
                                            JSONObject data2JsonObj = null;
                                            if (dataJsonObj != null) {
                                                data2JsonObj = dataJsonObj.optJSONObject("data");
                                            }
                                            if (data2JsonObj != null) {
                                                String ID = data2JsonObj.optString("id");
                                                String firstName = data2JsonObj.optString("firstName");
                                                String status = data2JsonObj.optString("status");
                                                if (status.equals("pending")) {
                                                    AlertDialog alertDialog = new AlertDialog.Builder(LogIn.this).create();
                                                    alertDialog.setMessage(activateAccountEmailText);
                                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, ok,
                                                            (dialog, which) -> {
                                                                dialog.dismiss();
                                                            });
                                                    alertDialog.show();
                                                    alertDialog.setCancelable(false);
                                                } else {
                                                    SharedHelper.putKey(LogIn.this, "UserId", ID);
                                                    SharedHelper.putKey(LogIn.this, "UserName", firstName);
                                                    Api.getClient().getUserBatteries(ID, new Callback<JsonObject>() {
                                                        @Override
                                                        public void success(JsonObject jsonObject, Response response) {
                                                            try {
                                                                JSONObject object = new JSONObject(String.valueOf(jsonObject));
                                                                SharedHelper.putKey(LogIn.this, "UserBatteriesList", object.getJSONArray("data").toString());
                                                                startActivity(new Intent(LogIn.this, MainActivity.class));
                                                                finish();

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void failure(RetrofitError error) {

                                                        }
                                                    });
                                                }


                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        customLoader.dismiss();

                                        Toast.makeText(LogIn.this, somethingWentWrong, Toast.LENGTH_SHORT).show();

                                    }
                                });
//                                SharedHelper.putKey(LogIn.this, "UserId", data.optString("uid"));
//                                SharedHelper.putKey(LogIn.this, "UserToken", data.optString("accessToken"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        customLoader.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(error.getBody().toString());
                            String errorobj = jsonObject.optString("error");
                            Toast.makeText(LogIn.this, errorobj, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });


//        authenticateUser.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
//            customLoader.dismiss();
//            if (task.isSuccessful()) {
//                startActivity(new Intent(LogIn.this, MainActivity.class));
//                finish();
//            } else {
//                customLoader.dismiss();
//                try {
//                    throw task.getException();
//                } catch (FirebaseAuthInvalidUserException e) {
//                    etEmail.setError("User does not exist");
//                    etEmail.requestFocus();
//                } catch (FirebaseAuthInvalidCredentialsException e) {
//                    etEmail.setError("Invalid email or password");
//                    etEmail.requestFocus();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
