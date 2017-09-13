package com.doxa360.android.dutch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Apple on 28/12/2016.
 */

public class AdImage implements Parcelable {

    @SerializedName("id")
    private int mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("s3key")
    private String mS3key;
    @SerializedName("item_id")
    private int mItemId;
    @SerializedName("published")
    private int mPublished;

    public AdImage() {
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getS3key() {
        return mS3key;
    }

    public void setS3key(String s3key) {
        mS3key = s3key;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public int getPublished() {
        return mPublished;
    }

    public void setPublished(int published) {
        mPublished = published;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, AdImage.class);
    }

    private AdImage(Parcel in) {
        mId = in.readInt();
        mItemId = in.readInt();
        mPublished = in.readInt();
        mImage = in.readString();
        mS3key = in.readString();
    }

    public static final Creator<AdImage> CREATOR = new Creator<AdImage>() {
        @Override
        public AdImage createFromParcel(Parcel in) {
            return new AdImage(in);
        }

        @Override
        public AdImage[] newArray(int size) {
            return new AdImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeInt(mItemId);
        parcel.writeInt(mPublished);
        parcel.writeString(mImage);
        parcel.writeString(mS3key);
    }

}
