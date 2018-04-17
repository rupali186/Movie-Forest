package com.example.rupali.movieforest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by RUPALI on 17-04-2018.
 */

public class ViewPagerAdapter extends PagerAdapter {
    LayoutInflater inflater;
    Context context;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    int[] image_list={
            R.drawable.movies_icon_min,
            R.drawable.tv_icon_min,
            R.drawable.celebs_emoji,
            R.drawable.explore_icon,
            R.drawable.favourites_icon_min,
            R.drawable.share_icon
    };
    String [] des_list={
      "Explore the latest, popular, upcoming ,top rated movies and much more... ",
            "Stay updated about whats on the Tv. Get info about newly launched, top rated and popular shows.",
            "Never miss interesting information about your favourite celebs. Get their birthdays, biography etc in one go.",
            "Search your favourite movies, tv shows and celebs.",
            "Mark your movies, tv shows and celebs as favourites and save your time from searching them again.",
            "Share your favourite movies, tv shows and celebs with your loved ones."
    };
    @Override
    public int getCount() {
        return des_list.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view=inflater.inflate(R.layout.viewpager_layout,container,false);
        ImageView imageView=view.findViewById(R.id.viewpager_image);
        TextView descriptionTextView=view.findViewById(R.id.viewpager_des);
        imageView.setImageResource(image_list[position]);
        descriptionTextView.setText(des_list[position]);
        container.addView(view);
        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((LinearLayout)object);
    }
}
