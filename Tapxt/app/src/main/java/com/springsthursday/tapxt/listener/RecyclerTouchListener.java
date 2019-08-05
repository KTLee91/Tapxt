package com.springsthursday.tapxt.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.presenter.ContentPresenter;
import com.springsthursday.tapxt.util.ContentUtil;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector gestureDetector;
    private RecyclerViewClickListener clickListener;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                View exactView = null;

                if (childView != null)
                    exactView = ContentUtil.findExactChild(childView, e.getX(), e.getY());

                if (exactView != null) {
                    if (exactView.getId() == R.id.btn) {
                        return false;
                    }
                }
                clickListener.onClick();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (clickListener != null) {
                    clickListener.onLongClick();
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        return this.gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}


