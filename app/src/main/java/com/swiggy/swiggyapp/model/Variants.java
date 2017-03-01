package com.swiggy.swiggyapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adarsh on 01/03/17.
 */

public class Variants {
    @SerializedName("variant_groups")
    public List<VariantGroup> variantGroups = null;
    @SerializedName("exclude_list")
    public List<List<ExcludeList>> excludeList = null;
}
