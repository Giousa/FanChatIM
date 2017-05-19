package com.zmm.fanchatim.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.zmm.fanchatim.R;
import com.zmm.fanchatim.presenter.DynamicPresenter;
import com.zmm.fanchatim.presenter.impl.DynamicPresenterImpl;
import com.zmm.fanchatim.ui.activity.LoginActivity;
import com.zmm.fanchatim.utils.ToastUtils;
import com.zmm.fanchatim.view.fragment.DynamicView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Description:退出
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午5:09
 */

public class DynamicFragment extends BaseFragment implements DynamicView {

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.add)
    ImageView mAdd;
    @InjectView(R.id.logout)
    Button mLogout;

    private DynamicPresenter mDynamicPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void init() {
        super.init();
        mDynamicPresenter = new DynamicPresenterImpl(this);
        String logout = String.format(getString(R.string.logout), EMClient.getInstance().getCurrentUser());
        mLogout.setText(logout);
        mTitle.setText(getString(R.string.dynamic));
    }


    @OnClick({R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                mDynamicPresenter.logout();
                break;
        }
    }

    @Override
    public void onStartLogout() {
        showProgress(getString(R.string.logouting));
    }

    @Override
    public void onLogoutSuccess() {
        hideProgress();
        ToastUtils.SimpleToast(getString(R.string.logout_success));
        startActivity(LoginActivity.class, true);
    }

    @Override
    public void onLogoutFailure() {
        hideProgress();
        ToastUtils.SimpleToast(getString(R.string.logout_failed));
    }
}
