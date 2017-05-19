package com.zmm.fanchatim.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.zmm.fanchatim.presenter.SplashPresenter;
import com.zmm.fanchatim.view.SplashView;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:上午11:49
 */

public class SplashPresenterImpl implements SplashPresenter{

    public SplashView mSplashView;

    public SplashPresenterImpl(SplashView splashView) {
        mSplashView = splashView;
    }


    @Override
    public void checkLoginStatus() {
        if(EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()){
            mSplashView.onLoginSuccess();
        }else {
            mSplashView.onLoginFailure();
        }
    }
}
