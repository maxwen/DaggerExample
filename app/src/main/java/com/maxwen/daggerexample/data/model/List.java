
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import javax.annotation.Nullable;

@AutoValue
public abstract class List {

    public abstract Integer dt();

    @Nullable
    public abstract Temp temp();

    @Nullable
    public abstract Double pressure();

    @Nullable
    public abstract Integer humidity();

    public abstract java.util.List<Weather> weather();

    @Nullable
    public abstract Double speed();

    @Nullable
    public abstract Integer deg();

    public abstract Clouds clouds();

    public static JsonAdapter<List> jsonAdapter(Moshi moshi) {
        return new AutoValue_List.MoshiJsonAdapter(moshi);
    }
}
