package com.maxwen.daggerexample.di;

import com.maxwen.daggerexample.data.BuildImageProvider;
import com.maxwen.daggerexample.data.NorwayWeatherProvider;
import com.maxwen.daggerexample.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject (MainActivity target);

    BuildImageProvider getBuildImageProvider();

    NorwayWeatherProvider getWeatherProvider();

}