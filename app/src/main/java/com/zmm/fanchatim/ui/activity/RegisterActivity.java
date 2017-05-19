package com.zmm.fanchatim.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.zmm.fanchatim.R;
import com.zmm.fanchatim.presenter.RegisterPresenter;
import com.zmm.fanchatim.presenter.impl.RegisterPresenterImpl;
import com.zmm.fanchatim.utils.ToastUtils;
import com.zmm.fanchatim.view.RegisterView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午3:46
 */

public class RegisterActivity extends BaseActivity implements RegisterView {
    @InjectView(R.id.user_name)
    EditText mUserName;
    @InjectView(R.id.password)
    EditText mPassword;
    @InjectView(R.id.confirm_password)
    EditText mConfirmPassword;

    private RegisterPresenter mRegisterPresenter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        super.init();
        mRegisterPresenter = new RegisterPresenterImpl(this);
        mConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    register();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick({R.id.register, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                register();
                break;
            case R.id.cancel:
                startActivity(LoginActivity.class);
                break;
        }
    }

    private void register() {
        hideKeyBoard();
        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        mRegisterPresenter.register(userName, password, confirmPassword);
    }

    @Override
    public void onStartRegister() {
        showProgress(getString(R.string.registering));
    }

    @Override
    public void onRegisterSuccess() {
        hideProgress();
        ToastUtils.SimpleToast(getString(R.string.register_success));
        startActivity(LoginActivity.class);
    }

    @Override
    public void onRegisterFailure() {
        hideProgress();
        ToastUtils.SimpleToast(getString(R.string.register_failed));
    }

    @Override
    public void onRegisterUserExist() {
        hideProgress();
        ToastUtils.SimpleToast(getString(R.string.register_failed_user_exist));
    }

    @Override
    public void onUserNameError() {
        mUserName.setError(getString(R.string.user_name_error));
    }

    @Override
    public void onPasswrodError() {
        mPassword.setError(getString(R.string.user_password_error));
    }

    @Override
    public void onConfirmPasswordError() {
        mConfirmPassword.setError(getString(R.string.user_password_confirm_error));
    }
}
