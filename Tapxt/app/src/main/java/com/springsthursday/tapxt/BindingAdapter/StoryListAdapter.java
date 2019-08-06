package com.springsthursday.tapxt.BindingAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.databinding.RecyclerviewCommentBinding;
import com.springsthursday.tapxt.databinding.RecyclerviewStorylistBinding;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.item.StoryItem;
import com.springsthursday.tapxt.listener.CommentClickListener;

import java.util.ArrayList;

public class StoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<StoryItem> items = new ArrayList<StoryItem>();

   /* public StoryListAdapter(CommentClickListener listener, Context context)
    {
        this.listener = listener;
        this.context = context;
    }*/

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_storylist, parent, false);

        return new StoryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StoryItem item = items.get(holder.getAdapterPosition());
        ((StoryListViewHolder) holder).binding.setStoryItem(item);
    }


    @Override
    public int getItemCount() {
        if (items != null) return items.size();
        else return 0;
    }

    public void setItems(ArrayList<StoryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class StoryListViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewStorylistBinding binding;

        public StoryListViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
