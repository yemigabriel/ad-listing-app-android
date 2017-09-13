package com.doxa360.android.dutch;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class ProfileFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private ImageView mProfilePhoto;
    private TextView mName, mPhone, mAdCount, mCreatedSince, mRecentAdsLabel;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private FloatingActionButton mPostAdBtn;
    private Context mContext;
    private DutchSharedPref mSharedPref;
    private User currentUser;
    private DutchApiInterface mDutchApiInterface;
    private List<Ad> adList;
    private AdAdapter mAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mProfilePhoto = (ImageView) rootView.findViewById(R.id.avatar);
        mName = (TextView) rootView.findViewById(R.id.name);
        mPhone = (TextView) rootView.findViewById(R.id.phone);
        mAdCount = (TextView) rootView.findViewById(R.id.ads_count);
        mCreatedSince = (TextView) rootView.findViewById(R.id.joined_since);
        mRecentAdsLabel = (TextView) rootView.findViewById(R.id.recent_ads_label);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recent_ads_recyclerview);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mPostAdBtn = (FloatingActionButton) rootView.findViewById(R.id.post_ad_btn);

        mSharedPref = new DutchSharedPref(mContext);
        currentUser = mSharedPref.getCurrentUser();

        mName.setText(currentUser.getName());
        mPhone.setText(currentUser.getPhone() != null ? currentUser.getPhone():"");
//        mCreatedSince.setText(currentUser.getCreatedAt());

        String avatar = currentUser.getAvatar();
        if (avatar!=null) {
            if(!avatar.isEmpty()) {
                if (!avatar.startsWith("http")) {
                    avatar = Dutch.PHOTO_URL + avatar;
                }
                Picasso.with(mContext).load(avatar).into(mProfilePhoto);
            } else {
                Picasso.with(mContext).load(R.drawable.wil_profile).into(mProfilePhoto);
            }
        } else {
            Picasso.with(mContext).load(R.drawable.wil_profile).into(mProfilePhoto);
        }

        mPostAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostAdActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false);
//      StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(mContext.getResources().getInteger(R.integer.channels_grid_columns), StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);


        if (MyToolBox.isNetworkAvailable(mContext)) {
            getUserDetails();
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
                    Log.e(TAG, adList.get(0).toString()+"");
                    Log.e(TAG, adList.get(0).getTitle() + " - " + adList.get(0).getUser().getName());
                    if (adList.size() == 0) {
                        mRecentAdsLabel.setText("You have no recent ads.");
                    } else {
                        mRecentAdsLabel.setText("Recent Ads");
                        mAdCount.setText(String.format("Ads posted: %s", adList.size()));
                    }
                    boolean isHorizontal = true;
                    mAdapter = new AdAdapter(adList, mContext, isHorizontal);
                    mRecyclerView.setAdapter(mAdapter);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    try {
                        Log.e(TAG, response.errorBody().string()+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    mEmptyLabel.setText("Network error. Please check your connection.");
                }
            }

            @Override
            public void onFailure(Call<List<Ad>> call, Throwable t) {
//                mEmptyLabel.setText("Network error. Please check your connection.");
                Log.e(TAG, t.getMessage()+"");
            }
        });
    }


    private void getUserDetails() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
