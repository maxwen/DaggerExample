package com.maxwen.daggerexample.di;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.maxwen.daggerexample.data.model.AdapterFactory;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by renegens on 25/07/16.
 */

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    public Retrofit.Builder provideRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("maxwen", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        return new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(MoshiConverterFactory.create(new Moshi.Builder()
                        .add(AdapterFactory.create())
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }
}
