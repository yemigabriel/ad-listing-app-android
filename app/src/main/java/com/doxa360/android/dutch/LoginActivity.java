package com.doxa360.android.dutch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.android.dutch.helpers.DutchSharedPref;
import com.doxa360.android.dutch.helpers.MyToolBox;
import com.doxa360.android.dutch.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private EditText mEmail,mPassword;
    private TextInputLayout mEmailLayout,mPasswordLayout;
    private Button mLoginBtn;
    private ProgressDialog mProgressDialog;
    private DutchApiInterface mDutchApiInterface;
    private String email,password;

    private DutchSharedPref mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView mForgotPassword = (TextView)  findViewById(R.id.forgot_password);
        mForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

        mSharedPref = new DutchSharedPref(this);
        mDutchApiInterface = DutchApiClient.getClient().create(DutchApiInterface.class);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Just a minute ...");
//
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mEmailLayout = (TextInputLayout) findViewById(R.id.layout_email);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.layout_password);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


    }

    private void validate() {

        if (!MyToolBox.isEmailValid(mEmail.getText().toString())) {
            mEmailLayout.setError("Please use a valid email address");
        }
        else if (!MyToolBox.isMinimumCharacters(mPassword.getText().toString(), 5)) {
            mPasswordLayout.setError("Type a password with at least 6 characters");
        }
        else {
//            mEmailLayout.setErrorEnabled(false);
//            mPasswordLayout.setErrorEnabled(false);
            email = mEmail.getText().toString().trim();
            password = mPassword.getText().toString().trim();
            login();
        }

    }

    private void login() {
        if (MyToolBox.isNetworkAvailable(LoginActivity.this)) {
            mProgressDialog.show();
            //do internet thingy
            User user = new User(email, password);
            Call<User> call = mDutchApiInterface.signInUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.e(TAG, "Response: "+response.body().getName()+response.message()+response.code());
                    if (response.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                        mSharedPref.setCurrentUser(response.body().toString());
                        Log.e(TAG, mSharedPref.getCurrentUser().getName()+"");

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        try {
                            JSONObject errorObject = new JSONObject(response.errorBody().string());
                            Toast.makeText(LoginActivity.this, errorObject.getString("error")+". Try a different email and password combination", Toast.LENGTH_LONG).show();
                        }
                        catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(TAG, "Failed: "+t.getMessage());
                    Toast.makeText(LoginActivity.this, "Failed: "+t.getMessage(), Toast.LENGTH_LONG).show();

                    mProgressDialog.dismiss();
                }
            });

        }
        else {
            Toast.makeText(LoginActivity.this, "Network Error. Please check your connection", Toast.LENGTH_LONG).show();

            mProgressDialog.dismiss();
        }


        //save to shared pref
        //move to home
//        Intent intent = new Intent(this, HomeActivity.class);
////        intent.putExtra(parcelable bla);
//        startActivity(intent);
    }


}
