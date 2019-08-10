package com.springsthursday.tapxt.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.databinding.FragmentStorylistBinding;
import com.springsthursday.tapxt.decoration.GridItemDecoration;
import com.springsthursday.tapxt.item.StoryItem;
import com.springsthursday.tapxt.presenter.StoryListPresenter;

public class StoryListFragment extends Fragment {

    private StoryListPresenter viewModel;
    private int storyListType;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        storyListType = getArguments().getInt("StoryListType");
        return DataBindingUtil.inflate(inflater, R.layout.fragment_storylist, container, false).getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new StoryListPresenter(storyListType, getActivity().getApplicationContext(), new StoryClickListener());
        FragmentStorylistBinding binding = DataBindingUtil.getBinding(getView());
        binding.setViewModel(viewModel);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerView.addItemDecoration(new GridItemDecoration(50, 2));
        binding.recyclerView.setHasFixedSize(true);

        viewModel.bindingData();
    }

    class StoryClickListener implements com.springsthursday.tapxt.listener.StoryClickListener
    {
        @Override
        public void storyClick(StoryItem item) {
            Intent intent = new Intent(getActivity(), ContentCoverActivity.class);
            intent.putExtra("Story", item.getStoryid());
            getActivity().startActivity(intent);
        }
    }
}
