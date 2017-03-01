package com.swiggy.swiggyapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by adarsh on 01/03/17.
 */

public class ExcludeList {
    @SerializedName("group_id")
    public String groupId;
    @SerializedName("variation_id")
    public String variationId;
}
