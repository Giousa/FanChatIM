package com.zmm.fanchatim.view;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午3:53
 */

public interface RegisterView {

    void onStartRegister();

    void onRegisterSuccess();

    void onRegisterFailure();

    void onRegisterUserExist();

    void onUserNameError();

    void onPasswrodError();

    void onConfirmPasswordError();
}
