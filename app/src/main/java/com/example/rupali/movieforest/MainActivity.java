package com.example.rupali.movieforest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView skip;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    LoginButton loginButton;
    CallbackManager callbackManager;
    String username=null;
    String url=null;
    String userId;
    URL profilePicture;
    String firstName;
    private LinearLayout llPagerDots;
    private ImageView[] ivArrayDotsPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        skip=findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(Constants.LOGIN_NAME,"Guest");
                editor.putBoolean(Constants.CONNECT_WITH_FACEBOOK,false);
                editor.putBoolean(Constants.PREVIOUSLY_STARTED,true);
                editor.commit();
                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        viewPager=findViewById(R.id.viewPager);
        llPagerDots = (LinearLayout)findViewById(R.id.pager_dots);
        adapter=new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        setupPagerIndidcatorDots();
        ivArrayDotsPager[0].setImageResource(R.drawable.selected_dot);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < ivArrayDotsPager.length; i++) {
                    ivArrayDotsPager[i].setImageResource(R.drawable.unselected_dot);
                }
                ivArrayDotsPager[position].setImageResource(R.drawable.selected_dot);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
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
                                                String profilePicUrl=new String();
                                                JSONObject data = response.getJSONObject();
                                                if (data.has("picture")) {
                                                     profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                                }
                                                if(data.has("first_name")){
                                                    firstName=data.getString("first_name");
                                                }
                                                Log.d("New",firstName+" "+profilePicUrl);
                                                SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF_NAME,MODE_PRIVATE);
                                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                                editor.putString(Constants.LOGIN_NAME,firstName);
                                                editor.putBoolean(Constants.CONNECT_WITH_FACEBOOK,true);
                                                editor.putString(Constants.LOGIN_PROFILE_URL,profilePicUrl);
                                                editor.putBoolean(Constants.PREVIOUSLY_STARTED,true);
                                                editor.commit();
                                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    Log.d("Fb","try");
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
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
            }

        });

//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                    }
//                });


//        boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

    }

    private void setupPagerIndidcatorDots() {
        ivArrayDotsPager = new ImageView[6];
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            ivArrayDotsPager[i] = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            ivArrayDotsPager[i].setLayoutParams(params);
            ivArrayDotsPager[i].setImageResource(R.drawable.unselected_dot);
            //ivArrayDotsPager[i].setAlpha(0.4f);
            ivArrayDotsPager[i].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    view.setAlpha(1);
                }
            });
            llPagerDots.addView(ivArrayDotsPager[i]);
//
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
