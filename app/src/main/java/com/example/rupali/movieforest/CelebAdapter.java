package com.example.rupali.movieforest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by RUPALI on 09-04-2018.
 */

public class CelebAdapter extends RecyclerView.Adapter<CelebAdapter.ViewHolder> {
    Context context;
    ArrayList<CelebResponse.Celeb> celebsArrayList;
    OnItemClickListener listener;

    public CelebAdapter(Context context, ArrayList<CelebResponse.Celeb> celebsArrayList, OnItemClickListener listener) {
        this.context = context;
        this.celebsArrayList = celebsArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.celeb_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CelebResponse.Celeb celeb=celebsArrayList.get(position);
        if(celeb.name!=null) {
            holder.celebName.setText(celeb.name);
        }
        if (celeb.profile_path!=null) {
            Picasso.get().load(Constants.IMAGE_BASE_URL + "/w185" + celeb.profile_path).resize(300, 400).into(holder.celebImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return celebsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView celebName;
        ImageView celebImage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            celebImage=itemView.findViewById(R.id.celebImage);
            celebName=itemView.findViewById(R.id.celebName);
        }
    }
    interface OnItemClickListener{
        void onItemClick(int position);
    }
}
