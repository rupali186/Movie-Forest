package com.example.rupali.movieforest;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
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
public class TvFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView airingtodayRecycler;
    RecyclerView onTheAirRecycler;
    RecyclerView popularRecycler;
    RecyclerView topRatedRecycler;
    TextView airingTodayviewall;
    TextView onTheAirViewAll;
    TextView popularViewAll;
    TextView topRatedViewAll;
    TvActivityAdapter airingTodayAdapter;
    TvActivityAdapter onTheairAdapter;
    TvActivityAdapter popularAdapter;
    TvActivityAdapter topRatedAdapter;
    ArrayList<TvResponse.Tv> airingtodayList;
    ArrayList<TvResponse.Tv> onTheAirList;
    ArrayList<TvResponse.Tv> popularList;
    ArrayList<TvResponse.Tv> topRatedList;
    NestedScrollView nestedScrollView;
    SwipeRefreshLayout swipeRefreshLayout;
    FavOpenHelper openHelper;
    public TvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tv, container, false);
        openHelper=FavOpenHelper.getInstance(getContext());
        airingtodayList=new ArrayList<>();
        onTheAirList=new ArrayList<>();
        popularList=new ArrayList<>();
        topRatedList=new ArrayList<>();
        airingtodayRecycler=view.findViewById(R.id.airing_today_recycler);
        onTheAirRecycler=view.findViewById(R.id.tv_on_the_air_recycler);
        popularRecycler=view.findViewById(R.id.tv_popular_recycler);
        topRatedRecycler=view.findViewById(R.id.tv_top_rated_recycler);
        airingTodayviewall=view.findViewById(R.id.tv_airing_today_viewAll);
        onTheAirViewAll=view.findViewById(R.id.tv_on_the_air_vewAll);
        popularViewAll=view.findViewById(R.id.tv_popular_viewAll);
        topRatedViewAll=view.findViewById(R.id.tv_top_rated_viewAll);
        airingTodayviewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),TvViewAllActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Constants.VIEW_ALL_ID,Constants.AIRING_TODAY_VALUE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        onTheAirViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),TvViewAllActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Constants.VIEW_ALL_ID,Constants.ON_THE_AIR_VALUE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        topRatedViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),TvViewAllActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Constants.VIEW_ALL_ID,Constants.TOP_RATED_VALUE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        popularViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),TvViewAllActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Constants.VIEW_ALL_ID,Constants.POPULAR_VALUE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        airingTodayAdapter=new TvActivityAdapter(getContext(), airingtodayList, new TvActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getContext(),TvDetailActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.TV_ID,airingtodayList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }

            @Override
            public void onToggleClicked(int position, View view) {
                ToggleButton toggleButton =(ToggleButton)view;
                TvResponse.Tv tv=airingtodayList.get(position);
                SQLiteDatabase database=openHelper.getWritableDatabase();
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
        onTheairAdapter=new TvActivityAdapter(getContext(), onTheAirList, new TvActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getContext(),TvDetailActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.TV_ID,onTheAirList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }

            @Override
            public void onToggleClicked(int position, View view) {
                ToggleButton toggleButton =(ToggleButton)view;
                TvResponse.Tv tv=onTheAirList.get(position);
                SQLiteDatabase database=openHelper.getWritableDatabase();
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
        popularAdapter=new TvActivityAdapter(getContext(), popularList, new TvActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getContext(),TvDetailActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.TV_ID,popularList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }

            @Override
            public void onToggleClicked(int position, View view) {
                ToggleButton toggleButton =(ToggleButton)view;
                TvResponse.Tv tv=popularList.get(position);
                SQLiteDatabase database=openHelper.getWritableDatabase();
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
        topRatedAdapter=new TvActivityAdapter(getContext(), topRatedList, new TvActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getContext(),TvDetailActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt(Constants.TV_ID,topRatedList.get(position).id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }

            @Override
            public void onToggleClicked(int position, View view) {
                ToggleButton toggleButton =(ToggleButton)view;
                TvResponse.Tv tv=topRatedList.get(position);
                SQLiteDatabase database=openHelper.getWritableDatabase();
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
        airingtodayRecycler.setAdapter(airingTodayAdapter);
        airingtodayRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        onTheAirRecycler.setAdapter(onTheairAdapter);
        onTheAirRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        topRatedRecycler.setAdapter(topRatedAdapter);
        topRatedRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        popularRecycler.setAdapter(popularAdapter);
        popularRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        nestedScrollView=view.findViewById(R.id.contentTvAct);
        swipeRefreshLayout=view.findViewById(R.id.tv_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        fetchData();
        return view;
    }
    private void fetchData() {
        swipeRefreshLayout.setRefreshing(true);
        nestedScrollView.setVisibility(View.INVISIBLE);
        Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.TMDB_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        MovieAPI movieAPI=retrofit.create(MovieAPI.class);
        Call<TvResponse> airingTodayCall=movieAPI.getTvAirngToday();
        Call<TvResponse> onTheAirCall=movieAPI.getTvOnTheAir();
        Call<TvResponse> popularCall=movieAPI.getTvPopular();
        Call<TvResponse> topRatedCall=movieAPI.getTvTopRated();
        airingTodayCall.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                TvResponse tvResponse=response.body();
                ArrayList<TvResponse.Tv> arrayList=tvResponse.results;
                if(arrayList!=null){
                    airingtodayList.clear();
                    airingtodayList.addAll(arrayList);
                    airingTodayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.d("tv Response",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(nestedScrollView,"Network Error",Snackbar.LENGTH_LONG).show();
            }
        });
        onTheAirCall.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                TvResponse tvResponse=response.body();
                ArrayList<TvResponse.Tv> arrayList=tvResponse.results;
                if(arrayList!=null){
                    onTheAirList.clear();
                    onTheAirList.addAll(arrayList);
                    onTheairAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.d("tv Response",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(nestedScrollView,"Network Error",Snackbar.LENGTH_LONG).show();
            }
        });
        popularCall.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                TvResponse tvResponse=response.body();
                ArrayList<TvResponse.Tv> arrayList=tvResponse.results;
                if(arrayList!=null){
                    popularList.clear();
                    popularList.addAll(arrayList);
                    popularAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.d("tv Response",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(nestedScrollView,"Network Error",Snackbar.LENGTH_LONG).show();
            }
        });
        topRatedCall.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                TvResponse tvResponse=response.body();
                ArrayList<TvResponse.Tv> arrayList=tvResponse.results;
                if(arrayList!=null){
                    topRatedList.clear();
                    topRatedList.addAll(arrayList);
                    topRatedAdapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
                nestedScrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.d("tv Response",t.getMessage());
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
