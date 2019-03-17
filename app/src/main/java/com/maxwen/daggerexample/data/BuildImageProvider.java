package com.maxwen.daggerexample.data;

import android.content.Context;
import android.os.PatternMatcher;
import android.util.Log;

import com.maxwen.daggerexample.data.model.AdapterFactory;
import com.maxwen.daggerexample.data.model.BuildImageFile;
import com.maxwen.daggerexample.data.model.BuildImageList;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Singleton
public class BuildImageProvider {

    private static final String BASE_URL = "https://dl.omnirom.org/";

    private Context mContext;
    private BuildImageAPI mBuildImageAPI;

    public interface BuildImageListCallback {
        public void updateList(List<BuildImageFile> imageList);
        public void updateList(Single<List<BuildImageFile>> imageList);
    }

    @Inject
    public BuildImageProvider(Context context) {
        this.mContext = context;
    }

    private Moshi provideMoshi() {
        return new Moshi.Builder()
                .add(AdapterFactory.create())
                .build();
    }

    private BuildImageAPI getBuildImageAPI() {
        if (mBuildImageAPI == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("maxwen", message);
                }
            });
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            mBuildImageAPI = retrofit.create(BuildImageAPI.class);
        }
        return mBuildImageAPI;
    }

    public void getImageList(final String filter, final BuildImageListCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<BuildImageFile> imageList = new ArrayList<>();
                PatternMatcher matcher = new PatternMatcher(filter, PatternMatcher.PATTERN_SIMPLE_GLOB);

                Call<List<BuildImageList>> call = getBuildImageAPI().getBuildImageList();
                try {
                    List<BuildImageList> buildImageList = call.execute().body();
                    if (buildImageList != null) {
                        imageList = buildImageList.stream()
                                .flatMap(device -> device.files().stream())
                                .filter(file -> matcher.match(new File(file.filename()).getName()))
                                .collect(Collectors.toList());
                    }
                    callback.updateList(imageList);
                } catch (IOException e) {
                    Log.d("maxwen", "", e);
                }
            }
        }).start();
    }

    public void getImageList2(final String filter, final BuildImageListCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PatternMatcher matcher = new PatternMatcher(filter, PatternMatcher.PATTERN_SIMPLE_GLOB);

                Single<List<BuildImageFile>> imageList = getBuildImageAPI().getBuildImageList2()
                        .flatMapIterable(x -> x)
                        .flatMap(device -> Observable.fromIterable(device.files()))
                        .filter(file -> matcher.match(new File(file.filename()).getName()))
                        .toList();

                callback.updateList(imageList);
            }
        }).start();
    }

    public void getImageList3(final String filter, Consumer<List<BuildImageFile>> onLoadDone, Consumer<Throwable> onError) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PatternMatcher matcher = new PatternMatcher(filter, PatternMatcher.PATTERN_SIMPLE_GLOB);

                getBuildImageAPI().getBuildImageList2()
                        .flatMapIterable(x -> x)
                        .flatMap(device -> Observable.fromIterable(device.files()))
                        .filter(file -> matcher.match(new File(file.filename()).getName()))
                        .toList()
                        .subscribe(onLoadDone, onError);

            }
        }).start();
    }
}
