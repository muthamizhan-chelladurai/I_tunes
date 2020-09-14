package com.bmtt.itunes.View;

import com.bmtt.itunes.Model.AlbumModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ItunesSearchService {


    @GET("/search")
    Call<AlbumModel> searchAlbum(
            @Query("term") String query
    );

}
