package com.doxa360.android.dutch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private EditText mEmail,mPassword, mName;
    private TextInputLayout mEmailLayout,mPasswordLayout, mNameLayout;
    private Button mSignUpBtn;
    private ProgressDialog mProgressDialog;
    private DutchApiInterface mDutchApiInterface;
    private String name,email,password;

    private DutchSharedPref mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView mTermsLabel = (TextView)  findViewById(R.id.terms_label);
        mTermsLabel.setMovementMethod(LinkMovementMethod.getInstance());

        mSharedPref = new DutchSharedPref(this);
        mDutchApiInterface = DutchApiClient.getClient().create(DutchApiInterface.class);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Creating account ...");
//
        mName = (EditText) findViewById(R.id.name);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mNameLayout = (TextInputLayout) findViewById(R.id.layout_name);
        mEmailLayout = (TextInputLayout) findViewById(R.id.layout_email);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.layout_password);
        mSignUpBtn = (Button) findViewById(R.id.signUpBtn);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    private void validate() {

        if (!MyToolBox.isMinimumCharacters(mName.getText().toString(), 5)) {
            mNameLayout.setError("Type in your name");
        }
        else if (!MyToolBox.isEmailValid(mEmail.getText().toString())) {
            mEmailLayout.setError("Please use a valid email address");
        }
        else if (!MyToolBox.isMinimumCharacters(mPassword.getText().toString(), 5)) {
            mPasswordLayout.setError("Type a password with at least 6 characters");
        }
        else {
//            mEmailLayout.setErrorEnabled(false);
//            mPasswordLayout.setErrorEnabled(false);
            name = mName.getText().toString().trim();
            email = mEmail.getText().toString().trim();
            password = mPassword.getText().toString().trim();
            signup();
        }

    }

    private void signup() {
        if (MyToolBox.isNetworkAvailable(SignUpActivity.this)) {
            mProgressDialog.show();
            //do internet thingy
            Log.e(TAG, name+email+password+" ");
            User user = new User(email, password, name);
            Call<User> call = mDutchApiInterface.signUpUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.e(TAG, "Response: "+response.message()+response.code());
                    if (response.isSuccessful()) {
//                        response.body()
                        Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_LONG).show();
                        mSharedPref.setCurrentUser(response.body().toString());
                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        try {
                            JSONObject errorObject = new JSONObject(response.errorBody().string());
                            Toast.makeText(SignUpActivity.this, "Response: " + errorObject.getString("error"), Toast.LENGTH_LONG).show();
                        }
                        catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Toast.makeText(SignUpActivity.this, "Response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(TAG, "Failed: "+t.getMessage());
                    Toast.makeText(SignUpActivity.this, "The email address has already been registered", Toast.LENGTH_LONG).show();

                    mProgressDialog.dismiss();
                }
            });

        }
        else {
            Toast.makeText(SignUpActivity.this, "Network Error. Please check your connection", Toast.LENGTH_LONG).show();

            mProgressDialog.dismiss();
        }

        //save to shared pref
        //move to home
//        Intent intent = new Intent(this, HomeActivity.class);
////        intent.putExtra(parcelable bla);
//        startActivity(intent);
    }


}
