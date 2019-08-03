package com.maxwen.daggerexample.data;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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
import com.maxwen.daggerexample.data.model.Weatherdata;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

@Singleton
public class NorwayWeatherProvider {
    private static final String BASE_URL = "https://api.met.no/weatherapi/";
    private static final long TIMEOUT_LOCATION_THRESHOLD_MILLIS = 30L * 1000L; // 30 seconds

    private Context mContext;
    private NorwayWeatherAPI mWeatherAPI;
    private FusedLocationProviderClient mFusedLocationClient;

    public interface LocationCallbackCustom {
        public void onLocationAvailable(Location location);

        public void onLocationFailed();
    }

    @Inject
    public NorwayWeatherProvider(Context context, Retrofit.Builder builder) {
        mContext = context;
        builder.baseUrl(BASE_URL);
        Retrofit r = builder.build();
        mWeatherAPI = r.create(NorwayWeatherAPI.class);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

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
                        public void onLocationAvailability(LocationAvailability locationAvailability) {
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

    public static String getCityOfLocation(Context context, Double lat, Double lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    lat,
                    lon,
                    1);
        } catch (IOException ioException) {
            return "";
        } catch (IllegalArgumentException illegalArgumentException) {
            return "";
        }

        if (addresses != null && addresses.size() != 0) {
            Address address = addresses.get(0);
            return address.getAdminArea();
        }
        return "";
    }

    public void getWeather(Consumer<Weatherdata> onLoadDone, Consumer<Throwable> onError) {
        getCurrentLocationGoogle(new LocationCallbackCustom() {
            @Override
            public void onLocationAvailable(Location location) {
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                mWeatherAPI.getForecastWeatherOfLocation(lat, lon)
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
