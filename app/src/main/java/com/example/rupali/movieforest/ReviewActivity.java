package com.example.rupali.movieforest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Reviews.Result> reviewArrayList;
    ReviewRecyclerAdapter reviewRecyclerAdapter;
    Bundle bundle;
    TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=findViewById(R.id.review_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Reviews");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView=findViewById(R.id.reviRecycler);
        reviewArrayList=new ArrayList<>();
        reviewRecyclerAdapter=new ReviewRecyclerAdapter(this, reviewArrayList, new ReviewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemCilck(int position) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(reviewRecyclerAdapter);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        if(bundle!=null){
            fetchDataFromBundle();
        }
    }

    private void fetchDataFromBundle() {
        if(bundle.containsKey(Constants.MOVIE_ID)){
            int movieId=bundle.getInt(Constants.MOVIE_ID);
            Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://api.themoviedb.org/3/").build();
            MovieAPI movieAPI=retrofit.create(MovieAPI.class);
            String movieIdString=Integer.toString(movieId);
            Call<Movie> call=movieAPI.getMovieDetails(movieIdString);
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    Movie movie=response.body();
                    ArrayList<Reviews.Result> results=movie.reviews.results;
                    reviewArrayList.clear();
                    reviewArrayList.addAll(results);
                    reviewRecyclerAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Log.d("Response",t.getMessage());

                }
            });
        }
        else if(bundle.containsKey(Constants.TV_ID)){
            int tvId=bundle.getInt(Constants.TV_ID);
            Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://api.themoviedb.org/3/").build();
            MovieAPI movieAPI=retrofit.create(MovieAPI.class);
            Call<TvResponse.Tv> tvCall=movieAPI.getTvDetails(tvId);
            tvCall.enqueue(new Callback<TvResponse.Tv>() {
                @Override
                public void onResponse(Call<TvResponse.Tv> call, Response<TvResponse.Tv> response) {
                    TvResponse.Tv tv=response.body();
                    ArrayList<Reviews.Result> results=tv.reviews.results;
                    reviewArrayList.clear();
                    reviewArrayList.addAll(results);
                    reviewRecyclerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<TvResponse.Tv> call, Throwable t) {
                    Log.d("Response",t.getMessage());
                }
            });
        }
    }

}
