
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Clouds {

    public abstract Integer all();

    public static JsonAdapter<Clouds> jsonAdapter(Moshi moshi) {
        return new AutoValue_Clouds.MoshiJsonAdapter(moshi);
    }
}
