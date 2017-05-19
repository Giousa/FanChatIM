package com.zmm.fanchatim.presenter;

import com.zmm.fanchatim.model.AddFriendItem;

import java.util.List;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/10
 * Time:上午10:37
 */

public interface AddFriendPresenter {

    void searchFriend(String keyword);

    void onDestroy();

    List<AddFriendItem> getAddFriendList();
}
