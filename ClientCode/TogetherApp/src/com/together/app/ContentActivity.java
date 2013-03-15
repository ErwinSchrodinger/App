package com.together.app;

import android.os.Bundle;
import android.widget.TextView;

import com.together.app.data.DataEngine;
import com.together.app.ui.UrlImageView;

public class ContentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        DataEngine engine = DataEngine.getInstance();
        ((TextView) (findViewById(R.id.name))).setText(engine.getUserName());
        ((UrlImageView) (findViewById(R.id.avatar))).setImageFromUrl(engine
                .getUserAvatar());
        ((TextView) (findViewById(R.id.lastLoginTime))).setText(engine
                .getLastLoginTime(getString(R.string.format_last_login_time)));
        setTitle(engine.getUserName());
    }
}
