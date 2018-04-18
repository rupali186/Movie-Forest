package com.example.rupali.movieforest;

import android.content.Context;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by RUPALI on 21-03-2018.
 */

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    Context context;
    OnItemClickListener onClickListener;
    ArrayList<Movie> movies;

    public MovieRecyclerAdapter(Context context, OnItemClickListener onClickListener, ArrayList<Movie> movies) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=inflater.inflate(R.layout.movie_recycler_list_item,parent,false);
        MovieViewHolder movieViewHolder=new MovieViewHolder(itemView);
        return  movieViewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        Movie movie=movies.get(position);
        holder.movieName.setText(movie.title);
        Double rating=movie.vote_average/2;
        Drawable drawable = holder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#DAA520"), PorterDuff.Mode.SRC_ATOP);
        holder.ratingBar.setRating(rating.floatValue());
        Log.d("Rating",rating.floatValue()+" "+movie.title);
        Picasso.get().load(Constants.IMAGE_BASE_URL+"w500"+movie.poster_path).resize(410,600).into(holder.movieImage, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("Picasso","success");
            }

            @Override
            public void onError(Exception e) {
                Log.d("Picasso","failure: "+e.getMessage());

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onItemclick(holder.getAdapterPosition());
            }
        });
        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onFavoriteClicked(holder.getAdapterPosition(),view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{
        ImageView movieImage;
        TextView movieName;
        RatingBar ratingBar;
        ToggleButton toggleButton;
        View itemView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            movieImage=itemView.findViewById(R.id.movieImageView);
            movieName=itemView.findViewById(R.id.textView);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            toggleButton=itemView.findViewById(R.id.toggleButton);
        }
    }
    interface OnItemClickListener{
        void onItemclick(int position);
        void onFavoriteClicked(int position,View view);
    }
}
