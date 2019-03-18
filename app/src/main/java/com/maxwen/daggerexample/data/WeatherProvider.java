package com.maxwen.daggerexample.data;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.maxwen.daggerexample.data.model.CurrentWeather;
import com.maxwen.daggerexample.data.model.ForecastWeather;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

@Singleton
public class WeatherProvider {
    private static final String BASE_URL = "https://api.openweathermap.org/";
    //private static final float LOCATION_ACCURACY_THRESHOLD_METERS = 50000;
    //private static final long OUTDATED_LOCATION_THRESHOLD_MILLIS = 10L * 60L * 1000L; // 10 minutes
    private static final long TIMEOUT_LOCATION_THRESHOLD_MILLIS = 30L * 1000L; // 30 seconds

    private Context mContext;
    private WeatherAPI mWeatherAPI;
    private FusedLocationProviderClient mFusedLocationClient;

    /*static {
        final Criteria sLocationCriteria = new Criteria();
        sLocationCriteria.setPowerRequirement(Criteria.POWER_LOW);
        sLocationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        sLocationCriteria.setCostAllowed(false);
    }*/

    public interface LocationCallbackCustom {
        public void onLocationAvailable(Location location);
        public void onLocationFailed();
    }

    @Inject
    public WeatherProvider(Context context, Retrofit.Builder builder) {
        mContext = context;
        builder.baseUrl(BASE_URL);
        Retrofit r = builder.build();
        mWeatherAPI = r.create(WeatherAPI.class);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    /*private void getCurrentLocation(final LocationCallbackCustom callback) throws SecurityException {
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
    }*/

    private void getCurrentLocationGoogle(final LocationCallbackCustom callback) throws SecurityException {
        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) throws SecurityException {
                if (location != null) {
                    callback.onLocationAvailable(location);
                } else {
                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                    locationRequest.setExpirationDuration(TIMEOUT_LOCATION_THRESHOLD_MILLIS);
                    mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            if (locationResult == null) {
                                callback.onLocationFailed();
                                return;
                            }
                            for (Location location : locationResult.getLocations()) {
                                if (location != null) {
                                    callback.onLocationAvailable(location);
                                    return;
                                }
                            }
                            callback.onLocationFailed();
                        }
                        @Override
                        public void onLocationAvailability (LocationAvailability locationAvailability) {
                            if (!locationAvailability.isLocationAvailable()) {
                                callback.onLocationFailed();
                            }
                        }
                    }, mContext.getMainLooper());
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onLocationFailed();
            }
        });
    }

    public void getCurrentWeather2(Consumer<CurrentWeather> onLoadDone, Consumer<Throwable> onError) {
        getCurrentLocationGoogle(new LocationCallbackCustom() {
            @Override
            public void onLocationAvailable(Location location) {
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                mWeatherAPI.getCurrentWeatherOfLocation2(lat, lon, "f839981a9e6195410e563ef35d1e7fb4", "metric")
                        .subscribeOn(Schedulers.io())
                        .subscribe(onLoadDone, onError);
            }
            @Override
            public void onLocationFailed() {
                try {
                    onError.accept(new RuntimeException());
                } catch (Exception e) {
                }
            }
        });
    }

    public void getForecastWeather2(Consumer<ForecastWeather> onLoadDone, Consumer<Throwable> onError) {
        getCurrentLocationGoogle(new LocationCallbackCustom() {
            @Override
            public void onLocationAvailable(Location location) {
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                mWeatherAPI.getForecastWeatherOfLocation2(lat, lon, "f839981a9e6195410e563ef35d1e7fb4", "metric", 5)
                        .subscribeOn(Schedulers.io())
                        .subscribe(onLoadDone, onError);
            }
            @Override
            public void onLocationFailed() {
                try {
                    onError.accept(new RuntimeException());
                } catch (Exception e) {
                }
            }
        });
    }
}
