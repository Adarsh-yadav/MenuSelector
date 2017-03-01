package com.swiggy.swiggyapp.rest;

import com.swiggy.swiggyapp.model.Group;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by adarsh on 01/03/17.
 */

public interface ApiInterface {
    @GET("bins/3b0u2")
    Call<Group> getGroups();
}
