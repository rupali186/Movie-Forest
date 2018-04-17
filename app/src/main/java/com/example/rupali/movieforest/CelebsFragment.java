package com.example.rupali.movieforest;


import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class CelebsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView popularRecycler;
    CelebAdapterVertical celebAdapter;
    ArrayList<CelebResponse.Celeb> celebArrayList;
    ConstraintLayout constraintLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    public CelebsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_celebs, container, false);
        celebArrayList=new ArrayList<>();
        popularRecycler=view.findViewById(R.id.popularcelebRecycler);
        celebAdapter=new CelebAdapterVertical(getContext(), celebArrayList, new CelebAdapterVertical.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getContext(),CelebsDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Constants.CELEB_ID,celebArrayList.get(position).id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        popularRecycler.setAdapter(celebAdapter);
        popularRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        popularRecycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        constraintLayout=view.findViewById(R.id.contentCeleb);
        swipeRefreshLayout=view.findViewById(R.id.celebs_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        fetchData();
        return view;
    }
    private void fetchData() {
        swipeRefreshLayout.setRefreshing(true);
        constraintLayout.setVisibility(View.INVISIBLE);;
        Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.TMDB_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        MovieAPI movieAPI=retrofit.create(MovieAPI.class);
        Call<CelebResponse> popularCelebResponse=movieAPI.getPopularCelebs();
        popularCelebResponse.enqueue(new Callback<CelebResponse>() {
            @Override
            public void onResponse(Call<CelebResponse> call, Response<CelebResponse> response) {
                CelebResponse celebs=response.body();
                celebArrayList.clear();
                ArrayList<CelebResponse.Celeb> responseArrayList=celebs.results;
                celebArrayList.addAll(responseArrayList);
                celebAdapter.notifyDataSetChanged();
                constraintLayout.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<CelebResponse> call, Throwable t) {
                Log.d("NetworkResponse",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(constraintLayout,"Network Error",Snackbar.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
