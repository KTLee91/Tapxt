package com.springsthursday.tapxt.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.CategoryContract;
import com.springsthursday.tapxt.presenter.CategoryPresenter;
import com.springsthursday.tapxt.util.NetWorkBrodcastReceiver;

public class CategoryFragment extends Fragment implements CategoryContract.View {
    private CategoryPresenter viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (!NetWorkBrodcastReceiver.getInstance(getActivity().getApplicationContext()).isOnline())
            Toast.makeText(getActivity().getApplicationContext(), "네트워크 상태가 불안정합니다",Toast.LENGTH_LONG).show();

        ((AppCompatActivity)getActivity()).setSupportActionBar(getActivity().findViewById(R.id.toolbar));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewModel = new CategoryPresenter(this);

        return inflater.inflate(R.layout.fragment_category, container, false);
    }
}
