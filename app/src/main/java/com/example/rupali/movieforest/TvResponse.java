package com.example.rupali.movieforest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by RUPALI on 09-04-2018.
 */

public class TvResponse {
    int total_results;
    int total_pages;
    ArrayList<Tv> results;
    class Tv{
        String original_name;
        ArrayList<Integer> genre_ids;
        String name;
        Double popularity;
        ArrayList<String> origin_country;
        int vote_count;
        String first_air_date;
        String backdrop_path;
        String original_language;
        int id;
        Double vote_average;
        String overview;
        String poster_path;
        Reviews reviews;
        Videos videos;
        ArrayList<CreatedBy> created_by;
        ArrayList<Integer> episode_run_time;
        ArrayList<Genres> genres;
        String homepage;
        boolean in_production;
        ArrayList<String> languages;
        String last_air_date;
        ArrayList<Network> networks;
        ArrayList<ProductionCompany> production_companies;
        ArrayList<Seasons> seasons;
        String status;
        TvResponse similar;
        String type;
        Credits credits;
//        class Credits{
//            ArrayList<Cast> cast;
//            class Cast{
//                String credit_id;
//                int gender;
//                int id;
//                String name;
//                int order;
//                String profile_path;
//            }
//        }
//        class Reviews{
//            int total_results;
//            int page;
//            int total_pages;
//            ArrayList<Result> results;
//            class Result{
//                String author;
//                String content;
//                String id;
//                String url;
//            }
//        }
        class Videos{
            ArrayList<Result> results;
            class Result{
                String id;
                @SerializedName("iso_639_1")
                String languages;
                @SerializedName("iso_3166_1")
                String countries;
                String key;
                String name;
                String site;
                int size;
                String type;
            }
        }
        class CreatedBy{
            int id;
            String name;
            int gender;
            String profile_path;
        }
        class Genres{
            int id;
            String name;
        }
        class Network{
            String name;
            int id;
            String logo_path;
            String origin_country;
        }
        class ProductionCompany{
            int id;
            String logo_path;
            String name;
            String origin_country;
        }
        class Seasons {
            String air_date;
            int episode_count;
            int id;
            String name;
            String overview;
            String poster_path;
            int season_number;
        }
    }
}


