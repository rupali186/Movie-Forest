package com.example.rupali.movieforest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RUPALI on 23-03-2018.
 */

public class CastRecyclerAdapter extends RecyclerView.Adapter<CastRecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<Credits.Cast> castArrayList;
    OnItemClickListener listener;

    public CastRecyclerAdapter(Context context, ArrayList<Credits.Cast> castArrayList, OnItemClickListener listener) {
        this.context = context;
        this.castArrayList = castArrayList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.credit_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Credits.Cast cast=castArrayList.get(position);
        Log.d("PicassoCast: ","success");
        holder.creditTitle.setText(cast.name);
        holder.creditPost.setText(cast.character);
        Picasso.get().load("http://image.tmdb.org/t/p/w185/"+cast.profile_path).resize(400,600).into(holder.creditImage, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("PicassoCast: ","success");
            }

            @Override
            public void onError(Exception e) {
                Log.d("PicassoCast: ",e.getMessage());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return castArrayList.size();
    }
    interface OnItemClickListener{
        void onItemClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView creditImage;
        TextView creditTitle;
        TextView creditPost;
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            creditImage=itemView.findViewById(R.id.creditImage);
            creditTitle=itemView.findViewById(R.id.creditTitle);
            creditPost=itemView.findViewById(R.id.creditPost);
        }
    }
}
