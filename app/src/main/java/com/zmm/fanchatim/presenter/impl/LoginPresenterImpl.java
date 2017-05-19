package com.zmm.fanchatim.presenter.impl;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zmm.fanchatim.presenter.LoginPresenter;
import com.zmm.fanchatim.utils.LogUtils;
import com.zmm.fanchatim.utils.StringUtils;
import com.zmm.fanchatim.utils.UIUtils;
import com.zmm.fanchatim.view.LoginView;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午3:07
 */

public class LoginPresenterImpl implements LoginPresenter{

    private LoginView mLoginView;

    public LoginPresenterImpl(LoginView loginView) {
        mLoginView = loginView;
    }


    @Override
    public void login(String username, String password) {
        if(StringUtils.checkUserName(username)){
            if(StringUtils.checkPassword(password)){
                mLoginView.onStartLogin();
                EMClient.getInstance().login(username, password, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        UIUtils.runInMainThread(new Runnable() {
                            @Override
                            public void run() {
                                LogUtils.d("登录成功");
                                mLoginView.onLoginSuccess();
                            }
                        });
                    }

                    @Override
                    public void onError(int code, String error) {
                        UIUtils.runInMainThread(new Runnable() {
                            @Override
                            public void run() {
                                LogUtils.d("登录失败");
                                mLoginView.onLoginFailed();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
            }else {
                mLoginView.onPasswordError();
            }

        }else {
            mLoginView.onUserNameError();
        }
    }
}
