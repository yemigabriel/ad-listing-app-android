package com.doxa360.android.dutch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Apple on 28/12/2016.
 */

public class User implements Parcelable {

    @SerializedName("id")
    private int mId;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("name")
    private String mName;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("avatar")
    private String mAvatar;
    @SerializedName("provider")
    private String mProvider;
    @SerializedName("provider_id")
    private String mProviderId;
    @SerializedName("role")
    private String mRole;
//    @SerializedName("create")
//    private float mDistance;
//    @SerializedName("created_at")
//    private long mCreatedAt;


    public User() {
    }

    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public User(String email, String password, String name) {
        mEmail = email;
        mPassword = password;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getProvider() {
        return mProvider;
    }

    public void setProvider(String provider) {
        mProvider = provider;
    }

    public String getProviderId() {
        return mProviderId;
    }

    public void setProviderId(String providerId) {
        mProviderId = providerId;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public String getRole() {
        return mRole;
    }

    public void setRole(String role) {
        mRole = role;
    }

//    public long getCreatedAt() {
//        return mCreatedAt;
//    }
//
//    public void setCreatedAt(long created_at) {
//        mCreatedAt = created_at;
//    }

//    public String getJSON() {
//        Gson gson = new Gson();
//        String json = gson.toJson(User.class);
//        return json;
//    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, User.class);
    }

    private User(Parcel in) {
        mId = in.readInt();
        mEmail = in.readString();
        mName = in.readString();
        mPhone = in.readString();
        mAvatar = in.readString();
        mProvider = in.readString();
        mProviderId = in.readString();
        mRole = in.readString();
//        mCreatedAt = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mEmail);
        parcel.writeString(mName);
        parcel.writeString(mPhone);
        parcel.writeString(mAvatar);
        parcel.writeString(mProvider);
        parcel.writeString(mProviderId);
        parcel.writeString(mRole);
//        parcel.writeLong(mCreatedAt);

    }

}
