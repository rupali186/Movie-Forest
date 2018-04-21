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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by RUPALI on 12-04-2018.
 */

public class CelebAdapterVertical extends RecyclerView.Adapter<CelebAdapterVertical.ViewHolder> {
    Context context;
    ArrayList<CelebResponse.Celeb> celebArrayList;
    OnItemClickListener listener;

    public CelebAdapterVertical(Context context, ArrayList<CelebResponse.Celeb> celebArrayList, OnItemClickListener listener) {
        this.context = context;
        this.celebArrayList = celebArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=inflater.inflate(R.layout.celeb_item_vertical_layout,parent,false);
        ViewHolder holder=new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CelebResponse.Celeb celeb=celebArrayList.get(position);
        holder.name.setText(celeb.name);
        DecimalFormat df = new DecimalFormat("#.00");
        Double popu = celeb.popularity;
        String angleFormatted = df.format(popu);
        holder.popularity.setText(angleFormatted);
        if(celeb.profile_path!=null) {
            Picasso.get().load(Constants.IMAGE_BASE_URL + "/w185" + celeb.profile_path).resize(300, 400).into(holder.profile);
        }
        holder.itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return celebArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View itemview;
        TextView name;
        ImageView profile;
        TextView popularity;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemview=itemView;
            name=itemView.findViewById(R.id.celebname);
            popularity=itemView.findViewById(R.id.celebpopularity);
            profile=itemView.findViewById(R.id.celebimage);
        }
    }
    interface OnItemClickListener{
        void onItemClick(int position);
    }
}
