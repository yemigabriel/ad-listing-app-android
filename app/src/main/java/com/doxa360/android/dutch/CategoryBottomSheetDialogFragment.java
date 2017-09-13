package com.doxa360.android.dutch;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doxa360.android.dutch.adapter.CategoryAdapter;
import com.doxa360.android.dutch.helpers.DutchSharedPref;
import com.doxa360.android.dutch.model.Category;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryBottomSheetDialogFragment extends BottomSheetDialogFragment {


    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mEmptyLabel;

    private DutchApiInterface mDutchApiInterface;
    private DutchSharedPref mDutchSharedPref;
    private List<Category> categoryList;
    private CategoryAdapter mAdapter;

    public CategoryBottomSheetDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category_bottom_sheet_dialog, container, false);

        mDutchSharedPref = new DutchSharedPref(mContext);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.category_recyclerview);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mEmptyLabel = (TextView) rootView.findViewById(R.id.empty_label);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        getCategories();

        return rootView;
    }

    private void getCategories() {
        Log.e(TAG, "categories");
        mDutchApiInterface = DutchApiClient.getClient().create(DutchApiInterface.class);

        Call<List<Category>> call = mDutchApiInterface.allCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                Log.e(TAG, response.message() + response.body().size());
                if (response.isSuccessful()) {
                    categoryList = response.body();
                    Log.e(TAG, categoryList.get(0).toString()+"");
                    mAdapter = new CategoryAdapter(categoryList, mContext);
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
            public void onFailure(Call<List<Category>> call, Throwable t) {
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
}
