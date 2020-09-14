package com.bmtt.itunes.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmtt.itunes.View.ItunesSearchService;
import com.bmtt.itunes.Model.AlbumModel;

import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumRepository {

    private static final String ALBUM_SEARCH_SERVICE_BASE_URL = "https://itunes.apple.com";

    private ItunesSearchService ituneSearchService;
    private MutableLiveData<AlbumModel> albumResponseLiveData;

    public AlbumRepository() {

        albumResponseLiveData = new MutableLiveData<>();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        ituneSearchService = new retrofit2.Retrofit.Builder()
                .baseUrl(ALBUM_SEARCH_SERVICE_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItunesSearchService.class);

    }

    public void searchAlbum(String keyword) {
        ituneSearchService.searchAlbum(keyword)
                .enqueue(new Callback<AlbumModel>() {
                    @Override
                    public void onResponse(Call<AlbumModel> call, Response<AlbumModel> response) {
                        if (response.body() != null) {
                            albumResponseLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<AlbumModel> call, Throwable t) {
                        albumResponseLiveData.postValue(null);
                    }
                });
    }

    public LiveData<AlbumModel> getAlbumResponseLiveData() {
        return albumResponseLiveData;
    }
}