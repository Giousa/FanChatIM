package com.zmm.fanchatim.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.zmm.fanchatim.adapter.EMContactListenerAdapter;
import com.zmm.fanchatim.utils.LogUtils;

import butterknife.ButterKnife;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:上午11:16
 */

public abstract class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(getLayoutRes(), null);
        ButterKnife.inject(this, root);
        init();
        return root;
    }

    protected void init() {
        LogUtils.d("init: ");
    }


    protected abstract int getLayoutRes();

    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void startActivity(Class activity) {
        startActivity(activity, false);
    }

    protected void startActivity(Class activity, String key, String extra) {
        Intent intent = new Intent(getContext(), activity);
        intent.putExtra(key, extra);
        startActivity(intent);
    }

    protected void startActivity(Class activity, boolean finish) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
        if (finish) {
            getActivity().finish();
        }
    }

    /**
     *  Reset ProgressDialog when finish activity, since we will reuse the fragment created before,
     *  and the dialog refer to the finished activity if we not reset here.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mProgressDialog = null;
    }

}
