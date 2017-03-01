package com.swiggy.swiggyapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.swiggy.swiggyapp.ItemChecked;
import com.swiggy.swiggyapp.R;
import com.swiggy.swiggyapp.adapter.GroupAdapter;
import com.swiggy.swiggyapp.model.ExcludeList;
import com.swiggy.swiggyapp.model.Group;
import com.swiggy.swiggyapp.model.VariantGroup;
import com.swiggy.swiggyapp.model.Variation;
import com.swiggy.swiggyapp.rest.ApiClient;
import com.swiggy.swiggyapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ItemChecked {

    private List<GroupAdapter.Item> data;
    private HashMap<String, GroupAdapter.Item> selectedItems;
    private List<List<ExcludeList>> excludeLists;
    private RecyclerView recycler;
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Group> group = apiInterface.getGroups();

        group.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                List<VariantGroup> variantGroups = response.body().variants.variantGroups;
                excludeLists = response.body().variants.excludeList;
                parseResponse(variantGroups);
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {

            }
        });

    }

    private void parseResponse(List<VariantGroup> variantGroups) {
        data = new ArrayList<>();
        selectedItems = new HashMap<>();
        for (VariantGroup varGroup : variantGroups) {
            GroupAdapter.Item item = new GroupAdapter.Item(GroupAdapter.HEADER, varGroup.groupId, varGroup.name, false, varGroup.groupId);
            data.add(item);
            List<Variation> variations = varGroup.variations;
            for (Variation variation : variations) {
                GroupAdapter.Item itemChild = new GroupAdapter.Item(GroupAdapter.CHILD, variation.id, variation.name, false, varGroup.groupId);
                data.add(itemChild);
            }
        }
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupAdapter(data, this, this);
        recycler.setAdapter(new GroupAdapter(data, this, this));
    }


    @Override
    public void onItemChecked(String id, String groupID) {
        boolean doesExist = checkIfItemInExcludeList(id, groupID);
        if(doesExist){
            //Item exists in exclude list, ignore
            Toast.makeText(this, "We don't have this combination, please try something else.", Toast.LENGTH_LONG).show();
        }else{
            //Item not in exclude list
            refreshData(id, groupID);
        }
    }

    private void refreshData(String id, String groupID) {
        List<GroupAdapter.Item> newData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            GroupAdapter.Item item = data.get(i);
            if (item.id.equalsIgnoreCase(id) || item.groupID.equalsIgnoreCase(groupID)) {
                if (item.id.equalsIgnoreCase(id)) {
                    GroupAdapter.Item newItem = new GroupAdapter.Item(data.get(i).type, data.get(i).id, data.get(i).name, true, data.get(i).groupID);
                    newData.add(newItem);
                    //Add this item to selected list
                    if(!(selectedItems.containsKey(data.get(i).id))){
                        selectedItems.put(data.get(i).id, newItem);
                    }
                } else {
                    GroupAdapter.Item newItem = new GroupAdapter.Item(data.get(i).type, data.get(i).id, data.get(i).name, false, data.get(i).groupID);
                    newData.add(newItem);
                    if(selectedItems.containsKey(data.get(i).id)){
                        selectedItems.remove(data.get(i).id);
                    }
                }
            } else {
                newData.add(data.get(i));
            }
        }
        data = newData;
        recycler.setAdapter(new GroupAdapter(newData, this, this));
    }

    private boolean checkIfItemInExcludeList(String id, String groupID) {
        //Check the number of selected items
        int count = 0;
        //Check if the group id matches
        for (List<ExcludeList> excludeList : excludeLists) {
            for(ExcludeList excludeList1: excludeList){
                if(excludeList1.variationId.equalsIgnoreCase(id) && excludeList1.groupId.equalsIgnoreCase(groupID)){
                    //Checking if the selected values are in the same exclude list
                    for(ExcludeList excludeList2: excludeList){
                        String excludeVariationID = excludeList2.variationId;
                        String excludeGroupID = excludeList2.groupId;
                        if(selectedItems.containsKey(excludeVariationID)){
                            GroupAdapter.Item checkItem = selectedItems.get(excludeVariationID);
                            if(checkItem.groupID.equalsIgnoreCase(excludeGroupID)){
                                count++;
                            }
                        }
                    }
                }
            }
        }
        return count > 0;
    }
}
