package com.maxwen.daggerexample.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.maxwen.daggerexample.R;
import com.maxwen.daggerexample.data.BuildImageProvider;
import com.maxwen.daggerexample.data.model.BuildImageFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BuildImageView extends FrameLayout implements BuildImageProvider.BuildImageListCallback {

    private RecyclerView mListView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<BuildImageFile> mBuildImageList = new ArrayList<>();
    private static final SimpleDateFormat formatter
            = new SimpleDateFormat("yyyy.MM.dd");
    private Button mUpdateList;
    private ProgressBar mProgress;

    private BuildImageController mController;
    private final BehaviorRelay<List<BuildImageFile>> mBuildImageRelay = BehaviorRelay.create();
    private final BehaviorRelay<Throwable> mErrorRelay = BehaviorRelay.create();

    private class BuildImageViewHolder extends RecyclerView.ViewHolder {
        TextView mFileName;
        TextView mTimestamp;
        ImageView mImage;

        public BuildImageViewHolder(View v) {
            super(v);
            mFileName = v.findViewById(R.id.filename);
            mTimestamp = v.findViewById(R.id.timestamp);
        }

        public void setData(BuildImageFile buildImage) {
            mFileName.setText(buildImage.filename());
            mTimestamp.setText(formatter.format(buildImage.timestamp()));
        }
    }

    private class BuildImageAdapter extends RecyclerView.Adapter<BuildImageViewHolder> {
        public BuildImageViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            BuildImageViewHolder vh = new BuildImageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.build_item, parent, false));
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull BuildImageViewHolder buildImageViewHolder, int i) {
            buildImageViewHolder.setData(mBuildImageList.get(i));
        }

        @Override
        public int getItemCount() {
            return mBuildImageList.size();
        }
    }

    public BuildImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setController(BuildImageController controller) {
        mController = controller;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mListView = findViewById(R.id.list_view);
        mListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(layoutManager);
        mAdapter = new BuildImageAdapter();
        mListView.setAdapter(mAdapter);
        mProgress = findViewById(R.id.list_progress);

        mUpdateList = findViewById(R.id.list_update);
        mUpdateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setVisibility(VISIBLE);
                mController.getBuildImageProvider().getImageList3(".*\\.zip", mBuildImageRelay, mErrorRelay);
            }
        });
        mBuildImageRelay.observeOn(AndroidSchedulers.mainThread())
                .subscribe(buildImageList -> {
                    mBuildImageList.clear();
                    mBuildImageList.addAll(buildImageList);
                    mProgress.setVisibility(GONE);
                    mAdapter.notifyDataSetChanged();
                });
        mErrorRelay.observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> Log.e("maxwen", "", e));
    }

    @Override
    public void updateList(List<BuildImageFile> imageList) {
        mBuildImageList.clear();
        mBuildImageList.addAll(imageList);
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void updateList(Single<List<BuildImageFile>> imageList) {
        mBuildImageList.clear();
        imageList.subscribe(files -> mBuildImageList.addAll(files));
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mProgress.setVisibility(GONE);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}