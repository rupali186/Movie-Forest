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

public class MovieViewActivity extends AppCompatActivity {
    Bundle bundle;
    ArrayList<Movie> movieArrayList;
    RecyclerView recyclerView;
    MovieViewRecyclerAdapter recyclerAdapter;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;
    TextView toolbarTitle;
    FavOpenHelper openHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=findViewById(R.id.movie_view_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Movies");
        openHelper=FavOpenHelper.getInstance(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        movieArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.viewRecyclerView);
        recyclerAdapter=new MovieViewRecyclerAdapter(this, new MovieViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.MOVIE_ID,movieArrayList.get(position).id);
                Intent intent=new Intent(MovieViewActivity.this,MovieItemActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }

            @Override
            public void onToggleClicked(int position, View view) {
                ToggleButton toggleButton =(ToggleButton)view;
                Movie movie1=movieArrayList.get(position);
                SQLiteDatabase database=openHelper.getWritableDatabase();
                String []selectionArgs={movie1.id+"",Constants.MOVIE_MEDIA_TYPE};
                Cursor cursor=database.query(Contract.FavTable.TABLE_NAME,null,Contract.FavTable.ID+" =? AND "+
                        Contract.FavTable.MEDIA_TYPE+" =? ",selectionArgs,null,null,null);
                if(cursor.moveToFirst()){
                    toggleButton.setChecked(false);
                    database.delete(Contract.FavTable.TABLE_NAME,Contract.FavTable.ID+" =? AND "+
                            Contract.FavTable.MEDIA_TYPE+" =? ",selectionArgs);
                }
                else {
                    toggleButton.setChecked(true);
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(Contract.FavTable.ID,movie1.id);
                    contentValues.put(Contract.FavTable.IS_TOGGLED,"true");
                    contentValues.put(Contract.FavTable.MEDIA_TYPE,Constants.MOVIE_MEDIA_TYPE);
                    contentValues.put(Contract.FavTable.POPULARITY,movie1.popularity);
                    contentValues.put(Contract.FavTable.POSTER_PATH,movie1.poster_path);
                    contentValues.put(Contract.FavTable.TITLE,movie1.title);
                    database.insert(Contract.FavTable.TABLE_NAME,null,contentValues);
                }
            }
        }, movieArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
        constraintLayout=findViewById(R.id.contentMovieView);
        progressBar=findViewById(R.id.movieViewProgressBar);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        if(bundle!=null){
            fetchDataFromBundle();
        }
    }

    private void fetchDataFromBundle() {
        constraintLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://api.themoviedb.org/3/").build();
        MovieAPI movieAPI = retrofit.create(MovieAPI.class);
        if(bundle.containsKey(Constants.VIEW_ALL_ID)) {
            int viewAllid = bundle.getInt(Constants.VIEW_ALL_ID);
            switch (viewAllid) {
                case Constants.NOW_PLAYING_VALUE:
                    toolbarTitle.setText("In Cinemas");
                    Call<MovieResponse> callNowPlaying = movieAPI.getNowPlaying();
                    callNowPlaying.enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            MovieResponse movieResponse = response.body();
                            ArrayList<Movie> movies = movieResponse.results;
                            movieArrayList.clear();
                            movieArrayList.addAll(movies);
                            recyclerAdapter.notifyDataSetChanged();
                            constraintLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {
                            Log.d("ResponseViewALLActivity", t.getMessage());
                            Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();
                        }
                    });
                    break;
                case Constants.POPULAR_VALUE:
                    toolbarTitle.setText("Popular Movies");
                    Call<MovieResponse> callPopular = movieAPI.getPopularMovies();
                    callPopular.enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            MovieResponse movieResponse = response.body();
                            ArrayList<Movie> movies = movieResponse.results;
                            movieArrayList.clear();
                            movieArrayList.addAll(movies);
                            recyclerAdapter.notifyDataSetChanged();
                            constraintLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {
                            Log.d("ResponseViewALLActivity", t.getMessage());
                            Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();
                        }
                    });
                    break;
                case Constants.UPCOMING_VALUE:
                    toolbarTitle.setText("Upcoming Movies");
                    Call<MovieResponse> callUpcoming = movieAPI.getUpcoming();
                    callUpcoming.enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            MovieResponse movieResponse = response.body();
                            ArrayList<Movie> movies = movieResponse.results;
                            movieArrayList.clear();
                            movieArrayList.addAll(movies);
                            recyclerAdapter.notifyDataSetChanged();
                            constraintLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {
                            Log.d("ResponseViewALLActivity", t.getMessage());
                            Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();
                        }
                    });
                    break;
                case Constants.TOP_RATED_VALUE:
                    toolbarTitle.setText("Top Rated Movies");
                    Call<MovieResponse> callTopRated = movieAPI.getTopRated();
                    callTopRated.enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            MovieResponse movieResponse = response.body();
                            ArrayList<Movie> movies = movieResponse.results;
                            movieArrayList.clear();
                            movieArrayList.addAll(movies);
                            recyclerAdapter.notifyDataSetChanged();
                            constraintLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {
                            Log.d("ResponseViewALLActivity", t.getMessage());
                            Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();
                        }
                    });
                    break;
                default:
                    break;


            }
        }
        else if(bundle.containsKey(Constants.GENRE_ID)){
            toolbarTitle.setText("Genre");
            int id=bundle.getInt(Constants.GENRE_ID);
            if(bundle.containsKey(Constants.GENRE_NAME)) {
                String name = bundle.getString(Constants.GENRE_NAME);
                toolbarTitle.setText(name);
            }
            Call<MovieResponse> call=movieAPI.getMoviesByGenre(Integer.toString(id));
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    MovieResponse movieResponse=response.body();
                    ArrayList<Movie> movies=movieResponse.results;
                    movieArrayList.clear();
                    movieArrayList.addAll(movies);
                    recyclerAdapter.notifyDataSetChanged();
                    constraintLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.d("ResponseViewALLActivity", t.getMessage());
                    Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();
                }
            });
        }

    }

}
