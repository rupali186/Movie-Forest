package com.example.rupali.movieforest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView skip;
    ImageView connectWithGoogle;
    ImageView connectWithFacebook;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbarTitle=findViewById(R.id.main_toolbar_title);
//        setSupportActionBar(toolbar);
//        toolbarTitle.setText("MovieForest");
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        skip=findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(Constants.LOGIN_NAME,"Guest");
                editor.putString(Constants.LOGIN_EMAIL,"Login Or Sign Up");
                editor.putBoolean(Constants.PREVIOUSLY_STARTED,true);
                editor.commit();
                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        connectWithGoogle=findViewById(R.id.connectWithGoogle);
        connectWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        connectWithFacebook=findViewById(R.id.connectWithFacebook);
        connectWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewPager=findViewById(R.id.viewPager);
        adapter=new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
//        explore=findViewById(R.id.explore);
//        nameEditText=findViewById(R.id.nameEditText);
//        emailEditText=findViewById(R.id.emailEditText);
//        explore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(nameEditText.getText().toString().length()==0){
//                    Snackbar.make(nameEditText,"name is required",Snackbar.LENGTH_LONG).show();
//                    return;
//                }
//                if(emailEditText.getText().toString().length()==0){
//                    Snackbar.make(emailEditText,"email is required",Snackbar.LENGTH_LONG).show();
//                    return;
//                }
//                SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF_NAME,MODE_PRIVATE);
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//                editor.putString(Constants.LOGIN_NAME,nameEditText.getText().toString());
//                editor.putString(Constants.LOGIN_EMAIL,emailEditText.getText().toString());
//                editor.putBoolean(Constants.PREVIOUSLY_STARTED,true);
//                editor.commit();
//                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
//                startActivity(intent);
//            }
//        });

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
}
