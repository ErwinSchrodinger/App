package com.together.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;

import com.together.app.data.DataEngine;
import com.together.app.data.EventSearchCondition;
import com.together.app.net.AbstractModel;
import com.together.app.ui.EventListAdapter;
import com.together.app.ui.PullToRefreshListView;
import com.together.app.ui.PullToRefreshListView.OnRefreshListener;
import com.together.app.util.Constant;
import com.together.app.util.SharePreferencesKeeper;

public class ContentActivity extends BaseActivity implements OnRefreshListener {
    private PullToRefreshListView mEventList;
    private EventListAdapter mEventListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        DataEngine engine = DataEngine.getInstance();
        setTitle(engine.getUserName());
        setAvatar(engine.getUserAvatar());
        showLoadingView();

        mEventList = (PullToRefreshListView) findViewById(R.id.event_list);
        mEventList.setOnRefreshListener(this);
        mEventListAdapter = new EventListAdapter(this);
        mEventList.setAdapter(mEventListAdapter);

        reloadEventList();
    }

    @Override
    public void onActionResult(int action, boolean success) {
        super.onActionResult(action, success);
        switch (action) {
        case AbstractModel.MODEL_ACTION_GET_EVENT_LIST:
            if (success) {
                if (0 == DataEngine.getInstance().getEventList().length) {
                    showEmptyView();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat(
                            "MM-dd HH:mm");
                    mEventList.onRefreshComplete(getResources().getString(
                            R.string.tip_pull_to_refresh_time,
                            format.format(calendar.getTime())));
                    mEventListAdapter.notifyDataSetChanged();
                    showContentView();
                }
            } else {
                showLoadFailView();
            }
            break;
        }
    }

    @Override
    protected void onReloadContent() {
        reloadEventList();
    }

    @Override
    public void onRefresh() {
        reloadEventList();
    }

    private void reloadEventList() {
        EventSearchCondition condition = SharePreferencesKeeper
                .getLastSearchCondition();
        Bundle data = new Bundle();
        data.putSerializable(Constant.KEY_SEARCH_CONDITION, condition);
        mNetModel.put(AbstractModel.KEY_SUBMIT_INFO, data);
        mNetModel.doAction(AbstractModel.MODEL_ACTION_GET_EVENT_LIST);
    }
}
