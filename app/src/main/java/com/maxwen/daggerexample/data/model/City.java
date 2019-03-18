
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import javax.annotation.Nullable;

@AutoValue
public abstract class City {

    @Nullable
    public abstract Integer geonameId();

    public abstract String name();

    @Nullable
    public abstract Double lat();

    @Nullable
    public abstract Double lon();

    public abstract String country();

    @Nullable
    public abstract String iso2();

    @Nullable
    public abstract String type();

    @Nullable
    public abstract Integer population();

    public static JsonAdapter<City> jsonAdapter(Moshi moshi) {
        return new AutoValue_City.MoshiJsonAdapter(moshi);
    }
}
