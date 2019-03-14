package com.maxwen.daggerexample.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.maxwen.daggerexample.R;

public class SelectView extends LinearLayout {

    private Button mBuildImageButton;
    private Button mOwmButton;
    private SelectController mController;

    public SelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setController(SelectController controller) {
        mController = controller;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mOwmButton = findViewById(R.id.owm_view);
        mOwmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.switchToOwm();
            }
        });
        mBuildImageButton = findViewById(R.id.build_image_view);
        mBuildImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.switchToBuildImages();
            }
        });
    }

}