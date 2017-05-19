package com.zmm.fanchatim.model;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

    public User(String userName, String password) {
        setUsername(userName);
        setPassword(password);
    }

}
