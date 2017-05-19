package com.zmm.fanchatim.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.zmm.fanchatim.R;
import com.zmm.fanchatim.adapter.ContactListAdapter;
import com.zmm.fanchatim.adapter.EMContactListenerAdapter;
import com.zmm.fanchatim.conf.CommonConfig;
import com.zmm.fanchatim.model.ContactListItem;
import com.zmm.fanchatim.presenter.ContactPresenter;
import com.zmm.fanchatim.presenter.impl.ContactPresenterImpl;
import com.zmm.fanchatim.ui.activity.AddFriendActivity;
import com.zmm.fanchatim.ui.activity.ChatActivity;
import com.zmm.fanchatim.utils.LogUtils;
import com.zmm.fanchatim.utils.ToastUtils;
import com.zmm.fanchatim.view.fragment.ContactView;
import com.zmm.fanchatim.widget.SlideBar;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Description:通讯联系人
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午5:09
 */

public class ContactFragment extends BaseFragment implements ContactView {
    @InjectView(R.id.back)
    ImageView mBack;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.add)
    ImageView mAdd;
    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.slide_bar)
    SlideBar mSlideBar;
    @InjectView(R.id.section)
    TextView mSection;

    private static final int POSITION_NOT_FOUND = -1;
    private ContactPresenter mContactPresenter;
    private ContactListAdapter mContactListAdapter;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void init() {
        super.init();
        mContactPresenter = new ContactPresenterImpl(this);
        mTitle.setText("联系人");
        mAdd.setVisibility(View.VISIBLE);

        EMClient.getInstance().contactManager().setContactListener(mEMContactListener);

        initRecyclerView();

        initSwipeRefresh();

        mContactPresenter.getContactsFromServer();


    }

    private void initSwipeRefresh() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.qq_blue, R.color.qq_red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mContactPresenter.refreshContactList();
            }
        });
        mSlideBar.setOnSlidingBarChangeListener(new SlideBar.OnSlideBarChangeListener() {
            @Override
            public void onSectionChange(int index, String section) {
                mSection.setVisibility(View.VISIBLE);
                mSection.setText(section);
                scrollToSection(section);
            }

            @Override
            public void onSlidingFinish() {
                mSection.setVisibility(View.GONE);
            }
        });

    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mContactListAdapter = new ContactListAdapter(getContext(), mContactPresenter.getContactList());
        mContactListAdapter.setOnItemClickListener(new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name) {
                startActivity(ChatActivity.class, CommonConfig.USER_NAME, name);
            }

            @Override
            public void onItemLongClick(String name) {
                showDeleteFriendDialog(name);
            }
        });
        mRecyclerView.setAdapter(mContactListAdapter);
    }

    @OnClick(R.id.add)
    public void onClick() {
        startActivity(AddFriendActivity.class, false);
    }

    @Override
    public void onGetContactListSuccess() {
        mContactListAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onGetContactListFailure() {
        ToastUtils.SimpleToast(getString(R.string.get_contacts_error));
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDeleteFriendSuccess() {
        hideProgress();
        ToastUtils.SimpleToast(getString(R.string.delete_friend_success));
        mContactPresenter.refreshContactList();
    }

    @Override
    public void onDeleteFriendFailure() {
        hideProgress();
        ToastUtils.SimpleToast(getString(R.string.delete_friend_failed));
    }

    private void showDeleteFriendDialog(final String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String message = String.format(getString(R.string.delete_friend_message), name);
        builder.setTitle(getString(R.string.delete_friend))
                .setMessage(message)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showProgress(getString(R.string.deleting_friend));
                        mContactPresenter.deleteFriend(name);

                    }
                });
        builder.show();
    }


    /**
     * RecyclerView滚动直到界面出现对应section的联系人
     *
     * @param section 首字符
     */
    private void scrollToSection(String section) {
        int sectionPosition = getSectionPosition(section);
        if (sectionPosition != POSITION_NOT_FOUND) {
            mRecyclerView.smoothScrollToPosition(sectionPosition);
        }
    }

    /**
     *
     * @param section 首字符
     * @return 在联系人列表中首字符是section的第一个联系人在联系人列表中的位置
     */
    private int getSectionPosition(String section) {
        List<ContactListItem> contactListItems = mContactListAdapter.getContactListItems();
        for (int i = 0; i < contactListItems.size(); i++) {
            if (section.equals(contactListItems.get(i).getFirstLetterString())) {
                return i;
            }
        }
        return POSITION_NOT_FOUND;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().contactManager().removeContactListener(mEMContactListener);
    }

    private EMContactListenerAdapter mEMContactListener = new EMContactListenerAdapter() {

        @Override
        public void onContactAdded(String s) {
            mContactPresenter.refreshContactList();
        }

        @Override
        public void onContactDeleted(String s) {
            mContactPresenter.refreshContactList();
        }
    };
}
