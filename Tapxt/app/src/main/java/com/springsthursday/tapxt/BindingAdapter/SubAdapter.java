package com.springsthursday.tapxt.BindingAdapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.databinding.RecyclerviewSubBinding;
import com.springsthursday.tapxt.item.StoryItem;
import com.springsthursday.tapxt.listener.StoryClickListener;

import java.util.ArrayList;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubViewHolder> {

    private ArrayList<StoryItem> storyItems;
    private StoryClickListener listener;

    public SubAdapter(ArrayList<StoryItem> items, StoryClickListener listener)
    {
        this.listener = listener;
        storyItems = items;
    }

    @NonNull
    @Override
    public SubAdapter.SubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_sub, viewGroup, false);
        return new SubAdapter.SubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubAdapter.SubViewHolder viewHolder, int i) {
         StoryItem item = storyItems.get(viewHolder.getAdapterPosition());
         viewHolder.setClickListener(item);

         viewHolder.binding.setStoryItem(item);
    }

    @Override
    public int getItemCount() {
        if(storyItems == null) return 0;
        else return storyItems.size();
    }

    public class SubViewHolder extends RecyclerView.ViewHolder
    {
        protected RecyclerviewSubBinding binding;

        public SubViewHolder(View view)
        {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public void setClickListener(StoryItem item)
        {
            binding.getRoot().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.storyClick(item);
                }
            });
        }
    }
}
