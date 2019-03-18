
package com.maxwen.daggerexample.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Rain {

    public abstract Integer _3h();

    public static JsonAdapter<Rain> jsonAdapter(Moshi moshi) {
        return new AutoValue_Rain.MoshiJsonAdapter(moshi);
    }
}
