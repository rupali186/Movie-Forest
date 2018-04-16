package com.example.rupali.movieforest;

import java.util.ArrayList;

/**
 * Created by RUPALI on 09-04-2018.
 */

public class CelebResponse {
    int page;
    int total_results;
    int total_pages;
    ArrayList<Celeb> results;

    class Celeb {
        Double popularity;
        int id;
        String profile_path;
        String name;
        ArrayList<KnownFor> known_for;
        boolean adult;

        class KnownFor {
            Double vote_average;
            int vote_count;
            int id;
            boolean video;
            String media_type;
            String title;
            Double popularity;
            String poster_path;
            String original_language;
            String original_title;
            ArrayList<Integer> genre_ids;
            String backdrop_path;
            boolean adult;
            String overview;
            String release_date;
        }

        String birthday;
        int gender;
        String biography;
        String place_of_birth;
        String imdb_id;
        String homepage;
        MovieCredits movie_credits;
        TvCredits tv_credits;
        class MovieCredits {
            ArrayList<Movie> cast;
            ArrayList<Crew> crew;
            class Cast {
                String release_date;
                boolean adult;
                Double vote_average;
                int vote_count;
                boolean video;
                String title;
                Double popularity;
                ArrayList<Integer> genre_ids;
                String original_language;
                String character;
                String original_title;
                String poster_path;
                int id;
                String backdrop_path;
                String overview;
                String credit_id;
            }
            class Crew{
                int id;
                String department;
                String original_language;
                String original_title;
                String job;
                String overview;
                ArrayList<Integer> genre_ids;
                boolean video;
                String credit_id;
                String release_date;
                Double popularity;
                Double vote_average;
                Double vote_count;
                String title;
                boolean adult;
                String backdrop_path;
                String poster_path;
            }
        }
        class TvCredits{
            ArrayList<TvResponse.Tv> cast;
            class Cast{
                ArrayList<String> origin_country;
                String original_name;
                ArrayList<Integer> genre_ids;
                int vote_count;
                String backdrop_path;
                String name;
                String first_air_date;
                String original_language;
                Double popularity;
                String character;
                int episode_count;
                int id;
                String credit_id;
                Double vote_average;
                String overview;
                String poster_path;
            }
        }
    }

}

