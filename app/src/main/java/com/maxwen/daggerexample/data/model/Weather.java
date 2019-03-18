
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Weather {

    public abstract Integer id();

    public abstract String main();

    public abstract String description();

    public abstract String icon();

    public static JsonAdapter<Weather> jsonAdapter(Moshi moshi) {
        return new AutoValue_Weather.MoshiJsonAdapter(moshi);
    }

}
