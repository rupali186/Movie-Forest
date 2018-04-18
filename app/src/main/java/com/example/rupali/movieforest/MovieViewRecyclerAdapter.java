package com.example.rupali.movieforest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
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

    public MovieViewRecyclerAdapter(Context context, OnItemClickListener listener, ArrayList<Movie> movies) {
        this.context = context;
        this.listener = listener;
        this.movies = movies;
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
        Movie movie=movies.get(position);
        holder.title.setText(movie.title);
        Double rating=movie.vote_average/2;
        Drawable drawable = holder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#DAA520"), PorterDuff.Mode.SRC_ATOP);
        holder.ratingBar.setRating(rating.floatValue());
        Picasso.get().load(Constants.IMAGE_BASE_URL+"w342"+movie.poster_path).resize(340,500).into(holder.poster);
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
                listener.onToggleClicked(holder.getAdapterPosition(),view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    interface OnItemClickListener{
        void onItemClick(int position);
        void onToggleClicked(int position,View view);
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
