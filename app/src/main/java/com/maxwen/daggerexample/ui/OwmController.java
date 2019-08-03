package com.maxwen.daggerexample.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.maxwen.daggerexample.R;
import com.maxwen.daggerexample.data.NorwayWeatherProvider;
import com.maxwen.daggerexample.data.model.Weatherdata;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class OwmController extends BaseController {

    private boolean mLoadDone;
    private final BehaviorRelay<Weatherdata> mWeatherRelay = BehaviorRelay.create();
    private final BehaviorRelay<Throwable> mErrorRelay = BehaviorRelay.create();
    private final BehaviorRelay<Boolean> mLoadingRelay = BehaviorRelay.create();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        OwmView view = (OwmView) inflater.inflate(R.layout.owm_view, container, false);
        view.setController(this);
        mWeatherRelay.observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    mLoadingRelay.accept(false);
                    view.updateWeather(weather);
                });
        mErrorRelay.observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    mLoadingRelay.accept(false);
                    Log.e("maxwen", "", e);
                });
        mLoadingRelay.observeOn(AndroidSchedulers.mainThread())
                .subscribe(loading -> view.setLoading(loading));
        if (!mLoadDone) {
            updateWeather();
            mLoadDone = true;
        }
        return view;
    }

    public String getTitle() {
        return "Weather";
    }

    public NorwayWeatherProvider getWeatherProvider() {
        MainActivity activity = (MainActivity) getActivity();
        return activity.getApplicationComponent().getWeatherProvider();
    }

    @Override
    protected void onRestoreViewState(@androidx.annotation.NonNull View view, @androidx.annotation.NonNull Bundle savedViewState) {
        Log.d("maxwen", "onRestoreViewState");
    }

    @Override
    protected void onSaveInstanceState(@androidx.annotation.NonNull Bundle outState) {
        Log.d("maxwen", "onSaveInstanceState");
    }

    public void updateWeather() {
        mLoadingRelay.accept(true);
        getWeatherProvider().getWeather(mWeatherRelay, mErrorRelay);
    }
}
