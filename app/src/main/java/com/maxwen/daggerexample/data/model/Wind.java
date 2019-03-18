
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import javax.annotation.Nullable;

@AutoValue
public abstract class Wind {

    public abstract Double speed();

    @Nullable
    public abstract Integer deg();

    public static JsonAdapter<Wind> jsonAdapter(Moshi moshi) {
        return new AutoValue_Wind.MoshiJsonAdapter(moshi);
    }
}
