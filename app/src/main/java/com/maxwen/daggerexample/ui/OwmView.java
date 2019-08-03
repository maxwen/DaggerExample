package com.maxwen.daggerexample.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxwen.daggerexample.R;
import com.maxwen.daggerexample.data.NorwayWeatherProvider;
import com.maxwen.daggerexample.data.WeatherInfo;
import com.maxwen.daggerexample.data.model.Weatherdata;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class OwmView extends FrameLayout {

    private static final String PATH_TO_WEATHER_FONT = "fonts/weathericons-regular-webfont.ttf";

    private TextView mWeatherData;
    private Button mWeatherUpdate;
    private ImageView mWeatherIcon;
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

    private void setWeatherIcon(int conditionCode, ImageView t) {
        String uri = "https://api.met.no/weatherapi/weathericon/1.1?content_type=image/png&symbol=" + String.valueOf(conditionCode);
        Picasso.with(getContext()).load(uri).into(t);
    }

    public void updateWeather(Weatherdata weather) {
        final Date now = new Date();
        WeatherInfo.DayForecast[] dayList = new WeatherInfo.DayForecast[5];
        ArrayList<WeatherInfo.DayForecast> dayForecastList = new ArrayList<>();
        WeatherInfo wi = new WeatherInfo(getContext(),
                "",
                "",
                "",
                -1,
                666,
                0,
                0f,
                0,
                true,
                dayForecastList,
                System.currentTimeMillis());


        Calendar nowCal = Calendar.getInstance();
        nowCal.setTimeInMillis(now.getTime());
        nowCal.add(Calendar.DAY_OF_YEAR, 1);
        nowCal.set(Calendar.HOUR_OF_DAY, 11);
        nowCal.set(Calendar.MINUTE, 0);
        for (int i = 0; i < 5; i++) {
            //Log.i("updateForecastWeather", "" + timeFormat.format(nowCal.getTimeInMillis()));
            WeatherInfo.DayForecast df = dayList[i];
            if (df == null) {
                df = new WeatherInfo.DayForecast(666, 666, "", -1, "NaN", true);
                dayList[i] = df;
            }
            for (com.maxwen.daggerexample.data.model.Weatherdata.Time l : weather.getProduct().getTime()) {
                Weatherdata.Time dayWeather = l;
                try {
                    if (dayWeather.getLocation().getPrecipitation() != null) {
                        if (l.getToDate().after(nowCal.getTime())) {
                            if (l.getLocation().getMinTemperature() != null && df.low == 666) {
                                df.low = l.getLocation().getMinTemperature().getValue().floatValue();
                                df.high = l.getLocation().getMaxTemperature().getValue().floatValue();
                                df.conditionCode = l.getLocation().getSymbol().getNumber();
                                df.condition = l.getLocation().getSymbol().getId();
                            }
                        }
                    }
                } catch (ParseException e) {
                }
            }
            nowCal.add(Calendar.DAY_OF_YEAR, 1);
        }
        dayForecastList.addAll(Arrays.asList(dayList));
        //Log.i("updateForecastWeather", "" + timeFormat.format(now));

        for (com.maxwen.daggerexample.data.model.Weatherdata.Time l : weather.getProduct().getTime()) {
            Weatherdata.Time dayWeather = l;
            try {
                if (dayWeather.getLocation().getPrecipitation() != null) {
                    if (l.getToDate().after(now) && l.getLocation().getSymbol() != null && wi.conditionCode == -1) {
                        //Log.i("updateForecastWeather", "" + timeFormat.format(l.getFromDate()));

                        wi.conditionCode = l.getLocation().getSymbol().getNumber();
                        wi.condition = l.getLocation().getSymbol().getId();
                        String city = NorwayWeatherProvider.getCityOfLocation(getContext(),
                                Double.valueOf(l.getLocation().getLatitude()),
                                Double.valueOf(l.getLocation().getLongitude()));
                        wi.city = city;
                    }
                } else {
                    if (l.getFromDate().after(now) && wi.temperature == 666) {
                        //Log.i("updateForecastWeather", "" + timeFormat.format(l.getFromDate()));

                        wi.temperature = l.getLocation().getTemperature().getValue().floatValue();
                        wi.wind = l.getLocation().getWindSpeed().getBeaufort().intValue();
                        wi.windDirection = l.getLocation().getWindDirection().getDeg().intValue();
                        wi.humidity = l.getLocation().getHumidity().getValue().floatValue();
                    }
                }
            } catch (ParseException e) {

            }
        }
        Log.i("Current", "" + wi.getConditionCode() + " " + wi.condition + " " + wi.getTemperature() + " ");
        setWeatherIcon(wi.getConditionCode(), mWeatherIcon);
        mWeatherData.setText(wi.toString());
        int i = 1;
        for (WeatherInfo.DayForecast t : wi.getForecasts()) {
            Log.i("Forecast", "" + t.getConditionCode() + " " + t.condition +
                    " " + t.getLow() + ":" + t.getHigh());
            setWeatherIcon(t.getConditionCode(), findViewById(getWeatherIconForDay(i)));
            i++;
        }

    }

    public void setLoading(boolean loading) {
        mProgress.setVisibility(loading ? VISIBLE : GONE);
    }
}