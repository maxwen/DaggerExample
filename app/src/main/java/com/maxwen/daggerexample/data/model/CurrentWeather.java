
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.List;

import javax.annotation.Nullable;

@AutoValue
public abstract class CurrentWeather {

    public abstract Coord coord();

    public abstract List<Weather> weather();

    public abstract String base();

    public abstract Main main();

    public abstract Wind wind();

    public abstract Clouds clouds();

    @Nullable
    public abstract Rain rain();

    public abstract Integer dt();

    public abstract Sys sys();

    public abstract Integer id();

    public abstract String name();

    public abstract Integer cod();

    public static JsonAdapter<CurrentWeather> jsonAdapter(Moshi moshi) {
        return new AutoValue_CurrentWeather.MoshiJsonAdapter(moshi);
    }
}
