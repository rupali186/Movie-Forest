package com.example.rupali.movieforest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by RUPALI on 22-03-2018.
 */

public class MovieViewRecyclerAdapter extends RecyclerView.Adapter<MovieViewRecyclerAdapter.ViewHolder> {
    Context context;
    OnItemClickListener listener;
    ArrayList<Movie> movies;
    FavOpenHelper openHelper;
    SQLiteDatabase database;
    ArrayList<SearchResults> results;
    public MovieViewRecyclerAdapter(Context context, OnItemClickListener listener, ArrayList<Movie> movies) {
        this.context = context;
        this.listener = listener;
        this.movies = movies;
        openHelper=FavOpenHelper.getInstance(context);
        results=new ArrayList<>();
        database=openHelper.getWritableDatabase();
        fetchDataFromDataBase();
    }

    private void fetchDataFromDataBase() {
        Cursor cursor=database.query(Contract.FavTable.TABLE_NAME,null,null,null,null,null,null);
        results.clear();
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
        Log.d("Results",results.size()+"");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=inflater.inflate(R.layout.movie_view_recycler_item,parent,false);
        ViewHolder holder=new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Movie movie=movies.get(position);
//        for (int i=0;i<results.size();i++){
//            SearchResults result=results.get(i);
//            if(result.media_type.equalsIgnoreCase(Constants.MOVIE_MEDIA_TYPE)&&result.id==movie.id){
//                Log.d("Resultid ",result.id+"");
//                result.isToggled="true";
//                holder.toggleButton.setChecked(true);
//                holder.toggleButton.setVisibility(View.VISIBLE);
//            }
//
//        }
        holder.title.setText(movie.title);
        Double rating=movie.vote_average/2;
        Drawable drawable = holder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#DAA520"), PorterDuff.Mode.SRC_ATOP);
        holder.ratingBar.setRating(rating.floatValue());
        Picasso.get().load(Constants.IMAGE_BASE_URL+"w185"+movie.poster_path).resize(340,500).into(holder.poster);
//        ArrayList<Genre> genreArrayList=movie.genres;
//        for (int i=0;i<genreArrayList.size();i++){
//            String text=holder.genres.getText().toString();
//            if(text.length()>0){
//                holder.genres.setText(text+", "+genreArrayList.get(i).name);
//            }
//            else {
//                holder.genres.setText(genreArrayList.get(i).name);
//            }
//        }
        holder.genres.setText("Release Date: "+movie.release_date);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton toggleButton =(ToggleButton)view;
                String []selectionArgs={movie.id+"",Constants.MOVIE_MEDIA_TYPE};
                Cursor cursor=database.query(Contract.FavTable.TABLE_NAME,null,Contract.FavTable.ID+" =? AND "+
                        Contract.FavTable.MEDIA_TYPE+" =? ",selectionArgs,null,null,null);
                if(cursor.moveToFirst()){
                    toggleButton.setChecked(false);
                    database.delete(Contract.FavTable.TABLE_NAME,Contract.FavTable.ID+" =? AND "+
                            Contract.FavTable.MEDIA_TYPE+" =? ",selectionArgs);
                    fetchDataFromDataBase();
                }
                else {
                    toggleButton.setChecked(true);
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(Contract.FavTable.ID,movie.id);
                    contentValues.put(Contract.FavTable.IS_TOGGLED,"true");
                    contentValues.put(Contract.FavTable.MEDIA_TYPE,Constants.MOVIE_MEDIA_TYPE);
                    contentValues.put(Contract.FavTable.POPULARITY,movie.popularity);
                    contentValues.put(Contract.FavTable.POSTER_PATH,movie.poster_path);
                    contentValues.put(Contract.FavTable.TITLE,movie.title);
                    database.insert(Contract.FavTable.TABLE_NAME,null,contentValues);
                    fetchDataFromDataBase();
                }
                listener.onToggleClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    interface OnItemClickListener{
        void onItemClick(int position);
        void onToggleClicked(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView poster;
        TextView title;
        TextView genres;
        RatingBar ratingBar;
        View itemView;
        ToggleButton toggleButton;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            poster=itemView.findViewById(R.id.viewPosterImage);
            title=itemView.findViewById(R.id.viewTitle);
            genres=itemView.findViewById(R.id.viewGenres);
            ratingBar=itemView.findViewById(R.id.viewRatingBar);
            toggleButton=itemView.findViewById(R.id.movie_view_toggle);
        }
    }
}
