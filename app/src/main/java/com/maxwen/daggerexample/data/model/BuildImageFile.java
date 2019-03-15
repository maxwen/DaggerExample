package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class BuildImageFile {
    public abstract Long timestamp();

    public abstract String filename();

    public static JsonAdapter<BuildImageFile> jsonAdapter(Moshi moshi) {
        return new AutoValue_BuildImageFile.MoshiJsonAdapter(moshi);
    }
}
