package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class BuildImageList {

    public abstract String dir();

    public abstract java.util.List<BuildImageFile> files();

    public static JsonAdapter<BuildImageList> jsonAdapter(Moshi moshi) {
        return new AutoValue_BuildImageList.MoshiJsonAdapter(moshi);
    }
}
