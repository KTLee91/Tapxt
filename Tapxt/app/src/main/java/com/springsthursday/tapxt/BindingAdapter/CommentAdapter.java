package com.springsthursday.tapxt.BindingAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.databinding.RecyclerviewCommentBinding;
import com.springsthursday.tapxt.item.CommentItem;
import com.springsthursday.tapxt.listener.CommentClickListener;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<CommentItem> items;
    private CommentClickListener listener;
    private Context context;

    public CommentAdapter(CommentClickListener listener, Context context)
    {
        this.listener = listener;
        this.context = context;
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
        else
        {

            Drawable drawable =  context.getResources().getDrawable(R.drawable.rectangle_white, null);
            drawable.setColorFilter(context.getResources().getColor(R.color.defaulitViolet, null), PorterDuff.Mode.SRC_IN);

            ((CommentViewHolder) holder).binding.comment.setBackground(drawable);
        }

        if(item.isClapsMe())
        {
            ((CommentViewHolder) holder).binding.clapsCount.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.left_like_red, null),null,null,null);
            //Red로 변경
        }

        ((CommentViewHolder) holder).bindClickListener(item, ((CommentViewHolder) holder).binding.clapsCount);

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
        notifyItemInserted(items.size()-1);
    }

    public void removeItem(CommentItem item)
    {
        int position = this.items.indexOf(item);
        this.items.remove(item);
        notifyItemRemoved(position);
    }

    public void updateComment(String comment, String commentID)
    {
        for(int i=0; i< items.size(); i++)
        {
            CommentItem item = items.get(i);
            if(item.getId().equals(commentID)) {
                item.setText(comment);
                return;
            }
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewCommentBinding binding;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bindClickListener(final CommentItem item, final TextView claps)
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

            binding.clapsCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClapsClickListener(item, claps);
                }
            });
        }
    }
}
