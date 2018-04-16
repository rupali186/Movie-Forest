package com.example.rupali.movieforest;

import java.util.ArrayList;

/**
 * Created by RUPALI on 12-04-2018.
 */

public class Search {
    ArrayList<Result> results;
    int total_results;
    int total_pages;
    class Result{
        int id;
        Double vote_average;
        boolean video;
        String media_type;
        String title;
        String name;
        Double popularity;
        String poster_path;
        String profile_path;
        String original_language;
        String original_title;
        String backdrop_path;
        String overview;
        String release_date;
        boolean adult;
    }
//    "page": 1,
//            "total_results": 284,
//            "total_pages": 15,
//            "results": [
//    {
//        "vote_average": 7.8,
//            "vote_count": 3807,
//            "id": 354912,
//            "video": false,
//            "media_type": "movie",
//            "title": "Coco",
//            "popularity": 239.701617,
//            "poster_path": "/eKi8dIrr8voobbaGzDpe8w0PVbC.jpg",
//            "original_language": "en",
//            "original_title": "Coco",
//            "genre_ids": [
//        12,
//                35,
//                10751,
//                16
//      ],
//        "backdrop_path": "/askg3SMvhqEl4OL52YuvdtY40Yb.jpg",
//            "adult": false,
//            "overview": "Despite his family’s baffling generations-old ban on music, Miguel dreams of becoming an accomplished musician like his idol, Ernesto de la Cruz. Desperate to prove his talent, Miguel finds himself in the stunning and colorful Land of the Dead following a mysterious chain of events. Along the way, he meets charming trickster Hector, and together, they set off on an extraordinary journey to unlock the real story behind Miguel's family history.",
//            "release_date": "2017-10-27"
//    },
}
