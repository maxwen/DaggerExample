package com.maxwen.daggerexample.data;

import android.content.Context;
import android.os.PatternMatcher;
import android.util.Log;

import com.maxwen.daggerexample.data.model.AdapterFactory;
import com.maxwen.daggerexample.data.model.BuildImage;
import com.maxwen.daggerexample.data.model.BuildImageFile;
import com.maxwen.daggerexample.data.model.BuildImageList;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Singleton
public class BuildImageProvider {

    private static final String BASE_URL = "https://dl.omnirom.org/";

    private Context mContext;
    private BuildImageAPI mBuildImageAPI;

    public interface BuildImageListCallback {
        public void updateList(List<BuildImage> imageList);
    }

    @Inject
    public BuildImageProvider(Context context) {
        this.mContext = context;
    }

    public void filterForDevice(final List<BuildImage> imageList, final String device, final BuildImageListCallback callback) {

        Observable<BuildImage> listObservable = Observable.fromIterable(imageList).filter(
                new Predicate<BuildImage>() {
                    @Override
                    public boolean test(BuildImage buildImage) throws Exception {
                        return buildImage.getFilename().contains(device);
                    }
                });
        listObservable.subscribe(new Observer<BuildImage>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BuildImage buildImage) {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                callback.updateList(imageList);
            }
        });
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
                    .build();

            mBuildImageAPI = retrofit.create(BuildImageAPI.class);
        }
        return mBuildImageAPI;
    }

    public void getImageList(final String filter, final BuildImageListCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                List<BuildImage> imageList = new ArrayList<>();
                PatternMatcher matcher = new PatternMatcher(filter, PatternMatcher.PATTERN_SIMPLE_GLOB);

                Call<List<BuildImageList>> call = getBuildImageAPI().getBuildImageList();
                call.enqueue(new Callback<List<BuildImageList>>() {
                    @Override
                    public void onResponse(Call<List<BuildImageList>> call, Response<List<BuildImageList>> response) {
                        if (response.isSuccessful()) {
                            List<BuildImageList> buildImageList = response.body();
                            for (BuildImageList device : buildImageList) {
                                for (BuildImageFile file : device.files()) {
                                    String fileName = new File(file.filename()).getName();
                                    if (matcher.match(fileName)) {
                                        long timestamp = file.timestamp();
                                        BuildImage image = new BuildImage(fileName, timestamp);
                                        imageList.add(image);
                                    }
                                }
                            }
                            callback.updateList(imageList);
                        } else {
                            Log.d("maxwen", "" + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BuildImageList>> call, Throwable t) {
                        Log.d("maxwen", "", t);
                    }
                });
            }


        }).start();
    }
}
