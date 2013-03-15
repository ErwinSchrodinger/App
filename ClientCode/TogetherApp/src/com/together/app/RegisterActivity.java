package com.together.app;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.together.app.net.AbstractModel;
import com.together.app.util.Constant;

public class RegisterActivity extends BaseActivity {

    private EditText mAccount;
    private EditText mPassword;
    private EditText mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.title_register);
        mAccount = (EditText) findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);
        mName = (EditText) findViewById(R.id.name);

        findViewById(R.id.regBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    @Override
    public void onActionResult(int action, boolean success) {
        super.onActionResult(action, success);
        if (AbstractModel.MODEL_ACTION_REGISTER == action && success) {
            showToast(R.string.toast_register_success);
            finish();
        }
    }

    private void register() {
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
        if (0 == mName.getText().length()) {
            showToast(R.string.toast_name_empty);
            return;
        } else {
            data.putString(Constant.KEY_NAME, mName.getText().toString());
        }
        data.putInt(Constant.KEY_SEX, 0);
        mNetModel.put(AbstractModel.KEY_SUBMIT_INFO, data);
        mNetModel.doAction(AbstractModel.MODEL_ACTION_REGISTER);
    }

}
