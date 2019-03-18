package com.maxwen.daggerexample.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxwen.daggerexample.R;
import com.maxwen.daggerexample.data.model.CurrentWeather;
import com.maxwen.daggerexample.data.model.ForecastWeather;
import com.maxwen.daggerexample.data.model.Weather;

public class OwmView extends FrameLayout {

    private static final String PATH_TO_WEATHER_FONT = "fonts/weathericons-regular-webfont.ttf";

    private TextView mWeatherData;
    private Button mWeatherUpdate;
    private TextView mWeatherIcon;
    private Typeface mWeatherFont;
    private OwmController mController;
    private ProgressBar mProgress;

    public OwmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setController(OwmController controller) {
        mController = controller;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mWeatherData = findViewById(R.id.weather_text);
        mProgress = findViewById(R.id.weather_progress);
        mWeatherUpdate = findViewById(R.id.weather_update);
        mWeatherUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.updateWeather();
            }
        });
        mWeatherIcon = findViewById(R.id.weather_icon);
        mWeatherFont = Typeface.createFromAsset(getContext().getAssets(), PATH_TO_WEATHER_FONT);
        mWeatherIcon.setTypeface(mWeatherFont);
    }

    public void updateCurrentWeather(CurrentWeather weather) {
        mWeatherData.setText("" + weather.name() + "\n" +
                weather.coord().lat() + " - " +
                weather.coord().lon() + "\n" +
                weather.weather().get(0).main() + "\n" +
                weather.weather().get(0).description() + "\n" +
                weather.weather().get(0).icon() + "\n" +
                weather.main().temp());
        setWeatherIcon(weather.weather().get(0).icon(), mWeatherIcon);
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
            case "09d":
                t.setText(R.string.wi_day_rain_mix);
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
            case "09n":
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

    public void updateForecastWeather(ForecastWeather weather) {
        int i = 1;
        for (com.maxwen.daggerexample.data.model.List l : weather.list()) {
            for (Weather dayWeather : l.weather()) {
                TextView t = findViewById(getWeatherIconForDay(i));
                if (t == null) {
                    continue;
                }
                t.setTypeface(mWeatherFont);
                setWeatherIcon(dayWeather.icon(), t);
            }
            i++;
        }
    }

    public void setLoading(boolean loading) {
        mProgress.setVisibility(loading ? VISIBLE : GONE);
    }
}