package com.maxwen.daggerexample.data;

import com.maxwen.daggerexample.data.model.Weatherdata;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NorwayWeatherAPI {

    @GET("locationforecastlts/1.3")
    Observable<Weatherdata> getForecastWeatherOfLocation(@Query("lat") String lat,
                                                         @Query("lon") String lon);
}
