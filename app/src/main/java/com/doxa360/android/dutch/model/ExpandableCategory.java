//package com.doxa360.android.dutch.model;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.GsonBuilder;
//import com.google.gson.annotations.SerializedName;
//import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
//
//import java.util.List;
//
///**
// * Created by Apple on 28/12/2016.
// */
//
//public class ExpandableCategory extends ExpandableGroup<ExpandableCategory> implements Parcelable  {
//
//    @SerializedName("id")
//    private int mId;
//    @SerializedName("parent_id")
//    private int mParentId;
//    @SerializedName("published")
//    private int mPublished;
//    @SerializedName("title")
//    private String mTitle;
//    @SerializedName("alias")
//    private String mAlias;
//    @SerializedName("logo")
//    private String mLogo;
//    @SerializedName("font_awesome")
//    private String mFontAwesome;
//
////    public Category() {
////    }
////
////    public Category(int parentId, int published, String title, String alias, String logo, String fontAwesome) {
////        mParentId = parentId;
////        mPublished = published;
////        mTitle = title;
////        mAlias = alias;
////        mLogo = logo;
////        mFontAwesome = fontAwesome;
////    }
//
//    public ExpandableCategory(String title, List<ExpandableCategory> subCategories) {
//        super(title, subCategories);
//    }
//
//    public int getId() {
//        return mId;
//    }
//
//    public void setId(int id) {
//        mId = id;
//    }
//
//    public int getParentId() {
//        return mParentId;
//    }
//
//    public void setParentId(int parentId) {
//        mParentId = parentId;
//    }
//
//    public int getPublished() {
//        return mPublished;
//    }
//
//    public void setPublished(int published) {
//        mPublished = published;
//    }
//
//    public String getTitle() {
//        return mTitle;
//    }
//
//    public void setTitle(String title) {
//        mTitle = title;
//    }
//
//    public String getAlias() {
//        return mAlias;
//    }
//
//    public void setAlias(String alias) {
//        mAlias = alias;
//    }
//
//    public String getLogo() {
//        return mLogo;
//    }
//
//    public void setLogo(String logo) {
//        mLogo = logo;
//    }
//
//    public String getFontAwesome() {
//        return mFontAwesome;
//    }
//
//    public void setFontAwesome(String fontAwesome) {
//        mFontAwesome = fontAwesome;
//    }
//
//    @Override
//    public String toString() {
//        return new GsonBuilder().create().toJson(this, ExpandableCategory.class);
//    }
//
//    private ExpandableCategory(Parcel in) {
//        super(in);
//        mId = in.readInt();
//        mParentId = in.readInt();
//        mPublished = in.readInt();
//        mTitle = in.readString();
//        mAlias = in.readString();
//        mLogo = in.readString();
//        mFontAwesome = in.readString();
//
//    }
//
//    public static final Creator<ExpandableCategory> CREATOR = new Creator<ExpandableCategory>() {
//        @Override
//        public ExpandableCategory createFromParcel(Parcel in) {
//            return new ExpandableCategory(in);
//        }
//
//        @Override
//        public ExpandableCategory[] newArray(int size) {
//            return new ExpandableCategory[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(mId);
//        parcel.writeInt(mParentId);
//        parcel.writeInt(mPublished);
//        parcel.writeString(mTitle);
//        parcel.writeString(mAlias);
//        parcel.writeString(mLogo);
//        parcel.writeString(mFontAwesome);
//
//    }
//
//}
