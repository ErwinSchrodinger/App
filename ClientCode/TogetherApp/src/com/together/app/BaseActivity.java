package com.together.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.together.app.net.AbstractModel;
import com.together.app.net.IModelListener;
import com.together.app.net.NetModel;
import com.together.app.ui.AppDialog;
import com.together.app.util.AppLog;
import com.together.app.util.Errors;

public abstract class BaseActivity extends Activity implements IModelListener {
    protected static final int MSG_ACTION_RESULT = 0;
    private TextView mTitle;
    private Button mRightBtn;

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
            if (null != errorMsg && 0 == errorMsg.length()) {
                showToast(errorMsg);
            } else {
                showToast(Errors.getLocalErrorMsg(errorCode));
            }
        }
    }

    protected void showProgress(int resid) {
        showProgress(getResources().getString(resid));
    }

    protected void showProgress(CharSequence message) {
        hideDialog();
        showDialog(this, AppDialog.PROGRESS, message);
    }

    private void showDialog(Context context, int style, CharSequence message) {
        if (this == mNetModel.getTopListner()) {
            mDialog = new AppDialog(context, style, message, mOnClickListener);
            mDialog.show();
        }
    }

    protected void showInfo(int resid) {
        showInfo(getResources().getString(resid));
    }

    protected void showInfo(CharSequence message) {
        hideDialog();
        showDialog(this, AppDialog.INFO, message);
    }

    protected void showAlert(int resid) {
        showAlert(getResources().getString(resid));
    }

    protected void showAlert(CharSequence message) {
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
            mTitle = (TextView) findViewById(R.id.title);
        }
        mTitle.setText(title);
    }

    @Override
    public void setTitle(int resid) {
        if (null == mTitle) {
            mTitle = (TextView) findViewById(R.id.title);
        }
        mTitle.setText(resid);
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
    };

    protected void onAlertConfirmed() {
    };

    protected void onAlertCanceled() {
    };

}
