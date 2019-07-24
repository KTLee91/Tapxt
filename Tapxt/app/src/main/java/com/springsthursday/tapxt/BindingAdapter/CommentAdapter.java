package com.springsthursday.tapxt.BindingAdapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.databinding.RecyclerviewCommentBinding;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.listener.CommentClickListener;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<CommentItem> items;
    private CommentClickListener listener;

    public CommentAdapter(CommentClickListener listener)
    {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommentItem item = items.get(holder.getAdapterPosition());

        if(!item.isMe())
        {
            ((CommentViewHolder) holder).binding.update.setVisibility(View.GONE);
            ((CommentViewHolder) holder).binding.delete.setVisibility(View.GONE);
        }

        if(item.isClapsMe())
            ((CommentViewHolder) holder).binding.claps.setAnimation("like_red.json");

        ((CommentViewHolder) holder).bindClickListener(item, ((CommentViewHolder) holder).binding.claps);

        ((CommentViewHolder) holder).binding.setCommentItem(item);

    }

    @Override
    public int getItemCount() {
        if (items != null) return items.size();
        else return 0;
    }

    public void setItems(ArrayList<CommentItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(CommentItem item)
    {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(CommentItem item)
    {
        this.items.remove(item);
        notifyDataSetChanged();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewCommentBinding binding;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bindClickListener(final CommentItem item, final LottieAnimationView lottie)
        {
            binding.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUpdateTextClickListener(item);
                }
            });

            binding.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteTextClickListener(item);
                }
            });

            binding.claps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLottieClickListener(item, lottie);
                }
            });
        }
    }
}
