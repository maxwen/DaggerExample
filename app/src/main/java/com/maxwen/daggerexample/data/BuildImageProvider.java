package com.maxwen.daggerexample.data;

import android.content.Context;
import android.os.PatternMatcher;
import android.util.Log;

import com.maxwen.daggerexample.data.model.BuildImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

@Singleton
public class BuildImageProvider {

    private static final String BASE_URL = "https://dl.omnirom.org/json.php";
    private static final int HTTP_READ_TIMEOUT = 30000;
    private static final int HTTP_CONNECTION_TIMEOUT = 30000;

    private Context mContext;

    public interface BuildImageListCallback {
        public void updateList(List<BuildImage> imageList);
    }

    @Inject
    public BuildImageProvider(Context context) {
        this.mContext = context;
    }


    private HttpsURLConnection setupHttpsRequest(String urlStr) {
        URL url;
        HttpsURLConnection urlConnection = null;
        try {
            url = new URL(urlStr);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(HTTP_CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(HTTP_READ_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            int code = urlConnection.getResponseCode();
            if (code != HttpsURLConnection.HTTP_OK) {
                return null;
            }
            return urlConnection;
        } catch (Exception e) {
            return null;
        }
    }

    private String downloadUrlMemoryAsString() {
        HttpsURLConnection urlConnection = null;
        try {
            urlConnection = setupHttpsRequest(BASE_URL);
            if (urlConnection == null) {
                return null;
            }

            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            int byteInt;

            while ((byteInt = is.read()) >= 0) {
                byteArray.write(byteInt);
            }

            byte[] bytes = byteArray.toByteArray();
            if (bytes == null) {
                return null;
            }
            String responseBody = new String(bytes, StandardCharsets.UTF_8);

            return responseBody;
        } catch (Exception e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public void getImageList(final String filter, final BuildImageListCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                List<BuildImage> imageList = new ArrayList<>();
                PatternMatcher matcher = new PatternMatcher(filter, PatternMatcher.PATTERN_SIMPLE_GLOB);

                JSONObject object = null;
                try {
                    String buildData = downloadUrlMemoryAsString();
                    if (buildData == null || buildData.length() == 0) {
                        return;
                    }
                    object = new JSONObject(buildData);
                    Iterator<String> nextKey = object.keys();
                    while (nextKey.hasNext()) {
                        String key = nextKey.next();
                        JSONArray builds = new JSONArray();
                        try {
                            builds = object.getJSONArray(key);

                            for (int i = 0; i < builds.length(); i++) {
                                JSONObject build = builds.getJSONObject(i);
                                String fileName = new File(build.getString("filename")).getName();
                                if (matcher.match(fileName)) {
                                    long timestamp = build.getLong("timestamp") * 1000;
                                    BuildImage image = new BuildImage(fileName, timestamp);
                                    imageList.add(image);
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                } catch (JSONException e) {
                } finally {
                    Observable<BuildImage> listObservable = Observable.fromIterable(imageList).filter(
                            new Predicate<BuildImage>() {
                                @Override
                                public boolean test(BuildImage buildImage) throws Exception {
                                    return buildImage.getFilename().contains("athene");
                                }
                            });
                    listObservable.subscribe(new Observer<BuildImage>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BuildImage buildImage) {
                            Log.d("maxwen", "" + buildImage);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                    callback.updateList(imageList);
                }
            }
        }).start();
    }
}
