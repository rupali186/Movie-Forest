package com.example.rupali.movieforest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    String firstName="";
    String profilePicUrl=new String();
    TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarTitle=findViewById(R.id.login_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Login");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        loginButton=findViewById(R.id.loginWithFb);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Bundle params = new Bundle();
                        params.putString("fields", "id,email,first_name,gender,cover,picture.type(large)");
                        new GraphRequest(loginResult.getAccessToken(), "me", params, HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        if (response != null) {
                                            try {
                                                JSONObject data = response.getJSONObject();
                                                if (data.has("picture")) {
                                                    profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                                }
                                                if(data.has("first_name")){
                                                    firstName=data.getString("first_name");
                                                }
                                                Log.d("Tag",firstName+" "+profilePicUrl);
                                                SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF_NAME,MODE_PRIVATE);
                                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                                editor.putString(Constants.LOGIN_NAME,firstName);
                                                editor.putBoolean(Constants.CONNECT_WITH_FACEBOOK,true);
                                                editor.putString(Constants.LOGIN_PROFILE_URL,profilePicUrl);
                                                editor.putBoolean(Constants.PREVIOUSLY_STARTED,true);
                                                editor.commit();
                                                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).executeAsync();
                        // App code

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("Tag","Cancelled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("Tag",exception.getMessage());
                    }
                });
            }

        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


}
