package com.swiggy.swiggyapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adarsh on 01/03/17.
 */

public class VariantGroup {
    @SerializedName("group_id")
    public String groupId;
    @SerializedName("name")
    public String name;
    @SerializedName("variations")
    public List<Variation> variations = null;
}
