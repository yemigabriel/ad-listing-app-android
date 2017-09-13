package com.doxa360.android.dutch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.android.dutch.adapter.AdAdapter;
import com.doxa360.android.dutch.helpers.DutchSharedPref;
import com.doxa360.android.dutch.helpers.MyToolBox;
import com.doxa360.android.dutch.model.Ad;
import com.doxa360.android.dutch.model.RecentSuggestionProvider;
import com.doxa360.android.dutch.model.User;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchableActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private Context mContext = this;
    private TextView mEmptyLabel;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private DutchSharedPref mSharedPref;
    private User currentUser;
    private DutchApiInterface mDutchApiInterface;
    private List<Ad> adList;
    private AdAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        

        mEmptyLabel = (TextView)  findViewById(R.id.empty_label);
        mRecyclerView = (RecyclerView)  findViewById(R.id.user_ads_recyclerview);
        mProgressBar = (ProgressBar)  findViewById(R.id.progress_bar);

        mSharedPref = new DutchSharedPref(mContext);
        currentUser = mSharedPref.getCurrentUser();

        mRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(mContext.getResources().getInteger(R.integer.channels_grid_columns), StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    RecentSuggestionProvider.AUTHORITY, RecentSuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            getSupportActionBar().setTitle("Search: "+query);
            if (MyToolBox.isNetworkAvailable(mContext)) {
                doSearch(query);
            }
            else {
                Toast.makeText(mContext, "Network Error. Please check your connection", Toast.LENGTH_SHORT).show();
            }
        }
        
    }


    private void doSearch(String query) {
        Log.e(TAG, "hello");
        mDutchApiInterface = DutchApiClient.getClient().create(DutchApiInterface.class);

        Call<List<Ad>> call = mDutchApiInterface.search(query);
        call.enqueue(new Callback<List<Ad>>() {
            @Override
            public void onResponse(Call<List<Ad>> call, Response<List<Ad>> response) {
                Log.e(TAG, response.message() + response.body().size());
                if (response.isSuccessful()) {
                    adList = response.body();
                    if (adList.size() == 0) {
                        mEmptyLabel.setText("No ad matches your search.");
                    } else {
                        mEmptyLabel.setText("");
                    }
                    mAdapter = new AdAdapter(adList, mContext);
                    mRecyclerView.setAdapter(mAdapter);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    try {
                        Log.e(TAG, response.errorBody().string()+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mEmptyLabel.setText("Network error. Please check your connection.");
                }
            }

            @Override
            public void onFailure(Call<List<Ad>> call, Throwable t) {
                mEmptyLabel.setText("Network error. Please check your connection.");
                Log.e(TAG, t.getMessage()+"");
            }
        });
    }

    public void clearSearchHistory() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                RecentSuggestionProvider.AUTHORITY, RecentSuggestionProvider.MODE);
        suggestions.clearHistory();
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }



}
