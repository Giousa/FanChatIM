package com.zmm.fanchatim.view;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午3:04
 */

public interface LoginView {

    void onUserNameError();

    void onPasswordError();

    void onStartLogin();

    void onLoginSuccess();

    void onLoginFailed();
}
