package com.springsthursday.tapxt.BindingAdapter;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.springsthursday.tapxt.Code.Code;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.databinding.RecyclerviewCenterImageBinding;
import com.springsthursday.tapxt.databinding.RecyclerviewContentBinding;
import com.springsthursday.tapxt.databinding.RecyclerviewContentNarrationBinding;
import com.springsthursday.tapxt.databinding.RecyclerviewContentRightBinding;
import com.springsthursday.tapxt.databinding.RecyclerviewLeftImageBinding;
import com.springsthursday.tapxt.databinding.RecyclerviewRightImageBinding;
import com.springsthursday.tapxt.item.ContentItem;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<ContentItem> contentItems;

    //region onCreatrViewHolder Method
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType == Code.ContentType.LEFT_FIRST_CONTENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_content, parent, false);
            return new ContentViewHolder(view);
        }
        else if(viewType == Code.ContentType.RIGHT_FIRST_CONTENT)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_content_right, parent, false);
            return new RightFirstViewHolder(view);
        }
        else if(viewType == Code.ContentType.LEFT_SERIES_CONTENT)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_content, parent, false);
            return new ContentViewHolder(view);
        }
        else if(viewType == Code.ContentType.RIGHT_SERIES_CONTENT)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_content_right, parent, false);
            return new RightFirstViewHolder(view);
        }
        else if(viewType == Code.ContentType.NARRATION_CONTENT)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_content_narration, parent, false);
            return new NarrationViewHolder(view);
        }
        else if(viewType == Code.ContentType.LEFT_IMAGE)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_left_image, parent, false);
            return new LeftImageViewHolder(view);
        }
        else if(viewType == Code.ContentType.LEFT_SERIES_IMAGE)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_left_image, parent, false);
            return new LeftImageViewHolder(view);
        }
        else if(viewType == Code.ContentType.RIGHT_IMAGE)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_right_image, parent, false);
            return new RightImageViewHolder(view);
        }
        else if(viewType == Code.ContentType.RIGHT_SERIES_IMAGE)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_right_image, parent, false);
            return new RightImageViewHolder(view);
        }

        else if(viewType == Code.ContentType.CENTER_IMAGE)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_center_image, parent, false);
            return new CenterImageViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_dummy, parent,false);
            return new DummyViewHolder(view);
        }
    }
    //endregion

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ContentItem item = contentItems.get(holder.getAdapterPosition());

        if(holder instanceof ContentViewHolder) {
            if(item.getContentType() == Code.ContentType.LEFT_SERIES_CONTENT)
            {
                ((ContentViewHolder) holder).binding.imageView3.setVisibility(View.GONE);
                ((ContentViewHolder) holder).binding.name.setVisibility(View.GONE);
            }

            ((ContentViewHolder) holder).binding.content.setTextColor(Color.parseColor(item.getTextColor()));
            this.setTextViewStyle(((ContentViewHolder) holder).binding.content, item);

            GradientDrawable bgShape = (GradientDrawable) ((ContentViewHolder) holder).binding.content.getBackground();
            bgShape.setColor(Color.parseColor(item.getBoxColor()));

            ((ContentViewHolder) holder).binding.setContentItem(item);
        }
        else if(holder instanceof RightFirstViewHolder) {
            if(item.getContentType() == Code.ContentType.RIGHT_SERIES_CONTENT)
            {
                ((RightFirstViewHolder) holder).binding.imageView3.setVisibility(View.GONE);
                ((RightFirstViewHolder) holder).binding.name.setVisibility(View.GONE);
            }

            ((RightFirstViewHolder) holder).binding.content.setTextColor(Color.parseColor(item.getTextColor()));
            this.setTextViewStyle(((RightFirstViewHolder) holder).binding.content, item);

            GradientDrawable bgShape = (GradientDrawable) ((RightFirstViewHolder) holder).binding.content.getBackground();
            bgShape.setColor(Color.parseColor(item.getBoxColor()));

            ((RightFirstViewHolder) holder).binding.setContentItem(item);
        }
        else if(holder instanceof NarrationViewHolder) {
            ((NarrationViewHolder) holder).binding.setContentItem(item);

            ((NarrationViewHolder) holder).binding.content.setTextColor(Color.parseColor(item.getTextColor()));
            this.setTextViewStyle(((NarrationViewHolder) holder).binding.content, item);
        }
        else if(holder instanceof LeftImageViewHolder)
        {
            if(item.getContentType() == Code.ContentType.LEFT_SERIES_IMAGE)
            {
                ((LeftImageViewHolder) holder).binding.imageView3.setVisibility(View.GONE);
                ((LeftImageViewHolder) holder).binding.name.setVisibility(View.GONE);
            }

            GradientDrawable bgShape = (GradientDrawable) ((LeftImageViewHolder) holder).binding.content.getBackground();
            bgShape.setColor(Color.parseColor(item.getBoxColor()));

            ((LeftImageViewHolder) holder).binding.setContentItem(item);
        }
        else if(holder instanceof RightImageViewHolder)
        {
            if(item.getContentType() == Code.ContentType.RIGHT_SERIES_IMAGE)
            {
                ((RightImageViewHolder) holder).binding.imageView3.setVisibility(View.GONE);
                ((RightImageViewHolder) holder).binding.name.setVisibility(View.GONE);
            }

            GradientDrawable bgShape = (GradientDrawable) ((RightImageViewHolder) holder).binding.content.getBackground();
            bgShape.setColor(Color.parseColor(item.getBoxColor()));

            ((RightImageViewHolder) holder).binding.setContentItem(item);
        }
        else if(holder instanceof CenterImageViewHolder)
        {
            ((CenterImageViewHolder) holder).binding.setContentItem(item);
        }
        else
            return;
    }

    @Override
    public int getItemCount()
    {
        if(contentItems != null) return contentItems.size();
        else return 0;
    }

    @Override
    public long getItemId(int position)
    {
        return contentItems.get(position).getContentSequence();
    }

    public void setItems(ArrayList<ContentItem> items)
    {
        this.contentItems = items;

        if(contentItems.size() > 0)
            notifyItemInserted(contentItems.size() - 1);
        else
            notifyDataSetChanged();
    }

    public ArrayList<ContentItem> getItems()
    {
        return contentItems;
    }

    @Override
    public int getItemViewType(int position)
    {
        ContentItem item = contentItems.get(position);
        int viewType = item.getContentType();
        return viewType;
    }

    //region ViewHolder Class
    public class ContentViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerviewContentBinding binding;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public class RightFirstViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerviewContentRightBinding binding;

        public RightFirstViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public class NarrationViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerviewContentNarrationBinding binding;

        public NarrationViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public class DummyViewHolder extends RecyclerView.ViewHolder
    {
        private View view;

        public DummyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            view = itemView.findViewById(R.id.dummyView);
        }
    }
    public class LeftImageViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerviewLeftImageBinding binding;

        public LeftImageViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public class RightImageViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerviewRightImageBinding binding;

        public RightImageViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public class CenterImageViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerviewCenterImageBinding binding;

        public CenterImageViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
    //endregion

    public void setTextViewStyle(TextView textView, ContentItem item)
    {
        if(item.isBold() == true && item.isItalic() == true)
        {
            textView.setTypeface(null, Typeface.BOLD_ITALIC);
        }
        else if(item.isBold())
        {
            textView.setTypeface(null, Typeface.BOLD);
        }
        else if(item.isItalic())
        {
            textView.setTypeface(null, Typeface.ITALIC);
        }
    }
}
