package com.doxa360.android.dutch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doxa360.android.dutch.Dutch;
import com.doxa360.android.dutch.R;
import com.doxa360.android.dutch.model.State;

import java.util.List;

/**
 * Created by Apple on 09/01/16.
 */
public class StateAdapter extends RecyclerView.Adapter<StateAdapter.AdViewHolder> {

    private static final String TAG = StateAdapter.class.getSimpleName();
    private List<State> mStateList;
    AdViewHolder mHolder;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public StateAdapter(List<State> stateList, Context context) {
        mStateList = stateList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

//        ImageView mPhoto;
        TextView mTitle;
//        TextView mCount;
//        TextView mParentCategory;

        public AdViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.state_title);
//            mCount = (TextView) itemView.findViewById(R.id.ad_count);
//            mParentCategory = (TextView) itemView.findViewById(R.id.parent_category);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext.getString(R.string.pick_state));
                    intent.putExtra(Dutch.STATE, mStateList.get(getPosition()));
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            });

        }
    }

    @Override
    public AdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.state_layout, parent, false);
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdViewHolder holder, int position) {
        State state = mStateList.get(position);
        mHolder = holder;

        holder.mTitle.setText(state.getStateName());
//        holder.mCount.setText(category.getId()+"");
//        holder.mParentCategory.setText(category.getAlias());


    }

    @Override
    public int getItemCount() {
        if (mStateList != null)
            return mStateList.size();
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
