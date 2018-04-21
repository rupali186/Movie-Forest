package com.example.rupali.movieforest;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvDetailActivity extends AppCompatActivity {
//    YouTubePlayerSupportFragment frag;
    ImageView poster;
    TextView title;
    TextView voteAverage;
    TextView overview;
    ImageView expandOverview;
    RecyclerView reviewRecycler;
    ReviewRecyclerAdapter reviewAdapter;
    ArrayList<Reviews.Result> reviewsArrayList;
    TextView viewAllReview;
    RecyclerView castRecycler;
    CastRecyclerAdapter castRecyclerAdapter;
    ArrayList<Credits.Cast> castArrayList;
    TextView firstAirDate;
    TextView languages;
    TextView genres;
    RecyclerView similarRecycler;
    TvActivityAdapter similarAdapter;
    ArrayList<TvResponse.Tv> similarArrayList;
    Bundle bundle;
    boolean isExpanded;
    String videoKey;
    ProgressBar progressBar;
    NestedScrollView nestedScrollView;
    TextView toolbarTitle;
    FavOpenHelper openHelper;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    boolean isFullscreen=false;
    YouTubePlayer youTubePlayer=null;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=findViewById(R.id.tv_detail_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Tv Shows");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        poster=findViewById(R.id.showPoster);
        openHelper=FavOpenHelper.getInstance(this);
        title=findViewById(R.id.showTitle);
        voteAverage=findViewById(R.id.showVoteAverage);
        overview=findViewById(R.id.showOverviewContent);
        expandOverview=findViewById(R.id.showOverviwExpand);
        expandOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isExpanded){
                    overview.setMaxLines(Integer.MAX_VALUE);
                    expandOverview.setBackgroundResource(R.drawable.ic_collapse);
                    isExpanded=true;
                }
                else{
                    overview.setMaxLines(3);
                    expandOverview.setBackgroundResource(R.drawable.ic_expand);
                    isExpanded=false;
                }
            }
        });

        reviewRecycler=findViewById(R.id.showreviewRecyclr);
        reviewsArrayList=new ArrayList<>();
        reviewAdapter=new ReviewRecyclerAdapter(this, reviewsArrayList, new ReviewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemCilck(int position) {

            }
        });
        reviewRecycler.setAdapter(reviewAdapter);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        viewAllReview=findViewById(R.id.showReviewViewAll);
        viewAllReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bundle!=null) {
                    Intent intent = new Intent(TvDetailActivity.this, ReviewActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(Constants.TV_ID, bundle.getInt(Constants.TV_ID));
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }
            }
        });
        castRecycler=findViewById(R.id.showCastRecycler);
        castArrayList=new ArrayList<>();
        castRecyclerAdapter=new CastRecyclerAdapter(this, castArrayList, new CastRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(TvDetailActivity.this,CelebsDetailActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.CELEB_ID,castArrayList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        castRecycler.setAdapter(castRecyclerAdapter);
        castRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        firstAirDate=findViewById(R.id.showFirstAirDate);
        languages=findViewById(R.id.showLanguages);
        genres=findViewById(R.id.showGenres);
        similarRecycler=findViewById(R.id.showSimilarRecycler);
        similarArrayList=new ArrayList<>();
        similarAdapter=new TvActivityAdapter(this, similarArrayList, new TvActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(TvDetailActivity.this,TvDetailActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.TV_ID,similarArrayList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }

            @Override
            public void onToggleClicked(int position,View view) {
                SQLiteDatabase database=openHelper.getWritableDatabase();
                ToggleButton toggleButton =(ToggleButton)view;
                TvResponse.Tv tv=similarArrayList.get(position);
                String []selectionArgs={tv.id+"",Constants.TV_MEDIA_TYPE};
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
                    contentValues.put(Contract.FavTable.ID,tv.id);
                    contentValues.put(Contract.FavTable.IS_TOGGLED,"true");
                    contentValues.put(Contract.FavTable.MEDIA_TYPE,Constants.TV_MEDIA_TYPE);
                    contentValues.put(Contract.FavTable.POPULARITY,tv.popularity);
                    contentValues.put(Contract.FavTable.POSTER_PATH,tv.poster_path);
                    contentValues.put(Contract.FavTable.TITLE,tv.name);
                    database.insert(Contract.FavTable.TABLE_NAME,null,contentValues);
                }

            }
        });
        similarRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        similarRecycler.setAdapter(similarAdapter);
        progressBar=findViewById(R.id.tvDetailProgressBsr);
        nestedScrollView=findViewById(R.id.contentTvDetail);
