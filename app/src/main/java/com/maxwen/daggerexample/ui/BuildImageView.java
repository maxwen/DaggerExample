package com.maxwen.daggerexample.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxwen.daggerexample.R;
import com.maxwen.daggerexample.data.BuildImageProvider;
import com.maxwen.daggerexample.data.model.BuildImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BuildImageView extends LinearLayout implements BuildImageProvider.BuildImageListCallback {

    private RecyclerView mListView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<BuildImage> mBuildImageList = new ArrayList<>();
    private static final SimpleDateFormat formatter
            = new SimpleDateFormat("yyyy.MM.dd");
    private Button mUpdateList;

    private BuildImageController mController;

    private class BuildImageViewHolder extends RecyclerView.ViewHolder {
        TextView mFileName;
        TextView mTimestamp;
        ImageView mImage;

        public BuildImageViewHolder(View v) {
            super(v);
            mFileName = v.findViewById(R.id.filename);
            mTimestamp = v.findViewById(R.id.timestamp);
        }

        public void setData(BuildImage buildImage) {
            mFileName.setText(buildImage.getFilename());
            mTimestamp.setText(formatter.format(buildImage.getTimestamp()));
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

        mUpdateList = findViewById(R.id.list_update);
        mUpdateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.getBuildImageProvider().getImageList(".*\\.zip", BuildImageView.this);
            }
        });
    }

    @Override
    public void updateList(List<BuildImage> imageList) {
        mBuildImageList.clear();
        mBuildImageList.addAll(imageList);
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}