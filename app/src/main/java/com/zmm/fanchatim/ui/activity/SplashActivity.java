package com.zmm.fanchatim.ui.activity;

import com.zmm.fanchatim.R;
import com.zmm.fanchatim.presenter.SplashPresenter;
import com.zmm.fanchatim.presenter.impl.SplashPresenterImpl;
import com.zmm.fanchatim.utils.LogUtils;
import com.zmm.fanchatim.utils.UIUtils;
import com.zmm.fanchatim.view.SplashView;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:上午11:25
 */

public class SplashActivity extends BaseActivity implements SplashView {

    private SplashPresenter mSplashPresenter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        super.init();
        mSplashPresenter = new SplashPresenterImpl(this);
        mSplashPresenter.checkLoginStatus();
    }

    @Override
    public void onLoginSuccess() {
        LogUtils.d("success");
        UIUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MainActivity.class);
            }
        },2000);

    }

    @Override
    public void onLoginFailure() {
        LogUtils.d("failure");
        UIUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(LoginActivity.class);
            }
        },2000);
    }
}
