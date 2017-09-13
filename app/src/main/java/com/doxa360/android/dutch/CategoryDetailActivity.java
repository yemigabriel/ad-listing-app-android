package com.doxa360.android.dutch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.doxa360.android.dutch.model.Category;
import com.doxa360.android.dutch.model.User;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mEmptyLabel;

    private Context mContext = this;
    private DutchApiInterface mDutchApiInterface;
    private DutchSharedPref mSharedPref;
    private List<Ad> adList;
    private AdAdapter mAdapter;

    int categoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Category category = intent.getParcelableExtra(Dutch.CATEGORY);


        Log.e("category error", category.toString());
        categoryId = category.getId();
        getSupportActionBar().setTitle(category.getTitle());

        mSwipeRefreshLayout = (SwipeRefreshLayout)  findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView)  findViewById(R.id.home_recyclerview);
        mProgressBar = (ProgressBar)  findViewById(R.id.progress_bar);
        mEmptyLabel = (TextView)  findViewById(R.id.empty_label);

//        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setHasFixedSize(true);
//        GridLayoutManager layoutManager = new GridLayoutManager(mContext, mContext.getResources().getInteger(R.integer.channels_grid_columns));
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(mContext.getResources().getInteger(R.integer.channels_grid_columns), StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        Log.e(TAG, "here o");


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAds();
            }
        });

        mSwipeRefreshLayout.setEnabled(false);

        if (MyToolBox.isNetworkAvailable(mContext)) {
            getAllAds(categoryId);
        }
        else {
            Toast.makeText(mContext, "Network unavailable. Please check your connection", Toast.LENGTH_LONG).show();
        }
//        mAdapter = new AdAdapter(adList, mContext);
//        mRecyclerView.setAdapter(mAdapter);
//        mProgressBar.setVisibility(View.INVISIBLE);
//        if(mSwipeRefreshLayout.isRefreshing()) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }

    }

    private void refreshAds() {
        getAllAds(categoryId);
    }

    private void getAllAds(int categoryId ) {
        Log.e(TAG, "hello");
        mDutchApiInterface = DutchApiClient.getClient().create(DutchApiInterface.class);

        Call<List<Ad>> call = mDutchApiInterface.allItemsByCategory(categoryId);
        call.enqueue(new Callback<List<Ad>>() {
            @Override
            public void onResponse(Call<List<Ad>> call, Response<List<Ad>> response) {
                Log.e(TAG, response.message() + response.body().size());
                if (response.isSuccessful()) {
                    adList = response.body();
                    Log.e(TAG, "first item by cat is: "+adList.get(0).toString()+"");
//                    Log.e(TAG, adList.get(0).getTitle() + " - " + adList.get(0).getUser().getName());
                    mAdapter = new AdAdapter(adList, mContext);
                    mRecyclerView.setAdapter(mAdapter);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    if(mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
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

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }



}
