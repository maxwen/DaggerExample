
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Temp {

    public abstract Double day();

    public abstract Double min();

    public abstract Double max();

    public abstract Double night();

    public abstract Double eve();

    public abstract Double morn();

    public static JsonAdapter<Temp> jsonAdapter(Moshi moshi) {
        return new AutoValue_Temp.MoshiJsonAdapter(moshi);
    }
}
