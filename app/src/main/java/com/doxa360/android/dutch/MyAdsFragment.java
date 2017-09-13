package com.doxa360.android.dutch;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.android.dutch.adapter.AdAdapter;
import com.doxa360.android.dutch.helpers.DutchSharedPref;
import com.doxa360.android.dutch.helpers.MyToolBox;
import com.doxa360.android.dutch.model.Ad;
import com.doxa360.android.dutch.model.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAdsFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private TextView mEmptyLabel;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private FloatingActionButton mPostAdBtn;
    private Context mContext;
    private DutchSharedPref mSharedPref;
    private User currentUser;
    private DutchApiInterface mDutchApiInterface;
    private List<Ad> adList;
    private AdAdapter mAdapter;

    public MyAdsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_ads, container, false);

        mEmptyLabel = (TextView) rootView.findViewById(R.id.empty_label);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.user_ads_recyclerview);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mPostAdBtn = (FloatingActionButton) rootView.findViewById(R.id.post_ad_btn);

        mSharedPref = new DutchSharedPref(mContext);
        currentUser = mSharedPref.getCurrentUser();

        mPostAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostAdActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView.setHasFixedSize(true);
//        GridLayoutManager layoutManager = new GridLayoutManager(mContext, mContext.getResources().getInteger(R.integer.channels_grid_columns), GridLayoutManager.HORIZONTAL, false);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(mContext.getResources().getInteger(R.integer.channels_grid_columns), StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);


        if (MyToolBox.isNetworkAvailable(mContext)) {
            getUserAds();
        }
        else {
            Toast.makeText(mContext, "Network Error. Please check your connection", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    private void getUserAds() {
        Log.e(TAG, "hello");
        mDutchApiInterface = DutchApiClient.getClient().create(DutchApiInterface.class);

        Call<List<Ad>> call = mDutchApiInterface.userAds(currentUser.getId());
        call.enqueue(new Callback<List<Ad>>() {
            @Override
            public void onResponse(Call<List<Ad>> call, Response<List<Ad>> response) {
                Log.e(TAG, response.message() + response.body().size());
                if (response.isSuccessful()) {
                    adList = response.body();
//                    Log.e(TAG, adList.get(0).toString()+"");
//                    Log.e(TAG, adList.get(0).getTitle() + " - " + adList.get(0).getUser().getName());
                    if (adList.size() == 0) {
                        mEmptyLabel.setText("You have not posted an ad yet.");
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
//                Log.e(TAG, t.getMessage()+"");
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
