package com.Andriod.ER.com;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.Andriod.ER.R;
import com.Andriod.ER.com.API.Api;
import com.Andriod.ER.com.Model.LoginResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

public class RegistrationPage extends AppCompatActivity implements View.OnClickListener {

    //    FirebaseAuth mAuth;
    CustomLoader customLoader;
    MaterialButton btnRegister;
    private TextInputEditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword;
    private AutoCompleteTextView atCountry;
    MaterialTextView tvCreateAccount;
    TextInputLayout firstNameTextInput, lastNameTextInput, emailTextInput, cityDropDown, passwordTextInput, confirmPasswordTextInput;
    String connectToInternet, somethingWentWrong, passwordLengthAtLeast6, registeredSuccessfully,
            passwordDontMatch, pleaseEnterFirstName, pleaseEnterLastName, pleaseSelectCountry,
            enterValidEmail, enterValidPassword = "";

    @Override
    protected void onResume() {
        super.onResume();
        try {
            JSONObject Languages = new JSONObject(SharedHelper.getKey(RegistrationPage.this, "Languages"));
            String enterFirstName = Languages.optString("enterFirstName");
            String enterLastName = Languages.optString("enterLastName");
            enterValidPassword = Languages.optString("enterValidPassword");
            enterValidEmail = Languages.optString("enterValidEmail");
            pleaseSelectCountry = Languages.optString("pleaseSelectCountry");
            String pleaseEnterPassword = Languages.optString("pleaseEnterPassword");
            String pleaseEnterConfirmPassword = Languages.optString("pleaseEnterConfirmPassword");
            String firstName = Languages.optString("firstName");
            String lastName = Languages.optString("lastName");
            String email = Languages.optString("email");
            String password = Languages.optString("password");
            String createAnAccountHere = Languages.optString("createAnAccountHere");
            String createAccount = Languages.optString("createAccount");
            connectToInternet = Languages.optString("connectToInternet");
            somethingWentWrong = Languages.optString("somethingWentWrong");
            passwordLengthAtLeast6 = Languages.optString("passwordLengthAtLeast6");
            registeredSuccessfully = Languages.optString("registeredSuccessfully");
            passwordDontMatch = Languages.optString("passwordDontMatch");
            pleaseEnterFirstName = Languages.optString("pleaseEnterFirstName");
            pleaseEnterLastName = Languages.optString("pleaseEnterLastName");
            firstNameTextInput.setHint(firstName + "*");
            firstNameTextInput.setPlaceholderText(enterFirstName);
            lastNameTextInput.setHint(lastName + "*");
            lastNameTextInput.setPlaceholderText(enterLastName);
            emailTextInput.setHint(email + "*");
            emailTextInput.setPlaceholderText(enterValidEmail);
            cityDropDown.setPlaceholderText(pleaseSelectCountry);
            cityDropDown.setHint(pleaseSelectCountry);
            passwordTextInput.setHint(password + "*");
            passwordTextInput.setPlaceholderText(pleaseEnterPassword);
            confirmPasswordTextInput.setHint(password + "*");
            confirmPasswordTextInput.setPlaceholderText(pleaseEnterConfirmPassword);
            tvCreateAccount.setText(createAnAccountHere);
            btnRegister.setText(createAccount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page2);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        atCountry = findViewById(R.id.etSelectCountry);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        firstNameTextInput = findViewById(R.id.firstNameTextInput);
        lastNameTextInput = findViewById(R.id.lastNameTextInput);
        emailTextInput = findViewById(R.id.emailTextInput);
        cityDropDown = findViewById(R.id.cityDropDown);
        passwordTextInput = findViewById(R.id.passwordTextInput);
        confirmPasswordTextInput = findViewById(R.id.confirmPasswordTextInput);
        customLoader = new CustomLoader(this);

        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        ArrayAdapter autoCompleteTextAdapter = new ArrayAdapter<>(
                this,
                R.layout.item_country, R.id.tvCountryItem,
                getResources().getStringArray(R.array.countries_array));
        atCountry.setAdapter(autoCompleteTextAdapter);

        atCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atCountry.setText(parent.getItemAtPosition(position).toString(), false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            if (NetworkManager.isConnectingToInternet(this)) {
                if (!Validation()) {
                    customLoader.show();
                    registerUserToFirebase();
                }
            } else {
                Toast.makeText(this, connectToInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUserToFirebase() {

//        // Api is a class in which we define a method getClient() that returns the API Interface class object
//        // registration is a POST request type method in which we are sending our field's data
        Api.getClient().register(
                etEmail.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                etFirstName.getText().toString().trim(),
                etLastName.getText().toString().trim(),
                atCountry.getText().toString().trim(),
                new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject loginResponse, Response response) {
                        customLoader.dismiss();
//                        Log.d("Hamza", loginResponse.toString());
//                        SharedHelper.putKey(RegistrationPage.this,"Login","true");
                        Toast.makeText(RegistrationPage.this, registeredSuccessfully, Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(RegistrationPage.this, LogIn.class));
                        finishAffinity();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        customLoader.dismiss();
                        Toast.makeText(RegistrationPage.this, somethingWentWrong, Toast.LENGTH_SHORT).show();


                    }
                });


//        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("TAG", "createUserWithEmail:success");
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        Log.d("TAG", user.getEmail());
//                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(
//                                etFirstName.getText().toString().trim(),
//                                etLastName.getText().toString().trim(),
//                                etEmail.getText().toString().trim(),
//                                atCountry.getText().toString().trim(),
//                                etPassword.getText().toString().trim()
//                        );
//
//
////                            add registered user data into firebase realtime database
//                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersData");
//                        reference.child(user.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                customLoader.dismiss();
//                                if (task.isSuccessful()) {
//                                    AlertDialog alertDialog = new AlertDialog.Builder(RegistrationPage.this).create();
//                                    alertDialog.setTitle("Success");
//                                    alertDialog.setMessage("Your account has been created successfully");
//                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                            (dialog, which) -> {
//                                                startActivity(new Intent(RegistrationPage.this, MainActivity.class));
//                                                finishAffinity();
//                                                dialog.dismiss();
//                                            });
//                                    alertDialog.show();
//                                    alertDialog.setCancelable(false);
//                                } else {
//                                    Toast.makeText(RegistrationPage.this, "Registration process has been failed", Toast.LENGTH_SHORT).show();
//                                }
//
//
//                            }
//                        });
//                    } else {
//                        customLoader.dismiss();
//                        try {
//                            throw task.getException();
//                        } catch (FirebaseAuthUserCollisionException e) {
//                            e.printStackTrace();
//                            AlertDialog alertDialog = new AlertDialog.Builder(RegistrationPage.this).create();
//                            alertDialog.setTitle("Alert");
//                            alertDialog.setMessage(e.getLocalizedMessage());
//                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                    (dialog, which) -> {
//                                        etEmail.setError("Please use another valid email address");
//                                        etEmail.requestFocus();
//                                        dialog.dismiss();
//                                    });
//                            alertDialog.show();
//                            alertDialog.setCancelable(false);
//                        } catch (Exception e) {
//                            Log.w("TAG", "createUserWithEmail:failure" + e.getMessage());
//                        }
//                    }
//                });
    }

    public boolean Validation() {
        boolean failFlag = false;

        if (etFirstName.getText().toString().trim().length() == 0) {
            etFirstName.setError(pleaseEnterFirstName);
            etFirstName.requestFocus();
            failFlag = true;
        } else if (etLastName.getText().toString().trim().length() < 1) {
            etLastName.setError(pleaseEnterLastName);
            etLastName.requestFocus();
            failFlag = true;
        } else if (!isValidEmail(etEmail.getText().toString().trim()) || etEmail.getText().toString().contains(" ")) {
            etEmail.setError(enterValidEmail);
            etEmail.requestFocus();
            failFlag = true;
        } else if (etPassword.getText().toString().trim().length() < 1) {
            etPassword.setError(enterValidPassword);
            etPassword.requestFocus();
            failFlag = true;
        } else if (etConfirmPassword.getText().toString().trim().length() < 1) {
            failFlag = true;
            etConfirmPassword.setError(enterValidPassword);
            etConfirmPassword.requestFocus();
        } else if (!etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {
            Toast.makeText(RegistrationPage.this, passwordDontMatch, Toast.LENGTH_SHORT).show();
            failFlag = true;
        } else if (atCountry.getText().toString().trim().length() < 1) {
            Toast.makeText(RegistrationPage.this, pleaseSelectCountry, Toast.LENGTH_SHORT).show();
            failFlag = true;
        }

        return failFlag;
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
