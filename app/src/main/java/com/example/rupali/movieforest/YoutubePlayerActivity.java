package com.example.rupali.movieforest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends YouTubeBaseActivity {
    Button play;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        play=findViewById(R.id.play);
        youTubePlayerView=findViewById(R.id.youtubePlay);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        if(bundle!=null){
            String key=bundle.getString(Constants.VIDEO_KEY);
        }
        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(bundle!=null){
                    play.setVisibility(View.GONE);
                    String key=bundle.getString(Constants.VIDEO_KEY);
              //      youTubePlayer.loadVideo(key);
                    youTubePlayer.cueVideo(key);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayerView.initialize(AppConfig.YOUTUBE_API_KEY,onInitializedListener);
            }
        });

    }
}
