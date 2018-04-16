package com.example.rupali.movieforest;

import android.content.Intent;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GenreRecyclerAdapter recyclerAdapter;
    ArrayList<Genre> genreArrayList;
    Bundle bundle;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;
    TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=findViewById(R.id.genre_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Genres");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView=findViewById(R.id.genreActivityRecyclerView);
        genreArrayList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerAdapter=new GenreRecyclerAdapter(this, genreArrayList, new GenreRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(GenreActivity.this,MovieViewActivity.class);
                Bundle bundle1=new Bundle();
                int id=genreArrayList.get(position).id;
                String name=genreArrayList.get(position).name;
                bundle1.putString(Constants.GENRE_NAME,name);
                bundle1.putInt(Constants.GENRE_ID,id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        constraintLayout=findViewById(R.id.contentGenre);
        progressBar=findViewById(R.id.genreActivityProgressBsr);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        if(bundle!=null){
            fetchDataFromBundle();
        }

    }

    private void fetchDataFromBundle() {
        constraintLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        int id=bundle.getInt(Constants.VIEW_ALL_ID);
        if(id==Constants.POPULAR_GENRES_VALUE){
            Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.TMDB_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            MovieAPI movieAPI=retrofit.create(MovieAPI.class);
            Call<GenreResponse> call=movieAPI.getGenres();
            call.enqueue(new Callback<GenreResponse>() {
                @Override
                public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                    GenreResponse genreResponse=response.body();
                    ArrayList<Genre> genres=genreResponse.genres;
                    genreArrayList.clear();
                    genreArrayList.addAll(genres);
                    recyclerAdapter.notifyDataSetChanged();
                    constraintLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<GenreResponse> call, Throwable t) {
                    Log.d("ResponseGenre",t.getMessage());
                    Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

}
