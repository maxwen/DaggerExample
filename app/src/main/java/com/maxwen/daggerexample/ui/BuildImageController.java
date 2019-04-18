package com.maxwen.daggerexample.ui;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.maxwen.daggerexample.R;
import com.maxwen.daggerexample.data.BuildImageProvider;
import com.maxwen.daggerexample.data.model.BuildImageFile;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class BuildImageController extends BaseController {
    private final BehaviorRelay<List<BuildImageFile>> mBuildImageRelay = BehaviorRelay.create();
    private final BehaviorRelay<Throwable> mErrorRelay = BehaviorRelay.create();
    private final BehaviorRelay<Boolean> mLoadingRelay = BehaviorRelay.create();
    private boolean mInitDone;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        BuildImageView view = (BuildImageView) inflater.inflate(R.layout.build_image_view, container, false);
        view.setController(this);

        mBuildImageRelay.observeOn(AndroidSchedulers.mainThread())
                .subscribe(buildImageList -> {
                    mLoadingRelay.accept(false);
                    view.setData(buildImageList);
                });
        mErrorRelay.observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    mLoadingRelay.accept(false);
                    Log.e("maxwen", "", e);
                });
        mLoadingRelay.observeOn(AndroidSchedulers.mainThread())
                .subscribe(loading -> view.setLoading(loading));
        if (!mInitDone) {
            updateList();
            mInitDone = true;
        }
        return view;
    }

    public BuildImageProvider getBuildImageProvider() {
        MainActivity activity = (MainActivity) getActivity();
        return activity.getApplicationComponent().getBuildImageProvider();
    }

    public String getTitle() {
        return "Builds";
    }

    public void updateList() {
        mLoadingRelay.accept(true);
        getBuildImageProvider().getImageList(".*\\.zip", mBuildImageRelay, mErrorRelay);
    }
}
