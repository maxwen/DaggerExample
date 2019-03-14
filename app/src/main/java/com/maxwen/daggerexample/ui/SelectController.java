package com.maxwen.daggerexample.ui;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.maxwen.daggerexample.R;

public class SelectController extends BaseController {

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        SelectView view = (SelectView) inflater.inflate(R.layout.select_view, container, false);
        view.setController(this);
        return view;
    }

    public String getTitle() {
        return "DaggerExample";
    }

    public void switchToBuildImages() {
        getRouter().pushController(RouterTransaction
                .with(new BuildImageController())
                .pushChangeHandler(new FadeChangeHandler())
                .popChangeHandler(new FadeChangeHandler()));
    }

    public void switchToOwm() {
        getRouter().pushController(RouterTransaction
                .with(new OwmController())
                .pushChangeHandler(new FadeChangeHandler())
                .popChangeHandler(new FadeChangeHandler()));
    }
}
