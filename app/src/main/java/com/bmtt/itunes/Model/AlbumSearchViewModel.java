package com.bmtt.itunes.Model;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.bmtt.itunes.ViewModel.AlbumRepository;


public class AlbumSearchViewModel extends AndroidViewModel {

    private AlbumRepository albumRepository;

    private LiveData<AlbumModel> albumResponseLiveData;

    public AlbumSearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        albumRepository = new AlbumRepository();
        albumResponseLiveData = albumRepository.getAlbumResponseLiveData();
    }

    public void searchAlbum(String keyword) {

        albumRepository.searchAlbum(keyword);
    }

    public LiveData<AlbumModel> getAlbumResponseLiveData() {
        return albumResponseLiveData;
    }
}

