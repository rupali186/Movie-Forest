package com.example.rupali.movieforest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CelebsDetailActivity extends AppCompatActivity {
    Bundle bundle;
    TextView celebName;
    TextView birthday;
    TextView birthPlace;
    TextView popularity;
    ImageView celebImage;
    RecyclerView movieCastRecycler;
    RecyclerView tvCastRecycler;
    MovieRecyclerAdapter movieRecyclerAdapter;
    ArrayList<Movie> movieCastArrayList;
    TextView biography;
    ImageView expand;
    ArrayList<TvResponse.Tv> tvCastArrayList;
    TvActivityAdapter tvCastAdapter;
    boolean isExpanded=false;
    ProgressBar progressBar;
    NestedScrollView nestedScrollView;
    TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebs_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle=findViewById(R.id.celebs_detail_toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Celebs");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        movieCastArrayList=new ArrayList<>();
        tvCastArrayList=new ArrayList<>();
        celebName=findViewById(R.id.celeb_name);
        birthday=findViewById(R.id.celeb_birthday_detail);
        birthPlace=findViewById(R.id.celeb_birthplace_detail);
        popularity=findViewById(R.id.celeb_popularity);
        celebImage=findViewById(R.id.celeb_image);
        biography=findViewById(R.id.celeb_biography_detail);
        expand=findViewById(R.id.celeb_bio_expand);
        progressBar=findViewById(R.id.celebsDetailProgressBsr);
        nestedScrollView=findViewById(R.id.contentCelebdetail);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isExpanded){
                    biography.setMaxLines(Integer.MAX_VALUE);
                    expand.setBackgroundResource(R.drawable.ic_collapse);
                    isExpanded=true;
                }
                else{
                    biography.setMaxLines(3);
                    expand.setBackgroundResource(R.drawable.ic_expand);
                    isExpanded=false;
                }
            }
        });
        movieCastRecycler=findViewById(R.id.celeb_movie_cast_recycler);
        tvCastRecycler=findViewById(R.id.celeb_tv_recycler);
        movieRecyclerAdapter=new MovieRecyclerAdapter(this, new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position) {
                Intent intent=new Intent(CelebsDetailActivity.this,MovieItemActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.MOVIE_ID,movieCastArrayList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        },movieCastArrayList);
        movieCastRecycler.setAdapter(movieRecyclerAdapter);
        movieCastRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        tvCastAdapter=new TvActivityAdapter(this, tvCastArrayList, new TvActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(CelebsDetailActivity.this,TvDetailActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.TV_ID,tvCastArrayList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        tvCastRecycler.setAdapter(tvCastAdapter);
        tvCastRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        Intent intent=getIntent();
        bundle=intent.getExtras();
        if(bundle!=null){
            fetchDataFromBundle();
        }
    }

    private void fetchDataFromBundle() {
        nestedScrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        if(bundle.containsKey(Constants.CELEB_ID)) {
            int celebId = bundle.getInt(Constants.CELEB_ID);
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.TMDB_BASE_URL).build();
            MovieAPI movieAPI = retrofit.create(MovieAPI.class);
            Call<CelebResponse.Celeb> call = movieAPI.getCelebDetail(celebId);
            call.enqueue(new Callback<CelebResponse.Celeb>() {
                @Override
                public void onResponse(Call<CelebResponse.Celeb> call, Response<CelebResponse.Celeb> response) {
                    CelebResponse.Celeb celeb = response.body();
                    toolbarTitle.setText(celeb.name);
                    if(celeb.profile_path!=null) {
                        Picasso.get().load(Constants.IMAGE_BASE_URL + "w185/" + celeb.profile_path).resize(380, 500).into(celebImage);
                    }
                    else{
                        celebImage.setBackgroundResource(R.drawable.no_image_availaible);
                    }
                    celebName.setText(celeb.name);
                    birthday.setText(celeb.birthday);
                    //birthPlace.setText(celeb.place_of_birth);
                    String birth_place=celeb.place_of_birth;
                    if(birth_place!=null) {
                        int length = 0;
                        for (int i = 0; i < birth_place.length(); i++) {
                            if (birth_place.charAt(i) == ',') {
                                break;
                            }
                            length++;
                        }
                        birthPlace.setText(birth_place.substring(0, length));
                    }
                    else {
                        birthPlace.setText("");
                    }

                    DecimalFormat df = new DecimalFormat("#.00");
                    Double popu = celeb.popularity;
                    String angleFormatted = df.format(popu);
                    popularity.setText(angleFormatted);
                    ArrayList<Movie> movies = celeb.movie_credits.cast;
                    if (movies != null) {
                        movieCastArrayList.clear();
                        movieCastArrayList.addAll(movies);
                        movieRecyclerAdapter.notifyDataSetChanged();
                    }
                    ArrayList<TvResponse.Tv> tvArrayList=celeb.tv_credits.cast;
                    if(tvArrayList!=null){
                        tvCastArrayList.clear();
                        for(int i=0;i<tvArrayList.size();i++){
                            if(tvArrayList.get(i).poster_path!=null){
                                tvCastArrayList.add(tvArrayList.get(i));
                            }
                        }
                        tvCastAdapter.notifyDataSetChanged();
                    }
                    biography.setText(celeb.biography);
                    nestedScrollView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<CelebResponse.Celeb> call, Throwable t) {

                    Snackbar.make(nestedScrollView,"Network Error",Snackbar.LENGTH_LONG).show();
                }
            });

        }
    }

}
