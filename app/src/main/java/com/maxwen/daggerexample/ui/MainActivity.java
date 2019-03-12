package com.maxwen.daggerexample.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxwen.daggerexample.R;
import com.maxwen.daggerexample.data.BuildImageProvider;
import com.maxwen.daggerexample.data.WeatherProvider;
import com.maxwen.daggerexample.data.model.BuildImage;
import com.maxwen.daggerexample.data.model.CurrentWeather;
import com.maxwen.daggerexample.data.model.ForecastWeather;
import com.maxwen.daggerexample.data.model.Weather;
import com.maxwen.daggerexample.di.App;
import com.maxwen.daggerexample.di.ApplicationComponent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BuildImageProvider.BuildImageListCallback, WeatherProvider.WeatherProviderCallback {

    //@Inject
    //BuildImageProvider mProvider;
    private static final int PERMISSIONS_REQUEST_LOCATION = 0;
    private static final String PATH_TO_WEATHER_FONT = "fonts/weathericons-regular-webfont.ttf";

    ApplicationComponent mApplicationComponent;

    private RecyclerView mListView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<BuildImage> mBuildImageList = new ArrayList<>();
    private static final SimpleDateFormat formatter
            = new SimpleDateFormat("yyyy.MM.dd");
    private TextView mWeatherData;
    private Button mWeatherUpdate;
    private TextView mWeatherIcon;
    private Typeface mWeatherFont;

    private class BuildImageViewHolder extends RecyclerView.ViewHolder {
        TextView mFileName;
        TextView mTimestamp;
        ImageView mImage;

        public BuildImageViewHolder(View v) {
            super(v);
            mFileName = v.findViewById(R.id.filename);
            mTimestamp = v.findViewById(R.id.timestamp);
        }

        public void setData(BuildImage buildImage) {
            mFileName.setText(buildImage.getFilename());
            mTimestamp.setText(formatter.format(buildImage.getTimestamp()));
        }
    }

    private class BuildImageAdapter extends RecyclerView.Adapter<BuildImageViewHolder> {
        public BuildImageViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            BuildImageViewHolder vh = new BuildImageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.build_item, parent, false));
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull BuildImageViewHolder buildImageViewHolder, int i) {
            buildImageViewHolder.setData(mBuildImageList.get(i));
        }

        @Override
        public int getItemCount() {
            return mBuildImageList.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.list_view);
        mWeatherData = findViewById(R.id.weather_text);
        mWeatherUpdate = findViewById(R.id.weather_update);
        mWeatherUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_LOCATION);
                } else {
                    mApplicationComponent.getWeatherProvider().getCurrentWeather(MainActivity.this);
                }
            }
        });
        mWeatherIcon = findViewById(R.id.weather_icon);
        mWeatherFont = Typeface.createFromAsset(getAssets(), PATH_TO_WEATHER_FONT);
        mWeatherIcon.setTypeface(mWeatherFont);

        mListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(layoutManager);
        mAdapter = new BuildImageAdapter();
        mListView.setAdapter(mAdapter);

        mApplicationComponent = ((App) getApplication()).getComponent();
        mApplicationComponent.inject(this);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mApplicationComponent.getBuildImageProvider().getImageList(".*\\.zip", this);
    }

    @Override
    public void updateList(List<BuildImage> imageList) {
        mBuildImageList.clear();
        mBuildImageList.addAll(imageList);
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void updateCurrentWeather(CurrentWeather weather) {
        mWeatherData.setText("" + weather.getName() + "\n" +
                weather.getCoord().getLat() + " - " +
                weather.getCoord().getLon() + "\n" +
                weather.getWeather().get(0).getMain() + "\n" +
                weather.getWeather().get(0).getDescription() + "\n" +
                weather.getWeather().get(0).getIcon());
        setWeatherIcon(weather.getWeather().get(0).getIcon(), mWeatherIcon);
    }

    private int getWeatherIconForDay(int day) {
        switch (day) {
            case 1:
                return R.id.weather_icon_1;
            case 2:
                return R.id.weather_icon_2;
            case 3:
                return R.id.weather_icon_3;
            case 4:
                return R.id.weather_icon_4;
            case 5:
                return R.id.weather_icon_5;
            default:
                return -1;
        }
    }

    private void setWeatherIcon(String icon, TextView t) {
        switch (icon) {
            case "01d":
                t.setText(R.string.wi_day_sunny);
                break;
            case "02d":
                t.setText(R.string.wi_cloudy_gusts);
                break;
            case "03d":
                t.setText(R.string.wi_cloud_down);
                break;
            case "10d":
                t.setText(R.string.wi_day_rain_mix);
                break;
            case "11d":
                t.setText(R.string.wi_day_thunderstorm);
                break;
            case "13d":
                t.setText(R.string.wi_day_snow);
                break;
            case "01n":
                t.setText(R.string.wi_night_clear);
                break;
            case "04d":
                t.setText(R.string.wi_cloudy);
                break;
            case "04n":
                t.setText(R.string.wi_night_cloudy);
                break;
            case "02n":
                t.setText(R.string.wi_night_cloudy);
                break;
            case "03n":
                t.setText(R.string.wi_night_cloudy_gusts);
                break;
            case "10n":
                t.setText(R.string.wi_night_cloudy_gusts);
                break;
            case "11n":
                t.setText(R.string.wi_night_rain);
                break;
            case "13n":
                t.setText(R.string.wi_night_snow);
                break;
        }
    }

    @Override
    public void updateForecastWeather(ForecastWeather weather) {
        int i = 1;
        for (com.maxwen.daggerexample.data.model.List l : weather.getList()) {
            for (Weather dayWeather : l.getWeather()) {
                TextView t = findViewById(getWeatherIconForDay(i));
                if (t == null) {
                    continue;
                }
                t.setTypeface(mWeatherFont);
                setWeatherIcon(dayWeather.getIcon(), t);
            }
            i++;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mApplicationComponent.getWeatherProvider().getCurrentWeather(MainActivity.this);
                }
                return;
            }
        }
    }
}