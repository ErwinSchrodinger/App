package com.together.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.together.app.R;

public class AppDialog extends Dialog {

    public static final int PROGRESS = 0;
    public static final int INFO = 1;
    public static final int ALERT = 2;

    private TextView mTextView;
    private boolean cancelable = true;
    private int style;
    private android.view.View.OnClickListener onClickListener;

    public AppDialog(Context context, int style, CharSequence message,
            android.view.View.OnClickListener onClickListener) {
        super(context);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        this.style = style;

        this.onClickListener = onClickListener;

        switch (style) {
        case PROGRESS:
            initProgressStyle();
            break;
        case INFO:
            initInfoStyle();
            break;
        case ALERT:
            initAlertStyle();
            break;
        default:
            break;
        }
        setCancelable(false);
        setDialogText(message);
    }

    private void initProgressStyle() {
        setContentView(R.layout.dialog_progress);
        mTextView = (TextView) findViewById(R.id.dialog_info);

    }

    private void initInfoStyle() {
        setContentView(R.layout.dialog_info);
        Button btnConfirmation = (Button) findViewById(R.id.confirmation);
        mTextView = (TextView) findViewById(R.id.dialog_info);
        btnConfirmation.setOnClickListener(onClickListener);
    }

    private void initAlertStyle() {
        setContentView(R.layout.dialog_alert);
        mTextView = (TextView) findViewById(R.id.dialog_info);
        Button btnConfirmation = (Button) findViewById(R.id.confirmation);
        btnConfirmation.setOnClickListener(onClickListener);
        Button btnCancel = (Button) findViewById(R.id.cancel);
        btnCancel.setOnClickListener(onClickListener);
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setDialogText(CharSequence text) {
        mTextView.setText(text);
    }

    public void setDialogText(int resid) {
        mTextView.setText(resid);
    }

    @Override
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !cancelable) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
