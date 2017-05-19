package com.zmm.fanchatim.presenter;

import com.zmm.fanchatim.model.ContactListItem;

import java.util.List;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/9
 * Time:下午5:17
 */

public interface ContactPresenter {

    void getContactsFromServer();

    List<ContactListItem> getContactList();

    void refreshContactList();

    void deleteFriend(String name);

}
