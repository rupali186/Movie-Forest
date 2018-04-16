package com.example.rupali.movieforest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RUPALI on 21-03-2018.
 */

public class GenreRecyclerAdapter extends RecyclerView.Adapter<GenreRecyclerAdapter.GenreViewHolder> {

    Context context;
    ArrayList<Genre> genres;
    OnItemClickListener listener;

    public GenreRecyclerAdapter(Context context, ArrayList<Genre> genres, OnItemClickListener listener) {
        this.context = context;
        this.genres = genres;
        this.listener = listener;
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=inflater.inflate(R.layout.genre_list_item,parent,false);
        GenreViewHolder holder=new GenreViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GenreViewHolder holder, int position) {
        Genre genre=genres.get(position);
        holder.name.setText(genre.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
//        holder.name.setBackgroundResource(R.drawable.background);
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        View itemView;
        public GenreViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            name=itemView.findViewById(R.id.genreTextView);
        }
    }
    interface OnItemClickListener{
        void onItemClick(int position);
    }
}
