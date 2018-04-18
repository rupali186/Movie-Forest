package com.example.rupali.movieforest;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {
    ArrayList<SearchResults> results=new ArrayList<>();
    SearchAdapter adapter;
    RecyclerView recyclerView;
    FavOpenHelper openHelper;
    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerView=view.findViewById(R.id.favouriteRecycler);
        openHelper=FavOpenHelper.getInstance(getContext());
        adapter=new SearchAdapter(getContext(), results, new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onToggleClick(int position, View view) {
                SearchResults result=results.get(position);
                ToggleButton toggleButton=(ToggleButton)view;
                toggleButton.setChecked(false);
                SQLiteDatabase database=openHelper.getWritableDatabase();
                String []selectionArgs={result.id+"",result.media_type};
                database.delete(Contract.FavTable.TABLE_NAME,Contract.FavTable.ID+" =? AND "+
                        Contract.FavTable.MEDIA_TYPE+" =? ",selectionArgs);
                results.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        fetchDataFromDatabase();
        return  view;
    }

    public void fetchDataFromDatabase() {
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.FavTable.TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String title=cursor.getString(cursor.getColumnIndex(Contract.FavTable.TITLE));
            int id=cursor.getInt(cursor.getColumnIndex(Contract.FavTable.ID));
            String mediaType=cursor.getString(cursor.getColumnIndex(Contract.FavTable.MEDIA_TYPE));
            Double popularity=cursor.getDouble(cursor.getColumnIndex(Contract.FavTable.POPULARITY));
            String posterPath=cursor.getString(cursor.getColumnIndex(Contract.FavTable.POSTER_PATH));
            String isToggled=cursor.getString(cursor.getColumnIndex(Contract.FavTable.IS_TOGGLED));
            SearchResults result=new SearchResults(id,isToggled,mediaType,title,popularity,posterPath,title);
            results.add(result);
        }
        adapter.notifyDataSetChanged();
    }
}