//        frag=(YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.showTrailer);
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.showTrailer,youTubePlayerFragment).commit();
        Intent intent=getIntent();
        bundle=intent.getExtras();
        if(bundle!=null){
            fetchDataFromBundle();
        }
    }

    private void fetchDataFromBundle() {
        nestedScrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        if(bundle.containsKey(Constants.TV_ID)){
            int tvId=bundle.getInt(Constants.TV_ID);
            Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.TMDB_BASE_URL).build();
            MovieAPI movieAPI=retrofit.create(MovieAPI.class);
            Call<TvResponse.Tv> call=movieAPI.getTvDetails(tvId);
            call.enqueue(new Callback<TvResponse.Tv>() {
                @Override
                public void onResponse(Call<TvResponse.Tv> call, Response<TvResponse.Tv> response) {
                    TvResponse.Tv tv=response.body();
                    toolbarTitle.setText(tv.name);
                    Picasso.get().load(Constants.IMAGE_BASE_URL+"w185/"+tv.poster_path).resize(340,500).into(poster);
                    if(tv.videos.results.size()!=0) {
                        videoKey = tv.videos.results.get(0).key;
                        youTubePlayerFragment.initialize(AppConfig.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {
                                if (!wasRestored) {
                                    if(videoKey!=null) {
                                        youTubePlayer=player;
                                        player.cueVideo(videoKey);
                                        player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                            @Override
                                            public void onFullscreen(boolean b) {
                                                isFullscreen=b;
                                            }
                                        });
                                        player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                                            @Override
                                            public void onPlaying() {

                                            }

                                            @Override
                                            public void onPaused() {

                                            }

                                            @Override
                                            public void onStopped() {

                                            }

                                            @Override
                                            public void onBuffering(boolean b) {
                                                player.setFullscreen(true);
                                            }

                                            @Override
                                            public void onSeekTo(int i) {

                                            }
                                        });


                                    }
                                }
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                                String errorMessage = error.toString();
                                Toast.makeText(TvDetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                Log.d("errorMessage:", errorMessage);

                            }
                        });

                    }
                    else{
                    }
                    title.setText(tv.name);
                    voteAverage.setText(tv.vote_average+"");
                    overview.setText(tv.overview);
                    ArrayList<Reviews.Result> results=tv.reviews.results;
                    if(results!=null){
                        reviewsArrayList.clear();;
                        reviewsArrayList.addAll(results);
                        reviewAdapter.notifyDataSetChanged();
                    }
                    ArrayList<Credits.Cast> casts=tv.credits.cast;
                    if(casts!=null){
                        castArrayList.clear();
                        for (int i=0;i<casts.size();i++){
                            if(casts.get(i).profile_path!=null){
                                castArrayList.add(casts.get(i));
                            }
                        }
                        castRecyclerAdapter.notifyDataSetChanged();
                    }
                    firstAirDate.setText("First Air Date: "+tv.first_air_date);
                    ArrayList<String> langs=tv.languages;
                    if(langs.size()!=0){
                       languages.setText("Languages: ");
                       int i=0;
                       for(i=0;i<langs.size()-1;i++){
                           languages.setText(languages.getText()+langs.get(i)+", ");
                       }
                       languages.setText(languages.getText()+langs.get(i));
                    }
                    ArrayList<TvResponse.Tv> similars=tv.similar.results;
                    if(similars.size()!=0){
                        similarArrayList.clear();
                        similarArrayList.addAll(similars);
                        similarAdapter.notifyDataSetChanged();
                    }
                    ArrayList<TvResponse.Tv.Genres> genresArrayList=tv.genres;
                    if(genresArrayList.size()!=0){
                        genres.setText("Genres: ");
                        int i=0;
                        for(i=0;i<genresArrayList.size()-1;i++){
                            genres.setText(genres.getText()+genresArrayList.get(i).name+", ");
                        }
                        genres.setText(genres.getText()+genresArrayList.get(i).name);
                    }
                    nestedScrollView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<TvResponse.Tv> call, Throwable t) {
                    Snackbar.make(nestedScrollView,"Network Error",Snackbar.LENGTH_LONG).show();
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        if(youTubePlayer!=null&&isFullscreen){
            youTubePlayer.setFullscreen(false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isFullscreen=false;

        }
        else {
            super.onBackPressed();
        }
    }
}
