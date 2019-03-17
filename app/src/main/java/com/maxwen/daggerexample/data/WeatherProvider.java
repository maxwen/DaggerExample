package com.maxwen.daggerexample.data;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.maxwen.daggerexample.data.model.CurrentWeather;
import com.maxwen.daggerexample.data.model.ForecastWeather;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class WeatherProvider {
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final float LOCATION_ACCURACY_THRESHOLD_METERS = 50000;
    private static final long OUTDATED_LOCATION_THRESHOLD_MILLIS = 10L * 60L * 1000L; // 10 minutes

    private Context mContext;
    private WeatherAPI mWeatherAPI;


    private static final Criteria sLocationCriteria;

    static {
        sLocationCriteria = new Criteria();
        sLocationCriteria.setPowerRequirement(Criteria.POWER_LOW);
        sLocationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        sLocationCriteria.setCostAllowed(false);
    }

    public interface LocationCallback {
        public void onLocationAvailable(Location location);
    }

    @Inject
    public WeatherProvider(Context context) {
        this.mContext = context;
    }

    private WeatherAPI getWeatherAPI() {
        if (mWeatherAPI == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("maxwen", message);
                }
            });
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            mWeatherAPI = retrofit.create(WeatherAPI.class);
        }
        return mWeatherAPI;
    }

    private void getCurrentLocation(final LocationCallback callback) throws SecurityException {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (location != null && location.getAccuracy() > LOCATION_ACCURACY_THRESHOLD_METERS) {
            location = null;
        }

        // If lastKnownLocation is not present (because none of the apps in the
        // device has requested the current location to the system yet) or outdated,
        // then try to get the current location use the provider that best matches the criteria.
        boolean needsUpdate = location == null;
        if (location != null) {
            long delta = System.currentTimeMillis() - location.getTime();
            needsUpdate = delta > OUTDATED_LOCATION_THRESHOLD_MILLIS;
        }
        if (needsUpdate) {
            String locationProvider = lm.getBestProvider(sLocationCriteria, true);
            if (!TextUtils.isEmpty(locationProvider)) {
                lm.requestSingleUpdate(locationProvider, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                callback.onLocationAvailable(location);
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                        },
                        mContext.getMainLooper());
            }
        } else {
            callback.onLocationAvailable(location);
        }
    }

    /*public void getCurrentWeather(WeatherProviderCallback callback) {
        getCurrentLocation(new LocationCallback() {
            @Override
            public void onLocationAvailable(Location location) {
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                Call<CurrentWeather> call = getWeatherAPI().getCurrentWeatherOfLocation(lat, lon, "f839981a9e6195410e563ef35d1e7fb4", "metric");
                call.enqueue(new Callback<CurrentWeather>() {
                    @Override
                    public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                        if (response.isSuccessful()) {
                            CurrentWeather weather = response.body();
                            callback.updateCurrentWeather(weather);
                        } else {
                            Log.d("maxwen", "" + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeather> call, Throwable t) {
                        Log.d("maxwen", "", t);
                    }
                });

                Call<ForecastWeather> call1 = getWeatherAPI().getForecastWeatherOfLocation(lat, lon, "f839981a9e6195410e563ef35d1e7fb4", "metric", 5);
                call1.enqueue(new Callback<ForecastWeather>() {
                    @Override
                    public void onResponse(Call<ForecastWeather> call, Response<ForecastWeather> response) {
                        if (response.isSuccessful()) {
                            ForecastWeather weather = response.body();
                            callback.updateForecastWeather(weather);
                        } else {
                            Log.d("maxwen", "" + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastWeather> call, Throwable t) {
                        Log.d("maxwen", "", t);
                    }
                });
            }
        });
    }*/

    public void getCurrentWeather2(Consumer<CurrentWeather> onLoadDone, Consumer<Throwable> onError) {
        getCurrentLocation(new LocationCallback() {
            @Override
            public void onLocationAvailable(Location location) {
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                getWeatherAPI().getCurrentWeatherOfLocation2(lat, lon, "f839981a9e6195410e563ef35d1e7fb4", "metric")
                        .subscribeOn(Schedulers.io())
                        .subscribe(onLoadDone, onError);
            }
        });
    }
    public void getForecastWeather2(Consumer<ForecastWeather> onLoadDone, Consumer<Throwable> onError) {
        getCurrentLocation(new LocationCallback() {
            @Override
            public void onLocationAvailable(Location location) {
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                getWeatherAPI().getForecastWeatherOfLocation2(lat, lon, "f839981a9e6195410e563ef35d1e7fb4", "metric", 5)
                        .subscribeOn(Schedulers.io())
                        .subscribe(onLoadDone, onError);
            }
        });
    }

}
