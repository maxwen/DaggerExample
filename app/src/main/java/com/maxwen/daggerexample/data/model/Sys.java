
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Sys {

    public abstract Integer type();

    public abstract Integer id();

    public abstract Double message();

    public abstract String country();

    public abstract Integer sunrise();

    public abstract Integer sunset();

    public static JsonAdapter<Sys> jsonAdapter(Moshi moshi) {
        return new AutoValue_Sys.MoshiJsonAdapter(moshi);
    }

}
