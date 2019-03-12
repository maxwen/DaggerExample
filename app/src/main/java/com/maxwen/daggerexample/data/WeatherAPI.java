package com.maxwen.daggerexample.data;

// api.openweathermap.org/data/2.5/weather?q=London
// api.openweathermap.org/data/2.5/weather?lat=35&lon=139
// api.openweathermap.org/data/2.5/forecast?lat=35&lon=139

import com.maxwen.daggerexample.data.model.CurrentWeather;
import com.maxwen.daggerexample.data.model.ForecastWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("data/2.5//weather")
    Call<CurrentWeather> getCurrentWeatherOfLocation(@Query("lat") String lat,
                                                     @Query("lon") String lon,
                                                     @Query("appId") String appid,
                                                     @Query("units") String units);

    @GET("data/2.5//forecast")
    Call<ForecastWeather> getForecastWeatherOfLocation(@Query("lat") String lat,
                                                       @Query("lon") String lon,
                                                       @Query("appId") String appid,
                                                       @Query("units") String units,
                                                       @Query("cnt") Integer days);
}
