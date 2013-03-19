package com.together.app;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.together.app.data.DataEngine;
import com.together.app.net.AbstractModel;
import com.together.app.net.SinaManager;
import com.together.app.net.TencentManager;
import com.together.app.util.Constant;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class MainActivity extends BaseActivity {

    private EditText mAccount;
    private EditText mPassword;
    private SinaManager mSinaManager;
    private TencentManager mTencentManager;

    private WeiboAuthListener mSinaAuthListener = new WeiboAuthListener() {

        @Override
        public void onComplete(Bundle values) {
            if (mSinaManager.isSessionValid()) {
                showProgressDialog(R.string.dialog_loading);
                getSinaInfo();
            } else {
                hideDialog();
                showToast(R.string.error_sina);
            }
        }

        @Override
        public void onError(WeiboDialogError e) {
            hideDialog();
            showToast(getText(R.string.error_sina) + "(" + e.getMessage() + ")");
        }

        @Override
        public void onCancel() {
            hideDialog();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            hideDialog();
            showToast(getText(R.string.error_sina) + "(" + e.getMessage() + ")");
        }

    };

    private IUiListener mTencentAuthListener = new IUiListener() {

        @Override
        public void onError(UiError arg0) {
            hideDialog();
            showToast(R.string.error_tencent);
        }

        @Override
        public void onComplete(JSONObject arg0) {
            if (mTencentManager.isSessionValid()) {
                showProgressDialog(R.string.dialog_loading);
                getTencentInfo();
            } else {
                hideDialog();
                showToast(R.string.error_tencent);
            }
        }

        @Override
        public void onCancel() {
            hideDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        mAccount = (EditText) findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);

        findViewById(R.id.loginBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        findViewById(R.id.regBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                register();
            }
        });

        mSinaManager = SinaManager.getInstance();
        findViewById(R.id.login_sina).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSinaManager.authorize(MainActivity.this, mSinaAuthListener);
            }
        });

        mTencentManager = TencentManager.getInstance();
        mTencentManager.init(this);
        findViewById(R.id.login_tencent).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mTencentManager.login(MainActivity.this,
                                mTencentAuthListener);
                    }
                });
    }

    @Override
    public void onActionResult(int action, boolean success) {
        super.onActionResult(action, success);
        switch (action) {
        case AbstractModel.MODEL_ACTION_LOGIN:
            hideDialog();
            if (success) {
                showToast(R.string.toast_login_success);
                Intent intent = new Intent(this, ContentActivity.class);
                startActivity(intent);
                finish();
            }
            break;
        case AbstractModel.MODEL_ACTION_SINA_UID:
            if (!success) {
                hideDialog();
            }
            break;
        case AbstractModel.MODEL_ACTION_SINA_INFO:
            if (!success) {
                hideDialog();
            } else {
                setDialogText(R.string.dialog_create_account);
                loginBySina();
            }
            break;
        case AbstractModel.MODEL_ACTION_TENCENT_INFO:
            if (!success) {
                hideDialog();
            } else {
                setDialogText(R.string.dialog_create_account);
                loginByTencent();
            }
            break;
        }
    }

    private void login() {
        Bundle data = new Bundle();
        if (0 == mAccount.getText().length()) {
            showToast(R.string.toast_account_empty);
            return;
        } else {
            data.putString(Constant.KEY_ACCOUNT, mAccount.getText().toString());
        }
        if (0 == mPassword.getText().length()) {
            showToast(R.string.toast_password_empty);
            return;
        } else {
            data.putString(Constant.KEY_PASSWORD, mPassword.getText()
                    .toString());
        }
        data.putInt(Constant.KEY_USER_TYPE, Constant.TYPE_USER_NORMAL);
        mNetModel.put(AbstractModel.KEY_SUBMIT_INFO, data);
        mNetModel.doAction(AbstractModel.MODEL_ACTION_LOGIN);
    }

    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void loginBySina() {
        Bundle data = new Bundle();
        data.putInt(Constant.KEY_USER_TYPE, Constant.TYPE_USER_SINA);
        data.putString(Constant.KEY_OPENID, DataEngine.getInstance()
                .getSinaUID());
        data.putString(Constant.KEY_NAME, DataEngine.getInstance()
                .getSinaName());
        data.putString(Constant.KEY_AVATAR, DataEngine.getInstance()
                .getSinaAvatar());
        mNetModel.put(AbstractModel.KEY_SUBMIT_INFO, data);
        mNetModel.doAction(AbstractModel.MODEL_ACTION_LOGIN);
    }

    private void loginByTencent() {
        Bundle data = new Bundle();
        data.putInt(Constant.KEY_USER_TYPE, Constant.TYPE_USER_TECENT);
        data.putString(Constant.KEY_OPENID, DataEngine.getInstance()
                .getTencentUID());
        data.putString(Constant.KEY_NAME, DataEngine.getInstance()
                .getTencentName());
        data.putString(Constant.KEY_AVATAR, DataEngine.getInstance()
                .getTencentAvatar());
        mNetModel.put(AbstractModel.KEY_SUBMIT_INFO, data);
        mNetModel.doAction(AbstractModel.MODEL_ACTION_LOGIN);
    }

    private void getSinaInfo() {
        mNetModel.doAction(AbstractModel.MODEL_ACTION_SINA_UID);
    }

    private void getTencentInfo() {
        mNetModel.doAction(AbstractModel.MODEL_ACTION_TENCENT_INFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TencentManager.getInstance().onActivityResult(requestCode, resultCode,
                data);
    }
}
