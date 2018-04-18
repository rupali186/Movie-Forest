package com.example.rupali.movieforest;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView nowShowingRecyclerView;
    RecyclerView popularRecyclerView;
    RecyclerView upcomingRecyclerView;
    RecyclerView topRatedRecyclerView;
    RecyclerView genreRecyclerView;
    MovieRecyclerAdapter nowShowingAdapter;
    MovieRecyclerAdapter popularAdapter;
    MovieRecyclerAdapter upcomingAdapter;
    MovieRecyclerAdapter topRatedAdapter;
    GenreRecyclerAdapter genreAdapter;
    ArrayList<Movie> nowShowingList;
    ArrayList<Movie> popularList;
    ArrayList<Movie> upcomingList;
    ArrayList<Movie> topRatedList;
    ArrayList<Genre> genreList;
    TextView nowShowingViewAll;
    TextView popularViewAll;
    TextView upcomingViewAll;
    TextView topRatedViewAll;
    TextView popularGenresViewAll;
//    ProgressBar progressBar;
    NestedScrollView nestedScrollView;
    SwipeRefreshLayout swipeRefreshLayout;
    FavOpenHelper openHelper;
//    ClickCallback mCallback;
//    public interface ClickCallback{
//        void onInCinemasMovieClick(int position);
//        void onUpcomingMovieClick(int position);
//        void onPopularMovieClick(int position);
//        void onTopRatedMovieClick(int position);
//        void onGenreItemClick(int position);
//    }
    public MoviesFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mCallback=(ClickCallback)context;
//        }catch (ClassCastException e){
//            throw new ClassCastException("Activity should implement ClickCallback");
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_movies, container, false);
        nowShowingList=new ArrayList<Movie>();
        popularList=new ArrayList<>();
        upcomingList=new ArrayList<>();
        topRatedList=new ArrayList<>();
        genreList=new ArrayList<>();
        nowShowingRecyclerView=view.findViewById(R.id.nowShowingRecycler);
        upcomingRecyclerView=view.findViewById(R.id.upcomingRecyclerView);
        popularRecyclerView=view.findViewById(R.id.popularRecyclerView);
        topRatedRecyclerView=view.findViewById(R.id.topRatedRecyclerView);
        genreRecyclerView=view.findViewById(R.id.popularGenresRecyclerView);
        openHelper=FavOpenHelper.getInstance(getContext());
        nowShowingAdapter=new MovieRecyclerAdapter(getContext(), new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position) {
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.MOVIE_ID,nowShowingList.get(position).id);
                Intent intent=new Intent(getContext(),MovieItemActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }

            @Override
            public void onFavoriteClicked(int position,View view) {
                ToggleButton toggleButton =(ToggleButton)view;
                SQLiteDatabase database=openHelper.getWritableDatabase();
                String []selectionArgs={nowShowingList.get(position).id+"",Constants.MOVIE_MEDIA_TYPE};
                Cursor cursor=database.query(Contract.FavTable.TABLE_NAME,null,Contract.FavTable.ID+" =? AND "+
                        Contract.FavTable.MEDIA_TYPE+" =? ",selectionArgs,null,null,null);
                if(cursor.moveToFirst()){
                    toggleButton.setChecked(false);
                    database.delete(Contract.FavTable.TABLE_NAME,Contract.FavTable.ID+" =? AND "+
                            Contract.FavTable.MEDIA_TYPE+" =? ",selectionArgs);
                }
                else {
                    toggleButton.setChecked(true);
                    Movie movie1=nowShowingList.get(position);
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
        },nowShowingList);
        popularAdapter=new MovieRecyclerAdapter(getContext(), new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position) {
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.MOVIE_ID,popularList.get(position).id);
                Intent intent=new Intent(getContext(),MovieItemActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }

            @Override
            public void onFavoriteClicked(int position,View view) {
                ToggleButton toggleButton =(ToggleButton)view;
                Movie movie1=popularList.get(position);
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
        }, popularList);
        upcomingAdapter=new MovieRecyclerAdapter(getContext(), new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position) {
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.MOVIE_ID,upcomingList.get(position).id);
                Intent intent=new Intent(getContext(),MovieItemActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);            }

            @Override
            public void onFavoriteClicked(int position,View view) {
                ToggleButton toggleButton =(ToggleButton)view;
                Movie movie1=upcomingList.get(position);
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
        }, upcomingList);
        topRatedAdapter=new MovieRecyclerAdapter(getContext(), new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position) {
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.MOVIE_ID,topRatedList.get(position).id);
                Intent intent=new Intent(getContext(),MovieItemActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);            }

            @Override
            public void onFavoriteClicked(int position,View view) {
                ToggleButton toggleButton =(ToggleButton)view;
                Movie movie1=topRatedList.get(position);
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
        },topRatedList);
        genreAdapter=new GenreRecyclerAdapter(getContext(), genreList, new GenreRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getContext(),MovieViewActivity.class);
                Bundle bundle1=new Bundle();
                int id=genreList.get(position).id;
                bundle1.putInt(Constants.GENRE_ID,id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        }) ;
        nowShowingRecyclerView.setAdapter(nowShowingAdapter);
        nowShowingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        upcomingRecyclerView.setAdapter(upcomingAdapter);
        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        popularRecyclerView.setAdapter(popularAdapter);
        popularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        topRatedRecyclerView.setAdapter(topRatedAdapter);
        topRatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        genreRecyclerView.setAdapter(genreAdapter);
        genreRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,LinearLayoutManager.HORIZONTAL,false));
        nestedScrollView=view.findViewById(R.id.contentmovieAct);
