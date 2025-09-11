package com.jason.demo;

/**
 * @author li jia
 * @date 2025/9/8 17:00
 * @description:
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jason.demo.appbar.MyRecyclerViewAdapter;

import java.util.List;

public class MyViewPager2Adapter extends RecyclerView.Adapter<MyViewPager2Adapter.ViewHolder> {

    private List<String> itemData;

    private String[] TabTitle;

    private Context context;

    public MyViewPager2Adapter(List<String> itemData, String[] tabTitle, Context context) {
        this.itemData = itemData;
        TabTitle = tabTitle;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager2_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recyclerView.setAdapter(new MyRecyclerViewAdapter(itemData));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return TabTitle.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
}