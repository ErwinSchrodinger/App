package com.together.app.data;

import java.io.Serializable;

import com.together.app.util.Constant;

public class EventSearchCondition implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5653297919812678943L;

    private int mEventType = Constant.SEARCH_ALL;
    private int mAreaID = Constant.SEARCH_ALL;
    private int mCostType = Constant.SEARCH_ALL;
    private long mStartDate = Constant.SEARCH_ALL;
    private long mEndDate = Constant.SEARCH_ALL;

    public EventSearchCondition() {
    }

    public EventSearchCondition(int eventType, int areaID, int costType,
            int startDate, int endDate) {
        mEventType = eventType;
        mAreaID = areaID;
        mCostType = costType;
        mStartDate = startDate;
        mEndDate = endDate;
    }

    public int getEventType() {
        return mEventType;
    }

    public void setEventType(int eventType) {
        mEventType = eventType;
    }

    public int getAreaID() {
        return mAreaID;
    }

    public void setAreaID(int areaID) {
        mAreaID = areaID;
    }

    public int getCostType() {
        return mCostType;
    }

    public void setCostType(int costType) {
        mCostType = costType;
    }

    public long getStartDate() {
        return mStartDate;
    }

    public void setStartDate(long startDate) {
        mStartDate = startDate;
    }

    public long getEndDate() {
        return mEndDate;
    }

    public void setEndDate(long endDate) {
        mEndDate = endDate;
    }

}
