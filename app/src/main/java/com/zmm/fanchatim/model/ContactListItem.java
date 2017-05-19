package com.zmm.fanchatim.model;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/9
 * Time:下午5:17
 */

public class ContactListItem {

    public String userName;

    public boolean showFirstLetter = true;

    public char getFirstLetter() {
        return userName.charAt(0);
    }

    public String getFirstLetterString() {
        return String.valueOf(getFirstLetter()).toUpperCase();
    }
}
