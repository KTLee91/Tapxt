package com.springsthursday.tapxt.decoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridItemDecoration extends RecyclerView.ItemDecoration{

    private int gridSpacing;
    private int gridSize;
    private boolean needLeftSpacing = false;

    public GridItemDecoration(int gridSpacing, int gridSize)
    {
        this.gridSpacing = gridSpacing;
        this.gridSize = gridSize;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int frameWidth = ((parent.getWidth() - gridSpacing * (gridSize - 1)) / gridSize);
        int padding = parent.getWidth() / gridSize - frameWidth;
        int itemPosition = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewAdapterPosition();
        if (itemPosition < gridSize) {
            outRect.top = 0;
        } else {
            outRect.top = gridSpacing;
        }
        if (itemPosition % gridSize == 0) {
            outRect.left = 0;
            outRect.right = padding;
            needLeftSpacing = true;
        } else if ((itemPosition + 1) % gridSize == 0) {
            needLeftSpacing = false;
            outRect.right = 0;
            outRect.left = padding;
        } else if (needLeftSpacing) {
            needLeftSpacing = false;
            outRect.left = gridSpacing - padding;
            if ((itemPosition + 2) % gridSpacing == 0) {
                outRect.right = gridSpacing - padding;
            } else {
                outRect.right = gridSpacing / 2;
            }
        } else if ((itemPosition + 2) % gridSize == 0) {
            needLeftSpacing = false;
            outRect.left = gridSpacing / 2;
            outRect.right = gridSpacing - padding;
        } else {
            needLeftSpacing = false;
            outRect.left = gridSpacing / 2;
            outRect.right = gridSpacing / 2;
        }
        outRect.bottom = 0;
    }
}
