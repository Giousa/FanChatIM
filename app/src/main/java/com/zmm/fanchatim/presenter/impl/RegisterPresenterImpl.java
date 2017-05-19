package com.zmm.fanchatim.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zmm.fanchatim.model.Player;
import com.zmm.fanchatim.presenter.RegisterPresenter;
import com.zmm.fanchatim.utils.LogUtils;
import com.zmm.fanchatim.utils.StringUtils;
import com.zmm.fanchatim.utils.ThreadUtils;
import com.zmm.fanchatim.view.RegisterView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午3:57
 */

public class RegisterPresenterImpl implements RegisterPresenter{

    private RegisterView mRegisterView;


    public RegisterPresenterImpl(RegisterView registerView) {
        mRegisterView = registerView;
    }


    @Override
    public void register(String username, String password, String confirmPassword) {
        if(StringUtils.checkUserName(username)){
            if(StringUtils.checkPassword(password)){
                if(password.equals(confirmPassword)){
                    mRegisterView.onStartRegister();
                    registerBmob(username,password);
                }else {
                    mRegisterView.onConfirmPasswordError();
                }
            }else {
                mRegisterView.onPasswrodError();
            }
        }else {
            mRegisterView.onUserNameError();
        }
    }

    private void registerBmob(final String username, final String password) {
        Player player = new Player(username, password);
        player.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){//注册Bmob成功
                    registerEaseMob(username,password);
                }else {//注册Bmob失败
                    notifyRegisterFailed(e);
                }
            }
        });
    }

    private void notifyRegisterFailed(BmobException errorCode) {
        if(errorCode.getErrorCode() == 202){
            //用户名已存在
            mRegisterView.onRegisterUserExist();
        }else{
            //注册失败
            mRegisterView.onRegisterFailure();
        }
    }

    private void registerEaseMob(final String username, final String password) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username,password);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterSuccess();
                        }
                    });
                }catch (HyphenateException e){
                    e.printStackTrace();
                    if(e.getErrorCode() == 203){
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRegisterView.onRegisterUserExist();
                            }
                        });
                    }else {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRegisterView.onRegisterFailure();
                            }
                        });
                    }

                }
            }
        });
    }
}
