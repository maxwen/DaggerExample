package com.maxwen.daggerexample.data;

import com.maxwen.daggerexample.data.model.BuildImageList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BuildImageAPI {
    @GET("json2.php")
    Call<List<BuildImageList>> getBuildImageList();
}
