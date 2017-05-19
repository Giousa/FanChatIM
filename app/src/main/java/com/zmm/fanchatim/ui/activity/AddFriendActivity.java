package com.zmm.fanchatim.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.zmm.fanchatim.R;
import com.zmm.fanchatim.adapter.AddFriendListAdapter;
import com.zmm.fanchatim.presenter.AddFriendPresenter;
import com.zmm.fanchatim.presenter.impl.AddFriendPresenterImpl;
import com.zmm.fanchatim.utils.ToastUtils;
import com.zmm.fanchatim.view.AddFriendView;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/10
 * Time:上午10:26
 */

public class AddFriendActivity extends BaseActivity implements AddFriendView {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.user_name)
    EditText mUserName;
    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.friend_not_found)
    TextView mFriendNotFound;

    private AddFriendPresenter mAddFriendPresenter;
    private AddFriendListAdapter mAddFriendListAdapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void init() {
        super.init();
        mAddFriendPresenter = new AddFriendPresenterImpl(this);
        mTitle.setText(getString(R.string.add_friend));
        mUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchFriend();
                    return true;
                }
                return false;
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAddFriendListAdapter = new AddFriendListAdapter(this, mAddFriendPresenter.getAddFriendList());
        mRecyclerView.setAdapter(mAddFriendListAdapter);
    }

    private void searchFriend() {
        hideKeyBoard();
        String keyword = mUserName.getText().toString().trim();
        mAddFriendPresenter.searchFriend(keyword);
    }

    @OnClick(R.id.search)
    public void onClick() {
        searchFriend();
    }

    @Override
    public void onStartSearch() {
        showProgress(getString(R.string.searching));
    }

    @Override
    public void onSearchSuccess() {
        hideProgress();
        mFriendNotFound.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAddFriendListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchFailure() {
        hideProgress();
        mFriendNotFound.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onAddFriendSuccess() {
        ToastUtils.SimpleToast(getString(R.string.add_friend_success));
    }

    @Override
    public void onAddFriendFailure() {
        ToastUtils.SimpleToast(getString(R.string.add_friend_failed));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddFriendPresenter.onDestroy();
    }
}
