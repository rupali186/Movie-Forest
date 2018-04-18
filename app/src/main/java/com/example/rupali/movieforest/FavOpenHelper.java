package com.example.rupali.movieforest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RUPALI on 18-04-2018.
 */

public class FavOpenHelper extends SQLiteOpenHelper {
    private static FavOpenHelper favOpenHelper;
    public static FavOpenHelper getInstance(Context context){
        if(favOpenHelper==null){
            favOpenHelper=new FavOpenHelper(context.getApplicationContext());
        }
        return favOpenHelper;
    }
    public FavOpenHelper(Context context) {
        super(context, Contract.DB_NAME, null, Contract.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String favSql="CREATE TABLE "+Contract.FavTable.TABLE_NAME+" ( "+
                Contract.FavTable.FAV_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , " +
                Contract.FavTable.TITLE+" TEXT , "+Contract.FavTable.ID+" INTEGER , "+Contract.FavTable.POPULARITY+
                " REAL , "+Contract.FavTable.POSTER_PATH+" TEXT , "+Contract.FavTable.IS_TOGGLED+
                " TEXT , "+Contract.FavTable.MEDIA_TYPE+" TEXT )";
        sqLiteDatabase.execSQL(favSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
