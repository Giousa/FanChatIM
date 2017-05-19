package com.zmm.fanchatim.ui.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.zmm.fanchatim.R;
import com.zmm.fanchatim.adapter.EMContactListenerAdapter;
import com.zmm.fanchatim.adapter.EMMessageListenerAdapter;
import com.zmm.fanchatim.factory.FragmentFactory;
import com.zmm.fanchatim.utils.LogUtils;
import com.zmm.fanchatim.utils.ThreadUtils;
import com.zmm.fanchatim.utils.ToastUtils;

import java.util.List;

import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @InjectView(R.id.bottomBar)
    BottomBar mBottomBar;

    private FragmentManager mFragmentManager;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();

        mFragmentManager = getSupportFragmentManager();
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, FragmentFactory.getInstance().getFragment(tabId)).commit();
            }
        });

        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
        EMClient.getInstance().addConnectionListener(mEMConnectionListener);
//        inviteContact();

    }

    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {

        //该回调在子线程中调用
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            updateUnreadCount();
        }
    };

    private void updateUnreadCount() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BottomBarTab bottomBar = mBottomBar.getTabWithId(R.id.conversations);
                int count = EMClient.getInstance().chatManager().getUnreadMsgsCount();
                bottomBar.setBadgeCount(count);
            }
        });
    }

    private EMConnectionListener mEMConnectionListener = new EMConnectionListener() {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(int i) {
            if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(LoginActivity.class);
                        ToastUtils.SimpleToast(getString(R.string.user_login_another_device));
                    }
                });
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadCount();
    }


    public void inviteContact() {
        EMClient.getInstance().contactManager().setContactListener(mEMContactListener);
    }

    private EMContactListenerAdapter mEMContactListener = new EMContactListenerAdapter() {

        @Override
        public void onContactAdded(String s) {

        }

        @Override
        public void onContactDeleted(String s) {

        }

        @Override
        public void onContactInvited(String s, String s1) {
        }

        @Override
        public void onContactAgreed(String s) {

        }

        @Override
        public void onContactRefused(String s) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(mEMConnectionListener);
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListenerAdapter);
    }
}
