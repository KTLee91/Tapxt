package com.springsthursday.tapxt.BindingAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.databinding.RecyclerviewMainBinding;
import com.springsthursday.tapxt.item.StoryItem;
import com.springsthursday.tapxt.listener.StoryClickListener;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private ArrayList<ArrayList<StoryItem>> AllStoryList;
    private StoryClickListener listener;
    private Context context;

    public MainAdapter(Context context, StoryClickListener listener)
    {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_main,null);

        return new MainAdapter.MainViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MainViewHolder viewHolder, int position) {
        viewHolder.binding.tag.setText(AllStoryList.get(position).get(0).getFeedTag());

        viewHolder.binding.recyclerview.setHasFixedSize(true);
        viewHolder.binding.recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewHolder.binding.recyclerview.setNestedScrollingEnabled (false);
        viewHolder.binding.recyclerview.setAdapter(new SubAdapter(AllStoryList.get(position), listener));
    }

    @Override
    public int getItemCount() {
        if(AllStoryList == null) return 0;
        else return AllStoryList.size();
    }

    public void setItems(ArrayList<ArrayList<StoryItem>> items)
    {
        AllStoryList = items;
        notifyDataSetChanged();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder{
        protected RecyclerviewMainBinding binding;

        public MainViewHolder(View view)
        {
            super(view);
            this.binding = DataBindingUtil.bind(view);
        }
    }
}
