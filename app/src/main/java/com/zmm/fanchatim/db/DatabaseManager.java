package com.zmm.fanchatim.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zmm.fanchatim.conf.CommonConfig;
import com.zmm.fanchatim.gen.ContactDao;
import com.zmm.fanchatim.gen.DaoMaster;
import com.zmm.fanchatim.gen.DaoSession;
import com.zmm.fanchatim.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/5/9
 * Time:下午5:35
 */

public class DatabaseManager {

    private static DatabaseManager sInstance;
    private DaoSession mDaoSession;


    public static DatabaseManager getInstance() {
        if (sInstance == null) {
            synchronized (DatabaseManager.class) {
                if (sInstance == null) {
                    sInstance = new DatabaseManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, CommonConfig.DATABASE_NAME, null);
        SQLiteDatabase writableDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        mDaoSession = daoMaster.newSession();
    }

    public void saveContact(String userName) {
        Contact contact = new Contact();
        contact.setUsername(userName);
        mDaoSession.getContactDao().save(contact);
    }

    public List<String> queryAllContacts() {
        List<Contact> list = mDaoSession.getContactDao().queryBuilder().list();
        ArrayList<String> contacts = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String contact = list.get(i).getUsername();
            contacts.add(contact);
        }
        LogUtils.d("contacts = "+contacts.toString());
        return contacts;
    }

    public void deleteAllContacts() {
        ContactDao contactDao = mDaoSession.getContactDao();
        contactDao.deleteAll();

    }
}
