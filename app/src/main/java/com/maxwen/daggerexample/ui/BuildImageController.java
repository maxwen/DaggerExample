package com.maxwen.daggerexample.ui;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxwen.daggerexample.R;
import com.maxwen.daggerexample.data.BuildImageProvider;

public class BuildImageController extends BaseController {

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        BuildImageView view = (BuildImageView) inflater.inflate(R.layout.build_image_view, container, false);
        view.setController(this);
        getBuildImageProvider().getImageList(".*\\.zip", view);
        return view;
    }

    public BuildImageProvider getBuildImageProvider() {
        MainActivity activity = (MainActivity) getActivity();
        return activity.getApplicationComponent().getBuildImageProvider();
    }

    public String getTitle() {
        return "Builds";
    }

}
