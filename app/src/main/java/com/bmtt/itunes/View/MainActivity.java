package com.bmtt.itunes.View;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmtt.itunes.Model.AlbumModel;
import com.bmtt.itunes.Model.AlbumSearchViewModel;
import com.bmtt.itunes.Model.Result;
import com.bmtt.itunes.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlbumSearchViewModel viewModel;
    private AlbumSearchAdapter adapter;
    private TextInputEditText keywordEditText;
    AlertDialog alert;
    private Button searchButton, sort_button;
    AlertDialog.Builder builder;
    boolean col_name = false, track_name = false, artist_name = false, coll_price_desc = false;
    List<Result> results = new ArrayList<>();
    CheckBox collection_cb, track_name_cb, artist_name_cb, coll_price_desc_cb;
    RecyclerView recyclerView;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keywordEditText = findViewById(R.id.album_search_keyword);
        searchButton = findViewById(R.id.search_button);
        sort_button = findViewById(R.id.sort_button);
        builder = new AlertDialog.Builder(this);
        adapter = new AlbumSearchAdapter();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                performSearch();
            }
        });
        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.show();
            }
        });
        LayoutInflater factory = LayoutInflater.from(this);

        final View sortDialogView = factory.inflate(R.layout.sort_selection_lo, null);

        collection_cb = sortDialogView.findViewById(R.id.collection_cb);
        track_name_cb = sortDialogView.findViewById(R.id.track_name_cb);
        artist_name_cb = sortDialogView.findViewById(R.id.artist_name_cb);
        coll_price_desc_cb = sortDialogView.findViewById(R.id.coll_price_desc_cb);

        collection_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    col_name = true;
                } else {
                    col_name = false;
                }
            }
        });
        track_name_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    track_name = true;
                } else {
                    track_name = false;
                }
            }
        });
        artist_name_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    artist_name = true;
                } else {
                    artist_name = false;
                }
            }
        });
        coll_price_desc_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    coll_price_desc = true;
                } else {
                    coll_price_desc = false;
                }
            }
        });

        builder.setView(sortDialogView);

        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage(R.string.dialog_message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.dismiss();

                        if (col_name) {
                            Collections.sort(results, new sortCollectionName());
                            adapter.setResults(results, MainActivity.this);
                        } else if (track_name) {
                            Collections.sort(results, new sortTrackName());
                            adapter.setResults(results, MainActivity.this);
                        } else if (artist_name) {
                            Collections.sort(results, new sortArtistName());
                            adapter.setResults(results, MainActivity.this);
                        } else if (coll_price_desc) {
                            Collections.sort(results, new sortCollectionPrice());
                            adapter.setResults(results, MainActivity.this);
                        }
                        recyclerView.getLayoutManager().scrollToPosition(0);
                        collection_cb.setChecked(false);
                        track_name_cb.setChecked(false);
                        artist_name_cb.setChecked(false);
                        coll_price_desc_cb.setChecked(false);

                        // track_name, artist_name, coll_price_desc;
                    }
                });

        //Creating dialog box
        alert = builder.create();
        //Setting the title manually
        alert.setTitle("Select your choice(s)");

        viewModel = ViewModelProviders.of(this).get(AlbumSearchViewModel.class);
        viewModel.init();
        viewModel.getAlbumResponseLiveData().observe(this, new Observer<AlbumModel>() {
            @Override
            public void onChanged(AlbumModel volumesResponse) {
                if (volumesResponse != null) {

                    //if results is there then clear it and store
                    if (results.size() > 0) {
                        results.clear();
                    }
                    results = volumesResponse.getResults();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //your own date format

                    Collections.sort(results, new Comparator<Result>() {
                        @Override
                        public int compare(Result o1, Result o2) {
                            try {
                                return simpleDateFormat.parse(o1.getReleaseDate().substring(0, 10)).compareTo(
                                        simpleDateFormat.parse(o2.getReleaseDate().substring(0, 10)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });

                    adapter.setResults(results, MainActivity.this);
                }
            }
        });


        recyclerView = findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);

    }

    class sortCollectionName implements Comparator<Result> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Result a, Result b) {

            if (a.getCollectionName() == null && b.getCollectionName() != null) return -1;
            if (a.getCollectionName() == null && b.getCollectionName() == null) return 0;
            if (a.getCollectionName() != null && b.getCollectionName() == null) return 1;
            if (a.getCollectionName() != null && b.getCollectionName() != null) {
                return a.getCollectionName().compareTo(b.getCollectionName());
            } else {
                return 100;
            }
        }
    }

    class sortTrackName implements Comparator<Result> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Result a, Result b) {

            if (a.getTrackName() == null && b.getTrackName() != null) return -1;
            if (a.getTrackName() == null && b.getTrackName() == null) return 0;
            if (a.getTrackName() != null && b.getTrackName() == null) return 1;
            if (a.getTrackName() != null && b.getTrackName() != null) {
                return a.getTrackName().compareTo(b.getTrackName());
            } else {
                return 100;
            }
        }
    }

    class sortArtistName implements Comparator<Result> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Result a, Result b) {

            if (a.getArtistName() == null && b.getArtistName() != null) return -1;
            if (a.getArtistName() == null && b.getArtistName() == null) return 0;
            if (a.getArtistName() != null && b.getArtistName() == null) return 1;
            if (a.getArtistName() != null && b.getArtistName() != null) {
                return a.getArtistName().compareTo(b.getArtistName());
            } else {
                return 100;
            }
        }
    }

    class sortCollectionPrice implements Comparator<Result> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Result a, Result b) {

            if (b.getCollectionPrice() == null && a.getCollectionPrice() != null) return -1;
            if (b.getCollectionPrice() == null && a.getCollectionPrice() == null) return 0;
            if (b.getCollectionPrice() != null && a.getCollectionPrice() == null) return 1;
            if (b.getCollectionPrice() != null && a.getCollectionPrice() != null) {
                return b.getCollectionPrice().compareTo(a.getCollectionPrice());
            } else {
                return 100;
            }
        }
    }

   /* class Sortbyname implements Comparator<Student>
    {
        // Used for sorting in ascending order of
        // roll name
        public int compare(Student a, Student b)
        {
            return a.name.compareTo(b.name);
        }
    }*/

    public void performSearch() {
        String keyword = keywordEditText.getEditableText().toString();
        viewModel.searchAlbum(keyword);

    }
}