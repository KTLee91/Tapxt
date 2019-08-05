package com.springsthursday.tapxt.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.ProfileContract;
import com.springsthursday.tapxt.databinding.FragmentProfileBinding;
import com.springsthursday.tapxt.presenter.ProfilePresenter;

public class ProfileFragment extends Fragment implements ProfileContract.View {
    private ProfilePresenter viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        setHasOptionsMenu(true);
        return DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false).getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ProfilePresenter(this);
        FragmentProfileBinding binding = DataBindingUtil.getBinding(getView());
        binding.setViewModel(viewModel);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu_appbar_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setUserInfo(String nickName, String imageUrl)
    {
        viewModel.setUserInfo(nickName, imageUrl);
    }

    public Context getContext()
    {
        return getActivity().getApplicationContext();
    }

}