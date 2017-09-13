package com.doxa360.android.dutch;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.android.dutch.adapter.AdAdapter;
import com.doxa360.android.dutch.helpers.DutchSharedPref;
import com.doxa360.android.dutch.helpers.MyToolBox;
import com.doxa360.android.dutch.model.Ad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mEmptyLabel;

    private Context mContext;
    private DutchApiInterface mDutchApiInterface;
    private DutchSharedPref mSharedPref;
    private List<Ad> adList;
    private AdAdapter mAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.add_ad);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostAdActivity.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.home_recyclerview);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mEmptyLabel = (TextView) rootView.findViewById(R.id.empty_label);

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
            getAllAds();
        }
        else {
            Toast.makeText(mContext, "Network unavailable. Please check your connection", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }

    private void refreshAds() {
        getAllAds();
    }

    private void getAllAds() {
        Log.e(TAG, "hello");
        mDutchApiInterface = DutchApiClient.getClient().create(DutchApiInterface.class);

        Call<List<Ad>> call = mDutchApiInterface.allItems();
        call.enqueue(new Callback<List<Ad>>() {
            @Override
            public void onResponse(Call<List<Ad>> call, Response<List<Ad>> response) {
                Log.e(TAG, response.message() + response.body().size());
                if (response.isSuccessful()) {
                    adList = response.body();
                    Log.e(TAG, adList.get(0).toString()+"");
                    Log.e(TAG, adList.get(0).getTitle() + " - " + adList.get(0).getUser().getName());
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mContext = (AppCompatActivity) activity;
//    }

}
