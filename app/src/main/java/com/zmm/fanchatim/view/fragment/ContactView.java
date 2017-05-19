package com.zmm.fanchatim.view.fragment;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午5:57
 */

public interface ContactView {

    void onGetContactListSuccess();

    void onGetContactListFailure();

    void onDeleteFriendSuccess();

    void onDeleteFriendFailure();
}
