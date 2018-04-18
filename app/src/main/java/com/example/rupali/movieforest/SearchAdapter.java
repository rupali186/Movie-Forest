package com.example.rupali.movieforest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by RUPALI on 16-04-2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;
    ArrayList<SearchResults> results;
    OnItemClickListener listener;

    public SearchAdapter(Context context, ArrayList<SearchResults> results, OnItemClickListener listener) {
        this.context = context;
        this.results = results;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.search_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        SearchResults result=results.get(position);
        holder.mediaType.setText(result.media_type);
        if(result.media_type.equalsIgnoreCase("person")){
            Picasso.get().load(Constants.IMAGE_BASE_URL+"/w185"+result.profile_path).resize(300,400).into(holder.profile);
            if (result.profile_path==null){
                holder.profile.setBackgroundResource(R.drawable.no_image_availaible);
            }
            holder.mediaType.setText("Celeb");
            holder.name.setText(result.name);
            holder.toggleButton.setVisibility(View.GONE);
        }
        else if(result.media_type.equalsIgnoreCase("tv")) {
            Picasso.get().load(Constants.IMAGE_BASE_URL + "/w185" + result.poster_path).resize(300, 400).into(holder.profile);
            if (result.poster_path==null){
                holder.profile.setBackgroundResource(R.drawable.no_image_availaible);
            }
            holder.mediaType.setText("Tv");
            holder.name.setText(result.name);
        }
        else {
            Picasso.get().load(Constants.IMAGE_BASE_URL + "/w185" + result.poster_path).resize(300, 400).into(holder.profile);
            if (result.poster_path==null){
                holder.profile.setBackgroundResource(R.drawable.no_image_availaible);
            }
            holder.mediaType.setText("Movie");
            holder.name.setText(result.title);
        }
        DecimalFormat df = new DecimalFormat("#.00");
        Double popu = result.popularity;
        String angleFormatted = df.format(popu);
        holder.popularity.setText(angleFormatted);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
        holder.toggleButton.setVisibility(View.VISIBLE);
        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onToggleClick(holder.getAdapterPosition(),view);
            }
        });
        if (result.isToggled.charAt(0)=='t'){
            holder.toggleButton.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
    interface OnItemClickListener{
        void onItemClick(int position);
        void onToggleClick(int position, View view);
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView name;
        TextView popularity;
        TextView mediaType;
        ImageView profile;
        ToggleButton toggleButton;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            name=itemView.findViewById(R.id.search_name);
            popularity=itemView.findViewById(R.id.search_popularity);
            mediaType=itemView.findViewById(R.id.search_media_type);
            profile=itemView.findViewById(R.id.search_profile);
            toggleButton=itemView.findViewById(R.id.search_toggle);
        }
    }
}
