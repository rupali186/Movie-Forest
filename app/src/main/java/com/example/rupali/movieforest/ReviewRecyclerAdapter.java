package com.example.rupali.movieforest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RUPALI on 26-03-2018.
 */

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<Reviews.Result> reviews;
    OnItemClickListener listener;

    public ReviewRecyclerAdapter(Context context, ArrayList<Reviews.Result> reviews, OnItemClickListener listener) {
        this.context = context;
        this.reviews = reviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.review_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Reviews.Result review=reviews.get(position);
        holder.name.setText(review.author);
        holder.content.setText(review.content);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (review.isExpanded){
                    review.isExpanded=false;
                    holder.content.setMaxLines(4);
                }
                else{
                    review.isExpanded=true;
                    holder.content.setMaxLines(Integer.MAX_VALUE);
                }

                listener.onItemCilck(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
    interface OnItemClickListener{
        void onItemCilck(int position);
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView content;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            name=itemView.findViewById(R.id.reviewUsername);
            content=itemView.findViewById(R.id.reviewContent);
        }
    }
}
