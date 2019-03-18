package com.maxwen.daggerexample.data;

import android.content.Context;
import android.os.PatternMatcher;

import com.maxwen.daggerexample.data.model.BuildImageFile;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

@Singleton
public class BuildImageProvider {

    private static final String BASE_URL = "https://dl.omnirom.org/";

    private Context mContext;
    private BuildImageAPI mBuildImageAPI;

    @Inject
    public BuildImageProvider(Context context, Retrofit.Builder builder) {
        mContext = context;
        builder.baseUrl(BASE_URL);
        Retrofit r = builder.build();
        mBuildImageAPI = r.create(BuildImageAPI.class);
    }

    /*public void getImageList(final String filter, final BuildImageListCallback callback) {
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
    }*/

    public void getImageList(final String filter, Consumer<List<BuildImageFile>> onLoadDone, Consumer<Throwable> onError) {
        PatternMatcher matcher = new PatternMatcher(filter, PatternMatcher.PATTERN_SIMPLE_GLOB);
        mBuildImageAPI.getBuildImageList2()
                .subscribeOn(Schedulers.io())
                .flatMapIterable(x -> x)
                .flatMap(device -> Observable.fromIterable(device.files()))
                .filter(file -> matcher.match(new File(file.filename()).getName()))
                .toList()
                .subscribe(onLoadDone, onError);
    }
}
