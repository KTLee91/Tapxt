package com.springsthursday.tapxt.BindingAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.airbnb.lottie.LottieAnimationView;
import com.springsthursday.tapxt.Code.Code;
import com.springsthursday.tapxt.listener.Listener;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.databinding.RecyclerviewContentcoverBinding;
import com.springsthursday.tapxt.databinding.RecyclerviewContentcoverHeaderBinding;
import com.springsthursday.tapxt.item.EpisodeItem;
import com.springsthursday.tapxt.item.StoryItem;

public class ContentCoverAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private StoryItem storyItem;
    private Listener.OnItemClickListener listener;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;

    public ContentCoverAdapter(Listener.OnItemClickListener listener, Context context)
    {
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType == TYPE_HEADER)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_contentcover_header, parent, false);
            return new ContentCoverAdapter.HeaderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_contentcover, parent, false);

            return new ContentCoverAdapter.ContentCoverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        StoryItem item = this.storyItem;

        if(holder instanceof ContentCoverAdapter.HeaderViewHolder)
        {
            if(item.isFollowed() == true)
            {
                ((HeaderViewHolder) holder).binding.follow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selectdheart,null));
            }

            ((ContentCoverAdapter.HeaderViewHolder) holder).binding.setStoryItem(item);

            ((HeaderViewHolder) holder).bindTextClickListener(((HeaderViewHolder) holder).binding.follow, item);
        }
        else
        {
            EpisodeItem episodeItem = item.getEpisodeList().get(holder.getAdapterPosition() -1);
            ((ContentCoverAdapter.ContentCoverViewHolder) holder).binding.setEpisodeItem(episodeItem);

            ((ContentCoverViewHolder) holder).bind(episodeItem, listener);
        }
    }

    @Override
    public int getItemCount()
    {
        if(storyItem != null) return storyItem.getEpisodeCount()+ 1;
        else return 0;
    }

    public void setItems(StoryItem item)
    {
        this.storyItem = item;
        notifyDataSetChanged();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerviewContentcoverHeaderBinding binding;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bindTextClickListener(final ToggleButton followToggle, StoryItem item)
        {
            binding.orderbyRecent.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    binding.orderbyRecent.setBackgroundResource(R.drawable.selectedshape);
                    binding.orderbyOneTime.setBackgroundResource(R.drawable.unselectedshape);

                    listener.onTextClick(Code.Orderby.ORDER_BY_RECENT);
                }
            });

            binding.orderbyOneTime.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    binding.orderbyOneTime.setBackgroundResource(R.drawable.selectedshape);
                    binding.orderbyRecent.setBackgroundResource(R.drawable.unselectedshape);


                    listener.onTextClick(Code.Orderby.ORDER_BY_FIRST);
                }
            });

            binding.follow.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onFollowClick(followToggle, item);
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    public class ContentCoverViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerviewContentcoverBinding binding;

        public ContentCoverViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(final EpisodeItem item, final Listener.OnItemClickListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
