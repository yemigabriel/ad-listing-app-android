package com.doxa360.android.dutch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.doxa360.android.dutch.CategoryDetailActivity;
import com.doxa360.android.dutch.Dutch;
import com.doxa360.android.dutch.model.Category;
import com.doxa360.android.dutch.R;

import java.util.List;

/**
 * Created by Apple on 09/01/16.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.AdViewHolder> {

    private static final String TAG = CategoryAdapter.class.getSimpleName();
    private List<Category> mCategoryList;
    AdViewHolder mHolder;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private boolean isFragment;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        mCategoryList = categoryList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.isFragment = false;
    }

    public CategoryAdapter(List<Category> categoryList, Context context, boolean isFragment) {
        mCategoryList = categoryList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.isFragment = isFragment;
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

//        ImageView mPhoto;
        TextView mTitle;
        TextView mCount;
        TextView mParentCategory;

        public AdViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.category_title);
            mCount = (TextView) itemView.findViewById(R.id.ad_count);
            mParentCategory = (TextView) itemView.findViewById(R.id.parent_category);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFragment) {
//                        Toast.makeText(mContext, "Items in category", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, CategoryDetailActivity.class);
                        Log.e(TAG, mCategoryList.get(getPosition()).toString());
                        intent.putExtra(Dutch.CATEGORY, mCategoryList.get(getPosition()));
                        mContext.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(mContext.getString(R.string.pick_category));
                        intent.putExtra(Dutch.CATEGORY, mCategoryList.get(getPosition()));
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }
                }
            });

        }
    }

    @Override
    public AdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.category_layout, parent, false);
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdViewHolder holder, int position) {
        Category category = mCategoryList.get(position);
        mHolder = holder;

        holder.mTitle.setText(category.getTitle());
        holder.mCount.setText(category.getItems().size()+"");
        holder.mParentCategory.setText(category.getParent().getTitle());


    }

    @Override
    public int getItemCount() {
        if (mCategoryList != null)
            return mCategoryList.size();
        else
            return 0;
    }

//    private double getDistanceApartKm(double lat1, double lon1, double lat2, double lon2) {
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//        return (dist);
//    }
//
//    private double deg2rad(double deg) {
//        return (deg * Math.PI / 180.0);
//    }
//    private double rad2deg(double rad) {
//        return (rad * 180.0 / Math.PI);
//    }







}
