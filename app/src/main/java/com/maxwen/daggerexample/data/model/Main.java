
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import javax.annotation.Nullable;

@AutoValue
public abstract class Main {

    public abstract Double temp();

    public abstract Integer pressure();

    public abstract Integer humidity();

    @Nullable
    public abstract Double tempMin();

    @Nullable
    public abstract Double tempMax();

    public static JsonAdapter<Main> jsonAdapter(Moshi moshi) {
        return new AutoValue_Main.MoshiJsonAdapter(moshi);
    }
}
