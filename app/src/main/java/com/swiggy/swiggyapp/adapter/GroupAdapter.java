package com.swiggy.swiggyapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swiggy.swiggyapp.ItemChecked;
import com.swiggy.swiggyapp.R;

import java.util.List;

/**
 * Created by adarsh on 01/03/17.
 */

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER = 0;
    public static final int CHILD = 1;
    private final List<Item> data;
    Context context;
    private final ItemChecked itemChecked;

    public GroupAdapter(List<Item> groups, Context context, ItemChecked itemChecked) {
        this.data = groups;
        this.itemChecked = itemChecked;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        GroupViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (5 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);

        switch (viewType) {
            case HEADER:
                TextView itemTextView = new TextView(context);
                itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
                itemTextView.setTextColor(0x88000000);
                itemTextView.setTextSize(18);
                itemTextView.setLayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                return new RecyclerView.ViewHolder(itemTextView) {
                };

            case CHILD:
                view = inflater.inflate(R.layout.item_layout_child, parent, false);
                viewHolder = new GroupViewHolder(view);
                return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GroupAdapter.Item item = data.get(position);
        final GroupViewHolder viewHolder;

        switch (item.type) {
            case HEADER:
                TextView itemTextView = (TextView) holder.itemView;
                itemTextView.setText(data.get(position).name);
                break;

            case CHILD:
                viewHolder = (GroupViewHolder) holder;
                viewHolder.textView1.setText(item.name);

                if(data.get(position).selected){
                    viewHolder.selected.setImageResource(R.drawable.ic_checked);
                }else{
                    viewHolder.selected.setImageResource(R.drawable.ic_unchecked);
                }

                viewHolder.childLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemChecked.onItemChecked(item.id, item.groupID);
                    }
                });

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class GroupViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView1;
                public final ImageView selected;
        final RelativeLayout childLayout;

        public GroupViewHolder(View itemView) {
            super(itemView);
            selected = (ImageView) itemView.findViewById(R.id.selected);
            textView1 = (TextView) itemView.findViewById(R.id.groupHeader);
            childLayout = (RelativeLayout)itemView.findViewById(R.id.childLayout);
        }
    }

    public static class Item {
        public int type;
        public String name;
        public String id;
        public String groupID;
        public boolean selected;

        public Item() {
        }

        public Item(int type, String id, String name, boolean selected, String groupID) {
            this.type = type;
            this.id = id;
            this.name = name;
            this.selected = selected;
            this.groupID = groupID;
        }
    }
}
