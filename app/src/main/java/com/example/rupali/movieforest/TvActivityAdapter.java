package com.example.rupali.movieforest;

import android.content.Context;
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

    public TvActivityAdapter(Context context, ArrayList<TvResponse.Tv> tvArrayList, OnItemClickListener listener) {
        this.context = context;
        this.tvArrayList = tvArrayList;
        this.listener = listener;
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
        TvResponse.Tv tv=tvArrayList.get(position);
        holder.name.setText(tv.name);
        Double rating=tv.vote_average/2;
        Drawable drawable = holder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#DAA520"), PorterDuff.Mode.SRC_ATOP);
        holder.ratingBar.setRating(rating.floatValue());
        Picasso.get().load(Constants.IMAGE_BASE_URL+"w500/"+tv.poster_path).resize(410,600).into(holder.poster);
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
