package com.example.rupali.movieforest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvViewAllActivity extends AppCompatActivity {
    ArrayList<TvResponse.Tv> tvArrayList;
    TvViewAllAdapter adapter;
    Bundle bundle;
    RecyclerView recyclerView;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;
    TextView toolbarTitle;
    FavOpenHelper openHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_view_all);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=findViewById(R.id.tv_view_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Tv Shows");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        openHelper=FavOpenHelper.getInstance(this);
        tvArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.tvViewAllRecycler);
        adapter=new TvViewAllAdapter(this, tvArrayList, new TvViewAllAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(TvViewAllActivity.this,TvDetailActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.TV_ID,tvArrayList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }

            @Override
            public void onToggleClicked(int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(TvViewAllActivity.this,DividerItemDecoration.VERTICAL));
        Intent intent=getIntent();
        bundle=intent.getExtras();
        progressBar=findViewById(R.id.tvViewAllProgressBsr);
        constraintLayout=findViewById(R.id.contentTvViewAll);
        if(bundle!=null){
            fetchDataFrombundle();
        }
    }

    private void fetchDataFrombundle() {
        constraintLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://api.themoviedb.org/3/").build();
        MovieAPI movieAPI = retrofit.create(MovieAPI.class);
        if(bundle.containsKey(Constants.VIEW_ALL_ID)){
            int viewAllId=bundle.getInt(Constants.VIEW_ALL_ID);
            switch (viewAllId){
                case Constants.AIRING_TODAY_VALUE:
                    toolbarTitle.setText("Airing Today");
                    Call<TvResponse> callAiringtoday = movieAPI.getTvAirngToday();
                    callAiringtoday.enqueue(new Callback<TvResponse>() {
                        @Override
                        public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                            TvResponse tvResponse = response.body();
                            ArrayList<TvResponse.Tv> tvs = tvResponse.results;
                            tvArrayList.clear();
                            tvArrayList.addAll(tvs);
                            adapter.notifyDataSetChanged();
                            constraintLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<TvResponse> call, Throwable t) {
                            Log.d("ResponseViewALLActivity", t.getMessage());
                            Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();

                        }
                    });
                    break;
                case Constants.ON_THE_AIR_VALUE:
                    toolbarTitle.setText("On The Air");
                    Call<TvResponse> callOnTheAir = movieAPI.getTvOnTheAir();
                    callOnTheAir.enqueue(new Callback<TvResponse>() {
                        @Override
                        public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                            TvResponse tvResponse = response.body();
                            ArrayList<TvResponse.Tv> tvs = tvResponse.results;
                            tvArrayList.clear();
                            tvArrayList.addAll(tvs);
                            adapter.notifyDataSetChanged();
                            constraintLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<TvResponse> call, Throwable t) {
                            Log.d("ResponseViewALLActivity", t.getMessage());
                            Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();
                        }
                    });
                    break;
                case Constants.POPULAR_VALUE:
                    toolbarTitle.setText("Popular Tv Shows");
                    Call<TvResponse> callPopular = movieAPI.getTvPopular();
                    callPopular.enqueue(new Callback<TvResponse>() {
                        @Override
                        public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                            TvResponse tvResponse = response.body();
                            ArrayList<TvResponse.Tv> tvs = tvResponse.results;
                            tvArrayList.clear();
                            tvArrayList.addAll(tvs);
                            adapter.notifyDataSetChanged();
                            constraintLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<TvResponse> call, Throwable t) {
                            Log.d("ResponseViewALLActivity", t.getMessage());
                            Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();
                        }
                    });
                    break;
                case Constants.TOP_RATED_VALUE:
                    toolbarTitle.setText("Top Rated Shows");
                    Call<TvResponse> callTopRated = movieAPI.getTvTopRated();
                    callTopRated.enqueue(new Callback<TvResponse>() {
                        @Override
                        public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                            TvResponse tvResponse = response.body();
                            ArrayList<TvResponse.Tv> tvs = tvResponse.results;
                            tvArrayList.clear();
                            tvArrayList.addAll(tvs);
                            adapter.notifyDataSetChanged();
                            constraintLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<TvResponse> call, Throwable t) {
                            Log.d("ResponseViewALLActivity", t.getMessage());
                            Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();
                        }
                    });
                    break;
                default:
                    break;
            }
        }

    }

}
