package com.example.rupali.movieforest;


import android.app.SearchableInfo;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    ArrayList<Search.Result> searchResults;
    SearchAdapter searchAdapter;
    RecyclerView searchRecycler;
    String query=null;
    Bundle bundle;
    SwipeRefreshLayout swipeRefreshLayout;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        searchResults=new ArrayList<>();
        searchAdapter=new SearchAdapter(getContext(), searchResults, new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Search.Result result=searchResults.get(position);
                if(result.media_type.equalsIgnoreCase("tv")){
                    Intent intent=new Intent(getContext(),TvDetailActivity.class);
                    Bundle bundle1=new Bundle();
                    bundle1.putInt(Constants.TV_ID,result.id);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }
                else if(result.media_type.equalsIgnoreCase("movie")){
                    Intent intent=new Intent(getContext(),MovieItemActivity.class);
                    Bundle bundle1=new Bundle();
                    bundle1.putInt(Constants.MOVIE_ID,result.id);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }
                else if(result.media_type.equalsIgnoreCase("person")){
                    Intent intent=new Intent(getContext(),CelebsDetailActivity.class);
                    Bundle bundle1=new Bundle();
                    bundle1.putInt(Constants.CELEB_ID,result.id);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }

            }
        });
        swipeRefreshLayout=view.findViewById(R.id.searchSwipeRefresh);
        searchRecycler=view.findViewById(R.id.searchRecycler);
        searchRecycler.setAdapter(searchAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        bundle=getArguments();
        if(bundle!=null){
            query=bundle.getString(Constants.SEARCH_QUERY);
            swipeRefreshLayout.setRefreshing(true);
            Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.TMDB_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            MovieAPI movieAPI=retrofit.create(MovieAPI.class);
            Call<Search> searchCall=movieAPI.search(query);
            searchCall.enqueue(new Callback<Search>() {
                @Override
                public void onResponse(Call<Search> call, Response<Search> response) {
                    Log.d("SearchResponse",response.message());
                    Search search=response.body();
                    ArrayList<Search.Result> searchArrayList=search.results;
                    if(searchResults!=null){
                        searchResults.clear();
                        searchResults.addAll(searchArrayList);
                        searchAdapter.notifyDataSetChanged();
                    }
                    else{
                        Snackbar.make(searchRecycler,"No match Found",Snackbar.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<Search> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(searchRecycler,"Network error",Snackbar.LENGTH_SHORT).show();
                }
            });
        }
        return  view;
    }

}
