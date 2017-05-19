package com.zmm.fanchatim.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zmm.fanchatim.db.DatabaseManager;
import com.zmm.fanchatim.model.ContactListItem;
import com.zmm.fanchatim.presenter.ContactPresenter;
import com.zmm.fanchatim.utils.ThreadUtils;
import com.zmm.fanchatim.view.fragment.ContactView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/9
 * Time:下午5:20
 */

public class ContactPresenterImpl implements ContactPresenter {

    private ContactView mContactView;
    private List<ContactListItem> mContactListItems;

    public ContactPresenterImpl(ContactView contactView) {
        mContactView = contactView;
        mContactListItems = new ArrayList<>();
    }

    @Override
    public List<ContactListItem> getContactList() {
        return mContactListItems;
    }

    @Override
    public void refreshContactList() {
        mContactListItems.clear();
        getContactsFromServer();
    }

    @Override
    public void deleteFriend(final String name) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(name);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDeleteFriendSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDeleteFriendFailure();
                        }
                    });
                }
            }
        });
    }


    @Override
    public void getContactsFromServer() {
        if(mContactListItems.size() > 0){
            mContactView.onGetContactListSuccess();
            return;
        }

        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取联系人列表,并保持到数据库
                    startGetContactList();

                    //获取联系人成功
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onGetContactListSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();

                    //获取联系人失败
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onGetContactListFailure();
                        }
                    });

                }
            }
        });
    }

    private void startGetContactList() throws HyphenateException{
        List<String> contacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
        DatabaseManager.getInstance().deleteAllContacts();
        if(!contacts.isEmpty()){
            for (int i = 0; i < contacts.size(); i++) {
                ContactListItem contactListItem = new ContactListItem();
                contactListItem.userName = contacts.get(i);
                if(itemInSameGroup(i,contactListItem)){
                    contactListItem.showFirstLetter = false;
                }

                mContactListItems.add(contactListItem);
                saveContactToDatabase(contactListItem.userName);
            }
        }
    }

    private boolean itemInSameGroup(int i, ContactListItem item) {
        return i > 0 && (item.getFirstLetter() == mContactListItems.get(i - 1).getFirstLetter());
    }

    private void saveContactToDatabase(String userName) {
        DatabaseManager.getInstance().saveContact(userName);
    }

}
