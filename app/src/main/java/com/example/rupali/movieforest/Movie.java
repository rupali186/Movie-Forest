package com.example.rupali.movieforest;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RUPALI on 20-03-2018.
 */
@Entity
public class Movie {
    boolean adult;
    String backdrop_path;
    Collection belongs_to_collection;
    int budget;
    ArrayList<Genre> genres;
    String homepage;
    @PrimaryKey
    @NonNull
    int id;
    String imdb_id;
    String original_language;
    String original_title;
    String overview;
    Double popularity;
    String poster_path;
    ArrayList<ProductionCompanies> production_companies;
    ArrayList<ProductionCountry> production_countries;
    String release_date;
    int revenue;
    int runtime;
    ArrayList<SpokenLanguage> spoken_languages;
    String status;
    String tagline;
    String title;
    boolean video;
    String character;//for cast
    String credit_id;//for cast
    Double vote_average;
    int vote_count;
    ArrayList<Integer> genre_ids;
    Videos videos;
    Credits credits;
    Image images;
    Reviews reviews;
    Similar similar;

}
class Similar{
    ArrayList<Movie> results;
}
class Reviews{
    int page;
    int total_pages;
    int total_results;
    ArrayList<Result> results;
    class  Result{
        String author;
        String content;
        String id;
        String url;
        boolean isExpanded=false;
    }
}
class Image{
    ArrayList<BackDrops> backdrops;
    ArrayList<Posters> posters;
    class BackDrops{
        Double aspect_ratio;
        String file_path;
        int height;
        String iso_639_1;
        Double vote_average;
        int vote_count;
        int width;
    }
    class Posters{
        Double aspect_ratio;
        String file_path;
        int height;
        String iso_639_1;
        Double vote_average;
        Double vote_count;
        int width;
    }
}
class Videos {
    ArrayList<Video> results;
    class Video{
        String id;
        @SerializedName("iso_639_1")
        String language;
        @SerializedName("iso_3166_1")
        String country;
        String key;
        String name;
        String site;
        int size;
        String type;

    }
}
 class Credits {
    ArrayList<Cast> cast;
    ArrayList<Crew> crew;
    class Cast{
        int cast_id;
        String character;
        String credit_id;
        int gender;
        int id;
        String name;
        int order;
        String profile_path;
    }
    class Crew{
        String credit_id;
        String department;
        int gender;
        int id;
        String job;
        String name;
        String profile_path;
    }

}

class Collection{
    int id;
    String name;
    String poster_path;
    String backdrop_path;
}
class Genre{
    int id;
    String name;
}
class ProductionCompanies{
    int id;
    String logo_path;
    String name;
    String origin_country;
}
class ProductionCountry{
    String iso_3166_1;
    String name;

}
class SpokenLanguage{
    String iso_639_1;
    String name;
}
