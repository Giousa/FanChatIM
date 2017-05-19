package com.zmm.fanchatim.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zmm.fanchatim.db.DatabaseManager;
import com.zmm.fanchatim.model.AddFriendItem;
import com.zmm.fanchatim.model.Player;
import com.zmm.fanchatim.model.event.AddFriendEvent;
import com.zmm.fanchatim.presenter.AddFriendPresenter;
import com.zmm.fanchatim.utils.ThreadUtils;
import com.zmm.fanchatim.utils.ToastUtils;
import com.zmm.fanchatim.view.AddFriendView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/10
 * Time:上午10:44
 */

public class AddFriendPresenterImpl implements AddFriendPresenter {


    private AddFriendView mAddFriendView;
    private ArrayList<AddFriendItem> mAddFriendItems;

    public AddFriendPresenterImpl(AddFriendView addFriendView) {
        mAddFriendView = addFriendView;
        mAddFriendItems = new ArrayList<AddFriendItem>();
        EventBus.getDefault().register(this);
    }

    @Override
    public void searchFriend(final String keyword) {
        mAddFriendView.onStartSearch();
        //注:模糊查询只对付费用户开放，付费后可直接使用。
        BmobQuery<Player> query = new BmobQuery<>();
        query.addWhereContains("username", keyword).addWhereNotEqualTo("username", EMClient.getInstance().getCurrentUser());
        query.findObjects(new FindListener<Player>() {
            @Override
            public void done(List<Player> list, BmobException e) {
                processResult(list, e);
            }
        });
    }


    private void processResult(final List<Player> list, final BmobException e) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if (e == null && list.size() > 0) {
                    List<String> contacts = DatabaseManager.getInstance().queryAllContacts();
                    for (int i = 0; i < list.size(); i++) {
                        AddFriendItem item = new AddFriendItem();
                        item.timestamp = list.get(i).getCreatedAt();
                        item.userName = list.get(i).getUsername();
                        item.isAdded = contacts.contains(item.userName);
                        mAddFriendItems.add(item);
                    }
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAddFriendView.onSearchSuccess();
                        }
                    });
                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAddFriendView.onSearchFailure();
                        }
                    });
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void addFriend(AddFriendEvent event) {
        try {
            EMClient.getInstance().contactManager().addContact(event.getFriendName(), event.getReason());
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.SimpleToast("添加好友成功");
                    mAddFriendView.onAddFriendSuccess();
                }
            });
        } catch (HyphenateException e) {
            e.printStackTrace();
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.SimpleToast("添加好友失败");
                    mAddFriendView.onSearchFailure();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public List<AddFriendItem> getAddFriendList() {
        return mAddFriendItems;
    }
}
