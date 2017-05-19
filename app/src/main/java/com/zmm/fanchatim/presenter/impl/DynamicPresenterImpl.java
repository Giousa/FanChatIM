package com.zmm.fanchatim.presenter.impl;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zmm.fanchatim.presenter.DynamicPresenter;
import com.zmm.fanchatim.utils.ThreadUtils;
import com.zmm.fanchatim.view.fragment.DynamicView;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午5:19
 */

public class DynamicPresenterImpl implements DynamicPresenter {

    private DynamicView mDynamicView;

    public DynamicPresenterImpl(DynamicView dynamicView) {
        mDynamicView = dynamicView;
    }

    @Override
    public void logout() {
        mDynamicView.onStartLogout();

        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDynamicView.onLogoutSuccess();
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDynamicView.onLogoutFailure();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
}
