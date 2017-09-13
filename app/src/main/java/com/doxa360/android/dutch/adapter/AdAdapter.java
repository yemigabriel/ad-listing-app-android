package com.doxa360.android.dutch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doxa360.android.dutch.AdDetail;
import com.doxa360.android.dutch.Dutch;
import com.doxa360.android.dutch.HomeActivity;
import com.doxa360.android.dutch.R;
import com.doxa360.android.dutch.model.Ad;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Apple on 09/01/16.
 */
public class AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {

    private static final String TAG = AdAdapter.class.getSimpleName();
    private List<Ad> mAdList;
    AdViewHolder mHolder;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private  boolean isHorizontal = false;

    public AdAdapter(List<Ad> adList, Context context) {
        mAdList = adList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public AdAdapter(List<Ad> adList, Context context, boolean isHorizontal) {
        mAdList = adList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.isHorizontal = isHorizontal;
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

        ImageView mPhoto;
        TextView mTitle;
        TextView mPrice;
        TextView mCategory;

        public AdViewHolder(View itemView) {
            super(itemView);

            mPhoto = (ImageView) itemView.findViewById(R.id.photo);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mPrice = (TextView) itemView.findViewById(R.id.price);
            mCategory = (TextView) itemView.findViewById(R.id.category);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AdDetail.class);
                    Log.e(TAG, "cat ad extra: "+ mAdList.get(getPosition()));
                    intent.putExtra("AD", mAdList.get(getPosition()));
                    mContext.startActivity(intent);
                }
            });

        }
    }

    @Override
    public AdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (isHorizontal) {
            view = mLayoutInflater.inflate(R.layout.ad_horizontal_layout, parent, false);
        } else {
            view = mLayoutInflater.inflate(R.layout.ad_layout, parent, false);
        }
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdViewHolder holder, int position) {
        Ad ad = mAdList.get(position);
        mHolder = holder;
//        String price;
//        if (ad.getPrice() == 0) {
//            price = "Contact for price";
//        }
//        else {
//            price = "N " + ad.getPrice();
//        }
        holder.mTitle.setText(ad.getTitle());
        holder.mPrice.setText(ad.getFormattedPrice());
        Log.e(TAG, ad.getPriceFormatted());
        if (ad.getCategory()!=null) {
            holder.mCategory.setText(ad.getCategory().getTitle() + "");
        } else {
            holder.mCategory.setVisibility(View.INVISIBLE);
        }

        Picasso.with(mContext)
                .load(Dutch.PHOTO_URL+ad.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.mPhoto);

    }

    @Override
    public int getItemCount() {
        if (mAdList != null)
            return mAdList.size();
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
