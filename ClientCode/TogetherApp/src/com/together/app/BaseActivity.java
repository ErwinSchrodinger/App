package com.together.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.together.app.net.AbstractModel;
import com.together.app.net.IModelListener;
import com.together.app.net.NetModel;
import com.together.app.ui.AppDialog;
import com.together.app.ui.UrlImageView;
import com.together.app.util.AppLog;
import com.together.app.util.Errors;

public abstract class BaseActivity extends Activity implements IModelListener {
    protected static final int MSG_ACTION_RESULT = 0;
    private TextView mTitle;
    private UrlImageView mAvatar;
    private Button mRightBtn;

    private View mLoadingView;
    private View mContentView;
    private View mLoadFailView;
    private View mLoadEmptyView;
    private TextView mReloadBtn;

    protected NetModel mNetModel;
    protected AppDialog mDialog;
    private Toast mToast;

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.title_btn:
                onRightBtnClicked(v);
                break;
            case R.id.confirmation:
                mDialog.dismiss();
                onAlertConfirmed();
                break;
            case R.id.cancel:
                mDialog.dismiss();
                onAlertCanceled();
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetModel = NetModel.getInstance();
        mNetModel.addListener(this);
    }

    @Override
    public void onActionResult(int action, boolean success) {
        AppLog.d("onActionResult:" + action + "," + success);
        if (!success) {
            String errorCode = mNetModel
                    .getString(AbstractModel.KEY_DIALOG_ERROR_CODE);
            String errorMsg = mNetModel
                    .getString(AbstractModel.KEY_DIALOG_ERROR_MSG);
            if (null != errorMsg && 0 != errorMsg.length()) {
                showToast(errorMsg);
            } else {
                showToast(Errors.getLocalErrorMsg(errorCode));
            }
        }
    }

    protected void showLoadingView() {
        if (null == mLoadingView) {
            initLoadingView();
        }
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadFailView.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
        mLoadEmptyView.setVisibility(View.GONE);
    }

    protected void showLoadFailView() {
        if (null == mLoadingView) {
            initLoadingView();
        }
        mLoadFailView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
        mLoadEmptyView.setVisibility(View.GONE);
    }

    protected void showContentView() {
        if (null == mLoadingView) {
            initLoadingView();
        }
        mContentView.setVisibility(View.VISIBLE);
        mLoadFailView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mLoadEmptyView.setVisibility(View.GONE);
    }

    protected void showEmptyView() {
        if (null == mLoadingView) {
            initLoadingView();
        }
        mLoadEmptyView.setVisibility(View.VISIBLE);
        mLoadFailView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
    }

    private void initLoadingView() {
        mLoadingView = findViewById(R.id.loading_view);
        mLoadFailView = findViewById(R.id.loading_fail_view);
        mLoadEmptyView = findViewById(R.id.loading_empty_view);
        mContentView = findViewById(R.id.content_view);
        mReloadBtn = (TextView) findViewById(R.id.reloadBtn);
        mReloadBtn.setText(Html.fromHtml(getString(R.string.tip_reload)));
        mReloadBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showLoadingView();
                onReloadContent();
            }
        });
    }

    protected void showProgressDialog(int resid) {
        showProgressDialog(getResources().getString(resid));
    }

    protected void showProgressDialog(CharSequence message) {
        hideDialog();
        showDialog(this, AppDialog.PROGRESS, message);
    }

    private void showDialog(Context context, int style, CharSequence message) {
        if (this == mNetModel.getTopListner()) {
            mDialog = new AppDialog(context, style, message, mOnClickListener);
            mDialog.show();
        }
    }

    protected void showInfoDialog(int resid) {
        showInfoDialog(getResources().getString(resid));
    }

    protected void showInfoDialog(CharSequence message) {
        hideDialog();
        showDialog(this, AppDialog.INFO, message);
    }

    protected void showAlertDialog(int resid) {
        showAlertDialog(getResources().getString(resid));
    }

    protected void showAlertDialog(CharSequence message) {
        hideDialog();
        showDialog(this, AppDialog.ALERT, message);
    }

    protected void hideDialog() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    protected void setDialogText(CharSequence text) {
        if (null != mDialog) {
            mDialog.setDialogText(text);
        }
    }

    protected void setDialogText(int resid) {
        setDialogText(getResources().getString(resid));
    }

    public void showToast(String msg) {
        cancelToast();
        mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void showToast(int resid) {
        showToast(getResources().getString(resid));
    }

    public void cancelToast() {
        if (null != mToast) {
            mToast.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != mNetModel) {
            mNetModel.removeListener(this);
        }
        cancelToast();
        hideDialog();
        super.onDestroy();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (null == mTitle) {
            mTitle = (TextView) findViewById(R.id.title_text);
        }
        mTitle.setText(title);
    }

    @Override
    public void setTitle(int resid) {
        if (null == mTitle) {
            mTitle = (TextView) findViewById(R.id.title_text);
        }
        mTitle.setText(resid);
    }

    public void setAvatar(int resid) {
        if (null == mAvatar) {
            mAvatar = (UrlImageView) findViewById(R.id.title_avatar);
        }
        mAvatar.setImageResource(resid);
    }

    public void setAvatar(String url) {
        if (null == mAvatar) {
            mAvatar = (UrlImageView) findViewById(R.id.title_avatar);
            int avatarSize = getResources().getDimensionPixelSize(
                    R.dimen.title_avatar_size);
            mAvatar.setImageHeight(avatarSize);
            mAvatar.setImageWidth(avatarSize);
        }
        mAvatar.setImageFromUrl(url);
    }

    public void showTitleButton(String text) {
        if (null == mRightBtn) {
            mRightBtn = (Button) findViewById(R.id.title_btn);
            mRightBtn.setOnClickListener(mOnClickListener);
        }
        mRightBtn.setText(text);
        mRightBtn.setVisibility(View.VISIBLE);
    }

    public void hideTitleButton() {
        mRightBtn.setVisibility(View.GONE);
    }

    protected void onRightBtnClicked(View v) {
    }

    protected void onAlertConfirmed() {
    }

    protected void onAlertCanceled() {
    }

    protected void onReloadContent() {
    }
}
