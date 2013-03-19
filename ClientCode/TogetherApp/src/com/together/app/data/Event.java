package com.together.app.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.together.app.util.Constant;

public class Event {
    private int mID;
    private int mInterestCount;
    private int mJoinCount;
    private String mTitle;
    private String mAddress;
    private String mAuthor;
    private String mImg;
    private String mEventTime;
    private long mStartDate;
    private long mEndDate;
    private long mPublishDate;

    private int mPersonLimit;
    private int mEventType;
    private int mAreaId;
    private String mArea;
    private int mCostType;
    private String mCost;
    private String mContact;
    private String mIntro;
    private Double mLatitude;
    private Double mLongtitude;

    public int getEventId() {
        return mID;
    }

    public int getInterestCount() {
        return mInterestCount;
    }

    public int getJoinCount() {
        return mJoinCount;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getImg() {
        return mImg;
    }

    public String getEventTime() {
        return mEventTime;
    }

    public long getStartDate() {
        return mStartDate;
    }

    public long getEndDate() {
        return mEndDate;
    }

    public long getPublishDate() {
        return mPublishDate;
    }

    public int getPersonLimit() {
        return mPersonLimit;
    }

    public int getEventType() {
        return mEventType;
    }

    public int getAreaId() {
        return mAreaId;
    }

    public String getArea() {
        return mArea;
    }

    public int getCostType() {
        return mCostType;
    }

    public String getCost() {
        return mCost;
    }

    public String getContact() {
        return mContact;
    }

    public String getIntro() {
        return mIntro;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public Double getLongtitude() {
        return mLongtitude;
    }

    public void updateEvent(Event event) {
        mID = event.getEventId();
        mInterestCount = event.getInterestCount();
        mJoinCount = event.getJoinCount();
        mTitle = event.getTitle();
        mAddress = event.getAddress();
        mAuthor = event.getAuthor();
        mImg = event.getImg();
        mEventTime = event.getEventTime();
        mStartDate = event.getStartDate();
        mEndDate = event.getEndDate();
        mPublishDate = event.getPublishDate();
        mPersonLimit = event.getPersonLimit();
        mEventType = event.getEventType();
        mAreaId = event.getAreaId();
        mArea = event.getArea();
        mCostType = event.getCostType();
        mCost = event.getCost();
        mIntro = event.getIntro();
        mLatitude = event.getLatitude();
        mLongtitude = event.getLongtitude();
    }

    public static Event initFromList(JSONObject json) throws JSONException {
        Event e = new Event();
        e.mID = json.getInt(Constant.KEY_EVENT_ID);
        e.mInterestCount = json.getInt(Constant.KEY_INTEREST_COUNT);
        e.mJoinCount = json.getInt(Constant.KEY_JOIN_COUNT);
        e.mTitle = json.getString(Constant.KEY_TITLE);
        e.mAddress = json.getString(Constant.KEY_ADDRESS);
        e.mAuthor = json.getString(Constant.KEY_AUTHOR);
        e.mImg = json.getString(Constant.KEY_IMG);
        e.mEventTime = json.getString(Constant.KEY_EVENT_TIME);
        e.mStartDate = json.getLong(Constant.KEY_START_DATE);
        e.mEndDate = json.getLong(Constant.KEY_END_DATE);
        e.mPublishDate = json.getLong(Constant.KEY_PUBLISH_DATE);
        return e;
    }

    public static Event initFromDetail(JSONObject json) throws JSONException {
        Event e = initFromList(json);
        e.mPersonLimit = json.getInt(Constant.KEY_PERSON_LIMIT);
        e.mEventType = json.getInt(Constant.KEY_EVENT_TYPE);
        e.mAreaId = json.getInt(Constant.KEY_AREA_ID);
        e.mArea = json.getString(Constant.KEY_AREA);
        e.mCostType = json.getInt(Constant.KEY_COST_TYPE);
        e.mCost = json.getString(Constant.KEY_COST);
        e.mIntro = json.getString(Constant.KEY_INTRO);
        e.mLatitude = json.getDouble(Constant.KEY_LATITUDE);
        e.mLongtitude = json.getDouble(Constant.KEY_LONGTITUDE);
        return e;
    }
}
