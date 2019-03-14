package com.maxwen.daggerexample.ui;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxwen.daggerexample.R;
import com.maxwen.daggerexample.data.WeatherProvider;

public class OwmController extends BaseController {

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        OwmView view = (OwmView) inflater.inflate(R.layout.owm_view, container, false);
        view.setController(this);
        return view;
    }

    public String getTitle() {
        return "Weather";
    }

    public WeatherProvider getWeatherProvider() {
        MainActivity activity = (MainActivity) getActivity();
        return activity.getApplicationComponent().getWeatherProvider();
    }
}