//        progressBar=view.findViewById(R.id.movieActivityProgressBar);
        nowShowingViewAll=view.findViewById(R.id.viewAllNST);
        nowShowingViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MovieViewActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Constants.VIEW_ALL_ID,Constants.NOW_PLAYING_VALUE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        popularViewAll=view.findViewById(R.id.viewALLPTV);
        popularViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MovieViewActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Constants.VIEW_ALL_ID,Constants.POPULAR_VALUE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        upcomingViewAll=view.findViewById(R.id.viewAllUTV);
        upcomingViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MovieViewActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Constants.VIEW_ALL_ID,Constants.UPCOMING_VALUE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        topRatedViewAll=view.findViewById(R.id.viewAllTopRated);
        topRatedViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MovieViewActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Constants.VIEW_ALL_ID,Constants.TOP_RATED_VALUE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        popularGenresViewAll=view.findViewById(R.id.viewAllGenre);
        popularGenresViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),GenreActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Constants.VIEW_ALL_ID,Constants.POPULAR_GENRES_VALUE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        swipeRefreshLayout=view.findViewById(R.id.movies_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        fetchData();
        return view;
    }

    private void fetchData() {
        nestedScrollView.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.TMDB_BASE_URL).build();
        MovieAPI movieAPI=retrofit.create(MovieAPI.class);
        Call<MovieResponse> nowPlayingCall=movieAPI.getNowPlaying();
        Call<MovieResponse> popularCall=movieAPI.getPopularMovies();
        Call<MovieResponse> upcomingCall=movieAPI.getUpcoming();
        Call<MovieResponse> topRatedCall=movieAPI.getTopRated();
        Call<GenreResponse> genreCall=movieAPI.getGenres();
        nowPlayingCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> movieArrayList=movieResponse.results;
                nowShowingList.clear();
                nowShowingList.addAll(movieArrayList);
                nowShowingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("NetworkResponse",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(nestedScrollView,"Network Error",Snackbar.LENGTH_LONG).show();

            }
        });
        popularCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> movieArrayList=movieResponse.results;
                popularList.clear();
                popularList.addAll(movieArrayList);
                popularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("NetworkResponse",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(nestedScrollView,"Network Error",Snackbar.LENGTH_LONG).show();
            }
        });
        upcomingCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> movieArrayList=movieResponse.results;
                upcomingList.clear();
                upcomingList.addAll(movieArrayList);
                upcomingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("NetworkResponse",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(nestedScrollView,"Network Error",Snackbar.LENGTH_LONG).show();

            }
        });

        topRatedCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> movieArrayList=movieResponse.results;
                topRatedList.clear();
                topRatedList.addAll(movieArrayList);
                topRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("NetworkResponse",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(nestedScrollView,"Network Error",Snackbar.LENGTH_LONG).show();
            }

        });
        //                youTubeThumbnailView.initialize(AppConfig.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
//                    @Override
//                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
//                        youTubeThumbnailLoader.setVideo(videoKey);
//                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//                            @Override
//                            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//                                youTubeThumbnailLoader.release();
//                            }
//
//                            @Override
//                            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//                                Log.d("Youtube Player",errorReason.name());
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//                        Log.d("Youtube Player Failure",youTubeInitializationResult.name());
//                    }
//                });
        //videoKey=movie.videos.results.get(0).key;
        //Picasso.get().load("https://img.youtube.com/vi/"+videoKey+"/maxresdefault.jpg").resize(2000,800).into(youTubeThumbnailView);
        genreCall.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                GenreResponse genreResponse=response.body();
                ArrayList<Genre> genreArrayList=genreResponse.genres;
                genreList.clear();
                genreList.addAll(genreArrayList);
                genreList.remove(genreList.size()-1);
                genreAdapter.notifyDataSetChanged();
                nestedScrollView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                Log.d("NetworkResponse",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(nestedScrollView,"Network Error",Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
