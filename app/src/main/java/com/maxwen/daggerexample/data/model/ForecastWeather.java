
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class ForecastWeather {

    public abstract String cod();

    public abstract City city();

    public abstract Integer cnt();

    public abstract java.util.List<com.maxwen.daggerexample.data.model.List> list();

    public static JsonAdapter<ForecastWeather> jsonAdapter(Moshi moshi) {
        return new AutoValue_ForecastWeather.MoshiJsonAdapter(moshi);
    }
}
