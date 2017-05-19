package com.zmm.fanchatim.model;

import cn.bmob.v3.BmobObject;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/5
 * Time:下午4:00
 */

public class Player extends BmobObject {

    private String username;
    private String password;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
