package com.zmm.fanchatim.factory;

import com.zmm.fanchatim.R;
import com.zmm.fanchatim.ui.fragment.BaseFragment;
import com.zmm.fanchatim.ui.fragment.ContactFragment;
import com.zmm.fanchatim.ui.fragment.ConversationFragment;
import com.zmm.fanchatim.ui.fragment.DynamicFragment;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午5:07
 */

public class FragmentFactory {

    private static FragmentFactory sFragmentFactory;

    private BaseFragment mMessageFragment;
    private BaseFragment mContactFragment;
    private BaseFragment mDynamicFragment;

    public static FragmentFactory getInstance() {
        if (sFragmentFactory == null) {
            synchronized (FragmentFactory.class) {
                if (sFragmentFactory == null) {
                    sFragmentFactory = new FragmentFactory();
                }
            }
        }
        return sFragmentFactory;
    }

    public BaseFragment getFragment(int id) {
        switch (id) {
            case R.id.conversations:
                return getConversationFragment();
            case R.id.contacts:
                return getContactFragment();
            case R.id.dynamic:
                return getDynamicFragment();
        }
        return null;
    }

    private BaseFragment getConversationFragment() {
        if (mMessageFragment == null) {
            mMessageFragment = new ConversationFragment();
        }
        return mMessageFragment;
    }

    private BaseFragment getDynamicFragment() {
        if (mDynamicFragment == null) {
            mDynamicFragment = new DynamicFragment();
        }
        return mDynamicFragment;
    }

    private BaseFragment getContactFragment() {
        if (mContactFragment == null) {
            mContactFragment = new ContactFragment();
        }
        return mContactFragment;
    }
}
