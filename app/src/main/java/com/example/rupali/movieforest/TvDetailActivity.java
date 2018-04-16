package com.example.rupali.movieforest;

import android.content.Intent;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvDetailActivity extends AppCompatActivity {
    ImageView trailerView;
    ImageView poster;
    ImageButton play;
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
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=findViewById(R.id.tv_detail_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Tv Shows");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        trailerView=findViewById(R.id.showTrailer);
        poster=findViewById(R.id.showPoster);
        play=findViewById(R.id.show_play_button);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoKey.length()!=0) {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_BASE_URL+videoKey));
                    startActivity(intent);
                }
            }
        });
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

            }
        });
        similarRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        similarRecycler.setAdapter(similarAdapter);
        progressBar=findViewById(R.id.tvDetailProgressBsr);
        nestedScrollView=findViewById(R.id.contentTvDetail);
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
                    Picasso.get().load(Constants.IMAGE_BASE_URL+"w185/"+tv.poster_path).resize(325,500).into(poster);
                    if(tv.videos.results.size()!=0) {
                        videoKey = tv.videos.results.get(0).key;
                        Picasso.get().load("http://img.youtube.com/vi/" + videoKey + "/mqdefault.jpg").resize(2000, 900).into(trailerView);
                    }
                    else{
                        trailerView.setBackgroundResource(R.drawable.no_video_availaible);
                        play.setVisibility(View.GONE);
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
                        castArrayList.addAll(casts);
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

}
