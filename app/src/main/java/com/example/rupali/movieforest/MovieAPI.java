package com.example.rupali.movieforest;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by RUPALI on 21-03-2018.
 */

public interface MovieAPI {
    @GET("movie/popular?api_key="+AppConfig.TMDB_API_KEY)
    Call<MovieResponse> getPopularMovies();
    @GET("movie/now_playing?api_key="+AppConfig.TMDB_API_KEY)
    Call<MovieResponse> getNowPlaying();
    @GET("movie/top_rated?api_key="+AppConfig.TMDB_API_KEY)
    Call<MovieResponse> getTopRated();
    @GET("movie/upcoming?api_key="+AppConfig.TMDB_API_KEY)
    Call<MovieResponse> getUpcoming();
    @GET("genre/{genre_id}/movies?api_key="+AppConfig.TMDB_API_KEY+"&sort_by=created_at.asc")
    Call<MovieResponse> getMoviesByGenre(@Path("genre_id") String genreID);
    @GET("movie/{movie_id}?api_key="+AppConfig.TMDB_API_KEY+"&append_to_response=videos,credits,images,similar,reviews")
    Call<Movie> getMovieDetails(@Path("movie_id") String movieID);
    @GET("movie/{movie_id}/reviews?api_key="+AppConfig.TMDB_API_KEY)
    Call<MovieResponse> getReviews(@Path("movie_id") String movieID);
    @GET("genre/movie/list?api_key="+AppConfig.TMDB_API_KEY)
    Call<GenreResponse> getGenres();
    @GET("person/popular?api_key="+AppConfig.TMDB_API_KEY)
    Call<CelebResponse> getPopularCelebs();
    @GET("person/{celeb_id}?api_key="+AppConfig.TMDB_API_KEY+"&append_to_response=movie_credits,tv_credits")
    Call<CelebResponse.Celeb> getCelebDetail(@Path("celeb_id") int celeb_id);
    @GET("tv/airing_today?api_key="+AppConfig.TMDB_API_KEY)
    Call<TvResponse> getTvAirngToday();
    @GET("tv/on_the_air?api_key="+AppConfig.TMDB_API_KEY)
    Call<TvResponse> getTvOnTheAir();
    @GET("tv/popular?api_key="+AppConfig.TMDB_API_KEY)
    Call<TvResponse> getTvPopular();
    @GET("tv/top_rated?api_key="+AppConfig.TMDB_API_KEY)
    Call<TvResponse> getTvTopRated();
    @GET("tv/{tv_id}?api_key="+AppConfig.TMDB_API_KEY+"&append_to_response=similar,reviews,videos,credits")
    Call<TvResponse.Tv> getTvDetails(@Path("tv_id") int tv_id);
    @GET("search/multi?api_key="+AppConfig.TMDB_API_KEY)
    Call<Search> search(@Query("query") String searchString);
}
