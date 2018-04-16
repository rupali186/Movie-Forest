package com.example.rupali.movieforest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by RUPALI on 11-04-2018.
 */

public class TvViewAllAdapter extends RecyclerView.Adapter<TvViewAllAdapter.ViewHolder> {
    Context context;
    ArrayList<TvResponse.Tv> tvArrayList;
    OnItemClickListener listener;

    public TvViewAllAdapter(Context context, ArrayList<TvResponse.Tv> tvArrayList, OnItemClickListener listener) {
        this.context = context;
        this.tvArrayList = tvArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.movie_view_recycler_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        TvResponse.Tv tv=tvArrayList.get(position);
        holder.title.setText(tv.name);
        Double rating=tv.vote_average/2;
        Drawable drawable = holder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#DAA520"), PorterDuff.Mode.SRC_ATOP);
        holder.ratingBar.setRating(rating.floatValue());
        Picasso.get().load(Constants.IMAGE_BASE_URL+"w342"+tv.poster_path).into(holder.poster);

        holder.genres.setText("First Air Date: "+tv.first_air_date);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvArrayList.size();
    }

    interface OnItemClickListener{
        void onItemClick(int position);
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView poster;
        TextView title;
        TextView genres;
        RatingBar ratingBar;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            poster=itemView.findViewById(R.id.viewPosterImage);
            title=itemView.findViewById(R.id.viewTitle);
            genres=itemView.findViewById(R.id.viewGenres);
            ratingBar=itemView.findViewById(R.id.viewRatingBar);

        }
    }
}
