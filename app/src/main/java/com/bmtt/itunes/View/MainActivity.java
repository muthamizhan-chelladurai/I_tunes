package com.bmtt.itunes.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmtt.itunes.R;
import com.bmtt.itunes.Model.AlbumModel;
import com.bmtt.itunes.Model.AlbumSearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private AlbumSearchViewModel viewModel;
    private AlbumSearchAdapter adapter;
    private TextInputEditText keywordEditText;

    private Button searchButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keywordEditText = findViewById(R.id.album_search_keyword);
        searchButton = findViewById(R.id.search_button);

        adapter = new AlbumSearchAdapter();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });


        viewModel = ViewModelProviders.of(this).get(AlbumSearchViewModel.class);
        viewModel.init();
        viewModel.getAlbumResponseLiveData().observe(this, new Observer<AlbumModel>() {
            @Override
            public void onChanged(AlbumModel volumesResponse) {
                if (volumesResponse != null) {
                    adapter.setResults(volumesResponse.getResults());
                }
            }
        });


        RecyclerView recyclerView = findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);

    }


    public void performSearch() {
        String keyword = keywordEditText.getEditableText().toString();
        viewModel.searchAlbum(keyword);

    }
}