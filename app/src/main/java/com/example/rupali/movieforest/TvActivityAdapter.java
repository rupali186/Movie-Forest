package com.example.rupali.movieforest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
 * Created by RUPALI on 11-04-2018.
 */

public class TvActivityAdapter extends RecyclerView.Adapter<TvActivityAdapter.Viewholder>{
    Context context;
    ArrayList<TvResponse.Tv> tvArrayList;
    OnItemClickListener listener;
    FavOpenHelper openHelper;
    ArrayList<SearchResults> results;
    SQLiteDatabase database;

    public TvActivityAdapter(Context context, ArrayList<TvResponse.Tv> tvArrayList, OnItemClickListener listener) {
        this.context = context;
        this.tvArrayList = tvArrayList;
        this.listener = listener;
        openHelper=FavOpenHelper.getInstance(context);
        results=new ArrayList<>();
        database=openHelper.getReadableDatabase();
//        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
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

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=inflater.inflate(R.layout.movie_recycler_list_item,parent,false);
        Viewholder viewholder=new Viewholder(itemView);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        Log.d("tvAdapter","onBind");

        final TvResponse.Tv tv=tvArrayList.get(position);
//        for (int i=0;i<results.size();i++){
//            SearchResults result=results.get(i);
//            if(result.id==tv.id){
//                Log.d("Resultid ",result.id+"");
//                holder.toggleButton.setChecked(true);
//                holder.toggleButton.setVisibility(View.VISIBLE);
//            }
//
//        }
        holder.name.setText(tv.name);
        Double rating=tv.vote_average/2;
        Drawable drawable = holder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#DAA520"), PorterDuff.Mode.SRC_ATOP);
        holder.ratingBar.setRating(rating.floatValue());
        Picasso.get().load(Constants.IMAGE_BASE_URL+"w185/"+tv.poster_path).resize(410,600).into(holder.poster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onToggleClicked(holder.getAdapterPosition(),view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvArrayList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        View itemView;
        TextView name;
        ImageView poster;
        RatingBar ratingBar;
        ToggleButton toggleButton;

        public Viewholder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            name=itemView.findViewById(R.id.textView);
            poster=itemView.findViewById(R.id.movieImageView);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            toggleButton=itemView.findViewById(R.id.toggleButton);
        }
    }
    interface OnItemClickListener{
        void onItemClick(int position);
        void onToggleClicked(int position,View view);
    }
}
