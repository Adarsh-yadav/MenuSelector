package com.swiggy.swiggyapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by adarsh on 01/03/17.
 */

public class Variation {
    @SerializedName("name")
    public String name;
    @SerializedName("price")
    public Integer price;
    @SerializedName("default")
    public Integer _default;
    @SerializedName("id")
    public String id;
    @SerializedName("inStock")
    public Integer inStock;
    @SerializedName("isVeg")
    public Integer isVeg;
}
