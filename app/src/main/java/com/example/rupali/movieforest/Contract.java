package com.example.rupali.movieforest;

/**
 * Created by RUPALI on 18-04-2018.
 */

public class Contract {
    final static String DB_NAME="favDb";
    final static int VERSION=1;
    class FavTable{
        final static String TABLE_NAME="favTable";
        final static String ID="id";
        final static String FAV_ID="favId";
        final static String MEDIA_TYPE="media_type";
        final static String POPULARITY="popularity";
        final static String TITLE="title";
        final static String POSTER_PATH="posterPath";
        final static String IS_TOGGLED="toggled";
    }
}
