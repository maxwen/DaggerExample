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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.maxwen.daggerexample.R;
import com.maxwen.daggerexample.data.model.BuildImageFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuildImageView extends FrameLayout {

    private RecyclerView mListView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<BuildImageFile> mBuildImageList = new ArrayList<>();
    private static final SimpleDateFormat formatter
            = new SimpleDateFormat("yyyy.MM.dd");
    private Button mUpdateList;
    private ProgressBar mProgress;
    private Spinner mDeviceSpinner;

    private BuildImageController mController;
    private ArrayAdapter<String> mDeviceSpinnerAdapter;
    private List<String> mDeviceList = new ArrayList<>();
    private List<BuildImageFile> mFilteredBuildList = new ArrayList<>();
    private int mSelectedDevice;

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
            buildImageViewHolder.setData(mFilteredBuildList.get(i));
        }

        @Override
        public int getItemCount() {
            return mFilteredBuildList.size();
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
        mDeviceSpinner = findViewById(R.id.device_select);

        mDeviceSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mDeviceList);
        mDeviceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mDeviceSpinner.setAdapter(mDeviceSpinnerAdapter);
        mDeviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedDevice = position;
                String device = mDeviceList.get(position);
                setFilteredData(getFilesOfDevice(device));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mUpdateList = findViewById(R.id.list_update);
        mUpdateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.updateList();
            }
        });
    }

    public void setData(List<BuildImageFile> imageList) {
        mBuildImageList.clear();
        mBuildImageList.addAll(imageList);

        mDeviceList.clear();
        mDeviceList.addAll(getDirList());

        mDeviceSpinnerAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();

        mFilteredBuildList.clear();
        mFilteredBuildList.addAll(getFilesOfDevice(mDeviceList.get(mSelectedDevice)));
    }

    public void setFilteredData(List<BuildImageFile> imageList) {
        mFilteredBuildList.clear();
        mFilteredBuildList.addAll(imageList);
        mAdapter.notifyDataSetChanged();
    }

    public void setLoading(boolean loading) {
        mProgress.setVisibility(loading ? VISIBLE : GONE);
    }

    private List<BuildImageFile> getFilesOfDevice(String device) {
        return mBuildImageList.stream()
                .filter(file -> new File(file.filename()).getParentFile().getName().equals(device))
                .collect(Collectors.toList());
    }

    private List<String> getDirList() {
        return mBuildImageList.stream()
                .map(file -> new File(file.filename()).getParentFile().getName())
                .distinct()
                .collect(Collectors.toList());
    }
}