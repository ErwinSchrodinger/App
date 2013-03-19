package com.together.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.together.app.R;

public class PullToRefreshListView extends ListView implements OnScrollListener {
    private final static int RELEASE_To_REFRESH = 0;
    private final static int PULL_To_REFRESH = 1;
    // 正在刷新
    private final static int REFRESHING = 2;
    // 刷新完成
    private final static int DONE = 3;
    private final static int LOADING = 4;

    private final static int RATIO = 3;
    private LayoutInflater mInflater;
    private ViewGroup mHeadView;
    private TextView mTipsTextview;
    private TextView mLastUpdatedTextView;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private View mFootLoadingView;
    private View mMoreView;

    private RotateAnimation mArrowAnimation;
    private RotateAnimation mArrowReverseAnimation;
    private boolean isRecored;
    private int mHeadContentHeight;
    private int mStartY;
    private int mFirstItemIndex;
    private int mState;
    private boolean isBack;
    private OnRefreshListener mOnRefreshListener;
    private boolean isRefreshable;

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mInflater = LayoutInflater.from(context);
        mHeadView = (ViewGroup) mInflater.inflate(R.layout.view_list_header,
                null);
        mArrowImageView = (ImageView) mHeadView
                .findViewById(R.id.head_arrowImageView);
        mArrowImageView.setMinimumWidth(70);
        mArrowImageView.setMinimumHeight(50);
        mProgressBar = (ProgressBar) mHeadView
                .findViewById(R.id.head_progressBar);
        mTipsTextview = (TextView) mHeadView
                .findViewById(R.id.head_tipsTextView);
        mLastUpdatedTextView = (TextView) mHeadView
                .findViewById(R.id.head_lastUpdatedTextView);

        measureView(mHeadView);
        mLastUpdatedTextView.setVisibility(View.GONE);
        mHeadContentHeight = mHeadView.getMeasuredHeight();
        mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
        mHeadView.invalidate();

        addHeaderView(mHeadView, null, false);

        View footer = mInflater.inflate(R.layout.view_list_footer, null);
        mFootLoadingView = footer.findViewById(R.id.footer_loadingView);
        mMoreView = footer.findViewById(R.id.footer_more);

        addFooterView(footer, null, true);

        setOnScrollListener(this);

        mArrowAnimation = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mArrowAnimation.setInterpolator(new LinearInterpolator());
        mArrowAnimation.setDuration(250);
        mArrowAnimation.setFillAfter(true);

        mArrowReverseAnimation = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mArrowReverseAnimation.setInterpolator(new LinearInterpolator());
        mArrowReverseAnimation.setDuration(200);
        mArrowReverseAnimation.setFillAfter(true);

        mState = DONE;
        isRefreshable = false;
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
            int arg3) {
        mFirstItemIndex = firstVisiableItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRefreshable) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mFirstItemIndex == 0 && !isRecored) {
                    isRecored = true;
                    mStartY = (int) event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mState != REFRESHING && mState != LOADING) {
                    if (mState == DONE) {
                    }
                    if (mState == PULL_To_REFRESH) {
                        mState = DONE;
                        changeHeaderViewByState();
                    }
                    if (mState == RELEASE_To_REFRESH) {
                        mState = REFRESHING;
                        changeHeaderViewByState();
                        onRefresh();
                    }
                }
                isRecored = false;
                isBack = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int tempY = (int) event.getY();
                if (!isRecored && mFirstItemIndex == 0) {
                    isRecored = true;
                    mStartY = tempY;
                }
                if (mState != REFRESHING && isRecored && mState != LOADING) {
                    if (mState == RELEASE_To_REFRESH) {
                        setSelection(0);
                        if (((tempY - mStartY) / RATIO < mHeadContentHeight)
                                && (tempY - mStartY) > 0) {
                            mState = PULL_To_REFRESH;
                            changeHeaderViewByState();
                        } else if (tempY - mStartY <= 0) {
                            mState = DONE;
                            changeHeaderViewByState();
                        }
                    }
                    if (mState == PULL_To_REFRESH) {
                        setSelection(0);
                        if ((tempY - mStartY) / RATIO >= mHeadContentHeight) {
                            mState = RELEASE_To_REFRESH;
                            isBack = true;
                            changeHeaderViewByState();
                        } else if (tempY - mStartY <= 0) {
                            mState = DONE;
                            changeHeaderViewByState();
                        }
                    }
                    if (mState == DONE) {
                        if (tempY - mStartY > 0) {
                            mState = PULL_To_REFRESH;
                            changeHeaderViewByState();
                        }
                    }
                    if (mState == PULL_To_REFRESH) {
                        mHeadView.setPadding(0, -1 * mHeadContentHeight
                                + (tempY - mStartY) / RATIO, 0, 0);
                    }
                    if (mState == RELEASE_To_REFRESH) {
                        mHeadView.setPadding(0, (tempY - mStartY) / RATIO
                                - mHeadContentHeight, 0, 0);
                    }
                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void changeHeaderViewByState() {
        switch (mState) {
        case RELEASE_To_REFRESH:
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mTipsTextview.setVisibility(View.VISIBLE);
            // mLastUpdatedTextView.setVisibility(View.VISIBLE);
            mArrowImageView.clearAnimation();
            mArrowImageView.startAnimation(mArrowAnimation);
            mTipsTextview.setText(R.string.tip_pull_to_refresh_release);
            break;
        case PULL_To_REFRESH:
            mProgressBar.setVisibility(View.GONE);
            mTipsTextview.setVisibility(View.VISIBLE);
            // mLastUpdatedTextView.setVisibility(View.VISIBLE);
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.VISIBLE);
            mTipsTextview.setText(R.string.tip_pull_to_refresh_pull);
            if (isBack) {
                isBack = false;
                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mArrowReverseAnimation);
            }
            break;
        case REFRESHING:
            mHeadView.setPadding(0, 0, 0, 0);
            mProgressBar.setVisibility(View.VISIBLE);
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.GONE);
            mTipsTextview.setText(R.string.tip_pull_to_refresh_refreshing);
            // mLastUpdatedTextView.setVisibility(View.VISIBLE);
            break;
        case DONE:
            mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
            mProgressBar.setVisibility(View.GONE);
            mArrowImageView.clearAnimation();
            mArrowImageView.setImageResource(R.drawable.z_arrow_down);
            // mLastUpdatedTextView.setVisibility(View.VISIBLE);
            break;
        }
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        mOnRefreshListener = refreshListener;
        isRefreshable = true;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    public void onRefreshComplete(CharSequence text) {
        mLastUpdatedTextView.setText(text);
        mLastUpdatedTextView.setVisibility(View.VISIBLE);
        onRefreshComplete();
    }

    public void onRefreshComplete() {
        mState = DONE;
        changeHeaderViewByState();
    }

    private void onRefresh() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    // public void setAdapter(BaseAdapter adapter) {
    // super.setAdapter(adapter);
    // }
}