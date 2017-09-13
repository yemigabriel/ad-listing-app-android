package com.doxa360.android.dutch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;

/**
 * Created by Apple on 28/12/2016.
 */

public class Ad implements Parcelable {

    @SerializedName("id")
    private int mId;
    @SerializedName("category_id")
    private int mCategoryId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("alias")
    private String mAlias;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("price")
    private float mPrice;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("address1")
    private String mAddress1;
    @SerializedName("address2")
    private String mAddress2;
    @SerializedName("address3")
    private String mAddress3;
    @SerializedName("views")
    private int mViews;
    @SerializedName("published")
    private int mPublished;
    @SerializedName("user_id")
    private int mUserId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("category")
    private Category mCategory;
    @SerializedName("user")
    private User mUser;
    @SerializedName("images")
    private ArrayList<AdImage> mImages;

    private String mPriceFormatted;

    public Ad() {
    }

    public Ad(int categoryId, String title, String description, float price, String phone,
              String address1, String address2, String address3, int published, int userId, String image) {
        mCategoryId = categoryId;
        mTitle = title;
        mDescription = description;
        mPrice = price;
        mPhone = phone;
        mAddress1 = address1;
        mAddress2 = address2;
        mAddress3 = address3;
        mPublished = published;
        mUserId = userId;
        mImage = image;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public String getTitle() {
        String[] strArray = mTitle.split(" ");
        StringBuilder builder = new StringBuilder();

        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }
        return builder.toString();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAlias() {
        return mAlias;
    }

    public void setAlias(String alias) {
        mAlias = alias;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float price) {
        mPrice = price;
    }

    public String getPriceFormatted() {
        return String.format("%.2f", mPrice);
    }

    public String getFormattedPrice() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setCurrency(Currency.getInstance("NGN"));
        String formattedPrice;
        if (mPrice == 0) {
            formattedPrice = "Contact for price";
        }
        else {
            formattedPrice = numberFormat.format(mPrice);
        }
        return formattedPrice;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getAddress1() {
        return mAddress1;
    }

    public void setAddress1(String address1) {
        mAddress1 = address1;
    }

    public String getAddress2() {
        return mAddress2;
    }

    public void setAddress2(String address2) {
        mAddress2 = address2;
    }

    public String getAddress3() {
        return mAddress3;
    }

    public void setAddress3(String address3) {
        mAddress3 = address3;
    }

    public int getViews() {
        return mViews;
    }

    public void setViews(int views) {
        mViews = views;
    }

    public int getPublished() {
        return mPublished;
    }

    public void setPublished(int published) {
        mPublished = published;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category category) {
        mCategory = category;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public ArrayList<AdImage> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<AdImage> images) {
        mImages = images;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, Ad.class);
    }

    private Ad(Parcel in) {
        mImages = in.readArrayList(AdImage.class.getClassLoader());
        mUser = in.readParcelable(User.class.getClassLoader());
        mCategory = in.readParcelable(Category.class.getClassLoader());
        mId = in.readInt();
        mCategoryId = in.readInt();
        mPublished = in.readInt();
        mUserId = in.readInt();
        mViews = in.readInt();
        mTitle = in.readString();
        mAlias = in.readString();
        mDescription = in.readString();
        mPrice = in.readFloat();
//        mPriceFormatted = in.readString();
        mPhone = in.readString();
        mImage = in.readString();
        mAddress1 = in.readString();
        mAddress2 = in.readString();
        mAddress3 = in.readString();
        mCreatedAt = in.readString();
    }

    public static final Creator<Ad> CREATOR = new Creator<Ad>() {
        @Override
        public Ad createFromParcel(Parcel in) {
            return new Ad(in);
        }

        @Override
        public Ad[] newArray(int size) {
            return new Ad[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(mImages);
        parcel.writeParcelable(mUser, i);
        parcel.writeParcelable(mCategory, i);
        parcel.writeInt(mId);
        parcel.writeInt(mCategoryId);
        parcel.writeInt(mPublished);
        parcel.writeInt(mUserId);
        parcel.writeInt(mViews);
        parcel.writeString(mTitle);
        parcel.writeString(mAlias);
        parcel.writeString(mDescription);
        parcel.writeFloat(mPrice);
//        parcel.writeString(mPriceFormatted);
        parcel.writeString(mPhone);
        parcel.writeString(mImage);
        parcel.writeString(mAddress1);
        parcel.writeString(mAddress2);
        parcel.writeString(mAddress3);
        parcel.writeString(mCreatedAt);

    }

}
