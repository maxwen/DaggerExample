
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Coord {

    public abstract Double lon();

    public abstract Double lat();

    public static JsonAdapter<Coord> jsonAdapter(Moshi moshi) {
        return new AutoValue_Coord.MoshiJsonAdapter(moshi);
    }
}
