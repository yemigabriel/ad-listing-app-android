package com.doxa360.android.dutch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Apple on 28/12/2016.
 */

public class State implements Parcelable {

    @SerializedName("state_id")
    private int mStateId;
    @SerializedName("lg_id")
    private int mLgId;
    @SerializedName("state_name")
    private String mStateName;
    @SerializedName("lg_name")
    private String mLgName;

    public State() {
    }

    public int getStateId() {
        return mStateId;
    }

    public void setStateId(int stateId) {
        mStateId = stateId;
    }

    public int getLgId() {
        return mLgId;
    }

    public void setLgId(int lgId) {
        mLgId = lgId;
    }

    public String getStateName() {
        return mStateName;
    }

    public void setStateName(String stateName) {
        mStateName = stateName;
    }

    public String getLgName() {
        return mLgName;
    }

    public void setLgName(String lgName) {
        mLgName = lgName;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, State.class);
    }

    private State(Parcel in) {
        mStateId = in.readInt();
        mLgId = in.readInt();
        mStateName= in.readString();
        mLgName = in.readString();
    }

    public static final Creator<State> CREATOR = new Creator<State>() {
        @Override
        public State createFromParcel(Parcel in) {
            return new State(in);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mStateId);
        parcel.writeInt(mLgId);
        parcel.writeString(mStateName);
        parcel.writeString(mLgName);
    }

}
